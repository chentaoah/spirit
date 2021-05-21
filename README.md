# Spirit

一种轻量的转译型语言，可生成Java代码。它致力于减少Java代码的冗余，并提供流畅的开发体验。它能够和Java代码无缝集成，并减少60%的代码量。

## 代码示例

### 原始代码

```go
func main() {
    print "hello world!"
}
```

### 目标代码

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

### 依赖配置

在pom.xml中引入以下jar包：

```xml
<!-- spirit-stdlib -->
<dependency>
	<groupId>com.gitee.chentaoah</groupId>
	<artifactId>spirit-stdlib</artifactId>
	<version>最新版（>= 2.1.30）</version>
</dependency>
<!-- slf4j-api -->
<dependency>
	<groupId>org.slf4j</groupId>
	<artifactId>slf4j-api</artifactId>
	<version>1.7.25</version>
</dependency>
<!-- commons-lang3 -->
<dependency>
	<groupId>org.apache.commons</groupId>
	<artifactId>commons-lang3</artifactId>
	<version>3.9</version>
</dependency>
```

### 代码编辑

1. 下载并安装sublime编辑器。

2. 打开sublime，点击上方Preferences->Browse Packages按钮。

3. 将spirit项目中sublime-plugs目录下的所有文件，拷贝到User文件夹中。

4. 在项目src/main/resources目录下，创建sources子目录。在sources目录下，创建一个文件夹。例如：com.sum.spirit.example。

5. 在文件夹下，创建Main.sp文件，并在sublime中打开，输入如下内容：

   ```go
   func main() {
       print "hello world!"
   }
   ```

### 代码编译

1. 在pom.xml中引入插件。

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

2. 执行Maven命令（mvn spirit:compile）并刷新，在src/main/java目录下，即可看到生成的Java代码。



