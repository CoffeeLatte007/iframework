package org.framework.helper;

//~--- non-JDK imports --------------------------------------------------------

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import org.framework.bean.FileParam;
import org.framework.bean.FormParam;
import org.framework.bean.Param;
import org.framework.util.CollectionUtil;
import org.framework.util.FileUtil;
import org.framework.util.StreamUtil;
import org.framework.util.StringUtil;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

//~--- JDK imports ------------------------------------------------------------

import java.io.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

/**
 * 文件上传助手类
 * Created by lizhaoz on 2015/12/31.
 */
public class UploadHelper {
    private static final Logger LOGGER = LoggerFactory.getLogger(UploadHelper.class);

    // Apache Commons FileUpload提供的Servlet文件上传对象
    private static ServletFileUpload servletFileUpload;

    /**
     * 、初始化servletFileUpload
     * author：Lizhao
     * Date:15/12/31
     * version:1.0
     *
     * @param servletContext
     */
    public static void init(ServletContext servletContext) {

        // 得到servlet缓存目录 如果用的是tomcat就是work\Catalina\localhost\ServletConfigContext
        File repository = (File) servletContext.getAttribute("javax.servlet.context.tempdir");

        // 默认大小10kb保存在内存中，如果超出了就保存在磁盘中
        servletFileUpload = new ServletFileUpload(new DiskFileItemFactory(DiskFileItemFactory.DEFAULT_SIZE_THRESHOLD,
                repository));

        int uploadLimit = ConfigHelper.getAppUploadLimit();

        if (uploadLimit != 0) {

            // 设置上传限制
            servletFileUpload.setFileSizeMax(uploadLimit * 1024 * 1024);
        }
    }

    /**
     * 判断请求是否为multipart
     * author：Lizhao
     * Date:15/12/31
     * version:1.0
     *
     * @param request
     *
     * @return
     */
    public static boolean isMultipart(HttpServletRequest request) {
        return ServletFileUpload.isMultipartContent(request);
    }

    /**
     * 初始化Param参数
     * 把两种请求参数填充
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

        // 两种参数列表
        List<FormParam> formParamList = new ArrayList<FormParam>();
        List<FileParam> fileParamList = new ArrayList<FileParam>();

        try {

            // 解析请求参数
            Map<String, List<FileItem>> fileItemListMap = servletFileUpload.parseParameterMap(request);

            if (CollectionUtil.isNotEmpty(fileItemListMap)) {
                for (Map.Entry<String, List<FileItem>> fileItemListEntry : fileItemListMap.entrySet()) {

                    // 得到
                    String fieldName = fileItemListEntry.getKey();

                    // 得到文件列表
                    List<FileItem> fileItemList = fileItemListEntry.getValue();

                    if (CollectionUtil.isNotEmpty(fileItemList)) {
                        for (FileItem fileItem : fileItemList) {
                            if (fileItem.isFormField()) {
                                String fieldValue = fileItem.getString("UTF-8");

                                formParamList.add(new FormParam(fieldName, fieldValue));
                            } else {
                                String fileName = FileUtil.getRealFileName(new String(fileItem.getName().getBytes(),
                                                      "UTF-8"));

                                if (StringUtil.isNotEmpty(fileName)) {
                                    long        fileSize    = fileItem.getSize();
                                    String      contentType = fileItem.getContentType();
                                    InputStream inputSteam  = fileItem.getInputStream();

                                    fileParamList.add(new FileParam(fieldName, fileName, fileSize, contentType,
                                                                    inputSteam));
                                }
                            }
                        }
                    }
                }
            }
        } catch (FileUploadException e) {
            LOGGER.error("create param failure", e);

            throw new RuntimeException(e);
        }

        return new Param(formParamList, fileParamList);
    }

    /**
     * 单文件上传
     * author：Lizhao
     * Date:15/12/31
     * version:1.0
     *
     * @param basePath
     * @param fileParam
     */
    public static void uploadFile(String basePath, FileParam fileParam) {
        try {
            if (fileParam != null) {

                // 通过文件夹地址和文件名字得到文件地址
                String filePath = basePath + fileParam.getFileName();

                FileUtil.createFile(filePath);

                InputStream inputStream = new BufferedInputStream(fileParam.getInputStream());

                // 输出到该地址，文件上传过程
                OutputStream outputStream = new BufferedOutputStream(new FileOutputStream(filePath));

                StreamUtil.copyStream(inputStream, outputStream);
            }
        } catch (Exception e) {
            LOGGER.error("upload file failure", e);

            throw new RuntimeException(e);
        }
    }

    /**
     * 批量上传文件
     * author：Lizhao
     * Date:15/12/31
     * version:1.0
     *
     * @param basePath
     * @param fileParamList
     */
    public static void uploadFile(String basePath, List<FileParam> fileParamList) {
        try {
            if (CollectionUtil.isNotEmpty(fileParamList)) {
                for (FileParam fileParam : fileParamList) {
                    uploadFile(basePath, fileParam);
                }
            }
        } catch (Exception e) {
            LOGGER.error("upload file failure", e);

            throw new RuntimeException(e);
        }
    }
}


//~ Formatted by Jindent --- http://www.jindent.com
