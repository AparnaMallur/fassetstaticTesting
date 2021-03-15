<%@include file="/WEB-INF/includes/header.jsp"%>
<%@page import="com.fasset.controller.abstracts.MyAbstractController"%>
<spring:url value="/resources/images/edit.png" var="editImg" />
<spring:url value="/resources/images/view.png" var="viewImg" />
<spring:url value="/resources/images/delete.png" var="deleteImg" />
<spring:url value="/resources/images/add.png" var="additionImg" />


<script type="text/javascript" src="${valid}"></script>
<div class="breadcrumb">
	<h3>Opening Balances</h3>					
	<a href="homePage">Home</a> » <a href="subledgerOpeningList">Opening Balances</a>
</div>
<div class="col-md-12">		
<c:if test="${successMsg != null}">
	<div class="successMsg" id = "successMsg"> 
		<strong>${successMsg}</strong>
	</div>
</c:if>
	<div class="col-md-12 text-center" >
		<button class="fassetBtn acs-insert" type="button" onclick = "admin()">
			Sub Ledgers
		</button>	
	</div>	
	   
			
</div>
<div class="col-md-12 wideform">
	<div class="fassetForm">
		<form:form id="paymentReportForm"
			action="saveclientsubledgerOpeningList" method="post" commandName="form" onsubmit="return checkdata();">
			<div class="row">
							<div class="col-md-2 control-label">
							<label>Company<span>*</span></label>
							</div>
							<div class="col-md-10">
									<select id="company_id" name="company_id"  style="width:25%">
						    	    <option value='0' selected disabled>Select Company</option>
						    	   											
												<c:forEach var = "company" items = "${companyList}">													
													<option value = "${company.company_id}">${company.company_name}</option>	
												</c:forEach>
									 </select>	
							
							</div>
			</div>
			
			<div class="row text-center-btn">
				<button class="fassetBtn" type="submit" >
					<spring:message code="Show Details" />
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
    
});
	function setcompanydata(company_id)
	{
		var range ;
		var companyrange =[];
		$('#year_id').find('option').remove();
		$('#year_id').append($('<option>', {
		    value: 0,
		    text: 'Select Year'
		}));
		<c:forEach var="company" items="${companyList}">
		var cid = '<c:out value = "${company.company_id}" />';
		 if(cid==company_id)
			{
			range='<c:out value = "${company.yearRange}" />';
			} 			
		</c:forEach>
		 companyrange = range.split(",");
		 <c:forEach var="year" items="${yearList}">
			for(var i = 0; i < companyrange.length; i++)
				{
				var yid = '<c:out value = "${year.year_id}" />';
				var yrange = '<c:out value = "${year.year_range}" />';
					if(yid==companyrange[i])
						{
						 $('#year_id').append($('<option>', {
						    	value: yid,
						    	text: yrange,
							})); 
						}				
				}
		</c:forEach>
	}
	function checkdata()
	{		
		if(($('#company_id').val()==null) || ($('#company_id').val()==0))
			{
				alert("Select Company");
				return false;
			}		
		else
			{
			return true;
			}
	}
	
</script>
<style>
.textBoxCSS{border:0px;background-color:transparent}
</style>
<%@include file="/WEB-INF/includes/footer.jsp" %>
