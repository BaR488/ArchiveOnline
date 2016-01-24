/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ArchiverThreads;

import ArchiveMethods.SevenZArchiver;
import ArchiverClasses.Archiver;
import ArchiverClasses.ArchiverThread;
import ArchiverClasses.FileEntity;

/**
 *
 * @author minel
 */
public class SevenZArchiveThread extends ArchiverThread {

    public static int type = Archiver.ServerType.COMPRESSOR.ordinal();
    public static String format = "7z";

    public SevenZArchiveThread() {

    }

    public SevenZArchiveThread(FileEntity file) {
        super(file);
    }

    @Override
    public FileEntity call() throws Exception {
        getFile().setFileNameOutput(SevenZArchiver.compress(getInFileName(), Archiver.OUTPUTFILE_PATH));
        return getFile();
    }

}
