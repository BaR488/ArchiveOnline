/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DispatcherServices;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.WebResource;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedHashMap;
import javax.ws.rs.core.UriBuilder;
import org.json.simple.JSONArray;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

/**
 *
 *
 * @author minel
 */
public class RESTService {

    protected Client client;
    protected WebResource service;

    public RESTService() {

    }

    //Создает объект типа RESTservice по указанному юрл сервиса
    public RESTService(String serviceUrl) {
        init(serviceUrl);
    }

    //Инициализирует клиента и веб ресурс
    public void init(String serviceUrl) {
        client = Client.create();
        service = client.resource(UriBuilder.fromUri(serviceUrl).build());
    }

    //Возвращает Массив JSONов от указанного ресурса сервиса, с указанными параметрами
    public JSONArray getResponse(String resourcePath, MultivaluedHashMap<String, String> params) throws ParseException {

        //Получаем ответ сервера в виде строки
        String serviceResponse = service.path(resourcePath).queryParams(params).accept(MediaType.APPLICATION_JSON).get(String.class);

        //Парсим из ответа JSON
        JSONParser parser = new JSONParser();

        //И возвращаем его
        return (JSONArray) parser.parse(serviceResponse);

    }

    //Возвращает Массив JSONов от указанного ресурса сервиса, с указанными параметрами
    public JSONArray getResponse(MultivaluedHashMap<String, String> params) throws ParseException {

        //Получаем ответ сервера в виде строки
        String serviceResponse = service.queryParams(params).accept(MediaType.APPLICATION_JSON).get(String.class);

        //Парсим из ответа JSON
        JSONParser parser = new JSONParser();

        //И возвращаем его
        return (JSONArray) parser.parse(serviceResponse);

    }
}
