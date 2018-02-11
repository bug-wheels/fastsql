package com.zyndev;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.junit.Test;

import java.io.*;

/**
 * @author 张瑀楠 zyndev@gmail.com
 * @version 1.0
 * 2018/2/3 下午4:27
 */
public class JsonTest {

    @Test
    public void test() throws IOException {


        File file = new File("/Users/zhangyunan/Downloads/auto");

        FileReader fileReader = new FileReader(file);
        BufferedReader bufferedReader = new BufferedReader(fileReader);
        String text;
        text = bufferedReader.readLine();
        while (text != null) {
            System.out.println(text);
            if (text.startsWith("{")) {
                CarInfo carInfo = JSON.parseObject(text, CarInfo.class);
                StringBuilder sql = new StringBuilder("insert into `tb_car_info`(`img_url`,`car_id`,`year`,`full_amount`,`star_config`) values(");

                sql.append("'").append(carInfo.getImgUrl()).append("'");
                sql.append(",").append(carInfo.getCarID());
                sql.append(",").append(carInfo.getYear());
                sql.append(",'").append(carInfo.getFullAmount()).append("'");
                sql.append(",'").append(carInfo.getStarConfig().toString()).append("'");
                sql.append(")");

                StringBuilder paramSql = new StringBuilder("insert into `tb_param`(`img_url`,`car_id`,`year`,`full_amount`,`star_config`) values(");

                Param param = carInfo.getParam();
                sql.append("'").append(carInfo.getImgUrl()).append("'");
                sql.append(",").append(carInfo.getCarID());
                sql.append(",").append(carInfo.getYear());
                sql.append(",'").append(carInfo.getFullAmount()).append("'");
                sql.append(",'").append(carInfo.getStarConfig().toString()).append("'");
                sql.append(")");

                System.out.println("sql:" + sql.toString());
                System.out.println(carInfo);
            }
            text = bufferedReader.readLine();
        }
        bufferedReader.close();

    }

}
