/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ArchiverServices;

import ArchiverClasses.Archiver;
import java.io.File;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 *
 * @author minel
 */
@Path("/archiver/downloadFile")
public class FileDownloader {

    @GET
    @Produces(MediaType.APPLICATION_OCTET_STREAM)
    public Response uploadFile(@QueryParam("fileName") String fileName) {
        try {
            File file = new File(Archiver.OUTPUTFILE_PATH + fileName);
            RFC5987Encoder encoder = new RFC5987Encoder();
            String encodedName = encoder.encode(file.getName());
            return Response.ok((Object) file).header("Content-Disposition", "attachment; filename=" + encodedName).build();
        } catch (Exception ex) {
            System.err.println(ex);
            return Response.status(Response.Status.BAD_REQUEST).build();
        }

    }

}
