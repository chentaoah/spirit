# Spirit
[![star](https://gitee.com/chentaoah/spirit/badge/star.svg?theme=white)](https://gitee.com/chentaoah/spirit/stargazers)
[![fork](https://gitee.com/chentaoah/spirit/badge/fork.svg?theme=white)](https://gitee.com/chentaoah/spirit/members)

一种轻量的语言。

```
func main(){
    print "hello world!"
}
```

上述代码转换后得到以下代码：

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

设计目标：
1. 在原生语言基础上，减少代码的冗余。
2. 转换后得到的代码，和直接使用原生语言一样。
3. 引导用户书写统一的逻辑，让“逻辑”可以在多个语言之上，进行迁移。
4. 给脚本语言，带来类型的优势。
5. 代码可以根据条件动态转换，减少开发工作量。

功能点：
1. 支持面向过程编程。方法可以不在类型中实现。
2. 支持自动引入类型。按照一定规则，自动引入工程中其它的类型和方法中推导而来的类型。
3. 支持别名机制。可以为某个类型取别名，解决多个类名相同，而不得不使用类型全名的场景。
4. 支持类型自动推导。包括变量类型和方法返回类型。
5. 支持快速构造集合List和Map。例如：["xxx","xxx"] 和 {"key":"value"}
6. 支持快速遍历集合。例如：for item in list {
7. 支持字符串的操作符“==”的重载。s=="string"等效于StringUtils.equals(s, "string")
8. 支持print等关键字。打印日志变得更加便捷。
9. 支持某些语句多行合并。例如：if s=="string" : print s
10. 支持与Java的无缝集成。 :sweat: 底层不就是Java吗？有什么好集成的。

上述功能点，具体可以参考spirit-test模块中，src/test/resources/com.sum.test目录下的示例代码。  

使用说明：
1. 下载安装sublime编辑器，并将项目中的sublime-plugs目录下的所有文件，拷贝到sublime插件目录下的User文件夹中。
2. 下载项目中的spirit-deploy文件夹，到任意目录下。（spirit-deploy文件夹为打包部署目录）
3. 在eclipse中，借助maven命令导出工程依赖项，并拷贝到spirit-deploy/lib_dep文件夹中。(Run As->Maven-build->dependency:copy-dependencies)
4. 双击运行spirit-deploy/bin/startup.bat，并在target目录下查看生成的示例代码。

未来：
1. 编辑器提供提示功能。
