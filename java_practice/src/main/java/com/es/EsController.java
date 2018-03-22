package com.es;

import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.RangeQueryBuilder;
import org.elasticsearch.search.SearchHit;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by FangYan on 2017/10/3 0003.
 */
/*@Scope("session")*/
@Scope("session")
/**
 * prototype为多例，每次的请求(sessionid一样也会创建)都会创建一个controller,
 * 无状态模式,没有sessionid
 * singleton为单例，只会创建一个
 * session为回话级别，不同的sessionid会创建不同的controller
 */
@Controller
public class EsController {

    private int i =5;

    @RequestMapping(value = "/get/book/novel/{id}",method = RequestMethod.GET)
    @ResponseBody
    public Map get(@PathVariable("id") String id) throws UnknownHostException {
        Map<String,Object> map = new HashMap<String,Object>();
        MyConfig myConfig = new MyConfig();
        if(id.isEmpty()){
            map.put("error",404);
            return map;
        }
        GetResponse result = myConfig.client().prepareGet("book", "type", id).get();
        if(!result.isExists()){
            map.put("error",404);
            return map;
        }
        return result.getSource();
    }

    @RequestMapping("add/book/novel")
    @ResponseBody
    public Map add(String title,String author,Integer word_count,String publish_date){
        Map<String,Object> map = new HashMap<String,Object>();
        MyConfig myConfig = new MyConfig();
        try {
            XContentBuilder content = XContentFactory.jsonBuilder().startObject()
                    .field("title", title)
                    .field("author", author)
                    .field("word_count", word_count)
                    .field("publish_date", publish_date)
                    .endObject();
            IndexResponse result = myConfig.client().prepareIndex("book", "type")
                    .setSource(content)
                    .get();
            map.put("添加成功",result.getId());
            return  map;
        } catch (IOException e) {
            e.printStackTrace();
            map.put("error","错误");
            return map;
        }
    }

    @RequestMapping("query/book/novel")
    @ResponseBody
    public Map query(String author,String title,Integer gtWordCount,Integer ltWordCount) throws UnknownHostException {
        Map<String,Object> map = new HashMap<String,Object>();
        MyConfig myConfig = new MyConfig();
        BoolQueryBuilder boolQuery = QueryBuilders.boolQuery();
        if(author!=null&&author!=""){
            boolQuery.must(QueryBuilders.matchQuery("author",author));
        }
        if(title!=null&&author!=""){
            boolQuery.must(QueryBuilders.matchQuery("title",title));
        }
        RangeQueryBuilder rangeCount = QueryBuilders.rangeQuery("word_count").from(gtWordCount);
        if(ltWordCount!=null&& ltWordCount >0 ){
                rangeCount.to(ltWordCount);
        }
        boolQuery.filter(rangeCount);
        SearchRequestBuilder searchRequestBuilder = myConfig.client().prepareSearch("book").setTypes("type")
                .setSearchType(SearchType.QUERY_THEN_FETCH)
                .setQuery(boolQuery)
                .setFrom(0)
                .setSize(10);
        System.out.println(searchRequestBuilder);
        SearchResponse searchResponse = searchRequestBuilder.get();
        List<Map<String,Object>> list = new ArrayList<Map<String, Object>>();
        for ( SearchHit searchHit : searchResponse.getHits()) {
                list.add(searchHit.getSource());
        }
        map.put("ok",list);
        return  map;
    }


    @RequestMapping("hello")
    @ResponseBody
    public Integer say(Integer id) throws InterruptedException {
       if(id==0){
           return  0;
       }
        if(id==1){
            return  1;
        }
        if(id==2){
            return  2;
        }
        if(id==3){
            return  3;
        }else {
            return 4;
        }
    }

    @RequestMapping("spring")
    public String test() throws  Exception{
        try {
            Person person = SpringApplicationContext.getBean("person");
            String school = person.school();
            int i = 1/0;
            return  school;
        } catch (Exception e) {
            e.printStackTrace();
            StringWriter s = new StringWriter();
            PrintWriter printWriter = new PrintWriter(s);
            e.printStackTrace(printWriter);
            s.toString();
            //java有2种返回方式，throws和return
            throw e;
        }
    }

    @RequestMapping("reduce")
    @ResponseBody
    public Integer reduce(){
        i--;
        return i;
    }

    @RequestMapping("person")
    @ResponseBody
    public  Integer add(HttpSession session){
        Person person = new Person();
        //不同会话访问得到的person1的哈希码是一样的，说明spring创建的bean是单例。
//        Person person1 = SpringApplicationContext.getBean("person");\
        Person person1 = (Person) session.getAttribute("user");
        return person1.hashCode();
    }

    @RequestMapping("session")
    @ResponseBody
    public String session(HttpSession session){
        Person person = new Person();
        person.setName("小明");
        person.setAge(12);
        session.setAttribute("user",person);
        return "success";
    }
}
