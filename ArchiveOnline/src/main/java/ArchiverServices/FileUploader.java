/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ArchiverServices;

import java.io.File;
import java.io.InputStream;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.Context;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;

/**
 *
 * @author boris
 */
@Path("/archiver/uploadfile")
public class FileUploader {

    @Context
    private HttpServletRequest servletRequest;

    @POST
    public void getStatus() {
        int i = 1;
        try {
            List<FileItem> items = new ServletFileUpload(new DiskFileItemFactory()).parseRequest(servletRequest);

            for (FileItem item : items) {
                String fileName = FilenameUtils.getName(item.getName());
                InputStream fileContent = item.getInputStream();
                File targetFile = new File("C:\\"+fileName);

                FileUtils.copyInputStreamToFile(fileContent, targetFile);
            }

        } catch (Exception e) {
            System.err.println(e);
        }

    }
}
