/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ArchiverThreads;

import ArchiveMethods.GZIPArchiver;
import ArchiverClasses.Archiver;
import ArchiverClasses.ArchiverThread;
import ArchiverClasses.FileEntity;

/**
 *
 * @author minel
 */
public class UnGZIPArchiverThread extends ArchiverThread{

    public static int type = Archiver.ServerType.DEPRESSOR.ordinal();
    public static String format = "gzip";
    
    @Override
    public FileEntity call() throws Exception {
        getFile().setFileNameOutput(GZIPArchiver.decompress(getInFileName(), Archiver.OUTPUTFILE_PATH));
        return getFile();
    }
    
}
