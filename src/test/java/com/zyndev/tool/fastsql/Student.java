package com.zyndev.tool.fastsql;

import javax.persistence.*;

/**
 * desc :
 * author: 张瑀楠
 * email : zyndev@gmail.com
 * date  : 2017/11/30 下午11:21
 * todo  :
 */
@Entity
@Table(name = "tb_student")
public class Student {


    @Id
    @Column
    private Integer id;

    @Column
    private String name;

    @Column(updatable = true, insertable = true, nullable = false)
    private Integer age;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

}
