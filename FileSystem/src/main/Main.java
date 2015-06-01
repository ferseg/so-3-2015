/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main;

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
        archive = (Archive) folder.getFile("IS/Projects/privado.txt");
        String path = archive.getPath();
        System.out.println(path);
        archive = new Archive(subFolder2, "archivito");
        archive = new Archive(subFolder2, "archivito2");
        subFolder = new Folder(subFolder2, "Private");
        subFolder = new Folder(subFolder, "Mine");
        archive = new Archive(subFolder, "Some.txt");
        Folder res = (Folder) folder.getFile("IS/Projects");
        boolean result = folder.moveFile("IS/Projects/archivito2", "IS/Projects/RenamedArchivito");
        System.out.println("Result: " + result);
        Archive ar = (Archive) folder.getFile("IS/Projects/RenamedArchivito");
        System.out.println("Archive path: " + ar.getPath());
        System.out.println("-->" + res.getPath());
        for(Entry<String, File> actual : res.getContent()) {
            String name = actual.getKey();
            File value = actual.getValue();
            if(value instanceof Folder) {
                System.out.print("Folder ");
            }
            System.out.println("Name : " + name + ", content: " + value);
        }
        
    }
    
}
