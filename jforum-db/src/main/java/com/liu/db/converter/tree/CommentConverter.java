package com.liu.db.converter.tree;

import com.liu.core.converter.TreeConverter;
import com.liu.db.vo.CommentVo;

import java.util.List;

/**
 * Description:
 *
 * @author 杰
 * @version 1.0
 * @since 2024/05/11 18:27
 */
public class CommentConverter implements TreeConverter<CommentVo> {

    @Override
    public List<CommentVo> getChildren(CommentVo data) {
        return data.getChildren();
    }

    @Override
    public void setChildren(CommentVo data, List<CommentVo> children) {
        data.setChildren(children);
    }

    @Override
    public Long getPid(CommentVo data) {
        return data.getCommentPid();
    }

    @Override
    public Long getId(CommentVo data) {
        return data.getCommentId();
    }
}