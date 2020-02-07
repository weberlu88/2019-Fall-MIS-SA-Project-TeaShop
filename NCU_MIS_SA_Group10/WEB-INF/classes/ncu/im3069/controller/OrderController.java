package ncu.im3069.controller;

import java.awt.List;
import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.*;
import javax.servlet.http.*;
import org.json.*;

import ncu.im3069.app.Order;
import ncu.im3069.app.Product;
import ncu.im3069.app.ProductHelper;
import ncu.im3069.app.OrderHelper;
import ncu.im3069.tools.JsonReader;

import javax.servlet.annotation.WebServlet;

@WebServlet("/api/order.do")
public class OrderController extends HttpServlet {

    /** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

    /** ph，ProductHelper 之物件與 Product 相關之資料庫方法（Sigleton） */
    private ProductHelper ph =  ProductHelper.getHelper();

    /** oh，OrderHelper 之物件與 order 相關之資料庫方法（Sigleton） */
	private OrderHelper oh =  OrderHelper.getHelper();

    public OrderController() {
        super();
    }

    /**
     * 處理 Http Method 請求 GET 方法（新增資料）
     *
     * @param request Servlet 請求之 HttpServletRequest 之 Request 物件（前端到後端）
     * @param response Servlet 回傳之 HttpServletResponse 之 Response 物件（後端到前端）
     * @throws ServletException the servlet exception
     * @throws IOException Signals that an I/O exception has occurred.
     */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        /** 透過 JsonReader 類別將 Request 之 JSON 格式資料解析並取回 */
        JsonReader jsr = new JsonReader(request);

        /** 取出經解析到 JsonReader 之 Request 參數 */
        String id = jsr.getParameter("id");

        /** 新建一個 JSONObject 用於將回傳之資料進行封裝 */
        JSONObject resp = new JSONObject();

        /** 判斷該字串是否存在，若存在代表要取回個別訂單之資料，否則代表要取回全部資料庫內訂單之資料 */
        if (!id.isEmpty()) {
          /** 透過 orderHelper 物件的 getByID() 方法自資料庫取回該筆訂單之資料，回傳之資料為 JSONObject 物件 */
          JSONObject query = oh.getById(id);
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
     * 處理 Http Method 請求 POST 方法（新增資料）
     *
     * @param request Servlet 請求之 HttpServletRequest 之 Request 物件（前端到後端）
     * @param response Servlet 回傳之 HttpServletResponse 之 Response 物件（後端到前端）
     * @throws ServletException the servlet exception
     * @throws IOException Signals that an I/O exception has occurred.
     */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	    /** 透過 JsonReader 類別將 Request 之 JSON 格式資料解析並取回 */
        JsonReader jsr = new JsonReader(request);
        JSONObject jso = jsr.getObject();

        /** 取出經解析到 JSONObject 之 Request 參數 */
        int member_id = jso.getInt("member_id");
        int total_price = jso.getInt("total_price");
        String paymethod = jso.getString("paymethod");
        String address = jso.getString("address");
        String creditcard_number = jso.getString("creditcard_number");
        JSONArray cart_data = jso.getJSONArray("cart_item");

        /** 建立一個新的訂單物件 */
        Order od = new Order(member_id,total_price,paymethod,creditcard_number,address);
        for (int i = 0; i < cart_data.length(); i++) {
        	JSONObject drink_detail = cart_data.getJSONObject(i);
        	
        	String drink_name = drink_detail.getString("drink_name");
        	String drink_size = drink_detail.getString("drink_size");
        	int drink_quantity= drink_detail.getInt("drink_quantity");
        	String drink_sugar = drink_detail.getString("drink_sugar");
        	String drink_ice = drink_detail.getString("drink_ice");
        	int drink_price=drink_detail.getInt("drink_price");
        	Product pd = ph.getByName(drink_name);
        	
        	od.addOrderProduct(pd.getID(),pd.getName(),drink_quantity,drink_price,drink_ice,drink_sugar,drink_size);
        	
        }
        /** 透過 orderHelper 物件的 create() 方法新建一筆訂單至資料庫 */
        JSONObject result = oh.create(od);

        /** 設定回傳回來的訂單編號與訂單細項編號 */
        od.setId((int) result.getLong("order_id"));
        od.setOrderProductId(result.getJSONArray("order_product_id"));

        /** 新建一個 JSONObject 用於將回傳之資料進行封裝 */
        JSONObject resp = new JSONObject();
        resp.put("status", "200");
        resp.put("message", "訂單新增成功！");
        resp.put("response", od.getOrderAllInfo());

        /** 透過 JsonReader 物件回傳到前端（以 JSONObject 方式） */
        jsr.response(resp, response);
	}
	
	 public void doPut(HttpServletRequest request, HttpServletResponse response)throws ServletException, IOException 
	 {
		        /** 透過JsonReader類別將Request之JSON格式資料解析並取回 */
		        JsonReader jsr = new JsonReader(request);
		        JSONObject jso = jsr.getObject();
		        
		        /** 取出經解析到JSONObject之Request參數 */
		        
		        int id = jso.getInt("id");
		        int status = jso.getInt("order_status");


		        /** 透過傳入之參數，新建一個以這些參數之產品Product物件 */
		        Order m = new Order(id,status);
		        
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
	 
	 
	 public void doDelete(HttpServletRequest request, HttpServletResponse response)throws ServletException, IOException 
	 {
		        /** 透過JsonReader類別將Request之JSON格式資料解析並取回 */
		        JsonReader jsr = new JsonReader(request);
		        JSONObject jso = jsr.getObject();
		        
		        /** 取出經解析到JSONObject之Request參數 */
		        
		        int id = jso.getInt("id");

		        /** 透過傳入之參數，新建一個以這些參數之產品Product物件 */
		        Order m = new Order(id);
		        
		        /** 透過Product物件的update()方法至資料庫更新該名產品資料，回傳之資料為JSONObject物件 */
		        JSONObject data = m.delete();
		        
		        /** 新建一個JSONObject用於將回傳之資料進行封裝 */
		        JSONObject resp = new JSONObject();
		        resp.put("status", "200");
		        resp.put("message", "成功! 更新產品資料...");
		        resp.put("response", data);
		        
		        /** 透過JsonReader物件回傳到前端（以JSONObject方式） */
		        jsr.response(resp, response);
		    }

}
