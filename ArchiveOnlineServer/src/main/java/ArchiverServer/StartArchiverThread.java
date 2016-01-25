/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ArchiverServer;

import ArchiverClasses.Archiver;
import static Utils.ConsoleLogger.logServerStopped;
import javax.swing.JFrame;

/**
 *
 * @author minel
 */
public class StartArchiverThread implements Runnable {

    private Archiver<?> archiver;
    public final JFrame jFrame;

    public StartArchiverThread(Archiver<?> archiver, JFrame jFrame) {
        this.archiver = archiver;
        this.jFrame = jFrame;
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
