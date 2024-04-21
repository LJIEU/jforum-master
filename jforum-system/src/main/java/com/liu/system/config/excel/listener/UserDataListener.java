package com.liu.system.config.excel.listener;

import cn.hutool.core.util.ObjUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.metadata.data.ReadCellData;
import com.alibaba.excel.read.listener.ReadListener;
import com.alibaba.excel.util.ListUtils;
import com.liu.core.utils.SecurityUtils;
import com.liu.core.utils.SpringUtils;
import com.liu.core.utils.XssUtils;
import com.liu.db.entity.SysUser;
import com.liu.db.service.SysUserService;
import com.liu.system.config.excel.temple.UserTemple;
import com.liu.system.context.UserDataListenerHolder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Description: 用户上传 数据 监听器
 *
 * @author 杰
 * @version 1.0
 * @since 2024/04/15 14:12
 */
public class UserDataListener implements ReadListener<UserTemple> {
    public static final Logger log = LoggerFactory.getLogger(UserDataListener.class);


    public static final int BATCH_COUNT = 100;
    public static final AtomicInteger SUCCESS = new AtomicInteger(0);
    public static final AtomicInteger FAIL = new AtomicInteger(0);
    private List<UserTemple> cacheDataList = ListUtils.newArrayListWithExpectedSize(BATCH_COUNT);

    /**
     * 读取 头数据
     *
     * @param headMap 头数据
     * @param context 上下文
     */
    @Override
    public void invokeHead(Map<Integer, ReadCellData<?>> headMap, AnalysisContext context) {
        ReadListener.super.invokeHead(headMap, context);
        if (headMap.size() != UserTemple.class.getDeclaredFields().length) {
            throw new RuntimeException("解析Excel出错,请输入正确的模板");
        }
        // TODO 2024/4/15/22:49 正确的做法是这里 去查询是否有没有改动模板 而不是"xxx".equals(xxx)
        // 并且设置数据是从 第3行开始的 前2行是 一个头数据  一个实例数据
    }

    /**
     * 读取数据
     *
     * @param userTemple      数据
     * @param analysisContext 上下文
     */
    @Override
    public void invoke(UserTemple userTemple, AnalysisContext analysisContext) {
        if ("xxx".equals(userTemple.getUserName()) || "xxx".equals(userTemple.getEmail())) {
            return;
        }
        cacheDataList.add(userTemple);
        if (cacheDataList.size() >= BATCH_COUNT) {
            // 1.保存到数据库
            saveData();
            // 2.创建新的缓存列表
            cacheDataList = ListUtils.newArrayListWithExpectedSize(BATCH_COUNT);
        }
    }

    @Override
    public void doAfterAllAnalysed(AnalysisContext context) {
        saveData();
    }

    /**
     * 整理数据并存储到数据库
     */
    private void saveData() {
        // 2024/4/15/23:10 对数据结果进行处理 保存x  失败x
        log.info("{}条数据,开始存储~", cacheDataList.size());
        for (UserTemple userTemple : cacheDataList) {
            if (ObjUtil.isNotEmpty(userTemple)) {
                SysUser user = templeToUser(userTemple);
                try {
                    SpringUtils.getBean(SysUserService.class).insert(user);
                    SUCCESS.incrementAndGet();
                } catch (Exception e) {
                    FAIL.incrementAndGet();
//                    throw new RuntimeException("导入失败");
                }

            }
        }
        log.info("存储完成:{}条", SUCCESS.get());
        String message = "成功:" + SUCCESS.get() + "条\t失败:" + FAIL.get() + "条";
        UserDataListenerHolder.setContext(message);
    }

    private SysUser templeToUser(UserTemple userTemple) {
        SysUser user = new SysUser();
        // ID采用自动生成
        user.setDeptId(userTemple.getDeptId());
        // 2024/4/15/21:14 最好这里 进行 XSS 防止注入
        user.setUserName(strXssAndBlank(userTemple.getUserName()));
        user.setNickName(strXssAndBlank(userTemple.getNickName()));
        user.setUserType(strXssAndBlank(userTemple.getUserType()));
        user.setEmail(strXssAndBlank(userTemple.getEmail()));
        user.setPhone(strXssAndBlank(userTemple.getPhone()));
        user.setSex(strXssAndBlank(userTemple.getSex()));
        user.setAvatar(strXssAndBlank(userTemple.getAvatar()));
        // 默认密码 "123456"
        user.setPassword(SecurityUtils.encryptPassword("123456"));
        user.setStatus(strXssAndBlank(userTemple.getStatus()));
        user.setCreateBy("");
        user.setRemark("");
        return user;
    }

    /**
     * 去除空格 以及 XSS过滤
     *
     * @param str 字符串
     * @return 处理完成后的
     */
    private String strXssAndBlank(String str) {
        String xssClean = XssUtils.xssClean(str, null);
        return StrUtil.cleanBlank(xssClean);
    }
}
