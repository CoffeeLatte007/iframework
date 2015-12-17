package org.framework.helper;

//~--- non-JDK imports --------------------------------------------------------

import org.framework.util.PropsUtil;
import org.framework.ConfigConstant;

//~--- JDK imports ------------------------------------------------------------

import java.util.Properties;

/**
 * 属性文件助手
 * Created by lizhaoz on 2015/12/16.
 */
public final class ConfigHelper {

    // 得到配置属性
    private static final Properties CONFIG_PROPS = PropsUtil.loadProps(ConfigConstant.CONFIG_FILE);

    /**
     * 得到Jdbc驱动
     * author：Lizhao
     * Date:15/12/16
     * version:1.0
     *
     * @return
     */
    public static String getJdbcDriver() {
        return PropsUtil.getString(CONFIG_PROPS, ConfigConstant.JDBC_DRIVER);
    }

    /**
     * 得到jdbcurl
     * author：Lizhao
     * Date:15/12/16
     * version:1.0
     *
     * @return
     */
    public static String getJdbcUrl() {
        return PropsUtil.getString(CONFIG_PROPS, ConfigConstant.JDBC_URL);
    }

    /**
     * 得到jdbcusername
     * author：Lizhao
     * Date:15/12/16
     * version:1.0
     *
     * @return
     */
    public static String getJdbcUserName() {
        return PropsUtil.getString(CONFIG_PROPS, ConfigConstant.JDBC_USERNAME);
    }

    /**
     * 得到Jdbcpassword
     * author：Lizhao
     * Date:15/12/16
     * version:1.0
     *
     * @return
     */
    public static String getJdvcPassword() {
        return PropsUtil.getString(CONFIG_PROPS, ConfigConstant.JDBC_PASSWORD);
    }

    /**得到基础包
     * author：Lizhao
     * Date:15/12/16
     * version:1.0
     *
     * @return
     */
    public static String getAppBasePackage() {
        return PropsUtil.getString(CONFIG_PROPS, ConfigConstant.APP_BASE_PACKAGE);
    }

    /**
     * 得到Jsp路径
     * author：Lizhao
     * Date:15/12/16
     * version:1.0
     *
     * @return
     */
    public static String getAppJspPath() {
        return PropsUtil.getString(CONFIG_PROPS, ConfigConstant.APP_JSP_PATH, "/WEB-INF/view/");
    }

    /**
     * 得到静态资源路径
     * author：Lizhao
     * Date:15/12/16
     * version:1.0
     *
     * @return
     */
    public static String getAppAssetPath() {
        return PropsUtil.getString(CONFIG_PROPS, ConfigConstant.APP_ASSET_PATH, "/asset/");
    }
}


//~ Formatted by Jindent --- http://www.jindent.com
