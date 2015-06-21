/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model.structure;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;

/**
 *
 * VirtualMemory.java
 * @author kennethms64
 * @created June 8, 2015
 * @modified June 8, 2015 
 * @description File manager
 */
public class FileManager{
    
    static final String OPEN_FILE_ERROR = "Error al acceder la memoria virtual :(";
    static final String FILE_ERROR = "Error con la creación de la memoria virtual :(";
    static final String EMPTY_STRING = "";
        
    private File _File;
    
    public FileManager(String pFileName,int pErrase){
        try{
            _File = new File(pFileName); 
            _File.createNewFile();
            if(pErrase == 1){
                FileWriter fw = new FileWriter(_File);
                fw.write(EMPTY_STRING);
                fw.close();
            }
        }
        catch(Exception Ex){
            System.out.println(FILE_ERROR);
        }
    }
    
    public void WriteFile(String pData){
        try{
            FileWriter fw = new FileWriter(_File);
            fw.write(pData);
            fw.close();
        }
        catch(Exception Ex){
            System.out.println(OPEN_FILE_ERROR);
        }
    }
    
    public String ReadFile(){
        try{
            BufferedReader br = new BufferedReader(new FileReader(_File));
            String temp = EMPTY_STRING;
            temp = br.readLine();
            br.close();
            if(temp == null){
                temp = "";
            }
            return temp;
        }
        catch(Exception Ex){
            System.out.println(OPEN_FILE_ERROR);
        }
        return EMPTY_STRING;
    }
}
