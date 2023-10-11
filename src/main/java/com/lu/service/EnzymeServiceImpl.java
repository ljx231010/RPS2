package com.lu.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.lu.javabean.Url;
import com.lu.mapper.DynamicsEnzymeMapper;
import com.lu.mapper.EnzymeMapper;
import com.lu.mapper.ReactionMapper;
import com.lu.mapper.SpeciesMapper;
import com.lu.pojo.*;
import com.lu.utils.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service
public class EnzymeServiceImpl implements EnzymeService {
    @Autowired
    private SpeciesMapper speciesMapper;

    @Autowired
    private EnzymeMapper enzymeMapper;

    @Autowired
    private DynamicsEnzymeMapper dynamicsEnzymeMapper;

    @Autowired
    private ReactionMapper reactionMapper;

    @Qualifier("PathServiceImpl")
    @Autowired
    PathService pathService;

    @Qualifier("MREServiceImpl")
    @Autowired
    MREService mreService;

    @Qualifier("SpeciesNetworkServiceImpl")
    @Autowired
    SpeciesNetworkService speciesNetworkService;

    public void setSpeciesMapper(SpeciesMapper speciesMapper) {
        this.speciesMapper = speciesMapper;
    }

    public void setEnzymeMapper(EnzymeMapper enzymeMapper) {
        this.enzymeMapper = enzymeMapper;
    }

    public void setDynamicsEnzymeMapper(DynamicsEnzymeMapper dynamicsEnzymeMapper) {
        this.dynamicsEnzymeMapper = dynamicsEnzymeMapper;
    }

    public void setReactionMapper(ReactionMapper reactionMapper) {
        this.reactionMapper = reactionMapper;
    }

    @Override
    public SpeciesTreeNode<SpeciesNode> createSpeciesTree() {
        int num = 0;
        SpeciesTreeNode<SpeciesNode> node = new SpeciesTreeNode<SpeciesNode>();
        node.setParent(null);
        String path = "D:/data/Species_classification.json";
        String s = FileUtils.readJsonFile(path);
        JSONObject jobj = JSON.parseObject(s);
        SpeciesNode speciesNode = new SpeciesNode();
        node.setNodeData(new SpeciesNode(num++, (String) jobj.get("name"), -1));
        setTree(node, (JSONArray) jobj.get("children"), num);
        return node;
    }

    @Override
    public List<String> mapping(String taxId) {
        List<Species> list = speciesMapper.getSpeceisByTaxId(taxId);
        List<String> idList = new ArrayList<>();
        for (Species species : list) {
            idList.add(species.getSpeciesId());
        }
        return idList;
    }

    @Override
    public List<Reaction> getForeignReaction(String path, String speciesId) {
        Path completePath = pathService.createCompletePath(path);
        ArrayList<Reaction> reactionsOfPath = completePath.getReactionsOfPath();
        int[] flag = new int[reactionsOfPath.size()];
        for (int i = 0; i < reactionsOfPath.size(); i++) {
            Reaction reaction = reactionsOfPath.get(i);
            for (Enzyme enzyme : reaction.getEcNumber1()) {
                if (enzyme.getSpecies().contains(speciesId)) {
                    flag[i] = 1;
                    break;
                }
            }
        }
        List<Reaction> list = new ArrayList<>();
        for (int i = 0; i < flag.length; i++) {
            if (flag[i] == 0) list.add(reactionsOfPath.get(i));
        }
        return list;
    }

    public List<SpeciesReaction> allReaction(String path, String speciesId) {
        List<SpeciesReaction> allReaction = new ArrayList<>();
        List<Reaction> foreignReaction = getForeignReaction(path, speciesId);
        List<String> reactions = getByRegular(path, "R\\d{5}");
        for (String reactionId : reactions) {
            Boolean flag = false;
            for (Reaction reaction : foreignReaction) {
                if (reaction.getRId().equals(reactionId)) {
                    allReaction.add(new SpeciesReaction(reaction.getRId(), reaction.getRName(),
                            reaction.getDefinition(), reaction.getEquation(), reaction.getEcNumber(),
                            reaction.getEcNumber1(), reaction.getEnergy(), reaction.getProductId(),
                            reaction.getProductId(), false));
                    flag = true;
                    break;
                }
            }
            if (!flag) {
                Reaction reaction1 = reactionMapper.getReactionById(reactionId);
                allReaction.add(new SpeciesReaction(reaction1.getRId(), reaction1.getRName(),
                        reaction1.getDefinition(), reaction1.getEquation(), reaction1.getEcNumber(),
                        reaction1.getEcNumber1(), reaction1.getEnergy(), reaction1.getProductId(),
                        reaction1.getProductId(), true));
            }
        }
        return allReaction;
    }

    public List<String> getByRegular(String s, String regex) {
        List<String> content = new ArrayList<>();
        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(s);
        while (m.find()) {
            String sub = s.substring(m.start(), m.end());
            content.add(sub);
        }
        return content;
    }

    @Override
    public List<DistanceEnzyme> recom1(String ecNumber, String host) {
        SpeciesTreeNode<SpeciesNode> speciesTree = createSpeciesTree();
        SpeciesTreeNode<SpeciesNode> hostNode = speciesTree.searchSubNodeBySpeciesId(host);
        String allOrganism = enzymeMapper.getAllOrganism(ecNumber);
        String[] organisms = allOrganism.split(";");
        Map<String, Double> map = new HashMap<>();
        List<DistanceEnzyme> list = new ArrayList<>();
        for (String organism : organisms) {
            int distance = speciesTree.calculateClassificationDistance(hostNode,
                    speciesTree.searchSubNodeBySpeciesId(organism));
            if (distance == 99999)
                continue;
            map.put(organism, (double) distance);
            list.add(new DistanceEnzyme(ecNumber, organism, null, (double) distance, 0.0, new ArrayList<>()));
        }
        return list;
    }

    @Override
    public Map<String, List<DistanceEnzyme>> enzymeAboutDIS(String path, String host) {
        List<Reaction> foreignReaction = getForeignReaction(path, host);
        Map<String, List<DistanceEnzyme>> map = new HashMap<>();
        for (Reaction reaction : foreignReaction) {
            List<String> ecNumbers = reaction.getEcNumber();
            List<DistanceEnzyme> allList = new ArrayList<>();
            for (String ecNumber : ecNumbers) {
                List<DistanceEnzyme> distanceEnzymeList = recom1(ecNumber, host);
                allList.addAll(distanceEnzymeList);

            }
            allList.sort(new Comparator<DistanceEnzyme>() {
                @Override
                public int compare(DistanceEnzyme o1, DistanceEnzyme o2) {
                    return Double.compare(o1.getDistance(), o2.getDistance());
                }
            });
            map.put(reaction.getRId(), allList);
        }
        return map;
    }

    @Override
    public List<DistanceEnzyme> enzymeAboutDISOneReaction(String reactionId, String host) {
        Reaction reaction = reactionMapper.getReactionById(reactionId);
        List<String> ecNumbers = reaction.getEcNumber();
        List<DistanceEnzyme> allList = new ArrayList<>();
        for (String ecNumber : ecNumbers) {
            List<DistanceEnzyme> distanceEnzymeList = recom1(ecNumber, host);
            allList.addAll(distanceEnzymeList);

        }
        allList.sort(new Comparator<DistanceEnzyme>() {
            @Override
            public int compare(DistanceEnzyme o1, DistanceEnzyme o2) {
                return Double.compare(o1.getDistance(), o2.getDistance());
            }
        });

        List<DistanceEnzyme> allList1 = new ArrayList<>();
        if (allList.size() <= 60)
            return allList;
        else {
            double d = allList.get(49).getDistance();
            allList1.addAll(allList.stream().limit(50).collect(Collectors.toList()));
            for (int i = 50; i < allList.size(); i++) {
                if (allList.get(i).getDistance() == d) {
                    allList1.add(allList.get(i));
                } else
                    break;
            }
        }
        allList1.removeIf(distanceEnzyme -> distanceEnzyme.getDistance() == 99999);
        return allList1;
    }

    @Override
    public Species getSpeciesById(String id) {
        return speciesMapper.getSpeciesById(id);
    }


    @Override
    public List<EnzymeResult> calculateBothOneReaction(String reactionId, String host, double disWeight, double KMWeight) {
        SpeciesTreeNode<SpeciesNode> speciesTree = createSpeciesTree();
        SpeciesTreeNode<SpeciesNode> hostNode = speciesTree.searchSubNodeBySpeciesId(host);
        List<DynamicsEnzyme> dynamicsEnzymeList = enzymeAboutKmOneReaction(reactionId);
        this.dataStandard1(dynamicsEnzymeList);
        List<DistanceEnzyme> distanceEnzymesList = enzymeAboutDISOneReaction(reactionId, host);
        this.dataStandard2(distanceEnzymesList);
        double disWeight1 = 0.5;
        double KMWeight1 = 0.5;
        if (disWeight != 0 || KMWeight != 0) {
            disWeight1 = disWeight;
            KMWeight1 = KMWeight;
        }
        Map<String, List<EnzymeResult>> map = new HashMap<>();
        List<String[]> appearedIdList = new ArrayList<>();
        List<EnzymeResult> resultList = new ArrayList<>();
        for (DynamicsEnzyme enzyme : dynamicsEnzymeList) {
            List<String> idList = mapping(enzyme.getSourceOrganismTaxId());
            if (idList.size() > 0) {
                boolean flag = false;
                for (DistanceEnzyme distanceEnzyme : distanceEnzymesList) {
                    String id = distanceEnzyme.getSourceOrganismId();
                    if (enzyme.getEcNumber().equals(distanceEnzyme.getEcNumber()) && idList.contains(distanceEnzyme.getSourceOrganismId())) {
                        appearedIdList.add(new String[]{enzyme.getEcNumber(), id});
                        double score = 1 - KMWeight1 * enzyme.getKmStandardValue() + 1 - disWeight1 * distanceEnzyme.getDistanceStandardValue();
                        EnzymeResult object = new EnzymeResult(enzyme.getEcNumber(), id, null, enzyme.getSourceOrganismTaxId(), null, distanceEnzyme.getDistanceStandardValue(), enzyme.getKmStandardValue(), score, new ArrayList<>());
                        resultList.add(object);
                        flag = true;
                        break;
                    }
                }
                if (!flag) {
                    double score = 1 - KMWeight1 * enzyme.getKmStandardValue();
                    EnzymeResult object = new EnzymeResult(enzyme.getEcNumber(), null, null, enzyme.getSourceOrganismTaxId(), null, 0, enzyme.getKmStandardValue(), score, new ArrayList<>());
                    resultList.add(object);
                }
            } else {
                double score = 1 - KMWeight1 * enzyme.getKmStandardValue();
                EnzymeResult object = new EnzymeResult(enzyme.getEcNumber(), null, null, enzyme.getSourceOrganismTaxId(), null, 0, enzyme.getKmStandardValue(), score, new ArrayList<>());
                resultList.add(object);
            }

        }
        map.put(reactionId, resultList);
        for (String key : map.keySet()) {
            List<EnzymeResult> enzymeResults = map.get(key);
            for (DistanceEnzyme distanceEnzyme : distanceEnzymesList) {
                if (!judge1(appearedIdList, distanceEnzyme.getEcNumber(), distanceEnzyme.getSourceOrganismId())) {
                    double score = 1 - disWeight1 * distanceEnzyme.getDistanceStandardValue();
                    EnzymeResult object = new EnzymeResult(distanceEnzyme.getEcNumber(), distanceEnzyme.getSourceOrganismId(), null, null, null, distanceEnzyme.getDistanceStandardValue(), 0, score, new ArrayList<>());
                    enzymeResults.add(object);
                }

            }
        }
        for (String key : map.keySet()) {
            map.get(key).sort(new Comparator<EnzymeResult>() {
                @Override
                public int compare(EnzymeResult o1, EnzymeResult o2) {
                    return Double.compare(o2.getScore(), o1.getScore());
                }
            });
        }

        Map<String, List<EnzymeResult>> newMap = new HashMap<>();
        newMap = map;

        List<EnzymeResult> resultList1 = newMap.get(reactionId);
        for (EnzymeResult enzymeResult : resultList1) {
            DecimalFormat df = new DecimalFormat("#.0000");
            String numStr = df.format(enzymeResult.getScore());
            enzymeResult.setScore(Double.parseDouble(numStr));
        }
        return newMap.get(reactionId);
    }

    public boolean judge1(List<String[]> list, String ecNumber, String speciesId) {
        for (String[] strings : list) {
            if (ecNumber.equals(strings[0]) && speciesId.equals(strings[1]))
                return true;
        }
        return false;
    }

    @Override
    public void toNamesAboutDistanceEnzyme(List<DistanceEnzyme> resultList) {
        for (DistanceEnzyme enzyme : resultList) {
            String name = speciesMapper.getOrganismNameBySpeciesId(enzyme.getSourceOrganismId());
            enzyme.setSourceOrganismName(name);
        }
    }

    @Override
    public void toNamesAboutEnzymeResult(List<EnzymeResult> resultList) {
        for (EnzymeResult enzymeResult : resultList) {
            if (enzymeResult.getSourceOrganismId() != null) {
                String name = speciesMapper.getOrganismNameBySpeciesId(enzymeResult.getSourceOrganismId());
                enzymeResult.setSourceOrganismName(name);
            } else {
                String name = speciesMapper.getNameByTaxId(enzymeResult.getSourceOrganismTaxId());
                enzymeResult.setSourceOrganismTaxName(name);
            }
        }
    }

    @Override
    public List<Url> recomSpecificInfoOfEnzyme(String ecNumber, String organism) {
        if ("".equals(organism) || organism == null)
            return new ArrayList<>();
        String baseSrc = "d:/data/kegg_enzyme/ec%s/%s.txt";
        FileReader fr = null;
        String[] genes = null;
        try {
            fr = new FileReader(String.format(baseSrc, ecNumber.charAt(0), ecNumber));
            BufferedReader br = new BufferedReader(fr);
            String line = null;
            while ((line = br.readLine()) != null) {
                if (line.contains(organism.toUpperCase() + ":"))
                    break;
            }
            if (line == null)
                return new ArrayList<>();
            genes = line.split(":")[1].trim().split(" ");
        } catch (IOException e) {
            e.printStackTrace();
        }
        List<Url> urls = new ArrayList<>();
        for (String gene : genes) {
            String name = organism + ":" + gene;
            String webSite = "https://www.genome.jp/entry/" + name.split("\\(")[0];
            urls.add(new Url(name, webSite));
        }
        return urls;
    }

    @Override
    public void addUrlsAboutDistanceEnzyme(List<DistanceEnzyme> list) {
        for (DistanceEnzyme enzyme : list) {
            List<Url> urls = recomSpecificInfoOfEnzyme(enzyme.getEcNumber(), enzyme.getSourceOrganismId());
            enzyme.setUrls(urls);
        }
    }

    public void addUrlsAboutDynamicsEnzyme(List<DynamicsEnzyme> list) {
        for (DynamicsEnzyme enzyme : list) {
            List<Url> urls = new ArrayList<>();
            String taxId = enzyme.getSourceOrganismTaxId();
            List<Url> urls1 = replenishUrls(enzyme.getEcNumber(), taxId);
            enzyme.setUrls(urls1);
        }
    }

    @Override
    public void addUrlsAboutEnzymeResult(List<EnzymeResult> collect) {
        for (EnzymeResult enzymeResult : collect) {
            if (enzymeResult.getSourceOrganismId() != null) {
                List<Url> urls = recomSpecificInfoOfEnzyme(enzymeResult.getEcNumber(), enzymeResult.getSourceOrganismId());
                if (urls.size() == 0 && enzymeResult.getSourceOrganismTaxId() != null) {
                    List<Url> urlList = replenishUrls(enzymeResult.getEcNumber(), enzymeResult.getSourceOrganismTaxId());
                    enzymeResult.setUrls(urlList);
                } else {
                    enzymeResult.setUrls(urls);
                }
            } else {
                if (enzymeResult.getSourceOrganismTaxId() != null) {
                    List<Url> urlList = replenishUrls(enzymeResult.getEcNumber(), enzymeResult.getSourceOrganismTaxId());
                    enzymeResult.setUrls(urlList);
                }
            }
        }
    }

    public List<Url> replenishUrls(String ecNumber, String taxId) {
        List<Url> urlList = new ArrayList<>();
        List<String> entryIdList = enzymeMapper.getEntryId(ecNumber, taxId);
        String base_url = "https://www.uniprot.org/uniprotkb/%s/entry";
        for (String s : entryIdList) {
            urlList.add(new Url(s, String.format(base_url, s)));
        }
        return urlList;
}

    private void setTree(SpeciesTreeNode<SpeciesNode> node, JSONArray array, int num) {
        if (array == null)
            return;
        for (int i = 0; i < array.size(); i++) {
            JSONObject key1 = (JSONObject) array.get(i);
            String name = (String) key1.get("name");
            SpeciesNode speciesNode = new SpeciesNode(num++, name, node.getNodeData().getNodeId());
            SpeciesTreeNode<SpeciesNode> speciesTreeNode = new SpeciesTreeNode<>(speciesNode, node, new ArrayList<SpeciesTreeNode<SpeciesNode>>());
            node.getChildren().add(speciesTreeNode);
            setTree(speciesTreeNode, (JSONArray) key1.get("children"), num);
        }
    }

    public void func2(List<Reaction> foreignReactionList) {
        Map<String, List<DynamicsEnzyme>> rMap = new HashMap<>();
        for (Reaction reaction : foreignReactionList) {
            List<DynamicsEnzyme> list1 = new ArrayList<>();
            List<String> ecNumberList = reaction.getEcNumber();
            for (String ecNumber : ecNumberList) {
                List<DynamicsEnzyme> dynamicsEnzymeList = dynamicsEnzymeMapper.getALlDynamicsEnzymeByEcNumber(ecNumber);
                list1.addAll(dynamicsEnzymeList);
            }
            rMap.put(reaction.getRId(), list1);
        }

        for (String key : rMap.keySet()) {
            rMap.get(key).sort(new Comparator<DynamicsEnzyme>() {
                @Override
                public int compare(DynamicsEnzyme o1, DynamicsEnzyme o2) {
                    return Double.compare(o2.getKm(), o1.getKm());
                }
            });
        }
    }

    public Map<String, List<DynamicsEnzyme>> enzymeAboutKm(List<Reaction> foreignReactionList) {
        Map<String, List<DynamicsEnzyme>> rMap = new HashMap<>();
        for (Reaction reaction : foreignReactionList) {
            List<DynamicsEnzyme> list1 = new ArrayList<>();
            List<String> ecNumberList = reaction.getEcNumber();
            for (String ecNumber : ecNumberList) {
                List<DynamicsEnzyme> dynamicsEnzymeList = dynamicsEnzymeMapper.getALlDynamicsEnzymeByEcNumber(ecNumber);
                list1.addAll(dynamicsEnzymeList);
            }
            rMap.put(reaction.getRId(), list1);
        }

        for (String key : rMap.keySet()) {
            rMap.get(key).sort(new Comparator<DynamicsEnzyme>() {
                @Override
                public int compare(DynamicsEnzyme o1, DynamicsEnzyme o2) {
                    return Double.compare(o2.getKm(), o1.getKm());
                }
            });
        }
        return rMap;
    }

    @Override
    public List<DynamicsEnzyme> enzymeAboutKmOneReaction(String reactionId) {
        Reaction reaction = reactionMapper.getReactionById(reactionId);
        List<DynamicsEnzyme> list1 = new ArrayList<>();
        List<String> ecNumberList = reaction.getEcNumber();
        for (String ecNumber : ecNumberList) {
            List<DynamicsEnzyme> dynamicsEnzymeList = dynamicsEnzymeMapper.getALlDynamicsEnzymeByEcNumber(ecNumber);
            list1.addAll(dynamicsEnzymeList);
        }
        list1.sort(new Comparator<DynamicsEnzyme>() {
            @Override
            public int compare(DynamicsEnzyme o1, DynamicsEnzyme o2) {
                return Double.compare(o1.getKm(), o2.getKm());
            }
        });
        return list1;
    }

    public void dataStandard1(Map<String, List<DynamicsEnzyme>> map) {
        if (map.size() == 0)
            return;
        double max = 0;
        double min = 0;
        for (String key : map.keySet()) {
            List<DynamicsEnzyme> l1 = map.get(key);
            max = l1.get(0).getKm();
            min = l1.get(0).getKm();
            break;
        }
        for (String key : map.keySet()) {
            List<DynamicsEnzyme> l1 = map.get(key);
            for (DynamicsEnzyme dynamicsEnzyme : l1) {
                double curKm = dynamicsEnzyme.getKm();
                if (curKm > max)
                    max = curKm;
                if (curKm < min)
                    min = curKm;

            }
        }
        DecimalFormat df = new DecimalFormat("0.0000");
        double newValue;
        for (String key : map.keySet()) {
            List<DynamicsEnzyme> list = map.get(key);
            for (DynamicsEnzyme dynamicsEnzyme : list) {
                newValue = (dynamicsEnzyme.getKm() - min) / (max - min);
                if (Double.isNaN(newValue))
                    dynamicsEnzyme.setKmStandardValue(0.0);
                else
                    dynamicsEnzyme.setKmStandardValue(Double.parseDouble(df.format(newValue)));
            }
        }
    }

    public void dataStandard1(List<DynamicsEnzyme> list) {
        if (list.size() == 0)
            return;
        double max = 0;
        double min = 0;
        max = list.get(0).getKm();
        min = list.get(0).getKm();

        for (DynamicsEnzyme dynamicsEnzyme : list) {
            double curKm = dynamicsEnzyme.getKm();
            if (curKm > max)
                max = curKm;
            if (curKm < min)
                min = curKm;

        }

        DecimalFormat df = new DecimalFormat("0.0000");
        double newValue;
        for (DynamicsEnzyme dynamicsEnzyme : list) {
            newValue = (dynamicsEnzyme.getKm() - min) / (max - min);
            if (Double.isNaN(newValue))
                dynamicsEnzyme.setKmStandardValue(0.0);
            else
                dynamicsEnzyme.setKmStandardValue(Double.parseDouble(df.format(newValue)));
        }
    }

    public void dataStandard2(Map<String, List<DistanceEnzyme>> map) {
        if (map.size() == 0)
            return;
        double max = 0;
        double min = 0;
        for (String key : map.keySet()) {
            List<DistanceEnzyme> l1 = map.get(key);
            max = l1.get(0).getDistance();
            min = l1.get(0).getDistance();
            break;
        }
        for (String key : map.keySet()) {
            List<DistanceEnzyme> l1 = map.get(key);
            for (DistanceEnzyme distanceEnzyme : l1) {
                double curKm = distanceEnzyme.getDistance();
                if (curKm > max)
                    max = curKm;
                if (curKm < min)
                    min = curKm;

            }
        }
        DecimalFormat df = new DecimalFormat("0.0000");
        double newValue;
        for (String key : map.keySet()) {
            List<DistanceEnzyme> list = map.get(key);
            for (DistanceEnzyme distanceEnzyme : list) {
                newValue = (distanceEnzyme.getDistance() - min) / (max - min);
                if (Double.isNaN(newValue))
                    distanceEnzyme.setDistanceStandardValue(0.0);
                else
                    distanceEnzyme.setDistanceStandardValue(Double.parseDouble(df.format(newValue)));
            }
        }
    }

    public void dataStandard2(List<DistanceEnzyme> list) {
        if (list.size() == 0)
            return;
        double max = 0;
        double min = 0;
        max = list.get(0).getDistance();
        min = list.get(0).getDistance();

        for (DistanceEnzyme distanceEnzyme : list) {
            double curKm = distanceEnzyme.getDistance();
            if (curKm > max)
                max = curKm;
            if (curKm < min)
                min = curKm;

        }
        if (max == min) {
            for (DistanceEnzyme distanceEnzyme : list) {
                distanceEnzyme.setDistanceStandardValue(1);
            }
            return;
        }
        DecimalFormat df = new DecimalFormat("0.0000");
        double newValue;
        for (DistanceEnzyme distanceEnzyme : list) {
            newValue = (distanceEnzyme.getDistance() - min) / (max - min);
            if (Double.isNaN(newValue))
                distanceEnzyme.setDistanceStandardValue(0.0);
            else
                distanceEnzyme.setDistanceStandardValue(Double.parseDouble(df.format(newValue)));
        }
    }

    public void getCompetingReactions(String FReactionId, int direction, String speciesId) {
        SpeciesNetwork1 speciesNetwork = null;
        try {
            speciesNetwork = speciesNetworkService.getArrayFromTxt(speciesId);
        } catch (IOException e) {
            e.printStackTrace();
        }
        Reaction reaction = reactionMapper.getReactionById(FReactionId);
        List<String> substrateId = new ArrayList<>();
        if (direction == -1) {
            substrateId = reaction.getSubstrateId();
        }
        if (direction == 1) {
            substrateId = reaction.getProductId();
        }
        List<Reaction> rns = new ArrayList<>();
        for (String compoundId : substrateId) {
            List<Reaction> rn = mreService.getRN(speciesNetwork, compoundId);
            rns.addAll(rn);
        }
        List<String> cIdList = speciesNetwork.getCIdList();
    }

    public Map<String, Double> dataStandard(Map<String, Double> map) {
        if (map.size() == 0)
            return new HashMap<>();
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
            newMap.put(key, Double.valueOf(df.format(newValue)));
        }
        return newMap;
    }
}
