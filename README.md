```
                                                                     
   ad88                                                          88  
  d8"                             ,d                             88  
  88                              88                             88  
MM88MMM  ,adPPYYba,  ,adPPYba,  MM88MMM  ,adPPYba,   ,adPPYb,d8  88  
  88     ""     `Y8  I8[    ""    88     I8[    ""  a8"    `Y88  88  
  88     ,adPPPPP88   `"Y8ba,     88      `"Y8ba,   8b       88  88  
  88     88,    ,88  aa    ]8I    88,    aa    ]8I  "8a    ,d88  88  
  88     `"8bbdP"Y8  `"YbbdP"'    "Y888  `"YbbdP"'   `"YbbdP'88  88  
                                                             88      
                                                             88      
     
```

# fastsql

一个简单的数据库工具类，可以简化 `DB` 操作，减少 `SQL` 语句的书写，同时提供将 `SQL` 转换 `Bean` 和将 `Bean` 转换 `SQL` 的方法，

# 例子

提供几个简单的`orm`操作，简化部分`sql`书写

准备一个实体

```java
package com.zyndev.tool.fastsql.repository;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;

/**
 * @author 张瑀楠 zyndev@gmail.com
 * @version 1.0
 * @date 2017-12-27 15:04:13
 */
@Data
@Entity
@Table(name = "tb_user")
public class User implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     *
     */
    @Id
    @Column
    private Integer id;

    /**
     *
     */
    @Column
    private String uid;

    /**
     * 登录用户名
     */
    @Column(name = "account_name")
    private String accountName;

    /**
     * 最多10个汉字
     */
    @Column(name = "nick_name")
    private String nickName;

    /**
     *
     */
    @Column
    private String password;

    /**
     *
     */
    @Column
    private String phone;

    /**
     *
     */
    @Column(name = "register_time")
    private Date registerTime;

    /**
     *
     */
    @Column(name = "update_time")
    private Date updateTime;

}

```

**`BaseRepository`** 使用
```java
/**
 * 保存一个实体，并返回受影响的行数，
 *
 * @param entity the entity
 * @return the int
 */
int save(Object entity);

/**
 * 更新一个实体，并返回受影响的行数，实体中 null 将不参与更新
 *
 * @param entity the entity
 * @return the int
 */
int update(Object entity);

/**
 * 更新一个实体，并返回受影响的行数，手动设置是否要忽略 null
 *
 * @param entity     the entity
 * @param ignoreNull the ignore null
 * @return the int
 */
int update(Object entity, boolean ignoreNull);

/**
 * 根据 id 删除一个对象
 *
 * @param entity the entity
 * @return the int
 */
int delete(Object entity);

/**
 * Find by id e.
 * 根据 id 查找一个对象，将查询所有的列
 *
 * @param <E>    the type parameter
 * @param entity the entity
 * @return the e
 */
<E> E findById(E entity);

/**
 * Find by id e.
 * 根据 id 查找一个对象，规定返回的列
 *
 * @param <E>     the type parameter
 * @param entity  the entity
 * @param columns the columns
 * @return the e
 */
<E> E findById(E entity, String columns);

/**
 * Gets entity list.
 * 根据实体中非空字段进行等值查询数据
 *
 * @param <E>    the type parameter
 * @param entity the entity
 * @return the entity list
 */
<E> List<E> getEntityList(E entity);
```

# 带条件查询

```java
@Query("select count(*) from tb_user")
public Integer getCount();

@Query("delete from tb_user where id = ?1")
public Boolean deleteById(int id);

@Query("delete from tb_user where id = ?1")
public Integer getCountByPassword(@Param("password") String password);

@Query("select uid from tb_user where password = ?1 ")
public String getUidByPassword(@Param("password") String password);

@Query("select * from tb_user where id = :id ")
public User getUserById(@Param("id") Integer id);

@Query("select * " +
        " from tb_user " +
        " where account_name = :accountName ")
public List<User> getUserByAccountName(@Param("accountName") String accountName);

@Query("insert into tb_user(id, account_name, password, uid, nick_name, register_time, update_time) " +
        "values(:id, :user.accountName, :user.password, :user.uid, :user.nickName, :user.registerTime, :user.updateTime )")
public int saveUser(@Param("id") Integer id, @Param("user") User user);

@ReturnGeneratedKey
@Query("insert into tb_user(account_name, password, uid, nick_name, register_time, update_time) " +
        "values(:user.accountName, :user.password, :user.uid, :user.nickName, :user.registerTime, :user.updateTime )")
public int saveUser(@Param("user") User user);

@Query("select * " +
        " from tb_user " +
        " where 1=1 " +
        " @if(?1 != null ) { and name like concat('%',?1,'%')} ")
public List<User> queryUserByName(String name);
```

## 参数通过两种方式指定
1. 位置参数
2. 命名参数

**位置参数**

可以使用 ?1 ?2 等指定

**命令参数**

使用 `@Param` 进行处理

例如：

```java
@Query("select * from tb_user where id = :id ")
public User getUserById(@Param("id") Integer id);
```

**位置参数和明明参数可以混用**

**注意**：在没有查询到数据的情况下,如果返回值是集合类型,返回具体的值不会是`null`,而是一个空集合. 如果是对象，则返回 `null` 

# 条件表达式

根据业务我们经常需要动态的构建`sql`,例如`mybatis`的`if`标签，在这里也提供了一种语法 `@if( condition) { statement }`,其中 `condition` 为一个布尔语句，当
语句成立时，拼接后面的 `statement`

例如：
```java
@Query("select * " +
        " from tb_user " +
        " where 1=1 " +
        " @if(?1 != null ) { and name like concat('%',?1,'%')} ")
public List<User> queryUserByName(String name);
```
当 `name` 为`null` 时，则查询语句为 `select * from tb_user where 1=1 `,
否则为 `select * from tb_user where 1=1 and name like concat('%',?1,'%') `

# 注意

**注意**: 查询单个字段,还支持返回如下类型:
- `String`
- `int` 或 `Integer`

除了改操作或count外,查单个字段不能返回基本类型,因为:基本类型不能接受`null`值,而SQL表字段可以为`null`.
返回类型若是基本类型的包装类型,若返回null, 表示:没有查到或查到的值本身就是null.
例如: 

**注解使用**

| Annotation | 作用 |
|:---|:---|
|`@Query`|标识查询语句|
|`@Param`|标识命名参数|
|`@ReturnGeneratedKey`|返回自增主键id|


# 运行环境要求
jdk1.8

# 关于作者

邮箱 zyndev@gmail.com
