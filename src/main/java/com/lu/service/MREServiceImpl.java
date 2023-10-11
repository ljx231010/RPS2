package com.lu.service;

import com.lu.mapper.CompoundMapper;
import com.lu.mapper.ReactionMapper;
import com.lu.mapper.SpeciesMapper;
import com.lu.pojo.Compound;
import com.lu.pojo.Path;
import com.lu.pojo.Reaction;
import com.lu.pojo.SpeciesNetwork1;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.*;

@Service
public class MREServiceImpl implements MREService {
    @Autowired
    private CompoundMapper compoundMapper;
    @Autowired
    private ReactionMapper reactionMapper;
    @Autowired
    private SpeciesMapper speciesMapper;

    public static String[] clusterMetabolites = {"C00001", "C00002", "C00003", "C00004", "C00005", "C00006", "C00007", "C00008", "C00009",
            "C00010", "C00011", "C00013", "C00015", "C01342", "C00019", "C00020", "C00035", "C00044", "C00080", "C00131"};    public static List<String> clusterMetabolites1 = new ArrayList<String>(Arrays.asList(clusterMetabolites));

    public void setCompoundMapper(CompoundMapper compoundMapper) {
        this.compoundMapper = compoundMapper;
    }

    public void setReactionMapper(ReactionMapper reactionMapper) {
        this.reactionMapper = reactionMapper;
    }

    public void setSpeciesMapper(SpeciesMapper speciesMapper) {
        this.speciesMapper = speciesMapper;
    }

        @Override
    public double calculate1(Path path, SpeciesNetwork1 speciesNetwork) throws IOException {
        double scoreOfPath = 0.0;
        double sum = 0.0;
        List<String> noInSPeciesRids = new ArrayList<>();
        Map<String, Double> AStepMap = new HashMap<>();
        for (int i = 0; i < path.getReactionsOfPath().size(); i++) {
            Map<String, Double> rScoreMap = new HashMap<>();            String rId = path.getReactionsOfPath().get(i).getRId();            Compound c = path.getCompoundsOfPath().get(i);
            List<String> l1 = path.getReactionsOfPath().get(i).getSubstrateId();
            List<String> l2 = path.getReactionsOfPath().get(i).getProductId();
            List<String> subs = l1.contains(c.getCId()) ? l1 : l2;            if (!speciesNetwork.getRIdList().contains(rId)) {
                noInSPeciesRids.add(rId);
            }
            List<Double> scoreList = new ArrayList<>();
            Map<String, Double> map2 = new HashMap<>();            for (String sub : subs) {
                if (clusterMetabolites1.contains(sub))
                    continue;
                Map<String, Double> map = new HashMap<>();                List<Reaction> rn = getRN(speciesNetwork, sub);
                Compound subCompound = compoundMapper.getCompoundById(sub);
                for (int j = 0; j < rn.size(); j++) {
                    double result = getfr(speciesNetwork, rn, rn.get(j), subCompound);
                    map.put(rn.get(j).getRId(), result);
                }
                map.putIfAbsent(rId, getfr(speciesNetwork, rn, reactionMapper.getReactionById(rId), subCompound));
                double score = getScoreOfAStep1(map, rId);                scoreList.add(score);
                map2.put(sub, map.get(rId));
            }

            double sum1 = 0.0;
            for (Double score : map2.values()) {
                sum1 += score;
            }
            double result = sum1 / map2.size();            AStepMap.put(rId, result);
            scoreOfPath += result;

        }
        return scoreOfPath;    }

        public Double cScoreMapToRScoreMap(Map<String, Double> cScoreMap) {
        if (cScoreMap.size() == 0)
            System.out.println("cScoreMap.size()==0");
        else if (cScoreMap.size() == 1)
            System.out.println("cScoreMap.size()==1");
        else if (clusterMetabolites1.containsAll(cScoreMap.keySet())) {
            System.out.println("clusterMetabolites1.containsAll(cScoreMap.keySet())");
        } else {
            List<String> removeKey = new ArrayList<>();
            for (String s : cScoreMap.keySet()) {
                if (clusterMetabolites1.contains(s))
                    removeKey.add(s);
            }
            for (String s : removeKey)
                cScoreMap.remove(s);
        }

        Double sum = 0.0;
        for (String s : cScoreMap.keySet()) {
            sum += cScoreMap.get(s);
        }
        Double result = sum / cScoreMap.size();
        return result;
    }

        public double getScoreOfAStep(Map<String, Double> map, String rId) {
        Double curReactionFr = map.get(rId);
        Map<String, Double> otherMap = new HashMap<>();        for (String id : map.keySet()) {
            if (map.get(id) >= curReactionFr)
                otherMap.put(id, map.get(id));
        }
        DecimalFormat df = new DecimalFormat("0.0000");
        double sum = 0.0;
        sum += curReactionFr;
        for (String id : otherMap.keySet()) {
            sum += otherMap.get(id);
        }
        return Double.parseDouble(df.format(curReactionFr / sum));
    }

        public double getScoreOfAStep1(Map<String, Double> map, String rId) {
        Map<String, Double> stringDoubleMap = dataStandard(map);
        return stringDoubleMap.get(rId);
    }
    
        @Override
    public double getfr(SpeciesNetwork1 speciesNetwork, List<Reaction> rn, Reaction curReaction, Compound c) {
        double R = 8.314;        double T = 298.15;        double mean = energyFromStringToDouble(curReaction.getEnergy());        double energy = judgeDirectionOfReaction(c, curReaction) == 1 ? mean : -mean;        double b1 = Math.pow(Math.E, -energy / (R * T));        double b2 = 0;
        for (Reaction reaction : rn) {
            if (!reaction.getRId().equals(curReaction.getRId())) {
                double m = energyFromStringToDouble(reaction.getEnergy());
                double e = judgeDirectionOfReaction(c, reaction) == 1 ? m : -m;
                b2 += Math.pow(Math.E, -e / (R * T));
            }
        }
        DecimalFormat df = new DecimalFormat("0.0000");        return Double.parseDouble(df.format(Math.log(b1 / (1 + b1 + b2))));    }

        public int judgeDirectionOfReaction(Compound substrate, Reaction reaction) {
        return reaction.getSubstrateId().contains(substrate.getCId()) ? 1 : -1;
    }

        @Override
    public List<Reaction> getRN(SpeciesNetwork1 speciesNetwork, String cId) {
        int[][] network = speciesNetwork.getNetwork();
        int index = speciesNetwork.getCIdList().indexOf(cId);
        if (index == -1) {
            System.out.println(speciesNetwork.getSpeciesId()+" not found");
            return new ArrayList<>();
        }
        List<Reaction> rIdList = new ArrayList<>();
        for (int i = 0; i < network.length; i++) {
            if (network[i][index] != 0)
                rIdList.add(reactionMapper.getReactionById(speciesNetwork.getRIdList().get(i)));
        }
        return rIdList;
    }


        public double energyFromStringToDouble(String energy) {
        if ("nan".equals(energy) || energy == null)
            return 0;
        else
            return Double.parseDouble(energy);
    }

    public void writeToTxt(String fileName, String method, String content) throws IOException {
        FileWriter fw;
        if (method.equals("a") || method.equals("a+"))
            fw = new FileWriter(fileName, true);
        else {
            fw = new FileWriter(fileName);
        }
        BufferedWriter bf = new BufferedWriter(fw);
        bf.write(content);
        bf.flush();
        bf.close();
        fw.close();
    }

    public void test1() throws IOException {
        SpeciesNetworkServiceImpl speciesNetworkService2 = new SpeciesNetworkServiceImpl();
        String[] ss = new String[]{"aaa", "aab", "aac", "aace", "aaci", "aacn", "aaci", "aad", "aae", "aaf", "aag"};
        SpeciesNetwork1 speciesNetwork = speciesNetworkService2.getArrayFromTxt("eco");
        String path = "C00022->R00226->C06010->R05071->C04181->R04440->C04272->R04441->C00141->R01434->C00183";
        List<String> speciesIdList = speciesMapper.getAllSpeciesId();

        PathServiceImpl pathService = new PathServiceImpl();
        long l6 = System.currentTimeMillis();
        Path completePath = pathService.createCompletePath(path);
        MREServiceImpl mreService = new MREServiceImpl();
        Map<String, Double> scoreMap = new HashMap<>();
        long l1 = System.currentTimeMillis();
        for (int i = 0; i < speciesIdList.size(); i++) {
            String speciesId = speciesIdList.get(i);
            SpeciesNetwork1 speciesNetwork1 = speciesNetworkService2.getArrayFromTxt(speciesId);
            double scoreOfPath = mreService.calculate1(completePath, speciesNetwork1);
            scoreMap.put(speciesId, scoreOfPath);
        }
        dataStandard(scoreMap);
    }

        @Override
    public Map<String, Double> dataStandard(Map<String, Double> map) {
        double max = 0;
        double min = 0;
        for (String key : map.keySet()) {
            max = map.get(key);
            min = map.get(key);
            break;
        }
        for (Double x : map.values()) {
            if (x > max)
                max = x;
            if (x < min)
                min = x;
        }
        Map<String, Double> newMap = new HashMap<>();
        DecimalFormat df = new DecimalFormat("0.0000");        double newValue = 0;
        for (String key : map.keySet()) {
            newValue = (map.get(key) - min) / (max - min);
            if (Double.compare(newValue, Double.NaN) == 0) {
                newMap.put(key, 1.0);
            }
            else{
            newMap.put(key, Double.valueOf(df.format(newValue)));
            }
        }
        return newMap;
    }

}
