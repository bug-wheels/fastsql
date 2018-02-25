package com.zyndev.tool.fastsql.repository.po;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;

/**
 * author: 张瑀楠
 * email : zyndev@gmail.com
 * desc  :
 * date  : 2017/8/27 上午11:34
 * todo  :
 */
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "tb_user_sync_config")
public class UserSyncConfig implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column
    private Long id;
    private String sellerId;
    private Date productSyncTime;   // 商品同步时间
    private Date orderSyncTime;     // 订单同步时间
    private Date accessTokenSyncTime;   // 授权同步时间

}
