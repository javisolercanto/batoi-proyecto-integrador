/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controllers;

/**
 *
 * @author javi
 */

import com.dam.group2.desktopapp.client.App;
import dao.AddressDAO;
import dao.CityDAO;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;

import dao.CustomersDAO;
import dao.PostalCodeDAO;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import models.BatoiLogicAddress;
import models.BatoiLogicCity;
import models.BatoiLogicCustomer;
import models.BatoiLogicPostalCode;
import org.apache.xmlrpc.XmlRpcException;
import tools.Tools;

public class ControllerProfile {

    @FXML private ImageView btnSave;
    @FXML private ImageView btnBack;
    @FXML private ImageView btnRemoveAccount;

    @FXML private TextField tfUser;
    @FXML private PasswordField tfPassword;
    @FXML private ComboBox<BatoiLogicAddress> cbAddress;

    @FXML private ImageView btnRemove;
    @FXML private ImageView btnEdit;
    @FXML private ImageView btnAdd;

    @FXML private TextField tfStreet;
    @FXML private TextField tfPostalCode;
    @FXML private ComboBox<BatoiLogicCity> cbCity;
    @FXML private TextField tfProvince;
    @FXML private TextField tfName;
    @FXML private TextField tfEmail;
    @FXML private TextField tfPhone;

    private static final String ADD = "ADD";
    private static final String EDIT = "EDIT";
    private static final String REMOVE = "REMOVE";
    private static final String VIEW = "VIEW";
    private static final String VIEW_MAIN = "ViewMain";
    
    private static String MODE = VIEW;
    private static ArrayList<BatoiLogicAddress> addresses;
    private static ArrayList<String> cities;
    private static ArrayList<String> codes;
    private static ArrayList<String> provinces;

    private CustomersDAO customersDB;
    private AddressDAO addressDB;
    private CityDAO cityDB;
    private PostalCodeDAO postalCodeDB;
    
    @FXML
    public void initialize()
    {
        try
        {
            changeMode();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        tfName.setText(CurrentCustomer.getCurrentCustomer().getName());
        tfUser.setText(CurrentCustomer.getCurrentCustomer().getNickname());
        tfPassword.setText(CurrentCustomer.getRealPassword());
        tfEmail.setText(CurrentCustomer.getCurrentCustomer().getEmail() == null ? ""
                : CurrentCustomer.getCurrentCustomer().getEmail());
        tfPhone.setText(CurrentCustomer.getCurrentCustomer().getPhone() == null ? ""
                : CurrentCustomer.getCurrentCustomer().getPhone().toString());

        try
        {
            customersDB = new CustomersDAO();
            addressDB = new AddressDAO();
            cityDB = new CityDAO();
            postalCodeDB = new PostalCodeDAO();
            
            loadProfileInfo();
            
            if (!CurrentCustomer.getCurrentCustomer().getAddresses_id().isEmpty())
            {
                chooseAddress();
                chooseCode();
            }
   
        }
        catch (MalformedURLException | XmlRpcException e)
        {
            e.printStackTrace();
        }
    }
    
    @FXML
    public void chooseCode()
    {
        try
        {
            BatoiLogicPostalCode code = postalCodeDB.findByName(Integer.parseInt(tfPostalCode.getText()));
            cbCity.getItems().setAll(code.getBatoiLogicCity());
            cbCity.getSelectionModel().select(code.getBatoiLogicCity());
            tfProvince.setText(code.getBatoiLogicCity().getProvince());

        } catch (Exception ex) {
            Logger.getLogger(ControllerProfile.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    private void typingPostalCode()
    {
        String zip = tfPostalCode.getText();
        if(zip.length()>=4)
        {
            try
            {
                BatoiLogicPostalCode postalCode = postalCodeDB.findByName(Integer.parseInt(zip));
                if(postalCode!=null)
                    chooseCode();
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }
    }

    @FXML
    public void chooseAddress()
    {
        try {
            BatoiLogicAddress address = cbAddress.getSelectionModel().getSelectedItem();

            if (address != null) {
                tfStreet.setText(address.getName());
                cbCity.getSelectionModel().select(address.getBatoiLogicPostalCode().getBatoiLogicCity());
                tfPostalCode.setText(address.getBatoiLogicPostalCode().getName()+"");
                tfProvince.setText(address.getBatoiLogicPostalCode().getBatoiLogicCity().getProvince());
            }
        } catch (Exception ex) {
            Logger.getLogger(ControllerProfile.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    @FXML
    public void saveChanges()
    {
        if (tfName.getText().length() > 5 && tfPassword.getText().length() > 3 && tfUser.getText().length() > 3)
        {
            BatoiLogicCustomer user = CurrentCustomer.getCurrentCustomer();
            user.setNickname(tfUser.getText());
            user.setName(tfName.getText());
            user.setPassword(tfPassword.getText());
            user.setEmail(tfEmail.getText());
            user.setPhone(Integer.valueOf(tfPhone.getText()));
            CurrentCustomer.setUser(user);

            try
            {
                //  PERSIST CHANGES
                if(customersDB.update(CurrentCustomer.getCurrentCustomer()))
                    Tools.showSucceed("Updated","Your Profile was UPDATED!");
                else
                    Tools.showSimpleError("NOT Updated", "Profile not updated for any reason");
            }
            catch (Exception e)
            {
                Tools.showExceptionMessage(e, "Impossible to updated");
            }
        }
        else
        {
            //  SHOW ALERT
            Tools.showSimpleError("Error Saving", "No CHANGES saved");
        }
    }
    
    @FXML
    public void cancelAccount(MouseEvent event)
    {
        try
        {
            //  SHOW CONFIRMATION DIALOG
            if(Tools.confirmationDialog("Deleting your account",
                    "All your information may be erased as well","Cancel Account"))
            {
                if(customersDB.delete(CurrentCustomer.getCurrentCustomer().getId()))
                {
                    Tools.showSucceed("Done it", "You are no longer with us");
                    System.exit(0);
                }
                else
                    Tools.showSimpleError("Error", "You cannot be deleted");
            }
        }
        catch (Exception e)
        {
            try
            {
                BatoiLogicCustomer user = CurrentCustomer.getCurrentCustomer();
                user.setNickname(CurrentCustomer.DELETED+user.getNickname());
                customersDB.update(user);
                CurrentCustomer.disconnect();
                openView(VIEW_MAIN, event);
            }
            catch (Exception ex)
            {
                ex.printStackTrace();
            }
        }
    }
    
    @FXML
    public void addAddress(MouseEvent event) {
        try {
            switch (MODE) {
                case ADD:
                    if (tfStreet.getText() != null && tfStreet.getText().length() > 5) {
                        if (addressDB.insert(new BatoiLogicAddress(CurrentCustomer.getCurrentCustomer(), postalCodeDB.findByName(Integer.parseInt(tfPostalCode.getText())), tfStreet.getText()))) {
                            Tools.showSucceed("Created", "Address created succesful");
                            loadProfileInfo();
                        } else 
                            Tools.showSimpleError("Error", "Address can't be created");
                    } 
                    else 
                        Tools.showSimpleError("Error", "Error at street name");
                    
                    MODE = VIEW;
                    changeMode();
                    break;
                case EDIT:
                    if (tfStreet.getText() != null && tfStreet.getText().length() > 5 && tfPostalCode.getText().length()>=4 && cbCity.getValue()!=null && cbAddress.getValue()!=null) {
                        BatoiLogicAddress address = new BatoiLogicAddress(CurrentCustomer.getCurrentCustomer(), postalCodeDB.findByName(Integer.parseInt(tfPostalCode.getText())), tfStreet.getText());
                        address.setId(cbAddress.getSelectionModel().getSelectedItem().getId());
                        
                        if (addressDB.update(address)) {
                            Tools.showSucceed("Updated", "Address updated succesful");
                            loadProfileInfo();
                        } else 
                            Tools.showSimpleError("Error", "Address can't be created");
                    } 
                    else 
                        Tools.showSimpleError("Error", "Error at street name");
                    
                    MODE = VIEW;
                    changeMode();
                    break;
                default:
                    // CHANGE VIEW TO ADD
                    
                    MODE = ADD;
                    changeMode();
                    break;
            }
        } catch (Exception ex) {
            Tools.showExceptionMessage(ex, "Error at working in addresses");
        }
    }
    
    @FXML
    public void editAddress(MouseEvent event) {
        MODE = EDIT;
        try {
            changeMode();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    @FXML
    public void removeAddress(MouseEvent event) {
        
        //  DELETE ADDRESS     ¿CONFIRMATION DIALOG?
        if (MODE.equals(EDIT) || MODE.equals(ADD)) {
            cbAddress.getSelectionModel().selectFirst();
            
            loadProfileInfo();
            chooseAddress();
            chooseCode();
            
            MODE = VIEW;
            try
            {
                changeMode();
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }

        } else {
            try {
                if (addressDB.delete(cbAddress.getSelectionModel().getSelectedItem().getId())) {
                    Tools.showSucceed("Deleted", "Address deleted successfully");
                    loadProfileInfo();
                } else
                    Tools.showSimpleError("Error", "Address can't be deleted");
            } catch (Exception ex) {
                Tools.showSimpleError("Deleting", "Sorry you can't delete an address associated to an order");
            }
        }   
    }
    
    @FXML
    public void back(MouseEvent event) throws IOException
    {
        openView(CurrentCustomer.getPreviousScreen(), event);
    }
    
    public void changeMode() throws Exception {
        switch (MODE) {
            case ADD:
                if (CurrentCustomer.getCurrentCustomer().getAddresses_id().size() > 0)
                {
                    cbAddress.setValue(null);
                    tfStreet.clear();
                    tfPostalCode.clear();
                    cbCity.setValue(null);
                    tfProvince.setText(null);
                }
            
            case EDIT: 
                cbAddress.setDisable(true);
                tfStreet.setEditable(true);
                tfPostalCode.setDisable(false);
                btnEdit.setDisable(true);
                break;
                
            case VIEW:
                cbAddress.setDisable(false);
                tfStreet.setEditable(false);
                tfPostalCode.setDisable(true);
                btnEdit.setDisable(false);
                btnRemove.setDisable(false);
                break;
        }
    }
    
    private void openView(String view, MouseEvent event) throws IOException {
        Scene scene = new Scene(App.loadFXML(view));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.centerOnScreen();
        stage.show();
    }

    private void loadProfileInfo() {
        try {
            CurrentCustomer.refreshCustomer(customersDB);
            
            cbAddress.getItems().clear();
            cbAddress.getItems().addAll(addressDB.findAllByPks(CurrentCustomer.getCurrentCustomer().getAddresses_id()));



            if (CurrentCustomer.getCurrentCustomer().getAddresses_id().size()>0)
            {
                cbAddress.getSelectionModel().selectFirst();
                tfPostalCode.setText(cbAddress.getSelectionModel().getSelectedItem().getBatoiLogicPostalCode().getName()+"");
            }

            if(cbAddress.getItems().isEmpty())
            {
                tfStreet.setText("");
                tfPostalCode.setText("");
                cbAddress.setValue(null);
                cbCity.setValue(null);
                tfProvince.setText("");
            }

        } catch (Exception ex) {
            Tools.showExceptionMessage(ex, "Error loading customer information");
        }
    }

}
