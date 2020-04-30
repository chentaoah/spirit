# Shy
一种面向对象的语言，可以转换成其它比较流行的语言。

```
func main(){
    print "hello world!"
}
```

目前已经支持Java，理想情况下，能节省60%的代码量。上述代码转换得到的代码如下：

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
设计方向：
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

上述特点，具体可以参考shy-start的resources的示例代码。
目前的IDE是无法正确的显示出语法样式的，所以这里推荐sublime来编辑。
在sublime添加样式文件Shy.sublime-syntax即可。

未来：
1. 编辑器提供提示功能。
2. 支持C语言。














