package dam.android.sergic.app2.ui.map;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.GroundOverlayOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.Objects;

import dam.android.sergic.app2.MainActivity;
import dam.android.sergic.app2.R;
import dam.android.sergic.app2.asyncTasks.AsyncFather;
import dam.android.sergic.app2.asyncTasks.GetLocationNames;
import dam.android.sergic.app2.db.BatoiLogicDBManager;
import dam.android.sergic.app2.models.Order;
import dam.android.sergic.app2.tools.Tools;

public class MapFragment extends SupportMapFragment implements OnMapReadyCallback, AsyncFather.AsyncResponse {
    private static final String TAG = MapFragment.class.getSimpleName();
    private static final int REQUEST_LOCATION_PERMISSION = 1;

    private View root;
    private GoogleMap mMap;
    private GetLocationNames locations;
    private BatoiLogicDBManager manager;
    private ArrayList<Order> orders;

    private Snackbar info;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        root = super.onCreateView(inflater, container, savedInstanceState);

        manager = new BatoiLogicDBManager(getContext());
        getMapAsync(this);
        return root;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        UiSettings uiSettings = mMap.getUiSettings();
        uiSettings.setZoomControlsEnabled(true);

        // Zoom Camera.
        LatLng distributionCentre = new LatLng(38.6910773, -0.4984189);
        mMap.addMarker(new MarkerOptions().position(distributionCentre)
                .title(getString(R.string.distribution_center)).icon(BitmapDescriptorFactory
                        .defaultMarker(BitmapDescriptorFactory.HUE_ORANGE)));

        // Apply zoom.
        float zoomLevel = 14F;
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(distributionCentre, zoomLevel));

        GroundOverlayOptions homeOverlay = new GroundOverlayOptions()
                .image(BitmapDescriptorFactory.fromResource(R.drawable.logo))
                .position(distributionCentre, 100);
        mMap.addGroundOverlay(homeOverlay);
        mMap.setMyLocationEnabled(true);

        setPoiClick(mMap);

        // Night mode.
        if ((getResources().getConfiguration().uiMode &
                Configuration.UI_MODE_NIGHT_MASK) == Configuration.UI_MODE_NIGHT_YES) {
            setMapStyle(mMap);
        }

        setupPoints();

    }

    private boolean checkPermissions() {
        // check for permissions granted before setting listView Adapter data
        if (ActivityCompat.checkSelfPermission(Objects.requireNonNull(getContext()), Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_BACKGROUND_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            // opens Dialog: requests user to grant permission.
            ActivityCompat.requestPermissions(Objects.requireNonNull(getActivity()), MainActivity.PERMISSIONS,
                    MainActivity.REQUEST_LOCATION_PERMISSION);
            return false;
        } else
            return true;
    }

    private boolean isLocationEnabled() {
        boolean location = true;
        LocationManager locationManager = (LocationManager) Objects.requireNonNull(getContext()).getSystemService(Context.LOCATION_SERVICE);
        if (locationManager != null && !(locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(
                LocationManager.NETWORK_PROVIDER))) {

            Toast.makeText(getContext(), getString(R.string.location_enable), Toast.LENGTH_SHORT).show();
            startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
            location = false;
        }

        return location;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions
            , @NonNull int[] grantResults) {
        if (requestCode == REQUEST_LOCATION_PERMISSION) {
            // We have requested one READ permission for contacts, so only need [0] to be checked.
            if (grantResults.length > 0 && grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(getContext(), getString(R.string.permissions_neccesary), Toast.LENGTH_LONG).show();
                Objects.requireNonNull(getActivity()).finish();
            }
        } else
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }


    private void setupPoints() {
        orders = manager.getOrders();
        locations = new GetLocationNames(getContext(), this, orders);
        locations.execute();

        info = Snackbar.make(root, getString(R.string.retrieving_information), Snackbar.LENGTH_LONG)
                .setAction(getString(R.string.retrieving_information), null);
        info.show();
    }

    @SuppressLint("StringFormatInvalid")
    @Override
    public void processFinish(ArrayList<?> output) {
        for (int i = 0; i < output.size(); i++) {
            LatLng point = (LatLng) output.get(i);

            if (point != null)
                mMap.addMarker(new MarkerOptions().position(point)
                        .title(orders.get(i).getCustomer().getName()).icon(BitmapDescriptorFactory
                                .defaultMarker(BitmapDescriptorFactory.HUE_RED)));
            else {
                // TODO HERE -->
                Tools.showAlert(getContext(), getString(R.string.location_not_found), getString(R.string.alert_location, orders.get(i).getOrderNumber(), orders.get(i).getCustomer().getName(), orders.get(i).getAddress()));
            }
        }
    }

    private void setPoiClick(final GoogleMap map) {
        map.setOnPoiClickListener(poi ->
        {
            Marker poiMarker = map.addMarker(new MarkerOptions()
                    .position(poi.latLng)
                    .title(poi.name));
            poiMarker.showInfoWindow();
            poiMarker.setTag(getString(R.string.poi));

        });
    }

    private void setMapStyle(GoogleMap map) {
        try {
            map.setMapStyle(MapStyleOptions.loadRawResourceStyle(Objects
                    .requireNonNull(getContext()), R.raw.map_style));
        } catch (Resources.NotFoundException e) {
            Log.e(TAG, "Can't find style. Error: ", e);
        }
    }

    private boolean isNetworkAvailable() {
        boolean networkAvailable = false;

        ConnectivityManager cm = (ConnectivityManager) Objects.requireNonNull(getContext()).getSystemService(Context.CONNECTIVITY_SERVICE);
        if (cm != null) {
            NetworkInfo networkInfo = cm.getActiveNetworkInfo();

            if (networkInfo != null && networkInfo.isConnected())
                networkAvailable = true;
        }

        return networkAvailable;
    }

    @Override
    public void onResume() {
        super.onResume();

        // Avoid app Crashing, if there is a dialog.
        if (Tools.dialog != null)
            Tools.dialog.dismiss();

        if (!isNetworkAvailable())
        {
            Toast.makeText(getContext(), getString(R.string.no_internet), Toast.LENGTH_SHORT).show();
            startActivity(new Intent(Settings.ACTION_WIRELESS_SETTINGS));
        }
        else if(checkPermissions() && !isLocationEnabled())
        {
            Toast.makeText(getContext(), getString(R.string.location_enable), Toast.LENGTH_SHORT).show();
            startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
        }
    }

    @Override
    public void onPause() {
        super.onPause();

        if (info != null)
            info.dismiss();

        if (locations != null)
            locations.cancel(true);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (manager != null)
            manager.close();
    }
}