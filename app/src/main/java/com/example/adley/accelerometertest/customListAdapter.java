package com.example.adley.accelerometertest;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by adley on 2/8/2016.
 */
public class customListAdapter extends ArrayAdapter<Model> {
    //private ArrayList<File> fileArrayList;
    private ArrayList<Model> modelArrayList;
    private Context context;

    /*public customListAdapter(ArrayList<File> files, Context contxt){
        super(contxt, R.layout.mylayout, files);
        this.fileArrayList=files;
        this.context=contxt;
    }*/

    public customListAdapter(ArrayList<Model> models, Context contxt){
        super(contxt, R.layout.mylayout, models);
        this.modelArrayList=models;
        this.context=contxt;
    }

    static class ViewHolder{
        protected TextView fileName;
        protected CheckBox checkBox;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent){
        ViewHolder viewHolder=null;
        if(convertView==null){
            LayoutInflater inflater= ((Activity)context).getLayoutInflater();
            convertView=inflater.inflate(R.layout.mylayout, parent, false);
            viewHolder=new ViewHolder();
            viewHolder.fileName=(TextView)convertView.findViewById(R.id.fileNameTextView);
            viewHolder.checkBox=(CheckBox)convertView.findViewById(R.id.checkBox);
            viewHolder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    int getPosition=(Integer) buttonView.getTag();
                    modelArrayList.get(getPosition).setChecked(buttonView.isChecked());
                }
            });
            convertView.setTag(viewHolder);
            convertView.setTag(R.id.fileNameTextView, viewHolder.fileName);
            convertView.setTag(R.id.checkBox,viewHolder.checkBox);
        }
        else{
            viewHolder=(ViewHolder)convertView.getTag();
        }
        viewHolder.checkBox.setTag(position);
        viewHolder.fileName.setText(modelArrayList.get(position).getName());
        viewHolder.checkBox.setChecked(modelArrayList.get(position).isChecked());

        //TextView fileName=(TextView)convertView.findViewById(R.id.fileNameTextView);
        //CheckBox checkBox=(CheckBox)convertView.findViewById(R.id.checkBox);

        //File f=fileArrayList.get(position);
        //fileName.setText(f.getName());

        return convertView;
    }

    public void removeItem(int pos){
        modelArrayList.remove(pos);
        notifyDataSetChanged();
    }
    @Override
    public void remove(Model m){
        modelArrayList.remove(m);
        notifyDataSetChanged();
    }

    public ArrayList<Model> getModelArrayList(){
        return this.modelArrayList;
    }




}
