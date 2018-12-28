package com.tensquare.spit.dao;

import com.tensquare.spit.pojo.Spit;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * @author Lzf
 * @create 2018-12-28 17:07
 * @Description:
 */
public interface SpitDao extends MongoRepository<Spit,String>{

}
