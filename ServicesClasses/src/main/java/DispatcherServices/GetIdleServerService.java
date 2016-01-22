/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DispatcherServices;

import java.io.FileInputStream;
import java.io.IOException;
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
public class GetIdleServerService extends RESTService {

    //Константы типа серверов
    public final static Integer ARCHIVE_TYPE = 0;
    public final static Integer UNARCHIVE_TYPE = 1;

    public GetIdleServerService() {
        try {
            Properties properties = new Properties();
            try (FileInputStream fileInputStream = new FileInputStream("../Properties/services.properties")) {
                properties.load(fileInputStream);
                init(properties.getProperty("getIdleServerResource"));
            }
        } catch (IOException ex) {
            Logger.getLogger(GetServersService.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    //Возвращает полный адрес самого не загруженого сервера
    public String getIdleServerAdress(Integer type, String format) throws ParseException {

        //Получаем объект от сервиса
        JSONObject jsonObj = getIdleServer(type, format);

        if (jsonObj != null) {

            //Извлекаем из него адресс и порт сервера
            String ipAdress = jsonObj.get("Address").toString();
            String port = jsonObj.get("Port").toString();

            //Возвращаем полный адресс
            return "http://" + ipAdress + ":" + port;

        } else {
            return "";
        }

    }

    //Возвращает самый не загруженный сервер
    public JSONObject getIdleServer(Integer type, String format) throws ParseException {

        //Создаем карту с параметрами
        MultivaluedHashMap paramsMap = new MultivaluedHashMap();
        paramsMap.putSingle("type", type.toString());
        paramsMap.putSingle("format", format);

        //Получаем объект 
        return getResponseObject(paramsMap);
    }

}
