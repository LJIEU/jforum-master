package com.liu.system.controller;

import com.liu.core.controller.BaseController;
import com.liu.core.result.R;
import com.liu.core.utils.ExcelUtil;
import com.liu.core.utils.SecurityUtils;
import com.liu.core.utils.SpringUtils;
import com.liu.system.dao.SysDictData;
import com.liu.system.dao.SysDictType;
import com.liu.system.service.SysDictDataService;
import com.liu.system.service.SysDictTypeService;
import com.liu.system.vo.level.Level;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 字典类型控制层 sys_dict_type
 *
 * @author JIE
 * @since 2024-04-11
 */
@Tag(name = "字典类型模块")
@RestController
@RequestMapping("/sys/dict-type")
public class SysDictTypeController extends BaseController {

    @Autowired
    private SysDictTypeService sysdicttypeService;

    /**
     * 查询 字典类型 列表
     *
     * @param pageNum     当前页码
     * @param pageSize    页记录数
     * @param sortRules   排序规则
     * @param isDesc      是否逆序
     * @param sysdicttype 字典类型对象
     * @return 返回 分页 查询结果
     */
    @Operation(summary = "分页查询")
    @Parameters({
            @Parameter(name = "pageNum", description = "当前页", example = "1"),
            @Parameter(name = "pageSize", description = "页大小", example = "10"),
            @Parameter(name = "sortRules", description = "排序规则"),
            @Parameter(name = "isDesc", description = "是否逆序排序"),
            @Parameter(name = "sysdicttype", description = "实体参数")
    })
    @GetMapping("/list")
    public R<List<SysDictType>> list(
            @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
            @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize,
            @RequestParam(value = "sortRules", defaultValue = "dict_id") String sortRules,
            @RequestParam(value = "isDesc", defaultValue = "false") Boolean isDesc,
            SysDictType sysdicttype) {
        startPage(pageNum, pageSize, sortRules, isDesc);
        // 获取到数据 进行整理[当前页码,页记录数,总页数,查询总条数,数据]
        List<SysDictType> list = sysdicttypeService.selectSysDictTypeList(sysdicttype);
        clearPage();
        return R.success(list);
    }


    /**
     * 导出数据 Excel格式
     */
    @Operation(summary = "导出数据 Excel格式")
    @GetMapping("/export")
    public void export(HttpServletResponse response, SysDictType sysdicttype) {
        // 忽略字段
        Set<String> excludeColumnFiledNames = new HashSet<>();
        excludeColumnFiledNames.add("isDelete");
        List<SysDictType> list = sysdicttypeService.selectSysDictTypeList(sysdicttype);
        ExcelUtil<SysDictType> util = new ExcelUtil<>(SysDictType.class);
        util.exportExcel(response, list, "字典类型数据", excludeColumnFiledNames);
    }


    /**
     * 获取 字典类型 详细信息
     */
    @Operation(summary = "根据ID获取详细信息")
    @GetMapping("/{dictId}")
    public R<SysDictType> getInfo(
            @Parameter(name = "dictId", description = "ID", in = ParameterIn.PATH)
            @PathVariable("dictId") Long dictId) {
        return R.success(sysdicttypeService.selectSysDictTypeByDictId(dictId));
    }

    /**
     * 字典 下拉选项  根据 DictType
     */
    @Operation(summary = "根据 DictType 获取 字典下拉列表")
    @GetMapping("/{dictType}/options")
    public R<List<Level>> options(
            @Parameter(name = "dictType", description = "字典类型", in = ParameterIn.PATH)
            @PathVariable("dictType") String type) {
        Level level = new Level();
        level.setChildren(new ArrayList<>());
        // 1. 获取 字典类型 ===>  用户性别:sys_user_sex
        List<SysDictType> dictTypeList = sysdicttypeService.selectSysDictTypeList(null);
        SysDictDataService dataService = SpringUtils.getBean(SysDictDataService.class);
        SysDictData sysDictData = new SysDictData();
        List<Level> result = dictTypeList.stream().filter(sysDictType -> sysDictType.getDictType().equals(type)).map(dictType -> {
            level.setValue(dictType.getDictId());
            level.setLabel(dictType.getDictName());
            // 2.根据 dict_type 获取下一层
            sysDictData.setDictType(type);
            List<SysDictData> dictDataList = dataService.selectSysDictDataList(sysDictData);
            // 3.整理这些子列表
            level.getChildren().addAll(dictDataList.stream().map(v -> {
                Level l = new Level();
                l.setLabel(v.getDictLabel());
                l.setValue(v.getDictCode());
                return l;
            }).collect(Collectors.toList()));
            return level;
        }).collect(Collectors.toList());
        return R.success(result);
    }


    /**
     * 新增 字典类型
     */
    @Operation(summary = "新增")
    @PostMapping("/add")
    public R<Integer> add(@RequestBody SysDictType sysdicttype, HttpServletRequest request) {
        sysdicttype.setCreateBy(SecurityUtils.getCurrentUser(request));
        return R.success(sysdicttypeService.insert(sysdicttype));
    }


    /**
     * 修改 字典类型
     */
    @Operation(summary = "修改")
    @PutMapping("/update")
    public R<Integer> update(@RequestBody SysDictType params, HttpServletRequest request) {
        params.setUpdateBy(SecurityUtils.getCurrentUser(request));
        SysDictType sysDictType = sysdicttypeService.selectSysDictTypeByDictId(params.getDictId());
        if (StringUtils.isNotBlank(sysDictType.getDictType()) &&
                sysDictType.getDictType().equals(params.getDictType())) {
            // 如果 改变了 dict_type 那 字典数据的 dict_type 也要更改
            SysDictDataService dictDataService = SpringUtils.getBean(SysDictDataService.class);
            dictDataService.updateAllDictType(sysDictType.getDictType(), params.getDictType());
        }
        // 进行修改
        sysdicttypeService.update(params);
        return R.success();
    }


    /**
     * 删除 字典类型
     * /delete/1,2,3
     */
    @Operation(summary = "删除")
    @DeleteMapping("/delete/{dictIds}")
    public R<Integer> delete(@PathVariable("dictIds") Long[] dictIds) {
        return R.success(sysdicttypeService.delete(dictIds));
    }


}
