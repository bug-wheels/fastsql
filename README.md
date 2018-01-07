# zyntool
```
                                                           
                                                       ,,  
                               mm                    `7MM  
                               MM                      MM  
M"""MMV `7M'   `MF'`7MMpMMMb.mmMMmm ,pW"Wq.   ,pW"Wq.  MM  
'  AMV    VA   ,V    MM    MM  MM  6W'   `Wb 6W'   `Wb MM  
  AMV      VA ,V     MM    MM  MM  8M     M8 8M     M8 MM  
 AMV  ,     VVV      MM    MM  MM  YA.   ,A9 YA.   ,A9 MM  
AMMmmmM     ,V     .JMML  JMML.`Mbmo`Ybmd9'   `Ybmd9'.JMML.
           ,V                                              
        OOb"       
```
常用工具类集合，方便使用，暂时分为 
- core
- fastsql
- validation

core 主要一些基础类型操作，例如格式转换，格式化，加密解密等常用类型工具集合

fastsql 为一个简单的数据库工具类，可以简化 DB 操作，减少sql语句的书写，如果你不想使用hibernate，也不想写SQL可以使用

validation 做一些常用参数校验，例如：非空、最大值、最小值、正则校验、长度校验、邮箱校验、空值校验等常用操作

# fastsql

一个简单的数据库工具类，可以简化 `DB` 操作，减少 `SQL` 语句的书写，同时提供将 `SQL` 转换 `Bean` 和将 `Bean` 转换 `SQL` 的方法，

**注意**

该项目在功能上并没有原创性，最终结果会接近jpa的用法

提供几个简单的`orm`操作，简化部分`sql`书写

## 例子

准备一个实体

```java
@Entity
@Table(name = "tb_student")
public class Student {


    @Id
    @Column
    private Integer id;

    @Column
    private String name;

    // getter and setter

}
```

**`BaseRepository`** 使用
```java
int save(Object entity);

int update(Object entity);

int update(Object entity, boolean ignoreNull);

int delete(Object entity);

<E> E findById(E entity);

<E> E findById(E entity, String columns);

<E> List<E> getEntityList(E entity);
```

## 带条件查询

暂时测试通过的例子，后期会添加更多支持

```java
@Query("select count(*) from tb_user")
public Integer getCount();

@Query("delete from tb_user where id = ?")
public Boolean deleteById(int id);

@Query("select count(*) from tb_user where password = ? ")
public int getCountByPassword(@Param("password") String password);

@Query("select uid from tb_user where password = ? ")
public String getUidByPassword(@Param("password") String password);

@Query("select * from tb_user where id = ? ")
public User getUserById(Integer id);

@Query("select * " +
        " from tb_user " +
        " where account_name = ? ")
public List<User> getUserByAccountName(String accountName);
```

**注意**: 
在没有查询到数据的情况下,如果返回值是集合类型,返回具体的值不会是`null`,而是一个空集合. 如果是对象，则返回 `null` 


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


# validation

在日常开发中，常常需要检验一些参数是否合法，尤其在web开发中，由前端传来的数据更加不可靠，即使前端存在验证，我们也要担心数据
是否被恶意篡改，而且做的大部分校验都是重复工作，例如：字符串的长度校验，数字的合法性，最小值，最大值等

# 运行环境要求
jdk1.8

# 关于作者

邮箱 zyndev@gmail.com
