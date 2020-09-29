package common.po;

import java.io.Serializable;

public class Offline_message implements Serializable {

	private Integer eid;
	private java.sql.Timestamp message_time;
	private Integer to_account_id;
	private String message;
	private Integer from_account_id;


	public Integer getEid(){
		return eid;
	}
	public java.sql.Timestamp getMessage_time(){
		return message_time;
	}
	public Integer getTo_account_id(){
		return to_account_id;
	}
	public String getMessage(){
		return message;
	}
	public Integer getFrom_account_id(){
		return from_account_id;
	}
	public void setEid(Integer eid){
		this.eid=eid;
	}
	public void setMessage_time(java.sql.Timestamp message_time){
		this.message_time=message_time;
	}
	public void setTo_account_id(Integer to_account_id){
		this.to_account_id=to_account_id;
	}
	public void setMessage(String message){
		this.message=message;
	}
	public void setFrom_account_id(Integer from_account_id){
		this.from_account_id=from_account_id;
	}
}
