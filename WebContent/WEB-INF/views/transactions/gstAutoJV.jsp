<%@include file="/WEB-INF/includes/header.jsp"%>
<%@page import="com.fasset.controller.abstracts.MyAbstractController"%>
<spring:url value="/resources/images/edit.png" var="editImg" />
<spring:url value="/resources/images/view.png" var="viewImg" />
<spring:url value="/resources/images/delete.png" var="deleteImg" />

<script type="text/javascript" src="${valid}"></script>
<c:set var="SIZE_EIGHTEEN"><%=MyAbstractController.SIZE_EIGHTEEN%></c:set>
<c:set var="SIZE_THIRTY"><%=MyAbstractController.SIZE_THIRTY%></c:set>
<c:set var="SIZE_TEN"><%=MyAbstractController.SIZE_TEN%></c:set>
<c:set var="SIZE_SIX"><%=MyAbstractController.SIZE_SIX%></c:set>
<div class="breadcrumb">
	<h3>GST Auto JV</h3>
	<a href="homePage">Home</a> » <a href="gstAutoJVList">GST Auto JV</a>
</div>

<div>
<div class="col-md-12">
	<div class="col-md-6">
		<table class="table">
			<thead>
				<tr>
					<td colspan="3" style="text-align: center;">Input GST</td>
				</tr>
				<tr>
					<th>igst</th>
					<th>cgst</th>
					<th>sgst</th>
				</tr>
			</thead>
			<tbody>

				<tr>

					<td style="text-align: right;"><fmt:formatNumber type="number"
							minFractionDigits="2" maxFractionDigits="2"
							value="${autoJv.initialInputIgstBalance}" /></td>


					<td style="text-align: right;"><fmt:formatNumber type="number"
							minFractionDigits="2" maxFractionDigits="2"
							value="${autoJv.initialInputCgstBalance}" /></td>


					<td style="text-align: right;"><fmt:formatNumber type="number"
							minFractionDigits="2" maxFractionDigits="2"
							value="${autoJv.initialInputSgstBalance}" /></td>


				</tr>
			</tbody>
		</table>

	</div>
	<div class="col-md-6">
		<table class="table">
			<thead>
				<tr>
					<td colspan="3" style="text-align: center;">Output GST</td>
				</tr>
				<tr>
					<th>igst</th>
					<th>cgst</th>
					<th>sgst</th>
				</tr>
			</thead>
			<tbody>

				<tr>

					<td style="text-align: right;"><fmt:formatNumber type="number"
							minFractionDigits="2" maxFractionDigits="2"
							value="${autoJv.initialOutputIgstBalance}" /></td>


					<td style="text-align: right;"><fmt:formatNumber type="number"
							minFractionDigits="2" maxFractionDigits="2"
							value="${autoJv.initialOutputCgstbalance}" /></td>


					<td style="text-align: right;"><fmt:formatNumber type="number"
							minFractionDigits="2" maxFractionDigits="2"
							value="${autoJv.initialOutputSgstBalance}" /></td>

				</tr>
			</tbody>
		</table>
	</div>
</div>
<div class="col-md-offset-2 col-md-6">

	<table class="table">
		<thead>
			<tr>
				<th>sr. no</th>
				<th>sub-ledger</th>
				<th>cr</th>
				<th>dr</th>
			</tr>
		</thead>
	<tbody>
			<tr>
				<td rowspan="2">1</td>
				<td>i/p Igst</td>
				<td style="text-align: right;"><fmt:formatNumber type="number"
						minFractionDigits="2" maxFractionDigits="2"
						value="${autoJv.totalInputIgstDrBalance}" /></td>
				<td></td>
				
				
			</tr>
			<tr>
				<td>o/p igst</td>
				
				
						<td></td>
						<td style="text-align: right;"><fmt:formatNumber type="number"
						minFractionDigits="2" maxFractionDigits="2"
						value="${autoJv.totalOutputIgstCrBalance}" /></td>
			</tr>
			<tr>
				<td rowspan="2">2</td>
				<td>i/p cgst</td>
				<td style="text-align: right;"><fmt:formatNumber type="number"
						minFractionDigits="2" maxFractionDigits="2"
						value="${autoJv.totalInputCgstDrBalance}" /></td>
				
				<td></td>
				
				
			</tr>
			<tr>
				<td>o/p cgst</td>
				
										<td></td>
						<td style="text-align: right;"><fmt:formatNumber type="number"
						minFractionDigits="2" maxFractionDigits="2"
						value="${autoJv.totalOutputCgstCrbalance}" /></td>
			</tr>
			<tr>
				<td rowspan="2">3</td>

				<td>i/p sgst</td>
				<td style="text-align: right;"><fmt:formatNumber type="number"
						minFractionDigits="2" maxFractionDigits="2"
						value="${autoJv.totalInputSgstDrBalance}" /></td>
				<td></td>
				
				
			</tr>
			<tr>
				<td>o/p sgst</td>
				
				
						<td></td>
						<td style="text-align: right;"><fmt:formatNumber type="number"
						minFractionDigits="2" maxFractionDigits="2"
						value="${autoJv.totalOutputSgstCrBalance}" /></td>
			</tr>
			<%-- <tr>
				<td rowspan="2">4</td>
				<td>gst payable</td>
				<td style="text-align: right;"><fmt:formatNumber type="number"
						minFractionDigits="2" maxFractionDigits="2"
						value="${autoJv.gstPaybleBalance}" /></td>
				<td></td>
			</tr> --%>
		</tbody>
	</table>
</div>


<div class="clearfix"></div><br/>
<div class="col-md-12">
	<div class="col-md-6">
		<table class="table">
			<thead>
				<tr>
					<td colspan="3" style="text-align: center;"color:yellow">Input
						GST</td>
				</tr>
				<tr>
					<th>igst</th>
					<th>cgst</th>
					<th>sgst</th>
				</tr>
			</thead>
			<tbody>

				<tr>


					<td style="text-align: right;"><fmt:formatNumber type="number"
							minFractionDigits="2" maxFractionDigits="2"
							value="${autoJv.inputIgstBalance}" /></td>


					<td style="text-align: right;"><fmt:formatNumber type="number"
							minFractionDigits="2" maxFractionDigits="2"
							value="${autoJv.inputCgstBalance}" /></td>


					<td style="text-align: right;"><fmt:formatNumber type="number"
							minFractionDigits="2" maxFractionDigits="2"
							value="${autoJv.inputSgstBalance}" /></td>


				</tr>
			</tbody>
		</table>

	</div>
	<div class="col-md-6">
		<table class="table">
			<thead>
				<tr>
					<td colspan="3" style="text-align: center;">Output GST</td>
				</tr>
				<tr>
					<th>igst</th>
					<th>cgst</th>
					<th>sgst</th>
				</tr>
			</thead>
			<tbody>

				<tr>

					<td style="text-align: right;"><fmt:formatNumber type="number"
							minFractionDigits="2" maxFractionDigits="2"
							value="${autoJv.outputIgstBalance}" /></td>


					<td style="text-align: right;"><fmt:formatNumber type="number"
							minFractionDigits="2" maxFractionDigits="2"
							value="${autoJv.outputCgstbalance}" /></td>


					<td style="text-align: right;"><fmt:formatNumber type="number"
							minFractionDigits="2" maxFractionDigits="2"
							value="${autoJv.outputSgstBalance}" /></td>

				</tr>
			</tbody>
		</table>
	</div>
</div>
<div class="clearfix"></div><br/>
<div class="row" style="text-align: center; margin: 15px;">
	<button class="fassetBtn" type="button" onclick="ok()">
		<spring:message code="btn_save" />
	</button>
	<button class="fassetBtn" type="button" onclick="cancel()">
		<spring:message code="btn_cancel" />
	</button>
</div>
</div>


<script>
function cancel(){
	window.location.assign('<c:url value="gstAutoJVList"/>');
}
function ok(){
	window.location.assign('<c:url value="submitGstAutoJV"/>');
}
</script>

<%@include file="/WEB-INF/includes/footer.jsp"%>
--%>