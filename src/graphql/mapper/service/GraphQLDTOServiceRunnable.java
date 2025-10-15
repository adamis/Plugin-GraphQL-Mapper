package graphql.mapper.service;


import java.io.File;
import java.io.PrintWriter;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;

import com.fasterxml.jackson.databind.ObjectMapper;

import graphql.mapper.db.ConfigurationDatabase;
import graphql.mapper.enums.ColunsParams;
import graphql.mapper.model.Configuration;
import graphql.mapper.request.HeaderFactory;
import graphql.mapper.request.RequestClientFactory;
import graphql.mapper.service.dto.SendGraphQLDTO;
import graphql.mapper.utils.GraphQLParamExtractor;
import graphql.mapper.utils.JsonToClassGenerator;
import okhttp3.Response;

public class GraphQLDTOServiceRunnable implements Runnable {

	private String filePath;
	private String packageName;
	private String dtoName;
	private String queryJson;
	private List<HashMap<String, String>> listHM;
	private ObjectMapper mapper = new ObjectMapper();
	private String packageFilePath;

	public GraphQLDTOServiceRunnable(String filePath, String packageName, 
			List<HashMap<String, String>> listHM, String dtoName, String queryJson, String packageFilePath) {
		this.filePath = filePath;
		this.packageName = packageName;
		this.listHM = listHM;
		this.dtoName = dtoName;
		this.queryJson = queryJson;
		this.packageFilePath = packageFilePath;
	}

	@Override
	public void run() {

		try {
			this.queryJson = this.queryJson.replaceFirst("\\([^)]*\\)", "").trim();

			// INSERT_YOUR_CODE
			for (int i = 0; i < listHM.size(); i++) {
				String type = listHM.get(i).get(ColunsParams.TYPE.name()).equals("String!")?"\"":"";
				this.queryJson = this.queryJson.replace(listHM.get(i).get(ColunsParams.PARAM.name()), type+listHM.get(i).get(ColunsParams.VALUE.name())+type);
			}

			// Busca a configuração do banco SQLite e preenche a variável graphqlUrl
			String graphqlUrl = "";			
			ConfigurationDatabase configurationDatabase = ConfigurationDatabase.getInstance();
			Configuration configuration = configurationDatabase.getConfiguration();
			if (configuration != null && configuration.getGraphqlUrl() != null) {
				graphqlUrl = configuration.getGraphqlUrl();
			}

			SendGraphQLDTO sendGraphQLDTO = new SendGraphQLDTO();
			sendGraphQLDTO.setQuery(queryJson);
			sendGraphQLDTO.setOperationName(GraphQLParamExtractor.extrairNomeQuery(queryJson));
			
			String writeValueAsString = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(sendGraphQLDTO);
			
			RequestClientFactory RequestClientFactory = new RequestClientFactory();			
			Response sendPostRequest = RequestClientFactory.sendPostRequest(
					graphqlUrl, new HeaderFactory(),writeValueAsString, null, null, false);

			
			if (sendPostRequest != null && sendPostRequest.isSuccessful()) {
				String responseBody = sendPostRequest.body().string();
				
				//responseBody = jsonObject.getJSONArray(GraphQLParamExtractor.extrairNomeQuery(queryJson).toLowerCase()).toString();
				JsonToClassGenerator jsonToClassGenerator = new JsonToClassGenerator();
				String classFromJson = jsonToClassGenerator.generateClassFromJson(dtoName,packageName, responseBody,GraphQLParamExtractor.extrairNomeQuery(queryJson));
				
				File file = new File(packageFilePath+"/"+dtoName + ".java");
				
				System.out.println(""+file.getAbsolutePath());
				
				 try (PrintWriter out = new PrintWriter(file)) {
			            out.println(classFromJson);
			     }
				
				System.err.println("Class generated: \n" + classFromJson);
				
				
			} else {
				System.out.println("Erro na requisição GraphQL: " + (sendPostRequest != null ? sendPostRequest.code() : "No Response"));
			}

		} catch (Exception e) {			
			e.printStackTrace();
		}
	}

}
