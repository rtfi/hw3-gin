package com.example.adley.accelerometertest;

/**
 * Created by adley on 2/1/2016.
 */
public class AccelerometerDataPoint {
    private float x;
    private float y;
    private float z;

    public AccelerometerDataPoint(){
        x=0;
        y=0;
        z=0;
    }
    public AccelerometerDataPoint(float newX, float newY, float newZ){
        this.x=newX;
        this.y=newY;
        this.z=newZ;
    }

    public void setX(float newX) {
        this.x=newX;
    }
    public void setY(float newY){
        this.y=newY;
    }
    public void setZ(float newZ){
        this.z=newZ;
    }

    public float getX(){
        return this.x;
    }
    public float getY(){
        return this.y;
    }
    public  float getZ(){
        return this.z;
    }
}
