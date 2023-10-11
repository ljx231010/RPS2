package com.lu.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Objects;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class Reaction {
    private String rId;
    private String rName;
    private String definition;
    private String equation;

    private List<String> ecNumber;
    private List<Enzyme> ecNumber1;
    private String energy;
    private List<String> substrateId;

    private List<String> productId;


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Reaction)) return false;
        Reaction reaction = (Reaction) o;
        return Objects.equals(rId, reaction.rId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(rId, rName, definition, equation, ecNumber, ecNumber1, energy, substrateId, productId);
    }
}

