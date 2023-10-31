package com.lu.service;

import com.lu.pojo.Path;
import com.lu.pojo.Reaction;

import java.util.Map;

public interface PathService {

    Path createCompletePath(String path);

    Map<String, Double> r1(Path path);


    void reactionAddEnzymeObject(Reaction reaction);

    Map<String, String> getCompoundIdToName(String path);

    Map<String, String> getReactionIdToName(String path);

    Reaction getReactionById(String id);
}
