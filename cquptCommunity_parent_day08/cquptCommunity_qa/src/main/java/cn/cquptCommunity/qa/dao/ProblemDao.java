package cn.cquptCommunity.qa.dao;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import cn.cquptCommunity.qa.pojo.Problem;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * 数据访问接口
 * @author Administrator
 *
 */
public interface ProblemDao extends JpaRepository<Problem,String>,JpaSpecificationExecutor<Problem>{

    /*
        一个标签下边有多个相关问题，一个问题可以属于多个标签。
        q_problem表中的字段主键id  q_pl中的联合主键（problemid,labelid）
     */


    /**
     * 点击不同的标签，会有不同的问题,所以是根据标签id来查询
     * 查询最新问答: 根据回复时间排序
     */
    @Query(value ="SELECT * FROM q_problem,q_pl WHERE id=problemid AND labelid=? order by replytime desc ",nativeQuery = true)
    public Page<Problem> newList(String labelid, Pageable pageable);

    /**
     * 查询最热问答: 根据回复人数排序
     */
    @Query(value ="SELECT * FROM q_problem,q_pl WHERE id=problemid AND labelid=? order by reply desc ",nativeQuery = true)
    public Page<Problem> hotList(String labelid, Pageable pageable);

    /**
     * 查询等待中的问答: 回复数为0
     */
    @Query(value ="SELECT * FROM q_problem,q_pl WHERE id=problemid AND labelid=? AND reply=0",nativeQuery = true)
    public Page<Problem> waitList(String labelid, Pageable pageable);
}
