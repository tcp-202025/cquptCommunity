package cn.cquptCommunity.base.dao;

import cn.cquptCommunity.base.pojo.Label;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * 创建符合是SpringDataJPA的dao层接口规范
 *      需要继承JpaRepository和JpaSpecificationExecutor这两个接口
 *      JpaRepository<Label,String>对应操作的实体类类型和实体类中主键属性的类型
 *          在JpaRepository封装了基本的CRUD操作
 *      JpaSpecificationExecutor<Label>对应操作的实体类类型
 *          在JpaSpecificationExecutor中封装了复杂查询的操作
 */
public interface ILabelDao extends JpaRepository<Label,String>, JpaSpecificationExecutor<Label> {
}
