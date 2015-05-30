/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package model.structure;

import java.util.Map;

/**
 *
 * Archive.java
 * @author fsegovia
 * @created May 30, 2015
 * @modified May 30, 2015
 * @description File type archive
 */
public class Archive extends File<Map<Integer, String>> {

    public Archive(File pParent, String pFilename) {
        super(pParent, pFilename);
        
    }
    
    public boolean addLine(String pContent) {
        return true;
    }

    @Override
    protected int calculateSize() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public File removeFile(String pName) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean renameFile(File pFile, String pNewName) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
