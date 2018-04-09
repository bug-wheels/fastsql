package com.zyndev.tool.fastsql.repository.po;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;

/**
 * @author 张瑀楠 zyndev@gmail.com
 * @version 1.0
 * date  : 2017/12/16 下午4:14
 * todo  :
 */
@Setter
@Getter
@ToString
@Entity
@Table(name = "tb_sender_info")
public class SenderInfoPO implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column
    private Long id;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column
    private String name;

    @Column
    private String phone;

    @Column
    private String address;

    @Column(name = "create_time")
    private Date createTime;    // 账户创建时间

    @Column(name = "update_time")
    private Date updateTime;    // 最后更新时间

}
