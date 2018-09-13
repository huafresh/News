package com.example.hua.framework.lifebus;

/**
 * @author hua
 * @version 2018/8/29 10:48
 */

public enum ThreadMode {

    /**
     * 与post方法执行的线程一致
     */
    POSTING,

    /**
     * 主线程。默认值。
     */
    MAIN,

    /**
     * 子线程。暂不实现。配置此值效果同{@link ThreadMode#MAIN}
     */
    BACKGROUD

}
