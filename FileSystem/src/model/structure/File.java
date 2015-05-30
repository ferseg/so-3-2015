/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model.structure;

/**
 *
 * @author fsegovia
 * @since 30/05/2015
 */
public abstract class File<T> {

    protected File _Parent;
    protected String _Name;
    protected int _Size;
    protected T _Content;

    public File(File pParent, String pName) {
        _Parent = pParent;
        _Name = pName;
        _Size = 0;
    }

    /**
     * Determines the size of the file
     *
     * @return The size
     */
    protected abstract int calculateSize();

    /**
     * Removes a file
     *
     * @param pName
     * @return
     */
    public abstract File removeFile(String pName); // Could be changed
    
    public abstract boolean renameFile(File pFile, String pNewName);

    /**
     * Updates the size of the file
     */
    protected void updateSize() {
        _Size = calculateSize();
    }

    /**
     * Gets the path of this file
     *
     * @return
     */
    public String getPath() {
        return getPath(this);
    }

    /**
     * Gets the path of a file
     *
     * @param pFile
     * @return
     */
    public static String getPath(File pFile) {
        String path = pFile._Name;
        File actual;
        while ((actual = pFile._Parent) != null) {
            path = actual._Name + "/" + path;
        }
        return path;
    }

}
