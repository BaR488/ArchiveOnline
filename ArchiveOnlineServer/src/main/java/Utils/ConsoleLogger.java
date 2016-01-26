/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Utils;

import ArchiverClasses.FileEntity;
import java.text.SimpleDateFormat;
import java.util.Date;
import org.apache.commons.io.FilenameUtils;

/**
 *
 * @author minel
 */
public class ConsoleLogger {
    
    public static void logMessage(String message) {
        SimpleDateFormat dt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        System.out.println(dt.format(new Date()) + " " + message);
    }
    
    public static void logServerStopped(){
        logMessage("************************************SERVER STOPPED************************************");
    }
    
    public static void logServerStarting(){
        logMessage("************************************STARTING SERVER************************************");
    }
    
    public static void logServerStarted(){
        logMessage("************************************SERVER STARTED************************************");
    }
    
    public static void logServerStopping(){
        logMessage("************************************SERVER STOPPING************************************");
    }
    
    public static void logFileAddedInQueue(FileEntity file){
        logMessage("File " + FilenameUtils.getName(file.getFileNameInput()) + " from " + file.getEmail() + " added in queue");
    }
    
    public static void logFileAddedInProgress(FileEntity file){
        logMessage("File " + FilenameUtils.getName(file.getFileNameInput()) + " from " + file.getEmail() + " is processing now");
    }
    
    public static void logFileArchivateCompleted(FileEntity file){
        logMessage("Processing file " + FilenameUtils.getName(file.getFileNameInput()) + " from " + file.getEmail() + " is done");
    }
    
    public static void logFileSended(FileEntity file){
        logMessage("File " + FilenameUtils.getName(file.getFileNameOutput()) + " was sended to " + file.getEmail());
    }
    
    public static void logFileError(FileEntity file){
        logMessage("Error during processing file " + FilenameUtils.getName(file.getFileNameInput()));
    }
    
    public static void logFileErrorSended(FileEntity file){
        logMessage("Error message was sended to " + file.getEmail());
    }
    
}
