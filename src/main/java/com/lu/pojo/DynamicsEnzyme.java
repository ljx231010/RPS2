package com.lu.pojo;

import com.lu.javabean.Url;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DynamicsEnzyme {
    private String ecNumber;
    private String sourceOrganismName;
    private String sourceOrganismTaxId;
    private double km;
    private double kmStandardValue;
    private List<Url> urls;
}
