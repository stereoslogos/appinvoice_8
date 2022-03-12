package com.example.appinvoice_8;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    EditText etidinvoice, etidcust, etcoste, etbalance;
    Button btnsave,btnsearch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        etidinvoice = findViewById(R.id.etidinvoice);
        etidcust = findViewById(R.id.etidcust);
        etcoste = findViewById(R.id.etcoste);
        etbalance = findViewById(R.id.etbalance);
        btnsave = findViewById(R.id.btnsave);
        btnsearch = findViewById(R.id.btnsearch);

        btnsave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String xidcust = etidcust.getText().toString();
                String xcoste = etcoste.getText().toString();
                String xbalance = etbalance.getText().toString();
                saveinvoice(xidcust, xcoste, xbalance);
            }
        });
    }

    private void saveinvoice(String xidcust, String xcoste, String xbalance) {
        // Instanciar la clase invoice
        invoice sohInvoice = new invoice(getApplicationContext(),"dbinvoice",null,1);
        SQLiteDatabase db = sohInvoice.getReadableDatabase();
        String query = "Select idcust From customer Where idcust = '"+xidcust+"'";
        Cursor ccustomer = db.rawQuery(query,null);
        if (ccustomer.moveToFirst()){
            // Guardar la factura
            SQLiteDatabase dbi = sohInvoice.getWritableDatabase();
            ContentValues cinvoice = new ContentValues();
            try{
                cinvoice.put("idcust",xidcust);
                cinvoice.put("coste", xcoste);
                cinvoice.put("balance",xbalance);
                // insertar este registro a través del contenvalues cinvoice
                db.insert("invoice",null,cinvoice);
                // Consultar el ultimo número de factura (invoice)
                String qryidinvoice = "Select max(idinvoice) From invoice";
                Cursor cidinvoice = db.rawQuery(qryidinvoice,null);
                if (cidinvoice.moveToFirst()){
                    etidinvoice.setText(cidinvoice.getString(0));
                    etidinvoice.setEnabled(false);
                }
                db.close();
                Toast.makeText(getApplicationContext(),"Factura agregada correctamente",Toast.LENGTH_SHORT).show();
                /*etidcust.setText("");
                etcoste.setText("");
                etbalance.setText("");*/
            }
            catch (Exception e){
                Toast.makeText(getApplicationContext(),"Error: "+e.getMessage(),Toast.LENGTH_SHORT).show();
            }
        }
        else{
            Toast.makeText(getApplicationContext(),"Id de Cliente NO existe...",Toast.LENGTH_SHORT).show();
            etidcust.requestFocus();
        }
    }
}