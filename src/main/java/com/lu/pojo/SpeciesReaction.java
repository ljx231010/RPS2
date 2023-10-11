package com.lu.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Objects;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SpeciesReaction {
    private String rId;    private String rName;    private String definition;    private String equation;    private List<String> ecNumber;
    private List<Enzyme> ecNumber1;
    private String energy;
    private List<String> substrateId;
    private List<String> productId;
    private Boolean ifForeign;
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof SpeciesReaction)) return false;
        SpeciesReaction that = (SpeciesReaction) o;
        return ifForeign == that.ifForeign && Objects.equals(rId, that.rId) && Objects.equals(rName, that.rName) && Objects.equals(definition, that.definition) && Objects.equals(equation, that.equation) && Objects.equals(ecNumber, that.ecNumber) && Objects.equals(ecNumber1, that.ecNumber1) && Objects.equals(energy, that.energy) && Objects.equals(substrateId, that.substrateId) && Objects.equals(productId, that.productId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(rId, rName, definition, equation, ecNumber, ecNumber1, energy, substrateId, productId);
    }
}

