package hua.news.module_common.base;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;

/**
 * @author hua
 * @version 2018/5/4 16:17
 */

public class BaseViewModel extends AndroidViewModel {

    public MutableLiveData<String> toast = new MutableLiveData<>();
    public MutableLiveData<Integer> pageStatus = new MutableLiveData<>();

    public static final int STATUS_LOADING = 0;
    public static final int STATUS_ERROR = 1;
    public static final int STATUS_COMPLETE = 2;


    public BaseViewModel(@NonNull Application application) {
        super(application);
    }
}
