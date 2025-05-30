import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Conexao {
    public static void main(String[] args) {
        // Configurações do banco de dados
        String url = "jdbc:mysql://localhost:3306/cerimoniasdb";
        String user = "root";
        String password = "CH0c0late6477667.";

        // Objetos para conexão, declaração e resultados
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");

            // Estabelecer a conexão com o banco de dados
            connection = DriverManager.getConnection(url, user, password);

            // Criar uma declaração SQL
            String sql = "SELECT * FROM minha_tabela";
            statement = connection.prepareStatement(sql);

            // Executar a consulta
            resultSet = statement.executeQuery();

            // Processar os resultados
            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String nome = resultSet.getString("nome");
                System.out.println("ID: " + id + ", Nome: " + nome);
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            // Fechar os recursos
            try {
                if (resultSet != null) {
                    resultSet.close();
                }
                if (statement != null) {
                    statement.close();
                }
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}