package org.framework.bean;

//~--- non-JDK imports --------------------------------------------------------

import org.framework.util.CastUtil;

//~--- JDK imports ------------------------------------------------------------

import java.util.Map;

/**
 * 存储HttpServletRequest里面的参数
 * Created by lizhaoz on 2015/12/17.
 */
public class Param {
    private Map<String, Object> paramMap;

    /**
     * Constructs ...
     *
     *
     * @param paramMap
     */
    public Param(Map<String, Object> paramMap) {
        this.paramMap = paramMap;
    }

    /**
     * 根据参数名称获取long型参数值
     * author：Lizhao
     * Date:15/12/17
     * version:1.0
     *
     * @param name
     *
     * @return
     */
    public long getLong(String name) {
        return CastUtil.castLong(paramMap.get(name));
    }

    /**
     * 获取所有请求参数信息
     * author：Lizhao
     * Date:15/12/17
     * version:1.0
     *
     * @return
     */
    public Map<String, Object> getFieldMap() {
        return paramMap;
    }
}


//~ Formatted by Jindent --- http://www.jindent.com
