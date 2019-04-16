package cn.ict.jwdsj.datapool.indexmanage.elastic.constant;

/**
 * Elastic字段类型模板
 */
public enum EsColumnTypeEnum {

    DOC("{\"doc\":{\"properties\":{\"elastic_database_id\":{\"type\":\"keyword\",\"eager_global_ordinals\":true},\"elastic_table_id\":{\"type\":\"keyword\",\"eager_global_ordinals\":true},\"md5_id\":{\"enabled\":false},\"elastic_index_name\":{\"enabled\":false},\"all_fields_text\":{\"type\":\"text\",\"analyzer\":\"hanlp_index\"},\"all_fields_keyword\":{\"type\":\"keyword\"}}}}"),

    TEXT("{\"type\":\"text\",\"analyzer\":\"hanlp_index\",\"copy_to\":\"all_fields_text\",\"term_vector\":\"with_positions_offsets\",\"index_options\":\"offsets\"}"),

    KEYWORD("{\"type\":\"keyword\",\"copy_to\":\"all_fields_keyword\"}"),

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
