package com.consolelogteam.inventorysystem.dal;

import com.consolelogteam.inventorysystem.logik.Item;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class Persistence {

    /** Loading and saving of Inventory List */

    //Gendanner indholdet af Inventory fra fil
    public ObservableList<Item> loadListOfItems(){

        //Prøver med resourcer i form af et objekt af "ObjectInputStream",
        // der bruger et objekt af "FileInputStream" som argument til oprettelse.
        //"FileReader" bruger selv en String som argument til dens oprettelse, som er filens pathway/sti.
        try(ObjectInputStream objectInput = new ObjectInputStream((new FileInputStream("SavedItems.ser")))){

            //En 'arraylist' laves med dataen fra den læste fil
            List<Item> savedList = (ArrayList<Item>) objectInput.readObject();

            //Dataen fra den ovenstående 'arraylist' bruges til at lave en 'observableList' med samme objekter
            //Denne 'observableList' returneres derefter
            return FXCollections.observableArrayList(savedList);

            //Fanger en række "Exception(s)" og kaster en "RuntimeException" som opsnappes i GUI Controller.
        } catch (FileNotFoundException fnfe) {
            throw new RuntimeException("Der blev ikke fundet nogen gemt fil til inventory");
        } catch (ClassNotFoundException cnfe){
            throw new RuntimeException("Der blev ikke fundet en item");
        } catch (IOException ioe){
            throw new RuntimeException("Der gik noget galt i forbindelse med at gendanne din inventory");
        }
    }

    //Gemmer indholdet af inventory til fil
    public void saveListOfItems(ObservableList<Item> listOfItems){

        //Prøver med resourcer i form af et objekt af "ObjectOutputStream",
        // der bruger et objekt af "FileOutputStream" som argument til oprettelse.
        //"FileReader" bruger selv en String som argument til dens oprettelse, som er filens pathway/sti,
        // samt en boolean der er sat til false.
        //false betyder i denne kontekst at filen overskrives rettere end at indhold tilføjes.
        //Her er try-with-resources delt på 2 linjer, men kode-mæssigt er det ens med at gøre det på 1.
        try(FileOutputStream fileOutput = new FileOutputStream("SavedItems.ser", false);
            ObjectOutputStream objectOutput = new ObjectOutputStream((fileOutput))){

            //Dataen fra listOfItems lægges over i en ny lavet 'arrayList' som skrives/gemmes til filen
            //Man gemmer et 'arrayList', da en 'observableList' ikke kan gemmes på denne måde.
            objectOutput.writeObject(new ArrayList<Item>(listOfItems));

            //Dette betyder bare at ændringen skal ske med det samme i stedet for at den ligger i bufferen.
            objectOutput.flush();

            //Fanger en række "Exception(s)" og kaster en "RuntimeException" som opsnappes i GUI Controller.
        } catch (FileNotFoundException fnfe){
            throw new RuntimeException("Der blev ikke fundet nogen fil at gemme til");
        } catch (IOException ioe) {
            throw new RuntimeException("Der gik noget galt i forbindelse med at gemme din inventory");
        }
    }


    /** Loading and Saving of Inventory Slots */

    //Gendanner mængden af inventory slots/pladser,
    // og kaster en række fejl som fanges i "InventoryManager",
    // sådan at der i forbindelse med fejl bliver udregnet det nødvendige antal slots.
    public int loadAmountOfInventorySlots() throws  FileNotFoundException, IOException, NumberFormatException{

        //Prøver med resourcer i form af et objekt af "BufferedReader",
        // der bruger et objekt af "FileReader" som argument til oprettelse.
        //"FileReader" bruger selv en String som argument til dens oprettelse, som er filens pathway/sti.
        try (BufferedReader reader = new BufferedReader(new FileReader("InventorySlotsAmount.txt"))){

            //En lokal String bliver lig med indholdet af filen
            String line = reader.readLine();

            //Returnere tekststykket der er konverteret til en Integer
            return Integer.parseInt(line);
        }
    }

    //Gemmer mængden af inventory slots/pladser,
    public void saveAmountOfInventorySlots(int amountOfInventorySlots){

        //Prøver med resourcer i form af et objekt af "BufferedWriter",
        // der bruger et objekt af "FileWriter" som argument til oprettelse.
        //"FileReader" bruger selv en String som argument til dens oprettelse( som er filens pathway/sti),
        // samt en boolean der er sat til false.
        //false betyder i denne kontekst at filen overskrives rettere end at indhold tilføjes.
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("InventorySlotsAmount.txt", false))){

            //Den modtagede Integer konverteres til en String og skrives til filen
            writer.write(Integer.toString(amountOfInventorySlots));

            //Dette betyder bare at ændringen skal ske med det samme i stedet for at den ligger i bufferen.
            writer.flush();

            //Fanger "IOException" og kaster en "RuntimeException" som opsnappes i GUI Controller.
        } catch (IOException ioe){
            throw new RuntimeException("Der gik noget galt i forbindelse med at gemme optjente inventory pladser");
        }
    }
}
