/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ArchiveMethods;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.Deflater;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;
import org.apache.commons.io.FilenameUtils;

/**
 *
 * @author minel
 */
public class ZipArchiver {

    public static String format = "zip"; //Формат архивирования

    //Сжатие
    public static String compress(String filePath, String outputFolder) {

        //Создаем директорию если таковой не существует
        File outputDir = new File(outputFolder);
        if (!outputDir.exists()) {
            outputDir.mkdir();
        }

        String fileName = FilenameUtils.getName(filePath);
        String zippedFilePath = outputFolder + FilenameUtils.removeExtension(fileName) + ".zip"; //Путь к результирующему файлу
        String zippedFileName = FilenameUtils.removeExtension(fileName) + ".zip";
        byte[] b = new byte[1024]; //Буфер

        try (FileInputStream in = new FileInputStream(filePath);
                ZipOutputStream out = new ZipOutputStream(new FileOutputStream(zippedFilePath))) {
            out.setLevel(Deflater.BEST_COMPRESSION);
            out.putNextEntry(new ZipEntry(fileName));
            int count;
            while ((count = in.read(b)) > 0) {
                out.write(b, 0, count);
            }
            return zippedFileName;
        } catch (Exception e) {
            System.err.println(e);
            return null;
        }

    }

    //Расжатие
    public static String decompress(String inputfilePath, String outputFolder) {

        String fileName = FilenameUtils.getName(inputfilePath);

        //Создаем директорию если таковой не существует
        File outputDir = new File(outputFolder);
        if (!outputDir.exists()) {
            outputDir.mkdir();
        }

        try (ZipInputStream zipIn = new ZipInputStream(new FileInputStream(inputfilePath))) {

            ZipEntry entry = zipIn.getNextEntry();
            int bufferSize = 1024;
                String filePath = outputFolder + File.separator + entry.getName();
                if (!entry.isDirectory()) {
                    //Если файл, то распоковываем его
                    extractFile(zipIn, filePath, bufferSize);
                } else {
                    // Если директория, то создаем ее
                    File dir = new File(filePath);
                    dir.mkdir();
                }
                zipIn.closeEntry();
            return entry.getName();
        } catch (IOException ex) {
            System.err.println(ex);
            return null;
        }
    }

    /**
     * Extracts a zip entry (file entry)
     *
     * @param zipIn
     * @param filePath
     * @throws IOException
     */
    private static void extractFile(ZipInputStream zipIn, String filePath, int bufferSize) throws IOException {
        BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(filePath));
        byte[] bytesIn = new byte[bufferSize];
        int read = 0;
        while ((read = zipIn.read(bytesIn)) != -1) {
            bos.write(bytesIn, 0, read);
        }
        bos.close();
    }

}
