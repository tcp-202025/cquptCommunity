package cn.cquptCommunity.base.service.impl;

import cn.cquptCommunity.base.dao.ILabelDao;
import cn.cquptCommunity.base.pojo.Label;
import cn.cquptCommunity.base.service.ILabelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import util.IdWorker;

import java.util.List;

@Service
@Transactional
public class LabelService implements ILabelService {

    @Autowired
    private ILabelDao labelDao;

    @Autowired
    private IdWorker idWorker;//ID生成器

    /**查询全部*/
    public List<Label> findAll(){
        return labelDao.findAll();
    }

    /**根据id查询*/
    public Label findById(String id){
        return labelDao.findById(id).get();
    }

    /**保存操作*/
    public void save(Label label){
        label.setId(idWorker.nextId()+"");
        labelDao.save(label);
    }

    /**更新操作*/
    public void update(Label label){
        labelDao.save(label);//save方法：传过来的值有id是更新，无id是保存
    }

    /**删除操作*/
    public void delete(String id){
        labelDao.deleteById(id);
    }
}
