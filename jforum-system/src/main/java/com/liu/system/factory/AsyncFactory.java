package com.liu.system.factory;

import com.liu.core.constant.Constants;
import com.liu.core.utils.AddressUtils;
import com.liu.core.utils.IpUtils;
import com.liu.core.utils.ServletUtils;
import com.liu.core.utils.SpringUtils;
import com.liu.db.entity.SysLoginLog;
import com.liu.db.entity.SysOperateLog;
import com.liu.db.service.SysLoginLogService;
import com.liu.db.service.SysOperateLogService;
import eu.bitwalker.useragentutils.UserAgent;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.TimerTask;

/**
 * Description: 异步工厂执行方法
 *
 * @author 杰
 * @version 1.0
 * @since 2024/03/30 11:22
 */
public class AsyncFactory {
    public static final Logger log = LoggerFactory.getLogger(AsyncFactory.class);


    /**
     * 操作日志写入
     */
    public static TimerTask recordOperate(SysOperateLog operateLog) {
        return new TimerTask() {
            @Override
            public void run() {
                // 远程查询操作地点
                operateLog.setOperateLocation(AddressUtils.getRealAddressByIp(operateLog.getIp()));
                SpringUtils.getBean(SysOperateLogService.class).insert(operateLog);
            }
        };
    }

    /**
     * 登录日志写入
     */
    public static TimerTask recordLoginLog(final String username, final String status, final String message, Object... args) {
        final UserAgent userAgent = UserAgent.parseUserAgentString(ServletUtils.getRequest().getHeader("User-Agent"));
        final String ip = IpUtils.getIpAddress();
        return new TimerTask() {
            @Override
            public void run() {
                String address = AddressUtils.getRealAddressByIp(ip);
                String s = new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
                        .append(ip).append(address).append(username).append(status).append(message).toString();
                // 打印信息到日志
                log.info(s.toString(), args);
                // 获取客户端操作系统
                String os = userAgent.getOperatingSystem().getName();
                // 获取客户端浏览器
                String browser = userAgent.getBrowser().getName();
                // 封装对象
                SysLoginLog loginLog = new SysLoginLog();
                loginLog.setUserName(username);
                loginLog.setIp(ip);
                loginLog.setLoginLocation(address);
                loginLog.setBrowser(browser);
                loginLog.setOs(os);
                loginLog.setMessage(message);
                // 日志状态
                if (StringUtils.equalsAny(status, Constants.LOGIN_SUCCESS, Constants.LOGOUT, Constants.REGISTER)) {
                    loginLog.setStatus(Constants.SUCCESS);
                } else if (Constants.LOGIN_FAIL.equals(status)) {
                    loginLog.setStatus(Constants.FAIL);
                }
                // 插入数据
                SpringUtils.getBean(SysLoginLogService.class).insert(loginLog);
            }
        };
    }

}
