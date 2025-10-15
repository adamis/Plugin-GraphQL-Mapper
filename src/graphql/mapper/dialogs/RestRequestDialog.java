package graphql.mapper.dialogs;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

/**
 * Janela para Montar Rest Request
 */
public class RestRequestDialog extends Dialog {

    private String filePath;
    private Text filePathText;

    public RestRequestDialog(Shell parentShell, String filePath) {
        super(parentShell);
        this.filePath = filePath;
    }

    @Override
    protected void configureShell(Shell newShell) {
        super.configureShell(newShell);
        newShell.setText("Montar Rest Request");
    }

    @Override
    protected Control createDialogArea(Composite parent) {
        Composite container = (Composite) super.createDialogArea(parent);
        GridLayout layout = new GridLayout(2, false);
        layout.marginHeight = 10;
        layout.marginWidth = 10;
        layout.verticalSpacing = 10;
        container.setLayout(layout);

        // Label do título
        Label titleLabel = new Label(container, SWT.NONE);
        titleLabel.setText("Montar Rest Request");
        titleLabel.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 2, 1));
        
        // Espaço
        new Label(container, SWT.NONE);
        new Label(container, SWT.NONE);

        // Label do path
        Label pathLabel = new Label(container, SWT.NONE);
        pathLabel.setText("Arquivo selecionado:");
        pathLabel.setLayoutData(new GridData(SWT.BEGINNING, SWT.CENTER, false, false));

        // Campo de texto com o path
        filePathText = new Text(container, SWT.BORDER | SWT.READ_ONLY | SWT.WRAP);
        filePathText.setText(filePath);
        GridData gridData = new GridData(SWT.FILL, SWT.CENTER, true, false);
        gridData.widthHint = 400;
        gridData.heightHint = 50;
        filePathText.setLayoutData(gridData);

        return container;
    }

    @Override
    protected void createButtonsForButtonBar(Composite parent) {
        createButton(parent, IDialogConstants.OK_ID, "OK", true);
        createButton(parent, IDialogConstants.CANCEL_ID, "Cancelar", false);
    }

    @Override
    protected boolean isResizable() {
        return true;
    }
}

