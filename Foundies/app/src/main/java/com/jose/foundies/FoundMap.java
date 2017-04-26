package com.jose.foundies;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class FoundMap extends FragmentActivity implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {


    private GoogleMap mMap;
    private PlaceAutocompleteFragment autocompleteFragment;
    private GoogleApiClient mGoogleApiClient;
    private Location myLocation;
    private int radiusValue = 10;
    private SeekBar radius;
    private TextView radiusText;
    private LatLng centerLatLng;
    private Controller controller = null;
    Place chosenLocation;
    Location center = new Location("");

    public static boolean isLost() {
        return isLost;
    }

    public static void setIsLost(boolean isLost) {
        FoundMap.isLost = isLost;
    }

    private static boolean isLost = false;

    @Override
    protected void onStart() {
        super.onStart();
        new Thread(new Runnable() {
            @Override
            public void run() {
                mGoogleApiClient.connect();
            }
        }).start();
    }

    @Override
    protected void onStop() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                mGoogleApiClient.disconnect();
            }
        }).start();
        super.onStop();
    }


    @Override
    public void onConnected(Bundle bundle) {
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            myLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);

        } else {
            //TODO: Need to figure out how to ask user for permission
        }

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_found_map);
        controller = (Controller) getApplicationContext();
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        new Thread(new Runnable() {
            @Override
            public void run() {
                mGoogleApiClient.connect();
            }
        }).start();
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        // Create the AlertDialog
        final AlertDialog dialog = builder.create();

        dialog.setButton(AlertDialog.BUTTON_POSITIVE, "OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialog.cancel();
            }
        });

        mMap = googleMap;
        if(myLocation != null) {
            LatLng currentLocation = new LatLng(myLocation.getLatitude(), myLocation.getLongitude());
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(currentLocation, 10));
        }

        System.out.println("Jason: " + controller.getLatitude() + " " + controller.getLongitude());
        if(controller.getLatitude() != 0.0 && controller.getLongitude() != 0.0){
            LatLng currentLocation = new LatLng(controller.getLatitude(), controller.getLongitude());
            if(currentLocation != null) {
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(currentLocation, 13));
                center.setLatitude(controller.getLatitude());
                center.setLongitude(controller.getLongitude());
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            foundItems(center);
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                    }
                }).start();
            }
        }

        mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
            @Override
            public void onInfoWindowClick(Marker marker) {
                Toast toast = Toast.makeText(getApplicationContext(), "Window Clicked!", Toast.LENGTH_SHORT);
                toast.show();
            }
        });

        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            mMap.setMyLocationEnabled(true);

        } else {
            //TODO: Need to figure out how to ask user for permission
        }
        autocompleteFragment = (PlaceAutocompleteFragment)
                getFragmentManager().findFragmentById(R.id.address);
        radius = (SeekBar) findViewById(R.id.seekBar);
        radius.setProgress(100);
        radiusText = (TextView) findViewById(R.id.changeRadius);
        final TextView mileValue = (TextView) findViewById(R.id.mileValue);
        radius.setVisibility(View.GONE);
        radiusText.setVisibility(View.GONE);
        mileValue.setVisibility(View.GONE);

        Button backButton = (Button) findViewById(R.id.backButton);
        final Button nextButton = (Button) findViewById(R.id.nextButton);

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getBaseContext(), AdditionalDetails.class);
                startActivity(i);
                finish();
            }
        });

        if(isLost) {
            dialog.setMessage("1. Select a location where you lost your item\n\n" +
                    "2. Click on a marker or the save item button to find a match or post an item");
            dialog.show();
            mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                @Override
                public boolean onMarkerClick(Marker marker) {
                    ItemDialog dialogBox = new ItemDialog();
                    dialogBox.setContext(getApplicationContext());
                    LatLng chosenMarker = marker.getPosition();
                    Location location = new Location(LocationManager.GPS_PROVIDER);
                    List<Address> addresses = null;
                    Geocoder geocoder = new Geocoder(FoundMap.this, Locale.getDefault());
                    try{
                        addresses = geocoder.getFromLocation(chosenMarker.latitude, chosenMarker.longitude, 1);
                    }
                    catch(IOException e){
                        e.printStackTrace();
                    }
                    dialogBox.setAddress(addresses.get(0).getAddressLine(0));
                    dialogBox.show(getFragmentManager(), "test");
                    controller.setLatLong(marker.getPosition().latitude, marker.getPosition().longitude);
                    return false;
                }
            });

            radius.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                    radiusValue = i / 10;
                    String text = radiusValue + " mi";
                    mileValue.setText(text);
                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {

                }

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {
                    mMap.clear();
                    try {
                        foundItems(center);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    //Toast.makeText(getApplicationContext(), String.valueOf(radiusValue),Toast.LENGTH_LONG).show();
                }
            });

            autocompleteFragment.setHint("Select Location");


            autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
                @Override
                public void onPlaceSelected(Place place) {
                    // TODO: Get info about the selected place.
                    mMap.clear();
                    chosenLocation = place;
                    Log.i("FoundMap", "Place: " + place.getName());

                    radius.setVisibility(View.VISIBLE);
                    radiusText.setVisibility(View.VISIBLE);
                    mileValue.setVisibility(View.VISIBLE);

                    LatLng locationText = place.getLatLng();
                    List<Address> addressList = null;
                    Address address = null;
                    if(locationText != null && !locationText.equals("")){
                        try {
                            centerLatLng = new LatLng(place.getLatLng().latitude, place.getLatLng().longitude);
                            center.setLatitude(place.getLatLng().latitude);
                            center.setLongitude(place.getLatLng().longitude);
                            controller.setLatLong(place.getLatLng().latitude, place.getLatLng().longitude);
                            try {
                                foundItems(center);
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(centerLatLng, 13));
                            mMap.addMarker(new MarkerOptions().position(centerLatLng).title(place.getName().toString())
                                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));
                        }
                        catch(NullPointerException e){

                            /*
                                Print out "No results!" to device if no address was found
                             */
                            Toast toast = Toast.makeText(getApplicationContext(), "No Results!", Toast.LENGTH_SHORT);
                            toast.show();
                            e.printStackTrace();
                        }
                    }
                }

                @Override
                public void onError(Status status) {
                    // TODO: Handle the error.
                    Log.i("FoundMap", "An error occurred: " + status);
                }
            });
            nextButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //FoundConfirmation.setIsLost(true);
                    if(chosenLocation != null) {
                        Intent i = new Intent(getBaseContext(), LostConfirmation.class);
                        startActivity(i);
                        finish();
                    }
                    else{
                        Toast toast = Toast.makeText(getApplicationContext(), "Select Location", Toast.LENGTH_SHORT);
                        toast.show();
                    }
                }
            });
        }
        else{
            dialog.setMessage("Select the location where you found the item");
            dialog.show();
            nextButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    FoundConfirmation.setIsLost(false);
                    if(chosenLocation != null) {
                        Intent i = new Intent(getBaseContext(), FoundConfirmation.class);
                        startActivity(i);
                        finish();
                    }
                    else{
                        Toast toast = Toast.makeText(getApplicationContext(), "Select Location", Toast.LENGTH_SHORT);
                        toast.show();
                    }
                }
            });

            autocompleteFragment.setHint("Select Location");
            autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
                @Override
                public void onPlaceSelected(Place place) {
                    // TODO: Get info about the selected place.
                    mMap.clear();
                    Log.i("FoundMap", "Place: " + place.getName());
                    chosenLocation = place;
                    LatLng locationText = place.getLatLng();
                    List<Address> addressList = null;
                    Address address = null;
                    if(locationText != null && !locationText.equals("")){
                        try {
                            LatLng dropPin = new LatLng(place.getLatLng().latitude, place.getLatLng().longitude);
                            mMap.addMarker(new MarkerOptions().position(dropPin).title(place.getName().toString())
                            .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)));
                            controller.setLatLong(dropPin.latitude, dropPin.longitude);
                            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(dropPin, 15));
                        }
                        catch(NullPointerException e){
                            /*
                                Print out "No results!" to device if no address was found
                             */
                            Toast toast = Toast.makeText(getApplicationContext(), "No Results!", Toast.LENGTH_SHORT);
                            toast.show();
                            e.printStackTrace();
                        }
                    }
                }

                @Override
                public void onError(Status status) {
                    // TODO: Handle the error.
                    Log.i("FoundMap", "An error occurred: " + status);
                }
            });
        }
    }

    public void foundItems(Location loc) throws ParseException {
        System.out.println("JASON " + controller.getAnswersString());
        ArrayList<Item> itemsFound = controller.getFoundItems();
        System.out.println("Jason: " + itemsFound.size());
        for(Item item : itemsFound){
            LatLng dropPin = new LatLng(item.getLatitude(), item.getLongitude());
            Location location = new Location("");
            location.setLatitude(dropPin.latitude);
            location.setLongitude(dropPin.longitude);
            Geocoder geocoder;
            List<Address> addresses = null;
            geocoder = new Geocoder(this, Locale.getDefault());
            try{
                addresses = geocoder.getFromLocation(dropPin.latitude, dropPin.longitude, 1);
            }
            catch(IOException e){
                e.printStackTrace();
            }


            double distanceBetween = loc.distanceTo(location) / 1000 * 0.62137;
            if(distanceBetween < radiusValue) {
                mMap.addMarker(new MarkerOptions().position(dropPin).title(addresses.get(0).getAddressLine(0)));
            }
            /*if(myLocation != null) {
                LatLng currentLocation = new LatLng(myLocation.getLatitude(), myLocation.getLongitude());
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(currentLocation, 10));
            }
            else{
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(dropPin, 1));
            }*/

        }
}
}
