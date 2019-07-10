package com.example.marcos.nuevacliente.BaseDatosXML;

import android.os.Environment;
import android.util.Xml;

import com.example.marcos.nuevacliente.Listas.ListDatos;
import com.example.marcos.nuevacliente.Listas.ListMarker;
import com.example.marcos.nuevacliente.Mapeo.MyMarker;
import com.example.marcos.nuevacliente.R;

import org.mapsforge.core.model.LatLong;
import org.mapsforge.map.android.graphics.AndroidGraphicFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xmlpull.v1.XmlSerializer;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import static java.lang.Double.parseDouble;

/**
 * Created by marcos on 1/28/2016.
 */
public class BaseDatosMarker {
    public ArrayList<ListMarker> basemarker;
    public void CrearBaseDatosMarkerXML(ArrayList<ListMarker> l) throws IOException
    {
        XmlSerializer ser = Xml.newSerializer();

        File tarjeta = Environment.getExternalStorageDirectory();
        File file = new File(tarjeta.getAbsolutePath()/*.concat("/dbanatomia/")*/, "/Maps/dbmap/marker.xml");

        OutputStreamWriter osw = new OutputStreamWriter(new FileOutputStream(file));

        ser.setOutput(osw);
        if (l.size() == 0) {
            ser.startTag("","");
            ser.endTag("","");
        }
        else {
            ser.startTag("", "markers");

            for (int i = 0; i < l.size(); i++) {
                ser.startTag("", "marker");

                ser.startTag("", "latitud");
                ser.text(l.get(i).getLatitud());
                ser.endTag("", "latitud");

                ser.startTag("", "longitud");
                ser.text(l.get(i).getLongitud());
                ser.endTag("", "longitud");

                ser.startTag("", "identificador");
                ser.text(l.get(i).getIdentificador());
                ser.endTag("", "identificador");

                ser.startTag("", "fecha_hora");
                ser.text(l.get(i).getFechaHora());
                ser.endTag("", "fecha_hora");

                ser.endTag("", "marker");
            }
            ser.endTag("", "markers");
        }
        ser.endDocument();
        osw.flush();
        osw.close();
    }
//    public ArrayList<MyMarker> LeerBaseDatosRetunArrayMyMarker(){
//        ArrayList<ListMarker> lis = LeerBaseDatosMarkerXML();
//        ArrayList<MyMarker> lisMarker = new ArrayList<MyMarker>();
//        if (lis.size()!= 0){
//            for (int i=0;i < lis.size();i++){
//                double mlatitud = parseDouble(lis.get(i).getLatitud());
//                double mlongitud = parseDouble(lis.get(i).getLongitud());
//                String ident = lis.get(i).getIdentificador();
//                String fecha = lis.get(i).getFechaHora();
//                MyMarker myMarker = new MyMarker(this,new LatLong(mlatitud,mlongitud), AndroidGraphicFactory.convertToBitmap(getResources().getDrawable(R.drawable.location)),0,0,ident,fecha);
//                lisMarker.add(myMarker);
//            }
//
//        }
//        return lisMarker;
//    }
    public ArrayList<ListMarker> LeerBaseDatosMarkerXML() {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        ArrayList<ListMarker> terms = new ArrayList<>();

        try {
            File tarjeta = Environment.getExternalStorageDirectory();
            File file = new File(tarjeta.getAbsolutePath()/*.concat("/dbanatomia/")*/, "/Maps/dbmap/marker.xml");
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document dom = builder.parse(file);
            Element root = dom.getDocumentElement();
            NodeList items = root.getChildNodes();// getElementsByTagName("terminos");
            //  Node item2 = items.item(0);
            //  NodeList datosNoticia = item2.getChildNodes();
            for (int i = 0; i < items.getLength(); i++) {
                ListMarker createmarker = new ListMarker();
                Node item = items.item(i);
                NodeList datosNoticia2 = item.getChildNodes();
                for (int j = 0; j < datosNoticia2.getLength(); j++) {
                    Node dato = datosNoticia2.item(j);
                    String etiqueta = dato.getNodeName();

                    if (etiqueta.equals("latitud")) {
                        String texto = obtenerTexto(dato);
                        createmarker.setLatitud(texto);
                    } else if (etiqueta.equals("longitud")) {
                        String texto = obtenerTexto(dato);
                        createmarker.setLongitud(texto);
                    } else if (etiqueta.equals("identificador")) {
                        String texto = obtenerTexto(dato);
                        createmarker.setIdentificador(texto);
                    } else if (etiqueta.equals("fecha_hora")) {
                        String texto = obtenerTexto(dato);
                        createmarker.setFechaHora(texto);
                    }

                }
                terms.add(createmarker);
            }
        } catch(Exception ex) {
            //throw new RuntimeException(ex);

            return terms;
        }
        return terms;
    }
    private String obtenerTexto(Node dato)
    {
        StringBuilder texto = new StringBuilder();
        NodeList fragmentos = dato.getChildNodes();

        for (int k=0;k<fragmentos.getLength();k++)
        {
            texto.append(fragmentos.item(k).getNodeValue());
        }
        return texto.toString();
    }
    public void ActualizarBaseDatosXML(ListMarker lm){
        basemarker = LeerBaseDatosMarkerXML();
        basemarker.add(lm);
        try {
            CrearBaseDatosMarkerXML(basemarker);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void CrearBaseDatosMyMarker(ArrayList<MyMarker> l) throws IOException
    {
        XmlSerializer ser = Xml.newSerializer();

        File tarjeta = Environment.getExternalStorageDirectory();
        File file = new File(tarjeta.getAbsolutePath()/*.concat("/dbanatomia/")*/, "/Maps/dbmap/marker.xml");

        OutputStreamWriter osw = new OutputStreamWriter(new FileOutputStream(file));

        ser.setOutput(osw);
        if (l.size() == 0) {
            ser.startTag("","");
            ser.endTag("","");
        }
        else {
            ser.startTag("", "markers");

            for (int i = 0; i < l.size(); i++) {
                ser.startTag("", "marker");

                ser.startTag("", "latitud");
                ser.text(String.valueOf(l.get(i).getLatLong().latitude));
                ser.endTag("", "latitud");

                ser.startTag("", "longitud");
                ser.text(String.valueOf(l.get(i).getLatLong().longitude));
                ser.endTag("", "longitud");

                ser.startTag("", "identificador");
                ser.text(l.get(i).ident);
                ser.endTag("", "identificador");

                ser.startTag("", "fecha_hora");
                ser.text(l.get(i).fechaHora);
                ser.endTag("", "fecha_hora");

                ser.endTag("", "marker");
            }
            ser.endTag("", "markers");
        }
        ser.endDocument();
        osw.flush();
        osw.close();
    }

}
