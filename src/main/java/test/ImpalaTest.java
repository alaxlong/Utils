package test;

import java.sql.*;

/**
 * \* @Created with IntelliJ IDEA.
 * \* @author: baibing.shang
 * \* @Date: 2018/12/24
 * \* @Description:
 * \
 */
public class ImpalaTest {
    private static String driverName = "org.apache.hive.jdbc.HiveDriver";
    public static void main(String[] args){
        /*try {
            try {
                Class.forName(driverName);
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
                System.exit(1);
            }
            Connection con = DriverManager.getConnection("jdbc:hive2://42.159.93.67:10000/test", "hive", "hive123");
            Statement stmt = con.createStatement();
            String sql = "select count(*) from customer";
            System.out.println("Running: " + sql);
            ResultSet res = stmt.executeQuery(sql);
            if (res.next()) {
                System.out.println(res.getString(1));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }*/

        try {
            select();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static Connection getConnection() throws ClassNotFoundException, SQLException{
        String driver = "org.apache.hive.jdbc.HiveDriver";
        String url = "jdbc:hive2://42.159.93.67:21050/chunqiu";
        String username = "";
        String password = "";
        Connection conn = null;
        Class.forName(driver);
        conn = (Connection) DriverManager.getConnection(url,username,password);
        return conn;
    }

    public static void select() throws ClassNotFoundException, SQLException{
        Connection conn = getConnection();
        String sql = "select * from hive_lab_user;";
        Statement stmt = conn.createStatement();
            System.out.println("Running: " + sql);
            ResultSet res = stmt.executeQuery(sql);
            if (res.next()) {
                System.out.println(res.getString(1));
            }
        /*PreparedStatement ps = conn.prepareStatement(sql);
        ResultSet rs = ps.executeQuery();
        int col = rs.getMetaData().getColumnCount();
        System.out.println("=====================================");
        while (rs.next()){
            for(int i=1;i<=col;i++){
                System.out.print(rs.getString(i)+"\t");
            }
            System.out.print("\n");
        }*/
        System.out.println("=====================================");
    }


}