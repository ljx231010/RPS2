package com.lu.mapper;

public interface ITreeNodeData extends Cloneable{

    int getNodeId();


    String getSpeciesName();

    int getParentNodeId();


    public Object clone();
}