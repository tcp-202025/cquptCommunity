package cn.cquptCommunity.article.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import cn.cquptCommunity.article.pojo.Column;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * 专栏模块的数据层访问接口
 */
public interface ColumnDao extends JpaRepository<Column,String>,JpaSpecificationExecutor<Column>{
    /**
     * 根据用户id查询专栏名称
     */
    public List<Column> findByUserid(String userid);

    /**
     * 申请专栏审核：将状态state改为1
     */
    @Modifying
    @Query(value = "update a_column set state='1' where id=?",nativeQuery = true)
    public void updateState(String columnId);
}
