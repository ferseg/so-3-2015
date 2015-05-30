/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main;

import java.util.Arrays;
import java.util.Map.Entry;
import model.structure.Archive;
import model.structure.File;
import model.structure.Folder;

/**
 *
 * @author fsegovia
 * @version 0.0.1
 */
public class Main {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        
        Folder folder = new Folder(null, "Documentos");
        Folder subFolder = new Folder(folder, "IS");
        Folder subFolder2 = new Folder(subFolder, "Projects");
        Archive archive = new Archive(subFolder2, "privado.txt");
        archive.addLine(1, "Hello");
        archive.addLine(5, "Wold!");
        File result = folder.getFile("IS/Projects/privado.txt");
        String path = result.getPath();
        System.out.println(path);
        for(Entry<Integer, String> actual : archive.getContent()) {
            System.out.println("Sector : " + actual.getKey() + ", content: " + actual.getValue());
        }
    }
    
}
