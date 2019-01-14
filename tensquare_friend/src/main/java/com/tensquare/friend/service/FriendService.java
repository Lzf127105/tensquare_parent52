package com.tensquare.friend.service;

import com.tensquare.friend.dao.FriendDao;
import com.tensquare.friend.dao.NoFriendDao;
import com.tensquare.friend.pojo.Friend;
import com.tensquare.friend.pojo.NoFriend;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

/**
 * @author Lzf
 * @create 2019-01-14 9:59
 * @Description:
 */
@Service
@Transactional
public class FriendService {

    @Autowired
    private FriendDao friendDao;

    @Autowired
    private NoFriendDao noFriendDao;

    public int addFriend(String userid, String friendid) {
        //先判断userId到friendId是否有数据，有就是重复添加，返回“0”
        Friend friend = friendDao.findByUseridAndFriendid(userid, friendid);
        if(friend != null){
            return  0;
        }
        //直接添加好友，让好友表中userId到friendId方向的type为“0”
        //if(friend == null){}
        friend = new Friend();
        friend.setUserid(userid);
        friend.setFriendid(friendid);
        friend.setIslike("0");
        friendDao.save(friend);
        //判断从friendId到userId是否有有数据，如果都有，把双方的状态都改为“1”(friendid 和 userid 位置调换)
        if(friendDao.findByUseridAndFriendid(friendid, userid) != null){
            friendDao.updateByLike("1",userid,friendid);
            friendDao.updateByLike("1",friendid,userid);
        }
        return 1;
    }

    public int addNoFriend(String userid, String friendid) {
        NoFriend nofriend = noFriendDao.findByUseridAndFriendid(userid, friendid);
        if(nofriend != null){
            return  0;
        }
        nofriend = new NoFriend();
        nofriend.setUserid(userid);
        nofriend.setFriendid(friendid);
        noFriendDao.save(nofriend);

        return 1;
    }

    public void deleteFriend(String userid, String friendid) {
        //删除还有表中userid到friendid这条数据
        friendDao.deleteFriend(userid,friendid);
        //更新friendid到userid的islike为0
        friendDao.updateByLike("0",friendid,userid);
        //非好友表中添加数据
        NoFriend noFriend = new NoFriend();
        noFriend.setUserid(userid);
        noFriend.setFriendid(friendid);
        noFriendDao.save(noFriend);
    }
}
