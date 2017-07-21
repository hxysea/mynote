----------
10/24/2017 2:42:02 PM 

----------

## 聊聊Spring AOP ##

1. 开场白
	现在主流的开发思想是OOP（面向对象编程），其中抽象、封装、继承、多态是其最重要的四个特性。而AOP（面向切面编程）是对OOP的增强和补充，其本质上也是对抽象和封装思想的运用。在OOP中以类（Class）作为我们的基本单元，在AOP中的基本单元是Aspect(切面)。

2. AOP的核心术语
	- Join Point 连接点
	- Aspect 切面
		- Point Cut 切点
		- Advice  增强

3. 一个例子
	话说悠悠今天心情不错，准备亲自下厨做个红烧肉犒劳一下最近忙碌的自己。于是，一下班就冲到了菜市场的生肉铺，左挑右选，终于看上一块**肥瘦合适**的肉。一脸真诚的对老板说，‘老板，从这来一刀’。于是乎，老板手起**刀**落，一块**肉**就给切下来了，然后打包。

	对以上这个小例子稍作转换，就是“说人话”...
	- Joint Point
		- 整块猪肉每个地方都可以作为连接点，类比程序运行的方法点
	- Aspcect(刀--类比封装的切面逻辑)
		- Point Cut
			- 肉肥瘦合适的地方（哪些连接点可执行切面增强逻辑）
		- Advice
			- 打包（增强逻辑）

	简单来说，OOP是业务逻辑开发的一个解决方案，而AOP是对原有业务逻辑增加的功能需求解决方案。AOP思想的实现模型已有过滤器、拦截器等，而今天要说的是更加灵活的AOP实现方式。通俗来说，就是把需要增加的附加功能（日志记录，权限校验，声明式事务等）**抽象、封装**为一个切面，然后设计某种规则将其嵌入到业务逻辑中。而业务逻辑执行方法的点定义为Join Point,将切面分为两部分：Point Cut（定义需要切入的Join Point规则）和Advice（所需增强逻辑功能）

4. 实战——AOP应用之日志记录
	- 环境
		- **Spring Boot 1.5.8**
		- **IntelliJ Idea 14.1.7**
		- **JDK 1.8.0**
	- What is it?
		- 可以简单的认为，使用@Aspect注解的类就是切面
		- 在Spring AOP中，Join Point总是方法的执行点，即只有方法连接点
		- Point Cut提供一组规则来匹配Joint Point，给满足规则的Join Point 添加Advice
		- Advice在Join Point上执行，Point Cut规定了哪些Joint Point可以执行Advice
	- How to do it?
		- 定义一个注解用来标识需要切面的方法
		- 定义一个切面——封装advice逻辑
		- 定义PointCut规则——即使用注解标识的方法
		- 编写advice逻辑
	- Why?
		- 注解标识
		- Spring IOC和AOP实现

```java

@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface LogInfo {
}

```

```java
@Aspect 
@Component
public class LogAspect {
    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Pointcut("@annotation(com.springframework.roadmap.aop.demo_00001.LogInfo)")
    public void pointcut() {

    }

    @Before("pointcut()")
    public void logMethodInvokeParam(JoinPoint joinPoint) {
        logger.info("--start to execute method {} invoke, param: {}---", joinPoint.getSignature().toShortString(),
                joinPoint
                        .getArgs());

    }

    @After("pointcut()")
    public void logMethodInvokeAfter() {
        logger.info("-------after--------\n");
    }

    @AfterReturning(pointcut = "pointcut()", returning = "retVal")
    public void logMethodInvokeResult(JoinPoint joinPoint, Object retVal) {

        logger.info("---end to execute method {} invoke,  and args: {}, result:{}---", joinPoint.getSignature()
                        .toShortString(),
                joinPoint.getArgs(), retVal);

    }

    @AfterThrowing(pointcut = "pointcut()", throwing = "exception")
    public void logMethodInvokeException(JoinPoint point, Exception exception) {

        logger.info("-----------------throw()----------\n");

    }

    @Around(value = "pointcut()")
    public void logMethodInvokeAround(ProceedingJoinPoint joinPoint) {

        try {
            Object result = joinPoint.proceed();
            logger.info("---end to execute method {} invoke and result:{}",joinPoint.getSignature().toLongString(),
                    result);
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }

    }
}



```

```java

@Component
public class LogService {

    @LogInfo
    public String logTest(int x, String str) {
      System.out.println("================test=========");

        return "test ok!";
    }
}

```


```java

@SpringBootApplication
@EnableAspectJAutoProxy
@Component
public class LogAOPTest {

    @Resource
    private LogService logService;

    private Logger logger = LoggerFactory.getLogger(getClass());

    public static void main(String[] args) {

        SpringApplication.run(LogAOPTest.class, args);

    }

    @PostConstruct
    public void aopTest() {
        logService.logTest(123, "aop");
    }
}
```


```xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>

    <groupId>com.springframework</groupId>
    <artifactId>spring-roadmap</artifactId>
    <version>1.0-SNAPSHOT</version>

    <parent>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-parent</artifactId>
        <version>1.5.8.RELEASE</version>
    </parent>

    <dependencies>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-aop</artifactId>
        </dependency>

        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-web</artifactId>
        </dependency>

    </dependencies>

</project>
```

**参数资料：**
[https://segmentfault.com/a/1190000007469968](https://segmentfault.com/a/1190000007469968)
[https://segmentfault.com/a/1190000007469982](https://segmentfault.com/a/1190000007469982)



	