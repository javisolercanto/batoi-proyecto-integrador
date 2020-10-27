package controller;

import com.dam.group2.desktopapp.employees.App;
import java.io.IOException;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.application.Platform;

public class ControllerMain {

    @FXML
    private ImageView btnStock;
    @FXML
    private ImageView btnorders;
    @FXML
    private ImageView btntrucktrucking;
    @FXML
    private ImageView btnroutes;

    @FXML
    public void initialize() throws Exception {
        
    }

    @FXML
    void openStock(MouseEvent event) throws IOException {
        openView("ViewStock", event);
    }

    @FXML
    void openRoutes(MouseEvent event) throws IOException {
        openView("ViewDeliveryNotes", event);
    }

    @FXML
    void openTruckTracking(MouseEvent event) throws IOException {
        openView("ViewTruckTracking", event);
    }

    @FXML
    void openOrders(MouseEvent event) throws IOException {
        openView("ViewOrders", event);
    }

    @FXML
    private void exitApp() {
        
        Platform.exit();
    }

    private void openView(String view, MouseEvent event) throws IOException {

        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(App.loadFXML(view));
        stage.setScene(scene);
        stage.centerOnScreen();
        stage.sizeToScene();
        stage.show();
    }
}