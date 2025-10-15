package graphql.mapper.service;


import java.util.HashMap;
import java.util.List;

import graphql.mapper.db.ConfigurationDatabase;
import graphql.mapper.enums.ColunsParams;
import graphql.mapper.model.Configuration;
import graphql.mapper.request.HeaderFactory;
import graphql.mapper.request.RequestClientFactory;
import okhttp3.Response;

public class GraphQLDTOServiceRunnable implements Runnable {

	String filePath;
	String packageName;
	String dtoName;
	String queryJson;
	List<HashMap<String, String>> listHM;

	public GraphQLDTOServiceRunnable(String filePath, String packageName, 
			List<HashMap<String, String>> listHM, String dtoName, String queryJson) {
		this.filePath = filePath;
		this.packageName = packageName;
		this.listHM = listHM;
		this.dtoName = dtoName;
		this.queryJson = queryJson;
	}

	@Override
	public void run() {

		try {
			this.queryJson = this.queryJson.replaceFirst("\\([^)]*\\)", "").trim();

			// INSERT_YOUR_CODE
			for (int i = 0; i < listHM.size(); i++) {
				this.queryJson = this.queryJson.replace(listHM.get(i).get(ColunsParams.PARAM.name()), listHM.get(i).get(ColunsParams.VALUE.name()));
			}

			// Busca a configuração do banco SQLite e preenche a variável graphqlUrl
			String graphqlUrl = "";			
			ConfigurationDatabase configurationDatabase = ConfigurationDatabase.getInstance();
			Configuration configuration = configurationDatabase.getConfiguration();
			if (configuration != null && configuration.getGraphqlUrl() != null) {
				graphqlUrl = configuration.getGraphqlUrl();
			}

			RequestClientFactory RequestClientFactory = new RequestClientFactory();			
			Response sendPostRequest = RequestClientFactory.sendPostRequest(graphqlUrl, new HeaderFactory(), queryJson, null, null, false);

			//Thread.sleep(6000);

		} catch (Exception e) {			
			e.printStackTrace();
		}
	}

}
