package com.lu.mapper;

import com.lu.pojo.Reaction;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ReactionMapper {
    Reaction getReactionById(@Param("id") String id);

    List<Reaction> getReactionsByEcNumber(@Param("ecNumber") String ecNumber);

    List<String> getRIdByEcNumber(@Param("ecNumber") String ecNumber);

    String getEnergyByRId(String rid);

    int getReactionCount();

    List<Reaction> getReactionsByEcNumber1(@Param("ecNumber") String ecNumber);

    String getReactionNameById(@Param("id") String id);
}
