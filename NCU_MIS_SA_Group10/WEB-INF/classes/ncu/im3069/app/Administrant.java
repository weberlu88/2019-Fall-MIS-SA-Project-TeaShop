package ncu.im3069.app;

import org.json.JSONObject;

public class Administrant {
	
	// idtblAdministrant, Email, Password, FullName
	/* 管理員資訊 4欄 */
	private int id;
	private String email;
	private String password;
	private String name;
	private AdministrantHelper ah =  AdministrantHelper.getHelper();
	
	/**
     * Administrant物件<br>
     * 實例化（Instantiates）一個新的（new）Administrant物件<br>
     * 此建構子用於查詢管理員資料時，將每一筆資料新增為一個管理員物件
     *
     * @param id 管理員編號
     * @param email 管理員電子信箱
     * @param password 管理員密碼
     * @param name 會員管理員姓名
     */
	public Administrant(int id, String email, String password, String name) {
		super();
		this.id = id;
		this.email = email;
		this.password = password;
		this.name = name;
	}
	
	/**
     * 取得該名管理員所有資料
     *
     * @return the data 取得該名管理員之所有資料並封裝於JSONObject物件內<br>
     * 格式 {id:int, name:str, email:str, password:str}
     */
    public JSONObject getData() {
        /** 透過JSONObject將該名會員所需之資料全部進行封裝*/ 
        JSONObject jso = new JSONObject();
        jso.put("id", getId());
        jso.put("name", getName());
        jso.put("email", getEmail());
        jso.put("password", getPassword());
        
        return jso;
    }
	
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

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}
