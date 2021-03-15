<%@include file="/WEB-INF/includes/header.jsp"%>
<%@page import="com.fasset.controller.abstracts.MyAbstractController"%>

<script type="text/javascript" src="${valid}"></script>
<c:set var="SIZE_EIGHTEEN"><%=MyAbstractController.SIZE_EIGHTEEN%></c:set>
<c:set var="SIZE_THIRTY"><%=MyAbstractController.SIZE_THIRTY%></c:set>
<c:set var="SIZE_TEN"><%=MyAbstractController.SIZE_TEN%></c:set>
<c:set var="SIZE_SIX"><%=MyAbstractController.SIZE_SIX%></c:set>
<div class="breadcrumb">
	<h3>GSTReport2</h3>
	<a href="homePage">Home</a> 
</div>
<div class="col-md-12 wideform">
	<div class="fassetForm">
		<form:form id="GSTReport2Form"
			action="gstReport2Input" method="post" commandName="form" onsubmit="return checkdata();">
			

			<div class="row">
				<div class="col-md-2 control-label">
					<label>Year<span>*</span></label>
				</div>
				<div class="col-md-10">
					<form:select path="yearId" class="logInput" placeholder="Group Name" id="yearId">
						<form:option value="0" label="Select Year" />
						<c:forEach var="accountYear" items="${accountingYear}">
							<form:option value="${accountYear.year_id}">${accountYear.year_range}  </form:option>
						</c:forEach>	
					</form:select>
					<span class="logError"><form:errors path="yearId" /></span>
				</div>
			</div> 
			
			<div class="row">				
				<div class="col-md-2 control-label"><label>Month<span>*</span></label></div>
				<div class="col-md-10">
					<form:select path="month" class="logInput" id="monthId" >
						<form:option value="0" label="Select Month"/>
						<form:option value="1" label="January"/>
						<form:option value="2" label="February"/>
						<form:option value="3" label="March"/>
						<form:option value="4" label="April"/>	
						<form:option value="5" label="May"/>	
						<form:option value="6" label="June"/>	
						<form:option value="7" label="July"/>	
						<form:option value="8" label="August"/>	
						<form:option value="9" label="September"/>	
						<form:option value="10" label="October"/>
						<form:option value="11" label="November"/>
						<form:option value="12" label="December"/>										
					</form:select>
					<span class="logError"><form:errors path="month" /></span>
				</div>
			</div>
			
			<div class="row text-center-btn">
				<button class="fassetBtn" type="submit">
					<spring:message code="btn_show_report" />
				</button>
				<button class="fassetBtn" type="button" onclick="cancel()">
					<spring:message code="btn_cancel" />
				</button>
			</div>
		</form:form>
	</div>
</div>
<script type="text/javascript">

	function setDate(e){
		document.getElementById("from_date").value = e.value;		
	}
	
	function setDate1(e){
		document.getElementById("to_date").value = e.value;		
	}
	function cancel(){
		window.location.assign('<c:url value = "homePage"/>');	
	}
	
</script>
<script>
	$(function() {
			$( "#frmDate" ).datepicker();
			$( "#toDate" ).datepicker();
	   });
	
	function checkdata(){
		var yearId=document.getElementById("yearId").value;
		var monthId=document.getElementById("monthId").value;
	
			if(yearId=="0")
			{
			alert("Please Select Year");
			return false;
			}
			else if(monthId=="0")
			{
				alert("Please Select Month");
				return false;
			}
		else
			{
			return true;
			}
		//frmDate
		//toDate
	}
 </script>

<%@include file="/WEB-INF/includes/footer.jsp"%>
