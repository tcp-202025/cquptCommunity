package  cn.cquptCommunity.recruit.dao;

import cn.cquptCommunity.recruit.pojo.Recruit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

/**
 * 数据访问接口
 * @author Administrator
 *
 */
public interface RecruitDao extends JpaRepository<Recruit,String>,JpaSpecificationExecutor<Recruit>{

    /**
     * 根据status来查询推荐职位：state表示状态，0表示关闭，1表示开启，2表示推荐
     */
    public List<Recruit> findTop5ByStateOrderByCreatetimeDesc(String state);//查询前5个推荐职位 select * from r_recruit where state=2 order by createtime desc

    /**
     * 查询最新职位
     */
    public List<Recruit> findTop5ByStateNotOrderByCreatetimeDesc(String state);//查询最新职位（包括推荐与不推荐的） 所以是 where state !=0 order by createtime desc

}
