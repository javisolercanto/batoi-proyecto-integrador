package controller;

import com.dam.group2.desktopapp.employees.App;
import com.lynden.gmapsfx.GoogleMapView;
import com.lynden.gmapsfx.MapComponentInitializedListener;
import com.lynden.gmapsfx.javascript.object.*;
import com.lynden.gmapsfx.service.geocoding.*;
import dao.*;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import models.*;
import odooAPI.SimpleOrdersDAO;
import odooAPI.SimpleRoutesDAO;
import org.apache.xmlrpc.XmlRpcException;
import tools.Tools;
import tools.WebServiceHttp;

public class ControllerDeliveryNotes implements Initializable, MapComponentInitializedListener, WebServiceHttp.OnMyRequestComplete
{
    @FXML private ImageView btnGenerate;
    @FXML private Text textRoutes;
    @FXML private ImageView btnViewRoutes;
    @FXML private Text textNotes;
    @FXML private ImageView btnViewNotes;
    @FXML private Text textName;

    @FXML private TableView<BatoiLogicDeliveryNote> tableViewDeliveryNotes;
    @FXML private TableColumn<BatoiLogicDeliveryNote, String> cDate;
    @FXML private TableColumn<BatoiLogicDeliveryNote, String> cAddress;
    @FXML private TableColumn<BatoiLogicDeliveryNote, String> cCustomer;
    @FXML private TableColumn<BatoiLogicDeliveryNote, String> cStatus;

    private ObservableList<BatoiLogicDeliveryNote> deliveryNotesList;
    private ObservableList<BatoiLogicRoute> routesList;

    @FXML private TableView<BatoiLogicRoute> tableViewRoutes;
    @FXML private TableColumn<BatoiLogicRoute, Integer> tcIdRoute;
    @FXML private TableColumn<BatoiLogicRoute, String> tcDelvMan;

    @FXML private GoogleMapView googleMap;
    private GoogleMap map;
    private List<LatLong> places;
    private List<Integer> toDeliver;
    private WebServiceHttp webServiceHttp;

    private DeliveryNoteDao deliveryNoteDao;
    private DeliveryManDao deliveryManDao;
    private ComboBox<BatoiLogicDeliveryMan> cmbDeliveryMen;
    
    private static final String ALL = "ALL";
    private static final String ROUTE = "ROUTE";
    private static final String NOTES = "NOTES";
    public static final String PENDING = "0";
    public static final String READY = "1";
    public static final String OUT_FOR_DELIVERY = "2";
    public static final String DELIVERED = "3";
    public static final String NO_DELIVERED = "4";
    
    private static String MODE = NOTES;

    @Override
    public void initialize(URL url, ResourceBundle rb)
    {
        places = new ArrayList<>();
        toDeliver = new ArrayList<>();

        deliveryNoteDao = new DeliveryNoteDao();
        deliveryManDao = new DeliveryManDao();

        deliveryNotesList = FXCollections.observableArrayList();
        routesList = FXCollections.observableArrayList();
        
        try
        {
            addDataToObservableList();
            setupTables();
            googleMap.addMapInializedListener(this);
        }
        catch (NullPointerException ex)
        {
            System.err.println("Error while creating the table, some rows might be empty " + ex.getMessage());
        }
        catch (Exception ex)
        {
            System.err.println("Error while creating table " + ex.getMessage());
        }
        
        tableViewRoutes.setVisible(false);
    }

    private void setupTables()
    {
        // TableView DeliveryNotes -->
        tableViewDeliveryNotes.setItems(deliveryNotesList);
        tableViewDeliveryNotes.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);                            // Multiple selections.

        cDate.setCellValueFactory(o -> new SimpleStringProperty(new SimpleDateFormat("dd-MM-yyyy")
                .format(o.getValue().getBatoiLogicOrder().getDate())));
        cAddress.setCellValueFactory(o -> new SimpleStringProperty(o.getValue().getBatoiLogicOrder().getBatoiLogicAddress().getName()));
        cCustomer.setCellValueFactory(o -> new SimpleStringProperty(o.getValue().getBatoiLogicOrder().getBatoiLogicCustomer().getName()));
        cStatus.setCellValueFactory(o -> new SimpleStringProperty(o.getValue().getBatoiLogicOrder().getOrderStatus()));

        // On Selected items.
        tableViewDeliveryNotes.setOnMouseClicked(c ->{

            toDeliver.clear();
            places.clear();

            ObservableList<BatoiLogicDeliveryNote> notes = tableViewDeliveryNotes.getSelectionModel().getSelectedItems();
            if(!notes.isEmpty())
            {
                map.clearMarkers();

                if(webServiceHttp!=null)
                    webServiceHttp.interrupt();

                webServiceHttp = new WebServiceHttp(notes, this);
                webServiceHttp.start();
            }
            else
            {
                tableViewDeliveryNotes.getSelectionModel().clearSelection();
                initializeMap();
            }
        });
    }

    public void addDataToObservableList() throws Exception
    {
        deliveryNoteDao.close();
        deliveryNoteDao = new DeliveryNoteDao();
        deliveryNotesList.clear();
        deliveryNotesList.addAll(deliveryNoteDao.findAll());

        // Only want to show the delivery notes that can have a route.
        deliveryNotesList.removeIf(l -> !(l.getBatoiLogicOrder().getStatus().equals(BatoiLogicOrder.PENDING)
                || l.getBatoiLogicOrder().getStatus().equals(BatoiLogicOrder.NO_DELIVERED)));
    }

    @Override
    public void mapInitialized()
    {
        //Set the initial properties of the map.
        MapOptions mapOptions = new MapOptions();
        mapOptions.center(new LatLong(38.7054500, -0.4743200))
                .mapType(MapTypeIdEnum.ROADMAP)
                .overviewMapControl(true)
                .panControl(true)
                .rotateControl(true)
                .scaleControl(true)
                .streetViewControl(true)
                .zoomControl(true)
                .zoom(12);

        map = googleMap.createMap(mapOptions);
        initializeMap();
    }

    private void initializeMap()
    {
        if(webServiceHttp!=null)
            webServiceHttp.interrupt();

        webServiceHttp = new WebServiceHttp(deliveryNotesList, this);
        webServiceHttp.start();
    }

    @Override
    public synchronized void requestComplete(double[] latLong)
    {
        // Java FX thread to update the GUI.
        Platform.runLater(() -> {
            addMarker(new LatLong(latLong[WebServiceHttp.LAT],latLong[WebServiceHttp.LON]));
        });
    }

    private synchronized void addMarker(LatLong latLong)
    {
        int i;
        boolean found = false;

        LatLong l;
        LatLong location = null;
        for (i=0;i<places.size() && !found;i++)
        {
            l = places.get(i);
            if(l.getLatitude()==latLong.getLatitude() && l.getLongitude()==latLong.getLongitude())
            {
                location = l;
                found = true;
            }
        }

        if(location!=null)
            toDeliver.set(i-1,toDeliver.get(i-1)+1);
        else
        {
            places.add(latLong);
            toDeliver.add(1);
        }

        this.loadPoints();
    }

    private void loadPoints()
    {
        map.clearMarkers();

        for (int i=0;i<places.size();i++)
        {
            //Add a marker to the map
            MarkerOptions markerOptions = new MarkerOptions();
            markerOptions.position(places.get(i))
                    .visible(Boolean.TRUE)
                    .label(toDeliver.get(i)+"");

            Marker marker = new Marker(markerOptions);

            map.addMarker(marker);
        }
        tableViewDeliveryNotes.setDisable(false);
    }

    @FXML
    public void back(MouseEvent event) throws IOException {
        openView("ViewMain", event);
    }
    
    @FXML
    public void generateRoutes() {
        try
        {
            List<BatoiLogicDeliveryNote> notes = tableViewDeliveryNotes.getSelectionModel().getSelectedItems();
            BatoiLogicDeliveryMan dman = chooseDeliveryman();
            if (dman != null && notes.size()>0) {
//                if (!dman.getBatoiLogicLorry().isFull()) {
//                    int quantity = 0;
//                    for (BatoiLogicDeliveryNote note : notes)
//                    {
//                        for (Object line : note.getBatoiLogicOrder().getBatoiLogicOrderLines()) {
//                            quantity += ((BatoiLogicOrderLine) line).getQuantity();
//                        }
//                    }
                    
//                    if (quantity + dman.getBatoiLogicLorry().getCurrentLoad() <= dman.getBatoiLogicLorry().getCapacity())
//                    {
                        SimpleOrdersDAO ordersDAO = new SimpleOrdersDAO();
                        SimpleRoutesDAO routesDAO = new SimpleRoutesDAO();
                        BatoiLogicRoute route = new BatoiLogicRoute(dman, notes);
                        for (BatoiLogicDeliveryNote note : notes)
                        {
                            note.getBatoiLogicOrder().setStatus(READY);
                            ordersDAO.update(note.getBatoiLogicOrder());
                        }

                        if(routesDAO.insert(route))
                        {
                            Tools.showSucceed("Route created", "Route created successfully");
                            addDataToObservableList();
                        }
                        else
                            Tools.showSimpleError("Load error", "Error creating the route");
                        
//                    }
//                    else {
//                        Tools.showSimpleError("Load error", "The quantity selected exceeds lorry capacity");
//                    }
                    
                } else {
                    Tools.showSimpleError("Load error", "This lorry is full");
                }
            }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    @FXML
    public void viewRoutesOrOrders(MouseEvent event) {
        try {
            if (MODE.equals(NOTES)) {
                BatoiLogicDeliveryMan man = chooseDeliveryman();
                
                if (man != null) {
                    MODE = ROUTE;
                    changeMode();
                    textName.setText(man.getName());
                    deliveryNotesList.clear();

                    man.getBatoiLogicRoutes().forEach(r -> {

                        Set<BatoiLogicDeliveryNote> notes = ((BatoiLogicRoute)r).getBatoiLogicDeliveryNotes();
                        if(!notes.isEmpty())
                            deliveryNotesList.addAll(notes);
                    });
//                    for (Object route : man.getBatoiLogicRoutes())
//                    {
//                        System.out.println(man.getBatoiLogicRoutes().size());
//                        deliveryNotesList.add((BatoiLogicDeliveryNote) ((BatoiLogicRoute) route).getBatoiLogicDeliveryNotes().toArray()[0]);
//                        System.out.println(deliveryNotesList);
//                    }
                }
                
            } else {
                MODE = NOTES;
                changeMode();
                textName.setText(ALL);
                addDataToObservableList();
            }

            tableViewDeliveryNotes.setItems(deliveryNotesList);
            tableViewDeliveryNotes.refresh();
        } catch (Exception ex) {
        }
    }   
    
    private BatoiLogicDeliveryMan chooseDeliveryman() throws Exception {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Route");
        alert.setHeaderText("Generating the route");

        Label label = new Label("Choose the deliveryman: ");

        ComboBox<BatoiLogicDeliveryMan> cbDeliveryman = new ComboBox<>();
        cbDeliveryman.getItems().setAll(deliveryManDao.findAll());
        GridPane.setVgrow(cbDeliveryman, Priority.ALWAYS);
        GridPane.setHgrow(cbDeliveryman, Priority.ALWAYS);

        GridPane expContent = new GridPane();
        expContent.setMaxWidth(Double.MAX_VALUE);
        expContent.add(label, 0, 0);
        expContent.add(new Label(), 0, 1);
        expContent.add(cbDeliveryman, 0, 2);

        alert.getDialogPane().setContent(expContent);

        alert.showAndWait();
        if (alert.getResult() == ButtonType.OK)
            return cbDeliveryman.getSelectionModel().getSelectedItem();
            
        else
            return null;
    }
    
    private void changeMode() {
        if (MODE.equals(ROUTE)) {
            btnViewRoutes.setVisible(false);
            textRoutes.setVisible(false);
            btnViewNotes.setVisible(true);
            textNotes.setVisible(true);
            btnGenerate.setDisable(true);
            
        } else {
            btnViewRoutes.setVisible(true);
            textRoutes.setVisible(true);
            btnViewNotes.setVisible(false);
            textNotes.setVisible(false);
            btnGenerate.setDisable(false);
        }
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