/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ArchiverServer;

import ArchiverClasses.Archiver;
import static Utils.ConsoleLogger.logServerStopped;

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
