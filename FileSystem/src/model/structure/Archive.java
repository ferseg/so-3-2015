/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package model.structure;

import java.util.Map.Entry;
import java.util.Set;

/**
 *
 * Archive.java
 * @author fsegovia
 * @created May 30, 2015
 * @modified May 30, 2015
 * @description File type archive
 */
public class Archive extends File<Integer, String> {
    
    private String _Extension;
    
    public Archive(File pParent, String pFilename) {
        super(pParent, pFilename);
        _Extension = ".def";
        initParent(pFilename);
    }
    
    public Archive(File pParent, String pFilename, String pExtension) {
        super(pParent, pFilename);
        _Extension = pExtension;
        initParent(pFilename);
    }
    
    public boolean addLine(int pLineNumber, String pContent) {
        if(!containsKey(pLineNumber)) {
            put(pLineNumber, pContent);
            updateSize();
            return true;
        }
        return false;
    }
    
    public Set<Entry<Integer, String>> getContent() {
        return entrySet();
    }

    @Override
    protected int calculateSize() {
        int size = 0;
        size = entrySet().stream().map((entry) -> entry.getValue().length()).reduce(size, Integer::sum);
        return size;
    }

    @Override
    public File removeFile(String pName) {
        return _Parent.removeFile(pName);
    }

    @Override
    public boolean renameFile(File pFile, String pNewName) {
       return pFile._Parent.renameFile(pFile, pNewName);
    }
    
    public boolean renameFile(String pNewName) {
        return _Parent.renameFile(this, pNewName);
    }

    public String getExtension() {
        return _Extension;
    }

    private void initParent(String pFilename) {
        if(_Parent != null) {
            _Parent.put(pFilename, this);
        }
    }

}
