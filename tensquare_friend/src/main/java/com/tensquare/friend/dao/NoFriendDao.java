package com.tensquare.friend.dao;

import com.tensquare.friend.pojo.NoFriend;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author Lzf
 * @create 2019-01-14 10:03
 * @Description: 数据访问层
 */
public interface NoFriendDao extends JpaRepository<NoFriend, String> {

    public NoFriend findByUseridAndFriendid(String userid, String friendid);

}
