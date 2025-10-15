@echo off
REM Script de Instalação Automatizado - GraphQL Mapper Plugin
REM Windows

echo.
echo ========================================
echo   GraphQL Mapper - Install Script
echo ========================================
echo.

REM Verificar se o JAR existe
if not exist "target\GraphQL-Mapper-1.0.0-SNAPSHOT.jar" (
    echo [AVISO] JAR nao encontrado. Executando build primeiro...
    call build.bat
    if %ERRORLEVEL% NEQ 0 (
        exit /b 1
    )
)

REM Perguntar o diretório do Eclipse
echo [INFO] Digite o caminho de instalacao do Eclipse:
echo        Exemplo: C:\eclipse
echo.
set /p ECLIPSE_HOME="Eclipse Home: "

if "%ECLIPSE_HOME%"=="" (
    echo [ERRO] Caminho nao fornecido!
    exit /b 1
)

REM Verificar se o diretório existe
if not exist "%ECLIPSE_HOME%" (
    echo [ERRO] Diretorio do Eclipse nao encontrado: %ECLIPSE_HOME%
    exit /b 1
)

REM Criar pasta dropins se não existir
if not exist "%ECLIPSE_HOME%\dropins" (
    echo [INFO] Criando pasta dropins...
    mkdir "%ECLIPSE_HOME%\dropins"
)

REM Remover versão antiga se existir
if exist "%ECLIPSE_HOME%\dropins\GraphQL-Mapper*.jar" (
    echo [INFO] Removendo versao antiga...
    del "%ECLIPSE_HOME%\dropins\GraphQL-Mapper*.jar"
)

REM Copiar novo plugin
echo [INFO] Instalando plugin...
copy "target\GraphQL-Mapper-1.0.0-SNAPSHOT.jar" "%ECLIPSE_HOME%\dropins\"

if %ERRORLEVEL% NEQ 0 (
    echo [ERRO] Falha ao copiar plugin!
    exit /b 1
)

echo.
echo ========================================
echo   Plugin instalado com sucesso!
echo ========================================
echo.
echo Local: %ECLIPSE_HOME%\dropins\GraphQL-Mapper-1.0.0-SNAPSHOT.jar
echo.
echo Para ativar o plugin:
echo   1. Reinicie o Eclipse
echo   2. Ou execute: eclipse -clean
echo.
echo Para verificar a instalacao:
echo   1. Help ^> About Eclipse IDE
echo   2. Installation Details
echo   3. Procure por 'GraphQL-Mapper'
echo.

exit /b 0


