package com.tensquare.search.dao;

import com.tensquare.search.pojo.Article;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * @author Lzf
 * @create 2018-12-29 16:55
 * @Description:
 */
public interface ArticleSearchDao extends ElasticsearchRepository<Article,String>{
    /**
     * 模糊查询 title和content
     * @param title
     * @param content
     * @param pageable
     * @return
     */
    public Page<Article> findByTitleOrContentLike(String title,String content, Pageable pageable);
}
