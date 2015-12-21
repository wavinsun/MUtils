@echo off&title Build Android Application
move gradle\wrapper\gradle-wrapper.properties gradle\wrapper\gradle-wrapper.properties.bak
copy gradle\wrapper\gradle-wrapper-local.properties gradle\wrapper\gradle-wrapper.properties
call gradlew clean
call gradlew assembleRelease
move gradle\wrapper\gradle-wrapper.properties.bak gradle\wrapper\gradle-wrapper.properties
echo. & pause