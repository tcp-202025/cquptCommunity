package cn.cquptCommunity.spit.dao;

import cn.cquptCommunity.spit.pojo.Spit;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * 吐槽模块的dao：操纵的是MongoDB数据库，只需要继承MongoRepository接口就行
 * MongoRepository<Spit,String>:第一个泛型是实体类的类型，第二个泛型是主键id的类型
 */
public interface SpitDao extends MongoRepository<Spit,String> {

    /**
     * 根据上级id分页查询：pageable封装了分页参数
     * 返回的是springData中为我们提供的Page对象
     */
    public Page<Spit> findByParentid(String parentid, Pageable pageable); //利用的是springData中提供的根据方法命名规则来进行查询，这里相当于select * from where Parentid=? limit ?,?

}
