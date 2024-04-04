package com.liu.system.aspect;

import cn.hutool.core.bean.BeanUtil;
import com.liu.core.annotation.Log;
import com.liu.core.aspect.BaseLogAspect;
import com.liu.core.manager.AsyncManager;
import com.liu.core.model.BaseOperateLog;
import com.liu.system.dao.SysOperateLog;
import com.liu.system.factory.AsyncFactory;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;


/**
 * Description: 自定义Log切面
 *
 * @author 杰
 * @version 1.0
 * @since 2024/03/30 13:56
 */
@Slf4j
@Aspect
@Component
public class SysLogAspect extends BaseLogAspect {
    @Override
    public BaseOperateLog handleLog(JoinPoint joinPoint, Log controllerLog, Exception e, Object jsonResult) {
        BaseOperateLog baseOperateLog = super.handleLog(joinPoint, controllerLog, e, jsonResult);
        log.info(baseOperateLog.toString());
        // 进行保存 ===================
        SysOperateLog sysOperateLog = new SysOperateLog();
        // 将 base的数据 拷贝到 sys中
        BeanUtil.copyProperties(baseOperateLog, sysOperateLog);
        AsyncManager.manager().execute(AsyncFactory.recordOperate(sysOperateLog));
        return baseOperateLog;
    }
}
