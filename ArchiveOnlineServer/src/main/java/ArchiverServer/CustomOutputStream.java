/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ArchiverServer;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.Charset;
import javax.swing.JTextArea;

/**
 * Переопределенный OutPut Stream коотрый перенарправляет вывод на JTextArea
 *
 * @author minel
 */
public class CustomOutputStream extends OutputStream {

    private JTextArea jTextArea;

    public CustomOutputStream(JTextArea jTextArea) {
        this.jTextArea = jTextArea;
    }

    @Override
    public void write(int b) throws IOException {
        // redirects data to the text area
        jTextArea.append(new String(new byte[]{(byte) b}, Charset.forName("utf-8")));
        // scrolls the text area to the end of data
        jTextArea.setCaretPosition(jTextArea.getDocument().getLength());
    }

    @Override
    public void write(byte[] b) throws IOException {
        // redirects data to the text area
        jTextArea.append(String.valueOf(b));

        // scrolls the text area to the end of data
        jTextArea.setCaretPosition(jTextArea.getDocument().getLength());

    }

}
