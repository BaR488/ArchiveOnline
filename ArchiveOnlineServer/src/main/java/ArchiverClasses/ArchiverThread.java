/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ArchiverClasses;

import java.util.concurrent.Callable;

/**
 *
 * @author minel
 */
public abstract class ArchiverThread implements Callable<FileEntity> {

    private FileEntity file;

    public FileEntity getFile() {
        return file;
    }

    /**
     * @return the file
     */
    public String getInFileName() {
        return file.getFileNameInput();
    }
    
        /**
     * @return the file
     */
    public String getOutFileName() {
        return file.getFileNameOutput();
    }
    
    /**
     * @param fileName the fileName to set
     */
    public void setFile(FileEntity fileName) {
        this.file = fileName;
    }


    public ArchiverThread() {

    }
    
    public ArchiverThread(FileEntity file) {
        this.file = file;
    }


}
