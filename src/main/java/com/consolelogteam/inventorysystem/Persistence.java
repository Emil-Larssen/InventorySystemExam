package com.consolelogteam.inventorysystem;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class Persistence {

    public ObservableList<Item> loadListOfItems(){
        try(ObjectInputStream objectInput = new ObjectInputStream((new FileInputStream("SavedItems.ser")))){

            List<Item> savedList = (ArrayList<Item>) objectInput.readObject();

            return FXCollections.observableArrayList(savedList);

        } catch (FileNotFoundException fnfe) {
            throw new RuntimeException("Der blev ikke fundet nogen gemt fil");
        } catch (ClassNotFoundException cnfe){
            throw new RuntimeException("Der blev ikke fundet en item");
        } catch (IOException ioe){
            throw new RuntimeException("Der gik noget galt i forbindelse med at gendanne din inventory");
        }
    }


    public void saveListOfItems(ObservableList<Item> listOfItems){
        try(FileOutputStream fileOutput = new FileOutputStream("SavedItems.ser", false);
            ObjectOutputStream objectOutput = new ObjectOutputStream((fileOutput))){

            objectOutput.writeObject(new ArrayList<Item>(listOfItems));
            objectOutput.flush();

        } catch (FileNotFoundException fnfe){
            throw new RuntimeException("Der blev ikke fundet nogen fil at gemme til");
        } catch (IOException ioe) {
            throw new RuntimeException("Der gik noget galt i forbindelse med at gemme din inventory");
        }
    }



}
