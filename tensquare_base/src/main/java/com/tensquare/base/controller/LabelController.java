package com.tensquare.base.controller;

import com.tensquare.base.pojo.Label;
import com.tensquare.base.service.LabelService;
import entity.PageResult;
import entity.Result;
import entity.StatusCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author Lzf
 * @create 2018-12-25 10:47
 * @Description:
 */
@CrossOrigin
@RestController
@RequestMapping(value = "/label")
public class LabelController {

    @Autowired
    private LabelService labelService;

    @RequestMapping(method = RequestMethod.GET)
    public Result findAll(){
        return  new Result(true, StatusCode.OK ,"查询成功！",labelService.findAll());
    }

    @RequestMapping(value = "{labelId}", method = RequestMethod.GET)
    public Result findById(@PathVariable("labelId") String labelId){
        System.out.println("11111111");
        return  new Result(true, StatusCode.OK ,"查询成功！",labelService.findById(labelId));
    }

    /**
     * @param label
     * @return 前端传json格式数据，要转成对象,也可以转成map
     */
    @RequestMapping(method = RequestMethod.POST)
    public Result save(@RequestBody Label label){
        labelService.save(label);
        return  new Result(true, StatusCode.OK ,"保存成功！");
    }

    @RequestMapping(value = "/{labelId}",method = RequestMethod.DELETE)
    public Result deleteById(@PathVariable String labelId){
        labelService.deleteById(labelId);
        return  new Result(true, StatusCode.OK ,"删除成功！");
    }

    @RequestMapping(value = "/{labelId}", method = RequestMethod.PUT)
    public Result update(@PathVariable String labelId,@RequestBody Label label){
        label.setId(labelId);
        labelService.update(label);
        return  new Result(true, StatusCode.OK ,"修改成功！");
    }

    @RequestMapping(value = "/search",method = RequestMethod.POST)
    public Result findSeach(@RequestBody Label label){
        List<Label> list = labelService.findSeach(label);
        return new Result(true,StatusCode.OK,"查询成功！",list);
    }

    @RequestMapping(value = "/search/{page}/{size}",method = RequestMethod.POST)
    public Result pageQuery(@RequestBody Label label,@PathVariable int page,@PathVariable int size){
        Page<Label> pageQuery = labelService.pageQuery(label,page,size);
        return new Result(true,StatusCode.OK,"查询成功！",new PageResult<Label>(pageQuery.getTotalElements(),pageQuery.getContent()));
    }
}
