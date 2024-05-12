package com.liu.db.service.nodb.document;

import com.liu.db.nodb.document.TagDocument;

import java.util.List;

/**
 * Description:
 *
 * @author 杰
 * @version 1.0
 * @since 2024/05/12 11:09
 */
public interface TagService {

    List<TagDocument> list();

    TagDocument inertTag(String name);

    TagDocument getItem(String tagId);
}
