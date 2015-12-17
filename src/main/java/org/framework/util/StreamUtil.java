package org.framework.util;

//~--- non-JDK imports --------------------------------------------------------

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

//~--- JDK imports ------------------------------------------------------------

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * 流操作工具
 * Created by lizhaoz on 2015/12/17.
 */
public final class StreamUtil {
    private static final Logger LOGGER = LoggerFactory.getLogger(StreamUtil.class);

    /**
     * 从输入流中获取字符串
     * author：Lizhao
     * Date:15/12/17
     * version:1.0
     *
     * @param is
     */
    public static String getString(InputStream is) {
        StringBuilder sb = new StringBuilder();

        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(is));
            String         line;

            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
        } catch (IOException e) {
            LOGGER.error("get string failure", e);

            throw new RuntimeException(e);
        }
        return sb.toString();
    }
}


//~ Formatted by Jindent --- http://www.jindent.com
