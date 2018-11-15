package cl.project.myapplication;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.PolylineOptions;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import cz.msebera.android.httpclient.Header;

public class MainActivity extends AppCompatActivity {
    EditText lcorreo, lpass;
    private AsyncHttpClient client = new AsyncHttpClient();
    private static final String TAG = "MainActivity";
    private static final int ERROR_DIALOG_REQUEST = 9001;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        lcorreo = (EditText) findViewById(R.id.loginCorreo);
        lpass = (EditText) findViewById(R.id.loginPass);


    }


    public boolean isServicesOK() {
        Log.d(TAG, "isServicesOK: checking google services version");

        int available = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(MainActivity.this);

        if (available == ConnectionResult.SUCCESS) {
            //everything is fine and the user can make map requests
            Log.d(TAG, "isServicesOK: Google Play Services is working");
            return true;
        } else if (GoogleApiAvailability.getInstance().isUserResolvableError(available)) {
            //an error occured but we can resolve it
            Log.d(TAG, "isServicesOK: an error occured but we can fix it");
            Dialog dialog = GoogleApiAvailability.getInstance().getErrorDialog(MainActivity.this, available, ERROR_DIALOG_REQUEST);
            dialog.show();
        } else {
            Toast.makeText(this, "You can't make map requests", Toast.LENGTH_SHORT).show();
        }
        return false;
    }

    public void buttonLogin(View view) {
        if (lcorreo.getText().toString().isEmpty() || lpass.getText().toString().isEmpty()) {
            Toast.makeText(this, "Debe llenar todos los campos", Toast.LENGTH_SHORT).show();
        } else {
            procesaSesion();
        }


    }

    public void procesaSesion() {
        String url = "https://www.rendicionsostenedor.cl/iniciarsesion";
        RequestParams params = new RequestParams();
        params.add("mail", lcorreo.getText().toString());
        params.add("pass", lpass.getText().toString());
        client.post(url, params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                if (statusCode == 200) {

                    String respuesta = new String(responseBody);
                    String b = respuesta.toString().replace("{", "");
                    String c = b.toString().replace("}", "");
                    String d = c.toString().replace("\"", "");
                    String valido = "result:Usuario valido";
                    if (d.equals(valido)) {
                        if (isServicesOK()) {
                            Intent intent = new Intent(MainActivity.this, MapsActivity.class);
                            intent.putExtra("a", "c");
                            startActivity(intent);
                            finish();

                        }
                    } else {
                        printToast("Correo o contraseña invalida");
                    }

                    /**   try {

                     JSONArray json = new JSONArray(respuesta);
                     String result = json.getJSONObject(0).getString("result:");

                     printToast(result);
                     finish();
                     /*
                     JSONArray json = new JSONArray(respuesta);
                     if (isServicesOK()){
                     Button btnLogin = (Button) findViewById(R.id.btnLogin);
                     Intent intent = new Intent(MainActivity.this, MapsActivity.class);
                     startActivity(intent);
                     }


                     } catch (JSONException e) {
                     e.printStackTrace();
                     printToast("Usuario incorrecto");
                     }*/


                }

            }


            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

            }
        });


    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK) {

            new AlertDialog.Builder(this)
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .setTitle("Salir")
                    .setMessage("Estás seguro?")
                    .setNegativeButton(android.R.string.cancel, null)// sin listener
                    .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {// un listener que al pulsar, cierre la aplicacion
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
// Salir
                            MainActivity.this.finish();
                        }
                    })
                    .show();

// Si el listener devuelve true, significa que el evento esta procesado, y nadie debe hacer nada mas
            return true;
        }
// para las demas cosas, se reenvia el evento al listener habitual
        return super.onKeyDown(keyCode, event);
    }

    public void printToast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    public void btnregisterr(View view) {

        Intent intent = new Intent(MainActivity.this, RegistrarActivity.class);
        startActivity(intent);

    }
}
