package com.sunnysnow.test;

import com.sunnysnow.dao.IUserDao;
import com.sunnysnow.domain.QueryVo;
import com.sunnysnow.domain.User;
import javafx.scene.chart.PieChart;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.List;

public class MybatisTest {
    private InputStream in;
    private SqlSession session;
    private IUserDao userDao;

    @Before
    public void  init() throws IOException {
        //1.读取配置文件
        in = Resources.getResourceAsStream("SqlMapConfig.xml");
        //2.创建SqlSessionFactory工厂
        SqlSessionFactoryBuilder builder = new SqlSessionFactoryBuilder();
        SqlSessionFactory factory = builder.build(in);
        //3、使用工厂生产SqlSession对象
        session = factory.openSession();
        //4.使用SqlSession创建Dao接口的代理对象
        userDao =session.getMapper(IUserDao.class);
    }

    @After
    public void destory() throws IOException {
        //6.提交事务
        session.commit();
        //7.释放资源
        session.close();
        in.close();
    }
    @Test
    public  void testFindAll() throws IOException {
        userDao =session.getMapper(IUserDao.class);
        //5.使用代理对象执行方法
        List<User> users = userDao.findAll();
        for(User user:users){
            System.out.println(user);
        }
    }

    /**
     * 测试保存操作
     */
    @Test
    public void testSave() throws IOException {
        User user = new User();
        user.setUsername("mybatis last insertid");
        user.setBirthday(new Date());
        user.setSex("女");
        user.setAddress("江苏南通");
        System.out.println("保存之前："+user);
        //5.使用代理对象执行方法
        userDao.saveUser(user);
        System.out.println("保存之后："+user);
    }

    /**
     * 测试更新操作
     */
    @Test
    public void testUpdate() throws IOException {
        User user = new User();
        user.setId(10);
        user.setUsername("晴晴2");
        user.setBirthday(new Date());
        user.setSex("女");
        user.setAddress("江苏南通");
        //5.使用代理对象执行方法
        userDao.updateUser(user);
    }

    /**
     * 测试删除操作
     */
    @Test
    public void testDelete() throws IOException {
        //5.使用代理对象执行方法
        userDao.deleteUser(10);
    }

    /**
     * 测试查询一个用户
     */
    @Test
    public void testFindOne() throws IOException {
        //5.使用代理对象执行方法
        User user = userDao.findById(9);
        System.out.println(user);
    }

    /**
     * 测试模糊查询用户
     */
    @Test
    public void testfindByName() throws IOException {
        //5.使用代理对象执行方法
        //预处理占位方式更安全，常用这个
        List<User> users = userDao.findByName("%王%");
        //这种是字符串拼接的方式，可以sal注入，一般不用。
        //List<User> users = userDao.findByName("王");
        for (User u:users){
            System.out.println(u);
        }
    }


    /**
     * 测试查询总用户条数
     */
    @Test
    public void testfindTotal() throws IOException {
        //5.使用代理对象执行方法
        int num = userDao.findTotal();
        System.out.println("总用户数为："+num);
    }


    /**
     * 测试使用QueryVo查询条件
     */
    @Test
    public void testfindByQueryVo() throws IOException {
        QueryVo vo = new QueryVo();
        User user = new User();
        user.setUsername("%王%");
        vo.setUser(user);
        //5.使用代理对象执行方法
        //预处理占位方式更安全，常用这个
        List<User> users = userDao.findUserByVo(vo);
        for (User u:users){
            System.out.println(u);
        }
    }
}
