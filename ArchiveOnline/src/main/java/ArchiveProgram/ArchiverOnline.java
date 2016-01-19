/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ArchiveProgram;

/**
 * В интерфейсе содержаться методы специфичные для онлайн архиваторов
 * такие как: регистация, добавление в очередь, и т.д.
 * @author minel
 */
public interface ArchiverOnline {
    public void register();
    public void addFile(String file);
}
