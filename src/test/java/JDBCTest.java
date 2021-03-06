import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;

class JDBCTest {
    JDBC jdbc=new JDBC("jdbc:mysql://localhost:3307/users","root","Password123");
    @Test
    void verifyDuplicate() {
        System.out.println(jdbc.doesntExist("alex"));
        Assertions.assertFalse(jdbc.doesntExist("alex"));
    }

    @Test
    void auth(){
        try {
            Assertions.assertFalse(jdbc.validAuth("alex","password"));
            Assertions.assertFalse(jdbc.validAuth("andreiiii","password"));
            Assertions.assertTrue(jdbc.validAuth("andrei","Password123"));

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Test
    void selectId() throws SQLException {
        jdbc.statement = jdbc.connection.createStatement();
        jdbc.resultSet = jdbc.statement.executeQuery("select status, task from tasks where user_id=1;");
        while(jdbc.resultSet.next()){
           if(jdbc.resultSet.getInt(1)==1){
               System.out.println("yes");
           }
        }
    }
}