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
	<a href="homePage">Home</a> » <a href="gstReportList">GST Auto JV</a>
</div>

<div>
<div class="col-md-12">
	<div class="col-md-6">
		<table class="table">
			<thead>
				<tr>
					<td colspan="3" style="text-align:center;">Input GST</td>
				</tr>
				<tr>
					<th>igst</th>
					<th>cgst</th>
					<th>sgst</th>
				</tr>
			</thead>
			<tbody>
				
				<tr>
						<td>1</td>
						<td>2</td>
						<td>3</td>
					</tr>
			</tbody>
		</table>

	</div>
	<div class="col-md-6">
		<table class="table">
			<thead>
				<tr>
					<td colspan="3" style="text-align:center;">Output GST</td>
				</tr>
				<tr>
					<th>igst</th>
					<th>cgst</th>
					<th>sgst</th>
				</tr>
			</thead>
			<tbody>
				
				<tr>
					<td>1</td>
					<td>2</td>
					<td>3</td>
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
					<td  rowspan="2">1</td>
					<td>o/p Igst</td>
					<td>2</td>
					<td></td>
				</tr>
				<tr>
					<td>i/p igst</td>
					<td></td>
					<td>3</td>
				</tr>
				<tr>
					<td  rowspan="2">2</td>
					<td>o/p cgst</td>
					<td>2</td>
					<td></td>
				</tr>
				<tr>
					<td>i/p cgst</td>
					<td></td>
					<td>3</td>
				</tr>
					<tr>
					<td  rowspan="2">3</td>
					
					<td>o/p sgst</td>	
					<td>2</td>
					
					<td></td>
				</tr>
				<tr>
				<td>i/p sgst</td>
					<td></td>
					
					<td>3</td>
				</tr>
			</tbody>
		</table>
</div>



<div class="col-md-12">
	<div class="col-md-6">
		<table class="table">
			<thead>
				<tr>
					<td colspan="3" style="text-align:center; style="color:yellow">Input GST</td>
				</tr>
				<tr>
					<th>igst</th>
					<th>cgst</th>
					<th>sgst</th>
				</tr>
			</thead>
			<tbody>
				
				<tr>
					<td>1</td>
					<td>2</td>
					<td>3</td>
				</tr>
			</tbody>
		</table>

	</div>
	<div class="col-md-6">
		<table class="table">
			<thead>
				<tr>
					<td colspan="3" style="text-align:center;">Output GST</td>
				</tr>
				<tr>
					<th>igst</th>
					<th>cgst</th>
					<th>sgst</th>
				</tr>
			</thead>
			<tbody>
			
				<tr>
						<td>1</td>
					<td>2</td>
					<td>3</td>
				</tr>
			</tbody>
		</table>
	</div>
</div>
<div class="clearfix"></div>
<div class="row" style="text-align: center; margin: 15px;">
			<button class="fassetBtn" type="submit" onclick="return save()">
				Confirm</button>
			<button class="fassetBtn" type="button" onclick="cancel()">
				<spring:message code="btn_cancel" />
			</button>
		</div>
		</div>

<%@include file="/WEB-INF/includes/footer.jsp"%><%@include file="/WEB-INF/includes/header.jsp"%>
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
	<a href="homePage">Home</a> » <a href="gstReportList">GST Auto JV</a>
</div>

<div>
<div class="col-md-12">
	<div class="col-md-6">
		<table class="table">
			<thead>
				<tr>
					<td colspan="3" style="text-align:center;">Input GST</td>
				</tr>
				<tr>
					<th>igst</th>
					<th>cgst</th>
					<th>sgst</th>
				</tr>
			</thead>
			<tbody>
				
				<tr>
						<td>1</td>
						<td>2</td>
						<td>3</td>
					</tr>
			</tbody>
		</table>

	</div>
	<div class="col-md-6">
		<table class="table">
			<thead>
				<tr>
					<td colspan="3" style="text-align:center;">Output GST</td>
				</tr>
				<tr>
					<th>igst</th>
					<th>cgst</th>
					<th>sgst</th>
				</tr>
			</thead>
			<tbody>
				
				<tr>
					<td>1</td>
					<td>2</td>
					<td>3</td>
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
					<td  rowspan="2">1</td>
					<td>o/p Igst</td>
					<td>2</td>
					<td></td>
				</tr>
				<tr>
					<td>i/p igst</td>
					<td></td>
					<td>3</td>
				</tr>
				<tr>
					<td  rowspan="2">2</td>
					<td>o/p cgst</td>
					<td>2</td>
					<td></td>
				</tr>
				<tr>
					<td>i/p cgst</td>
					<td></td>
					<td>3</td>
				</tr>
					<tr>
					<td  rowspan="2">3</td>
					
					<td>o/p sgst</td>	
					<td>2</td>
					
					<td></td>
				</tr>
				<tr>
				<td>i/p sgst</td>
					<td></td>
					
					<td>3</td>
				</tr>
			</tbody>
		</table>
</div>



<div class="col-md-12">
	<div class="col-md-6">
		<table class="table">
			<thead>
				<tr>
					<td colspan="3" style="text-align:center; style="color:yellow">Input GST</td>
				</tr>
				<tr>
					<th>igst</th>
					<th>cgst</th>
					<th>sgst</th>
				</tr>
			</thead>
			<tbody>
				
				<tr>
					<td>1</td>
					<td>2</td>
					<td>3</td>
				</tr>
			</tbody>
		</table>

	</div>
	<div class="col-md-6">
		<table class="table">
			<thead>
				<tr>
					<td colspan="3" style="text-align:center;">Output GST</td>
				</tr>
				<tr>
					<th>igst</th>
					<th>cgst</th>
					<th>sgst</th>
				</tr>
			</thead>
			<tbody>
			
				<tr>
						<td>1</td>
					<td>2</td>
					<td>3</td>
				</tr>
			</tbody>
		</table>
	</div>
</div>
<div class="clearfix"></div>
<div class="row" style="text-align: center; margin: 15px;">
			<button class="fassetBtn" type="submit" onclick="return save()">
				Confirm</button>
			<button class="fassetBtn" type="button" onclick="cancel()">
				<spring:message code="btn_cancel" />
			</button>
		</div>
		</div>

<%@include file="/WEB-INF/includes/footer.jsp"%><%@include file="/WEB-INF/includes/header.jsp"%>
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
	<a href="homePage">Home</a> » <a href="gstReportList">GST Auto JV</a>
</div>

<div>
<div class="col-md-12">
	<div class="col-md-6">
		<table class="table">
			<thead>
				<tr>
					<td colspan="3" style="text-align:center;">Input GST</td>
				</tr>
				<tr>
					<th>igst</th>
					<th>cgst</th>
					<th>sgst</th>
				</tr>
			</thead>
			<tbody>
				
				<tr>
						<td>1</td>
						<td>2</td>
						<td>3</td>
					</tr>
			</tbody>
		</table>

	</div>
	<div class="col-md-6">
		<table class="table">
			<thead>
				<tr>
					<td colspan="3" style="text-align:center;">Output GST</td>
				</tr>
				<tr>
					<th>igst</th>
					<th>cgst</th>
					<th>sgst</th>
				</tr>
			</thead>
			<tbody>
				
				<tr>
					<td>1</td>
					<td>2</td>
					<td>3</td>
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
					<td  rowspan="2">1</td>
					<td>o/p Igst</td>
					<td>2</td>
					<td></td>
				</tr>
				<tr>
					<td>i/p igst</td>
					<td></td>
					<td>3</td>
				</tr>
				<tr>
					<td  rowspan="2">2</td>
					<td>o/p cgst</td>
					<td>2</td>
					<td></td>
				</tr>
				<tr>
					<td>i/p cgst</td>
					<td></td>
					<td>3</td>
				</tr>
					<tr>
					<td  rowspan="2">3</td>
					
					<td>o/p sgst</td>	
					<td>2</td>
					
					<td></td>
				</tr>
				<tr>
				<td>i/p sgst</td>
					<td></td>
					
					<td>3</td>
				</tr>
			</tbody>
		</table>
</div>



<div class="col-md-12">
	<div class="col-md-6">
		<table class="table">
			<thead>
				<tr>
					<td colspan="3" style="text-align:center; style="color:yellow">Input GST</td>
				</tr>
				<tr>
					<th>igst</th>
					<th>cgst</th>
					<th>sgst</th>
				</tr>
			</thead>
			<tbody>
				
				<tr>
					<td>1</td>
					<td>2</td>
					<td>3</td>
				</tr>
			</tbody>
		</table>

	</div>
	<div class="col-md-6">
		<table class="table">
			<thead>
				<tr>
					<td colspan="3" style="text-align:center;">Output GST</td>
				</tr>
				<tr>
					<th>igst</th>
					<th>cgst</th>
					<th>sgst</th>
				</tr>
			</thead>
			<tbody>
			
				<tr>
						<td>1</td>
					<td>2</td>
					<td>3</td>
				</tr>
			</tbody>
		</table>
	</div>
</div>
<div class="clearfix"></div>
<div class="row" style="text-align: center; margin: 15px;">
			<button class="fassetBtn" type="submit" onclick="return save()">
				Confirm</button>
			<button class="fassetBtn" type="button" onclick="cancel()">
				<spring:message code="btn_cancel" />
			</button>
		</div>
		</div>

<%@include file="/WEB-INF/includes/footer.jsp"%>