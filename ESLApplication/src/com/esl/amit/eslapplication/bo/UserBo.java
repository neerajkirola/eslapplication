package com.esl.amit.eslapplication.bo;

import java.io.Serializable;

public class UserBo implements Serializable {

	private String fullName;
	private String msgHeading;
	private String msgDesc;
	private String dateTime;
	private String id;
	private String dateStamp;
	
	private boolean readStatus;
	private boolean status;

	public String getFullName() {
		return fullName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	public String getMsgHeading() {
		return msgHeading;
	}

	public void setMsgHeading(String msgHeading) {
		this.msgHeading = msgHeading;
	}

	public String getMsgDesc() {
		return msgDesc;
	}

	public void setMsgDesc(String msgDesc) {
		this.msgDesc = msgDesc;
	}

	public String getDateTime() {
		return dateTime;
	}

	public void setDateTime(String dateTime) {
		this.dateTime = dateTime;
	}

	public boolean isReadStatus() {
		return readStatus;
	}

	public void setReadStatus(boolean readStatus) {
		this.readStatus = readStatus;
	}

	public boolean isStatus() {
		return status;
	}

	public void setStatus(boolean status) {
		this.status = status;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getDateStamp() {
		return dateStamp;
	}

	public void setDateStamp(String dateStamp) {
		this.dateStamp = dateStamp;
	}	
		
}
