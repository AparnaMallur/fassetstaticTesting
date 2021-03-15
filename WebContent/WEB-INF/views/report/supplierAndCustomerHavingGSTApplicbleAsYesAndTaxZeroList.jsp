<%@include file="/WEB-INF/includes/header.jsp"%>
<%@page import="com.fasset.controller.abstracts.MyAbstractController"%>
<spring:url value="/resources/images/edit.png" var="editImg" />
<spring:url value="/resources/images/view.png" var="viewImg" />
<script type="text/javascript" src="${valid}"></script>
<spring:url value="/resources/js/jspdf.min.js" var="jspdfmin" />
<spring:url value="/resources/js/jspdf.plugin.autotable.js"
	var="jspdfauto" />
<spring:url value="/resources/js/report_table/exceptionReport4.js"
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
	<h3>SHORT GST EXCEPTION REPORT</h3>
	<a href="homePage">Home</a> » <a href="exceptionReport4">SHORT GST EXCEPTION REPORT</a>
</div>
<div class="col-md-12">
	<!-- Excel Start -->
	<div style="display: none" id="excel_report">
		<table>
			<tr style="text-align: center;">
				<td></td>
				<td></td>
				<td><b>SHORT GST EXCEPTION REPORT</b></td>
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
						pattern="dd-MM-yyyy" /> From ${from_date} To ${to_date}</td>
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

			<tr>
				<th data-field="date" data-filter-control="input"
					data-sortable="true">Date</th>
				<th data-field="particulars" data-filter-control="input"
					data-sortable="true">Supplier/Customer Name</th>
				<th data-field="voucher number" data-filter-control="input"
					data-sortable="true">Voucher Number</th>

				<th data-field="voucher type" data-filter-control="input"
					data-sortable="true">Voucher Type</th>

				<th data-field="Invoice No" data-filter-control="input"
					data-sortable="true">Invoice No</th>

				<th data-field="Total Value(Invoice value)"
					data-filter-control="input" data-sortable="true">Total
					Value(Invoice value)</th>
			</tr>

			<tbody>





				<c:forEach var="exceptionReport1"
					items="${exceptionReport4FormsList}">
					<tr>
						<td><fmt:parseDate value="${exceptionReport1.date}"
								pattern="yyyy-MM-dd" var="parsedDate" type="date" /> <fmt:formatDate
								value="${parsedDate}" var="createdDate" type="date"
								pattern="dd-MM-yyyy" /> ${createdDate}</td>

						<td style="text-align: left;">${exceptionReport1.customer}
							${exceptionReport1.suppliers}</td>
						<td>${exceptionReport1.voucher_Number}</td>
						<td>${exceptionReport1.voucher_Type}</td>

						<td class="tright">${exceptionReport1.invoiceNo}</td>

						<td class="tright">
					    <fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${exceptionReport1.totalAmount}" />
								 
					    </td>


					</tr>
				</c:forEach>
			</tbody>
		</table>

	</div>

	<!-- PDF Start  -->
	<div class="table-scroll" style="display: none;" id="tableDiv">
	
	<c:set var="rowcount" value="0" scope="page" />
		
		<c:if test="${rowcount == 0}">
	
	
		<table id="Hiddentable">
			<tr>
				<td></td>
				<td></td>
				<td style="color: blue;">SHORT GST EXCEPTION REPORT</td>
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
				<td>${from_date}To${to_date}</td>
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
					data-sortable="true"  align="left">Date</th>
				<th data-field="particulars" data-filter-control="input"
					data-sortable="true"  align="left">Supplier/Customer Name</th>
				<th data-field="voucher number" data-filter-control="input"
					data-sortable="true"  align="left" colspan="1">Voucher Number</th>
				<th class="tright" data-field="voucher type" data-filter-control="input"
					data-sortable="true"  style="left:80px;">Voucher Type</th>
				<th class="tright"  data-field="Invoice No" data-filter-control="input"
					data-sortable="true" align="right">Invoice No</th>
				<th  class="tright" data-field="Total Value(Invoice value)"
					data-filter-control="input" data-sortable="true" align="right" >Total
					Value(Invoice value)</th>
			</tr>
</c:if>
			<tbody>


				<c:forEach var="exceptionReport1"
					items="${exceptionReport4FormsList}">
					<tr>
					
					
							<c:if test="${rowcount >  45}">
									<c:set var="rowcount" value="0" scope="page" />
								</c:if>
								<c:if test="${rowcount > 44}">
									<%@include file="/WEB-INF/views/report/supplierAndCustomerHavingGSTApplicbleAsYesAndTaxZeroHeader.jsp"%>
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

						<td class="tright">${exceptionReport1.invoiceNo}</td>

						<td class="tright">
					    <fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${exceptionReport1.totalAmount}" />
								 
					    </td>


					</tr>
				</c:forEach>




			</tbody>
		</table>

	</div>
				<!-- PDF End  -->

	<!-- UI Start  -->
	<div class="table-scroll">
		<table id="table" data-toggle="table" data-search="false"
			data-escape="false" data-filter-control="true"
			data-show-export="false" data-click-to-select="true"
			data-pagination="true" data-page-size="10" data-toolbar="#toolbar"
			class="table ">
			<thead>
				<tr>
					<th data-field="date" data-filter-control="input"
						data-sortable="true">Date</th>
					<th data-field="particulars" data-filter-control="input"
						data-sortable="true">Supplier/Customer Name</th>
					<th data-field="voucher number" data-filter-control="input"
						data-sortable="true">Voucher Number</th>

					<th data-field="voucher type" data-filter-control="input"
						data-sortable="true">Voucher Type</th>

					<th data-field="Invoice No" data-filter-control="input"
						data-sortable="true">Invoice No</th>

					<th data-field="Total Value(Invoice value)"
						data-filter-control="input" data-sortable="true">Total
						Value(Invoice value)</th>
				</tr>
			</thead>
			<tbody>






				<c:forEach var="exceptionReport1"
					items="${exceptionReport4FormsList}">
					<tr>
						<td><fmt:parseDate value="${exceptionReport1.date}"
								pattern="yyyy-MM-dd" var="parsedDate" type="date" /> <fmt:formatDate
								value="${parsedDate}" var="createdDate" type="date"
								pattern="dd-MM-yyyy" /> ${createdDate}</td>

						<td style="text-align: left;">${exceptionReport1.customer}
							${exceptionReport1.suppliers}</td>
						<td>${exceptionReport1.voucher_Number}</td>
						<td>${exceptionReport1.voucher_Type}</td>

						<td class="tright">${exceptionReport1.invoiceNo}</td>
						
						<td class="tright">
					    <fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${exceptionReport1.totalAmount}" />
								 
					    </td>

						

					</tr>
				</c:forEach>


			</tbody>
		</table>
		<div class="row text-center-btn">
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
				onclick='exportexcel("SHORT GST EXCEPTION REPORT")'>Download
				as Excel</button>

			<button class="fassetBtn" type="button" onclick="back();">
				<spring:message code="btn_back" />
			</button>
		</div>
	</div>


</div>



<script type="text/javascript">
	$(function() {
		setTimeout(function() {
			$("#successMsg").hide();
		}, 3000);
	});
	function back() {
		window.location.assign('<c:url value = "exceptionReport4"/>');
	}
</script>
<%@include file="/WEB-INF/includes/footer.jsp"%>