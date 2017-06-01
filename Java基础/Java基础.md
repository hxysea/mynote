----------
5/17/2017 7:15:51 PM 

----------
## java.io.NotSerializableException: java.util.ArrayList$SubList异常解决办法 ##
1. 问题是什么？（What）
	- 未序列化异常
	- 由ArrayList的subList方法引起的
	- SubList类未实现序列化接口Serializable
2. 为什么会出现这种现象？（Why）
	- 查API
		- 返回的是List的一个视图非实体对象
		- 
	**List<E> subList(int fromIndex, int toIndex)**
	**Returns: a view of the specified range within this list**
Returns a view of the portion of this list between the specified fromIndex, inclusive, and toIndex, exclusive. (If fromIndex and toIndex are equal, the returned list is empty.) The returned list is backed(支持) by this list, so non-structural changes in the returned list are reflected in this list, and vice-versa. The returned list supports all of the optional list operations supported by this list.
	- 序列化需要使用的是实体对象故序列化时出现异常
3. 如何解决？（How）
	- new一个对象出来存储子列表数据
	```java
	List tmpList = new ArrayList<>(sourceList.subList(fromIndex, endIndex));
	```
4. 修复（Reult）
	- 根据错误日志定位缺陷代码位置-->修复
	- 验证功能
5. 源码
	- SubList是ArrayList的内部类
	- 此对象并未存储数据，只是可以操作原数据
	```java
	 public List<E> subList(int fromIndex, int toIndex) {
	        subListRangeCheck(fromIndex, toIndex, size);
	        return new SubList(this, 0, fromIndex, toIndex);
	    }
	
	 private class SubList extends AbstractList<E> implements RandomAccess {
	        private final AbstractList<E> parent;
	        private final int parentOffset;
	        private final int offset;
	        int size;
	
	        SubList(AbstractList<E> parent,
	                int offset, int fromIndex, int toIndex) {
	            this.parent = parent;
	            this.parentOffset = fromIndex;
	            this.offset = offset + fromIndex;
	            this.size = toIndex - fromIndex;
	            this.modCount = ArrayList.this.modCount;
	        }
	
	        public E set(int index, E e) {
	            rangeCheck(index);
	            checkForComodification();
	            E oldValue = ArrayList.this.elementData(offset + index);
	            ArrayList.this.elementData[offset + index] = e;
	            return oldValue;
	        }
	
	        public E get(int index) {
	            rangeCheck(index);
	            checkForComodification();
	            return ArrayList.this.elementData(offset + index);
	        }
	
	        public int size() {
	            checkForComodification();
	            return this.size;
	        }
	
	        public void add(int index, E e) {
	            rangeCheckForAdd(index);
	            checkForComodification();
	            parent.add(parentOffset + index, e);
	            this.modCount = parent.modCount;
	            this.size++;
	        }
	
	        public E remove(int index) {
	            rangeCheck(index);
	            checkForComodification();
	            E result = parent.remove(parentOffset + index);
	            this.modCount = parent.modCount;
	            this.size--;
	            return result;
	        }
	
	        protected void removeRange(int fromIndex, int toIndex) {
	            checkForComodification();
	            parent.removeRange(parentOffset + fromIndex,
	                               parentOffset + toIndex);
	            this.modCount = parent.modCount;
	            this.size -= toIndex - fromIndex;
	        }
	
	        public boolean addAll(Collection<? extends E> c) {
	            return addAll(this.size, c);
	        }
	
	        public boolean addAll(int index, Collection<? extends E> c) {
	            rangeCheckForAdd(index);
	            int cSize = c.size();
	            if (cSize==0)
	                return false;
	
	            checkForComodification();
	            parent.addAll(parentOffset + index, c);
	            this.modCount = parent.modCount;
	            this.size += cSize;
	            return true;
	        }
	
	        public Iterator<E> iterator() {
	            return listIterator();
	        }
	
	        public ListIterator<E> listIterator(final int index) {
	            checkForComodification();
	            rangeCheckForAdd(index);
	            final int offset = this.offset;
	
	            return new ListIterator<E>() {
	                int cursor = index;
	                int lastRet = -1;
	                int expectedModCount = ArrayList.this.modCount;
	
	                public boolean hasNext() {
	                    return cursor != SubList.this.size;
	                }
	
	                @SuppressWarnings("unchecked")
	                public E next() {
	                    checkForComodification();
	                    int i = cursor;
	                    if (i >= SubList.this.size)
	                        throw new NoSuchElementException();
	                    Object[] elementData = ArrayList.this.elementData;
	                    if (offset + i >= elementData.length)
	                        throw new ConcurrentModificationException();
	                    cursor = i + 1;
	                    return (E) elementData[offset + (lastRet = i)];
	                }
	
	                public boolean hasPrevious() {
	                    return cursor != 0;
	                }
	
	                @SuppressWarnings("unchecked")
	                public E previous() {
	                    checkForComodification();
	                    int i = cursor - 1;
	                    if (i < 0)
	                        throw new NoSuchElementException();
	                    Object[] elementData = ArrayList.this.elementData;
	                    if (offset + i >= elementData.length)
	                        throw new ConcurrentModificationException();
	                    cursor = i;
	                    return (E) elementData[offset + (lastRet = i)];
	                }
	
	                public int nextIndex() {
	                    return cursor;
	                }
	
	                public int previousIndex() {
	                    return cursor - 1;
	                }
	
	                public void remove() {
	                    if (lastRet < 0)
	                        throw new IllegalStateException();
	                    checkForComodification();
	
	                    try {
	                        SubList.this.remove(lastRet);
	                        cursor = lastRet;
	                        lastRet = -1;
	                        expectedModCount = ArrayList.this.modCount;
	                    } catch (IndexOutOfBoundsException ex) {
	                        throw new ConcurrentModificationException();
	                    }
	                }
	
	                public void set(E e) {
	                    if (lastRet < 0)
	                        throw new IllegalStateException();
	                    checkForComodification();
	
	                    try {
	                        ArrayList.this.set(offset + lastRet, e);
	                    } catch (IndexOutOfBoundsException ex) {
	                        throw new ConcurrentModificationException();
	                    }
	                }
	
	                public void add(E e) {
	                    checkForComodification();
	
	                    try {
	                        int i = cursor;
	                        SubList.this.add(i, e);
	                        cursor = i + 1;
	                        lastRet = -1;
	                        expectedModCount = ArrayList.this.modCount;
	                    } catch (IndexOutOfBoundsException ex) {
	                        throw new ConcurrentModificationException();
	                    }
	                }
	
	                final void checkForComodification() {
	                    if (expectedModCount != ArrayList.this.modCount)
	                        throw new ConcurrentModificationException();
	                }
	            };
	        }
	```   
**PS:**
- ArrayList重写了readObject和writeObject所以序列化和反序列化时使用这两个方法
- 这也是为什么private **transient** Object[] elementData;也可以序列化数据（transient语义是序列化时不序列化此属性），由于覆写了读写对象的方法，则序列化（反序列化）时使用此读写对象方式

	```java
	/**
	* The array buffer into which the elements of the ArrayList are stored.
	* The capacity of the ArrayList is the length of this array buffer.
	*/
	private transient Object[] elementData;
	/**
	* The size of the ArrayList (the number of elements it contains).
	*
	* @serial
	*/
	private int size;
	
	/**
	* Reconstitute the <tt>ArrayList</tt> instance from a stream (that is,
	* deserialize it).
	*/
	private void readObject(java.io.ObjectInputStream s)
	throws java.io.IOException, ClassNotFoundException {
	// Read in size, and any hidden stuff
	s.defaultReadObject();
	
	// Read in array length and allocate array
	int arrayLength = s.readInt();
	Object[] a = elementData = new Object[arrayLength];
	
	// Read in all elements in the proper order.
	for (int i=0; i<size; i++)
	a[i] = s.readObject();
	}
	
	private void writeObject(java.io.ObjectOutputStream s)
	throws java.io.IOException{
	// Write out element count, and any hidden stuff
	int expectedModCount = modCount;
	s.defaultWriteObject();
	
	// Write out array length
	s.writeInt(elementData.length);
	
	// Write out all elements in the proper order.
	for (int i=0; i<size; i++)
	s.writeObject(elementData[i]);
	
	if (modCount != expectedModCount) {
	throw new ConcurrentModificationException();
	}
	
	}
	```



----------
5/31/2017 7:20:00 PM 

----------
## Java注解赏析及自定义注解 ##
1. 看看注解什么样？
	```java
	@Target(ElementType.METHOD)
	@Retention(RetentionPolicy.SOURCE)
	public @interface Override {
	}
	
	
	
	@Documented
	@Retention(RetentionPolicy.RUNTIME)
	@Target(value={CONSTRUCTOR, FIELD, LOCAL_VARIABLE, METHOD, PACKAGE, PARAMETER, TYPE})
	public @interface Deprecated {
	}
	
	
	@Target({TYPE, FIELD, METHOD, PARAMETER, CONSTRUCTOR, LOCAL_VARIABLE})
	@Retention(RetentionPolicy.SOURCE)
	public @interface SuppressWarnings {
	    String[] value();
	}
	```
2. 注解到底是什么？
	- 是一个类
	- 是一种标记，使用@interface标识
	- 使用@xxx的形式标注其他类，java注解是**附加**在代码（注解）中的一些元信息，用于一些工具在**编译、运行**时进行**解析和使用**，起到**说明、配置**的功能，用来**描述类或属性**
	- 注解不会也不能影响代码的实际逻辑，仅仅起到辅助性的作用
3. JDK中的注解及相关讲解
	注解分为三类：
	- 元注解
		- 定义在java.lang.annotation包中,对其他注解进行注解
		- @Documented 	标记生成javadoc
		- @Retention 	注解的生命周期，使用枚举类RetentionPolicy标识
			- @Retention(RetentionPolicy.SOURCE)   //注解仅存在于源码中，在class字节码文件中不包含
			- @Retention(RetentionPolicy.CLASS)     // 默认的保留策略，注解会在class字节码文件中存在，但运行时无法获得，
			- @Retention(RetentionPolicy.RUNTIME)  // 注解会在class字节码文件中存在，在运行时可以通过反射获取到
			```java
			public enum RetentionPolicy {
			    /**
			     * Annotations are to be discarded by the compiler.
			     */
			    SOURCE,
			
			    /**
			     * Annotations are to be recorded in the class file by the compiler
			     * but need not be retained by the VM at run time.  This is the default
			     * behavior.
			     */
			    CLASS,
			
			    /**
			     * Annotations are to be recorded in the class file by the compiler and
			     * retained by the VM at run time, so they may be read reflectively.
			     *
			     * @see java.lang.reflect.AnnotatedElement
			     */
			    RUNTIME
			}
			```
		- @Target 	注解的目标
			- 如@Target({TYPE, FIELD, METHOD, PARAMETER, CONSTRUCTOR, LOCAL_VARIABLE})
		- @Inherited 	注解的继承关系
	- 标准注解
		- @Override 		标识为覆写超类方法
		- @Deprecated 	标识为弃用的类/方法
		- @SuppressWarnings 	标识抑制编译器发出特定警告
	- 一般注解
		- @Component
		- @Service
		- 自定义注解

			```java
			package com.abuge.demo00001;
			
			import java.lang.annotation.Documented;
			import java.lang.annotation.ElementType;
			import java.lang.annotation.Retention;
			import java.lang.annotation.RetentionPolicy;
			import java.lang.annotation.Target;
			/**
			 * 
			 * 自定义注解，用于描述字段、方法信息
			 *
			 */
			@Retention(RetentionPolicy.RUNTIME) // 注解会在class字节码文件中存在且在运行时可以通过反射获取到
			@Target({ ElementType.FIELD, ElementType.METHOD }) // 定义注解的作用目标及作用范围为声明的属性（包括枚举常量）、方法
			@Documented // 该注解被包含在javadoc中
			public @interface FieldMeta {
			
				boolean id() default false; //是否为序列号
			
				String name() default ""; //字段名称
			
				boolean editable() default true; //是否可编辑
			
				boolean summary() default true; //是否在列表中显示
			
				String description() default ""; //字段描述
			
				int order() default 0; //排序字段
			
			}
			
			
			package com.abuge.demo00001;
			/**
			 * 使用注解
			 *
			 */
			public class Anno {
				@FieldMeta(id = true, name = "序列号", order = 1)
				private int id;
				@FieldMeta(name = "姓名", order = 3)
				private String name;
				@FieldMeta(name = "年龄", order = 2)
				private int age;
			
				@FieldMeta(description = "描述", order = 4)
				public String desc() {
					return "java反射获取方法的注解测试";
				}
			
				public int getId() {
					return id;
				}
			
				public void setId(int id) {
					this.id = id;
				}
			
				public String getName() {
					return name;
				}
			
				public void setName(String name) {
					this.name = name;
				}
			
				public int getAge() {
					return age;
				}
			
				public void setAge(int age) {
					this.age = age;
				}
			}
			
			
			package com.abuge.demo00001;
			
			/**
			 * 
			 * 字段/方法注解业务类
			 *
			 */
			public class SortableField {
			
				private FieldMeta meta;
			
				private String name;
			
				private Class<?> type;
			
				public SortableField() {
				}
			
				public SortableField(FieldMeta meta, String name, Class<?> type) {
					super();
					this.meta = meta;
					this.name = name;
					this.type = type;
				}
			
				public FieldMeta getMeta() {
					return meta;
				}
			
				public String getName() {
					return name;
				}
			
				public Class<?> getType() {
					return type;
				}
			
				public void setMeta(FieldMeta meta) {
					this.meta = meta;
				}
			
				public void setName(String name) {
					this.name = name;
				}
			
				public void setType(Class<?> type) {
					this.type = type;
				}
			
			}
			
			
			
			package com.abuge.demo00001;
			
			import java.lang.reflect.Field;
			import java.lang.reflect.Method;
			import java.lang.reflect.ParameterizedType;
			import java.util.ArrayList;
			import java.util.Collections;
			import java.util.Comparator;
			import java.util.List;
			/**
			 * 
			 * 注解信息获取基类
			 *
			 */
			public class Parent<T> {
			
				Class<T> entity;
			
				@SuppressWarnings("unchecked")
				public List<SortableField> init() {
			
					List<SortableField> fieldList = new ArrayList<SortableField>();
					// 获得超类的泛型参数的实际类型
					entity = (Class<T>) ((ParameterizedType) this.getClass().getGenericSuperclass()).getActualTypeArguments()[0];
			
					if (null != entity) {
						Field[] fields = entity.getDeclaredFields();
						for (Field field : fields) {
							FieldMeta meta = field.getAnnotation(FieldMeta.class);
							if (null != meta) {
								SortableField sortableField = new SortableField(meta, field.getName(), field.getType());
								fieldList.add(sortableField);
							}
						}
			
						Method[] methods = entity.getDeclaredMethods();
						for (Method method : methods) {
							FieldMeta meta = method.getAnnotation(FieldMeta.class);
							if (null != meta) {
								SortableField sortableField = new SortableField(meta, method.getName(), method.getReturnType());
			
								fieldList.add(sortableField);
							}
						}
			
						Collections.sort(fieldList, new Comparator<SortableField>() {
			
							@Override
							public int compare(SortableField o1, SortableField o2) {
								return o1.getMeta().order() - o2.getMeta().order();
							}
			
						});
					}
					return fieldList;
				}
			}
			
			
			package com.abuge.demo00001;
			
			public class Child extends Parent<Anno> {
			
			}
			
			
			package com.abuge.demo00001;
			
			import java.util.List;
			/**
			 * 测试类
			 * @author huxianyang
			 *
			 */
			public class AnnotaionTest {
				public static void main(String[] args) {
					Parent<?> child = new Child();
					List<SortableField> fields = child.init();
					for (SortableField f : fields) {
						System.out.println("字段名称：" + f.getName() + "\t字段类型：" + f.getType() + "\t\t注解名称：" + f.getMeta().name()
								+ "\t注解描述：" + f.getMeta().description());
					}
				}
			}
			
			
			```
