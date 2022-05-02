import java.sql.*;
import java.util.Locale;

public class JDBC {
     Connection connection;
     Statement statement;
     ResultSet resultSet;

    private String url;
    private String username;
    private String password;

    JDBC(String url, String username, String password) {
        this.url = url;
        this.username = username;
        this.password = password;
        try {
            connection = DriverManager.getConnection(url, username, password);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    String query(String text) {
        text = text.toLowerCase();
        try {
            if (!text.contains("select")) {
                PreparedStatement preparedStatement=connection.prepareStatement(text);
                preparedStatement.execute();
                return null;
            } else {
                statement = connection.createStatement();
                resultSet=statement.executeQuery(text);
                resultSet.next();
                return String.valueOf(resultSet.getString(1));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }



    //create acc:
    boolean doesntExist(String user) {
        try {
            statement = connection.createStatement();
            resultSet = statement.executeQuery("select username from users");
            while (resultSet.next()) {
                if (user.equals(resultSet.getString(1))) return false;
            }
        } catch (Exception ignored) {

        }
        return true;
    }

    public void insertion(String user, String pass) {
        PreparedStatement prepared = null;
        try {
            prepared = connection.prepareStatement("insert into users(username, password) value('" + user + "','" + pass + "')");
            prepared.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    //login
    boolean validAuth(String user, String pass) throws SQLException {
        if (!doesntExist(user)) {//if a user with this username already exists
            resultSet = statement.executeQuery("select password from users where username='" + user + "';");
            resultSet.next();
            String correctPass = resultSet.getString(1);
            if (correctPass.equals(pass))
                return true;
        }
        return false;
    }
}
