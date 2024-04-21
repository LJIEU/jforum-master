package com.liu.db.converter;

import com.liu.core.converter.TreeConverter;
import com.liu.db.entity.TestTwo;

import java.util.List;

/**
 * Description: 适配器
 *
 * @author 杰
 * @version 1.0
 * @since 2024/04/02 13:17
 */
public class TestTwoConverter implements TreeConverter<TestTwo> {
    /**
     * 获取子节点
     *
     * @param data 原始数据
     * @return
     */
    @Override
    public List<TestTwo> getChildren(TestTwo data) {
        return data.getChildren();
    }

    /**
     * 设置值  将整理好的 children 添加到 原始数据中
     *
     * @param data     原始数据
     * @param children 子节点
     */
    @Override
    public void setChildren(TestTwo data, List<TestTwo> children) {
        data.setChildren(children);
    }

    @Override
    public Long getPid(TestTwo data) {
        return data.getPid();
    }

    @Override
    public Long getId(TestTwo data) {
        return data.getTwoId();
    }
}
