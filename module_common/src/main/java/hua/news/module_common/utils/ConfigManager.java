package hua.news.module_common.utils;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.XmlResourceParser;


import com.example.hua.framework.utils.MLog;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.util.HashMap;

import hua.news.module_common.base.BaseApplication;

/**
 * 配置管理，使用此类需要在本模块的res/xml目录下新建configuration.xml文件，文件内容的结构如下
 * <configs>
 * <system-config>
 * <item
 * name="配置的名称"
 * description="配置的描述"
 * value="配置的值" />
 * </system-config>
 * </configs>
 *
 * @author hua
 * @date 2017/6/12
 */

public class ConfigManager {

    private XmlResourceParser mXmlResParser;
    private HashMap<String, String> mSystemConfig;
    private Context mContext;

    private ConfigManager(Context context) {
        mContext = context.getApplicationContext();
        initPullParse();
    }

    private void initPullParse() {
        Resources resources = mContext.getResources();
        int resId = resources.getIdentifier("configuration", "xml", mContext.getPackageName());
        if (resId <= 0) {
            MLog.e("res/xml目录下没有找到configuration.xml文件，ConfigManager初始化失败");
        } else {
            mXmlResParser = resources.getXml(resId);
            parseXml(mXmlResParser);
        }
    }

    public static ConfigManager getInstance() {
        return Holder.sInstance;
    }

    private static final class Holder {
        private static final ConfigManager sInstance = new ConfigManager(BaseApplication.getContext());
    }
    private void parseXml(XmlResourceParser xmlResParser) {
        if (xmlResParser == null) {
            return;
        }
        try {
            int eventType = xmlResParser.getEventType();
            while (eventType != XmlPullParser.END_DOCUMENT) {
                switch (eventType) {
                    case XmlPullParser.START_TAG:
                        String tagName = xmlResParser.getName();
                        if ("system-config".equals(tagName)) {
                            mSystemConfig = new HashMap<>(16);
                        }
                        if ("item".equals(tagName)) {
                            String itemName = xmlResParser.getAttributeValue(null, "name");
                            String itemValue = mXmlResParser.getAttributeValue(null, "value");
                            mSystemConfig.put(itemName, itemValue);
                        }
                        break;
                    default:
                        break;

                }
                eventType = xmlResParser.next();
            }
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getItemConfigValue(String itemName) {
        return mSystemConfig != null ? mSystemConfig.get(itemName) : null;
    }

}
