package com.liu.db.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.liu.core.config.redis.RedisCache;
import com.liu.core.excption.ServiceException;
import com.liu.core.utils.AddressUtils;
import com.liu.core.utils.IpUtils;
import com.liu.db.entity.Category;
import com.liu.db.entity.Post;
import com.liu.db.mapper.PostMapper;
import com.liu.db.nodb.document.TagDocument;
import com.liu.db.service.CategoryService;
import com.liu.db.service.PostService;
import com.liu.db.service.nodb.document.repository.TagRepository;
import com.liu.db.service.relation.PostAndCategoryService;
import com.liu.db.service.relation.PostAndTagService;
import com.liu.db.vo.api.PostParam;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

/**
 * 帖子实际业务层 post
 *
 * @author JIE
 * @since 2024-04-23
 */
@Service
public class PostServiceImpl implements PostService {

    @Autowired
    private PostMapper postMapper;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private PostAndCategoryService postAndCategoryService;

    @Autowired
    private TagRepository tagDocumentRepository;

    @Autowired
    private PostAndTagService postAndTagService;

    @Autowired
    private RedisCache redisCache;

    @Override
    public List<Post> selectPostList(Post post) {
        return postMapper.selectPostList(post);
    }

    @Override
    public Post selectPostByPostId(String postId) {
        return postMapper.selectPostByPostId(postId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int insert(Post post) {
        return postMapper.insert(post);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int update(Post post) {
        return postMapper.update(post);
    }

    @Override
    public int delete(String[] postIds) {
        return postMapper.deleteById(postIds);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void createPost(PostParam param, HttpServletRequest request, Long userId) {
        Post post = new Post();
        if (StrUtil.isEmpty(param.getTitle())) {
            throw new ServiceException("标题不能为空");
        }
        String title = param.getTitle();
        // 初始化一下 将数据库的屏蔽词加入列表中
//        Map<String, String> titleMap = this.filterContent(title);
//        postDocument.setTitle(titleMap.get("resultContent"));
        post.setTitle(title);

        // 内容解码
        String content = null;
        content = URLDecoder.decode(param.getContent(), StandardCharsets.UTF_8);
        if (content == null) {
            throw new ServiceException("内容为空");
        }

//        Map<String, String> contentMap = this.filterContent(content);
//        postDocument.setContent(contentMap.get("resultContent"));
        post.setContent(content);
//        BeanUtil.copyProperties(param, postDocument);
        // 最后一个才是类别
//        postDocument.setCategoryId(param.getCategoryId().get(param.getCategoryId().size() - 1));
//        postDocument.setCategoryId(param.getCategoryId());

        // 获取 生成ID
        Long postId = redisCache.uniqueId("app:postId");
        post.setPostId(postId.toString());

        // 设置类别
        Category category = categoryService.selectCategoryByCategoryId(param.getCategoryId());
        if (category == null) {
            throw new ServiceException("类别不存在");
        }
        postAndCategoryService.insert(param.getCategoryId(), post.getPostId());

//        // 匹配图片
//        if (CollUtil.isNotEmpty(param.getImage())) {
//            postDocument.setImage(param.getImage());
//        } else {
//            postDocument.setImage(null);
//        }

        // 对剩下的属性初始化
        post.setUserId(userId);
        post.setType(0);
        post.setViews(0L);
        post.setIpAddress(AddressUtils.getRealAddressByIp(IpUtils.getIpAddress(request)));
        post.setSort(0);
        // 状态: 1已发布  2待审核  0隐藏
        post.setState("2");

        // 添加标签
        ArrayList<String> tagIdList = new ArrayList<>();
        if (CollUtil.isNotEmpty(param.getTagList())) {
            for (String o : param.getTagList()) {
                TagDocument tag = tagDocumentRepository.findByMultipleConditions(o);
                // 如果该标签存在
                if (tag != null) {
                    tagIdList.add(tag.getId().toString());
                } else {
                    // 否则就是不存在  那就创建一个
                    TagDocument tagDocument = new TagDocument();
                    tagDocument.setName(o);
                    tagDocumentRepository.save(tagDocument);
                    // 然后再关联
                    tagIdList.add(tagDocument.getId().toString());
                }
            }
//            postDocument.setTagIds(tagIdList);
        }
        if (CollUtil.isNotEmpty(tagIdList)) {
            for (String tag : tagIdList) {
                postAndTagService.insert(tag, post.getPostId());
            }
        }
        // 添加帖子
        postMapper.insert(post);


        // TODO 2024/5/12/18:01 添加成功后 由定时任务推送给 后台审核
        // 添加过滤内容记录 [标题、内容]
        // 类型: 0:标题  1:帖子内容  2:评论
//        insertSensitiveRecord(userId, postDocument.getId(), titleMap, (byte) 0);
//        insertSensitiveRecord(userId, postDocument.getId(), contentMap, (byte) 1);

        // 查询
//        Criteria criteria = new Criteria().orOperator(
//                Criteria.where("_id").is(postDocument.getId()),
//                Criteria.where("_id").is(new ObjectId(postDocument.getId().toString()))
//        ).and("isDelete").is(false);
//
//        Query query = new Query(criteria);

//        return mongoOperations.findOne(query, PostDocument.class);
    }

    @Override
    public void updateViews(Post post) {
        post.setViews((post.getViews() == null ? 0L : post.getViews()) + 1);
        postMapper.update(post);
    }
}
