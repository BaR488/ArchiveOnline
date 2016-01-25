/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Utils;

import java.text.SimpleDateFormat;
import java.util.Date;

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
}
