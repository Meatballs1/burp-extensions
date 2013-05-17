@echo off
rem The debug command-line option is used by DeflateBurpPlugin.jar. 
rem Remove it to disable plug-in debugging information.
java -Xmx256m -classpath burpsuite_v1.1.jar;DeflateBurpPlugin.jar burp.StartBurp debug
