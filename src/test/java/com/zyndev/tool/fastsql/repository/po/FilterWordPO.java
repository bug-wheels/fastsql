package com.zyndev.tool.fastsql.repository.po;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;

/**
 * 过滤词设置
 *
 * @author 张瑀楠 zyndev@gmail.com
 * @version 1.0
 * @date 2017/12/20 下午10:16
 */
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "tb_filter_word")
public class FilterWordPO implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "id", columnDefinition = "主键id")
    private Integer id;

    @Column(name = "user_id", columnDefinition = "用户id")
    private Long userId;

    @Column(name = "origin", columnDefinition = "原字符串")
    private String origin;

    @Column(name = "target", columnDefinition = "目标字符串")
    private String target;

    @Column(name = "create_time", columnDefinition = "创建时间")
    private Date createTime;

    @Column(name = "update_time", columnDefinition = "更新时间")
    private Date updateTime;
}
