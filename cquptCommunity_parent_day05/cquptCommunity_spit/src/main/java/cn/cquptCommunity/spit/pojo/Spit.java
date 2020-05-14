package cn.cquptCommunity.spit.pojo;

import org.springframework.data.annotation.Id;

import java.io.Serializable;
import java.util.Date;

/**
 * 吐槽模块的实体类
 */
public class Spit implements Serializable {
    @Id
    private String _id;//主键
    private String content;//吐槽的内容
    private Date publishtime;//吐槽的时间
    private String userid;//吐槽的发布者的id
    private String nickname;//吐槽的发布者的名称
    private Integer visits;//访问量
    private Integer thumbup;//点赞数
    private Integer share;//转发数
    private Integer comment;//回复数
    private String state;//是否可见
    private String parentid;//上一级的id

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Date getPublishtime() {
        return publishtime;
    }

    public void setPublishtime(Date publishtime) {
        this.publishtime = publishtime;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public Integer getVisits() {
        return visits;
    }

    public void setVisits(Integer visits) {
        this.visits = visits;
    }

    public Integer getThumbup() {
        return thumbup;
    }

    public void setThumbup(Integer thumbup) {
        this.thumbup = thumbup;
    }

    public Integer getShare() {
        return share;
    }

    public void setShare(Integer share) {
        this.share = share;
    }

    public Integer getComment() {
        return comment;
    }

    public void setComment(Integer comment) {
        this.comment = comment;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getParentid() {
        return parentid;
    }

    public void setParentid(String parentid) {
        this.parentid = parentid;
    }
}
