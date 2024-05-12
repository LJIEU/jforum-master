package com.liu.db.service.nodb.document.impl;

import com.liu.db.nodb.document.TagDocument;
import com.liu.db.service.nodb.document.TagService;
import com.liu.db.service.nodb.document.repository.TagRepository;
import jakarta.annotation.Resource;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * Description:
 *
 * @author 杰
 * @version 1.0
 * @since 2024/05/12 11:09
 */
@Service
public class TagDocumentServiceImpl implements TagService {

    @Resource
    private TagRepository tagDocumentRepository;

    @Resource
    private MongoOperations mongoOperations;

    @Override
    public List<TagDocument> list() {
        return tagDocumentRepository.find();
/*        Query query = new Query();
        Criteria criteria = new Criteria();
        Criteria.where("isDelete").is(false);
        query.addCriteria(criteria);
        return mongoOperations.find(query, TagDocument.class, "tag");*/
    }

    @Override
    public TagDocument inertTag(String name) {
        TagDocument tag = new TagDocument();
        tag.setName(name);
        tag.setUpdateTime(new Date());
        tag.setDelete(false);
        return tagDocumentRepository.save(tag);
    }

    @Override
    public TagDocument getItem(String tagId) {

        Criteria criteria = new Criteria();
        if (tagId.length() == 24) {
            criteria.and("_id").is(new ObjectId(tagId));
        } else {
            criteria.and("_id").is(tagId);
        }
        criteria.and("isDelete").is(false);
        Query query = new Query(criteria);

        return mongoOperations.findOne(query, TagDocument.class);
    }
}
