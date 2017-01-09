maven install 跳过test方法

方式1：用命令带上参数

mvn install -Dmaven.test.skip=true

 

方式2：在pom.xml里面配置

<build>
<defaultGoal>compile</defaultGoal>
<plugins>
<plugin>
<groupId>org.apache.maven.plugins</groupId>
<artifactId>maven-surefire-plugin</artifactId>
<configuration>
<skip>true</skip>
</configuration>
</plugin>
</plugins>
</build>