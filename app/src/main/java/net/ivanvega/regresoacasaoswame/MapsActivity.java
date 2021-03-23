package net.ivanvega.regresoacasaoswame;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.IndoorBuilding;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;

public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }



    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
mMap.setOnIndoorStateChangeListener(new GoogleMap.OnIndoorStateChangeListener() {
    @Override
    public void onIndoorBuildingFocused() {
        Toast.makeText(MapsActivity.this,
                "onIndoorBuildingFocused: " + mMap.getFocusedBuilding().getActiveLevelIndex(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onIndoorLevelActivated(IndoorBuilding indoorBuilding) {
        Toast.makeText(MapsActivity.this,
                "onIndoorLevelActivated: " + indoorBuilding.getActiveLevelIndex(), Toast.LENGTH_SHORT).show();
    }
});
        LatLng angelInd = new LatLng(41.8785774, -87.6356801);


        CameraPosition position = new CameraPosition.Builder()
                .target(angelInd)
                .bearing(45)
                .zoom(18)
                .tilt(70)
                .build();
        CameraUpdate campos = CameraUpdateFactory.newCameraPosition(position);
        mMap.animateCamera(campos);

                                        mMap.addMarker(new MarkerOptions().position(angelInd).title("Torre Willis"));

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_tipomapa, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if (item.getItemId() == R.id.Normal)
            mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);

        if (item.getItemId() == R.id.Satellite) {
            mMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);

        }
        if (item.getItemId() == R.id.Terrain) {
            mMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);

        }
        if (item.getItemId() == R.id.hybrid) {
            mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);

        }
        if (item.getItemId() == R.id.None) {
            mMap.setMapType(GoogleMap.MAP_TYPE_NONE);

        }
        if (item.getItemId() == R.id.MyLocation) {
          miUbicacion();
        }
        if (item.getItemId() == R.id.Polilinear) {
            startActivity(new Intent(getApplicationContext(), MapsActivityPolilineasMarcadores.class));

        }


        if (item.getItemId() == R.id.Traffic) {
            //mMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);

            item.setChecked(!item.isChecked() ) ;
            mMap.setTrafficEnabled(item.isChecked());
            Toast.makeText(this, "" + String.valueOf(item.isChecked()), Toast.LENGTH_SHORT).show();
        }
        if (item.getItemId() == R.id.MyLocation) {
            //mMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
            item.setChecked(!item.isChecked() ) ;
            Toast.makeText(this, "" + String.valueOf(item.isChecked()), Toast.LENGTH_SHORT).show();
        }
        if (item.getItemId() == R.id.Buildings) {
            //mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);

            item.setChecked(!item.isChecked() ) ;
            mMap.getUiSettings().setIndoorLevelPickerEnabled(false);
            Toast.makeText(this, "" + String.valueOf(item.isChecked()), Toast.LENGTH_SHORT).show();
        }
        if (item.getItemId() == R.id.indoor) {
            //mMap.setMapType(GoogleMap.MAP_TYPE_NONE);
            item.setChecked(!item.isChecked() ) ;
            mMap.setIndoorEnabled(item.isChecked());
            Toast.makeText(this, "" + String.valueOf(item.isChecked()), Toast.LENGTH_SHORT).show();
        }


        return true;
    }

    private FusedLocationProviderClient mFusedLocationClient;

    private void miUbicacion() {

        mFusedLocationClient.getLastLocation()
                .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        // Got last known location. In some rare situations this can be null.
                        if (location != null) {
                            // Logic to handle location object
                            Toast.makeText(MapsActivity.this,
                                    "Mi última unicación conocisa es Latitud: "
                                            + String.valueOf( location.getLatitude()) +
                                            ", Longitud: " + location.getLongitude()
                                    ,Toast.LENGTH_LONG).show();

                            LatLng ll = new LatLng(location.getLatitude(),
                                    location.getLongitude());

                            mMap.addMarker(
                                    new MarkerOptions().position(ll).
                                            title("Mi ubicación actual")
                            );

                            mMap.animateCamera(
                                    CameraUpdateFactory.newLatLngZoom(ll, 15)
                            );

                        }else{
                            Toast.makeText(MapsActivity.this,
                                    "Niguas de ubicación "
                                    ,Toast.LENGTH_LONG).show();
                        }
                    }
                });


    }
}
