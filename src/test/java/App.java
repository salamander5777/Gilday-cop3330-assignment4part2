/*
 *  UCF COP3330 Fall 2021 Assignment 4 Solution
 *  Copyright 2021 Michael Gilday
 */

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.File;
import java.io.IOException;
import java.util.Objects;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;

class App extends Application {

    @Test
    //Used to open up the App.fxml, which will create an instance that will allow a new to-do list to be opened.
    @Override
    public void start(Stage primaryStage) throws IOException {
        Assertions.assertTrue(new File("App.fxml").exists());
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("App.fxml")));
        primaryStage.setTitle("To-do List");
        primaryStage.setScene(new Scene(root, 600, 430));
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
