<%@include file="/WEB-INF/includes/header.jsp"%>
<%@page import="com.fasset.controller.abstracts.MyAbstractController"%>


<script type="text/javascript" src="${valid}"></script>
<spring:url value="/resources/js/jspdf.min.js" var="jspdfmin" />
<spring:url value="/resources/js/jspdf.plugin.autotable.js"
	var="jspdfauto" />
<spring:url value="/resources/js/report_table/exceptionReport3.js"
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
	<h3>PENDING ADVANCE REPORT</h3>
	<a href="homePage">Home</a> » <a href="exceptionReport3">PENDING ADVANCE REPORT</a>
</div>
<div class="col-md-12">
	<!--  Excel Start -->
	<div style="display: none" id="excel_report">


		<table>
			<tr style="text-align: center;">
				<td></td>
				<td></td>
				<td><b>PENDING ADVANCE REPORT</b></td>
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
						pattern="dd-MM-yyyy" /> Report date 	${from_date} </td>
			</tr>
			<tr>
				<td colspan='5'>CIN: <c:if
						test="${company.company_statutory_type.company_statutory_id==10 || company.company_statutory_type.company_statutory_id==14}">
						 ${company.registration_no}
					    </c:if>
				</td>
			</tr>
		</table>
		<table id="table">
			<tr>
				<th data-field="date" data-filter-control="input"
					data-sortable="true">Date</th>
				<th data-field="name" data-filter-control="input"
					data-sortable="true">Customer Name/Supplier Name</th>
				<th data-field="voucher" data-filter-control="input"
					data-sortable="true">Voucher No</th>
				<th data-field="type" data-filter-control="input"
					data-sortable="true">Voucher Type</th>
				<th data-field="amount" data-filter-control="input"
					data-sortable="true">Total Amount</th>
				<th data-field="days" data-filter-control="input"
					data-sortable="true">Aging in Days</th>
			</tr>



			<tbody>
				<c:forEach var="dayBook" items="${exceptionReport3FormsList}">
					<tr>
						<td><fmt:parseDate value="${dayBook.date}"
								pattern="yyyy-MM-dd" var="parsedDate" type="date" /> <fmt:formatDate
								value="${parsedDate}" var="createdDate" type="date"
								pattern="dd-MM-yyyy" /> ${createdDate}</td>
						<td style="text-align: left;">${dayBook.customer}
							${dayBook.suppliers}</td>
						<td>${dayBook.voucher_Number}</td>
						<td>${dayBook.voucher_Type}</td>
							<td class="tright">
					    <fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${dayBook.totalAmount}" />
								 
					    </td>
						<td class="tright">${dayBook.numberOfDays}</td>
					</tr>

				</c:forEach>


			</tbody>

		</table>


	</div>

	<!--  PDF Start -->
	<div class="table-scroll" style="display: none;" id="tableDiv">
		
		
		
		
		
		<table id="Hiddentable">

			<tr>
				<td></td>
				<td></td>
				<td style="color: blue; margin-left: 50px;">Supplier and
					Customer Advance Unadjusted Report</td>
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
					Report date:
				</td>
				<td></td>
				<td>${from_date}</td>
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
				<th data-field="name" data-filter-control="input"
					data-sortable="true">Customer Name/Supplier Name</th>
				<th data-field="voucher" data-filter-control="input"
					data-sortable="true">Voucher No</th>
				<th data-field="type" data-filter-control="input"
					data-sortable="true">Voucher Type</th>
				<th data-field="amount" data-filter-control="input"
					data-sortable="true">Total Amount</th>
				<th data-field="days" data-filter-control="input"
					data-sortable="true">Aging in Days</th>
			</tr>
			
			
			
			<tbody>
				<c:forEach var="dayBook" items="${exceptionReport3FormsList}">
					<tr>
						
						
							
					
					
						<td><fmt:parseDate value="${dayBook.date}"
								pattern="yyyy-MM-dd" var="parsedDate" type="date" /> <fmt:formatDate
								value="${parsedDate}" var="createdDate" type="date"
								pattern="dd-MM-yyyy" /> ${createdDate}</td>
						<td style="text-align: left;">${dayBook.customer}
							${dayBook.suppliers}</td>
						<td>${dayBook.voucher_Number}</td>
						<td>${dayBook.voucher_Type}</td>
							<td class="tright">
					    <fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${dayBook.totalAmount}" />
								 
					    </td>
						<td class="tright">${dayBook.numberOfDays}</td>
					</tr>

				</c:forEach>


			</tbody>




		</table>


	</div>

	<!-- UI Start  -->
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
					<th data-field="name" data-filter-control="input"
						data-sortable="true">Customer Name/Supplier Name</th>
					<th data-field="voucher" data-filter-control="input"
						data-sortable="true">Voucher No</th>
					<th data-field="type" data-filter-control="input"
						data-sortable="true">Voucher Type</th>
					<th data-field="amount" data-filter-control="input"
						data-sortable="true">Total Amount</th>
					<th data-field="days" data-filter-control="input"
						data-sortable="true">Aging in Days</th>
				</tr>
			</thead>
			<tbody>
				<c:forEach var="dayBook" items="${exceptionReport3FormsList}">
					<tr>
						<td><fmt:parseDate value="${dayBook.date}"
								pattern="yyyy-MM-dd" var="parsedDate" type="date" /> <fmt:formatDate
								value="${parsedDate}" var="createdDate" type="date"
								pattern="dd-MM-yyyy" /> ${createdDate}</td>
						<td style="text-align: left;">${dayBook.customer}
							${dayBook.suppliers}</td>
						<td>${dayBook.voucher_Number}</td>
						<td>${dayBook.voucher_Type}</td>
							<td class="tright">
					    <fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${dayBook.totalAmount}" />
								 
					    </td>
						
						<td class="tright">${dayBook.numberOfDays}</td>
					</tr>

				</c:forEach>


			</tbody>

		</table>
	</div>


	<div class="row text-center-btn">
		<c:if test="${role!=7}">
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
				onclick='exportexcel("PENDING ADVANCE REPORT")'>Download
				as Excel</button>
		</c:if>
		<button class="fassetBtn" type="button" onclick="back();">
			<spring:message code="btn_back" />
		</button>
	</div>
</div>
<script type="text/javascript">	
	function back(){
		window.location.assign('<c:url value = "exceptionReport3"/>');	
	}
</script>
<%@include file="/WEB-INF/includes/footer.jsp"%>