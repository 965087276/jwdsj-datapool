package cn.ict.jwdsj.datapool.indexmanage.elastic.constant;

/**
 * Elastic字段类型模板
 */
public enum EsColumnTypeEnum {

    DOC("{\"doc\":{\"properties\":{\"elastic_database_name\":{\"type\":\"keyword\",\"eager_global_ordinals\":true},\"elastic_table_name\":{\"type\":\"keyword\",\"eager_global_ordinals\":true}}}}"),

    TEXT("{\"type\":\"text\",\"analyzer\":\"ik_max_word\"}"),

    KEYWORD("{\"type\":\"keyword\"}"),

    INTEGER("integer"),

    DATE("date"),

    NOT_SEARCH("{\"enabled\": \"false\"}");

    private String template;

    EsColumnTypeEnum(String template) {
        this.template = template;
    }

    public String getTemplate() {
        return this.template;
    }
}
