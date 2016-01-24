/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ArchiverServer;

import ArchiverClasses.Archiver;
import ArchiverThreads.ZipArchiveThread;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.glassfish.jersey.media.multipart.MultiPartFeature;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.servlet.ServletContainer;

/**
 *
 * @author minel
 */
public class StartServerThread implements Callable<Server> {

    private Class typeArgumentClass; //Тип потока архиватора
    private Integer port; //Порт на котором запущен архиватор
    private Integer type; //тип сжатие/расжатие
    private String format; //Формат сервера
    private Integer threadCount; //Количество одновременно работающих потоков
    private Integer queueSize; //Размер очереди

    private Server jettyServer;

    public StartServerThread(Class typeArgumentClass, Integer port, String format, Integer threadCount, Integer queueSize, Archiver.ServerType type) {
        this.typeArgumentClass = typeArgumentClass;
        this.format = format;
        this.threadCount = threadCount;
        this.queueSize = queueSize;
        this.type = type.ordinal();
        this.port = port;
    }

    //Инициализирует сервер Jetty 
    private void startJetty(int port) throws Exception {

        try {

            jettyServer = new Server(port);

            ResourceConfig config = new ResourceConfig();
            config.packages("ArchiverServices");
            config.register(MultiPartFeature.class);

            ServletHolder servlet = new ServletHolder(new ServletContainer(config));

            ServletContextHandler context = new ServletContextHandler(jettyServer, "/*");
            context.addServlet(servlet, "/*");

            jettyServer.start();

        } catch (Exception ex) {
            Logger.getLogger(StartServerThread.class.getName()).log(Level.SEVERE, null, ex);
            stopJetty();
        }

    }

    @Override
    public Server call() throws Exception {

        //Запускаем сервер Jetty и севрисы
        startJetty(port);
        
        if (jettyServer.isRunning()) {

            //Создаем объект архиватор
            Archiver<ZipArchiveThread> zipArchiver = new Archiver<>(typeArgumentClass, port, format, threadCount, queueSize, Archiver.ServerType.values()[type]);

            //Регистрируем его
            zipArchiver.register();

            //Запускаем архивацию
            try {
                zipArchiver.start();
            } catch (InterruptedException ex) {
                stopJetty();
            }

        }
        return jettyServer;
    }

    private void stopJetty() {
        try {
            jettyServer.setStopAtShutdown(true);
            jettyServer.stop();
            jettyServer.destroy();
        } catch (Exception ex) {
            Logger.getLogger(StartServerThread.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
