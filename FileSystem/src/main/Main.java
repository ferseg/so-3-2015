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
import model.structure.VirtualMemory;

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
        
        
        Folder root = new Folder(null, "home");
          Folder subFolder = new Folder(root, "Documents");
            Archive archive = new Archive(subFolder, "privado.txt");
            Folder subFolder3 = new Folder(subFolder, "Fotos vacaciones");
              Archive archive2 = new Archive(subFolder3, "foto1.png");
              Archive archive3 = new Archive(subFolder3, "foto2.png");
              Archive archive4 = new Archive(subFolder3, "foto3.png");
                
            Folder subFolder4 = new Folder(subFolder, "Musica");
              Archive archive5 = new Archive(subFolder4, "cancion1.mp3");
              Archive archive6 = new Archive(subFolder4, "cancion2.mp3");
              Archive archive7 = new Archive(subFolder4, "cacion3.mp3");
            Folder subFolder5 = new Folder(subFolder, "Tec");
              Archive archive8 = new Archive(subFolder5, "trabajo1.docx");
              Archive archive9 = new Archive(subFolder5, "trabajo2.docx");
              Archive archive10 = new Archive(subFolder5, "trabajo3.docx");
          Folder subFolder2 = new Folder(root, "Projects");
          Archive archive11 = new Archive(root, "text.txt");
        System.out.println(root.print() + "\n");
        System.out.println(subFolder.print()); 
    }
}
