package graphql.mapper.dialogs;

import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.core.resources.IResource;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IPackageFragment;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.search.IJavaSearchScope;
import org.eclipse.jdt.core.search.SearchEngine;
import org.eclipse.jdt.ui.JavaUI;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.dialogs.SelectionDialog;

import graphql.mapper.enums.ColunsParams;
import graphql.mapper.service.GraphQLDTOServiceRunnable;
import graphql.mapper.utils.GraphQLParamExtractor;
import graphql.mapper.utils.Utils;

import org.eclipse.swt.custom.TableEditor;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;

/**
 * Janela para Gerar GraphQL DTO
 */
public class GraphQLDTODialog extends Dialog {

    private String filePath;
    private String queryJson;
    private Text filePathText;
    private IResource resource;
    private Text packagePath;
    private Text text;
    private Table table;
    private String packageFilePath;
   

    public GraphQLDTODialog(Shell parentShell, IResource resource, String filePath) {
        super(parentShell);
        this.filePath = filePath;
        this.resource = resource;
     
    }

    @Override
    protected void configureShell(Shell newShell) {
        super.configureShell(newShell);
        newShell.setText("Gerar GraphQL DTO");
    }

    @Override
    protected Control createDialogArea(Composite parent) {
        Composite container = (Composite) super.createDialogArea(parent);
        container.setLayout(null);

        // Label do path
        Label pathLabel = new Label(container, SWT.NONE);
        pathLabel.setBounds(10, 13, 111, 15);
        pathLabel.setText("Arquivo selecionado:");

        // Campo de texto com o path
        filePathText = new Text(container, SWT.BORDER | SWT.READ_ONLY | SWT.WRAP);
        filePathText.setBounds(126, 10, 466, 56);
        filePathText.setText(filePath);
        
        Label lblArquivoDto = new Label(container, SWT.NONE);
        lblArquivoDto.setText("Package DTO:");
        lblArquivoDto.setBounds(10, 84, 111, 15);
        
        packagePath = new Text(container, SWT.BORDER | SWT.READ_ONLY | SWT.WRAP);
        packagePath.setBounds(126, 84, 407, 40);
        
        Button btnNewButton = new Button(container, SWT.NONE);
        btnNewButton.setText("...");
        btnNewButton.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                openPackageSelector();
            }
        });
        btnNewButton.setBounds(539, 84, 53, 40);
        
        Label lblArquivoDto_1 = new Label(container, SWT.NONE);
        lblArquivoDto_1.setText("Arquivo DTO:");
        lblArquivoDto_1.setBounds(10, 146, 111, 15);
        
        text = new Text(container, SWT.BORDER | SWT.RIGHT);
        text.setBounds(126, 143, 407, 21);
        
        Label lbljava = new Label(container, SWT.NONE);
        lbljava.setBounds(537, 146, 55, 15);
        lbljava.setText(".java");
        
        table = new Table(container, SWT.BORDER | SWT.FULL_SELECTION);
        table.setBounds(124, 180, 468, 127);
        table.setHeaderVisible(true);
        table.setLinesVisible(true);
        
        // Criar coluna "Parâmetros"
        TableColumn colParametros = new TableColumn(table, SWT.NONE);
        colParametros.setText("Parâmetros");
        colParametros.setWidth(200);
        
     // Criar coluna "Parâmetros"
        TableColumn colType = new TableColumn(table, SWT.NONE);
        colType.setText("Type");
        colType.setWidth(100);
        
        // Criar coluna "Valor"
        TableColumn colValor = new TableColumn(table, SWT.NONE);
        colValor.setText("Valor");
        colValor.setWidth(250);
        
        // Tornar as células editáveis
        makeTableEditable(table);
        
        // Botão para adicionar linha
        Button btnAdicionar = new Button(container, SWT.NONE);
        btnAdicionar.setText("Adicionar");
        btnAdicionar.setBounds(126, 313, 100, 25);
        btnAdicionar.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                addTableRow(table, "", "","");
            }
        });
        
        // Botão para remover linha selecionada
        Button btnRemover = new Button(container, SWT.NONE);
        btnRemover.setText("Remover");
        btnRemover.setBounds(231, 313, 100, 25);
        btnRemover.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                int selectedIndex = table.getSelectionIndex();
                if (selectedIndex >= 0) {
                    table.remove(selectedIndex);
                }
            }
        });
        
        Label lblArquivoDto_1_1 = new Label(container, SWT.NONE);
        lblArquivoDto_1_1.setText("Parametros:");
        lblArquivoDto_1_1.setBounds(10, 233, 111, 15);

        
		try {
			
			queryJson = Utils.readTxt(filePath);
			Map<String, String> params = GraphQLParamExtractor.extrairParametros(queryJson);
			
			// Preenche colParametros com a lista params
			for (Map.Entry<String, String> entry : params.entrySet()) {
			    String param = entry.getKey();
			    String type = entry.getValue();
			    addTableRow(table, param, type,"");			    
			}

		} catch (IOException e1) {
			e1.printStackTrace();
			return null;			
		}
        
        return container;
    }

    
    private void openPackageSelector() {
        try {
            // Obtém o projeto Java
            IJavaProject javaProject = JavaCore.create(resource.getProject());
            
            // Cria o escopo de busca (apenas o projeto atual)
            IJavaSearchScope scope = SearchEngine.createJavaSearchScope(new IJavaProject[] { javaProject });
            
            // Abre o diálogo de seleção de package
            SelectionDialog dialog = JavaUI.createPackageDialog(
                getShell(), 
                javaProject, 
                0  // flags - 0 para comportamento padrão
            );
            
            dialog.setTitle("Selecionar Package");
            dialog.setMessage("Selecione o package onde o DTO será criado:");
            
            // Exibe o diálogo e verifica se o usuário clicou em OK
            if (dialog.open() == SelectionDialog.OK) {
                Object[] results = dialog.getResult();
                if (results != null && results.length > 0) {
                    IPackageFragment selectedPackage = (IPackageFragment) results[0];
                    // INSERT_YOUR_CODE
                    // Pega o caminho real do sistema de arquivos para o package selecionado
                    IResource packageResource = selectedPackage.getCorrespondingResource();
                    packageFilePath = packageResource.getLocation().toOSString();                    
                    packagePath.setText(selectedPackage.getElementName());
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    
    @Override
    protected void createButtonsForButtonBar(Composite parent) {
        createButton(parent, IDialogConstants.OK_ID, "Gerar DTO", true);
        createButton(parent, IDialogConstants.CANCEL_ID, "Cancelar", false);
    }
    
    @Override
    protected void okPressed() {
        // Validar campos antes de executar
        String packageName = packagePath.getText().trim();
        String dtoName = text.getText().trim();
        
        if (packageName.isEmpty()) {
            showError("Erro de Validação", "Por favor, selecione um package para o DTO.");
            return; // NÃO fecha o diálogo
        }
        
        if (dtoName.isEmpty()) {
            showError("Erro de Validação", "Por favor, informe o nome do arquivo DTO.");
            return; // NÃO fecha o diálogo
        }
        
        // Validar nome do arquivo (não pode ter caracteres especiais)
        if (!dtoName.matches("[a-zA-Z0-9_]+")) {
            showError("Erro de Validação", "O nome do arquivo DTO deve conter apenas letras, números e underscore.");
            return; // NÃO fecha o diálogo
        }
        
        try {
        	
        	List<HashMap<String, String>> listHM = new ArrayList<HashMap<String,String>>();
        	
			for (int i = 0; i < table.getItemCount(); i++) {
				org.eclipse.swt.widgets.TableItem item = table.getItem(i);
				
				String param = item.getText(0);   // coluna Param
				
				if(param.isEmpty()) continue;
				
				String type = item.getText(1);   // coluna Type
				String valor = item.getText(2);  // coluna Valor
				HashMap<String, String> hm = new HashMap<String, String>();
				hm.put(ColunsParams.PARAM.name(), param);
				hm.put(ColunsParams.TYPE.name(), type);
				hm.put(ColunsParams.VALUE.name(), valor);
				listHM.add(hm);
			}
        	 		     
	            // Criar e executar a thread de geração do DTO        	
            GraphQLDTOServiceRunnable runnable = new GraphQLDTOServiceRunnable(filePath,packageName,listHM,dtoName, queryJson,packageFilePath);
            Thread thread = new Thread(runnable, "Gerando DTO");
            thread.start(); // Usar start() ao invés de run() para executar em nova thread
            
            // Aguardar conclusão (com timeout de 16 segundos)
            thread.join(16000);
            
            // Verificar se a thread ainda está rodando (timeout)
            if (thread.isAlive()) {
                thread.interrupt();
                showError("Timeout", "A geração do DTO está demorando muito. Tente novamente.");
                return; // NÃO fecha o diálogo
            }
            
            // Se chegou aqui, sucesso! Fecha o diálogo
            super.okPressed();
            
        } catch (InterruptedException e) {
            showError("Erro de Execução", "A geração do DTO foi interrompida: " + e.getMessage());
            Thread.currentThread().interrupt(); // Restaurar flag de interrupção
            // NÃO fecha o diálogo
        } catch (Exception e) {
            showError("Erro Inesperado", "Ocorreu um erro ao gerar o DTO:\n" + e.getMessage());
            e.printStackTrace();
            // NÃO fecha o diálogo
        }
    }
    
    /**
     * Exibe uma mensagem de erro ao usuário
     */
    private void showError(String title, String message) {
        org.eclipse.jface.dialogs.MessageDialog.openError(
            getShell(),
            title,
            message
        );
    }

    @Override
    protected boolean isResizable() {
        return true;
    }
    
    /**
     * Getters para serem usados pelo service
     */
    public String getPackageName() {
        return packagePath != null ? packagePath.getText().trim() : "";
    }
    
    public String getDtoName() {
        return text != null ? text.getText().trim() : "";
    }
    
    public String getFilePath() {
        return filePath;
    }
    
    public IResource getResource() {
        return resource;
    }
    
    /**
     * Adiciona uma linha à tabela de parâmetros
     */
    private void addTableRow(Table table, String parametro, String type, String valor) {
        TableItem item = new TableItem(table, SWT.NONE);
        item.setText(0, parametro);
        item.setText(1, type);
        item.setText(2, valor);
    }
    
    /**
     * Torna as células da tabela editáveis
     */
    private void makeTableEditable(Table table) {
        // Listener para editar ao dar duplo clique
        table.addListener(SWT.MouseDoubleClick, event -> {
            TableItem item = table.getItem(table.getSelectionIndex());
            if (item == null) return;
            
            // Determinar qual coluna foi clicada
            int columnIndex = -1;
            int x = 0;
            for (int i = 0; i < table.getColumnCount(); i++) {
                x += table.getColumn(i).getWidth();
                if (event.x < x) {
                    columnIndex = i;
                    break;
                }
            }
            
            if (columnIndex >= 0) {
                editCell(table, item, columnIndex);
            }
        });
    }
    
    /**
     * Edita uma célula da tabela
     */
    private void editCell(Table table, TableItem item, int columnIndex) {
        // Criar editor de texto
        final TableEditor editor = new TableEditor(table);
        final Text textEditor = new Text(table, SWT.NONE);
        textEditor.setText(item.getText(columnIndex));
        textEditor.selectAll();
        textEditor.setFocus();
        
        // Configurar o editor
        editor.grabHorizontal = true;
        editor.minimumWidth = 50;
        
        // Salvar ao perder foco ou pressionar Enter
        final int col = columnIndex;
        textEditor.addListener(SWT.FocusOut, e -> {
            item.setText(col, textEditor.getText());
            textEditor.dispose();
            editor.dispose();
        });
        
        textEditor.addListener(SWT.Traverse, e -> {
            if (e.detail == SWT.TRAVERSE_RETURN) {
                item.setText(col, textEditor.getText());
                textEditor.dispose();
                editor.dispose();
                e.doit = false;
            } else if (e.detail == SWT.TRAVERSE_ESCAPE) {
                textEditor.dispose();
                editor.dispose();
                e.doit = false;
            }
        });
        
        editor.setEditor(textEditor, item, columnIndex);
    }
    
    /**
     * Retorna os parâmetros da tabela como um Map
     */
    public java.util.Map<String, String> getParametros() {
        java.util.Map<String, String> parametros = new java.util.HashMap<>();
        
        for (TableItem item : table.getItems()) {
            String parametro = item.getText(0).trim();
            String valor = item.getText(1).trim();
            
            if (!parametro.isEmpty()) {
                parametros.put(parametro, valor);
            }
        }
        
        return parametros;
    }
}

