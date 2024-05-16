package com.liu.app.controller;

import com.liu.core.result.R;
import com.liu.db.entity.SysDictData;
import com.liu.db.service.SysDictDataService;
import com.liu.db.vo.level.Level;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Description:
 *
 * @author 杰
 * @version 1.0
 * @since 2024/05/15 20:12
 */
@Tag(name = "字典数据模块")
@RestController
@RequestMapping("/app/dict")
public class DictDataController {

    @Autowired
    private SysDictDataService dictDataService;

    @Operation(summary = "获取选项列表")
    @GetMapping("/{type}")
    public R<Map<String, Object>> getDictList(@PathVariable("type") String type) {
        List<SysDictData> dictDataList = dictDataService.selectSysDictDataByDictType(type);
        List<Level> result = new ArrayList<>(dictDataList.size());
        dictDataList.forEach(dictData -> {
            Level level = new Level();
            level.setLabel(dictData.getDictLabel());
            level.setValue(dictData.getDictValue());
            result.add(level);
        });
        Map<String, Object> map = new HashMap<>(2);
        map.put("type", type);
        map.put("list", result);
        return R.success(map);
    }
}
