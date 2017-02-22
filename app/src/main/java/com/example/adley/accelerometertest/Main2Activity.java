/*
Created by Adley Gin 2.11.16
ECE 473 HW2
Main2Activity.java is a second activity used for the list of files to be emailed.
 */
package com.example.adley.accelerometertest;

import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class Main2Activity extends AppCompatActivity {

    private customListAdapter myAdapter;
    private ArrayList<Model> modelArrayList;
    private ListView myList;
    private Email email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        /*
        Create a custom adapter for the checkbox listview. Allows me to set a custom text for each item row.
         */
        modelArrayList=new ArrayList<Model>();
        myAdapter=new customListAdapter(modelArrayList,this);
        myList=(ListView)findViewById(R.id.listView2);
        myList.setAdapter(myAdapter);

        email=new Email();  //Email object allows me to email stuff with attachements.

        /*
        Populate the list view of file names by going through the external public storage and grabbing all the files.
         */
        for(File f: Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS).listFiles()){
            Model m=new Model(f);
            modelArrayList.add(m);
        }

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }


    /*
    Allows the user to delete files selected with a checkbox from a listview of files.
    The function iterates backwards from the end of the list to the top of the list.
    I found that if you don't iterate backwards, the app will crash if you remove an item.
    After removing the file from the list, the file is also permanently deleted from the external storage.
     */
    public void deleteFilesSelected(View view){
        for(int i=modelArrayList.size()-1;i>=0;i--){
            if(modelArrayList.get(i).isChecked()){
                String name=modelArrayList.get(i).getFile().getName();
                myAdapter.removeItem(i);
                File f=new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS),name);
                f.delete();
            }
        }
    }

    /*
    This function emails the selected files after the user presses the email files button.
    it goes through the list, and adds the file's name to an arraylist. This arraylist of file names is then sent
    to the email object, which has a function to send an email with multiple attachments.
     */
    public void emailFilesSelected(View view){
        ArrayList<String> filePaths=new ArrayList<String>();
        for(Model m:modelArrayList){
            if(m.isChecked()){
                filePaths.add(m.getName());
            }
        }
        email.sendEmailWithMultipleAttachments(Main2Activity.this,filePaths);
    }

}
