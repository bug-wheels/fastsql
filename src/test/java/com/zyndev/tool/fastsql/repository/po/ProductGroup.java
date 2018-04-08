package com.zyndev.tool.fastsql.repository.po;


import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Entity
@Table(name = "tb_product_group")
public class ProductGroup {

    @Id
    @Column
    private Long id;            // 用户id

	private String sellerId;
	private String groupId;
	private String groupName;
	private String pid;
	private Integer orderId;
	private Integer level;

	public void setLevel(Integer level) {
	    if (level == null) {
	        level = 1;
        }
        this.level = level;
    }
}
