package graphql.mapper.handlers;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.resources.IResource;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.handlers.HandlerUtil;

import graphql.mapper.dialogs.ConfigurationDialog;

/**
 * Handler para o comando "Montar Rest Request"
 */
public class ConfigurationHandler extends AbstractHandler {

    @Override
    public Object execute(ExecutionEvent event) throws ExecutionException {
        IStructuredSelection selection = (IStructuredSelection) HandlerUtil.getCurrentSelection(event);
        
        if (selection != null && !selection.isEmpty()) {
            Object element = selection.getFirstElement();
            
            if (element instanceof IResource) {
//                IResource resource = (IResource) element;
//                String filePath = resource.getLocation().toOSString();
                
                // Abre a janela com o path do arquivo
                ConfigurationDialog dialog = new ConfigurationDialog(HandlerUtil.getActiveShell(event));
                dialog.open();
            }
        }
        
        return null;
    }
}

