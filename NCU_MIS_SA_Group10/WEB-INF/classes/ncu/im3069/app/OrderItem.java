package ncu.im3069.app;

import org.json.JSONObject;
import ncu.im3069.util.Arith;

public class OrderItem {

    /** id，產品細項編號 */
    private int id;

    /** pd，產品 */
    private Product pd;
    private int product_id;
    private String product_name;
    /** quantity，產品數量 */
    private int quantity;

    /** price，產品價格 */
    private int price;

    /** subtotal，產品小計 */
    private String ice;
    private String sugar;
    private String size;

    /** ph，ProductHelper 之物件與 OrderItem 相關之資料庫方法（Sigleton） */
    private ProductHelper ph =  ProductHelper.getHelper();

    /**
     * 實例化（Instantiates）一個新的（new）OrderItem 物件<br>
     * 採用多載（overload）方法進行，此建構子用於建立訂單細項時
     *
     * @param pd 會員電子信箱
     * @param quantity 會員密碼
     */
    public OrderItem(int product_id,String product_name, int quantity,int price,String ice,String sugar,String size) {
        this.product_id = product_id;
        this.product_name=product_name;
        this.quantity = quantity;
        this.price = price;
        this.sugar = sugar;
        this.ice=ice;
        this.size=size;
        }
    public OrderItem(int order_product_id, int order_id, int product_id,int price, int quantity,String ice,String sugar,String size) {
        this.id = order_product_id;
        this.quantity = quantity;
        this.price = price;
        this.sugar = sugar;
        this.ice=ice;
        this.size=size;
        getProductFromDB(product_id);
        
        this.product_id=pd.getID();
        this.product_name=pd.getName();
    }

 

    /**
     * 從 DB 中取得產品
     */
    private void getProductFromDB(int product_id) {
        String id = String.valueOf(product_id);
        this.pd = ph.getById(id);
    }
    
    private void getProductFromDB(String product_name) {
        String name = String.valueOf(product_name);
        this.pd = ph.getByName(name);
    }

    /**
     * 取得產品
     *
     * @return Product 回傳產品
     */
    public int getProductId() {
        return this.product_id;
    }
    
    public String getProductName() {
        return this.product_name;
    }
    /**
     * 設定訂單細項編號
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * 取得訂單細項編號
     *
     * @return int 回傳訂單細項編號
     */
    public int getId() {
        return this.id;
    }

    /**
     * 取得產品價格
     *
     * @return double 回傳產品價格
     */
    public int getPrice() {
        return this.price;
    }
    
    public String getSize() {
		return size;
	}

	public String getSugar() {
		return sugar;
	}

	public String getIce() {
		return ice;
	}
	
    public int getQuantity() {
        return this.quantity;
    }

    /**
     * 取得產品細項資料
     *
     * @return JSONObject 回傳產品細項資料
     */
    public JSONObject getData() {
        JSONObject data = new JSONObject();
        data.put("id", getId());
        data.put("product_id",getProductId());
        data.put("product_name",getProductName());
        data.put("quantity", getQuantity());
        data.put("ice",getIce());
        data.put("sugar", getSugar());
        data.put("price", getPrice());
        data.put("size",getSize());

        return data;
    }
}
