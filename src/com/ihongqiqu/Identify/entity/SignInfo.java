package com.ihongqiqu.Identify.entity;

import java.util.List;

/**
 * 每日一签
 * <p/>
 * Created by zhenguo on 9/8/15.
 */
public class SignInfo extends BaseEntity {

    /**
     * sid : 1495 tts : http://news.iciba.com/admin/tts/2015-09-08-day.mp3 content : Regret for the
     * things we did can be tempered by time. It is regret for the things we did not do that is
     * inconsolable. note : 为做过的事而产生的悔恨会因时间而被慢慢平抚；为没做过的事而后悔却是无法被平抚的。（西德尼·哈里斯） love : 1908
     * translation : 词霸小编：如果你把思考交给了电视剧；把联系和人际圈交给了手机；把双腿和出行都交给了汽车；把健康都交给了补品和药片负责；那么你的生活也许是时尚的，但一定是错误的；也许是习惯的，但一定会为此买单的。
     * 请让书，聚会，步行，锻炼更多的出现在生活里。【9月16日，词霸大事件！在左侧菜单栏“悦读”活动中附上你认为的“大事件”，猜对有奖呦~】 picture :
     * http://cdn.iciba.com/news/word/2015-09-08.jpg picture2 : http://cdn.iciba.com/news/word/big_2015-09-08b.jpg
     * caption : 词霸每日一句 dateline : 2015-09-08 s_pv : 6990 sp_pv : 421 tags :
     * [{"id":"10","name":"正能量"},{"id":"13","name":"名人名言"}] fenxiang_img :
     * http://cdn.iciba.com/web/news/longweibo/imag/2015-09-08.jpg
     */

    private String sid;
    private String tts;
    private String content;
    private String note;
    private String love;
    private String translation;
    private String picture;
    private String picture2;
    private String caption;
    private String dateline;
    private String s_pv;
    private String sp_pv;
    private String fenxiang_img;
    private List<TagsEntity> tags;

    public void setSid(String sid) { this.sid = sid;}

    public void setTts(String tts) { this.tts = tts;}

    public void setContent(String content) { this.content = content;}

    public void setNote(String note) { this.note = note;}

    public void setLove(String love) { this.love = love;}

    public void setTranslation(String translation) { this.translation = translation;}

    public void setPicture(String picture) { this.picture = picture;}

    public void setPicture2(String picture2) { this.picture2 = picture2;}

    public void setCaption(String caption) { this.caption = caption;}

    public void setDateline(String dateline) { this.dateline = dateline;}

    public void setS_pv(String s_pv) { this.s_pv = s_pv;}

    public void setSp_pv(String sp_pv) { this.sp_pv = sp_pv;}

    public void setFenxiang_img(String fenxiang_img) { this.fenxiang_img = fenxiang_img;}

    public void setTags(List<TagsEntity> tags) { this.tags = tags;}

    public String getSid() { return sid;}

    public String getTts() { return tts;}

    public String getContent() { return content;}

    public String getNote() { return note;}

    public String getLove() { return love;}

    public String getTranslation() { return translation;}

    public String getPicture() { return picture;}

    public String getPicture2() { return picture2;}

    public String getCaption() { return caption;}

    public String getDateline() { return dateline;}

    public String getS_pv() { return s_pv;}

    public String getSp_pv() { return sp_pv;}

    public String getFenxiang_img() { return fenxiang_img;}

    public List<TagsEntity> getTags() { return tags;}

    public static class TagsEntity extends BaseEntity {
        /**
         * id : 10 name : 正能量
         */

        private String id;
        private String name;

        public void setId(String id) { this.id = id;}

        public void setName(String name) { this.name = name;}

        public String getId() { return id;}

        public String getName() { return name;}
    }
}
