package com.example.marcos.nuevacliente.SMS;
import android.telephony.SmsManager;
import android.util.Log;

/**
 * Created by marcos on 1/13/2016.
 */
public class EnviarSms {
    public boolean sendSms(String numerophone,String mensaje)
    {
        Log.i("Send SMS", "");
        try {
            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage(numerophone, null, mensaje, null, null);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

}
