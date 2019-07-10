package com.example.marcos.nuevacliente.Dialogos;

/**
 * Created by marcos on 1/13/2016.
 */
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.util.Log;


public class DialogConfirmSend extends DialogFragment {

    public interface Dialogo1Interface{
        public void onDialogoPositivo(String phone,String nom,int posit);
        public void onDialogoNegativo();
    }

    Dialogo1Interface nuevo;
    String phone;
    String nom;
    int posit;
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        // Verify that the host activity implements the callback interface
        try {
            // Instantiate the NoticeDialogListener so we can send events to the host
            nuevo = (Dialogo1Interface) activity;
        } catch (ClassCastException e) {
            // The activity doesn't implement the interface, throw exception
            throw new ClassCastException(activity.toString()
                    + " must implement NoticeDialogListener");
        }
    }

    public DialogConfirmSend(String num,String nom,int posit ){this.phone = num;this.nom = nom;this.posit = posit;}
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        builder.setMessage("Desea enviar un sms para localizarlo?")
               // .setTitle("Confirmacion")
                .setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        nuevo.onDialogoPositivo(phone,nom,posit);
                        Log.i("Dialogos", "Confirmacion Aceptada.");
                        dialog.cancel();
                    }
                })
                .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        nuevo.onDialogoNegativo();
                        Log.i("Dialogos", "Confirmacion Cancelada.");
                        dialog.cancel();
                    }
                });

        return builder.create();
    }
}