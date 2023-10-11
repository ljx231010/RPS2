package com.lu.pojo.GraphEntity;

public class EdegeNode{
    public int adjvex;
    public int value;
    public EdegeNode nextEdge;
    public EdegeNode() {}
    public EdegeNode(int adjvex,int value)
    {
        this.adjvex=adjvex;
        this.value=value;
        this.nextEdge=null;
    }
}