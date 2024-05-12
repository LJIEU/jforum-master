package com.liu.db.service.nodb.document.repository;

import com.liu.db.nodb.document.TagDocument;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Description: 直接对MongoDB进行操作 类似于 Mapper的.xml文件
 *
 * @author 杰
 * @version 1.0
 * @since 2024/05/12 11:11
 */
@Repository
public interface TagRepository extends MongoRepository<TagDocument, String> {
    TagDocument findOneByName(String o);

    @Query("{ $or: [{'name':?0},{'_id':?0}]},{ $and:[{'isDelete':false}] }")
    TagDocument findByMultipleConditions(String o);

    @Query("{ $and:[{'isDelete':false}] }")
    List<TagDocument> find();

    @Query("{" +
            "$or: [" +
            "        { _id: ?0 }," +
            "        { _id: ObjectId(?0) }" +
            "    ]," +
            "    isDelete: false" +
            "}")
    TagDocument getItem(String tagId);
}
