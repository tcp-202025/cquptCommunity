package cn.cquptCommunity.base.dao;
import cn.cquptCommunity.base.pojo.City;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * 城市模块的数据层访问接口
 */
public interface CityDao extends JpaRepository<City,String>,JpaSpecificationExecutor<City>{

}
