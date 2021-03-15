<%@include file="/WEB-INF/includes/header.jsp"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<script type="text/javascript" src="${valid}"></script>
<spring:url value="/resources/js/jspdf.min.js" var="jspdfmin" />
<spring:url value="/resources/js/jspdf.plugin.autotable.js" var="jspdfauto" />
<spring:url	value="/resources/js/report_table/manualJournalVoucherAccountingVoucher.js"	var="tableexport" />
 <script type="text/javascript" src="http://code.jquery.com/jquery-latest.min.js"></script>
 <script type="text/javascript" src="${jspdfmin}"></script>
 <script type="text/javascript" src="${jspdfauto}"></script>
 <script type="text/javascript" src="${tableexport}"></script>

	


<div class="breadcrumb">
	<h3>Manual Journal Voucher</h3>
	<a href="homePage">Home</a> » <a href="manualjournalVoucherList">Manual
		Journal Voucher</a> » <a href="#">View</a>
</div>
<div class="fassetForm">
	<form:form id="mjv" commandName="journalVoucher">
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
							<td style="text-align: left;">${journalVoucher.company.company_name}</td>
						 </tr>
				     </c:if>			
					      
					  <tr>			
						<td><strong>Created By</strong></td>							    
						<td style="text-align: left;">${created_by.first_name} ${created_by.last_name}</td>
					
					 </tr>
					
					 <tr>			
						<td><strong>Updated By</strong></td>
						<td style="text-align: left;">${updated_by.first_name} ${updated_by.last_name}</td>
					 </tr>
						<tr>
							<td><strong>Voucher No:</strong></td>
							<td style="text-align: left;">${journalVoucher.voucher_no}</td>
						</tr>
						<tr>
							<td><strong>Date:</strong></td>
							<fmt:parseDate value="${journalVoucher.date}"
								pattern="yyyy-MM-dd" var="parsedDate" type="date" />
							<fmt:formatDate value="${parsedDate}" var="date" type="date"
								pattern="dd-MM-yyyy" />
							<td style="text-align: left;">${date}</td>
						</tr>
						<tr>
							<td><strong>Financial Year:</strong></td>
							<td style="text-align: left;">${journalVoucher.accounting_year_id.year_range}</td>
						</tr>

						<tr>
							<td><strong>Remark:</strong></td>
							<td style="text-align: left;">${journalVoucher.remark}</td>
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
								
                               <c:if test="${drmanualdetail.subledgerdr.subledger_Id >0}">
								<td>${drmanualdetail.subledgerdr.subledger_name}</td>
									</c:if>	
								<c:if test="${drmanualdetail.supplierdr.supplier_id >0}">
								<td>${drmanualdetail.supplierdr.company_name}</td>
									</c:if>	
									<c:if test="${drmanualdetail.customerdr.customer_id >0}">
								<td>${drmanualdetail.customerdr.firm_name}</td>
									</c:if>	
									
							<%-- 	<td>${drmanualdetail.subledgerdr.subledger_name}</td> --%>


									<td class='tright'><fmt:formatNumber type="number" minFractionDigits="2"
											maxFractionDigits="2" value="${drmanualdetail.dramount}" /></td>
                    

								<c:set var="amount_dr"
									value="${amount_dr + drmanualdetail.dramount}" />
								<%-- <td class='tright'>${mjvDetailList.cramount}</td>
					         <c:set var="amount_cr" value="${amount_cr + drmanualdetail.cramount}" /> --%>
									<td></td>
							</tr>

						</c:forEach>
						<c:forEach var="crmanualdetail" items="${crmanualdetail}">
							<tr>
                                <c:if test="${crmanualdetail.subledgercr.subledger_Id >0}">
									<td>${crmanualdetail.subledgercr.subledger_name}</td>
								</c:if>	
								<c:if test="${crmanualdetail.suppliercr.supplier_id >0}">
									<td>${crmanualdetail.suppliercr.company_name}</td>
								</c:if>	
								<c:if test="${crmanualdetail.customercr.customer_id >0}">
									<td>${crmanualdetail.customercr.firm_name}</td>
								</c:if>
								<%-- <td>${crmanualdetail.subledgercr.subledger_name}</td> --%>
								<td></td>

								
									<td class='tright'><fmt:formatNumber type="number" minFractionDigits="2"
											maxFractionDigits="2" value="${crmanualdetail.cramount}" /></td>
                    

								<c:set var="amount_cr"
									value="${amount_cr + crmanualdetail.cramount}" />
							</tr>

						</c:forEach>
						<tr>
						<td>Total</td>
						<td class="tright"><b><fmt:formatNumber type="number"
											minFractionDigits="2" maxFractionDigits="2"
											value="${amount_dr}" /></b></td>
								<td class="tright"><b><fmt:formatNumber type="number"
											minFractionDigits="2" maxFractionDigits="2"
											value="${amount_cr}" /></b></td>
											</tr>
								</tbody>
							</table>
						</c:if>
					</table>
				</div>
				<div class="row" style="text-align: center; margin: 15px;">
					<button class="fassetBtn" type="button"
						onclick="edit(${journalVoucher.journal_id})">
						<spring:message code="btn_edit" />
					</button>
				
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
						     <p><b>Company Name: ${journalVoucher.company.company_name} 
							</b> </p>
							 <p> <b>
							Manual Journal Voucher No: ${journalVoucher.voucher_no}</b> </p>
						
						<p>
						 <fmt:parseDate value="${journalVoucher.date}" pattern="yyyy-MM-dd" var="parsedDate" type="date" />
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
								
		                       <c:if test="${drmanualdetail.subledgerdr.subledger_Id >0}">
									<td>${drmanualdetail.subledgerdr.subledger_name}</td>
								</c:if>	
								<c:if test="${drmanualdetail.supplierdr.supplier_id >0}">
									<td>${drmanualdetail.supplierdr.company_name}</td>
								</c:if>	
								<c:if test="${drmanualdetail.customerdr.customer_id >0}">
									<td>${drmanualdetail.customerdr.firm_name}</td>
								</c:if>	

								<%-- <td>${drmanualdetail.subledgerdr.subledger_name}</td> --%>


									<td class='tright'><fmt:formatNumber type="number" minFractionDigits="2"
											maxFractionDigits="2" value="${drmanualdetail.dramount}" /></td>
                    

								<c:set var="amount_dr"
									value="${amount_dr + drmanualdetail.dramount}" />
								<%-- <td class='tright'>${mjvDetailList.cramount}</td>
					         <c:set var="amount_cr" value="${amount_cr + drmanualdetail.cramount}" /> --%>
									<td></td>
							</tr>

						</c:forEach>
						<c:forEach var="crmanualdetail" items="${crmanualdetail}">
							<tr>
                                <c:if test="${crmanualdetail.subledgercr.subledger_Id >0}">
									<td>${crmanualdetail.subledgercr.subledger_name}</td>
								</c:if>	
								<c:if test="${crmanualdetail.suppliercr.supplier_id >0}">
									<td>${crmanualdetail.suppliercr.company_name}</td>
								</c:if>	
								<c:if test="${crmanualdetail.customercr.customer_id >0}">
									<td>${crmanualdetail.customercr.firm_name}</td>
								</c:if>
								<%-- <td>${crmanualdetail.subledgercr.subledger_name}</td> --%>
								<td></td>

								
									<td class='tright'><fmt:formatNumber type="number" minFractionDigits="2"
											maxFractionDigits="2" value="${crmanualdetail.cramount}" /></td>
                    

								<c:set var="amount_cr"
									value="${amount_cr + crmanualdetail.cramount}" />
							</tr>

						</c:forEach>
							<tr style='border: 1px solid #bbb'>
								<td>Narration: <br /> Being ${journalVoucher.remark}
								</td>
									<td></td>
										<td></td>
								</tr>
								<tr>
								<td></td>
								<td class="tright"><b><fmt:formatNumber type="number"
											minFractionDigits="2" maxFractionDigits="2"
											value="${amount_dr}" /></b></td>
								<td class="tright"><b><fmt:formatNumber type="number"
											minFractionDigits="2" maxFractionDigits="2"
											value="${amount_cr}" /></b></td>
							</tr>
						</tbody>
					</table>
				</div>
				<div class="row" style="text-align: center; margin: 15px;">
					<button class="fassetBtn" type="button"
						onclick="manualJournalVoucherAccountingVoucherpdf('#Hiddentable', {type: 'pdf',
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

						<td style="color: blue; margin-left: 50px; text-align: center;">Manual Journal Voucher</td>

					</tr>

					<tr>
					<td>
						 <fmt:parseDate value="${journalVoucher.created_date}" pattern="yyyy-MM-dd" var="parsedDate" type="date" />
                   			 <fmt:formatDate value="${parsedDate}" var="mjv_bill_date" type="date" pattern="dd-MM-yyyy"  />
                   			 
							 <b>Date: ${mjv_bill_date}</b> 
							 </td>
					</tr>


	                 <tr>
						<td>Company Name: ${journalVoucher.company.company_name}</td>
					</tr>
					<tr>
						<td>Manual Journal Voucher No : ${journalVoucher.voucher_no}</td>
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
								<c:if test="${drmanualdetail.subledgerdr.subledger_Id >0}">
								<td>${drmanualdetail.subledgerdr.subledger_name}</td>
									</c:if>	
								<c:if test="${drmanualdetail.supplierdr.supplier_id >0}">
								<td>${drmanualdetail.supplierdr.company_name}</td>
									</c:if>	
									<c:if test="${drmanualdetail.customerdr.customer_id >0}">
								<td>${drmanualdetail.customerdr.firm_name}</td>
									</c:if>	
							<%-- 	<td>${drmanualdetail.subledgerdr.subledger_name}</td> --%>

										<td class='tright'><fmt:formatNumber type="number" minFractionDigits="2"
											maxFractionDigits="2" value="${drmanualdetail.dramount}" /></td>
                    

								<c:set var="amount_dr"
									value="${amount_dr + drmanualdetail.dramount}" />
							
									<td></td>
							</tr>

						</c:forEach>
						<c:forEach var="crmanualdetail" items="${crmanualdetail}">
							<tr>
<c:if test="${crmanualdetail.subledgercr.subledger_Id >0}">
								<td>${crmanualdetail.subledgercr.subledger_name}</td>
									</c:if>	
								<c:if test="${crmanualdetail.suppliercr.supplier_id >0}">
								<td>${crmanualdetail.suppliercr.company_name}</td>
									</c:if>	
									<c:if test="${crmanualdetail.customercr.customer_id >0}">
								<td>${crmanualdetail.customercr.firm_name}</td>
									</c:if>
								<%-- <td>${crmanualdetail.subledgercr.subledger_name}</td> --%>
								<td></td>

								
									<td class='tright'><fmt:formatNumber type="number" minFractionDigits="2"
											maxFractionDigits="2" value="${crmanualdetail.cramount}" /></td>
                    

								<c:set var="amount_cr"
									value="${amount_cr + crmanualdetail.cramount}" />
							</tr>

						</c:forEach>
							<tr style='border: 1px solid #bbb'>
								<td>Narration: <br /> Being ${journalVoucher.remark}
								</td>
									<td></td>
										<td></td>
								</tr>
								<tr>
								<td></td>
								<td class="tright"><b><fmt:formatNumber type="number"
											minFractionDigits="2" maxFractionDigits="2"
											value="${amount_dr}" /></b></td>
								<td class="tright"><b><fmt:formatNumber type="number"
											minFractionDigits="2" maxFractionDigits="2"
											value="${amount_cr}" /></b></td>
							</tr>
							<tr>
                                <c:if test="${journalVoucher.remark!=null }"> 
								
                                  <td>Remark:  ${journalVoucher.remark}</td>
                              </c:if>
                              <c:if test="${journalVoucher.remark==null }"> 
								
                                  <td>Remark:  </td>
                              </c:if>
							</tr>
						</tbody>
					</table>
				</div>
		</div>
		</form:form>
	
</div>



<script type="text/javascript">
function manualJournalVoucherAccountingVoucherpdf(selector, params) {
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
	function edit(id){
		window.location.assign('<c:url value = "editJournal"/>?id='+id);	
	}	
	function back(){
		window.location.assign('<c:url value = "manualjournalVoucherList"/>');	
	}
	/*  function DownloadPdf(id){
		window.location.assign('<c:url value = "downloadManualJV"/>?id='+id);	
	}  */
</script>
<%@include file="/WEB-INF/includes/footer.jsp"%>