package com.tensquare.user.service;

import com.tensquare.user.dao.UserDao;
import com.tensquare.user.pojo.User;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import util.IdWorker;
import util.JwtUtil;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * 服务层
 * 
 * @author Administrator
 *
 */
@Service
@Transactional
public class UserService {

	@Autowired
	private UserDao userDao;
	
	@Autowired
	private IdWorker idWorker;

	@Autowired
	private RedisTemplate redisTemplate;

	@Autowired
	private RabbitTemplate rabbitTemplate; //暂时注释了，不加人消息队列，在控制台看就可以了

	@Autowired
	private BCryptPasswordEncoder encoder;

	@Autowired
	private HttpServletRequest request;

	@Autowired
	private JwtUtil jwtUtil;

	/**
	 * http://192.168.78.128:15672 (rabbit 查看消息)
	 * 大致流程：1、用户点击获取验证码（后台随机产生一条6位数的验证码，发送到消息队列（缓存中也存放一条））
	 * 2、而在消息队列那边（MQ）,有个专门消费短信验证码的。（只要队列中有消息，就会将验证码发到填写的号码上去）
	 * 3、注册，对比验证码（用户填写的验证码 和 redis中存的验证码 对比）
	 * @param mobile
	 */
	public void sendSms(String mobile){
		//生成六位数字随机数
		String checkCode = RandomStringUtils.randomNumeric(6);
		//向缓存放一份
		redisTemplate.opsForValue().set("checkCode_"+mobile,checkCode,6, TimeUnit.HOURS);
		//给用户发一份
		Map<Object, Object> map = new HashMap<>();
		map.put("mobile",mobile);
		map.put("checkCode",checkCode);
		//rabbitTemplate.convertAndSend("sms",map);
		//在控制台显示一份（方便测试）
		System.out.println("验证码为："+checkCode);
	}

	/**
	 * 查询全部列表
	 * @return
	 */
	public List<User> findAll() {
		return userDao.findAll();
	}

	
	/**
	 * 条件查询+分页
	 * @param whereMap
	 * @param page
	 * @param size
	 * @return
	 */
	public Page<User> findSearch(Map whereMap, int page, int size) {
		Specification<User> specification = createSpecification(whereMap);
		PageRequest pageRequest =  PageRequest.of(page-1, size);
		return userDao.findAll(specification, pageRequest);
	}

	
	/**
	 * 条件查询
	 * @param whereMap
	 * @return
	 */
	public List<User> findSearch(Map whereMap) {
		Specification<User> specification = createSpecification(whereMap);
		return userDao.findAll(specification);
	}

	/**
	 * 根据ID查询实体
	 * @param id
	 * @return
	 */
	public User findById(String id) {
		return userDao.findById(id).get();
	}

	/**
	 * 增加
	 * @param user
	 */
	public void add(User user) {
		user.setId( idWorker.nextId()+"" );
		user.setPassword(encoder.encode(user.getPassword()));
		user.setFollowcount(0);//关注数        
		user.setFanscount(0);//粉丝数        
		user.setOnline(0L);//在线时长        
		user.setRegdate(new Date());//注册日期        
		user.setUpdatedate(new Date());//更新日期        
		user.setLastdate(new Date());//最后登陆日期
		userDao.save(user);
	}

	/**
	 * 修改
	 * @param user
	 */
	public void update(User user) {
		userDao.save(user);
	}

	/**
	 * 删除
	 * @param id
	 * 必须是admin角色才能删除（现在写死）
	 * 传参: id和{key:Authorization   values:Bearer eyJhbGciOiJIUzI1NiJ9.eyJqdGkiOiIx....}
	 * 获取的参数：  header = "Bearer eyJhbGciOiJIUzI1NiJ9.eyJqdGkiOiIxMDgzMjU1ODI..."
	 */
	public void deleteById(String id) {
		//提取前
		/*String header = request.getHeader("Authorization");//获取请求头信息
		if(header == null || "".equals(header)){
			throw new RuntimeException("权限不足");
			//return new Result(false, StatusCode.ACCESSERROR,"权限不足"); 这个写在了controller层
		}
		if(!header.startsWith("Bearer ")){  //前后端在header约定的,startsWith获取的是Bearer （固定写法）
			throw new RuntimeException("权限不足");
		}
		try {
			String token = header.substring(7);//提取token
			Claims claims = jwtUtil.parseJWT(token);
			String roles = (String) claims.get("roles");
			if (roles == null || !roles.equals("admin")){
				throw new RuntimeException("权限不足");
			}
		}catch (Exception e){
			throw new RuntimeException("权限不足");
		}*/
		//提取后的调用
		String token = (String) request.getAttribute("claims_admin");
		if(token == null || "".equals(token)){
			throw new RuntimeException("权限不足！");
		}
		userDao.deleteById(id);
	}

	/**
	 * 动态条件构建
	 * @param searchMap
	 * @return
	 */
	private Specification<User> createSpecification(Map searchMap) {

		return new Specification<User>() {

			@Override
			public Predicate toPredicate(Root<User> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				List<Predicate> predicateList = new ArrayList<Predicate>();
                // ID
                if (searchMap.get("id")!=null && !"".equals(searchMap.get("id"))) {
                	predicateList.add(cb.like(root.get("id").as(String.class), "%"+(String)searchMap.get("id")+"%"));
                }
                // 手机号码
                if (searchMap.get("mobile")!=null && !"".equals(searchMap.get("mobile"))) {
                	predicateList.add(cb.like(root.get("mobile").as(String.class), "%"+(String)searchMap.get("mobile")+"%"));
                }
                // 密码
                if (searchMap.get("password")!=null && !"".equals(searchMap.get("password"))) {
                	predicateList.add(cb.like(root.get("password").as(String.class), "%"+(String)searchMap.get("password")+"%"));
                }
                // 昵称
                if (searchMap.get("nickname")!=null && !"".equals(searchMap.get("nickname"))) {
                	predicateList.add(cb.like(root.get("nickname").as(String.class), "%"+(String)searchMap.get("nickname")+"%"));
                }
                // 性别
                if (searchMap.get("sex")!=null && !"".equals(searchMap.get("sex"))) {
                	predicateList.add(cb.like(root.get("sex").as(String.class), "%"+(String)searchMap.get("sex")+"%"));
                }
                // 头像
                if (searchMap.get("avatar")!=null && !"".equals(searchMap.get("avatar"))) {
                	predicateList.add(cb.like(root.get("avatar").as(String.class), "%"+(String)searchMap.get("avatar")+"%"));
                }
                // E-Mail
                if (searchMap.get("email")!=null && !"".equals(searchMap.get("email"))) {
                	predicateList.add(cb.like(root.get("email").as(String.class), "%"+(String)searchMap.get("email")+"%"));
                }
                // 兴趣
                if (searchMap.get("interest")!=null && !"".equals(searchMap.get("interest"))) {
                	predicateList.add(cb.like(root.get("interest").as(String.class), "%"+(String)searchMap.get("interest")+"%"));
                }
                // 个性
                if (searchMap.get("personality")!=null && !"".equals(searchMap.get("personality"))) {
                	predicateList.add(cb.like(root.get("personality").as(String.class), "%"+(String)searchMap.get("personality")+"%"));
                }
				
				return cb.and( predicateList.toArray(new Predicate[predicateList.size()]));

			}
		};

	}

    public User login(String mobile, String password) {
		User user = userDao.findByMobile(mobile);
		if(user != null && encoder.matches(password,user.getPassword())){
			return user;
		}
		return null;
    }

    public void updateFanscountAndFollowCount(int flag, String userid, String friendid) {
		userDao.updateFanscount(flag,friendid);
		userDao.updateFollowCount(flag,userid);
    }
}
