/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ArchiveMethods;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;

/**
 *
 * @author minel
 */
public class GZIPArchiver {

    public static String compress(String filePath, String outputFolder) {

        //Создаем директорию если таковой не существует
        File outputDir = new File(outputFolder);
        if (!outputDir.exists()) {
            outputDir.mkdir();
        }

        String fileName = FilenameUtils.getName(filePath);
        String zippedFilePath = outputFolder + fileName + ".gz"; //Путь к результирующему файлу
        try (GZIPOutputStream gzos = new GZIPOutputStream(new FileOutputStream(zippedFilePath))) {
            gzos.write(FileUtils.readFileToByteArray(new File(filePath)));
            gzos.finish();
            return FilenameUtils.getName(fileName);
        } catch (IOException ex) {
            Logger.getLogger(GZIPArchiver.class.getName()).log(Level.SEVERE, null, ex);
            return "";
        }

    }

    public static String decompress(String filePath, String outputFolder) {

        //Создаем директорию если таковой не существует
        File outputDir = new File(outputFolder);
        if (!outputDir.exists()) {
            outputDir.mkdir();
        }

        String fileName = FilenameUtils.getName(filePath);
        String extractedFilePath = outputFolder + File.separator + FilenameUtils.removeExtension(fileName);

        try (GZIPInputStream gzis = new GZIPInputStream(new FileInputStream(filePath));
                FileOutputStream out = new FileOutputStream(extractedFilePath)) {
            int count = 0;
            int buffLength = 4096;
            byte[] buffer = new byte[buffLength];
            while ((count = gzis.read(buffer)) > 0) {
                out.write(buffer, 0, count);
            }
            return FilenameUtils.getName(extractedFilePath);
        } catch (IOException ex) {
            Logger.getLogger(GZIPArchiver.class.getName()).log(Level.SEVERE, null, ex);
            return "";
        }

    }
}
