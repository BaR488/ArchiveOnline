/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ArchiveProgram;

import java.io.File;

/**
 *
 * @author minel
 */
public abstract class ArchiverThread implements Runnable {

    private String fileName;
    
    /**
     * @return the file
     */
    public String getFileName() {
        return fileName;
    }

    public ArchiverThread(String file) {
        this.fileName = file;
    }

    /**
     * @param fileName the fileName to set
     */
    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

}
