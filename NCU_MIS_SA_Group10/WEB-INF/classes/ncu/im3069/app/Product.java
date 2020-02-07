package ncu.im3069.app;

import java.util.Calendar;

import org.json.*;

public class Product {

    /** id，產品編號 */
    private int id;
    
    /** id，產品種類 */
    private String catagory;

    /** id，產品名字 */
    private String name;

    /** id，產品大杯價格 */
    private int large_price;
    
    /** id，產品小杯價格*/
    private int small_price;

    /** id，產品描述 */
    private String describe;

    /** id，產品是否上架*/
	private int isshelf;
	
	private ProductHelper ph =  ProductHelper.getHelper();
	
    /**
     * 實例化（Instantiates）一個新的（new）Product 物件<br>
     * 採用多載（overload）方法進行，此建構子用於新增產品時
     *
     * @param id 產品編號
     */
	public Product(int id) {
		this.id = id;
	}

    /**
     * 實例化（Instantiates）一個新的（new）Product 物件<br>
     * 採用多載（overload）方法進行，此建構子用於新增產品時
     *
     * @param name 產品名稱
     * @param price 產品價格
     * @param image 產品圖片
     */
	public Product( String catagory,String name, int large_price, int small_price , String describe) {
		this.name = name;
		this.large_price = large_price;
		this.small_price = small_price;
		this.catagory = catagory;
		this.describe = describe;
		this.isshelf = 0;
	}

    /**
     * 實例化（Instantiates）一個新的（new）Product 物件<br>
     * 採用多載（overload）方法進行，此建構子用於修改產品時
     *
     * @param id 產品編號
     * @param name 產品名稱
     * @param price 產品價格
     * @param image 產品圖片
     * @param describe 產品敘述
     */
	public Product(int id,String catagory,String name,int  large_price,int small_price, String describe) {
		this.id = id;
		this.catagory=catagory;
		this.name = name;
		this.large_price = large_price;
		this.small_price=small_price;
		this.describe = describe;
		this.isshelf=0;
	}

    /**
     * 取得產品編號
     *
     * @return int 回傳產品編號
     */
	public int getID() {
		return this.id;
	}

    /**
     * 取得產品名稱
     *
     * @return String 回傳產品名稱
     */
	public String getName() {
		return this.name;
	}
	public String getCatagory() {
		return this.catagory;
	}

    /**
     * 取得產品價格
     *
     * @return double 回傳產品價格
     */
	public int getLargePrice() {
		return this.large_price;
	}
	public int getMiddlePrice() {
		return this.small_price;
	}

 
    /**
     * 取得產品敘述
     *
     * @return String 回傳產品敘述
     */
	public String getDescribe() {
		return this.describe;
	}

    /**
     * 取得產品資訊
     *
     * @return JSONObject 回傳產品資訊
     */
	public JSONObject getData() {
        /** 透過JSONObject將該項產品所需之資料全部進行封裝*/
        JSONObject jso = new JSONObject();
        jso.put("id", getID());
        jso.put("drink_catagory",getCatagory());
        jso.put("drink_name", getName());
        jso.put("large_price", getLargePrice());
        jso.put("middle_price", getMiddlePrice());
        jso.put("describe", getDescribe());

        return jso;
    }
	
	 public JSONObject update() {
	        /** 新建一個JSONObject用以儲存更新後之資料 */
	        JSONObject data = new JSONObject();
	        
	        /** 檢查該項產品是否已經在資料庫 */
	        if(this.id != 0) {
	            /** 若有則將目前更新後之資料更新至資料庫中 */
	            /** 透過ProductHelper物件，更新目前之產品資料置資料庫中 */
	            data = ph.update(this);
	        }
	        
	        return data;
	    }
}
