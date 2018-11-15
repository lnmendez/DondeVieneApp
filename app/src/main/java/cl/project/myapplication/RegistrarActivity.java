package cl.project.myapplication;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;

import cz.msebera.android.httpclient.Header;

import org.json.JSONException;


public class RegistrarActivity extends AppCompatActivity {
    EditText enombre, eapellido, ecorreo, etelefono, epass;
    private AsyncHttpClient client = new AsyncHttpClient();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrar);
        enombre = (EditText) findViewById(R.id.registrarNombre);
        eapellido = (EditText) findViewById(R.id.registrarApellido);
        ecorreo = (EditText) findViewById(R.id.registrarCorreo);
        etelefono = (EditText) findViewById(R.id.registrarTelefono);
        epass = (EditText) findViewById(R.id.registrarPass);
        setupActionBar();

    }


    //hacia atras
    private void setupActionBar() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle("Atras");
        }
    }


    public void buttonRegistrar(View view) {
        if (enombre.getText().toString().isEmpty() || eapellido.getText().toString().isEmpty() || ecorreo.getText().toString().isEmpty()
                || etelefono.getText().toString().isEmpty() || epass.getText().toString().isEmpty()) {
            Toast.makeText(this, "Debe llenar todos los campos", Toast.LENGTH_SHORT).show();
        } else {
            crearUsuario();
        }


    }


    public void crearUsuario() {


        String url = "https://www.rendicionsostenedor.cl/add_usuario";
        RequestParams params = new RequestParams();
        params.add("nombre", enombre.getText().toString());
        params.add("apellidos", eapellido.getText().toString());
        params.add("mail", ecorreo.getText().toString());
        params.add("telefono", etelefono.getText().toString());
        params.add("clave", epass.getText().toString());

        client.post(url, params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                enombre.setText("");
                eapellido.setText("");
                ecorreo.setText("");
                etelefono.setText("");
                epass.setText("");
                String respuesta = new String(responseBody);
                try {
                    JSONArray json = new JSONArray(respuesta);
                    String result = json.getJSONObject(0).getString("msg");

                    printToast(result);
                    finish();

                } catch (JSONException e) {
                    e.printStackTrace();
                    printToast("Usuario agregado exitosamente");
                }

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

            }
        });

    }

    public void printToast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }
}