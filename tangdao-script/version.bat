@echo off
rem /**
rem  * Copyright (c) 2020-Now http://tangdao.io All rights reserved.
rem  *
rem  * Author: ruyangit@gmail.com
rem  */
%~d0
cd %~dp0

cd ..
call mvn -f "pom.xml" versions:set -DoldVersion=* -DnewVersion=0.0.36-SNAPSHOT -DprocessAllModules=true -DallowSnapshots=true -DgenerateBackupPoms=true

pause