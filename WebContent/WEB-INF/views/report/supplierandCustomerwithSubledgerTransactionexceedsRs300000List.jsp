<%@include file="/WEB-INF/includes/header.jsp"%>
<%@page import="com.fasset.controller.abstracts.MyAbstractController"%>
<spring:url value="/resources/images/edit.png" var="editImg" />
<spring:url value="/resources/images/view.png" var="viewImg" />
<script type="text/javascript" src="${valid}"></script>
<spring:url value="/resources/js/jspdf.min.js" var="jspdfmin" />
<spring:url value="/resources/js/jspdf.plugin.autotable.js"
	var="jspdfauto" />
<spring:url value="/resources/js/report_table/exceptionReport6.js"
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
	<h3>TDS NON DEDUCTION REPORT</h3>
	<a href="homePage">Home</a> » <a href="exceptionReport6">TDS NON DEDUCTION REPORT</a> 
</div>
<div class="col-md-12">	
	<!-- code for Excel   -->
<div style="display: none" id="excel_report">
	<table>
				<tr style="text-align: center;">
					<td></td>
					<td></td>
				<td><b>TDS NON DEDUCTION REPORT</b></td>
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
		
			<table id="table">
			<thead>
					<tr>
						<th data-field="date" data-filter-control="input"
							data-sortable="true">Date</th>
					     <th data-field="voucher number" data-filter-control="input"
							data-sortable="true">Voucher Number</th>
						<th data-field="particulars" data-filter-control="input"
							data-sortable="true">Supplier/Customer Name</th>
						<th data-field="subledger" data-filter-control="input"
							data-sortable="true">Subledger Name</th>
					    <th data-field="type" data-filter-control="input"
							data-sortable="true">Voucher Type</th>
						<th data-field="transaction" data-filter-control="input"
							data-sortable="true">Transaction value</th>
						<th data-field="tds" data-filter-control="input"
							data-sortable="true">TDS</th>
							
					</tr>
				</thead>
				<tbody>
				<c:forEach var="exceptionReport1"
					items="${exceptionReport6FormsList}">
					<tr>
						<td><fmt:parseDate value="${exceptionReport1.date}"
								pattern="yyyy-MM-dd" var="parsedDate" type="date" /> <fmt:formatDate
								value="${parsedDate}" var="createdDate" type="date"
								pattern="dd-MM-yyyy" /> ${createdDate}</td>
						<td>${exceptionReport1.voucher_Number}</td>
						<td style="text-align: left;">${exceptionReport1.customer}
							${exceptionReport1.suppliers}</td>
						<td> ${exceptionReport1.subledgerName }
						<td>${exceptionReport1.voucher_Type}</td>
							<td class="tright">
					    <fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${exceptionReport1.transactionValue}" />
								 
					    </td>
					    <td class="tright">
					    <fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${exceptionReport1.TDS}" />
								 
					    </td>
						

					</tr>
				</c:forEach>
				
					
				</tbody>
		
		</table>	
			</div>
			
	<div class="table-scroll" style="display: none;" id="tableDiv">
			
			
			
			
			
			<table id="Hiddentable" >
			<tr>
					<td></td>
					<td></td>
					<td  style="text-align: left">TDS NON DEDUCTION REPORT
					</td>
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
					<td>${from_date}To ${to_date}</td>
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
					     <th data-field="voucher number" data-filter-control="input"
							data-sortable="true">Voucher Number</th>
						<th data-field="particulars" data-filter-control="input"
							data-sortable="true">Supplier/Customer Name</th>
						<th data-field="subledger" data-filter-control="input"
							data-sortable="true">Subledger Name</th>
					    <th data-field="type" data-filter-control="input"
							data-sortable="true">Voucher Type</th>
						<th data-field="transaction" data-filter-control="input"
							data-sortable="true">Transaction value</th>
						<th data-field="tds" data-filter-control="input"
							data-sortable="true">TDS</th>
							
					</tr>
				
			
		<tbody>
				<c:forEach var="exceptionReport1"
					items="${exceptionReport6FormsList}">
					<tr>
					
						
								
						
								

					
						<td><fmt:parseDate value="${exceptionReport1.date}"
								pattern="yyyy-MM-dd" var="parsedDate" type="date" /> <fmt:formatDate
								value="${parsedDate}" var="createdDate" type="date"
								pattern="dd-MM-yyyy" /> ${createdDate}</td>
						<td>${exceptionReport1.voucher_Number}</td>
						<td style="text-align: left;">${exceptionReport1.customer}
							${exceptionReport1.suppliers}</td>
						<td> ${exceptionReport1.subledgerName }
						<td>${exceptionReport1.voucher_Type}</td>

							<td class="tright">
					    <fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${exceptionReport1.transactionValue}" />
								 
					    </td>
					    <td class="tright">
					    <fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${exceptionReport1.TDS}" />
								 
					    </td>

					</tr>
				</c:forEach>
				
					
				</tbody>
		</table>

	
	</div>
		
			<table id="table" data-toggle="table" data-search="false"
				data-escape="false" data-filter-control="true"
				data-show-export="false" data-click-to-select="true"
				data-pagination="true" data-page-size="10" data-toolbar="#toolbar"
				class="table table-scroll">
				<thead>
					<tr>
						<th data-field="date" data-filter-control="input"
							data-sortable="true">Date</th>
					     <th data-field="voucher number" data-filter-control="input"
							data-sortable="true">Voucher Number</th>
						<th data-field="particulars" data-filter-control="input"
							data-sortable="true">Supplier/Customer Name</th>
						<th data-field="subledger" data-filter-control="input"
							data-sortable="true">Subledger Name</th>
					    <th data-field="type" data-filter-control="input"
							data-sortable="true">Voucher Type</th>
						<th data-field="transaction" data-filter-control="input"
							data-sortable="true">Transaction value</th>
						<th data-field="tds" data-filter-control="input"
							data-sortable="true">TDS</th>
							
					</tr>
				</thead>
			<tbody>
				<c:forEach var="exceptionReport1"
					items="${exceptionReport6FormsList}">
					<tr>
						<td><fmt:parseDate value="${exceptionReport1.date}"
								pattern="yyyy-MM-dd" var="parsedDate" type="date" /> <fmt:formatDate
								value="${parsedDate}" var="createdDate" type="date"
								pattern="dd-MM-yyyy" /> ${createdDate}</td>
						<td>${exceptionReport1.voucher_Number}</td>
						<td style="text-align: left;">${exceptionReport1.customer}
							${exceptionReport1.suppliers}</td>
						<td> ${exceptionReport1.subledgerName }
						<td>${exceptionReport1.voucher_Type}</td>

							<td class="tright">
					    <fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${exceptionReport1.transactionValue}" />
								 
					    </td>
					    <td class="tright">
					    <fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${exceptionReport1.TDS}" />
								 
					    </td>

					</tr>
				</c:forEach>
				
					
				</tbody>
			</table>
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
				onclick='exportexcel("TDS NON DEDUCTION REPORT")'>Download as Excel
			</button>
		
		<button class="fassetBtn" type="button" onclick="back();">
			<spring:message code="btn_back" />
		</button>
	</div>
	
		</div>
		
			
	


<script type="text/javascript">
	$(function() {		
	    setTimeout(function() {
	        $("#successMsg").hide();
	    }, 3000);
	});
	function back(){
		window.location.assign('<c:url value = "exceptionReport6"/>');	
	}
</script>
<%@include file="/WEB-INF/includes/footer.jsp"%>