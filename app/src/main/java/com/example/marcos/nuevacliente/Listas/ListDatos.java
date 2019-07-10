package com.example.marcos.nuevacliente.Listas;

/**
 * Created by marcos on 1/13/2016.
 */
public class ListDatos {
    public String Nombre = "";
    private  String Image="";
    public String Numero = "";

    public void setNumero(String numero) {
        this.Numero = numero;
    }

    public void setImage(String image) {
        Image = image;
    }

    public void setNombre(String nombre) {
        this.Nombre = nombre;
    }

    public String getNombre() {
        return Nombre;
    }

    public String getImage() {
        return Image;
    }

    public String getNumero() {
        return Numero;
    }
}
