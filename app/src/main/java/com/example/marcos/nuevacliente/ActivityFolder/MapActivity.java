package com.example.marcos.nuevacliente.ActivityFolder;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Array;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import org.mapsforge.core.model.LatLong;
import org.mapsforge.map.android.graphics.AndroidGraphicFactory;
import org.mapsforge.map.android.util.AndroidUtil;
import org.mapsforge.map.android.view.MapView;
import org.mapsforge.map.layer.cache.TileCache;
import org.mapsforge.map.layer.renderer.TileRendererLayer;
import org.mapsforge.map.rendertheme.InternalRenderTheme;

import android.app.DialogFragment;
import android.content.Context;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.marcos.nuevacliente.BaseDatosXML.BaseDatos;
import com.example.marcos.nuevacliente.BaseDatosXML.BaseDatosMarker;
import com.example.marcos.nuevacliente.Dialogos.DialogAdicionarPunto;
import com.example.marcos.nuevacliente.Dialogos.DialogBuscarPunto;
import com.example.marcos.nuevacliente.Dialogos.DialogEliminarPunto;
import com.example.marcos.nuevacliente.Listas.ListMarker;
import com.example.marcos.nuevacliente.Mapeo.MyMarker;
import com.example.marcos.nuevacliente.R;
import com.example.marcos.nuevacliente.SMS.CaptureResponserHandler;
import com.example.marcos.nuevacliente.SMS.RecibirSms;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.RequestParams;

import static java.lang.Double.parseDouble;

public class MapActivity extends ActionBarActivity implements DialogEliminarPunto.Dialogo3Interface,DialogAdicionarPunto.Dialogo4Interface, DialogBuscarPunto.Dialogo5Interface{

    private MapView mapView;
    private TileCache tileCache;
    private TileRendererLayer tileRendererLayer;
    public ArrayList<MyMarker> arrayPuntosAMostrar = new ArrayList<MyMarker>();
    public ArrayList<ListMarker> markerloc= new ArrayList<ListMarker>();
    BaseDatosMarker baseDatosMarker;
    public int contador = 0;
    public boolean tgle = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Recopilar Informacion de Nuestro Dispositivo necesaria para mostrar el mapa.
        AndroidGraphicFactory.createInstance(getApplication());

        setContentView(R.layout.activity_map);
        //Leer base de datos que contiene los Marker de Localizacion
        baseDatosMarker = new BaseDatosMarker();
        markerloc = baseDatosMarker.LeerBaseDatosMarkerXML();
        if (markerloc.size()!= 0){
            for (int i=0;i < markerloc.size();i++){
                double mlatitud = parseDouble(markerloc.get(i).getLatitud());
                double mlongitud = parseDouble(markerloc.get(i).getLongitud());
                String ident = markerloc.get(i).getIdentificador();
                String fecha = markerloc.get(i).getFechaHora();
                MyMarker myMarker = new MyMarker(this,new LatLong(mlatitud,mlongitud),AndroidGraphicFactory.convertToBitmap(getResources().getDrawable(R.drawable.location)),0,0,ident,fecha);
                arrayPuntosAMostrar.add(myMarker);
            }
        }
        //Extraigo el Bundle
//            Bundle bundle = getIntent().getExtras();
//            String nombreClase = bundle.getString("NombreClase");
//            if (nombreClase.equals("RecSms")) {
//                String identifPhone = bundle.getString("identificador");
//                String latitud = bundle.getString("latitud");
//                String longitud = bundle.getString("long");
//                double lat = parseDouble(latitud);
//                double lon = parseDouble(longitud);
//                MyMarker newMarker = new MyMarker(this, new LatLong(lat, lon), AndroidGraphicFactory.convertToBitmap(getResources().getDrawable(R.drawable.location)), 0, 0);
//                arrayPuntosAMostrar.add(newMarker);
//                ListMarker nuevo = new ListMarker();
//                nuevo.setLatitud(latitud);
//                nuevo.setLongitud(longitud);
//                nuevo.setIdentificador(identifPhone);
//                markerloc.add(nuevo);
//                try {
//                    baseDatosMarker.CrearBaseDatosMarkerXML(markerloc);
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//
//
//            }



        mapView = (MapView)findViewById(R.id.mapView);
        //Apagar la aceleraci?n de hardware
        mapView.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        //Hace el Mapa Pulsable
        mapView.setClickable(true);

        //Se crea un "Tile cach?" que se encarga de cachear los "tiles" (porciones de mapa)
        // que se van mostrando para no tener que andar carg?ndolas siempre desde cero
        tileCache = AndroidUtil.createTileCache(this, "mapcache",
                mapView.getModel().displayModel.getTileSize(), 1f,
                mapView.getModel().frameBufferModel.getOverdrawFactor());

        // Se indica el zoom por defecto y los niveles m?nimo y m?ximo de zoom
        mapView.getModel().mapViewPosition.setZoomLevel((byte) 15);
        mapView.getMapZoomControls().setZoomLevelMin((byte) 10);
        mapView.getMapZoomControls().setZoomLevelMax((byte) 20);

        //Se toma la direccion del mapa en la memoria del phone
        String filepath = Environment.getExternalStorageDirectory().getPath() + "/Maps/cuba.map";

        // Se crea un "Tile renderer layer" encargado de dibujar los "tiles" al que se le indica
        // el "Tile Cach?" que utilizar? y la ruta al mapa adem?s del tipo de "renderer".
        tileRendererLayer = new TileRendererLayer(tileCache,
                mapView.getModel().mapViewPosition, false, AndroidGraphicFactory.INSTANCE);
        tileRendererLayer.setMapFile(new File(filepath));
        tileRendererLayer.setXmlRenderTheme(InternalRenderTheme.OSMARENDER);

        // Esto a?ade la "capa" creada al mapa
        mapView.getLayerManager().getLayers().add(tileRendererLayer);

        //mapView.setClickable(true);
        mapView.setBuiltInZoomControls(true);
        mapView.getMapScaleBar().setVisible(false);
        //Central Mapa en última posicion
       // mapView.getModel().mapViewPosition.setCenter(new LatLong(22.41224, -83.69562));
        LatLong point = arrayPuntosAMostrar.get(arrayPuntosAMostrar.size()-1).getLatLong();
        mapView.getModel().mapViewPosition.setCenter(point);
        mapView.getLayerManager().getLayers().add(arrayPuntosAMostrar.get(arrayPuntosAMostrar.size()-1));
//        for (int i = 0;i < arrayPuntosAMostrar.size() ;i++){
//            mapView.getLayerManager().getLayers().add(arrayPuntosAMostrar.get(i));
//        }
        if (arrayPuntosAMostrar.size()==0){
            MyMarker locationnull = new MyMarker(this, new LatLong(20.47249, -74.30302), AndroidGraphicFactory.convertToBitmap(getResources().getDrawable(R.drawable.locationnull)), 0, 0,"Marcos","");
            mapView.getLayerManager().getLayers().add(locationnull);
        }
//        MyMarker marker = new MyMarker(this, new LatLong(22.41224, -83.69562), AndroidGraphicFactory.convertToBitmap(getResources().getDrawable(R.drawable.location)), 0, 0,"Marcos");
//        mapView.getLayerManager().getLayers().add(marker);
//        arrayPuntosAMostrar.add(marker);
//        MyMarker marker2 = new MyMarker(this, new LatLong(22.412502111, -83.69562), AndroidGraphicFactory.convertToBitmap(getResources().getDrawable(R.drawable.location)), 0, 0,"Marcos");
//        mapView.getLayerManager().getLayers().add(marker2);
//        arrayPuntosAMostrar.add(marker2);
//        MyMarker marker1 = new MyMarker(this, new LatLong(22.41524, -83.69562), AndroidGraphicFactory.convertToBitmap(getResources().getDrawable(R.drawable.pin)), 0, 0,"Pedro");
//        mapView.getLayerManager().getLayers().add(marker1);
//        arrayPuntosAMostrar.add(marker1);
//       try {
//            baseDatosMarker.CrearBaseDatosMyMarker(arrayPuntosAMostrar);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_map, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        if (id == R.id.buscarPunto) {
//            android.support.v4.app.DialogFragment formato5 = new DialogBuscarPunto();
//            formato5.show(getSupportFragmentManager(), "Dialogo5");
            baseDatosMarker = new BaseDatosMarker();
            markerloc = baseDatosMarker.LeerBaseDatosMarkerXML();
            if (markerloc.size()!= 0){
                for (int i=0;i < markerloc.size();i++){
                    double mlatitud = parseDouble(markerloc.get(i).getLatitud());
                    double mlongitud = parseDouble(markerloc.get(i).getLongitud());
                    String ident = markerloc.get(i).getIdentificador();
                    String fecha = markerloc.get(i).getFechaHora();
                    MyMarker myMarker = new MyMarker(this,new LatLong(mlatitud,mlongitud),AndroidGraphicFactory.convertToBitmap(getResources().getDrawable(R.drawable.location)),0,0,ident,fecha);
                    arrayPuntosAMostrar.add(myMarker);
                }
            }
            tgle = !tgle;
            if (tgle) {
                actualizarMap();
            }else{
                getlastpositioninMap();
            }

            return true;
        }


        return super.onOptionsItemSelected(item);
    }
    //Dialogo3: Para Eliminar Punto
    private MyMarker _toDelete;

    public void DeletePoint(MyMarker toDelete){
        _toDelete = toDelete;
        android.support.v4.app.DialogFragment formato3 = new DialogEliminarPunto();
        formato3.show(getSupportFragmentManager(), "Dialogo3");
    }

    @Override
    public void onDialogoPositivo3() {
        //aqui mandar a eliminar el marker
        arrayPuntosAMostrar.remove(_toDelete);

        try {
            BaseDatosMarker newRemove = new BaseDatosMarker();
            newRemove.CrearBaseDatosMyMarker(arrayPuntosAMostrar);
        } catch (IOException e) {
            e.printStackTrace();
        }
        actualizarMap();
        Toast.makeText(this, "Ubicacion Eliminada", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onDialogoNegativo3() {
        Toast.makeText(this, "Cancelada la eliminacion.", Toast.LENGTH_SHORT).show();
    }
    //Dialogo4 Para Adicionar Punto
    private double _latitude,_longitude;
    public void AddPoint(double latitud,double longitud){
        _latitude = latitud;
        _longitude = longitud;
        android.support.v4.app.DialogFragment formato4 = new DialogAdicionarPunto();
        formato4.show(getSupportFragmentManager(), "Dialogo4");

    }
    @Override
    public void onDialogoPositivo4(String inputId) {
        MyMarker addMarker = new MyMarker(this, new LatLong(_latitude, _longitude), AndroidGraphicFactory.convertToBitmap(getResources().getDrawable(R.drawable.location)), 0, 0,inputId,": Punto adicionado por el usuario: "+fechaHoraActual());
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
//        params.put("container", inputId);
//        params.put("number_id", "54568156");
//        params.put("latitud", _latitude);
//        params.put("longitud", _longitude);
//        params.put("fechaHora", fechaHoraActual());
//        client.post(this, "http://192.168.201.1/slimapp/public/index.php/api/location/add", params,new CaptureResponserHandler(this));
//        //192.168.1.104 Etecsa
//        Toast.makeText(this, "Envioooo Https", Toast.LENGTH_SHORT).show();
        params.put("truck_id",25);
        //params.put("number_id", "54568156");
        params.put("latitude", _latitude);
        params.put("longitude", _longitude);
        params.put("recorded_at",fechaHoraActual());
        client.post(this, "http://www.nojotros.tk/api/newlocation", params,new CaptureResponserHandler(this));

        //192.168.1.104 Etecsa
        Toast.makeText(this, "Envioooo Https", Toast.LENGTH_SHORT).show();
//                  MapActivity actMapa = new MapActivity();
        arrayPuntosAMostrar.add(addMarker);
        try {
            BaseDatosMarker newAdd = new BaseDatosMarker();
            newAdd.CrearBaseDatosMyMarker(arrayPuntosAMostrar);
        } catch (IOException e) {
            e.printStackTrace();
        }
        actualizarMap();
        //Toast.makeText(this, "Ubicacion Adicionada", Toast.LENGTH_SHORT).show();
    }
    public String fechaHoraActual(){
        return new SimpleDateFormat( "yyyy-MM-dd hh:mm:ss", java.util.Locale.getDefault()).format(Calendar.getInstance() .getTime());
    }

    @Override
    public void onDialogoNegativo4() {
        Toast.makeText(this, "Cancelada.", Toast.LENGTH_SHORT).show();
    }



    public void getlastpositioninMap(){
        mapView.getLayerManager().getLayers().clear();
        mapView.getLayerManager().getLayers().add(tileRendererLayer);
        if (arrayPuntosAMostrar.size()==0){
            MyMarker locationnull = new MyMarker(this, new LatLong(20.47249, -74.30302), AndroidGraphicFactory.convertToBitmap(getResources().getDrawable(R.drawable.locationnull)), 0, 0,"Marcos","");
            mapView.getLayerManager().getLayers().add(locationnull);
        }else {
            LatLong point = arrayPuntosAMostrar.get(arrayPuntosAMostrar.size() - 1).getLatLong();
            mapView.getModel().mapViewPosition.setCenter(point);
            mapView.getLayerManager().getLayers().add(arrayPuntosAMostrar.get(arrayPuntosAMostrar.size() - 1));
        }
    }

    public void actualizarMap(){
        mapView.getLayerManager().getLayers().clear();
        mapView.getLayerManager().getLayers().add(tileRendererLayer);
        for (int i = 0;i < arrayPuntosAMostrar.size() ;i++){
            mapView.getLayerManager().getLayers().add(arrayPuntosAMostrar.get(i));
        }
        if (arrayPuntosAMostrar.size()==0){
            MyMarker locationnull = new MyMarker(this, new LatLong(20.47249, -74.30302), AndroidGraphicFactory.convertToBitmap(getResources().getDrawable(R.drawable.locationnull)), 0, 0,"Marcos","");
            mapView.getLayerManager().getLayers().add(locationnull);
        }else{
            LatLong point = arrayPuntosAMostrar.get(arrayPuntosAMostrar.size() - 1).getLatLong();
            mapView.getModel().mapViewPosition.setCenter(point);
        }

    }


    // Dialogo 5 Buscar Punto en Mapa
    @Override
    public void onDialogoPositivo5(double lat,double lon,String iden) {
        _latitude = lat;
        _longitude = lon;
        String identificador = iden;
        MyMarker addMarker = new MyMarker(this, new LatLong(_latitude, _longitude), AndroidGraphicFactory.convertToBitmap(getResources().getDrawable(R.drawable.location)), 0, 0,identificador,"Punto adicionado por el usuario-"+fechaHoraActual());
        arrayPuntosAMostrar.add(addMarker);
        try {
            BaseDatosMarker newAdd = new BaseDatosMarker();
            newAdd.CrearBaseDatosMyMarker(arrayPuntosAMostrar);
        } catch (IOException e) {
            e.printStackTrace();
        }
        actualizarMap();
        Toast.makeText(this, "Punto Ubicado", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onDialogoNegativo5() {
        Toast.makeText(this, "Cancelado", Toast.LENGTH_SHORT).show();
    }
    public int ContarCapas(){
        contador++;
        return contador;
    }
    public void incializarConteoCapas(){
        contador=0;

    }
    public static boolean verificaConexion(Context ctx) {
        boolean bConectado = false;
        ConnectivityManager connec = (ConnectivityManager) ctx
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        // No sólo wifi, también GPRS
        NetworkInfo[] redes = connec.getAllNetworkInfo();
        // este bucle debería no ser tan ñapa
        for (int i = 0; i < 2; i++) {
            // ¿Tenemos conexión? ponemos a true
            if (redes[i].getState() == NetworkInfo.State.CONNECTED) {
                bConectado = true;
            }
        }
        return bConectado;
    }
}