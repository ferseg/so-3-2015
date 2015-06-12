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
        VirtualMemory disco = new VirtualMemory(5,5);
        System.out.println(disco.toString());
        disco.WriteSector("");
        System.out.println(disco.toString());
        disco.WriteSector("aaa");
        System.out.println(disco.toString());
        disco.WriteSector("hola mundo");
        System.out.println(disco.toString());
        disco.ErraseSector(new int[]{2});
        System.out.println(disco.toString());
        disco.WriteSector("asd_main");
        System.out.println(disco.toString());
        
        /*VirtualMemory disco = new VirtualMemory(5,5);
        System.out.println(disco.toString());
        disco.WriteSector("hola mundo");
        System.out.println(disco.toString());
        disco.ErraseSector(new int[]{1});
        System.out.println(disco.toString());
        disco.ReplaceSector("esto es una prueba", new int[]{0});
        System.out.println(disco.toString());*/
        
        
    }
}
