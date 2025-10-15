package graphql.mapper.utils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Gera automaticamente uma classe Java com Lombok (@Data)
 * a partir de um arquivo JSON arbitrário.
 */
public class JsonToClassGenerator {

	private List<String> imports = new ArrayList<>();
	private List<String> variaveis = new ArrayList<>();
	private List<String> innerClasses = new ArrayList<>();
	
	private static String PACKAGE_NAME ;
	private static String ROOT_CLASS_NAME;


	public String generateClassFromJson(String dtoName,String packageDTO, String responseBody, String nameQuery) throws IOException {
		
		ROOT_CLASS_NAME = dtoName;		
		PACKAGE_NAME = packageDTO;
		
		JSONObject jsonObject = new JSONObject(responseBody);
		jsonObject = jsonObject.getJSONObject("data");
		
		JSONArray jsonArray = jsonObject.getJSONArray(nameQuery.toLowerCase());
	    carregaObjetos(jsonArray);

		StringBuilder sb = new StringBuilder();		
		sb.append("package ").append(PACKAGE_NAME).append(";\n\n");
		
		sb.append("import lombok.Data;").append("\n");		
		for (int i = 0; i < imports.size(); i++) {
			sb.append(imports.get(i)).append("\n");
		}
		
		sb.append("\n");
		
		sb.append("@Data\n");
		sb.append("public class ").append(ROOT_CLASS_NAME).append(" {\n\n");

		for (int i = 0; i < variaveis.size(); i++) {
			sb.append(variaveis.get(i)).append("\n");
		}
		
		sb.append("\n");
		
		for (int i = 0; i < innerClasses.size(); i++) {
			sb.append(innerClasses.get(i)).append("\n");
		}
		sb.append("\n");
		sb.append("}\n");

		return sb.toString();
		
//		try (PrintWriter out = new PrintWriter(ROOT_CLASS_NAME + ".java")) {
//			out.println(sb.toString());
//		}
//
//		System.out.println("✅ Classe gerada: " + ROOT_CLASS_NAME + ".java");
	}

	private void carregaObjetos(JSONArray jsonArray) {
		
	// INSERT_YOUR_CODE

		// Limpa as listas para evitar dados antigos caso o método seja chamado novamente
		imports.clear();
		variaveis.clear();
		innerClasses.clear();

		// Se jsonArray é vazio, não faz nada
		if (jsonArray == null || jsonArray.length() == 0) return;

		// Para armazenar tipos de dados dos campos, ajuda na verificação de tipos
		Map<String, String> tiposCampos = new LinkedHashMap<>();

		// Retorna o objeto não nulo mais completo do array para inspecionar todos os campos
		JSONObject exampleObj = null;
		int maxFields = 0;
		for (int i = 0; i < jsonArray.length(); i++) {
			JSONObject obj = jsonArray.optJSONObject(i);
			if (obj != null && obj.length() > maxFields) {
				exampleObj = obj;
				maxFields = obj.length();
			}
		}

		if (exampleObj == null) { // Só tem arrays nulos ou vazios
			return;
		}

		// Descobrir tipos de cada campo baseado no objeto de exemplo 
		for (String key : exampleObj.keySet()) {
			Object val = exampleObj.opt(key);

			// Check nulls nos outros objetos para melhor detecção de tipos
			for (int j = 0; j < jsonArray.length(); j++) {
				JSONObject obj = jsonArray.optJSONObject(j);
				if (obj != null && obj.has(key) && obj.opt(key) != null) {
					val = obj.opt(key);
					break;
				}
			}

			if (val == null) {
				tiposCampos.put(key, "Object");
			} else if (val instanceof JSONArray) {
				JSONArray arr = (JSONArray) val;
				if (arr.length() > 0 && arr.opt(0) instanceof JSONObject) {
					// Gerar classe interna recursivamente
					String className = toCamelCase(key, true);
					carregaInnerClasse(className, arr);
					tiposCampos.put(key, "List<" + className + ">");
					if (!imports.contains("import java.util.List;")) {
						imports.add("import java.util.List;");
					}
				} else {
					// Array simples
					String baseType = "Object";
					if (arr.length() > 0) {
						Object arrVal = arr.opt(0);
						baseType = javaTypeOf(arrVal);
					}
					tiposCampos.put(key, baseType + "[]");
				}
			} else if (val instanceof JSONObject) {
				String className = toCamelCase(key, true);
				JSONArray arrTemp = new JSONArray();
				arrTemp.put(val);
				carregaInnerClasse(className, arrTemp);
				tiposCampos.put(key, className);
			} else {
				String type = javaTypeOf(val);
				if ("OffsetDateTime".equals(type) && !imports.contains("import java.time.OffsetDateTime;")) {
					imports.add("import java.time.OffsetDateTime;");
				}
				tiposCampos.put(key, type);
			}
		}

		// Montar lista de variáveis
		for (Map.Entry<String, String> entry : tiposCampos.entrySet()) {
			String varDecl = "    private " + entry.getValue() + " " + entry.getKey() + ";";
			variaveis.add(varDecl);
		}

	// Métodos auxiliares locais

	}

	// Função recursiva para criar inner classes para Objetos e Arrays de Objetos
	private void carregaInnerClasse(String className, JSONArray arr) {
		if (arr == null || arr.length() == 0) return;

		// Pega o objeto mais completo do array
		JSONObject exampleObj = null;
		int maxFields = 0;
		for (int i = 0; i < arr.length(); i++) {
			JSONObject obj = arr.optJSONObject(i);
			if (obj != null && obj.length() > maxFields) {
				exampleObj = obj;
				maxFields = obj.length();
			}
		}
		if (exampleObj == null) return;

		Map<String, String> tiposCampos = new LinkedHashMap<>();

		for (String key : exampleObj.keySet()) {
			Object val = exampleObj.opt(key);

			// Tenta pegar valor não nulo no restante do array
			for (int j = 0; j < arr.length(); j++) {
				JSONObject obj = arr.optJSONObject(j);
				if (obj != null && obj.has(key) && obj.opt(key) != null) {
					val = obj.opt(key);
					break;
				}
			}

			if (val == null) {
				tiposCampos.put(key, "Object");
			} else if (val instanceof JSONArray) {
				JSONArray subArr = (JSONArray) val;
				if (subArr.length() > 0 && subArr.opt(0) instanceof JSONObject) {
					String innerClassName = toCamelCase(key, true);
					carregaInnerClasse(innerClassName, subArr);
					tiposCampos.put(key, "List<" + innerClassName + ">");
					if (!imports.contains("import java.util.List;")) {
						imports.add("import java.util.List;");
					}
				} else {
					String baseType = "Object";
					if (subArr.length() > 0) {
						Object arrVal = subArr.opt(0);
						baseType = javaTypeOf(arrVal);
					}
					tiposCampos.put(key, baseType + "[]");
				}
			} else if (val instanceof JSONObject) {
				String innerClassName = toCamelCase(key, true);
				JSONArray arrTemp = new JSONArray();
				arrTemp.put(val);
				carregaInnerClasse(innerClassName, arrTemp);
				tiposCampos.put(key, innerClassName);
			} else {
				String type = javaTypeOf(val);
				if ("OffsetDateTime".equals(type) && !imports.contains("import java.time.OffsetDateTime;")) {
					imports.add("import java.time.OffsetDateTime;");
				}
				tiposCampos.put(key, type);
			}
		}

		// Monta o corpo da inner class
		StringBuilder sb = new StringBuilder();
		sb.append("    @Data\n");
		sb.append("    public static class ").append(className).append(" {\n");
		for (Map.Entry<String, String> entry : tiposCampos.entrySet()) {
			sb.append("        private ").append(entry.getValue()).append(" ").append(entry.getKey()).append(";\n");
		}
		sb.append("    }\n");
		innerClasses.add(sb.toString());
	}

	// Função para determinar tipo Java do objeto
	private String javaTypeOf(Object val) {
		if (val instanceof Integer || val instanceof Long) return "Integer";
		if (val instanceof Double || val instanceof Float) return "Double";
		if (val instanceof Boolean) return "Boolean";
		if (val instanceof String) {
			String s = (String) val;
			// Checa se é data ISO-8601, então sugere OffsetDateTime
			if (s.matches("^\\d{4}-\\d{2}-\\d{2}.*[T ].*")) return "OffsetDateTime";
			return "String";
		}
		return "Object";
	}

	// Helper para transformar nomes em PascalCase
	private String toCamelCase(String s, boolean upperFirst) {
		String[] parts = s.replaceAll("[^a-zA-Z0-9_]", "_").split("_");
		StringBuilder sb = new StringBuilder();
		for (String part : parts) {
			if (part.isEmpty()) continue;
			sb.append(part.substring(0,1).toUpperCase()).append(part.substring(1));
		}
		String result = sb.toString();
		if (!upperFirst && !result.isEmpty()) {
			result = result.substring(0,1).toLowerCase() + result.substring(1);
		}
		return result;
	}

		
	

}

