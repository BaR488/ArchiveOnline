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
import org.json.simple.JSONObject;
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
                init(properties.getProperty("registerService"));
            }
        } catch (IOException ex) {
            Logger.getLogger(GetServersService.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public int register(Integer port, Integer type, String format, Integer threadCount, Integer queueSize) throws ParseException {

        try {
            Properties properties = new Properties();
            try (FileInputStream fileInputStream = new FileInputStream("../Properties/services.properties")) {

                properties.load(fileInputStream);
                String resource = properties.getProperty("registerResource");

                //Создаем карту с параметрами
                MultivaluedHashMap paramsMap = new MultivaluedHashMap();
                paramsMap.putSingle("port", port.toString());
                paramsMap.putSingle("type", type.toString());
                paramsMap.putSingle("format", format);
                paramsMap.putSingle("threadCount", threadCount.toString());
                paramsMap.putSingle("queueSize", queueSize.toString());

                //Отправляем запрос серверу
                JSONObject jsonServer = getResponseObject(resource, paramsMap);
                if (jsonServer != null) {
                    return new Integer(jsonServer.get("Id").toString());
                } else {
                    return -1;
                }

            }
        } catch (IOException ex) {
            Logger.getLogger(GetServersService.class.getName()).log(Level.SEVERE, null, ex);
            return -1;
        }
    }

    public void unRegister(Integer serverId) {

        try {
            Properties properties = new Properties();
            try (FileInputStream fileInputStream = new FileInputStream("../Properties/services.properties")) {
                
                properties.load(fileInputStream);
                String resource = properties.getProperty("deleteResource");
                
                //Создаем карту с параметрами
                MultivaluedHashMap paramsMap = new MultivaluedHashMap();
                paramsMap.putSingle("serverId", serverId.toString());

                //Отправляем запрос серверу
                doGetRequest(resource, paramsMap);
            }
        } catch (IOException ex) {
            Logger.getLogger(GetServersService.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

}
