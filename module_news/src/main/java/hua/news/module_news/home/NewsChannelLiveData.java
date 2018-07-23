package hua.news.module_news.home;

import android.arch.lifecycle.MutableLiveData;
import android.content.Context;
import android.text.TextUtils;

import com.example.hua.framework.json.JsonParseUtil;
import com.example.hua.framework.storage.StorageManager;

import java.util.ArrayList;
import java.util.List;

import hua.news.module_news.R;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

/**
 * 新闻频道数据
 *
 * @author hua
 * @version 2018/5/4 14:00
 */
public class NewsChannelLiveData extends MutableLiveData<List<IFengColumnEntity>> {

    public static final String KEY_CHANNEL = "key_channel";
    private Context context;

    public NewsChannelLiveData(Context context) {
        this.context = context;
    }

    @Override
    protected void onActive() {
        getNewsChannelData();
    }

    public void getNewsChannelData() {
        Observable.just("")
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .map(new Function<String, List<IFengColumnEntity>>() {
                    @Override
                    public List<IFengColumnEntity> apply(String s) throws Exception {
                        String channel = StorageManager.getInstance(context).getFromDisk(KEY_CHANNEL);
                        List<IFengColumnEntity> channelList = null;
                        if (!TextUtils.isEmpty(channel)) {
                            channelList = JsonParseUtil.getInstance().parseJsonToList(channel, IFengColumnEntity.class);
                            if (channelList.size() < 1) {
                                channelList = getChannelListFromStringArray();
                            }
                        } else {
                            channelList = getChannelListFromStringArray();
                        }
                        StorageManager.getInstance(context).saveToDisk(KEY_CHANNEL,
                                JsonParseUtil.getInstance().parseObjectToString(channelList));

                        return channelList;
                    }
                })
                .subscribe(new Consumer<List<IFengColumnEntity>>() {
                    @Override
                    public void accept(List<IFengColumnEntity> iFengColumnEntities) throws Exception {
                        setValue(iFengColumnEntities);
                    }
                });

    }

    private List<IFengColumnEntity> getChannelListFromStringArray() {
        String[] channelNames = context.getResources().getStringArray(R.array.news_channel);
        String[] channelIds = context.getResources().getStringArray(R.array.news_channel_id);

        List<IFengColumnEntity> list = new ArrayList<>();

        for (int i = 0; i < channelNames.length; i++) {
            String name = channelNames[i];
            IFengColumnEntity entity = new IFengColumnEntity();
            entity.setName(name);
            entity.setChannel_id(channelIds[i]);
            if (i < channelNames.length - 4) {
                entity.setIs_add(true);
            } else {
                entity.setIs_add(false);
            }
            if (i == 0) {
                entity.setIs_removable(false);
            } else {
                entity.setIs_removable(true);
            }
            list.add(entity);
        }

        return list;
    }
}
