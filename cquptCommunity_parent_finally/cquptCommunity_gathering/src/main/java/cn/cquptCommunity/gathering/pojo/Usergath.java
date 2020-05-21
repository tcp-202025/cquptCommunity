package cn.cquptCommunity.gathering.pojo;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;

/**
 * 存储用户参加了哪些活动
 */
@Entity
@Table(name= "g_usergath")
@IdClass(Usergath.class) //@IdClass定义复合主键，指定的是一个主键类
public class Usergath implements Serializable {
    @Id
    String userid;//用户id

    @Id
    String gathid;//活动id

    Date exetime;//参加的活动的时间

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getGathid() {
        return gathid;
    }

    public void setGathid(String gathid) {
        this.gathid = gathid;
    }

    public Date getExetime() {
        return exetime;
    }

    public void setExetime(Date exetime) {
        this.exetime = exetime;
    }
}
