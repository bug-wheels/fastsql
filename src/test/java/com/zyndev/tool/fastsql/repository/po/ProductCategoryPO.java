/*
 * Copyright (c) 2018. author and authors
 */

package com.zyndev.tool.fastsql.repository.po;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.Data;

/**
 * @author 张瑀楠 zyndev@gmail.com
 * @version 1.0.0 2018/2/20 上午11:00
 */
@Entity
@Data
@Table(name = "tb_product_category")
public class ProductCategoryPO implements Serializable {

  private static final long serialVersionUID = 1L;

  @Id
  @Column
  private Integer id;

  private Long categoryId;

  private String name;

  private Long parentCateoryId;

  private Integer level;

  private Integer isLeaf;

}
