package com.fasset.entities;

import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.joda.time.LocalDate;

import com.fasset.entities.abstracts.AbstractEntity;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "user")
public class User extends AbstractEntity{
	
	private static final long serialVersionUID = 1L;

	
	@Id
	@GeneratedValue
	@Column(name = "user_id", unique = true, nullable = false)
	private Long user_id;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "company_id")
	@JsonIgnore
	private Company company;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "city_id", referencedColumnName = "city_id", insertable = true, updatable = true)
	@JsonIgnore
	private City city;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "country_id", referencedColumnName = "country_id", insertable = true, updatable = true)
	@JsonIgnore
	private Country country;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "state_id", referencedColumnName = "state_id", insertable = true, updatable = true)
	@JsonIgnore
	private State state;
	
	@Transient
	private Long country_id ;
	
	@Transient
	private Long state_id ;
	
	@Transient
	private Long city_id ;
	
	@Transient
	private Long role_id ;
	
	@Transient
	private Long company_id ;
	
	@Column(name = "first_name")
	private String first_name;
	
	@Column(name = "middle_name")
	private String middle_name;
	
	@Column(name = "last_name")
	private String last_name;
	
	@Column(name = "dob")
	private LocalDate dob;
	
	@Column(name = "email")
	private String email;
	
	@Column(name = "email_varification_code")
	private String email_varification_code;
	
	@Column(name = "mobile_no")
	private String mobile_no;
	
	@Column(name = "landline_no")
	private String landline_no;
	
	@Column(name = "adhaar_no")
	private String adhaar_no;
	
	@Column(name = "pan_no")
	private String pan_no;
	
	@Column(name = "amount", length = 20)
	private String amount;
	
	@Column(name = "current_address")
	private String current_address;
	
	@Column(name = "permenant_address")
	private String permenant_address;
	
	@Column(name = "pin_code")
	private String pin_code;
	
	@Column(name = "joinDate")
	private String joinDate;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "manager_id")
	@JsonIgnore
	private User manager_id;
	
	@Transient
	private Long manager;
	
	@Column(name = "password")
	private String password;
	
	@ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(name = "year_range", joinColumns = {@JoinColumn(name = "user_id")}, inverseJoinColumns = {@JoinColumn(name = "year_id")})
	@JsonIgnore
	private Set<AccountingYear> year_range;
	
	@Column(name = "status")
	private Boolean status;
	
	@Column(name = "created_by")
	private Integer created_by;
	
	@Column(name = "created_date")
	private LocalDate created_date;
	
	@Column(name = "updated_by")
	private Integer updated_by;
	
	@Column(name = "updated_date")
	private LocalDate updated_date;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "role_id")
	@JsonIgnore
	private Role role;
	
	@Column(name = "date_of_joining")
	private LocalDate date_of_joining;
	
	@Column(name = "date_of_leaving")
	private LocalDate date_of_leaving;
	
	@Column(name = "password_change_date")
	private LocalDate password_change_date;
	
	@Column(name = "ip_address")
	private String ip_address;
	
	@Column(name = "is_updated")
	private Boolean is_updated;
	
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
	@JsonIgnore
	private Set<ForgotPasswordLog> forgotPassword;
	
	public Long getRole_id() {
		return role_id;
	}

	public void setRole_id(Long role_id) {
		this.role_id = role_id;
	}

	public Long getCountry_id() {
		return country_id;
	}

	public void setCountry_id(Long country_id) {
		this.country_id = country_id;
	}

	public Long getState_id() {
		return state_id;
	}

	public void setState_id(Long state_id) {
		this.state_id = state_id;
	}

	public Long getCity_id() {
		return city_id;
	}

	public void setCity_id(Long city_id) {
		this.city_id = city_id;
	}

	public City getCity() {
		return city;
	}

	public void setCity(City city) {
		this.city = city;
	}

	public Country getCountry() {
		return country;
	}

	public void setCountry(Country country) {
		this.country = country;
	}

	public State getState() {
		return state;
	}

	public void setState(State state) {
		this.state = state;
	}

	public Long getUser_id() {
		return user_id;
	}

	public void setUser_id(Long user_id) {
		this.user_id = user_id;
	}

	public Company getCompany() {
		return company;
	}

	public void setCompany(Company company) {
		this.company = company;
	}

	public String getFirst_name() {
		return first_name;
	}

	public void setFirst_name(String first_name) {
		this.first_name = first_name;
	}

	public String getMiddle_name() {
		return middle_name;
	}

	public void setMiddle_name(String middle_name) {
		this.middle_name = middle_name;
	}

	public String getLast_name() {
		return last_name;
	}

	public void setLast_name(String last_name) {
		this.last_name = last_name;
	}

	public LocalDate getDob() {
		return dob;
	}

	public void setDob(LocalDate dob) {
		this.dob = dob;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getEmail_varification_code() {
		return email_varification_code;
	}

	public void setEmail_varification_code(String email_varification_code) {
		this.email_varification_code = email_varification_code;
	}

	public String getMobile_no() {
		return mobile_no;
	}

	public void setMobile_no(String mobile_no) {
		this.mobile_no = mobile_no;
	}

	public String getAdhaar_no() {
		return adhaar_no;
	}

	public void setAdhaar_no(String adhaar_no) {
		this.adhaar_no = adhaar_no;
	}

	public String getPan_no() {
		return pan_no;
	}

	public void setPan_no(String pan_no) {
		this.pan_no = pan_no;
	}

	public User getManager_id() {
		return manager_id;
	}

	public void setManager_id(User manager_id) {
		this.manager_id = manager_id;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Set<AccountingYear> getYear_range() {
		return year_range;
	}

	public void setYear_range(Set<AccountingYear> year_range) {
		this.year_range = year_range;
	}

	public Integer getCreated_by() {
		return created_by;
	}

	public void setCreated_by(Integer created_by) {
		this.created_by = created_by;
	}

	public LocalDate getCreated_date() {
		return created_date;
	}

	public void setCreated_date(LocalDate created_date) {
		this.created_date = created_date;
	}

	public Integer getUpdated_by() {
		return updated_by;
	}

	public void setUpdated_by(Integer updated_by) {
		this.updated_by = updated_by;
	}

	public LocalDate getUpdated_date() {
		return updated_date;
	}

	public void setUpdated_date(LocalDate updated_date) {
		this.updated_date = updated_date;
	}

	public Role getRole() {
		return role;
	}

	public void setRole(Role role) {
		this.role = role;
	}

	public LocalDate getDate_of_joining() {
		return date_of_joining;
	}

	public void setDate_of_joining(LocalDate date_of_joining) {
		this.date_of_joining = date_of_joining;
	}

	public LocalDate getDate_of_leaving() {
		return date_of_leaving;
	}

	public void setDate_of_leaving(LocalDate date_of_leaving) {
		this.date_of_leaving = date_of_leaving;
	}

	public LocalDate getPassword_change_date() {
		return password_change_date;
	}

	public void setPassword_change_date(LocalDate password_change_date) {
		this.password_change_date = password_change_date;
	}

	public String getIp_address() {
		return ip_address;
	}

	public void setIp_address(String ip_address) {
		this.ip_address = ip_address;
	}

	public Boolean getIs_updated() {
		return is_updated;
	}

	public void setIs_updated(Boolean is_updated) {
		this.is_updated = is_updated;
	}	

	public Set<ForgotPasswordLog> getForgotPassword() {
		return forgotPassword;
	}

	public void setForgotPassword(Set<ForgotPasswordLog> forgotPassword) {
		this.forgotPassword = forgotPassword;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((first_name == null) ? 0 : first_name.hashCode());
		result = prime * result + ((user_id == null) ? 0 : user_id.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		User other = (User) obj;
		if (first_name == null) {
			if (other.first_name != null)
				return false;
		} else if (!first_name.equals(other.first_name))
			return false;
		if (user_id == null) {
			if (other.user_id != null)
				return false;
		} else if (!user_id.equals(other.user_id))
			return false;
		return true;
	}

	public Boolean getStatus() {
		return status;
	}

	public void setStatus(Boolean status) {
		this.status = status;
	}

	public Long getCompany_id() {
		return company_id;
	}

	public void setCompany_id(Long company_id) {
		this.company_id = company_id;
	}

	/**
	 * @return the current_address
	 */
	public String getCurrent_address() {
		return current_address;
	}

	/**
	 * @param current_address the current_address to set
	 */
	public void setCurrent_address(String current_address) {
		this.current_address = current_address;
	}

	/**
	 * @return the permenant_address
	 */
	public String getPermenant_address() {
		return permenant_address;
	}

	/**
	 * @param permenant_address the permenant_address to set
	 */
	public void setPermenant_address(String permenant_address) {
		this.permenant_address = permenant_address;
	}

	/**
	 * @return the pin_code
	 */
	public String getPin_code() {
		return pin_code;
	}

	/**
	 * @param pin_code the pin_code to set
	 */
	public void setPin_code(String pin_code) {
		this.pin_code = pin_code;
	}

	/**
	 * @return the landline_no
	 */
	public String getLandline_no() {
		return landline_no;
	}

	/**
	 * @param landline_no the landline_no to set
	 */
	public void setLandline_no(String landline_no) {
		this.landline_no = landline_no;
	}

	/**
	 * @return the joinDate
	 */
	public String getJoinDate() {
		return joinDate;
	}

	/**
	 * @param joinDate the joinDate to set
	 */
	public void setJoinDate(String joinDate) {
		this.joinDate = joinDate;
	}

	/**
	 * @return the amount
	 */
	public String getAmount() {
		return amount;
	}

	/**
	 * @param amount the amount to set
	 */
	public void setAmount(String amount) {
		this.amount = amount;
	}

	public Long getManager() {
		return manager;
	}

	public void setManager(Long manager) {
		this.manager = manager;
	}	
}