package controller;

import com.dam.group2.desktopapp.employees.App;
import com.lynden.gmapsfx.GoogleMapView;
import com.lynden.gmapsfx.MapComponentInitializedListener;
import com.lynden.gmapsfx.javascript.object.*;

import java.io.IOException;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Comparator;
import java.util.Optional;
import java.util.Set;
import dao.DeliveryManDao;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.stage.Stage;
import models.BatoiLogicDeliveryMan;
import models.BatoiLogicLocation;
import tools.SocketLocation;
import tools.Tools;

public class ControllerTruckTracking implements MapComponentInitializedListener, SocketLocation.MyMapListener {
    @FXML private ImageView btnRemoveCart;
    @FXML private ImageView btnBack;
    @FXML private ImageView btnCheckout;

    @FXML private GoogleMapView mapView;
    private GoogleMap map;
    public static SocketLocation locationSocket;

    @FXML
    public void initialize()
    {
        try
        {
            mapView.addMapInializedListener(this);

            // Starting Socket.
            locationSocket = new SocketLocation(this);
            locationSocket.start();
        }
        catch (UnknownHostException | SocketException e)
        {
            e.printStackTrace();
        }
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

        map = mapView.createMap(mapOptions);

        try
        {
            loadPoints();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    private synchronized void loadPoints() throws Exception
    {
        Set<BatoiLogicDeliveryMan> list = new DeliveryManDao().findAll();

        list.forEach(d ->
        {
            if(d.getBatoiLogicLocations().size()>0)
            {
                // Searching for the most recent location.
                Optional<BatoiLogicLocation> location = d.getBatoiLogicLocations()
                        .stream().max(Comparator.comparingInt(o -> ((BatoiLogicLocation) o).getId()));

                //Add a marker to the map
                MarkerOptions markerOptions = new MarkerOptions();
                markerOptions.position(new LatLong(location.get().getLatitude(), location.get().getLongitude()))
                        .visible(Boolean.TRUE)
                        .title(location.get().getBatoiLogicDeliveryMan().getName());
                Marker marker = new Marker(markerOptions);

                map.addMarker(marker);
            }
        });
    }

    @FXML
    private void reload()
    {
        Tools.showSucceed("Reload","Latest known locations loaded!");
        onMapChange();
    }

    @Override
    public synchronized void onMapChange()
    {
        // JavaFX thread, otherwise exception is thrown
        Platform.runLater(() ->
        {
            //Update your GUI here
            try
            {
                map.clearMarkers();
                loadPoints();
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        });
    }

    @FXML
    public void back(MouseEvent event) throws IOException
    {
        /*
            Because is impossible to stop the thread even with interrupts when is receiving,
            the simplest way to do it is closing socket.
            This is for noticing the Location socket that i no longer want his services and also
            to stop this unnecessary thread.
         */
        locationSocket.getSocket().close();
        openView("ViewMain", event);
    }

    @FXML
    private void findDeliveryman(MouseEvent event)
    {
        try
        {
            BatoiLogicDeliveryMan selected = alertWithComboBox();
            if(selected!=null)
            {
                // Searching for the most recent location.
                Optional<BatoiLogicLocation> location = selected.getBatoiLogicLocations()
                        .stream().max(Comparator.comparingInt(o -> ((BatoiLogicLocation) o).getId()));

                map.setCenter(new LatLong(location.get().getLatitude(),location.get().getLongitude()));
                map.setZoom(20);
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    private BatoiLogicDeliveryMan alertWithComboBox() throws Exception
    {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("FIND");
        alert.setHeaderText("Choose a deliveryman");

        Label label = new Label("List Deliverymen: ");

        ComboBox<BatoiLogicDeliveryMan> cbDeliveryman = new ComboBox<>();

        cbDeliveryman.getItems().setAll(new DeliveryManDao().findAll());
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

    private void openView(String view, MouseEvent event) throws IOException {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(App.loadFXML(view));
        stage.setScene(scene);
        stage.centerOnScreen();
        stage.sizeToScene();
        stage.show();
    }
}