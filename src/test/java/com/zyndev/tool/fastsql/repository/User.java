/*
 * Copyright (c) 2017. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package com.zyndev.tool.fastsql.repository;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * @author 张瑀楠 zyndev@gmail.com
 * @version 1.0
 * @date 2017-12-27 15:04:13
 */
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "tb_user")
public class User implements Serializable {

  private static final long serialVersionUID = 1L;

  /**
   *
   */
  @Id
  @Column
  private Integer id;

  /**
   *
   */
  @Column
  private String uid;

  /**
   * 登录用户名
   */
  @Column(name = "account_name")
  private String accountName;

  /**
   * 最多10个汉字
   */
  @Column(name = "nick_name")
  private String nickName;

  /**
   *
   */
  @Column
  private String password;

  /**
   *
   */
  @Column
  private String phone;

  /**
   *
   */
  @Column(name = "register_time")
  private Date registerTime;

  /**
   *
   */
  @Column(name = "update_time")
  private Date updateTime;

}
