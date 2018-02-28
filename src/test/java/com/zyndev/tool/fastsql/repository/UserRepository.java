package com.zyndev.tool.fastsql.repository;

import com.zyndev.tool.fastsql.annotation.Param;
import com.zyndev.tool.fastsql.annotation.Query;
import com.zyndev.tool.fastsql.annotation.ReturnGeneratedKey;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import java.util.List;
import java.util.Map;

/**
 *
 * @version 1.0
 * @author 张瑀楠 zyndev@gmail.com
 */
@NamedQueries({
        @NamedQuery(name = "findAll", query = "select o from User o")
})
@Repository
public interface UserRepository {

    @Query("select count(*) from tb_user")
    public Integer getCount();

    @Query("delete from tb_user where id = ?1")
    public Boolean deleteById(int id);

    @Query("select count(*) from tb_user where password = ?1 ")
    public int getCountByPassword(@Param("password") String password);

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
            " #if(?1 != null ) { name like concat('%',?1,'%')} ")
    public Map<Integer, User> queryUserByName(String name);

}
