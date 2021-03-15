<%@include file="/WEB-INF/includes/header.jsp"%>
<script type="text/javascript" src="${valid}"></script>

    <!-- Tags Input -->
    <link href="resources/css/bootstrap-tagsinput.css" rel="stylesheet">
    <script src="resources/js/bootstrap-tagsinput.js"></script>
<%@page import="com.fasset.controller.abstracts.MyAbstractController"%>
<c:set var="SIZE_12"><%=MyAbstractController.SIZE_12%></c:set>
<c:set var="SIZE_TEN"><%=MyAbstractController.SIZE_TEN%></c:set>
<c:set var="SIZE_SIX"><%=MyAbstractController.SIZE_SIX%></c:set>
<c:set var="SIZE_13"><%=MyAbstractController.SIZE_13%></c:set>
<c:set var="SIZE_15"><%=MyAbstractController.SIZE_15%></c:set>
<c:if test="${successMsg != null}">
	<div class="successMsg" id = "successMsg"> 
		<strong>${successMsg}</strong>
	</div>
</c:if>

<div class="breadcrumb">
	<h3>User Master</h3>					
	<a href="homePage">Home</a> » <a href="#">User Master</a>
</div>	
<div class="col-md-12 wideform">
	<div class="fassetForm">
		<form:form id="userForm" action="userProfile" method="post" enctype="multipart/form-data" commandName = "userform" onsubmit = "return validate();">
			<div class="row">				
				<form:input path="user.user_id" hidden = "hidden"/>
				<form:input path="company.company_id" hidden = "hidden"/>
				<form:input path="company.empLimit" hidden = "hidden"/>
				<form:input path="user.email" hidden = "hidden"/>
				<form:input path="company.yearRange" id="yearRange" hidden = "hidden"/>
			</div>
						<div class="row">	
				<div class="col-md-3 control-label"><label>Company Name<span>*</span></label></div>		
				<div class="col-md-9">
					<form:input path="company.company_name" class="logInput" id = "company_name" placeholder="Company Name" />
					<span class="logError"><form:errors path="company.company_name" /></span>
				</div>	
			</div>
			<div class="row">	
				<div class="col-md-3 control-label"><label>Contact First Name<span>*</span></label></div>		
				<div class="col-md-9">
					<form:input path="user.first_name" class="logInput" id = "first_name" placeholder="First Name" />
					<span class="logError"><form:errors path="user.first_name" /></span>
				</div>	
			</div>
			<div class="row">	
				<div class="col-md-3 control-label"><label>Contact Middle Name</label></div>		
				<div class="col-md-9">
					<form:input path="user.middle_name" class="logInput" id = "middle_name" placeholder="Middle Name" />
					<span class="logError"><form:errors path="user.middle_name" /></span>
				</div>	
			</div>	
			<div class="row">	
				<div class="col-md-3 control-label"><label>Contact Last Name<span>*</span></label></div>		
				<div class="col-md-9">
					<form:input path="user.last_name" class="logInput" id = "last_name" placeholder="Last Name" />
					<span class="logError"><form:errors path="user.last_name" /></span>
				</div>	
			</div>
			<div class="row">	
				<div class="col-md-3 control-label"><label>Primary Mobile Number<span>*</span></label></div>		
				<div class="col-md-9">
					<form:input path="user.mobile_no" class="logInput" id = "mobile_no" minlength="${SIZE_TEN}" maxlength="${SIZE_13}" placeholder="Priary Mobile Number" />
					<span class="logError"><form:errors path="user.mobile_no" /></span>
				</div>	
			</div>	
			<div class="row">	
				<div class="col-md-3 control-label"><label>Landline Number</label></div>		
				<div class="col-md-9">
					<form:input path="company.landline_no" class="logInput" id = "landline_no" placeholder="Landline Number" />
					<span class="logError"><form:errors path="company.landline_no" /></span>
				</div>	
			</div>	
			
			<div class="row">	
				<div class="col-md-3 control-label"><label>Email Address<span>*</span></label></div>		
				<div class="col-md-9">
					<input value="${userform.user.email}" class="logInput" style="width:100%;" disabled="disabled"/>
				</div>	
			</div>	
			
			<div class="row">	
				<div class="col-md-3 control-label"><label>Aadhar Number</label></div>		
				<div class="col-md-9">
					<form:input path="user.adhaar_no" class="logInput" id = "adhaar_no" placeholder="Adhaar Number" maxlength="${SIZE_12}" />
					<span class="logError"><form:errors path="user.adhaar_no" /></span>
				</div>	
			</div>	
			<div class="row">	
				<div class="col-md-3 control-label"><label>Super User PAN Number<span>*</span></label></div>		
				<div class="col-md-9">
					<form:input path="user.pan_no" class="logInput" id = "pan_no" placeholder="PAN Number" maxlength="${SIZE_TEN}"/>
					<span class="logError"><form:errors path="user.pan_no" /></span>
				</div>	
			</div>

			
			
								
			<c:if test="${role == '5'}">

				<div class="row">
					<div class="col-md-3 control-label">
						<label>Financial Year</label>
					</div>
					<div class="col-md-9">
						<c:forEach var="year" items="${yearlist}">
							<ul>
								<li>${year.year_range}</li>
							</ul>
						</c:forEach>

					</div>
				</div>
				
				<div class="row">	
				<div class="col-md-3 control-label"><label>Employee Limit</label></div>		
				<div class="col-md-9">
					<input value="${userform.company.empLimit}" class="logInput" style="width:100%;" disabled="disabled"/>
				</div>	
			</div>
				
				
			</c:if>	
			
			<div class = "row">	
				<div class="col-md-3 control-label"><label>Type of Company</label></div>
				<div class = "col-md-9">										
					<form:select path = "company.companyTypeId" class="logInput">
						<form:option value="0" label="Type of Company" id="companyTypeId"/>		
						<c:forEach var = "companyType" items = "${companyTypeList}">							
							<form:option value = "${companyType.company_statutory_id}">${companyType.company_statutory_name}</form:option>	
						</c:forEach>									
					</form:select>
					<span class="logError"><form:errors path="company.companyTypeId" /></span>
				</div>
			</div>		
			
			
			
			<div class = "row">	
				<div class="col-md-3 control-label"><label>Industry Type</label></div>
				<div class = "col-md-9">										
					<form:select path = "company.industryTypeId" class="logInput">
						<form:option value="0" label="Industry Type"/>		
						<c:forEach var = "industryType" items = "${industryTypeList}">							
							<form:option value = "${industryType.industry_id}">${industryType.industry_name}</form:option>	
						</c:forEach>									
					</form:select>
					<span class="logError"><form:errors path="company.industryTypeId" /></span>
				</div>
			</div>
			
			<div class = "row">	
				<div class="col-md-3 control-label"><label>Country<span>*</span></label></div>
				<div class = "col-md-9">										
					<form:select path = "company.countryId" class="logInput" onChange="getStateList(this.value)">
						<form:option value="0" label="Select Country"/>		
						<c:forEach var = "country" items = "${countryList}">							
							<form:option value = "${country.country_id}">${country.country_name}</form:option>	
						</c:forEach>									
					</form:select>
					<span class="logError"><form:errors path="company.countryId" /></span>
				</div>
			</div>
			
			<div class = "row">	
				<div class="col-md-3 control-label"><label>State<span>*</span></label></div>
				<div class = "col-md-9">										
					<form:select path = "company.stateId" id = "stateId" class="logInput" onChange='getCityList(this.value)'>
						<form:option value="0" label="Select State"/>		
						<%-- <c:forEach var = "state" items = "${stateList}">							
							<form:option value = "${state.state_id}">${state.state_name}</form:option>	
						</c:forEach> --%>									
					</form:select>
					<span class="logError"><form:errors path="company.stateId" /></span>
				</div>
			</div>
			<div class = "row">	
				<div class="col-md-3 control-label"><label>City<span>*</span></label></div>
				<div class = "col-md-9">										
					<form:select path = "company.cityId" id = "cityId" class="logInput">
						<form:option value="0" label="Select City"/>		
						<%-- <c:forEach var = "city" items = "${cityList}">							
							<form:option value = "${city.city_id}">${city.city_name}</form:option>	
						</c:forEach> --%>									
					</form:select>
					<span class="logError"><form:errors path="company.cityId" /></span>
				</div>
			</div>	
			<div class="row">	
				<div class="col-md-3 control-label"><label>Current Address<span>*</span></label></div>		
				<div class="col-md-9">
				<form:textarea path="company.current_address" class="logInput"
								id="current_address" rows="3" placeholder="Current Address"></form:textarea>
					<span class="logError"><form:errors path="company.current_address" /></span>
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
				<div class="col-md-3 control-label"><label>Permanent Address<span>*</span></label></div>		
				<div class="col-md-9">
				<form:textarea path="company.permenant_address" class="logInput"
								id="permenant_address" rows="3" placeholder="Permanent Address"></form:textarea>
				<span class="logError"><form:errors path="company.permenant_address" /></span>
				</div>	
			</div>	
			<div class="row">	
				<div class="col-md-3 control-label"><label>PIN Code<span>*</span></label></div>		
				<div class="col-md-9">
					<form:input path="company.pincode" class="logInput" id = "pincode" maxlength="${SIZE_SIX}" placeholder="PIN Code" />
					<span class="logError"><form:errors path="company.pincode" /></span>
				</div>	
			</div>	
			<div class="row">	
				<div class="col-md-3 control-label"><label>Company PAN Number<span>*</span></label></div>		
				<div class="col-md-9">
					<form:input path="company.pan_no" class="logInput" id = "company_pan_no" placeholder="PAN Number" maxlength="${SIZE_TEN}"/>
					<span class="logError"><form:errors path="company.pan_no" /></span>
				</div>	
			</div>
			<%-- <div class = "row">	
				<div class="col-md-3 control-label"><label>Bank</label></div>
				<div class = "col-md-9">										
					<form:select path = "company.bankId" class="logInput">
						<form:option value="0" label="Select Bank"/>		
						<c:forEach var = "bank" items = "${bankList}">							
							<form:option value = "${bank.bank_id}">${bank.bank_name} - ${bank.account_no}</form:option>	
						</c:forEach>									
					</form:select>
					<span class="logError"><form:errors path="company.bankId" /></span>
				</div>
			</div> --%>
			<div class="row">	
				<div class="col-md-3 control-label"><label>Registration Number</label></div>		
				<div class="col-md-9">
					<form:input path="company.registration_no" class="logInput" id = "registration_no" placeholder="Registration Number" />
					<span class="logError"><form:errors path="company.registration_no" /></span>
				</div>	
			</div>
			<div class = "row">	
				<div class="col-md-3 control-label"><label>Sales Voucher Range<span>*</span></label></div>
				<div class = "col-md-9">					
				<form:input path="company.voucher_range" class="tagsinput form-control" id = "voucher_range" placeholder="Sales Voucher Range" />
	<!-- <input class="tagsinput form-control" type="text" value="Amsterdam,Washington,Sydney,Beijing,Cairo"/>	 -->		                           

					<span class="logError"><form:errors path="company.voucher_range" /></span>
				</div>
			</div>	
			<!--  <div class="row">	
				<div class="col-md-3 control-label"><label>LOGO</label></div>		
				<div class="col-md-9">
					<form:input path="company.logo" class="logInput" id = "logo" placeholder="LOGO" />
					<span class="logError"><form:errors path="company.logo" /></span>
				</div>	
			</div>-->

			<div class="row">	
				<div class="col-md-3 control-label"><label>Nature of Business</label></div>		
				<div class="col-md-9">
					<form:input path="company.business_nature" class="logInput" id = "business_nature" placeholder="Nature of Business" />
					<span class="logError"><form:errors path="company.business_nature" /></span>
				</div>	
			</div>	
			<div class="row">	
				<div class="col-md-3 control-label"><label>GST Number</label></div>		
				<div class="col-md-9">
					<form:input path="company.gst_no" class="logInput" id = "gst_no" placeholder="GST Number" maxlength="${SIZE_15}"  />
					<span class="logError"><form:errors path="company.gst_no" /></span>
				</div>	
			</div>
			<div class="row">	
				<div class="col-md-3 control-label"><label>PTE Number</label></div>		
				<div class="col-md-9">
					<form:input path="company.pte_no" class="logInput" id = "pte_no" placeholder="PTE Number" />
					<span class="logError"><form:errors path="company.pte_no" /></span>
				</div>	
			</div>	
			<div class="row">	
				<div class="col-md-3 control-label"><label>PTR Number</label></div>		
				<div class="col-md-9">
					<form:input path="company.ptr_no" class="logInput" id = "ptr_no" placeholder="PTR Number" />
					<span class="logError"><form:errors path="company.ptr_no" /></span>
				</div>	
			</div>	
			<div class="row">	
				<div class="col-md-3 control-label"><label>Eway Bill Registration Number</label></div>		
				<div class="col-md-9">
					<form:input path="company.eway_bill_no" class="logInput" id = "eway_bill_no" placeholder="Eway Bill Registration Number" />
					<span class="logError"><form:errors path="company.eway_bill_no" /></span>
				</div>	
			</div>	
			<div class="row">	
				<div class="col-md-3 control-label"><label>IEC Number</label></div>		
				<div class="col-md-9">
					<form:input path="company.iec_no" class="logInput" id = "iec_no" placeholder="IEC Number" />
					<span class="logError"><form:errors path="company.iec_no" /></span>
				</div>	
			</div>	
			<div class="row">	
				<div class="col-md-3 control-label"><label>Other Tax 1</label></div>		
				<div class="col-md-9">
					<form:input path="company.other_tax_1" class="logInput" id = "other_tax_1" placeholder="Other Tax 1" />
					<span class="logError"><form:errors path="company.other_tax_1" /></span>
				</div>	
			</div>	
			<div class="row">	
				<div class="col-md-3 control-label"><label>Other Tax 2</label></div>		
				<div class="col-md-9">
					<form:input path="company.other_tax_2" class="logInput" id = "other_tax_2" placeholder="Other Tax 2" />
					<span class="logError"><form:errors path="company.other_tax_2" /></span>
				</div>	
			</div>
			<div class="row">	
				<div class="col-md-3 control-label"><label>Logo</label></div>		
				<div class="col-md-9">
					<input id="logo" class="logInput" name="logo" type="file" accept=".jpg, .png, .jpeg|images/*">
					
				</div>	
			</div>
			
			
					
			<div class="text-center">
				<button class="fassetBtn" type="submit">
			      Update
		        </button>
			</div>
		</form:form>
	</div>
</div>
<script>
$(document).ready(function(){

    $('.tagsinput').tagsinput({
        tagClass: 'label label-primary'
    });
	
	$("#first_name,#middle_name,#last_name,#company_name").keypress(function(e) {
		if (!letters(e)) {
			return false;
		}
	});
	
	$("#mobile_no,#landline_no,#adhaar_no,#pincode").keypress(function(e) {
		if (!digitsOnly(e)) {
			return false;
		}
	});
	
	$("#pan_no,#company_pan_no,#gst_no").keyup(function(e) {
	   $(this).val($(this).val().toUpperCase());
	});
	/*$("#current_address,#permenant_address").keypress(function(e) {
		if (!lettersAndDigitsAndSlashOnly(e)) {
			return false;
		}
	});*/
	//alert("country id : ${userform.company.countryId}");
	//alert("state id : ${userform.company.stateId}");
	//alert("city id : ${userform.company.cityId}");
	
	getStateList('${userform.company.countryId}');

	<c:if test="${userform.company.stateId != null}">
		$("#stateId").val('${userform.company.stateId}');		
		getCityList('${userform.company.stateId}');
	</c:if>

	<c:if test="${userform.company.cityId != null}">	
		$("#cityId").val('${userform.company.cityId}');		
	</c:if> 
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
function validate(){
		
	$("#yearRange").val(1);  
	var logo = document.getElementById("logo").value;
	var validFileExtensions = [".png",".jpg",".jpeg"];	
	
	if(logo != null && logo != ""){
		var blnValid = false;
	    for (var j = 0; j < validFileExtensions.length; j++) {
	        var sCurExtension = validFileExtensions[j];
	        if (logo.substr(logo.length - sCurExtension.length, sCurExtension.length).toLowerCase() == sCurExtension.toLowerCase()) {
	            blnValid = true;
	            break;
	        }
	    }
	    
	    if (!blnValid) {
	        alert("File format should be " + validFileExtensions.join(","));
	        $("#logo").val('');
	        return false;
	    }

		//alert("file size :"+fileSize);
	    /* if(fileSize > 1024){
   			alert("Please Select a file below 1GB");
   			return false;
   		} */
	}
	
	return true;
}

function getStateList(countryId){
	/* $('#stateId').find('option').remove();
	$('#cityId').find('option').remove();
	$('#cityId').append($('<option>', {
	    value: 0,
	    text: 'Select City'
	})); */
	var stateArray = [];
	<c:forEach var = "country" items = "${countryList}">
		var id = <c:out value = "${country.country_id}"/>
		if(id == countryId){
			//alert("matched country id : "+id);
			$('#stateId').find('option').remove();
			$('#stateId').append($('<option>', {
			    value: 0,
			    text: 'Select State'
			}));
			<c:forEach var = "state" items = "${country.state}">
				//alert('${state.state_name}');
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
	$('#cityId').find('option').remove();
    var cityArray = [];
	var stateId = e;	
	<c:forEach var = "state" items = "${stateList}">
		var id = <c:out value = "${state.state_id}"/>
		if(id == stateId){
			$('#cityId').append($('<option>', {
			    value: 0,
			    text: 'Select City'
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
</script>
<%@include file="/WEB-INF/includes/footer.jsp" %>