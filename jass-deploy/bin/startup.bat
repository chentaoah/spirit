@echo off
title JASS编译器 - JAVA
echo 当前路径为：%~dp0
java -Xms128m -Xmx128m -cp ".;../lib/*;../lib_dep/*" com.sum.jass.start.JavaStarter %~dp0..\src %~dp0..\target
@pause