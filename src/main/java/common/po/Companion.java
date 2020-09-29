package common.po;

import java.io.Serializable;

public class Companion implements Serializable {

	private Integer eid;
	private String mygroup;
	private Integer friend_account;
	private Integer my_account;
	private String remark;


	public Integer getEid(){
		return eid;
	}
	public String getMygroup(){
		return mygroup;
	}
	public Integer getFriend_account(){
		return friend_account;
	}
	public Integer getMy_account(){
		return my_account;
	}
	public String getRemark(){
		return remark;
	}
	public void setEid(Integer eid){
		this.eid=eid;
	}
	public void setMygroup(String mygroup){
		this.mygroup=mygroup;
	}
	public void setFriend_account(Integer friend_account){
		this.friend_account=friend_account;
	}
	public void setMy_account(Integer my_account){
		this.my_account=my_account;
	}
	public void setRemark(String remark){
		this.remark=remark;
	}
}
