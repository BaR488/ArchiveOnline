/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DispatcherServices;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Properties;
import javax.ws.rs.core.MultivaluedHashMap;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;

/**
 * Сервис для получения списка способов сжатия/расжатия
 *
 * @author minel
 */
public class GetServersService extends RESTService {

    //Константы способов сжатия
    private final static Integer ARCHIVE_TYPE = 0;
    private final static Integer UNARCHIVE_TYPE = 1;

    //URL ресурса со списком форматов
    private final static String GETSERVERS_RESOURCE = "api/servers";

    public GetServersService(String serviceUrl) {
        super(serviceUrl);
    }

    //Возвращает массив доступных способов сжатия
    public ArrayList<String> getArchiveMethods() throws ParseException {
        return getMethodsByType(ARCHIVE_TYPE);
    }

    //Возвращает список доступных способов расжатия
    public ArrayList<String> getUnArchiveMethods() throws ParseException {
        return getMethodsByType(UNARCHIVE_TYPE);
    }

    //Возвращает список доступных способов по типу действия
    private ArrayList<String> getMethodsByType(Integer type) throws ParseException {

        //Список доступных форматов сжатия
        ArrayList<String> result = new ArrayList<>();

        //Создаем карту с параметрами
        MultivaluedHashMap paramsMap = new MultivaluedHashMap();
        paramsMap.putSingle("type", type.toString());

        //Обращаемся к сервису и получаем массив JSON
        JSONArray jsonArray = getResponse(GETSERVERS_RESOURCE, paramsMap);

        //Извлекаем имена способов
        jsonArray.forEach((jsonObj) -> {
            result.add(((JSONObject) jsonObj).get("Name").toString());
        });

        //Возвращаем результат
        return result;
    }

}
