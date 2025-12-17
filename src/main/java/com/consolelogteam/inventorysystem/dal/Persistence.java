package com.consolelogteam.inventorysystem.dal;

import com.consolelogteam.inventorysystem.logik.Item;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class Persistence {

    /** Loading and saving of Inventory List */

    public ObservableList<Item> loadListOfItems(){
        try(ObjectInputStream objectInput = new ObjectInputStream((new FileInputStream("SavedItems.ser")))){

            List<Item> savedList = (ArrayList<Item>) objectInput.readObject();

            return FXCollections.observableArrayList(savedList);

        } catch (FileNotFoundException fnfe) {
            throw new RuntimeException("Der blev ikke fundet nogen gemt fil til inventory");
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


    /** Loading and Saving of Inventory Slots */

    public int loadAmountOfInventorySlots() throws  FileNotFoundException, IOException, NumberFormatException{
        try (BufferedReader reader = new BufferedReader(new FileReader("InventorySlotsAmount.txt"))){
            String line = reader.readLine();
            return Integer.parseInt(line);
        }
    }

    public void saveAmountOfInventorySlots(int amountOfInventorySlots){
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("InventorySlotsAmount.txt", false))){
            writer.write(Integer.toString(amountOfInventorySlots));
            writer.flush();
        } catch (IOException ioe){
            throw new RuntimeException("Der gik noget galt i forbindelse med at gemme optjente inventory pladser");
        }
    }
}
