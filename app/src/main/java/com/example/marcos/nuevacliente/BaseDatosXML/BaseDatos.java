package com.example.marcos.nuevacliente.BaseDatosXML;

import android.os.Environment;
import android.util.Xml;

import com.example.marcos.nuevacliente.Listas.ListDatos;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xmlpull.v1.XmlSerializer;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

/**
 * Created by marcos on 1/28/2016.
 */
public class BaseDatos {

    public void CrearBaseDatosXML(ArrayList<ListDatos> l) throws IOException
    {
        XmlSerializer ser = Xml.newSerializer();

        File tarjeta = Environment.getExternalStorageDirectory();
        File file = new File(tarjeta.getAbsolutePath()/*.concat("/dbanatomia/")*/, "/Maps/dbmap/data.xml");

        OutputStreamWriter osw = new OutputStreamWriter(new FileOutputStream(file));

        ser.setOutput(osw);
        if (l.size() == 0) {
            ser.startTag("","");
            ser.endTag("","");
        }
        else {
            ser.startTag("", "moviles");

            for (int i = 0; i < l.size(); i++) {
                ser.startTag("", "movil");

                ser.startTag("", "identificador");
                ser.text(l.get(i).getNombre());
                ser.endTag("", "identificador");

                ser.startTag("", "numero");
                ser.text(l.get(i).getNumero());
                ser.endTag("", "numero");

                ser.startTag("", "image");
                ser.text(l.get(i).getImage());
                ser.endTag("", "image");

                ser.endTag("", "movil");
            }
            ser.endTag("", "moviles");
        }
        ser.endDocument();
        osw.flush();
        osw.close();
    }
    public ArrayList<ListDatos> LeerBaseDatosXML() {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        ArrayList<ListDatos> terms = new ArrayList<>();

        try {
            File tarjeta = Environment.getExternalStorageDirectory();
            File file = new File(tarjeta.getAbsolutePath()/*.concat("/dbanatomia/")*/, "/Maps/dbmap/data.xml");

            DocumentBuilder builder = factory.newDocumentBuilder();

            Document dom = builder.parse(file);



            Element root = dom.getDocumentElement();


            NodeList items = root.getChildNodes();// getElementsByTagName("terminos");

            //  Node item2 = items.item(0);
            //  NodeList datosNoticia = item2.getChildNodes();


            for (int i = 0; i < items.getLength(); i++) {
                ListDatos movil = new ListDatos();

                Node item = items.item(i);

                NodeList datosNoticia2 = item.getChildNodes();

                for (int j = 0; j < datosNoticia2.getLength(); j++) {
                    Node dato = datosNoticia2.item(j);
                    String etiqueta = dato.getNodeName();

                    if (etiqueta.equals("identificador")) {
                        String texto = obtenerTexto(dato);
                        movil.setNombre(texto);
                    } else if (etiqueta.equals("numero")) {
                        String texto = obtenerTexto(dato);
                        movil.setNumero(texto);
                    } else if (etiqueta.equals("image")) {
                        String texto = obtenerTexto(dato);
                        movil.setImage(texto);
                    }
                }
                terms.add(movil);
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
}
