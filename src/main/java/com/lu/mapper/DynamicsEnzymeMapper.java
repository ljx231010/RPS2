package com.lu.mapper;

import com.lu.pojo.DynamicsEnzyme;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface DynamicsEnzymeMapper {
    public List<DynamicsEnzyme> getALlDynamicsEnzymeByEcNumber(@Param("ecNumber") String ecNumber);
}
