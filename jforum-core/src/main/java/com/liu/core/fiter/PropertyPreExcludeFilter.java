package com.liu.core.fiter;

import com.alibaba.fastjson2.filter.SimplePropertyPreFilter;

/**
 * Description: 排除JSON敏感属性
 *
 * @author 杰
 * @version 1.0
 * @since 2024/03/30 13:15
 */
public class PropertyPreExcludeFilter extends SimplePropertyPreFilter {
    public PropertyPreExcludeFilter() {
    }

    public PropertyPreExcludeFilter addExcludes(String... filters) {
        for (String filter : filters) {
            // 进行排除
            this.getExcludes().add(filter);
        }
        return this;
    }
}
