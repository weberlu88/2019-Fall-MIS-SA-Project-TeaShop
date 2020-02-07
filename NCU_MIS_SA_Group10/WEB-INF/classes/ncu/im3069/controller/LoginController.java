package ncu.im3069.controller;

import java.io.*;
import javax.servlet.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import org.json.*;
import ncu.im3069.app.Member;
import ncu.im3069.app.MemberHelper;
import ncu.im3069.app.OrderHelper;
import ncu.im3069.app.Administrant;
import ncu.im3069.app.AdministrantHelper;
import ncu.im3069.tools.JsonReader;

// TODO: Auto-generated Javadoc
/**
 * <p>
 * The Class LoginController<br>
 * LoginController類別（class）主要用於處理Member和管理員login相關之Http請求（Request），繼承HttpServlet
 * </p>
 * 
 * @author IPLab
 * @version 1.0.0
 * @since 1.0.0
 */


public class LoginController extends HttpServlet {
    
    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 1L;
    
    /** mh，MemberHelper之物件與Member相關之資料庫方法（Sigleton） */
    private MemberHelper mh =  MemberHelper.getHelper();
    private AdministrantHelper ah =  AdministrantHelper.getHelper();
    private OrderHelper oh =  OrderHelper.getHelper();
    /**
     * 處理Http Method請求GET方法
     * 
     *
     * @param request Servlet請求之HttpServletRequest之Request物件（前端到後端）
     * @param response Servlet回傳之HttpServletResponse之Response物件（後端到前端）
     * @throws ServletException the servlet exception
     * @throws IOException Signals that an I/O exception has occurred.
     */
//    public void doGet(HttpServletRequest request, HttpServletResponse response)
//        throws ServletException, IOException {
//        
//    }
    
    /**
     * 處理Http Method請求POST方法（會員登入/管理員登入）因為回應not idempotent所以doPost
     * 
     * #) member或admin要登入 > 用email從DB撈會員資料 >  比對帳號密碼 > 回傳true false
     * request格式 {identity: "member"/"administrant/register", email, password}
     * response格式 {success: "true"/"false"}
     *
     * @param request Servlet請求之HttpServletRequest之Request物件（前端到後端）
     * @param response Servlet回傳之HttpServletResponse之Response物件（後端到前端）
     * @throws ServletException the servlet exception
     * @throws IOException Signals that an I/O exception has occurred.
     */
    public void doPost(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException {
    	
    	/** 透過JsonReader類別將Request之JSON格式資料解析並取回 */
        JsonReader jsr = new JsonReader(request);
        JSONObject jso = jsr.getObject();
        /** 若直接透過前端AJAX之data以key=value之字串方式進行傳遞參數，可以直接由此方法取回資料 */
        String identity = jso.getString("identity");
        String email = jso.getString("email");
        String password = jso.getString("password");
        String name = "", phone = "";
        if( jso.has("name") ) {
        	name = jso.getString("name");
        }
        if( jso.has("phone") ) {
        	phone = jso.getString("phone");
        }
        
        // 後端檢查空值，若有則回傳錯誤訊息
        if(email.isEmpty() || password.isEmpty() || identity.isEmpty()) {
			/** 以字串組出JSON格式之資料 */
			String resp = "{\"status\": \'400\', \"message\": \'欄位不能有空值\', \'response\': \'\'}";
			/** 透過JsonReader物件回傳到前端（以字串方式） */
			jsr.response(resp, response);
			return;
        }
        
        /** 新建一個JSONObject用於將回傳之資料進行封裝 */
        JSONObject resp = new JSONObject();
        JSONObject query = new JSONObject();
        
        // 判斷會員或管理員要求登入 > check email exist > helper get個人資訊 > check password
        switch (identity) {
	        case "member": { // create further nested scopes with braces for each case
	        	
	        	// 儲存email password的物件
	        	Member m = new Member(email, password, name, phone);
	        	
	        	// Step1:檢查email是否存在
	        	if ( mh.checkDuplicate(m) == false ) {
	        		// email不存在
	        		System.out.println("無此帳號!");
	        		/** 後端要回傳error response要一點心力，姑且先放在success()處理error
	        		 * https://stackoverflow.com/questions/20686465/override-the-http-response-status-text */
//	        		response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Email does not exist."); //醬才是error，可是後面的msg是放在html檔裡面
	        		String resp_error = "{\"status\": \'400\', \"message\": \"Email doesn't exist.\", \"response\": \"email\"}";
	        		jsr.response(resp_error, response);
	        		return;
	        	}
	        	
	        	// Step2:檢查密碼
	        	/** 透過MemberHelper物件之getByEmail()方法取回所有會員之資料，回傳之資料為Member物件 */
	        	Member m_correct = mh.getByEmail(email); //按F3可以跳至宣告 MemberController的部分
	        	
	        	if ( m_correct.isDeleted() ) {
	        		// 會員已被刪除
	        		System.out.println("會員已被刪除!");
	        		String resp_error = "{\"status\": \'400\', \"message\": \'This email is abandoned.\', \'response\': \'email\'}";
	        		jsr.response(resp_error, response);
	        		return;
	        	}
	        	else if ( m_correct.getPassword().equals(m.getPassword()) ) {
	        		// 密碼正確 登入成功      
	        		String id = Integer.toString( m_correct.getId() );
	        		System.out.println("會員登入成功!");
	        		query.put("data",m_correct.getData()); // 回傳member的會員資訊
	        		query.put("order",oh.getByMemberId(id)); // 回傳member的訂單資訊
	        		resp.put("status", "200");
	                resp.put("message", "會員登入成功");
	                resp.put("response", query);
	        	}
	        	else {
	        		// 密碼錯誤
	            	System.out.println("密碼錯誤!");
	        		String resp_error = "{\"status\": \'400\', \"message\": \'Wrong password, please try again.\', \'response\': \'password\'}";
	        		jsr.response(resp_error, response);
	        		return;
	        	}
	        	
	        	/** 透過JsonReader物件回傳到前端（以JSONObject方式） */
	            jsr.response(resp, response); //http response 只有登入成功才用這行回傳
	        	break;
	        	
//	        	/** 透過MemberHelper物件之getByEmail()方法取回所有會員之資料，回傳之資料為Member物件 */
//	            Member m = mh.getByEmail(email); //按F3可以跳至宣告 MemberController的部分
//	            String id=Integer.toString(m.getId());
//	            
//	            if( m != null && m.getPassword().equals(password) ) { // .equals() method for content comparison
//	            	// 密碼正確 登入成功            	
//	            	System.out.println("會員登入成功!");
//	                query.put("success", "true");
//	                query.put("order",oh.getByMemberId(id)); //把會員的資料放進x.response.order
//	                resp.put("status", "200");
//	                resp.put("data",m.getData());
//	                resp.put("message", "會員登入成功");
//	                resp.put("response", query);
//	
//	            } else if( m == null ) {
//	            	// 無此email
//	            	System.out.println("No such email!");
//	                query.put("success", "false");
//	                resp.put("status", "400");
//	                resp.put("message", "找不到此信箱");
//	                resp.put("response", query);
//	            }
//	            else {
//	            	// 密碼錯誤
//	            	System.out.println("Worng pwd!");
//	                query.put("success", "false");
//	                resp.put("status", "400");
//	                resp.put("message", "密碼錯誤");
//	                resp.put("response", query);
//	            }
//	            
//	            /** 透過JsonReader物件回傳到前端（以JSONObject方式） */
//	            jsr.response(resp, response); //http response
//	        	break;
	        }
	        case "administrant": {
	        	
	        	/** 透過MemberHelper物件之getByEmail()方法取回所有會員之資料，回傳之資料為Member物件 */
	        	Administrant a = ah.getByEmail(email); //按F3可以跳至宣告 MemberController的部分
	        	System.out.println("Login Controller print admin: "+a);
	            
	            if( a != null && a.getPassword().equals(password) ) { // .equals() method for content comparison
	            	// 密碼正確 登入成功            	
	                query.put("success", "true");
	                query.put("data", a.getData());
	                resp.put("status", "200");
	                resp.put("message", "管理者登入成功");
	                resp.put("response", query);
	
	            } else if( a == null ) {
	            	// 無此email
	                query.put("success", "false");
	                resp.put("status", "400");
	                resp.put("message", "找不到此信箱");
	                resp.put("response", query);
	            }
	            else {
	            	// 密碼錯誤
	                query.put("success", "false");
	                resp.put("status", "400");
	                resp.put("message", "密碼錯誤");
	                resp.put("response", query);
	            }
	            
	            /** 透過JsonReader物件回傳到前端（以JSONObject方式） */
	            jsr.response(resp, response); //http response
	        	break;
	        }
	        case "register": {
	        	
	        	Member m = new Member(email, password, name, phone);
	        	
	        	// 後端檢查name phone空值，若有則回傳錯誤訊息，其他3種資料switch前已檢查過
	        	if( name.isEmpty() || phone.isEmpty() ) {
	        		/** 以字串組出JSON格式之資料 */
	    			String resp_error = "{\"status\": \'400\', \"message\": \'欄位不能有空值\', \'response\': \'\'}";
	    			/** 透過JsonReader物件回傳到前端（以字串方式） */
	    			jsr.response(resp_error, response);
	    			break;
	        	} 
	        	/** 透過MemberHelper物件的checkDuplicate()檢查該會員電子郵件信箱是否有重複 */
	            else if (!mh.checkDuplicate(m)) {
	            	 // 如果沒有重複則呼叫mh新增會員
	                /** 透過MemberHelper物件的create()方法新建一個會員至資料庫 */
	                JSONObject data = mh.create(m);
	                
	                /** 新建一個JSONObject用於將回傳之資料進行封裝，data沒有會員資料喔*/
	                resp = new JSONObject();
	                resp.put("status", "200");
	                resp.put("message", "成功! 註冊會員資料...");
	                resp.put("response", data);
	                
	                /** 透過JsonReader物件回傳到前端（以JSONObject方式） */
	                jsr.response(resp, response);
	            }
	            else {
	                /** 以字串組出JSON格式之資料 */
	                String resp_error = "{\"status\": \'400\', \"message\": \'新增帳號失敗，此E-Mail帳號重複！\', \'response\': \'\'}";
	                /** 透過JsonReader物件回傳到前端（以字串方式） */
	                jsr.response(resp_error, response);
	            }
	        	break;
	        } // end switch-case
        }
    }
    
    protected void doGET(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        /** 透過 JsonReader 類別將 Request 之 JSON 格式資料解析並取回 */
        JsonReader jsr = new JsonReader(request);

        /** 取出經解析到 JsonReader 之 Request 參數 */
        String id = jsr.getParameter("id");

        /** 新建一個 JSONObject 用於將回傳之資料進行封裝 */
        JSONObject resp = new JSONObject();

        /** 判斷該字串是否存在，若存在代表要取回個別訂單之資料，否則代表要取回全部資料庫內訂單之資料 */
        if (!id.isEmpty()) {
          /** 透過 orderHelper 物件的 getByID() 方法自資料庫取回該筆訂單之資料，回傳之資料為 JSONObject 物件 */
          JSONObject query = oh.getByMemberId(id);
          resp.put("status", "200");
          resp.put("message", "單筆訂單資料取得成功");
          resp.put("response", query);
        }
        else {
          /** 透過 orderHelper 物件之 getAll() 方法取回所有訂單之資料，回傳之資料為 JSONObject 物件 */
          JSONObject query = oh.getAll();
          resp.put("status", "200");
          resp.put("message", "所有訂單資料取得成功");
          resp.put("response", query);
        }

        /** 透過 JsonReader 物件回傳到前端（以 JSONObject 方式） */
        jsr.response(resp, response);
	}


    /**
     * 處理Http Method請求DELETE方法（刪除），request需要有"id"的key
     *
     * @param request Servlet請求之HttpServletRequest之Request物件（前端到後端）
     * @param response Servlet回傳之HttpServletResponse之Response物件（後端到前端）
     * @throws ServletException the servlet exception
     * @throws IOException Signals that an I/O exception has occurred.
     */
//    public void doDelete(HttpServletRequest request, HttpServletResponse response)
//        throws ServletException, IOException {
//        /** 透過JsonReader類別將Request之JSON格式資料解析並取回 */
//        JsonReader jsr = new JsonReader(request);
//        JSONObject jso = jsr.getObject();
//        
//        /** 取出經解析到JSONObject之Request參數 */
//        int id = jso.getInt("id");
//        
//        /** 透過MemberHelper物件的deleteByID()方法至資料庫刪除該名會員，回傳之資料為JSONObject物件 */
//        JSONObject query = mh.deleteByID(id);
//        
//        /** 新建一個JSONObject用於將回傳之資料進行封裝 */
//        JSONObject resp = new JSONObject();
//        resp.put("status", "200");
//        resp.put("message", "會員移除成功！");
//        resp.put("response", query);
//
//        /** 透過JsonReader物件回傳到前端（以JSONObject方式） */
//        jsr.response(resp, response);
//    }

    /**
     * 處理Http Method請求PUT方法（更新）
     *
     * @param request Servlet請求之HttpServletRequest之Request物件（前端到後端）
     * @param response Servlet回傳之HttpServletResponse之Response物件（後端到前端）
     * @throws ServletException the servlet exception
     * @throws IOException Signals that an I/O exception has occurred.
     */
//    public void doPut(HttpServletRequest request, HttpServletResponse response)
//        throws ServletException, IOException {
//        /** 透過JsonReader類別將Request之JSON格式資料解析並取回 */
//        JsonReader jsr = new JsonReader(request);
//        JSONObject jso = jsr.getObject();
//        
//        /** 取出經解析到JSONObject之Request參數 */
//        int id = jso.getInt("id");
//        String email = jso.getString("email");
//        String password = jso.getString("password");
//        String name = jso.getString("name");
//
//        /** 透過傳入之參數，新建一個以這些參數之會員Member物件 */
//        Member m = new Member(id, email, password, name);
//        
//        /** 透過Member物件的update()方法至資料庫更新該名會員資料，回傳之資料為JSONObject物件 */
//        JSONObject data = m.update();
//        
//        /** 新建一個JSONObject用於將回傳之資料進行封裝 */
//        JSONObject resp = new JSONObject();
//        resp.put("status", "200");
//        resp.put("message", "成功! 更新會員資料...");
//        resp.put("response", data);
//        
//        /** 透過JsonReader物件回傳到前端（以JSONObject方式） */
//        jsr.response(resp, response);
//    }
}