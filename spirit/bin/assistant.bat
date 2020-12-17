@echo off
title JAVA - SPIRIT-ASSISTANT
echo path:%~dp0
java -Xms128m -Xmx128m -cp ".;../lib/*;../lib_dep/*;../lib_web/*" com.sum.spirit.starter.assistant.AssistantStarter
@pause