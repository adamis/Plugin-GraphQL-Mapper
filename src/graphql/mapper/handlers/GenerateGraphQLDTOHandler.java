package graphql.mapper.handlers;

import java.nio.file.Path;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.handlers.HandlerUtil;

import graphql.mapper.dialogs.GraphQLDTODialog;

/**
 * Handler para o comando "Gerar GraphQL DTO"
 */
public class GenerateGraphQLDTOHandler extends AbstractHandler {

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		IStructuredSelection selection = (IStructuredSelection) HandlerUtil.getCurrentSelection(event);
		IResource resource = null;

		if (selection != null && !selection.isEmpty()) {
			Object element = selection.getFirstElement();

			// Tenta converter para IResource
			if (element instanceof IResource) {
				resource = (IResource) element;
			} else if (element instanceof IAdaptable) {
				resource = ((IAdaptable) element).getAdapter(IResource.class);
			}

			String filePath = resource.getLocation().toOSString();			

			// Abre a janela com o path do arquivo
			GraphQLDTODialog dialog = new GraphQLDTODialog(
					HandlerUtil.getActiveShell(event),
					resource,
					filePath					
					);
			dialog.open();
		}


		return null;
	}
}

