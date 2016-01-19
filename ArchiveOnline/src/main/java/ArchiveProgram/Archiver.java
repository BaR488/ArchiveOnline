/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ArchiveProgram;

//import DispatcherServices.RegisterServerService;
import DispatcherServices.RegisterServerService;
import java.io.File;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author minel
 */
public class Archiver implements ArchiverOnline {

    //Тип сервера сжиматель, расжматель
    public enum ServerType { COMPRESSOR, DEPRESSOR }

    private Integer type;
    private String format; //Формат сервера
    private Integer threadCount; //Количество одновременно работающих потоков
    private Integer queueSize; //Размер очереди
    protected ArrayList<? extends ArchiverThread> runningThreads; //Запущенные потоки
    protected ArrayList<File> filesInQueue; //Файлы в очереди

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

    public Archiver(String format, Integer threadCount, Integer queueSize, ServerType type) {
        this.format = format;
        this.threadCount = threadCount;
        this.queueSize = queueSize;
        this.runningThreads = new ArrayList<>();
        this.filesInQueue = new ArrayList<>();
        this.type = type.ordinal();
    }

    @Override
    public void register() {
        try {
            RegisterServerService registerService = new RegisterServerService();
            registerService.register(type, format, threadCount, queueSize);
        } catch (Exception ex) {
            Logger.getLogger(Archiver.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
