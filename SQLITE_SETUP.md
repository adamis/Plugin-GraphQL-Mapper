# 🗄️ Banco de Dados SQLite Local - GraphQL Mapper Plugin

## ✅ Implementação Completa

O plugin agora possui um **banco de dados SQLite local** totalmente funcional para salvar configurações e dados diretamente no workspace do Eclipse.

---

## 📁 Estrutura Criada

### 1. **Driver JDBC SQLite**
- **Localização**: `lib/sqlite-jdbc-3.43.0.0.jar`
- **Tamanho**: 13.2 MB
- **Versão**: 3.43.0.0
- **Fonte**: Maven Central Repository

### 2. **Classe de Gerenciamento**
- **Arquivo**: `src/graphql/mapper/db/ConfigurationDatabase.java`
- **Padrão**: Singleton
- **Funcionalidades**:
  - ✅ Inicialização automática do banco
  - ✅ Criação de tabelas
  - ✅ CRUD completo (Create, Read, Update, Delete)
  - ✅ Gestão de conexões

### 3. **Modelo de Dados**
- **Arquivo**: `src/graphql/mapper/model/Configuration.java`
- **Campos**:
  - `id` (Integer)
  - `graphqlUrl` (String)

### 4. **Integração com Interface**
- **Arquivo**: `src/graphql/mapper/dialogs/ConfigurationDialog.java`
- **Funcionalidades**:
  - Carrega dados salvos automaticamente
  - Salva/atualiza ao clicar em "Salvar"
  - Validações de entrada
  - Mensagens de feedback

---

## 🗂️ Localização do Banco de Dados

O arquivo `graphql_mapper.db` é salvo automaticamente em:

### Windows
```
C:\Users\[USUARIO]\workspace\.metadata\.plugins\GraphQL-Mapper\graphql_mapper.db
```

### Linux
```
/home/[USUARIO]/workspace/.metadata/.plugins/GraphQL-Mapper/graphql_mapper.db
```

### Mac
```
/Users/[USUARIO]/workspace/.metadata/.plugins/GraphQL-Mapper/graphql_mapper.db
```

> **Nota**: O caminho usa o diretório de estado do plugin no workspace do Eclipse, garantindo isolamento e organização.

---

## 🏗️ Configuração OSGi

### MANIFEST.MF
```
Bundle-ClassPath: .,
 lib/sqlite-jdbc-3.43.0.0.jar
```
- Define o JAR do SQLite no classpath do bundle OSGi
- Permite que o plugin acesse o driver JDBC

### build.properties
```
bin.includes = META-INF/,\
               .,\
               plugin.xml,\
               icons/,\
               lib/
```
- Inclui a pasta `lib/` no build do plugin
- Garante que o JAR seja empacotado no plugin final

---

## 📊 Estrutura do Banco de Dados

### Tabela: `configuration`

| Coluna | Tipo | Restrições | Descrição |
|--------|------|------------|-----------|
| `id` | INTEGER | PRIMARY KEY AUTOINCREMENT | Identificador único |
| `graphql_url` | TEXT | NOT NULL | URL do endpoint GraphQL |

### SQL de Criação
```sql
CREATE TABLE IF NOT EXISTS configuration (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    graphql_url TEXT NOT NULL
)
```

---

## 💻 Como Usar no Código

### 1. Obter Instância do Banco

```java
ConfigurationDatabase db = ConfigurationDatabase.getInstance();
```

### 2. Salvar/Atualizar Configuração

```java
boolean success = db.saveOrUpdateConfiguration("https://api.exemplo.com/graphql");

if (success) {
    System.out.println("Configuração salva com sucesso!");
} else {
    System.err.println("Erro ao salvar configuração");
}
```

### 3. Buscar Configuração

```java
Configuration config = db.getConfiguration();

if (config != null) {
    int id = config.getId();
    String url = config.getGraphqlUrl();
    System.out.println("ID: " + id + ", URL: " + url);
} else {
    System.out.println("Nenhuma configuração encontrada");
}
```

### 4. Deletar Todas as Configurações

```java
boolean deleted = db.deleteAllConfigurations();

if (deleted) {
    System.out.println("Configurações removidas");
}
```

---

## 🎯 API Completa - ConfigurationDatabase

| Método | Parâmetros | Retorno | Descrição |
|--------|-----------|---------|-----------|
| `getInstance()` | - | `ConfigurationDatabase` | Obtém instância Singleton |
| `saveOrUpdateConfiguration(String url)` | url: String | boolean | Salva nova ou atualiza existente |
| `getConfiguration()` | - | Configuration (nullable) | Busca primeira configuração |
| `deleteAllConfigurations()` | - | boolean | Remove todas as configurações |

### Métodos Privados (Internos)

| Método | Descrição |
|--------|-----------|
| `initializeDatabasePath()` | Define caminho do arquivo .db no workspace |
| `initializeDatabase()` | Cria tabela se não existir |
| `getConnection()` | Obtém conexão JDBC com o banco |
| `insertConfiguration(String url)` | Insere nova configuração |
| `updateConfiguration(int id, String url)` | Atualiza configuração existente |

---

## 🔧 Funcionamento Interno

### 1. Inicialização Automática

Ao criar a primeira instância:

```java
ConfigurationDatabase db = ConfigurationDatabase.getInstance();
```

**O que acontece:**
1. ✅ Obtém o diretório de estado do plugin
2. ✅ Define o caminho completo do arquivo `.db`
3. ✅ Cria a conexão JDBC
4. ✅ Executa `CREATE TABLE IF NOT EXISTS`
5. ✅ Pronto para uso!

### 2. Gerenciamento de Conexões

- **Try-with-resources**: Todas as conexões são fechadas automaticamente
- **Sem pool**: Para plugins simples, uma conexão por operação é suficiente
- **Thread-safe**: Singleton implementado com `synchronized`

### 3. Tratamento de Erros

- **SQLException**: Capturada e logada no console
- **Fallback**: Se não conseguir o diretório do plugin, usa diretório atual
- **Mensagens claras**: Logs informativos para debug

---

## 🚀 Compilação e Build

### Build Bem-Sucedido ✅

```bash
[INFO] BUILD SUCCESS
[INFO] Total time:  12.987 s
[INFO] Building jar: GraphQL-Mapper-1.0.0-SNAPSHOT.jar
```

### Comandos Disponíveis

```bash
# Compilar o plugin
mvn clean package

# Ou usar o script
.\build.bat          # Windows
./build.sh           # Linux/Mac

# Instalar no Eclipse
.\install.bat        # Windows
./install.sh         # Linux/Mac
```

---

## 🔍 Verificar o Banco de Dados

### 1. Usando SQLite CLI

```bash
# Instalar sqlite (se não tiver)
# Windows: scoop install sqlite
# Mac: brew install sqlite
# Linux: apt-get install sqlite3

# Conectar ao banco
cd C:\Users\[USUARIO]\workspace\.metadata\.plugins\GraphQL-Mapper
sqlite3 graphql_mapper.db

# Executar queries
SELECT * FROM configuration;
.schema configuration
.exit
```

### 2. Usando DB Browser for SQLite

1. Baixe [DB Browser for SQLite](https://sqlitebrowser.org/)
2. Abra o arquivo `graphql_mapper.db`
3. Visualize e edite dados graficamente

### 3. Via Código Java

```java
Configuration config = ConfigurationDatabase.getInstance().getConfiguration();
System.out.println("URL salva: " + config.getGraphqlUrl());
```

---

## 🛠️ Adicionar Novas Tabelas

### Exemplo: Tabela de Histórico

```java
private void initializeDatabase() {
    String createConfigTable = """
        CREATE TABLE IF NOT EXISTS configuration (
            id INTEGER PRIMARY KEY AUTOINCREMENT,
            graphql_url TEXT NOT NULL
        )
    """;
    
    String createHistoryTable = """
        CREATE TABLE IF NOT EXISTS history (
            id INTEGER PRIMARY KEY AUTOINCREMENT,
            query TEXT NOT NULL,
            response TEXT,
            created_at DATETIME DEFAULT CURRENT_TIMESTAMP
        )
    """;
    
    try (Connection conn = getConnection();
         Statement stmt = conn.createStatement()) {
        
        stmt.execute(createConfigTable);
        stmt.execute(createHistoryTable);
        System.out.println("Banco de dados inicializado!");
        
    } catch (SQLException e) {
        e.printStackTrace();
    }
}
```

---

## ✨ Vantagens do SQLite Local

✅ **Sem servidor**: Arquivo único, não precisa de servidor de banco  
✅ **Zero configuração**: Funciona imediatamente  
✅ **Portável**: Um único arquivo pode ser copiado/backupado  
✅ **Rápido**: Performance excelente para aplicações desktop  
✅ **Confiável**: Usado por milhões de aplicações  
✅ **SQL completo**: Suporta queries complexas, índices, etc.  
✅ **Tamanho pequeno**: O arquivo .db começa com apenas alguns KB  

---

## 🔐 Segurança

### ⚠️ Importante

- Os dados são salvos em **texto não criptografado** dentro do arquivo .db
- **Não salve senhas ou tokens** diretamente
- Para dados sensíveis, considere:
  - Eclipse Secure Storage API
  - Criptografar dados antes de salvar
  - Usar variáveis de ambiente

### Exemplo de Dados Seguros

```java
// ❌ NÃO FAÇA ISSO
config.setPassword("senha123");

// ✅ FAÇA ISSO
// Use Eclipse Secure Storage para dados sensíveis
ISecurePreferences securePrefs = SecurePreferencesFactory.getDefault();
securePrefs.put("password", "senha123", true);
```

---

## 🐛 Solução de Problemas

### Erro: "No suitable driver found"
✅ **RESOLVIDO!** O JAR está incluído no `Bundle-ClassPath`

### Banco não está sendo criado
1. Verifique os logs no Console do Eclipse
2. Confirme que o workspace tem permissão de escrita
3. Verifique o caminho com:
```java
System.out.println("DB Path: " + dbUrl);
```

### Dados não persistem
1. Verifique se `saveOrUpdateConfiguration()` retorna `true`
2. Confirme que não há exceções no log
3. Verifique se o arquivo `.db` existe no disco

### Performance lenta
- SQLite é extremamente rápido para plugins
- Se encontrar lentidão, considere:
  - Adicionar índices: `CREATE INDEX idx_url ON configuration(graphql_url)`
  - Usar transações para múltiplas operações

---

## 📚 Recursos Adicionais

### Documentação SQLite
- [SQLite Official](https://www.sqlite.org/)
- [JDBC SQLite](https://github.com/xerial/sqlite-jdbc)
- [SQL Tutorial](https://www.sqlitetutorial.net/)

### Eclipse Plugin Development
- [OSGi Bundle ClassPath](https://wiki.eclipse.org/Equinox/p2/FAQ#How_do_I_add_third-party_JARs_to_my_plug-in.3F)
- [Eclipse State Location](https://wiki.eclipse.org/FAQ_Where_is_the_workspace%3F)

---

## 📝 Exemplo Completo

```java
// 1. Obter instância
ConfigurationDatabase db = ConfigurationDatabase.getInstance();

// 2. Salvar dados
boolean saved = db.saveOrUpdateConfiguration("https://api.exemplo.com/graphql");
System.out.println("Salvou: " + saved);

// 3. Buscar dados
Configuration config = db.getConfiguration();
if (config != null) {
    System.out.println("ID: " + config.getId());
    System.out.println("URL: " + config.getGraphqlUrl());
}

// 4. Atualizar (mesma URL)
db.saveOrUpdateConfiguration("https://nova-api.exemplo.com/graphql");

// 5. Limpar tudo (se necessário)
db.deleteAllConfigurations();
```

---

## ✅ Checklist de Implementação

- [x] Driver JDBC SQLite baixado e adicionado
- [x] `MANIFEST.MF` configurado com `Bundle-ClassPath`
- [x] `build.properties` incluindo pasta `lib/`
- [x] Classe `ConfigurationDatabase` implementada
- [x] Modelo `Configuration` criado
- [x] Integração com `ConfigurationDialog`
- [x] Build compilando com sucesso
- [x] Documentação completa criada

---

**Versão**: 1.0.0  
**Data**: Outubro 2025  
**Status**: ✅ Pronto para Produção


