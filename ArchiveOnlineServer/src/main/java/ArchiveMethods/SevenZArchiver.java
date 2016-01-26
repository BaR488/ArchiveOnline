/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ArchiveMethods;

import static Utils.ConsoleLogger.logMessage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.compress.archivers.sevenz.SevenZArchiveEntry;
import org.apache.commons.compress.archivers.sevenz.SevenZFile;
import org.apache.commons.compress.archivers.sevenz.SevenZMethod;
import org.apache.commons.compress.archivers.sevenz.SevenZOutputFile;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;

/**
 *
 * @author minel
 */
public class SevenZArchiver {

    public static String compress(String filePath, String outputFolder) {

        try {
            //Создаем директорию если таковой не существует
            File outputDir = new File(outputFolder);
            if (!outputDir.exists()) {
                outputDir.mkdir();
            }

            String fileName = FilenameUtils.getName(filePath);
            String zippedFilePath = outputFolder + FilenameUtils.removeExtension(fileName) + ".7z"; //Путь к результирующему файлу

            File fileToArchive = new File(filePath);

            try (SevenZOutputFile sevenZOutput = new SevenZOutputFile(new File(zippedFilePath))) {
                sevenZOutput.setContentCompression(SevenZMethod.LZMA2);
                SevenZArchiveEntry entry = sevenZOutput.createArchiveEntry(fileToArchive, fileName);
                sevenZOutput.putArchiveEntry(entry);
                sevenZOutput.write(FileUtils.readFileToByteArray(fileToArchive));
                sevenZOutput.closeArchiveEntry();
            }
            return FilenameUtils.getName(zippedFilePath);

        } catch (Exception ex) {
            System.err.println(ex);
            return null;
        }
    }

    public static String decompress(String filePath, String outputFolder) {
        
        //Создаем директорию если таковой не существует
        File outputDir = new File(outputFolder);
        if (!outputDir.exists()) {
            outputDir.mkdir();
        }

        try {
            SevenZArchiveEntry entry;
            try (SevenZFile sevenZFile = new SevenZFile(new File(filePath))) {
                entry = sevenZFile.getNextEntry();
                String extractedFilePath = outputFolder + File.separator + entry.getName();
                File extractedFile = new File(extractedFilePath);
                byte[] content = new byte[(int) entry.getSize()];
                sevenZFile.read(content);
                FileUtils.writeByteArrayToFile(extractedFile, content);
                return FilenameUtils.getName(extractedFilePath);
            }
        } catch (Exception ex) {
            System.err.println(ex);
            return null;
        }

    }

}
