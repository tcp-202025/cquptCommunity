package cn.cquptCommunity.base.dao;

import cn.cquptCommunity.base.pojo.Label;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * 创建符合是SpringDataJPA的dao层接口规范
 *      需要继承JpaRepository和JpaSpecificationExecutor这两个接口
 *      JpaRepository<Label,String>对应操作的实体类类型和实体类中主键属性的类型
 *          在JpaRepository封装了基本的CRUD操作
 *      JpaSpecificationExecutor<Label>对应操作的实体类类型
 *          在JpaSpecificationExecutor中封装了复杂查询的操作
 */
public interface LabelDao extends JpaRepository<Label,String>, JpaSpecificationExecutor<Label> {

    /**
     * 查询推荐标签列表：label表中有个recommend字段：0表示不推荐  1表示推荐
     */
    @Modifying
    @Query(value = "SELECT * FROM b_label WHERE recommend='1'",nativeQuery = true)
    public List<Label> findTopList();

    /**
     * 查询推荐标签列表：label表中有个state字段：0表示无效 1表示有效
     */
    @Modifying
    @Query(value = "SELECT * FROM b_label WHERE state='1'",nativeQuery = true)
    public List<Label> findEffective();
}
