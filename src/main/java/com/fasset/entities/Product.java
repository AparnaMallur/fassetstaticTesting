package com.fasset.entities;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.joda.time.LocalDate;

import com.fasset.controller.abstracts.MyAbstractController;
import com.fasset.entities.abstracts.AbstractEntity;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "product")
public class Product extends AbstractEntity{
	
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue
	@Column(name = "product_id")
	private Long product_id;
	
	@Column(name = "product_name")
	private String product_name;
	
	@Column(name = "type")
	private Integer type;
	
	@Column(name = "hsn_san_no")
	private String hsn_san_no;
	
	@Column(name = "sales_rate")
	private Float sales_rate;
	
	@Column(name = "tax_type")
	private Long tax_type;
	
	@Transient
	private Long company_id ;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "uom")
	@JsonIgnore
	private UnitOfMeasurement uom;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "gst_id")
	@JsonIgnore
	private GstTaxMaster gst_id;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "tax_id")
	@JsonIgnore
	private TaxMaster taxMaster;	
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "stock_id", referencedColumnName = "stock_id", insertable = true, updatable = true)
	@JsonIgnore
	private Stock stock;

	@Column(name = "allocated", nullable = true)
	private Boolean allocated ;
	
	@Override
	public String toString() {
		return "Product [product_id=" + this.product_id + ", product_name=" + this.product_name + "]";
	}

	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(name="supplier_master_product",joinColumns=@JoinColumn(name="product_product_id",referencedColumnName="product_id"),
	inverseJoinColumns=@JoinColumn(name="supplier_master_supplier_id",referencedColumnName="supplier_id"))
	@JsonIgnore
	private Set<Suppliers> supplier = new HashSet<Suppliers>();
	
	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(name="customer_master_product",joinColumns=@JoinColumn(name="product_product_id",referencedColumnName="product_id"),
	inverseJoinColumns=@JoinColumn(name="customer_master_customer_id",referencedColumnName="customer_id"))
	@JsonIgnore
	private Set<Customer> customer = new HashSet<Customer>();

	
	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(name="purchase_Entry_product",joinColumns=@JoinColumn(name="products_product_id",referencedColumnName="product_id"),
	inverseJoinColumns=@JoinColumn(name="purchase_Entry_purchase_id",referencedColumnName="purchase_id"))
	@JsonIgnore
	private Set<PurchaseEntry> purchaseEntry = new HashSet<PurchaseEntry>();
	
	
	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(name="sales_Entry_product",joinColumns=@JoinColumn(name="products_product_id",referencedColumnName="product_id"),
	inverseJoinColumns=@JoinColumn(name="sales_Entry_sales_id",referencedColumnName="sales_id"))
	@JsonIgnore
	private Set<SalesEntry> salesEntry = new HashSet<SalesEntry>();
	
	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(name="receipt_product",joinColumns=@JoinColumn(name="products_product_id",referencedColumnName="product_id"),
	inverseJoinColumns=@JoinColumn(name="receipt_receipt_id",referencedColumnName="receipt_id"))
	@JsonIgnore
	private Set<Receipt> receipt = new HashSet<Receipt>();
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "company_id")
	@JsonIgnore
	private Company company ;

	@Column(name = "status")
	private Boolean status;
	
	@Column(name = "from_mobile")
	private Boolean from_mobile;

	@Column(name = "created_date")
	private LocalDate created_date;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "created_by")
	@JsonIgnore
	private User created_by;
	
	@Column(name = "updated_date")
	private LocalDate updated_date;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "updated_by")
	@JsonIgnore
	private User updated_by;
	
	@Column(name = "product_approval", nullable = true, length = MyAbstractController.SIZE_TWENTY)
	private Integer product_approval ;
	
	
	@Transient
	private List<String> productList ;
	
	@Transient
	private Boolean primaryApproval;
	
	@Transient
	private Boolean rejectApproval;
	
	@Column(name = "ip_address", nullable = true)
	private String ip_address ;
	
	public Integer getProduct_approval() {
		return product_approval;
	}

	public void setProduct_approval(Integer product_approval) {
		this.product_approval = product_approval;
	}

	
	public Set<PurchaseEntry> getPurchaseEntry() {
		return purchaseEntry;
	}

	public void setPurchaseEntry(Set<PurchaseEntry> purchaseEntry) {
		this.purchaseEntry = purchaseEntry;
	}

	@Transient
	private Long unit;
	
	@Transient
	private Long tax_id;

	@Transient
	private Long vat_id;
	
	@Column(name = "flag")
	private Boolean flag;
	

	public Long getProduct_id() {
		return product_id;
	}

	public void setProduct_id(Long product_id) {
		this.product_id = product_id;
	}	
	
	
	public String getProduct_name() {
		return product_name;
	}

	public void setProduct_name(String product_name) {
		this.product_name = product_name;
	}	
	
	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}		
	
	public String getHsn_san_no() {
		return hsn_san_no;
	}

	public void setHsn_san_no(String hsn_san_no) {
		this.hsn_san_no = hsn_san_no;
	}
	
	public Float getSales_rate() {
		return sales_rate;
	}

	public void setSales_rate(Float sales_rate) {
		this.sales_rate = sales_rate;
	}
	
	public UnitOfMeasurement getUom() {
		return uom;
	}

	public void setUom(UnitOfMeasurement uom) {
		this.uom = uom;
	}
	
	public GstTaxMaster getGst_id() {
		return gst_id;
	}

	public void setGst_id(GstTaxMaster gst_id) {
		this.gst_id = gst_id;
	}	
	
	public Boolean getStatus() {
		return status;
	}

	public void setStatus(Boolean status) {
		this.status = status;
	}	
	
	public Boolean getFrom_mobile() {
		return from_mobile;
	}

	public void setFrom_mobile(Boolean from_mobile) {
		this.from_mobile = from_mobile;
	}	
	
	public LocalDate getCreated_date() {
		return created_date;
	}

	public void setCreated_date(LocalDate created_date) {
		this.created_date = created_date;
	}	
	
	public User getCreated_by() {
		return created_by;
	}

	public void setCreated_by(User created_by) {
		this.created_by = created_by;
	}	
	
	public LocalDate getUpdated_date() {
		return updated_date;
	}

	public void setUpdated_date(LocalDate updated_date) {
		this.updated_date = updated_date;
	}	
	
	public User getUpdated_by() {
		return updated_by;
	}

	public void setUpdated_by(User updated_by) {
		this.updated_by = updated_by;
	}
		
	public Long getUnit() {
		return unit;
	}

	public void setUnit(Long unit) {
		this.unit = unit;
	}
		
	public Long getTax_id() {
		return tax_id;
	}

	public void setTax_id(Long tax_id) {
		this.tax_id = tax_id;
	}
		
	public Set<Customer> getCustomer() {
		return customer;
	}

	public void setCustomer(Set<Customer> customer) {
		this.customer = customer;
	}
		
	public Set<Suppliers> getSupplier() {
		return supplier;
	}

	public void setSupplier(Set<Suppliers> supplier) {
		this.supplier = supplier;
	}

	public Set<SalesEntry> getSalesEntry() {
		return salesEntry;
	}

	public void setSalesEntry(Set<SalesEntry> salesEntry) {
		this.salesEntry = salesEntry;
	}

	public Company getCompany() {
		return company;
	}

	public void setCompany(Company company) {
		this.company = company;
	}

	public Long getCompany_id() {
		return company_id;
	}

	public void setCompany_id(Long company_id) {
		this.company_id = company_id;
	}

	public Set<Receipt> getReceipt() {
		return receipt;
	}

	public void setReceipt(Set<Receipt> receipt) {
		this.receipt = receipt;
	}

	public TaxMaster getTaxMaster() {
		return taxMaster;
	}

	public void setTaxMaster(TaxMaster taxMaster) {
		this.taxMaster = taxMaster;
	}

	public Long getVat_id() {
		return vat_id;
	}

	public void setVat_id(Long vat_id) {
		this.vat_id = vat_id;
	}

	public Long getTax_type() {
		return tax_type;
	}

	public void setTax_type(Long tax_type) {
		this.tax_type = tax_type;
	}

	public Boolean getFlag() {
		return flag;
	}

	public void setFlag(Boolean flag) {
		this.flag = flag;
	}

	public Stock getStock() {
		return stock;
	}

	public void setStock(Stock stock) {
		this.stock = stock;
	}

	public Boolean getAllocated() {
		return allocated;
	}

	public void setAllocated(Boolean allocated) {
		this.allocated = allocated;
	}

	public List<String> getProductList() {
		return productList;
	}

	public void setProductList(List<String> productList) {
		this.productList = productList;
	}

	public Boolean getPrimaryApproval() {
		return primaryApproval;
	}

	public void setPrimaryApproval(Boolean primaryApproval) {
		this.primaryApproval = primaryApproval;
	}

	public Boolean getRejectApproval() {
		return rejectApproval;
	}

	public void setRejectApproval(Boolean rejectApproval) {
		this.rejectApproval = rejectApproval;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((product_id == null) ? 0 : product_id.hashCode());
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
		Product other = (Product) obj;
		if (product_id == null) {
			if (other.product_id != null)
				return false;
		} else if (!product_id.equals(other.product_id))
			return false;
		return true;
	}

	public String getIp_address() {
		return ip_address;
	}

	public void setIp_address(String ip_address) {
		this.ip_address = ip_address;
	}
}