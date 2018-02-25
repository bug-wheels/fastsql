package com.zyndev;


import com.zyndev.tool.fastsql.repository.User;
import com.zyndev.tool.fastsql.repository.UserRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;


/**
 * Unit test for simple App.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"/app*.xml"})
public class AppTest {

    @Autowired
    private UserRepository userRepository;

    @Test
    public void testApp() {
        System.out.println("getCount:\t" + userRepository.getCount());
        System.out.println("deleteById:\t" + userRepository.deleteById(3));
        System.out.println("getCountByPassword:\t" + userRepository.getCountByPassword("zhang"));
        System.out.println("getUidByPassword:\t" + userRepository.getUidByPassword("123456"));
        User user = userRepository.getUserById(2);
        System.out.println(user);
        List<User> userList = userRepository.getUserByAccountName("zyndev");
        System.out.println(userList);

        user.setId(3);
        System.out.println(userRepository.saveUser(3, user));
        System.out.println(userRepository.saveUser(user));
        // userRepository.queryUserByName("");
    }
}
