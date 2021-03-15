package com.fasset.entities;


import javax.persistence.*;

import com.fasset.controller.abstracts.MyAbstractController;
import com.fasset.entities.abstracts.AbstractEntity;

/*
 * @author Vijay Kumbharkar
 *
 * deven infotech pvt ltd.
 */
@Entity
@Table(name="menu_master")
public class MenuMaster extends AbstractEntity 
{

	 private static final  long  serialVersionUID = 1L;
	
	@Id
	@GeneratedValue
	@Column(name="menu_id", unique = true, nullable = false)
	private Long menu_id;
	
	@Column(name = "menu_name",  nullable = true, length = MyAbstractController.SIZE_TWO_HUNDRED)
	private String menu_name;
	 
	@Column(name = "status", nullable = true, length = MyAbstractController.SIZE_THIRTY)
	private Boolean status;
	
	@Column(name="menu_url", nullable = true, length = MyAbstractController.SIZE_TWO_HUNDRED)
	private String menu_url;
	 
	@OneToOne
	@JoinColumn(name="parent_id", nullable = true)
	private MenuMaster parent_id;
	
	@Transient
	private Long parent;
	 
	@Column(name="sequence_no", nullable = true, length = MyAbstractController.SIZE_10)
	private Long sequence_no;
	
	@Column(name="menu_icon", nullable = true, length = MyAbstractController.SIZE_HUNDRED)
	private String menu_icon;
	
	@Column(name="superUser", nullable = true, length = MyAbstractController.SIZE_TWO_HUNDRED)
	private int superUser;
	
	@Column(name="client", nullable = true, length = MyAbstractController.SIZE_TWO_HUNDRED)
	private int client;
	
	@Column(name="auditor", nullable = true, length = MyAbstractController.SIZE_TWO_HUNDRED)
	private int auditor;
	
	@Column(name = "from_mobile", nullable = true, length = MyAbstractController.SIZE_THIRTY)
	private Boolean from_mobile;
	
	@Column(name = "ip_address", nullable = true)
	private String ip_address ;

	public MenuMaster() {
		super();
	}

	public MenuMaster(Long menu_id, String menu_name) {
		super();
		this.menu_id = menu_id;
		this.menu_name = menu_name;
	}

	public Long getMenu_id() {
		return menu_id;
	}

	public void setMenu_id(Long menu_id) {
		this.menu_id = menu_id;
	}

	public String getMenu_name() {
		return menu_name;
	}

	public void setMenu_name(String menu_name) {
		this.menu_name = menu_name;
	}

	public Boolean getStatus() {
		return status;
	}

	public void setStatus(Boolean status) {
		this.status = status;
	}

	public String getMenu_url() {
		return menu_url;
	}

	public void setMenu_url(String menu_url) {
		this.menu_url = menu_url;
	}

	public MenuMaster getParent_id() {
		return parent_id;
	}

	public void setParent_id(MenuMaster parent_id) {
		this.parent_id = parent_id;
	}

	public Long getParent() {
		return parent;
	}

	public void setParent(Long parent) {
		this.parent = parent;
	}

	public Long getSequence_no() {
		return sequence_no;
	}

	public void setSequence_no(Long sequence_no) {
		this.sequence_no = sequence_no;
	}

	public String getMenu_icon() {
		return menu_icon;
	}

	public void setMenu_icon(String menu_icon) {
		this.menu_icon = menu_icon;
	}

	public int getSuperUser() {
		return superUser;
	}

	public void setSuperUser(int superUser) {
		this.superUser = superUser;
	}

	public int getClient() {
		return client;
	}

	public void setClient(int client) {
		this.client = client;
	}

	public int getAuditor() {
		return auditor;
	}

	public void setAuditor(int auditor) {
		this.auditor = auditor;
	}

	public Boolean getFrom_mobile() {
		return from_mobile;
	}

	public void setFrom_mobile(Boolean from_mobile) {
		this.from_mobile = from_mobile;
	}

	public String getIp_address() {
		return ip_address;
	}

	public void setIp_address(String ip_address) {
		this.ip_address = ip_address;
	}

}
