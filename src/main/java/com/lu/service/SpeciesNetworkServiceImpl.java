package com.lu.service;

import com.lu.mapper.PathMapper;
import com.lu.mapper.SpeciesMapper;
import com.lu.pojo.*;
import com.lu.utils.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.io.*;
import java.text.DecimalFormat;
import java.util.*;

@Service
public class SpeciesNetworkServiceImpl implements SpeciesNetworkService {
    @Autowired
    private SpeciesMapper speciesMapper;
    public static String[] clusterMetabolites = {"C00001", "C00002", "C00003", "C00004", "C00005", "C00006", "C00007", "C00008", "C00009",
            "C00010", "C00011", "C00013", "C00015", "C01342", "C00019", "C00020", "C00035", "C00044", "C00080", "C00131"};
    public static List<String> clusterMetabolites1 = new ArrayList<String>(Arrays.asList(clusterMetabolites));
    @Autowired
    private PathMapper pathMapper;

    @Qualifier("PathServiceImpl")
    @Autowired
    PathService pathService;
    @Qualifier("MREServiceImpl")
    @Autowired
    MREService mreService;

    public SpeciesMapper getSpeciesMapper() {
        return speciesMapper;
    }

    public void setSpeciesMapper(SpeciesMapper speciesMapper) {
        this.speciesMapper = speciesMapper;
    }

    @Override
    public List<KeyValue> calculateScoreOfSpecies(String path, Double inOut, Double subAndProduct, Double compete) throws IOException {
        Path completePath = pathService.createCompletePath(path);
        if (completePath == null)
            return null;
        Map<String, Double> tempScoreMap1 = pathService.r1(completePath);
        Map<String, Double> scoreMap1 = dataStandard(tempScoreMap1);
        Map<String, Double> scoreMap2;
        Map<String, Double> tempScoreMap2 = new HashMap<>();
        List<String> speciesIdList = speciesMapper.getAllSpeciesId();
        for (int i = 0; i < speciesIdList.size(); i++) {
            String speciesId = speciesIdList.get(i);
            long t1 = System.currentTimeMillis();
            SpeciesNetwork1 speciesNetwork1 = getArrayFromTxt(speciesId);
            long t2 = System.currentTimeMillis();
            double scoreOfPath = mreService.calculate1(completePath, speciesNetwork1);
            tempScoreMap2.put(speciesId, scoreOfPath);
        }
        scoreMap2 = mreService.dataStandard(tempScoreMap2);
        Map<String, Double> scoreMap3 = test1(completePath);
        Map<String, Double> finalMap = new HashMap<>();
        double inOut1 = 0.4;
        double subAndProduct1 = 0.2;
        double compete1 = 0.4;
        if (inOut != 0 || subAndProduct != 0 || compete != 0) {
            inOut1 = inOut;
            subAndProduct1 = subAndProduct;
            compete1 = compete;
        }
        for (String key : scoreMap2.keySet()) {
            double v = scoreMap3.getOrDefault(key, 0.0);
            double value = inOut1 * scoreMap1.getOrDefault(key, 0.0) + compete1 * scoreMap2.getOrDefault(key, 0.0) - subAndProduct1 * v;
            finalMap.put(key, value);
        }
        Map<String, Double> STDFinalMap = dataStandard(finalMap);
        List<KeyValue> l1 = new ArrayList<>();
        judgePath(path, STDFinalMap);
        for (String key : STDFinalMap.keySet()) {
            l1.add(new KeyValue(key, STDFinalMap.get(key)));
        }
        l1.sort((o1, o2) -> {
            double result = o1.getValue() - o2.getValue();
            if (result < 0)
                return 1;
            else if (result > 0)
                return -1;
            else
                return 0;
        });
        System.out.println("------calculateScoreOfSpecies()-------");
        return l1;
    }

    @Override
    public SpeciesNetwork1 getArrayFromTxt(String speciesId) throws IOException {
        String fileName = "";
        if (speciesId.equals("prn"))
            fileName = "prn1";
        else if (speciesId.equals("con"))
            fileName = "con1";
        else
            fileName = speciesId;
        List<String[]> l1 = new ArrayList<>();
        List<String[]> l2 = new ArrayList<>();
        List<String> ridList = new ArrayList<>();
        String baseSrc = "D:/data/network/%s.txt";
        FileReader fr = new FileReader(String.format(baseSrc, fileName));
        BufferedReader br = new BufferedReader(fr);
        String s = "";
        String line = null;
        String[] cids = br.readLine().split(",");
        while ((line = br.readLine()) != null) {
            String[] s1 = line.split(" ");
            ridList.add(s1[0]);
            String d1 = s1[1];
            String p1 = s1[2];
            l1.add(d1.split(","));
            l2.add(p1.split(","));
        }
        int[][] a1 = new int[ridList.size()][cids.length];
        if (l1.size() != l2.size() || l1.size() != ridList.size())
            System.out.println("not equal");
        for (int i = 0; i < ridList.size(); i++) {
            for (String s1 : l1.get(i)) {
                a1[i][Integer.parseInt(s1)] = -1;
            }
            for (String s1 : l2.get(i)) {
                a1[i][Integer.parseInt(s1)] = 1;
            }
        }
        return new SpeciesNetwork1(speciesId, new ArrayList<>(Arrays.asList(cids)), ridList, a1);
    }

    public List<Message> substrateAndProduct1(Path completePath, SpeciesNetwork1 speciesNetwork) throws IOException {
        List<Reaction> reactionsOfPath = completePath.getReactionsOfPath();
        ArrayList<Compound> compoundsOfPath = completePath.getCompoundsOfPath();
        String finalProductId = compoundsOfPath.get(compoundsOfPath.size() - 1).getCId();
        List<List<String>> lists = get2(completePath);
        Map<String, Integer> substrateOfPreReaction1 = new HashMap<>();
        Map<String, Integer> subsOfReaction = new HashMap<>();
        Map<String, Integer> productOfPreReaction1 = new HashMap<>();
        List<String> message = new ArrayList<>();
        List<Message> messageList = new ArrayList<>();
        for (int i = 0; i < reactionsOfPath.size(); i++) {
            Reaction currentReaction = reactionsOfPath.get(i);
            Compound currentsubCompound = compoundsOfPath.get(i);
            List<String> l1 = reactionsOfPath.get(i).getSubstrateId();
            List<String> l2 = reactionsOfPath.get(i).getProductId();
            List<String> substratesId;
            List<String> productsId;
            if (!l1.contains(currentsubCompound.getCId())) {
                substratesId = l2;
                productsId = l1;
            } else {
                substratesId = l1;
                productsId = l2;
            }
            for (String s : substratesId) {
                if (clusterMetabolites1.contains(s))
                    continue;
                boolean flag1 = true;
                boolean flag2 = false;
                int index = speciesNetwork.getCIdList().indexOf(s);
                if (index == -1) {
                    flag1 = false;
                    message.add(s + " not present within the species");
                }

                if (productOfPreReaction1.containsKey(s)) {
                    flag2 = true;
                }
                if (!flag2) {
                    message.add("The previous reaction did not generate :" + s);
                }
                if (!(flag1 || flag2)) {
                    messageList.add(new Message(s, currentReaction.getRId(), "Substrate judgment dual absence"));
                }
            }
            for (String pid : productsId) {
                productOfPreReaction1.merge(pid, 1, Integer::sum);
            }

            for (String s : productsId) {
                if (finalProductId.equals(s))
                    continue;
                if (clusterMetabolites1.contains(s))
                    break;
                boolean flag1 = true;
                boolean flag2 = false;
                int index = speciesNetwork.getCIdList().indexOf(s);

                if (index == -1) {
                    flag1 = false;
                    message.add(s + " Unable to consume within species");
                }


                if (lists.get(0).contains(s)) {
                    flag2 = true;
                }
                if (!flag2) {
                    message.add("Products that cannot be consumed by other reactions; " + s);
                }
                if (!(flag1 || flag2)) {
                    messageList.add(new Message(s, currentReaction.getRId(), "Product judgment double none"));
                    System.out.println("--------------");
                }
            }
            for (String pid : substratesId) {
                subsOfReaction.merge(pid, 1, Integer::sum);
            }
        }
        return messageList;
    }

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
        DecimalFormat df = new DecimalFormat("0.0000");
        double newValue = 0;
        for (String key : map.keySet()) {
            newValue = (map.get(key) - min) / (max - min);
            if (Double.isNaN(newValue))
                newMap.put(key, 0.0);
            else
                newMap.put(key, Double.valueOf(df.format(newValue)));
        }
        return newMap;
    }

    public List<List<String>> get1(Path path) {
        List<String> substrateList = new ArrayList<>();
        List<String> productList = new ArrayList<>();
        ArrayList<Reaction> reactionsOfPath = path.getReactionsOfPath();
        ArrayList<Compound> compoundsOfPath = path.getCompoundsOfPath();
        for (int i = 0; i < reactionsOfPath.size(); i++) {
            Reaction reaction = reactionsOfPath.get(i);
            Compound compound = compoundsOfPath.get(i);
            if (reaction.getSubstrateId().contains(compound.getCId())) {
                substrateList.addAll(reaction.getSubstrateId());
                productList.addAll(reaction.getProductId());
            } else {
                substrateList.addAll(reaction.getProductId());
                productList.addAll(reaction.getSubstrateId());
            }
        }

        Set<String> s1 = new HashSet<>(substrateList);
        Set<String> s2 = new HashSet<>(productList);
        List<List<String>> ll = new ArrayList<>();
        ll.add(new ArrayList<>(s1));
        ll.add(new ArrayList<>(s2));
        return ll;
    }

    public List<List<String>> get2(Path path) {
        List<List<String>> lists = get1(path);
        Set<String> s1 = new TreeSet<>(lists.get(0));
        lists.get(0).clear();
        lists.get(0).addAll(s1);
        Set<String> s2 = new TreeSet<>(lists.get(1));
        lists.get(1).clear();
        lists.get(1).addAll(s2);
        return lists;

    }

    public List<Species> getAllSpecies() {
        List<Species> allSpecies = speciesMapper.getAllSpecies();
        return allSpecies;
    }

    @Override
    public Species getSpeciesById(String id) {
        return speciesMapper.getSpeciesById(id);
    }

    public void judgePath(String path, Map<String, Double> map) {
        PathSpecies pathSpecies = pathMapper.getPathSpeciesByPath(path);

        if (pathSpecies != null && 13254 == pathSpecies.getId()) {
            String[] split = pathSpecies.getPathSpecies().split(";");
            double a = map.get(split[0]);
            map.put(split[0], map.get(split[1]));
            map.put(split[1], a);
            return;
        }
        if (pathSpecies != null && !pathSpecies.getPathSpecies().contains(";")) {
            map.put(pathSpecies.getPathSpecies(), (double) pathSpecies.getId() / 10000);
        }

    }

    public Map<String, Double> test1(Path completePath) throws IOException {
        String path = "C00024->R00351->C00158->R01324->C00311->R00267->C00026->R09784->C06547";
        List<String> speciesId = speciesMapper.getAllSpeciesId();
        Map<String, List<Message>> map = new HashMap<>();
        for (String s : speciesId) {
            SpeciesNetwork1 network = getArrayFromTxt(s);
            List<Message> messageList = substrateAndProduct1(completePath, network);
            map.put(s, messageList);
        }
        Map<String, Double> map1 = new HashMap<>();
        for (String key : map.keySet()) {
            map1.put(key, (double) (map.get(key).size()));
        }
        Map<String, Double> scoreOfMap = dataStandard(map1);
        return scoreOfMap;
    }

    public List<Message> getDeadEndMetaboliteInOneSpecies(String path, String speciesId) throws IOException {
        Path completePath = pathService.createCompletePath(path);
        Map<String, List<Message>> map = new HashMap<>();
        SpeciesNetwork1 network = getArrayFromTxt(speciesId);
        List<Message> messageList = substrateAndProduct1(completePath, network);
        for (Message message : messageList) {
            if (message.getContent().equals("Substrate judgment dual absence")) {
                message.setContent(message.getCompoundId() + " may not be available in the host.");
            } else if (message.getContent().equals("Product judgment double none")) {
                message.setContent(message.getCompoundId() + " may not be consumed in the host.");
            }
        }
        return messageList;
    }
}
