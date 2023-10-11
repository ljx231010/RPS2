package com.lu.mapper;

import com.lu.pojo.Compound;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface SpeciesNetworkMapper {

    List<List<Integer>> get1(@Param("speciesId") String speciesId);

    List<Compound> createCompoundsList();

    List<String> getCIdList();
}
