@echo off
title SPIRIT(JAVA)
echo path:%~dp0
java -Xms128m -Xmx128m -cp ".;../lib/*;../lib_dep/*" com.sum.spirit.starter.JavaStarter --input=%~dp0..\src --output=%~dp0..\target
@pause