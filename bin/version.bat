@echo off
%~d0
cd %~dp0

cd ..
call mvn -f "pom.xml" versions:set -DoldVersion=* -DnewVersion=1.0.0 -DprocessAllModules=true -DallowSnapshots=true -DgenerateBackupPoms=true

pause