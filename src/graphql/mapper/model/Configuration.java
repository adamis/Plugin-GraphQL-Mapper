package graphql.mapper.model;

/**
 * Modelo de configuração do plugin
 */
public class Configuration {
    
    private int id;
    private String graphqlUrl;
    
    public Configuration() {
    }
    
    public Configuration(int id, String graphqlUrl) {
        this.id = id;
        this.graphqlUrl = graphqlUrl;
    }
    
    public int getId() {
        return id;
    }
    
    public void setId(int id) {
        this.id = id;
    }
    
    public String getGraphqlUrl() {
        return graphqlUrl;
    }
    
    public void setGraphqlUrl(String graphqlUrl) {
        this.graphqlUrl = graphqlUrl;
    }
}

