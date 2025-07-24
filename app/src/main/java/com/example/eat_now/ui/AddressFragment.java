package com.example.eat_now.ui;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import com.example.eat_now.R;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class AddressFragment extends Fragment implements OnMapReadyCallback {

    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1001;

    private MapView mapView;
    private GoogleMap googleMap;
    private FusedLocationProviderClient fusedLocationClient;
    private TextView tvCurrentAddress;
    private FloatingActionButton fabCurrentLocation;
    private List<Restaurant> restaurants;

    public AddressFragment() {
        // Required empty public constructor
    }

    public static AddressFragment newInstance() {
        return new AddressFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity());
        initializeRestaurants(); // Initialize sample restaurant data
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_address, container, false);

        // Initialize views
        mapView = view.findViewById(R.id.mapView);
        tvCurrentAddress = view.findViewById(R.id.tvCurrentAddress);
        fabCurrentLocation = view.findViewById(R.id.fabCurrentLocation);

        // Initialize map
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);

        // Set click listener for current location button
        fabCurrentLocation.setOnClickListener(v -> {
            getCurrentLocation();
            // Accessibility announcement
            announceForAccessibility("Centering map on your current location");
        });

        return view;
    }

    @Override
    public void onMapReady(@NonNull GoogleMap map) {
        googleMap = map;

        // Enable zoom controls
        googleMap.getUiSettings().setZoomControlsEnabled(true);
        googleMap.getUiSettings().setMyLocationButtonEnabled(false); // We have our own button

        // Set accessibility content description for map
        updateMapAccessibilityDescription("Map loaded with restaurant locations");

        // Check permissions and enable location
        if (checkLocationPermission()) {
            enableMyLocation();
            getCurrentLocation();
        } else {
            requestLocationPermission();
        }

        // Add restaurant markers
        addRestaurantMarkers();
    }

    private boolean checkLocationPermission() {
        return ActivityCompat.checkSelfPermission(requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED;
    }

    private void requestLocationPermission() {
        ActivityCompat.requestPermissions(requireActivity(),
                new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                LOCATION_PERMISSION_REQUEST_CODE);
    }

    private void enableMyLocation() {
        if (checkLocationPermission()) {
            googleMap.setMyLocationEnabled(true);
        }
    }

    private void getCurrentLocation() {
        if (!checkLocationPermission()) {
            requestLocationPermission();
            return;
        }

        fusedLocationClient.getLastLocation()
                .addOnSuccessListener(location -> {
                    if (location != null) {
                        LatLng currentLatLng = new LatLng(location.getLatitude(), location.getLongitude());

                        // Move camera to current location
                        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(currentLatLng, 15f));

                        // Get address from coordinates
                        getAddressFromLocation(location.getLatitude(), location.getLongitude());

                    } else {
                        Toast.makeText(getContext(), "Unable to get current location", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(getContext(), "Failed to get location: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    announceForAccessibility("Failed to get location");
                });
    }

    private void getAddressFromLocation(double latitude, double longitude) {
        Geocoder geocoder = new Geocoder(requireContext(), Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(latitude, longitude, 1);
            if (addresses != null && !addresses.isEmpty()) {
                Address address = addresses.get(0);
                String addressText = address.getAddressLine(0);
                String displayText = "Current Location: " + addressText;
                tvCurrentAddress.setText(displayText);

                // Update accessibility
                tvCurrentAddress.setContentDescription("Your current address is " + addressText);
                announceForAccessibility("Address updated: " + addressText);
            }
        } catch (IOException e) {
            tvCurrentAddress.setText("Unable to get address");
            tvCurrentAddress.setContentDescription("Unable to get current address");
        }
    }

    private void addRestaurantMarkers() {
        if (googleMap == null || restaurants == null) return;

        for (Restaurant restaurant : restaurants) {
            LatLng restaurantLatLng = new LatLng(restaurant.getLatitude(), restaurant.getLongitude());

            MarkerOptions markerOptions = new MarkerOptions()
                    .position(restaurantLatLng)
                    .title(restaurant.getName())
                    .snippet(restaurant.getAddress())
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));

            googleMap.addMarker(markerOptions);
        }

        // Accessibility announcement
        announceForAccessibility(restaurants.size() + " restaurants loaded on map");
    }

    private void initializeRestaurants() {
        // Sample restaurant data - replace with your actual data from database/API
        restaurants = new ArrayList<>();
        restaurants.add(new Restaurant("Pizza Palace", "123 Main St", 23.7808, 90.4142));
        restaurants.add(new Restaurant("Burger King", "456 Oak Ave", 23.7858, 90.4192));
        restaurants.add(new Restaurant("Sushi House", "789 Pine Rd", 23.7758, 90.4092));
        // Add more restaurants as needed
    }

    private void announceForAccessibility(String message) {
        if (getView() != null) {
            getView().announceForAccessibility(message);
        }
    }

    private void updateMapAccessibilityDescription(String description) {
        if (mapView != null) {
            mapView.setContentDescription(description);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                enableMyLocation();
                getCurrentLocation();
                announceForAccessibility("Location permission granted");
            } else {
                Toast.makeText(getContext(), "Location permission denied", Toast.LENGTH_SHORT).show();
                announceForAccessibility("Location permission denied");
            }
        }
    }

    // MapView lifecycle methods
    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

    // Restaurant model class
    public static class Restaurant {
        private String name;
        private String address;
        private double latitude;
        private double longitude;

        public Restaurant(String name, String address, double latitude, double longitude) {
            this.name = name;
            this.address = address;
            this.latitude = latitude;
            this.longitude = longitude;
        }

        // Getters
        public String getName() { return name; }
        public String getAddress() { return address; }
        public double getLatitude() { return latitude; }
        public double getLongitude() { return longitude; }
    }
}