package com.example.marcos.nuevacliente.Listas;

/**
 * Created by marcos on 1/14/2016.
 */

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.marcos.nuevacliente.R;

import java.util.ArrayList;


public class CustomAdapter extends BaseAdapter implements View.OnClickListener {

    /*********** Declare Used Variables *********/
    private Activity activity;
    private ArrayList data;
    private static LayoutInflater inflater=null;
    public Resources res;
    ListDatos tempValues=null;
    int i=0;
    public int selecdelayout;

    /*************  CustomAdapter Constructor *****************/
    public CustomAdapter(Activity a, ArrayList d, Resources resLocal, int selec) {

        /********** Take passed values **********/
        activity = a;
        data=d;
        res = resLocal;
        selecdelayout= selec;

        /***********  Layout inflator to call external xml layout () ***********/
        inflater = ( LayoutInflater )activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }

    /******** What is the size of Passed Arraylist Size ************/
    public int getCount() {

        if(data.size()<=0)
            return 1;
        return data.size();
    }

    public Object getItem(int position) {
        return position;
    }

    public long getItemId(int position) {
        return position;
    }

    /********* Create a holder Class to contain inflated xml file elements *********/
    public static class ViewHolder{

        public TextView text;
        public TextView text1;
        public TextView textWide;
        public ImageView image;

    }

    /****** Depends upon data size called for each row , Create each ListView row *****/
    public View getView(int position, View convertView, ViewGroup parent) {

        View vi = convertView;
        ViewHolder holder;

        if(convertView==null){

            /****** Inflate tabitem.xml file for each row ( Defined below ) *******/
            if (selecdelayout == -1){

                vi = inflater.inflate(R.layout.tabelminar,null);
            }
            else {
                vi = inflater.inflate(R.layout.tabitem, null);
            }
            /****** View Holder Object to contain tabitem.xml file elements ******/

            holder = new ViewHolder();
            holder.text = (TextView) vi.findViewById(R.id.textView2);
            holder.text1=(TextView)vi.findViewById(R.id.textView3);
            holder.image=(ImageView)vi.findViewById(R.id.image);

            /************  Set holder with LayoutInflater ************/
            vi.setTag( holder );
        }
        else
            holder=(ViewHolder)vi.getTag();

        if(data.size()<=0)
        {
            holder.text.setText("No Data");

        }
        else
        {
            /***** Get each Model object from Arraylist ********/
            tempValues=null;
            tempValues = ( ListDatos ) data.get( position );

            /************  Set Model values in Holder elements ***********/

            holder.text.setText( tempValues.getNombre() );
            holder.text1.setText( tempValues.getNumero() );
            //holder.image.setImageResource(
            //        res.getIdentifier(
            //                "com.androidexample.customlistview:drawable/"+tempValues.getImage()
            //                ,null,null));

            /******** Set Item Click Listner for LayoutInflater for each row *******/

            //vi.setOnClickListener(new OnItemClickListener( position ));
        }
        return vi;
    }

    @Override
    public void onClick(View v) {
        Log.v("CustomAdapter", "=====Row button clicked=====");
    }

    /********* Called when Item click in ListView ************/
    private class OnItemClickListener  implements View.OnClickListener {
        private int mPosition;

        OnItemClickListener(int position){
            mPosition = position;
        }

        @Override
        public void onClick(View arg0) {


            //CustomListViewAndroidExample sct = (CustomListViewAndroidExample)activity;

            /****  Call  onItemClick Method inside CustomListViewAndroidExample Class ( See Below )****/

            // sct.onItemClick(mPosition);
        }
    }
}
