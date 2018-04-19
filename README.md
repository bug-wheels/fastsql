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

# Apache Maven
```xml
<!-- https://mvnrepository.com/artifact/com.github.zyndev/fastsql -->
<dependency>
    <groupId>com.github.zyndev</groupId>
    <artifactId>fastsql</artifactId>
</dependency>
```

# FastSQL 主要特性如下:
1. 遵循非侵入式原则,设计优雅或简单,极易上手
2. 支持安全查询,防止SQL注入
3. 支持与主流数据库连接池框架集成
4. 支持 `@Query` 查询
5. 拥有非常优雅的`Page`(分页)设计
6. 支持单表`ORM`查询
7. 支持部分`jpa`注解
8. 支持动态SQL创建
9. 支持驼峰标识与下划线标识转换

# 运行环境要求
jdk1.8+

# 入门例子

- 准备一个实体

```java
@Data
@Entity
@Table(name = "tb_user")
public class User implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column
    private Integer id;

    @Column
    private String uid;

    @Column(name = "account_name")
    private String accountName;

    @Column(name = "nick_name")
    private String nickName;

    @Column
    private String password;

    @Column
    private String phone;

    @Column(name = "register_time")
    private Date registerTime;

    @Column(name = "update_time")
    private Date updateTime;

}
```

- DAO接口

```java
/**
 * @version 0.0.5
 * @author 张瑀楠 zyndev@gmail.com
 */
@Repository
public interface UserRepository {

    @Query("select count(*) from tb_user")
    public Integer getCount();

    @Query("delete from tb_user where id = ?1")
    public Boolean deleteById(int id);

    @Query("select count(*) from tb_user where id = ?1")
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
    public Map<Integer, User> queryUserByName(String name);

}
```

- 使用DAO接口.

该项目主要为了和Spring项目整合使用，这里可以直接通过自动注入方式使用,后面会讲到如何整合到`Spring MVC`和`Spring Boot`项目，暂时这里使用注入的方式

```java
@Autowired
private UserRepository userRepository;
```

这里直接调用指定的方法即可

# 关于 @Query 的使用

在上面的示例中，使用了类似`JPA`的`Query`方式,这里讲解一下

## 参数通过两种方式指定

1. 位置参数
2. 命名参数

在上面的`Query` 语句中，大致可分为以下三类：

1. 不需要参数

这类查询不需要参数
```java
@Query("select count(*) from tb_user")
public Integer getCount();
```

2. 位置参数

这类需要初入参数，在语句中可以使用 ?1 ?2 等指定

**注意：这里从 1 开始计数，而不是0**

```java
@Query("delete from tb_user where id = ?1")
public Boolean deleteById(int id);
```

3. 命名参数

使用 `@Param` 进行处理，在语句中可以使用 :xxx :xxxx 等指定

例如：

```java
@Query("select * from tb_user where id = :id ")
public User getUserById(@Param("id") Integer id);
```


**位置参数和命名参数可以混用**

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
- `Byte` 和 `byte`
- `Short` 和 `short`
- `Integer` 和 `int`
- `Long` 和 `long`
- `Float` 和 `float`
- `Double` 和 `double`
- `Character` 和 `char`
- `Boolean` 和 `boolean`

**注解使用**

| Annotation | 作用 |
|:---|:---|
|`@Query`|标识查询语句|
|`@Param`|标识命名参数|
|`@ReturnGeneratedKey`|返回自增主键id|

 
## BaseRepository的内置方法

**这里的entity比如有 @Entity 注解**
```java
// 验证id是否存在
<T> boolean existsById(T entity);

// 统计entity的数量
<T> long count(T entity);

// 保存一个entity, null属性 不插入
<T> int save(T entity);

// 保存多个entity, null属性 不插入
<T> int saveAll(Iterable<T> entities);

// 保存一个entity, 并设置null属性 是否插入
<T> int save(T entity, boolean ignoreNull);

// 保存多个entity, 并设置null属性 是否插入
<T> int saveAll(Iterable<T> entities, boolean ignoreNull);

// 删除一个对象
<T> int deleteById(T entity);

// 删除符合条件的相似对象
<T> int delete(T entity);

// 删除多个对象，其中每个对象必须有id
<T> int deleteAll(Iterable<T> entities);

// 更新一个对象，其中对象必须有id，null 值不更新
<T> int update(T entity);

// 更新一个对象，其中对象必须有id，并设置null是否更新
<T> int update(T entity, boolean ignoreNull);

// 更新多个对象，其中对象必须有id，null 值不更新
<T> int update(Iterable<T> entities);

// 更新多个对象，其中对象必须有id，并设置null是否更新
<T> int update(Iterable<T> entities, boolean ignoreNull);

<T> T findById(T entity);

// 只查询指定的字段
<T> T findById(T entity, String... columns);

<T> List<T> getEntityList(T entity);

<T> List<T> getEntityList(T entity, String... columns);

<T> List<T> getEntityListBySQL(String sql, T entity);

<T> List<T> getEntityListBySQL(String sql, Object[] args, T entity);

<T> PageListContent<T> getEntityPageList(T entity, int pageNum, int pageSize);

<T> PageListContent<T> getEntityPageList(T entity, int pageNum, int pageSize, String orderBy);

<T> PageListContent<T> getEntityPageList(T entity, int pageNum, int pageSize, String orderBy, String... columns);

<T> PageListContent<T> getEntityPageListBySql(String sql, T entity, int pageNum, int pageSize);

<T> PageListContent<T> getEntityPageListBySql(String sql, T entity, int pageNum, int pageSize, String orderBy);

<T> PageListContent<T> getEntityPageListBySql(String sql, Object[] args, T entity, int pageNum, int pageSize, String orderBy);

```

## 分页
**BaseRepository内置分页方法**

```
<T> PageListContent<T> getEntityPageList(T entity, int pageNum, int pageSize);

<T> PageListContent<T> getEntityPageList(T entity, int pageNum, int pageSize, String orderBy);

<T> PageListContent<T> getEntityPageList(T entity, int pageNum, int pageSize, String orderBy, String... columns);

<T> PageListContent<T> getEntityPageListBySql(String sql, T entity, int pageNum, int pageSize);

<T> PageListContent<T> getEntityPageListBySql(String sql, T entity, int pageNum, int pageSize, String orderBy);

<T> PageListContent<T> getEntityPageListBySql(String sql, Object[] args, T entity, int pageNum, int pageSize, String orderBy);
```


## 开发环境
  IDE: IDEA          
build: maven 

# 关于作者

张瑀楠 zyndev@gmail.com
