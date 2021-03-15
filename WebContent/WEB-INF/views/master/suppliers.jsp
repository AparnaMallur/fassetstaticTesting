<%@include file="/WEB-INF/includes/header.jsp"%>
<%@page import="com.fasset.controller.abstracts.MyAbstractController"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>

<script type="text/javascript" src="${valid}"></script>
<spring:url value="/resources/images/edit.png" var="editImg" />
<spring:url value="/resources/images/view.png" var="viewImg" />
<spring:url value="/resources/images/delete.png" var="deleteImg" />
<c:set var="SIZE_EIGHTEEN"><%=MyAbstractController.SIZE_EIGHTEEN%></c:set>
<c:set var="SIZE_THIRTY"><%=MyAbstractController.SIZE_THIRTY%></c:set>
<c:set var="SIZE_TEN"><%=MyAbstractController.SIZE_TEN%></c:set>
<c:set var="SIZE_13"><%=MyAbstractController.SIZE_13%></c:set>
<c:set var="SIZE_FIVE_HUNDRED"><%=MyAbstractController.SIZE_FIVE_HUNDRED%></c:set>
<c:set var="SIZE_HUNDRED"><%=MyAbstractController.SIZE_HUNDRED%></c:set>
<c:set var="SIZE_SIX"><%=MyAbstractController.SIZE_FIVE_HUNDRED%></c:set>
<c:set var="SIZE_12"><%=MyAbstractController.SIZE_12%></c:set>
<div class="breadcrumb">
	<h3>Supplier</h3>
	<a href="homePage">Home</a> » <a href="suppliersList">Supplier</a> » <a href="#">Create</a>
</div>
<c:if test="${successMsg != null}">
	<div class="successMsg" id="successMsg">
		<strong>${successMsg}</strong>
	</div>
</c:if>
<div class="col-md-12 wideform">
	<div class="fassetForm">
		<form:form id="supplierForm" action="savesuppliers" method="post" commandName="supplier" onsubmit="return addProduct();">
			<div class="row">
				<div class="col-md-6 col-xs-12">
					<div class="">
						<form:input path="supplier_id" hidden="hidden" />
						<form:input path="created_date" hidden="hidden" />
						<form:input path="productList" id="productList" type="hidden" />
						<form:input path="subNatureList" id="subNatureList" type="hidden" />
						<form:input path="tds_rate1" id="tds_rate1" hidden="hidden" />
					</div>
					<c:choose>
						<c:when test="${((role == '2') || (role == '3') ||(role == '4'))}">
							<div class="row">
								<div class="col-md-3 control-label">
									<label>Company Name<span>*</span></label>
								</div>
								<div class="col-md-9">
									<form:select path="company_id" id="company_id" class="logInput" onchange = "setProduct(this.value)">
										<form:option value="0" label="Select Company Name" />
										<c:forEach var="company" items="${companyList}">
												<%-- <c:choose>
													<c:when test="${company.company_id==2}">
														<form:option value = "${company.company_id}" selected="selected">${company.company_name}</form:option>	
													</c:when>
													<c:otherwise>
														<form:option value = "${company.company_id}">${company.company_name}</form:option>	
													</c:otherwise>
												</c:choose>		 --%>			
												<form:option value = "${company.company_id}">${company.company_name}</form:option>	
																	
										</c:forEach>
									</form:select>
									<span class="logError"><form:errors path="company_id" /></span>
								</div>
							</div>
						</c:when>
						<c:otherwise>
							<form:input path="company_id" id="company_id" type="hidden" value="${company_id}" />
						</c:otherwise>
					</c:choose>

					<div class="row">
						<div class="col-md-3 control-label">
						<!-- <label>Contact Name<span>*</span></label> make it non mandatory -->
						<label>Contact Name</label>	
						</div>
						<div class="col-md-9">
							<form:input path="contact_name" class="logInput" id="contact_name" maxlength="40" placeholder="Contact Name" />
							<span class="logError"><form:errors path="contact_name" /></span>
						</div>
					</div>

					<div class="row">
						<div class="col-md-3 control-label">
							<!-- <label>Mobile<span>*</span></label> make it non mandatory -->
							<label>Mobile</label>
						</div>
						<div class="col-md-9">
<form:input path="mobile" class="logInput" id="mobile"
								minlength="${SIZE_TEN}" maxlength="${SIZE_13}" placeholder="Mobile Number" />	
								
												<span class="logError"><form:errors path="mobile" /></span>
						</div>
					</div>
					<div class="row">
						<div class="col-md-3 control-label">
							<!--<label>Email Address<span>*</span></label> make it non mandatory -->
							<label>Email Address</label>
						</div>
						<div class="col-md-9">
							<form:input path="email_id" class="logInput" id="email_id" placeholder="Email Address" />
							<span class="logError"><form:errors path="email_id" /></span>
						</div>
					</div>
					<div class="row">
						<div class="col-md-3 control-label">
							<label> Suppiler Company Name<span>*</span></label>
						</div>
						<div class="col-md-9">
							<form:input path="company_name" class="logInput" id="company_name" maxlength="40" placeholder="Company Name" />
							<span class="logError"><form:errors path="company_name" /></span>
						</div>
					</div>
					<div class="row">
						<div class="col-md-3 control-label">
							<!-- <label>Company Statutory Type<span>*</span></label> make it non mandatory -->
							<label>Company Statutory Type</label>
						</div>
						<div class="col-md-9">
							<form:select path="company_statutory_id" class="logInput"
								placeholder="Company Statutory Type" id="company_statutory_id">
								<form:option value="0" label="Select Company Statutory Type" />
								<c:forEach var="typelist" items="${statutoryTypeList}">
									<form:option value="${typelist.company_statutory_id}">${typelist.company_statutory_name}</form:option>
								</c:forEach>
							</form:select>
							<span class="logError"><form:errors path="company_statutory_id" /></span>
						</div>
					</div>
					<div class="row">
						<div class="col-md-3 control-label">
							<label>Industry Type</label>
						</div>
						<div class="col-md-9">
							<form:select path="industry_id" class="logInput" placeholder="Industry type" id="industry_id">
								<form:option value="0" label="Select Industry type" />
								<c:forEach var="ilist" items="${industryTypeList}">
									<form:option value="${ilist.industry_id}">${ilist.industry_name}</form:option>
								</c:forEach>
							</form:select>
							<span class="logError"><form:errors path="industry_id" /></span>
						</div>
					</div>
					<div class="row">
						<div class="col-md-3 control-label">
							<label>Landline No</label>
						</div>
						<div class="col-md-9">
							<form:input path="landline_no" class="logInput" id="landline_no" maxlength="${SIZE_12}" placeholder="Land-line Number" />
							<span class="logError"><form:errors path="landline_no" /></span>
						</div>
					</div>
					<div class="row">
						<div class="col-md-3 control-label">
							<label>Owner PAN No</label>
						</div>
						<div class="col-md-9">
							<form:input path="owner_pan_no" class="logInput" id="owner_pan_no" maxlength="${SIZE_TEN}" placeholder="Owner PAN Number" />
							<span class="logError"><form:errors path="owner_pan_no" /></span>
						</div>
					</div>
					<div class="row">
						<div class="col-md-3 control-label">
							<label>Aadhar Number</label>
						</div>
						<div class="col-md-9">
							<form:input path="adhaar_no" class="logInput" id="adhaar_no" maxlength="${SIZE_12}" placeholder="Aadhar Number" />
							<span class="logError"><form:errors path="adhaar_no" /></span>
						</div>
					</div>
					<div class="row">
						<div class="col-md-3 control-label">
							<label>GST Applicable<span>*</span></label>
						</div>
						<div class="col-md-9">
							<form:select path="gst_applicable" class="logInput" placeholder="GST Applicable" id="gst_applicable" onChange="checktype(1,this.value)">
								<form:option value="true">Yes</form:option>
								<form:option value="false">No</form:option>
							</form:select>
						</div>
					</div>
					<div class="row" id='gstcomp'>
						<div class="col-md-3 control-label">
							<label>GST No<span></span></label>
						</div>
						<div class="col-md-9">
							<form:input path="gst_no" class="logInput" id="gst_no" maxlength="${SIZE_15}" placeholder="GST Number" />
							<span class="logError"><form:errors path="gst_no" /></span>
						</div>
					</div>
					<div class="row">
						<div class="col-md-3 control-label">
						<!-- <label>Company PAN No<span>*</span></label> make it non mandatory -->
						<label>Company PAN No</label>	
						</div>
						<div class="col-md-9">
							<form:input path="company_pan_no" class="logInput" id="company_pan_no" maxlength="${SIZE_TEN}" placeholder="Company PAN Number" />
							<span class="logError"><form:errors path="company_pan_no" /></span>
						</div>
					</div>

					<div class="row">
						<div class="col-md-3 control-label">
							<label>Other Tax No</label>
						</div>
						<div class="col-md-9">
							<form:input path="other_tax_no" class="logInput" id="other_tax_no" maxlength="${SIZE_EIGHTEEN}" placeholder="Other Tax Number" />
						</div>
					</div>
					<div class="row">
						<div class="col-md-3 control-label">
							<label>TAN No</label>
						</div>
						<div class="col-md-9">
							<form:input path="tan_no" class="logInput" id="tan_no" maxlength="${SIZE_TEN}" placeholder="TAN Number" />
							<span class="logError"><form:errors path="tan_no" /></span>
						</div>
					</div>

				</div>
				<div class="col-md-6 col-xs-12">
					<div class="row">
						<div class="col-md-3 control-label">
							<!--<label>Current Address<span>*</span></label> make it non mandatory  -->
							<label>Current Address</label>
						</div>
						<div class="col-md-9">
							<form:textarea path="current_address" class="logInput" id="current_address" rows="3" placeholder="Current Address" maxlength="200" pattern='[A-Za-z\\s]*'></form:textarea>
							<span class="logError"><form:errors path="current_address" /></span>
						</div>
					</div>

					<div class="row">
						<div class="col-md-3"></div>
				        <div class="col-md-9">
							<div class="checkbox checkbox-primary">
								<input id="checkbox2" type="checkbox" onclick="check(this)">
								<label> Same as current address </label>
							</div>
						</div>
						<div class="col-md-3 control-label">
						<!--<label>Permanent Address<span>*</span></label> make it non mandatory  -->
						<label>Permanent Address</label>	
						</div>
						<div class="col-md-9">
							<form:textarea path="permenant_address" class="logInput" id="permenant_address" rows="3" placeholder="Permanent Address"></form:textarea>
							<span class="logError"><form:errors path="permenant_address" /></span>
						</div>
					</div>

					<div class="row">
						<div class="col-md-3 control-label">
							<!--<label>Country Name<span>*</span></label> make it non mandatory  -->
							<label>Country Name</label>
						</div>
						<div class="col-md-9">

							<form:select path="country_id" class="logInput" placeholder="Country Name" id="country_id" onChange="getStateList(this.value)">
								<form:option value="0" label="Select Country Name" />
								<c:forEach var="Country" items="${countryList}">
									<form:option value="${Country.country_id}">${Country.country_name}  </form:option>
								</c:forEach>
							</form:select>
							<span class="logError"><form:errors path="country_id" /></span>
						</div>
					</div>

					<div class="row">
						<div class="col-md-3 control-label">
						<!-- <label>State Name<span>*</span></label> make it non mandatory -->
						<label>State Name</label>	
						</div>
						<div class="col-md-9">
							<form:select path="state_id" class="logInput" placeholder="State Name" id="stateId" onChange='getCityList(this.value)'>
								<form:option value="0" label="Select State Name" />
							</form:select>
							<span class="logError"><form:errors path="state_id" /></span>
						</div>
					</div>

					<div class="row">
						<div class="col-md-3 control-label">
							<!--<label>City Name<span>*</span></label> make it non mandatory  -->
							<label>City Name</label>
						</div>
						<div class="col-md-9">
							<form:select path="city_id" class="logInput" placeholder="City Name" id="cityId">
								<form:option value="0" label="Select City Name" />
							</form:select>
							<span class="logError"><form:errors path="city_id" /></span>
						</div>
					</div>

					<div class="row">
						<div class="col-md-3 control-label">
							<label>PIN Code</label>
						</div>
						<div class="col-md-9">
							<form:input path="pincode" class="logInput" id="pincode" maxlength="${SIZE_EIGHTEEN}" placeholder="PIN Code" />
							<span class="logError"><form:errors path="pincode" /></span>
						</div>
					</div>

					<div class="row" id='rcmcomp'>
						<div class="col-md-3 control-label">
							<label>Reverse Charge Mechanism <span>*</span></label>
						</div>
						<div class="col-md-9">
							<form:select path="reverse_mecha" class="logInput" placeholder="Reverse Charge Mechanism" id="reverse_mecha">
								<form:option value="Yes">Yes</form:option>
								<form:option value="No">No</form:option>
							</form:select>
						</div>
					</div>
					<div class="row">
						<div class="col-md-3 control-label">
							<label>TDS Applicable</label>
						</div>
						<div class="col-md-9">
							<form:select path="tds_applicable" class="logInput" placeholder="TDS Applicable" onChange="checktype(2,this.value)">
								<form:option value="1">Yes</form:option>
								<form:option value="0">No</form:option>
							</form:select>
						</div>
					</div>
					<div id="tds-div">
						<div class="row">
							<div class="col-md-3 control-label">
								<label>TDS Type<span>*</span></label>
							</div>
							<div class="col-md-9">
								<form:select path="deductee_id" class="logInput" placeholder="TDS Type" id="TdsId" onChange="getTdsRate(this.value)">
									<form:option value="0" label="Select TDS Type" />
									<c:forEach var="ilist" items="${deducteeList}">
										<form:option value="${ilist.deductee_id}">${ilist.deductee_title}</form:option>
									</c:forEach>
								</form:select>
								<span class="logError"><form:errors path="deductee_id" /></span>
							</div>
						</div>
						<div class="row">
							<div class="col-md-3 control-label">
								<label>TDS Rate<span>*</span></label>
							</div>
							<div class="col-md-9">
								<form:input path="tds_rate" class="logInput" placeholder="TDS Rate" id="tds_rate" />
								<span class="logError"><form:errors path="tds_rate" /></span>
							</div>
						</div>
					</div>
					<div class="row">
						<div class="col-md-3 control-label">
							<label>Status<span>*</span></label>
						</div>
						<div class="col-md-9">
							<form:radiobutton path="status" value="true" checked="checked" />
							Enable
							<form:radiobutton path="status" value="false" />
							Disable
						</div>
					</div>
				</div>
			</div>
			<div class="row">
				<ul class="nav nav-tabs navtab-bg">
					<li class="active"><a href="#home" data-toggle="tab"
						aria-expanded="false"> <span class="visible-xs"><i
								class="fa fa-home"></i></span> Ledger
					</a></li>
					<li class=""><a href="#profile" data-toggle="tab"
						aria-expanded="true"> <span class="visible-xs"><i
								class="fa fa-user"></i></span>Products
					</a></li>
				</ul>
				<div class="tab-content">
					<div class="tab-pane active" id="home">
					<div class="row sub-list" id="company-subledger">
						
							<%-- <c:set var="total_ledger" value=" " />
							<c:forEach var="lglist" items="${subLedgerList}">
								<c:set var="insertval" value="0" />
								<c:if test="${fn:contains(total_ledger, lglist.ledger.ledger_id )}">
									<c:set var="insertval" value="-1" />
								</c:if>
								<c:if test="${insertval==0}">
									<c:set var="total_ledger" value='${total_ledger},${lglist.ledger.ledger_id}' />
								</c:if>
							</c:forEach>
							<c:forTokens items="${total_ledger}" delims="," var="ledgerid">
								<c:set var="lprint" value="0" />

								<div class=" sub-list1" style="background-color: #eee;">
									<c:forEach var="subLedger" items="${subLedgerList}">
												<c:if
													test="${fn:contains(subLedger.ledger.ledger_id, ledgerid )}">

													<c:if test="${lprint==0}">
														<p class="lgr-div">${subLedger.ledger.ledger_name}
															<input name="32-ck" type="checkbox"
																onchange="checkall(this,${subLedger.ledger.ledger_id})"
																style="float: left; margin-left: 1%; margin-top: 1.8%;">
														</p>
														<c:set var="lprint" value="1" />
													</c:if>
													<div style="width: auto; display: inline"
														id="lg-${subLedger.ledger.ledger_id}">
														<input class="ckeck" name="32-ck"
															id="${subLedger.subledger_Id}-subid"
															value="${subLedger.subledger_Id}" type="checkbox"
															onchange="addSub(this)">${subLedger.subledger_name}

														<input class="logInput" style="margin: 1% 0px;"
															id="nature_type_${subLedger.subledger_Id}"
															placeholder="Nature of purpose" />
													</div>

												</c:if>
									</c:forEach>

								</div>

							</c:forTokens>
 --%>
						</div>
					</div>
					<div class="tab-pane " id="profile">
						<c:if test="${productList.size()!=0}">
							<input name="32-ck" type="checkbox"
								onchange="checkallproduct(this)"><b>Select All</b>
						</c:if>
						<div class="row sub-list"  id="products">
						</div>
					</div>
				</div>
				<c:if test="${!empty subLedgers}">	
				<div class="row" style="background-color: #eee; padding: 5px">
					<h5>Added Subledgers:</h5>
					<div class="col-md-12 col-xs-12">
						<c:forEach var="subledger" items="${subLedgers}">
							<div class="col-md-3 col-sm-4 col-xs-6" style="margin: 2px 0px;">
								<a href="#"
									onclick="deleteSubledger('${subledger.subledger_Id}')"><img
									src='${deleteImg}' style="width: 20px; margin-right: 5px" /></a>${subledger.subledger_name}
							</div>
						</c:forEach>
					</div>
				</div>
				</c:if>
				<c:if test="${!empty suppilerproductList}">
				
				<div class="row" style="background-color: #eee; padding: 5px">
					<h5>Added Products:</h5>
					<div class="col-md-12 col-xs-12">
						<c:forEach var="product" items="${suppilerproductList}">
							<div class="col-md-3 col-sm-4 col-xs-6" style="margin: 2px 0px;">
								<a href="#" onclick="deleteProduct('${product.product_id}')">
								<img src='${deleteImg}' style="width: 20px; margin-right: 5px" /></a>${product.product_name}
							</div>
						</c:forEach>
					</div>
				</div>
				</c:if>
			</div>
			<div class="row text-center-btn">
				<button class="fassetBtn" type="submit">
					<spring:message code="btn_save" />
				</button>
				<button class="fassetBtn" type="button" onclick="cancel()">
					<spring:message code="btn_cancel" />
				</button>
			</div>
		</form:form>

	</div>
</div>
<script type="text/javascript">

var company_id=document.getElementById("company_id").value;
var contact_name=document.getElementById("contact_name").value;
var mobile=document.getElementById("mobile").value;
var email_id=document.getElementById("email_id").value;
var company_name=document.getElementById("company_name").value;
var company_statutory_id=document.getElementById("company_statutory_id").value;
var gst_applicable=document.getElementById("gst_applicable").value;
var gst_no=document.getElementById("gst_no").value;
var company_pan_no=document.getElementById("company_pan_no").value;
var current_address=document.getElementById("current_address").value;
var permenant_address=document.getElementById("permenant_address").value;
var country_id=document.getElementById("country_id").value;
var stateId=document.getElementById("stateId").value;
var cityId=document.getElementById("cityId").value;
var TdsId=document.getElementById("TdsId").value;
var tds_rate=document.getElementById("tds_rate").value;


var proList = [];
	var subList = [];

	$(function() {

		setTimeout(function() {
			$("#successMsg").hide()
		}, 3000);
		$("#contact_name,#company_name").keypress(function(e) {
			if (!lettersAndDigitsAndSlashOnly(e)) {
				return false;
			}
		});

		$("#reverse_mecha").keypress(function(e) {
			if (!lettersAndHyphenOnly(e)) {
				return false;
			}
		});

		$("#mobile,#landline_no,#adhaar_no,#pincode").keypress(function(e) {
			if (!digitsOnly(e)) {
				return false;
			}
		});

		$("#owner_pan_no,#other_tax_no,#company_pan_no,#tan_no,#gst_no")
				.keypress(function(e) {
					if (!lettersAndDigitsAndSlashOnly(e)) {
						return false;
					}
				});
	/* 	$("#gst_no,#owner_pan_no,#company_pan_no").keyup(function(e) {
			   $(this).val($(this).val().toUpperCase());
			});		
		 */
		$("#gst_no,#owner_pan_no,#company_pan_no").on('input', function(){	    
		    var start = this.selectionStart,
		        end = this.selectionEnd;
		    
		    this.value = this.value.toUpperCase();
		    this.setSelectionRange(start, end);
		});
		
		$("#tds_rate1").keypress(function(e) {
			if (!digitsAndDotOnly(e)) {
				return false;
			}
		});
		
    
	});
	function check(cb) {
		if ($(cb).is(":checked")) {
			$("#permenant_address").val($("#current_address").val());
			$('#permenant_address').attr('readonly', true);
		} else {
			$("#permenant_address").val("");
			$('#permenant_address').attr('readonly', false);

		}
	}
	function cancel() {
		window.location.assign('<c:url value = "suppliersList"/>');
	}

	function deleteSubledger(id) {
		window.location.assign('<c:url value="deleteSupplierSubLedger"/>?id='
				+ id);
	}

	function deleteProduct(id) {
		window.location.assign('<c:url value="deleteSupplierProduct"/>?id='
				+ id);
	}
	function addPro(product) {
		var proValue = product.value;
		if (product.checked) {
			proList.push(proValue);
			console.log(proList);
		} else {
			for (i = 0; i < proList.length; i++) {
				if (proList[i] == proValue) {
					proList.splice(i, 1);
					break;
				}
			}
			console.log(proList);
		}
	}

	function addProduct() {
		$("#productList").val(proList);
		$("#subNatureList").val(JSON.stringify(subList));
	
		console.log(JSON.stringify(subList));
		console.log($("#subNatureList").val());
		
    var supplier_id = '<c:out value= "${supplier.supplier_id}"/>'; 
	if(contact_name!=null && contact_name!="" && mobile!=null && mobile!="" && email_id!=null && email_id!="" && company_name!=null && company_name!="" && company_statutory_id!=null && company_statutory_id!="" && gst_applicable!=null && gst_applicable!="" && gst_no!=null && gst_no!="" && company_pan_no!=null && company_pan_no!="" && current_address!=null && current_address!="" && permenant_address!=null && permenant_address!="" && country_id!=null && country_id!="" && stateId!=null && stateId!="" && cityId!=null && cityId!="" && TdsId!=null && TdsId!="" && tds_rate!="" && tds_rate!=null)
    {
		alert("sek 1")
		if(supplier_id==null || supplier_id == "")
		{	
			
			<c:if test="${supplier.subLedgers.size() == 0}">	
				if(subList.length == 0){
					alert('Please select atleast one ledger');
					return false;
				}
			</c:if>
		
			<c:if test="${supplier.product.size() == 0}">
				if(proList.length == 0){
					alert('Please select atleast one product');
					return false;
				}
			</c:if>
			}
		}
		else
		{
			
			<c:if test="${supplier.subLedgers.size() == 0}">	
			if(subList.length == 0){
				alert('Please select atleast one ledger');
				return false;
			}
		</c:if>	
		<c:if test="${suppilerproductList.size() == 0}">
			if(proList.length == 0){
				alert('Please select atleast one product');
				return false;
			}
		</c:if>
			
		}
		return true;
	}

	function getTdsRate(deductee_id) {
		<c:forEach var = "list" items = "${deducteeList}">
		var id = <c:out value = "${list.deductee_id}"/>
			if(id == deductee_id){
				 $("#tds_rate").val("${list.value}");
				 $("#tds_rate1").val("${list.value}");
			}
		</c:forEach>
	}
	
	$( document ).ready(function() {
		var supplier_id = '<c:out value= "${supplier.supplier_id}"/>';
		var company_id=$("#company_id").val();
		if((supplier_id=="") && (company_id==0))
		$("#company_id").val("2");
		setProduct($("#company_id").val());
		getStateList('${supplier.country_id}');
		<c:if test="${supplier.state_id != null}">
	   		$("#stateId").val('${supplier.state_id}');
        </c:if>
		getCityList(document.getElementById("stateId").value);
	    <c:if test="${supplier.city_id != null}">
			$("#cityId").val('${supplier.city_id}');
	    </c:if> 
		var sblist=$("#subNatureList").val();
	  
		if((sblist!=null) && (sblist!="")){
			sblist=JSON.parse(sblist);
			for(var i=0;i<sblist.length;i++){
				document.getElementById(sblist[i].subId+"-subid").checked = true;
				document.getElementById("nature_type_"+sblist[i].subId).value = sblist[i].purpose;
				subList.push({
					"subId" : sblist[i].subId,
					"purpose" : sblist[i].purpose
				});
			}
		}
		var prList= $('#productList').val();
		if((prList!=null) && (prList!="")){
			var arr=prList.split(",");
			for(var i=0;i<arr.length;i++){
				proList.push(arr[i]);
				document.getElementById(arr[i]+"-prdid").checked = true;
			}
		}
	var tds_rate1=document.getElementById("tds_rate1").value;
	if(tds_rate1!=0){
		$("#tds_rate").val(tds_rate1);
	}
	var gst = '<c:out value= "${supplier.gst_applicable}"/>';
	var tds = '<c:out value= "${supplier.tds_applicable}"/>';	
	var cid = '<c:out value= "${supplier.supplier_id}"/>';
	if(cid!=""){
		checktype(1,gst);
		checktype(2,tds);
	}
	});
	function checktype(flag,value){
		if(flag==1){			
			if(value=="true"){				
				document.getElementById('gstcomp').style.display="block";
				document.getElementById('rcmcomp').style.display="block";
			}
			else{ 
				document.getElementById('gstcomp').style.display="none";
				document.getElementById('rcmcomp').style.display="none";
			}
		}
		else{
			if(value==1)
				document.getElementById('tds-div').style.display="block";
			else
				document.getElementById('tds-div').style.display="none";
		}
	}
	
	function addSub(e) {
		var length = subList.length;
		if (e.checked) {
			var purpose = $("#nature_type_" + e.value).val();
			subList.push({
				"subId" : e.value,
				"purpose" : purpose
			});
			console.log(JSON.stringify(subList));
		} else {
			for (i = 0; i < length; i++) {
				if (subList[i].subId == e.value) {
					subList.splice(i, 1);
					break;
				}
			}
			$("#nature_type_" + e.value).val('');
			console.log(JSON.stringify(subList));
		}
	}
	
	function getStateList(countryId){
		var stateArray = [];
		<c:forEach var = "country" items = "${countryList}">
			var id = <c:out value = "${country.country_id}"/>
			if(id == countryId){
				$('#stateId').find('option').remove();
				$('#stateId').append($('<option>', {
				    value: 0,
				    text: 'Select State Name'
				}));
				<c:forEach var = "state" items = "${country.state}">
				if(${state.status}==true){					
					var tempArray = [];
				    tempArray["id"]=${state.state_id};
				    tempArray["name"]='${state.state_name}';
				    stateArray.push(tempArray);					
				}
		</c:forEach>	
		stateArray.sort(function(a, b) {
            var textA = a.name.toUpperCase();
            var textB = b.name.toUpperCase();
            return (textA < textB) ? -1 : (textA > textB) ? 1 : 0;
        });
			for(i=0;i<stateArray.length;i++){
				$('#stateId').append($('<option>', {
			    	value: stateArray[i].id,
			    	text: stateArray[i].name,
				})); 
			}
		}
	   </c:forEach>
	}

	function getCityList(e){	

	    //Testing the implementation
	    var cityArray = [];
		var stateId = e;	
		<c:forEach var = "state" items = "${stateList}">
			var id = <c:out value = "${state.state_id}"/>
			if(id == stateId){
				$('#cityId').find('option').remove();
				$('#cityId').append($('<option>', {
				    value: 0,
				    text: 'Select City Name'
				}));
				
				<c:forEach var = "city" items = "${state.city}">	
				if(${city.status}==true){
				    var tempArray = [];
				    tempArray["id"]=${city.city_id};
				    tempArray["name"]='${city.city_name}';
					cityArray.push(tempArray);
				}
		        </c:forEach>
		        cityArray.sort(function(a, b) {
		            var textA = a.name.toUpperCase();
		            var textB = b.name.toUpperCase();
		            return (textA < textB) ? -1 : (textA > textB) ? 1 : 0;
		        });
					for(i=0;i<cityArray.length;i++)
					{
						 $('#cityId').append($('<option>', {
					    value: cityArray[i].id,
					    text: cityArray[i].name,
					})); 
					}
			}
		</c:forEach>
	    
	}
	function checkall(e,id){
		if($(e).prop('checked')){
        	$('#lg-'+id+' input.ckeck').each(function(){
        		$(this).prop('checked', true);
	        	addSub(this);
	        });
	    }
	 	else{
        	$('#lg-'+id+' input.ckeck').each(function(){
	            $(this).prop('checked', false);
	        	addSub(this);
	        });
	    }
	}
	function checkallproduct(e){
		 if($(e).prop('checked')){
		        $('#productchecks input.ckeck').each(function(){		        	
		            $(this).prop('checked', true);
		            addPro(this);
		        });
		    }else{
		        $('#productchecks input.ckeck').each(function(){
		            $(this).prop('checked', false);
		            addPro(this);

		        });
		    }
	}
	
	function setProduct(compId){
		$("#products").find('div').remove();
		$("#company-subledger").html("");
		<c:forEach var="product" items="${productList}">
			var cId = <c:out value = "${product.company.company_id}" />;
			if(cId == compId){
				var html="<div class='col-md-4 col-sm-6 col-xs-12 sub-list1'><div class='row' id='productchecks'><input class='ckeck' name='32-ck' id='${product.product_id}-prdid' value='${product.product_id}' type='checkbox' onchange='addPro(this)'>${product.product_name}</div></div>";
				console.log(html);
				$("#products").append(html);
			}			
		</c:forEach>
		var total_ledger=0
		var ledgerdata = new Array();
		<c:forEach var="lglist" items="${subLedgerList}">
		var insertval=0;
				var lId = '<c:out value = "${lglist.ledger.company.company_id}" />';				
					if(lId == compId){
						if (ledgerdata.indexOf('${lglist.ledger.ledger_id}') == -1)
						{
							ledgerdata.push('${lglist.ledger.ledger_id}');
						}
					}
			
		</c:forEach>
		
		for(i=0;i<ledgerdata.length;i++)
		{	
			var lprint = 0;	
			var shtml="<div class='col-sm-12 sub-list1' style='background-color: #eee;'>";
			<c:forEach var="subLedger" items="${subLedgerList}">	
			    var sId = '<c:out value = "${subLedger.company.company_id}" />';
				if(sId == compId){		
					if(ledgerdata[i]=='${subLedger.ledger.ledger_id}')
						{  
						var lname="${subLedger.ledger.ledger_name}".replace('\'', '\'');
						var sname="${subLedger.subledger_name}".replace('\'', '\'');	
								if (lprint==0)				
									{
								        shtml+="<p class='col-sm-6 lgr-div'>"+lname+
									"<input name='32-ck' type='checkbox' onchange='checkall(this,${subLedger.ledger.ledger_id})' style='float: left; margin-left: 1%; margin-top: ;'></p>";
								    	lprint=1;
									}
								shtml+="<div class='col-sm-6' style='width: auto; display: inline' id='lg-${subLedger.ledger.ledger_id}'>"+
								"<input name='32-ck' class='ckeck' id='${subLedger.subledger_Id}-subid' value='${subLedger.subledger_Id}' type='checkbox' onchange='addSub(this)'>"+sname+
									"<input class='logInput' style='margin: 1% 5px;' id='nature_type_${subLedger.subledger_Id}'  placeholder='Nature of purpose'/>"+
							   "</div>";							  
						}			
				}			
			  </c:forEach>	
			  shtml+="</div>";
			  $("#company-subledger").append(shtml);
		}		  	 
	}
</script>
<%@include file="/WEB-INF/includes/footer.jsp"%>