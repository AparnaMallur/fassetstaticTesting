package com.fasset.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.joda.time.LocalDateTime;

import com.fasset.controller.abstracts.MyAbstractController;
import com.fasset.entities.abstracts.AbstractEntity;

@Entity
@Table(name="MenuAccess_Master")
public class MenuAccessMaster extends AbstractEntity {
	
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue
	@Column(name="access_Id",unique = true, nullable = false)
	private Long access_Id;
	
	@Column(name="user_Id", nullable = true, length = MyAbstractController.SIZE_10)
	private  Long user_Id;
	
	@Column(name="menu_Id", nullable = true, length = MyAbstractController.SIZE_10)
	private int menu_Id;
	
	@Column(name = "access_Update", nullable = true, length = MyAbstractController.SIZE_TWO)
	private Boolean access_Update;
	
	@Column(name = "access_Insert", nullable = true, length = MyAbstractController.SIZE_TWO)
	private Boolean access_Insert;
	
	@Column(name = "access_Delete", nullable = true, length = MyAbstractController.SIZE_TWO)
	private Boolean access_Delete;
	
	@Column(name = "access_View", nullable = true, length = MyAbstractController.SIZE_TWO)
	private Boolean access_View;
	
	@Column(name = "access_List", nullable = true, length = MyAbstractController.SIZE_TWO)
	private Boolean access_List;
	
	@Column(name = "from_Mobile", nullable = true, length = MyAbstractController.SIZE_TWO)
	private Boolean from_Mobile;
	
	@Column(name = "created_Date", nullable = true)
	private LocalDateTime created_Date; 
	
	@Column(name = "IpAddress", nullable = true, length = MyAbstractController.SIZE_FIFTY)
	private String IpAddress;
	
    @Column(name = "created_By", nullable = true, length = MyAbstractController.SIZE_THIRTY)
	 private  Long created_By ;

    @Column(name="flag", nullable = true, length = MyAbstractController.SIZE_TWO)
    private Boolean flag;
    
    @Column(name="role_Id",nullable = true, length = MyAbstractController.SIZE_10)
    private Long role_Id;
    
	public Long getAccess_Id() {
		return access_Id;
	}

	public void setAccess_Id(Long access_Id) {
		this.access_Id = access_Id;
	}

	public Long getUser_Id() {
		return user_Id;
	}

	public void setUser_Id(Long user_Id) {
		this.user_Id = user_Id;
	}

	public int getMenu_Id() {
		return menu_Id;
	}

	public void setMenu_Id(int menu_Id) {
		this.menu_Id = menu_Id;
	}

	public Boolean getAccess_Update() {
		return access_Update;
	}

	public void setAccess_Update(Boolean access_Update) {
		this.access_Update = access_Update;
	}

	public Boolean getAccess_Insert() {
		return access_Insert;
	}

	public void setAccess_Insert(Boolean access_Insert) {
		this.access_Insert = access_Insert;
	}

	public Boolean getAccess_Delete() {
		return access_Delete;
	}

	public void setAccess_Delete(Boolean access_Delete) {
		this.access_Delete = access_Delete;
	}

	public Boolean getAccess_View() {
		return access_View;
	}

	public void setAccess_View(Boolean access_View) {
		this.access_View = access_View;
	}

	public Boolean getAccess_List() {
		return access_List;
	}

	public void setAccess_List(Boolean access_List) {
		this.access_List = access_List;
	}

	public Boolean getFrom_Mobile() {
		return from_Mobile;
	}

	public void setFrom_Mobile(Boolean from_Mobile) {
		this.from_Mobile = from_Mobile;
	}

	public LocalDateTime getCreated_Date() {
		return created_Date;
	}

	public void setCreated_Date(LocalDateTime created_Date) {
		this.created_Date = created_Date;
	}

	public String getIpAddress() {
		return IpAddress;
	}

	public void setIpAddress(String ipAddress) {
		IpAddress = ipAddress;
	}

	public Long getCreated_By() {
		return created_By;
	}

	public void setCreated_By(Long created_By) {
		this.created_By = created_By;
	}

	public Boolean getFlag() {
		return flag;
	}

	public void setFlag(Boolean flag) {
		this.flag = flag;
	}

	public Long getRole_Id() {
		return role_Id;
	}

	public void setRole_Id(Long role_Id) {
		this.role_Id = role_Id;
	}
	
}
