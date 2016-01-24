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
public class UnSevenZArchiveThread extends ArchiverThread {

    public static int type = Archiver.ServerType.DEPRESSOR.ordinal();
    public static String format = "7z";

    public UnSevenZArchiveThread() {

    }

    public UnSevenZArchiveThread(FileEntity file) {
        super(file);
    }

    @Override
    public FileEntity call() throws Exception {
        getFile().setFileNameOutput(SevenZArchiver.decompress(getInFileName(), Archiver.OUTPUTFILE_PATH));
        return getFile();
    }
}
