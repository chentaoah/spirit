@echo off
title Shy编译器 - JAVA
java -Xms128m -Xmx128m -cp ".;../lib/*;../lib_dep/*" com.sum.shy.start.JavaStarter ../src ../target
@pause