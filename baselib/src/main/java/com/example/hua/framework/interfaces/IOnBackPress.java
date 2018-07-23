package com.example.hua.framework.interfaces;

/**
 * 有时候Fragment需要知道Activity的返回事件，使用此接口可以解决。
 * <p>
 * 使用方法：
 * 1、Activity实现此接口
 * 2、Fragment在拿到Activity后调用{@link #addOnBackPressListener}设置监听即可
 * 3、记得在Frament退出时移除监听
 * <p>
 * 不要局限于在fragment中使用，只要能拿到Activity的实例，则可以在任意地方使用。
 *
 * @author hua
 * @version 2017/11/16 10:09
 */

public interface IOnBackPress {

    /**
     * 添加返回监听
     *
     * @param listener 返回监听
     * @see OnBackPressListener
     */
    void addOnBackPressListener(OnBackPressListener listener);

    /**
     * 移除返回监听
     *
     * @param listener 返回监听
     * @see OnBackPressListener
     */
    void removeOnBackPressListener(OnBackPressListener listener);

    interface OnBackPressListener {
        /**
         * activity返回时调用
         *
         * @return 是否消费。true表示消费，则返回事件停止传递
         */
        boolean onBackPress();
    }

}
