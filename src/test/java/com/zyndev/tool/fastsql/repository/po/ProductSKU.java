package com.zyndev.tool.fastsql.repository.po;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

/**
 * Created by zyndev on 2017/7/16.
 * author: 张瑀楠
 * email : zyndev@gmail.com  zyndev@163.com
 * description: 商品规格表
 * todo:
 */
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name ="tb_user")
public class ProductSKU implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column
    private Long id;            // 用户id



}
