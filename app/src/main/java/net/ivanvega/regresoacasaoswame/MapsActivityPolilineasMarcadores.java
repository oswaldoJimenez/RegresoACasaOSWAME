package net.ivanvega.regresoacasaoswame;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolygonOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.tasks.OnSuccessListener;

public class MapsActivityPolilineasMarcadores extends FragmentActivity
        implements OnMapReadyCallback, View.OnClickListener {

    private GoogleMap mMap;

    private Button btnMarcador, btnLinea, btnPoligono;

    private FusedLocationProviderClient mFusedLocationClient;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps_polilineas_marcadores);

        inicializarUI();

        mFusedLocationClient =
                LocationServices.getFusedLocationProviderClient(this);


        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map_markers);
        mapFragment.getMapAsync(this);

    }

    private void inicializarUI() {
        btnMarcador = (Button) findViewById(R.id.btnMarcador);
        btnMarcador.setOnClickListener(this);
        btnLinea = (Button) findViewById(R.id.btnLineas);
        btnLinea.setOnClickListener(this);
        btnPoligono = (Button) findViewById(R.id.btnPoligono);
        btnPoligono.setOnClickListener(this);
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

        // Add a marker in Sydney and move the camera



        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        
        mFusedLocationClient.getLastLocation()
                .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        // Got last known location. In some rare situations this can be null.
                        if (location != null) {
                            // Logic to handle location object
                            Toast.makeText(MapsActivityPolilineasMarcadores.this,
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
                            Toast.makeText(MapsActivityPolilineasMarcadores.this,
                                    "Niguas de ubicación "
                                    ,Toast.LENGTH_LONG).show();
                        }
                    }
                });



    }

    @Override
    public void onClick(View v) {
        if (v.equals(btnPoligono)){
            //dibuja un poligono spbre el mapa
            mostrarPoligono();
        }
        if (v.equals(btnLinea)){
            mostrarLineas();
        }
        if (v.equals(btnMarcador)){


            LatLng angelInd = new LatLng(19.426978, -99.167775);
            CameraPosition position = new CameraPosition.Builder()
                    .target(angelInd)
                    .bearing(45)
                    .zoom(19)
                    .tilt(70)
                    .build();
            CameraUpdate campos = CameraUpdateFactory.newCameraPosition(position);
            mMap.animateCamera(campos);


            mMap.addMarker(new MarkerOptions().position(angelInd).title("Angel de la independencia"));

        }

    }

    private void mostrarPoligono()
    {
        //Dibujo con Poligonos

        PolygonOptions rectangulo = new PolygonOptions()
                .add(new LatLng(45.0, -12.0),
                        new LatLng(45.0, 5.0),
                        new LatLng(34.5, 5.0),
                        new LatLng(34.5, -12.0),
                       new LatLng(45.0, -12.0));

        // Instantiates a new Polygon object and adds points to define a rectangle
        PolygonOptions rectOptions = new PolygonOptions()
                .add(new LatLng(37.35, -122.0),
                        new LatLng(37.45, -122.0),
                        new LatLng(37.45, -122.2),
                        new LatLng(37.35, -122.2),
                        new LatLng(37.35, -122.0));

        rectangulo.strokeWidth(8);
        rectangulo.strokeColor(Color.RED);

        mMap.addPolygon(rectOptions);

        CameraUpdate camera = CameraUpdateFactory.
                newLatLngZoom(new LatLng(37.35, -122.0),10);

        mMap.moveCamera(camera);
    }

    private void mostrarLineas()
    {

        PolylineOptions lineas = new PolylineOptions()
                .add(new LatLng(45.0, -12.0))
                .add(new LatLng(45.0, 5.0))
                .add(new LatLng(34.5, 5.0));

        lineas.width(8);
        lineas.color(Color.RED);

        mMap.addPolyline(lineas);

        LatLng angelInd = new LatLng(45.0, 5.0);
        CameraPosition position = new CameraPosition.Builder()
                .target(angelInd)
                .bearing(45)
                .zoom(19)
                .tilt(70)
                .build();
        CameraUpdate campos = CameraUpdateFactory.newCameraPosition(position);
        mMap.animateCamera(campos);


    }


}