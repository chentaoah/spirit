@echo off
title SPIRIT-CODE-TOOLS
echo path:%~dp0
java -Xms512m -Xmx512m -cp ".;../lib/*" com.sum.spirit.code.tools.CodeToolsStarter
@pause