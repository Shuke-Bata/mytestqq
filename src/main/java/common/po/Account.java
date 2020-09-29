package common.po;

import java.io.Serializable;

public class Account implements Serializable {

	private Integer eid;
	private String password;
	private String signature;
	private String user_name;
	private String background;
	private String nickname;
	private String avatar;
	private String account;


	public Integer getEid(){
		return eid;
	}
	public String getPassword(){
		return password;
	}
	public String getSignature(){
		return signature;
	}
	public String getUser_name(){
		return user_name;
	}
	public String getBackground(){
		return background;
	}
	public String getNickname(){
		return nickname;
	}
	public String getAvatar(){
		return avatar;
	}
	public String getAccount(){
		return account;
	}
	public void setEid(Integer eid){
		this.eid=eid;
	}
	public void setPassword(String password){
		this.password=password;
	}
	public void setSignature(String signature){
		this.signature=signature;
	}
	public void setUser_name(String user_name){
		this.user_name=user_name;
	}
	public void setBackground(String background){
		this.background=background;
	}
	public void setNickname(String nickname){
		this.nickname=nickname;
	}
	public void setAvatar(String avatar){
		this.avatar=avatar;
	}
	public void setAccount(String account){
		this.account=account;
	}
}
