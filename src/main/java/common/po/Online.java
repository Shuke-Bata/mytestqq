package common.po;

import java.io.Serializable;

public class Online implements Serializable {

	private Integer eid;
	private String account;


	public Integer getEid(){
		return eid;
	}
	public String getAccount(){
		return account;
	}
	public void setEid(Integer eid){
		this.eid=eid;
	}
	public void setAccount(String account){
		this.account=account;
	}
}
