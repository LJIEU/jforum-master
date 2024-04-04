package com.liu.core.converter;

import java.util.List;

/**
 * Description: Tree结构适配器
 *
 * @author 杰
 * @version 1.0
 * @since 2024/04/02 12:47
 */
public interface TreeConverter<D> {
    List<D> getChildren(D data);

    void setChildren(D data, List<D> children);

    Long getPid(D data);

    Long getId(D data);
}
