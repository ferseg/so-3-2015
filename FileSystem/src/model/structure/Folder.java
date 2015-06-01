/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model.structure;

/**
 *
 * Folder.java
 *
 * @author fsegovia
 * @created May 30, 2015
 * @modified May 30, 2015
 * @description Class that
 */
public class Folder extends File<String, File> {

    public Folder(File pParent, String pName) {
        super(pParent, pName);
        if(_Parent != null) {
            _Parent.put(pName, this);
        }
    }

    /**
     * Adds a file to a folder
     *
     * @param pFile
     * @return true if the files is added, false otherwise
     */
    public boolean addFile(File pFile) {
        if (!containsKey(pFile._Name)) {
            pFile._Parent = this;
            put(pFile._Name, pFile);
            updateSize();
            return true;
        }
        return false;
    }

    @Override
    protected int calculateSize() {
        return size();
    }

    @Override
    public File removeFile(String pFilename) {
        if(containsKey(pFilename)) {
            File file = remove(pFilename);
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
