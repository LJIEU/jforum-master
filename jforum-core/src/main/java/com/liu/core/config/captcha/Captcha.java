package com.liu.core.config.captcha;

/**
 * @author 杰
 * @version 1.0
 * @since 2023/06/25 20:52
 */
public class Captcha {
    /**
     * 随机字符串
     **/
    private String nonceStr;
    /**
     * 验证值
     **/
    private String value;
    /**
     * 生成的画布的base64
     **/
    private String canvasSrc;
    /**
     * 画布宽度
     **/
    private Integer canvasWidth;
    /**
     * 画布高度
     **/
    private Integer canvasHeight;
    /**
     * 生成的阻塞块的base64
     **/
    private String blockSrc;
    /**
     * 阻塞块宽度
     **/
    private Integer blockWidth;
    /**
     * 阻塞块高度
     **/
    private Integer blockHeight;
    /**
     * 阻塞块凸凹半径
     **/
    private Integer blockRadius;
    /**
     * 阻塞块的横轴坐标
     **/
    private Integer blockX;
    /**
     * 阻塞块的纵轴坐标
     **/
    private Integer blockY;
    /**
     * 图片获取位置
     **/
    private Integer place;


    public String getNonceStr() {
        return nonceStr;
    }

    public void setNonceStr(String nonceStr) {
        this.nonceStr = nonceStr;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getCanvasSrc() {
        return canvasSrc;
    }

    public void setCanvasSrc(String canvasSrc) {
        this.canvasSrc = canvasSrc;
    }

    public Integer getCanvasWidth() {
        return canvasWidth;
    }

    public void setCanvasWidth(Integer canvasWidth) {
        this.canvasWidth = canvasWidth;
    }

    public Integer getCanvasHeight() {
        return canvasHeight;
    }

    public void setCanvasHeight(Integer canvasHeight) {
        this.canvasHeight = canvasHeight;
    }

    public String getBlockSrc() {
        return blockSrc;
    }

    public void setBlockSrc(String blockSrc) {
        this.blockSrc = blockSrc;
    }

    public Integer getBlockWidth() {
        return blockWidth;
    }

    public void setBlockWidth(Integer blockWidth) {
        this.blockWidth = blockWidth;
    }

    public Integer getBlockHeight() {
        return blockHeight;
    }

    public void setBlockHeight(Integer blockHeight) {
        this.blockHeight = blockHeight;
    }

    public Integer getBlockRadius() {
        return blockRadius;
    }

    public void setBlockRadius(Integer blockRadius) {
        this.blockRadius = blockRadius;
    }

    public Integer getBlockX() {
        return blockX;
    }

    public void setBlockX(Integer blockX) {
        this.blockX = blockX;
    }

    public Integer getBlockY() {
        return blockY;
    }

    public void setBlockY(Integer blockY) {
        this.blockY = blockY;
    }

    public Integer getPlace() {
        return place;
    }

    public void setPlace(Integer place) {
        this.place = place;
    }
}
