package ncu.im3069.app;

import java.sql.*;
import java.time.LocalDateTime;
import org.json.*;
import ncu.im3069.app.Administrant;
import ncu.im3069.util.DBMgr;

public class AdministrantHelper {
	
	/**
     * 實例化（Instantiates）一個新的（new）AdministrantHelper物件<br>
     * 採用Singleton不需要透過new，因為是static變數，只有一個存在系統中。
     */
    private AdministrantHelper() {
        
    }
    
    /** 靜態變數，儲存MemberHelper物件 */
    private static AdministrantHelper ah;
    
    /** 儲存JDBC資料庫連線 */
    private Connection conn = null;
    
    /** 儲存JDBC預準備之SQL指令 */
    private PreparedStatement pres = null;
    
    /**
     * 靜態方法<br>
     * 實作Singleton（單例模式），僅允許建立一個AdministrantHelper物件
     *
     * @return the helper 回傳AdministrantHelper物件
     */
    public static AdministrantHelper getHelper() {
        /** Singleton檢查是否已經有AdministrantHelper物件，若無則new一個，若有則直接回傳 */
        if(ah == null) ah = new AdministrantHelper();
        
        return ah;
    }
    
    /**
     * 透過管理員編號（Email）取得管理員資料<br>
     * 格式 {id:int, name:str, phone:str, email:str, password:str, create:timestamp}
     *
     * @param id 管理員編號
     * @return the JSON object 回傳SQL執行結果與該管理員Email之管理員資料
     */
    public Administrant getByEmail(String in_email) {
        /** 新建一個 Member 物件之 m 變數，用於紀錄每一位查詢回之會員資料 */
    	Administrant a = null;
        /** 用於儲存所有檢索回之會員，以JSONArray方式儲存 */
        JSONArray jsa = new JSONArray();
        /** 記錄實際執行之SQL指令 */
        String exexcute_sql = "";
        /** 紀錄程式開始執行時間 */
        long start_time = System.nanoTime();
        /** 紀錄SQL總行數 */
        int row = 0;
        /** 儲存JDBC檢索資料庫後回傳之結果，以 pointer 方式移動到下一筆資料 */
        ResultSet rs = null;
        
        try {
            /** 取得資料庫之連線 */
            conn = DBMgr.getConnection();
            /** SQL指令 */
            String sql = "SELECT * FROM `sa_group10`.`tbladministrant` WHERE `Email` = ? LIMIT 1";
            
            /** 將參數回填至SQL指令當中 */ // 重要!!
            pres = conn.prepareStatement(sql);
            pres.setString(1, in_email);
            /** 執行查詢之SQL指令並記錄其回傳之資料 */
            rs = pres.executeQuery(); // Select > executeQuery

            /** 紀錄真實執行的SQL指令，並印出 **/
            exexcute_sql = pres.toString();
            System.out.println(exexcute_sql);
            
            /** 透過 while 迴圈移動pointer，取得每一筆回傳資料 */
            /** 正確來說資料庫只會有一筆該會員編號之資料，因此其實可以不用使用 while 迴圈 */
            while(rs.next()) {
                /** 每執行一次迴圈表示有一筆資料 */
                row += 1;
                
                if (rs.wasNull()) {
                    // handle NULL field value > 沒有這個email
                	a = null;
                } else {
                	 /** 將 ResultSet 之資料取出 */
                    int administrant_id = rs.getInt("idtbladministrant");
                    String name = rs.getString("FullName");
                    String email = rs.getString("Email");
                    String password = rs.getString("Password");
                    
                    /** 將每一筆會員資料產生一名新Member物件 > 回傳 */
                    a = new Administrant(administrant_id, email, password, name);
//                    /** 取出該名會員之資料並封裝至 JSONsonArray 內 */
//                    jsa.put(m.getData());
                }
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
        
        return a;
    }// end getByEmail()
    
    /**
     * 透過會員編號（ID）取得會員資料
     * 格式 {id:int, name:str, phone:str, email:str, password:str, create:timestamp}
     *
     * @param id 會員編號
     * @return the JSON object 回傳SQL執行結果與該會員編號之會員資料
     */
    public JSONObject getByID(String id) {
        /** 新建一個 Member 物件之 m 變數，用於紀錄每一位查詢回之會員資料 */
    	Administrant a = null;
        /** 用於儲存所有檢索回之會員，以JSONArray方式儲存 */
        JSONArray jsa = new JSONArray();
        /** 記錄實際執行之SQL指令 */
        String exexcute_sql = "";
        /** 紀錄程式開始執行時間 */
        long start_time = System.nanoTime();
        /** 紀錄SQL總行數 */
        int row = 0;
        /** 儲存JDBC檢索資料庫後回傳之結果，以 pointer 方式移動到下一筆資料 */
        ResultSet rs = null;
        
        try {
            /** 取得資料庫之連線 */
            conn = DBMgr.getConnection();
            /** SQL指令 */
            String sql = "SELECT * FROM `sa_group10`.`tbladministrant` WHERE `idtblAdministrant` = ? LIMIT 1";
            
            /** 將參數回填至SQL指令當中 */ // 重要!!
            pres = conn.prepareStatement(sql);
            pres.setString(1, id);
            /** 執行查詢之SQL指令並記錄其回傳之資料 */
            rs = pres.executeQuery(); // Select > executeQuery

            /** 紀錄真實執行的SQL指令，並印出 **/
            exexcute_sql = pres.toString();
            System.out.println(exexcute_sql);
            
            /** 透過 while 迴圈移動pointer，取得每一筆回傳資料 */
            /** 正確來說資料庫只會有一筆該會員編號之資料，因此其實可以不用使用 while 迴圈 */
            while(rs.next()) {
                /** 每執行一次迴圈表示有一筆資料 */
                row += 1;
                
                /** 將 ResultSet 之資料取出 */
                int administrant_id = rs.getInt("idtblAdministrant");
                String name = rs.getString("FullName");
                String email = rs.getString("Email");
                String password = rs.getString("Password");
                
                /** 將每一筆會員資料產生一名新Member物件 */
                a = new Administrant(administrant_id, email, password, name);
                /** 取出該名會員之資料並封裝至 JSONsonArray 內 */
                jsa.put(a.getData());
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
    }// end getByID()

}
