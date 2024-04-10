package com.liu.system.test;

import cn.hutool.json.JSONUtil;
import com.liu.core.config.redis.RedisCache;
import com.liu.core.converter.LevelConverter;
import com.liu.core.utils.LevelUtils;
import com.liu.system.converter.MenuLevelConverter;
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
}
