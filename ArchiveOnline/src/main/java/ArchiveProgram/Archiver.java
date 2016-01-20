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
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author minel
 */
public class Archiver implements ArchiverOnline {

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

    private Integer port; //Порт на котором запущен архиватор
    private Integer type; //тип сжатие/расжатие
    private String format; //Формат сервера
    private Integer threadCount; //Количество одновременно работающих потоков
    private Integer queueSize; //Размер очереди

    //protected ArrayList<? extends ArchiverThread> runningThreads; //Запущенные потоки
    //protected ArrayList<File> filesInQueue; //Файлы в очереди
    protected ArrayList<ArchiverThread> runningThreads; //Запущенные потоки
    protected ArrayList<String> filesInQueue; //Файлы в очереди

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

    public Archiver(Integer port, String format, Integer threadCount, Integer queueSize, ServerType type) {
        this.format = format;
        this.threadCount = threadCount;
        this.queueSize = queueSize;
        this.runningThreads = new ArrayList<>();
        this.filesInQueue = new ArrayList<>();
        this.type = type.ordinal();
        this.port=port;
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
    public void addFile(String file) {
        if (runningThreads.size() == threadCount) {
            filesInQueue.add(file);
        } else if (runningThreads.size() < threadCount) {
            runningThreads.add(new ArchiverThread(file));
        }
    }

    //Возвращает статус архиватора
    public ArchiverStatus getStatus() {
        return new ArchiverStatus(filesInQueue.size(), runningThreads.size());
    }

}
