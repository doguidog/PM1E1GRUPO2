package com.example.pm2examengrupo2;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.pm2examengrupo2.API.Api;
import com.example.pm2examengrupo2.Contexto.Contactos;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Activity_Contact extends AppCompatActivity {
    private List<Contactos> mLista = new ArrayList<>();
    ListView lista;
    List<Contactos> ContactList;
    ArrayList<String> arrayContact;
    Button btnactualizar, btneliminar, btnubicacion;
    Contactos contacto;
    EditText buscar;
    ImageView atras;
    ArrayAdapter adp;
    Intent intent;


    int previousPosition = 1;
    int count = 1;
    long previousMil = 0;
    final Context context = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact);

        lista = (ListView) findViewById(R.id.lista);
        intent = new Intent(getApplicationContext(), Activity_Contact.class);//para obtener el contacto seleccionado mas adelante


        ContactList = new ArrayList<>();
        arrayContact = new ArrayList<String>();

        btnactualizar = (Button) findViewById(R.id.btnactualizar);
        atras = (ImageView) findViewById(R.id.btnatras);
        btneliminar = (Button) findViewById(R.id.btneliminar);
        btnubicacion = (Button) findViewById(R.id.btnubicacion);

        buscar = (EditText) findViewById(R.id.txtbusqueda);

        listarContactos();

        //------------------------------EVENTOS BOTONES Y BUSCAR------------------------------------
        buscar.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {


            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                try {
                    BuscarUsuario(buscar.getText().toString());
                    if (buscar.getText().toString().equals("")) {
                        listarContactos();
                    }
                } catch (Exception ex) {
                    Toast.makeText(getApplicationContext(), "Valor invalido", Toast.LENGTH_SHORT).show();
                }

            }
        });


        btnactualizar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), Activity_Actualizar.class);
                intent.putExtra("id", contacto.getId() + "");
                intent.putExtra("nombre", contacto.getNombre() + "");
                intent.putExtra("telefono", contacto.getTelefono() + "");
                intent.putExtra("latitud", contacto.getLatitud() + "");
                intent.putExtra("longitud", contacto.getLongitud() + "");
                intent.putExtra("foto", contacto.getFoto() + "").toString();
                startActivity(intent);

            }
        });

        atras.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
            }
        });

        btneliminar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);

                // set title

                alertDialogBuilder.setTitle("Eliminar Contacto");


                // set dialog message
                alertDialogBuilder
                        .setMessage("¿Está seguro de eliminar el contacto " + contacto.getNombre() + "?")
                        .setCancelable(false)
                        .setPositiveButton("SI", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                // si el usuario da click en si procede a llamar el metodo de eliminar
                                eliminarContacto(String.valueOf(contacto.getId()));
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                // if this button is clicked, just close
                                // the dialog box and do nothing
                                dialog.cancel();
                            }
                        });

                // create alert dialog
                AlertDialog alertDialog = alertDialogBuilder.create();

                // show it
                alertDialog.show();
            }
        });

        btnubicacion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), GoogleMaps.class);
                intent.putExtra("latitud", contacto.getLatitud());
                intent.putExtra("longitud", contacto.getLongitud());
                startActivity(intent);
            }
        });

        //-- lista evento click
        lista.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (previousPosition == i) {
                    count++;
                    if (count == 2 && System.currentTimeMillis() - previousMil <= 1000) {
                        //Toast.makeText(getApplicationContext(), "Doble Click ",Toast.LENGTH_LONG).show();
                        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
                        alertDialogBuilder.setTitle("Acción");
                        alertDialogBuilder
                                .setMessage("¿Desea ir a la Ubicacion de " + contacto.getNombre() + "?")
                                .setCancelable(false)
                                .setPositiveButton("SI", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        // se procede a ir a la ubicacion seteando los parametros
                                        try {
                                            Intent intent = new Intent(getApplicationContext(), GoogleMaps.class);
                                            intent.putExtra("latitud", contacto.getLatitud());
                                            intent.putExtra("longitud", contacto.getLongitud());
                                            startActivity(intent);

                                        } catch (Exception ex) {
                                            ex.toString();
                                        }
                                    }
                                })
                                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        dialog.cancel();
                                    }
                                });
                        AlertDialog alertDialog = alertDialogBuilder.create();
                        alertDialog.show();
                        count = 1;
                    }
                } else {
                    previousPosition = i;
                    count = 1;
                    previousMil = System.currentTimeMillis();
                    //un clic
                    contacto = ContactList.get(i);//lleno la lista de contacto
                    setContactSelected();//obtengo el usuario seleccionado de la lista
                    //Toast.makeText(getApplicationContext(),"usuario id: "+usuario.getId(), Toast.LENGTH_SHORT).show();
                }
            }


        });


    }
    //-----------------------------------METODOS---------------------------------------


    private void listarContactos() {
        RequestQueue queue = Volley.newRequestQueue(this);

        StringRequest stringRequest = new StringRequest(Request.Method.GET, Api.EndPointGetContact,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray contactoArray = jsonObject.getJSONArray("contacto");

                            arrayContact.clear();//limpiar la lista de usuario antes de comenzar a listar
                            for (int i = 0; i < contactoArray.length(); i++) {
                                JSONObject RowContact = contactoArray.getJSONObject(i);
                                contacto = new Contactos(RowContact.getInt("id"),
                                        RowContact.getString("nombre"),
                                        RowContact.getInt("telefono"),
                                        RowContact.getString("latitud"),
                                        RowContact.getString("longitud"),
                                        RowContact.getString("foto")
                                );

                                ContactList.add(contacto);
                                arrayContact.add(contacto.getNombre() + ' ' + contacto.getTelefono());
                            }

                            adp = new ArrayAdapter(getApplicationContext(), android.R.layout.simple_list_item_checked, arrayContact);
                            lista.setAdapter(adp);

                        } catch (JSONException ex) {
                            Toast.makeText(getApplicationContext(), "mensaje " + ex, Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), "mensaje " + error, Toast.LENGTH_SHORT).show();
            }
        });
        queue.add(stringRequest);
    }

    private void BuscarUsuario(String dato) {
        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, Api.EndPointGetBuscarContact + dato,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray contactoArray = jsonObject.getJSONArray("contacto");

                            arrayContact.clear();//limpiar la lista de usuario antes de comenzar a buscar

//                            if ()){
//                                Toast.makeText(getApplicationContext(), "No se encontro el valor", Toast.LENGTH_SHORT).show();
//                            }

                            for (int i = 0; i < contactoArray.length(); i++) {
                                JSONObject RowContacto = contactoArray.getJSONObject(i);
                                Contactos contacto = new Contactos(RowContacto.getInt("id"),
                                        RowContacto.getString("nombre"),
                                        RowContacto.getInt("telefono"),
                                        RowContacto.getString("latitud"),
                                        RowContacto.getString("longitud"),
                                        RowContacto.getString("foto")
                                );
                                ContactList.add(contacto);
                                arrayContact.add(contacto.getNombre() + ' ' + contacto.getTelefono());
                            }

                            adp = new ArrayAdapter(getApplicationContext(), android.R.layout.simple_list_item_checked, arrayContact);
                            lista.setAdapter(adp);

                        } catch (JSONException ex) {
                            Toast.makeText(getApplicationContext(), "mensaje " + ex, Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //Toast.makeText(getApplicationContext(), "mensaje "+error, Toast.LENGTH_SHORT).show();
            }
        });
        queue.add(stringRequest);
    }

    private void eliminarContacto(String dato) {
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "https://elkinhn.online/APIEG2/eliminarcontacto.php?id=";
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url + dato,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Toast.makeText(getApplicationContext(), "Contacto eliminado exitosamente", Toast.LENGTH_SHORT).show();
                        listarContactos();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //Toast.makeText(getApplicationContext(), "mensaje "+error, Toast.LENGTH_SHORT).show();
            }
        });
        queue.add(stringRequest);
    }


    private void setContactSelected() {

        //contacto = listaContactos.get(id);
        //intent = new Intent(getApplicationContext(),ActivityActualizarContacto.class);
        intent.putExtra("id", contacto.getId() + "");
        intent.putExtra("nombre", contacto.getNombre());
        intent.putExtra("telefono", contacto.getTelefono() + "");
        intent.putExtra("latitud", contacto.getLatitud());
        intent.putExtra("longitud", contacto.getLongitud());
        intent.putExtra("foto2", contacto.getFoto() + "").toString();

        //startActivity(intent);
    }


}