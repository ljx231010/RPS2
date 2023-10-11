package com.lu.mapper;

import com.lu.pojo.Enzyme;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface EnzymeMapper {

    Enzyme getEnzymeByEcNumber(@Param("ecNumber") String ecNumber);

    List<Enzyme> getEnzymesBySpeciesId(@Param("speciesId") String speciesId);

    String getAllOrganism(@Param("ecNumber") String ecNUmber);

    List<String> getEntryId(@Param("ecNumber") String ecNumber, @Param("taxId") String taxId);
}
