<%@include file="/WEB-INF/includes/header.jsp"%>
<%@page import="com.fasset.controller.abstracts.MyAbstractController"%>


<script type="text/javascript" src="${valid}"></script>
<spring:url value="/resources/js/jspdf.min.js" var="jspdfmin" />
<spring:url value="/resources/js/jspdf.plugin.autotable.js"
	var="jspdfauto" />
<spring:url value="/resources/js/report_table/exceptionReport1.js"
	var="tableexport" />
<script type="text/javascript"
	src="http://code.jquery.com/jquery-latest.min.js"></script>
<script type="text/javascript" src="${jspdfmin}"></script>
<script type="text/javascript" src="${jspdfauto}"></script>
<script type="text/javascript" src="${tableexport}"></script>
<script type="text/javascript">
	function pdf(selector, params) {
		selector = "#tableDiv";
		$("#tableDiv").css("display", "block");
		var options = {
			//ignoreRow: [1,11,12,-2],
			//ignoreColumn: [0,-1],
			//pdfmake: {enabled: true},
			tableName : 'Countries',
			worksheetName : 'Countries by population'
		};
		$.extend(true, options, params);
		$(selector).tableExport(options);
		$("#tableDiv").css("display", "none");
	}
</script>
<div class="breadcrumb">
	<h3>EXCEPTIONAL CASH PAYMENT REPORT</h3>
	<a href="homePage">Home</a> » <a href="exceptionReport1">EXCEPTIONAL CASH PAYMENT REPORT</a>
</div>
<div class="col-md-12">
	<c:if test="${successMsg != null}">
		<div class="successMsg" id="successMsg">
			<strong>${successMsg}</strong>
		</div>
	</c:if>
	<!-- code for Excel   -->
	<div style="display: none" id="excel_report">
		<table>
			<tr style="text-align: center;">
				<td></td>
				<td></td>
				<td><b>EXCEPTIONAL CASH PAYMENT REPORT</b></td>
			</tr>
			<tr></tr>
			<tr>
				<td colspan='7'>Company Name: ${company.company_name}</td>
			</tr>
			<tr>
				<td colspan='7'>Address: ${company.permenant_address}</td>
			</tr>
			<tr>
				<td colspan='7'><fmt:parseDate value="${from_date}"
						pattern="yyyy-MM-dd" var="parsedDate" type="date" /> <fmt:formatDate
						value="${parsedDate}" var="from_date" type="date"
						pattern="dd-MM-yyyy" /> <fmt:parseDate value="${to_date}"
						pattern="yyyy-MM-dd" var="parsedDate" type="date" /> <fmt:formatDate
						value="${parsedDate}" var="to_date" type="date"
						pattern="dd-MM-yyyy" /> From ${from_date}  To ${to_date}</td>
			</tr>
			<tr>
				<td colspan='5'>CIN: <c:if
						test="${company.company_statutory_type.company_statutory_id==10 || company.company_statutory_type.company_statutory_id==14}">
						 ${company.registration_no}
					    </c:if>
				</td>
			</tr>
		</table>
		<table>
			<thead>
				<tr>

					<th data-field="date" data-filter-control="input"
						data-sortable="true">Date</th>
					<th data-field="customer" data-filter-control="input"
						data-sortable="true">Customer Name/Supplier Name</th>
					<th data-field="voucher" data-filter-control="input"
						data-sortable="true">Voucher No</th>
					<th data-field="type" data-filter-control="select"
						data-sortable="true">Voucher Type</th>
					<th data-field="paymentType" data-filter-control="select"
						data-sortable="true">Payment Type</th>
					<th data-field="receipt" data-filter-control="input"
						data-sortable="true">Total Amount</th>


				</tr>
			</thead>

			<tbody>
<c:forEach var="exceptionReport1"
					items="${exceptionReport1FormsList}">
					<tr>
						<td><fmt:parseDate value="${exceptionReport1.date}"
								pattern="yyyy-MM-dd" var="parsedDate" type="date" /> <fmt:formatDate
								value="${parsedDate}" var="createdDate" type="date"
								pattern="dd-MM-yyyy" /> ${createdDate}</td>

						<td style="text-align: left;">${exceptionReport1.customer}
							${exceptionReport1.suppliers}</td>
						<td>${exceptionReport1.voucher_Number}</td>
						<td>${exceptionReport1.voucher_Type}</td>

						<td>${exceptionReport1.paymentType}</td>
						<td class="tright">
					    <fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${exceptionReport1.totalAmount}" />
								 
					    </td>
						
					

					</tr>
				</c:forEach>
			
			</tbody>
		</table>
	</div>
	<!-- code for PDF   -->

	<div class="table-scroll" style="display: none;" id="tableDiv">
		
		<c:set var="rowcount" value="0" scope="page" />
		
		<c:if test="${rowcount == 0}">
	
	
	
		<table id="Hiddentable">
			<tr>
				<td></td>
				<td></td>
				<td style="color: blue; margin-left: 50px;">EXCEPTIONAL CASH PAYMENT REPORT</td>
			</tr>

			<tr>
				<td align="center">Company Name:</td>
				<td></td>
				<td align="center">${company.company_name}</td>
			</tr>
			<tr>
				<td align="center">Address:</td>
				<td></td>
				<td align="center">${company.permenant_address}</td>
			</tr>
			<tr>
				<td>
					<%-- <fmt:parseDate value="${from_date}" pattern="yyyy-MM-dd" var="parsedDate" type="date" />
                   			 <fmt:formatDate value="${parsedDate}" var="from_date" type="date" pattern="dd-MM-yyyy" />
                   			 <fmt:parseDate value="${to_date}" pattern="yyyy-MM-dd" var="parsedDate" type="date" />
                   			 <fmt:formatDate value="${parsedDate}" var="to_date" type="date" pattern="dd-MM-yyyy" /> --%>
					From:
				</td>
				<td></td>
				<td>${from_date}  To${to_date}</td>
			</tr>
			<tr>

				<td colspan='3'>CIN: <c:if
						test="${company.company_statutory_type.company_statutory_id==10 || company.company_statutory_type.company_statutory_id==14}">
					 ${company.registration_no}
				    </c:if>
				</td>
			</tr>

			<tr>

				<th data-field="date" data-filter-control="input"
					data-sortable="true">Date</th>
				<th data-field="customer" data-filter-control="input"
					data-sortable="true">Customer Name/Supplier Name</th>
				<th data-field="voucher" data-filter-control="input"
					data-sortable="true">Voucher No</th>
				<th data-field="type" data-filter-control="select"
					data-sortable="true">Voucher Type</th>
				<th data-field="paymentType" data-filter-control="select"
					data-sortable="true">Payment Type</th>
				<th data-field="receipt" data-filter-control="input"
					data-sortable="true">Total Amount</th>


			</tr>
</c:if>



			<tbody>
<c:forEach var="exceptionReport1"
					items="${exceptionReport1FormsList}">
					<tr>
					<c:if test="${rowcount >  45}">
									<c:set var="rowcount" value="0" scope="page" />
								</c:if>
								<c:if test="${rowcount > 44}">
									<%@include file="/WEB-INF/views/report/cashPaymentAndReceiptHeader.jsp"%>
								</c:if>
						
								<c:set var="rowcount" value="${rowcount + 1}" scope="page" />
								
								
						<td><fmt:parseDate value="${exceptionReport1.date}"
								pattern="yyyy-MM-dd" var="parsedDate" type="date" /> <fmt:formatDate
								value="${parsedDate}" var="createdDate" type="date"
								pattern="dd-MM-yyyy" /> ${createdDate}</td>

						<td style="text-align: left;">${exceptionReport1.customer}
							${exceptionReport1.suppliers}</td>
						<td>${exceptionReport1.voucher_Number}</td>
						<td>${exceptionReport1.voucher_Type}</td>

						<td>${exceptionReport1.paymentType}</td>

						<td class="tright">
					    <fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${exceptionReport1.totalAmount}" />
								 
					    </td>

					</tr>
				</c:forEach>
			
			</tbody>
		</table>

				<!-- UI Start -->
	</div>
	<div class="borderForm">
		<table id="table" data-toggle="table" data-search="false"
			data-escape="false" data-filter-control="true"
			data-show-export="false" data-click-to-select="true"
			data-pagination="true" data-page-size="10" data-toolbar="#toolbar"
			class="table">
			<thead>
				<tr>

					<th data-field="date" data-filter-control="input"
						data-sortable="true">Date</th>
					<th data-field="customer" data-filter-control="input"
						data-sortable="true">Customer Name/Supplier Name</th>
					<th data-field="voucher" data-filter-control="input"
						data-sortable="true">Voucher No</th>
					<th data-field="type" data-filter-control="select"
						data-sortable="true">Voucher Type</th>
					<th data-field="paymentType" data-filter-control="select"
						data-sortable="true">Payment Type</th>
					<th data-field="receipt" data-filter-control="input"
						data-sortable="true">Total Amount</th>


				</tr>
			</thead>
			<tbody>

				<c:forEach var="exceptionReport1"
					items="${exceptionReport1FormsList}">
					<tr>
						<td><fmt:parseDate value="${exceptionReport1.date}"
								pattern="yyyy-MM-dd" var="parsedDate" type="date" /> <fmt:formatDate
								value="${parsedDate}" var="createdDate" type="date"
								pattern="dd-MM-yyyy" /> ${createdDate}</td>

						<td style="text-align: left;">${exceptionReport1.customer}
							${exceptionReport1.suppliers}</td>
						<td>${exceptionReport1.voucher_Number}</td>
						<td>${exceptionReport1.voucher_Type}</td>

						<td>${exceptionReport1.paymentType}</td>

							<td class="tright">
					    <fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${exceptionReport1.totalAmount}" />
								 
					    </td>

					</tr>
				</c:forEach>
				
			</tbody>
		</table>
	</div>
</div>
<div class="row" style="text-align: center; margin: 15px;">

	<button class="fassetBtn" type="button"
		onclick="pdf('#Hiddentable', {type: 'pdf',
                jspdf: {
                    autotable: {
                        styles: {overflow: 'linebreak',
                             fontSize: 9,
                             rowHeight: 5},
                        headerStyles: {rowHeight: 10,
                             fontSize: 8},
                        bodyStyles: {rowHeight: 10}
                    }
                }});">
		Download as PDF</button>
	<button class="fassetBtn" type="button" id='btnExport3'
		onclick='exportexcel("EXCEPTIONAL CASH PAYMENT REPORT")'>Download
		as Excel</button>

	<button class="fassetBtn" type="button" onclick="back();">
		<spring:message code="btn_back" />
	</button>
</div>




<script type="text/javascript">
	function back() {
		window.location.assign('<c:url value = "exceptionReport1"/>');
	}
</script>
<%@include file="/WEB-INF/includes/footer.jsp"%>