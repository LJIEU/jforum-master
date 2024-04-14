package com.liu.system.test;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.json.JSONUtil;
import com.liu.core.config.redis.RedisCache;
import com.liu.core.converter.LevelConverter;
import com.liu.core.utils.LevelUtils;
import com.liu.system.converter.level.MenuLevelConverter;
import com.liu.system.dao.SysMenu;
import com.liu.system.dao.SysUser;
import com.liu.system.service.SysMenuService;
import com.liu.system.service.SysUserService;
import com.liu.system.vo.level.MenuLevel;
import jakarta.annotation.Resource;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * Description:
 *
 * @author 杰
 * @version 1.0
 * @since 2024/04/08 20:16
 */
@SpringBootTest
public class T {

    @Resource
    private SysMenuService menuService;

    @Resource
    private SysUserService userService;


    @Autowired
    private RedisCache redisCache;


    @Test
    public void t() {
        List<SysMenu> menus = menuService.selectSysMenuList(null);
        LevelConverter<SysMenu, MenuLevel> converter = new MenuLevelConverter();
        List<MenuLevel> menuLevels = LevelUtils.buildTree(menus, converter, 0L);
        System.out.println(JSONUtil.toJsonStr(menuLevels));
    }

    // 故意设置 2个 导致线程一直阻塞
    CountDownLatch countDownLatch = new CountDownLatch(2);

    @SneakyThrows
    @Test
    public void logic() {
        new Thread(() -> {
            SysUser sysUser = new SysUser();
            sysUser.setUserId(1L);
            sysUser.setDeptId(1L);
            sysUser.setUserName("Test");
            sysUser.setNickName("A");
            sysUser.setUserType("00");
//        LocalDateTime expired = LocalDateTime.now().plusSeconds(TimeUnit.SECONDS.toSeconds(10L));
            redisCache.setCacheObject("test", sysUser, 5, TimeUnit.SECONDS);
            redisCache.setCacheObjectLogicExpired("test", sysUser, 5L, TimeUnit.SECONDS);
            countDownLatch.countDown();
            try {
                TimeUnit.SECONDS.sleep(5);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("线程休眠完成~");
/*            SysUser user = redisCache.getCacheObjectBySecurity("test", 1L, SysUser.class,
                    t -> userService.selectSysUserByUserId(t), 5L, TimeUnit.SECONDS);
            System.out.println(Thread.currentThread().getName() + ":" + JSONUtil.toJsonStr(user));*/
            // 抢占 并更新
            for (int i = 0; i < 100; i++) {
                new Thread(() -> {
                    try {
                        TimeUnit.MILLISECONDS.sleep(500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    SysUser user = redisCache.getCacheObjectBySecurity("test", 1L, SysUser.class,
                            t -> userService.selectSysUserByUserId(t), 5L, TimeUnit.SECONDS);
                    System.out.println(Thread.currentThread().getName() + ":" + JSONUtil.toJsonStr(user));
                }).start();
            }
        }).start();
        for (int i = 0; i < 100; i++) {
            new Thread(() -> {
                try {
                    TimeUnit.MILLISECONDS.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                SysUser user = redisCache.getCacheObjectBySecurity("test", 1L, SysUser.class,
                        t -> userService.selectSysUserByUserId(t), 5L, TimeUnit.SECONDS);
                System.out.println(Thread.currentThread().getName() + ":" + JSONUtil.toJsonStr(user));
            }).start();
        }

        countDownLatch.await();
    }

    @Autowired
    private RedisTemplate redisTemplate;

    @Test
    public void uniqueId() {
//        Long time = 1712719076L;
        for (int i = 0; i < 100; i++) {
            new Thread(() -> {
/*
                // 当前时间戳
                Long nowTime = LocalDateTime.now().toEpochSecond(ZoneOffset.UTC);
//                System.out.println(nowTime - time);
                long startTime = nowTime - time;
                // 获取Redis中的ID
                String key = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy:MM:dd"));
                Long count = redisTemplate.opsForValue().increment(key + ":" + "myId");
                System.out.println(Thread.currentThread().getName() + ":" + (startTime << 32 | count));
*/
                Long myId = redisCache.uniqueId("myId");
                System.out.println(Thread.currentThread().getName() +
                        ":" + myId + "\t" + Long.toBinaryString(myId));
            }).start();
        }
        try {
            countDownLatch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


    public enum Operation {
        ADD,
        MODIFY,
        DELETE,
        ADD_AND_MODIFY,
        MODIFY_AND_DELETE
    }

    //    @Test
    public static void main(String[] args) /*menuUpdate()*/ {
        List<Long> currentUserMenus = List.of(1L, 2L, 3L, 4L);
        List<Long> requiredMenus = List.of(1L, 2L, 5L);
        List<Long> dbRoleMenus = new ArrayList<>();

        // 1.从 赋权者 中 筛选出 可添加的权限 以赋权者为准 就是两个集合的交集
        requiredMenus = CollUtil.intersection(currentUserMenus, requiredMenus).stream().toList();
        System.out.println(requiredMenus);
        if (requiredMenus == null || CollUtil.isEmpty(requiredMenus)) {
            System.out.println("赋值为空!请检查是否拥有权限~~");
            return;
        }

        // 情况一  db角色列表为空 ==> 直接添加
//        if (CollUtil.isEmpty(dbRoleMenus)) {
//            System.out.println("直接添加数据:" + requiredMenus);
//        }

        // 情况二  db角色列表有值[2,3,4]   [1,2] ==> [1,2] ==》 修改
        dbRoleMenus = List.of(1L, 4L, 3L);
//        ToolUtils.multiple(dbRoleMenus, requiredMenus, null);
/*        int minLength = Math.min(dbRoleMenus.size(), requiredMenus.size());

        for (int i = 0; i < minLength; i++) {
            if (!dbRoleMenus.get(i).equals(requiredMenus.get(i))) {
                System.out.println("修改:" + dbRoleMenus.get(i) + "--->" + requiredMenus.get(i));
            }
        }

        // 处理多余的元素
        for (int i = minLength; i < dbRoleMenus.size(); i++) {
            System.out.println("删除:" + dbRoleMenus.get(i));
        }

        // 处理缺失的元素
        for (int i = minLength; i < requiredMenus.size(); i++) {
            System.out.println("添加:" + requiredMenus.get(i));
        }*/
/*
        // 如果源数据 <= 需要赋值的数据  添加+修改
        if (dbRoleMenus.size() <= requiredMenus.size()) {
            for (int i = 0; i < requiredMenus.size(); i++) {
                if (i > dbRoleMenus.size() - 1) {
                    System.out.println("添加:" + requiredMenus.get(i));
                } else if (!dbRoleMenus.get(i).equals(requiredMenus.get(i))) {
                    System.out.println("修改:" + dbRoleMenus.get(i) + "--->" + requiredMenus.get(i));
                }
            } // 源数据 > 需要赋值的数据  删除+修改
        } else {
            for (int i = 0; i < dbRoleMenus.size(); i++) {
                if (i > requiredMenus.size() - 1) {
                    System.out.println("删除:" + dbRoleMenus.get(i));
                } else if (!dbRoleMenus.get(i).equals(requiredMenus.get(i))) {
                    System.out.println("修改:" + dbRoleMenus.get(i) + "--->" + requiredMenus.get(i));
                }
            }
        }
*/
    }
}
