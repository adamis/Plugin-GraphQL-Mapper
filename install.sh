#!/bin/bash
# Script de Instalação Automatizado - GraphQL Mapper Plugin
# Linux/macOS

echo ""
echo "========================================"
echo "  GraphQL Mapper - Install Script"
echo "========================================"
echo ""

# Verificar se o JAR existe
if [ ! -f "target/GraphQL-Mapper-1.0.0-SNAPSHOT.jar" ]; then
    echo "[AVISO] JAR não encontrado. Executando build primeiro..."
    ./build.sh
    if [ $? -ne 0 ]; then
        exit 1
    fi
fi

# Perguntar o diretório do Eclipse
echo "[INFO] Digite o caminho de instalação do Eclipse:"
echo "       Exemplo: /opt/eclipse ou ~/eclipse"
echo ""
read -p "Eclipse Home: " ECLIPSE_HOME

if [ -z "$ECLIPSE_HOME" ]; then
    echo "[ERRO] Caminho não fornecido!"
    exit 1
fi

# Expandir ~ se usado
ECLIPSE_HOME="${ECLIPSE_HOME/#\~/$HOME}"

# Verificar se o diretório existe
if [ ! -d "$ECLIPSE_HOME" ]; then
    echo "[ERRO] Diretório do Eclipse não encontrado: $ECLIPSE_HOME"
    exit 1
fi

# Criar pasta dropins se não existir
if [ ! -d "$ECLIPSE_HOME/dropins" ]; then
    echo "[INFO] Criando pasta dropins..."
    mkdir -p "$ECLIPSE_HOME/dropins"
fi

# Remover versão antiga se existir
if ls "$ECLIPSE_HOME/dropins/GraphQL-Mapper"*.jar 1> /dev/null 2>&1; then
    echo "[INFO] Removendo versão antiga..."
    rm "$ECLIPSE_HOME/dropins/GraphQL-Mapper"*.jar
fi

# Copiar novo plugin
echo "[INFO] Instalando plugin..."
cp "target/GraphQL-Mapper-1.0.0-SNAPSHOT.jar" "$ECLIPSE_HOME/dropins/"

if [ $? -ne 0 ]; then
    echo "[ERRO] Falha ao copiar plugin!"
    exit 1
fi

echo ""
echo "========================================"
echo "  Plugin instalado com sucesso!"
echo "========================================"
echo ""
echo "Local: $ECLIPSE_HOME/dropins/GraphQL-Mapper-1.0.0-SNAPSHOT.jar"
echo ""
echo "Para ativar o plugin:"
echo "  1. Reinicie o Eclipse"
echo "  2. Ou execute: eclipse -clean"
echo ""
echo "Para verificar a instalação:"
echo "  1. Help > About Eclipse IDE"
echo "  2. Installation Details"
echo "  3. Procure por 'GraphQL-Mapper'"
echo ""

exit 0


