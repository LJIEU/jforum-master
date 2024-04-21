package com.liu.system.controller;

import cn.hutool.core.util.StrUtil;
import com.github.pagehelper.PageInfo;
import com.liu.core.controller.BaseController;
import com.liu.core.result.R;
import com.liu.core.utils.ExcelUtil;
import com.liu.core.utils.SecurityUtils;
import com.liu.core.utils.SpringUtils;
import com.liu.db.entity.SysDictData;
import com.liu.db.entity.SysDictType;
import com.liu.db.service.SysDictDataService;
import com.liu.db.service.SysDictTypeService;
import com.liu.db.vo.DictTypeVo;
import com.liu.db.vo.level.Level;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.*;
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
            @Parameter(name = "keywords", description = "关键词[字典类型、字典名称]"),
            @Parameter(name = "sysdicttype", description = "实体参数")
    })
    @GetMapping("/list")
    public R<Map<String, Object>> list(
            @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
            @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize,
            @RequestParam(value = "sortRules", defaultValue = "dict_id") String sortRules,
            @RequestParam(value = "isDesc", defaultValue = "false") Boolean isDesc,
            @RequestParam(value = "keywords", required = false) String keywords,
            SysDictType sysdicttype) {
        startPage(pageNum, pageSize, sortRules, isDesc);
        // 获取到数据 进行整理[当前页码,页记录数,总页数,查询总条数,数据]
        SysDictType dictType = new SysDictType();
        if (StrUtil.isNotEmpty(keywords)) {
            dictType.setDictName(keywords);
            dictType.setDictType(keywords);
        }
        List<SysDictType> list = sysdicttypeService.selectSysDictTypeList(dictType);
        List<DictTypeVo> voList = list.stream().map(this::dictTypeToVo).collect(Collectors.toList());
        PageInfo<SysDictType> pageInfo = new PageInfo<>();
        HashMap<String, Object> map = new HashMap<>(2);
        map.put("list", voList);
        map.put("total", pageInfo.getTotal());
        clearPage();
        return R.success(map);
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
    public R<DictTypeVo> getInfo(
            @Parameter(name = "dictId", description = "ID", in = ParameterIn.PATH)
            @PathVariable("dictId") Long dictId) {
        return R.success(dictTypeToVo(sysdicttypeService.selectSysDictTypeByDictId(dictId)));
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
                l.setValue(Long.valueOf(v.getDictValue()));
                return l;
            }).collect(Collectors.toList()));
            return level;
        }).collect(Collectors.toList());
        return R.success(result);
    }


    /**
     * 新增 字典类型
     */
    @Operation(summary = "新增字典类型")
    @PostMapping("/add")
    public R<Integer> add(@Valid @RequestBody DictTypeVo dictTypeVo, HttpServletRequest request) {
        SysDictType dictType = voToDictType(dictTypeVo);
        dictType.setCreateBy(SecurityUtils.currentUsername(request));
        return R.success(sysdicttypeService.insert(dictType));
    }


    /**
     * 修改 字典类型
     */
    @Operation(summary = "修改字典类型")
    @PutMapping("/update")
    public R<Integer> update(@RequestBody DictTypeVo dictTypeVo, HttpServletRequest request) {
        SysDictType params = voToDictType(dictTypeVo);
        params.setUpdateBy(SecurityUtils.currentUsername(request));
        SysDictType dbDictType = sysdicttypeService.selectSysDictTypeByDictId(params.getDictId());
        if (StringUtils.isNotBlank(dbDictType.getDictType()) &&
                !dbDictType.getDictType().equals(params.getDictType())) {
            // 如果 改变了 dict_type 那 字典数据的 dict_type 也要更改
            SysDictDataService dictDataService = SpringUtils.getBean(SysDictDataService.class);
            dictDataService.updateAllDictType(dbDictType.getDictType(), params.getDictType());
        }
        // 进行修改
        sysdicttypeService.update(params);
        return R.success();
    }


    /**
     * 删除 字典类型
     * /delete/1,2,3
     */
    @Operation(summary = "删除字典类型")
    @DeleteMapping("/delete/{dictIds}")
    public R<Integer> delete(@PathVariable("dictIds") Long[] dictIds) {
        return R.success(sysdicttypeService.delete(dictIds));
    }

    private SysDictType voToDictType(DictTypeVo vo) {
        SysDictType sysDictType = new SysDictType();
        sysDictType.setDictId(vo.getId());
        sysDictType.setDictName(vo.getName());
        sysDictType.setDictType(vo.getCode());
        sysDictType.setStatus(String.valueOf(vo.getStatus()));
        sysDictType.setRemark(vo.getRemark());
        return sysDictType;
    }

    private DictTypeVo dictTypeToVo(SysDictType dictType) {
        DictTypeVo dictTypeVo = new DictTypeVo();
        dictTypeVo.setId(dictType.getDictId());
        dictTypeVo.setName(dictType.getDictName());
        dictTypeVo.setCode(dictType.getDictType());
        dictTypeVo.setStatus(Integer.valueOf(dictType.getStatus()));
        dictTypeVo.setRemark(dictType.getRemark());
        return dictTypeVo;
    }

}
