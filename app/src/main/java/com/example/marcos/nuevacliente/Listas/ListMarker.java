package com.example.marcos.nuevacliente.Listas;

/**
 * Created by marcos on 1/28/2016.
 */
public class ListMarker {
    public String latitud = "";
    private String longitud="";
    public String identificador = "";
    public String fechaHora="";



    public void setFechaHora(String fechaHora) {this.fechaHora = fechaHora;}

    public void setLatitud(String latitud) {this.latitud = latitud;}

    public void setLongitud(String longitud) {
        this.longitud = longitud;
    }

    public void setIdentificador(String identificador) {
        this.identificador = identificador;
    }

    public String getLatitud() {
        return latitud;
    }

    public String getLongitud() {
        return longitud;
    }

    public String getIdentificador() {return identificador;}

    public String getFechaHora() {return fechaHora;}

}
