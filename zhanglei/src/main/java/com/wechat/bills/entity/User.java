package com.wechat.bills.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import net.sf.oval.constraint.Length;
import net.sf.oval.constraint.NotNull;

import java.util.Date;

public class User {
    @ApiModelProperty(value = "id，可以不填",required = false)
    private Integer id;

    @ApiModelProperty(value = "微信账号",required = true)
    @NotNull(message = "wechatId不能为空")
    @Length(min = 1)
    private String wechatId;

    @ApiModelProperty(value = "导出key",required = false)
    @NotNull(message = "exportKey不能为空")
    @Length(min = 1)
    private String exportKey;

    @ApiModelProperty(value = "加密key",required = false)
    private String userrollEncryption;

    @ApiModelProperty(value = "凭证key",required = false)
    private String userrollPassTicket;

    @ApiModelProperty(value = "查询余额key",required = false)
    @NotNull(message = "balanceuserrollEncryption不能为空")
    @Length(min = 1)
    private String balanceuserrollEncryption;

    @ApiModelProperty(value = "秘钥过期时间",required = false)
    // timezone = "GMT+8"
    @JsonFormat( pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT")
    private Date expire;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getWechatId() {
        return wechatId;
    }

    public void setWechatId(String wechatId) {
        this.wechatId = wechatId == null ? null : wechatId.trim();
    }

    public String getExportKey() {
        return exportKey;
    }

    public void setExportKey(String exportKey) {
        this.exportKey = exportKey == null ? null : exportKey.trim();
    }

    public String getUserrollEncryption() {
        return userrollEncryption;
    }

    public void setUserrollEncryption(String userrollEncryption) {
        this.userrollEncryption = userrollEncryption == null ? null : userrollEncryption.trim();
    }

    public String getUserrollPassTicket() {
        return userrollPassTicket;
    }

    public void setUserrollPassTicket(String userrollPassTicket) {
        this.userrollPassTicket = userrollPassTicket == null ? null : userrollPassTicket.trim();
    }

    public String getBalanceuserrollEncryption() {
        return balanceuserrollEncryption;
    }

    public void setBalanceuserrollEncryption(String balanceuserrollEncryption) {
        this.balanceuserrollEncryption = balanceuserrollEncryption == null ? null : balanceuserrollEncryption.trim();
    }

    public Date getExpire() {
        return expire;
    }

    public void setExpire(Date expire) {
        this.expire = expire;
    }
}