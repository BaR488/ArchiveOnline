/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DispatcherServices;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientHandlerException;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedHashMap;
import javax.ws.rs.core.UriBuilder;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
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
        ClientConfig config = new DefaultClientConfig();
        client = Client.create(config);
        service = client.resource(UriBuilder.fromUri(serviceUrl).build());

    }

    //Возвращает Массив JSONов от указанного ресурса сервиса, с указанными параметрами
    public JSONArray getResponseArray(String resourcePath, MultivaluedHashMap<String, String> params) throws ParseException, ClientHandlerException {

        //Получаем ответ сервера в виде строки
        String serviceResponse = service.path(resourcePath).queryParams(params).accept(MediaType.APPLICATION_JSON).get(String.class);

        //Парсим из ответа JSON
        JSONParser parser = new JSONParser();

        //И возвращаем его
        return (JSONArray) parser.parse(serviceResponse);

    }

    //Возвращает Массив JSONов от указанного ресурса сервиса, с указанными параметрами
    public JSONArray getResponseArray(MultivaluedHashMap<String, String> params) throws ParseException, ClientHandlerException {

        //Получаем ответ сервера в виде строки
        String serviceResponse = service.queryParams(params).accept(MediaType.APPLICATION_JSON).get(String.class);

        //Парсим из ответа JSON
        JSONParser parser = new JSONParser();

        //И возвращаем его
        return (JSONArray) parser.parse(serviceResponse);

    }

    //Возвращает объект JSON полученный серсиса
    public JSONObject getResponseObject(MultivaluedHashMap<String, String> params) throws ParseException, ClientHandlerException {

        //Получаем ответ сервера в виде строки
        String serviceResponse = service.queryParams(params).accept(MediaType.APPLICATION_JSON).get(String.class);

        //Парсим из ответа JSON
        JSONParser parser = new JSONParser();

        //И возвращаем его
        return (JSONObject) parser.parse(serviceResponse);

    }

    //Возвращает true если сервис вернул 200 OK
    public boolean checkGetRequestStatus(MultivaluedHashMap<String, String> params) {

        try {
            ClientResponse serviceResponse = service.queryParams(params).accept(MediaType.APPLICATION_JSON).get(ClientResponse.class);
            return serviceResponse.getStatusInfo().getStatusCode() == ClientResponse.Status.OK.getStatusCode();
        } catch (Exception ex){
            return false;
        }

    }

}
