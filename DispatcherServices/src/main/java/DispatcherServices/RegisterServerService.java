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
import org.json.simple.JSONArray;
import org.json.simple.parser.ParseException;

/**
 *
 * @author minel
 */
public class RegisterServerService extends RESTService {

    public RegisterServerService() {
        try {
            Properties properties = new Properties();
            FileInputStream fileInputStream = new FileInputStream("src/main/resources/services.properties");
            properties.load(fileInputStream);
            init(properties.getProperty("registerResource"));
            fileInputStream.close();
        } catch (IOException ex) {
            Logger.getLogger(GetServersService.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void register(Integer type, String format, Integer threadCount, Integer queueSize) throws ParseException, RegistrationFailedException{
        
        //Создаем карту с параметрами
        MultivaluedHashMap paramsMap = new MultivaluedHashMap();
        paramsMap.putSingle("type", type.toString());
        paramsMap.putSingle("format", format);
        paramsMap.putSingle("threadCount", threadCount.toString());
        paramsMap.putSingle("queueSize", queueSize.toString());
        
        JSONArray response = getResponse(paramsMap);
        if (response.isEmpty()){
            throw new RegistrationFailedException("Ошибка во время регистации. Сервер не был зарегестрирован.");
        }       
    }
    
}
