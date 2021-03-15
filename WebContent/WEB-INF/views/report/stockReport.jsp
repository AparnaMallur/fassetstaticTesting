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
			action="stockReport" method="post" commandName="stockReportForm">
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
					<label>Product Name</label>
				</div>
				<div class="col-md-10">
					<form:select path="productId" class="logInput"
						placeholder="Product Name" id="productId">
						<form:option value="0" label="All" />
						<c:forEach var="product" items="${productList}">
						<c:if test="${product.type != 2}">
							<form:option value="${product.product_id}">${product.product_name}</form:option>
						</c:if>
						</c:forEach>
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
</script>
<script>
	$(function() {
			$( "#frmDate" ).datepicker();
			$( "#toDate" ).datepicker();
	   });
	
	function checkdata(){
		var frmDate=document.getElementById("frmDate").value;
		var toDate=document.getElementById("toDate").value;
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
			else if(productId=="0")
			{
			alert("Please Select Product Name");
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