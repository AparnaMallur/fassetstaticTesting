package com.fasset.mobileApp.entity;

public class LoginDetails {

    public String username;

    public String password;
    
    public String firstName;

    public String lastName;
    
    public String successMsg;

    public String responseMassage;
    
    public String getUsername ()
    {
        return username;
    }

    public void setUsername (String username)
    {
        this.username = username;
    }

    public String getPassword ()
    {
        return password;
    }

    public void setPassword (String password)
    {
        this.password = password;
    }
	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	
	public String getResponseMassage() {
		return responseMassage;
	}

	public void setResponseMassage(String responseMassage) {
		this.responseMassage = responseMassage;
	}

	public String getSuccessMsg() {
		return successMsg;
	}

	public void setSuccessMsg(String successMsg) {
		this.successMsg = successMsg;
	}
}