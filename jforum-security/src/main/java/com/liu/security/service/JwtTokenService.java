package com.liu.security.service;

import cn.hutool.core.util.StrUtil;
import com.liu.core.config.redis.RedisCache;
import com.liu.core.constant.CacheConstants;
import com.liu.core.constant.Constants;
import com.liu.core.model.LoginUser;
import com.liu.core.utils.AddressUtils;
import com.liu.core.utils.IpUtils;
import eu.bitwalker.useragentutils.UserAgent;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * Description:
 *
 * @author 杰
 * @version 1.0
 * @since 2024/04/02 16:01
 */
@Service
public class JwtTokenService {
    public static final Logger log = LoggerFactory.getLogger(JwtTokenService.class);

    /**
     * 令牌自定义标识
     */
    @Value("${token.header}")
    private String header;

    /**
     * 令牌密钥
     */
    @Value("${token.key}")
    private String key;

    /**
     * 令牌有效期(单位: 分钟)
     */
    @Value("${token.expireTime}")
    private int expireTime;

    @Autowired
    private RedisCache redisCache;


    private final static long MINUTE = 1000 * 60L;
    private final static long TEN_MINUTE_REFRESH = 10 * MINUTE;


    /**
     * 创建令牌
     */
    public String createToken(LoginUser loginUser) {
        String token = UUID.randomUUID().toString();
        loginUser.setToken(token);
        // 设置用户信息
        // 获取 request 请求
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        assert requestAttributes != null;
        HttpServletRequest request = requestAttributes.getRequest();
        UserAgent userAgent = UserAgent.parseUserAgentString(request.getHeader("User-Agent"));
        String ip = IpUtils.getIpAddress(request);
        loginUser.setIp(ip);
        loginUser.setLocation(AddressUtils.getRealAddressByIp(ip));
        loginUser.setBrowser(userAgent.getBrowser().getName());
        loginUser.setOs(userAgent.getOperatingSystem().getName());
        // 刷新或者创建令牌
        refreshToken(loginUser);
        Map<String, Object> claims = new HashMap<String, Object>(1);
        claims.put(Constants.LOGIN_USER_KEY, token);
        claims.put(Constants.LOGIN_USER_NAME, loginUser.getUsername());
        claims.put(Constants.LOGIN_OS, loginUser.getOs());
        claims.put(Constants.LOGIN_CURR_ROLE, loginUser.getCurrRole());
        return Jwts.builder()
                .setClaims(claims)
                .signWith(SignatureAlgorithm.HS512, key).compact();
    }

    /**
     * 刷新令牌有效期
     *
     * @param loginUser 登录信息
     */
    public void refreshToken(LoginUser loginUser) {
        // 刷新令牌有效期
        loginUser.setLoginTime(System.currentTimeMillis());
        loginUser.setExpireTime(loginUser.getLoginTime() + expireTime * MINUTE);
        // 根据UUID将当前登录用户缓存
        String userKey = getTokenKey(loginUser.getUsername(), loginUser.getToken(), loginUser.getOs(), loginUser.getCurrRole());
        // 判断缓存中是否存在值
        String existKey = redisCache.existValue(CacheConstants.LOGIN_TOKEN_KEY + ":" + loginUser.getUsername()
                + ":*");
        // 如果不为null那就将其删除
        if (StringUtils.isNotEmpty(existKey)) {
            redisCache.delObject(existKey);
        }
        // 添加到缓存中
        redisCache.setCacheObject(userKey, loginUser, expireTime, TimeUnit.MINUTES);
    }

    /**
     * 获取用户身份信息
     */
    public LoginUser getLoginUser(HttpServletRequest request) {
        String token = request.getHeader(header);
        if (StrUtil.isEmpty(token)) {
            if (request.getParameter(header) != null) {
                token = request.getParameter(header);
            }
        }
        if (StringUtils.isNotEmpty(token)) {
            try {
                Claims claims = Jwts.parser().setSigningKey(key).parseClaimsJws(token).getBody();
                // 解析对应的权限和用户信息
                String uuid = (String) claims.get(Constants.LOGIN_USER_KEY);
                String username = (String) claims.get(Constants.LOGIN_USER_NAME);
                String os = (String) claims.get(Constants.LOGIN_OS);
                String currRole = (String) claims.get(Constants.LOGIN_CURR_ROLE);
                String userKey = getTokenKey(username, uuid, os, currRole);
                return (LoginUser) redisCache.getCacheObject(userKey);
            } catch (Exception e) {
                log.error("获取用户信息异常 {}.", e.getMessage());
            }
        }
        return null;
    }

    /**
     * 生成Redis中的 key 值 实现单一登录
     *
     * @param username 用户名
     * @param uuid     UUID
     */
    private String getTokenKey(String username, String uuid, String os, String currRole) {
        // 2024/2/19/12:33 后期再识别设备登录 一个系统一个Token...
        return CacheConstants.LOGIN_TOKEN_KEY + ":" + username + ":"
                + os + ":" + uuid + ":" + currRole;
    }

    /**
     * 验证 Token 有效期 不足 10 分钟自动刷新
     */
    public void verifyToken(LoginUser loginUser) {
        Long expireTime = loginUser.getExpireTime();
        long currentTime = System.currentTimeMillis();
        if (expireTime - currentTime <= TEN_MINUTE_REFRESH) {
            refreshToken(loginUser);
        }
    }
}
