@echo off
setlocal enabledelayedexpansion
title Configuracao do Ambiente - Projeto A3

if "%~1"=="INTERNAL_RUN" goto :main_execution

set "LOG_FILE=%~dp0log_erro.txt"
echo [INFO] Iniciando ambiente e validando scripts... > "%LOG_FILE%"
cmd /c ""%~f0" INTERNAL_RUN" >> "%LOG_FILE%" 2>&1
set "SCRIPT_RETURN=%errorLevel%"

if %SCRIPT_RETURN% neq 0 (
    echo ========================================================
    echo   [ERRO DETECTADO] O script fechou inesperadamente!
    echo ========================================================
    echo O log completo foi salvo em: "%LOG_FILE%"
    echo.
    echo Ultimas linhas do log:
    echo.
    type "%LOG_FILE%"
    echo.
    echo ========================================================
) else (
    echo ========================================================
    echo   [SUCESSO] Script executado sem erros.
    echo ========================================================
    echo Log disponivel em: "%LOG_FILE%"
    echo.
)
echo Pressione qualquer tecla para fechar...
pause >nul
exit /b %SCRIPT_RETURN%

:main_execution
chcp 850 >nul
reg add "HKCU\Console" /v VirtualTerminalLevel /t REG_DWORD /d 1 /f >nul 2>&1
for /F %%a in ('echo prompt $E ^| cmd') do set "ESC=%%a"
set "RESET=%ESC%[0m"
set "BOLD=%ESC%[1m"
set "RED=%ESC%[91m"
set "GREEN=%ESC%[92m"
set "YELLOW=%ESC%[93m"
set "CYAN=%ESC%[96m"
set "WHITE=%ESC%[97m"

echo %CYAN%========================================================%RESET%
echo %BOLD%%WHITE%  CONFIGURACAO DO AMBIENTE - PROJETO A3%RESET%
echo %WHITE%  Java 21 ^| Maven ^| PostgreSQL (Latest) ^| Git%RESET%
echo %CYAN%========================================================%RESET%
echo.

net session >nul 2>&1
if %errorLevel% neq 0 (
    echo %RED%[ERRO] Este script precisa ser executado como ADMINISTRADOR.%RESET%
    pause
    exit /b 1
)

where winget >nul 2>&1
if %errorLevel% neq 0 (
    echo %RED%[ERRO] Winget nao encontrado.%RESET%
    pause
    exit /b 1
)

:: ============================================================
:: SUBROTINA: executa winget silenciosamente e retorna ERR
:: FIX: winget imprime caracteres de animacao residuais (". \ | /")
:: que corrompem o parser do CMD quando capturados no log.
:: Solucao: redirecionar TODA saida do winget para nul >nul 2>&1
:: e informar resultado apenas via echo proprio do script.
:: ============================================================

:: ============================================================
:: PASSO 1 - JAVA 21
:: ============================================================
echo %CYAN%[PASSO 1] Verificando Java JDK 21...%RESET%
set "JAVA_OK=0"

java -version 2>&1 | findstr /C:"version" | findstr /C:"21" >nul
if %errorLevel% equ 0 set "JAVA_OK=1"

if defined JAVA_HOME (
    "%JAVA_HOME%\bin\java" -version 2>&1 | findstr /C:"version" | findstr /C:"21" >nul
    if %errorLevel% equ 0 set "JAVA_OK=1"
)

if %JAVA_OK% equ 1 goto :java_ok
echo %YELLOW%[INSTALANDO] Java JDK 21...%RESET%
winget install EclipseAdoptium.Temurin.21.JDK --silent --accept-package-agreements --accept-source-agreements >nul 2>&1
set "ERR=%errorLevel%"
if %ERR% equ 0 (
    echo %GREEN%[OK] Java instalado com sucesso.%RESET%
    for /f "tokens=2*" %%a in ('reg query "HKLM\SOFTWARE\JavaSoft\JDK\21" /v JavaHome 2^>nul') do set "JAVA_HOME=%%b"
    if defined JAVA_HOME set "PATH=!JAVA_HOME!\bin;!PATH!"
) else (
    echo %RED%[FALHA] Java nao instalado (codigo: %ERR%).%RESET%
)
goto :java_done
:java_ok
echo %GREEN%[OK] Java 21 ja instalado.%RESET%
java -version 2>&1 | findstr /C:"version"
:java_done
echo.

:: ============================================================
:: PASSO 2 - MAVEN
:: ============================================================
echo %CYAN%[PASSO 2] Verificando Apache Maven...%RESET%
set "MAVEN_OK=0"

where mvn >nul 2>&1
if %errorLevel% equ 0 set "MAVEN_OK=1"

if defined MAVEN_HOME (
    if exist "%MAVEN_HOME%\bin\mvn.cmd" set "MAVEN_OK=1"
)

if %MAVEN_OK% equ 1 goto :maven_ok
echo %YELLOW%[INSTALANDO] Maven...%RESET%
winget install Apache.Maven --silent --accept-package-agreements --accept-source-agreements >nul 2>&1
set "ERR=%errorLevel%"
if %ERR% equ 0 (
    echo %GREEN%[OK] Maven instalado com sucesso.%RESET%
    for /f "tokens=2*" %%a in ('reg query "HKLM\SOFTWARE\Apache Software Foundation\Maven" /v "M2_HOME" 2^>nul') do set "MAVEN_HOME=%%b"
    if defined MAVEN_HOME set "PATH=!MAVEN_HOME!\bin;!PATH!"
) else (
    echo %RED%[FALHA] Maven nao instalado (codigo: %ERR%).%RESET%
)
goto :maven_done
:maven_ok
echo %GREEN%[OK] Maven ja instalado.%RESET%
mvn -v 2>&1 | findstr /C:"Apache Maven"
:maven_done
echo.

:: ============================================================
:: PASSO 3 - POSTGRESQL
:: FIX PRINCIPAL: winget >nul 2>&1 em TODOS os comandos winget.
:: Os caracteres ". \ | / -" que o winget imprime apos
:: "Instalado com exito" corrompem o parser do CMD quando
:: capturados pelo >> do log externo sem redirecionamento.
:: ============================================================
echo %CYAN%[PASSO 3] Instalando PostgreSQL (Ultima versao)...%RESET%
set "PG_OK=0"

where psql >nul 2>&1
set "ERR=%errorLevel%"
if %ERR% equ 0 (
    set "PG_OK=1"
    for /f "tokens=2" %%v in ('psql --version 2^>nul') do echo %GREEN%[OK] PostgreSQL ja instalado: %%v%RESET%
)
if %PG_OK% equ 1 goto :postgres_path_done

:: --- [A] winget v17 ---
echo %YELLOW%[A] Tentando winget (PostgreSQL 17)...%RESET%
winget install PostgreSQL.PostgreSQL.17 --silent --accept-package-agreements --accept-source-agreements >nul 2>&1
set "ERR=%errorLevel%"
if %ERR% equ 0 (
    echo %GREEN%[OK] PostgreSQL 17 instalado via winget.%RESET%
    set "PG_OK=1"
    goto :postgres_update_path
)
echo %YELLOW%[A] v17 falhou (codigo: %ERR%). Tentando v16...%RESET%

:: --- [A] winget v16 ---
winget install PostgreSQL.PostgreSQL.16 --silent --accept-package-agreements --accept-source-agreements >nul 2>&1
set "ERR=%errorLevel%"
if %ERR% equ 0 (
    echo %GREEN%[OK] PostgreSQL 16 instalado via winget.%RESET%
    set "PG_OK=1"
    goto :postgres_update_path
)
echo %YELLOW%[A] v16 falhou (codigo: %ERR%). Tentando v15...%RESET%

:: --- [A] winget v15 ---
winget install PostgreSQL.PostgreSQL.15 --silent --accept-package-agreements --accept-source-agreements >nul 2>&1
set "ERR=%errorLevel%"
if %ERR% equ 0 (
    echo %GREEN%[OK] PostgreSQL 15 instalado via winget.%RESET%
    set "PG_OK=1"
    goto :postgres_update_path
)
echo %RED%[A] Todos os IDs winget falharam.%RESET%

:: --- [B] Chocolatey ---
echo %YELLOW%[B] Tentando Chocolatey...%RESET%
where choco >nul 2>&1
set "CHOCO_FOUND=%errorLevel%"

if %CHOCO_FOUND% neq 0 (
    echo %YELLOW%[B] Instalando Chocolatey...%RESET%
    powershell -NoProfile -ExecutionPolicy Bypass -Command "Set-ExecutionPolicy Bypass -Scope Process -Force; [System.Net.ServicePointManager]::SecurityProtocol = [System.Net.ServicePointManager]::SecurityProtocol -bor 3072; iex ((New-Object System.Net.WebClient).DownloadString('https://community.chocolatey.org/install.ps1'))" >nul 2>&1
    set "ERR=%errorLevel%"
    if %ERR% equ 0 (
        echo %GREEN%[B] Chocolatey instalado.%RESET%
        set "PATH=%ALLUSERSPROFILE%\chocolatey\bin;!PATH!"
    ) else (
        echo %RED%[B] Falha ao instalar Chocolatey (codigo: %ERR%).%RESET%
        goto :choco_skip
    )
)

where choco >nul 2>&1
if %errorLevel% neq 0 goto :choco_skip

echo %YELLOW%[B] Instalando postgresql via choco...%RESET%
choco install postgresql --yes --no-progress >nul 2>&1
set "ERR=%errorLevel%"
if %ERR% equ 0 (
    echo %GREEN%[OK] PostgreSQL instalado via Chocolatey.%RESET%
    set "PG_OK=1"
    goto :postgres_update_path
)
echo %RED%[B] Falha no Chocolatey (codigo: %ERR%).%RESET%

:choco_skip

:: --- [C] Download direto EnterpriseDB ---
echo %YELLOW%[C] Tentando download direto (EnterpriseDB)...%RESET%
set "PG_INSTALLER=%TEMP%\postgresql-17-windows-x64.exe"

powershell -NoProfile -Command "try { Invoke-WebRequest -Uri 'https://get.enterprisedb.com/postgresql/postgresql-17.10-1-windows-x64.exe' -OutFile '%PG_INSTALLER%' -TimeoutSec 120 } catch { exit 1 }" >nul 2>&1
set "ERR=%errorLevel%"

if %ERR% neq 0 goto :pg_download_fail
if not exist "%PG_INSTALLER%" goto :pg_download_fail

echo %YELLOW%[C] Executando instalador silencioso...%RESET%
start /wait "" "%PG_INSTALLER%" --mode unattended --unattendedmodeui none --superpassword postgres --servicename postgresql --servicepassword postgres --install_runtimes 1
set "ERR=%errorLevel%"
move /y "%PG_INSTALLER%" "%~dp0postgresql-installer-baixado.exe" >nul 2>&1
if %ERR% equ 0 (
    echo %GREEN%[OK] PostgreSQL instalado via EnterpriseDB.%RESET%
    set "PG_OK=1"
    goto :postgres_update_path
)
echo %RED%[C] Instalador falhou (codigo: %ERR%).%RESET%
goto :pg_install_failed

:pg_download_fail
echo %RED%[C] Download falhou (rede bloqueada?).%RESET%

:pg_install_failed
echo.
echo %RED%========================================================%RESET%
echo %BOLD%%RED%  [D] INSTALACAO AUTOMATICA FALHOU%RESET%
echo %WHITE%  Instale manualmente: https://www.postgresql.org/download/windows/%RESET%
echo %WHITE%  Apos instalar, adicione ao PATH:%RESET%
echo %WHITE%  C:\Program Files\PostgreSQL\17\bin%RESET%
echo %RED%========================================================%RESET%
goto :postgres_path_done

:postgres_update_path
for /f "tokens=2*" %%a in ('reg query "HKLM\SYSTEM\CurrentControlSet\Control\Session Manager\Environment" /v PATH 2^>nul') do set "SysPath=%%b"
if defined SysPath set "PATH=!SysPath!;!PATH!"

set "PG_LATEST_DIR="
if exist "C:\Program Files\PostgreSQL\" (
    for /d %%d in ("C:\Program Files\PostgreSQL\*") do set "PG_LATEST_DIR=%%d"
)
if defined PG_LATEST_DIR (
    if exist "!PG_LATEST_DIR!\bin\psql.exe" (
        set "PATH=!PG_LATEST_DIR!\bin;!PATH!"
        echo %GREEN%PATH atualizado: !PG_LATEST_DIR!\bin%RESET%
    )
)

:postgres_path_done
echo.

:: ============================================================
:: PASSO 4 - GIT
:: ============================================================
echo %CYAN%[PASSO 4] Verificando Git...%RESET%

where git >nul 2>&1
set "ERR=%errorLevel%"
if %ERR% equ 0 goto :git_ok

echo %YELLOW%[INSTALANDO] Git...%RESET%
winget install Git.Git --silent --accept-package-agreements --accept-source-agreements >nul 2>&1
set "ERR=%errorLevel%"
if %ERR% equ 0 (
    echo %GREEN%[OK] Git instalado com sucesso.%RESET%
    for /f "tokens=2*" %%a in ('reg query "HKLM\SYSTEM\CurrentControlSet\Control\Session Manager\Environment" /v PATH 2^>nul') do set "SysPath=%%b"
    if defined SysPath set "PATH=!SysPath!;!PATH!"
    git --version
) else (
    echo %RED%[FALHA] Git nao instalado (codigo: %ERR%).%RESET%
)
goto :git_done
:git_ok
echo %GREEN%[OK] Git ja instalado.%RESET%
git --version
:git_done
echo.

:: ============================================================
:: VERIFICACAO FINAL
:: ============================================================
echo %CYAN%========================================================%RESET%
echo %BOLD%%WHITE%  VERIFICACAO FINAL DAS VERSOES%RESET%
echo %CYAN%========================================================%RESET%

echo %WHITE%Java:%RESET%
java -version 2>&1 | findstr /C:"version"
if %errorLevel% neq 0 echo %RED%  Nao detectado%RESET%
echo.

echo %WHITE%Maven:%RESET%
mvn -v 2>&1 | findstr /C:"Apache Maven"
if %errorLevel% neq 0 echo %RED%  Nao detectado%RESET%
echo.

echo %WHITE%PostgreSQL:%RESET%
where psql >nul 2>&1
set "ERR=%errorLevel%"
if %ERR% equ 0 (
    psql --version
) else (
    echo %YELLOW%  Nao encontrado no PATH. Reinicie o terminal.%RESET%
)
echo.

echo %WHITE%Git:%RESET%
git --version
if %errorLevel% neq 0 echo %RED%  Nao detectado%RESET%
echo.

echo %CYAN%========================================================%RESET%
echo %BOLD%%GREEN%  CONCLUIDO!%RESET%
echo %YELLOW%  IMPORTANTE: Feche este terminal e abra um NOVO terminal%RESET%
echo %YELLOW%  para carregar as novas variaveis de ambiente.%RESET%
echo %CYAN%========================================================%RESET%
echo.
echo Log salvo em: "%LOG_FILE%"
pause
exit /b 0
