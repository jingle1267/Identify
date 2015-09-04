package com.ihongqiqu.Identify.entity;

import java.util.List;

/**
 * 有道翻译结果实体
 * <p/>
 * Created by zhenguo on 9/4/15.
 */
public class TranslationInfo extends BaseEntity {

    /**
     * translation : ["The dictionary"] basic : {"phonetic":"cí diǎn","explains":["[语][计]
     * dictionary"]} query : 词典 errorCode : 0 web : [{"value":["dictionary","MDict","Dictionaries"],"key":"词典"},{"value":["The
     * Dictionary","The dictionary a-z","Text"],"key":"词典正文"},{"value":["MiniDict","PocketDictionary"],"key":"迷你词典"}]
     */

    private BasicEntity basic;
    private String query;
    private int errorCode;
    private List<String> translation;
    private List<WebEntity> web;

    public void setBasic(BasicEntity basic) { this.basic = basic;}

    public void setQuery(String query) { this.query = query;}

    public void setErrorCode(int errorCode) { this.errorCode = errorCode;}

    public void setTranslation(List<String> translation) { this.translation = translation;}

    public void setWeb(List<WebEntity> web) { this.web = web;}

    public BasicEntity getBasic() { return basic;}

    public String getQuery() { return query;}

    public int getErrorCode() { return errorCode;}

    public List<String> getTranslation() { return translation;}

    public List<WebEntity> getWeb() { return web;}

    public static class BasicEntity extends BaseEntity {
        /**
         * phonetic : cí diǎn explains : ["[语][计] dictionary"]
         */

        private String phonetic;
        private List<String> explains;

        public void setPhonetic(String phonetic) { this.phonetic = phonetic;}

        public void setExplains(List<String> explains) { this.explains = explains;}

        public String getPhonetic() { return phonetic;}

        public List<String> getExplains() { return explains;}
    }


    public static class WebEntity extends BaseEntity {
        /**
         * value : ["dictionary","MDict","Dictionaries"] key : 词典
         */

        private String key;
        private List<String> value;

        public void setKey(String key) { this.key = key;}

        public void setValue(List<String> value) { this.value = value;}

        public String getKey() { return key;}

        public List<String> getValue() { return value;}
    }
}
