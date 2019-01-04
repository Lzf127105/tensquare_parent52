package com.tensquare.search.controller;

import com.tensquare.search.pojo.Article;
import com.tensquare.search.service.ArticleSearchService;
import entity.PageResult;
import entity.Result;
import entity.StatusCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

/**
 * @author Lzf
 * @create 2018-12-29 17:03
 * @Description:
 */
@RestController
@RequestMapping("/article")
@CrossOrigin
public class ArticleSearchController {
    @Autowired
    private ArticleSearchService articleSearchService;

    @PostMapping
    public Result save(@RequestBody Article article) {
        articleSearchService.save(article);
        return new Result(true, StatusCode.OK, "添加成功");
    }

    /**
     * 分页+多条件查询
     */
    @GetMapping(value="/key/{page}/{size}")
    public Result findByKey(@RequestBody String key, @PathVariable int page, @PathVariable int size){
        Page<Article> pageList = articleSearchService.findByKey(key, page, size);
        return  new Result(true,StatusCode.OK,"查询成功",  new PageResult<Article>(pageList.getTotalElements(), pageList.getContent()) );
    }
}
