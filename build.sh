#!/bin/bash
# Script de Build Automatizado - GraphQL Mapper Plugin
# Linux/macOS

echo ""
echo "========================================"
echo "  GraphQL Mapper - Build Script"
echo "========================================"
echo ""

# Verificar se Maven está instalado
if ! command -v mvn &> /dev/null; then
    echo "[ERRO] Maven não encontrado! Instale o Maven."
    exit 1
fi

# Verificar se Java está instalado
if ! command -v java &> /dev/null; then
    echo "[ERRO] Java não encontrado! Instale o JDK 21."
    exit 1
fi

# Mostrar versões
echo "[INFO] Verificando versões..."
echo ""
java -version
echo ""
mvn -version
echo ""

# Executar build
echo "[INFO] Iniciando build..."
echo ""
mvn clean package

# Verificar resultado
if [ $? -ne 0 ]; then
    echo ""
    echo "[ERRO] Build falhou! Verifique os erros acima."
    exit 1
fi

echo ""
echo "========================================"
echo "  Build concluído com sucesso!"
echo "========================================"
echo ""
echo "Artefatos gerados:"
ls -lh target/*.jar
echo ""
echo "Para instalar no Eclipse:"
echo "  cp target/GraphQL-Mapper-1.0.0-SNAPSHOT.jar ~/eclipse/dropins/"
echo ""
echo "Depois reinicie o Eclipse."
echo ""

exit 0


