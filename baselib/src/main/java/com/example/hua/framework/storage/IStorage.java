package com.example.hua.framework.storage;

/**
 * 存储接口，定义数据存储的一般方法
 *
 * @author hua
 * @version 2017/10/21 11:27
 */

public interface IStorage<K, V> {

    /**
     * 存储数据
     *
     * @param key   键值
     * @param value 数据
     * @return 是否成功
     */
    boolean save(K key, V value);

    /**
     * 获取数据
     *
     * @param key 键值
     * @return 与键值对应的数据
     */
    V get(K key);

    /**
     * 存储加密数据
     *
     * @param key    键值
     * @param cotent 明文数据
     * @return 是否成功
     */
    boolean saveEncrypt(String key, String cotent);

    /**
     * 获取解密数据
     *
     * @param key 键值
     * @return 解密后的明文数据
     */
    String getDecrypt(String key);

    /**
     * 删除指定数据
     *
     * @param key 键值
     * @return 是否成功
     */
    boolean delete(K key);

    /**
     * 删除所有数据
     *
     * @return 是否成功
     */
    boolean clear();
}
