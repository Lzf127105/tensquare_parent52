package com.tensquare.friend.controller;

import com.tensquare.friend.client.UserClient;
import com.tensquare.friend.service.FriendService;
import entity.Result;
import entity.StatusCode;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * @author Lzf
 * @create 2019-01-14 10:02
 * @Description: 交友
 */
@RestController
@RequestMapping("/friend")
public class FriendController {
    @Autowired
    private HttpServletRequest request;

    @Autowired
    private FriendService friendService;

    @Autowired
    private UserClient userClient;

    /**
     * 删除喜欢
     */
    @DeleteMapping(value = "/{friendid}")
    public Result deleteFriend(@PathVariable String friendid){
        //验证是否登录，并且拿到当前登录的用户Id
        Claims claims = (Claims) request.getAttribute("claims_user");
        String userid = claims.getId();
        if (claims == null) {
            return new Result(false, StatusCode.ACCESSERROR, "无权访问");
        }
        friendService.deleteFriend(userid,friendid);
        userClient.updateFanscountAndFollowCount(userid,friendid,-1);
        return new Result(true, StatusCode.OK, "删除成功");
    }

    /**
     * 添加好友和添加非好友
     * @param friendid 对方用户ID
     * @param type 1：喜欢  0：不喜欢
     * @return
     */
    @PutMapping(value = "/like/{friendid}/{type}")
    public Result addFriend(@PathVariable String friendid, @PathVariable String type) {
        //验证是否登录，并且拿到当前登录的用户Id
        Claims claims = (Claims) request.getAttribute("claims_user");
        String userid = claims.getId();
        if (claims == null) {
            return new Result(false, StatusCode.ACCESSERROR, "无权访问");
        }
        //判断是添加好友还是添加非好友
        if(type.equals("1")){
            //添加好友
            int flag = friendService.addFriend(userid,friendid);
            if(flag == 0){
                return new Result(false, StatusCode.REPERROR,"不能重复添加好友");
            }
            if(flag == 1){
                userClient.updateFanscountAndFollowCount(userid,friendid,1);
                return new Result(true, StatusCode.OK,"添加成功");
            }
        }else {
            //添加非好友,向不喜欢列表中添加记录
            int flag = friendService.addNoFriend(userid,friendid);
            if(flag == 0){
                return new Result(false, StatusCode.REPERROR,"不能重复添加非好友");
            }
            if(flag == 1){
                return new Result(true, StatusCode.OK,"添加成功");
            }
        }
        return new Result(true, StatusCode.OK, "操作成功");
    }
}
