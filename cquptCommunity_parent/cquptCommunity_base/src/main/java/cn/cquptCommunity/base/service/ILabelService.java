package cn.cquptCommunity.base.service;

import cn.cquptCommunity.base.pojo.Label;

import java.util.List;

public interface ILabelService {
    /**查询全部*/
    public List<Label> findAll();

    /**根据id查询*/
    public Label findById(String id);

    /**保存操作*/
    public void save(Label label);

    /**更新操作*/
    public void update(Label label);

    /**删除操作*/
    public void delete(String id);

}
