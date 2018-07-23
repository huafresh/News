package hua.plugins.comgradle

public class ComExtension {

    /**
     * 是否自动注册组件，true则会使用字节码插入的方式自动注册代码
     * false的话，需要手动使用反射的方式来注册
     */
    boolean isRegisterCompoAuto = false

    /**
     * 当前组件的applicationName，用于字节码插入。
     * 当isRegisterCompoAuto==true的时候是必须的
     */
    String applicationName

    /**
     * 是否可以作为application运行
     */
    boolean isRunAlone = false

    /**
     * 作为application时需要动态依赖的组件
     */
    String applicationComponent

    /**
     * 作为library时需要动态依赖的组件
     */
    String libraryComponent
}