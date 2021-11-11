/*
 *  UCF COP3330 Fall 2021 Assignment 4 Solution
 *  Copyright 2021 Michael Gilday
 */

import java.io.*;
import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;
import java.util.Scanner;
import java.util.function.BooleanSupplier;

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
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;

public class GuiController implements Initializable {
    @Test
    @Override //Starts up menu options, so they may be used later and sets up cell value factories.
    public void initialize(URL location, ResourceBundle resources) {
        taskCol.setCellValueFactory(taskEData -> taskEData.getValue().taskEntry);
        taskCol.setCellFactory(TextFieldTableCell.forTableColumn());
        taskCol.setOnEditCommit(event -> {
            ucf.assignments.GuiController.toDo currentColumn = event.getRowValue();
            currentColumn.setTask(event.getNewValue());
        });
        Assertions.assertTrue(taskCol.hasProperties());

        dueDateCol.setCellValueFactory(dateEData -> dateEData.getValue().dateEntry);
        dueDateCol.setCellFactory(TextFieldTableCell.forTableColumn());
        dueDateCol.setOnEditCommit(event -> {
            ucf.assignments.GuiController.toDo currentColumn = event.getRowValue();
            currentColumn.setDate(event.getNewValue());
        });
        Assertions.assertTrue(dueDateCol.hasProperties());

        taskDescCol.setCellValueFactory(descEData -> descEData.getValue().descriptionEntry);
        taskDescCol.setCellFactory(TextFieldTableCell.forTableColumn());
        taskDescCol.setOnEditCommit(event -> {
            ucf.assignments.GuiController.toDo currentColumn = event.getRowValue();
            currentColumn.setDescription(event.getNewValue());
        });
        Assertions.assertTrue(taskDescCol.hasProperties());

        taskFinished.setCellValueFactory(new PropertyValueFactory<>("verifyCheck"));
        tableViewer.setItems(data); //Used to populate tableview with gathered input.
        menuOptions();
    }

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
    public TableColumn<ucf.assignments.GuiController.toDo, String> taskCol;
    @FXML
    public TableColumn<ucf.assignments.GuiController.toDo, String> dueDateCol;
    @FXML
    public TableColumn<ucf.assignments.GuiController.toDo, String> taskDescCol;
    @FXML
    public TableColumn<ucf.assignments.GuiController.toDo, String> taskFinished;

    @FXML
    public MenuItem open;
    @FXML
    public MenuItem save;
    @FXML
    public MenuItem exit;

    @FXML
    public TableView<ucf.assignments.GuiController.toDo> tableViewer;
    public final ObservableList<ucf.assignments.GuiController.toDo> data = FXCollections.observableArrayList();

    @Test
    @FXML
    public void addTask(){ //Adds information from the entry fields to the ObservableList 'data', which is set to the
        //TableView 'tableViewer'.
        if(taskEntry.getText().length() != 0 &&  dateEntry.getText().length() != 0 && taskDescription.getText().length() != 0){
            Assertions.assertTrue(taskEntry.getText().length() >= 1);
            Assertions.assertTrue(dateEntry.getText().length() >= 1);
            Assertions.assertTrue(taskDescription.getText().length() >= 1);
            data.add(new ucf.assignments.GuiController.toDo(taskEntry.getText(), dateEntry.getText(), taskDescription.getText()));
            refresh();
        }
    }

    @Test
    @FXML
    public void refresh(){ //Refreshes entry fields so that text may be re-input more efficiently.
        taskEntry.clear();
        dateEntry.clear();
        taskDescription.clear();
        Assertions.assertEquals(0, taskEntry.getLength());
        Assertions.assertEquals(0, dateEntry.getLength());
        Assertions.assertEquals(0, taskDescription.getLength());
    }

    @Test
    @FXML
    public void deleteTask(){//This method is used to delete the selected row from the content table.
        ObservableList<ucf.assignments.GuiController.toDo> removeSelected, allObjects;
        allObjects = tableViewer.getItems();
        removeSelected = tableViewer.getSelectionModel().getSelectedItems();

        Assertions.assertTrue(tableViewer.hasProperties());
        removeSelected.forEach(allObjects::remove);
    }

    @Test
    @FXML
    public void deleteList(){
        Assertions.assertTrue(tableViewer.hasProperties());
        tableViewer.getItems().clear();
    }

    @Test
    @FXML
    public void showComplete(){
        if(checkComplete.isSelected()){
            ObservableList<ucf.assignments.GuiController.toDo> showSelected = FXCollections.observableArrayList();

            for (ucf.assignments.GuiController.toDo task : data){
                if(task.getVerifyCheck().isSelected()){
                    showSelected.add(task);
                }
            }
            tableViewer.setItems(showSelected);
            Assertions.assertTrue(tableViewer.hasProperties());
        }
        else{
            tableViewer.setItems(data);
        }
    }

    @Test
    @FXML
    public void showIncomplete(){
        if(checkIncomplete.isSelected()){
            ObservableList<ucf.assignments.GuiController.toDo> showUnselected = FXCollections.observableArrayList();

            for (ucf.assignments.GuiController.toDo task : data){
                if(!task.getVerifyCheck().isSelected()){
                    showUnselected.add(task);
                }
            }
            tableViewer.setItems(showUnselected);
            Assertions.assertTrue(tableViewer.hasProperties());
        }
        else{
            tableViewer.setItems(data);
        }
    }

    @Test
    public void menuOptions(){ //This method is used to handle when a user clicks on a menu item.
        final FileChooser fileC = new FileChooser();
        fileC.setInitialFileName("file.csv");
        fileC.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("CSV file", "*.csv*"));

        exit.setOnAction(e -> Platform.exit());

        save.setOnAction(e -> {
            File fileSaver = fileC.showSaveDialog(null);
            if (fileSaver != null) {
                Assertions.assertTrue(fileSaver.exists());
                try {
                    saveFile(fileSaver);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });

        open.setOnAction(e -> {
            File fileOpener = fileC.showOpenDialog(null);
            if (fileOpener != null) {
                Assertions.assertTrue(fileOpener.exists());
                try {
                    openFile(fileOpener);
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        });
    }

    @Test
    public void saveFile(File savesFile) throws Exception {
        Writer bw = null;
        try {
            bw = new BufferedWriter(new FileWriter(savesFile));
            for (ucf.assignments.GuiController.toDo task : data) {

                String currentCSV = task.getTask() + "," + task.getDate() + "," + task.getDescription() + "," + task.getVerifyCheck().isSelected() + "\n";

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
        Assertions.assertTrue(savesFile.exists());
    }

    @Test
    public void openFile(File opensFile) throws IOException {
        try(Scanner scanner = new Scanner(opensFile)) {
            Assertions.assertTrue(opensFile.exists());
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
            this.taskEntry = new SimpleStringProperty(task);
            this.dateEntry = new SimpleStringProperty(date);
            this.descriptionEntry = new SimpleStringProperty(description);
            this.verifyCheck = new CheckBox();
        }

        @Test
        public String getTask() {
            Assertions.assertTrue(Boolean.parseBoolean(taskEntry.get()));
            return taskEntry.get();
        }

        @Test
        public void setTask(String task1) {
            taskEntry.set(task1);
            Assertions.assertEquals(taskEntry.get(), task1);
        }

        @Test
        public String getDate() {
            Assertions.assertTrue(Boolean.parseBoolean(dateEntry.get()));
            return dateEntry.get();
        }

        @Test
        public void setDate(String date1) {
            dateEntry.set(date1);
            Assertions.assertEquals(dateEntry.get(), date1);
        }

        @Test
        public String getDescription() {
            Assertions.assertTrue(Boolean.parseBoolean(descriptionEntry.get()));
            return descriptionEntry.get();
        }

        @Test
        public void setDescription(String description1) {
            descriptionEntry.set(description1);
            Assertions.assertEquals(descriptionEntry.get(), description1);
        }

        @Test
        public CheckBox getVerifyCheck(){
            Assertions.assertTrue((BooleanSupplier) verifyCheck);
            return verifyCheck;
        }

        @Test
        public void setCheck(CheckBox verifyCheck){
            this.verifyCheck = verifyCheck;
            Assertions.assertEquals(verifyCheck, verifyCheck);
        }
    }
}
