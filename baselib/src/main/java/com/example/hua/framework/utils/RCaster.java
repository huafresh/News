package com.example.hua.framework.utils;

import android.util.SparseArray;

import java.lang.reflect.Field;
import java.util.HashMap;

/**
 * Created by retrox on 23/03/2017.
 */
public class RCaster {
    private static final String R_ID_NAME = "id";
    /**
     * id取得属性名字
     */
    private SparseArray<String> r1Map = new SparseArray<>();
    /**
     * 属性名字回取id
     */
    private HashMap<String, Integer> r2Map = new HashMap<>();


    private Class R;
    private Class R2;

    public RCaster(Class r, Class r2) {
        R = r;
        R2 = r2;
        initMap1();
        initMap2();
    }

    /**
     * R1 id cast to R2
     */
    public int cast(int rid) {
        String name = r1Map.get(rid);
        return r2Map.get(name);
    }

    /**
     * 初始化r1Map
     */
    @SuppressWarnings("Duplicates")
    private void initMap1() {

        long time = System.currentTimeMillis();

        Class[] classes = R.getClasses();
        Object r = null;
        try {
            r = R.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }

        for (Class aClass : classes) {
            if (aClass.getSimpleName().equals(R_ID_NAME)) {
                Field[] fields = aClass.getFields();
                for (Field field : fields) {
                    field.setAccessible(true);
                    try {
                        Object value = field.get(r);
                        int x = 0;
                        if (value != null) {
                            x = (int) value;
                        }
                        r1Map.put(x, field.getName());
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        long time2 = System.currentTimeMillis();
        long timeCost = time2 - time;

        System.out.println("\nTimecost:" + timeCost + "ms");

    }

    /**
     * 初始化r2Map
     */
    @SuppressWarnings("Duplicates")
    private void initMap2() {

        long time = System.currentTimeMillis();

        Class[] classes = R2.getClasses();
        Object r = null;
        try {
            r = R2.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }

        for (Class aClass : classes) {
            if (aClass.getSimpleName().equals(R_ID_NAME)) {
                Field[] fields = aClass.getFields();
                for (Field field : fields) {
                    field.setAccessible(true);
                    try {
                        Object value = field.get(r);
                        int x = 0;
                        if (value != null) {
                            x = (int) value;
                        }
                        r2Map.put(field.getName(), x);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }

        }
        long time2 = System.currentTimeMillis();
        long timeCost = time2 - time;

        System.out.println("\nTimecost:" + timeCost + "ms");

    }


}
