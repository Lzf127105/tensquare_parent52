package com.tensquare.spit.service;

import com.tensquare.spit.dao.SpitDao;
import com.tensquare.spit.pojo.Spit;
import org.springframework.beans.factory.annotation.Autowired;
import util.IdWorker;

import java.util.List;

/**
 * @author Lzf
 * @create 2018-12-28 17:09
 * @Description:
 */
public class SpitService {

    @Autowired
    private SpitDao spitDao;

    @Autowired
    private IdWorker idWorker;

    /**
     * 查询全部记录
     * @return
     */
    public List<Spit> findAll() {
        return spitDao.findAll();
    }

    /**
     * 根据主键查询实体
     * @param id
     * @return
     */
    public Spit findById(String id) {
        Spit spit = spitDao.findById(id).get();
        return spit;
    }

    /**
     * 增加
     * @param spit
     */
    public void add(Spit spit) {
        spit.set_id(idWorker.nextId() + ""); //主键值
        spitDao.save(spit);
    }

    /**
     * 修改
     * @param spit
     */

    public void update(Spit spit) {
        spitDao.save(spit);
    }

    /**
     * 删除
     * @param id
     */
    public void deleteById(String id) {
        spitDao.deleteById(id);
    }
}