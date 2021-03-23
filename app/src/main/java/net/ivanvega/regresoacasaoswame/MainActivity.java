package net.ivanvega.regresoacasaoswame;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.maps.model.LatLng;

public class MainActivity extends AppCompatActivity implements MapasFragment.OnFragmentInteractionListener {
    private static final int REQUEST_CHECK_SETTINGS = 1001;
    LocationRequest locationRequest;
    private FusedLocationProviderClient fusedLocationClient;
    LocationSettingsRequest.Builder builder;
    TextView txt;
    final private int REQUEST_CODE_ASK_PERMISSION = 0;
    private boolean requestingLocationUpdates = true;
    public LatLng ubicacion;
    public Button btnRuta;
    EditText editText;
    EditText editText1;

    public void click(View v) {
        startActivity(new Intent(getApplicationContext(), MapsActivity.class));
    }

    private LocationCallback locationCallback = new LocationCallback() {
        @Override
        public void onLocationResult(LocationResult locationResult) {
            if (locationResult == null) {
                return;
            }
            for (Location location : locationResult.getLocations()) {
                // Update UI with location data
                // ...
                String msj = "Latitud: " + String.valueOf(location.getLatitude())
                        + "\nLongitud: " + location.getLongitude();
                Toast.makeText(MainActivity.this, msj,
                        Toast.LENGTH_LONG).show();
                Log.i("POSICION", msj);
                txt.setText(msj);
            }
        }

        ;
    };


    public void button(View v){

        Fragment fragmento = new MapasFragmentDos(editText.getText().toString(),editText1.getText().toString());
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment, fragmento).commit();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
       editText = (EditText) findViewById(R.id.latitud);
       editText1 = (EditText) findViewById(R.id.longitud);



        MapasFragment mp = new MapasFragment();
        Fragment fragmento = new MapasFragment();
        ubicacion = mp.UbicacionDestino;
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment, fragmento).commit();
    }





}