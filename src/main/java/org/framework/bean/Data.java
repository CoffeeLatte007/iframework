package org.framework.bean;

/**
 * 返回Json数据对象
 * 返回一个Object类型的模型数据，框架会将该对象写入HttpServletResponse，直到输出给浏览器。
 * Created by lizhaoz on 2015/12/17.
 */

public class Data {
    private Object model;

    public Data(Object model) {
        this.model = model;
    }
    public Object getModel(){
        return model;
    }
}
