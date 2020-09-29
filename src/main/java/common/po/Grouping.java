package common.po;

import java.io.Serializable;

public class Grouping implements Serializable {

	private Integer eid;
	private String group_name;
	private String account;


	public Integer getEid(){
		return eid;
	}
	public String getGroup_name(){
		return group_name;
	}
	public String getAccount(){
		return account;
	}
	public void setEid(Integer eid){
		this.eid=eid;
	}
	public void setGroup_name(String group_name){
		this.group_name=group_name;
	}
	public void setAccount(String account){
		this.account=account;
	}
}
