package com.lu.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lu.mapper.CompoundMapper;
import com.lu.mapper.EnzymeMapper;
import com.lu.mapper.ReactionMapper;
import com.lu.pojo.Compound;
import com.lu.pojo.Enzyme;
import com.lu.pojo.Path;
import com.lu.pojo.Reaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class PathServiceImpl implements PathService {

    @Autowired
    private CompoundMapper compoundMapper;
    @Autowired
    private ReactionMapper reactionMapper;
    @Autowired
    private EnzymeMapper enzymeMapper;

    public void setCompoundMapper(CompoundMapper compoundMapper) {
        this.compoundMapper = compoundMapper;
    }

    public void setReactionMapper(ReactionMapper reactionMapper) {
        this.reactionMapper = reactionMapper;
    }

    public void setEnzymeMapper(EnzymeMapper enzymeMapper) {
        this.enzymeMapper = enzymeMapper;
    }

    public Path createCompletePath(String path) {
        Path newPath = new Path();
        String[] strings = path.split("->");
        for (String s : strings) {
            if (!s.matches("^C\\d{5}$")&&!s.matches("^R\\d{5}$"))
                return null;
            if (s.startsWith("C") || s.startsWith("G")) {
                Compound compound = compoundMapper.getCompoundById(s);
                if (compound == null)
                    return null;
                newPath.getCompoundsOfPath().add(compound);
            } else if (s.startsWith("R")) {
                Reaction reaction = reactionMapper.getReactionById(s);
                if (reaction == null) {
                    return null;
                }
                reactionAddEnzymeObject(reaction);
                newPath.getReactionsOfPath().add(reaction);
            } else {
                System.out.println("Path error");
            }
        }
        ArrayList<Reaction> reactionsOfPath = newPath.getReactionsOfPath();
        for (int i = 0; i < reactionsOfPath.size(); i++) {
            Reaction reaction = reactionsOfPath.get(i);
            String c1 = i<newPath.getCompoundsOfPath().size()-1?newPath.getCompoundsOfPath().get(i).getCId():"";
            String c2 = i+1<=newPath.getCompoundsOfPath().size()-1?newPath.getCompoundsOfPath().get(i + 1).getCId():"";
            if (!((reaction.getSubstrateId().contains(c1) && reaction.getProductId().contains(c2)) || (reaction.getSubstrateId().contains(c2) && reaction.getProductId().contains(c1)))) {
                if (!reaction.getEquation().contains(c1)) {
//                    newPath.addMessage("Compound " + c1 + " does not belong to reaction " + reaction.getRId());
                    return null;
                }
                if (!reaction.getEquation().contains(c2)) {
//                    newPath.addMessage("Compound " + c2 + " does not belong to reaction " + reaction.getRId());
                    return null;

                }
                if ((reaction.getSubstrateId().contains(c1) && reaction.getSubstrateId().contains(c2)) || (reaction.getProductId().contains(c1) && reaction.getProductId().contains(c2)))
//                    newPath.addMessage("Compound " + c1 + " and Compound " + c2 + " are on the same side of the reaction "+reaction.getRId());
                    return null;
            }
        }
        return newPath;
    }


    public Map<String, Double> r1(Path path) {
        Map<String, Integer> SNmap = new HashMap<>();
        ArrayList<Reaction> reactionsOfPath = path.getReactionsOfPath();
        for (Reaction reaction : reactionsOfPath) {
            Map<String, Integer> map = new HashMap<>();
            if (reaction.getEcNumber1() == null) {
                System.out.println(reaction.getRId());
                continue;
            }
            for (Enzyme enzyme : reaction.getEcNumber1()) {
                for (String s : enzyme.getSpecies()) {
                    map.putIfAbsent(s, 1);
                }
            }
            map.forEach((key, value) -> SNmap.merge(key, value, Integer::sum));
        }
        Map<String, Double> scoreMap = new HashMap<>();
        for (String speciesId : SNmap.keySet()) {
            double value = (double) SNmap.get(speciesId) / path.getReactionsOfPath().size();
            scoreMap.put(speciesId, value);
        }
        return scoreMap;
    }


    public Map<String, Double> dataStandard(Map<String, Double> map) {
        double max = 0;
        double min = 0;
        for (Double x : map.values()) {
            if (x > max)
                max = x;
            if (x < min)
                min = x;
        }
        Map<String, Double> newMap = new HashMap<>();
        double newValue = 0;
        for (String key : map.keySet()) {
            newValue = (map.get(key) - min) / (max - min);
            newMap.put(key, newValue);
        }
        return newMap;
    }


    public void reactionAddEnzymeObject(Reaction reaction) {
        List<String> ecNumber = reaction.getEcNumber();
        List<Enzyme> enzymeList = new ArrayList<>();
        for (String s : ecNumber) {
            enzymeList.add(enzymeMapper.getEnzymeByEcNumber(s));
        }
        reaction.setEcNumber1(enzymeList);
    }

    @Override
    public Map<String, String> getCompoundIdToName(String path) {
        Map<String, String> map = new HashMap<>();
        String[] split = path.split("->");
        String regex = "C[0-9]{5}";
        for (String s : split) {
            if (s.matches(regex)) {
                map.put(s, compoundMapper.getCompoundNameById(s).split(";")[0]);
            }
        }
        return map;
    }

    @Override
    public Map<String, String> getReactionIdToName(String path) {
        Map<String, String> map = new HashMap<>();
        String[] split = path.split("->");
        String regex = "R[0-9]{5}";
        for (String s : split) {
            if (s.matches(regex)) {
                map.put(s, reactionMapper.getReactionNameById(s).split(";")[0]);
            }
        }
        return map;
    }

    @Override
    public Reaction getReactionById(String id) {
        return reactionMapper.getReactionById(id);
    }
}
