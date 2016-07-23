#说明
##难点分析：

AOP框架的实现：
http://blog.csdn.net/li563868273/article/details/50764650

jackson和fastjson速度对比blog分析
http://blog.csdn.net/li563868273/article/details/52010695

如何对于给定包名获取所有类：
http://blog.csdn.net/li563868273/article/details/50764652

shiro的学习：
http://jinnianshilongnian.iteye.com/blog/2018398

##使用说明：
首先把该源码下载下来，和需要使用框架的项目放在一个项目下面，不同Moudle。
直接使用Maven在需要使用框架的项目上：

     <dependency>
    <groupId>org.iframe</groupId>
    <artifactId>iframework</artifactId>
    <version>1.0.0</version>
    </dependency>
     <dependency>
            <groupId>org.iframe</groupId>
            <artifactId>iframe-plugin-security</artifactId>
            <version>1.0.0</version>
        </dependency>

在resource下面新建一个iframe.properties(这里没有使用xml)

    /*这里写数据库驱动和数据库*/
	iframework.jdbc.driver=com.mysql.jdbc.Driver 
    iframework.jdbc.url=jdbc:mysql://localhost:3306/demo_test
    iframework.jdbc.username=root
    iframework.jdbc.password=123
    /*这里写需要扫描的基础包*/
    basepackage=com.lz
	/*这里写jsp文件的路径*/
    jsppath=/WEB-INF/view/
	/*静态资源路径*/
    assetpath=/asset/
	/*下面是安全框架的配置可以不写*/
    iframe.plugin.security.realms=custom
    iframe.plugin.security.custom.class=com.lz.AppSecurity

这里配置log4j的配置文件：
    log4j.rootLogger=ERROR,console,file
    
    log4j.appender.console=org.apache.log4j.ConsoleAppender
    log4j.appender.console.layout=org.apache.log4j.PatternLayout
    log4j.appender.console.layout.ConversionPattern=%m%n
    
    log4j.appender.file=org.apache.log4j.DailyRollingFileAppender
    log4j.appender.file.File=${user.home}/logs/book.log
    log4j.appender.file.DatePattern='_'yyyyMMdd
    log4j.appender.file.layout=org.apache.log4j.PatternLayout
    log4j.appender.file.layout.ConversionPattern=%d{HH:mm:ss,SSS} %p %c (%L) - %m%n
    
    log4j.logger.com.lz=DEBUG
##初始化
1.如果使用的是servlet3.0(推荐)，这里直接会初始化，因为在我们的DispatcherServlet中已经使用注解配置。

2.如果非要使用Servlet3.0以下(强烈不推荐)，可以使用xml配置：

    <servlet>
        <servlet-name>DispatcherServlet</servlet-name>
        <servlet-class>org.framework.DispatcherServlet</servlet-class>
    </servlet>
    <servlet-mapping>
        <servlet-name>DispatcherServlet</servlet-name>
        <url-pattern>/*</url-pattern>
    </servlet-mapping>

## @Controller

这里对所有控制层都是用@Controller 下面简单的例子：

    @Controller
    public class CustomerController {
    
    @Inject
    private CustomerService customerService;
    
    /**
     * 进入 客户列表 界面
     */
    @Action("get:/customer")
    public View index() {
    List<Customer> customerList = customerService.getCustomerList();
    return new View("customer.jsp").addModel("customerList", customerList);
    }
    }

这里使用@Controller标识自己是控制层，也把自己加入了IOC容器。
##@Action
在上面的例子中，@Action是定义在方法之上的后面跟字符串，字符串的格式规定为：
HTTP方法:地址。

在ACtion方法中如果需要参数Param param 统一使用规定参数，如果要获取参数值，例如获取名为id的参数值：

    long id = param.getLong("id");

对于返回值有两种：

###1.Data:

对于Data来说是用于返回json数据，并不会返回页面。对于Data没有页面跳转只有Modle

###2.View:
对于View中path有两种一种是重定向，一种是转发。对于view中的model我们会在转发的时候才会填充model重定向不会填充。

具体使用view：

return new View("customer.jsp").addModel("customerList", customerList);

具体使用View在构造函数中传入："/customer.jsp"和"customer.jsp"

1.对于加前缀/的我们的处理时对其进行重定向。

2.对于不加的我们添加模型并且转发。

##@Service

使用@Service在类上注解，和Spring中用法一样。
@Service
public class CustomerService {
	
}
##@Transaction
使用事务管理

    @Transaction
    public boolean createCustomer(Map<String, Object> fieldMap, FileParam fileParam) {
        boolean result=DatabaseHelper.insertEntity(Customer.class, fieldMap);
        if(result){
            UploadHelper.uploadFile("/tmp/upload/", fileParam);
        }
        return result;
    }

在框架中是使用AOP完成事务管理。事务策略是每个方法都是独立的一个事务。
##@Inject
依赖注入，默认是使用名字判断依赖注入的策略：

    @Inject
    private CustomerService customerService;

##@Aspect
下面是一个例子，一个简单的@Aspect

    @Aspect(Controller.class)
    public class ControllerAspect extends AspectProxy {
    private static final Logger LOGGER= LoggerFactory.getLogger(ControllerAspect.class);
    private long begin;
    @Override
    public void before(Class<?> cls, Method method, Object[] params) throws Throwable {
    LOGGER.debug("---------- begin ----------");
    LOGGER.debug(String.format("class: %s", cls.getName()));
    LOGGER.debug(String.format("method: %s", method.getName()));
    begin = System.currentTimeMillis();
    }
    
    @Override
    public void after(Class<?> cls, Method method, Object[] params, Object result) throws Throwable {
    LOGGER.debug(String.format("time: %dms", System.currentTimeMillis() - begin));
    LOGGER.debug("----------- end -----------");
    }
    
    }

上面的例子是一个对所有有@Controller注解的类进行AOP,目前只支持类级别上的AOP。



