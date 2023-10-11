package com.lu.utils;

import com.lu.pojo.DynamicsEnzyme;

import java.util.Comparator;

public class DyENzymeCompartor implements Comparator<DynamicsEnzyme> {
    @Override
    public int compare(DynamicsEnzyme o1, DynamicsEnzyme o2) {
        return Double.compare(o2.getKm(), o1.getKm());
    }
}
