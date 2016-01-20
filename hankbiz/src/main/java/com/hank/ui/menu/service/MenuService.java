package com.hank.ui.menu.service;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlSerializer;

import android.util.Xml;

import com.hank.ui.menu.dto.Menu;

/**
 * Created by Thinkpad on 2016/1/12.
 */
public class MenuService {
    public static List<Menu> getMenus(InputStream xml) throws Exception {
        List<Menu> items = null;
        Menu item = null;
        XmlPullParser pullParser = Xml.newPullParser();
        pullParser.setInput(xml, "UTF-8"); //为Pull解释器设置要解析的XML数据
        int event = pullParser.getEventType();

        while (event != XmlPullParser.END_DOCUMENT){

            switch (event) {

                case XmlPullParser.START_DOCUMENT:
                    items = new ArrayList<Menu>();
                    break;
                case XmlPullParser.START_TAG:
                    if ("item".equals(pullParser.getName())){
                        int id = Integer.valueOf(pullParser.getAttributeValue(0));
                        item = new Menu();
                        item.setId(id);
                    }
                    if ("name".equals(pullParser.getName())){
                        String name = pullParser.nextText();
                        item.setName(name);
                    }
                    if ("value".equals(pullParser.getName())){
                        String value = pullParser.nextText();
                        item.setValue(value);
                    }
                    if ("date".equals(pullParser.getName())){
                        String date = pullParser.nextText();
                        item.setDate(date);
                    }
                    break;

                case XmlPullParser.END_TAG:
                    if ("item".equals(pullParser.getName())){
                        items.add(item);
                        item = null;
                    }
                    break;

            }

            event = pullParser.next();
        }


        return items;
    }

    /**
     * 保存数据到xml文件中
     * @param items
     * @param out
     * @throws Exception
     */
    public static void save(List<Menu> items, OutputStream out) throws Exception {
        XmlSerializer serializer = Xml.newSerializer();
        serializer.setOutput(out, "UTF-8");
        serializer.startDocument("UTF-8", true);
        serializer.startTag(null, "items");
        for (Menu item : items) {
            serializer.startTag(null, "item");
            serializer.attribute(null, "id", String.valueOf(item.getId()));
            serializer.startTag(null, "name");
            serializer.text(item.getName().toString());
            serializer.endTag(null, "name");
            serializer.startTag(null, "value");
            serializer.text(item.getValue().toString());
            serializer.endTag(null, "value");
            serializer.endTag(null, "item");
        }
        serializer.endTag(null, "items");
        serializer.endDocument();
        out.flush();
        out.close();
    }
}
