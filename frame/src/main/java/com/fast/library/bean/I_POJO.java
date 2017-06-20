package com.fast.library.bean;

/**
 * 说明：实体对象模型
 * <p/>
 * 作者：fanly
 * <p/>
 * 时间：2015/12/26 14:59
 * <p/>
 * 版本：verson 1.0
 */
public interface I_POJO {

    /**
     * 说明：获取类型
     * @return
     */
    String getType();

    /**
     * 说明：json转bean
     * @param json
     * @param <T>
     * @return
     */
    <T extends Object> T toBean(String json);

    /**
     * 说明：json转list
     * @param json
     * @param <T>
     * @return
     */
    <T extends Object> T toList(String json);

}
