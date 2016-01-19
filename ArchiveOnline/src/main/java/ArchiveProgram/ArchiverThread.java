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
public class ArchiverThread implements Runnable {

    //private File file;
    private String file;
    
    

    /**
     * @return the file
     */
    public String getFile() {
        return file;
    }

    public ArchiverThread(String file) {
        this.file = file;
    }

    //В данном методе будет происходть сжатие/расжатие файла
    @Override
    public void run() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
