----------
11/6/2017 7:29:16 PM 

----------
## Spring RoadMap ##
```
讲述Spring的各个核心思想
```

##  Inversion of Control Containers and the Dependency Injection pattern ## 

IOC/DI是Spring的核心思想，其在Spring生态圈中有着举足轻重的作用，地位崇高，今天我们就来看看IOC/DI缘起何处，路在何方。

在Java社区中曾掀起一股轻量级容器的潮流，它们致力于将不同项目中的组件装配到一个高内聚的应用中。其实，这些容器的底层使用了同一种设计模式，也就是通常所所说的IOC（控制反转）。在下面的内容中，将会探究下这种模式（依赖注入）是如何运作的，而且还会与Service Locator（服务定位器，也是一种IOC实现方式）进行对比。重要的并不是选择哪种实现方式，DI也好，Service Locator也罢，重要的是配置与使用分离的原则，这个思想才是IOC的精髓所在。

在Java的世界中，有一件非常有趣的现象，出现了很多主流或者说官方JavaEE技术的替代的开源的组件或活动。在很大程度上，有很多组件是出于对重量级且复杂的主流JavaEE技术的本能应对，但是也有很多是源自于创造性想法的替代产品。这些组件需要解决一个通用问题：如何将不同的元素连接并装配在一起。比如，如何将一个web控制器架构体系与数据库接口体系组装在一起，而这两个组件是由不同团队开发的，他们之间彼此并不认识。很多框架已经尝试解决这个问题，他们中有些提供一个通过不同分层来装配组件的通用能力，这些组件通常被称为轻量级容器，比如Spring和PicoContainer。


这些容器的底层是很多有意思的设计原则，接下来会介绍这里面的几个原则，示例代码使用Java，但是其中的大部分原则同样适用于其他OO环境，尤其是.NET。

### Comonents and Services ###

谈到连接元素就不得不说到Component（组件）和Service（服务）这两个令人困惑的技术术语，你可能读到很多定义这两个概念的文章，他们很多是冗长且矛盾的。以下就是我目前对这两个重载术语的使用。


我使用Component表示提供给其他应用程序使用的一系列软件（不会主动变化），而这个应用程序并不受Component开发者的控制，换句话说Component是个售出的成品（比如手机），用户直接使用，厂家并不知道用户如何使用。所谓的不会主动变化，我的意思是应用程序不会去改变Component的源码，但是可以通过Component提供的接口来改变Component的行为，进而实现对Component的扩展。

Service与Component的相近，它被用来提供给为外部程序你使用。我认为它们之间主要的不同在于，Component是本地使用（如jar文件，assembly, dll或 a source import），Service将通过远程接口远程使用，同步或异步都可以（比如web Service，messsaging system, RPC or socket）。

我在文章中会大量使用Service，但是其中的大部分逻辑也可以应用于本地Component。事实上，通常我们需要使用一些本地Component去访问远程的Service。由于Component or Service不易读写，所以就简写为Service，这样形式看上去更加“时髦”。


### A Naive Example ###

闲话少说，直接上代码。为了不至于让读者陷入复杂业务场景中，文中都使用极其简单的示例代码，但这足以让你get到其中的精髓。


在下面这个例子中我将编写一个Component，它有一个提供某个导演的电影的名称列表。我们使用一个方法就可以实现这个非常实用的功能，代码片段如下：

```java
public class MovieLister {
    private MovieFindler findler;
	...
    public Movie[] moviesDirectedBy(String args) {
        List<Movie> allMovies = findler.findAll();
        for (Iterator<Movie> it = allMovies.iterator(); it.hasNext(); ) {
            Movie movie = it.next();
            if (!(args).equals(movie.getDirector())) {
                it.remove();
            }
        }
        return allMovies.toArray(new Movie[allMovies.size()]);
    }
}
```

这段代码很简单，做了3件事：
1. 从findler对象（稍后介绍）**获取**所有电影的列表
2. **遍历**该列表，删除不是该导演的电影名称
3. **返回**剩余的电影名称列表（即该导演执导的电影）


我并不打算修正这段代码使其看上去更加高级，因为这是本文所要讲述的真实的要点，这至关重要。

这个要点就是这个**finder**对象，或者说如何将**finder**对象和**lister**对象关联起来。

这个例子有趣的地方在于我想要**movieDirectedBy**这个方法与电影如何被存储是完全没有依赖的或者说完全解耦的，所以这个方法引用了**finder**对象，而**finder**对象所要做的就是通过**finderAll**方法将数据响应给**lister**对象。我可以通过为finder定义一个接口来实现这一点：

```java
public interface MovieFindler {
    List<Movie> findAll();
}
```

现在所有的一切都很好的解耦，但是如果要正常使用，还需要顶一个具体类来实现findAll接口，在这个例子中我把创建代码放在lister类的构造函数中。

```java
public MovieLister(){
    findler = new ColonDelimitedMovieFinder("movies1.txt");
}
```

这个实现类的名称源于这是从一个内容是逗号分割的文件中获取数据，这样的命名意义明确，是一种良好的命名习惯。

现在，这个实现类只是我一个人用，这一切都很好。但是，如果我的朋友觉得我写的这个功能十分诱人并且想要拷贝一份我的代码呢？如果他们的电影列表也刚好存在用冒号分割的文件中并且文件名也是movie1.txt，这当然是极好的。如果他们的文件名不同，我们可以设计一个属性文件，将对应的文件名配置为在里面，然后从该属性文件中读取电影文件名，这样实现起来也很简单。但是，如果朋友们使用其他的存储介质：比如SQL数据库，XML文件或者使用Web Service，或者使用另一种格式的文本文档呢？这个时候，我们需要另一个类用于抓取数据。因为我们现在是面向接口编程，并不需要去修改**moviesDirectedBy**方法，但是，我们确实需要采取某种方式来获取正确的finder实现类对象。

![Figure 1: The dependencies using a simple creation in the lister class](https://i.imgur.com/Wn0Eyua.png)

从上图中我么你可以看到示例代码中的依赖关系，**MovieLister**依赖**MovieFinlder**接口和其实现**ColonDelimitedMovieFinder**。我们更喜欢让它只依赖接口，但是怎么才能创建出合适的对象呢？


根据上述描述，finder的实现不能再编译期就连接到lister中，因为我并不知道我的朋友使用哪种方式来获取数据。此外，我还希望我的lister可以使用所有的finder实现类而且这个实现将会在以后的某个点插入到lister中。那么，现在的问题就是，我应该如何创建这种连接使lister在finder对象实现一无所知的情况下还可以与finder实例对象对话以完成其工作。

将其扩展到一个真实系统中，我们肯能会有很多这样的服务或组件。在每一种情况下，我们都可以将使用到的组件抽象出接口，然后通过接口来交互（如果组件不是面向接口开发的，就使用适配器来进行适配，进而通过适配器进行交互）。但是如果我们想要以不同方式部署系统，就需要使用插件来处理与服务的交互，这样我们就可以在不同的发布环境中使用不同的实现了。

所以这个核心问题就是如何把这些插件装配到应用中呢？这是新型轻量级容器面临的主要问题之一，通常他们都是通过Inversion of Control（控制反转）来实现的。


### Inversion of Control ###

有些容器说自己是很有用的，原因竟然是因为他们实现了IOC，这让我困惑不已。IOC是框架的共有属性，所以说轻量级容器因为使用了IOC就变得特殊了，就像说我的汽车之所以不一样，是因为它有轮子，这听起来就挺滑稽的。


现在的问题是：什么方面的控制被反转了？当我第一次遇到IOC时，它出现在一个用户交互的控制界面中。早期的用户界面是由应用程序控制的，你将会看到一系列的指令，如“输入用户名”，“输入地址”；这个应用程序将会给出对应的提示，并对你的输入给出响应。使用图形化的或者基于屏幕的UI框架，框架包含对某事件的主循环过程，而应用程序提供对屏幕各个字段的事件处理器。在这里，对程序的主要控制被转了，从你转移到了框架。简而言之，用户只需要提供属性信息，处理过程交给了UI框架，比如用户只需要输入用户名和密码，点击登录即可，至于其中的用户名重复校验等等交给UI框架自动去完成，或者请求提交这个操作，不是由人敲回车实现交互了，而是交由UI框架去执行这个交互过程。


对于这种新型的容器，反转指的是如何查找一个接口实现。在上面的小例子中，lister对象通过直接实例化finder对象实现查找。这样的话，finder对象就不可插拔了。这些容器要求接口使用者遵循一些规定，从而可以将一些分离的模块实现注入到lister对象中。

因此，我们需要一个更加具体的名字来命名这个模式。IOC是一个太通用的术语，这让人感到困惑。最后经过IOC拥护者的大量讨论，最后决定使用Dependency Injection（依赖注入）这个名字来命名该模式。

接下来我们要讲的是依赖注入的各种实现形式，但是我要指出的是这并不是**从应用程序中移除依赖**转换为插件式的实现的唯一方式。另外一种可以实现这一点的是Service Locator,在介绍完DI之后，我将会继续介绍Service Locator。


### Forms of Dependency Injection ###

依赖注入的基本思想是有一个单独的对象（一个装配器），它可以从finder接口的实现中选择一个合适的对象填充从到lister对象的finder属性中，依赖图如下：

主要有三种注入方式：Contructor Injection（构造器注入）,Setter Injection（set方法注入）,Interface Injection（接口注入）。如果你以前阅读过IOC的介绍，你可能会听说过type 1控制反转（interface injection）,type 2控制反转（setter injection）和type 3控制反转（constuctor injection）。数字类型的名字不易区分，所以我这里使用更有表征意义的名字。

#### Constructor Injection with Spring ####

Spring是一个应用广泛的企业级框架，它包括事务抽象层，持久层框架，web应用开发和JDBC等。它支持构造方法和setter方法注入。

下面介绍一下Spring使用构造方法来实现将finder实现注入到lister类中。所以，MovieLister类需要声明一个构造方法，它包含所需要注入的所有东西。

```java
class MovieLister...

public MovieLister(MovieFindler findler) {
    this.findler = findler;
}
```

finder类本身也需要被Spring管理，text文件名也可以使用这个容器文件注入。

```java
public class ColonDelimitedMovieFinder implements MovieFindler {
    private String fileName;

    public ColonDelimitedMovieFinder(String fileName) {
        this.fileName = fileName;
    }

    @Override
    public List<Movie> findAll() {
        return null;
    }
}
```

然后，Spring需要被告知哪个接口和哪个实现类关联以及需要把哪个字符串注入到finder类。换句话说，我们需要告诉容器谁需要注入，至于如何注入，由容器去完成。

```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xsi:schemaLocation="http://www.springframework.org/schema/beans
        http://www.springframework.org/schema/beans/spring-beans.xsd">

    <bean id="movieLister" class="com.springframework.roadmap.ioc.demo_00002.MovieLister">
        <constructor-arg ref="movieFinder"/>
    </bean>

    <bean id="movieFinder" class="com.springframework.roadmap.ioc.demo_00002.ColonDelimitedMovieFinder">
        <constructor-arg value="F:\SourceCode\SpringRoadMap\src\test\resources\movie1.txt"/>
    </bean>
</beans>
```

```java
public void testConstructorInjectWithSpring() {
    ApplicationContext ctx = new FileSystemXmlApplicationContext("F:\\SourceCode\\SpringRoadMap\\src\\main\\resources" +
            "\\applicationContext.xml");
    MovieLister movieLister = (MovieLister) ctx.getBean("movieLister");
    Movie[] movies = movieLister.moviesDirectedBy("yoyo");

    System.out.println("===============" + "The Big Bang".equals(movies[0].getTitle()) + "===============");
}
```
我们会在另一个类中增加配置代码，使用MovieLister类的朋友可以在配置类中编写适合自己的配置代码。当然，通常会把这些配置信息以单独的配置文件的形式保存。你可以写一个类来读取这个配置文件并在恰当的时候把这些信息配置到容器中。Spring项目的设计哲学之一就是**分离配置**文件**与底层实现机制**.

#### Setter Injection with Spring ####

```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xsi:schemaLocation="http://www.springframework.org/schema/beans
        http://www.springframework.org/schema/beans/spring-beans.xsd">

    <bean id="movieLister" class="com.springframework.roadmap.ioc.demo_00003.MovieLister">
        <property name="movieFinder">
            <ref bean="movieFinder"/>
        </property>
    </bean>

    <bean id="movieFinder" class="com.springframework.roadmap.ioc.demo_00003.ColonDelimitedMovieFinder">
        <property name="fileName">
            <value>F:\SourceCode\SpringRoadMap\src\test\resources\movie1.txt</value>
        </property>
    </bean>
</beans>
```



```java
private static void testSetterInjectWithSpring() {
    ApplicationContext ctx = new FileSystemXmlApplicationContext("F:\\SourceCode\\SpringRoadMap\\src\\test\\resources\\applicationContext_00003.xml");
    MovieLister movieLister = (MovieLister) ctx.getBean("movieLister");
    Movie[] movies = movieLister.moviesDirectedBy("yoyo");
    System.out.println("===============" + "The Big Bang".equals(movies[0].getTitle()) + "===============");

}
```

#### Interface Injection ####
这个不做具体展开，想要进一步了解的可以看下文末的参考资料。





对于使用者而言，无论是constructor注入还是setter注入，使用上并没有区别，其依赖注入解析过程如下：
- 创建ApplicaionContxt并根据配置的元数据（文中applicationContext_*.xml）初始化bean。配置元素数据可以使用XML、Java Code或者注解。
- 对于每个bean，它的依赖可以使用属性、构造参数或者静态工厂方法参数来表达。当这个bean被创建的时候，它所依赖的bean会被自动创建
- 每个属性或构造参数是对需要在容器中实际设置的另一个bean的值或引用的定义
- 对于每个值类型的属性或构造参数可以从某种特殊的格式转化为其实际的类型。在Spring容器中，默认将字符串格式的值转换为内置类型，如int、long、String、boolean等等。
#### Using a Service Locator ####

文中使用依赖注入最重要的好处就是它移除了MovieLister类对具体MovieFindler实现的依赖。这样我就可以把这个MovieLister类提供给我的朋友，他们就可以在自己的环境中插入合适的实现（修改配置文件）。依赖注入并不是打破这种依赖的唯一方式，另外一种就是使用Service Locator（服务定位器）。

Service Locator的核心思想就是有一个对象，它知道如何获取应用所需要的所有服务。所以，一个应用的Service Locator有一个方法可以返回MovieLister需要的MovieFinder对象。当然，这只是稍微减轻了负担，我们仍然必须要将Service Locator注入到MovieLister，所以依赖图如下：

和注入方法一样，我们必须配置service locator。下面直接使用代码实现配置，也可以使用配置文件或注解形式。

```java
public class ServiceLocator {
    private MovieFindler movieFindler;
    private static ServiceLocator soleInstance;

    public static MovieFindler movieFinder() {
        return soleInstance.movieFindler;
    }

    public static void load(ServiceLocator arg) {
        soleInstance = arg;
    }

    public ServiceLocator(MovieFindler movieFindler) {
        this.movieFindler = movieFindler;
    }

}
```

```java
public class MovieLister {
    private MovieFindler movieFinder = ServiceLocator.movieFinder();


    public Movie[] moviesDirectedBy(String args) {
        List<Movie> allMovies = movieFinder.findAll();
        for (Iterator<Movie> it = allMovies.iterator(); it.hasNext(); ) {
            Movie movie = it.next();
            if (!(args).equals(movie.getDirector())) {
                it.remove();
            }
        }
        return allMovies.toArray(new Movie[allMovies.size()]);
    }

}
```

```java
private static void configure() {
    ServiceLocator.load(new ServiceLocator(new ColonDelimitedMovieFinder("F:\\SourceCode\\SpringRoadMap" +
            "\\src\\test\\resources\\movie1.txt")));
}

private static void testServiceLocator(){
    configure();
    MovieLister movieLister = new MovieLister();

    Movie[] movies = movieLister.moviesDirectedBy("yoyo");
    System.out.println("===============" + "The Big Bang".equals(movies[0].getTitle()) + "===============");
}
```
如上，使用了静态的单例对象做为ServiceLocator，从其中获取movieFindler。显然，这个ServiceLocator实现并不可以动态获取movieFindler，就是直接返回movieFinder属性的。并不建议这样设计，文中只是为了方便测试。

一个更高级的或者说更加优秀的服务定位器可以设计service locator子类并将该子类传递到注册表类的属性变量中。将ServiceLocator直接调用movieFinder属性的方法修改为调用静态方法，可以一个ThreadLocal变量来存储所有的子类。这样并不修改使用该SeviceLocator的客户端，而且是线程相关，可以根据需求使用对应的ServiceLocator实现。

```java
public class ServiceLocator {
    private MovieFindler movieFindler;
    private static ThreadLocal<ServiceLocator> threadLocal = new ThreadLocal<ServiceLocator>();

    public static MovieFindler movieFinder() {
        return getMovieFinder();
    }

    public static void load(ServiceLocator arg) {
        threadLocal.set(arg);
    }

    public ServiceLocator(MovieFindler movieFindler) {
        this.movieFindler = movieFindler;
    }

    public static MovieFindler getMovieFinder() {
        return threadLocal.get().movieFindler;

    }

}
```

```java
private static void configure() {
    ServiceLocator.load(new SubServiceLocator(new ColonDelimitedMovieFinder("F:\\SourceCode\\SpringRoadMap" +
            "\\src\\test\\resources\\movie1.txt")));
}

```
从上面代码可以看到，只是修改了配置configure，客户端调用并不需要修改。而且使用ThreadLocal实现了类似注册表的机制，这样该线程后续使用的就是该注册的Service Locator。


#### Using a Segregated Interface for the Locator ####

细心的的读者可能会发现，MovieLister需要依赖一个完整的服务器定位类，而你可能只需要其中一个服务。我们可以使用role interface来减少服务依赖，换句话说，MovieLister可以声明它需要用到的服务，而不用使用全部的服务定位器接口。

在这种情形下，我们可以声明一个定位接口，它提供一个获取finder的方法。


```java
public interface MovieFindlerLocator {
    MovieFindler movieFindler();
}
```


```java
public class ServiceLocator implements MovieFindlerLocator {
...
    @Override
    public MovieFindler movieFindler() {
        return getMovieFinder();
    }
}
```

```java
public class MovieLister {
    private MovieFindler movieFinder ;
    public void setMovieFindlerLocator(MovieFindlerLocator movieFindlerLocator) {
        this.movieFinder = movieFindlerLocator.movieFindler();
    }
...

}
```

读者可以看到，因为我们使用了接口，我们不可以再通过静态方法来访问服务。我们不惜获取一个locator实例，然后通过它来获取我们需要的。

#### A Dynamic Service Loctor ####

以上的例子都是静态的，通过一个service locator提供的方法获取我们需要的数据。显然，还有其他方式可以用，你可以编写一个动态的service locator，它可以允许你存储你需要的任何服务，而且你可以在运行时指定你需要哪一个。

在下面的例子中，service locator提供使用map来存储每一个服务，而不是使用一个属性，并且提供获取服务的通用方法。

```java
public class ServiceLocator {
    private Map services = new ConcurrentHashMap();
    private MovieFindler movieFindler;

    private static ServiceLocator soleInstance;

    public static Object getService(String key) {
        return soleInstance.services.get(key);
    }


    public static void loadService(String key, Object value) {
        soleInstance.services.put(key, value);
    }

    public static void load(ServiceLocator locator) {
        soleInstance = locator;
    }

}
```

```java


private static void configure_2() {
    ServiceLocator locator = new ServiceLocator();
    ServiceLocator.load(locator);

    ServiceLocator.loadService("MovieFindler", new ColonDelimitedMovieFinder("F:\\SourceCode\\SpringRoadMap" +
            "\\src\\test\\resources\\movie1.txt"));

}

```

```java

public class MovieLister {
    private MovieFindler movieFinder = (MovieFindler) ServiceLocator.getService("MovieFindler");
...
}
```

总的来说，上面这个例子提供了一种获取服务的新思路，但是需要通过一个String类型的key来获取服务，这显然不够标准。我更希望使用严格的方法定义来获取服务，因为这个很容易从接口定义中查找想要的服务。


### Deciding which option to use ###

现在我已经介绍了IOC模式及其变种，现在我们开始讨论它们的优缺点，进而帮助我们判断什么时候应该使用哪种实现。

#### Service Locator vs Dependency Injection ####

两者都提供了解耦功能，面向接口开发，具体实现与接口分离。两者主要的不同在于，提供给应用的使用方式。使用Service Locator时，应用需要显式的通过locator定位服务，而DI并不需要显式的请求，而是利用容器来实现服务注入进而实现了控制反转。

IOC是框架的一个通用特性，但是在获取IOC有点的同时也需要付出对应的代价。尤其是当你debug时很难理解并且找到问题产生的原因。所以，我们在使用时要根据自身需求，审慎的选择。

两者之间的一个关键不同是每一个应用都对Service Locator有依赖性，但是Service Locator隐藏了对其他实现的依赖。所以两者之间的选择取决于依赖是不是一个问题。

使用DI可以帮助我们更容易看到组件的依赖是什么，现在的IDE可以很容易查找DI的依赖是什么并找到这些依赖。但是，如果应用需要的东西都可以通过一个或几个服务来完成，那么依赖就不是问题，DI的也就没什么优势了。

因为有了DI容器，你可以不必处理组件到容器的依赖关系，但是一旦配置好容器，组件就不能从容器中获取进一步的服务。

#### Constructor versus Setter Injection ####

当我们需要组合使用一些服务的时候，为了把他们组装到一起，我们需要制定一些规则。DI的优势在于它的规则很简单，比如coustructor和setter注入，你不必去让组件做一些古怪的事情而且它的配置很简单。与之相对的接口注入，则并不赞成使用，使用这种方式的注入，需要制定过多的接口，且不易维护。

constructor和setter注入反映出面向对象编程的一个普遍问题，你需要通过setter或constructor填充属性。使用constructor构造注入可以在一开始就制定需要使用的属性，封装性更好。相对而言，setter过多则显得冗余，但是参数过多的构造函数注入也是很恐怖的，这个时候就需要把对象进行拆分了。

构造函数注入依赖参数的未知，如果都是相同类型，则易出错，在这一方面，setter注入就没有这个困扰，注入更加具有指向性。所以，现在的大部分框架两种注入都支持的，比如Spring，根据需求选择合适的注入方式即可。



自动注入VS手动注入

#### Code or configuration files ####

有一个常见的问题：1.使用配置文件来装配服务 2.使用代码API装配服务。因为大部分应用可能被发布到不同场景，一个分离的配置文件还是很有意义的。很多时候这可能是xml文件，但是有些场景下直接使用代码来装配更容易。当我们的应用比较简单而且发布场景也比较单一的情况下，使用代码比XML文件更清晰自然。


尤其在装配时需要根据条件装配不同的对象时，使用代码比使用XML文件指定依赖更容易且更易维护。当然也可以使用一个简单的XML配置文件，然后编写创建类，根据配置文件来选择如何装配。


很多时候，人们总是急于定义配置文件。其实，一般来说，编程语言可以制定简单而强大的配置机制。现代编程语言可以容易的编写装配器来把插件装配到大型系统，如果编译是个问题，我们还可以使用脚本语言，如python等。

有一个不和谐的问题，那就是每个组件维护一套自己的配置文件，而使用者需要保持这些配置文件的同步正确，这很让人困扰。


我的建议是使用编程接口来进行配置，另外，再提供一个配置文件来关联编程接口，这样我们就可以进行简单的配置就可以实现功能。在设计层面，SpringBoot简化依赖及构建过程的思想及去配置化的思路有异曲同工之妙。


#### Separating Configuration from Use ####

配置与业务逻辑分离这与面向接口开发的OO思想殊途同归，这样更易解耦与扩展。使用多态而不是条件判断来决定实例化哪一个对象。


配置机制可以用来配置Service Locator或使用DI方式来直接配置对象。配置机制可以让应用适配不同的组件，就像一个适配器或插件，让我们的应用更易组合不同组件并适配不同的环境。

### Concluding Thoughts ###

当下的轻量级容器底层的原理多是有一个共性：如何装配服务/组件（即依赖注入模式）。

相对而言，Service Locator指向性更加明确，但是如果开发的组件是供不同应用使用的情况下，DI则更合适。

最重要的是将服务/组件的**配置与**服务/组件的**使用分离**的原则及思想，这比在DI和Service Locator之间选择更有价值。



**Spring 部分依赖图**：

![](https://i.imgur.com/uaWYWdD.png)

**PS：**
Spring处理bean步骤：
- 分离出配置文件
- 读取配置文件
- 根据配置文件生成对象工厂
- 使用对象时，从工厂中取（注解或XML声明实现）而无需自己去new
- 对象都是由容器在启动时或使用时给new好了，直接声明使用（注解或XML文件配置），无需自己new
Spring核心思想：
- 对于一个Java Bean来说，如果你依赖别的Bean ,只需要声明（注解或XML配置）即可，spring 容器负责把依赖的bean 给“注入进去“， 这就是控制反转(IoC)
- 如果一个Java Bean需要一些像事务，日志，安全这样的通用的服务，也是只需要声明（注解或XML配置）即可， spring 容器在运行时能够动态的“织入”这些服务， 这就是AOP

**参考书籍：**
Expert One-on-One J2EE Design and Development
Expert one on one J2EE development withoutEJB














	
	

	







## 参考资料 ##

[https://martinfowler.com/articles/injection.html](https://martinfowler.com/articles/injection.html "Inversion of Control Containers and the Dependency Injection pattern")