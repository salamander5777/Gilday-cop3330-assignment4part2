package ucf.assignments;

/*
 *  UCF COP3330 Fall 2021 Assignment 4 Solution
 *  Copyright 2021 Michael Gilday
 */

import java.io.*;
import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;
import java.util.Scanner;

import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.stage.FileChooser;

public class GuiController implements Initializable {
    @Override
    //Sets up cell factory for each column, the ability to edit and what occurs during commit, and starts up menu options.
    public void initialize(URL location, ResourceBundle resources) {
        //Blocks of code are used to set up the cell value factory and cell factory for the columns 'Task', 'Due Date',
        //'Task Description', and 'Completion' of the TableView 'tableViewer'. Each column will also have it's 'EditCommit'
        //established directly after the cell factories are established. The items from an ObservableList are set to
        //the TableView 'tableViewer', allowing the table to become populated. A method called 'menuOptions()' will need
        //to be called in order to pre-emptively set up the ActionEvents for 'Save...' and 'Open...'.
        taskCol.setCellValueFactory(taskEData -> taskEData.getValue().taskEntry);
        taskCol.setCellFactory(TextFieldTableCell.forTableColumn());
        taskCol.setOnEditCommit(event -> {
            toDo currentColumn = event.getRowValue();
            currentColumn.setTask(event.getNewValue());
        });

        dueDateCol.setCellValueFactory(dateEData -> dateEData.getValue().dateEntry);
        dueDateCol.setCellFactory(TextFieldTableCell.forTableColumn());
        dueDateCol.setOnEditCommit(event -> {
            toDo currentColumn = event.getRowValue();
            currentColumn.setDate(event.getNewValue());
        });

        taskDescCol.setCellValueFactory(descEData -> descEData.getValue().descriptionEntry);
        taskDescCol.setCellFactory(TextFieldTableCell.forTableColumn());
        taskDescCol.setOnEditCommit(event -> {
            toDo currentColumn = event.getRowValue();
            currentColumn.setDescription(event.getNewValue());
        });

        taskFinished.setCellValueFactory(new PropertyValueFactory<>("verifyCheck"));
        tableViewer.setItems(data); //Used to populate tableview with gathered input.
        menuOptions();
    }

    //Establishing connection to various items in the FXML file.
    @FXML
    public TextField taskEntry;
    @FXML
    public TextField dateEntry;
    @FXML
    public TextField taskDescription;
    @FXML
    public CheckBox checkComplete;
    @FXML
    public CheckBox checkIncomplete;

    @FXML
    public TableColumn<toDo, String> taskCol;
    @FXML
    public TableColumn<toDo, String> dueDateCol;
    @FXML
    public TableColumn<toDo, String> taskDescCol;
    @FXML
    public TableColumn<toDo, String> taskFinished;

    @FXML
    public MenuItem open;
    @FXML
    public MenuItem save;
    @FXML
    public MenuItem exit;

    @FXML
    public TableView<toDo> tableViewer;
    public final ObservableList<toDo> data = FXCollections.observableArrayList();

    @FXML
    //Adds information from the entry fields to the ObservableList 'data', which is set to the TableView 'tableViewer'.
    public void addTask(){
        //An if statement is used to verify whether the three entry fields in the program have text in them, and to deny
        //pushing the entries to the ObservableList 'data' if not all the fields are filled. If they are then push, then
        //call a function to refresh the entry fields.
        if(taskEntry.getText().length() != 0 &&  dateEntry.getText().length() != 0 && taskDescription.getText().length() != 0){
            data.add(new toDo(taskEntry.getText(), dateEntry.getText(), taskDescription.getText()));
            refresh();
        }
    }

    @FXML
    public void refresh(){ //Refreshes entry fields so that text may be re-input more efficiently.
        //Calls the three entry fields and uses the function '.clear()' to empty the fields.
        taskEntry.clear();
        dateEntry.clear();
        taskDescription.clear();
    }

    @FXML
    public void deleteTask(){//This method is used to delete the selected row from the content table.
        //Creation of two ObservableList, one that will hold the objects gotten from the main TableView and another which will
        //gather the specific object from a specified row in the TableView. The selected objects will then be removed through
        //the use of 'forEach(---::remove)'.
        ObservableList<toDo> removeSelected, allObjects;
        allObjects = tableViewer.getItems();
        removeSelected = tableViewer.getSelectionModel().getSelectedItems();

        removeSelected.forEach(allObjects::remove);
    }

    @FXML
    public void deleteList(){//This method is used to completely erase the items found in the table.
        //Get the items from the TableView and remove them through the use of '.clear()'.
        tableViewer.getItems().clear();
    }

    @FXML
    public void showComplete(){//This method is used to show only completed objects found in the TableView.
        //Creation of an if statement to check if a specific CheckBox linked to display control of 'Complete Task' is checked.
        //If the box is checked then an ObservableList will be created that will be filled in a for loop directly below.
        //The for loop will iterate through the main ObservableList matched to the TableView, if the CheckBox of a column for
        //completion is checked then add the item to the new ObservableList. Once the for loop is done then set the items of the new
        //ObservableList to the TableView; Unchecking the Checkbox for 'Incomplete Task' will reset the TableView to normal.
        if(checkComplete.isSelected()){
            ObservableList<toDo> showSelected = FXCollections.observableArrayList();

            for (toDo task : data){
                if(task.getVerifyCheck().isSelected()){
                    showSelected.add(task);
                }
            }
            tableViewer.setItems(showSelected);
        }
        else{
            tableViewer.setItems(data);
        }
    }

    @FXML
    public void showIncomplete(){//This method is used to show only show incomplete objects found in the TableView.
        //Creation of an if statement to check if a specific CheckBox linked to display control of 'Incomplete Task' is checked.
        //If the box is checked then an ObservableList will be created that will be filled in a for loop directly below.
        //The for loop will iterate through the main ObservableList matched to the TableView, if the CheckBox of a column for
        //completion is unchecked then add the item to the new ObservableList. Once the for loop is done then set the items of the new
        //ObservableList to the TableView; Unchecking the Checkbox for 'Incomplete Task' will reset the TableView to normal.
        if(checkIncomplete.isSelected()){
            ObservableList<toDo> showUnselected = FXCollections.observableArrayList();

            for (toDo task : data){
                if(!task.getVerifyCheck().isSelected()){
                    showUnselected.add(task);
                }
            }
            tableViewer.setItems(showUnselected);
        }
        else{
            tableViewer.setItems(data);
        }
    }

    public void menuOptions(){ //This method is used to handle when a user clicks on a menu item.
        //Creation of a FileChooser named 'fileC' which will be used for the setOnAction of 'save' and 'open', then
        //'setInitialFileName' for a generic '.txt' file and then set the ExtensionFilters to '.csv' so that the 'save'
        //option may only save as a '.scv' file.
        //Creation of individual 'setOnAction's for each menu option (exit, save, and open). The setOnAction for exit will use
        //Platform.exit() in order to close the program without saving. The setOnAction for 'save' will create a new File
        //matched to the FileChooser.showSaveDialog, once open and chosen an if statement will be used to verify whether
        //the new File is null, if it isn't then a try/catch will be used, attempting to call the method related to 'save'.
        //The setOnAction for 'open' will create a new File matched to FileChooser.showOpenDialog, once open and chosen an if
        //statement will be used to verify whether the new File is null, if it isn't then a try/catch will be used, attempting
        //to call the method related to 'open'.

        final FileChooser fileC = new FileChooser();
        fileC.setInitialFileName("file.csv");
        fileC.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("CSV file", "*.csv*"));

        //Establishing what is used when 'Exit' is clicked in the menu.
        exit.setOnAction(e -> Platform.exit());

        //Establishing what is used when 'Save...' is clicked in the menu.
        save.setOnAction(e -> {
            File fileSaver = fileC.showSaveDialog(null);
            if (fileSaver != null) {
                try {
                    saveFile(fileSaver);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });

        //Establishing what is used when 'Open...' is clicked in the menu.
        open.setOnAction(e -> {
            File fileOpener = fileC.showOpenDialog(null);
            if (fileOpener != null) {
                try {
                    openFile(fileOpener);
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        });
    }

    public void saveFile(File savesFile) throws Exception {
        //Creation of a new Writer that will be set to a new BufferedWriter, matched to the save file, that is found in a
        //'try'. A 'for' loop will then be used to iterate through the collected data in an ObservableList, separating the data
        //found on each row through the use of commas and a new line to separate each row.
        Writer bw = null;
        try {
            bw = new BufferedWriter(new FileWriter(savesFile));
            for (toDo task : data) {

                String currentCSV = task.getTask() + ";,;" + task.getDate() + ";,;" + task.getDescription() + ";,;" + task.getVerifyCheck().isSelected() + "\n";

                bw.write(currentCSV);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        finally {
            assert bw != null;
            bw.flush();
            bw.close();
        }
    }

    public void openFile(File opensFile) throws IOException {
        //Use of a 'try' for a new Scanner set equal to a Scanner for the open file. While the file has new lines then the code
        //block will loop while appending text to the fields linked to entry for 'task', 'date', and 'description' which will
        //then be pushed into the ObservableList through the use of a method whose purpose is adding the entry from the fields
        //to the ObservableList. The loop will also verify whether the fourth entry on each line is 'true' or 'false' which
        //will cause a CheckBox found on the specified row to become ticked if 'true'.
        try(Scanner scanner = new Scanner(opensFile)) {
            scanner.useDelimiter(";,;|\\n");
            int i = 0;
            while(scanner.hasNext()){
                taskEntry.appendText(scanner.next());
                dateEntry.appendText(scanner.next());
                taskDescription.appendText(scanner.next());
                addTask();

                if(Objects.equals(scanner.next(), "true")){
                    tableViewer.getItems().get(i).getVerifyCheck().setSelected(true);
                }
                i++;
            }
        }
    }

    public static class toDo{
        public final SimpleStringProperty taskEntry;
        public final SimpleStringProperty dateEntry;
        public final SimpleStringProperty descriptionEntry;
        public CheckBox verifyCheck;

        public toDo(String task, String date, String description){
            //Creation of a constructor for entry items of 'task', 'date', 'description', and 'CheckBox()'.
            this.taskEntry = new SimpleStringProperty(task);
            this.dateEntry = new SimpleStringProperty(date);
            this.descriptionEntry = new SimpleStringProperty(description);
            this.verifyCheck = new CheckBox();
        }

        public String getTask() {
            //Return entry gotten from task entry. (This is a getter).
            return taskEntry.get();
        }

        public void setTask(String task1) {
            //Set entry from task to task1 (This is a setter).
            taskEntry.set(task1);
        }

        public String getDate() {
            //Return entry gotten from task date. (This is a getter).
            return dateEntry.get();
        }

        public void setDate(String date1) {
            //Set entry from date to date1 (This is a setter).
            dateEntry.set(date1);
        }

        public String getDescription() {
            //Return entry gotten from task description. (This is a getter).
            return descriptionEntry.get();
        }

        public void setDescription(String description1) {
            //Set entry from description to description1 (This is a setter).
            descriptionEntry.set(description1);
        }

        public CheckBox getVerifyCheck(){
            //Return verify. (This is a getter).
            return verifyCheck;
        }

        public void setCheck(CheckBox verifyCheck){
            //Set entry from verify to verify (This is a setter).
            this.verifyCheck = verifyCheck;
        }
    }
}