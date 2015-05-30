/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model.structure;

import java.util.HashMap;

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
        File actualFile = (File) get((K) routes[FIRST_ARRAY_ELEMENT]);
        for (int index = 1; index < routes.length; index++) {
            String actualRoute = routes[index];
            actualFile = (File) actualFile.get(actualRoute);
        }

        return actualFile;
    }

}
