//package com.liu.db.converter;
//
//import com.liu.core.converter.TreeConverter;
//import com.liu.db.entity.Category;
//
//import java.util.List;
//
///**
// * Description:
// *
// * @author 杰
// * @version 1.0
// * @since 2024/04/23 9:24
// */
//public class CategoryConverter implements TreeConverter<Category> {
//    @Override
//    public List<Category> getChildren(Category data) {
//        return data.getChildren();
//    }
//
//    @Override
//    public void setChildren(Category data, List<Category> children) {
//        data.setChildren(children);
//    }
//
//    @Override
//    public Long getPid(Category data) {
//        return data.getPid();
//    }
//
//    @Override
//    public Long getId(Category data) {
//        return data.getCategoryId();
//    }
//}
