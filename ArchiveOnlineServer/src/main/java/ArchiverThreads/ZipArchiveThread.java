/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ArchiverThreads;

import ArchiverClasses.Archiver;
import ArchiverClasses.ArchiverThread;
import ArchiveMethods.ZipArchiver;
import ArchiverClasses.FileEntity;

/**
 *
 * @author minel
 */
public class ZipArchiveThread extends ArchiverThread {

    public static int type = Archiver.ServerType.COMPRESSOR.ordinal();
    public static String format = "zip";

    public ZipArchiveThread() {

    }

    public ZipArchiveThread(FileEntity file) {
        super(file);
    }

    @Override
    public FileEntity call() throws Exception {
        getFile().setFileNameOutput(ZipArchiver.compress(getInFileName(), Archiver.OUTPUTFILE_PATH));
        return getFile();
    }

}
