package graphql.mapper.db;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import graphql.mapper.Activator;
import graphql.mapper.model.Configuration;

/**
 * Gerenciador do banco de dados SQLite para configurações
 * Salva o arquivo .db no diretório de estado do plugin dentro do workspace
 */
public class ConfigurationDatabase {
    
    private static final String DB_NAME = "graphql_mapper.db";
    private String dbUrl;
    
    private static ConfigurationDatabase instance;
    
    private ConfigurationDatabase() {
        initializeDatabasePath();
        initializeDatabase();
    }
    
    /**
     * Obtém a instância única do banco de dados (Singleton)
     */
    public static synchronized ConfigurationDatabase getInstance() {
        if (instance == null) {
            instance = new ConfigurationDatabase();
        }
        return instance;
    }
    
    /**
     * Inicializa o caminho do banco de dados
     * Usa o diretório de estado do plugin no workspace do Eclipse
     */
    private void initializeDatabasePath() {
        try {
            // Obtém o diretório de estado do plugin usando o Activator
            Activator activator = Activator.getDefault();
            if (activator != null) {
                File stateLocation = activator.getStateLocation().toFile();
                
                // Cria o arquivo do banco de dados no diretório de estado
                File dbFile = new File(stateLocation, DB_NAME);
                
                // Monta a URL JDBC
                dbUrl = "jdbc:sqlite:" + dbFile.getAbsolutePath();
                
                System.out.println("Banco de dados SQLite será salvo em: " + dbFile.getAbsolutePath());
            } else {
                // Fallback se o activator ainda não foi inicializado
                System.err.println("Aviso: Activator não inicializado. Usando diretório atual.");
                dbUrl = "jdbc:sqlite:" + DB_NAME;
            }
            
        } catch (Exception e) {
            // Fallback: salva no diretório atual se houver erro
            System.err.println("Aviso: Não foi possível obter diretório de estado do plugin. Usando diretório atual.");
            e.printStackTrace();
            dbUrl = "jdbc:sqlite:" + DB_NAME;
        }
    }
    
    /**
     * Obtém conexão com o banco de dados
     */
    private Connection getConnection() throws SQLException {
        return DriverManager.getConnection(dbUrl);
    }
    
    /**
     * Inicializa o banco de dados criando a tabela se não existir
     */
    private void initializeDatabase() {
        String createTableSQL = """
            CREATE TABLE IF NOT EXISTS configuration (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                graphql_url TEXT NOT NULL
            )
        """;
        
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement()) {
            
            stmt.execute(createTableSQL);
            System.out.println("Banco de dados inicializado com sucesso!");
            
        } catch (SQLException e) {
            System.err.println("Erro ao inicializar banco de dados: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    /**
     * Salva ou atualiza a configuração
     * Se já existir uma configuração, atualiza. Caso contrário, cria uma nova.
     */
    public boolean saveOrUpdateConfiguration(String graphqlUrl) {
        Configuration existing = getConfiguration();
        
        if (existing != null) {
            return updateConfiguration(existing.getId(), graphqlUrl);
        } else {
            return insertConfiguration(graphqlUrl);
        }
    }
    
    /**
     * Insere uma nova configuração
     */
    private boolean insertConfiguration(String graphqlUrl) {
        String insertSQL = "INSERT INTO configuration (graphql_url) VALUES (?)";
        
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(insertSQL)) {
            
            pstmt.setString(1, graphqlUrl);
            int affectedRows = pstmt.executeUpdate();
            
            System.out.println("Configuração inserida com sucesso!");
            return affectedRows > 0;
            
        } catch (SQLException e) {
            System.err.println("Erro ao inserir configuração: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    
    /**
     * Atualiza uma configuração existente
     */
    private boolean updateConfiguration(int id, String graphqlUrl) {
        String updateSQL = "UPDATE configuration SET graphql_url = ? WHERE id = ?";
        
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(updateSQL)) {
            
            pstmt.setString(1, graphqlUrl);
            pstmt.setInt(2, id);
            int affectedRows = pstmt.executeUpdate();
            
            System.out.println("Configuração atualizada com sucesso!");
            return affectedRows > 0;
            
        } catch (SQLException e) {
            System.err.println("Erro ao atualizar configuração: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    
    /**
     * Obtém a configuração atual (primeira configuração encontrada)
     */
    public Configuration getConfiguration() {
        String selectSQL = "SELECT id, graphql_url FROM configuration LIMIT 1";
        
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(selectSQL)) {
            
            if (rs.next()) {
                Configuration config = new Configuration();
                config.setId(rs.getInt("id"));
                config.setGraphqlUrl(rs.getString("graphql_url"));
                return config;
            }
            
        } catch (SQLException e) {
            System.err.println("Erro ao buscar configuração: " + e.getMessage());
            e.printStackTrace();
        }
        
        return null;
    }
    
    /**
     * Remove todas as configurações (útil para testes)
     */
    public boolean deleteAllConfigurations() {
        String deleteSQL = "DELETE FROM configuration";
        
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement()) {
            
            int affectedRows = stmt.executeUpdate(deleteSQL);
            System.out.println("Todas as configurações foram removidas!");
            return affectedRows > 0;
            
        } catch (SQLException e) {
            System.err.println("Erro ao deletar configurações: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
}

