package Util;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class GetLoginOwner {

    private Connection conn;
    private String token;

    public String getOwner(Connection connection) throws SQLException {

        this.conn = connection;
        Statement stmnt = null;
        try {
            String sql = "SELECT ownerID FROM discordsettings";
            stmnt = conn.createStatement();
            ResultSet rs = stmnt.executeQuery(sql);

            if (!rs.next() ) {
                token = "err";
                rs.close();
            }
            else{
                token = rs.getString("ownerID");
                rs.close();
            }
        } catch (SQLException e) {

            System.out.println(e.getLocalizedMessage());
        }
        finally{
            stmnt.close();
            return token;
        }


    }

}
