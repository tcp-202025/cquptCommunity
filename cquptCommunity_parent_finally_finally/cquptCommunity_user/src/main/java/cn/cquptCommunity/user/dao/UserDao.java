package cn.cquptCommunity.user.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import cn.cquptCommunity.user.pojo.User;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import javax.xml.soap.SAAJResult;

/**
 * user的数据层访问接口
 */
public interface UserDao extends JpaRepository<User,String>,JpaSpecificationExecutor<User>{

    public User findByMobile(String mobile);//根据手机号查询

    public User findByNickname(String nickname);//根据用户名查询
    @Modifying
    @Query(value = "update u_user set fanscount=fanscount+? where id = ?",nativeQuery = true)
    public void updateFansCount(int x, String friendid);//更新粉丝数

    @Modifying
    @Query(value = "update u_user set followcount=followcount+? where id = ?",nativeQuery = true)
    public void updateFollowCount(int x, String userid);//更新关注数

    @Modifying
    @Query(value = "update u_user set password=?2 where mobile=?1",nativeQuery = true)
    public void updatePassword(String mobile,String password);

}
