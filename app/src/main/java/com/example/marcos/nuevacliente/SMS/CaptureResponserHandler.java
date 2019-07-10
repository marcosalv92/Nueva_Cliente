package com.example.marcos.nuevacliente.SMS;

import android.content.Context;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.ResponseHandlerInterface;

import java.io.UnsupportedEncodingException;

import cz.msebera.android.httpclient.Header;

/**
 * Created by windows7 on 6/25/2019.
 */
public class CaptureResponserHandler extends AsyncHttpResponseHandler {
    Context context;
    public CaptureResponserHandler(Context context){
        this.context = context;
    }

    @Override
    public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
        try {
            String msg = new String(responseBody, "UTF-8");
            mostraMensaje(msg);


        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
        try {
            String msg = new String(responseBody, "UTF-8");
            mostraMensaje(msg);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    public void mostraMensaje(String msg){
        Toast.makeText(context,msg,Toast.LENGTH_LONG).show();
    }
}

