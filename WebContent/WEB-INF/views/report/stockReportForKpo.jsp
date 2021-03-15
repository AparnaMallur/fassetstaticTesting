<%@include file="/WEB-INF/includes/header.jsp"%>
<%@page import="com.fasset.controller.abstracts.MyAbstractController"%>

<script type="text/javascript" src="${valid}"></script>
<c:set var="SIZE_EIGHTEEN"><%=MyAbstractController.SIZE_EIGHTEEN%></c:set>
<c:set var="SIZE_THIRTY"><%=MyAbstractController.SIZE_THIRTY%></c:set>
<c:set var="SIZE_TEN"><%=MyAbstractController.SIZE_TEN%></c:set>
<c:set var="SIZE_SIX"><%=MyAbstractController.SIZE_SIX%></c:set>

<div class="breadcrumb">
	<h3>Stock Report</h3>
	<a href="homePage">Home</a> » <a href="stockReport">Stock Report</a>
</div>
<c:if test="${successMsg != null}">
	<div class="successMsg" id="successMsg">
		<strong>${successMsg}</strong>
	</div>
</c:if>
<div class="col-md-12 wideform">
	<div class="fassetForm">
		<form:form id="stockReportForm"
			action="stockReportforKpo" method="post" commandName="stockReportForm">
			<%-- <div class="row">
				<form:input path="fromDate" id="from_date" hidden="hidden" />
				<form:input path="toDate" id="to_date" hidden="hidden" />
			</div> --%>

			<%-- <div class="row">
				<div class="col-md-2 control-label">
					<label>From Date<span>*</span></label>
				</div>
				<div class="col-md-10">
					<input type="text" style="color: black;" id="frmDate"
						name="frmDate" onchange="setDate(this)" placeholder="From Date">
					<span class="logError"><form:errors path="fromDate" /></span>
				</div>
			</div>
			<div class="row">
				<div class="col-md-2 control-label">
					<label>To Date<span>*</span></label>
				</div>
				<div class="col-md-10">
					<input type="text" style="color: black;" id="toDate" name="toDate"
						onchange="setDate1(this)" placeholder="To Date"> <span
						class="logError"><form:errors path="toDate" /></span>
				</div>
			</div> --%>
			<div class="row">
				<div class="col-md-2 control-label">
					<label>Company Name<span>*</span></label>
				</div>
				<div class="col-md-10">
					<form:select path="clientId" class="logInput" id="companyId"
						placeholder="Client Name" onChange = "getList(this.value)">
						<form:option value="0" label="Select Comapny Name" />
						<c:forEach var="company" items="${compList}">
							<form:option value="${company.company_id}">${company.company_name}</form:option>						
							</c:forEach>
					</form:select>
					<span class="logError"><form:errors path="clientId" /></span>
				</div>
			</div>
			<div class="row">
				<div class="col-md-2 control-label">
					<label>Product Name<span>*</span></label>
				</div>
				<div class="col-md-10">
					<form:select path="productId" class="logInput"
						placeholder="Product Name" id="productId">
						<form:option value="0" label="All" />
					</form:select>
					<span class="logError"><form:errors path="productId" /></span>
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
	
	$(function() {		
	    setTimeout(function() {
	        $("#successMsg").hide()
	    }, 3000);
	    
	});
	function cancel(){
		window.location.assign('<c:url value = "homePage"/>');	
	}
	
	function getList(usId){
		var stockArray = [];
		$.ajax({
	        type: "POST",
	        url: "getproductListForStockReport?id="+usId,
	        contentType: 'application/json',
	        dataType: 'json',
	        
	        success: function (data) {
	        	console.log("User Name==="+data);
	        	 $('#productId').empty();
	        	 $('#productId').append('<option value="0">All</option>');
	        	 $.each(data, function (index, product) {
	        		 
	        		 var tempArray = [];
					    tempArray["id"]=product.product_id;
					    tempArray["name"]=product.product_name;
					    stockArray.push(tempArray);	                         
	             });
	        	 
	        	 stockArray.sort(function(a, b) {
		 	            var textA = a.name.toUpperCase();
		 	            var textB = b.name.toUpperCase();
		 	            return (textA < textB) ? -1 : (textA > textB) ? 1 : 0;
		 	        });
		 			for(i=0;i<stockArray.length;i++)
		 			{
		 				$('#productId').append($('<option>', {
		 			    value: stockArray[i].id,
		 			    text: stockArray[i].name,
		 			})); 
		 			}
	        },
	        error: function (e) {
	            console.log("ERROR: ", e);
	            
	        },
	        done: function (e) {
	            
	            console.log("DONE");
	        }
	    });
	}	
	
</script>
<script>
	$(function() {
			$( "#frmDate" ).datepicker();
			$( "#toDate" ).datepicker();
			
			<c:if test="${stockReportForm.clientId != null && stockReportForm.clientId > 0}">
			var companyID = <c:out value = "${stockReportForm.clientId}"/>;
			if(companyID > 0){
				getList(companyID);
			}
		 </c:if>
	   });
	
	function checkdata(){
		var frmDate=document.getElementById("frmDate").value;
		var toDate=document.getElementById("toDate").value;
		var companyId=document.getElementById("companyId").value;
		var productId=document.getElementById("productId").value;
		
		var date1 = new Date(frmDate);
		var date2 = new Date(toDate);		
		   if(frmDate=="")
			{
			alert("Please Select From Date");
			return false;
			}
		  else if(toDate=="")
			{
			alert("Please Select To Date");
			return false;
			}
			else if(companyId=="0")
			{
			alert("Please Select Company Name");
			return false;
			}
			else if(date1 > date2)
			{				
				alert("From Date Cann't be greater than To Date");
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