package com.lu.pojo;

import com.lu.javabean.Url;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EnzymeResult {
    private String ecNumber;
    private String sourceOrganismId;
    private String sourceOrganismName;
    private String sourceOrganismTaxId;
    private String sourceOrganismTaxName;
    private double distanceStandardValue;
    private double kmStandardValue;
    private double score;
    private List<Url> urls;
}
