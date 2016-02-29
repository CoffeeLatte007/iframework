package org.framework.util;

//~--- non-JDK imports --------------------------------------------------------

import org.apache.commons.codec.digest.DigestUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

//~--- JDK imports ------------------------------------------------------------

import java.awt.font.GlyphVector;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;

/**
 * 编码与解码操作工具类
 * Created by lizhaoz on 2015/12/17.
 */
public final class CodeUtil {
    private static final Logger LOGGER = LoggerFactory.getLogger(CodeUtil.class);

    /**
     * URL编码
     * author：Lizhao
     * Date:15/12/17
     * version:1.0
     *
     * @param source
     *
     * @return
     */
    public static String encodeURL(String source) {
        String target;

        try {
            target = URLEncoder.encode(source, "UTF-8");
        } catch (Exception e) {
            LOGGER.error("encode url failure", e);

            throw new RuntimeException(e);
        }

        return target;
    }

    /**
     * 解码
     * author：Lizhao
     * Date:15/12/17
     * version:1.0
     *
     * @param source
     *
     * @return
     */
    public static String decodeURL(String source) {
        String target;
        try {
            target= URLDecoder.decode(source,"utf-8");
        } catch (UnsupportedEncodingException e) {
            LOGGER.error("decode url failure",e);
            throw new RuntimeException(e);
        }
        return target;
    }

    public static String  md5(String submitted) {
        return DigestUtils.md5Hex(submitted);
    }
}


//~ Formatted by Jindent --- http://www.jindent.com
