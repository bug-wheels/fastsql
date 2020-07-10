package com.zyndev;

import java.io.Serializable;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import lombok.Data;

/**
 * @author 张瑀楠 zyndev@gmail.com
 * @version 1.0 2018/2/3 下午4:46
 */
@Data
@Entity
@Table(name = "tb_car_info")

public class CarInfo implements Serializable {

  @Column
  private String imgUrl;

  @Column
  private Integer CarID;

  @Column
  private Integer year;

  @Column
  private String fullAmount;

  private List<String> starConfig;

  Param param;

}
