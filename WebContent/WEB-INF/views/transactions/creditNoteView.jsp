<%@include file="/WEB-INF/includes/header.jsp"%>
<spring:url value="/resources/images/edit.png" var="editImg" />
<spring:url value="/resources/images/view.png" var="viewImg" />
<script type="text/javascript" src="${valid}"></script>
<spring:url value="/resources/js/jspdf.min.js" var="jspdfmin" />
<spring:url value="/resources/js/jspdf.plugin.autotable.js" var="jspdfauto" />
<spring:url value="/resources/js/report_table/creditNoteAccountingVoucher.js" var="tableexport" />
 <script type="text/javascript" src="http://code.jquery.com/jquery-latest.min.js"></script>
 <script type="text/javascript" src="${jspdfmin}"></script>
 <script type="text/javascript" src="${jspdfauto}"></script>
 <script type="text/javascript" src="${tableexport}"></script>

<div class="breadcrumb">
	<h3>Credit Note</h3>
	<a href="homePage">Home</a> » <a href="creditNoteList">Credit Note</a> » <a href="#">View</a>
</div>	

<div class="fassetForm">
	<form:form id="creditNoteForm"  commandName = "creditNote">
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
							<td style="text-align: left;">${creditNote.voucher_no}</td>			
						</tr>				
						<tr>
							<td><strong>Date:</strong></td>
							<fmt:parseDate value="${creditNote.date}" pattern="yyyy-MM-dd" var="parsedDate" type="date"  />
							<fmt:formatDate value="${parsedDate}" var="date" type="date" pattern="dd-MM-yyyy" />
							<td style="text-align: left;">${date}</td>			
						</tr>				
						<tr>
							<td><strong>Supplier Name:</strong></td>
							<td style="text-align: left;">${creditNote.customer.firm_name}</td>			
						</tr>				
						<tr>
							<td><strong>Sales Bill No:</strong></td>
							<td style="text-align: left;">${creditNote.sales_bill_id.voucher_no}</td>			
						</tr>	
						<c:if test="${creditNote.transaction_value_head > 0}">			
							<tr>
								<td><strong>Transaction Amount:</strong></td>
								<td style="text-align: left;"><fmt:formatNumber type="number"   minFractionDigits="2" maxFractionDigits="2" value="${creditNote.transaction_value_head}" /></td>			
							</tr>
							<tr>
								<td><strong>CGST:</strong></td>
								<td style="text-align: left;"><fmt:formatNumber type="number"   minFractionDigits="2" maxFractionDigits="2" value="${creditNote.CGST_head}" /></td>			
							</tr>					
							<tr>
								<td><strong>SGST:</strong></td>
								<td style="text-align: left;"><fmt:formatNumber type="number"   minFractionDigits="2" maxFractionDigits="2" value="${creditNote.SGST_head}" /></td>			
							</tr>					
							<tr>
								<td><strong>IGST:</strong></td>
								<td style="text-align: left;"><fmt:formatNumber type="number"   minFractionDigits="2" maxFractionDigits="2" value="${creditNote.IGST_head}" /></td>			
							</tr>					
												
							<tr>
								<td><strong>CESS:</strong></td>
								<td style="text-align: left;"><fmt:formatNumber type="number"   minFractionDigits="2" maxFractionDigits="2" value="${creditNote.SCT_head}" /></td>			
							</tr>
							<tr>
								<td><strong>VAT:</strong></td>
								<td style="text-align: left;"><fmt:formatNumber type="number"   minFractionDigits="2" maxFractionDigits="2" value="${creditNote.total_vat}" /></td>			
							</tr>
							<tr>
								<td><strong>CST:</strong></td>
								<td style="text-align: left;"><fmt:formatNumber type="number"   minFractionDigits="2" maxFractionDigits="2" value="${creditNote.total_vatcst}" /></td>			
							</tr>
							<tr>
								<td><strong>EXCISE:</strong></td>
								<td style="text-align: left;"><fmt:formatNumber type="number"   minFractionDigits="2" maxFractionDigits="2" value="${creditNote.total_excise}" /></td>			
							</tr>
							
						</c:if>	
						<tr>
							<td><strong>TDS:</strong></td>
							<td style="text-align: left;">						
							     <fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${creditNote.tds_amount}" />
							</td>			
						</tr>				
						<tr>
							<td><strong>Amount:</strong></td>
							<td style="text-align: left;">						
							     <fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${creditNote.round_off}" />
							</td>			
						</tr>	
						<tr>
							<td><strong>Description:</strong></td>
							<td style="text-align: left;">${creditNote.description == 1 ? "Sales return" : creditNote.description  == 2 ? "Post sale discount" : creditNote.description  == 3 ? "Deficiency in services" : creditNote.description  == 4 ? "Correction in invoices":creditNote.description  == 5 ? "Change in POS" :creditNote.description  == 6 ? "Finalization of provisional assessment" : "Others"} -  ${creditNote.subledger.ledger.ledger_name}</td>			
						</tr>
						<tr>
                                <c:if test="${creditNote.remark!=null }"> 
								
                                  <td colspan="3">>Remark:  ${creditNote.remark}</td>
                              </c:if>
                              <c:if test="${creditNote.remark==null }"> 
								
                                  <td colspan="3">Remark: </td>
                              </c:if>
							</tr>		
						<tr>
							<td><strong>Financial Year:</strong></td>
							<td style="text-align: left;">${creditNote.accounting_year_id.year_range}</td>	
							</tr>
							<tr>
							<c:if test="${creditNote.excel_voucher_no != null}">
							
						<td><strong>Excel Voucher No</strong></td>
						</c:if>
					    <c:if test="${creditNote.excel_voucher_no != null}">
						<td>${creditNote.excel_voucher_no}</td>
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
					
			<c:if test="${(creditNote.description!=null)&&(creditNote.description==1||creditNote.description==4||creditNote.description==5||creditNote.description==6||creditNote.description==7)}">
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
						<c:forEach var = "prod_list" items = "${creditDetailsList}">	
								<tr>
									<td>${prod_list.product_id.product_name}</td>
									<td><fmt:formatNumber type="number" minFractionDigits="2"
									maxFractionDigits="2" value="${prod_list.quantity}" />
									</td>
									<td>${prod_list.uom_id.unit}</td>
									<c:if test="${creditNote.excel_voucher_no==null}">
							<td>${prod_list.product_id.hsn_san_no}</td>
							</c:if>
							<c:if test="${creditNote.excel_voucher_no!=null}">
							<td>${prod_list.HSNCode}</td>
							</c:if>
									<td><fmt:formatNumber type="number"   minFractionDigits="2" maxFractionDigits="2" value="${prod_list.rate}" /></td>
									<td><fmt:formatNumber type="number" minFractionDigits="2"
									maxFractionDigits="2" value="${prod_list.discount}" />
									</td>
									<td><fmt:formatNumber type="number" minFractionDigits="2"
									maxFractionDigits="2" value="${prod_list.labour_charges}" />
									</td>
									<td><fmt:formatNumber type="number" minFractionDigits="2"
									maxFractionDigits="2" value="${prod_list.frieght}" />
									</td>
									<td><fmt:formatNumber type="number" minFractionDigits="2"
									maxFractionDigits="2" value="${prod_list.others}" />
									</td>
									
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
				
				<button class="fassetBtn" type="button" onclick = "DownloadPdf(${creditNote.credit_no_id})"> 
			   Download As Pdf
		        </button>
				
		</div>	
				</div>
			</div>
			
			<!--Profile UI view started  -->
			<div class="tab-pane" id="profile">
				<div class="row">
					<div class="col-md-12">
						<p class="col-md-6 col-xs-6 text-left" style="padding-left: 0px">
							<b>Credit Note Voucher No: ${creditNote.voucher_no}</b>
						</p>
						<p class="col-md-6 col-xs-6 text-right" style="padding-left: 0px">
							<b>Date: ${date}</b>
						</p>
					</div>
					<div class="col-md-12">
						<p>
							<b>Party A/c Name: ${creditNote.customer.firm_name}</b>
						</p>
						<p>
							<b>Current Balance:
							<fmt:formatNumber type="number"  minFractionDigits="2" maxFractionDigits="2" value="${closingbalance}" />
							</b>
						</p>
					</div>
					<div class="col-md-12">
						<p>
							<b>Purchase Ledger: ${creditNote.subledger.ledger.ledger_name}</b>
						</p>
					</div>
				</div>
				<table class="table">
					<thead>
						<tr>
							<th style="width: 55%">Name of Item</th>
							<c:if test="${creditNote.description != 2 && creditNote.description !=3}">
									<th>Quantity</th>
									<th>Rate</th>
									<th>per</th>
									<th>Discount</th>
							</c:if>
							<th>Amount</th>
						</tr>
					</thead>
					<tbody>
						<c:if test="${!empty creditDetailsList}">
						<c:forEach var = "creditDetail" items = "${creditDetailsList}">
							<tr>
								<td>${creditDetail.product_id.product_name}</td> 
							   <c:if test="${creditNote.description != 2 && creditNote.description !=3}">
									<<td><fmt:formatNumber type="number"   minFractionDigits="2" maxFractionDigits="2"
									 value="${creditDetail.quantity}" /></td>
								
									<td><fmt:formatNumber type="number"   minFractionDigits="2" maxFractionDigits="2" 
									value="${creditDetail.rate}" /></td>
									<td>${creditDetail.uom_id.unit}</td>
								<td><fmt:formatNumber type="number"   minFractionDigits="2" maxFractionDigits="2"
								 value="${creditDetail.discount}" /></td>
								
								</c:if>
								<td><fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${creditDetail.transaction_amount}" /></td>
							</tr>
						</c:forEach>
						</c:if>
						<c:if test="${creditNote.CGST_head>0}">
							<tr>
								<td>CGST</td>
								<td></td>
								<td></td>
								<td></td>
								<td></td>
								<td><fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${creditNote.CGST_head}" /> </td>
							</tr>
						</c:if>
						<c:if test="${creditNote.SGST_head>0}">
							<tr>
								<td>SGST</td>
								<td></td>
								<td></td>
								<td></td>
								<td></td>
								<td><fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${creditNote.SGST_head}" /> </td>
							</tr>
						</c:if>
						<c:if test="${creditNote.IGST_head>0}">
							<tr>
								<td>IGST</td>
								<td></td>
								<td></td>
								<td></td>
								<td></td>
								<td><fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${creditNote.IGST_head}" /> </td>
							</tr>
						</c:if>
						<c:if test="${creditNote.SCT_head>0}">
							<tr>
								<td>CESS</td>
								<td></td>
								<td></td>
								<td></td>
								<td></td>
								<td><fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${creditNote.SCT_head}" /> </td>

							</tr>
						</c:if>
						<c:if test="${creditNote.total_vat>0}">
							<tr>
								<td>VAT</td>
								<td></td>
								<td></td>
								<td></td>
								<td></td>
								<td><fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${creditNote.total_vat}" /> </td>

							</tr>
						</c:if>
						<c:if test="${creditNote.total_vatcst>0}">
							<tr>
								<td>CST</td>
								<td></td>
								<td></td>
								<td></td>
								<td></td>
								<td><fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${creditNote.total_vatcst}" /> </td>
							</tr>
						</c:if>
						<c:if test="${creditNote.total_excise>0}">
							<tr>
								<td>EXCISE</td>
								<td></td>
								<td></td>
								<td></td>
								<td></td>
								<td><fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${creditNote.total_excise}" /> </td>

							</tr>
						</c:if>
						<tr style='border: 1px solid #bbb'>
							<td>Narration: <br /> Being credit note issued to
								${creditNote.customer.firm_name} for ${creditNote.description == 1 ? "Sales return" : creditNote.description  == 2 ? "Post sale discount" : creditNote.description  == 3 ? "Deficiency in services" : creditNote.description  == 4 ? "Correction in invoices":creditNote.description  == 5 ? "Change in POS" :creditNote.description  == 6 ? "Finalization of provisional assessment" : creditNote.remark}
							</td>
							<c:if test="${creditNote.description != 2 && creditNote.description !=3}">							
								<td></td>
								<td></td>
								<td></td>
								<td></td>
							</c:if>
							<td><fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${creditNote.round_off+creditNote.tds_amount}" /> </td>
						</tr>
							<tr>
                                <c:if test="${creditNote.remark!=null }"> 
								
                                  <td colspan="3">>Remark:  ${creditNote.remark}</td>
                              </c:if>
                              <c:if test="${creditNote.remark==null }"> 
								
                                  <td colspan="3">>Remark: </td>
                              </c:if>
							</tr>
					</tbody>
				</table>
				<div class="row"  style = "text-align: center; margin:15px;">
			<button class="fassetBtn" type="button"  onclick ="creditNoteAccountingVoucherpdf('#Hiddentable', {type: 'pdf',
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
			
			<!--Profile UI view ended  -->
			
			<!--Hidden table pdf view started  -->
			
			<div class="table-scroll" style="display:none;" id="tableDiv">
			
			<table id="Hiddentable" >
					<!-- for PDf heading -->
					<tr >
						
						<td style="color:blue; margin-left: 50px;text-align: center;">Credit Note</td>
						
					</tr>
			
					<tr>
							<td>Date: ${date}</td>
					</tr>
			
					<tr>
						<td>Credit Note Voucher No: ${creditNote.voucher_no}</td>
						
					</tr>
					
					
					<tr>
							<td>Party A/c Name: ${creditNote.customer.firm_name}</td>
					</tr>
					
					<tr>
							<td>Current Balance: <fmt:formatNumber type="number"  minFractionDigits="2" maxFractionDigits="2" value="${closingbalance}" />
					</td>
					</tr>
					
					<tr>
							<td>Purchase Ledger: ${creditNote.subledger.ledger.ledger_name}</td>
					</tr>
					<tr>
					</tr>
					
				
					<tr id="row">
					
						<th style="width: 55%">Name of Item</th>
							<c:if test="${creditNote.description != 2 && creditNote.description !=3}">
									<th>Quantity</th>
									<th>Rate</th>
									<th>per</th>
									<th>Discount</th>
							</c:if>
							
						<th>Amount</th>
					
					</tr>
				<tbody>
				    
				    <c:if test="${!empty creditDetailsList}">
						<c:forEach var = "creditDetail" items = "${creditDetailsList}">
							<tr>
								<td>${creditDetail.product_id.product_name}</td> 
							   <c:if test="${creditNote.description != 2 && creditNote.description !=3}">
							   	<td><fmt:formatNumber type="number" minFractionDigits="2"
									maxFractionDigits="2" value="${creditDetail.quantity}" />
									</td>
										<td><fmt:formatNumber type="number" minFractionDigits="2"
									maxFractionDigits="2" value="${creditDetail.rate}" />
									</td>
										<td>${creditDetail.uom_id.unit}</td>
									</td>
										<td><fmt:formatNumber type="number" minFractionDigits="2"
									maxFractionDigits="2" value="${creditDetail.discount}" />
									</td>
									
								</c:if>
								<td><fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${creditDetail.transaction_amount}" /></td>
							</tr>
						</c:forEach>
						</c:if>
						<c:if test="${creditNote.CGST_head>0}">
							<tr>
								<td>CGST</td>
								<td></td>
								<td></td>
								<td></td>
								<td></td>
								<td><fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${creditNote.CGST_head}" /> </td>
							</tr>
						</c:if>
						<c:if test="${creditNote.SGST_head>0}">
							<tr>
								<td>SGST</td>
								<td></td>
								<td></td>
								<td></td>
								<td></td>
								<td><fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${creditNote.SGST_head}" /> </td>
							</tr>
						</c:if>
						<c:if test="${creditNote.IGST_head>0}">
							<tr>
								<td>IGST</td>
								<td></td>
								<td></td>
								<td></td>
								<td></td>
								<td><fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${creditNote.IGST_head}" /> </td>
							</tr>
						</c:if>
						<c:if test="${creditNote.SCT_head>0}">
							<tr>
								<td>CESS</td>
								<td></td>
								<td></td>
								<td></td>
								<td></td>
								<td><fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${creditNote.SCT_head}" /> </td>

							</tr>
						</c:if>
						<c:if test="${creditNote.total_vat>0}">
							<tr>
								<td>VAT</td>
								<td></td>
								<td></td>
								<td></td>
								<td></td>
								<td><fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${creditNote.total_vat}" /> </td>

							</tr>
						</c:if>
						<c:if test="${creditNote.total_vatcst>0}">
							<tr>
								<td>CST</td>
								<td></td>
								<td></td>
								<td></td>
								<td></td>
								<td><fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${creditNote.total_vatcst}" /> </td>
							</tr>
						</c:if>
						<c:if test="${creditNote.total_excise>0}">
							<tr>
								<td>EXCISE</td>
								<td></td>
								<td></td>
								<td></td>
								<td></td>
								<td><fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${creditNote.total_excise}" /> </td>

							</tr>
						</c:if>
						<tr style='border: 1px solid #bbb'>
							<td>Narration: <br /> Being credit note issued to
								${creditNote.customer.firm_name} for ${creditNote.description == 1 ? "Sales return" : creditNote.description  == 2 ? "Post sale discount" : creditNote.description  == 3 ? "Deficiency in services" : creditNote.description  == 4 ? "Correction in invoices":creditNote.description  == 5 ? "Change in POS" :creditNote.description  == 6 ? "Finalization of provisional assessment" : creditNote.remark}
							</td>
							<c:if test="${creditNote.description != 2 && creditNote.description !=3}">							
								<td></td>
								<td></td>
								<td></td>
								<td></td>
							</c:if>
							<td><fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${creditNote.round_off+creditNote.tds_amount}" /> </td>
						</tr>
						<tr>
                                <c:if test="${creditNote.remark!=null }"> 
								
                                  <td colspan="3">Remark:  ${creditNote.remark}</td>
                              </c:if>
                              <c:if test="${creditNote.remark==null }"> 
								
                                  <td colspan="3">>Remark: </td>
                              </c:if>
							</tr>
				</tbody>
				
			</table>
		</div> 
			
			
			
			
			<!--Hidden table pdf view ended  -->
			
		</div>
		
	</form:form>
</div>
<script type="text/javascript">

   function creditNoteAccountingVoucherpdf(selector, params) {
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
		window.location.assign('<c:url value = "editCreditNote"/>?id='+id);	
	}
	
	function back(){
		<c:if test="${flag==true}">  
		window.location.assign('<c:url value = "creditNoteList"/>');
		</c:if>
		<c:if test="${flag==false}">  
		window.location.assign('<c:url value = "creditNoteFailure"/>');
		</c:if>
	}
	
	function DownloadPdf(id){
		window.location.assign('<c:url value = "downloadCreditNote"/>?id='+id);	
	}
</script>
<%@include file="/WEB-INF/includes/footer.jsp" %>