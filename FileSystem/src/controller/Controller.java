/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package controller;

import GUI.MainFrame;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import model.structure.Archive;
import model.structure.Commands;
import model.structure.File;
import model.structure.Folder;
import model.structure.VirtualMemory;

/**
 *
 * Controller.java
 * @author fsegovia
 * @created Jun 20, 2015
 * @modified Jun 20, 2015
 * @description [Some desc]
 */
public class Controller {
    
    private static final int COMMAND_INDEX = 0;
    private static final int FIRST_PARAM = 1;
    private static final int SECOND_PARAM = 2;
    private static final int THIRD_PARAM = 3;
    private static final String NUMBER_REGEX = "[1-9][0-9]*";
    private static final String FOLDER_REGEX = "[a-zA-Z0-9]+";
    private static final String FILE_REGEX = "[a-zA-Z0-9]+.[a-zA-Z0-9]+";
    private static final String SPACE = " ";
    
    private File _RootFile;
    private File _CurrentFile;
    private VirtualMemory _VM;
    private MainFrame _View;

    public Controller(MainFrame pView) {
        _RootFile = _CurrentFile = new Folder(null, "home");
        _View = pView;
    }
    
    public void createFile(String pContent, String pFilename) {
        if(!_CurrentFile.containsKey(pFilename)) {
            Map<Integer, String> resultFromWriting = _VM.WriteSector(pContent);
            int lastDotIndex = pFilename.lastIndexOf(".");
            String filename = pFilename.substring(0, lastDotIndex);
            String extension = pFilename.substring(lastDotIndex);
            Archive archive = new Archive(_CurrentFile, filename, extension);
            archive.addContent(resultFromWriting);
            refreshTreeView();
            addStringToTheLog("[->] El archivo: " + pFilename + " ha sido creado exitosamente!");
        }
        else {
            addStringToTheLog("[->] El archivo: " + pFilename + " ya existe en la carpeta actual");
        }
    }
    
    public void createFolder(String pFolderName) {
        if (!_CurrentFile.containsKey(pFolderName)) {
            Folder folder = new Folder((_CurrentFile), pFolderName);
            refreshTreeView();
            addStringToTheLog("[->] El folder: " + pFolderName + " ha sido creado exitosamente!");
        }
        else {
            addStringToTheLog("[->] El folder: " + pFolderName + " ya existe en la carpeta actual");
        }
    }
    
    public void createFileSystem(int pSectorSize, int pSectorQuantity) {
        _VM = new VirtualMemory(pSectorSize, pSectorQuantity);
        refreshTreeView();
        refreshPath();
        addStringToTheLog("[->] Se ha creado un dico de manera exitosa!");
    }
    
    public boolean processCommand(String pCommand) {
        Pattern pattern = Pattern.compile(Commands.VALID_COMMAND_REGEX);
        Matcher matcher = pattern.matcher(pCommand);
        if (matcher.matches()) {
            String[] sequences = pCommand.split(SPACE);
            return processCommand(sequences);
        }
        else {
            return false;
        }
    }
    
    //============================================================================
    
    /**
     * 
     * @param pComandDescription
     * @return 
     */
    public boolean processCommand(String[] pComandDescription) {
        String commandType = pComandDescription[COMMAND_INDEX].toLowerCase();
        switch (commandType) {
            case Commands.COMMAND_CREATE:
                return processCreate(pComandDescription);
            case Commands.COMMAND_FILE:
                return processCreateFile(pComandDescription);
            case Commands.COMMAND_MKDIR:
                return processMKDIR(pComandDescription);
            case Commands.COMMAND_CHANGE_DIR:
                return processChangeDIR(pComandDescription);
            case Commands.COMMAND_LIST_DIR: /*TODO: */
            case Commands.COMMAND_MOD_FILE:
                
            default:
                addStringToTheLog("[->] Comando no reconocido!!! :(");
                return false;
        }
    }
    
    public boolean processCreate(String[] pParams) {
        Pattern pattern  = Pattern.compile(NUMBER_REGEX);
        String firstParam = pParams[FIRST_PARAM];
        String secondParam = pParams[SECOND_PARAM];
        if(pattern.matcher(firstParam).matches() && 
                pattern.matcher(secondParam).matches()) {
            int sectorSize = Integer.parseInt(firstParam);
            int sectorQuantity = Integer.parseInt(secondParam);
            createFileSystem(sectorSize, sectorQuantity);
            return true;
        }
        else {
            return false;
        }
    }
    
    public boolean processCreateFile(String[] pParams) {
        Pattern pattern = Pattern.compile(FILE_REGEX);
        String content = "";
        int length = pParams.length - 1;
        for(int i = 1; i < length; i++) {
            content += " " + pParams[i];
        }
        String filename = pParams[length];
        if(pattern.matcher(filename).matches()) {
            createFile(content, filename);
            return true;
        }
        addStringToTheLog("[->] Nombre de archivo inválido.");
        return false;
    }
    
    public boolean processMKDIR(String[] pParams) {
        Pattern pattern = Pattern.compile(FOLDER_REGEX);
        String folderName = pParams[FIRST_PARAM];
        if(pattern.matcher(folderName).matches()) {
            createFolder(folderName);
            return true;
        }
        addStringToTheLog("[->] Nombre de folder inválido");
        return false;
    }
    
    public boolean processChangeDIR(String[] pParams) {
        String path = pParams[FIRST_PARAM];
        System.out.println("PATH "+path);
        File file = !path.equals("..") ? _CurrentFile.getFile(path) : _CurrentFile.getParent();
        if (file != null && !(file instanceof Archive)) {
            _CurrentFile = file;
            refreshPath();
            addStringToTheLog("[->] Se ha cambiado de directorio!");
            return true;
        }
        addStringToTheLog("[->] El directorio que desea acceder no existe en: " + _CurrentFile.getName());
        return false;
    }
    
    private void refreshTreeView() {
        _View.refreshTreeView(((Folder) _RootFile).print());
    }
    
    public void refreshPath() {
        _View.refreshPath(_CurrentFile.getPath());
    }
    
    private void addStringToTheLog(String pMessage) {
        _View.addToLog(pMessage);
    }

}
