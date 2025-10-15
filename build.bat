@echo off
REM Script de Build Automatizado - GraphQL Mapper Plugin
REM Windows

echo.
echo ========================================
echo   GraphQL Mapper - Build Script
echo ========================================
echo.

REM Verificar se Maven está instalado
where mvn >nul 2>nul
if %ERRORLEVEL% NEQ 0 (
    echo [ERRO] Maven nao encontrado! Instale o Maven e adicione ao PATH.
    exit /b 1
)

REM Verificar se Java está instalado
where java >nul 2>nul
if %ERRORLEVEL% NEQ 0 (
    echo [ERRO] Java nao encontrado! Instale o JDK 21 e adicione ao PATH.
    exit /b 1
)

REM Mostrar versões
echo [INFO] Verificando versoes...
echo.
java -version
echo.
mvn -version
echo.

REM Executar build
echo [INFO] Iniciando build...
echo.
mvn clean package

REM Verificar resultado
if %ERRORLEVEL% NEQ 0 (
    echo.
    echo [ERRO] Build falhou! Verifique os erros acima.
    exit /b 1
)

echo.
echo ========================================
echo   Build concluido com sucesso!
echo ========================================
echo.
echo Artefatos gerados:
dir target\*.jar
echo.
echo Para instalar no Eclipse:
echo   copy target\GraphQL-Mapper-1.0.0-SNAPSHOT.jar "C:\eclipse\dropins\"
echo.
echo Depois reinicie o Eclipse.
echo.

exit /b 0


