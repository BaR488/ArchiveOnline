/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ArchiverClasses;

//import DispatcherServices.RegisterServerService;
import ArchiverServices.RunningArchiver;
import DispatcherServices.RegisterServerService;
import static Utils.ConsoleLogger.logMessage;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.eclipse.jetty.server.Server;
import org.json.simple.parser.ParseException;

/**
 *
 * @author minel
 * @param <T> the type of ArchiveThread
 */
public class Archiver<T extends ArchiverThread> implements ArchiverOnline {

    public static String INPUTFILE_PATH = "InputFiles\\";
    public static String OUTPUTFILE_PATH = "OutputFiles\\";

    /**
     * @return the jettyServer
     */
    public Server getJettyServer() {
        return jettyServer;
    }

    //Класс - статус сервера - колво файлов в очереди, колво файлов в процессе
    public static class ArchiverStatus {

        private int filesInProgress;

        public ArchiverStatus(int filesInQueue, int filesInProgress) {
            this.filesInProgress = filesInQueue + filesInProgress;
        }

        /**
         * @return the filesInProgress
         */
        public int getFilesInProgress() {
            return filesInProgress;
        }

        /**
         * @param filesInProgress the filesInProgress to set
         */
        public void setFilesInProgress(int filesInProgress) {
            this.filesInProgress = filesInProgress;
        }
    }

    //Тип сервера сжиматель, расжматель
    public enum ServerType {
        COMPRESSOR, DEPRESSOR
    }

    private final Class<T> typeArgumentClass; //Тип потока архиватора
    private final Integer port; //Порт на котором запущен архиватор
    private final Integer type; //тип сжатие/расжатие
    private final String format; //Формат сервера
    private final Integer threadCount; //Количество одновременно работающих потоков
    private final Integer queueSize; //Размер очереди
    private final Server jettyServer; //Сервер который обслуживает данный архиватор

    private int serverId = -1; //Id сервера, который выдается дистпечером
    private Integer runningThreads; //Количество выполняющихся потоков
    private final ExecutorService threadPool; //Пул потоков
    private final ExecutorCompletionService<FileEntity> pool; //Обертка пула потоков
    private final ArrayList<FileEntity> filesInQueue; //Файлы в очереди

    /**
     * @return the format
     */
    public String getFormat() {
        return format;
    }

    /**
     * @return the threadCount
     */
    public Integer getThreadCount() {
        return threadCount;
    }

    /**
     * @return the queueSize
     */
    public Integer getQueueSize() {
        return queueSize;
    }

    /**
     * @return the type
     */
    public Integer getType() {
        return type;
    }

    /**
     * @return the port
     */
    public Integer getPort() {
        return port;
    }

    //Возвращает статус архиватора
    public ArchiverStatus getStatus() {
        return new ArchiverStatus(filesInQueue.size(), runningThreads);
    }

    public boolean isRegistred() {
        return serverId > 0;
    }

    public Archiver(Server jettyServer, Class<T> typeArgumentClass, Integer port, String format, Integer threadCount, Integer queueSize, ServerType type) {
        this.jettyServer = jettyServer;
        this.typeArgumentClass = typeArgumentClass;
        this.format = format;
        this.threadCount = threadCount;
        this.queueSize = queueSize;
        this.runningThreads = 0;
        this.filesInQueue = new ArrayList<>();
        this.type = type.ordinal();
        this.port = port;
        this.threadPool = Executors.newFixedThreadPool(threadCount);
        this.pool = new ExecutorCompletionService<>(threadPool);
        RunningArchiver.archiver = this;
    }

    //Регистрация архиватора
    @Override
    public void register() {
        try {
            RegisterServerService registerService = new RegisterServerService();
            serverId = registerService.register(port, type, format, threadCount, queueSize);
            if (serverId >= 0) {
                logMessage("Server was successfully at main server.");
            } else {
                logMessage("Server was not registred at main server.");
            }
        } catch (ParseException ex) {
            Logger.getLogger(Archiver.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    //Добавляет очередной файл на сжатие
    @Override
    public void addFile(FileEntity fileName) {

        //Проверяем если очередь не пуста, или выполняется весь пул занят
        if (!filesInQueue.isEmpty() || runningThreads == threadCount) {

            //Добавляем файл в очередь
            filesInQueue.add(fileName);

        } else {
            try {

                //Создаем новое задание на сжатие файла и запускаем его
                pool.submit(createArchiverThread(fileName));

                //Увеличиваем количество запущенных потоков
                runningThreads++;

            } catch (InstantiationException | IllegalAccessException ex) {
                Logger.getLogger(Archiver.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

    }

    //Начинает архивацию
    public void start() throws InterruptedException {

        while (true) {
            try {
                //Получаем результат выполнения потока
                FileEntity result = pool.take().get();

                //Если есть файлы в очереди, запускаем первый в очереди, иначе уменьшаем количество потоков
                if (!filesInQueue.isEmpty()) {

                    //Берем следующий файл из очереди
                    FileEntity nextFile = filesInQueue.remove(0);

                    //Запускаем задание архивации
                    pool.submit(createArchiverThread(nextFile));

                } else {
                    runningThreads--;
                }
                Utils.MailSender.send(result.getEmail(), "http://localhost:1234/archiver/downloadFile?fileName=" + result.getFileNameOutput());
            } catch (ExecutionException | InstantiationException | IllegalAccessException ex) {
                Logger.getLogger(Archiver.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    //Создаем задание для архивации
    T createArchiverThread(FileEntity fileName) throws InstantiationException, IllegalAccessException {
        T thread = typeArgumentClass.newInstance();
        thread.setFile(fileName);
        return thread;
    }

}
