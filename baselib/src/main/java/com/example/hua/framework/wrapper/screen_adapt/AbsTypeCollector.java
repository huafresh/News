package com.example.hua.framework.wrapper.screen_adapt;

import java.util.ArrayList;
import java.util.List;

/**
 * @author hua
 * @version 1.0
 * @date 2018/11/3
 */
abstract class AbsTypeCollector implements ITypeCollector {

    static class Base extends AbsTypeCollector {

        @Override
        public boolean canHandle(Class cls) {
            return true;
        }

        @Override
        public List<AttrType> collect(Class cls) {
            List<AttrType> list = new ArrayList<>();
            list.add(AttrType.WidthHeight.INSTANCE);
            list.add(AttrType.Padding.INSTANCE);
            list.add(AttrType.Margin.INSTANCE);
            return list;
        }
    }

    static class TextView extends Base {

        @Override
        public boolean canHandle(Class cls) {
            return TextView.class.isAssignableFrom(cls);
        }

        @Override
        public List<AttrType> collect(Class cls) {
            List<AttrType> sup = super.collect(cls);
            sup.add(AttrType.TextSize.INSTANCE);
            return sup;
        }
    }

}
