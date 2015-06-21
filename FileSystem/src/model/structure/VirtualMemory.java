/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model.structure;

import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * VirtualMemory.java
 * @author kennethms64
 * @created June 8, 2015
 * @modified June 8, 2015
 * @description virtual memory manager
 */
public class VirtualMemory {
    static final String FILE_NAME = "Memoria Virtual.txt";
    static final String END_OF_LINE = "\n";
    static final String EMPTY_STRING = "";
    static final char EMPTY_BYTE = '0';
    static final char EMPTY_SPACE = ' ';
    static final char NULL_BYTE = '\0';
    static final int INVALID_VALUE = -1;
    static final int EMPTY = 0;
    static final int BUSSY = 1;
    static final int ERRASE = 1;
    static final int DONT_ERRASE = 0;
    
    private int _Size;
    private int _Sectors;
    private int _FreeSectors;
    private int _SectorsState[];
    private FileManager _FileManager;
    
    public VirtualMemory(int pSize, int pSectors){
        _Size = pSize;
        _Sectors = _FreeSectors = pSectors;
        _SectorsState = new int[pSectors];
        _FileManager = new FileManager(FILE_NAME,ERRASE);
        String line = new String(new char[_Size]).replace(NULL_BYTE, EMPTY_BYTE);
        String temp = EMPTY_STRING;
        for(int i=0;i<pSectors;i++)
            temp += line;
        _FileManager.WriteFile(temp);
    }
    
    public Map<Integer, String> WriteSector(String pData){
        int size = 0;
        int sectors[] = new int[_Sectors];
        Map result = new HashMap();
        if(_Size * _FreeSectors >= pData.length()){
            int sectorIndex = 0;
            int storedIndex = 0;
            do{
                if(_SectorsState[sectorIndex] == EMPTY){
                    String temp = getStart(pData);
                    pData = getFinish(pData);
                    String fileContent[] = SplitString(_FileManager.ReadFile());
                    fileContent[sectorIndex] = temp;
                    String content = JoinString(fileContent);
                    _FileManager.WriteFile(content);
                    _SectorsState[sectorIndex] = BUSSY;
                    _FreeSectors--;
                    result.put(sectorIndex, content);
                    storedIndex++;
                }
                sectorIndex++;
            } while(!pData.equals(EMPTY_STRING));
            size = storedIndex;
        }
           
        return result;
    }
    
    public void ErraseSector(int pSectors[]){
        int current;
        String fileContent[] = SplitString(_FileManager.ReadFile());
        String line = new String(new char[_Size]).replace(NULL_BYTE, EMPTY_BYTE);
        for(int sectorIndex=0;sectorIndex<pSectors.length;sectorIndex++){
            current = pSectors[sectorIndex];
            fileContent[current] = line;
            _SectorsState[current] = EMPTY;
            _FreeSectors++;
        }
        _FileManager.WriteFile(JoinString(fileContent));
    }
    
    public Map<Integer, String> ReplaceSector(String pData,int pSectors[]){
        int freeSpace = _FreeSectors * _Size;
        int usedSpace = pSectors.length * _Size;
        if(freeSpace + usedSpace >= pData.length()){
            ErraseSector(pSectors);
            return WriteSector(pData);
        }
        else{
            return null;
        }
    }
    
    public void SectorsToFile(int pSectors[],String pFile){
        String content = EMPTY_STRING;
        String fileContent[] = SplitString(_FileManager.ReadFile());
        for(int i= 0;i < pSectors.length;i++){
            content += fileContent[pSectors[i]];
        }
        FileManager fileManager = new FileManager(pFile,ERRASE);
        fileManager.WriteFile(content);
    }
    
    public Map<Integer, String> FileToSectors(String pFile){
        FileManager fileManager = new FileManager(pFile,DONT_ERRASE);
        String content = fileManager.ReadFile();
        return WriteSector(content);
    }
    
    public String toString(){
        String fileContent[] = SplitString(_FileManager.ReadFile());
        String temp = EMPTY_STRING;
        int sectorIndex = 0;
        while(sectorIndex<_Sectors){
            temp += fileContent[sectorIndex]+END_OF_LINE;
            sectorIndex++;
        }
        return temp;
    }
    
    private String[] SplitString(String pData){
        String temp[] = new String[_Sectors];
        int sectorIndex = 0;
        while(sectorIndex<_Sectors){
            temp[sectorIndex] = pData.substring(0, _Size);
            pData = pData.substring(_Size);
            sectorIndex++;
        }
        return temp;
    }
    
    private String JoinString(String pData[]){
        String temp = EMPTY_STRING;
        int sectorIndex = 0;
        while(sectorIndex<_Sectors){
            temp += pData[sectorIndex];
            sectorIndex++;
        }
        return temp;
    }
    
    private String getStart(String pData){
        String temp = EMPTY_STRING;
        if(pData.length()>_Size){
            temp = pData.substring(0, _Size);
        }
        else{
            temp += pData;
            while(temp.length()<_Size)
                temp += EMPTY_SPACE;
        }
        return temp;
    }
    
    private String getFinish(String pData){
        String temp = EMPTY_STRING;
        if(pData.length()>_Size){
            temp = pData.substring(_Size);
        }
        return temp;
    }
    
    private int[] getSectors(int pSectors[], int pSize){
        int temp[] = new int[pSize];
        if(pSize == 0){
            temp = new int[]{INVALID_VALUE};
        }
        else{
            temp = new int[pSize];
            for(int sectorIndex = 0 ; sectorIndex<pSize; sectorIndex++){
                temp[sectorIndex] = pSectors[sectorIndex];
            }
        }
        return temp;
    }
}

