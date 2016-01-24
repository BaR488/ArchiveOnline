/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ArchiverServices;

import DispatcherServices.GetServersService;
import DispatcherServices.RESTService;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.multipart.FormDataBodyPart;
import com.sun.jersey.multipart.FormDataMultiPart;
import com.sun.jersey.multipart.file.FileDataBodyPart;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ws.rs.core.MediaType;

/**
 *
 * @author minel
 */
public class UploadFileService extends RESTService {

    public UploadFileService(String serverAdress) {
        try {
            Properties properties = new Properties();
            try (FileInputStream fileInputStream = new FileInputStream("../Properties/services.properties")) {
                properties.load(fileInputStream);
                init(serverAdress + properties.getProperty("archiverUploadFileResource"));
            }
        } catch (IOException ex) {
            Logger.getLogger(GetServersService.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void uploadFile(File file, String email) {              
        FormDataMultiPart formDataMultiPart = new FormDataMultiPart();
        formDataMultiPart.bodyPart(new FileDataBodyPart("file", file, MediaType.MULTIPART_FORM_DATA_TYPE));
        ClientResponse  response = service.queryParam("email", email).type(MediaType.MULTIPART_FORM_DATA_TYPE).post(ClientResponse.class, formDataMultiPart); 
    }

}
