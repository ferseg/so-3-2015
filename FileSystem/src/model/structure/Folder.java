/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model.structure;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * Folder.java
 *
 * @author fsegovia
 * @created May 30, 2015
 * @modified May 30, 2015
 * @description Class that
 */
public class Folder extends File<Map<String, File>> {

    public Folder(File pParent, String pName) {
        super(pParent, pName);
        _Content = new HashMap<>();
    }

    /**
     * Adds a file to a folder
     *
     * @param pFile
     * @return true if the files is added, false otherwise
     */
    public boolean addFile(File pFile) {
        if (!_Content.containsKey(pFile._Name)) {
            _Content.put(pFile._Name, pFile);
            updateSize();
            return true;
        }
        return false;
    }

    @Override
    protected int calculateSize() {
        return _Content.size();
    }

    @Override
    public File removeFile(String pFilename) {
        if(_Content.containsKey(pFilename)) {
            File file = _Content.remove(pFilename);
            updateSize();
            return file;
        }
        return null;
    }

    @Override
    public boolean renameFile(File pFile, String pNewName) {
        File file = removeFile(pFile._Name);
        if(file != null) {
            file._Name = pNewName;
            addFile(file);
            return true;
        }
        return false;
    }

}
