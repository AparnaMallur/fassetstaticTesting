<%@include file="/WEB-INF/includes/header.jsp"%>
<%@page import="com.fasset.controller.abstracts.MyAbstractController"%>
<script type="text/javascript" src="${valid}"></script>
<c:set var="SIZE_EIGHTEEN"><%=MyAbstractController.SIZE_EIGHTEEN%></c:set>
<c:set var="SIZE_THIRTY"><%=MyAbstractController.SIZE_THIRTY%></c:set>
<c:set var="SIZE_TEN"><%=MyAbstractController.SIZE_TEN%></c:set>
<c:set var="SIZE_SIX"><%=MyAbstractController.SIZE_SIX%></c:set>
<c:set var="SIZE_ELEVEN"><%=MyAbstractController.SIZE_ELEVEN%></c:set>

<c:if test="${successMsg != null}">
	<div class="successMsg" id = "successMsg"> 
		<strong>${successMsg}</strong>
	</div>
</c:if>
<div class="breadcrumb">	
	<h3>Bank</h3>
	<a href="homePage">Home</a> » <a href="bankList">Bank</a> » <a href="#">Create Bank</a>
</div>
<div class="col-md-12 wideform">	
	<div class="fassetForm">
		<form:form id="bankForm" action="bank" method="post" commandName = "bank">
			<div class="row">
				<form:input path="bank_id" hidden = "hidden"/>
			</div>
			<c:if test="${((role == '2') || (role == '3') ||(role == '4'))}">
			<div class="row">			
				<div class="col-md-3 control-label"><label>Company Name<span>*</span></label></div>
				<div class = "col-md-9">
					<form:select path="company_id" class="logInput">
						<form:option value="0" label="Select Company Name"/>	
						<c:forEach var = "company" items = "${companyList}">													
							<form:option value = "${company.company_id}">${company.company_name}</form:option>	
						</c:forEach>
					</form:select>
					<span class="logError"><form:errors path="company_id" /></span>
				</div>
			</div>
			</c:if>		
			
			<div class="row">
				<div class="col-md-3 control-label"><label>Bank<span>*</span></label></div>
				<div class="col-md-9" id="bank-DD">	
				<c:choose>
						<c:when test='${bank.bank_id==Null}'>
					<form:select path="bank_name" class="logInput" onChange="checkbank(this.value)" id="bank_name">
						<form:option value="NONE" label="Select Bank"/>
								<form:option value="Allahabad Bank" label="Allahabad Bank" />
								<form:option value="Axis Bank" label="Axis Bank" />
								<form:option value="Andhra Bank" label="Andhra Bank" />
								<form:option value="Bank of Baroda" label="Bank of Baroda" />
								<form:option value="Bank Of Maharashtra" label="Bank of Maharashtra" />
								<form:option value="Canara Bank" label="Canara Bank" />
								<form:option value="Central Bank of India" label="Central Bank of India" />
								<form:option value="Corporation Bank" label="Corporation Bank" />
								<form:option value="City Union Bank" label="city Union Bank" />
								<form:option value="Cyndicate Bank" label="Cyndicate Bank" />
								<form:option value="Dena Bank" label="Dena Bank" />
								<form:option value="Federal Bank" label="Federal Bank" />
								<form:option value="HDFC Bank" label="HDFC Bank" />
								<form:option value="ICICI Bank" label="ICICI Bank" />
								<form:option value="IDFC Bank" label="IDFC Bank" />
								<form:option value="Indusland Bank" label="Indusland Bank" />
								<form:option value="Jammu and Kashmir Bank" label="Jammu and Kashmir bank" />
								<form:option value="Karur visv Bank" label="Karur visv Bank" />
								<form:option value="Kotak Mahindra Bank" label="Kotak Mahindra Bank" />
								<form:option value="Laxmi Vilas Bank" label="Laxmi Vilas Bank" />
								<form:option value="Nainital Bank" label="Nainital Bank " />
								<form:option value="Oriented Bank of Commerce" label="Oriented Bank of Commerce" />
								<form:option value="Punjab National Bank" label="Punjab National Bank" />
								<form:option value="Punjab and sindh bank" label="Punjab and sindh bank" />
								<form:option value="RBL Bank" label="RBL Bank" />
								<form:option value="Reserve Bank of India" label="Reserve Bank of India" />
								<form:option value="SBI LIFE INSURANCE" label="SBI LIFE INSURANCE" />
								<form:option value="State Bank of India" label="State Bank of India" />
								<form:option value="State Bank of Hyderabad" label="State Bank of Hyderabad" />
								<form:option value="State Bank of Patiala" label="State Bank of Patiala" />
								<form:option value="South Indian Bank" label="South Indian Bank" />
								<form:option value="Tamilnad Mercantile Bank" label="Tamilnad Mercantile Bank" />
								<form:option value="Union Bank of India" label="Union bank of india" />
								<form:option value="UCO Bank" label="UCO Bank" />
								<form:option value="Vijaya Bank" label="Vijaya Bank" />
								<form:option value="OTH" label="Other" />
							</form:select> 
					</c:when>
					<c:otherwise>
						<form:input path="bank_name" id="bank_name" class="logInput" placeholder="Bank Name" />
					</c:otherwise>
					</c:choose>
					<span class="logError"><form:errors path="bank_name" /></span>
				</div>
				<div class="col-md-9" id="bank-input" style="display:none">	
					<form:input path="other_bank_name" id="other_bank_name" class="logInput" placeholder="Bank Name" />
					<span class="logError"><form:errors path="other_bank_name" /></span>				
				</div> 
			</div>
			<div class="row">
				<div class="col-md-3 control-label"><label>Branch Name<span>*</span></label></div>
				<div class="col-md-9">
					<form:input path="branch" class="logInput" id = "branch" placeholder="Branch Name" maxlength="30"/>
					<span class="logError"><form:errors path="branch" /></span>
				</div>
			</div>
			<div class="row">
				<div class="col-md-3 control-label"><label>Account Number<span>*</span></label></div>
				<div class="col-md-9">
					<form:input path="account_no" class="logInput" id = "account_no" placeholder="Account Number"  maxlength="20" />
					<span class="logError"><form:errors path="account_no" /></span>
				</div>
			</div>
			<div class="row">
				<div class="col-md-3 control-label"><label>IFSC Code<span>*</span></label></div>
				<div class="col-md-9">
					<form:input path="ifsc_no" class="logInput" id = "ifsc_no" placeholder="IFSC Code" maxlength="${SIZE_ELEVEN}"/>
					<span class="logError"><form:errors path="ifsc_no" /></span>
				</div>
			</div>
			<div class="row">
				<div class="col-md-3 control-label"><label>Account Sub Group<span>*</span></label></div>
				<div class="col-md-9">	
					<form:select path="subGroupId" class="logInput">
						<form:option value="0" label="Select Account Sub Group"/>						
						<c:forEach var = "accountSubGroup" items = "${accountSubGroupList}">							
							<form:option value = "${accountSubGroup.subgroup_Id}">${accountSubGroup.subgroup_name}</form:option>	
						</c:forEach>									
					</form:select>
					<span class="logError"><form:errors path="subGroupId" /></span>
				</div>
			</div>
			<div class="row">
				<div class="col-md-3 control-label"><label>Status<span>*</span></label></div>		
				<div class="col-md-9">	
					<form:radiobutton path="status" value="true" checked="checked" />Enable 
					<form:radiobutton path="status" value="false" />Disable
				</div>
			</div>
			<div class="row text-center-btn">
				<button class="fassetBtn" type="submit" >
					<spring:message code="btn_save" />
				</button>
				<button class="fassetBtn" type="button" onclick = "cancel()">
					<spring:message code="btn_cancel" />
				</button>
			</div>
		</form:form>
	</div>
</div>
<script type="text/javascript">
	$(function() {		
	    setTimeout(function() {
	        $("#successMsg").hide()
	    }, 3000);

		$("#branch").keypress(function(e) {
			if (!lettersAndHyphenOnly(e)) {
				return false;
			}
		});
		$("#account_no").keypress(function(e) {
			if (!digitsOnly(e)) {
				return false;
			}
		});
		$("#ifsc_no").keypress(function(e) {
			if (!lettersAndDigitsAndSlashOnly(e)) {
				return false;
			}
		});
	});
	function cancel(){
		window.location.assign('<c:url value = "bankList"/>');	
	}
	function checkbank(bank)
	{
		if(bank=="NONE")
			{
			alert("Please Select Bank Name");
			return false;
			}
		else if(bank=='OTH')
			{
				document.getElementById('bank-DD').style.display="none";
				document.getElementById('bank-input').style.display="block";
			}
		else
			{
			document.getElementById('bank-DD').style.display="block";
			document.getElementById('bank-input').style.display="none";
			}
	} 
	$( document ).ready(function() {
		var bank=document.getElementById("bank_name").value;		
		var id='<c:out value= "${bank.bank_id}"/>';
		if(bank=='OTH')
		{
			document.getElementById('bank-DD').style.display="none";
			document.getElementById('bank-input').style.display="block";
		}
	else
		{
		document.getElementById('bank-DD').style.display="block";
		document.getElementById('bank-input').style.display="none";
		}
	});
</script>
<%@include file="/WEB-INF/includes/footer.jsp" %>