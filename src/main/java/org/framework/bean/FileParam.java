package org.framework.bean;

import java.io.InputStream;

/**
 * 封装上传文件参数
 * Created by lizhaoz on 2015/12/31.
 */

public class FileParam {
    //文件表单字段名
    private String fieldName;
    //文件名字
    private String fileName;
    //文件大小
    private long fileSize;
    //类型
    private String contentType;
    //字节输入流
    private InputStream inputStream;

    public FileParam(String fieldName, String fileName, long fileSize, String contentType, InputStream inputStream) {
        this.fieldName = fieldName;
        this.fileName = fileName;
        this.fileSize = fileSize;
        this.contentType = contentType;
        this.inputStream = inputStream;
    }

    public String getFieldName() {
        return fieldName;
    }

    public String getFileName() {
        return fileName;
    }

    public long getFileSize() {
        return fileSize;
    }

    public String getContentType() {
        return contentType;
    }

    public InputStream getInputStream() {
        return inputStream;
    }
}
