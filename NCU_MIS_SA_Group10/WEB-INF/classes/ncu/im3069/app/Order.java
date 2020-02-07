package ncu.im3069.app;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.*;

import org.json.*;

public class Order 
{
    private int id;
    private int member_id;
    private int order_status;
    private int total_price;
    private String address;
    private String paymethod;
    private String creditcard;
    private Timestamp create;
    
    // list，訂單列表
    private ArrayList<OrderItem> list = new ArrayList<OrderItem>();

    // 三種Helper物件呼叫
    private OrderItemHelper oph = OrderItemHelper.getHelper();
    private MemberHelper mh =  MemberHelper.getHelper();
    private OrderHelper oh = OrderHelper.getHelper();
    
    //overload多載->新增訂單、修改訂單(狀態改變及刪除)、查看訂單
    public Order(int member_id,int total_price, String paymethod, String creditcard, String address) 
    {
        this.member_id = member_id;
        this.total_price = total_price;
        this.creditcard=creditcard;
        this.paymethod = paymethod;
        this.address = address;
        this.order_status =0;
        this.create = Timestamp.valueOf(LocalDateTime.now());
    }
    
    public Order(int id,int status) 
    {
    	this.id=id;
        this.order_status =status;
    }
    
    public Order(int id) 
    {
    	this.id=id;
    }

    public Order(int order_id,int member_id,int total_price,int order_status, String paymethod, String creditcard, String address ,Timestamp create) {
        this.id=order_id;
    	this.member_id = member_id;
        this.total_price = total_price;
        this.creditcard=creditcard;
        this.paymethod = paymethod;
        this.address = address;
        this.create = Timestamp.valueOf(LocalDateTime.now());
        this.order_status=order_status;
        getOrderProductFromDB();
    }

    //新增一個訂單產品及其數量 
    public void addOrderProduct(int product_id,String name, int quantity,int price,String ice,String sugar,String size) 
    {
        this.list.add(new OrderItem(product_id,name, quantity,price,ice,sugar,size));
    }

    //設定物件數值
    public void setId(int id) 
    {
        this.id = id;
    }

    public int getId() 
    {
        return this.id;
    }

    public int getMemberId() 
    {
        return this.member_id;
    }

    public int getTotalPrice() 
    {
        return this.total_price;
    }

    public String getPaymethod() 
    {
        return this.paymethod;
    }

    public Timestamp getCreateTime() 
    {
        return this.create;
    }

    public String getCreditCard() 
    {
        return this.creditcard;
    }

    public String getAddress() 
    {
        return this.address;
    }

    public int getStatus() 
    {
        return this.order_status;
    }
    
    //更新訂單時要用的方法->先檢查有無重複id->若有則更新，無則回報
    public JSONObject update() 
    {
        JSONObject data = new JSONObject();
        
        this.order_status=1;//此為更新訂單狀態，因只會更新訂單狀態從進行中至已完成，所以直接設定為1
        
        if(this.id != 0) 
        {       
            data = oh.update(this);
        }
        return data;
    }
    
    //取得購物車產品
    public ArrayList<OrderItem> getOrderProduct() 
    {
        return this.list;
    }

   //從資料庫撈出有關此訂單的產品
    private void getOrderProductFromDB() 
    {
        ArrayList<OrderItem> data = oph.getOrderProductByOrderId(this.id);
        this.list = data;
    }

    //取得訂單資料
    public JSONObject getOrderData() 
    {
        JSONObject jso = new JSONObject();
        jso.put("id", getId());
        jso.put("member_id", getMemberId());
        jso.put("total_price", getTotalPrice());
        jso.put("order_status", getStatus());
        jso.put("payment_method", getPaymethod());
        jso.put("creditcard", getCreditCard());
        jso.put("address", getAddress());
        jso.put("create", getCreateTime());
        return jso;
    }

    //撈出傳回來的購物車產品資訊
    public JSONArray getOrderProductData() 
    {
        JSONArray result = new JSONArray();
        
        for(int i=0 ; i < this.list.size() ; i++) 
        {
            result.put(this.list.get(i).getData());
            
        }
        return result;
    }
    
    //取得訂購者的member資料->以id搜尋
    public JSONObject getOrderMemberData() 
    {
    	JSONObject jso = new JSONObject();
    	String member_id_str=String.valueOf(member_id);
    	Member m=mh.getByIDforOrder(member_id_str);
    	jso.put("id",m.getId());
    	jso.put("name",m.getName());
    	jso.put("phone",m.getPhone());
    	
       return jso;
    }

    //查詢訂單時，將所有有關訂單的資訊包裝好並傳出
    public JSONObject getOrderAllInfo() 
    {
        JSONObject jso = new JSONObject();
        jso.put("order_info", getOrderData());
        jso.put("product_info", getOrderProductData());
        jso.put("member_info", getOrderMemberData());
        return jso;
    }

   //幫助設置訂單產品的編號(PS但我們用auto_increment其實沒用ㄏㄏ)
    public void setOrderProductId(JSONArray data) {
        for(int i=0 ; i < this.list.size() ; i++) {
            this.list.get(i).setId((int) data.getLong(i));
        }
    }
    
    //刪除訂單用ㄉ方法與修改訂單類似
	public JSONObject delete() 
	{
		JSONObject data = new JSONObject();

        if(this.id != 0) 
        {
            data = oh.delete(this);
        }
        return data;
	}

}
