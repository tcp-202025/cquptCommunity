package cn.cquptCommunity.recruit.dao;

import cn.cquptCommunity.recruit.pojo.Enterprise;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

/**
 * 数据访问接口
 * @author Administrator
 *
 */
public interface EnterpriseDao extends JpaRepository<Enterprise,String>,JpaSpecificationExecutor<Enterprise>{
    /**查询热门企业*/
    public List<Enterprise> findByIshot(String isHot);  //相当于select * from r_enterprise where isHot=?
	
}
