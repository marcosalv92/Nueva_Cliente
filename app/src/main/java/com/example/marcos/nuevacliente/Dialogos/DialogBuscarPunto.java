package com.example.marcos.nuevacliente.Dialogos;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import com.example.marcos.nuevacliente.R;

/**
 * Created by marcos on 1/31/2016.
 */
public class DialogBuscarPunto extends DialogFragment {
    public interface Dialogo5Interface{
        public void onDialogoPositivo5(double lat,double lon, String iden);
        public void onDialogoNegativo5();
    }

    Dialogo5Interface nuevo;
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        // Verify that the host activity implements the callback interface
        try {
            // Instantiate the NoticeDialogListener so we can send events to the host
            nuevo = (Dialogo5Interface) activity;
        } catch (ClassCastException e) {
            // The activity doesn't implement the interface, throw exception
            throw new ClassCastException(activity.toString()
                    + " must implement NoticeDialogListener");
        }
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        final View view = inflater.inflate(R.layout.dialog5_inflate, null);
        builder.setView(view)

                .setPositiveButton("Buscar", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        EditText et1 = (EditText) view.findViewById(R.id.editText2);
                        EditText et2 = (EditText) view.findViewById(R.id.editText3);
                        EditText et3 = (EditText) view.findViewById(R.id.editText4);
                        String latitud = et1.getText().toString();
                        String longitud = et2.getText().toString();
                        String iden = et3.getText().toString();
                        double lat = Double.parseDouble(latitud);
                        double lon = Double.parseDouble(longitud);
                        nuevo.onDialogoPositivo5(lat,lon,iden);
//                        Log.i("Dialogos", "Confirmacion Aceptada.");
                        dialog.cancel();
                    }
                })
                .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        nuevo.onDialogoNegativo5();
//                        Log.i("Dialogos", "Confirmacion Cancelada.");
                        dialog.cancel();
                    }
                });

        return builder.create();
    }
}
