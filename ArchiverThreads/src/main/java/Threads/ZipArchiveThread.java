/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Threads;

import ArchiveProgram.Archiver;
import ArchiveProgram.ArchiverThread;

/**
 *
 * @author minel
 */
public class ZipArchiveThread extends ArchiverThread{

    public ZipArchiveThread(){
        
    }
    
    public ZipArchiveThread(String file) {
        super(file);
    }

    @Override
    public String call() throws Exception {
       return ZipArchivator.zip(getFileName(), Archiver.OUTPUTFILE_PATH);
    }
    
}
