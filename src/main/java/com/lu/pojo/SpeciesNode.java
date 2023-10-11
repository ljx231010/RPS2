package com.lu.pojo;

import com.lu.mapper.ITreeNodeData;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SpeciesNode implements ITreeNodeData, Serializable {
    private static final long serialVersionUID = 1L;
    private int nodeId;
    private String speciesName;
    private int parentNodeId;
    @Override
    public Object clone() {
        SpeciesNode obj = null;
        try {
            obj = (SpeciesNode) super.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        return obj;
    }
}
