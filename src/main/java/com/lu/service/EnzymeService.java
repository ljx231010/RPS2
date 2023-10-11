package com.lu.service;

import com.lu.javabean.Url;
import com.lu.pojo.*;

import java.util.List;
import java.util.Map;

public interface EnzymeService {
    public SpeciesTreeNode<SpeciesNode> createSpeciesTree();

    public List<String> mapping(String taxId);

    public List<Reaction> getForeignReaction(String path, String speciesId);

    public List<DistanceEnzyme> recom1(String ecNumber, String host);

    public Map<String, List<DistanceEnzyme>> enzymeAboutDIS(String path, String host);

    List<DistanceEnzyme> enzymeAboutDISOneReaction(String reactionId, String host);

    List<DynamicsEnzyme> enzymeAboutKmOneReaction(String reactionId);

    public List<EnzymeResult> calculateBothOneReaction(String reactionId, String host, double disWeight, double KMWeight);

    public List<Url> recomSpecificInfoOfEnzyme(String ecNumber, String organism);

    void addUrlsAboutDistanceEnzyme(List<DistanceEnzyme> collect);

    void addUrlsAboutEnzymeResult(List<EnzymeResult> collect);

    void addUrlsAboutDynamicsEnzyme(List<DynamicsEnzyme> list);

    List<SpeciesReaction> allReaction(String path, String speciesId);

    Species getSpeciesById(String id);

    void toNamesAboutDistanceEnzyme(List<DistanceEnzyme> resultList);

    void toNamesAboutEnzymeResult(List<EnzymeResult> resultList);


}
