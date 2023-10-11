package com.lu.mapper;

import com.lu.pojo.Path;
import com.lu.pojo.PathSpecies;

public interface PathMapper {
    public Path createCompletePath(String path);

    PathSpecies getPathSpeciesByPath(String path);
}
