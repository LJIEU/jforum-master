package com.liu.db.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.liu.core.config.redis.RedisCache;
import com.liu.core.constant.PostState;
import com.liu.core.excption.ServiceException;
import com.liu.core.utils.AddressUtils;
import com.liu.core.utils.IpUtils;
import com.liu.db.entity.Category;
import com.liu.db.entity.Post;
import com.liu.db.entity.SensitiveRecord;
import com.liu.db.entity.SensitiveWord;
import com.liu.db.mapper.PostMapper;
import com.liu.db.nodb.document.TagDocument;
import com.liu.db.service.CategoryService;
import com.liu.db.service.PostService;
import com.liu.db.service.SensitiveRecordService;
import com.liu.db.service.SensitiveWordService;
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
import java.util.*;

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
    private SensitiveWordService sensitiveWordService;

    @Autowired
    private SensitiveRecordService recordService;

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
    public void createPost(PostParam param, HttpServletRequest request, Long userId) {
        this.savePost(param, request, userId);
    }

    @Override
    public void updateViews(Post post) {
        post.setViews((post.getViews() == null ? 0L : post.getViews()) + 1);
        postMapper.update(post);
    }

    @Override
    public List<Post> pendingPostList(String state) {
        return postMapper.pendingPostList(state);
    }

    @Override
    public void update(PostParam param, HttpServletRequest request, Long userId) {
        this.savePost(param, request, userId);
    }


    @Transactional(rollbackFor = Exception.class)
    protected void savePost(PostParam param, HttpServletRequest request, Long userId) {
        Post post = new Post();
        // 状态: 1已发布  2待审核 3:审核中  0隐藏  5驳回
        post.setState(PostState.POST_PRIVACY);
        if (param.getPostId() != null) {
            // 说明是修改  去判断是否是我的
            Post db = postMapper.selectPostByPostId(param.getPostId());
            if (db == null) {
                throw new ServiceException("帖子不存在");
            }
            if (!db.getUserId().equals(userId)) {
                throw new ServiceException("你没有权限修改");
            }
            // 设置ID  ！！！！
            post.setPostId(param.getPostId());
            // 查询当前状态
            /*只有两种情况下修改状态  驳回|公布  */
            //noinspection EnhancedSwitchMigration
            switch (db.getState()) {
                // 修改为待审核
                case PostState.POST_PUBLISH:
                    post.setState(PostState.POST_PENDING);
                    break;
                // 修改为审核中
                case PostState.POST_REJECT:
                    post.setState(PostState.POST_REVIEWING);
                    break;
            }
        }
        if (StrUtil.isEmpty(param.getTitle())) {
            throw new ServiceException("标题不能为空");
        }
        String title = param.getTitle();
        // 初始化一下 将数据库的屏蔽词加入列表中
        Map<String, String> titleMap = this.filterContent(title);
        post.setTitle(titleMap.get("resultContent"));

        // 内容解码
        String content = null;
        content = URLDecoder.decode(param.getContent(), StandardCharsets.UTF_8);
        if (content == null) {
            throw new ServiceException("内容为空");
        }

        Map<String, String> contentMap = this.filterContent(content);
        post.setContent(contentMap.get("resultContent"));
        // 最后一个才是类别
//        postDocument.setCategoryId(param.getCategoryId().get(param.getCategoryId().size() - 1));
//        postDocument.setCategoryId(param.getCategoryId());

        // 获取 生成ID
        if (param.getPostId() == null) {
            Long postId = redisCache.uniqueId("app:postId");
            post.setPostId(postId.toString());
        }
        // 设置类别
        Category category = categoryService.selectCategoryByCategoryId(param.getCategoryId());
        if (category == null) {
            throw new ServiceException("类别不存在");
        }
        if (param.getPostId() == null) {
            postAndCategoryService.insert(param.getCategoryId(), post.getPostId());
        } else {
            postAndCategoryService.update(param.getCategoryId(), post.getPostId());
        }
//        // 匹配图片
//        if (CollUtil.isNotEmpty(param.getImage())) {
//            postDocument.setImage(param.getImage());
//        } else {
//            postDocument.setImage(null);
//        }

        // 对剩下的属性初始化
        if (param.getPostId() == null) {
            post.setUserId(userId);
            post.setType(0);
            post.setViews(0L);
            post.setIpAddress(AddressUtils.getRealAddressByIp(IpUtils.getIpAddress(request)));
            post.setSort(0);
        }

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
        }
        if (CollUtil.isNotEmpty(tagIdList)) {
            if (param.getPostId() == null) {
                for (String tag : tagIdList) {
                    postAndTagService.insert(tag, post.getPostId());
                }
            } else {
                // 进行 标签更新
                List<String> dbTags = postAndTagService.selectTagsByPostId(post.getPostId());
                if (CollUtil.isNotEmpty(dbTags)) {
                    // 删除
                    postAndTagService.delete(post.getPostId());
                    // 添加
                    for (String tag : tagIdList) {
                        postAndTagService.insert(tag, post.getPostId());
                    }
                } else {
                    // 添加
                    for (String tag : tagIdList) {
                        postAndTagService.insert(tag, post.getPostId());
                    }
                }
            }
        }
        if (param.getPostId() == null) {
            // 添加帖子
            postMapper.insert(post);
        } else {
            postMapper.update(post);
        }


        //  2024/5/12/18:01 添加成功后 由定时任务推送给 后台审核  PostTask.java
        // 添加过滤内容记录 [标题、内容]
        // 类型: 0:标题  1:帖子内容  2:评论
        insertSensitiveRecord(userId, post.getPostId(), titleMap, (byte) 0);
        insertSensitiveRecord(userId, post.getPostId(), contentMap, (byte) 1);
    }


    /**
     * 添加屏蔽词记录
     */
    private void insertSensitiveRecord(Long userId, String postId, Map<String, String> map, Byte
            type) {
        if (StrUtil.isNotEmpty(map.get("filteredContent"))) {
            // 说明有拦截记录
            SensitiveRecord record = new SensitiveRecord();
            // 类型: 0:标题  1:帖子内容  2:评论
            record.setType(Integer.valueOf(type));
            record.setFilteredContent(map.get("filteredContent"));
            record.setUserId(userId);
            record.setContentId(postId);
            recordService.insert(record);
        }
    }

    /**
     * 过滤
     */
    private Map<String, String> filterContent(String content) {
        if (content == null) {
            throw new ServiceException("过滤内容为空");
        }
        StringBuilder filteredContent = new StringBuilder(content);
        StringBuilder temp = new StringBuilder();
        Set<String> sensitiveWords = this.loadSensitiveWords();
        // 遍历敏感词集合，将内容中的敏感词替换为 *
        for (String word : sensitiveWords) {
            if (filteredContent.indexOf(word) != -1) {
                temp.append(word);
                String replacement = generateReplacement(word);
                filteredContent = new StringBuilder(
                        filteredContent.toString()
                                .replace(word, replacement));
            }
        }
        HashMap<String, String> map = new HashMap<>();
        map.put("resultContent", filteredContent.toString());
        map.put("filteredContent", temp.toString());
        return map;
    }


    private Set<String> loadSensitiveWords() {
        // 从数据库或文件中加载敏感词库
        HashSet<String> words = new HashSet<>();
        List<SensitiveWord> sensitiveWords = sensitiveWordService.selectSensitiveWordList(null);
        for (SensitiveWord sensitiveWord : sensitiveWords) {
            words.add(sensitiveWord.getWord());
        }
        return words;
    }

    private static String generateReplacement(String word) {
        // 替换 相同长度的 *
        return "*".repeat(word.length());
    }


}
