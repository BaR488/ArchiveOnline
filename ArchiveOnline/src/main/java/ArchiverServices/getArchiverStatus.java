/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ArchiverServices;

import ArchiveProgram.Archiver.ArchiverStatus;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

/**
 *
 * @author minel
 */
@Path("/archiver/getStatus")
public class getArchiverStatus {

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public ArchiverStatus getStatus() {
        return RunningArchiver.archiver.getStatus();
    }

}
