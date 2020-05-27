package cn.cquptCommunity.article.service;

import java.util.*;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import cn.cquptCommunity.article.client.UserClient;
import cn.cquptCommunity.article.pojo.Channel;
import cn.cquptCommunity.article.pojo.Column;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import org.springframework.transaction.annotation.Transactional;
import util.IdWorker;

import cn.cquptCommunity.article.dao.ArticleDao;
import cn.cquptCommunity.article.pojo.Article;

/**
 * 文章模块的业务层
 */
@Service
@Transactional
public class ArticleService {

	@Autowired
	private ColumnService columnService;//专栏模块的业务层

	@Autowired
	private ChannelService channelService;//频道模块的业务层

	@Autowired
	private ArticleDao articleDao;
	
	@Autowired
	private IdWorker idWorker;

	@Autowired
	private RedisTemplate redisTemplate;

	@Autowired
	private UserClient userClient;

	/**
	 * 审核文章：id为文章的序号，根据id去修改文章的审核状态
	 */
	public void updateState(String id){
		articleDao.updateState(id);
	}

	/**
	 * 文章点赞，根据id修改文章的点赞数
	 */
	public void addThumbup(String id){
		articleDao.addThumbup(id);
	}

	/**
	 * 取消当前用户对文章的点赞
	 */
	public void deleteThumbup(String articleId) {
		articleDao.deleteThumbup(articleId);
	}

	/**
	 * 根据id修改文章的访问数
	 */
	public void updateVisits(String id) {
		articleDao.addVisits(id);
	}


	/**
	 * 查询全部列表
	 * @return
	 */
	public List<Article> findAll() {
		return articleDao.findAll();
	}

	
	/**
	 * 条件查询+分页
	 * @param whereMap
	 * @param page
	 * @param size
	 * @return
	 */
	public Page<Article> findSearch(Map whereMap, int page, int size) {
		Specification<Article> specification = createSpecification(whereMap);
		PageRequest pageRequest =  PageRequest.of(page-1, size);
		return articleDao.findAll(specification, pageRequest);
	}

	
	/**
	 * 条件查询
	 * @param whereMap
	 * @return
	 */
	public List<Article> findSearch(Map whereMap) {
		Specification<Article> specification = createSpecification(whereMap);
		return articleDao.findAll(specification);
	}

	/**
	 * 根据ID查询实体
	 * @return
	 */
	public Article findById(String articleid) {
		//使用Redis优化：先从缓存中查询当前对象
		Article article =(Article) redisTemplate.opsForValue().get("article_" + articleid);
		if(article==null){
			//如果redis中没有,则从数据库中查询
			article=articleDao.findById(articleid).get();
			//存入缓存
			redisTemplate.opsForValue().set("article_" + articleid,article);
		}
		return article;
	}

	/**
	 * 增加
	 * @param article
	 */
	public void add(Article article,String userid) {
		article.setId( idWorker.nextId()+"" );
		//添加新文章的时候需要对一些数据初始化
		//查询专栏id，然后为文章随机分配一个
		List<Column> columnList = columnService.findAll();
		List<String> columnIds=new ArrayList<>();
		for (Column column : columnList) {
			String columnId = column.getId();//拿到专栏id
			columnIds.add(columnId);
		}
		int columnIndex=new Random().nextInt(columnIds.size());
		article.setColumnid(columnIds.get(columnIndex));//为文章分配专栏

		//查询频道id，为文章随机分配一个
		List<Channel> channelList=channelService.findAll();
		List<String> channelIds=new ArrayList<>();
		for (Channel channel : channelList) {
			String channelId = channel.getId();
			channelIds.add(channelId);
		}
		int channelIndex=new Random().nextInt(channelIds.size());
		article.setChannelid(channelIds.get(channelIndex));

		//根据userid查询文章发布者名称
		String nickname = userClient.findNickname(userid);
		article.setNickname(nickname);
		article.setUserid(userid);
		article.setIspublic("1");//文章公开
		article.setIstop("0");//初始的时候文章不置顶
		article.setVisits(0);//初始访问数为0
		article.setThumbup(0);//初始点赞数为0
		article.setComment(0);//初始评论数为0
		article.setState("0");//初始审核状态
		article.setCreatetime(new Date());
		articleDao.save(article);
	}

	/**
	 * 修改:修改完成之后要清除redis缓存中对应的id的数据
	 * @param article
	 */
	public void update(Article article) {
		redisTemplate.delete("article_"+article.getId());
		article.setUpdatetime(new Date());
		articleDao.save(article);
	}

	/**
	 * 删除:删除数据库的数据之后，也要删除redis中对应的缓存
	 * @param id
	 */
	public void deleteById(String id) {
		redisTemplate.delete("article_"+id);
		articleDao.deleteById(id);
	}

	/**
	 * 动态条件构建
	 * @param searchMap
	 * @return
	 */
	private Specification<Article> createSpecification(Map searchMap) {

		return new Specification<Article>() {

			@Override
			public Predicate toPredicate(Root<Article> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				List<Predicate> predicateList = new ArrayList<Predicate>();
                // ID
                if (searchMap.get("id")!=null && !"".equals(searchMap.get("id"))) {
                	predicateList.add(cb.like(root.get("id").as(String.class), "%"+(String)searchMap.get("id")+"%"));
                }
                // 专栏ID
                if (searchMap.get("columnid")!=null && !"".equals(searchMap.get("columnid"))) {
                	predicateList.add(cb.like(root.get("columnid").as(String.class), "%"+(String)searchMap.get("columnid")+"%"));
                }
                // 用户ID
                if (searchMap.get("userid")!=null && !"".equals(searchMap.get("userid"))) {
                	predicateList.add(cb.like(root.get("userid").as(String.class), "%"+(String)searchMap.get("userid")+"%"));
                }
                // 标题
                if (searchMap.get("title")!=null && !"".equals(searchMap.get("title"))) {
                	predicateList.add(cb.like(root.get("title").as(String.class), "%"+(String)searchMap.get("title")+"%"));
                }
                // 文章正文
                if (searchMap.get("content")!=null && !"".equals(searchMap.get("content"))) {
                	predicateList.add(cb.like(root.get("content").as(String.class), "%"+(String)searchMap.get("content")+"%"));
                }
                // 文章封面
                if (searchMap.get("image")!=null && !"".equals(searchMap.get("image"))) {
                	predicateList.add(cb.like(root.get("image").as(String.class), "%"+(String)searchMap.get("image")+"%"));
                }
                // 是否公开
                if (searchMap.get("ispublic")!=null && !"".equals(searchMap.get("ispublic"))) {
                	predicateList.add(cb.like(root.get("ispublic").as(String.class), "%"+(String)searchMap.get("ispublic")+"%"));
                }
                // 是否置顶
                if (searchMap.get("istop")!=null && !"".equals(searchMap.get("istop"))) {
                	predicateList.add(cb.like(root.get("istop").as(String.class), "%"+(String)searchMap.get("istop")+"%"));
                }
                // 审核状态
                if (searchMap.get("state")!=null && !"".equals(searchMap.get("state"))) {
                	predicateList.add(cb.like(root.get("state").as(String.class), "%"+(String)searchMap.get("state")+"%"));
                }
                // 所属频道
                if (searchMap.get("channelid")!=null && !"".equals(searchMap.get("channelid"))) {
                	predicateList.add(cb.like(root.get("channelid").as(String.class), "%"+(String)searchMap.get("channelid")+"%"));
                }
                // URL
                if (searchMap.get("url")!=null && !"".equals(searchMap.get("url"))) {
                	predicateList.add(cb.like(root.get("url").as(String.class), "%"+(String)searchMap.get("url")+"%"));
                }
                // 类型
                if (searchMap.get("type")!=null && !"".equals(searchMap.get("type"))) {
                	predicateList.add(cb.like(root.get("type").as(String.class), "%"+(String)searchMap.get("type")+"%"));
                }
				
				return cb.and( predicateList.toArray(new Predicate[predicateList.size()]));

			}
		};

	}

	/**
	 * 根据频道ID获取文章列表
	 */
	public Page<Article> findByChannelId(String channelId, int page, int size) {
		Pageable pageable=PageRequest.of(page-1,size);//封装分页参数，因为该方法中页面参数的索引是从0开始的，所以需要page-1
		return articleDao.findByChannelid(channelId,pageable);
	}

	/**
	 * 根据专栏ID获取文章列表
	 */
	public Page<Article> findByColumnId(String columnId, int page, int size) {
		Pageable pageable=PageRequest.of(page-1,size);//封装分页参数，因为该方法中页面参数的索引是从0开始的，所以需要page-1
		return articleDao.findByColumnid(columnId,pageable);
	}

	/**
	 * 查询头条文章:文章表中有一个istop字段，为1则表示该文章置顶
	 */
	public List<Article> findTop() {
		return articleDao.findTop();
	}

	/**
	 * 查询我的文章
	 */
	public List<Article> findMyArticle(String userid) {
		return articleDao.findByUserid(userid);
	}

	/**
	 * 根据nickname查询该作者的所有文章
	 */
    public List<Article> findByNickname(String nickname) {
    	return articleDao.findByNickname(nickname);
    }

	/**
	 * 修改文章置顶
	 */
	public void updateIsTop(String articleid) {
		articleDao.updateIsTop(articleid);
	}

	/**
	 * 申请文章是否公开
	 */
	public void updateIsPublic(String articleid) {
		articleDao.updateIsPublic(articleid);
	}
}
