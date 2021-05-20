# Spirit

一种轻量的转译型语言，可生成Java代码。它致力于减少Java代码的冗余，并提供流畅的开发体验。它能够和Java代码无缝集成，并减少60%的代码量。

## 代码示例

### Spirit

```
func main() {
    print "hello world!"
}
```

### Java

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

### Maven依赖

在pom.xml中引入jar包：

```xml
<!-- spirit-stdlib -->
<dependency>
	<groupId>com.gitee.chentaoah</groupId>
	<artifactId>spirit-stdlib</artifactId>
	<version>${project.version}</version>
</dependency>
<!-- slf4j-api -->
<dependency>
	<groupId>org.slf4j</groupId>
	<artifactId>slf4j-api</artifactId>
	<version>${slf4j.version}</version>
</dependency>
<!-- commons-lang3 -->
<dependency>
	<groupId>org.apache.commons</groupId>
	<artifactId>commons-lang3</artifactId>
	<version>${lang3.version}</version>
</dependency>
```

### Sublime编辑器

1. 下载并安装sublime编辑器。
2. 打开sublime，点击上方Preferences->Browse Packages按钮。
3. 将项目sublime-plugs目录下的所有文件，拷贝到User文件夹中。
4. 打开后缀名为sp的文件，即可看到代码样式。

### Maven插件

1、在pom.xml中引入插件。

```xml
<build>
    <plugins>
        <plugin>
            <groupId>com.gitee.chentaoah</groupId>
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

