package controllers;

import com.dam.group2.desktopapp.client.App;
import java.io.IOException;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

public class ControllerTracking {

    @FXML
    private TextField tfTrackingNumber;

    @FXML
    private Button btnSearch;
    
    @FXML
    private ImageView btnBack;
    
    
    
    
    @FXML
    public void initialize() {
        
    }
    
    @FXML
    public void back(MouseEvent event) throws IOException {
        Scene scene = new Scene(App.loadFXML("ViewOrders"));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.centerOnScreen();
        stage.show();
    }
    

}