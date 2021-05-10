# Spirit

一种轻量的语言，可生成Java代码。

## 代码示例

```
func main() {
    print "hello world!"
}
```

生成代码如下：

```java
package com.sum.test.main;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Main {
    
    public static Logger logger = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) {
        logger.info("hello world!");
    }

}  
```

## 快速开始

1、在pom文件中，引入Maven插件。

```xml
<build>
	<plugins>
		<plugin>
			<groupId>com.sum.spirit</groupId>
			<artifactId>spirit-maven-plugin</artifactId>
			<version>最新版（>= 2.1.30）</version>
			<executions>
				<execution>
					<goals>
						<goal>compile</goal>
					</goals>
				</execution>
			</executions>
		</plugin>
	</plugins>
</build>
```

2、在src/main/resources目录下，创建sources子目录。在sources目录下，创建一个文件夹，作为包。例如：com.sum.spirit.example。

3、在刚创建的包下，创建Main.sp文本文件。内容如下：

```
func main() {
    print "hello world!"
}
```

4、执行Maven命令（mvn spirit:compile）并刷新，在src/main/java目录下，即可看到生成Java代码。

## 编辑器

1. 下载安装sublime编辑器，并将项目中sublime-plugs目录下的所有文件，拷贝到sublime插件目录下的User文件夹中。
2. 在sublime中打开后缀名为sp的文件，即可看到代码样式。

## Maven依赖

请确保项目中有如下依赖：

```
<!-- slf4j -->
<dependency>
	<groupId>org.slf4j</groupId>
	<artifactId>slf4j-api</artifactId>
	<version>${slf4j.version}</version>
</dependency>
<!-- lang -->
<dependency>
	<groupId>org.apache.commons</groupId>
	<artifactId>commons-lang3</artifactId>
	<version>${lang3.version}</version>
</dependency>
```

