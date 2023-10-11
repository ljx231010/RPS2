package com.lu.typeHandler;

import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.TypeHandler;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SLTypeHandler implements TypeHandler<List<String>> {

    @Override
    public void setParameter(PreparedStatement preparedStatement, int i, List<String> strings, JdbcType jdbcType) throws SQLException {
        StringBuffer sb = new StringBuffer();
        for (String s : strings) {
            sb.append(s).append(";");
        }
        sb.deleteCharAt(-1);
        preparedStatement.setString(i, sb.toString());
    }

    @Override
    public List<String> getResult(ResultSet resultSet, String s) throws SQLException {
        if (resultSet.getString(s) == null)
            return new ArrayList<>();
        String[] list = resultSet.getString(s).split(";");
        return Arrays.asList(list);
    }

    @Override
    public List<String> getResult(ResultSet resultSet, int i) throws SQLException {
        if (resultSet.getString(i) == null)
            return new ArrayList<>();
        String[] list = resultSet.getString(i).split(";");
        return Arrays.asList(list);
    }

    @Override
    public List<String> getResult(CallableStatement callableStatement, int i) throws SQLException {
        String[] list = callableStatement.getString(i).split(";");
        return Arrays.asList(list);
    }
}
