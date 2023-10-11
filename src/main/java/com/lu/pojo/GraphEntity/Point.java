package com.lu.pojo.GraphEntity;

public class Point{
    public int data;
    public EdegeNode firstArc;
    public Point() {}
    public Point(int data)
    {
        this.data=data;
        this.firstArc=null;
    }

}