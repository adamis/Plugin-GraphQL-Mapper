# 📋 Tabela de Parâmetros Editável

## ✅ Implementação Completa

A tabela no `GraphQLDTODialog` agora possui duas colunas editáveis para gerenciar parâmetros.

---

## 🎯 Funcionalidades

### Colunas da Tabela

1. **Parâmetros** (200px)
   - Nome do parâmetro
   - Editável com duplo clique

2. **Valor** (250px)
   - Valor do parâmetro
   - Editável com duplo clique

### Botões de Controle

- **Adicionar**: Adiciona uma nova linha vazia na tabela
- **Remover**: Remove a linha selecionada

---

## 🖱️ Como Usar

### Adicionar Parâmetro

1. Clique no botão **"Adicionar"**
2. Uma nova linha vazia aparecerá na tabela

### Editar Célula

1. **Dê duplo clique** na célula que deseja editar
2. Um campo de texto aparecerá
3. Digite o valor desejado
4. Pressione **Enter** para salvar ou **Esc** para cancelar
5. Você também pode clicar fora da célula para salvar

### Remover Parâmetro

1. Clique na linha que deseja remover (para selecioná-la)
2. Clique no botão **"Remover"**

---

## 💻 Código Implementado

### Estrutura da Tabela

```java
// Criar tabela com seleção completa de linha
table = new Table(container, SWT.BORDER | SWT.FULL_SELECTION);
table.setHeaderVisible(true);
table.setLinesVisible(true);

// Coluna "Parâmetros"
TableColumn colParametros = new TableColumn(table, SWT.NONE);
colParametros.setText("Parâmetros");
colParametros.setWidth(200);

// Coluna "Valor"
TableColumn colValor = new TableColumn(table, SWT.NONE);
colValor.setText("Valor");
colValor.setWidth(250);
```

### Edição de Células

A edição é feita através de **duplo clique** usando `TableEditor`:

```java
private void editCell(Table table, TableItem item, int columnIndex) {
    final TableEditor editor = new TableEditor(table);
    final Text textEditor = new Text(table, SWT.NONE);
    textEditor.setText(item.getText(columnIndex));
    textEditor.selectAll();
    textEditor.setFocus();
    
    // Configurar editor
    editor.grabHorizontal = true;
    editor.minimumWidth = 50;
    
    // Salvar ao pressionar Enter ou perder foco
    textEditor.addListener(SWT.FocusOut, e -> {
        item.setText(columnIndex, textEditor.getText());
        textEditor.dispose();
        editor.dispose();
    });
    
    textEditor.addListener(SWT.Traverse, e -> {
        if (e.detail == SWT.TRAVERSE_RETURN) {
            // Enter: salvar
            item.setText(columnIndex, textEditor.getText());
            textEditor.dispose();
            editor.dispose();
        } else if (e.detail == SWT.TRAVERSE_ESCAPE) {
            // Esc: cancelar
            textEditor.dispose();
            editor.dispose();
        }
    });
    
    editor.setEditor(textEditor, item, columnIndex);
}
```

### Obter Parâmetros

Use o método `getParametros()` para obter os dados da tabela:

```java
Map<String, String> parametros = dialog.getParametros();

// Exemplo de uso
for (Map.Entry<String, String> entry : parametros.entrySet()) {
    System.out.println(entry.getKey() + " = " + entry.getValue());
}
```

---

## 📊 Exemplo de Uso

### No Dialog

```java
GraphQLDTODialog dialog = new GraphQLDTODialog(shell, resource, filePath);

if (dialog.open() == Dialog.OK) {
    // Obter parâmetros da tabela
    Map<String, String> parametros = dialog.getParametros();
    
    // Usar os parâmetros
    for (Map.Entry<String, String> entry : parametros.entrySet()) {
        String param = entry.getKey();    // "Authorization"
        String valor = entry.getValue();  // "Bearer token123"
        
        // Fazer algo com os parâmetros...
    }
}
```

### Adicionar Linha Programaticamente

```java
private void addTableRow(Table table, String parametro, String valor) {
    TableItem item = new TableItem(table, SWT.NONE);
    item.setText(0, parametro);
    item.setText(1, valor);
}

// Uso
addTableRow(table, "Authorization", "Bearer token");
addTableRow(table, "Content-Type", "application/json");
```

---

## 🎨 Características da Interface

### Visual

- ✅ Headers visíveis das colunas
- ✅ Linhas da tabela visíveis
- ✅ Seleção de linha completa
- ✅ Bordas ao redor da tabela

### Interação

- ✅ **Duplo clique** para editar qualquer célula
- ✅ **Enter** para salvar edição
- ✅ **Esc** para cancelar edição
- ✅ **Perder foco** também salva automaticamente
- ✅ Botão "Adicionar" para novas linhas
- ✅ Botão "Remover" para deletar linhas

---

## 🔧 Posicionamento

A tabela e botões estão posicionados em:

```java
// Tabela
table.setBounds(124, 205, 468, 77);

// Botão Adicionar
btnAdicionar.setBounds(124, 288, 100, 25);

// Botão Remover
btnRemover.setBounds(230, 288, 100, 25);
```

---

## 💡 Dicas de Uso

### Para Parâmetros HTTP Headers

```
Parâmetros              | Valor
-----------------------|---------------------------
Authorization          | Bearer eyJhbGciOiJIUzI1...
Content-Type           | application/json
X-Custom-Header        | custom-value
```

### Para Query Parameters

```
Parâmetros              | Valor
-----------------------|---------------------------
page                   | 1
limit                  | 10
filter                 | active
```

### Para Variables GraphQL

```
Parâmetros              | Valor
-----------------------|---------------------------
userId                 | 123
includeProfile         | true
fields                 | name,email,avatar
```

---

## 🚀 Extensões Futuras

Você pode adicionar mais funcionalidades:

### 1. Validação de Campos

```java
private boolean validateParameters() {
    for (TableItem item : table.getItems()) {
        String param = item.getText(0);
        String valor = item.getText(1);
        
        if (param.isEmpty() && !valor.isEmpty()) {
            showError("Erro", "Parâmetro vazio com valor preenchido!");
            return false;
        }
    }
    return true;
}
```

### 2. Salvar/Carregar Templates

```java
public void saveParametersTemplate(String templateName) {
    Map<String, String> params = getParametros();
    // Salvar no banco SQLite ou arquivo
}

public void loadParametersTemplate(String templateName) {
    // Carregar do banco SQLite ou arquivo
    // Preencher tabela com os parâmetros carregados
}
```

### 3. Importar de JSON

```java
public void importFromJSON(String json) {
    JSONObject obj = new JSONObject(json);
    for (String key : obj.keySet()) {
        addTableRow(table, key, obj.getString(key));
    }
}
```

### 4. Exportar para JSON

```java
public String exportToJSON() {
    JSONObject obj = new JSONObject();
    Map<String, String> params = getParametros();
    for (Map.Entry<String, String> entry : params.entrySet()) {
        obj.put(entry.getKey(), entry.getValue());
    }
    return obj.toString(2); // Pretty print com indentação 2
}
```

---

## ✅ Status

**Implementação completa e funcional!**

- [x] Duas colunas criadas
- [x] Edição com duplo clique
- [x] Botão Adicionar
- [x] Botão Remover
- [x] Método getParametros()
- [x] Validação básica (ignora linhas vazias)
- [x] Compilação sem erros

---

**Pronto para usar!** 🎉

