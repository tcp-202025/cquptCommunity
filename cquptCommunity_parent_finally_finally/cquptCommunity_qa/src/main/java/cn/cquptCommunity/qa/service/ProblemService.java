package cn.cquptCommunity.qa.service;

import java.util.*;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.Selection;

import cn.cquptCommunity.qa.client.UserClient;
import cn.cquptCommunity.qa.dao.PLDao;
import cn.cquptCommunity.qa.pojo.PL;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import org.springframework.transaction.annotation.Transactional;
import util.IdWorker;

import cn.cquptCommunity.qa.dao.ProblemDao;
import cn.cquptCommunity.qa.pojo.Problem;

/**
 * 问题模块的业务层
 */
@Service
@Transactional
public class ProblemService {

	@Autowired
	private ProblemDao problemDao;

	@Autowired
	private PLDao plDao;
	
	@Autowired
	private IdWorker idWorker;

	@Autowired
	private UserClient userClient;//调用user微服务模块

	/**
	 *分页查询最新问答
	 */
	public Page<Problem> newList(String labelid,int page,int size){
		Pageable pageable=PageRequest.of(page-1,size); //利用spring提供的类来封装分页参数,因为springdatajpa中页码的索引从0开始，所以要减一
		return problemDao.newList(labelid,pageable);
	}

	/**
	 *分页查询最热门问答
	 */
	public Page<Problem> hotList(String labelid,int page,int size){
		Pageable pageable=PageRequest.of(page-1,size); //利用spring提供的类来封装分页参数
		return problemDao.hotList(labelid,pageable);
	}


	/**
	 *分页查询等待中的问答
	 */
	public Page<Problem> waitList(String labelid,int page,int size){
		Pageable pageable=PageRequest.of(page-1,size); //利用spring提供的类来封装分页参数
		return problemDao.waitList(labelid,pageable);
	}

	/**
	 *根据标签id查询属于问题
	 */
	public Page<Problem> findAllByLabel(String labelid, int page, int size) {
		Pageable pageable=PageRequest.of(page-1,size); //利用spring提供的类来封装分页参数
		return problemDao.findAllByLabel(labelid,pageable);
	}


	/**
	 * 查询全部列表
	 * @return
	 */
	public List<Problem> findAll() {
		return problemDao.findAll();
	}

	
	/**
	 * 条件查询+分页
	 * @param whereMap
	 * @param page
	 * @param size
	 * @return
	 */
	public Page<Problem> findSearch(Map whereMap, int page, int size) {
		Specification<Problem> specification = createSpecification(whereMap);
		PageRequest pageRequest =  PageRequest.of(page-1, size);
		return problemDao.findAll(specification, pageRequest);
	}

	
	/**
	 * 条件查询
	 * @param whereMap
	 * @return
	 */
	public List<Problem> findSearch(Map whereMap) {
		Specification<Problem> specification = createSpecification(whereMap);
		return problemDao.findAll(specification);
	}

	/**
	 * 根据ID查询实体
	 * @param id
	 * @return
	 */
	public Problem findById(String id) {
		return problemDao.findById(id).get();
	}

	/**
	 * 增加
	 * @param problem
	 */
	public void add(Problem problem,String userid,String labelid) {
		String problemid = idWorker.nextId() + "";
		problem.setId(problemid);
		//对一些参数做初始化
		String nickname = userClient.findNickname(userid);//根据用户id查询出昵称
		problem.setUserid(userid);
		problem.setNickname(nickname);
		problem.setCreatetime(new Date());
		problem.setVisits(0l);//初始访问量为0
		problem.setThumbup(0l);//初始点赞数为0
		problem.setReply(0l);//初始回复数为0
		problem.setSolve("0");//0表示该问题没有回答
		problem.setReplyname(null);
		problem.setReplytime(null);
		problemDao.save(problem);
		//向标签-问题表中增加数据
		PL pl=new PL();
		pl.setProblemid(problemid);
		pl.setLabelid(labelid);
		plDao.save(pl);
	}

	/**
	 * 修改
	 * @param problem
	 */
	public void update(Problem problem) {
		problem.setUpdatetime(new Date());
		problemDao.save(problem);
	}

	/**
	 * 删除
	 * @param id
	 */
	public void deleteById(String id) {
		problemDao.deleteById(id);
	}

	/**
	 * 动态条件构建
	 * @param searchMap
	 * @return
	 */
	private Specification<Problem> createSpecification(Map searchMap) {

		return new Specification<Problem>() {

			@Override
			public Predicate toPredicate(Root<Problem> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				List<Predicate> predicateList = new ArrayList<Predicate>();
                // ID
                if (searchMap.get("id")!=null && !"".equals(searchMap.get("id"))) {
                	predicateList.add(cb.like(root.get("id").as(String.class), "%"+(String)searchMap.get("id")+"%"));
                }
                // 标题
                if (searchMap.get("title")!=null && !"".equals(searchMap.get("title"))) {
                	predicateList.add(cb.like(root.get("title").as(String.class), "%"+(String)searchMap.get("title")+"%"));
                }
                // 内容
                if (searchMap.get("content")!=null && !"".equals(searchMap.get("content"))) {
                	predicateList.add(cb.like(root.get("content").as(String.class), "%"+(String)searchMap.get("content")+"%"));
                }
                // 用户ID
                if (searchMap.get("userid")!=null && !"".equals(searchMap.get("userid"))) {
                	predicateList.add(cb.like(root.get("userid").as(String.class), "%"+(String)searchMap.get("userid")+"%"));
                }
                // 昵称
                if (searchMap.get("nickname")!=null && !"".equals(searchMap.get("nickname"))) {
                	predicateList.add(cb.like(root.get("nickname").as(String.class), "%"+(String)searchMap.get("nickname")+"%"));
                }
                // 是否解决
                if (searchMap.get("solve")!=null && !"".equals(searchMap.get("solve"))) {
                	predicateList.add(cb.like(root.get("solve").as(String.class), "%"+(String)searchMap.get("solve")+"%"));
                }
                // 回复人昵称
                if (searchMap.get("replyname")!=null && !"".equals(searchMap.get("replyname"))) {
                	predicateList.add(cb.like(root.get("replyname").as(String.class), "%"+(String)searchMap.get("replyname")+"%"));
                }
				
				return cb.and( predicateList.toArray(new Predicate[predicateList.size()]));

			}
		};

	}

	/**
	 * 查询我(当前登录用户)提问了哪些问题
	 */
    public List<Problem> findMyProblem(String userid) {
    	return problemDao.findByUserid(userid);
    }

	/**
	 * 根据用户的昵称查出他提问了哪些问题
	 */
	public List<Problem> findByNickName(String nickname) {
		return problemDao.findByNickname(nickname);
	}

	/**
	 * 更新访问数
	 */
	public void updateVisits(String problemId){
		problemDao.updateProblemVisits(problemId);
	}

	/**
	 * 更新点赞数
	 */
	public void addThumbup(String problemId){
		problemDao.addProblemThumbup(problemId);
	}

	/**
	 * 取消点赞
	 */
	public void deleteThumbup(String problemId){
		problemDao.deleteProblemThumbup(problemId);
	}

}
