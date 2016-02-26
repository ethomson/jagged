@echo off

setlocal

if "%JAVA_HOME%" == "" goto javahomeerror

if "%Platform%" == "X64" set ARCH=x86_64
if "%Platform%" == "" set ARCH=x86

if "%ARCH%" == "" goto archerror
if "%ARCH%" == "x86" set TARGET_PLATFORM="Visual Studio 12"
if "%ARCH%" == "x86_64" set TARGET_PLATFORM="Visual Studio 12 Win64"

if "%MSYSTEM%" == "MINGW64" set TARGET_PLATFORM="MinGW Makefiles"
if "%MSYSTEM%" == "MINGW64" set ARCH=x86_64

if "%CMAKE_CONFIG%" == "" set CMAKE_CONFIG=RelWithDebInfo


set NATIVE_INSTALL=%~dp0\native\win32\%ARCH%

set NATIVE_TARGET=%~dp0\target\native
set LIBGIT2_TARGET=%NATIVE_TARGET%\libgit2\%ARCH%
set LIBJAGGED_TARGET=%NATIVE_TARGET%\libjagged\%ARCH%
set LIBJAGGED_TEST_TARGET=%NATIVE_TARGET%\libjagged_test\%ARCH%

set LIBGIT2_SRC=%~dp0\src\main\native\libgit2
set LIBJAGGED_SRC=%~dp0\src\main\native\libjagged
set LIBJAGGED_TEST_SRC=%~dp0\src\test\native\libjagged_test


if "%1" == "" goto build
if "%1" == "build" goto build
if "%1" == "clean" goto clean
if "%1" == "install" goto install

echo Unknown target: %1
goto end


:build
:install
rem build libgit2
echo Building libgit2...
if not exist %LIBGIT2_TARGET% ( mkdir %LIBGIT2_TARGET% )
cd %LIBGIT2_TARGET%
cmake %LIBGIT2_SRC% -G%TARGET_PLATFORM% ^
 -DTHREADSAFE=ON -DBUILD_CLAR=OFF -DSTDCALL=OFF
cd %~dp0
cmake --build %LIBGIT2_TARGET% --config %CMAKE_CONFIG%
if not "%errorlevel%" == "0" ( goto end )

rem build libjagged
echo Building libjagged...
if not exist %LIBJAGGED_TARGET% ( mkdir %LIBJAGGED_TARGET% )
cd %LIBJAGGED_TARGET%
cmake %LIBJAGGED_SRC% -G%TARGET_PLATFORM% ^
 -DINCLUDE_LIBGIT2=%LIBGIT2_SRC%\include ^
 -DLINK_LIBGIT2="%LIBGIT2_TARGET%"
cd %~dp0
cmake --build %LIBJAGGED_TARGET% --config %CMAKE_CONFIG%
if not "%errorlevel%" == "0" ( goto end )

rem build libjagged_test
echo Building libjagged_test...
if not exist %LIBJAGGED_TEST_TARGET% ( mkdir %LIBJAGGED_TEST_TARGET% )
cd %LIBJAGGED_TEST_TARGET%
cmake %LIBJAGGED_TEST_SRC% -G%TARGET_PLATFORM% ^
 -DINCLUDE_LIBGIT2=%LIBGIT2_SRC%\include ^
 -DLINK_LIBGIT2=%LIBGIT2_TARGET% ^
 -DINCLUDE_LIBJAGGED=%LIBJAGGED_SRC% ^
 -DLINK_LIBJAGGED=%LIBJAGGED_TARGET%
cd %~dp0
cmake --build %LIBJAGGED_TEST_TARGET% --config %CMAKE_CONFIG%
if not "%errorlevel%" == "0" ( goto end )

if not "%1" == "install" ( goto end )

if not exist %NATIVE_INSTALL%\%ARCH_PATH% ( mkdir %NATIVE_INSTALL%\%ARCH_PATH% )
@echo on
copy %LIBGIT2_TARGET%\libgit2.dll %NATIVE_INSTALL%\%ARCH_PATH%
copy %LIBJAGGED_TARGET%\libjagged.dll %NATIVE_INSTALL%\%ARCH_PATH%
copy %LIBJAGGED_TEST_TARGET%\libjagged_test.dll %NATIVE_INSTALL%\%ARCH_PATH%
@echo off
goto end

:clean
if exist %LIBGIT2_TARGET% ( cmake --build %LIBGIT2_TARGET% --target clean )
if exist %LIBJAGGED_TARGET% ( cmake --build %LIBJAGGED_TARGET% --target clean )
if exist %LIBJAGGED_TEST_TARGET% ( cmake --build %LIBJAGGED_TEST_TARGET% --target clean )
goto end


:archerror
echo+
echo Unknown build architecture
goto end

:javahomeerror
echo+
echo JAVA_HOME must be set to an installed JDK (matching the current architecture)
goto end

:end
endlocal
