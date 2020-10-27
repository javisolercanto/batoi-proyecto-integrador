package controllers;

import com.dam.group2.desktopapp.client.App;
import dao.CustomersDAO;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import models.BatoiLogicCustomer;
import org.apache.xmlrpc.XmlRpcException;
import tools.Tools;

public class ControllerLogin implements Initializable{

    @FXML private TextField tfName;
    @FXML private TextField tfUser;
    @FXML private PasswordField tfPassword;
    @FXML private Button btnSignin;
    @FXML private ImageView btnBack;
    
    private BatoiLogicCustomer customer;
    private CustomersDAO customersDB;

    private static final String VIEW_MAIN = "ViewMain";
    private static final String VIEW_ORDERS = "ViewOrders";
    private static final String VIEW_SIGNUP = "ViewSignup";

    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        customer = new BatoiLogicCustomer();
        try {
            customersDB = new CustomersDAO();
            
        } catch (Exception ex) {
            Tools.showExceptionMessage(ex, "Connection error");
        }
    }
    
    @FXML
    public void signup(MouseEvent event) {
        if (tfName.getText().length() > 5 && tfPassword.getText().length() > 3 && tfUser.getText().length() > 3) {
            try {
                customer = new BatoiLogicCustomer(tfName.getText(), tfUser.getText(),
                        Tools.encryptThisSHA1(tfPassword.getText()));
                if (customersDB.insert(customer)) {
                    CurrentCustomer.setUser(customer);
                    CurrentCustomer.setRealPassword(tfPassword.getText());
                    Tools.showSucceed("Signup", "Succesful signup completed");

                    openView(VIEW_MAIN, event);
                }
            } catch (Exception ex) {
                Tools.showExceptionMessage(ex, "Database error");
            }
            
        } else {
            showAlert("Please, complete the fields correctly");
        }
    }
    
    @FXML
    public void signin(MouseEvent event) {
        try {
            customer = customersDB.findByNickname(tfUser.getText());
            if (customer != null && Tools.encryptThisSHA1(tfPassword.getText()).equals(customer.getPassword()))
            {
                if(!tfUser.getText().contains(CurrentCustomer.DELETED))
                {
                    CurrentCustomer.setUser(customer);
                    CurrentCustomer.setRealPassword(tfPassword.getText());
                    openView(CurrentCustomer.getPreviousScreen(),event);
                    CurrentCustomer.setPreviousScreen(VIEW_MAIN);
                }
            } else {
                Tools.showSimpleError("Login Error", "Nickname or Password incorrect");
            }

        } catch (Exception ex) {
            Tools.showExceptionMessage(ex, "Error loading information");
        }
    }
    
    @FXML
    public void openSignup(MouseEvent event) throws IOException {
        openView(VIEW_SIGNUP, event);
    }
    
    @FXML
    public void back(MouseEvent event) throws IOException
    {
//        openView(CurrentCustomer.getPreviousScreen(), event);
        openView(VIEW_MAIN, event);
    }
    
    private void openView(String view, MouseEvent event) throws IOException 
    {
        Scene scene = new Scene(App.loadFXML(view));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.centerOnScreen();
        stage.sizeToScene();
        stage.show();
    }
    
    private void showAlert(String message) {
        Alert alert = new Alert(AlertType.WARNING);
        alert.setTitle("Warning");
        alert.setHeaderText("");
        alert.setContentText(message);

        alert.showAndWait();
    }
}