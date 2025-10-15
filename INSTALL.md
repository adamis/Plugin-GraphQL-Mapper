# Guia de Instalação - GraphQL Mapper Plugin

## 📦 Instalação do Plugin no Eclipse

### Pré-requisitos

- **Eclipse IDE** (versão 2024-03 ou superior recomendada)
- **Java 21** ou superior

### Método 1: Instalação via Dropins (Mais Simples)

1. **Compile o projeto** (se ainda não fez):
   ```bash
   mvn clean package
   ```

2. **Localize o JAR do plugin**:
   - O arquivo estará em: `target/GraphQL-Mapper-1.0.0-SNAPSHOT.jar`

3. **Copie o JAR para a pasta dropins do Eclipse**:
   
   **Windows:**
   ```cmd
   copy target\GraphQL-Mapper-1.0.0-SNAPSHOT.jar "C:\eclipse\dropins\"
   ```
   
   **Linux/macOS:**
   ```bash
   cp target/GraphQL-Mapper-1.0.0-SNAPSHOT.jar ~/eclipse/dropins/
   ```

4. **Reinicie o Eclipse**

5. **Verifique a instalação**:
   - Clique com botão direito em qualquer arquivo no Project Explorer
   - Deve aparecer o menu "GraphQL Mapper" com as opções disponíveis

### Método 2: Instalação via Feature/Update Site

#### Passo 1: Criar Feature Project

1. No Eclipse: `File > New > Other... > Plug-in Development > Feature Project`
2. Nome: `graphql.mapper.feature`
3. Adicione o plugin `GraphQL-Mapper` na feature

#### Passo 2: Criar Update Site

1. `File > New > Other... > Plug-in Development > Update Site Project`
2. Nome: `graphql.mapper.updatesite`
3. Adicione a feature criada
4. Build All
5. Use a pasta gerada como repositório local

### Método 3: Instalação Manual com Link

1. **Crie um arquivo de link**:
   
   **Windows** - Crie `C:\eclipse\dropins\graphql-mapper.link`:
   ```
   path=C:/Temp/GraphQL-Mapper/target
   ```
   
   **Linux/macOS** - Crie `~/eclipse/dropins/graphql-mapper.link`:
   ```
   path=/path/to/GraphQL-Mapper/target
   ```

2. **Reinicie o Eclipse**

## ✅ Verificar Instalação

### Verificação Visual

1. Abra qualquer projeto Java no Eclipse
2. Clique com botão direito em um arquivo `.java` ou pasta
3. Procure por **"GraphQL Mapper"** no menu de contexto

### Verificação no Plugin Management

1. Vá em `Help > About Eclipse IDE`
2. Clique em `Installation Details`
3. Na aba `Plug-ins`, procure por `GraphQL-Mapper`
4. Deve aparecer: `GraphQL-Mapper (1.0.0.qualifier)`

### Verificação via Error Log

Se o plugin não aparecer:
1. Vá em `Window > Show View > Other... > General > Error Log`
2. Procure por erros relacionados ao bundle `GraphQL-Mapper`

## 🔧 Configuração Adicional (Opcional)

### Atalhos de Teclado

Para criar atalhos para os comandos do plugin:

1. `Window > Preferences > General > Keys`
2. Procure por "GraphQL" no campo de busca
3. Defina atalhos para:
   - `Gerar GraphQL DTO`
   - `Montar Rest Request`

### Personalizar Ícones

Os ícones do plugin estão em `icons/`. Para personalizar:

1. Substitua os arquivos:
   - `plugin_icon.png` (ícone principal)
   - `plugin_icon32.png` (ícone 32x32)
   - `package_icon.png` (ícone do pacote)

2. Recompile o projeto:
   ```bash
   mvn clean package
   ```

3. Reinstale o plugin

## 🐛 Troubleshooting

### Problema: Plugin não aparece no menu

**Possíveis causas:**
- Plugin não foi instalado corretamente
- Dependências do Eclipse não foram satisfeitas
- Eclipse precisa ser reiniciado com `-clean`

**Solução:**
```bash
eclipse -clean
```

### Problema: "Unresolved requirement"

**Solução:**
Verifique se você tem todos os componentes necessários do Eclipse instalados:
- Eclipse Platform
- Eclipse JDT (Java Development Tools)
- Eclipse UI

### Problema: "ClassNotFoundException"

**Solução:**
Certifique-se de que o Eclipse está usando Java 21:
1. `Window > Preferences > Java > Installed JREs`
2. Adicione/selecione JDK 21

### Problema: Plugin instalado mas não funciona

**Verificações:**
1. Verifique o MANIFEST.MF tem todas as dependências corretas
2. Verifique o Error Log do Eclipse
3. Execute o Eclipse em modo console:
   ```bash
   eclipse -consoleLog
   ```

## 🚀 Desenvolvimento e Debug

### Executar em Modo de Desenvolvimento

1. Importe o projeto no Eclipse como Maven Project
2. Clique com botão direito no projeto
3. `Run As > Eclipse Application`
4. Uma nova instância do Eclipse será aberta com o plugin

### Debug do Plugin

1. Clique com botão direito no projeto
2. `Debug As > Eclipse Application`
3. Defina breakpoints no código
4. Use a nova instância do Eclipse para testar

## 📝 Notas Importantes

- O plugin requer **Java 21** devido ao `Bundle-RequiredExecutionEnvironment` no MANIFEST.MF
- A versão do plugin inclui `.qualifier` que é substituído pela timestamp do Tycho durante o build
- Para ambientes de produção, considere criar um Update Site ou Feature para facilitar atualizações

## 🔄 Atualizar Plugin

Para atualizar uma versão já instalada:

1. **Remova a versão antiga:**
   - Delete o JAR da pasta `dropins`
   - Ou desinstale via `Help > About > Installation Details`

2. **Compile a nova versão:**
   ```bash
   mvn clean package
   ```

3. **Instale a nova versão** usando um dos métodos acima

4. **Reinicie o Eclipse com -clean:**
   ```bash
   eclipse -clean
   ```

## 📚 Recursos Adicionais

- [Documentação de Plugins Eclipse](https://www.eclipse.org/pde/)
- [OSGi Framework](https://www.osgi.org/)
- [Tycho Build](https://eclipse.dev/tycho/)


