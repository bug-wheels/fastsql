package com.zyndev.tool.fastsql.repository.po;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by zyndev on 2017/7/8. author: 张瑀楠 email : zyndev@gmail.com description: 用户各平台 令牌 todo:
 */
public class UserAuthorizationToken implements Serializable {

  private static final long serialVersionUID = 1L;


  private Long id;
  private String accessToken;
  private String refreshToken;    // 用户刷新access_token 4306d4e0-2b13-4316-9cf2-f437d7a40094
  private Long expiresIn;       // access_token接口调用凭证超时时间，单位（秒）
  private Date expiresTime;
  private Date refreshTokenTimeout;
  private String resourceOwner;
  private String uid;
  private Long aliId;
  private String memberId;
  private String shopName;
  private String shopLogo;
  private String openId;
  private String scope;  // 用户授权的作用域，使用逗号（,）分隔
  private Integer platform;   // 平台： 1 阿里巴巴 2 淘宝 3 京东 4 微店
  private Date createTime;
  private Date updateTime;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getAccessToken() {
    return accessToken;
  }

  public void setAccessToken(String accessToken) {
    this.accessToken = accessToken;
  }

  public String getRefreshToken() {
    return refreshToken;
  }

  public void setRefreshToken(String refreshToken) {
    this.refreshToken = refreshToken;
  }

  public Long getExpiresIn() {
    return expiresIn;
  }

  public void setExpiresIn(Long expiresIn) {
    this.expiresIn = expiresIn;
    Calendar cal = Calendar.getInstance();
    cal.add(Calendar.SECOND, expiresIn.intValue());
    this.expiresTime = cal.getTime();
  }

  public Date getExpiresTime() {
    return expiresTime;
  }

  public Date getRefreshTokenTimeout() {
    return refreshTokenTimeout;
  }

  public void setRefreshTokenTimeout(Date refreshTokenTimeout) {
    this.refreshTokenTimeout = refreshTokenTimeout;
  }

  public String getResourceOwner() {
    return resourceOwner;
  }

  public void setResourceOwner(String resourceOwner) {
    this.resourceOwner = resourceOwner;
  }

  public String getUid() {
    return uid;
  }

  public void setUid(String uid) {
    this.uid = uid;
  }

  public Long getAliId() {
    return aliId;
  }

  public void setAliId(Long aliId) {
    this.aliId = aliId;
  }

  public String getMemberId() {
    return memberId;
  }

  public void setMemberId(String memberId) {
    this.memberId = memberId;
  }

  public String getShopName() {
    return shopName;
  }

  public void setShopName(String shopName) {
    this.shopName = shopName;
  }

  public String getShopLogo() {
    return shopLogo;
  }

  public void setShopLogo(String shopLogo) {
    this.shopLogo = shopLogo;
  }

  public String getOpenId() {
    return openId;
  }

  public void setOpenId(String openId) {
    this.openId = openId;
  }

  public String getScope() {
    return scope;
  }

  public void setScope(String scope) {
    this.scope = scope;
  }

  public Integer getPlatform() {
    return platform;
  }

  public void setPlatform(Integer platform) {
    this.platform = platform;
  }

  public Date getCreateTime() {
    return createTime;
  }

  public void setCreateTime(Date createTime) {
    this.createTime = createTime;
  }

  public Date getUpdateTime() {
    return updateTime;
  }

  public void setUpdateTime(Date updateTime) {
    this.updateTime = updateTime;
  }
}
