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

    private File file;

    /**
     * @return the file
     */
    public File getFile() {
        return file;
    }

    public ArchiverThread(File file) {
        this.file = file;
    }

    //В данном методе будет происходть сжатие/расжатие файла
    @Override
    public void run() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
