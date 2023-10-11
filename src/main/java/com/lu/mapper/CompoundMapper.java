package com.lu.mapper;

import com.lu.pojo.Compound;
import org.apache.ibatis.annotations.Param;

public interface CompoundMapper {
    Compound getCompoundById(@Param("id") String id);

    String getRIdsByCIndex(@Param("cIndex") String cIndex);

    String getCompoundNameById(@Param("id") String id);
}
