----------
7/18/2017 6:39:21 PM 

----------
**缘起**：
调试一段代码判断字符串是否相同时，咋一看去字符串分明是一样的，怎么就是不相等呢？debug一开，瞬间明朗，原来配置文件中多了一个空格。
1. 先看下String的equals方法的源码
```java
    public boolean equals(Object anObject) {
        if (this == anObject) {
            return true;
        }
        if (anObject instanceof String) {
            String anotherString = (String)anObject;
            int n = value.length;
            if (n == anotherString.value.length) {
                char v1[] = value;
                char v2[] = anotherString.value;
                int i = 0;
                while (n-- != 0) {
                    if (v1[i] != v2[i])
                        return false;
                    i++;
                }
                return true;
            }
        }
        return false;
    }
```
2. 讲下这段代码
	- ==判断是否是同一个对象，即比较引用地址
	- 判断对象类型是否为目标对象
	- 比较字符串长度是否相同
	- 注意比较各个字符是否相同

细看来这段断码写的也是颇有意味，先比较地址，再判断类型，最后比较各个字符，一气呵成，不带半分冗余。

3. 再看一下Java Bean自动生成的equals方法
```java

public class RequestInfo implements Serializable{
	private String ip;

	public String getIp() {
		return ip;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		RequestInfo that = (RequestInfo) o;

		return !(ip != null ? !ip.equals(that.ip) : that.ip != null);

	}

	@Override
	public int hashCode() {
		return ip != null ? ip.hashCode() : 0;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}



}

```

- 单独看一下这个equals方法
	- 与String的equals方法有异曲同工之妙
	- 先比较地址，然后是对象类类型，最后比较各属性值

```java

public boolean equals(Object o) {
	if (this == o)
		 return true;

	if (o == null || getClass() != o.getClass())
		 return false;

	RequestInfo that = (RequestInfo) o;

	return !(ip != null ? !ip.equals(that.ip) : that.ip != null);

}

```

4. 总结一下equals方法
	- 精髓在于从大到小注意缩小比较范围
	- 逐步确定两者是否相同
5. 番外
	- 常言道覆写equals时一定要吧hashcode也一并覆写掉
	- 那这是为什么呢？
6. 番外解惑
	- 既然是hashcode自然与集合关系密切，这也是其需一起覆写的原因所在
	- 正所谓源码之下无秘密，来看一段HashSet源码

```java
private transient HashMap<E,Object> map;
...
public boolean add(E e) {
    return map.put(e, PRESENT)==null;
}

public boolean remove(Object o) {
    return map.remove(o)==PRESENT;
}

```    

- 此时需要继续跟进到HashMap的源码
	- 看到static final int hash(Object key)这个方法，一切就得到了一个合理的解释
	- hash类集合对象在add/remove等操作时，需要根据hash值和equals方法共同确定待比较对象与集合对象是否相同
	- 想进一步了解HashMap的put过程可以参考源码深入研究


```java
 public V put(K key, V value) {
        return putVal(hash(key), key, value, false, true);
    }

 static final int hash(Object key) {
        int h;
        return (key == null) ? 0 : (h = key.hashCode()) ^ (h >>> 16);
    }

```

**PS:**
	- 如果在计算hashcode时使用了某对象属性，如果修改该属性后，再去remove Hash类集合对象时会失败，因为修改属性值后，hashcode也就改变了，集合根据hashcode和equals方法判定此对象不存在

**参考：**
	- [http://blog.csdn.net/afgasdg/article/details/6889383](http://blog.csdn.net/afgasdg/article/details/6889383)