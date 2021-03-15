package com.fasset.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;	
import org.joda.time.LocalDate;

import com.fasset.entities.abstracts.AbstractEntity;

@Entity
@Table(name = "inventory_quantity_adjustment")
public class InventoryQuantityAdjustment extends AbstractEntity{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue
	@Column(name = "inventory_adj_id")
	private Long inventory_adj_id;
	
	@Column(name = "date")
	private LocalDate date;
	
	@Transient
	private String dateString;
	
	@Transient
	private Double inward;
	
	@Transient
	private Double outward;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "product")
	private Product product;
	
	@Transient
	private Long productId;
	
	@Column(name = "quantity")
	private Double quantity;
	
	@Column(name = "value")
	private Double value;
	
	@Column(name = "is_addition")
	private Boolean is_addition;
	
	@Column(name = "remark")
	private String remark;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "accounting_year_id")
	private AccountingYear accounting_year_id;
	
	@Transient
	private Long yearId;
	
	@Column(name = "status")
	private Boolean status;
	
	@Column(name = "updated_date")
	private LocalDate updated_date;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "created_by")
	private User created_by;
	
	@Column(name = "created_date")
	private LocalDate created_date;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "updated_by")
	private User updated_by;
	
	@Column(name = "from_mobile")
	private Boolean from_mobile;
	
	@Column(name = "ip_address")
	private String ip_address;
	
	 @Transient
	 private String voucher_range;
	 
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "company_id", referencedColumnName = "company_id", insertable = true, updatable = true)
	private Company company ;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "stock_id", referencedColumnName = "stock_id", insertable = true, updatable = true)
	private Stock stock;

	public Long getInventory_adj_id() {
		return inventory_adj_id;
	}

	public void setInventory_adj_id(Long inventory_adj_id) {
		this.inventory_adj_id = inventory_adj_id;
	}

	public LocalDate getDate() {
		return date;
	}

	public void setDate(LocalDate date) {
		this.date = date;
	}

	public Product getProduct() {
		return product;
	}

	public void setProduct(Product product) {
		this.product = product;
	}

	public Long getProductId() {
		return productId;
	}

	public void setProductId(Long productId) {
		this.productId = productId;
	}

	public Double getQuantity() {
		return quantity;
	}

	public void setQuantity(Double quantity) {
		this.quantity = quantity;
	}

	public Boolean getIs_addition() {
		return is_addition;
	}

	public void setIs_addition(Boolean is_addition) {
		this.is_addition = is_addition;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public AccountingYear getAccounting_year_id() {
		return accounting_year_id;
	}

	public void setAccounting_year_id(AccountingYear accounting_year_id) {
		this.accounting_year_id = accounting_year_id;
	}

	public Long getYearId() {
		return yearId;
	}

	public void setYearId(Long yearId) {
		this.yearId = yearId;
	}

	public Boolean getStatus() {
		return status;
	}

	public void setStatus(Boolean status) {
		this.status = status;
	}

	public LocalDate getUpdated_date() {
		return updated_date;
	}

	public void setUpdated_date(LocalDate updated_date) {
		this.updated_date = updated_date;
	}

	public User getCreated_by() {
		return created_by;
	}

	public void setCreated_by(User created_by) {
		this.created_by = created_by;
	}

	public LocalDate getCreated_date() {
		return created_date;
	}

	public void setCreated_date(LocalDate created_date) {
		this.created_date = created_date;
	}

	public User getUpdated_by() {
		return updated_by;
	}

	public void setUpdated_by(User updated_by) {
		this.updated_by = updated_by;
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

	public String getDateString() {
		return dateString;
	}

	public void setDateString(String dateString) {
		this.dateString = dateString;
	}

	public Company getCompany() {
		return company;
	}

	public void setCompany(Company company) {
		this.company = company;
	}

	public Stock getStock() {
		return stock;
	}

	public void setStock(Stock stock) {
		this.stock = stock;
	}
	
	public Double getInward() {
		return inward;
	}

	public void setInward(Double inward) {
		this.inward = inward;
	}

	public Double getOutward() {
		return outward;
	}

	public void setOutward(Double outward) {
		this.outward = outward;
	}

	public String getVoucher_range() {
		return voucher_range;
	}

	public void setVoucher_range(String voucher_range) {
		this.voucher_range = voucher_range;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public Double getValue() {
		return value;
	}

	public void setValue(Double value) {
		this.value = value;
	}
	
}
