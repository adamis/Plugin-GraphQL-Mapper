# GraphQL-Mapper Plugin para Eclipse

Plugin do Eclipse para trabalhar com GraphQL e REST.

## 📋 Funcionalidades

O plugin adiciona um menu de contexto "GraphQL Mapper" com as seguintes opções:

1. **Gerar GraphQL DTO** - Gera DTOs a partir de esquemas GraphQL
2. **Montar Rest Request** - Auxilia na montagem de requisições REST

## 🎨 Ícone

O plugin utiliza um ícone customizado que aparece no menu de contexto.

## 🏗️ Estrutura do Projeto

```
GraphQL-Mapper/
├── META-INF/
│   └── MANIFEST.MF           # Manifesto do bundle OSGi
├── icons/
│   ├── plugin_icon.png       # Ícone do plugin
│   ├── plugin_icon32.png     # Ícone 32x32
│   └── package_icon.png      # Ícone do pacote
├── src/
│   └── graphql/mapper/
│       ├── Activator.java    # Classe de ativação do plugin
│       ├── handlers/         # Handlers dos comandos
│       │   ├── GenerateGraphQLDTOHandler.java
│       │   └── MountRestRequestHandler.java
│       └── dialogs/          # Janelas de diálogo
│           ├── GraphQLDTODialog.java
│           └── RestRequestDialog.java
├── plugin.xml                # Configuração de extensões
├── build.properties          # Propriedades de build
└── pom.xml                   # Configuração Maven/Tycho
```

## 🔧 Requisitos

- **Java 21** ou superior
- **Maven 3.8+** para build
- **Eclipse IDE** para instalação/uso

## 🚀 Build do Projeto

Este projeto usa **Maven com Tycho** para compilação. Para compilar o plugin:

### Build Completo

```bash
mvn clean package
```

### Instalar no Repositório Maven Local

```bash
mvn clean install
```

### Build sem Testes

```bash
mvn clean package -DskipTests
```

### Limpar Build

```bash
mvn clean
```

## 📦 Instalação no Eclipse

Após compilar o projeto, o arquivo JAR do plugin estará em:
```
target/GraphQL-Mapper-1.0.0-SNAPSHOT.jar
```

### Opção 1: Instalação Manual
1. Copie o arquivo JAR para o diretório `dropins` do Eclipse
2. Reinicie o Eclipse

### Opção 2: Instalação via Update Site Local
1. Vá em `Help > Install New Software...`
2. Clique em `Add...` e selecione `Local...`
3. Navegue até a pasta `target/repository` do projeto
4. Siga o assistente de instalação

## 💻 Como Usar

1. Clique com o botão direito em um arquivo no Project Explorer
2. Selecione "GraphQL Mapper" no menu de contexto
3. Escolha uma das opções:
   - **Gerar GraphQL DTO**: Abre uma janela para gerar DTOs a partir de esquemas GraphQL
   - **Montar Rest Request**: Abre uma janela para montar requisições REST

## 📦 Dependências do Plugin

- Eclipse UI (`org.eclipse.ui`)
- Eclipse Core Runtime (`org.eclipse.core.runtime`)
- Eclipse Core Resources (`org.eclipse.core.resources`)
- Eclipse JFace (`org.eclipse.jface`)
- Eclipse UI IDE (`org.eclipse.ui.ide`)
- Eclipse Core Commands (`org.eclipse.core.commands`)
- Eclipse JDT Core (`org.eclipse.jdt.core`)
- Eclipse JDT UI (`org.eclipse.jdt.ui`)

## 🛠️ Desenvolvimento

### Importar no Eclipse

1. Clone o repositório
2. No Eclipse: `File > Import > Maven > Existing Maven Projects`
3. Selecione a pasta do projeto
4. O Eclipse configurará automaticamente o projeto

### Executar em Modo de Desenvolvimento

1. Clique com botão direito no projeto
2. `Run As > Eclipse Application`
3. Uma nova instância do Eclipse será aberta com o plugin carregado

## 📝 Notas de Desenvolvimento

- Cada funcionalidade possui sua própria classe Handler
- Cada janela de diálogo é implementada em uma classe separada
- O plugin segue a arquitetura padrão de plugins Eclipse baseada em OSGi
- Usa **Tycho** para build Maven de plugins Eclipse
- Compatível com Windows, Linux e macOS (incluindo Apple Silicon)

