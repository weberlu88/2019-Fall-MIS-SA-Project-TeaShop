package ncu.im3069.controller;

import java.io.IOException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.*;
import javax.servlet.http.*;
import org.json.*;

import ncu.im3069.app.Product;
import ncu.im3069.app.ProductHelper;
import ncu.im3069.tools.JsonReader;

@WebServlet("/api/product.do")
public class ProductController extends HttpServlet {
	private static final long serialVersionUID = 1L;

	private ProductHelper ph =  ProductHelper.getHelper();

    public ProductController() {
        super();
        // TODO Auto-generated constructor stub
    }


	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		/** 透過JsonReader類別將Request之JSON格式資料解析並取回 */
        JsonReader jsr = new JsonReader(request);
        /** 若直接透過前端AJAX之data以key=value之字串方式進行傳遞參數，可以直接由此方法取回資料 */
        String id_list = jsr.getParameter("id_list");

        JSONObject resp = new JSONObject();
        /** 判斷該字串是否存在，若存在代表要取回購物車內產品之資料，否則代表要取回全部資料庫內產品之資料 */
        if (!id_list.isEmpty()) {
          JSONObject query = ph.getByIdList(id_list);
          resp.put("status", "200");
          resp.put("message", "所有購物車之商品資料取得成功");
          resp.put("response", query);
        }
        else {
          JSONObject query = ph.getAll();

          resp.put("status", "200");
          resp.put("message", "所有商品資料取得成功");
          resp.put("response", query);
        }

        jsr.response(resp, response);
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response)
	        throws ServletException, IOException {
	        /** 透過JsonReader類別將Request之JSON格式資料解析並取回 */
	        JsonReader jsr = new JsonReader(request);
	        JSONObject jso = jsr.getObject();
	        
	        /** 取出經解析到JSONObject之Request參數 */
	        String name = jso.getString("drink_name");
	        String catagory = jso.getString("drink_catagory");
	        int middle_price=jso.getInt("middle_price");
	        int large_price=jso.getInt("large_price");
	        String describe = jso.getString("describe");
	        
	        /** 建立一個新的產品物件 */
	        Product m = new Product(catagory,name,large_price,middle_price,describe);
	        
	        /** 後端檢查是否有欄位為空值，若有則回傳錯誤訊息 */
	        if(catagory.isEmpty() || name.isEmpty() || describe.isEmpty()) {
	            /** 以字串組出JSON格式之資料 */
	            String resp = "{\"status\": \'400\', \"message\": \'欄位不能有空值\', \'response\': \'\'}";
	            /** 透過JsonReader物件回傳到前端（以字串方式） */
	            jsr.response(resp, response);
	        }
	        /** 透過ProductHelper物件的checkDuplicate()檢查該產品名稱是否有重複 */
	        else if (!ph.checkDuplicate(m)) {
	            /** 透過ProductHelper物件的create()方法新建一個產品至資料庫 */
	            JSONObject data = ph.create(m);
	            
	            /** 新建一個JSONObject用於將回傳之資料進行封裝 */
	            JSONObject resp = new JSONObject();
	            resp.put("status", "200");
	            resp.put("message", "成功! 註冊商品資料...");
	            resp.put("response", data);
	            
	            /** 透過JsonReader物件回傳到前端（以JSONObject方式） */
	            jsr.response(resp, response);
	        }
	        else {
	            /** 以字串組出JSON格式之資料 */
	            String resp = "{\"status\": \'400\', \"message\": \'新增帳號失敗，此E-Mail帳號重複！\', \'response\': \'\'}";
	            /** 透過JsonReader物件回傳到前端（以字串方式） */
	            jsr.response(resp, response);
	        }
	    }
	
	 public void doPut(HttpServletRequest request, HttpServletResponse response)
		        throws ServletException, IOException {
		        /** 透過JsonReader類別將Request之JSON格式資料解析並取回 */
		        JsonReader jsr = new JsonReader(request);
		        JSONObject jso = jsr.getObject();
		        
		        /** 取出經解析到JSONObject之Request參數 */
		        int id = jso.getInt("id");
		        String name = jso.getString("drink_name");
		        String catagory = jso.getString("drink_catagory");
		        int middle_price=jso.getInt("middle_price");
		        int large_price=jso.getInt("large_price");
		        String describe = jso.getString("describe");


		        /** 透過傳入之參數，新建一個以這些參數之產品Product物件 */
		        Product m = new Product(id,catagory,name,large_price,middle_price,describe);
		        
		        /** 透過Product物件的update()方法至資料庫更新該名產品資料，回傳之資料為JSONObject物件 */
		        JSONObject data = m.update();
		        
		        /** 新建一個JSONObject用於將回傳之資料進行封裝 */
		        JSONObject resp = new JSONObject();
		        resp.put("status", "200");
		        resp.put("message", "成功! 更新產品資料...");
		        resp.put("response", data);
		        
		        /** 透過JsonReader物件回傳到前端（以JSONObject方式） */
		        jsr.response(resp, response);
		    }

}
