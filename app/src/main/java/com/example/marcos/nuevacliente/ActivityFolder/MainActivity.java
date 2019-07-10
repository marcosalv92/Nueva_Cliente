package com.example.marcos.nuevacliente.ActivityFolder;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;


import android.content.res.Resources;
import android.support.v4.app.DialogFragment;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.example.marcos.nuevacliente.BaseDatosXML.BaseDatos;
import com.example.marcos.nuevacliente.Dialogos.DialogConfirmSend;
import com.example.marcos.nuevacliente.Dialogos.DialogCrearBusqueda;
import com.example.marcos.nuevacliente.Dialogos.DialogDetenerEnvio;
import com.example.marcos.nuevacliente.Dialogos.DialogEliminarPunto;
import com.example.marcos.nuevacliente.Dialogos.DialogInsertIP;
import com.example.marcos.nuevacliente.Listas.CustomAdapter;
import com.example.marcos.nuevacliente.Listas.ListDatos;
import com.example.marcos.nuevacliente.R;
import com.example.marcos.nuevacliente.SMS.EnviarSms;

import java.io.IOException;
import java.util.ArrayList;


public class MainActivity extends ActionBarActivity implements DialogConfirmSend.Dialogo1Interface,
        DialogCrearBusqueda.Dialogo2Interface, DialogDetenerEnvio.Dialogo6Interface, DialogInsertIP.Dialogo6Interface{

    public ArrayList<ListDatos> Contactos = new ArrayList<ListDatos>();
    public MainActivity pin = null;
    CustomAdapter adapter;
    ListView lista;
    EnviarSms sms = new EnviarSms();
    public int labeleliminar = 1;
    public MenuItem cambiaricono;
    Button mostrarMapaButton, veripnube ;
    BaseDatos baseDatos;
    public String ipNube;
    EditText editTextIpNube;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        pin = this;
        mostrarMapaButton = (Button)findViewById(R.id.button);
        veripnube = (Button)findViewById(R.id.button2);
        SharedPreferences preferences = getSharedPreferences("ip_nube",MODE_PRIVATE);
        String ip = preferences.getString("ip","false");
        if (ip.equals("false")){
            ipNube = "192.168.201.1";
            SharedPreferences.Editor editor = preferences.edit();
            editor.putString("ip","192.168.201.1");
            editor.commit();
        }else{
            ipNube = preferences.getString("ip","192.168.201.1");

        }

        mostrarMapaButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mostrarMap();
            }
        });
        lista = (ListView)findViewById(R.id.listView);

        LayoutInflater inflater = getLayoutInflater();
        final View view = inflater.inflate(R.layout.dialog6_inflate, null);
        editTextIpNube = (EditText) view.findViewById(R.id.editText_ipnube);
                //editTextIpNube = (EditText) findViewById(R.id.editText_ipnube);
        veripnube.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mostrarip();
            }
        });
        lista.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (labeleliminar == -1) { //if labeleliminar = -1 eliminamos
                    if (Contactos.get(position).getImage().equals("envLocalizar")) {

                        Contactos.remove(position);

                        if (Contactos.size() == 0) {
                            lista.setAdapter(null);
                            try {
                                baseDatos.CrearBaseDatosXML(Contactos);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        } else {
                            try {
                                baseDatos.CrearBaseDatosXML(Contactos);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            actualizarcontactos();
                        }
                    }else{
                        Toast.makeText(MainActivity.this, "Detenga primero el envío de actualizaciones de:"+Contactos.get(position).getNombre() , Toast.LENGTH_LONG).show();
                    }
                    onOptionsItemSelected(cambiaricono);

                } else {

                    if (Contactos.get(position).getImage().equals("envLocalizar")){
                        DialogFragment formato1 = new DialogConfirmSend(Contactos.get(position).getNumero(),Contactos.get(position).getNombre(),position);
                        formato1.show(getSupportFragmentManager(), getString(R.string.Dialogo));

                    }else {
                        DialogFragment formato2 = new DialogDetenerEnvio(Contactos.get(position).getNumero(),Contactos.get(position).getNombre(),position);
                        formato2.show(getSupportFragmentManager(),"Dialogo2");
                    }


                }
            }
        });
        //Extraigo los valores de la Base de Datos.
        baseDatos = new BaseDatos();
        Contactos = baseDatos.LeerBaseDatosXML();
        if (Contactos.size()!= 0){
            actualizarcontactos();
        }
    }

    public void mostrarMap(){
        Intent intmap = new Intent(MainActivity.this,MapActivity.class);
        //intmap.putExtra("NombreClase","MainActivity");
        startActivity(intmap);
    }

    public void setListData(String tipe1, String tipe2,String tipe3)
    {

        final ListDatos sched = new ListDatos();

        sched.setNombre(tipe1);
        sched.setImage(tipe3);
        sched.setNumero(tipe2);


        Contactos.add(sched);
        try {
            baseDatos.CrearBaseDatosXML(Contactos);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (labeleliminar == -1) {
            labeleliminar=1;
            actualizarcontactos();
            labeleliminar = -1;
            onOptionsItemSelected(cambiaricono);
        }
        else{
            actualizarcontactos();
        }

    }
    public void actualizarcontactos(){

        Resources res =getResources();
        adapter = new CustomAdapter( pin, Contactos,res,labeleliminar );
        lista.setAdapter(adapter);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            DialogFragment formato6 = new DialogInsertIP();
            formato6.show(getSupportFragmentManager(),"Dialogo6");
            return true;
        }
        if (id == R.id.adicionar) {

            DialogFragment formato2 = new DialogCrearBusqueda();
            formato2.show(getSupportFragmentManager(),"Dialogo2");


        }
        if (id == R.id.eliminar){
            cambiaricono = item;
            if (Contactos.size() != 0) {
                labeleliminar = labeleliminar * (-1);
                if (labeleliminar == 1) {
                    item.setIcon(R.drawable.elim);
                } else {
                    item.setIcon(R.drawable.cellitem);
                }

                actualizarcontactos();

            }
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    //Dialogo1-DialogConfirmSend
    @Override
    public void onDialogoPositivo(String phone,String nom,int posit) {
        if (sms.sendSms(phone,"CodigoGPS@"+nom)) {
            Contactos.get(posit).setImage("detLocalizacion");
            try {
                baseDatos.CrearBaseDatosXML(Contactos);
            } catch (IOException e) {
                e.printStackTrace();
            }
            Toast.makeText(this, "SMS Enviado para iniciar localizacion de:"+ nom , Toast.LENGTH_LONG).show();
        }else
            Toast.makeText(this, "SMS Faild" , Toast.LENGTH_LONG).show();
    }

    @Override
    public void onDialogoNegativo() {
        Toast.makeText(this, "Cancelado" , Toast.LENGTH_LONG).show();
    }
    //Dialogo2-DialogCrearBusqueda
    @Override
    public void onDialogoPositivo2(String inputNombre,String inputNumero,String inputImage) {

        setListData(inputNombre,inputNumero,inputImage);
        Toast.makeText(this, "Anadido Correctamente" , Toast.LENGTH_LONG).show();

    }


    @Override
    public void onDialogoNegativo2() {
        Toast.makeText(this, "Cancelada" , Toast.LENGTH_LONG).show();
    }

    //Dialogo3-DialogDetenerEnvio

    @Override
    public void onDialogoPositivo6(String phone, String nom,int posit) {
        if (sms.sendSms(phone,"DetenerGPS9208@"+nom)) {
            Contactos.get(posit).setImage("envLocalizar");
            try {
                baseDatos.CrearBaseDatosXML(Contactos);
            } catch (IOException e) {
                e.printStackTrace();
            }
            Toast.makeText(this, "SMS Enviado para detener localizacion de: "+ nom, Toast.LENGTH_LONG).show();
        }else
            Toast.makeText(this, "SMS Faild" , Toast.LENGTH_LONG).show();
    }

//Dialogo para insetar IP
    @Override
    public void onDialogoPositivo6(String inputIpnube) {
        if (inputIpnube.equals("")){
            Toast.makeText(this, "IP Nube Incorrecta" , Toast.LENGTH_LONG).show();
        }
        else if (inputIpnube.equals(ipNube)){
            Toast.makeText(this, "IP Nube Adicionada" , Toast.LENGTH_LONG).show();
        }
        else{
            ipNube = inputIpnube;
            setEditTextIpNube(ipNube);
        }

    }

    @Override
    public void onDialogoNegativo6() {
        Toast.makeText(this, "Cancelada" , Toast.LENGTH_LONG).show();
    }
    public String getIpNube(){
        return ipNube;
    }
    public void setEditTextIpNube(String ip){
        editTextIpNube.setText(ip);
        editTextIpNube.setHint(ip);
        SharedPreferences preferences = getSharedPreferences("ip_nube",MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("ip",ip);
        editor.commit();
        //editTextIpNube.setText(ip);
        String pru = editTextIpNube.getText().toString();
        Toast.makeText(this, pru , Toast.LENGTH_LONG).show();
    }
    public void mostrarip(){
        Toast.makeText(this, ipNube , Toast.LENGTH_LONG).show();
    }

}