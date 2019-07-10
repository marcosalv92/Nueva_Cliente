package com.example.marcos.nuevacliente.Dialogos;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;

/**
 * Created by windows7 on 11/3/2017.
 */
public class DialogDetenerEnvio extends android.support.v4.app.DialogFragment {

    public interface Dialogo6Interface {
        public void onDialogoPositivo6(String phone,String nom,int posit);
        public void onDialogoNegativo6();

    }
    Dialogo6Interface nuevo;
    String phone;
    String nom;
    int posit;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        // Verify that the host activity implements the callback interface
        try {
            // Instantiate the NoticeDialogListener so we can send events to the host
            nuevo = (Dialogo6Interface) activity;
        } catch (ClassCastException e) {
            // The activity doesn't implement the interface, throw exception
            throw new ClassCastException(activity.toString()
                    + " must implement NoticeDialogListener");
        }
    }

    public DialogDetenerEnvio(String num,String nom,int posit ){this.phone = num;this.nom = nom;this.posit=posit;}
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        builder.setMessage("Desea enviar un sms para detener localización?")
                // .setTitle("Confirmacion")
                .setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        nuevo.onDialogoPositivo6(phone,nom,posit);
                        Log.i("Dialogos", "Confirmacion Aceptada.");
                        dialog.cancel();
                    }
                })
                .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        nuevo.onDialogoNegativo6();
                        Log.i("Dialogos", "Confirmacion Cancelada.");
                        dialog.cancel();
                    }
                });

        return builder.create();
    }
}



