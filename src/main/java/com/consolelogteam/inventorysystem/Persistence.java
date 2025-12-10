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

            //TODO Exceptions are not properly caught TO-DO for later Exception handling
        } catch (FileNotFoundException fnfe) {
            throw new RuntimeException("No saved file was found");
        } catch (ClassNotFoundException cnfe){
            throw new RuntimeException("No class of that type was found");
        } catch (IOException ioe){
            throw new RuntimeException("Something went wrong during loading the file of the inventory");
        }
    }


    public void saveListOfItems(ObservableList<Item> listOfItems){
        try(FileOutputStream fileOutput = new FileOutputStream("SavedItems.ser", false);
            ObjectOutputStream objectOutput = new ObjectOutputStream((fileOutput))){

            objectOutput.writeObject(new ArrayList<Item>(listOfItems));
            objectOutput.flush();

            //TODO Exceptions are not properly caught TO-DO for later Exception handling
        } catch (FileNotFoundException fnfe){
            throw new RuntimeException("No file was found to save to");
        } catch (IOException ioe) {
            throw new RuntimeException("Something went wrong during saving the inventory");
        }
    }

}
