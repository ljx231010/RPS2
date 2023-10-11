package com.lu.service;

import com.lu.pojo.Compound;
import com.lu.pojo.Path;
import com.lu.pojo.Reaction;
import com.lu.pojo.SpeciesNetwork1;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public interface MREService {
        double calculate1(Path path, SpeciesNetwork1 speciesNetwork) throws IOException;

        double getfr(SpeciesNetwork1 speciesNetwork, List<Reaction> rn, Reaction curReaction, Compound c);

    List<Reaction> getRN(SpeciesNetwork1 speciesNetwork, String cId);

    Map<String, Double> dataStandard(Map<String, Double> map);
}
