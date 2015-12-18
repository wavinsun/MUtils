@echo off&title Build Android Application
call gradlew clean
call gradlew assembleRelease
echo. & pause