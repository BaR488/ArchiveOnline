/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ArchiverServer;

import ArchiverClasses.Archiver;
import ArchiverThreads.ZipArchiveThread;
import static Utils.ConsoleLogger.logMessage;
import static Utils.ConsoleLogger.logServerStopped;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.eclipse.jetty.server.Server;

/**
 *
 * @author minel
 */
public class StartArchiverThread implements Runnable {

    private Archiver<?> archiver;

    public StartArchiverThread(Archiver<?> archiver) {
        this.archiver = archiver;
    }

    @Override
    public void run() {
        try {
            //Запускаем архивацию
            archiver.start();
        } catch (InterruptedException ex) {
            logServerStopped();
        }
    }

}
