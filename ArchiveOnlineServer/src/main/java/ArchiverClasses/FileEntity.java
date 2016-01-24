/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ArchiverClasses;

/**
 *
 * @author boris
 */
public class FileEntity {
    private String fileNameInput;
    private String fileNameOutput;

    public FileEntity(String fileNameInput, String fileNameOutput, String email) {
        this.fileNameInput = fileNameInput;
        this.fileNameOutput = fileNameOutput;
        this.email = email;
    }

    public void setFileNameInput(String fileNameInput) {
        this.fileNameInput = fileNameInput;
    }

    public void setFileNameOutput(String fileNameOutput) {
        this.fileNameOutput = fileNameOutput;
    }

    
    private String email;

    public String getFileNameInput() {
        return fileNameInput;
    }

    public String getFileNameOutput() {
        return fileNameOutput;
    }


    public void setEmail(String email) {
        this.email = email;
    }

    public String getEmail() {
        return email;
    }
}
