package com.zyndev.tool.fastsql.repository.po;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 快递单模板
 *
 * @author 张瑀楠 zyndev@gmail.com
 * @version 1.0
 */
@Entity
@Table(name = "tb_seller_post_template")
public class SellerPostTemplatePO {

    /**
     * 模板id
     */
    @Id
    @Column
    private Integer id;

    /**
     * 模板id
     */
    @Column
    private Integer templateId;  // 模板 id

    /**
     * 模板图片
     */
    @Column
    private String templatePic;

    /**
     * 模板名称
     */
    @Column
    private String templateName;

    /**
     * 快递公司
     */
    @Column
    private String expressCompany;

    /**
     * 模板宽度
     */
    @Column
    private String templateWidth;

    /**
     * 模板高度
     */
    @Column
    private String templateHeight;

    @Column
    private String itemVals;

    @Column
    private String propVals;

    @Column
    private String sellerId; // 最后一次编辑  loginId

    @Column
    private String companyName;

    @Column
    private Integer isDefault;

    @Column
    private String printer;

    @Column
    private String logisticsCompanyName;

    @Column
    private String offsetInfo;

    @Column
    private String nextVal;
}
