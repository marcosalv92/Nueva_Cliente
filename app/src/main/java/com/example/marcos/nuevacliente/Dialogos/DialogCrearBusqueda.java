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
 * Created by marcos on 1/13/2016.
 */
public class DialogCrearBusqueda extends DialogFragment {
    public interface Dialogo2Interface{
        public void onDialogoPositivo2(String inputNombre, String inputNumero,String inputImage);
        public void onDialogoNegativo2();
    }

    Dialogo2Interface nuevo;
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        // Verify that the host activity implements the callback interface
        try {
            // Instantiate the NoticeDialogListener so we can send events to the host
            nuevo = (Dialogo2Interface) activity;
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
       final View view = inflater.inflate(R.layout.dialog2_inflate, null);
         builder.setView(view)

                .setPositiveButton("Adicionar", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        EditText et1 = (EditText) view.findViewById(R.id.nombre);
                        EditText et2 = (EditText) view.findViewById(R.id.numero);
                        String inputNombre = et1.getText().toString();
                        String inputNumero = et2.getText().toString();
                        String inputImage = "envLocalizar";

                        nuevo.onDialogoPositivo2(inputNombre,inputNumero,inputImage);
//                        Log.i("Dialogos", "Confirmacion Aceptada.");
                        dialog.cancel();
                    }
                })
                .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        nuevo.onDialogoNegativo2();
//                        Log.i("Dialogos", "Confirmacion Cancelada.");
                          dialog.cancel();
                    }
                });

        return builder.create();
    }
}

