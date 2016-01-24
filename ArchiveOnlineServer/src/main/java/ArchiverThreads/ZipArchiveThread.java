/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ArchiverThreads;

import ArchiverClasses.Archiver;
import ArchiverClasses.ArchiverThread;
import ArchiveMethods.ZipArchiver;

/**
 *
 * @author minel
 */
public class ZipArchiveThread extends ArchiverThread {

    public static int type = Archiver.ServerType.COMPRESSOR.ordinal();
    public static String format = "zip";

    public ZipArchiveThread() {

    }

    public ZipArchiveThread(String file) {
        super(file);
    }

    @Override
    public String call() throws Exception {
        return ZipArchiver.zip(getFileName(), Archiver.OUTPUTFILE_PATH);
    }

}
