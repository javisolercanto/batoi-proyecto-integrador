package com.dam.group2.desktopapp.employees;

import java.io.File;

import controller.ControllerTruckTracking;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import javafx.scene.image.Image;
import javafx.stage.FileChooser;

/**
 * JavaFX App
 */
public class App extends Application {

    public static Scene scene;
    public static Stage stage;
    public static String APP_NAME = "BatoiLogic - Employees";

    private static final Image ICON = new Image(new File("src/main/resources/com/dam/group2/desktopapp/employees/images/icon.png").toURI().toString());

    @Override
    public void start(Stage stage) throws IOException
    {
        App.stage = stage;
        scene = new Scene(loadFXML("ViewMain"));
        App.putTitleAndIcons(APP_NAME);
        App.stage.setScene(scene);
        stage.show();

        // Closing the socket and the app.
        stage.setOnCloseRequest(c ->{
            if(ControllerTruckTracking.locationSocket!=null)
                ControllerTruckTracking.locationSocket.getSocket().close();
            System.exit(0);
        });
    }

    static void setRoot(String fxml) throws IOException {
        scene.setRoot(loadFXML(fxml));
    }

    public static Parent loadFXML(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource(fxml + ".fxml"));
        return fxmlLoader.load();
    }

    public static File fileChooser(String titulo) {

        FileChooser fc = new FileChooser();
        fc.setTitle(titulo);
        fc.setInitialDirectory(new File(System.getProperty("user.home")));
        FileChooser.ExtensionFilter filter = new FileChooser.ExtensionFilter("CSV files", "*.csv");
        fc.getExtensionFilters().add(filter);

        return fc.showOpenDialog(App.stage);
    }

    public static void putTitleAndIcons(String title)
    {
        App.stage.setTitle(title);
        App.stage.getIcons().add(ICON);
        App.stage.setScene(scene);
        App.stage.centerOnScreen();
    }

    public static void main(String[] args) {
        launch();
    }
}