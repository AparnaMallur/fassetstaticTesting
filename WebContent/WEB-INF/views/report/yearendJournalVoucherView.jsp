<%@include file="/WEB-INF/includes/header.jsp"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<script type="text/javascript" src="${valid}"></script>
<spring:url value="/resources/js/jspdf.min.js" var="jspdfmin" />
<spring:url value="/resources/js/jspdf.plugin.autotable.js" var="jspdfauto" />
<spring:url	value="/resources/js/report_table/yearendJournalVoucher.js"	var="tableexport" />
 <script type="text/javascript" src="http://code.jquery.com/jquery-latest.min.js"></script>
 <script type="text/javascript" src="${jspdfmin}"></script>
 <script type="text/javascript" src="${jspdfauto}"></script>
 <script type="text/javascript" src="${tableexport}"></script>

	


<div class="breadcrumb">
	<h3>YearEnd Journal Voucher</h3>
	<a href="homePage">Home</a> » <a href="yearEndAutoJVList">YearEnd Journal Voucher</a>
</div>	
<div class="fassetForm">
	<form:form id="mjv" commandName="yearendjournalVoucher">
		<ul class="nav nav-tabs navtab-bg">
			<li class="active"><a href="#home" data-toggle="tab"
				aria-expanded="true"> <span class="visible-xs"> <i
						class="fa fa-home"></i>
				</span> <span class="hidden-xs">Details</span>
			</a></li>
			<li class=""><a href="#profile" data-toggle="tab"
				aria-expanded="false"> <span class="visible-xs"> <i
						class="fa fa-user"></i>
				</span> <span class="hidden-xs">Accounting Voucher</span>
			</a></li>
		</ul>
		<div class="tab-content">
			<div class="tab-pane active" id="home">
				<div class="row">
					<table class="table">
					
					 <c:if test="${((role == '2') || (role == '3') ||(role == '4'))}">
			              <tr>
							<td><strong>Company Name:</strong></td>
							<td style="text-align: left;">${yearendjournalVoucher.company.company_name}</td>
						 </tr>
				     </c:if>			
					      
					  <%-- <tr>			
						<td><strong>Created By</strong></td>							    
						<td style="text-align: left;">${yearendjournalVoucher.created_by}</td>
					
					 </tr> --%>
					
					 
						<tr>
							<td><strong>Voucher No:</strong></td>
							<td style="text-align: left;">${yearendjournalVoucher.voucher_no}</td>
						</tr>
						<tr>
							<td><strong>Date:</strong></td>
							<fmt:parseDate value="${yearendjournalVoucher.date}"
								pattern="yyyy-MM-dd" var="parsedDate" type="date" />
							<fmt:formatDate value="${parsedDate}" var="date" type="date"
								pattern="dd-MM-yyyy" />
							<td style="text-align: left;">${date}</td>
						</tr>
						<tr>
							<td><strong>Financial Year:</strong></td>
							<td style="text-align: left;">${yearendjournalVoucher.accountingYear.year_range}</td>
						</tr>

					
						<c:if test="${mjvDetailList.size() >0}">
							<table class="table table-bordered table-striped">
								<thead>
									<tr>
										<th>Sub-Ledger Name</th>
										<th>Amount(Dr)</th>
										<th>Amount(Cr)</th>
									</tr>
								</thead>
								<tbody>
									<c:forEach var="drmanualdetail" items="${drmanualdetail}">
							<tr>
								

								<td>${drmanualdetail.subledger.subledger_name}</td>


									<td class='tright'><fmt:formatNumber type="number" minFractionDigits="2"
											maxFractionDigits="2" value="${drmanualdetail.subLedgerAmount}" /></td>
                    

								<c:set var="amount_dr"
									value="${amount_dr + drmanualdetail.subLedgerAmount}" />
								<%-- <td class='tright'>${mjvDetailList.cramount}</td>
					         <c:set var="amount_cr" value="${amount_cr + drmanualdetail.cramount}" /> --%>
									<td></td>
							</tr>

						</c:forEach>
						<c:forEach var="crmanualdetail" items="${crmanualdetail}">
							<tr>

								<td>${crmanualdetail.subledger.subledger_name}</td>
								<td></td>

								
									<td class='tright'><fmt:formatNumber type="number" minFractionDigits="2"
											maxFractionDigits="2" value="${crmanualdetail.subLedgerAmount}" /></td>
                    

								<c:set var="amount_cr"
									value="${amount_cr + crmanualdetail.subLedgerAmount}" />
							</tr>

						</c:forEach>
							<%-- <tr>
								<td></td>
								<td class="tright"><b><fmt:formatNumber type="number"
											minFractionDigits="2" maxFractionDigits="2"
											value="${amount_dr}" /></b></td>
								<td class="tright"><b><fmt:formatNumber type="number"
											minFractionDigits="2" maxFractionDigits="2"
											value="${amount_cr}" /></b></td>
							</tr> --%>
						<tr>
						
						
						<c:set var="amount_total"
									value="${amount_cr + amount_dr}" />
									<c:if test="${amount_total>0}">
						<td>Total Profit</td>
						<td class="tright"><b><fmt:formatNumber type="number"
											minFractionDigits="2" maxFractionDigits="2"
											value="${amount_total}" /></b></td>
											<td></td>
											</c:if>
											
											<c:if test="${amount_total<0}">
						<td>Total Loss</td>
						<td class="tright"><b><fmt:formatNumber type="number"
											minFractionDigits="2" maxFractionDigits="2"
											value="${amount_total}" /></b></td>
											<td></td>
											</c:if>
											</tr>
											
						
								</tbody>
							</table>
						</c:if>
					</table>
				</div>
				<div class="row" style="text-align: center; margin: 15px;">
					
				
					<button class="fassetBtn" type="button" onclick="back();">
						<spring:message code="btn_back" />
					</button>
				</div>

			</div>


			<!--Profile UI view started  -->
			<div class="tab-pane" id="profile">
				<div class="row">
					<div class="col-md-12">
						<p style="text-align: center"><b>Journal Voucher</b></p>

					</div>
					<div class="col-md-12">
						     <p><b>Company Name: ${yearendjournalVoucher.company.company_name} 
							</b> </p>
							 <p> <b>
							YearEnd Journal Voucher No: ${yearendjournalVoucher.voucher_no}</b> </p>
						
						<p>
						 <fmt:parseDate value="${yearendjournalVoucher.date}" pattern="yyyy-MM-dd" var="parsedDate" type="date" />
                   			 <fmt:formatDate value="${parsedDate}" var="mjv_bill_date" type="date" pattern="dd-MM-yyyy"  />
                   			 
							 <b>Date: ${mjv_bill_date}</b> 
						</p>
					</div>
					<table class="table">
						<thead>
							<tr>
								<th style="width: 55%">Sub-Ledger Name</th>
								<th>Amount(Dr)</th>
								<th>Amount(Cr)</th>
							</tr>

						</thead>
						<tbody>
							<c:set var="amount_cr" value="0" />
							<c:set var="amount_dr" value="0" />
				<c:forEach var="drmanualdetail" items="${drmanualdetail}">
							<tr>
								

								<td>${drmanualdetail.subledger.subledger_name}</td>


									<td class='tright'><fmt:formatNumber type="number" minFractionDigits="2"
											maxFractionDigits="2" value="${drmanualdetail.subLedgerAmount}" /></td>
                    

								<c:set var="amount_dr"
									value="${amount_dr + drmanualdetail.subLedgerAmount}" />
								<%-- <td class='tright'>${mjvDetailList.cramount}</td>
					         <c:set var="amount_cr" value="${amount_cr + drmanualdetail.cramount}" /> --%>
									<td></td>
							</tr>

						</c:forEach>
						<c:forEach var="crmanualdetail" items="${crmanualdetail}">
							<tr>

								<td>${crmanualdetail.subledger.subledger_name}</td>
								<td></td>

								
									<td class='tright'><fmt:formatNumber type="number" minFractionDigits="2"
											maxFractionDigits="2" value="${crmanualdetail.subLedgerAmount}" /></td>
                    

								<c:set var="amount_cr"
									value="${amount_cr + crmanualdetail.subLedgerAmount}" />
							</tr>

						</c:forEach>
							<%-- <tr>
								<td></td>
								<td class="tright"><b><fmt:formatNumber type="number"
											minFractionDigits="2" maxFractionDigits="2"
											value="${amount_dr}" /></b></td>
								<td class="tright"><b><fmt:formatNumber type="number"
											minFractionDigits="2" maxFractionDigits="2"
											value="${amount_cr}" /></b></td>
							</tr> --%>
						
								<tr>
						
						
						<c:set var="amount_total"
									value="${amount_cr + amount_dr}" />
									<c:if test="${amount_total>0}">
						<td>Total Profit</td>
						<td class="tright"><b><fmt:formatNumber type="number"
											minFractionDigits="2" maxFractionDigits="2"
											value="${amount_total}" /></b></td>
											<td></td>
											</c:if>
											
											<c:if test="${amount_total<0}">
						<td>Total Loss</td>
						<td class="tright"><b><fmt:formatNumber type="number"
											minFractionDigits="2" maxFractionDigits="2"
											value="${amount_total}" /></b></td>
											<td></td>
											</c:if>
											</tr>
						</tbody>
					</table>
				</div>
				<div class="row" style="text-align: center; margin: 15px;">
					<button class="fassetBtn" type="button"
						onclick="manualyearendjournalVoucherAccountingVoucherpdf('#Hiddentable', {type: 'pdf',
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
					<button class="fassetBtn" type="button" onclick="back();">
						<spring:message code="btn_back" />
					</button>
				</div>
			</div>

			<div class="table-scroll" style="display: none;" id="tableDiv">

				<table id="Hiddentable">
					<!-- for PDf heading -->
					<tr>

						<td style="color: blue; margin-left: 50px; text-align: center;">YearEnd Journal Voucher</td>

					</tr>

					<tr>
					<td>
						 <fmt:parseDate value="${yearendjournalVoucher.date}" pattern="yyyy-MM-dd" var="parsedDate" type="date" />
                   			 <fmt:formatDate value="${parsedDate}" var="mjv_bill_date" type="date" pattern="dd-MM-yyyy"  />
                   			 
							 <b>Date: ${mjv_bill_date}</b> 
							 </td>
					</tr>


	                 <tr>
						<td>Company Name: ${yearendjournalVoucher.company.company_name}</td>
					</tr>
					<tr>
						<td>YearEnd Journal Voucher No : ${yearendjournalVoucher.voucher_no}</td>
					</tr>

					<tr>
					</tr>
					<tr>
					</tr>
						
							<tr>
								<th style="width: 55%">Sub-Ledger Name</th>
								<th class="tright">Amount(Dr)</th>
								<th class="tright">Amount(Cr)</th>

							</tr>
						
						<tbody>
							<c:set var="amount_cr" value="0" />
							<c:set var="amount_dr" value="0" />
				<c:forEach var="drmanualdetail" items="${drmanualdetail}">
							<tr>
								<td>${drmanualdetail.subledger.subledger_name}</td>

										<td class='tright'><fmt:formatNumber type="number" minFractionDigits="2"
											maxFractionDigits="2" value="${drmanualdetail.subLedgerAmount}" /></td>
                    

								<c:set var="amount_dr"
									value="${amount_dr + drmanualdetail.subLedgerAmount}" />
							
									<td></td>
							</tr>

						</c:forEach>
						<c:forEach var="crmanualdetail" items="${crmanualdetail}">
							<tr>

								<td>${crmanualdetail.subledger.subledger_name}</td>
								<td></td>

								
									<td class='tright'><fmt:formatNumber type="number" minFractionDigits="2"
											maxFractionDigits="2" value="${crmanualdetail.subLedgerAmount}" /></td>
                    

								<c:set var="amount_cr"
									value="${amount_cr + crmanualdetail.subLedgerAmount}" />
							</tr>

						</c:forEach>
							<tr>
						
						
						<c:set var="amount_total"
									value="${amount_cr + amount_dr}" />
									<c:if test="${amount_total>0}">
						<td>Total Profit</td>
						<td class="tright"><b><fmt:formatNumber type="number"
											minFractionDigits="2" maxFractionDigits="2"
											value="${amount_total}" /></b></td>
											<td></td>
											</c:if>
											
											<c:if test="${amount_total<0}">
						<td>Total Loss</td>
						<td class="tright"><b><fmt:formatNumber type="number"
											minFractionDigits="2" maxFractionDigits="2"
											value="${amount_total}" /></b></td>
											<td></td>
											</c:if>
											</tr>
											<%-- <tr>
											  <c:if test="${yearendjournalVoucher.remark!=null }"> 
								
                                  <td>Remark:  ${yearendjournalVoucher.remark}</td>
                              </c:if>
                              <c:if test="${yearendjournalVoucher.remark==null }"> 
								
                                  <td>Remark:  </td>
                              </c:if>
							</tr> --%>
								<%-- <tr>
								<td></td>
								<td class="tright"><b><fmt:formatNumber type="number"
											minFractionDigits="2" maxFractionDigits="2"
											value="${amount_dr}" /></b></td>
								<td class="tright"><b><fmt:formatNumber type="number"
											minFractionDigits="2" maxFractionDigits="2"
											value="${amount_cr}" /></b></td>
							</tr> --%>
						</tbody>
					</table>
				</div>
		</div>
		</form:form>
	
</div>



<script type="text/javascript">
function manualyearendjournalVoucherAccountingVoucherpdf(selector, params) {
	 selector = "#tableDiv";
	 $("#tableDiv").css("display", "block"); 	 	
   var options = {
     //ignoreRow: [1,11,12,-2],
     //ignoreColumn: [0,-1],
     //pdfmake: {enabled: true},
     tableName: 'Countries',
     worksheetName: 'Countries by population'
   };
   $.extend(true, options, params);
   $(selector).tableExport(options);
   $("#tableDiv").css("display", "none"); 	
 }
	
	function back(){
		window.location.assign('<c:url value = "yearEndAutoJVList"/>');	
	}
	/*  function DownloadPdf(id){
		window.location.assign('<c:url value = "downloadManualJV"/>?id='+id);	
	}  */
</script>
<%@include file="/WEB-INF/includes/footer.jsp"%>