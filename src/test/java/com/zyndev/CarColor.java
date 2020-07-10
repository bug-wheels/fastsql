package com.zyndev;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.Table;
import lombok.Data;

/**
 * @author 张瑀楠 zyndev@gmail.com
 * @version 1.0 2018/2/3 下午4:52
 */
@Data
@Entity
@Table(name = "tb_car_color")
public class CarColor implements Serializable {

  private Integer id;
  private String colorName;
  private String rgb;
}
