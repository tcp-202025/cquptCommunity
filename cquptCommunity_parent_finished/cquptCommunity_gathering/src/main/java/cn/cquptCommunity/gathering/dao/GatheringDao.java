package cn.cquptCommunity.gathering.dao;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import cn.cquptCommunity.gathering.pojo.Gathering;

/**
 * 活动模块的数据层访问接口
 */
public interface GatheringDao extends JpaRepository<Gathering,String>,JpaSpecificationExecutor<Gathering>{

    /**
     * 根据城市查询活动
     * city:要查询的城市
     * pageable:封装的分页参数
     */
    public Page<Gathering> findByCity(String city, Pageable pageable);//使用的是springDataJPA的方法命名规则：相当于where city=? limit ?,?

}
