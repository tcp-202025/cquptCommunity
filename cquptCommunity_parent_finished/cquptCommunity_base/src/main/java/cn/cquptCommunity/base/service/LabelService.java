package cn.cquptCommunity.base.service;

import cn.cquptCommunity.base.dao.LabelDao;
import cn.cquptCommunity.base.pojo.Label;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import util.IdWorker;

import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.List;

/**
 * 标签模块的业务层
 */
@Service
@Transactional
public class LabelService{

    @Autowired
    private LabelDao labelDao;

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
        //对新添加的数据做一些初始化
        label.setCount(0l);//新添加的数据的使用数肯定为0
        label.setFans(0l);//新添加的数据的粉丝数肯定为0
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



    /**根据条件查询*/
    public List<Label> findSearch(Label label) {
        Specification<Label> specification=new Specification<Label>() {
            /**
             * root:获取比较的属性
             * criteriaBuilder:封装条件
             */
            @Override
            public Predicate toPredicate(Root<Label> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                //使用一个集合来存放所有的条件
                List<Predicate> list=new ArrayList<>();
                //1.根据名称条件
                if (label.getLabelname() != null && !label.getLabelname().equals("")) {
                    //1.获取比较的属性
                    Path<Object> labelname = root.get("labelname");
                    //2.构造查询
                    /**
                     * equal方法:直接的到path对象 (属性)，然后进行比较即可
                     * gt,lt,ge,le,like :得到path对象，根据path指定比较的参数类型（path. as(类型的字节码对象)），再去进行比较
                     */
                    Predicate predicate1 = criteriaBuilder.like(labelname.as(String.class), "%" + label.getLabelname() + "%");
                    list.add(predicate1);
                }
                //2.根据状态条件
                if (label.getState() != null && !label.getState().equals("")) {
                    Predicate predicate2 = criteriaBuilder.equal(root.get("state"), label.getState());
                    list.add(predicate2);
                }

                //3.根据是否推荐来查询
                if (label.getRecommend() != null && !label.getRecommend().equals("")) {
                    Predicate predicate3 = criteriaBuilder.equal(root.get("recommend"), label.getRecommend());
                    list.add(predicate3);
                }


                Predicate[] parr=new Predicate[list.size()];
                list.toArray(parr);//将list转为数组
                return criteriaBuilder.and(parr);
            }
        };

        return labelDao.findAll(specification);//传入条件进行查询
    }


    /**
     * 分页查询
     * page:第几页
     * size:每页显示的条数
     */
    public Page<Label> pageQuery(Label label, int page, int size) {
        Specification<Label> specification=new Specification<Label>() {
            /**
             * root:获取比较的属性
             * criteriaBuilder:封装条件
             */
            @Override
            public Predicate toPredicate(Root<Label> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                //使用一个集合来存放所有的条件
                List<Predicate> list=new ArrayList<>();
                //1.根据名称条件
                if (label.getLabelname() != null && !label.getLabelname().equals("")) {
                    //1.获取比较的属性
                    Path<Object> labelname = root.get("labelname");
                    //2.构造查询
                    /**
                     * equal方法:直接的到path对象 (属性)，然后进行比较即可
                     * gt,lt,ge,le,like :得到path对象，根据path指定比较的参数类型（path. as(类型的字节码对象)），再去进行比较
                     */
                    Predicate predicate1 = criteriaBuilder.like(labelname.as(String.class), "%" + label.getLabelname() + "%");
                    list.add(predicate1);
                }
                //2.根据状态条件
                if (label.getState() != null && !label.getState().equals("")) {
                    Predicate predicate2 = criteriaBuilder.equal(root.get("state"), label.getState());
                    list.add(predicate2);
                }

                //3.根据是否推荐来查询
                if (label.getRecommend() != null && !label.getRecommend().equals("")) {
                    Predicate predicate3 = criteriaBuilder.equal(root.get("recommend"), label.getRecommend());
                    list.add(predicate3);
                }


                Predicate[] parr=new Predicate[list.size()];
                list.toArray(parr);//将list转为数组
                return criteriaBuilder.and(parr);
            }
        };

        //封装一个分页参数
        Pageable pageable= PageRequest.of(page-1,size);//PageRequest方法中的页的索引是从0开始，所以需要减一

        return labelDao.findAll(specification,pageable);//传入查询条件和分页参数进行查询
    }


    /**
     * 查询推荐标签列表
     */
    public List<Label> findTopList() {
        return labelDao.findTopList();
    }

    /**
     * 查询有效标签列表
     */
    public List<Label> findEffective() {
        return labelDao.findEffective();
    }
}
