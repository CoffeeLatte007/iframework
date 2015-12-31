package org.framework.bean;

//~--- non-JDK imports --------------------------------------------------------

import org.framework.util.CastUtil;
import org.framework.util.CollectionUtil;
import org.framework.util.StringUtil;

//~--- JDK imports ------------------------------------------------------------

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 在一个表单中有两类参数 表单参数和文件参数
 * 存储HttpServletRequest里面的参数
 * Created by lizhaoz on 2015/12/17.
 */
public class Param {

    // 表单参数
    private List<FormParam> formParamList;

    // 文件参数
    private List<FileParam> fileParamList;

    /**
     * Constructs ...
     *
     *
     * @param formParamList
     */
    public Param(List<FormParam> formParamList) {
        this.formParamList = formParamList;
    }

    /**
     * Constructs ...
     *
     *
     * @param formParamList
     * @param fileParamList
     */
    public Param(List<FormParam> formParamList, List<FileParam> fileParamList) {
        this.formParamList = formParamList;
        this.fileParamList = fileParamList;
    }

    /**
     * 获取请求参数映射
     * author：Lizhao
     * Date:15/12/31
     * version:1.0
     *
     * @return
     */
    public Map<String, Object> getFiledMap() {
        Map<String, Object> fieldMap = new HashMap<String, Object>();

        // 判断表单参数列表是否为空
        if (CollectionUtil.isNotEmpty(formParamList)) {

            // 不为空就遍历表单参数列表
            for (FormParam formParam : formParamList) {

                // 得到参数字段名字
                String fieldName = formParam.getFieldName();

                // 得到参数的值
                Object fieldValue = formParam.getFieldValue();

                // 判断是否有参数
                if (fieldMap.containsKey(fieldName)) {

                    // 使用值+分隔符+值的方式存储
                    fieldValue = fieldMap.get(fieldName) + StringUtil.SEPARATOR + fieldValue;
                }

                fieldMap.put(fieldName, fieldValue);
            }
        }

        return fieldMap;
    }

    /**
     * 获取上传文件映射
     * author：Lizhao
     * Date:15/12/31
     * version:1.0
     *
     * @return
     */
    public Map<String, List<FileParam>> getFileMap() {
        Map<String, List<FileParam>> fileMap = new HashMap<String, List<FileParam>>();

        if (CollectionUtil.isNotEmpty(fileParamList)) {
            for (FileParam fileParam : fileParamList) {
                String          fieldName = fileParam.getFieldName();
                List<FileParam> fileParamList;

                // 文件映射Map是否包含该字段
                if (fileMap.containsKey(fieldName)) {
                    fileParamList = fileMap.get(fieldName);
                } else {
                    fileParamList = new ArrayList<FileParam>();
                }

                fileParamList.add(fileParam);
                fileMap.put(fieldName, fileParamList);
            }
        }

        return fileMap;
    }

    /**
     * 通过字段名字得到所有上传文件(实现了多文件的上传)
     *
     * author：Lizhao
     * Date:15/12/31
     * version:1.0
     *
     * @param filedName
     *
     * @return
     */
    public List<FileParam> getFileList(String filedName) {
        return getFileMap().get(filedName);
    }

    /**
     * 获取单文件
     * author：Lizhao
     * Date:15/12/31
     * version:1.0
     *
     * @param filedName
     *
     * @return
     */
    public FileParam getFile(String filedName) {
        List<FileParam> fileParamList = getFileList(filedName);

        if (CollectionUtil.isNotEmpty(fileParamList) && (fileParamList.size() == 1)) {
            return fileParamList.get(0);
        }

        return null;
    }

    /**
     * 验证参数是否为空
     * author：Lizhao
     * Date:15/12/31
     * version:1.0
     *
     * @return
     */
    public boolean isEmpty() {
        return CollectionUtil.isEmpty(formParamList) && CollectionUtil.isEmpty(fileParamList);
    }

    /**
     * 得到字符串参数
     * author：Lizhao
     * Date:15/12/31
     * version:1.0
     *
     * @param name
     *
     * @return
     */
    public String getString(String name) {
        return CastUtil.castString(getFiledMap().get(name));
    }
    /**
     * 根据参数名获取 double 型参数值
     */
    public double getDouble(String name) {
        return CastUtil.castDouble(getFiledMap().get(name));
    }

    /**
     * 根据参数名获取 long 型参数值
     */
    public long getLong(String name) {
        return CastUtil.castLong(getFiledMap().get(name));
    }

    /**
     * 根据参数名获取 int 型参数值
     */
    public int getInt(String name) {
        return CastUtil.castInt(getFiledMap().get(name));
    }

    /**
     * 根据参数名获取 boolean 型参数值
     */
    public boolean getBoolean(String name) {
        return CastUtil.castBoolean(getFiledMap().get(name));
    }
}


//~ Formatted by Jindent --- http://www.jindent.com
