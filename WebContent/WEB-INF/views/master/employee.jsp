<%@include file="/WEB-INF/includes/header.jsp"%>
<script type="text/javascript" src="${valid}"></script>
<%@page import="com.fasset.controller.abstracts.MyAbstractController"%>
<c:set var="SIZE_12"><%=MyAbstractController.SIZE_12%></c:set>
<c:set var="SIZE_TEN"><%=MyAbstractController.SIZE_TEN%></c:set>
<c:set var="SIZE_SIX"><%=MyAbstractController.SIZE_SIX%></c:set>
<c:set var="SIZE_TEN"><%=MyAbstractController.SIZE_TEN%></c:set>
<c:set var="SIZE_13"><%=MyAbstractController.SIZE_13%></c:set>
<div class="breadcrumb">	
	<h3>Employee</h3>
	<a href="homePage">Home</a> » <a href="employeeList">Employee</a> » <a href="#">Create Employee</a>
</div>	
<c:if test="${errorMsg != null}">
		<div class="errorMsg" id = "errorMsg"> 
			<strong>${errorMsg}</strong>
		</div>
	</c:if>
<div class="col-md-12 wideform">
	<div class="fassetForm">
		<form:form id="employeeForm" action="saveEmployee" method="post" commandName = "employee">
			<div class="row">				
				<form:input path="user_id" hidden = "hidden"/>
				<c:if test="${employee.user_id != null}">
				<form:input path="password" hidden = "hidden"/>
				</c:if>	
			</div>
			
		<c:if test="${role == '2'}">
			
			                 <div class="row">			
								<div class="col-md-3 control-label"><label>Company Name<span>*</span></label></div>
								<div class = "col-md-9">
									<form:select path="company_id" id="company_id" class="logInput">
										<form:option value="0" label="Select Company Name"/>	
										<c:forEach var = "company" items = "${companyList}">													
											<form:option value = "${company.company_id}">${company.company_name}</form:option>	
										</c:forEach>
									</form:select>
									<span class="logError"><form:errors path="company_id" /></span>
								</div>
							</div>
			    </c:if>
			
			
			<c:choose>
				<c:when test="${acount==false}">
				<div class = "row">	
				<div class="col-md-3 control-label"><label>Role<span>*</span></label></div>
				<div class = "col-md-9">										
					<form:select path="role_id" id = "role_id" class="logInput" onChange="setManager(this.value)">
						<form:option value="0" label="Select Role"/>		
						<c:forEach var = "role" items = "${rolelist}">							
							<form:option value = "${role.role_id}">${role.role_name}</form:option>	
						</c:forEach>									
					</form:select>
				 <span class="logError"><form:errors path="role_id" /></span>
				</div>
			</div>
				</c:when>
				<c:otherwise>
				      <form:input path="role_id" type = "hidden" />
				</c:otherwise>
			</c:choose>
			
			
		
			
			<div class="row">	
				<div class="col-md-3 control-label"><label>First Name<span>*</span></label></div>		
				<div class="col-md-9">
					<form:input path="first_name" class="logInput" id = "first_name" placeholder="First Name" maxlength="20" pattern='[A-Za-z\\s]*'/>
					<span class="logError"><form:errors path="first_name" /></span>
				</div>	
			</div>
			<div class="row">	
				<div class="col-md-3 control-label"><label>Middle Name</label></div>		
				<div class="col-md-9">
					<form:input path="middle_name" class="logInput" id = "middle_name" placeholder="Middle Name" maxlength="20" pattern='[A-Za-z\\s]*'/>
					<span class="logError"><form:errors path="middle_name" /></span>
				</div>	
			</div>	
			<div class="row">	
				<div class="col-md-3 control-label"><label>Last Name<span>*</span></label></div>		
				<div class="col-md-9">
					<form:input path="last_name" class="logInput" id = "last_name" placeholder="Last Name" maxlength="20" pattern='[A-Za-z\\s]*'/>
					<span class="logError"><form:errors path="last_name" /></span>
				</div>	
			</div>
			<c:if test="${employee.user_id == null}">
			<div class="row">	
				<div class="col-md-3 control-label"><label>Password<span>*</span></label></div>		
				<div class="col-md-9">
					<div class="input-group m-b-15 passwd">
	                    <div class="bootstrap-timepicker">
						<form:password path="password" class="logInput" maxlength="${SIZE_TEN}" id = "password" placeholder="Password" />
						<span class="logError"><form:errors path="password" /></span>
						</div>
					   <span class="input-group-addon">		
                                	 <i class="fa fa-eye" onmouseover="mouseoverPass(1);" onmouseout="mouseoutPass(1);" ></i>
						</span>
					</div>
								
				</div>	
			</div>
			</c:if>	
			<div class="row">	
				<div class="col-md-3 control-label"><label>Mobile Number<span>*</span></label></div>		
				<div class="col-md-9">
					<form:input path="mobile_no" class="logInput" id = "mobile_no" minlength="${SIZE_TEN}" maxlength="${SIZE_13}" placeholder="Mobile Number" />
					<span class="logError"><form:errors path="mobile_no" /></span>
				</div>	
			</div>

			<div class="row">
				<div class="col-md-3 control-label">
					<label>Landline No</label>
				</div>
				<div class="col-md-9">
					<form:input path="landline_no" class="logInput" id="landline_no"
						maxlength="${SIZE_12}" placeholder="Landline Number" />
					<span class="logError"><form:errors path="landline_no" /></span>
				</div>
			</div>

			<div class="row">	
				<div class="col-md-3 control-label"><label>Email Address<span>*</span></label></div>		
				<div class="col-md-9">
					<c:if test="${employee.user_id != null}">
						<form:input path="email" class="logInput" id = "email" placeholder="Email Address" readonly="true"/>
					</c:if>
					<c:if test="${employee.user_id == null}">
						<form:input path="email" class="logInput" id = "email" placeholder="Email Address" />
					</c:if>
					<span class="logError"><form:errors path="email" /></span>
				</div>	
			</div>	
			
			<div class="row">
				<div class="col-md-3 control-label">
					<label>Date of Joining<span id="doj"> *</span></label>
				</div>
				<div class="col-md-9">
					<input type="text" style="color: black;" id="joinDate"
						name="joinDate" placeholder="Date of Joining" autocomplete="off"  onchange="setDate(this)" >
					<span class="logError"><form:errors path="joinDate" /></span>
				</div>
			</div>
			
			<div class="row">	
				<div class="col-md-3 control-label"><label>Aadhar Number<span id="adhar">*</span></label></div>		
				<div class="col-md-9">
					<form:input path="adhaar_no" class="logInput" id = "adhaar_no" placeholder="Aadhar Number" maxlength="${SIZE_12}" />
					<span class="logError"><form:errors path="adhaar_no" /></span>
				</div>	
			</div>	
			<div class="row">	
				<div class="col-md-3 control-label"><label>PAN Number<span>*</span></label></div>		
				<div class="col-md-9">
					<form:input path="pan_no" class="logInput" id = "pan_no" placeholder="PAN Number" maxlength="${SIZE_TEN}"/>
					<span class="logError"><form:errors path="pan_no" /></span>
				</div>	
			</div>
			
			<div class="row">
						<div class="col-md-3 control-label">
							<label>Current Address<span>*</span></label>
						</div>
						<div class="col-md-9">
							<form:textarea path="current_address" class="logInput"
								id="current_address" rows="3" placeholder="Current Address" maxlength="200"></form:textarea>
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
					<label>Permanent Address<span>*</span></label>
				</div>
				<div class="col-md-9">
					<form:textarea path="permenant_address" class="logInput"
						id="permenant_address" rows="3" placeholder="Permanent Address" maxlength="200"></form:textarea>
					<span class="logError"><form:errors path="permenant_address" /></span>
				</div>
			</div>
			
			<div class="row">
				<div class="col-md-3 control-label">
					<label>Pin Code<span>*</span></label>
				</div>
				<div class="col-md-9">
					<form:input path="pin_code" class="logInput" id="pan_no"
						placeholder="Pin Code" maxlength="6" />
					<span class="logError"><form:errors path="pin_code" /></span>
				</div>
			</div>
			
			<div class="row">
				<div class="col-md-3 control-label">
					<label>Country Name<span>*</span></label>
				</div>
				<div class="col-md-9">

					<form:select path="country_id" class="logInput"
						placeholder="Country Name" onChange="getStateList(this.value)">
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
					<label>State Name<span>*</span></label>
				</div>
				<div class="col-md-9">
					<form:select path="state_id" class="logInput"
						placeholder="State Name" id="stateId"
						onChange='getCityList(this.value)'>
						<form:option value="0" label="Select State Name" />
					</form:select>
					<span class="logError"><form:errors path="state_id" /></span>
				</div>
			</div>

			<div class="row">
				<div class="col-md-3 control-label">
					<label>City Name<span>*</span></label>
				</div>
				<div class="col-md-9">

					<form:select path="city_id" class="logInput"
						placeholder="City Name" id="cityId">
						<form:option value="0" label="Select City Name" />
					</form:select>

					<%-- <form:select path="city_id" class="logInput" id="cityId">
								<form:option value="0" label="Select City Name" />
								<c:forEach var="clist" items="${cityList}">
									<form:option value="${clist.city_id}">${clist.city_name}</form:option>
								</c:forEach>
							</form:select> --%>

					<span class="logError"><form:errors path="city_id" /></span>
				</div>
			</div>
			
			<%-- <c:if test="${role == '5'}"> --%>	
			<div class="row">
						<div class="col-md-3 control-label">
							<label>Status<span>*</span></label>
						</div>
						<div class="col-md-9">
							<form:radiobutton path="status" value="true" checked="checked" />
							Join
							<form:radiobutton path="status" value="false" />
							Left
						</div>
					</div>
            <%-- </c:if> --%>		 
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
<script>

$(function() {
	
	
	$( "#joinDate" ).datepicker();
	//$( "#toDate" ).datepicker();
	
	
	$("#first_name,#middle_name,#last_name").keypress(function(e) {
		if (!letters(e)) {
			return false;
		}
	});
	
	$("#mobile_no,#adhaar_no,#landline_no").keypress(function(e) {
		if (!digitsOnly(e)) {
			return false;
		}
	});
	
	getStateList('${employee.country_id}');
	<c:if test="${employee.country_id != null}">
   $("#stateId").val('${employee.state_id}');
    </c:if>
	getCityList(document.getElementById("stateId").value);
    <c:if test="${employee.city_id != null}">
	$("#cityId").val('${employee.city_id}');
    </c:if>
});

if (document.getElementById("role_id").selectedIndex==1){
	
	document.getElementById("doj").textContent="";
	document.getElementById("adhar").textContent="";
	
	
}else{
	
	document.getElementById("doj").textContent="*";
	document.getElementById("adhar").textContent="*";
}
$( document ).ready(function() {
	var join_date = '<c:out value= "${employee.joinDate}"/>';		
	if(join_date!="")
	{
	 	newdate = formatDate(join_date);	
		$("#joinDate").datepicker("setDate", newdate);
		$("#joinDate").datepicker("refresh");
	}	
});	

function setDate(e){
	document.getElementById("joinDate").value = e.value;
	// date format validation
	var datevali = document.getElementById("joinDate").value;
if(isValidDate(datevali)==true){
	 
		 return true;
 }else{
	alert("Invalid Date");
	window.location.reload();
	
}  
}

function isValidDate(dateStr) {
	
	
	 // Checks for the following valid date formats:
	 // MM/DD/YYYY
	 // Also separates date into month, day, and year variables
	 //var datePat = /^(\d{2,2})(-)(\d{2,2})\2(\d{4}|\d{4})$/;
	 
	 //var datePat= /^\d{2}\/\d{2}\/\d{4}$/;
	 
	 var datePat= /^((0?[1-9]|1[012])[- /.](0?[1-9]|[12][0-9]|3[01])[- /.](19|20)?[0-9]{2})*$/;
	 
	 var matchArray = dateStr.match(datePat); // is the format ok?
			 
			//alert(matchArray);
			 
	 if (matchArray == null) {
	  //alert("Date must be in MM-DD-YYYY format")
	  return false;
	 }
	 
	 month = matchArray[2];
	 day   = matchArray[3];
	 
	 
	 if (month < 1 || month > 12) { // check month range
		  alert("Month must be between 1 and 12");
		  return false;
		 }
	 
	 if (day < 1 || day > 31) {
		  alert("Day must be between 1 and 31");
		  return false;
		 }
	
	 return true;  // date is valid
	}
function formatDate(date) {
    var d = new Date(date),
        month = '' + (d.getMonth() + 1),
        day = '' + d.getDate(),
        year = d.getFullYear();
    if (month.length < 2) month = '0' + month;
    if (day.length < 2) day = '0' + day;

    return [month,day,year].join('/');
}
function check(cb) {
	if ($(cb).is(":checked")) {
		$("#permenant_address").val($("#current_address").val());
		$('#permenant_address').attr('readonly', true);
	} else {
		$("#permenant_address").val("");
		$('#permenant_address').attr('readonly', false);
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
			if(${state.status}==true)
			{			
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
			for(i=0;i<stateArray.length;i++)
			{
				 $('#stateId').append($('<option>', {
			    value: stateArray[i].id,
			    text: stateArray[i].name,
			})); 
			}
		}
   </c:forEach>
}

function getCityList(e){	

    // Testing the implementation
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
			if(${city.status}==true)
			{
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
function setManager(obj){
	
	if (obj==1){
		
		document.getElementById("doj").textContent="";
		document.getElementById("adhar").textContent="";
		
		
	}else{
		
		document.getElementById("doj").textContent="*";
		document.getElementById("adhar").textContent="*";
	}
}
function cancel(){
	window.location.assign('<c:url value = "employeeList"/>');	
}
function mouseoverPass(obj) {
	  var obj = document.getElementById('password');
	  obj.type = "text";
	}
	function mouseoutPass(obj) {
	  var obj = document.getElementById('password');
	  obj.type = "password";
	}
</script>


<%@include file="/WEB-INF/includes/footer.jsp" %>