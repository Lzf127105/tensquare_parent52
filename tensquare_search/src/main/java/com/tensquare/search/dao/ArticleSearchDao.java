package com.tensquare.search.dao;

import com.tensquare.search.pojo.Article;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * @author Lzf
 * @create 2018-12-29 16:55
 * @Description:
 */
public interface ArticleSearchDao extends ElasticsearchRepository<Article,String>{

}
