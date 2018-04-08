/*
 * Copyright (c) 2018. author and authors
 */

package com.zyndev.tool.fastsql.repository.po;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

/**
 * 标准菜鸟模板
 *
 * @author 张瑀楠 zyndev@gmail.com
 * @version 1.0
 */
@Data
@Entity
@Table(name = "tb_standard_cainiao_template")
public class StandardCaiNiaoTemplatePO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @Id
    @Column
    private Integer id;

    /**
     * YTOcp编码
     */
    @Column
    private String cpCode;

    /**
     * 模板id
     */
    @Column
    private Integer standardTemplateId;

    /**
     * 模板模板名称
     */
    @Column
    private String standardTemplateName;

    /**
     * 模板url
     */
    @Column
    private String standardTemplateUrl;

}
