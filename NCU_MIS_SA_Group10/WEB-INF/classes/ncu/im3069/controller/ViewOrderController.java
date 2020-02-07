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

@WebServlet("/api/viewOrder.do")
public class ViewOrderController extends HttpServlet {
    
    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 1L;
    
    /** mh，MemberHelper之物件與Member相關之資料庫方法（Sigleton） */
    private MemberHelper mh =  MemberHelper.getHelper();
    private AdministrantHelper ah =  AdministrantHelper.getHelper();
    private OrderHelper oh =  OrderHelper.getHelper();
    
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
        String email = jso.getString("email");
        //String email = jsr.getParameter("email");
        
        // 後端檢查空值，若有則回傳錯誤訊息
        if( email.isEmpty() ) {
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
	        	
    	// 儲存email password的物件
    	Member m = new Member(email, "", "", "");
    	
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
    	
    	// Step2:回傳訂單資料
    	/** 透過MemberHelper物件之getByEmail()方法取回所有會員之資料，回傳之資料為Member物件 */
    	Member m_correct = mh.getByEmail(email); //按F3可以跳至宣告 MemberController的部分
    	
    	if ( m_correct.isDeleted() ) {
    		// 會員已被刪除
    		System.out.println("會員已被刪除!");
    		String resp_error = "{\"status\": \'400\', \"message\": \'This email is abandoned.\', \'response\': \'email\'}";
    		jsr.response(resp_error, response);
    		return;
    	}
    	else  {
    		// 會員存在   
    		String id = Integer.toString( m_correct.getId() );
    		System.out.println("會員登入成功!");
    		query.put("data",m_correct.getData()); // 回傳member的會員資訊
    		query.put("order",oh.getByMemberId(id)); // 回傳member的訂單資訊
    		resp.put("status", "200");
            resp.put("message", "會員登入成功");
            resp.put("response", query);
    	}
    	
    	/** 透過JsonReader物件回傳到前端（以JSONObject方式） */
        jsr.response(resp, response); //http response 只有登入成功才用這行回傳
    	
    }// end doPost()
    
    
    
}