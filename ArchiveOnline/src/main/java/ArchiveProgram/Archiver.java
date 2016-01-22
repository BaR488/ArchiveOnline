/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ArchiveProgram;

//import DispatcherServices.RegisterServerService;
import ArchiverServices.RunningArchiver;
import DispatcherServices.RegisterServerService;
import java.util.ArrayList;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author minel
 * @param <T> the type of ArchiveThread
 */
public class Archiver<T extends ArchiverThread> implements ArchiverOnline {

    public static String INPUTFILE_PATH = "InputFiles\\";
    public static String OUTPUTFILE_PATH = "OutputFiles\\";

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

    private Class<T> typeArgumentClass;

    private Integer port; //Порт на котором запущен архиватор
    private Integer type; //тип сжатие/расжатие
    private String format; //Формат сервера
    private Integer threadCount; //Количество одновременно работающих потоков
    private Integer queueSize; //Размер очереди

    private Integer runningThreads; //Количество выполняющихся потоков
    private ExecutorService threadPool; //Пул потоков
    private ExecutorCompletionService<String> pool; //Обертка пула потоков
    private ArrayList<String> filesInQueue; //Файлы в очереди

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

    public Archiver(Class<T> typeArgumentClass, Integer port, String format, Integer threadCount, Integer queueSize, ServerType type) {
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
            registerService.register(port, type, format, threadCount, queueSize);
        } catch (Exception ex) {
            Logger.getLogger(Archiver.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    //Добавляет очередной файл на сжатие
    @Override
    public void addFile(String fileName) {

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

    //Возвращает статус архиватора
    public ArchiverStatus getStatus() {
        return new ArchiverStatus(filesInQueue.size(), runningThreads);
    }

    //Начинает архивацию
    public void start() {
        while (true) {
            try {
                //Получаем результат выполнения потока
                String result = pool.take().get();

                //Если есть файлы в очереди, запускаем первый в очереди, иначе уменьшаем количество потоков
                if (!filesInQueue.isEmpty()) {

                    //Берем следующий файл из очереди
                    String nextFile = filesInQueue.remove(0);

                    //Запускаем задание архивации
                    pool.submit(createArchiverThread(nextFile));

                } else {
                    runningThreads--;
                }
            } catch (InterruptedException | ExecutionException | InstantiationException | IllegalAccessException ex) {
                Logger.getLogger(Archiver.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    //Создаем задание для архивации
    T createArchiverThread(String fileName) throws InstantiationException, IllegalAccessException {
        T thread = typeArgumentClass.newInstance();
        thread.setFileName(fileName);
        return thread;
    }

}
