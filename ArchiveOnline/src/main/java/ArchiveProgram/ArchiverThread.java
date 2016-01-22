/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ArchiveProgram;

import java.io.File;
import java.util.concurrent.Callable;

/**
 *
 * @author minel
 */
public abstract class ArchiverThread implements Callable<String> {

    private String fileName;

    /**
     * @return the file
     */
    public String getFileName() {
        return fileName;
    }
    
    /**
     * @param fileName the fileName to set
     */
    public void setFileName(String fileName) {
        this.fileName = fileName;
    }


    public ArchiverThread() {

    }
    
    public ArchiverThread(String file) {
        this.fileName = file;
    }


}
