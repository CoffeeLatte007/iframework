package org.framework.util;

//~--- non-JDK imports --------------------------------------------------------

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

//~--- JDK imports ------------------------------------------------------------

import java.io.File;

/**
 * Created by lizhaoz on 2015/12/31.
 */
public class FileUtil {
    private static final Logger LOGGER = LoggerFactory.getLogger(FileUtil.class);

    /**
     * 获取真实文件名(自动去掉文件路径)
     * author：Lizhao
     * Date:15/12/31
     * version:1.0
     *
     * @param fileName
     *
     * @return
     */
    public static String getRealFileName(String fileName) {
        return FilenameUtils.getName(fileName);
    }

    /**
     * 根据路径创建文件
     * author：Lizhao
     * Date:15/12/31
     * version:1.0
     *
     * @param filePath
     *
     * @return
     */
    public static File createFile(String filePath) {
        File file;

        try {
            file = new File(filePath);
            //确定返回的是抽象名称路径
            File parentDir = file.getParentFile();

            if (!parentDir.exists()) {
                //创建文件夹
                FileUtils.forceMkdir(parentDir);
            }
        } catch (Exception e) {
            LOGGER.error("create file failure", e);

            throw new RuntimeException(e);
        }

        return file;
    }
}


//~ Formatted by Jindent --- http://www.jindent.com
