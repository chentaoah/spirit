@echo off
title Soon - JAVA
echo path:%~dp0
java -Xms128m -Xmx128m -cp ".;../lib/*;../lib_dep/*" com.sum.soon.start.JavaStarter %~dp0..\src %~dp0..\target
@pause