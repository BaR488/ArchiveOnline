/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ArchiverServices;

import ArchiveProgram.Archiver;
import java.io.File;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.io.FileUtils;

/**
 *
 * @author boris
 */
@Path("/archiver/uploadFile")
public class FileUploader {

    @POST
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.TEXT_HTML)
    public Response uploadFile(@Context HttpServletRequest servletRequest) {

        try {
            List<FileItem> items = new ServletFileUpload(new DiskFileItemFactory()).parseRequest(servletRequest);
            if (items.size() > 0) {

                FileItem item = items.get(0);
                String newFileName = Archiver.INPUTFILE_PATH + getTimeStamp() + item.getName();
                InputStream fileContent = items.get(0).getInputStream();
                File targetFile = new File(newFileName);
                FileUtils.copyInputStreamToFile(fileContent, targetFile);
                RunningArchiver.archiver.addFile(newFileName);
                return Response.ok().build();
            }
            return Response.status(Response.Status.BAD_REQUEST).build();
        } catch (Exception ex) {
            System.err.println(ex);
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
    }

    //Возвращает метку времени
    public String getTimeStamp() {
        DateFormat dateFormat = new SimpleDateFormat("MMddHHmmssSSS_");
        return dateFormat.format(new Date());
    }
}
