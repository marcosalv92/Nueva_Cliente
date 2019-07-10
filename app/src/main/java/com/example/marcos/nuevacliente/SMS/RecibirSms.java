package com.example.marcos.nuevacliente.SMS;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.widget.Toast;

import com.example.marcos.nuevacliente.ActivityFolder.MainActivity;
import com.example.marcos.nuevacliente.ActivityFolder.MapActivity;
import com.example.marcos.nuevacliente.BaseDatosXML.BaseDatosMarker;
import com.example.marcos.nuevacliente.Listas.ListMarker;
import com.example.marcos.nuevacliente.Mapeo.MyMarker;
import com.example.marcos.nuevacliente.R;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.RequestParams;

import org.mapsforge.core.model.LatLong;

import java.io.IOException;

public class RecibirSms extends BroadcastReceiver {
//    public RecibirSms ()
//    {
//
//    }

    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO: This method is called when the BroadcastReceiver is receiving
        // an Intent broadcast.
        SharedPreferences preferences = context.getSharedPreferences("ip_nube",Context.MODE_PRIVATE);
        String ipnew = preferences.getString("ip","198.168.1.1");
        Bundle bundle = intent.getExtras();
        SmsMessage [] mensajes =null;
        int Notif_Id = 0;
        MyMarker [] datoslocalizacion;
        //MapActivity claseMapActivity;
        if (bundle != null){
            Object [] pdus = (Object[])bundle.get("pdus");
            mensajes = new SmsMessage[pdus.length];
            for (int i = 0; i < mensajes.length; i++){
                mensajes[i] = SmsMessage.createFromPdu((byte[])pdus[i]);
            }
            if (mensajes.length > -1){
                String bodymensaje = mensajes[0].getMessageBody();
                if (bodymensaje.contains("codigo9208")){
                    String [] descompuesto = bodymensaje.split("@");
                    //Para Notificacion
//                    NotificationManager notManager = (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
//                    int icono = R.drawable.location;
//                    CharSequence textoEstado = "Alert!";
//                    long hora = System.currentTimeMillis();
//                    Notification notif = new Notification(icono, textoEstado, hora);
//                    CharSequence titulo = "Nueva Localizacion Enviada";
//                    CharSequence descripcion = "Pertenece a: " + descompuesto[3];
                    //Formato de mensaje "*codigo9208@20.3444@@112.5666l20@nombre@16-04-16_11.13.53"

                    ListMarker agregar = new ListMarker();
                    agregar.setLatitud(descompuesto[1]);
                    agregar.setLongitud(descompuesto[2]);
                    agregar.setIdentificador(descompuesto[3]);
                    agregar.setFechaHora(descompuesto[4]);
                ConnectivityManager connManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo mWifi = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);

                if (mWifi.isConnected()) {
                    AsyncHttpClient client = new AsyncHttpClient();
                    RequestParams params = new RequestParams();
                    params.put("container", descompuesto[3]);
                    params.put("number_id", mensajes[0].getOriginatingAddress());
                    params.put("latitud", Double.parseDouble(descompuesto[1]));
                    params.put("longitud", Double.parseDouble(descompuesto[2]));
                    params.put("fechaHora", descompuesto[4]);
//                    MainActivity newip = new MainActivity();
//                    String ipnew = newip.getIpNube();

                    try {
                        client.post(context, "http://"+ ipnew +"/slimapp/public/index.php/api/location/add", params,new CaptureResponserHandler(context));
                        //192.168.1.104 Etecsa
                        Toast.makeText(context, "Envioooo Https:"+ipnew, Toast.LENGTH_SHORT).show();
                    }catch (Error e) {
                       // e.printStackTrace();
                        Toast.makeText(context, "Error Verifique conexion WIFI", Toast.LENGTH_SHORT).show();
                    }

                    // Do whatever
                }else{
                    Toast.makeText(context, "Conectese a WIFI para enviar datos a la nube", Toast.LENGTH_SHORT).show();
                }

//                  MapActivity actMapa = new MapActivity();
//                  actMapa.actualizarMap();
                    BaseDatosMarker bd = new BaseDatosMarker();
                    bd.ActualizarBaseDatosXML(agregar);
                    //Para Notificacion
//                    Intent notIntent = new Intent(context,MapActivity.class);


//                    notIntent.putExtra("NombreClase","RecSms");
//                    notIntent.putExtra("identificador", mensajes[0].getOriginatingAddress());
//                    //Falta como coger los valores de la Localizacion del texto del Mensaje
//                    notIntent.putExtra("latitud", descompuesto[1]);
//                    notIntent.putExtra("long", descompuesto[2]);

                    //Para Notificacion
//                    notIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                    PendingIntent contIntent = PendingIntent.getActivity(context, 0, notIntent, 0);
//                    notif.setLatestEventInfo(context, titulo, descripcion, contIntent);
//
//                    notif.flags |= Notification.FLAG_AUTO_CANCEL;
//                    //Aadir sonido, vibraci�n y luces
//                    notif.defaults |= Notification.DEFAULT_SOUND;
//                    notif.defaults |= Notification.DEFAULT_VIBRATE;
//                    notif.defaults |= Notification.DEFAULT_LIGHTS;
//
//                    notManager.notify(Notif_Id,notif);
//                    Notif_Id++;





                    //notIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    //context.startActivity(notIntent);

                  }
                     else if (bodymensaje.contains("DetenidoGPS9208")){
                    String [] descompuesto = bodymensaje.split("@");
                    String nombre = descompuesto[1];
                    Toast.makeText(context,"Confirmacion de GPS Detenido de "+nombre,Toast.LENGTH_LONG).show();
                }
            }



        }

    }
}
