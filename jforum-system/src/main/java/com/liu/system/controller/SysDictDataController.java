package com.liu.system.controller;

import com.liu.core.controller.BaseController;
import com.liu.core.result.R;
import com.liu.core.utils.ExcelUtil;
import com.liu.system.dao.SysDictData;
import com.liu.system.service.SysDictDataService;
import com.liu.system.vo.DictDataVo;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 字典数据控制层 sys_dict_data
 *
 * @author JIE
 * @since 2024-04-11
 */
@Tag(name = "字典数据模块")
@RestController
@RequestMapping("/sys/dict-data")
public class SysDictDataController extends BaseController {

    @Autowired
    private SysDictDataService sysdictdataService;

    /**
     * 查询 字典数据 列表
     *
     * @param pageNum     当前页码
     * @param pageSize    页记录数
     * @param sortRules   排序规则
     * @param isDesc      是否逆序
     * @param sysdictdata 字典数据对象
     * @return 返回 分页 查询结果
     */
    @Operation(summary = "分页查询")
    @Parameters({
            @Parameter(name = "pageNum", description = "当前页", example = "1"),
            @Parameter(name = "pageSize", description = "页大小", example = "10"),
            @Parameter(name = "sortRules", description = "排序规则"),
            @Parameter(name = "isDesc", description = "是否逆序排序"),
            @Parameter(name = "sysdictdata", description = "实体参数")
    })
    @GetMapping("/list")
    public R<List<SysDictData>> list(
            @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
            @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize,
            @RequestParam(value = "sortRules", defaultValue = "dict_code") String sortRules,
            @RequestParam(value = "isDesc", defaultValue = "false") Boolean isDesc,
            SysDictData sysdictdata) {
        startPage(pageNum, pageSize, sortRules, isDesc);
        // 获取到数据 进行整理[当前页码,页记录数,总页数,查询总条数,数据]
        List<SysDictData> list = sysdictdataService.selectSysDictDataList(sysdictdata);
        clearPage();
        return R.success(list);
    }


    /**
     * 导出数据 Excel格式
     */
    @Operation(summary = "导出数据 Excel格式")
    @GetMapping("/export")
    public void export(HttpServletResponse response, SysDictData sysdictdata) {
        // 忽略字段
        Set<String> excludeColumnFiledNames = new HashSet<>();
        List<SysDictData> list = sysdictdataService.selectSysDictDataList(sysdictdata);
        ExcelUtil<SysDictData> util = new ExcelUtil<>(SysDictData.class);
        util.exportExcel(response, list, "字典数据数据", excludeColumnFiledNames);
    }


    /**
     * 获取 字典数据 详细信息
     */
    @Operation(summary = "根据ID获取详细信息")
    @GetMapping("/{dictCode}")
    public R<SysDictData> getInfo(
            @Parameter(name = "dictCode", description = "ID", in = ParameterIn.PATH)
            @PathVariable("dictCode") Long dictCode) {
        return R.success(sysdictdataService.selectSysDictDataByDictCode(dictCode));
    }

    /**
     * 根据 dictType 获取 字典列表
     */
    @Operation(summary = "根据 dictType 获取字典数据列表")
    @GetMapping("/selectDict/{dictType}")
    public R<List<DictDataVo>> selectDict(
            @Parameter(name = "dictType", description = "字典类型", in = ParameterIn.PATH)
            @PathVariable("dictType") String dictType) {
        SysDictData dictData = new SysDictData();
        dictData.setDictType(dictType);
        List<SysDictData> dictDataList = sysdictdataService.selectSysDictDataList(dictData);
        List<DictDataVo> dataVoList = dictDataList.stream().map(this::dictToDV).collect(Collectors.toList());
        return R.success(dataVoList);
    }


    /**
     * 新增 字典数据
     */
    @Operation(summary = "新增")
    @PostMapping("/add")
    public R<Integer> add(@RequestBody SysDictData sysdictdata) {
        return R.success(sysdictdataService.insert(sysdictdata));
    }


    /**
     * 修改 字典数据
     */
    @Operation(summary = "修改")
    @PutMapping("/update")
    public R<Integer> update(@RequestBody SysDictData sysdictdata) {
        return R.success(sysdictdataService.update(sysdictdata));
    }


    /**
     * 删除 字典数据
     * /delete/1,2,3
     */
    @Operation(summary = "删除")
    @DeleteMapping("/delete/{dictCodes}")
    public R<Integer> delete(@PathVariable("dictCodes") Long[] dictCodes) {
        return R.success(sysdictdataService.delete(dictCodes));
    }


    private DictDataVo dictToDV(SysDictData dictData) {
        DictDataVo dictVo = new DictDataVo();
        dictVo.setId(dictData.getDictCode());
        dictVo.setName(dictData.getDictLabel());
        dictVo.setSort(dictData.getSort());
        dictVo.setStatus(Integer.valueOf(dictData.getStatus()));
        dictVo.setTypeCode(dictData.getDictType());
        dictVo.setValue(dictData.getDictValue());
        dictVo.setCss(dictData.getCssClass());
        dictVo.setTheme(dictData.getListClass());
        dictVo.setRemark(dictData.getRemark());
        return dictVo;
    }

}
