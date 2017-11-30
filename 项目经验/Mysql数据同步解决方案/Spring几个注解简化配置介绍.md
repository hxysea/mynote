----------
11/30/2017 4:54:50 PM 

----------
## 聊聊Spring这两个注解的故事 ##
### 环境 ###
- JDk8
- Spring Boot 1.5.8
- IntelliJ Idea 14.1.7
### What is this annotation ###
1. @ Configuration
```java
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Component
public @interface Configuration {
	String value() default "";
}
```

2. @ Bean 
```java
@Target({ElementType.METHOD, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Bean {
	@AliasFor("name")
	String[] value() default {};

	@AliasFor("value")
	String[] name() default {};

	Autowire autowire() default Autowire.NO;

	String initMethod() default "";

	String destroyMethod() default AbstractBeanDefinition.INFER_METHOD;

}

```

### How to use this annotation ###
```java
 @Configuration
 public class AppConfig {

     @Bean
     public MyBean myBean() {
         // instantiate, configure and return bean ...
     }
 }
```

### Why these can work ###

1. **简化XML配置**,IOC配置从文件配置**转向Java文件配置**，通常以*Config命名此类Java类，如AppConfig等
2. AnnotationConfigApplicationContext
	- @ Configuration
	- @ Bean
3. Spring容器会解析@ Bean注解方法，并注册方法返回实例对象到容器中，默认以方法名命名。
4. @ Bean中定义的属性名与XML的<bean/>标签对应
5. 看下AnnotationConfigApplicationContext源码:
-  AnnotationConfigApplicationContext(Class<?>... annotatedClasses):
这是最常用的构造函数，通过将涉及到的配置类传递给该构造函数，以实现将相应配置类中的 Bean 自动注册到容器中。
- AnnotationConfigApplicationContext(String... basePackages)：该构造函数会自动扫描以给定的包及其子包下的所有类，并自动识别所有的 Spring Bean，将其注册到容器中。它不但识别标注 @ Configuration的配置类并正确解析，而且同样能识别使用 @ Repository、@ Service、@ Controller、@ Component 标注的类。
```java
public class AnnotationConfigApplicationContext extends GenericApplicationContext implements AnnotationConfigRegistry {
...
	public AnnotationConfigApplicationContext(Class<?>... annotatedClasses) {
		this();
		register(annotatedClasses);
		refresh();
	}

	public AnnotationConfigApplicationContext(String... basePackages) {
		this();
		scan(basePackages);
		refresh();
	}
...
	public void scan(String... basePackages) {
		Assert.notEmpty(basePackages, "At least one base package must be specified");
		this.scanner.scan(basePackages);
	}
}

```
- Spring Boot加载Bean流程
	- main()
		- SpringApplication
			- run()
				- createApplicationContext()
				- prepareContext
				- refreshContext()
					- AbstractApplicationContext --> refresh()
						- finishBeanFactoryInitialization()
							- beanFactory.preInstantiateSingletons()
								- AbstractBeanFactory->getBean()
								- AbstractBeanFactory->doGetBean()
									- AbstractAutowireCapableBeanFactory->createBean()
										- AbstractAutowireCapableBeanFactory->doCreateBean
											- AbstractAutowireCapableBeanFactory->registerDisposableBeanIfNecessary()
				- afterRefresh()

```java
public class SpringApplication {
	public static final String DEFAULT_CONTEXT_CLASS = "org.springframework.context."
			+ "annotation.AnnotationConfigApplicationContext";

	public static final String DEFAULT_WEB_CONTEXT_CLASS = "org.springframework."
			+ "boot.context.embedded.AnnotationConfigEmbeddedWebApplicationContext";

...
	context = createApplicationContext();
	analyzers = new FailureAnalyzers(context);
	prepareContext(context, environment, listeners, applicationArguments,
			printedBanner);
	refreshContext(context);
	afterRefresh(context, applicationArguments);
	listeners.finished(context, null);

}
```