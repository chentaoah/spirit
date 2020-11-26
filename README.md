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

1. 下载安装sublime编辑器，并将项目中的sublime-plugs目录下的所有文件，拷贝到sublime插件目录下的User文件夹中。
2. 下载项目中的spirit-deploy文件夹，到任意目录下。（spirit-deploy文件夹为打包部署目录）
3. 在eclipse中，借助maven命令导出工程依赖项，并拷贝到spirit-deploy/lib_dep文件夹中。(Run As->Maven-build->dependency:copy-dependencies)
4. 双击运行spirit-deploy/bin/startup.bat，并在target目录下查看生成的示例代码。
