package org.framework.helper;

//~--- non-JDK imports --------------------------------------------------------

import org.framework.bean.FormParam;
import org.framework.bean.Param;
import org.framework.util.ArrayUtil;
import org.framework.util.CodeUtil;
import org.framework.util.StreamUtil;
import org.framework.util.StringUtil;

//~--- JDK imports ------------------------------------------------------------

import java.io.IOException;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

/**
 * 请求助手类
 * Created by lizhaoz on 2015/12/31.
 */
public class RequestHelper {

    /**
     * 创建请求对象
     * author：Lizhao
     * Date:15/12/31
     * version:1.0
     *
     * @param request
     *
     * @return
     *
     * @throws IOException
     */
    public static Param createParam(HttpServletRequest request) throws IOException {
        List<FormParam> formParamList = new ArrayList<FormParam>();

        formParamList.addAll(parseParameterNames(request));
        formParamList.addAll(parseInputStream(request));

        return new Param(formParamList);
    }

    /**
     * 解析表单中的参数
     * author：Lizhao
     * Date:15/12/31
     * version:1.0
     *
     * @param request
     *
     * @return
     */
    private static List<FormParam> parseParameterNames(HttpServletRequest request) {
        List<FormParam>     formParamList = new ArrayList<FormParam>();
        Enumeration<String> paramNames    = request.getParameterNames();

        while (paramNames.hasMoreElements()) {
            String   fieldName   = paramNames.nextElement();
            String[] fieldValues = request.getParameterValues(fieldName);

            if (ArrayUtil.isNotEmpty(fieldValues)) {
                Object fieldValue;

                if (fieldValues.length == 1) {
                    fieldValue = fieldValues[0];
                } else {
                    StringBuilder sb = new StringBuilder("");

                    for (int i = 0; i < fieldValues.length; i++) {
                        sb.append(fieldValues[i]);

                        if (i != fieldValues.length - 1) {
                            sb.append(StringUtil.SEPARATOR);
                        }
                    }

                    fieldValue = sb.toString();
                }

                formParamList.add(new FormParam(fieldName, fieldValue));
            }
        }

        return formParamList;
    }

    /**
     * 解析地址后面带的参数
     * author：Lizhao
     * Date:15/12/31
     * version:1.0
     *
     * @param request
     *
     * @return
     *
     * @throws IOException
     */
    private static List<FormParam> parseInputStream(HttpServletRequest request) throws IOException {
        List<FormParam> formParamList = new ArrayList<FormParam>();
        String          body          = CodeUtil.decodeURL(StreamUtil.getString(request.getInputStream()));

        if (StringUtil.isNotEmpty(body)) {
            String[] kvs = StringUtil.splitString(body, "&");

            if (ArrayUtil.isNotEmpty(kvs)) {
                for (String kv : kvs) {
                    String[] array = StringUtil.splitString(kv, "=");

                    if (ArrayUtil.isNotEmpty(array) && (array.length == 2)) {
                        String fieldName  = array[0];
                        String fieldValue = array[1];

                        formParamList.add(new FormParam(fieldName, fieldValue));
                    }
                }
            }
        }

        return formParamList;
    }
}


//~ Formatted by Jindent --- http://www.jindent.com
