import java.lang.reflect.InvocationTargetException;
import java.sql.*;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

public class MySQLClass2
{
    public MySQLClass2(){
        baseCreate();
        tableCreate();
        cashTableCreate();
    }

    public Connection getConnection(String dbName) throws SQLException, ClassNotFoundException, NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        String url = "jdbc:mysql://localhost/" + ((dbName != null)? (dbName) : (""));
        String username = "root";
        String password = "1234";
        Class.forName("com.mysql.cj.jdbc.Driver").getDeclaredConstructor().newInstance();

        return DriverManager.getConnection(url, username, password);
    }

    public void baseCreate(){
        try{
            Connection conn = null;
            Statement st = null;

            try{
                conn = getConnection(null);
                st = conn.createStatement();
                st.executeUpdate("CREATE DATABASE IF NOT EXISTS farm");
            }
            finally {
                try{
                    if(conn != null){
                        conn.close();
                    }
                } catch (Exception e){
                    e.printStackTrace();
                }
                try{
                    if(st != null){
                        st.close();
                    }
                } catch (Exception e){
                    e.printStackTrace();
                }
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public void tableCreate(){
        try{
            Connection conn = null;
            Statement st = null;

            try{
                conn = getConnection("farm");
                st = conn.createStatement();
                st.executeUpdate("CREATE TABLE IF NOT EXISTS farm.plants " +
                        "(field INT NOT NULL, plant VARCHAR(20) NOT NULL, sPrice INT NOT NULL, " +
                        "hPrice INT NOT NULL, timer BIGINT NOT NULL, sign VARCHAR(20) NOT NULL)");
            }
            finally {
                try{
                    if(conn != null){
                        conn.close();
                    }
                } catch (Exception e){
                    e.printStackTrace();
                }
                try{
                    if(st != null){
                        st.close();
                    }
                } catch (Exception e){
                    e.printStackTrace();
                }
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public void cashTableCreate(){
        try{
            Connection conn = null;
            Statement st = null;

            try{
                conn = getConnection("farm");
                st = conn.createStatement();
                st.executeUpdate("CREATE TABLE IF NOT EXISTS farm.cash (cash INT NOT NULL)");
            }
            finally{
                try{
                    if(conn != null){
                        conn.close();
                    }
                } catch (Exception e){
                    e.printStackTrace();
                }
                try{
                    if(st != null){
                        st.close();
                    }
                } catch (Exception e){
                    e.printStackTrace();
                }
            }
        } catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public void addPlant(Integer field, Field plant){
        try{
            Connection conn = null;
            PreparedStatement ps = null;

            try{
                conn = getConnection("farm");

                ps = conn.prepareStatement("INSERT INTO plants" + "(field, plant, sPrice, hPrice, timer, sign) " +
                        "VALUES(?, ?, ?, ?, ?, ?)  ON DUPLICATE KEY UPDATE plant=?, sPrice=?, hPrice=?, timer=?, sign=?");
                ps.setInt(1, field);
                ps.setString(2, plant.getPlant().getName());
                ps.setInt(3, plant.getPlant().getSeedPrice());
                ps.setInt(4, plant.getPlant().getHarvestPrice());
                ps.setLong(5, plant.getPlant().getTime());
                ps.setString(6, plant.getSign().getSymbol());
                //  ON DUPLICATE

                ps.setString(7, plant.getPlant().getName());
                ps.setInt(8, plant.getPlant().getSeedPrice());
                ps.setInt(9, plant.getPlant().getHarvestPrice());
                ps.setLong(10, plant.getPlant().getTime());
                ps.setString(11, plant.getSign().getSymbol());

                ps.executeUpdate();

            } finally {
                try{
                    if(conn != null){
                        conn.close();
                    }
                } catch (Exception e){
                    e.printStackTrace();
                }
                try{
                    if(ps != null){
                        ps.close();
                    }
                } catch (Exception e){
                    e.printStackTrace();
                }
            }
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    public void addCash(Integer cash){
        try{
            Connection conn = null;
            PreparedStatement ps = null;

            try{
                conn = getConnection("farm");
                ps = conn.prepareStatement("INSERT INTO cash (cash) VALUES (?)");
                ps.setInt(1, cash);
                ps.executeUpdate();
            } finally {
                try{
                    if(conn != null){
                        conn.close();
                    }
                } catch (Exception e){
                    e.printStackTrace();
                }
                try{
                    if(ps != null){
                        ps.close();
                    }
                } catch (Exception e){
                    e.printStackTrace();
                }
            }
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    public Map<Integer, Field> cache(){
        Map<Integer, Field> map = new ConcurrentHashMap<>();

        try{
            Connection conn = null;
            PreparedStatement ps = null;
            ResultSet rs = null;

            try{
                conn = getConnection("farm");
                String query = "SELECT * FROM plants";
                ps = conn.prepareStatement(query);
                rs = ps.executeQuery();

                while (rs.next()){
                    try{
                        int field = rs.getInt("field");
                        String plantStr = rs.getString("plant");
                        int sPrice = rs.getInt("sPrice");
                        int hPrice = rs.getInt("hPrice");
                        long timer = rs.getLong("timer");
                        String signStr = rs.getString("sign");
                        SIGN sign = SIGN.valueOfDesc(signStr);
                        Plant plant = new Plant(plantStr, sPrice, hPrice, timer);
                        Field f = new Field(sign, plant);

                        map.put(field, f);
                    } catch (Exception e){
                        e.printStackTrace();
                    }
                }
            } finally {
                try{
                    if(conn != null){
                        conn.close();
                    }
                } catch (Exception e){
                    e.printStackTrace();
                }
                try{
                    if(ps != null){
                        ps.close();
                    }
                } catch (Exception e){
                    e.printStackTrace();
                }
                try{
                    if(rs != null){
                        rs.close();
                    }
                } catch (Exception e){
                    e.printStackTrace();
                }
            }
        } catch (Exception e){
            e.printStackTrace();
        }

        return map;
    }

    public List<Integer> cacheCash(){
        List<Integer> list = new CopyOnWriteArrayList<>();

        try{
            Connection conn = null;
            PreparedStatement ps = null;
            ResultSet rs = null;

            try{
                conn = getConnection("farm");
                String query = "SELECT * FROM cash";
                ps = conn.prepareStatement(query);
                rs = ps.executeQuery();

                while (rs.next()){
                    try{
                        int cash = rs.getInt("cash");

                        list.add(cash);
                    } catch (Exception e){
                        e.printStackTrace();
                    }
                }
            } finally {
                try{
                    if(conn != null){
                        conn.close();
                    }
                } catch (Exception e){
                    e.printStackTrace();
                }
                try{
                    if(ps != null){
                        ps.close();
                    }
                } catch (Exception e){
                    e.printStackTrace();
                }
                try{
                    if(rs != null){
                        rs.close();
                    }
                } catch (Exception e){
                    e.printStackTrace();
                }
            }
        } catch (Exception e){
            e.printStackTrace();
        }

        return list;
    }
}
