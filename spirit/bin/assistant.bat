@echo off
title SPIRIT-KIT(JAVA)
echo path:%~dp0
java -Xms128m -Xmx128m -cp ".;../lib/*;../lib_dep/*;../lib_web/*" com.sum.spirit.starter.kit.KitStarter --input=%~dp0..\src
@pause