/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ArchiverThreads;

import ArchiverClasses.Archiver;
import ArchiverClasses.ArchiverThread;

/**
 *
 * @author minel
 */
public class RarArchiveThread extends ArchiverThread {

    public static int type = Archiver.ServerType.COMPRESSOR.ordinal();
    public static String format = "rar";
    
    @Override
    public String call() throws Exception {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
