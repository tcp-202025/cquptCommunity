package cn.cquptCommunity.qa.dao;

import cn.cquptCommunity.qa.pojo.PL;
import org.springframework.data.jpa.repository.JpaRepository;
/**
 * 问题-标签表：使用的是springDataJPA,所以需要继承JpaRepository<PL（实体类）,String（主键id）>接口
 */
public interface PLDao extends JpaRepository<PL,String> {

}
