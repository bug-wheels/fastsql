package com.zyndev;

import java.util.Date;
import javax.persistence.Entity;
import javax.persistence.Table;
import lombok.Data;

/**
 * @author 张瑀楠 zyndev@gmail.com
 * @version 1.0 2018/2/3 下午5:02
 */
@Entity
@Table(name = "tb_car_param")

@Data
public class CarParam {

  private Integer fueltype;//0,
  private Date modifytime;//1516993203000,
  private Integer pv;//92,
  private String underpanTransmissionType;//private String 双离合private String ,
  private String starConfig;//
  private Integer producestate;//1,
  private String brandname;//private String AudiSportprivate String ,
  private String engineExhaustForFloat;//private String 2.5Tprivate String ,
  private Integer masterid;//8,
  private Integer brandid;//193,
  private Integer levelId;//9,
  private Integer id;//40758,
  private Integer caryear;//2017,
  private Integer hasfoucuspic;//0,
  private String urlspell;//private String TTARSprivate String ,
  private String referprice;//private String 84.80private String ,
  private Integer uv;//68,
  private String serialname;//private String TT RSprivate String ,
  private String createtime;//1505210483000,
  private String bodyform;//private String 三厢private String ,
  private String dealerminprice;//private String 73.78private String ,
  private Integer istravel;//0,
  private String dealermaxprice;//private String 84.80private String ,
  private String mastername;//private String 奥迪private String ,
  private String dealerpricedesc;//private String 73.78 - 84.80万private String ,
  private Date markettime;//1505145600000,
  private Integer isenable;//1,
  private Integer serialid;//5079,
  private String name;//private String TT RS 2.5T Coupeprivate String ,
  private Integer isdelete;//0,
  private Integer salestate;//1

}
