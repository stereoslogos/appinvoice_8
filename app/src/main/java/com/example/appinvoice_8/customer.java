package com.example.appinvoice_8;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class customer extends AppCompatActivity {
    //Instanciar los objetos con id del archivo xml asociado
    EditText idcust,name,email,phone;
    Button btnsave,btnsearch,btnivoice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer);

        //Referenciar los objetos instanciados con los id del archivo xml
        idcust = findViewById(R.id.etidcust);
        name = findViewById(R.id.etname);
        email = findViewById(R.id.etemail);
        phone = findViewById(R.id.etphone);

        btnsave = findViewById(R.id.btnsave);
        btnsearch = findViewById(R.id.btnsearch);
        btnivoice = findViewById(R.id.btninvoice);

        btnivoice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),MainActivity.class));
            }
        });

        btnsave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Instanciar la clase invoice que hace refrencia a las tablas
                invoice sohinvoice = new invoice(getApplicationContext(),"dbinvoice",null,1);

                //Instanciar SQLiteDataBase
                SQLiteDatabase db = sohinvoice.getWritableDatabase();
                ContentValues cust = new ContentValues();
                try{
                    cust.put("idcust",idcust.getText().toString());
                    cust.put("name",name.getText().toString());
                    cust.put("email",email.getText().toString());
                    cust.put("phone",phone.getText().toString());

                    //Insertar el registro a través del contentvalues cust
                    db.insert("customer",null,cust);
                    db.close();
                    Toast.makeText(getApplicationContext(),"Cliente agregado correctamente",Toast.LENGTH_SHORT).show();
                    idcust.setText("");
                    name.setText("");
                    email.setText("");
                    phone.setText("");

                }
                catch (Exception e){
                    Toast.makeText(getApplicationContext(),"ERROR Cliente NO agregado"+e.getMessage(),Toast.LENGTH_SHORT).show();
                }
            }
        });

        btnsearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                invoice sohinvoice = new invoice(getApplicationContext(),"dbinvoice",null,1);
                SQLiteDatabase db = sohinvoice.getReadableDatabase();

                String query = "SELECT name,email,phone FROM customer WHERE idcust = '"+idcust.getText().toString()+"'";
                Cursor ccust = db.rawQuery(query,null);
                //Si encuentra el registro
                if (ccust.moveToFirst())
                {
                    name.setText(ccust.getString(0));
                    email.setText(ccust.getString(1));
                    phone.setText(ccust.getString(2));
                }
                else
                {
                    Toast.makeText(getApplicationContext(),"Cliente NO Existe!",Toast.LENGTH_SHORT).show();
                }
                db.close();
            }
        });
    }
}