package cn.cquptCommunity.search.pojo;

import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;

import java.io.Serializable;

/**
 * 存入索引库中：一个Article对象就对应一个文档
 */
@Document(indexName = "cquptcommunity_article", type = "article") //(索引库名，类型)
public class Article implements Serializable {
    @Id
    private String id;//ID

    /**
     * Field注解：文档对象中的域（类似于数据库表中的列）
     * 1.index=true/false：是否索引:就是表示该域能否被搜索(默认情况下为true)
     * 2.是否分词：就表示搜索的时候是整体匹配还是单词匹配，如果没有分词则代表整体匹配(默认情况下是分词)
     *      analyzer = "ik_max_word", searchAnalyzer = "ik _max_word" 就是在指定如何分词
     * 3.store:是否存储（就是是否在页面上显示：比如文章的标题、文章的描述在搜索出来时就需要被显示，而文章的内容在搜索出来时不需要被显示，只有点进去才会显示文章内容)
     *      其实不管我们将store值设置为true或false，elasticsearch都会将该字段存储到Field域中；但是他们的区别是什么？
     *          （1）store = false时，默认设置；那么给字段只存储在"_source"的Field域中；
     *          （2）store = true时，该字段的value会存储在一个跟_source平级的独立Field域中；同时也会存储在_source中，所以有两份拷贝。
     *          你可以指定一些字段store为true，这意味着这个field的数据将会被单独存储。这时候，如果你要求返回field1（store：yes），es会分辨出field1已经被存储了，因此不会从_source中加载，而是从field1的存储块中加载。
     * 4.analyzer指定分析器
     * 5.searchAnalyzer指定搜索时的分析器
     */
    @Field(index = true, analyzer = "ik_max_word", searchAnalyzer = "ik _max_word")
    private String title;//标题

    @Field(index = true, analyzer = "ik_max_word", searchAnalyzer = "ik_max_word")
    private String content;//文章描述

    private String state;//审核状态

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }
}
