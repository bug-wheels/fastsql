package com.zyndev.tool.fastsql.repository.po;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;

/**
 * Created by zyndev on 2017/7/8.
 * author: 张瑀楠
 * email : zyndev@gmail.com  zyndev@163.com
 * description: 订单详情
 * todo:
 */
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "tb_customer_order_detail")
public class OrderDetailPO implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;        // id
    private Long orderId;   // 订单id 参考 CustomerOrder.id




}
