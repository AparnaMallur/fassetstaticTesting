<%@include file="/WEB-INF/includes/header.jsp"%>
<%@page import="com.fasset.controller.abstracts.MyAbstractController"%>
<spring:url value="/resources/images/edit.png" var="editImg" />
<spring:url value="/resources/images/view.png" var="viewImg" />
<script type="text/javascript" src="${valid}"></script>

<spring:url value="/resources/js/jspdf.min.js" var="jspdfmin" />
<spring:url value="/resources/js/jspdf.plugin.autotable.js"
	var="jspdfauto" />
<spring:url value="/resources/js/report_table/debitNoteReport.js"
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
	<h3>Debit Note Register</h3>
	<a href="homePage">Home</a> » <a href="debitNoteReport">Debit Note
		Register</a>
</div>
<div class="col-md-12">



	<c:set var="Totaldebit" value="0" />



	<c:forEach var="entry" items="${debitNoteList}">
		<c:if test="${(entry.supplier!=null)}">


			<c:set var="Totaldebit" value="${Totaldebit + entry.round_off}" />
		</c:if>
	</c:forEach>

	<c:if test="${(option==2)}">
		<!-- Excel Start -->
		<div style="display: none" id="excel_report">

			<!-- Date -->
			<font size="11" face="verdana" >
			<table>
				<tr style="text-align:center;"><td colspan='9'><b>Debit Note Register</b></td>
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
						<th data-field="date" data-filter-control="input"
							data-sortable="true">Dr note Date</th>
						<th data-field="voucherNumber" data-filter-control="input"
							data-sortable="true">Dr note Number</th>
						<th data-field="particulars" data-filter-control="input"
							data-sortable="true">Particulars</th>
						<th data-field="voucherType" data-filter-control="input"
							data-sortable="true">Voucher Type</th>

						<th data-field="debit" data-filter-control="input"
							data-sortable="true">Debit Note Amount</th>

						<th data-field="Original Invoice amount"
							data-filter-control="input" data-sortable="true">Original
							Invoice amount11</th>
						<th data-field="Original Inv no." data-filter-control="input"
							data-sortable="true" style="text-align: center;">Original
							Inv no.</th>

						<th data-field="Original Invoice date" data-filter-control="input"
							data-sortable="true" style="text-align: center;">Original
							Invoice date.</th>

						<th data-field="Reason for Dr note" data-filter-control="input"
							data-sortable="true" style="text-align: center;">Reason for
							Dr note</th>
					</tr>
				</thead>
				<c:set var="row_count_sales" value="0" />

				<tbody>
					<c:forEach var="debitNote" items="${debitNoteList}">

						<c:if test="${debitNote.supplier!=null}">
							<tr style="border:thin solid  !important ">
								<td style="text-align: left;"><fmt:parseDate
										value="${debitNote.date}" pattern="yyyy-MM-dd"
										var="parsedDate" type="date" /> <fmt:formatDate
										value="${parsedDate}" var="createdDate" type="date"
										pattern="dd-MM-yyyy" /> ${createdDate}</td>
								<td style="text-align: left;">${debitNote.voucher_no}</td>
								<td style="text-align: left;">
									<div>
										   ${debitNote.supplier.company_name} against
											${debitNote.purchase_bill_id.voucher_no} 
									</div>
								</td>
								<td style="text-align: left;">Debit Note</td>

								<td class="tright"><fmt:formatNumber type="number"
										minFractionDigits="2" maxFractionDigits="2"
										value="${debitNote.round_off+debitNote.tds_amount}" /></td>

								<td style="text-align: right;"><fmt:formatNumber
										type="number" minFractionDigits="2" maxFractionDigits="2"
										value="${debitNote.purchase_bill_id.round_off+debitNote.purchase_bill_id.tds_amount}" /> <c:set
										var="row_count_sales"
										value="${row_count_sales + debitNote.purchase_bill_id.round_off+debitNote.purchase_bill_id.tds_amount}" />
								</td>


								<td style="text-align: right;">${debitNote.purchase_bill_id.supplier_bill_no}</td>

								<fmt:parseDate value="${debitNote.purchase_bill_id.supplier_bill_date}" pattern="yyyy-MM-dd"
									var="parsedDate" type="date" />
								<fmt:formatDate value="${parsedDate}" var="date" type="date"
									pattern="dd-MM-yyyy" />
								<td style="text-align: left;">${date}</td>
								<td style="text-align: left;">${debitNote.description == 1 ? "Sales return" : debitNote.description  == 2 ? "Post sale discount" : debitNote.description  == 3 ? "Deficiency in services" : debitNote.description  == 4 ? "Correction in invoices":debitNote.description  == 5 ? "Change in POS" :debitNote.description  == 6 ? "Finalization of provisional assessment" : "Others"}</td>
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
									value="${Totaldebit}" /></b></td>
						<td><b><fmt:formatNumber type="number"
									minFractionDigits="2" maxFractionDigits="2"
									value="${row_count_sales}" /></b></td>
						<td></td>
						<td></td>
						<td></td>
					</tr>
				</tbody>
			</table>
			</font>
		</div>
		<!-- Excel End -->

		<!-- pdf for columner hidden start  -->

		<div class="table-scroll" style="display: none;" id="tableDiv">
			
			
			<c:set var="rowcount" value="0" scope="page" />
			<c:if test="${rowcount == 0}">
			
			
			<table id="Hiddentable">

				<tr>
					<td></td>
					<td></td>
					<td style="color: blue; margin-left: 50px;">Debit Note Report
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

					<td colspan='6'>CIN: <c:if
							test="${company.company_statutory_type.company_statutory_id==10 || company.company_statutory_type.company_statutory_id==14}">
					 ${company.registration_no}
				    </c:if>
					</td>
				</tr>

				<tr>

					<th data-field="date" data-filter-control="input"
						data-sortable="true">Dr note Date</th>
					<th data-field="voucherNumber" data-filter-control="input"
						data-sortable="true">Dr note Number</th>
					<th data-field="particulars" data-filter-control="input"
						data-sortable="true">Particulars</th>
					<th data-field="voucherType" data-filter-control="input"
						data-sortable="true">Voucher Type</th>
					<th data-field="debit" data-filter-control="input"
						data-sortable="true">Debit Note Amount</th>
					<th data-field="Original Invoice amount"
						data-filter-control="input" data-sortable="true">Original
						Invoice amount22</th>
					<th data-field="Original Inv no." data-filter-control="input"
						data-sortable="true" style="text-align: center;">Original Inv
						no.</th>
					<th data-field="Original Invoice date" data-filter-control="input"
						data-sortable="true" style="text-align: center;">Original
						Invoice date.</th>
					<th data-field="Reason for Dr note" data-filter-control="input"
						data-sortable="true" style="text-align: center;">Amount (Rs.)</th>
				</tr>
				</c:if>
				<tbody>
					<c:set var="row_count_sales" value="0" />
					<c:forEach var="debitNote" items="${debitNoteList}">

						<c:if test="${debitNote.supplier!=null}">
							<tr>							
								<td style="text-align: left;"><fmt:parseDate
										value="${debitNote.date}" pattern="yyyy-MM-dd"
										var="parsedDate" type="date" /> <fmt:formatDate
										value="${parsedDate}" var="createdDate" type="date"
										pattern="dd-MM-yyyy" /> ${createdDate}</td>
								<td style="text-align: left;">${debitNote.voucher_no}</td>
								<td style="text-align: left;">
									<div>
										   ${debitNote.supplier.company_name} against
											${debitNote.purchase_bill_id.voucher_no} 
									</div>
								</td>
								<td style="text-align: left;">Debit Note</td>

								<td class="tright"><fmt:formatNumber type="number"
										minFractionDigits="2" maxFractionDigits="2"
										value="${debitNote.round_off+debitNote.tds_amount}" /></td>

								<td style="text-align: right;"><fmt:formatNumber
										type="number" minFractionDigits="2" maxFractionDigits="2"
										value="${debitNote.purchase_bill_id.round_off+debitNote.purchase_bill_id.tds_amount}" /> <c:set
										var="row_count_sales"
										value="${row_count_sales + debitNote.purchase_bill_id.round_off+debitNote.purchase_bill_id.tds_amount}" />
								</td>


								<td style="text-align: right;">${debitNote.purchase_bill_id.supplier_bill_no}</td>

								<fmt:parseDate value="${debitNote.purchase_bill_id.supplier_bill_date}" pattern="yyyy-MM-dd"
									var="parsedDate" type="date" />
								<fmt:formatDate value="${parsedDate}" var="date" type="date"
									pattern="dd-MM-yyyy" />
								<td style="text-align: left;">${date}</td>
								<td style="text-align: left;">${debitNote.description == 1 ? "Sales return" : debitNote.description  == 2 ? "Post sale discount" : debitNote.description  == 3 ? "Deficiency in services" : debitNote.description  == 4 ? "Correction in invoices":debitNote.description  == 5 ? "Change in POS" :debitNote.description  == 6 ? "Finalization of provisional assessment" : "Others"}</td>
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
						<td class="tright"><b><fmt:formatNumber type="number"
									minFractionDigits="2" maxFractionDigits="2"
									value="${Totaldebit}" /></b></td>
						<td class="tright"><b><fmt:formatNumber type="number"
									minFractionDigits="2" maxFractionDigits="2"
									value="${row_count_sales}" /></b></td>
						<td></td>
						<td></td>
						<td></td>
					</tr>
				</tfoot>
			</table>
		</div>




		<!-- pdf for columner hidden end  -->


		<!-- columner view start -->


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

						<th data-field="date" data-filter-control="input"
							data-sortable="true">Dr note Date</th>
						<th data-field="voucherNumber" data-filter-control="input"
							data-sortable="true">Dr note Number</th>
						<th data-field="particulars" data-filter-control="input"
							data-sortable="true">Particulars</th>
						<th data-field="voucherType" data-filter-control="input"
							data-sortable="true">Voucher Type</th>
						<th data-field="debit" data-filter-control="input"
							data-sortable="true" style="text-align: right;">Debit Note Amount
							</th>
						<th data-field="purchaseAmount" data-filter-control="input"
							data-sortable="true" style="text-align: center;">Original
							Invoice amount</th>
						<th data-field="Original Inv no." data-filter-control="input"
							data-sortable="true" style="text-align: center;">Original
							Inv no.</th>
						<th data-field="Original Invoice date" data-filter-control="input"
							data-sortable="true" style="text-align: center;">Original
							Invoice date.</th>
						<th data-field="Reason for Dr note" data-filter-control="input"
							data-sortable="true" style="text-align: center;">Reason for
							Dr note</th>
					</tr>
					</c:if>
				</thead>
				<c:set var="row_count_sales" value="0" />
				<tbody>
					<c:forEach var="debitNote" items="${debitNoteList}">

						<c:if test="${debitNote.supplier!=null}">
							<tr>
								
								<td style="text-align: left;"><fmt:parseDate
										value="${debitNote.date}" pattern="yyyy-MM-dd"
										var="parsedDate" type="date" /> <fmt:formatDate
										value="${parsedDate}" var="createdDate" type="date"
										pattern="dd-MM-yyyy" /> ${createdDate}</td>
								<td style="text-align: left;">${debitNote.voucher_no}</td>
								<td style="text-align: left;">
									<div>
										   ${debitNote.supplier.company_name} against
											${debitNote.purchase_bill_id.voucher_no} 
									</div>
								</td>
								<td style="text-align: left;">Debit Note</td>

								<td class="tright"><fmt:formatNumber type="number"
										minFractionDigits="2" maxFractionDigits="2"
										value="${debitNote.round_off+debitNote.tds_amount}" /></td>

								<td style="text-align: right;"><fmt:formatNumber
										type="number" minFractionDigits="2" maxFractionDigits="2"
										value="${debitNote.purchase_bill_id.round_off+debitNote.purchase_bill_id.tds_amount}" /> <c:set
										var="row_count_sales"
										value="${row_count_sales + debitNote.purchase_bill_id.round_off+debitNote.purchase_bill_id.tds_amount}" />
								</td>


								<td style="text-align: right;">${debitNote.purchase_bill_id.supplier_bill_no}</td>

								<fmt:parseDate value="${debitNote.purchase_bill_id.supplier_bill_date}" pattern="yyyy-MM-dd"
									var="parsedDate" type="date" />
								<fmt:formatDate value="${parsedDate}" var="date" type="date"
									pattern="dd-MM-yyyy" />
								<td style="text-align: left;">${date}</td>
								<td style="text-align: left;">${debitNote.description == 1 ? "Sales return" : debitNote.description  == 2 ? "Post sale discount" : debitNote.description  == 3 ? "Deficiency in services" : debitNote.description  == 4 ? "Correction in invoices":debitNote.description  == 5 ? "Change in POS" :debitNote.description  == 6 ? "Finalization of provisional assessment" : "Others"}</td>
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
						<td class="tright"><b><fmt:formatNumber type="number"
									minFractionDigits="2" maxFractionDigits="2"
									value="${Totaldebit}" /></b></td>
						<td class="left"><b><fmt:formatNumber type="number"
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


		<c:set var="Totaldebit" value="0" />

		<c:forEach var="entry" items="${debitNoteList}">
			<c:if test="${entry.supplier != null}">

				<c:set var="Totaldebit" value="${Totaldebit + entry.round_off}" />
			</c:if>

		</c:forEach>
		<!-- Excel Start -->
		<div style="display: none" id="excel_report">
			<!-- Date -->
			<font size="11" face="verdana" >
			<table >
				<tr style="text-align:center;"><td colspan='6'><b>Debit Note Register</b></td>
				</tr>
				<tr></tr>
				<tr style="text-align:center;">
					<td colspan='6'>Company Name: ${company.company_name}</td>
				</tr>
				<tr style="text-align:center;">
					<td colspan='6'>Address: ${company.permenant_address}</td>
				</tr>
				<tr style="text-align:center;">
					<td colspan='6'><fmt:parseDate value="${from_date}"
							pattern="yyyy-MM-dd" var="parsedDate" type="date" /> <fmt:formatDate
							value="${parsedDate}" var="from_date" type="date"
							pattern="dd-MM-yyyy" /> <fmt:parseDate value="${to_date}"
							pattern="yyyy-MM-dd" var="parsedDate" type="date" /> <fmt:formatDate
							value="${parsedDate}" var="to_date" type="date"
							pattern="dd-MM-yyyy" /> From ${from_date} To ${to_date}</td>
				</tr>
				<tr style="text-align:center;">
					<td colspan='6'>CIN: <c:if
							test="${company.company_statutory_type.company_statutory_id==10 || company.company_statutory_type.company_statutory_id==14}">
						 ${company.registration_no}
					    </c:if>
					</td>
				</tr>
			</table >
			</font>
			<!-- Date -->
			<font size="11" face="verdana" >
			<table style="border:1pt solid  !important  border-collapse: collapse;">
				<thead>
					<tr style="border:thin solid  !important ">
						<th data-field="date" data-filter-control="input"
							data-sortable="true">Dr note Date</th>
						<th data-field="voucherNumber" data-filter-control="input"
							data-sortable="true">Dr note Number</th>
						<th data-field="particulars" data-filter-control="input"
							data-sortable="true">Particulars</th>
						<th data-field="voucherType" data-filter-control="input"
							data-sortable="true">Voucher Type</th>

						<th data-field="debit" data-filter-control="input"
							data-sortable="true">Debit Note Amount</th>

						<th data-field="purchaseAmount" data-filter-control="input"
							data-sortable="true">Amount (Rs.)</th>
					</tr>
				</thead>
				<c:set var="row_count_sales" value="0" />

				<tbody>
					<c:forEach var="debitNote" items="${debitNoteList}">
						<c:if test="${debitNote.supplier!=null}">
							<tr style="border:thin solid  !important ">
								<td style="text-align: left;"><fmt:parseDate
										value="${debitNote.date}" pattern="yyyy-MM-dd"
										var="parsedDate" type="date" /> <fmt:formatDate
										value="${parsedDate}" var="createdDate" type="date"
										pattern="dd-MM-yyyy" /> ${createdDate}</td>
								<td style="text-align: left;">${debitNote.voucher_no}</td>
								<td style="text-align: left;">
									<div>${debitNote.supplier.company_name}against
										${debitNote.purchase_bill_id.voucher_no}</div>

								</td>
								<td style="text-align: left;">Debit Note</td>

								<td class="tright"><fmt:formatNumber type="number"
										minFractionDigits="2" maxFractionDigits="2"
										value="${debitNote.round_off+debitNote.tds_amount}" /></td>
								<td class="tright"><fmt:formatNumber type="number"
										minFractionDigits="2" maxFractionDigits="2"
										value="${debitNote.purchase_bill_id.round_off+debitNote.purchase_bill_id.tds_amount}" /> <c:set
										var="row_count_sales"
										value="${row_count_sales + debitNote.purchase_bill_id.round_off+debitNote.purchase_bill_id.tds_amount}" />
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
									value="${Totaldebit}" /></b></td>
						<td><b><fmt:formatNumber type="number"
									minFractionDigits="2" maxFractionDigits="2"
									value="${row_count_sales}" /></b></td>
					</tr>
				</tbody>
			</table>
			</font>
		</div>
		<!-- Excel End -->

		<!-- pdf for condensed hidden table start -->

		<div class="table-scroll" style="display: none;" id="tableDiv">

			<c:set var="rowcount" value="0" scope="page" />
			<c:if test="${rowcount == 0}">
		
		
			<table id="Hiddentable">

				<tr>
					<td></td>
					<td></td>
					<td style="color: blue; margin-left: 50px;">Debit Note Report</td>
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
					<td>From:</td>
					<td></td>
					<td>${from_date}To ${to_date}</td>
				</tr>
				<tr>

					<td colspan='6'>CIN: <c:if
							test="${company.company_statutory_type.company_statutory_id==10 || company.company_statutory_type.company_statutory_id==14}">
					 ${company.registration_no}
				    </c:if>
					</td>
				</tr>

				<tr>
					<th data-field="date" data-filter-control="input"
						data-sortable="true">Dr note Date</th>
					<th data-field="voucherNumber" data-filter-control="input"
						data-sortable="true">Dr note Number</th>
					<th data-field="particulars" data-filter-control="input"
						data-sortable="true">Particulars</th>
					<th data-field="voucherType" data-filter-control="input"
						data-sortable="true">Voucher Type</th>

					<th data-field="debit" data-filter-control="input"
						data-sortable="true">Debit Note Amount</th>

					<th data-field="purchaseAmount" data-filter-control="input"
						data-sortable="true">Amount (Rs.)</th>
				</tr>
				</c:if>
				<c:set var="row_count_sales" value="0" />

				<tbody>
					<c:forEach var="debitNote" items="${debitNoteList}">
						<c:if test="${debitNote.supplier!=null}">
							<tr>
							<td style="text-align: left;"><fmt:parseDate
										value="${debitNote.date}" pattern="yyyy-MM-dd"
										var="parsedDate" type="date" /> <fmt:formatDate
										value="${parsedDate}" var="createdDate" type="date"
										pattern="dd-MM-yyyy" /> ${createdDate}</td>
								<td style="text-align: left;">${debitNote.voucher_no}</td>
								<td style="text-align: left;">
									<div>${debitNote.supplier.company_name}against
										${debitNote.purchase_bill_id.voucher_no}</div>

								</td>
								<td style="text-align: left;">Debit Note</td>

								<td class="tright"><fmt:formatNumber type="number"
										minFractionDigits="2" maxFractionDigits="2"
										value="${debitNote.round_off+debitNote.tds_amount}" /></td>
								<td class="tright"><fmt:formatNumber type="number"
										minFractionDigits="2" maxFractionDigits="2"
										value="${debitNote.purchase_bill_id.round_off+debitNote.purchase_bill_id.tds_amount}" /> <c:set
										var="row_count_sales"
										value="${row_count_sales + debitNote.purchase_bill_id.round_off+debitNote.purchase_bill_id.tds_amount}" />
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

						<td class="tright"><b><fmt:formatNumber type="number"
									minFractionDigits="2" maxFractionDigits="2"
									value="${Totaldebit}" /></b></td>

						<td class="tright"><b><fmt:formatNumber type="number"
									minFractionDigits="2" maxFractionDigits="2"
									value="${row_count_sales}" /></b></td>

					</tr>
				</tfoot>
			</table>
		</div>



		<!-- pdf for condensed hidden table end -->



		<!-- pdf view for condensed start -->
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
						<th data-field="date" data-filter-control="input"
							data-sortable="true">Dr note Date</th>
						<th data-field="voucherNumber" data-filter-control="input"
							data-sortable="true">Dr note Number</th>
						<th data-field="particulars" data-filter-control="input"
							data-sortable="true">Particulars</th>
						<th data-field="voucherType" data-filter-control="input"
							data-sortable="true">Voucher Type</th>

						<th data-field="debit" data-filter-control="input"
							data-sortable="true">Debit Note Amount</th>

						<th data-field="purchaseAmount" data-filter-control="input"
							data-sortable="true">Original Invoice amount</th>
					</tr>
			</c:if>
				</thead>


				<c:set var="row_count_sales" value="0" />

				<tbody>
					<c:forEach var="debitNote" items="${debitNoteList}">
						<c:if test="${debitNote.supplier!=null}">
							<tr>
						
								<td style="text-align: left;"><fmt:parseDate
										value="${debitNote.date}" pattern="yyyy-MM-dd"
										var="parsedDate" type="date" /> <fmt:formatDate
										value="${parsedDate}" var="createdDate" type="date"
										pattern="dd-MM-yyyy" /> ${createdDate}</td>
								<td style="text-align: left;">${debitNote.voucher_no}</td>
								<td style="text-align: left;">
									<div>${debitNote.supplier.company_name}against
										${debitNote.purchase_bill_id.voucher_no}</div>

								</td>
								<td style="text-align: left;">Debit Note</td>

								<td class="tright"><fmt:formatNumber type="number"
										minFractionDigits="2" maxFractionDigits="2"
										value="${debitNote.round_off+debitNote.tds_amount}" /></td>
								<td class="tright"><fmt:formatNumber type="number"
										minFractionDigits="2" maxFractionDigits="2"
										value="${debitNote.purchase_bill_id.round_off+debitNote.purchase_bill_id.tds_amount}" /> <c:set
										var="row_count_sales"
										value="${row_count_sales + debitNote.purchase_bill_id.round_off+debitNote.purchase_bill_id.tds_amount}" />
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
						<td class="tright"><b><fmt:formatNumber type="number"
									minFractionDigits="2" maxFractionDigits="2"
									value="${Totaldebit}" /></b></td>
						<td class="tright"><b><fmt:formatNumber type="number"
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
				Download as PDF</button>
			<button class="fassetBtn" type="button" id='btnExport3'
				onclick='exportexcel("DebitNote-Report")'>Download as Excel
			</button>
		</c:if>
		<button class="fassetBtn" type="button" onclick="back();">
			<spring:message code="btn_back" />
		</button>
	</div>
</div>
<script type="text/javascript">
	function back() {
		window.location.assign('<c:url value = "debitNoteReport"/>');
	}

	/* function pdf(){
		window.location.assign('<c:url value = "pdfDebitNoteRegister"/>');
	} */
</script>
<%@include file="/WEB-INF/includes/footer.jsp"%>