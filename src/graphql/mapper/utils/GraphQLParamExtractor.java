package graphql.mapper.utils;

import java.util.*;
import java.util.regex.*;

public class GraphQLParamExtractor {

	public static Map<String, String> extrairParametros(String graphqlQuery) {
        Map<String, String> parametros = new LinkedHashMap<>();

        // Pega o trecho entre parênteses da definição da query
        Pattern pattern = Pattern.compile("\\([^)]*\\)");
        Matcher matcher = pattern.matcher(graphqlQuery);

        if (matcher.find()) {
            String parametrosStr = matcher.group(); // Exemplo: "($idCadastro: String!, $numeroLegislatura: String!)"
            parametrosStr = parametrosStr.replaceAll("[()]", "").trim();

            // Divide os parâmetros por vírgula
            String[] partes = parametrosStr.split(",");
            for (String parte : partes) {
                // Expressão regular para pegar $nome e tipo
                Matcher m = Pattern.compile("\\$(\\w+)\\s*:\\s*([^=,]+)").matcher(parte.trim());
                if (m.find()) {
                    String nome = m.group(1).trim();
                    String tipo = m.group(2).trim();
                    parametros.put("$"+nome, tipo);
                }
            }
        }

        return parametros;
    }

    
}
