/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model.structure;

import java.util.HashMap;
import java.util.Set;

/**
 *
 * @author fsegovia
 * @param <K> The key
 * @param <V> The value
 * @since 30/05/2015
 */
public abstract class File<K, V> extends HashMap<K, V> {

    private final static String FILE_DIVIDER = "/";
    private final static int FIRST_ARRAY_ELEMENT = 0;
    protected File _Parent;
    protected String _Name;
    protected int _Size;

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

    /**
     *
     * @param pFile
     * @param pNewName
     * @return
     */
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

    public int getSize() {
        return _Size;
    }

    public Set<Entry<K, V>> getContent() {
        return entrySet();
    }

    /**
     * Gets the path of a file
     *
     * @param pFile
     * @return
     */
    public static String getPath(File pFile) {
        String path = pFile._Name;
        File actual = pFile;
        while ((actual = actual._Parent) != null) {
            path = actual._Name + FILE_DIVIDER + path;
        }
        return path;
    }

    /**
     * Gets a file depending on the path and the root folder.
     *
     * @param pPath
     * @return
     */
    public File getFile(String pPath) {
        String routes[] = pPath.split(FILE_DIVIDER);
        File actualFile = this;
        for (String actualRoute : routes) {
            actualFile = (File) actualFile.get(actualRoute);
            if (actualFile == null) {
                break;
            }
        }
        return actualFile;
    }

    public boolean moveFile(String pOldPath, String pNewPath) {
        File fileToBeMoved = getFile(pOldPath);
        File destination = getFile(pNewPath);
        if (fileToBeMoved == null || destination != null 
                || fileToBeMoved._Parent == null) {
            return false;
        }
        fileToBeMoved._Parent.removeFile(fileToBeMoved._Name);
        return insertFileInPath(fileToBeMoved, pNewPath);
    }

    /**
     * Inserts a file into a path
     *
     * @param pFile The file to be inserted
     * @param pPath The path in which the file will be inserted
     * @return true if it's inserted, false otherwise.
     */
    public boolean insertFileInPath(File pFile, String pPath) {
        int pivot = pPath.lastIndexOf("/");
        String path = pPath.substring(0, pivot);
        String filename = pPath.substring(pivot + 1);
        File parentFile = getFile(path);
        // The file cannot be inserted if the parent is null or the parent
        // is an archive
        if (parentFile == null || parentFile instanceof Archive) {
            return false;
        }
        pFile._Name = filename;
        ((Folder) parentFile).addFile(pFile);
        return true;
    }

}
