package net.ivanvega.regresoacasaoswame;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.maps.android.PolyUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MapasFragmentDos#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MapasFragmentDos extends Fragment implements OnMapReadyCallback, GoogleMap.OnMapLongClickListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    public LatLng UbicacionDestino, UbicacionActual;
    public String latitud;
    public String longitud;
    private Marker MarcadorDestino, MarcadorOrigen;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public MapasFragmentDos(){

    }


    public MapasFragmentDos(String lat,String Longitud) {
        // Required empty public constructor
       latitud = lat;
       longitud = Longitud;
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MapasFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MapasFragmentDos newInstance(String param1, String param2) {
        MapasFragmentDos fragment = new MapasFragmentDos();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v= inflater.inflate(R.layout.fragment_mapas, container, false);
        SupportMapFragment mapFragment=(SupportMapFragment) this.getChildFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        return v;
    }

    GoogleMap map;
    Boolean actualPosition=true;
    JSONObject jso;
    Double longitudOrigen, latitudOrigen;


    @Override
    public void onMapReady(GoogleMap googleMap) {
        map=googleMap;
        if(ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED){

            if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),
                    Manifest.permission.ACCESS_COARSE_LOCATION)) {

            } else {
                ActivityCompat.requestPermissions(getActivity(),
                        new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
                        1);
            }

            if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),
                    Manifest.permission.ACCESS_FINE_LOCATION)) {
            } else {
                ActivityCompat.requestPermissions(getActivity(),
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        1);
            }
            return;
        }

        map.setMyLocationEnabled(true);

        map.setOnMyLocationChangeListener(new GoogleMap.OnMyLocationChangeListener() {
            @Override
            public void onMyLocationChange(Location location) {
                if(actualPosition){
                    latitudOrigen=location.getLatitude();
                    longitudOrigen=location.getLongitude();
                    actualPosition=false;

                    LatLng miPosicion=new LatLng(latitudOrigen,longitudOrigen);
                    map.addMarker(new MarkerOptions().position(miPosicion).title("Mi Ubicacion"));

                    CameraPosition cameraPosotion= new CameraPosition.Builder()
                            .target(new LatLng(latitudOrigen,longitudOrigen))
                            .zoom(14)
                            .bearing(90)
                            .build();
                    map.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosotion));

                    String url="https://maps.googleapis.com/maps/api/directions/json?origin="+latitudOrigen+","+longitudOrigen+"&destination="+latitud+","+longitud+"&key=AIzaSyBHtYD_i3eqYqdCroUTQDwzb5FtqD323oc";

                    RequestQueue queue= Volley.newRequestQueue(getActivity());
                    StringRequest stringRequest= new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {

                            try {
                                jso= new JSONObject(response);
                                trazarRuta(jso);
                                Log.i("jsonRuta: ",""+response);

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {

                        }
                    });
                    queue.add(stringRequest);


                }
            }
        });


    }


    private void trazarRuta(JSONObject jso){
        JSONArray jRoutes;
        JSONArray jLegs;
        JSONArray jSteps;
        try{
            jRoutes=jso.getJSONArray("routes");
            for (int i=0; i<jRoutes.length(); i++){
                jLegs=((JSONObject) (jRoutes.get(i))).getJSONArray("legs");

                for (int j=0; j<jLegs.length();j++){
                    jSteps=((JSONObject) jLegs.get(j)).getJSONArray("steps");

                    for (int k=0; k<jSteps.length();k++){
                        String polyline=""+((JSONObject) ((JSONObject) jSteps.get(k)).get("polyline")).get("points");
                        Log.i("end",""+polyline);
                        List<LatLng> list= PolyUtil.decode(polyline);
                        map.addPolyline(new PolylineOptions().addAll(list).color(Color.GREEN).width(5));
                    }
                }
            }

        }catch (JSONException e){

        }

    }


    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case 1: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                } else {
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request.
        }
    }

    public void agregarMarcadorDestino(LatLng coordenadas) {
        if (MarcadorOrigen != null) MarcadorOrigen.remove();
        MarcadorOrigen = map.addMarker(new MarkerOptions()
                .position(coordenadas).title("Ubicación destino")
                .icon(BitmapDescriptorFactory.fromResource(R.mipmap.destino)));

    }

    @Override
    public void onMapLongClick(LatLng latLng) {
        UbicacionDestino = latLng;
        agregarMarcadorDestino(UbicacionDestino);
    }


    public interface OnFragmentInteractionListener {
    }
}
