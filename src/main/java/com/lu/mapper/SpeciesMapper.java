package com.lu.mapper;

import com.lu.pojo.Species;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface SpeciesMapper {

    Species getSpeciesById(@Param("id") String id);

    List<Species> getSpeceisByTaxId(@Param("taxId") String taxId);

    List<String> getAllSpeciesId();

    List<Species> getAllSpecies();

    String getOrganismNameBySpeciesId(@Param("id") String id);

    String getNameByTaxId(@Param("taxId") String taxId);

    List<String> getSpeciesIdByTaxId(@Param("taxId") String taxId);
}
