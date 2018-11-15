package cl.project.myapplication;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.loopj.android.http.AsyncHttpClient;

public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback {
    @Override
    public void onMapReady(GoogleMap googleMap) {
        Log.d(TAG, "onMapReady: Mapa iniciado");
        mMap = googleMap;

        if (mLocationPermissionsGranted) {
            getDeviceLocation();

            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this,
                    Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            mMap.setMyLocationEnabled(true);
            mMap.getUiSettings().setMyLocationButtonEnabled(true);
            mMap.getUiSettings().setZoomControlsEnabled(true);


            String c = getIntent().getExtras().getString("a");

            if(c.equals("c")) {
                printToast("Bienvendio a la mejor App");
            }else{
                getIntent().getExtras().getString("a");
                if (c.equals("a")) {

                    mostrarLineaIdaD();

                } else if (c.equals("b")) {

                    mostrarLinea();

                }
            }


        }







        }


    private static final String TAG = "MapsActivity";
    private static final String FINE_LOCATION = Manifest.permission.ACCESS_FINE_LOCATION;
    private static final String COURSE_LOCATION = Manifest.permission.ACCESS_COARSE_LOCATION;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1234;
    private static final float DEFAULT_ZOOM = 15f;
    private Boolean mLocationPermissionsGranted = false;
    public GoogleMap mMap;
    private AsyncHttpClient client = new AsyncHttpClient();
    private FusedLocationProviderClient mFusedLocationProviderClient;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        getLocationPermission();
    }

    //mi Ubicacion
    private void getDeviceLocation() {
        Log.d(TAG, "getDeviceLocation: getting the devices current location");

        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        try {
            if (mLocationPermissionsGranted) {

                final Task location = mFusedLocationProviderClient.getLastLocation();
                location.addOnCompleteListener(new OnCompleteListener() {
                    @Override
                    public void onComplete(@NonNull Task task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "onComplete: found location!");
                            Location currentLocation = (Location) task.getResult();

                            moveCamera(new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude()),
                                    15);

                        } else {
                            Log.d(TAG, "onComplete: current location is null");
                            Toast.makeText(MapsActivity.this, "unable to get current location", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        } catch (SecurityException e) {
            Log.e(TAG, "getDeviceLocation: SecurityException: " + e.getMessage());
        }
    }

    //zoom
    private void moveCamera(LatLng latLng, float zoom) {
        Log.d(TAG, "moveCamera: moving the camera to: lat: " + latLng.latitude + ", lng: " + latLng.longitude);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoom));
    }

    //iniciar mapa
    private void initMap() {
        Log.d(TAG, "initMap: initializing map");
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);

        mapFragment.getMapAsync(MapsActivity.this);
    }

    //permisos
    private void getLocationPermission() {
        Log.d(TAG, "getLocationPermission: getting location permissions");
        String[] permissions = {Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION};

        if (ContextCompat.checkSelfPermission(this.getApplicationContext(),
                FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            if (ContextCompat.checkSelfPermission(this.getApplicationContext(),
                    COURSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                mLocationPermissionsGranted = true;
                initMap();
            } else {
                ActivityCompat.requestPermissions(this,
                        permissions,
                        LOCATION_PERMISSION_REQUEST_CODE);
            }
        } else {
            ActivityCompat.requestPermissions(this,
                    permissions,
                    LOCATION_PERMISSION_REQUEST_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        Log.d(TAG, "onRequestPermissionsResult: called.");
        mLocationPermissionsGranted = false;

        switch (requestCode) {
            case LOCATION_PERMISSION_REQUEST_CODE: {
                if (grantResults.length > 0) {
                    for (int i = 0; i < grantResults.length; i++) {
                        if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                            mLocationPermissionsGranted = false;
                            Log.d(TAG, "onRequestPermissionsResult: permission failed");
                            return;
                        }
                    }
                    Log.d(TAG, "onRequestPermissionsResult: permission granted");
                    mLocationPermissionsGranted = true;
                    //initialize our map
                    initMap();
                }
            }
        }
    }

    public void btnTarifa(View view) {
        Intent intent = new Intent(MapsActivity.this, TarifaActivity.class);
        startActivity(intent);

    }

    public void btnHorario(View view) {
        Intent intent = new Intent(MapsActivity.this, HorarioActivity.class);
        startActivity(intent);
    }

    public void btnLinea(View view) {
    Intent intent = new Intent(MapsActivity.this, RecorridoActivity.class);
        finish();
        startActivity(intent);
    }


    public void mostrarLinea() {
        Polyline lineaDvuelta = mMap.addPolyline(new PolylineOptions()
                .add(
                        new LatLng(-35.459253, -71.694168),
                        new LatLng(-35.459477, -71.693567),
                        new LatLng(-35.455259, -71.692125),
                        new LatLng(-35.453316, -71.690504),
                        new LatLng(-35.453223, -71.689674),
                        new LatLng(-35.453773, -71.686634),
                        new LatLng(-35.453717, -71.685352),
                        new LatLng(-35.454552, -71.683972),
                        new LatLng(-35.456922, -71.685537),
                        new LatLng(-35.456915, -71.682366),
                        new LatLng(-35.448989, -71.682100),
                        new LatLng(-35.448743, -71.682011),
                        new LatLng(-35.445291, -71.680151),
                        new LatLng(-35.444973, -71.680020),
                        new LatLng(-35.444769, -71.679676),
                        new LatLng(-35.444370, -71.679343),
                        new LatLng(-35.445563, -71.676919),
                        new LatLng(-35.441810, -71.675466),
                        new LatLng(-35.441585, -71.675210),
                        new LatLng(-35.434387, -71.672426),
                        new LatLng(-35.434179, -71.672974),
                        new LatLng(-35.430268, -71.671421),
                        new LatLng(-35.429155, -71.672176),
                        new LatLng(-35.427923, -71.670326),
                        new LatLng(-35.428959, -71.657553),
                        new LatLng(-35.429175, -71.657251),
                        new LatLng(-35.429282, -71.655500),
                        new LatLng(-35.429124, -71.655391),
                        new LatLng(-35.429498, -71.651178),
                        new LatLng(-35.429766, -71.651069),
                        new LatLng(-35.429779, -71.650647),
                        new LatLng(-35.427286, -71.650079),
                        new LatLng(-35.428147, -71.640414),
                        new LatLng(-35.428291, -71.640237),
                        new LatLng(-35.429068, -71.640748),
                        new LatLng(-35.429362, -71.640766),
                        new LatLng(-35.431124, -71.639859),
                        new LatLng(-35.432794, -71.634522),
                        new LatLng(-35.429188, -71.632693),
                        new LatLng(-35.428773, -71.632607),
                        new LatLng(-35.426207, -71.631266),
                        new LatLng(-35.423362, -71.628552),
                        new LatLng(-35.424389, -71.627098),
                        new LatLng(-35.424612, -71.626996),
                        new LatLng(-35.424284, -71.625242),
                        new LatLng(-35.424792, -71.625250),
                        new LatLng(-35.429584, -71.627410),
                        new LatLng(-35.429112, -71.628998)));

        lineaDvuelta.setTag("linea D vuelta");
        printToast("pase por mostrar linea");


    }


    public void mostrarLineaIdaD() {


        Polyline lineaD = mMap.addPolyline(new PolylineOptions()
                .add(
                        new LatLng(-35.429109, -71.629066),
                        new LatLng(-35.429012, -71.629354),
                        new LatLng(-35.426853, -71.628407),
                        new LatLng(-35.427509, -71.625990),
                        new LatLng(-35.424836, -71.624899),
                        new LatLng(-35.424203, -71.624867),
                        new LatLng(-35.424609, -71.627002),
                        new LatLng(-35.424245, -71.627223),
                        new LatLng(-35.423357, -71.628558),
                        new LatLng(-35.428717, -71.632591),
                        new LatLng(-35.432679, -71.634478),
                        new LatLng(-35.431053, -71.639648),
                        new LatLng(-35.429452, -71.640636),
                        new LatLng(-35.429132, -71.641084),
                        new LatLng(-35.428804, -71.645343),
                        new LatLng(-35.425883, -71.645072),
                        new LatLng(-35.425650, -71.645303),
                        new LatLng(-35.425472, -71.646074),
                        new LatLng(-35.425692, -71.647591),
                        new LatLng(-35.425820, -71.647755),
                        new LatLng(-35.426352, -71.647838),
                        new LatLng(-35.426147, -71.650087),
                        new LatLng(-35.428367, -71.650282),
                        new LatLng(-35.429239, -71.650555),
                        new LatLng(-35.429192, -71.650831),
                        new LatLng(-35.429257, -71.651085),
                        new LatLng(-35.429456, -71.651207),
                        new LatLng(-35.429131, -71.655392),
                        new LatLng(-35.428898, -71.655489),
                        new LatLng(-35.428759, -71.657285),
                        new LatLng(-35.428937, -71.657628),
                        new LatLng(-35.427894, -71.670542),
                        new LatLng(-35.429244, -71.672511),
                        new LatLng(-35.433098, -71.675189),
                        new LatLng(-35.433198, -71.675176),
                        new LatLng(-35.434397, -71.672413),
                        new LatLng(-35.441566, -71.675210),
                        new LatLng(-35.441817, -71.675460),
                        new LatLng(-35.440738, -71.677850),
                        new LatLng(-35.442431, -71.679005),
                        new LatLng(-35.443560, -71.678862),
                        new LatLng(-35.444052, -71.679089),
                        new LatLng(-35.444776, -71.679701),
                        new LatLng(-35.444970, -71.680017),
                        new LatLng(-35.445340, -71.680171),
                        new LatLng(-35.447252, -71.681824),
                        new LatLng(-35.456964, -71.682381),
                        new LatLng(-35.457967, -71.682943),
                        new LatLng(-35.456920, -71.685559),
                        new LatLng(-35.453963, -71.683781),
                        new LatLng(-35.453703, -71.685559),
                        new LatLng(-35.453804, -71.686563),
                        new LatLng(-35.453217, -71.689777),
                        new LatLng(-35.453296, -71.690477),
                        new LatLng(-35.454010, -71.690948),
                        new LatLng(-35.459253, -71.694168)));

        lineaD.setTag("linea D");

    }

    public void printToast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }


}

