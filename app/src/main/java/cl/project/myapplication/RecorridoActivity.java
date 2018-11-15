package cl.project.myapplication;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.ActionBar;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.Toast;


public class RecorridoActivity extends AppCompatActivity {
    RadioButton c1;
    RadioButton c2;
    Spinner comboLineas;


    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recorrido);
        c1 = (RadioButton) findViewById(R.id.idRadioIda);
        c2 = (RadioButton) findViewById(R.id.idRadioVuelta);
        comboLineas = (Spinner) findViewById(R.id.idSpinnerLineas);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.lineas, android.R.layout.simple_spinner_item);

        comboLineas.setAdapter(adapter);
        comboLineas.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int i, long l) {


            }


            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        setupActionBar();

    }


    private void setupActionBar() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle("Volver");

        }
    }


    public void buttonSelectLinea(View view) {


        int a = comboLineas.getSelectedItemPosition();

        if (a == 12 && c1.isChecked() == true) {
            Intent intent = new Intent(RecorridoActivity.this, MapsActivity.class);
            intent.putExtra("a", "a");
            startActivity(intent);
            finish();


        }
        if (a == 12 && c2.isChecked() == true) {
            Intent intent = new Intent(RecorridoActivity.this, MapsActivity.class);
            intent.putExtra("a", "b");
            startActivity(intent);
            finish();


        }
        if (a != 12) {
            printToast("Seleccione linea D");
        }
    }

    public void printToast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

}
