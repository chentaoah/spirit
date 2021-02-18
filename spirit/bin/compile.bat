@echo off
title SPIRIT-STARTER-JAVA
echo path:%~dp0
java -Xms128m -Xmx128m -cp ".;../lib/*" com.sum.spirit.starter.JavaStarter --input=%~dp0..\src --output=%~dp0..\target
@pause