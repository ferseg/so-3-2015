/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package model.structure;

/**
 *
 * Commands.java
 * @author fsegovia
 * @created Jun 20, 2015
 * @modified Jun 20, 2015
 * @description Contains the commands constants
 */
public final class Commands {
    
    public static final String VALID_COMMAND_REGEX = "[a-z][ a-zA-Z0-9./\\-_\\*]*";
    
    public static final String COMMAND_CREATE = "create";
    public static final String COMMAND_FILE = "file";
    public static final String COMMAND_MKDIR = "mkdir";
    public static final String COMMAND_CHANGE_DIR = "cambiardir";
    public static final String COMMAND_CHANGE_DIR2 = "cd";
    public static final String COMMAND_LIST_DIR = "listardir";
    public static final String COMMAND_MOD_FILE = "modfile";
    public static final String COMMAND_CONT_FILE = "contfile";
    public static final String COMMAND_FILE_PROPERTIES = "verpropiedades";
    public static final String COMMAND_MOVE = "mover";
    public static final String COMMAND_COPY_V_V = "cpvv";
    public static final String COMMAND_COPY_V_C = "cpvc";
    public static final String COMMAND_COPY_C_V = "cpcv";
    public static final String COMMAND_REMOVE = "remove";
    public static final String COMMAND_FIND = "find";

}
