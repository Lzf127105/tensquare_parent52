package com.tensquare.friend.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;

/**
 * @author Lzf
 * @create 2019-01-14 17:14
 * @Description:
 */
@FeignClient("tensquare-user")
public interface UserClient {
    @PutMapping(value = "/user/{userid}/{friendid}/{flag}")
    public void updateFanscountAndFollowCount(@PathVariable("userid") String userid, @PathVariable("friendid") String friendid, @PathVariable("flag") int flag);
}
