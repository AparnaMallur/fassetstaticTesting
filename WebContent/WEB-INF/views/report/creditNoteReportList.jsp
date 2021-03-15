<%@include file="/WEB-INF/includes/header.jsp"%>
<%@page import="com.fasset.controller.abstracts.MyAbstractController"%>
<spring:url value="/resources/images/edit.png" var="editImg" />
<spring:url value="/resources/images/view.png" var="viewImg" />
<script type="text/javascript" src="${valid}"></script>
<spring:url value="/resources/js/jspdf.min.js" var="jspdfmin" />
<spring:url value="/resources/js/jspdf.plugin.autotable.js"
	var="jspdfauto" />
<spring:url value="/resources/js/report_table/creditNote.js"
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
	<h3>Credit Note Register</h3>
	<a href="homePage">Home</a> » <a href="creditNoteReport">Credit
		Note Register</a>
</div>
<div class="col-md-12">


	<c:set var="Totalcredit" value="0" />

	<c:forEach var="entry" items="${creditNoteList}">
		<c:if test="${entry.customer != null}">
			<c:set var="Totalcredit" value="${Totalcredit + entry.round_off}" />
		</c:if>
	</c:forEach>

	<c:if test="${(option==2)}">
		<!-- Excel Start -->
		<div style="display: none" id="excel_report">

			<!-- Date -->
			<font size="11" face="verdana" >
			<table>
				<tr style="text-align:center;"><td colspan='9'><b>Credit Note Register</b></td>
				</tr>
				<tr></tr>
				<tr style="text-align:center;">
					<td colspan='9'>Company Name: ${company.company_name}</td>
				</tr>
				<tr style="text-align:center;">
					<td colspan='9'>Address: ${company.permenant_address}</td>
				</tr>
				<tr style="text-align:center;">
					<td colspan='9'><fmt:parseDate value="${from_date}"
							pattern="yyyy-MM-dd" var="parsedDate" type="date" /> <fmt:formatDate
							value="${parsedDate}" var="from_date" type="date"
							pattern="dd-MM-yyyy" /> <fmt:parseDate value="${to_date}"
							pattern="yyyy-MM-dd" var="parsedDate" type="date" /> <fmt:formatDate
							value="${parsedDate}" var="to_date" type="date"
							pattern="dd-MM-yyyy" /> From ${from_date} To ${to_date}</td>
				</tr>
				<tr style="text-align:center;">
					<td colspan='9'>CIN: <c:if
							test="${company.company_statutory_type.company_statutory_id==10 || company.company_statutory_type.company_statutory_id==14}">
						 ${company.registration_no}
					    </c:if>
					</td>
				</tr>
			</table>
			</font>
			<!-- Date -->
			<font size="11" face="verdana" >
			<table style="border:1pt solid  !important  border-collapse: collapse;">
				<thead>
					<tr style="border:thin solid  !important ">
						<th data-field="Cr note Date" data-filter-control="input"
							data-sortable="true">Cr note Date</th>
						<th data-field="Cr note Number" data-filter-control="input"
							data-sortable="true">Cr note Number</th>
						<th data-field="particulars" data-filter-control="input"
							data-sortable="true">Particulars</th>
						<th data-field="voucherType" data-filter-control="input"
							data-sortable="true">Voucher Type</th>
						<th data-field="credit" data-filter-control="input"
							data-sortable="true">Credit  Note Amount</th>
						<th data-field="Original Invoice amount"
							data-filter-control="input" data-sortable="true">Original
							Invoice amount</th>
						<th data-field="Original Inv no." data-filter-control="input"
							data-sortable="true" style="text-align: center;">Original
							Inv no.</th>
						<th data-field="Original Invoice date" data-filter-control="input"
							data-sortable="true" style="text-align: center;">Original
							Invoice date.</th>
						<th data-field="Reason for Cr note" data-filter-control="input"
							data-sortable="true" style="text-align: center;">Amount(Rs.)</th>
					</tr>
				</thead>
				<c:set var="row_count_sales" value="0" />
				<tbody>
					<c:forEach var="creditNote" items="${creditNoteList}">
				<c:if test="${creditNote.customer != null}">
					<tr style="border:thin solid  !important ">
						<td style="text-align: left;">
								<fmt:parseDate value="${creditNote.date}" pattern="yyyy-MM-dd"
										var="parsedDate" type="date" />
                   				<fmt:formatDate value="${parsedDate}"
										var="createdDate" type="date" pattern="dd-MM-yyyy" />
								${createdDate}						
						</td>
						<td style="text-align: left;">${creditNote.voucher_no} </td>
						<td style="text-align: left;">
							<div>${creditNote.customer.firm_name} against ${creditNote.sales_bill_id.voucher_no}</div>
						</td>
						<td style="text-align: left;">Credit Note</td>
						
							
						<td class='tright'>
						<fmt:formatNumber type="number" minFractionDigits="2"
										maxFractionDigits="2" value="${creditNote.round_off+creditNote.tds_amount}" />
						</td>
						<td class='tright'>				
							<fmt:formatNumber type="number" minFractionDigits="2"
										maxFractionDigits="2"
										value="${creditNote.sales_bill_id.round_off+creditNote.sales_bill_id.tds_amount}" />
							<c:set var="row_count_sales"
										value="${row_count_sales + creditNote.sales_bill_id.round_off + creditNote.sales_bill_id.tds_amount}" />					
						</td>
							<td style="text-align: left;">${creditNote.sales_bill_id.voucher_no}</td>			
					
						<fmt:parseDate value="${creditNote.date}" pattern="yyyy-MM-dd"
									var="parsedDate" type="date" />
							<fmt:formatDate value="${parsedDate}" var="date" type="date"
									pattern="dd-MM-yyyy" />
							<td style="text-align: left;">${date}</td>	
								<td style="text-align: left;">
								 ${creditNote.description == 1 ? "Sales return" : creditNote.description  == 2 ? "Post sale discount" : creditNote.description  == 3 ? "Deficiency in services" : creditNote.description  == 4 ? "Correction in invoices":creditNote.description  == 5 ? "Change in POS" :creditNote.description  == 6 ? "Finalization of provisional assessment" : creditNote.remark}
							</td>
							
					</tr>
			</c:if>
				</c:forEach>

					<tr style="border:thin solid  !important ">
						<td></td>
						<td></td>
						<td></td>
						<td></td>

						<td><b><fmt:formatNumber type="number"
									minFractionDigits="2" maxFractionDigits="2"
									value="${Totalcredit}" /></b></td>

						<td><b><fmt:formatNumber type="number"
									minFractionDigits="2" maxFractionDigits="2"
									value="${row_count_sales}" /></b></td>

					</tr>
				</tbody>
			</table>
			</font>
		</div>
		<!-- Excel End -->

		<!-- Hidden table pdf for columner start -->

		<div class="table-scroll" style="display: none;" id="tableDiv">

		<c:set var="rowcount" value="0" scope="page" />
		<c:if test="${rowcount == 0}">
		

			<table id="Hiddentable">

				<tr>
					<td></td>
					<td></td>
					<td style="color: blue; margin-left: 50px;">Credit Note Report</td>
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
							
						From: 
						</td>
						<td></td>
						<td>${from_date} To ${to_date}</td>
					</tr>
					<tr>
					
					<td colspan='6'>
					CIN:
					<c:if
							test="${company.company_statutory_type.company_statutory_id==10 || company.company_statutory_type.company_statutory_id==14}">
					 ${company.registration_no}
				    </c:if>	
					</td>
					</tr>
					
				<tr>
					<th data-field="Cr note Date" data-filter-control="input"
						data-sortable="true">Cr note Date</th>
				    <th data-field="Cr note Number" data-filter-control="input"
						data-sortable="true"> Cr note Number</th>
					<th data-field="particulars" data-filter-control="input"
						data-sortable="true">Particulars</th>
					<th data-field="voucherType" data-filter-control="input"
						data-sortable="true">Voucher Type</th>
					<th data-field="credit" data-filter-control="input"
						data-sortable="true">Credit  Note Amount</th>
					<th data-field="Original Invoice amount"
						data-filter-control="input" data-sortable="true"> Original Invoice amount</th>
							<th data-field="Original Inv no." data-filter-control="input"
						data-sortable="true" style="text-align: center;">Original Inv no.</th>
					<th data-field="Original Invoice date" data-filter-control="input"
						data-sortable="true" style="text-align: center;">Original Invoice date.</th>
					<th data-field="Reason for Cr note" data-filter-control="input"
						data-sortable="true" style="text-align: center;">Amount (Rs.)</th>
				</tr>
				</c:if>
					
					<tbody>
					<c:set var="row_count_sales" value="0" />
					<c:forEach var="creditNote" items="${creditNoteList}">
				<c:if test="${creditNote.customer != null}">
					<tr>
						<td style="text-align: left;">
								<fmt:parseDate value="${creditNote.date}" pattern="yyyy-MM-dd"
										var="parsedDate" type="date" />
                   				<fmt:formatDate value="${parsedDate}"
										var="createdDate" type="date" pattern="dd-MM-yyyys" />
								${createdDate}						
						</td>
						<td style="text-align: left;">${creditNote.voucher_no} </td>
						<td style="text-align: left;">
							<div>${creditNote.customer.firm_name} against ${creditNote.sales_bill_id.voucher_no}</div>
						</td>
						<td style="text-align: left;">Credit Note</td>
						
							
						<td class='tright'>
						<fmt:formatNumber type="number" minFractionDigits="2"
										maxFractionDigits="2" value="${creditNote.round_off+creditNote.tds_amount}" />
						</td>
						<td class='tright'>				
							<fmt:formatNumber type="number" minFractionDigits="2"
										maxFractionDigits="2"
										value="${creditNote.sales_bill_id.round_off+creditNote.sales_bill_id.tds_amount}" />
							<c:set var="row_count_sales"
										value="${row_count_sales + creditNote.sales_bill_id.round_off + creditNote.sales_bill_id.tds_amount}" />					
						</td>
							<td style="text-align: left;">${creditNote.sales_bill_id.voucher_no}</td>			
					
						<fmt:parseDate value="${creditNote.date}" pattern="yyyy-MM-dd"
									var="parsedDate" type="date" />
							<fmt:formatDate value="${parsedDate}" var="date" type="date"
									pattern="dd-MM-yyyy" />
							<td style="text-align: left;">${date}</td>	
								<td style="text-align: left;">
								 ${creditNote.description == 1 ? "Sales return" : creditNote.description  == 2 ? "Post sale discount" : creditNote.description  == 3 ? "Deficiency in services" : creditNote.description  == 4 ? "Correction in invoices":creditNote.description  == 5 ? "Change in POS" :creditNote.description  == 6 ? "Finalization of provisional assessment" : creditNote.remark}
							</td>
							
					</tr>
			</c:if>
				</c:forEach>
				
				</tbody>
					
					<tfoot style='background-color: #eee'>
					<tr>
						<td>Total</td>
						<td></td>
						<td></td>
						<td></td>
						<td class='tright'><b><fmt:formatNumber type="number"
									minFractionDigits="2" maxFractionDigits="2"
									value="${Totalcredit}" /></b></td>
						<td class='tright'><b><fmt:formatNumber type="number"
									minFractionDigits="2" maxFractionDigits="2"
									value="${row_count_sales}" /></b></td>
					</tr>
				</tfoot>
		</table>
	</div>
					
		
		<!-- Hidden table pdf for columner end -->
		
		<!-- Hidden table pdf view for columner start -->
		
		<div class="table-scroll">
		
		<c:set var="rowcount" value="0" scope="page" />
		<c:if test="${rowcount == 0}">
		
		<table id="table" data-toggle="table" data-search="false"
				data-escape="false" data-filter-control="true"
				data-show-export="false" data-click-to-select="true"
				data-pagination="true" data-page-size="10" data-toolbar="#toolbar"
				class="table">
			<thead>
				<tr>
					<th data-field="Cr note Date" data-filter-control="input"
							data-sortable="true">Cr note Date</th>
				    <th data-field="Cr note Number" data-filter-control="input"
							data-sortable="true"> Cr note Number</th>
					<th data-field="particulars" data-filter-control="input"
							data-sortable="true">Particulars</th>
					<th data-field="voucherType" data-filter-control="input"
							data-sortable="true">Voucher Type</th>
					<th data-field="credit" data-filter-control="input"
							data-sortable="true">Credit  Note Amount</th>
					<th data-field="Original Invoice amount"
							data-filter-control="input" data-sortable="true"> Original Invoice amount</th>
							<th data-field="Original Inv no." data-filter-control="input"
							data-sortable="true" style="text-align: center;">Original Inv no.</th>
					<th data-field="Original Invoice date" data-filter-control="input"
							data-sortable="true" style="text-align: center;">Original Invoice date.</th>
					<th data-field="Reason for Cr note" data-filter-control="input"
							data-sortable="true" style="text-align: center;">Reason for Cr note</th>
				</tr>
				</c:if>
			</thead>
				<c:set var="row_count_sales" value="0" />
			<tbody>
				<c:forEach var="creditNote" items="${creditNoteList}">
				<c:if test="${creditNote.customer != null}">
					<tr>
						<td style="text-align: left;">
								<fmt:parseDate value="${creditNote.date}" pattern="yyyy-MM-dd"
										var="parsedDate" type="date" />
                   				<fmt:formatDate value="${parsedDate}"
										var="createdDate" type="date" pattern="dd-MM-yyyy" />
								${createdDate}						
						</td>
						<td style="text-align: left;">${creditNote.voucher_no} </td>
						<td style="text-align: left;">
							<div>${creditNote.customer.firm_name} against ${creditNote.sales_bill_id.voucher_no}</div>
						</td>
						<td style="text-align: left;">Credit Note</td>
						
							
						<td class='tright'>
						<fmt:formatNumber type="number" minFractionDigits="2"
										maxFractionDigits="2" value="${creditNote.round_off+creditNote.tds_amount}" />
						</td>
						<td class='tright'>				
							<fmt:formatNumber type="number" minFractionDigits="2"
										maxFractionDigits="2"
										value="${creditNote.sales_bill_id.round_off+creditNote.sales_bill_id.tds_amount}" />
							<c:set var="row_count_sales"
										value="${row_count_sales + creditNote.sales_bill_id.round_off + creditNote.sales_bill_id.tds_amount}" />					
						</td>
							<td style="text-align: left;">${creditNote.sales_bill_id.voucher_no}</td>			
					
						<fmt:parseDate value="${creditNote.date}" pattern="yyyy-MM-dd"
									var="parsedDate" type="date" />
							<fmt:formatDate value="${parsedDate}" var="date" type="date"
									pattern="dd-MM-yyyy" />
							<td style="text-align: left;">${date}</td>	
								<td style="text-align: left;">
								 ${creditNote.description == 1 ? "Sales return" : creditNote.description  == 2 ? "Post sale discount" : creditNote.description  == 3 ? "Deficiency in services" : creditNote.description  == 4 ? "Correction in invoices":creditNote.description  == 5 ? "Change in POS" :creditNote.description  == 6 ? "Finalization of provisional assessment" : creditNote.remark}
							</td>
							
					</tr>
			</c:if>
				</c:forEach>
			</tbody>
			
			<tfoot style='background-color: #eee'>
					<tr>
						<td>Total</td>
						<td></td>
						<td></td>
						<td></td>
						<td class='tright'><b><fmt:formatNumber type="number"
									minFractionDigits="2" maxFractionDigits="2"
									value="${Totalcredit}" /></b></td>
						<td class='tright'><b><fmt:formatNumber type="number"
									minFractionDigits="2" maxFractionDigits="2"
									value="${row_count_sales}" /></b></td>
			 			<td></td>
						<td></td>
						<td></td>
					</tr>
				</tfoot>
		</table>
	</div>
</c:if>
<c:if test="${(option==1)}">


	<c:set var="Totalcredit" value="0" />
	
	<c:forEach var="entry" items="${creditNoteList}">
	<c:if test="${entry.customer != null}">
	 <c:set var="Totalcredit" value="${Totalcredit + entry.round_off}" />
	</c:if>	
	
	</c:forEach>
		<!-- Excel Start -->
		<div style="display:none" id="excel_report">
				
					<!-- Date -->
					<font size="11" face="verdana" >
					<table>
					<tr style="text-align:center;"><td colspan='6'><b>Credit Note Register</b></td>
				</tr>	
					<tr ></tr>
						<tr style="text-align:center;">
					<td colspan='6'>Company Name: ${company.company_name}</td>
				</tr>
						<tr style="text-align:center;">
					<td colspan='6'>Address: ${company.permenant_address}</td>
				</tr>
						<tr style="text-align:center;">
					<td colspan='6'>
								<fmt:parseDate value="${from_date}" pattern="yyyy-MM-dd"
							var="parsedDate" type="date" />
	                   			 <fmt:formatDate value="${parsedDate}"
							var="from_date" type="date" pattern="dd-MM-yyyy" />
	                   			 <fmt:parseDate value="${to_date}"
							pattern="yyyy-MM-dd" var="parsedDate" type="date" />
	                   			 <fmt:formatDate value="${parsedDate}"
							var="to_date" type="date" pattern="dd-MM-yyyy" />
						From ${from_date} To ${to_date}</td>
				</tr>
						<tr style="text-align:center;">
					<td colspan='6'>
						CIN:
						<c:if
							test="${company.company_statutory_type.company_statutory_id==10 || company.company_statutory_type.company_statutory_id==14}">
						 ${company.registration_no}
					    </c:if>	
						</td>
				</tr>
					</table>
					</font>
			<!-- Date -->
			<font size="11" face="verdana" >
		<table style="border:1pt solid  !important  border-collapse: collapse;">
			<thead>
				<tr style="border:thin solid  !important ">
					<th data-field="Cr note Date" data-filter-control="input"
							data-sortable="true">Cr note Date</th>
				    <th data-field="Cr note Number" data-filter-control="input"
							data-sortable="true"> Cr note Number</th>
					<th data-field="particulars" data-filter-control="input"
							data-sortable="true">Particulars</th>
					<th data-field="voucherType" data-filter-control="input"
							data-sortable="true">Voucher Type</th>
					<th data-field="credit" data-filter-control="input"
							data-sortable="true">Credit  Note Amount</th>
					<th data-field="Original Invoice amount"
							data-filter-control="input" data-sortable="true"> Amount(Rs.)</th>
				</tr>
			</thead>
				<c:set var="row_count_sales" value="0" />
			<tbody>
				<c:forEach var="creditNote" items="${creditNoteList}">
				<c:if test="${creditNote.customer != null}">
					<tr style="border:thin solid  !important ">
						<td style="text-align: left;">
								<fmt:parseDate value="${creditNote.date}" pattern="yyyy-MM-dd"
										var="parsedDate" type="date" />
                   				<fmt:formatDate value="${parsedDate}"
										var="createdDate" type="date" pattern="dd-MM-yyyy" />
								${createdDate}
						</td>
						<td style="text-align: left;">${creditNote.voucher_no}</td>
						<td style="text-align: left;">
							<div>${creditNote.customer.firm_name} against ${creditNote.sales_bill_id.voucher_no}</div>							
						</td>
						<td style="text-align: left;">Credit Note</td>
						
						
						
						<td class='tright'>
						<fmt:formatNumber type="number" minFractionDigits="2"
										maxFractionDigits="2" value="${creditNote.round_off+creditNote.tds_amount}" />
						</td>
						
						<td class='tright'>						
							<fmt:formatNumber type="number" minFractionDigits="2"
										maxFractionDigits="2"
										value="${creditNote.sales_bill_id.round_off+creditNote.sales_bill_id.tds_amount}" />
							<c:set var="row_count_sales"
										value="${row_count_sales + creditNote.sales_bill_id.round_off+creditNote.sales_bill_id.tds_amount}" />						
						</td>
					</tr>
				</c:if>
				</c:forEach>		
					<tr style="border:thin solid  !important ">
						<td>Total</td>
						<td></td>
						<td></td>
						<td></td>
						
						<td><b><fmt:formatNumber type="number"
									minFractionDigits="2" maxFractionDigits="2"
									value="${Totalcredit}" /></b></td>
			 		
			 		
						<td><b><fmt:formatNumber type="number"
									minFractionDigits="2" maxFractionDigits="2"
									value="${row_count_sales}" /></b></td>
			 	
					</tr>
				</tbody>
		</table>
		</font>
				</div>
	<!-- Excel End -->
	
	
	<!-- pdf for condensed start  -->
	
	<div class="table-scroll" style="display:none;" id="tableDiv">
					
					<c:set var="rowcount" value="0" scope="page" />
					<c:if test="${rowcount == 0}">
			
	
			<table id="Hiddentable">
			
					<tr>
						<td></td>
						<td></td>
						<td style="color:blue; margin-left: 50px;">Credit Note Report</td>
					</tr>
			
					<tr>
						<td align="center">Company Name: </td>
						<td></td>
						<td align="center">${company.company_name}</td>
					</tr>
					<tr>
							<td align="center">Address: </td>
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
						<td>${from_date} To ${to_date}</td>
					</tr>
					<tr>
					
					<td colspan='3'>
					CIN:
					<c:if
							test="${company.company_statutory_type.company_statutory_id==10 || company.company_statutory_type.company_statutory_id==14}">
					 ${company.registration_no}
				    </c:if>	
					</td>
					</tr>
					<tr>
					<th data-field="Cr note Date" data-filter-control="input"
						data-sortable="true">Cr note Date</th>
				    <th data-field="Cr note Number" data-filter-control="input"
						data-sortable="true"> Cr note Number</th>
					<th data-field="particulars" data-filter-control="input"
						data-sortable="true">Particulars</th>
					<th data-field="voucherType" data-filter-control="input"
						data-sortable="true">Voucher Type</th>
					<th data-field="credit" data-filter-control="input"
						data-sortable="true">Credit  Note Amount</th>
					<th data-field="Original Invoice amount"
						data-filter-control="input" data-sortable="true"> Amount(Rs.)</th>
				</tr>
				</c:if>
				
				<c:set var="row_count_sales" value="0" />
			<tbody>
				<c:forEach var="creditNote" items="${creditNoteList}">
				<c:if test="${creditNote.customer != null}">
					<tr>
													
						<td style="text-align: left;">
								<fmt:parseDate value="${creditNote.date}" pattern="yyyy-MM-dd"
										var="parsedDate" type="date" />
                   				<fmt:formatDate value="${parsedDate}"
										var="createdDate" type="date" pattern="dd-MM-yyyy" />
								${createdDate}
						</td>
						<td style="text-align: left;">${creditNote.voucher_no}</td>
						<td style="text-align: left;">
							<div>${creditNote.customer.firm_name} against ${creditNote.sales_bill_id.voucher_no}</div>							
						</td>
						<td style="text-align: left;">Credit Note</td>
						
						
						
						<td class='tright'>
						<fmt:formatNumber type="number" minFractionDigits="2"
										maxFractionDigits="2" value="${creditNote.round_off+creditNote.tds_amount}" />
						</td>
						
						<td class='tright'>						
							<fmt:formatNumber type="number" minFractionDigits="2"
										maxFractionDigits="2"
										value="${creditNote.sales_bill_id.round_off+creditNote.sales_bill_id.tds_amount}" />
							<c:set var="row_count_sales"
										value="${row_count_sales + creditNote.sales_bill_id.round_off+creditNote.sales_bill_id.tds_amount}" />						
						</td>
					</tr>
				</c:if>
				</c:forEach>
			</tbody>
			<tfoot style='background-color: #eee'>
					<tr>
						<td>Total</td>
						<td></td>
						<td></td>
						<td></td>
						
						<td class='tright'><b><fmt:formatNumber type="number"
									minFractionDigits="2" maxFractionDigits="2"
									value="${Totalcredit}" /></b></td>
						<td class='tright'><b><fmt:formatNumber type="number"
									minFractionDigits="2" maxFractionDigits="2"
									value="${row_count_sales}" /></b></td>
					</tr>
				</tfoot>
		</table>
	</div>
					
	
	<!-- pdf for condensed end  -->
	
	
	
	<!-- pdf view for condensed -->
<div class="table-scroll">

	<c:set var="rowcount" value="0" scope="page" />
		
		<c:if test="${rowcount == 0}">
		
		<table id="table" data-toggle="table" data-search="false"
				data-escape="false" data-filter-control="true"
				data-show-export="false" data-click-to-select="true"
				data-pagination="true" data-page-size="10" data-toolbar="#toolbar"
				class="table">
			<thead>
				<tr>
					<th data-field="Cr note Date" data-filter-control="input"
							data-sortable="true">Cr note Date</th>
				    <th data-field="Cr note Number" data-filter-control="input"
							data-sortable="true"> Cr note Number</th>
					<th data-field="particulars" data-filter-control="input"
							data-sortable="true">Particulars</th>
					<th data-field="voucherType" data-filter-control="input"
							data-sortable="true">Voucher Type</th>
					<th data-field="credit" data-filter-control="input"
							data-sortable="true">Credit  Note Amount</th>
					<th data-field="Original Invoice amount"
							data-filter-control="input" data-sortable="true"> Original Invoice amount</th>
							
				</tr>
				</c:if>
				
			</thead>
				<c:set var="row_count_sales" value="0" />
			<tbody>
				<c:forEach var="creditNote" items="${creditNoteList}">
				<c:if test="${creditNote.customer != null}">
					<tr>
					
						<td style="text-align: left;">
								<fmt:parseDate value="${creditNote.date}" pattern="yyyy-MM-dd"
										var="parsedDate" type="date" />
                   				<fmt:formatDate value="${parsedDate}"
										var="createdDate" type="date" pattern="dd-MM-yyyy" />
								${createdDate}
						</td>
						<td style="text-align: left;">${creditNote.voucher_no}</td>
						<td style="text-align: left;">
							<div>${creditNote.customer.firm_name} against ${creditNote.sales_bill_id.voucher_no}</div>							
						</td>
						<td style="text-align: left;">Credit Note</td>
						
						
						
						<td class='tright'>
						<fmt:formatNumber type="number" minFractionDigits="2"
										maxFractionDigits="2" value="${creditNote.round_off+creditNote.tds_amount}" />
						</td>
						
						<td class='tright'>						
							<fmt:formatNumber type="number" minFractionDigits="2"
										maxFractionDigits="2"
										value="${creditNote.sales_bill_id.round_off+creditNote.sales_bill_id.tds_amount}" />
							<c:set var="row_count_sales"
										value="${row_count_sales + creditNote.sales_bill_id.round_off+creditNote.sales_bill_id.tds_amount}" />						
						</td>
					</tr>
				</c:if>
				</c:forEach>
			</tbody>
			<tfoot style='background-color: #eee'>
					<tr>
						<td>Total</td>
						<td></td>
						<td></td>
						<td></td>
						
						<td class='tright'><b><fmt:formatNumber type="number"
									minFractionDigits="2" maxFractionDigits="2"
									value="${Totalcredit}" /></b></td>
						<td class='tright'><b><fmt:formatNumber type="number"
									minFractionDigits="2" maxFractionDigits="2"
									value="${row_count_sales}" /></b></td>
					</tr>
				</tfoot>
		</table>
	</div>
</c:if>
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
				Download as PDF
			</button>		
			<button class="fassetBtn" type="button" id='btnExport3'
				onclick='exportexcel("CreditNote-Report")'>
				Download as Excel
			</button>
		</c:if>
	 	<button class="fassetBtn" type="button" onclick="back();">
			<spring:message code="btn_back" />
		</button>
	</div>
</div>
<script type="text/javascript">
	function back() {
		window.location.assign('<c:url value = "creditNoteReport"/>');
	}

	/* function pdf(){
		window.location.assign('<c:url value = "pdfCreditNoteRegister"/>');
	} */
</script>
<%@include file="/WEB-INF/includes/footer.jsp"%>