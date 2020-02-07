package ncu.im3069.app;

import java.sql.Timestamp;
import org.json.*;

//TODO: Auto-generated Javadoc
/**
* <p>
* The Class Member ~飲料系統的會員~
* Member類別（class）具有會員所需要之屬性與方法，並且儲存與會員相關之商業判斷邏輯<br>
* </p>
* 
* @author Weber Lu
* @version 1.0.0
* @since 1.0.0
*/

public class Member {
	
	/* 會員資訊 6欄 */
	private int id;
	private String email;
	private String password;
	private String name;
	private String phone;
	private Timestamp createDateTime;
	private boolean isDeleted;
	private MemberHelper mh =  MemberHelper.getHelper();
	
    /**
     * 最完整的Member物件
     * 實例化（Instantiates）一個新的（new）Member物件<br>
     * 採用多載（overload）方法進行，此建構子用於查詢會員資料時，將每一筆資料新增為一個會員物件
     *
     * @param id 會員編號
     * @param email 會員電子信箱
     * @param password 會員密碼
     * @param name 會員姓名
     * @param phone 會員電話
     * @param createDateTime 會員註冊時間
     */
	public Member(int id, String email, String password, String name, String phone, Timestamp createDateTime, Boolean isDeleted ) {
		this.id = id;
		this.email = email;
		this.password = password;
		this.name = name;
		this.phone = phone;
		this.createDateTime = createDateTime;
		this.isDeleted = isDeleted;
	}
	
	/**
     * 沒有isDelete的Member物件
     * 實例化（Instantiates）一個新的（new）Member物件<br>
     * 採用多載（overload）方法進行，此建構子用於查詢會員資料時，將每一筆資料新增為一個會員物件
     *
     * @param id 會員編號
     * @param email 會員電子信箱
     * @param password 會員密碼
     * @param name 會員姓名
     * @param phone 會員電話
     * @param createDateTime 會員註冊時間
     */
	public Member(int id, String email, String password, String name, String phone, Timestamp createDateTime ) {
		this.id = id;
		this.email = email;
		this.password = password;
		this.name = name;
		this.phone = phone;
		this.createDateTime = createDateTime;
	}
	
    /**
     * 新會員 > 少了註冊時間&id&isDelete的Member物件
     *
     * @param email 會員電子信箱
     * @param password 會員密碼
     * @param name 會員姓名
     * @param phone 會員電話
     */
	public Member( String email, String password, String name, String phone ) {
		this.email = email;
		this.password = password;
		this.name = name;
		this.phone = phone;
	}
	
    /**
     * 取得該名會員所有資料
     *
     * @return the data 取得該名會員之所有資料並封裝於JSONObject物件內<br>
     * 格式 {id:int, name:str, phone:str, email:str, password:str, create:timestamp}
     */
    public JSONObject getData() {
        /** 透過JSONObject將該名會員所需之資料全部進行封裝*/ 
        JSONObject jso = new JSONObject();
        jso.put("id", getId());
        jso.put("name", getName());
        jso.put("phone", getPhone());
        jso.put("email", getEmail());
        jso.put("password", getPassword());
        jso.put("create", getCreateDateTime());
        jso.put("isDeleted", isDeleted());
        
        return jso;
    }
	
	/* All get set method (auto generated) */
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Timestamp getCreateDateTime() {
		return createDateTime;
	}
	public void setCreateDateTime(Timestamp createDateTime) {
		this.createDateTime = createDateTime;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}

	public boolean isDeleted() {
		return isDeleted;
	}

	public void setDeleted(boolean isDelete) {
		this.isDeleted = isDelete;
	}

}
