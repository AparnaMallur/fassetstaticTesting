<%@include file="/WEB-INF/includes/header.jsp"%>
<spring:url value="/resources/images/edit.png" var="editImg" />
<spring:url value="/resources/images/view.png" var="viewImg" />
<script type="text/javascript" src="${valid}"></script>
<spring:url value="/resources/js/jspdf.min.js" var="jspdfmin" />
<spring:url value="/resources/js/jspdf.plugin.autotable.js" var="jspdfauto" />
<spring:url value="/resources/js/report_table/debitNote_AccountingVoucher.js" var="tableexport" />
 <script type="text/javascript" src="http://code.jquery.com/jquery-latest.min.js"></script>
 <script type="text/javascript" src="${jspdfmin}"></script>
 <script type="text/javascript" src="${jspdfauto}"></script>
 <script type="text/javascript" src="${tableexport}"></script>

<div class="breadcrumb">
	<h3>Debit Note</h3>
	<a href="homePage">Home</a> » <a href="debitNoteList">Debit Note</a> » <a href="#">View</a>
</div>	
<div class="fassetForm">
	<form:form id="debitNoteForm"  commandName = "debitNote">
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
					<table class = "table">
						<tr>
							<td><strong>Voucher No.:</strong></td>
							<td style="text-align: left;">${debitNote.voucher_no}</td>			
						</tr>				
						<tr>
							<td><strong>Date:</strong></td>
							<fmt:parseDate value="${debitNote.date}" pattern="yyyy-MM-dd" var="parsedDate" type="date"/>
							<fmt:formatDate value="${parsedDate}" var="date" type="date" pattern="dd-MM-yyyy"/>
							<td style="text-align: left;">${date}</td>		
						</tr>				
						<tr>
							<td><strong>Supplier Name:</strong></td>
							<td style="text-align: left;">${debitNote.supplier.company_name}</td>			
						</tr>				
						<tr>
							<td><strong>Purchase Bill Number:</strong></td>
							<td style="text-align: left;">${debitNote.purchase_bill_id.supplier_bill_no}</td>			
						</tr>	
						<c:if test="${debitNote.transaction_value_head > 0}">			
							<tr>
								<td><strong>Transaction Amount:</strong></td>
									<td style="text-align: left;"><fmt:formatNumber type="number"   
									minFractionDigits="2" maxFractionDigits="2" value="${debitNote.transaction_value_head}" /></td>			
								
							</tr>
							<tr>
								<td><strong>CGST:</strong></td>
								<td style="text-align: left;"><fmt:formatNumber type="number"   
									minFractionDigits="2" maxFractionDigits="2"  value="${debitNote.SGST_head}" /></td>			
									
							</tr>					
							<tr>
								<td><strong>SGST:</strong></td>
								<td style="text-align: left;"><fmt:formatNumber type="number"   
									minFractionDigits="2" maxFractionDigits="2"  value="${debitNote.SGST_head}" /></td>			
								<%-- <td style="text-align: left;">${debitNote.SGST_head}</td>		 --%>	
							</tr>					
							<tr>
								<td><strong>IGST:</strong></td>
									<td style="text-align: left;"><fmt:formatNumber type="number"   
									minFractionDigits="2" maxFractionDigits="2"  value="${debitNote.IGST_head}" /></td>			
										
							</tr>										
							<tr>
								<td><strong>CESS:</strong></td>
								<td style="text-align: left;"><fmt:formatNumber type="number"   
									minFractionDigits="2" maxFractionDigits="2"  value="${debitNote.SCT_head}" /></td>			
											
							</tr>
							<tr>
								<td><strong>VAT:</strong></td>
								<td style="text-align: left;"><fmt:formatNumber type="number"   
									minFractionDigits="2" maxFractionDigits="2"  value="${debitNote.total_vat}" /></td>			
											
											
							</tr>
							<tr>
								<td><strong>CST:</strong></td>
								<td style="text-align: left;"><fmt:formatNumber type="number"   
									minFractionDigits="2" maxFractionDigits="2"  value="${debitNote.total_vatcst}" /></td>			
											
										
							</tr>
							<tr>
								<td><strong>EXCISE:</strong></td>
								<td style="text-align: left;"><fmt:formatNumber type="number"   
									minFractionDigits="2" maxFractionDigits="2"  value="${debitNote.total_excise}" /></td>			
											
								
							</tr>
						</c:if>	
									
				        <tr>
							<td><strong>TDS:</strong></td>
							<td style="text-align: left;">						
							     <fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${debitNote.tds_amount}" />
							</td>			
						</tr>		
						<tr>
							<td><strong>Amount:</strong></td>
							<td style="text-align: left;"><fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${debitNote.round_off}" /></td>			
						</tr>	
						<tr>
							<td><strong>Description:</strong></td>
							<td style="text-align: left;">${debitNote.description == 1 ? "Sales return" : debitNote.description  == 2 ? "Post sale discount" : debitNote.description  == 3 ? "Deficiency in services" : debitNote.description  == 4 ? "Correction in invoices":debitNote.description  == 5 ? "Change in POS" :debitNote.description  == 6 ? "Finalization of provisional assessment" : "Others"}</td>			
						</tr>	
						<c:if test="${debitNote.description == 7}">
							<tr>
								<td><strong>Remark:</strong></td>
								<td style="text-align: left;">${debitNote.remark}</td>			
							</tr>	
						</c:if>	
						<tr>
							<td><strong>Financial Year:</strong></td>
							<td style="text-align: left;">${debitNote.accounting_year_id.year_range}</td>
							</tr>
							<tr>
							<c:if test="${debitNote.excel_voucher_no != null}">
						<td><strong>Excel Voucher No</strong></td>
						</c:if>
					<c:if test="${debitNote.excel_voucher_no != null}">
						<td>${debitNote.excel_voucher_no}</td>
						</c:if>	
						</tr>	
						<tr>			
						<td><strong>Created By</strong></td>							    
						<td style="text-align: left;">${created_by.first_name} ${created_by.last_name}</td>
						</tr>
						<tr>
						<td><strong>Updated By</strong></td>
						<td style="text-align: left;">${updated_by.first_name} ${updated_by.last_name}</td>
					   </tr>					
						
					</table>
		   <c:if test="${(debitNote.description!=null)&&(debitNote.description==1||debitNote.description==4||debitNote.description==5||debitNote.description==6||debitNote.description==7)}">		
							<h3>Product Details</h3>			
			<table class="table table-bordered table-striped">
			<thead>
					<tr>
						<th>Product </th>
						<th>Quantity</th>
						<th>UOM</th>
						<th>HSN/SAC Code</th>
						<th>Rate</th>
						<th>Discount(%)</th>
						<th>Labour Charge</th>
						<th>Freight Charges</th>
						<th>Others</th>
						<th>CGST/VAT</th>
						<th>SGST/CST</th>
						<th>IGST/EXCISE</th>
						<th>CESS</th>
						
					</tr>
			</thead>
			<tbody>
						<c:forEach var = "prod_list" items = "${debitDetailsList}">	
								<tr>
									<td>${prod_list.product_id.product_name}</td>
									<td><fmt:formatNumber type="number"   minFractionDigits="2" maxFractionDigits="2" value="${prod_list.quantity}" /></td>
								
									
									<td>${prod_list.uom_id.unit}</td>
									<c:if test="${debitNote.excel_voucher_no==null}">
							<td>${prod_list.product_id.hsn_san_no}</td>
							</c:if>
							<c:if test="${debitNote.excel_voucher_no!=null}">
							<td>${prod_list.HSNCode}</td>
							</c:if>
								<td><fmt:formatNumber type="number"   minFractionDigits="2" maxFractionDigits="2" value="${prod_list.rate}" /></td>
								
									<td><fmt:formatNumber type="number"   minFractionDigits="2" maxFractionDigits="2" value="${prod_list.discount}" /></td>
								
									<td><fmt:formatNumber type="number"   minFractionDigits="2" maxFractionDigits="2" value="${prod_list.labour_charges}" /></td>
								
									<td><fmt:formatNumber type="number"   minFractionDigits="2" maxFractionDigits="2" value="${prod_list.frieght}" /></td>
								
									<td><fmt:formatNumber type="number"   minFractionDigits="2" maxFractionDigits="2" value="${prod_list.others}" /></td>
								
								
									<c:if test="${prod_list.is_gst == 1}">
									<td><fmt:formatNumber type="number" minFractionDigits="2"
									maxFractionDigits="2" value="${prod_list.cgst}" />
									</td>
									<td><fmt:formatNumber type="number" minFractionDigits="2"
									maxFractionDigits="2" value="${prod_list.sgst}" />
									</td>
									<td><fmt:formatNumber type="number" minFractionDigits="2"
									maxFractionDigits="2" value="${prod_list.igst}" />
									</td>
									<td><fmt:formatNumber type="number" minFractionDigits="2"
									maxFractionDigits="2" value="${prod_list.state_com_tax}" />
									</td>
									 </c:if>
									  <c:if test="${prod_list.is_gst == 2}">
									<td><fmt:formatNumber type="number" minFractionDigits="2"
									maxFractionDigits="2" value="${prod_list.VAT}" />
									</td>
									<td><fmt:formatNumber type="number" minFractionDigits="2"
									maxFractionDigits="2" value="${prod_list.VATCST}" />
									</td>
									<td><fmt:formatNumber type="number" minFractionDigits="2"
									maxFractionDigits="2" value="${prod_list.excise}" />
									</td>
									<td>
									<fmt:formatNumber type="number" minFractionDigits="2"
									maxFractionDigits="2" value="0.0" />
								    </td>
							        </c:if>
								</tr>
							</c:forEach>	
			</tbody>
		</table>
		</c:if>	
			 <div class="col-md-12"  style = "text-align: center; margin:15px;">
				
				<button class="fassetBtn" type="button" onclick = "DownloadPdf(${debitNote.debit_no_id})"> 
			   Download As Pdf
		        </button>
				
		</div>
		
				</div>
			</div>
			
			<!--UI view for Account Vouvher starts  -->
			
			<div class="tab-pane" id="profile">
				<div class="row">
					<div class="col-md-12">
						<p class="col-md-6 col-xs-6 text-left" style="padding-left: 0px">
							<b>Debit Note Voucher No: ${debitNote.voucher_no}</b>
						</p>
						<p class="col-md-6 col-xs-6 text-right" style="padding-left: 0px">
							<b>Date: ${date}</b>
						</p>
					</div>
					<div class="col-md-12">
						<p>
							<b>Party A/c Name: ${debitNote.supplier.company_name}</b>
						</p>
						<p>
							<b>Current Balance: 
							<fmt:formatNumber type="number"  minFractionDigits="2" maxFractionDigits="2" value="${closingbalance}" />
							
							</b>
						</p>
					</div>
					<div class="col-md-12">
						<p>
							<b>Purchase Ledger: ${debitNote.subledger.ledger.ledger_name}</b>
						</p>
					</div>
				</div>
				<table class="table">
					<thead>
						<tr>
							<th style="width: 55%">Name of Item</th>
							<c:if test="${debitNote.description !=2 && debitNote.description!=3 }">
								<th>Quantity</th>
								<th>Rate</th>
								<th>per</th>
								<th>Discount</th>
							</c:if>
							<th>Amount</th>
						</tr>
					</thead>
					<tbody>
						<c:if test="${!empty debitDetailsList}">
						<c:forEach var = "debitDetail" items = "${debitDetailsList}">
							<tr>
								<td>${debitDetail.product_id.product_name}</td>
								<c:if test="${debitNote.description !=2 && debitNote.description!=3 }">
									<td><fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2"
										 value="${debitDetail.quantity}" /></td>
					
							<td><fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2"
							 value="${debitDetail.rate}" /></td>
					
							<td>${debitDetail.uom_id.unit}</td>
							<td><fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2"
							 value="${debitDetail.discount}" /></td>
								</c:if>
								<td><fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${debitDetail.transaction_amount}" /></td>
							</tr>
						</c:forEach>
						</c:if>
						<c:if test="${debitNote.CGST_head>0}">
							<tr>
								<td>CGST</td>
								<td></td>
								<td></td>
								<td></td>
								<td></td>
								<td><fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${debitNote.CGST_head}" /> </td>
							</tr>
						</c:if>
						<c:if test="${debitNote.SGST_head>0}">
							<tr>
								<td>SGST</td>
								<td></td>
								<td></td>
								<td></td>
								<td></td>
								<td><fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${debitNote.SGST_head}" /> </td>
							</tr>
						</c:if>
						<c:if test="${debitNote.IGST_head>0}">
							<tr>
								<td>IGST</td>
								<td></td>
								<td></td>
								<td></td>
								<td></td>
								<td><fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${debitNote.IGST_head}" /> </td>
							</tr>
						</c:if>
						<c:if test="${debitNote.SCT_head>0}">
							<tr>
								<td>CESS</td>
								<td></td>
								<td></td>
								<td></td>
								<td></td>
								<td><fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${debitNote.SCT_head}" /> </td>

							</tr>
						</c:if>
						<c:if test="${debitNote.total_vat>0}">
							<tr>
								<td>VAT</td>
								<td></td>
								<td></td>
								<td></td>
								<td></td>
								<td><fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${debitNote.total_vat}" /> </td>

							</tr>
						</c:if>
						<c:if test="${debitNote.total_vatcst>0}">
							<tr>
								<td>CST</td>
								<td></td>
								<td></td>
								<td></td>
								<td></td>
								<td><fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${debitNote.total_vatcst}" /> </td>
							</tr>
						</c:if>
						<c:if test="${debitNote.total_excise>0}">
							<tr>
								<td>EXCISE</td>
								<td></td>
								<td></td>
								<td></td>
								<td></td>
								<td><fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${debitNote.total_excise}" /> </td>

							</tr>
						</c:if>
						<tr style='border: 1px solid #bbb'>
							<td>Narration: <br /> Being debit note issued to
								${debitNote.supplier.company_name} for ${debitNote.description == 1 ? "Purchase return" : debitNote.description  == 2 ? "Post sale discount" : debitNote.description  == 3 ? "Deficiency in services" : debitNote.description  == 4 ? "Correction in invoices":debitNote.description  == 5 ? "Change in POS" :debitNote.description  == 6 ? "Finalization of provisional assessment" : debitNote.remark}
							</td>
							<c:if test="${debitNote.description !=2 && debitNote.description!=3 }">							
								<td></td>
								<td></td>
								<td></td>
								<td></td>
							</c:if>
							<td><fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${debitNote.round_off+debitNote.tds_amount}" /> </td>
						</tr>
						<tr>
                                <c:if test="${debitNote.remark!=null }"> 
								
                                  <td  colspan="6">Remark:  ${debitNote.remark}</td>
                              </c:if>
                              <c:if test="${debitNote.remark==null }"> 
								
                                  <td  colspan="6">Remark:  </td>
                              </c:if>
							</tr>
					</tbody>
				</table>
					<div class="row"  style = "text-align: center; margin:15px;">
			<button class="fassetBtn" type="button"  onclick ="debitAccountingVoucherPdf('#Hiddentable', {type: 'pdf',
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
			<button class="fassetBtn" type="button" onclick = "back();">
				<spring:message code="btn_back" />
			</button>
		</div>
				
			</div>	
			
			<!--UI view for Account Vouvher ends  -->
			
			<!--hidden table pdf starts fot Account Voucher  -->
			
			<div class="table-scroll" style="display:none;" id="tableDiv">
			
			<table id="Hiddentable" >
					<!-- for PDf heading -->
					<tr >
						
						<td style="color:blue; margin-left: 50px;text-align: center;">Debit Note</td>
						
					</tr>
			
					<tr>
							<td>Date: ${date}</td>
					</tr>
			
					<tr>
						<td>Debit Note Voucher No: ${debitNote.voucher_no}</td>
						
					</tr>
					
					
					<tr>
							<td>Party A/c Name: ${debitNote.supplier.company_name}</td>
					</tr>
					
					<tr>
							<td>Current Balance:<fmt:formatNumber type="number"  minFractionDigits="2" maxFractionDigits="2" value="${closingbalance}" />
							 </td>
					</tr>
					
					<tr>
							<td>Purchase Ledger: ${debitNote.subledger.ledger.ledger_name}</td>
					</tr>
					
					<tr>
					</tr>
					
				
					<tr id="row">
					
						<th style="width: 55%">Name of Item</th>
							<c:if test="${debitNote.description !=2 && debitNote.description!=3 }">
								<th>Quantity</th>
								<th>Rate</th>
								<th>per</th>
								<th>Discount</th>
							</c:if>
						<th>Amount</th>
					
					</tr>
				<tbody>
				    
				    <c:if test="${!empty debitDetailsList}">
						<c:forEach var = "debitDetail" items = "${debitDetailsList}">
							<tr>
								<td>${debitDetail.product_id.product_name}</td>
								<c:if test="${debitNote.description !=2 && debitNote.description!=3 }">
								
										<td><fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2"
										 value="${debitDetail.quantity}" /></td>
					
							<td><fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2"
							 value="${debitDetail.rate}" /></td>
					
							<td>${debitDetail.uom_id.unit}</td>
							<td><fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2"
							 value="${debitDetail.discount}" /></td>
					
								
								</c:if>
								<td><fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${debitDetail.transaction_amount}" /></td>
							</tr>
						</c:forEach>
						</c:if>
						<c:if test="${debitNote.CGST_head>0}">
							<tr>
								<td>CGST</td>
								<td></td>
								<td></td>
								<td></td>
								<td></td>
								<td><fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${debitNote.CGST_head}" /> </td>
							</tr>
						</c:if>
						<c:if test="${debitNote.SGST_head>0}">
							<tr>
								<td>SGST</td>
								<td></td>
								<td></td>
								<td></td>
								<td></td>
								<td><fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${debitNote.SGST_head}" /> </td>
							</tr>
						</c:if>
						<c:if test="${debitNote.IGST_head>0}">
							<tr>
								<td>IGST</td>
								<td></td>
								<td></td>
								<td></td>
								<td></td>
								<td><fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${debitNote.IGST_head}" /> </td>
							</tr>
						</c:if>
						<c:if test="${debitNote.SCT_head>0}">
							<tr>
								<td>CESS</td>
								<td></td>
								<td></td>
								<td></td>
								<td></td>
								<td><fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${debitNote.SCT_head}" /> </td>

							</tr>
						</c:if>
						<c:if test="${debitNote.total_vat>0}">
							<tr>
								<td>VAT</td>
								<td></td>
								<td></td>
								<td></td>
								<td></td>
								<td><fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${debitNote.total_vat}" /> </td>

							</tr>
						</c:if>
						<c:if test="${debitNote.total_vatcst>0}">
							<tr>
								<td>CST</td>
								<td></td>
								<td></td>
								<td></td>
								<td></td>
								<td><fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${debitNote.total_vatcst}" /> </td>
							</tr>
						</c:if>
						<c:if test="${debitNote.total_excise>0}">
							<tr>
								<td>EXCISE</td>
								<td></td>
								<td></td>
								<td></td>
								<td></td>
								<td><fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${debitNote.total_excise}" /> </td>

							</tr>
						</c:if>
						<tr style='border: 1px solid #bbb'>
							<td>Narration: <br /> Being debit note issued to
								${debitNote.supplier.company_name} for ${debitNote.description == 1 ? "Purchase return" : debitNote.description  == 2 ? "Post sale discount" : debitNote.description  == 3 ? "Deficiency in services" : debitNote.description  == 4 ? "Correction in invoices":debitNote.description  == 5 ? "Change in POS" :debitNote.description  == 6 ? "Finalization of provisional assessment" : debitNote.remark}
							</td>
							<c:if test="${debitNote.description !=2 && debitNote.description!=3 }">							
								<td></td>
								<td></td>
								<td></td>
								<td></td>
							</c:if>
							<td><fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${debitNote.round_off+debitNote.tds_amount}" /> </td>
						</tr>
					<tr>
                                <c:if test="${debitNote.remark!=null }"> 
								
                                  <td  colspan="6">Remark:  ${debitNote.remark}</td>
                              </c:if>
                              <c:if test="${debitNote.remark==null }"> 
								
                                  <td  colspan="6">Remark:  </td>
                              </c:if>
							</tr>
				</tbody>
				
			</table>
		</div> 
			
			
			<!--hidden table pdf ends fot Account Voucher  -->
			
		</div>
		
	</form:form>
</div>
<script type="text/javascript">

 function debitAccountingVoucherPdf(selector, params) {
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
		window.location.assign('<c:url value = "editDebitNote"/>?id='+id);	
	}
	
	function back(){
		
		<c:if test="${flag==true}">  
		window.location.assign('<c:url value = "debitNoteList"/>');
		</c:if>
		<c:if test="${flag==false}">  
		window.location.assign('<c:url value = "debitNoteFailure"/>');
		</c:if>
		
	}
	
	function DownloadPdf(id){
		window.location.assign('<c:url value = "downloadDebitNote"/>?id='+id);	
	}
	
</script>
<%@include file="/WEB-INF/includes/footer.jsp" %>