package com.example.marcos.nuevacliente.Mapeo;

/**
 * Created by marcos on 1/24/2016.
 */

import android.app.DialogFragment;
import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.example.marcos.nuevacliente.ActivityFolder.MapActivity;
import com.example.marcos.nuevacliente.BaseDatosXML.BaseDatosMarker;
import com.example.marcos.nuevacliente.Dialogos.DialogCrearBusqueda;
import com.example.marcos.nuevacliente.Dialogos.DialogEliminarPunto;
import com.example.marcos.nuevacliente.Listas.ListMarker;
import com.example.marcos.nuevacliente.R;

import org.mapsforge.core.graphics.Bitmap;
import org.mapsforge.core.model.LatLong;
import org.mapsforge.core.model.Point;
import org.mapsforge.map.android.graphics.AndroidGraphicFactory;
import org.mapsforge.map.layer.overlay.Marker;


import java.io.IOException;
import java.util.ArrayList;

import static android.support.v4.app.ActivityCompat.startActivity;

public class MyMarker extends Marker {
    private Context ctx;
    public String ident;
    public String fechaHora;
    public int conteoDeCapas = 0;
    public MyMarker(Context ctx, LatLong latLong, Bitmap bitmap, int horizontalOffset,
                    int verticalOffset,String ident,String fechaHora) {
        super(latLong, bitmap, horizontalOffset, verticalOffset);
        this.ctx = ctx;
        this.ident = ident;
        this.fechaHora = fechaHora;

    }

    @Override
    public boolean onTap(LatLong tapLatLong, Point layerXY, Point tapXY) {
          //El evento onTap se activa para todos los MyMarker,
          if (this.contains(layerXY, tapXY)) {
            //Buscar this en la lista arrayPuntosAMostrar.
            //Eliminar this de la lista arrayPuntosAMostrar
            Toast.makeText(ctx, this.ident+": HORA_DIA:"+ this.fechaHora /*+"-Ubicacion: latitud: " + tapLatLong.latitude + " y longitud: " + tapLatLong.longitude*/, Toast.LENGTH_SHORT).show();
            return true;
          }
        return super.onTap(tapLatLong, layerXY, tapXY);
    }

    @Override
    public boolean onLongPress(LatLong tapLatLong, Point layerXY, Point tapXY) {
        MapActivity pr = (MapActivity) ctx;
        conteoDeCapas = pr.ContarCapas();
        if (this.contains(layerXY, tapXY)) {
            pr.DeletePoint(this);
            pr.incializarConteoCapas();

            return true;
        }
        else if ((conteoDeCapas == (pr.arrayPuntosAMostrar.size())) || (pr.arrayPuntosAMostrar.size()==0) ) {
            pr.AddPoint( tapLatLong.latitude ,  tapLatLong.longitude);
            pr.incializarConteoCapas();
            return true;
        }

        return super.onLongPress(tapLatLong, layerXY, tapXY);
    }



}