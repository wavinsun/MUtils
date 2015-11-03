@echo off&title Build Android Application
if "%JAVA_HOME%"=="" goto JAVA_HOME
if "%ANT_HOME%"=="" goto ANT_HOME
if "%ANDROID_HOME%"=="" goto ANDROID_HOME
goto build
:JAVA_HOME
echo Require JAVA_HOME to be specified on your computer. 
echo Please install Java Environment and try again.
goto end
:ANT_HOME
echo Require ANT_HOME to be specified on your computer.
echo Please install Ant Environment and try again.
goto end
:ANDROID_HOME
echo Require ANDROID_HOME to be specified on your computer. 
echo Please install Android SDK Tools and try again.
goto end
:build
call ant
goto end
:end
echo. & pause
