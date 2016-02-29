package org.framework.util;

//~--- non-JDK imports --------------------------------------------------------

import net.sf.cglib.core.ReflectUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

//~--- JDK imports ------------------------------------------------------------

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;

import java.net.JarURLConnection;
import java.net.URL;

import java.util.Enumeration;
import java.util.HashSet;
import java.util.Set;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * 类的工具类 用于加载类
 * Created by lizhaoz on 2015/12/16.
 */
public final class ClassUtil {
    private static final Logger LOGGER = LoggerFactory.getLogger(ClassUtil.class);

    /**
     * 获取类加载器
     * author：Lizhao
     * Date:15/12/16
     * version:1.0
     *
     * @return
     */
    public static ClassLoader getClassloader() {
        return Thread.currentThread().getContextClassLoader();
    }

    /**
     * 加载类
     * author：Lizhao
     * Date:15/12/16
     * version:1.0
     *
     * @param className
     * @param isInitialized
     *
     * @return
     */
    public static Class<?> loadClass(String className, boolean isInitialized) {
        Class<?> cls;

        try {
            cls = Class.forName(className, isInitialized, getClassloader());
        } catch (ClassNotFoundException e) {
            LOGGER.error("load class failure", e);

            throw new RuntimeException(e);
        }

        return cls;
    }
    public static Class<?> loadClass(String className) {
        return loadClass(className, true);
    }
    /**
     * 对于制定包名的加载类
     * author：Lizhao
     * Date:15/12/16
     * version:1.0
     *
     * @param pacckageName
     *
     * @return
     */
    public static Set<Class<?>> getClassSet(String pacckageName) {
        Set<Class<?>> classSet = new HashSet<Class<?>>();

        try {

            // 得到制定包名下面的资源
            Enumeration<URL> urls = getClassloader().getResources(pacckageName.replace(".", "/"));

            while (urls.hasMoreElements()) {

                // url 格式file:/D:/eclipse/workspace/demo/WebContent/WEB-INF/classes/config/aaa.txt
                URL url = urls.nextElement();

                if (url != null) {
                    String protocol = url.getProtocol();    // 返回协议 是file 还是 jar

                    if (protocol.equals("file")) {

                        // 20%是URL的空格，将其代替，SUN公司也说明了这是一个BUG
                        String packagePath = url.getPath().replaceAll("%20", " ");

                        addClass(classSet, packagePath, pacckageName);
                    }
                    else if(protocol.equals("jar")){
                        //如果是Jar则建立连接
                        JarURLConnection jarURLConnection= (JarURLConnection) url.openConnection();
                        if(jarURLConnection!=null){
                            //得到Jarfile对象
                            JarFile jarFile=jarURLConnection.getJarFile();
                            //如果jarFile不为空
                            if(jarFile!=null){
                                //得到jarw文件内的实体
                                Enumeration<JarEntry> jarEntries=jarFile.entries();
                                while (jarEntries.hasMoreElements()){//遍历实体
                                    JarEntry jarEntry=jarEntries.nextElement();
                                    //得到jar实体的名字 例如javax/crypto/SecretKey.class
                                    String jarEntryName=jarEntry.getName();
                                    if(jarEntryName.endsWith(".class")){
                                        String className=jarEntryName.substring(0,jarEntryName.lastIndexOf(".")).replaceAll("/",".");
                                        doAddClass(classSet,className);
                                    }
                                }
                            }
                        }

                    }
                }
            }
        } catch (IOException e) {
            LOGGER.error("get class set failure",e);
             throw new RuntimeException(e);
        }

        return classSet;
    }

    /**
     *
     * author：Lizhao
     * Date:15/12/16
     * version:1.0
     *
     * @param classSet
     * @param packagePath
     * @param pacckageName
     */
    private static void addClass(Set<Class<?>> classSet, String packagePath, String pacckageName) {
        File[] files = new File(packagePath).listFiles(new FileFilter() {

            // 目录过滤器，过滤class文件和文件夹
            @Override
            public boolean accept(File file) {
                return (file.isFile() && file.getName().endsWith(".class")) || file.isDirectory();
            }
        });

        for (File file : files) {
            String fileName = file.getName();

            if (file.isFile()) {

                // 截取.class之前的部分
                String className = fileName.substring(0, fileName.lastIndexOf("."));

                if (StringUtil.isNotEmpty(pacckageName)) {

                    // 包名加类名
                    className = pacckageName + "." + className;
                }

                doAddClass(classSet, className);
            } else {

                // 如果是文件夹，就把路径和包名都相加，继续迭代
                String subPackagePath = fileName;

                if (StringUtil.isNotEmpty(packagePath)) {
                    subPackagePath = packagePath + "/" + subPackagePath;
                }

                String subPackageName = fileName;

                if (StringUtil.isNotEmpty(pacckageName)) {
                    subPackageName = pacckageName + "." + subPackageName;
                }
                //执行添加类
                addClass(classSet, subPackagePath, subPackageName);
            }
        }
    }

    /**
     * 添加类加载
     * author：Lizhao
     * Date:15/12/16
     * version:1.0
     *
     * @param classSet
     * @param className
     */
    private static void doAddClass(Set<Class<?>> classSet, String className) {
        Class<?> cls = loadClass(className, false);

        classSet.add(cls);
    }
}


//~ Formatted by Jindent --- http://www.jindent.com
