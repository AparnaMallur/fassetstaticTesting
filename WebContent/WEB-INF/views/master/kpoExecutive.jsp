<%@include file="/WEB-INF/includes/header.jsp"%>
<%@page import="com.fasset.controller.abstracts.MyAbstractController"%>
<script type="text/javascript" src="${valid}"></script>
<spring:url value="/resources/images/edit.png" var="editImg" />
<spring:url value="/resources/images/view.png" var="viewImg" />
<spring:url value="/resources/images/delete.png" var="deleteImg" />
<c:set var="SIZE_TEN"><%=MyAbstractController.SIZE_TEN%></c:set>
<c:set var="SIZE_12"><%=MyAbstractController.SIZE_12%></c:set>
<c:set var="SIZE_13"><%=MyAbstractController.SIZE_13%></c:set>
<c:set var="ROLE_MANAGER"><%=MyAbstractController.ROLE_MANAGER%></c:set>
<c:set var="ROLE_EXECUTIVE"><%=MyAbstractController.ROLE_EXECUTIVE%></c:set>

<c:if test="${successMsg != null}">
	<div class="successMsg" id = "successMsg"> 
		<strong>${successMsg}</strong>
	</div>
</c:if>
<div class="breadcrumb">
	<h3>KPO Executive</h3>					
	<a href="homePage">Home</a> » <a href="KPOExecutiveList">KPO Executive & Manager</a> » <a href="#">Create</a>
</div>	

<div class="col-md-12 wideform">
	<div class="fassetForm">
		<form:form id="KPOExecutiveForm" action="saveKPOExecutive" method="post" commandName ="userform">
			<div class="row">				
				<form:input path="user_id" hidden = "hidden"/>
				<form:input path="created_date" hidden = "hidden"/>
			</div>
			<div class="row">	
				<div class="col-md-3 control-label"><label>First Name<span>*</span></label></div>		
				<div class="col-md-9">
					<form:input path="first_name" class="logInput" id = "first_name" placeholder="First Name" />
					<span class="logError"><form:errors path="first_name" /></span>
				</div>	
			</div>
			<div class="row">	
				<div class="col-md-3 control-label"><label>Middle Name</label></div>		
				<div class="col-md-9">
					<form:input path="middle_name" class="logInput" id = "middle_name" placeholder="Middle Name" />
					<span class="logError"><form:errors path="middle_name" /></span>
				</div>	
			</div>	
			<div class="row">	
				<div class="col-md-3 control-label"><label>Last Name<span>*</span></label></div>		
				<div class="col-md-9">
					<form:input path="last_name" class="logInput" id = "last_name" placeholder="Last Name" />
					<span class="logError"><form:errors path="last_name" /></span>
				</div>	
			</div>
			<c:if test="${userform.user_id == null}">
			<div class="row">	
				<div class="col-md-3 control-label"><label>Password<span>*</span></label></div>		
				<div class="col-md-9">
				<div class="input-group m-b-15 passwd">
	                <div class="bootstrap-timepicker">
					<form:password path="password" class="logInput"  id = "password" placeholder="Password" />
					<span class="logError"><form:errors path="password" /></span>
					</div>
				   		<span class="input-group-addon">		
							<i class="fa fa-eye" onmouseover="mouseoverPass(1);" onmouseout="mouseoutPass(1);" ></i>
						</span>
					</div>
				</div>	
			</div>
			</c:if>	
			<div class = "row">	
				<div class="col-md-3 control-label"><label>Role<span>*</span></label></div>
				<div class = "col-md-9">										
					<form:select path="role_id" id = "role_id" class="logInput" onChange="setManager(this.value)">
						<form:option value="0" label="Select Role"/>		
						<c:forEach var = "role" items = "${roleforsuper}">							
							<form:option value = "${role.role_id}">${role.role_name}</form:option>	
						</c:forEach>									
					</form:select>
				 <span class="logError"><form:errors path="role_id" /></span>
				</div>
			</div>
			<div class = "row">	
				<div class="col-md-3 control-label"><label>Manager<span>*</span></label></div>
				<div class = "col-md-9">										
					<form:select path="manager" id = "manager" class="logInput">
						<form:option value="0" label="Select Manager"/>
					</form:select>
				 <span class="logError"><form:errors path="manager" /></span>
				</div>
			</div>
			<div class="row">	
				<div class="col-md-3 control-label"><label>Mobile Number<span>*</span></label></div>		
				<div class="col-md-9">
					<form:input path="mobile_no" class="logInput" id = "mobile_no" minlength="${SIZE_TEN}" maxlength="${SIZE_13}" placeholder="Primary Mobile Number" />
					<span class="logError"><form:errors path="mobile_no" /></span>
				</div>	
			</div>	
			<div class="row">	
				<div class="col-md-3 control-label"><label>Email ID<span>*</span></label></div>		
				<div class="col-md-9">
					<form:input path="email" class="logInput" id = "email" placeholder="Email Id" />
					<span class="logError"><form:errors path="email" /></span>
				</div>	
			</div>	
			<div class="row">	
				<div class="col-md-3 control-label"><label>Aadhar Card Number<span>*</span></label></div>		
				<div class="col-md-9">
					<form:input path="adhaar_no" class="logInput" id = "adhaar_no" maxlength="${SIZE_12}" placeholder="Adhaar Card Number" />
					<span class="logError"><form:errors path="adhaar_no" /></span>
				</div>	
			</div>	
			<div class="row">	
				<div class="col-md-3 control-label"><label>PAN Number<span>*</span></label></div>		
				<div class="col-md-9">
					<form:input path="pan_no" class="logInput" id = "pan_no" maxlength="${SIZE_TEN}" placeholder="PAN Number" />
					<span class="logError"><form:errors path="pan_no" /></span>
				</div>	
			</div>
			  <div class="row">
						<div class="col-md-3 control-label">
							<label>Country Name<span>*</span></label>
						</div>
						<div class="col-md-9">
							<form:select path="country_id"  id = "country_id" class="logInput"
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
								placeholder="State Name" id="stateId"  onChange="getCityList(this.value)">
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
						<span class="logError"><form:errors path="city_id" /></span>
						</div>
					</div>
			<c:if test="${role == '2'}">	
			<div class = "row">					
				<div class="col-md-3 control-label"><label>Status<span>*</span></label></div>
				<div class = "col-md-9">					
					<form:radiobutton path="status" value="true" checked="checked" />Join 
					<form:radiobutton path="status" value="false" />Left
				</div>				
			</div>	
			</c:if>				
			<div class="row text-center-btn">
				<button class="fassetBtn" type="submit" >
					<spring:message code="btn_save" />
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

	    $("#first_name").keypress(function(e) {
			if (!lettersAndHyphenOnly(e)) {
				return false;
			}
		});
		
		$("#mobile_no,#adhaar_no").keypress(function(e) {
			if (!digitsOnly(e)) {
				return false;
			}
		});
		
		$("#pan_no").keypress(function(e) {
			if (!lettersAndDigitsAndSlashOnly(e)) {
				return false;
			}
		});	
		<c:if test="${userform.manager != null}">
			setManager(document.getElementById("role_id").value);
			$("#manager").val('${userform.manager}');
	 	</c:if>

     	<c:if test="${userform.state_id != null}">
     		getStateList(document.getElementById("country_id").value);
			$("#stateId").val('${userform.state_id}');
   	 	</c:if>  

     	<c:if test="${userform.city_id != null}">
			getCityList(document.getElementById("stateId").value);
			$("#cityId").val('${userform.city_id}');
   	 	</c:if> 
	
	});

	function cancel(){
		window.location.assign('<c:url value = "KPOExecutiveList"/>');	
	}
	
	function deleteKPOExecutive(id){
		window.location.assign('<c:url value="deleteKPOExecutive"/>?id='+id);
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
					/* console.log("id :  ${state.state_id}   name : ${state.state_name}");
					$('#stateId').append($('<option>', {
					    value: '${state.state_id}',
					    text: '${state.state_name}' */
					    
					var tempArray = [];
				    tempArray["id"]=${state.state_id};
				    tempArray["name"]='${state.state_name} - ${state.state_code}';
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
			    var tempArray = [];
			    tempArray["id"]=${city.city_id};
			    tempArray["name"]='${city.city_name}';
				cityArray.push(tempArray);
					
		        </c:forEach>
		        cityArray.sort(function(a, b) {
		            var textA = a.name.toUpperCase();
		            var textB = b.name.toUpperCase();
		            return (textA < textB) ? -1 : (textA > textB) ? 1 : 0;
		        });
				for(i=0;i<cityArray.length;i++) {
			 		$('#cityId').append($('<option>', {
				    	value: cityArray[i].id,
				    	text: cityArray[i].name,
					})); 
				}
			}
		</c:forEach>
	    
	}
	
	function mouseoverPass(obj) {
	  	var obj = document.getElementById('password');
  		obj.type = "text";
	}
	
	function mouseoutPass(obj) {
	  	var obj = document.getElementById('password');
	  	obj.type = "password";
	}
	
	function setManager(roleId){
		$('#manager').find('option').remove();
		$('#manager').append($('<option>', {
		    value: 0,
		    text: 'Select Manager'
		}));
		if(roleId == 3){
			<c:forEach var = "managr" items = "${managerList}">
				$('#manager').append($('<option>', {
				    value: '${managr.user_id}',
				    text: '${managr.first_name}'
				}));
			</c:forEach>	
		}
		
		if(roleId == 4){
			<c:forEach var = "admin" items = "${adminList}">
				$('#manager').append($('<option>', {
				    value: '${admin.user_id}',
				    text: '${admin.first_name}'
				}));
			</c:forEach>	
		}
	}
	
</script>
<%@include file="/WEB-INF/includes/footer.jsp" %>