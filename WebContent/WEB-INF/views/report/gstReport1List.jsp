<%@include file="/WEB-INF/includes/header.jsp"%>
<div class="breadcrumb">
	<h3>GST Report 1</h3>
	<a href="homePage">Home</a> » <a href="gstReport1Input">GST Report 1</a>
</div>
<div class="col-md-12">
	<div class="borderForm">
		<div class="col-lg-12">
			<ul class="nav nav-tabs navtab-bg">
				<li class="active"><a href="#b2b" data-toggle="tab"
					aria-expanded="true"> <span class="visible-xs"><i
							class="fa fa-home"></i></span> <span class="hidden-xs">B2B</span>
				</a></li>
				<li class=""><a href="#b2cl" data-toggle="tab"
					aria-expanded="false"> <span class="visible-xs"><i
							class="fa fa-user"></i></span> <span class="hidden-xs">B2CL</span>
				</a></li>
				<li class=""><a href="#b2cs" data-toggle="tab"
					aria-expanded="false"> <span class="visible-xs"><i
							class="fa fa-user"></i></span> <span class="hidden-xs">B2CS</span>
				</a></li>
				<li class=""><a href="#cdnr" data-toggle="tab"
					aria-expanded="false"> <span class="visible-xs"><i
							class="fa fa-user"></i></span> <span class="hidden-xs">CDNR</span>
				</a></li>
				<li class=""><a href="#cdnur" data-toggle="tab"
					aria-expanded="false"> <span class="visible-xs"><i
							class="fa fa-user"></i></span> <span class="hidden-xs">CDNUR</span>
				</a></li>
				<li class=""><a href="#exp" data-toggle="tab"
					aria-expanded="false"> <span class="visible-xs"><i
							class="fa fa-user"></i></span> <span class="hidden-xs">EXP</span>
				</a></li>
				<li class=""><a href="#at" data-toggle="tab"
					aria-expanded="false"> <span class="visible-xs"><i
							class="fa fa-user"></i></span> <span class="hidden-xs">AT</span>
				</a></li>
				<li class=""><a href="#atadj" data-toggle="tab"
					aria-expanded="false"> <span class="visible-xs"><i
							class="fa fa-user"></i></span> <span class="hidden-xs">ATADJ</span>
				</a></li>
				<li class=""><a href="#exemp" data-toggle="tab"
					aria-expanded="false"> <span class="visible-xs"><i
							class="fa fa-user"></i></span> <span class="hidden-xs">EXEMP</span>
				</a></li>
				<li class=""><a href="#hsn" data-toggle="tab"
					aria-expanded="false"> <span class="visible-xs"><i
							class="fa fa-user"></i></span> <span class="hidden-xs">HSN</span>
				</a></li>
				<li class=""><a href="#docs" data-toggle="tab"
					aria-expanded="false"> <span class="visible-xs"><i
							class="fa fa-user"></i></span> <span class="hidden-xs">Docs</span>
				</a></li>
			</ul>
			<div class="tab-content">
				<div class="tab-pane active table-scroll" id="b2b">
					<table id="table" data-toggle="table" data-search="false"
						data-escape="false" data-filter-control="true"
						data-show-export="false" data-click-to-select="true"
						data-pagination="true" data-page-size="10" data-toolbar="#toolbar"
						class="table gst-table">
						
      		   
				  
						<thead>
							<tr>
								<th data-field="gst no" data-filter-control="input"
									data-sortable="true">GSTIN/UIN of Recipient</th>
								<th data-field="invoice number" data-filter-control="input"
									data-sortable="true">Invoice Number</th>
								<th data-field="invvoice date" data-filter-control="input"
									data-sortable="true">Invoice date</th>
								<th data-field="invoice value" data-filter-control="input"
									data-sortable="true">Invoice Value</th>
								<th data-field="supply place" data-filter-control="input"
									data-sortable="true">Place Of Supply</th>
								<th data-field="rcm" data-filter-control="input"
									data-sortable="true">Reverse Charge</th>
								<th data-field="type" data-filter-control="input"
									data-sortable="true">Invoice Type</th>
								<th data-field="ecom-gst" data-filter-control="input"
									data-sortable="true">E-Commerce GSTIN</th>
								<th data-field="rate" data-filter-control="input"
									data-sortable="true">Rate</th>
								<th data-field="Taxable Value" data-filter-control="input"
									data-sortable="true">Taxable Value</th>
								<th data-field="Cess Amount" data-filter-control="input"
									data-sortable="true">Cess Amount</th>
							</tr>
						</thead>
						
						<tbody>
						
						
				<c:set var="Total_invoice" value="0"/>	
				<c:set var="Total_tax" value="0"/>
				 <c:set var="Total_cess" value="0"/>	
				 <c:forEach var="salesEntry" items="${gstReport1Form.b2bList}">
				 <c:if test="${salesEntry.customer != null}">
				
				 <c:set var="Total_tax" value="${Total_tax +salesEntry.transaction_value}" />
				 <c:set var="Total_cess" value="${Total_cess +salesEntry.state_compansation_tax}" />
				  <c:set var="Total_invoice" value="${Total_invoice +salesEntry.round_off+salesEntry.tds_amount}" />
				 </c:if>
				 
				 </c:forEach>
						   <tr>
								<td>No. of Recipients</td>
								<td>No. of Invoices</td>
								<td></td>
								<td>Total Invoice Value</td>
								<td></td>
								<td></td>
								<td></td>
								<td></td>
								<td></td>
								<td>Total Taxable Value</td>
								<td>Total Cess</td>
								
							</tr>
							
						 <tr>
						<td>${gstReport1Form.noOfRecipientsForB2bList}</td>
						<td>${gstReport1Form.b2bList.size()}</td>
						<td></td>
						<td class="tright"><fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${Total_invoice}" /></td>
						
						<td></td>
						<td></td>
						<td></td>
						<td></td>
						<td></td>
			 		    <td class="tright"><fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${Total_tax}" /></td>
			 		
						<td class="tright"><fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${Total_cess}" /></td>
			 	
					</tr>
		      
							
							<c:forEach var="salesEntry" items="${gstReport1Form.b2bList}">
							
							<c:forEach var="product" items="${salesEntry.productinfoList}">
							<c:if test="${salesEntry.customer != null}"> <!-- // for restrict ---entries against cash sales -->
							
								<tr>
									<td style="text-align: left;">${salesEntry.customer.gst_no}</td>
									<td style="text-align: left;">${salesEntry.voucher_no}</td>
										<td style="text-align: left;">
								<fmt:parseDate value="${salesEntry.created_date}" pattern="yyyy-MM-dd" var="parsedDate" type="date" />
                   				<fmt:formatDate value="${parsedDate}" var="createdDate" type="date" pattern="dd-MMM-yyyy" />
								${createdDate}
							</td>
									<td class="tright">
									<fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${salesEntry.round_off+salesEntry.tds_amount}" /></td>
									<td>${salesEntry.customer.state.state_name}-${salesEntry.customer.state.state_code}</td>
									<td style="text-align: left;">N</td>
									<td style="text-align: left;">--</td>
									<td style="text-align: left;">--</td>
									<td class="tright">
					    			<fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${product.gstRate}" /></td>
									<td class="tright">
									<fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${product.transaction_amount}" /></td>
									<td class="tright">
									<fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${product.state_com_tax}" /></td>
								</tr>
								</c:if>
								</c:forEach>
							</c:forEach>
						</tbody>
						
					</table>
				</div>
				<div class="tab-pane table-scroll" id="b2cl">
					<table id="table" data-toggle="table" data-search="false"
						data-escape="false" data-filter-control="true"
						data-show-export="false" data-click-to-select="true"
						data-pagination="true" data-page-size="10" data-toolbar="#toolbar"
						class="table gst-table">
						<thead>
							<tr>
								<th data-field="Invoice Number" data-filter-control="input"
									data-sortable="true">Invoice Number</th>
								<th data-field="Invoice date" data-filter-control="input"
									data-sortable="true">Invoice date</th>
								<th data-field="Invoice Value" data-filter-control="input"
									data-sortable="true">Invoice Value</th>
								<th data-field="Place Of Supply" data-filter-control="input"
									data-sortable="true">Place Of Supply</th>
								<th data-field="Rate" data-filter-control="input"
									data-sortable="true">Rate</th>
								<th data-field="Taxable Value" data-filter-control="input"
									data-sortable="true">Taxable Value</th>
								<th data-field="Cess Amount" data-filter-control="input"
									data-sortable="true">Cess Amount</th>
								<th data-field="E-Commerce GSTIN" data-filter-control="input"
									data-sortable="true">E-Commerce GSTIN</th>
							</tr>
						</thead>
						<tbody>
				
				<c:set var="Total_invoice" value="0"/>	
				<c:set var="Total_tax" value="0"/>
				 <c:set var="Total_cess" value="0"/>	
				 <c:forEach var="salesEntry" items="${gstReport1Form.b2clList}">
				 <c:if test="${salesEntry.customer != null}">
				 <c:set var="Total_tax" value="${Total_tax +salesEntry.transaction_value}" />
				 <c:set var="Total_cess" value="${Total_cess +salesEntry.state_compansation_tax}" />
				  <c:set var="Total_invoice" value="${Total_invoice +salesEntry.round_off+salesEntry.tds_amount}" />
				 </c:if>
				 
				  </c:forEach>
				  
				           <tr>
								<td>No. of Invoices</td>
								<td></td>
								<td>Total Invoice Value</td>
								<td></td>
								<td></td>
								<td>Total Taxable Value</td>
								<td>Total Cess</td>
								<td></td>
							</tr>
							
							 <tr>
						
						<td>${gstReport1Form.b2clList.size()}</td>
						<td></td>
						<td class="tright"><fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${Total_invoice}" /></td>
					
						<td></td>
						<td></td>
			 		    <td class="tright"><fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${Total_tax}" /></td>
						<td class="tright"><fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${Total_cess}" /></td>
			 	<td></td>
					</tr>
				 
				 
							<c:forEach var="salesEntry" items="${gstReport1Form.b2clList}">
							<c:forEach var="product" items="${salesEntry.productinfoList}">
							<c:if test="${salesEntry.customer != null}">
							 
								<tr>
									<td style="text-align: left;">${salesEntry.voucher_no}</td>
									<td style="text-align: left;">
								<fmt:parseDate value="${salesEntry.created_date}" pattern="yyyy-MM-dd" var="parsedDate" type="date" />
                   				<fmt:formatDate value="${parsedDate}" var="createdDate" type="date" pattern="dd-MMM-yyyy" />
								${createdDate}
							</td>
									<td class="tright"><fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${salesEntry.round_off+salesEntry.tds_amount}" /></td>
									<td style="text-align: left;">${salesEntry.customer.state.state_name}-${salesEntry.customer.state.state_code}</td>
									<td class="tright">
											 
										<fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${product.gstRate}" />
								   </td>
									<td class="tright">
									<fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${product.transaction_amount}" />
									</td>
									<td class="tright">
									
									<fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${product.state_com_tax}" />
									</td>
									<td style="text-align: left;">--</td>
								</tr>
							</c:if>
							</c:forEach>
							</c:forEach>
						</tbody>
						
					</table>
				</div>
				<div class="tab-pane table-scroll" id="b2cs">
					<table id="table" data-toggle="table" data-search="false"
						data-escape="false" data-filter-control="true"
						data-show-export="false" data-click-to-select="true"
						data-pagination="true" data-page-size="10" data-toolbar="#toolbar"
						class="table gst-table">
						<thead>
							<tr>
								<th data-field="Type" data-filter-control="input"
									data-sortable="true">Type</th>
							    <!--<th data-field="Customer" data-filter-control="input"
									data-sortable="true">Customer Company Name</th>-->
								<th data-field="Place Of Supply" data-filter-control="input"
									data-sortable="true">Place Of Supply</th>
								<th data-field="Rate" data-filter-control="input"
									data-sortable="true">Rate</th>
								<th data-field="Taxable Value" data-filter-control="input"
									data-sortable="true">Taxable Value</th>
								<th data-field="Cess Amount" data-filter-control="input"
									data-sortable="true">Cess Amount</th>
								<th data-field="E-Commerce GSTIN" data-filter-control="input"
									data-sortable="true">E-Commerce GSTIN</th>
							</tr>
						</thead>
						<tbody>
							<c:set var="Total_tax" value="0"/>
				 <c:set var="Total_cess" value="0"/>	
				 <c:forEach var="salesEntry" items="${gstReport1Form.b2csList}">
				 <c:set var="Total_tax" value="${Total_tax +salesEntry.transaction_value}" />
				 <c:set var="Total_cess" value="${Total_cess +salesEntry.state_compansation_tax}" />
				 </c:forEach>
				 
				            <tr>
				                <td></td>
				                <!-- <td></td> -->
								<td></td>
								<td></td>
								<td>Total Taxable  Value</td>
								<td>Total Cess</td>
								<td></td>
							</tr>
				 
				  <tr>
				        <td></td>
						 <!--<td></td>-->
						<td></td>
					    <td></td>
						<td class="tright"><fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${Total_tax}" /></td>
						<td class="tright"><fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${Total_cess}" /></td>
						<td></td>
					</tr>
							<c:forEach var="salesEntry" items="${gstReport1Form.b2csList}">
								<c:forEach var="product" items="${salesEntry.productinfoList}">
							
							
								<tr>
									<td style="text-align: left;">--</td>
									<!-- <td style="text-align: left;">${salesEntry.customer.firm_name}
									<c:choose>
											<c:when test="${salesEntry.sale_type == 1}">
												Cash Sales
											</c:when>
											<c:when test="${salesEntry.sale_type == 2}">
												Card Sales - ${salesEntry.bank.bank_name} - ${salesEntry.bank.account_no}
											</c:when>
										</c:choose>
									</td>-->
									<td style="text-align: left;">${salesEntry.customer.state.state_name}-${salesEntry.customer.state.state_code}</td>
									<td class="tright">
								    	
										<fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${product.gstRate}" />
									</td>
									<td class="tright">
									<fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${product.transaction_amount}" />
										</td>
									<td class="tright">
									<fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${product.state_com_tax}" />
									</td>
									<td style="text-align: left;">--</td>
								</tr>
								</c:forEach>
							</c:forEach>
							
							
						</tbody>
						  
					</table>
				</div>
				<div class="tab-pane table-scroll" id="cdnr">
					<table id="table" data-toggle="table" data-search="false"
						data-escape="false" data-filter-control="true"
						data-show-export="false" data-click-to-select="true"
						data-pagination="true" data-page-size="10" data-toolbar="#toolbar"
						class="table gst-table ">
						<thead>
							<tr>
								<th data-field="Recipient" data-filter-control="input"
									data-sortable="true">GSTIN/UIN of Recipient</th>
								<th data-field="Receipt Number" data-filter-control="input"
									data-sortable="true">Invoice/Advance Receipt Number</th>
								<th data-field="Receipt date" data-filter-control="input"
									data-sortable="true">Invoice/Advance Receipt date</th>
								<th data-field="Voucher Number" data-filter-control="input"
									data-sortable="true">Note/Refund Voucher Number</th>
								<th data-field="Voucher date" data-filter-control="input"
									data-sortable="true">Note/Refund Voucher date</th>
								<th data-field="Document Type" data-filter-control="input"
									data-sortable="true">Document Type</th>
								<th data-field="document" data-filter-control="input"
									data-sortable="true">Reason For Issuing document</th>
								<th data-field="Place Of Supply" data-filter-control="input"
									data-sortable="true">Place Of Supply</th>
								<th data-field="Voucher Value" data-filter-control="input"
									data-sortable="true">Note/Refund Voucher Value</th>
								<th data-field="Rate" data-filter-control="input"
									data-sortable="true">Rate</th>
								<th data-field="Taxable Value" data-filter-control="input"
									data-sortable="true">Taxable Value</th>
								<th data-field="Cess Amount" data-filter-control="input"
									data-sortable="true">Cess Amount</th>
								<th data-field="Pre GST" data-filter-control="input"
									data-sortable="true">Pre GST</th>
							</tr>
						</thead>
						
						<tbody>
						
						<c:set var="Total_invoice" value="0"/>	
				<c:set var="Total_tax" value="0"/>
				 <c:set var="Total_cess" value="0"/>	
				 <c:forEach var="creditNote" items="${gstReport1Form.cdnrList}">
				 <c:if test="${creditNote.customer != null}">
				 <c:set var="Total_tax" value="${Total_tax +creditNote.transaction_value_head}" />
				 <c:set var="Total_cess" value="${Total_cess +creditNote.SCT_head}" />
				  <c:set var="Total_invoice" value="${Total_invoice +creditNote.round_off+creditNote.tds_amount}" />
				 </c:if>
				 
				 </c:forEach>
				 
				             <tr>
								<td>No. of Recipients</td>
								<td>No. of Invoices</td>
								<td></td>
								<td>No. of Notes/Vouchers</td>
								<td></td>
								<td></td>
								<td></td>
								<td></td>
								<td>Total Note/Refund Voucher Value</td>
								<td></td>
								<td>Total Taxable Value</td>
								<td>Total Cess</td>
								<td></td>
							</tr>
				 
				  <tr>
						<td>${gstReport1Form.noOfRecipientsForCdnrList}</td>
						<td>${gstReport1Form.cdnrList.size()}</td>
						<td></td>
						<td>${gstReport1Form.cdnrList.size()}</td>
							<td></td>
								<td></td>
								<td></td>
								<td></td>
								
						<td class="tright"><fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${Total_invoice}" /></td>
						<td></td>
						
			 <td class="tright"><fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${Total_tax}" /></td>
			 		
						<td class="tright"><fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${Total_cess}" /></td>
									
						<td></td>
						
			 		   
			 	
					</tr>
				 
							<c:forEach var="creditNote" items="${gstReport1Form.cdnrList}">
							
							<c:forEach var="product" items="${creditNote.creditDetails}">
							
							<c:if test="${creditNote.customer != null}">
							     	
								<tr>
									<td style="text-align: left;">${creditNote.customer.gst_no}</td>
									<td style="text-align: left;">${creditNote.sales_bill_id.voucher_no}</td>
									<td style="text-align: left;">
								<fmt:parseDate value="${creditNote.sales_bill_id.created_date}" pattern="yyyy-MM-dd" var="parsedDate" type="date" />
                   				<fmt:formatDate value="${parsedDate}" var="createdDate" type="date" pattern="dd-MMM-yyyy" />
								${createdDate}
							</td>
									
									<td style="text-align: left;">${creditNote.voucher_no}</td>
									<td style="text-align: left;">
								<fmt:parseDate value="${creditNote.date}" pattern="yyyy-MM-dd" var="parsedDate" type="date" />
                   				<fmt:formatDate value="${parsedDate}" var="createdDate" type="date" pattern="dd-MMM-yyyy" />
								${createdDate}
							</td>
								
									<td style="text-align: left;">C</td>
									<c:if test="${creditNote.description != 7}">
									<td style="text-align: left;">${creditNote.description == 1 ? "Sales return" : creditNote.description  == 2 ? "Post sale discount" : creditNote.description  == 3 ? "Deficiency in services" : creditNote.description  == 4 ? "Correction in invoices":creditNote.description  == 5 ? "Change in POS" : "Finalization of provisional assessment"}
									 </td>
									  </c:if>	
									<c:if test="${creditNote.description == 7}">
								<td style="text-align: left;">${creditNote.remark}</td>			
						            </c:if>	
									
									<td style="text-align: left;">${creditNote.customer.state.state_name}-${creditNote.customer.state.state_code}</td>
									<td class="tright"><fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${creditNote.round_off+creditNote.tds_amount}" /></td>
									<td class="tright">
										
										<fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${product.gstRate}" />
                                     </td>
									<td class="tright">	
									<fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${product.transaction_amount}" /></td>
									<td class="tright">
									<fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${product.state_com_tax}" /></td>
									<td>--</td>
								</tr>
							</c:if>
							
							</c:forEach>
								
							</c:forEach>
						</tbody>
					</table>
				</div>
				<div class="tab-pane table-scroll" id="cdnur">
					<table id="table" data-toggle="table" data-search="false"
						data-escape="false" data-filter-control="true"
						data-show-export="false" data-click-to-select="true"
						data-pagination="true" data-page-size="10" data-toolbar="#toolbar"
						class="table gst-table">
						<thead>
							<tr>
								<th data-field="UR Type" data-filter-control="input"
									data-sortable="true">UR Type</th>
								<th data-field="Note/Refund Voucher Number"
									data-filter-control="input" data-sortable="true">Note/Refund Voucher Number</th>
								<th data-field="Note/Refund Voucher date"
									data-filter-control="input" data-sortable="true">Note/Refund Voucher date</th>
								<th data-field="Document Type" data-filter-control="input"
									data-sortable="true">Document Type</th>
									
							   <th data-field="Invoice Receipt number"
									data-filter-control="input" data-sortable="true">Invoice/Advance Receipt Number</th>	
							   <th data-field="Invoice Receipt date"
									data-filter-control="input" data-sortable="true">Invoice/Advance Receipt date</th>	
									
								<th data-field="Reason For Issuing document"
									data-filter-control="input" data-sortable="true">Reason
									For Issuing document</th>
								<th data-field="Place Of Supply" data-filter-control="input"
									data-sortable="true">Place Of Supply</th>
								<th data-field="Note/Refund Voucher Value"
									data-filter-control="input" data-sortable="true">Note/Refund Voucher Value</th>
								<th data-field="Rate" data-filter-control="input"
									data-sortable="true">Rate</th>
								<th data-field="Taxable Value" data-filter-control="input"
									data-sortable="true">Taxable Value</th>
								<th data-field="Cess Amount" data-filter-control="input"
									data-sortable="true">Cess Amount</th>
								<th data-field="Pre GST" data-filter-control="input"
									data-sortable="true">Pre GST</th>
							</tr>
						</thead>
						
		
				 
						<tbody>
						
						<c:set var="Total_invoice" value="0"/>	
				<c:set var="Total_tax" value="0"/>
				 <c:set var="Total_cess" value="0"/>	
				 <c:forEach var="creditNote" items="${gstReport1Form.cdnurList}">
				 <c:if test="${creditNote.customer != null}">
				 <c:set var="Total_tax" value="${Total_tax +creditNote.transaction_value_head}" />
				 <c:set var="Total_cess" value="${Total_cess +creditNote.SCT_head}" />
				  <c:set var="Total_invoice" value="${Total_invoice +creditNote.round_off+creditNote.tds_amount}" />
				 </c:if>
				 
				 </c:forEach>
				 
				 
				           <tr>
				                <td></td>
								<td>No. of Notes/Vouchers</td>
								<td></td>
								<td></td>
								<td>No. of Invoices</td>
								<td></td>
								<td></td>
								<td></td>
								<td>Total Note Value</td>
								 <td></td>
								<td>Total Taxable Value</td>
								<td>Total Cess</td>
							    <td></td>
							</tr>
							
							
							 <tr>
						<td></td>
						<td>${gstReport1Form.cdnurList.size()}</td>
						<td></td>
						<td></td>
						<td>${gstReport1Form.cdnurList.size()}</td>
						<td></td>
								<td></td>
								<td></td>
								
						<td class="tright"><fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${Total_invoice}" /></td>
						<td></td>
						
			 <td class="tright"><fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${Total_tax}" /></td>
			 		
						<td class="tright"><fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${Total_cess}" /></td>
									
						<td></td>
						
			 		   
			 	
					</tr>
					
							
				 
							<c:forEach var="creditNote" items="${gstReport1Form.cdnurList}">
							
								<c:forEach var="product" items="${creditNote.creditDetails}">
							<c:if test="${creditNote.customer != null}">
							
								<tr>
									<td style="text-align: left;">--</td>
									<td style="text-align: left;">${creditNote.voucher_no}</td>
									<td style="text-align: left;">
								<fmt:parseDate value="${creditNote.date}" pattern="yyyy-MM-dd" var="parsedDate" type="date" />
                   				<fmt:formatDate value="${parsedDate}" var="createdDate" type="date" pattern="dd-MMM-yyyy" />
								${createdDate}
							</td>
									<td style="text-align: left;">C</td>
									<td style="text-align: left;">${creditNote.sales_bill_id.voucher_no}</td>
								<td style="text-align: left;">
								<fmt:parseDate value="${creditNote.sales_bill_id.created_date}" pattern="yyyy-MM-dd" var="parsedDate" type="date" />
                   				<fmt:formatDate value="${parsedDate}" var="createdDate" type="date" pattern="dd-MMM-yyyy" />
								${createdDate}
							</td >
									<c:if test="${creditNote.description != 7}">
									<td style="text-align: left;">${creditNote.description == 1 ? "Sales return" : creditNote.description  == 2 ? "Post sale discount" : creditNote.description  == 3 ? "Deficiency in services" : creditNote.description  == 4 ? "Correction in invoices":creditNote.description  == 5 ? "Change in POS" : "Finalization of provisional assessment"}
									 </td>
									  </c:if>	
									<c:if test="${creditNote.description == 7}">
								<td style="text-align: left;">${creditNote.remark}</td>			
						            </c:if>
									<td style="text-align: left;">${creditNote.customer.state.state_name}-${creditNote.customer.state.state_code}</td>
									<td class="tright">
									<fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${creditNote.round_off+creditNote.tds_amount}" /></td>
									<td class="tright">
										<fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${product.gstRate}" />
                                     </td>
									<td class="tright">	
									<fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${product.transaction_amount}" /></td>
									<td class="tright">
									<fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${product.state_com_tax}" /></td>
									<td style="text-align: left;">--</td>
								</tr>
							</c:if>
							</c:forEach>
							</c:forEach>
						</tbody>
						
					</table>
				</div>
				<div class="tab-pane table-scroll" id="exp">
					<table id="table" data-toggle="table" data-search="false"
						data-escape="false" data-filter-control="true"
						data-show-export="false" data-click-to-select="true"
						data-pagination="true" data-page-size="10" data-toolbar="#toolbar"
						class="table gst-table">
						<thead>
							<tr>
								<th data-field="Export Type" data-filter-control="input"
									data-sortable="true">Export Type</th>
								<th data-field="Invoice Number" data-filter-control="input"
									data-sortable="true">Invoice Number</th>
								<th data-field="Invoice date" data-filter-control="input"
									data-sortable="true">Invoice date</th>
								<th data-field="Invoice Value" data-filter-control="input"
									data-sortable="true">Invoice Value</th>
								<th data-field="Port Code" data-filter-control="input"
									data-sortable="true">Port Code</th>
								<th data-field="Shipping Bill Number"
									data-filter-control="input" data-sortable="true">Shipping
									Bill Number</th>
								<th data-field="Shipping Bill Date" data-filter-control="input"
									data-sortable="true">Shipping Bill Date</th>
								<th data-field="Rate" data-filter-control="input"
									data-sortable="true">Rate</th>
								<th data-field="Taxable Value" data-filter-control="input"
									data-sortable="true">Taxable Value</th>
							</tr>
						</thead>
						
						<tbody>
						
				
				<c:set var="Total_invoice" value="0"/>	
				<c:set var="Total_tax" value="0"/>
				 <c:forEach var="salesEntry" items="${gstReport1Form.expList}">
				 <c:if test="${salesEntry.customer != null}">
				 <c:set var="Total_tax" value="${Total_tax +salesEntry.transaction_value}" />
				  <c:set var="Total_invoice" value="${Total_invoice +salesEntry.round_off+salesEntry.tds_amount}" />
				 </c:if>
				 
				 </c:forEach>
				 
				  <tr>
				  <td></td>
				  <td>No. of Invoices</td>
				  <td></td>
				  <td>Total Invoice Value</td>
					<td></td>
					<td>No. of Shipping Bill</td>	
					<td></td>
								<td></td>	
								<td>Total Taxable Value</td>	
								
				  </tr>
				  
				  <tr>
						<td></td>
						<td>${gstReport1Form.expList.size()}</td>
						<td></td>
						<td class="tright"><fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${Total_invoice}" /></td>
						
						<td></td>
						<td>${gstReport1Form.expList.size()}</td>
						
						<td></td>
						<td></td>
						 <td class="tright"><fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${Total_tax}" /></td>
					</tr>
							
				 
						
							<c:forEach var="salesEntry" items="${gstReport1Form.expList}">
							<c:if test="${salesEntry.customer != null}">
							
								<tr>
									<td style="text-align: left;">${salesEntry.export_type==1 ? "WPAY" : "WOPAY"}</td>		
									<td style="text-align: left;">${salesEntry.voucher_no}</td>
									<td style="text-align: left;">
								<fmt:parseDate value="${salesEntry.created_date}" pattern="yyyy-MM-dd" var="parsedDate" type="date" />
                   				<fmt:formatDate value="${parsedDate}" var="createdDate" type="date" pattern="dd-MMM-yyyy" />
								${createdDate}
							</td>
									
									<td class="tright"><fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${salesEntry.round_off+salesEntry.tds_amount}" /></td>
									<td style="text-align: left;">${salesEntry.port_code}</td>
									<td style="text-align: left;">${salesEntry.shipping_bill_no}</td>
									<td style="text-align: left;">
								<fmt:parseDate value="${salesEntry.shipping_bill_date}" pattern="yyyy-MM-dd" var="parsedDate" type="date" />
                   				<fmt:formatDate value="${parsedDate}" var="createdDate" type="date" pattern="dd-MMM-yyyy" />
								${createdDate}
							</td>
									
									<td class="tright">
								    	
										<fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${product.gstRate}" />
									</td>
									<td class="tright">
									<fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${product.transaction_amount}" />
									 	</td>
								</tr>
							</c:if>
							</c:forEach>
						</tbody>
					</table>

				</div>
				<div class="tab-pane table-scroll" id="at">					
					<table id="table" data-toggle="table" data-search="false"
						data-escape="false" data-filter-control="true"
						data-show-export="false" data-click-to-select="true"
						data-pagination="true" data-page-size="10" data-toolbar="#toolbar"
						class="table gst-table">
						<thead>
							<tr>
								<th data-field="gst no" data-filter-control="input"
									data-sortable="true">Place Of Supply</th>
								<th data-field="invoice number" data-filter-control="input"
									data-sortable="true">Rate</th>
								<th data-field="invvoice date" data-filter-control="input"
									data-sortable="true">Gross Advance Received</th>
								<th data-field="Cess Amount" data-filter-control="input"
									data-sortable="true">Cess Amount</th>
							</tr>
						</thead>
               	
					
				 
						<tbody>
						
				 <c:set var="Total_invoice" value="0"/>
				 <c:set var="Total_cess" value="0"/>
				 
				  <c:forEach var="receipt" items="${gstReport1Form.atList}">
				 <c:if test="${receipt.customer != null}">
				 <c:set var="Total_cess" value="${Total_cess +receipt.state_compansation_tax}" />
				  <c:set var="Total_invoice" value="${Total_invoice +receipt.amount+receipt.tds_amount}" />
				 </c:if>
				 
				 </c:forEach>
				 
				        <tr>
				        <td></td>
				        <td></td>
								<td>Total Advance Received</td>
								<td>Total Cess</td>
								</tr>
								
								 <tr>
						<td></td>
						<td></td>
						
						<td class="tright"><fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${Total_invoice}" /></td>
						<td class="tright"><fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${Total_cess}" /></td>
			
					</tr>
							
							<c:forEach var="receipt" items="${gstReport1Form.atList}">
							<c:if test="${receipt.customer != null}">
							 <c:set var="total_count_rate" value="0"/>	
								<tr>
									<td style="text-align: left;">${receipt.customer.state.state_name}-${receipt.customer.state.state_code}</td>
									<td class="tright">
										<c:set var="total_count_rate" value="${total_count_rate+receipt.cgst + receipt.sgst + receipt.igst}" />
										<c:set var="Total_rate" value="${Total_rate +total_count_rate}" />	 
										<fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${total_count_rate}" />
									</td>
									<td class="tright">
									<fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${receipt.amount+receipt.tds_amount}" /></td>
									<td class="tright">
									<fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${receipt.state_compansation_tax}" /></td>
								</tr>
							</c:if>
							</c:forEach>
						</tbody>
						
					</table>
				</div>
				<div class="tab-pane table-scroll" id="atadj">					
					<table id="table" data-toggle="table" data-search="false"
						data-escape="false" data-filter-control="true"
						data-show-export="false" data-click-to-select="true"
						data-pagination="true" data-page-size="10" data-toolbar="#toolbar"
						class="table gst-table">
						<thead>
							<tr>
								<th data-field="gst no" data-filter-control="input"
									data-sortable="true">Place Of Supply</th>
								<th data-field="invoice number" data-filter-control="input"
									data-sortable="true">Rate</th>
								<th data-field="invvoice date" data-filter-control="input"
									data-sortable="true">Gross Advance Received</th>
								<th data-field="Cess Amount" data-filter-control="input"
									data-sortable="true">Cess Amount</th>
							</tr>
						</thead>
               <c:set var="Total_rate" value="0"/>		
				<c:set var="Total_gross" value="0"/>
				 <c:set var="Total_cess" value="0"/>	
						<tbody>
							<%-- <c:forEach var="receipt" items="${gstReport1Form.atAdjList}">
							<c:if test="${receipt.customer != null}">
							 <c:set var="total_count_rate" value="0"/>	
								<tr>
									<td style="text-align: left;">${receipt.customer.state.state_name}-${receipt.customer.state.state_code}</td>
								   <td class="tright">
										<c:set var="total_count_rate" value="${total_count_rate+receipt.cgst + receipt.sgst + receipt.igst}" />
										<c:set var="Total_rate" value="${Total_rate +total_count_rate}" />	 
										<fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${total_count_rate}" />
									</td>
									<td class="tright"><c:set var="Total_gross" value="${Total_gross +receipt.amount+receipt.tds_amount}" />
									<fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${receipt.amount+receipt.tds_amount}" /></td>
									<td class="tright"><c:set var="Total_cess" value="${Total_cess +receipt.state_compansation_tax}" />
									<fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${receipt.state_compansation_tax}" /></td>
								</tr>
							</c:if>
								
							</c:forEach> --%>
						</tbody>
						 <tfoot style='background-color: #eee'>
				<%-- 	<tr>
						<td></td>
						<td class="tright"><fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${Total_rate}" /></td>
			 		
			 		<td class="tright"><fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${Total_gross}" /></td>
			 		
						<td class="tright"><fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${Total_cess}" /></td>
			 	
			 	<td></td>
					</tr> --%>
				</tfoot>
					</table>
				</div>
				<div class="tab-pane table-scroll" id="exemp">
						<table id="table" data-toggle="table" data-search="false"
						data-escape="false" data-filter-control="true"
						data-show-export="false" data-click-to-select="true"
						data-pagination="true" data-page-size="10" data-toolbar="#toolbar"
						class="table gst-table">
						
						<thead>
							<tr>
								<th data-field="Description" data-filter-control="input"
									data-sortable="true">Description</th>
								<th data-field="NilRatedSupplies" data-filter-control="input"
									data-sortable="true">Nil Rated Supplies</th>
								<th data-field="NonGSTSupplies" data-filter-control="input"
									data-sortable="true">Non GST Supplies </th>
							</tr>
						</thead>
						<tbody>
						
						<c:set var="Total_nil" value="0"/>		
				<c:set var="Total_nongst" value="0"/>	
				
				<c:forEach var="form" items="${gstReport1Form.interRegisterList}">
				  <c:set var="Total_nil" value="${form[0]+form[1]}" />
				  
				 </c:forEach>
				 <c:forEach var="form" items="${gstReport1Form.intraRegisterList}">
				  <c:set var="Total_nil" value="${form[0]+form[1]}" />
				 </c:forEach>
				 <c:forEach var="form" items="${gstReport1Form.interNonRegisterList}">
				  <c:set var="Total_nil" value="${form[0]+form[1]}" />
				 </c:forEach>
				 <c:forEach var="form" items="${gstReport1Form.intraNonRegisterList}">
				  <c:set var="Total_nil" value="${form[0]+form[1]}" />
				 </c:forEach>
				 <c:forEach var="form" items="${gstReport1Form.interRegisterVATList}">
				  <c:set var="Total_nongst" value="${form[0]+form[1]}" />
				 </c:forEach>
				 <c:forEach var="form" items="${gstReport1Form.intraRegisterVATList}">
				  <c:set var="Total_nongst" value="${form[0]+form[1]}" />
				 </c:forEach>
				 <c:forEach var="form" items="${gstReport1Form.interNonRegisterVATList}">
				  <c:set var="Total_nongst" value="${form[0]+form[1]}" />
				 </c:forEach>
				 <c:forEach var="form" items="${gstReport1Form.intraNonRegisterVATList}">
				  <c:set var="Total_nongst" value="${form[0]+form[1]}" />
				 </c:forEach>
				 
						
						  <tr>
						  <td></td>
								<td>Total Nil Rated Supplies</td>
								<td>Total Non-GST Supplies</td>
								
							</tr>
							 <tr>
						  <td></td>
								<td class="tright"><fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${Total_nil}" /></td>
								<td class="tright"><fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${Total_nongst}" /></td>
								
							</tr>
							
							
							<c:forEach var="form"
								items="${gstReport1Form.interRegisterList}">
									<tr>
										<td style="text-align: left;">Inter-State supplies to registered persons</td>

										<td class="tright"><fmt:formatNumber type="number"
										minFractionDigits="2" maxFractionDigits="2"
										value="${form[0]+form[1]}" /></td>
										
										<td class="tright"><fmt:formatNumber type="number"
										minFractionDigits="2" maxFractionDigits="2"
										value="0" /></td>
									</tr>
						   </c:forEach>
						   
							<c:forEach var="form" items="${gstReport1Form.intraRegisterList}">
								<tr>
									<td style="text-align: left;">Intra-State supplies to registered persons</td>
									<td class="tright"><fmt:formatNumber type="number"
										minFractionDigits="2" maxFractionDigits="2"
										value="${form[0]+form[1]}" /></td>
										
										<td class="tright"><fmt:formatNumber type="number"
										minFractionDigits="2" maxFractionDigits="2"
										value="0" /></td>
								</tr>
							</c:forEach>
							
							<c:forEach var="form"
								items="${gstReport1Form.interNonRegisterList}">
								<tr>
									<td style="text-align: left;">Inter-State supplies to unregistered persons</td>
																		<td class="tright"><fmt:formatNumber type="number"
										minFractionDigits="2" maxFractionDigits="2"
										value="${form[0]+form[1]}" /></td>
										
										<td class="tright"><fmt:formatNumber type="number"
										minFractionDigits="2" maxFractionDigits="2"
										value="0" /></td>
								</tr>
							</c:forEach>
							
							<c:forEach var="form"
								items="${gstReport1Form.intraNonRegisterList}">
								<tr>
									<td style="text-align: left;">Intra-State supplies to unregistered persons</td>
									<td class="tright"><fmt:formatNumber type="number"
										minFractionDigits="2" maxFractionDigits="2"
										value="${form[0]+form[1]}" /></td>
										
										<td class="tright"><fmt:formatNumber type="number"
										minFractionDigits="2" maxFractionDigits="2"
										value="0" /></td>
								</tr>
							</c:forEach>
							
							<c:forEach var="form"
								items="${gstReport1Form.interRegisterVATList}">
								<tr>
									<td style="text-align: left;">Inter-State supplies to registered persons(Non-GST)</td>
																		<td class="tright"><fmt:formatNumber type="number"
										minFractionDigits="2" maxFractionDigits="2"
										value="0" /></td>
										
									<td class="tright"><fmt:formatNumber type="number"
										minFractionDigits="2" maxFractionDigits="2"
										value="${form[0]+form[1]}" /></td>
										
								</tr>
							</c:forEach>
							
							<c:forEach var="form"
								items="${gstReport1Form.intraRegisterVATList}">
								<tr>
										<td style="text-align: left;">Intra-State supplies to registered persons(Non-GST)</td>
																		<td class="tright"><fmt:formatNumber type="number"
										minFractionDigits="2" maxFractionDigits="2"
										value="0" /></td>
										
									<td class="tright"><fmt:formatNumber type="number"
										minFractionDigits="2" maxFractionDigits="2"
										value="${form[0]+form[1]}" /></td>
										
								</tr>
							</c:forEach>
							
							<c:forEach var="form"
								items="${gstReport1Form.interNonRegisterVATList}">
								<tr>
									<td style="text-align: left;">Inter-State supplies to unregistered persons(Non-GST)</td>
										
																		<td class="tright"><fmt:formatNumber type="number"
										minFractionDigits="2" maxFractionDigits="2"
										value="0" /></td>
										
									<td class="tright"><fmt:formatNumber type="number"
										minFractionDigits="2" maxFractionDigits="2"
										value="${form[0]+form[1]}" /></td>
										
								</tr>
							</c:forEach>
							
							<c:forEach var="form"
								items="${gstReport1Form.intraNonRegisterVATList}">
								<tr>
										<td style="text-align: left;">Intra-State supplies to unregistered persons(Non-GST)</td>
																		<td class="tright"><fmt:formatNumber type="number"
										minFractionDigits="2" maxFractionDigits="2"
										value="0" /></td>
										
									<td class="tright"><fmt:formatNumber type="number"
										minFractionDigits="2" maxFractionDigits="2"
										value="${form[0]+form[1]}" /></td>
										
								</tr>
							</c:forEach>
						</tbody>
				</table>
				</div>
				
				
				<div class="tab-pane table-scroll" id="hsn">
				<table id="table" data-toggle="table" data-search="false"
						data-escape="false" data-filter-control="true"
						data-show-export="false" data-click-to-select="true"
						data-pagination="true" data-page-size="10" data-toolbar="#toolbar"
						class="table gst-table">
						<thead>
							<tr>
								<th data-field="HSN" data-filter-control="input"
									data-sortable="true">HSN</th>
								<th data-field="Description" data-filter-control="input"
									data-sortable="true">Description</th>
								<th data-field="UQC" data-filter-control="input"
									data-sortable="true">UQC</th>
								<th data-field="Total Quantity" data-filter-control="input"
									data-sortable="true">Total Quantity</th>
								<th data-field="Total Value" data-filter-control="input"
									data-sortable="true">Total Value</th>
								<th data-field="Taxable Value" data-filter-control="input"
									data-sortable="true">Taxable Value</th>
								<th data-field="Integrated Tax Amount" data-filter-control="input"
									data-sortable="true">Integrated Tax Amount</th>
								<th data-field="Central Tax Amount" data-filter-control="input"
									data-sortable="true">Central Tax Amount</th>
								<th data-field="State/UT Tax Amount" data-filter-control="input"
									data-sortable="true">State/UT Tax Amount</th>
								<th data-field="Cess Amount" data-filter-control="input"
									data-sortable="true">Cess Amount</th>
							</tr>
						</thead>
						<tbody>
							
						
							<c:set var="Total_value" value="0" />
							<c:set var="Total_taxable" value="0" />
							<c:set var="Total_igst" value="0" />
							<c:set var="Total_sgst" value="0" />
							<c:set var="Total_cgst" value="0" />
							<c:set var="Total_cess" value="0" />
							
							
							 <c:forEach var="hsnReport" items="${gstReport1Form.hsnReportList}">
				
				 <c:set var="Total_value" value="${Total_value+hsnReport.totalValue}" />
				 <c:set var="Total_taxable" value="${Total_taxable +hsnReport.taxableValue}" />
				 <c:set var="Total_igst" value="${Total_igst +hsnReport.igstAmount}" />
				  <c:set var="Total_sgst" value="${Total_sgst +hsnReport.sgstAmount}" />
				 <c:set var="Total_cgst" value="${Total_cgst +hsnReport.cgstAmount}" />
				  <c:set var="Total_cess" value="${Total_cess +hsnReport.cessAmount}" />
				 
				 </c:forEach>
				 
				            <tr>
								<td>No. of HSN</td>
								<td></td>
								<td></td>
								<td></td>
								
								<td>Total Value</td>
								<td>Total Taxable Value</td>
								<td>Total Integrated Tax</td>
								<td>Total Central Tax</td>
								<td>Total State/UT Tax</td>
								<td>Total Cess</td>
								
								
								
							</tr>
							
							 <tr>
							 	<td>${gstReport1Form.hsnReportList.size()}</td>
					<td></td>
					<td></td>
					<td></td>
					<td class="tright"><fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${Total_value}" /></td>
					<td class="tright"><fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${Total_taxable}" /></td>
					<td class="tright"><fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${Total_igst}" /></td>
					<td class="tright"><fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${Total_sgst}" /></td>
					<td class="tright"><fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${Total_cgst}" /></td>
					<td class="tright"><fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${Total_cess}" /></td>
					</tr>
					
							
				 
							<c:forEach var="hsnReport" items="${gstReport1Form.hsnReportList}">
								<tr>
									<td style="text-align: left;">${hsnReport.hsnCode}</td>
									<td style="text-align: left;"></td>
									<td style="text-align: left;">${hsnReport.uqc}</td>
									<td class="tright">
									<fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${hsnReport.totalQuantity}" />
									</td>
									<td class="tright">
										<fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${hsnReport.totalValue}" />
										
									</td>
									<td class="tright">
							<fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${hsnReport.taxableValue}" />
										
									</td>
									<td class="tright">
							<fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${hsnReport.igstAmount}" />
										
									</td>
									<td class="tright">
										<fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${hsnReport.cgstAmount}" />
									
									</td>
									<td class="tright">
										<fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${hsnReport.sgstAmount}" />
										
									</td>
									<td class="tright">
								<fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${hsnReport.cessAmount}" />
										
									</td>
								</tr>
							</c:forEach>
						</tbody>
					</table>
				
				
				
				</div>
				<div class="tab-pane table-scroll" id="docs">
						<table id="table" data-toggle="table" data-search="false"
						data-escape="false" data-filter-control="true"
						data-show-export="false" data-click-to-select="true"
						data-pagination="true" data-page-size="10" data-toolbar="#toolbar"
						class="table gst-table">
						
						<thead>
							<tr>
								<th data-field="Nature  of Document" data-filter-control="input"
									data-sortable="true">Nature  of Document</th>
								<th data-field="Sr. No. From" data-filter-control="input"
									data-sortable="true">Sr. No. From</th>
								<th data-field="Sr. No. To" data-filter-control="input"
									data-sortable="true">Sr. No. To </th>
								<th data-field="Total Number" data-filter-control="input"
									data-sortable="true">Total Number</th>	
								<th data-field="Cancelled" data-filter-control="input"
									data-sortable="true">Cancelled</th>		
							</tr>
						</thead>
						<tbody>
						
						<c:set var="Totalnumber" value="0"/>
				 <c:set var="Total_cancel" value="0"/>	
				 
				 <c:forEach var="form"
								items="${gstReport1Form.outwardSupplyList}">
							
									 <c:set var="Totalnumber" value="${Totalnumber +form.totalNumber}" />
									  <c:set var="Total_cancel" value="${Total_cancel +form.totalCancel}" />
						   </c:forEach>
						   
						    <c:forEach var="form"
								items="${gstReport1Form.inwardSupplyList}">
									<c:set var="Totalnumber" value="${Totalnumber +form.totalNumber}" />
									  <c:set var="Total_cancel" value="${Total_cancel +form.totalCancel}" />
						   </c:forEach>
						    <c:forEach var="form"
								items="${gstReport1Form.creditNoteList}">
							
									 <c:set var="Totalnumber" value="${Totalnumber +form.totalNumber}" />
									  <c:set var="Total_cancel" value="${Total_cancel +form.totalCancel}" />
						   </c:forEach>
						    <c:forEach var="form"
								items="${gstReport1Form.debitNoteList}">
							
									 <c:set var="Totalnumber" value="${Totalnumber +form.totalNumber}" />
									  <c:set var="Total_cancel" value="${Total_cancel +form.totalCancel}" />
						   </c:forEach>
						   
						    <tr>
								<td></td>
								<td></td>
								<td></td>
								<td>Total Number</td>
								<td>Total Cancelled</td>
							</tr>
							
							 <tr>
								<td></td>
								<td></td>
								<td></td>
								<td>${Totalnumber}</td>
								<td>${Total_cancel}</td>
							</tr>
							
						
							<c:forEach var="form"
								items="${gstReport1Form.outwardSupplyList}">
									<tr>
										<td style="text-align: left;">${form.natureOfdocument}</td>
                                        <td style="text-align: left;">${form.srnofrom}</td>
                                        <td style="text-align: left;">${form.srnoto}</td>
                                        <td style="text-align: left;">${form.totalNumber}</td>
                                        <td style="text-align: left;">${form.totalCancel}</td>
									</tr>
						   </c:forEach>
						   <c:forEach var="form"
								items="${gstReport1Form.inwardSupplyList}">
									<tr>
										<td style="text-align: left;">${form.natureOfdocument}</td>
                                        <td style="text-align: left;">${form.srnofrom}</td>
                                        <td style="text-align: left;">${form.srnoto}</td>
                                        <td style="text-align: left;">${form.totalNumber}</td>
                                        <td style="text-align: left;">${form.totalCancel}</td>
									</tr>
						   </c:forEach>
						     <c:forEach var="form"
								items="${gstReport1Form.creditNoteList}">
									<tr>
										<td style="text-align: left;">${form.natureOfdocument}</td>
                                        <td style="text-align: left;">${form.srnofrom}</td>
                                        <td style="text-align: left;">${form.srnoto}</td>
                                        <td style="text-align: left;">${form.totalNumber}</td>
                                        <td style="text-align: left;">${form.totalCancel}</td>
									</tr>
						   </c:forEach>
						   <c:forEach var="form"
								items="${gstReport1Form.debitNoteList}">
									<tr>
										<td style="text-align: left;">${form.natureOfdocument}</td>
                                        <td style="text-align: left;">${form.srnofrom}</td>
                                        <td style="text-align: left;">${form.srnoto}</td>
                                        <td style="text-align: left;">${form.totalNumber}</td>
                                        <td style="text-align: left;">${form.totalCancel}</td>
									</tr>
						   </c:forEach>
						</tbody>
				</table>
				</div>
			</div>
		</div>

		<div class="row text-center-btn">
		<c:if test="${role!=7}">
			<button class="fassetBtn" type="button" onclick="excel();">
				Download as Excel
			</button>
			</c:if>
			<button class="fassetBtn" type="button" onclick="back();">
				<spring:message code="btn_back" />
			</button>
		</div>
	</div>
</div>
<script type="text/javascript">
	function back(){
		window.location.assign('<c:url value = "gstReport1Input"/>');
	}
	function excel(){
		window.location.assign('<c:url value = "downloadGSTReport1Excel"/>');
	}
</script>
<%@include file="/WEB-INF/includes/footer.jsp"%>