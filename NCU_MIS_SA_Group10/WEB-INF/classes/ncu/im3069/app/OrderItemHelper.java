package ncu.im3069.app;

import java.sql.*;
import java.util.*;

import org.json.*;

import ncu.im3069.util.DBMgr;

public class OrderItemHelper {
    
    private OrderItemHelper() {
        
    }
    
    private static OrderItemHelper oph;
    private Connection conn = null;
    private PreparedStatement pres = null;
    
    /**
     * 靜態方法<br>
     * 實作Singleton（單例模式），僅允許建立一個MemberHelper物件
     *
     * @return the helper 回傳MemberHelper物件
     */
    public static OrderItemHelper getHelper() {
        /** Singleton檢查是否已經有MemberHelper物件，若無則new一個，若有則直接回傳 */
        if(oph == null) oph = new OrderItemHelper();
        
        return oph;
    }
    
    public JSONArray createByList(long order_id, List<OrderItem> orderproduct) {
        JSONArray jsa = new JSONArray();
        /** 記錄實際執行之SQL指令 */
        String exexcute_sql = "";
        
        for(int i=0 ; i < orderproduct.size() ; i++) {
            OrderItem op = orderproduct.get(i);
            
            /** 取得所需之參數 */
            int product_id = op.getProductId();
            int price = op.getPrice();
            int quantity = op.getQuantity();
            String size=op.getSize();
            String ice=op.getIce();
            String sugar=op.getSugar();
 
            
            try {
                /** 取得資料庫之連線 */
                conn = DBMgr.getConnection();
                /** SQL指令 */
                String sql = "INSERT INTO `sa_group10`.`tblorderdetail`(`OrderId`, `ProductId`, `Amount`, `Size`, `Sugar`, `Ice` ,`Price`)"
                        + " VALUES(?, ?, ?, ?, ?, ?, ?)";
                
                /** 將參數回填至SQL指令當中 */
                pres = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
                pres.setLong(1, order_id);
                pres.setInt(2, product_id);
                pres.setInt(3, quantity);
                pres.setString(4,size);
                pres.setString(5,sugar);
                pres.setString(6,ice);
                pres.setInt(7,price);
                
                /** 執行新增之SQL指令並記錄影響之行數 */
                pres.executeUpdate();
                
                /** 紀錄真實執行的SQL指令，並印出 **/
                exexcute_sql = pres.toString();
                System.out.println(exexcute_sql);
                
                ResultSet rs = pres.getGeneratedKeys();

                if (rs.next()) {
                    long id = rs.getLong(1);
                    jsa.put(id);
                }
            } catch (SQLException e) {
                /** 印出JDBC SQL指令錯誤 **/
                System.err.format("SQL State: %s\n%s\n%s", e.getErrorCode(), e.getSQLState(), e.getMessage());
            } catch (Exception e) {
                /** 若錯誤則印出錯誤訊息 */
                e.printStackTrace();
            } finally {
                /** 關閉連線並釋放所有資料庫相關之資源 **/
                DBMgr.close(pres, conn);
            }
        }
        
        return jsa;
    }
    
    public ArrayList<OrderItem> getOrderProductByOrderId(int order_id) {
        ArrayList<OrderItem> result = new ArrayList<OrderItem>();
        /** 記錄實際執行之SQL指令 */
        String exexcute_sql = "";
        ResultSet rs = null;
        OrderItem op;
        
        try {
            /** 取得資料庫之連線 */
            conn = DBMgr.getConnection();
            /** SQL指令 */
            String sql = "SELECT * FROM `sa_group10`.`tblorderdetail` WHERE `tblorderdetail`.`OrderId` = ?";
            
            /** 將參數回填至SQL指令當中 */
            pres = conn.prepareStatement(sql);
            pres.setInt(1, order_id);
            
            /** 執行新增之SQL指令並記錄影響之行數 */
            rs = pres.executeQuery();
            
            /** 紀錄真實執行的SQL指令，並印出 **/
            exexcute_sql = pres.toString();
            System.out.println(exexcute_sql);
            
            while(rs.next()) {
                /** 每執行一次迴圈表示有一筆資料 */
                
                /** 將 ResultSet 之資料取出 */
                int order_product_id = rs.getInt("idtblOrderDetail");
                int product_id = rs.getInt("ProductId");
                int price = rs.getInt("Price");
                int quantity = rs.getInt("Amount");
                String size=rs.getString("Size");
                String sugar=rs.getString("Sugar");
                String ice=rs.getString("Ice");

                
                /** 將每一筆會員資料產生一名新Member物件 */
                op = new OrderItem(order_product_id, order_id, product_id, price, quantity,ice,sugar,size);
                /** 取出該名會員之資料並封裝至 JSONsonArray 內 */
                result.add(op);
            }
        } catch (SQLException e) {
            /** 印出JDBC SQL指令錯誤 **/
            System.err.format("SQL State: %s\n%s\n%s", e.getErrorCode(), e.getSQLState(), e.getMessage());
        } catch (Exception e) {
            /** 若錯誤則印出錯誤訊息 */
            e.printStackTrace();
        } finally {
            /** 關閉連線並釋放所有資料庫相關之資源 **/
            DBMgr.close(pres, conn);
        }
        
        return result;
    }
}
