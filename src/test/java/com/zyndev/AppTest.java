package com.zyndev;


import com.zyndev.tool.fastsql.annotation.EnableFastSql;
import com.zyndev.tool.fastsql.repository.UserRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;


/**
 * Unit test for simple App.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"/app*.xml"})
@EnableFastSql
public class AppTest {

  @Autowired
  private UserRepository userRepository;

  @Test
  public void testApp() {
    System.out.println("getCount:\t" + userRepository.getCount());
    System.out.println("deleteById:\t" + userRepository.deleteById(3));
    System.out.println("getCountByPassword:\t" + userRepository.getCountByPassword("zhang"));
    System.out.println("getUidByPassword:\t" + userRepository.getUidByPassword("123456"));
    System.out.println("queryUserByName:" + userRepository.queryUserByName("abc"));
    // userRepository.queryUserByName("");
  }
}
