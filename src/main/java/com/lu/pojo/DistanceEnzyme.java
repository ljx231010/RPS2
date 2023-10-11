package com.lu.pojo;

import com.lu.javabean.Url;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DistanceEnzyme {
    private String ecNumber;
    private String sourceOrganismId;
    private String sourceOrganismName;
    private double distance;
    private double distanceStandardValue;
    private List<Url> urls;
}
