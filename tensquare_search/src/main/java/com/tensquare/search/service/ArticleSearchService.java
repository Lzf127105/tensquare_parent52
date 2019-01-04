package com.tensquare.search.service;

import com.tensquare.search.dao.ArticleSearchDao;
import com.tensquare.search.pojo.Article;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

/**
 * @author Lzf
 * @create 2018-12-29 16:59
 * @Description:
 */
@Service
public class ArticleSearchService {
    @Autowired
    private ArticleSearchDao articleSearchDao;

    /**
     * 增加文章
     * @param article
     */
    public void save(Article article) {
        articleSearchDao.save(article);
    }

    public Page<Article> findByKey(String key,int page,int size){
        Pageable pageable = PageRequest.of(page-1,size);
        return articleSearchDao.findByTitleOrContentLike(key,key,pageable);
    }
}
