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
public class UnZipArchiveThread extends ArchiverThread {

    public UnZipArchiveThread() {

    }

    public UnZipArchiveThread(String file) {
        super(file);
    }

    @Override
    public String call() throws Exception {
        return ZipArchiver.unZip(getFileName(), Archiver.OUTPUTFILE_PATH);
    }
}
