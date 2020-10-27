package com.dam.group2.desktopapp.client;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import java.io.File;
import java.io.IOException;

/**
 * JavaFX App
 */
public class App extends Application
{
    private static final Image ICON = new Image(new File("src/main/resources/com/dam/group2/desktopapp/client/images/icon.png").toURI().toString());

    @Override
    public void start(Stage stage) throws IOException
    {
        Scene scene = new Scene(loadFXML("ViewMain"));
        stage.setScene(scene);
        stage.centerOnScreen();
        stage.setTitle("BatoiLogic - Customers");
        stage.getIcons().add(ICON);
        stage.show();
    }

    public static Parent loadFXML(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource(fxml + ".fxml"));
        return fxmlLoader.load();
    }

    public static void main(String[] args) {
        launch();
    }
}