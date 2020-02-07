package ncu.im3069.app;

import java.sql.*;
import java.util.*;

import org.json.*;

import ncu.im3069.util.DBMgr;

public class OrderHelper {
    
    private Connection conn = null;
    private PreparedStatement pres = null;
    private OrderItemHelper oph =  OrderItemHelper.getHelper();
    private static OrderHelper oh;
    
    private OrderHelper() {
    }
    
    public static OrderHelper getHelper() 
    {
        if(oh == null) oh = new OrderHelper();

        return oh;
    }
    
    //新增訂單到資料庫
    public JSONObject create(Order order) 
    {
        //紀錄SQL的語法
    	
        String exexcute_sql = "";
        long id = -1;
        JSONArray opa = new JSONArray();
        ResultSet rs = null;
        try 
        {         
            conn = DBMgr.getConnection();//取得資料庫連線

            String sql = "INSERT INTO `sa_group10`.`tblorder`(`MemberId`, `TotalPrice`, `OrderStatus`, `PaymentMethod`, `CreditCardNum`,`Address`,`OrderDateTime`)"
                    + " VALUES(?, ?, ?, ?, ?, ?, ?)";//SQL語法->新增資料到order資料庫表格
            
            //設置要輸入的參數
            int member_id = order.getMemberId();
            int total_price = order.getTotalPrice();
            int status = order.getStatus();
            String paymethod = order.getPaymethod();
            String creditcard_number = order.getCreditCard();
            String address = order.getAddress();
            Timestamp create = order.getCreateTime();
 
            //將參數塞到上面的問號裡
            pres = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            pres.setInt(1,member_id);
            pres.setInt(2,total_price);
            pres.setInt(3, status);
            pres.setString(4,paymethod);
            pres.setString(5,creditcard_number);
            pres.setString(6,address);
            pres.setTimestamp(7, create);
            
            //紀錄影響了幾行
            pres.executeUpdate();
            
            //紀錄SQL真的跑了啥並印出
            exexcute_sql = pres.toString();
            System.out.println(exexcute_sql);
            
            rs = pres.getGeneratedKeys();
            
            //將購物車的東西塞進資料庫裡
            if (rs.next()) 
            {
                id = rs.getLong(1);
                ArrayList<OrderItem> opd = order.getOrderProduct();
                opa = oph.createByList(id, opd);
            }
        } 
        catch (SQLException e) 
        {
            /** 印出JDBC SQL指令錯誤 **/
            System.err.format("SQL State: %s\n%s\n%s", e.getErrorCode(), e.getSQLState(), e.getMessage());
        } 
        catch (Exception e) 
        {
            /** 若錯誤則印出錯誤訊息 */
            e.printStackTrace();
        } 
        finally 
        {
            /** 關閉連線並釋放所有資料庫相關之資源 **/
            DBMgr.close(rs,pres, conn);
        }

        /** 將SQL指令、花費時間與影響行數，封裝成JSONObject回傳 */
        JSONObject response = new JSONObject();
        response.put("order_id", id);
        response.put("order_product_id", opa);

        return response;
    }
    
    //取得訂單資料
    public JSONObject getAll() 
    {
        Order o = null;
        JSONArray jsa = new JSONArray();
        String exexcute_sql = "";
        long start_time = System.nanoTime();
        
        int row = 0;//從第零列開始，後面會增加
        ResultSet rs = null;
        
        try 
        {
            conn = DBMgr.getConnection();
            String sql = "SELECT * FROM `sa_group10`.`tblorder`";//SQL語法->取得所有訂單資料
            
            pres = conn.prepareStatement(sql);
            rs = pres.executeQuery();

            exexcute_sql = pres.toString();
            System.out.println(exexcute_sql);
            
            //透過迴圈來將資料塞入Array裡->每有一筆先塞進去Order物件裡
            while(rs.next()) {
                row += 1;
                
                int id = rs.getInt("idtblOrder");
                int member_id = rs.getInt("MemberId");
                int total_price = rs.getInt("TotalPrice");
                int status = rs.getInt("OrderStatus");
                String paymethod = rs.getString("PaymentMethod");
                String creditcard_number = rs.getString("CreditCardNum");
                String address = rs.getString("Address");
                Timestamp create = rs.getTimestamp("OrderDateTime");
                System.out.println(status);
                o = new Order(id,member_id,total_price,status,paymethod,creditcard_number,address,create);
                jsa.put(o.getOrderAllInfo());
            }

        } 
        catch (SQLException e) 
        {
            System.err.format("SQL State: %s\n%s\n%s", e.getErrorCode(), e.getSQLState(), e.getMessage());
        } 
        catch (Exception e) 
        {
            e.printStackTrace();
        } 
        finally 
        {
            DBMgr.close(rs, pres, conn);
        }
        
        /** 紀錄程式結束執行時間 */
        long end_time = System.nanoTime();
        /** 紀錄程式執行時間 */
        long duration = (end_time - start_time);
        
        /** 將SQL指令、花費時間、影響行數與所有會員資料之JSONArray，封裝成JSONObject回傳 */
        JSONObject response = new JSONObject();
        response.put("sql", exexcute_sql);
        response.put("row", row);
        response.put("time", duration);
        response.put("data", jsa);

        return response;
    }
    
    //查詢訂單要用ㄉ
    public JSONObject getById(String order_id) 
    {
        JSONObject data = new JSONObject();
        Order o = null;
        String exexcute_sql = "";
        /** 紀錄程式開始執行時間 */
        long start_time = System.nanoTime();
        /** 紀錄SQL總行數 */
        int row = 0;
        /** 儲存JDBC檢索資料庫後回傳之結果，以 pointer 方式移動到下一筆資料 */
        ResultSet rs = null;
        
        try 
        {
            conn = DBMgr.getConnection();
            String sql = "SELECT * FROM `sa_group10`.`tblorder` WHERE `tblorder`.`idtblOrder` = ?";
            pres = conn.prepareStatement(sql);
            pres.setString(1, order_id);
            /** 執行查詢之SQL指令並記錄其回傳之資料 */
            rs = pres.executeQuery();

            /** 紀錄真實執行的SQL指令，並印出 **/
            exexcute_sql = pres.toString();
            System.out.println(exexcute_sql);
            
            /** 透過 while 迴圈移動pointer，取得每一筆回傳資料 */
            while(rs.next()) {
                /** 每執行一次迴圈表示有一筆資料 */
                row += 1;
                
                /** 將 ResultSet 之資料取出 */
                int id = rs.getInt("idtblOrder");
                int member_id = rs.getInt("MemberId");
                int total_price = rs.getInt("TotalPrice");
                int status = rs.getInt("OrderStatus");
                String paymethod = rs.getString("PaymentMethod");
                String creditcard_number = rs.getString("CreditCardNum");
                String address = rs.getString("Address");
                Timestamp create = rs.getTimestamp("OrderDateTime");
                
                /** 將每一筆商品資料產生一名新Product物件 */
                o = new Order(id,member_id,total_price,status,paymethod,creditcard_number,address,create);
                /** 取出該項商品之資料並封裝至 JSONsonArray 內 */
                data = o.getOrderAllInfo();
            }

        } catch (SQLException e) {
            /** 印出JDBC SQL指令錯誤 **/
            System.err.format("SQL State: %s\n%s\n%s", e.getErrorCode(), e.getSQLState(), e.getMessage());
        } catch (Exception e) {
            /** 若錯誤則印出錯誤訊息 */
            e.printStackTrace();
        } finally {
            /** 關閉連線並釋放所有資料庫相關之資源 **/
            DBMgr.close(rs, pres, conn);
        }
        
        /** 紀錄程式結束執行時間 */
        long end_time = System.nanoTime();
        /** 紀錄程式執行時間 */
        long duration = (end_time - start_time);
        
        /** 將SQL指令、花費時間、影響行數與所有會員資料之JSONArray，封裝成JSONObject回傳 */
        
        JSONObject response = new JSONObject();
        response.put("sql", exexcute_sql);
        response.put("row", row);
        response.put("time", duration);
        response.put("data", data);

        return response;
    }
    
    //Member 查詢訂單
    public JSONObject getByMemberId(String get_member_id) 
    {
        JSONObject data = new JSONObject();
        JSONArray jsa = new JSONArray();
        Order o = null;
        String exexcute_sql = "";
        /** 紀錄程式開始執行時間 */
        long start_time = System.nanoTime();
        /** 紀錄SQL總行數 */
        int row = 0;
        /** 儲存JDBC檢索資料庫後回傳之結果，以 pointer 方式移動到下一筆資料 */
        ResultSet rs = null;
        
        try 
        {
            conn = DBMgr.getConnection();
            String sql = "SELECT * FROM `sa_group10`.`tblorder` WHERE `tblorder`.`MemberId` = ?";
            pres = conn.prepareStatement(sql);
            pres.setString(1,get_member_id);
            /** 執行查詢之SQL指令並記錄其回傳之資料 */
            rs = pres.executeQuery();

            /** 紀錄真實執行的SQL指令，並印出 **/
            exexcute_sql = pres.toString();
            System.out.println(exexcute_sql);
            
            /** 透過 while 迴圈移動pointer，取得每一筆回傳資料 */
            while(rs.next()) {
                /** 每執行一次迴圈表示有一筆資料 */
                row += 1;
                
                /** 將 ResultSet 之資料取出 */
                int id = rs.getInt("idtblOrder");
                int member_id = rs.getInt("MemberId");
                int total_price = rs.getInt("TotalPrice");
                int status = rs.getInt("OrderStatus");
                String paymethod = rs.getString("PaymentMethod");
                String creditcard_number = rs.getString("CreditCardNum");
                String address = rs.getString("Address");
                Timestamp create = rs.getTimestamp("OrderDateTime");
                
                /** 將每一筆商品資料產生一名新Product物件 */
                o = new Order(id,member_id,total_price,status,paymethod,creditcard_number,address,create);
                /** 取出該項商品之資料並封裝至 JSONsonArray 內 */
                jsa.put(o.getOrderAllInfo());
            }

        } catch (SQLException e) {
            /** 印出JDBC SQL指令錯誤 **/
            System.err.format("SQL State: %s\n%s\n%s", e.getErrorCode(), e.getSQLState(), e.getMessage());
        } catch (Exception e) {
            /** 若錯誤則印出錯誤訊息 */
            e.printStackTrace();
        } finally {
            /** 關閉連線並釋放所有資料庫相關之資源 **/
            DBMgr.close(rs, pres, conn);
        }
        
        /** 紀錄程式結束執行時間 */
        long end_time = System.nanoTime();
        /** 紀錄程式執行時間 */
        long duration = (end_time - start_time);
        
        /** 將SQL指令、花費時間、影響行數與所有會員資料之JSONArray，封裝成JSONObject回傳 */
        
        JSONObject response = new JSONObject();
        response.put("sql", exexcute_sql);
        response.put("row", row);
        response.put("time", duration);
        response.put("data", jsa);

        return response;
    }
    
    public JSONObject update(Order m) {
        /** 紀錄回傳之資料 */
        JSONArray jsa = new JSONArray();
        /** 記錄實際執行之SQL指令 */
        String exexcute_sql = "";
        /** 紀錄程式開始執行時間 */
        long start_time = System.nanoTime();
        /** 紀錄SQL總行數 */
        int row = 0;
        
        try {
            /** 取得資料庫之連線 */
            conn = DBMgr.getConnection();
            /** SQL指令 */
            String sql = "Update `sa_group10`.`tblorder` SET `OrderStatus` = ? WHERE `idtblOrder` = ?";
            /** 取得所需之參數 */
            int id = m.getId();
            int status=m.getStatus();
            
            /** 將參數回填至SQL指令當中 */
            pres = conn.prepareStatement(sql);
            pres.setInt(1,status);
            pres.setInt(2,id);
 
            /** 執行更新之SQL指令並記錄影響之行數 */
            row = pres.executeUpdate();

            /** 紀錄真實執行的SQL指令，並印出 **/
            exexcute_sql = pres.toString();
            System.out.println(exexcute_sql);

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
        
        /** 紀錄程式結束執行時間 */
        long end_time = System.nanoTime();
        /** 紀錄程式執行時間 */
        long duration = (end_time - start_time);
        
        /** 將SQL指令、花費時間與影響行數，封裝成JSONObject回傳 */
        JSONObject response = new JSONObject();
        response.put("sql", exexcute_sql);
        response.put("row", row);
        response.put("time", duration);
        response.put("data", jsa);

        return response;
    }

	public JSONObject delete(Order order) {
		
		JSONArray jsa = new JSONArray();
        /** 記錄實際執行之SQL指令 */
        String exexcute_sql = "";
        /** 紀錄程式開始執行時間 */
        long start_time = System.nanoTime();
        /** 紀錄SQL總行數 */
        int row = 0;
        
        try {
            /** 取得資料庫之連線 */
            conn = DBMgr.getConnection();
            /** SQL指令 */
            String sql = "Delete From `sa_group10`.`tblorderdetail`  WHERE `OrderId` = ?";
            /** 取得所需之參數 */
            int id = order.getId();
            
            /** 將參數回填至SQL指令當中 */
            pres = conn.prepareStatement(sql);
            pres.setInt(1,id);
 
            /** 執行更新之SQL指令並記錄影響之行數 */
            row = pres.executeUpdate();

            /** 紀錄真實執行的SQL指令，並印出 **/
            exexcute_sql = pres.toString();
            System.out.println(exexcute_sql);
            
            sql = "Delete From `sa_group10`.`tblorder`  WHERE `idtblOrder` = ?";
            pres = conn.prepareStatement(sql);
            pres.setInt(1,id);
            row = pres.executeUpdate();

            /** 紀錄真實執行的SQL指令，並印出 **/
            exexcute_sql = pres.toString();
            System.out.println(exexcute_sql);

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
        
        /** 紀錄程式結束執行時間 */
        long end_time = System.nanoTime();
        /** 紀錄程式執行時間 */
        long duration = (end_time - start_time);
        
        /** 將SQL指令、花費時間與影響行數，封裝成JSONObject回傳 */
        JSONObject response = new JSONObject();
        response.put("sql", exexcute_sql);
        response.put("row", row);
        response.put("time", duration);
        response.put("data", jsa);

        return response;
	}
}
