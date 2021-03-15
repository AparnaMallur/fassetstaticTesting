<%@include file="/WEB-INF/includes/header.jsp"%>
<%@page import="com.fasset.controller.abstracts.MyAbstractController"%>
<script type="text/javascript" src="${valid}"></script>
<c:set var="SIZE_EIGHTEEN"><%=MyAbstractController.SIZE_EIGHTEEN%></c:set>
<c:set var="SIZE_THIRTY"><%=MyAbstractController.SIZE_THIRTY%></c:set>
<c:set var="SIZE_TEN"><%=MyAbstractController.SIZE_TEN%></c:set>
<c:set var="SIZE_SIX"><%=MyAbstractController.SIZE_SIX%></c:set>

<c:if test="${successMsg != null}">
	<div class="successMsg" id="successMsg">
		<strong>${successMsg}</strong>
	</div>
</c:if>
<div class="breadcrumb">
	<h3>City</h3>
	<a href="homePage">Home</a> » <a href="cityList">City</a> » <a href="#">Create</a>
</div>
<div class="col-md-12 wideform">
	<div class="fassetForm">
		<form:form id="cityForm" action="savecity" method="post"
			commandName="city">
			<div class="row">
				<form:input path="city_id" hidden="hidden" />
				<form:input path="created_date" hidden="hidden" />
			</div>
			<div class="row">
				<div class="col-md-2 control-label">
					<label>Country<span>*</span></label>
				</div>
				<div class="col-md-10">
					<%-- <form:select path="country_id" class="logInput">
					<form:option value="0" label="Select Country"/>						
						<c:forEach var = "Country" items = "${countryList}">							
							<form:option value = "${Country.country_id}">${Country.country_name}</form:option>	
						</c:forEach>									
					</form:select> --%>

					<form:select path="country_id" class="logInput" placeholder="Country Name" onChange="getStateList(this.value)">
						<form:option value="0" label="Select Country Name" />
						<c:forEach var="Country" items="${countryList}">
							<form:option value="${Country.country_id}">${Country.country_name}  </form:option>
						</c:forEach>
					</form:select>

					<span class="logError"><form:errors path="country_id" /></span>
				</div>
			</div>

			<div class="row">
				<div class="col-md-2 control-label">
					<label>State<span>*</span></label>
				</div>
				<div class="col-md-10">
					<form:select path="state_id" class="logInput"
						placeholder="State Name" id="stateId">
						<form:option value="0" label="Select State Name" />
					</form:select>
					<span class="logError"><form:errors path="state_id" /></span>
				</div>
			</div>

			<div class="row">
				<div class="col-md-2 control-label">
					<label>City<span>*</span></label>
				</div>
				<div class="col-md-10">
					<form:input path="city_name" class="logInput" id="city_name"
						maxlength="${SIZE_EIGHTEEN}" placeholder="City Name" />
					<span class="logError"><form:errors path="city_name" /></span>
				</div>
			</div>
			<div class="row">
				<div class="col-md-2 control-label">
					<label>Status<span>*</span></label>
				</div>
				<div class="col-md-10">
					<form:radiobutton path="status" value="true" checked="checked" />
					Enable
					<form:radiobutton path="status" value="false" />
					Disable
				</div>
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
	$(function() {		
	    setTimeout(function() {
	        $("#successMsg").hide()
	    }, 3000);

		$("#city_name").keypress(function(e) {
			if (!lettersAndHyphenOnly(e)) {
				return false;
			}
		});
		<c:if test="${city.state_id != null}">
			getStateList('${city.country_id}');
			$("#stateId").val('${city.state_id}');
		</c:if>
	});
	
	function cancel(){
		window.location.assign('<c:url value = "cityList"/>');	
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
						    text: '${state.state_name}',
						})); */
						
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
</script>
<%@include file="/WEB-INF/includes/footer.jsp"%>