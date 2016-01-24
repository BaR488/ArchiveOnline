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
public class GZIPArchiverThread extends ArchiverThread {

    public static int type = Archiver.ServerType.COMPRESSOR.ordinal();
    public static String format = "gz";

    @Override
    public FileEntity call() throws Exception {
        getFile().setFileNameOutput(GZIPArchiver.compress(getInFileName(), Archiver.OUTPUTFILE_PATH));
        return getFile();
    }

}
