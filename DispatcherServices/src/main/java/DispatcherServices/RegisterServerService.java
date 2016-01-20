/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DispatcherServices;

import java.io.FileInputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ws.rs.core.MultivaluedHashMap;
import org.json.simple.parser.ParseException;

/**
 *
 * @author minel
 */
public class RegisterServerService extends RESTService {

    public RegisterServerService() {
        try {
            Properties properties = new Properties();
            try (FileInputStream fileInputStream = new FileInputStream("../Properties/services.properties")) {
                properties.load(fileInputStream);
                init(properties.getProperty("registerResource"));
            }
        } catch (IOException ex) {
            Logger.getLogger(GetServersService.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void register(Integer port, Integer type, String format, Integer threadCount, Integer queueSize) throws ParseException, RegistrationFailedException {

        //Создаем карту с параметрами
        MultivaluedHashMap paramsMap = new MultivaluedHashMap();
        paramsMap.putSingle("port", port.toString());
        paramsMap.putSingle("type", type.toString());
        paramsMap.putSingle("format", format);
        paramsMap.putSingle("threadCount", threadCount.toString());
        paramsMap.putSingle("queueSize", queueSize.toString());
     
        if (checkGetRequestStatus(paramsMap)) {  
            SimpleDateFormat dt = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
            System.out.println(dt.format(new Date()) + " Сервер успешно зарегестрирован на главном сервере.");
        } else {
            SimpleDateFormat dt = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
            System.err.println(dt.format(new Date()) + " Ошибка во время регистации. Сервер не был зарегестрирован.");
            throw new RegistrationFailedException("Ошибка во время регистации. Сервер не был зарегестрирован.");
        }
        
    }

}
