/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package controller;

import GUI.MainFrame;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
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
    private static final String FOLDER_REGEX = "[a-zA-Z0-9\\-_]+";
    private static final String FILE_REGEX = "[a-zA-Z0-9\\-_]+.[a-zA-Z0-9]+";
    private static final String SPACE = " ";
    
    private File _RootFile;
    private File _CurrentFile;
    private VirtualMemory _VM;
    private MainFrame _View;

    public Controller(MainFrame pView) {
        _RootFile = _CurrentFile = new Folder(null, "home");
        _View = pView;
        /** Delete: Just for test **/
        String[] commandsDefault = {"create 30 20", 
            "mkdir f1", "cd f1", "file some filef11.txt", "file some filef12.mp3",
                "mkdir fs1", "cd fs1", "file some filefs11.txt", "file some filefs12.zip", "cd ..",
                "mkdir fs2", "cd fs2", "file some filefs21.mp3", "cd ..", "cd ..",
            "mkdir f2",  "cd f2", "file some filef21.some", "cd ..",
            "mkdir f3", "file some filef21.avi"};
        for(String command : commandsDefault) {
            processCommand(command);
        }
    }
    
    public void createFile(File pFileParent, String pContent, String pFilename, boolean pShow) {
        if(!_CurrentFile.containsKey(pFilename)) {
            Map<Integer, String> resultFromWriting = _VM.WriteSector(pContent);
            int lastDotIndex = pFilename.lastIndexOf(".");
            String filename = pFilename.substring(0, lastDotIndex);
            String extension = pFilename.substring(lastDotIndex);
            Archive archive = new Archive(pFileParent, filename, extension);
            archive.addContent(resultFromWriting);
            refreshTreeView();
            if (pShow)
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
    
    public void modifyFile(String[] pParams, File pArchive, String pFilename) {
        Set<Entry<Integer, String>> content = pArchive.getContent();
        Date creationDate = ((Archive) pArchive).getCreationDate();
        int[] sectors = new int[content.size()];
        int index = 0;
        for(Entry<Integer, String> current : content) {
            sectors[index] = current.getKey();
            index++;
        }
        _VM.ErraseSector(sectors);
        File parent = pArchive.getParent();
        _CurrentFile.removeFile(pFilename);
        processCreateFile(pParams, parent, false);
        Archive newFile = (Archive) _CurrentFile.getFile(pFilename);
        newFile.setCreationDate(creationDate);
        addStringToTheLog("[->] Archivo: " + pArchive.getName() + " modificado!");
    }
    
    public void showFileContent(Archive pArchive) {
        String result = "";
        result = pArchive.getContent().stream().map((currentContent) -> currentContent.getValue()).reduce(result, String::concat);
        addStringToTheLog("[->] Contenido del archivo: " + pArchive.getName() + "\n" + result);
    }
    
    public boolean moveFile(String pOrigin, String pDestination) {
        boolean result = _CurrentFile.moveFile(pOrigin, pDestination);
        if (!result) {
            File alternate = _CurrentFile.getFile(pOrigin);
            String alternatePath = alternate != null ? alternate.getPath() : pOrigin;
            result = _RootFile.moveFile(alternatePath, pDestination);
        }
        if (result) {
            refreshTreeView();
            addStringToTheLog("[->] El archivo se ha movido exitosamente!");
        }
        else {
            addStringToTheLog("[->] Upss no se pudo mover!"); 
        }
        return result;
    }
    
    public synchronized void removeFile(File pFile) {
        if (pFile instanceof Archive) {
            Archive archive = (Archive) pFile;
            int[] sectors = new int[archive.size()];
            int index = 0;
            for(Entry<Integer, String> current : archive.getContent()) {
                sectors[index] = current.getKey();
                index++;
            }
            _VM.ErraseSector(sectors);
            archive.removeFile(archive.getName()+archive.getExtension());
        }
        else {
            Folder folder = (Folder) pFile;
            if(folder != null) {
                Map<String, File> content = new HashMap<>();
                for (Entry<String, File> current : folder.getContent()) {
                    content.put(current.getKey(), current.getValue());
                }
                for(Entry<String, File> current : content.entrySet()) {
                   removeFile(current.getValue()); 
                }
                System.out.println("NAME " + folder.getName());
                folder.getParent().removeFile(folder.getName());
            }
            
        }
        addStringToTheLog("[->] Se ha removido el archivo: " + pFile.getName());
        refreshTreeView();
    }
    
    /**
     * Creates the file system.
     * @param pSectorSize
     * @param pSectorQuantity 
     */
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
     * @param pCommandDescription
     * @return 
     */
    public boolean processCommand(String[] pCommandDescription) {
        String commandType = pCommandDescription[COMMAND_INDEX].toLowerCase();
        if (!commandType.equals(Commands.COMMAND_CREATE) && _VM == null) {
            addStringToTheLog("[->] Debe inicializar el disco antes de poder ejecutar comandos.");
            return false;
        }
        switch (commandType) {
            case Commands.COMMAND_CREATE:
                return processCreate(pCommandDescription);
            case Commands.COMMAND_FILE:
                return processCreateFile(pCommandDescription, _CurrentFile, true);
            case Commands.COMMAND_MKDIR:
                return processMKDIR(pCommandDescription);
            case Commands.COMMAND_CHANGE_DIR2:
            case Commands.COMMAND_CHANGE_DIR:
                return processChangeDIR(pCommandDescription);
            case Commands.COMMAND_LIST_DIR:
                return processListDIR();
            case Commands.COMMAND_MOD_FILE:
                return processModFile(pCommandDescription);
            case Commands.COMMAND_CONT_FILE:
                return processContFile(pCommandDescription);
            case Commands.COMMAND_FILE_PROPERTIES:
                return processFileProperties(pCommandDescription);
            case Commands.COMMAND_MOVE:
                return processMoveFile(pCommandDescription);
            case Commands.COMMAND_REMOVE:
                return processRemoveFile(pCommandDescription[FIRST_PARAM]);
            case Commands.COMMAND_FIND:
                return processFind(pCommandDescription[FIRST_PARAM]);
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
    
    public boolean processCreateFile(String[] pParams, File pParent, boolean pShow) {
        Pattern pattern = Pattern.compile(FILE_REGEX);
        String content = "";
        int length = pParams.length - 1;
        for(int i = 1; i < length; i++) {
            content += " " + pParams[i];
        }
        String filename = pParams[length];
        if(pattern.matcher(filename).matches()) {
            createFile(pParent, content, filename, pShow);
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
    
    public boolean processModFile(String[] pParams) {
        String filename = pParams[pParams.length - 1];
        File file = _CurrentFile.getFile(filename);
        if (file != null) {
            if (file instanceof Archive) {
                modifyFile(pParams, file, filename);
                return true;
            }
            else {
                addStringToTheLog("[->] El elemento seleccionado no corresponde a un archivo!");
                return false;
            }
        }
        addStringToTheLog("[->] El archivo: " + filename + " no se encuentra "
                + "en el directorio actual. DIR Actual: " + _CurrentFile.getName());
        return false;
    }
    
    public boolean processContFile(String[] pParams) {
        String path = pParams[FIRST_PARAM];
        File searched = _CurrentFile.getFile(path);
        if(searched != null) {
            if (searched instanceof Archive) {
                showFileContent((Archive) searched);
                return true;
            }
            else {
                addStringToTheLog("[->] La ruta indicada no pertenece a un archivo!");
                return false;
            }
        }
        addStringToTheLog("[->] El archivo no existe en la ruta indicada!");
        return false;
    }
    
    public boolean processFileProperties(String[] pParams) {
        String path = pParams[FIRST_PARAM];
        File currentFile = _CurrentFile.getFile(path);
        if(currentFile != null) {
            addStringToTheLog("[->] Propiedades del archivo: " + currentFile.getName() + " :");
            if(currentFile instanceof Archive) {
                addStringToTheLog("[->] Extensión: " + ((Archive)currentFile).getExtension());
            }
            addStringToTheLog("[->] Fecha de creación: " + currentFile.getCreationDate());
            addStringToTheLog("[->] Fecha de modificación: " + currentFile.getLastModification());
            addStringToTheLog("[->] Tamaño: " + currentFile.getSize() + "kb");
            return true;
        }
        addStringToTheLog("[->] El archivo no existe en la ruta indicada!");
        return false;
    }
    
    public boolean processMoveFile(String[] pParams) {
        String origin = pParams[FIRST_PARAM];
        String destination = pParams[SECOND_PARAM];
        boolean result = moveFile(origin, destination);
        return result;
    }
    
    public boolean processListDIR() {
        for(Entry<String, File> current : ((Folder) _CurrentFile).getContent()) {
            File value = current.getValue();
            String name = current.getKey();
            String type = value instanceof Archive ? "DIR" : "FOLDER";
            addStringToTheLog("[->] " + type + ": " + name );
        }
        return true;
    }
    
    public boolean processRemoveFile(String pFilename) {
        File fileToBeRemoved = _CurrentFile.getFile(pFilename);
        System.out.println("FTBR " + fileToBeRemoved.getName());
        removeFile(fileToBeRemoved);
        return false;
    }
    
    public boolean processFind(String filename) {
        String[] paths = ((Folder) _RootFile).find(filename);
        addStringToTheLog("[->] Archivos encontrados con el nombre: " + filename);
        for(String path : paths) {
            addStringToTheLog("\t[->] : " + path);
        }
        return true;
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
