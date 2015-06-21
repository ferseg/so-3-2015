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
    static String s_Start;
    static final char EMPTY_SPACE = ' ';
    static final char NULL_SPACE = '\0';
    static final char END_OF_LINE = '\n';
    static final char SUB_DIR = '└';
    
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
    
    public String print(){
        s_Start = _Name + END_OF_LINE;
        print(0);
        return s_Start;
    }
    
    public void print(int pSpace){
        String space = new String(new char[pSpace]).replace(NULL_SPACE, EMPTY_SPACE);
        for(Entry<String, File> actual : getContent()){
            String fileName = actual.getKey();
            try{
                Folder content = (Folder)actual.getValue();
                s_Start += space + SUB_DIR + fileName + END_OF_LINE;
                content.print(pSpace+1);
            }
            catch(Exception Ex){
                s_Start += space + SUB_DIR + fileName + END_OF_LINE;
            }
        }
    }
}
