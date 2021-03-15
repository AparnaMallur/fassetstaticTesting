<%@include file="/WEB-INF/includes/header.jsp"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<spring:url value="/resources/images/edit.png" var="editImg" />
<spring:url value="/resources/images/view.png" var="viewImg" />
<script type="text/javascript" src="${valid}"></script>
<spring:url value="/resources/js/jspdf.min.js" var="jspdfmin" />
<spring:url value="/resources/js/jspdf.plugin.autotable.js" var="jspdfauto" />
<spring:url value="/resources/js/report_table/contraAccountingVoucher.js" var="tableexport" />
 <script type="text/javascript" src="http://code.jquery.com/jquery-latest.min.js"></script>
 <script type="text/javascript" src="${jspdfmin}"></script>
 <script type="text/javascript" src="${jspdfauto}"></script>
 <script type="text/javascript" src="${tableexport}"></script>

<div class="breadcrumb">
	<h3>Contra</h3>
	<a href="homePage">Home</a> » <a href="contraList">Contra</a> » <a href="#">View</a>
</div>	
<div class="fassetForm">
	<form:form id="contraForm"  commandName = "contra">
		<ul class="nav nav-tabs navtab-bg">
			<li class="active">
				<a href="#home" data-toggle="tab" aria-expanded="true"> 
					<span class="visible-xs">
						<i class="fa fa-home"></i>
					</span> 
					<span class="hidden-xs">Details</span>
				</a>
			</li>
			<li class="">
				<a href="#profile" data-toggle="tab" aria-expanded="false"> 
					<span class="visible-xs">
						<i class="fa fa-user"></i>
					</span> 
					<span class="hidden-xs">Accounting Voucher</span>
				</a>
			</li>
		</ul>
		<div class="tab-content">
			<div class="tab-pane active" id="home">
				<div class="row">
					<table class="table">
						<tr>
							<td><strong>Voucher No:</strong></td>
							<td style="text-align: left;">${contra.voucher_no}</td>
						</tr>
						<tr>
							<td><strong>Date:</strong></td>
							<fmt:parseDate value="${contra.date}" pattern="yyyy-MM-dd" var="parsedDate" type="date" />
							<fmt:formatDate value="${parsedDate}" var="date" type="date" pattern="dd-MM-yyyy" />
							<td style="text-align: left;">${date}</td>
						</tr>
						<tr>
							<td><strong>Type:</strong></td>
							<td style="text-align: left;">${contra.type==1 ? "Deposit" : (contra.type==2 ? "Withdraw" : "Transfer")}</td>
						</tr>
						<c:if test="${(contra.type == '1')}">
							<tr>
								<td><strong> Deposit To:</strong></td>
								<td style="text-align: left;">${contra.deposite_to.bank_name}
									${contra.deposite_to.account_no}</td>
							</tr>
						</c:if>
						<c:if test="${(contra.type == '2')}">
							<tr>
								<td><strong> Withdrawal From:</strong></td>
								<td style="text-align: left;">${contra.withdraw_from.bank_name}
									${contra.withdraw_from.account_no}</td>
							</tr>
						</c:if>
						<c:if test="${(contra.type == '3')}">
							<tr>
								<td><strong> Transfer From:</strong></td>
								<td style="text-align: left;">${contra.withdraw_from.bank_name}
									${contra.withdraw_from.account_no}</td>
							</tr>
							<tr>
								<td><strong> Transfer To:</strong></td>
								<td style="text-align: left;">${contra.deposite_to.bank_name}
									${contra.deposite_to.account_no}</td>
							</tr>
						</c:if>

						<tr>
							<td><strong> Amount:</strong></td>
							<td style="text-align: left;"><fmt:formatNumber type="number" minFractionDigits="2"
									maxFractionDigits="2" value="${contra.amount}" /></td>
						</tr>
						<tr>
							<td><strong>Financial Year:</strong></td>
							<td style="text-align: left;">${contra.accounting_year_id.year_range}</td>
						</tr>
						<tr>
							<td><strong>Remark:</strong></td>
							<td style="text-align: left;">${contra.other_remark}</td>
						</tr>
						
						<c:if test="${contra.excel_voucher_no != null}">
						<tr>
						<td><strong>Excel Voucher No</strong></td>
						
					   
						<td>${contra.excel_voucher_no}</td>
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
					
					</table>
				</div>
			<div class="row"  style = "text-align: center; margin:15px;">
			<button class="fassetBtn" type="button" onclick = "DownloadPdf(${contra.transaction_id})">
				Download As Pdf
			</button>
			</div>
			</div>
			
			<!--UI view for Account Voucher  -------------------------------------------------------------------------------->
			
			<div class="tab-pane" id="profile">
				<div class="col-md-12">
					<p class="col-md-6 col-xs-6 text-left" style="padding-left:0px">			
					<b>Contra Voucher No: ${contra.voucher_no}</b>
					</p>
					<p class="col-md-6 col-xs-6 text-right" style="padding-left:0px">			
					<b>Date: ${date}</b>
					</p>
				</div>
				<table class="table">
					<thead>
						<tr>
							<th style="width: 70%">Particulars</th>
							<th>Debit</th>
							<th>Credit</th>
						</tr>
					</thead>
					<tbody>
						<c:choose>
							<c:when test="${(contra.type == '1')}">
								<tr>
									<td>Dr ${contra.deposite_to.bank_name} -
										${contra.deposite_to.account_no}
										<br/>
										Cur Bal: 
										<fmt:formatNumber type="number" value="${bankto}" />
									
									 Dr
										</td>
									<td><fmt:formatNumber type="number" minFractionDigits="2"
									maxFractionDigits="2" value="${contra.amount}" /></td>
									<td></td>
								</tr>
								<tr>
									<td>Cr Cash In Hand
									<br/>
										Cur Bal: 
										</td>
									<td></td>
									<td><fmt:formatNumber type="number" minFractionDigits="2"
									maxFractionDigits="2" value="${contra.amount}" /></td>
								</tr>
								<!-- <tr>
									<td>Paid To: Deposite
									</td>
									<td></td>
									<td></td>
								</tr> -->
								<tr style='border:1px solid #bbb'>
									<td>Narration: <br /> Being cash Deposited into ${contra.deposite_to.bank_name} -
										${contra.deposite_to.account_no}.</td>
									<td><fmt:formatNumber type="number" minFractionDigits="2"
									maxFractionDigits="2" value="${contra.amount}" /></td>
									<td><fmt:formatNumber type="number" minFractionDigits="2"
									maxFractionDigits="2" value="${contra.amount}" /></td>
								</tr>
								<tr>
                                <c:if test="${contra.other_remark!=null }"> 
								
                                  <td  colspan="3">Remark:  ${contra.other_remark}</td>
                              </c:if>
                              <c:if test="${contra.other_remark==null }"> 
								
                                  <td  colspan="3">Remark:  </td>
                              </c:if>
							</tr>
							</c:when>
							<c:when test="${(contra.type == '2')}">

								<tr>
									<td>Cr ${contra.withdraw_from.bank_name} -
										${contra.withdraw_from.account_no}
										<br/>
										Cur Bal: 
										<fmt:formatNumber type="number" value="${bankfrom}" />									
										 Dr
										
									</td>
									<td></td>
									<td><fmt:formatNumber type="number" minFractionDigits="2"
									maxFractionDigits="2" value="${contra.amount}" /></td>
								</tr>
								<tr>
									<td>Dr Cash In Hand<br/>
										Cur Bal: </td>
									<td><fmt:formatNumber type="number" minFractionDigits="2"
									maxFractionDigits="2" value="${contra.amount}" /></td>
									<td></td>
								</tr>
								<!-- <tr>
									<td>Paid To: Self Withdrawl
									</td>
									<td></td>
									<td></td>
								</tr> -->
								<tr style='border:1px solid #bbb'>
									<td>Narration: <br /> Being cash withdrawn from ${contra.withdraw_from.bank_name} -
										${contra.withdraw_from.account_no}.
									</td>
									<td><fmt:formatNumber type="number" minFractionDigits="2"
									maxFractionDigits="2" value="${contra.amount}" /></td>
									<td><fmt:formatNumber type="number" minFractionDigits="2"
									maxFractionDigits="2" value="${contra.amount}" /></td>
								</tr>
										<tr>
                                <c:if test="${contra.other_remark!=null }"> 
								
                                  <td  colspan="3">Remark:  ${contra.other_remark}</td>
                              </c:if>
                              <c:if test="${contra.other_remark==null }"> 
								
                                  <td  colspan="3">Remark:  </td>
                              </c:if>
							</tr>
							</c:when>
							<c:when test="${(contra.type == '3')}">
								<tr>
									<td>Cr ${contra.withdraw_from.bank_name} -
										${contra.withdraw_from.account_no}
										<br/>
										Cur Bal: 
										<fmt:formatNumber type="number" value="${bankfrom}" />	
										 Dr
										</td>
									<td></td>
									<td><fmt:formatNumber type="number" minFractionDigits="2"
									maxFractionDigits="2" value="${contra.amount}" /></td>
								</tr>
								<tr>
									<td>Dr ${contra.deposite_to.bank_name} -
										${contra.deposite_to.account_no}
										<br/>
										Cur Bal: <fmt:formatNumber type="number" value="${bankto}" /> Dr
										</td>
									<td>${contra.amount}</td>
									<td></td>
								</tr>
								<!-- <tr>
									<td>Paid To: Transfer
									</td>
									<td></td>
									<td></td>
								</tr> -->
								<tr style='border:1px solid #bbb'>
									<td>Narration:<br /> Being amount transferred to ${contra.deposite_to.bank_name} -
										${contra.deposite_to.account_no}  from ${contra.withdraw_from.bank_name} -
										${contra.withdraw_from.account_no}.</td>
									<td><fmt:formatNumber type="number" minFractionDigits="2"
									maxFractionDigits="2" value="${contra.amount}" /></td>
									<td><fmt:formatNumber type="number" minFractionDigits="2"
									maxFractionDigits="2" value="${contra.amount}" /></td>
								</tr>
										<tr>
                                <c:if test="${contra.other_remark!=null }"> 
								
                                  <td  colspan="3">Remark:  ${contra.other_remark}</td>
                              </c:if>
                              <c:if test="${contra.other_remark==null }"> 
								
                                  <td  colspan="3">Remark:  </td>
                              </c:if>
							</tr>
							</c:when>

						</c:choose>

					</tbody>
				</table>
				<div class="row"  style = "text-align: center; margin:15px;">
			<button class="fassetBtn" type="button"  onclick ="ContraAccountVoucherPdf('#Hiddentable', {type: 'pdf',
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
			</div>
			</div>
		<!--UI view for Account Voucher ends  -------------------------------------------------------------------------------->
		
		<!--PDF view for Account Voucher starts  ----------------------------------------------------------------------------->
		
		<div class="table-scroll" style="display:none;" id="tableDiv">
			
			<table id="Hiddentable" >
					<!-- for PDf heading -->
					<tr >
						
						<td style="color:blue; margin-left: 50px;text-align: center;">Contra</td>
						
					</tr>
			
					<tr>
							<td>Date: ${date}</td>
					</tr>
			
					<tr>
						<td>Contra Voucher No: ${contra.voucher_no}</td>
						
					</tr>
				
					<tr id="row">
					
						<th style="width: 70%">Particulars</th>
							<th>Debit</th>
							<th>Credit</th>
					
					</tr>
				<tbody>
				    
				    <c:choose>
							<c:when test="${(contra.type == '1')}">
								<tr>
									<td>Dr ${contra.deposite_to.bank_name} -
										${contra.deposite_to.account_no}
										<br/>
										Cur Bal: 
										<fmt:formatNumber type="number" value="${bankto}" />
									
									 Dr
										</td>
									<td><fmt:formatNumber type="number" minFractionDigits="2"
									maxFractionDigits="2" value="${contra.amount}" /></td>
									<td></td>
								</tr>
								<tr>
									<td>Cr Cash In Hand
									<br/>
										Cur Bal: 
										</td>
									<td></td>
									<td><fmt:formatNumber type="number" minFractionDigits="2"
									maxFractionDigits="2" value="${contra.amount}" /></td>
								</tr>
								<!-- <tr>
									<td>Paid To: Deposite
									</td>
									<td></td>
									<td></td>
								</tr> -->
								<tr style='border:1px solid #bbb'>
									<td>Narration: <br /> Being cash Deposited into ${contra.deposite_to.bank_name} -
										${contra.deposite_to.account_no}.</td>
									<td><fmt:formatNumber type="number" minFractionDigits="2"
									maxFractionDigits="2" value="${contra.amount}" /></td>
									<td><fmt:formatNumber type="number" minFractionDigits="2"
									maxFractionDigits="2" value="${contra.amount}" /></td>
								</tr>
										<tr>
                                <c:if test="${contra.other_remark!=null }"> 
								
                                  <td  colspan="3">Remark:  ${contra.other_remark}</td>
                              </c:if>
                              <c:if test="${contra.other_remark==null }"> 
								
                                  <td  colspan="3">Remark:  </td>
                              </c:if>
							</tr>
							</c:when>
							<c:when test="${(contra.type == '2')}">

								<tr>
									<td>Cr ${contra.withdraw_from.bank_name} -
										${contra.withdraw_from.account_no}
										<br/>
										Cur Bal: 
										<fmt:formatNumber type="number" value="${bankfrom}" />									
										 Dr
										
									</td>
									<td></td>
									<td><fmt:formatNumber type="number" minFractionDigits="2"
									maxFractionDigits="2" value="${contra.amount}" /></td>
								</tr>
								<tr>
									<td>Dr Cash In Hand<br/>
										Cur Bal: </td>
									<td><fmt:formatNumber type="number" minFractionDigits="2"
									maxFractionDigits="2" value="${contra.amount}" /></td>
									<td></td>
								</tr>
								<!-- <tr>
									<td>Paid To: Self Withdrawl
									</td>
									<td></td>
									<td></td>
								</tr> -->
								<tr style='border:1px solid #bbb'>
									<td>Narration: <br /> Being cash withdrawn from ${contra.withdraw_from.bank_name} -
										${contra.withdraw_from.account_no}.
									</td>
									<td><fmt:formatNumber type="number" minFractionDigits="2"
									maxFractionDigits="2" value="${contra.amount}" /></td>
									<td><fmt:formatNumber type="number" minFractionDigits="2"
									maxFractionDigits="2" value="${contra.amount}" /></td>
								</tr>
								
								<tr>
                                <c:if test="${contra.other_remark!=null }"> 
								
                                  <td>Remark:  ${contra.other_remark}</td>
                              </c:if>
                              <c:if test="${contra.other_remark==null }"> 
								
                                  <td>Remark:  </td>
                              </c:if>
							</tr>
							</c:when>
							<c:when test="${(contra.type == '3')}">
								<tr>
									<td>Cr ${contra.withdraw_from.bank_name} -
										${contra.withdraw_from.account_no}
										<br/>
										Cur Bal: 
										<fmt:formatNumber type="number" value="${bankfrom}" />	
										 Dr
										</td>
									<td></td>
									<td><fmt:formatNumber type="number" minFractionDigits="2"
									maxFractionDigits="2" value="${contra.amount}" /></td>
								</tr>
								<tr>
									<td>Dr ${contra.deposite_to.bank_name} -
										${contra.deposite_to.account_no}
										<br/>
										Cur Bal: <fmt:formatNumber type="number" value="${bankto}" /> Dr
										</td>
									<td>${contra.amount}</td>
									<td></td>
								</tr>
								<!-- <tr>
									<td>Paid To: Transfer
									</td>
									<td></td>
									<td></td>
								</tr> -->
								<tr style='border:1px solid #bbb'>
									<td>Narration:<br /> Being amount transferred to ${contra.withdraw_from.bank_name} -
										${contra.withdraw_from.account_no} from ${contra.deposite_to.bank_name} -
										${contra.deposite_to.account_no}.</td>
									<td><fmt:formatNumber type="number" minFractionDigits="2"
									maxFractionDigits="2" value="${contra.amount}" /></td>
									<td><fmt:formatNumber type="number" minFractionDigits="2"
									maxFractionDigits="2" value="${contra.amount}" /></td>
								</tr>
								<tr>
                                <c:if test="${contra.other_remark!=null }"> 
								
                                  <td>Remark:  ${contra.other_remark}</td>
                              </c:if>
                              <c:if test="${contra.other_remark==null }"> 
								
                                  <td>Remark:  </td>
                              </c:if>
							</tr>
							</c:when>

						</c:choose>
					
				</tbody>
				
			</table>
		</div> 
		
		
		<!--PDF view for Account Voucher starts  -->
		</div>
	</form:form>
		<div class="row"  style = "text-align: center; margin:15px;">
				<button class="fassetBtn" type="button" onclick = "back();">
				<spring:message code="btn_back" />
			</button>
		</div>
</div>
<script type="text/javascript">


function ContraAccountVoucherPdf(selector, params) {
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
		window.location.assign('<c:url value = "editContra"/>?id='+id);	
	}	
	function back(){
		<c:if test="${flag==true}">  
		window.location.assign('<c:url value = "contraList"/>');
		</c:if>
		<c:if test="${flag==false}">  
		window.location.assign('<c:url value = "contraFailure"/>');
		</c:if>
	}
	
	function DownloadPdf(id){
		window.location.assign('<c:url value = "downloadContra"/>?id='+id);	
	}
	function DownloadAccountingPdf(id){
		window.location.assign('<c:url value = "downloadContraAccountingVoucher"/>?id='+id);	
	}
</script>
<%@include file="/WEB-INF/includes/footer.jsp" %>