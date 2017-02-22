package com.example.adley.accelerometertest;

import java.io.File;

/**
 * Created by adley on 2/8/2016.
 */
public class Model {
    private File file;
    String name;
    private boolean checked;

    public Model(File f){
        this.file=f;
        this.name=f.getName();
    }

    public File getFile(){
        return this.file;
    }
    public void setFile(File f){
        this.file=f;
    }
    public boolean isChecked(){
        return checked;
    }
    public void setChecked(boolean selection){
        checked=selection;
    }
    public String getName(){
        return this.name;
    }
    public void setName(String n){
        this.name=n;
    }

}
