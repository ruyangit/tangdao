@echo off
%~d0
cd %~dp0

cd ..
call mvn -f "pom.xml" versions:commit

pause