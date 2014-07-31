/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.RatView.main;

import java.io.*;
import java.util.ArrayList;
import java.util.logging.*;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;

/**
 *
 * @author Simon
 */
public class SaveController implements Serializable {

    public SaveController() {
    }

    public void save(SaveFile save) {
        FileOutputStream fos = null;
        try {
            JFileChooser chooser = new JFileChooser();
            FileNameExtensionFilter filter = new FileNameExtensionFilter(
                    "Save Files", "arvs");
            chooser.setFileFilter(filter);
            chooser.showSaveDialog(null);
            File file = chooser.getSelectedFile();
            
            fos = new FileOutputStream(file.getAbsoluteFile() + ".arvs");
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(save);
            oos.close();
        } catch (FileNotFoundException ex) {

            Logger.getLogger(SaveController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(SaveController.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                fos.close();
            } catch (IOException ex) {
                Logger.getLogger(SaveController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public SaveFile load() {
        FileInputStream fis = null;
        try {
            
            JFileChooser chooser = new JFileChooser();
            FileNameExtensionFilter filter = new FileNameExtensionFilter(
                    "Save Files", "arvs");
            chooser.setFileFilter(filter);
            chooser.showOpenDialog(null);
            File file = chooser.getSelectedFile();
            
            fis = new FileInputStream(file);
            ObjectInputStream ois = new ObjectInputStream(fis);
            SaveFile out = (SaveFile) ois.readObject();
            ois.close();
            return out;
        } catch (FileNotFoundException ex) {
            Logger.getLogger(SaveController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(SaveController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(SaveController.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                fis.close();
            } catch (IOException ex) {
                Logger.getLogger(SaveController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return null;
    }

//    public void save() {
//        try {
//            BufferedWriter out = new BufferedWriter(new FileWriter("./files/save.arvs"));
//
//            for (Project p : projects) {
//                out.write("$\n");
//                out.write(p.toString() + "\n");
//                for (Rat r : p.getRats()) {
//
//                }
//            }
//
//            out.close();
//        } catch (Exception e) {//Catch exception if any
//            System.err.println("Error in writing data: " + e.getMessage());
//        }
//    }
}
