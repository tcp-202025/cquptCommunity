package cn.cquptCommunity.qa.pojo;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;
import java.io.Serializable;

/**
 * 问题-标签中间表
 */
@Entity
@Table(name="q_pl")
@IdClass(PL.class) //@IdClass定义复合主键，指定的是一个主键类
public class PL implements Serializable {

    @Id
    String problemid;

    @Id
    String labelid;

    public String getProblemid() {
        return problemid;
    }

    public void setProblemid(String problemid) {
        this.problemid = problemid;
    }

    public String getLabelid() {
        return labelid;
    }

    public void setLabelid(String labelid) {
        this.labelid = labelid;
    }
}
