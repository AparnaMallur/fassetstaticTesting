<%@include file="/WEB-INF/includes/headerLogin.jsp"%>
<div class="col-md-12">
	<div class="row quote-data">
		<form:form  action="ccavRequestHandler"  modelAttribute="ccAvenueParams" method="post">
			<div class="col-md-12" >		 		
	 			<form:input type="hidden" id="merchant_id" name="merchant_id" path="merchant_id"/>
				<form:input type="hidden" id="order_id" name="order_id" path="order_id"/>
				<form:input type="hidden" id="company_id" name="company_id" path="company_id"/>
				<form:input type="hidden" id="currency" name="currency" path="currency"/>
				<form:input type="hidden" id="redirect_url" name="redirect_url" path="redirect_url"/>
				<form:input type="hidden" id="cancel_url" name="cancel_url" path="cancel_url"/>
				<form:input type="hidden" id="language" name="language" path="language"/>	
				<form:input type="hidden" id="amount" name="amount" path="amount"/>	
				<table class = "table">
					<tr>
						<td><strong>Quotation Id:</strong></td>
						<td style="text-align: left;">${ccAvenueParams.order_id}</td>			
					</tr>
					<tr>
						<td><strong>Amount:</strong></td>
						<td style="text-align: left;">${ccAvenueParams.amount}</td>			
					</tr>
				</table>
			</div>
			<div class="col-md-12"  style = "text-align: center; margin:15px;">
				<button class="fassetBtn" type="submit">
					Proceed to Pay
				</button>
			</div>
		</form:form>
	</div>
</div>
<!-- <script>
	window.onload = function() {
		var d = new Date().getTime();
		document.getElementById("tid").value = d;
	};
</script> -->
<%@include file="/WEB-INF/includes/footerLogin.jsp"%>
