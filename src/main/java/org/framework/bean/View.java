package org.framework.bean;

//~--- JDK imports ------------------------------------------------------------

import java.util.HashMap;
import java.util.Map;

/**
 * 返回一个视图对象view jsp
 * Created by lizhaoz on 2015/12/17.
 */
public class View {

    // 视图路径
    private String path;

    // 模型数据
    private Map<String, Object> model;

    /**
     * Constructs ...
     *
     *
     * @param path
     */
    public View(String path) {
        this.path = path;
        model     = new HashMap<String, Object>();
    }

    /**
     * 添加模型数据返回视图
     * author：Lizhao
     * Date:15/12/17
     * version:1.0
     *
     * @param key
     * @param value
     *
     * @return
     */
    public View addModel(String key, Object value) {
        model.put(key, value);

        return this;
    }

    /**
     * author：Lizhao
     * Date:15/12/17
     * version:1.0
     *
     * @return
     */
    public String getPath() {
        return path;
    }

    /**
     * author：Lizhao
     * Date:15/12/17
     * version:1.0
     *
     * @return
     */
    public Map<String, Object> getModel() {
        return model;
    }
}


//~ Formatted by Jindent --- http://www.jindent.com
