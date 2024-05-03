package com.liu.core.converter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.liu.camunda.domin.BpmnInfo;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.TypeHandler;

import java.lang.reflect.Type;
import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/**
 * Description: 类型转换
 *
 * @author 杰
 * @version 1.0
 * @since 2024/05/03 16:46
 */
public class BpmnHandler implements TypeHandler<List<BpmnInfo>> {
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void setParameter(PreparedStatement ps, int i, List<BpmnInfo> parameter, JdbcType jdbcType) throws SQLException {
        try {
            String json = objectMapper.writeValueAsString(parameter);
            ps.setString(i, json);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<BpmnInfo> getResult(ResultSet rs, String columnName) throws SQLException {
        return convertJsonToList(rs.getString(columnName));
    }


    @Override
    public List<BpmnInfo> getResult(ResultSet rs, int columnIndex) throws SQLException {
        return convertJsonToList(rs.getString(columnIndex));
    }

    @Override
    public List<BpmnInfo> getResult(CallableStatement cs, int columnIndex) throws SQLException {
        return convertJsonToList(cs.getString(columnIndex));
    }

    private List<BpmnInfo> convertJsonToList(String json) {
        try {
            return objectMapper.readValue(json, new TypeReference<List<BpmnInfo>>() {
                @Override
                public Type getType() {
                    return super.getType();
                }
            });
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return null;
    }

}
