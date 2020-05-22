package cn.cquptCommunity.user.pojo;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;
import java.io.Serializable;

/**
 * 用户关注表
 */
@Entity
@Table(name="u_follow")
@IdClass(UserFollow.class) //@IdClass定义复合主键，指定的是一个主键类
public class UserFollow implements Serializable {
    @Id
    String userid;
    @Id
    String targetuser;

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getTargetuser() {
        return targetuser;
    }

    public void setTargetuser(String targetuser) {
        this.targetuser = targetuser;
    }
}
