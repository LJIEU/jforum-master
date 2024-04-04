package com.liu.core.controller;

import com.github.pagehelper.PageHelper;
import com.liu.core.utils.DateUtils;
import com.liu.core.utils.PageUtils;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;

import java.beans.PropertyEditorSupport;
import java.util.Date;

/**
 * Description: 基类Controller
 *
 * @author 杰
 * @version 1.0
 * @since 2024/03/30 9:47
 */
public class BaseController {
    @InitBinder // 这个只对当前 Controller 有效 所以这个放在 BaseController 中 让所有 Controller 继承此类后 都可以拥有该效果
    // webDataBinder是用于表单到方法的数据绑定
    public void initBinder(WebDataBinder binder) {
        // 自定义日期编辑器 简陋版
/*
 binder.registerCustomEditor(Date.class,
         new CustomDateEditor(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
                 , true));
*/
        // 自定义日期编辑器 升级版
        binder.registerCustomEditor(Date.class, new PropertyEditorSupport() {
            @Override
            public void setAsText(String text) {
                setValue(DateUtils.parseDate(text));
            }
        });
    }


    public void startPage(Integer pageNum, Integer pageSize, String sortRules, Boolean isDesc) {
        /*desc 逆序，asc 正序*/
//        Mysql的分页 limit 公式是 limit (当前页 - 1) * 页记录数, 页记录数
//        总记录数 select count(*) from t_goods;
//        总页数公式 (总记录数 + 页记录数 - 1) / 页记录数
//        PageHelper.startPage(1, 2, "goods_id asc").setReasonable(true);
        // setReasonable 是分页合理化 对于不合理的分页参数自动处理 如: pageNum<0 就会默认设置为1
//        PageUtils.startPage();
        PageHelper.startPage(pageNum, pageSize, PageUtils.orderBy(sortRules, isDesc)).setReasonable(true);
    }

    public void clearPage() {
        PageUtils.clearPage();
    }

}

