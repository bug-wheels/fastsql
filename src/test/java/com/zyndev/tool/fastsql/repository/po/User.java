package com.zyndev.tool.fastsql.repository.po;

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
 * Created by zyndev on 2017/7/8. author: 张瑀楠 email : zyndev@gmail.com  zyndev@163.com description: 用户表 todo:
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

  @Id
  @Column
  private Long id;            // 用户id
  private String uuid;        // 用户 UUID, 在这里做处理，不要将用户的 id 传到前台
  private String sellerId;    // 淘宝提供的 sellerId
  private String userName;    // 用户名称
  private String password;    // 用户密码
  private Integer platform;   // 对应平台
  private Integer active;     // 是否可用
  private String companyName; // 用户公司名称
  private Integer free;       // 付费用户
  private Integer vip;        // vip 用户
  private Date paymentDate;   // 付款时间
  private Date lastLoginTime; // 最后登录时间
  private Date currentLoginTime;// 当前登录时间
  private Date createTime;    // 账户创建时间
  private Date updateTime;    // 最后更新时间
}
