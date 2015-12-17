package org.framework.util;

//~--- non-JDK imports --------------------------------------------------------

import com.fasterxml.jackson.databind.ObjectMapper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 基于Jackson实现的json转换类
 * Created by lizhaoz on 2015/12/17.
 */
public class JsonUtil {
    private static final Logger       LOGGER        = LoggerFactory.getLogger(JsonUtil.class);
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    /**
     * 把对象转换为json
     * author：Lizhao
     * Date:15/12/17
     * version:1.0
     *
     * @param obj
     * @param <T>
     *
     * @return
     */
    public static <T> String toJson(T obj) {
        String json;

        try {

            json = OBJECT_MAPPER.writeValueAsString(obj);
        } catch (Exception e) {
            LOGGER.error("convert POJO to JSON failure", e);

            throw new RuntimeException(e);
        }

        return json;
    }

    /**
     * 把json转换成pojo
     * author：Lizhao
     * Date:15/12/17
     * version:1.0
     *
     * @param json
     * @param type
     * @param <T>
     *
     * @return
     */
    public static <T> T fromJson(String json, Class<T> type) {
        T pojo;

        try {
            pojo = OBJECT_MAPPER.readValue(json, type);
        } catch (Exception e) {
            LOGGER.error("convert JSON to POJO failure", e);

            throw new RuntimeException(e);
        }

        return pojo;
    }
}


//~ Formatted by Jindent --- http://www.jindent.com
