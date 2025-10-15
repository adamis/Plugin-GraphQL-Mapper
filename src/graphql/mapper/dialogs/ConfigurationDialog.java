package graphql.mapper.dialogs;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import graphql.mapper.db.ConfigurationDatabase;
import graphql.mapper.model.Configuration;

/**
 * Janela para Configuração do Plugin
 */
public class ConfigurationDialog extends Dialog {

    
    private Text graphqlUrlText;
    private ConfigurationDatabase database;

    public ConfigurationDialog(Shell parentShell) {
        super(parentShell);        
        this.database = ConfigurationDatabase.getInstance();
    }

    @Override
    protected void configureShell(Shell newShell) {
        super.configureShell(newShell);
        newShell.setText("Configuração");
    }

    @Override
    protected Control createDialogArea(Composite parent) {
        Composite container = (Composite) super.createDialogArea(parent);
        container.setLayout(null);

        // Label do path
        Label pathLabel = new Label(container, SWT.NONE);
        pathLabel.setBounds(10, 24, 111, 15);
        pathLabel.setText("URL do GraphQL");
        
        graphqlUrlText = new Text(container, SWT.BORDER);
        graphqlUrlText.setBounds(10, 47, 414, 45);
        
        // Carrega a configuração existente do banco de dados
        loadConfiguration();

        return container;
    }
    
    /**
     * Carrega a configuração existente do banco de dados
     */
    private void loadConfiguration() {
        Configuration config = database.getConfiguration();
        if (config != null && config.getGraphqlUrl() != null) {
            graphqlUrlText.setText(config.getGraphqlUrl());
        }
    }

    @Override
    protected void createButtonsForButtonBar(Composite parent) {
        Button button = createButton(parent, IDialogConstants.OK_ID, "OK", true);
        button.setText("Salvar");
        createButton(parent, IDialogConstants.CANCEL_ID, "Cancelar", false);
    }
    
    @Override
    protected void okPressed() {
        // Salva ou atualiza a configuração no banco de dados
        String graphqlUrl = graphqlUrlText.getText().trim();
        
        if (graphqlUrl.isEmpty()) {
            MessageDialog.openWarning(getShell(), "Atenção", 
                "Por favor, informe a URL do GraphQL!");
            return;
        }
        
        boolean success = database.saveOrUpdateConfiguration(graphqlUrl);
        
        if (success) {
            MessageDialog.openInformation(getShell(), "Sucesso", 
                "Configuração salva com sucesso!");
            super.okPressed();
        } else {
            MessageDialog.openError(getShell(), "Erro", 
                "Erro ao salvar configuração. Verifique os logs.");
        }
    }

    @Override
    protected boolean isResizable() {
        return true;
    }
    
    /**
     * Retorna a URL do GraphQL configurada
     */
    public String getGraphqlUrl() {
        return graphqlUrlText.getText().trim();
    }
}

