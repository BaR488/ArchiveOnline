/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ArchiveOnlineServer;

import ArchiveProgram.Archiver;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.servlet.ServletContainer;

/**
 *
 * @author minel
 */
public class Main {

    private static Server jettyServer;

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        
        //Создаем объект архиватор
        Archiver archiver = new Archiver("rar4", 2, 10, Archiver.ServerType.DEPRESSOR);

        //Регистрируем его
        archiver.register();
        
        archiver.addFile("1");
        archiver.addFile("1");
        archiver.addFile("1");
        archiver.addFile("1");
        archiver.addFile("1");
        archiver.addFile("1");
        archiver.addFile("1");
        archiver.addFile("1");
        
        //Запускаем сервер Jetty и севрисы
        startJetty(8081);

    }

    //Запускает сервер Jetty на указаном порту
    private static void startJetty(int port) {

        jettyServer = new Server(port);
        
        ResourceConfig config = new ResourceConfig();
        config.packages("ArchiverServices");

        ServletHolder servlet = new ServletHolder(new ServletContainer(config));

        ServletContextHandler context = new ServletContextHandler(jettyServer, "/*");
        context.addServlet(servlet, "/*");

        try {
            jettyServer.start();
            jettyServer.join();
        } catch (Exception ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            jettyServer.destroy();
        }
    }
}
