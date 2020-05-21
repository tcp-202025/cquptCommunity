package cn.cquptCommunity.spit.service;

import cn.cquptCommunity.spit.dao.SpitDao;
import cn.cquptCommunity.spit.pojo.Spit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import util.IdWorker;

import java.util.Date;
import java.util.List;

/**
 * 吐槽模块的业务层
 */
@Service
@Transactional
public class SpitService {

    @Autowired
    private SpitDao spitDao;

    @Autowired
    private IdWorker idWorker;//主键生成器，会自动帮助我们生成主键

    @Autowired
    private MongoTemplate mongoTemplate;

    /**查询全部*/
    public List<Spit>  findAll(){
        return spitDao.findAll();
    }

    /**查询一个*/
    public Spit findById(String id){
        return spitDao.findById(id).get();
    }

    /**增加*/
    public void save(Spit spit){
        spit.set_id(idWorker.nextId()+"");
        //用户不用填的数据需要我们自己做初始化
        spit.setPublishtime(new Date());//发布日期
        spit.setVisits(0);//浏览量
        spit.setShare(0);//分享数
        spit.setThumbup(0);//点赞数
        spit.setComment(0);//回复数
        spit.setState("1");//状态

        //特殊情况：如果当前添加的吐槽有父节点，那么父节点的回复数也要加一
        if(spit.getParentid()!=null&&!"".equals(spit.getParentid())){
            Query query=new Query();
            query.addCriteria(Criteria.where("_id").is(spit.getParentid())); //对应_id:"1"
            Update update=new Update();
            update.inc("comment",1);  //对应 $inc:{comment:NumberInt(1)} 回复数加1
            mongoTemplate.updateFirst(query,update,"spit");
        }

        spitDao.save(spit);
    }


    /**更新*/
    public void update(Spit spit){
        spitDao.save(spit);
    }


    /**删除*/
    public void delete(String id){
        spitDao.deleteById(id);
    }

    /**根据上级id进行分页查询*/
    public Page<Spit> findByParentid(String parentid,int page,int size){
        //利用springData中为我们提供的类来封装分页参数
        Pageable pageable= PageRequest.of(page-1,size);
        return spitDao.findByParentid(parentid,pageable);
    }

    /**点赞操作：使用mongoTemplate来操作*/
    public void thumbup(String spitId){
        //点赞数加一：让表中的thumup字段的值+1（使用原生的mongo命令）: db.spit.update({_id:"1",{$inc:{thumbup:NumberInt(1)}}})

        Query query=new Query();
        query.addCriteria(Criteria.where("_id").is(spitId)); //对应_id:"1"
        Update update=new Update();
        update.inc("thumbup",1);  //对应 $inc:{thumbup:NumberInt(1)}
        mongoTemplate.updateFirst(query,update,"spit");//参数1：条件；参数2：具体操作；参数3：要操作的数据库中的集合
    }
}
