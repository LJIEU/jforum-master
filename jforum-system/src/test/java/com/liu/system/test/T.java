package com.liu.system.test;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.json.JSONUtil;
import com.alibaba.excel.EasyExcel;
import com.liu.core.config.redis.RedisCache;
import com.liu.core.converter.LevelConverter;
import com.liu.core.utils.LevelUtils;
import com.liu.db.converter.level.MenuLevelConverter;
import com.liu.db.entity.SysMenu;
import com.liu.db.entity.SysUser;
import com.liu.db.service.SysMenuService;
import com.liu.db.service.SysUserService;
import com.liu.db.vo.level.MenuLevel;
import com.liu.system.config.excel.handler.UserWriteHandler;
import com.liu.system.config.excel.listener.UserDataListener;
import com.liu.system.config.excel.temple.UserTemple;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.ArrayList;
import java.util.Date;
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

        try {
            countDownLatch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
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

    @Test
    public /*static void main(String[] args)*/ void menuUpdate() {
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

    @Test
    public void widthAndHeightWrite() {
        String fileName = "./test" + System.currentTimeMillis() + ".xlsx";
        // 这里 需要指定写用哪个class去写，然后写到第一个sheet，名字为模板 然后文件流会自动关闭
//        EasyExcel.write(fileName)
//                .registerWriteHandler(new UserWriteHandler())
//                .registerWriteHandler(UserWriteHandler.style())
//                .head(head())
//                .sheet("模板").doWrite(dataList());
        List<UserTemple> initList = new ArrayList<>();
        UserTemple userTemple = new UserTemple();
        userTemple.setUserId("不需要填写自动生成!!");
        userTemple.setDeptId(-1L);
        userTemple.setUserName("xxx");
        userTemple.setNickName("xxx");
        userTemple.setUserType("temple");
        userTemple.setEmail("xxx");
        userTemple.setPhone("xxx");
        userTemple.setSex("temple");
        userTemple.setAvatar("http://xxx");
        userTemple.setStatus("temple");
        initList.add(userTemple);

        EasyExcel.write(fileName, UserTemple.class)
                .registerWriteHandler(new UserWriteHandler())
                .registerWriteHandler(UserWriteHandler.style())
                .sheet("模板").doWrite(initList);
    }

    private List<List<String>> head() {
        List<List<String>> list = new ArrayList<>();
        List<String> head0 = new ArrayList<>();
        List<String> head1 = new ArrayList<>();
        List<String> head2 = new ArrayList<>();
        List<String> head3 = new ArrayList<>();
        List<String> head4 = new ArrayList<>();
        List<String> head5 = new ArrayList<>();
        List<String> head6 = new ArrayList<>();
        List<String> head7 = new ArrayList<>();
        List<String> head8 = new ArrayList<>();
        List<String> head9 = new ArrayList<>();
        List<String> head10 = new ArrayList<>();
        head0.add("用户ID");
        head1.add("部门");
        head2.add("用户账号");
        head3.add("用户昵称");
        head4.add("用户类型\n(00系统用户)");
        head5.add("用户邮箱");
        head6.add("手机号码");
        head7.add("用户性别");
        head8.add("头像地址");
        head9.add("账号状态");
        head10.add("备注");
        list.add(head0);
        list.add(head1);
        list.add(head2);
        list.add(head3);
        list.add(head4);
        list.add(head5);
        list.add(head6);
        list.add(head7);
        list.add(head8);
        list.add(head9);
        list.add(head10);
        return list;
    }

    private List<List<Object>> dataList() {
        List<List<Object>> list = new ArrayList<>();
        List<Object> data = new ArrayList<>();
        data.add("不需要填写自动生成!");
        data.add("部门 请根据提示填写");
        data.add("xxx");
        data.add("xxx");
        data.add("xxx");
        data.add("xxx@qq.com");
        data.add("xxx");
        data.add("性别 请根据提示填写");
        data.add("http://xxx");
        data.add("状态 请根据提示填写");
        data.add("用户模板样式");
        list.add(data);
        return list;
    }

    private List<Object> dataList2() {
        List<Object> data = new ArrayList<>();
        data.add("不需要填写自动生成!");
        data.add("部门 请根据提示填写");
        data.add("xxx");
        data.add("xxx");
        data.add("xxx");
        data.add("xxx@qq.com");
        data.add("xxx");
        data.add("性别 请根据提示填写");
        data.add("http://xxx");
        data.add("状态 请根据提示填写");
        data.add("用户模板样式");
        return data;
    }


    private List<SysUser> userTemple() {
        List<SysUser> list = new ArrayList<SysUser>();
        SysUser sysUser = new SysUser();
        sysUser.setUserId(-1L);
        sysUser.setDeptId(-1L);
        sysUser.setUserName("xxx");
        sysUser.setNickName("xxx");
        sysUser.setUserType("00");
        sysUser.setEmail("xxxx@qq.com");
        sysUser.setPhone("xxxxx");
        sysUser.setSex("0");
        sysUser.setAvatar("http://xxxx");
        sysUser.setStatus("0");
        sysUser.setLoginIp("xxx.xxx.xxx.xxx");
        sysUser.setCreateBy("xxx");
//        LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
//        sysUser.setCreateTime();
        sysUser.setUserType("xxx");
        sysUser.setUpdateTime(new Date());
        sysUser.setRemark("用户模板样式");
        sysUser.setIsDelete(0);
        list.add(sysUser);
        return list;
    }


    @Test
    public void read() {
        String fileName = "./test1713173549546.xlsx";
        // 读取文件 并设置表头为 2行 排除 实例数据 .headRowNumber(2) 有BUG 设置后导致读取空值
        EasyExcel.read(fileName, UserTemple.class, new UserDataListener())
                .sheet("模板").doRead();
    }
}
