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

4. 在项目src/main/resources目录下，创建sources子目录。在sources目录下，创建一个文件夹。例如：com.gitee.spirit.example。

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

## 语法说明

### 类型声明

```go
class Horse {
    name = "xiaoma"
    age = 18
    func Horse(String name, int age) {
        this.name = name
        this.age = age
    }
    func bark() {
        print "I am xiaoma!"
    }
}
```

### 赋值语句

Spirit编译器会自动推导类型，所以

```go
name = "xiaoma"
```

等效于

```go
String name = "xiaoma"
```

### 方法声明

Spirit编译器会自动推导类型，所以

```go
func getName() {
    return name
}
```

等效于

```go
String getName() {
    return name
}
```

### 构造方法

一般构造方法（省略了new关键字）

```
horse1 = Horse("xiaoma", 18)
```

builder模式（只要有默认构造方法，可以初始化任意属性，请配合lombok使用）

```
horse2 = Horse{name = "laoma", age = 38}
```

### 集合声明

```go
list = ["xiaoma", "laoma"]
```

```go
map = {"xiaoma":horse1, "laoma":horse2}
```

### if判断

Spirit重载了字符串的“==”操作符，所以

```go
if str == "xiaoma" {
    print str
}
```

等效于

```go
if StringUtils.equals(str, "xiaoma") {
    print str
}
```

### for循环

普通

```go
for (i=0; i < list.size(); i++) {
    print "The item is {}", list.get(i)
}
```

快速

```go
for str in list {
    print "The item is {}", str
}
```

### 异常处理

```go
try {
    throw Exception("Exception occurred!")
} catch Exception e {
    error "Exception occurred!", e
}
```

### Json构造

Spirit的map声明方式，恰好是标准的Json的格式，所以，你可以这样写：

```go
jsonMap = {
    "name":"xiaoma",
    "age":18,
    "brother":["dama", "zhongma"]
    "father":{"laoma":horse2}
}
print JSON.toJSONString(jsonMap)
```

## 未来

### 智能builder模式

能够根据上下文，分析出类型，并解析“{}”内的表达式，智能构造实例。

```go
list = mapper.selectByExample(${name = "xiaoma", age >= 18})
```

