# Spirit

一种轻量的语言，可生成Java代码。

## 代码示例

```
func main(){
    print "hello world!"
}
```

生成代码如下：

```
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

1. 下载安装sublime编辑器，并将项目中sublime-plugs目录下的所有文件，拷贝到sublime插件目录下的User文件夹中。
2. 下载项目中的spirit文件夹，到任意目录下。（spirit文件夹内为打包产物）
3. 在eclipse中，借助maven命令导出工程依赖项，并拷贝到spirit/lib_dep文件夹中。(Run As->Maven-build->dependency:copy-dependencies)
4. 双击运行spirit/bin/startup.bat，并在target目录下查看生成的示例代码。

## Maven依赖

生成的代码可以直接拷贝到maven项目中，但项目中必须包含以下依赖。

```
<!-- guava -->
<dependency>
	<groupId>com.google.guava</groupId>
	<artifactId>guava</artifactId>
	<version>${guava.version}</version>
</dependency>
<!-- lang -->
<dependency>
	<groupId>org.apache.commons</groupId>
	<artifactId>commons-lang3</artifactId>
	<version>${lang3.version}</version>
</dependency>
<!-- slf4j -->
<dependency>
	<groupId>org.slf4j</groupId>
	<artifactId>slf4j-api</artifactId>
	<version>${slf4j.version}</version>
</dependency>
```

