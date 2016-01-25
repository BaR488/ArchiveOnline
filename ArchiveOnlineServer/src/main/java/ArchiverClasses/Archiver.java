/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ArchiverClasses;

//import DispatcherServices.RegisterServerService;
import ArchiverServer.StartArchiverThread;
import ArchiverServer.jFrameMain;
import ArchiverServices.RunningArchiver;
import DispatcherServices.RegisterServerService;
import static Utils.ConsoleLogger.logFileAddedInProgress;
import static Utils.ConsoleLogger.logFileAddedInQueue;
import static Utils.ConsoleLogger.logFileArchivateCompleted;
import static Utils.ConsoleLogger.logFileSended;
import static Utils.ConsoleLogger.logMessage;
import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Properties;
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
    public class ArchiverStatus {

        private long filesSizeAtAll;
        private int filesInQueueNow;
        private int filesInProgressNow;

        public ArchiverStatus(long filesSizeInQueue, long filesSizeInProgress) {
            this.filesSizeAtAll = filesSizeInQueue + filesSizeInProgress;
            this.filesInQueueNow = getFilesInQueue().size();
            this.filesInProgressNow = filesInProgress.size();
        }

        /**
         * @return the filesSizeAtAll
         */
        public long getFilesSizeAtAll() {
            return filesSizeAtAll;
        }

        /**
         * @param filesSizeAtAll the filesSizeAtAll to set
         */
        public void setFilesSizeAtAll(long filesSizeAtAll) {
            this.filesSizeAtAll = filesSizeAtAll;
        }

        /**
         * @return the filesInQueueNow
         */
        public int getFilesInQueueNow() {
            return filesInQueueNow;
        }

        /**
         * @param filesInQueueNow the filesInQueueNow to set
         */
        public void setFilesInQueueNow(int filesInQueueNow) {
            this.filesInQueueNow = filesInQueueNow;
        }

        /**
         * @return the filesInProgressNow
         */
        public int getFilesInProgressNow() {
            return filesInProgressNow;
        }

        /**
         * @param filesInProgressNow the filesInProgressNow to set
         */
        public void setFilesInProgressNow(int filesInProgressNow) {
            this.filesInProgressNow = filesInProgressNow;
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
    private final ArrayList<FileEntity> filesInProgress; //Файды в обработке

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

    /**
     * @return the filesInQueue
     */
    public ArrayList<FileEntity> getFilesInQueue() {
        return filesInQueue;
    }

    /**
     * @return the runningThreads
     */
    public Integer getRunningThreads() {
        return runningThreads;
    }

    //Возвращает статус архиватора
    public ArchiverStatus getStatus() {

        long filesSizeInQueue = 0;
        for (FileEntity fileEntity : this.getFilesInQueue()) {
            File file = new File(fileEntity.getFileNameInput());
            if (file.exists()) {
                filesSizeInQueue += file.length() / 1024;
            }
        }

        long filesSizeInProgress = 0;
        for (FileEntity fileEntity : this.filesInProgress) {
            File file = new File(fileEntity.getFileNameInput());
            if (file.exists()) {
                filesSizeInQueue += file.length() / 1024;
            }
        }

        return new ArchiverStatus(filesSizeInQueue, filesSizeInProgress);
    }

    //Проверяет зарегистрирован ли сервер
    public boolean isRegistred() {
        return serverId >= 0;
    }

    public Archiver(Server jettyServer, Class<T> typeArgumentClass, Integer port, String format, Integer threadCount, Integer queueSize, ServerType type) {
        this.jettyServer = jettyServer;
        this.typeArgumentClass = typeArgumentClass;
        this.format = format;
        this.threadCount = threadCount;
        this.queueSize = queueSize;
        this.runningThreads = 0;
        this.filesInQueue = new ArrayList<>();
        this.filesInProgress = new ArrayList<>();
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

    //Отмена регистрации архиватора
    public void unRegister() {
        if (isRegistred()) {
            RegisterServerService registerService = new RegisterServerService();
            registerService.unRegister(serverId);
            logMessage("Server was succesfully unregistred from main server.");
            serverId = -1;
        }
    }

    //Добавляет очередной файл на сжатие
    @Override
    public void addFile(FileEntity fileEntity) {

        //Проверяем если очередь не пуста, или выполняется весь пул занят
        if (!filesInQueue.isEmpty() || getRunningThreads().equals(threadCount)) {

            //Добавляем файл в очередь
            getFilesInQueue().add(fileEntity);

            logFileAddedInQueue(fileEntity);

        } else {
            try {

                //Создаем новое задание на сжатие файла и запускаем его
                pool.submit(createArchiverThread(fileEntity));

                filesInProgress.add(fileEntity);

                //Увеличиваем количество запущенных потоков
                runningThreads++;

                logFileAddedInProgress(fileEntity);

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

                //Удаляем из списка файлов в обработке
                filesInProgress.remove(result);

                logFileArchivateCompleted(result);

                //Если есть файлы в очереди, запускаем первый в очереди, иначе уменьшаем количество потоков
                if (!filesInQueue.isEmpty()) {

                    //Берем следующий файл из очереди
                    FileEntity nextFile = getFilesInQueue().remove(0);

                    //Запускаем задание архивации
                    pool.submit(createArchiverThread(nextFile));

                    filesInProgress.add(nextFile);

                    logFileAddedInProgress(nextFile);

                } else {
                    runningThreads--;
                }

                Properties properties = new Properties();
                try (FileInputStream fileInputStream = new FileInputStream("src\\main\\resources\\config\\config.properties")) {
                    properties.load(fileInputStream);
                    String serviceDownloadAddress = properties.getProperty("archiverDownloadService");
                    Utils.MailSender.send(result.getEmail(), serviceDownloadAddress + result.getFileNameOutput());
                    logFileSended(result);
                } catch (Exception e) {
                    System.out.println(e);
                }

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
