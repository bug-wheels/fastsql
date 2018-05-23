package com.zyndev.tool.fastsql.repository.po;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;

/**
 * author: 张瑀楠
 * email : zyndev@gmail.com
 * desc  : 档口管理
 * date  : 2017/8/24 下午11:02
 * todo  :
 */

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name = "tb_stall")
public class Stall implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column
    private Long id;
    private Long userId;
    private String name;
    private String contact;
    private String address;
    private String phone;
    @Column(name = "we_chat")
    private String wechat;
    private String remark;
    private Date createTime;
    private Date updateTime;
}
