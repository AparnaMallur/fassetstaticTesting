<%@include file="/WEB-INF/includes/header.jsp"%>
<%@page import="com.fasset.controller.abstracts.MyAbstractController"%>
<spring:url value="/resources/images/edit.png" var="editImg" />
<spring:url value="/resources/images/view.png" var="viewImg" />
<spring:url value="/resources/images/delete.png" var="deleteImg" />
<spring:url value="/resources/images/add.png" var="additionImg" />
<%@ taglib uri = "http://java.sun.com/jsp/jstl/functions" prefix = "fn" %>


<script type="text/javascript" src="${valid}"></script>
<div class="breadcrumb">
	<h3>Opening Balances</h3>					
	<a href="homePage">Home</a> » <a href="subledgerOpeningList">Opening Balances</a>
</div>
<div class="col-md-12">		
<c:if test="${successMsg != null}">
	<div class="successMsg" id = "successMsg"> 
		<strong>${successMsg}</strong>
	</div>
</c:if>
	<div class="col-md-12 text-center" >
		<button  type="button" onclick = "admin()">
			Sub Ledgers
		</button>
		<c:if test="${clientflag==0}">
				<c:if test="${(opening_flag==0) && ( (role=='5') || (role=='2') || (role=='7') )}">
			    <button class="fassetBtn" type="button" onclick = "addexport()">
							Export Opening Balances
				</button>
				 <button class="fassetBtn" type="button" onclick = "uploaddivforopeningbalances()">
							Import Opening Balances
				</button>
				</c:if>
		</c:if>
		<c:if test="${(role=='2') || (role=='3') || (role=='4')}">
		<c:choose>
			<c:when test="${clientflag==1}">
			<!--  <button class="fassetBtn" type="button" onclick = "editcompanyopeningbalances()">
					           Company Balance
		                      </button> -->
			</c:when>
			<c:otherwise>
						      <button class="fassetBtn" type="button" onclick = "editclientopeningbalances()">
					           Edit Client Balance
		                      </button>
		      </c:otherwise>
		    </c:choose>
		  </c:if>
	</div>
	
	<div id="bulk_upload_opening_balance" class='row' >
	<div id="progress-div" style="text-align:center;display:none">
					<img src="${processing}" style="height:80px"/>
					<h4>File uploading is in progress...</h4>
				</div>
			<div class="text-center">
						<form:form  action="importOpeningBalancesExcel" method="post" enctype="multipart/form-data" onsubmit = "return validate();">
			    			<div style = "text-align: center;">			    				
								<label for="excelFile">Select Excel File:</label>	
								<input id = "excelFile" name="excelFile" type="file" accept="application/vnd.openxmlformats-officedocument.spreadsheetml.sheet, application/vnd.ms-excel" style = "display: inline; margin: 10px 0px 10px 0px;">	
			    	<input id ="importDate" name="importDate" type="hidden">
			    	<input id ="importRange" name="importRange" type="hidden">
			    			</div>
			    			<div class="form-inline" style="margin-top:2%" >
								<div class="form-group">
									<input type ="submit" class="logBt btn btn-primary" value="<spring:message code="btn_upload" />">				
								</div>
								<div class="form-group" style="margin-left: 30px;">
									<input class="logBt btn btn-primary" type="button" onclick = "hideuploaddivforopeningbalances()" id = "btnCancel" value="<spring:message code="btn_cancel"/>">				
								</div>
							</div>
						</form:form>
			</div>
	</div>
	<c:choose>
		<c:when test="${(opening_flag==0) && ( (role=='5') || (role=='2') || (role=='7') )}">
		
		<div class = "borderForm" id="subindustry-div" >
		<c:choose>
			<c:when test="${(clientflag==0) ||(clientflag==1)}">
						<div class="row">
							<div class="col-md-12 col-xs-12" style="margin:2% 0px">							  
							   		<lable>Opening Balance date: </lable><input type="text" id="date" />
							    	<lable>Year: </lable>
							    	    <select id="year" name="year"  style="width:25%">
							    	    <option value='0' selected disabled>Select Accounting Year</option>
							    	   				 <c:forEach var="year" items="${yearList}">
														<option value = "${year.year_id}" >${year.year_range}</option>	
													</c:forEach>
							    	    </select>
							</div>
							<c:forEach var="subledgerList" items="${subledgerList}">
									<c:set var="client_company_id" value="${subledgerList.company.company_id}" />	
									<input type="hidden" id="client_company_id" value="${client_company_id}"/>		
							</c:forEach>
							
					</div>	
					
			</c:when>
			<c:otherwise>
			
			<c:if test="${(role=='2') || (role=='3') || (role=='4')}">
			       <div class="row">
							<div class="col-md-12 col-xs-12" style="margin:2% 0px">							  
							   		<lable>Opening Balance date: </lable><input type="text" id="date" />
							    	<lable>Year: </lable>
							    	    <select id="year" name="year"  style="width:25%">
							    	    <option value='0' selected disabled>Select Accounting Year</option>
							    	   				 <c:forEach var="year" items="${yearList}">
														<option value = "${year.year_id}" >${year.year_range}</option>	
													</c:forEach>
							    	    </select>
							</div>
							<c:forEach var="subledgerList" items="${subledgerList}">
									<c:set var="client_company_id" value="${subledgerList.company.company_id}" />	
									<input type="hidden" id="client_company_id" value="${client_company_id}"/>		
							</c:forEach>
							
					</div>	
			<c:set var="client_company_name" value="0" />
			<c:set var="client_company_id" value="0" />	
			<c:forEach var="subledgerList" items="${subledgerList}">
					<c:set var="client_company_name" value="${subledgerList.company.company_name}" />	
					<c:set var="client_company_id" value="${subledgerList.company.company_id}" />	
					
			</c:forEach>
			<input type="hidden" id="client_company_id" value="${client_company_id}"/>			
			<h3 style='text-align:center'>Editing Client Balance For ${client_company_name}</h3>
			  </c:if>
			</c:otherwise>
			
		</c:choose>
		<div class="table-responsive">
		 <input class="form-control" id="search" onkeyup="searchTable();" placeholder="search distribution" name="s" autocomplete="off" autofocus>
			<table id="table" 
			 >
			<thead>
			<tr> 
				<th class='test' >Action</th>						
				<th  data-field="ledger" data-filter-control="input" data-sortable="true" >Ledger</th>
				<th  data-field="Subledger" data-filter-control="input" data-sortable="true" >Sub Ledger</th>
				<th  data-field="debit" data-filter-control="input" data-sortable="false" >Debit Balance</th>	
				<th  data-field="credit" data-filter-control="input" data-sortable="false" >Credit Balance</th>	
			
			</tr>
			</thead>				
			<tbody id="sub-div">
			<c:set var="total_credit_footer" value="0" />
			<c:set var="total_debit_footer" value="0" />			
			<c:set var="total_net_footer" value="0" />			

				<c:set var="total_sledger_data" value="1" />
				<input type="text" id="total-sledger-rows" value="${subledgerList.size()}" style="display:none"/>
				<c:forEach var="subledgerList" items="${subledgerList}">
					<tr>
					<td style="text-align: left;">
						<a class="acs-view" href = "#" onclick = "viewSubLedger('${subledgerList.subledger_Id}')"><img src='${viewImg}' style = "width: 20px;"/></a>
					</td>
					<td>${subledgerList.ledger.ledger_name}</td>
					<td>${subledgerList.subledger_name}</td>	
					<input type="text" id="sledger-data-${total_sledger_data}" value="${subledgerList.subledger_Id}" style="display:none"/>
					<c:set var="total_sledger_data" value="${total_sledger_data+1}" />		
					<!-- op code	 -->				
					<c:set var="subledgerListdebit_netamount" value="0" />			
					<c:set var="subledgerListcredit_netamount" value="0" />	
					<c:forEach var="subledgerOPList" items="${subledgerListOP}">					
							<c:choose>
								<c:when test="${subledgerOPList[0]==subledgerList.subledger_Id}">								
											<c:set var="subledgerListdebit_netamount" value="${subledgerOPList[1]}"/>
												
										<c:set var="total_debit_footer"	value="${total_debit_footer+subledgerOPList[1]}" />
								
											<c:set var="subledgerListcredit_netamount" value="${subledgerOPList[2]}"/>
												
									<c:set var="total_credit_footer" value="${total_credit_footer+subledgerOPList[2]}" />																
								</c:when>
							</c:choose>				            
					</c:forEach>
						 <td>
						
					      <fmt:formatNumber var = "subldgerDebitAmt" type="number" minFractionDigits="2" maxFractionDigits="2" value="${subledgerListdebit_netamount}"/>
					      
						 		<input type="text" class="subledgerDebitValue" id="sledgerdebit_balance_${subledgerList.subledger_Id}" value="${subldgerDebitAmt}" onchange="isDigit(id),checkAvailability(${subledgerList.subledger_Id})"   />
						 </td>	
						<td>				
						  <fmt:formatNumber var = "subldgerCreditAmt" type="number" minFractionDigits="2" maxFractionDigits="2" value="${subledgerListcredit_netamount}"/>
							<input type="text" class="subledgerCreditValue" id="sledgercredit_balance_${subledgerList.subledger_Id}" value="${subldgerCreditAmt}" onchange="isDigit(id),checkAvailability(${subledgerList.subledger_Id})" />
						</td>
					<!-- op code End -->				
						
					</tr>			
							
				</c:forEach>
			<tr><td colspan='5' ><h5 style='text-align:center'>Bank List</h5></td></tr>
				<c:set var="total_bank_data" value="1" />
			<input type="text" id="total-bank-rows" value="${bankList.size()}" style="display:none"/>
				
				<c:forEach var = "bank" items = "${bankList}">
				<tr>
					<td style="text-align: left;">
						<a class="acs-view" href = "#" onclick = "viewBank('${bank.bank_id}')"><img src='${viewImg}' style = "width: 20px;"/></a>
					</td>									
					<td style="text-align: left;">${bank.bank_name}</td>
					<td style="text-align: left;">${bank.branch}-${bank.account_no}</td>
					<input type="text" id="bank-data-${total_bank_data}" value="${bank.bank_id}" style="display:none"/>
					<c:set var="total_bank_data" value="${total_bank_data+1}" />	
							
								<!-- op code	 -->	
					<c:set var="bankListdebit_netamount" value="0" />			
					<c:set var="bankListcredit_netamount" value="0" />
					<c:forEach var="bankOPList" items="${bankListOP}">					
							<c:choose>
								<c:when test="${bankOPList[0]==bank.bank_id}">								
											<c:set var="bankListdebit_netamount" value="${bankOPList[1]}"/>
												
													<c:set var="total_debit_footer"	value="${total_debit_footer+bankOPList[1]}" />
									
											<c:set var="bankListcredit_netamount" value="${bankOPList[2]}" />
												
				<c:set var="total_credit_footer" value="${total_credit_footer+bankOPList[2]}" />
											
													
								</c:when>
							</c:choose>				            
					</c:forEach>
					<td>
					
					<fmt:formatNumber var = "bankDebitAmt" type="number" minFractionDigits="2" maxFractionDigits="2" value="${bankListdebit_netamount}"/>
					<input type="text" class="BankDebit" id="bankdebit_balance_${bank.bank_id}" value="${bankDebitAmt}" onchange="isDigit(id),checkAvailability(${bank.bank_id})"  />
					</td>
					
					<td>
					<fmt:formatNumber var = "bankCreditAmt" type="number" minFractionDigits="2" maxFractionDigits="2" value="${bankListcredit_netamount}"/>
					<input type="text" class="BankCredit" id="bankcredit_balance_${bank.bank_id}" value="${bankCreditAmt}" 
					onchange="isDigit(id),checkAvailability(${bank.bank_id})"   />
					</td>
												
								<!-- op code End	 -->				
			
			</tr>
			</c:forEach>
			<tr><td colspan='5' ><h5 style='text-align:center'>Supplier List</h5></td></tr>
				<c:set var="total_supplier_data" value="1" />
			<input type="text" id="total-supplier-rows" value="${supplierList.size()}" style="display:none"/>				
				<c:forEach var = "supplier" items = "${supplierList}">
				<tr>
					<td style="text-align: left;">
						<a class="acs-view" href = "#" onclick = "viewSupplier('${supplier.supplier_id}')"><img src='${viewImg}' style = "width: 20px;"/></a>
					</td>									
					<td style="text-align: left;">${supplier.company_name}</td>
					<td style="text-align: left;">${supplier.contact_name}</td>
				
					<input type="text" id="supplier-data-${total_supplier_data}" value="${supplier.supplier_id}" style="display:none"/>
					<c:set var="total_supplier_data" value="${total_supplier_data+1}" />	
						
					<!-- op code	 -->	
					<c:set var="supplierListdebit_netamount" value="0" />			
					<c:set var="supplierListcredit_netamount" value="0" />
					<c:forEach var="supplierOPList" items="${supplierListOP}">					
							<c:choose>
								<c:when test="${supplierOPList[0]==supplier.supplier_id}">								
											<c:set var="supplierListdebit_netamount" value="${supplierOPList[1]}" />
												
											<c:set var="total_debit_footer"	value="${total_debit_footer+supplierOPList[1]}" />
										
											<c:set var="supplierListcredit_netamount" value="${supplierOPList[2]}"/>
												
													<c:set var="total_credit_footer" value="${total_credit_footer+supplierOPList[2]}" />
											
													
								</c:when>
							</c:choose>				            
					</c:forEach>
					<td>
					<fmt:formatNumber var = "supplierDebitAmt" type="number" minFractionDigits="2" maxFractionDigits="2" value="${supplierListdebit_netamount}"/>
					<input type="text" class="SupplierDebit" id="supplierdebit_balance_${supplier.supplier_id}" value="${supplierDebitAmt}"
					 onchange="isDigit(id),checkAvailability(${supplier.supplier_id})"/>
					 
					</td>
					<td>
					<fmt:formatNumber var = "supplierCreditAmt" type="number" minFractionDigits="2" maxFractionDigits="2" value="${supplierListcredit_netamount}"/>
					<input type="text" class="SupplierCredit" id="suppliercredit_balance_${supplier.supplier_id}" value="${supplierCreditAmt}"
					 onchange="isDigit(id),checkAvailability(${supplier.supplier_id})"/>
					</td>
					
					<!-- op code End	 -->	
					
				</tr>
			</c:forEach>
			<tr><td colspan='5' ><h5 style='text-align:center'>Customer List</h5></td></tr>
				<c:set var="total_customer_data" value="1" />
			<input type="text" id="total-customer-rows" value="${customerList.size()}" style="display:none"/>
				
				<c:forEach var = "customer" items = "${customerList}">
				<tr>
					<td style="text-align: left;">
						<a class='acs-view' href = "#" onclick = "viewCustomer('${customer.customer_id}')"><img src='${viewImg}' style = "width: 20px;"/></a>
					</td>									
					<td style="text-align: left;">${customer.firm_name}</td>
					<td style="text-align: left;">${customer.contact_name}</td>
					
					<input type="text" id="customer-data-${total_customer_data}" value="${customer.customer_id}" style="display:none"/>
					<c:set var="total_customer_data" value="${total_customer_data+1}" />	
						
				<!-- op code	 -->	
				<c:set var="customerListdebit_netamount" value="0" />			
					<c:set var="customerListcredit_netamount" value="0" />
					<c:forEach var="customerOPList" items="${customerListOP}">	
								
							<c:choose>
								<c:when test="${customerOPList[0]==customer.customer_id}">								
											<c:set var="customerListdebit_netamount" value="${customerOPList[1]}" />
												
													<c:set var="total_debit_footer"	value="${total_debit_footer+customerOPList[1]}" />
											
											<c:set var="customerListcredit_netamount" value="${customerOPList[2]}"/>
											
								<c:set var="total_credit_footer" value="${total_credit_footer+customerOPList[2]}" />
								
													
								</c:when>
							</c:choose>				            
					</c:forEach>
						<td>
						<fmt:formatNumber var = "customerDebitAmt" type="number" minFractionDigits="2" maxFractionDigits="2" value="${customerListdebit_netamount}"/>
						<input type="text" class="CustomerDebit" id="customerdebit_balance_${customer.customer_id}"
						 value="${customerDebitAmt}" onchange="isDigit(id),checkAvailability(${customer.customer_id})" />
						</td>
		   			    <td>
		   			  <fmt:formatNumber var = "customerCreditAmt" type="number" minFractionDigits="2" maxFractionDigits="2" value="${customerListcredit_netamount}"/>
		   			    <input type="text" class="CustomerCredit" id="customercredit_balance_${customer.customer_id}"
value="${customerCreditAmt}" onchange="isDigit(id),checkAvailability(${customer.customer_id})"/>
		   			    </td>
		   			 	
				<!-- op code end	 -->	
						
				</tr>
			</c:forEach>
				<tfoot>
				<tr>
					<td colspan='3'></td>
					<td id="footer-debit-total">
							<c:set var="total_debit_footer">
								<fmt:formatNumber type="number" minFractionDigits="2"
									maxFractionDigits="2" value="${total_debit_footer}" />
							</c:set>
							${total_debit_footer}</td>
					<td id="footer-credit-total">
							<c:set var="total_credit_footer">
								<fmt:formatNumber type="number" minFractionDigits="2"
									maxFractionDigits="2" value="${total_credit_footer}" />
							</c:set>
							${total_credit_footer}</td>
					
				</tr>
					<tr>
						<td colspan='5' style="text-align:center">
						<c:choose>
							<c:when test="${opening_flag==0}">
						
								<button  id ="update_btn" type="button" onclick = "addopeningbalance(2,${clientflag})">
										Update Balances
									</button>
							</c:when>
							<c:otherwise>
								<h5>Opening balances can not edited after your trial balance is updated. In case you have to update Opening balance again, please contact to VDAKPO </h5>
							</c:otherwise>
						</c:choose>
							
						</td>
					</tr>
				</tfoot>
			</tbody>
			</table>
			</div>
	</div>
		</c:when>
		
		<c:otherwise>
		<div class="clearfix"></div>
		<div class = "borderForm" id="subindustry-div" >
		<div class="table-responsive">	
			<table id="table" 
			 data-toggle="table"
			 data-search="false"
			 data-escape="false"			 
			 data-filter-control="true" 
			 data-show-export="false" 
			 data-click-to-select="true"
			 data-pagination="true"
			 data-page-size="100"
			 data-toolbar="#toolbar" class = "table subledgerOpeningBalTable">
			<thead>
			<tr>
				<th class='test' >Action11</th>						
				<th  data-field="ledger" data-filter-control="input" data-sortable="true" >Ledger</th>
				<th  data-field="Subledger" data-filter-control="input" data-sortable="true" >Sub Ledger</th>
				<th  data-field="debit" data-filter-control="input" data-sortable="false" >Debit Balance</th>	
				<th  data-field="credit" data-filter-control="input" data-sortable="false" >Credit Balance</th>
				<th  data-field="running" data-filter-control="input" data-sortable="false" >Running Balance</th>	
		</tr>
			</thead>				
			<tbody id="sub-div">
			<c:set var="total_net_footer_credit" value="0" />			
			<c:set var="total_net_footer_debit" value="0" />			

				<input type="text" id="total-sledger-rows" value="${subledgerList.size()}" style="display:none"/>
				<c:forEach var="subledgerList" items="${subledgerList}">
				
					<tr>
					<td style="text-align: left;">
						<a class="acs-view" href = "#" onclick = "viewSubLedger('${subledgerList.subledger_Id}')"><img src='${viewImg}' style = "width: 20px;"/></a>
					</td>
					<td>${subledgerList.ledger.ledger_name}</td>
					<td>${subledgerList.subledger_name}</td>	
					<c:set var="subledgerListdebit_netamount" value="0" />			
					<c:set var="subledgerListcredit_netamount" value="0" />		
					<c:forEach var="subledgerOPList" items="${subledgerListOP}">					
							<c:choose>
								<c:when test="${subledgerOPList[0]==subledgerList.subledger_Id}">								
											<c:set var="subledgerListdebit_netamount">
												<fmt:formatNumber type="number" minFractionDigits="2"
													maxFractionDigits="2"
													value="${subledgerOPList[1]}" />
						<c:set var="total_net_footer_debit"	value="${total_net_footer_debit+subledgerOPList[1]}" /></td>
											</c:set> 
											<c:set var="subledgerListcredit_netamount">
												<fmt:formatNumber type="number" minFractionDigits="2"
													maxFractionDigits="2"
													value="${subledgerOPList[2]}" />
					<c:set var="total_net_footer_credit" value="${total_net_footer_credit+subledgerOPList[2]}" /></td>	
													
											</c:set> 
													
								</c:when>
							</c:choose>				            
					</c:forEach>
					<td>
					<fmt:parseNumber var = "debit" type = "number" value = "${subledgerListdebit_netamount}" />
					<fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${debit}"/>
					</td>
					<td>
					<fmt:parseNumber var = "credit" type = "number" value = "${subledgerListcredit_netamount}" />
					<fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${credit}"/>
					</td>
					<td>
					 <c:if test="${subledgerList.ledger.accsubgroup.accountGroup.postingSide.posting_id == 1}">
					<fmt:parseNumber var = "debit" type = "number" value = "${subledgerListdebit_netamount}" />
					<fmt:parseNumber var = "credit" type = "number" value = "${subledgerListcredit_netamount}" />
					<fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${debit-credit}"/>
					
					 </c:if>
					  <c:if test="${subledgerList.ledger.accsubgroup.accountGroup.postingSide.posting_id == 2}">
					  <fmt:parseNumber var = "debit" type = "number" value = "${subledgerListdebit_netamount}" />
					<fmt:parseNumber var = "credit" type = "number" value = "${subledgerListcredit_netamount}" />
					<fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${credit-debit}"/>
					 </c:if>
					  <c:if test="${subledgerList.ledger.accsubgroup.accountGroup.postingSide.posting_id == 3}">
					 <fmt:parseNumber var = "debit" type = "number" value = "${subledgerListdebit_netamount}" />
					<fmt:parseNumber var = "credit" type = "number" value = "${subledgerListcredit_netamount}" />
					<fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${debit-credit}"/>
					 </c:if>
					   <c:if test="${subledgerList.ledger.accsubgroup.accountGroup.postingSide.posting_id == 4}">
					  <fmt:parseNumber var = "debit" type = "number" value = "${subledgerListdebit_netamount}" />
					<fmt:parseNumber var = "credit" type = "number" value = "${subledgerListcredit_netamount}" />
					<fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${credit-debit}"/>
					 </c:if>
					</td>
					</tr>		
							
				</c:forEach>
				
			<tr><td colspan='4' ><h5 style='text-align:center'>Bank List</h5></td></tr>
				<c:set var="total_bank_data" value="1" />
			<input type="text" id="total-bank-rows" value="${bankList.size()}" style="display:none"/>
				
				<c:forEach var = "bank" items = "${bankList}">
				<tr>
					<td style="text-align: left;">
						<a class="acs-view" href = "#" onclick = "viewBank('${bank.bank_id}')"><img src='${viewImg}' style = "width: 20px;"/></a>
					</td>									
					<td style="text-align: left;">${bank.bank_name}</td>
					<td style="text-align: left;">${bank.branch}-${bank.account_no}</td>
					<c:set var="bankListdebit_netamount" value="0" />			
					<c:set var="bankListcredit_netamount" value="0" />		
					<c:forEach var="bankOPList" items="${bankListOP}">					
							<c:choose>
								<c:when test="${bankOPList[0]==bank.bank_id}">								
											<c:set var="bankListdebit_netamount">
												<fmt:formatNumber type="number" minFractionDigits="2"
													maxFractionDigits="2"
													value="${bankOPList[1]}" />
													<c:set var="total_net_footer_debit"	value="${total_net_footer_debit+bankOPList[1]}" />
											</c:set> 
											<c:set var="bankListcredit_netamount">
												<fmt:formatNumber type="number" minFractionDigits="2"
													maxFractionDigits="2"
													value="${bankOPList[2]}" />
				<c:set var="total_net_footer_credit" value="${total_net_footer_credit+bankOPList[2]}" />
											</c:set> 
													
								</c:when>
							</c:choose>				            
					</c:forEach>
					<td>
					<fmt:parseNumber var = "debit" type = "number" value = "${bankListdebit_netamount}" />
					<fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${debit}"/>
					</td>
					<td>
					<fmt:parseNumber var = "credit" type = "number" value = "${bankListcredit_netamount}" />
					<fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${credit}"/>
					</td>
					<fmt:parseNumber var = "debit" type = "number" value = "${bankListdebit_netamount}" />
					<fmt:parseNumber var = "credit" type = "number" value = "${bankListcredit_netamount}" />
					<td><fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${debit-credit}"/></td>
					     
					
				</tr>
			</c:forEach>
			<tr><td colspan='5' ><h5 style='text-align:center'>Supplier List</h5></td></tr>
				<c:set var="total_supplier_data" value="1" />
			<input type="text" id="total-supplier-rows" value="${supplierList.size()}" style="display:none"/>				
				<c:forEach var = "supplier" items = "${supplierList}">
				<tr>
					<td style="text-align: left;">
						<a class="acs-view" href = "#" onclick = "viewSupplier('${supplier.supplier_id}')"><img src='${viewImg}' style = "width: 20px;"/></a>
					</td>									
					<td style="text-align: left;">${supplier.company_name}</td>
					<td style="text-align: left;">${supplier.contact_name}</td>					
					<c:set var="supplierListdebit_netamount" value="0" />			
					<c:set var="supplierListcredit_netamount" value="0" />		
					<c:forEach var="supplierOPList" items="${supplierListOP}">					
							<c:choose>
								<c:when test="${supplierOPList[0]==supplier.supplier_id}">								
											<c:set var="supplierListdebit_netamount">
												<fmt:formatNumber type="number" minFractionDigits="2"
													maxFractionDigits="2"
													value="${supplierOPList[1]}" />
											<c:set var="total_net_footer_debit"	value="${total_net_footer_debit+supplierOPList[1]}" />
											</c:set> 
											<c:set var="supplierListcredit_netamount">
												<fmt:formatNumber type="number" minFractionDigits="2"
													maxFractionDigits="2"
													value="${supplierOPList[2]}" />
													<c:set var="total_net_footer_credit" value="${total_net_footer_credit+supplierOPList[2]}" />
											</c:set> 
													
								</c:when>
							</c:choose>				            
					</c:forEach>
					<td>
					<fmt:parseNumber var = "debit" type = "number" value = "${supplierListdebit_netamount}" />
					<fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${debit}"/>
					</td>
					<td>
					<fmt:parseNumber var = "credit" type = "number" value = "${supplierListcredit_netamount}" />
					<fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${credit}"/>
					</td>
					<fmt:parseNumber var = "debit" type = "number" value = "${supplierListdebit_netamount}" />
					<fmt:parseNumber var = "credit" type = "number" value = "${supplierListcredit_netamount}" />
					<td><fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${credit-debit}"/></td>		
						
				</tr>
			</c:forEach>
			<tr><td colspan='5' ><h5 style='text-align:center'>Customer List</h5></td></tr>
				<c:set var="total_customer_data" value="1" />
			<input type="text" id="total-customer-rows" value="${customerList.size()}" style="display:none"/>
				
				<c:forEach var = "customer" items = "${customerList}">
				<tr>
					<td style="text-align: left;">
						<a class='acs-view' href = "#" onclick = "viewCustomer('${customer.customer_id}')"><img src='${viewImg}' style = "width: 20px;"/></a>
					</td>									
					<td style="text-align: left;">${customer.firm_name}</td>
					<td style="text-align: left;">${customer.contact_name}</td>
					<c:set var="customerListdebit_netamount" value="0" />			
					<c:set var="customerListcredit_netamount" value="0" />	
						
					<c:forEach var="customerOPList" items="${customerListOP}">	
								
							<c:choose>
								<c:when test="${customerOPList[0]==customer.customer_id}">								
											<c:set var="customerListdebit_netamount">
												<fmt:formatNumber type="number" minFractionDigits="2"
													maxFractionDigits="2"
													value="${customerOPList[1]}" />
													<c:set var="total_net_footer_debit"	value="${total_net_footer_debit+customerOPList[1]}" />
											</c:set> 
											<c:set var="customerListcredit_netamount">
												<fmt:formatNumber type="number" minFractionDigits="2"
													maxFractionDigits="2"
													value="${customerOPList[2]}" />
								<c:set var="total_net_footer_credit" value="${total_net_footer_credit+customerOPList[2]}" />
									</c:set> 
													
								</c:when>
							</c:choose>				            
					</c:forEach>
						<td>
					<fmt:parseNumber var = "debit" type = "number" value = "${customerListdebit_netamount}" />
					<fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${debit}"/>
					</td>
					<td>
					<fmt:parseNumber var = "credit" type = "number" value = "${customerListcredit_netamount}" />
					<fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${credit}"/>
					</td>
					<fmt:parseNumber var = "debit" type = "number" value = "${customerListdebit_netamount}" />
					<fmt:parseNumber var = "credit" type = "number" value = "${customerListcredit_netamount}" />
					<td><fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${debit-credit}"/></td>		
				</tr>
			</c:forEach>
				<tfoot>
				<tr>
					<td colspan='3'></td>
					<td id="footer-debit-total" style="text-align:right;">
							<c:set var="total_net_footer_debit">
								<fmt:formatNumber type="number" minFractionDigits="2"
									maxFractionDigits="2" value="${total_net_footer_debit}" />
							</c:set>
					${total_net_footer_debit}</td>
					<td id="footer-credit-total" style="text-align:right;">
							<c:set var="total_net_footer_credit">
								<fmt:formatNumber type="number" minFractionDigits="2"
									maxFractionDigits="2" value="${total_net_footer_credit}" />
							</c:set>
					${total_net_footer_credit}</td>
					<td></td>
				</tr>
					<tr>
						<td colspan='6' style="text-align:center">
						
						<c:if test="${startNewYrFlag == true}">
                        <button  type="button" onclick = "requestForEditingLastYearAccount()">
										Request For Editing Last Year Account
					   </button>
					    <button  type="button" onclick = "startNewYearAccounting()">
										Start New Year Accounting
					   </button>
                        </c:if>
                        
					    <c:choose>
							<c:when test="${opening_flag==0}">
						
								<button  type="button" onclick = "addopeningbalance(2,${clientflag})">
										Add Balance
							   </button>
							</c:when>
							<c:otherwise>
								<h5>Opening balance can not edit after your trial balance updated. In case you have to update Opening balance again, please contact to VDAKPO </h5>
							</c:otherwise>
						</c:choose>
							
						</td>
					</tr>
				</tfoot>
			</tbody>
			</table>
			</div>
	</div>
		
		</c:otherwise>
	</c:choose>
	
</div>
<script type="text/javascript">	
$('table').on('beforetablefilter',function (event) {
	
	  // before
	//alert("before filter")
	});
	
	 
	
	$('table').on('aftertablefilter',function (event) {
	//alert("hi");
	  // after
	
	});
	function searchTable() {
		  var input, filter, table, tr, td, i;

		  var input = document.getElementById("search");
		  var filter = input.value.toUpperCase();
		  var table = document.getElementById("table");
		  var tr = table.getElementsByTagName("tr");

		  for (var i = 1; i < tr.length; i++) {
		    var tds = tr[i].getElementsByTagName("td");
		   if(tds.length>2){
		    var firstCol = tds[0].textContent.toUpperCase();
		    var secondCol = tds[1].textContent.toUpperCase();
		    console.log(secondCol);
		    console.log(firstCol);
		    if (firstCol.indexOf(filter) > -1  ) {
		      tr[i].style.display = "";
		      //console.log("first");
		    } else if(secondCol.indexOf(filter) > -1){
		    	 tr[i].style.display = "";
		    	//console.log("first");
		    }else {
		      tr[i].style.display = "none";
		    }
		   }
		  }
		}
function isDigit(id){

	var temp = document.getElementById(id);
	var amount=temp.value;   
	var rgx1 = "^(([0-9]*)|(([0-9]*)\.([0-9]*)))$";
	if(amount.match(rgx1)){	
		
	}else{
		//temp.value='';
		alert("Enter Numbers Only");
		temp.focus();
		event.preventDefault();
		
	}
	
	
}
/* function tabEvent(id) {
	
	var temp =  document.getElementById(id);
	var amount=temp.value;   
	var rgx1 = "^[-+]?[0-9]+\.[0-9]+$"; 
	var rgx2 = "^[-+]?[0-9]"; 
	
	if(amount.match(rgx1)||amount.match(rgx2)){	
		
	}else{
		
		temp.value='';
		  if(event.keyCode === 9)
		   {
		   temp.focus();
		   event.preventDefault();
		   }
		
	}
	} */


$(function() {		
    setTimeout(function() {
        $("#successMsg").hide()
    }, 3000);
    
    $( "#date" ).datepicker();

});
	//
	//
	var paramOne="${openingbalance_date}";
	if ((paramOne!="") && (paramOne!="0"))
	{
		//var paramOne =<c:out value="${openingbalance_date}"/>
	
	$("#date").val(paramOne);
	$("#date").attr('disabled',true);
   
	}
	function viewSubLedger(id){
		
		window.location.assign('<c:url value="viewSubLedger"/>?id='+id+'&flag='+1);
	}
	
	function admin(){
		window.location.assign('<c:url value="subledgerList"/>');
	}
	function add(){
		window.location.assign('<c:url value="subledger"/>');
	}
	
	function addexport(){
		window.location.assign('<c:url value="exportOpeningBalances"/>');
	}
	
	function addopeningbalance(type,cflag){
		
	
		var date1=document.getElementById("date").value;
		var year=document.getElementById("year").value;
		console.log("year is ");
	console.log(year);
		var oldDate1 = (date1).split("/");
		console.log(oldDate1[2]);
		var YearRange = $( "#year option:selected" ).text();
		console.log(YearRange[0]);
		var Year1=(YearRange).split("-")
		console.log(Year1[0]);
		var flag=0;
		var client_company_id=document.getElementById("client_company_id").value;
		if(date1=="")
		{
		alert("Select Date");
		}	
	
	else if(year=="0")
	{
		alert("Select Year");
	}
	else if(oldDate1[2]!=Year1[0])
	{
		var msg="The Year of Opening Balance date should be " + Year1[0];
		alert(msg);
		
	}else{flag=1;}
		 if(cflag==0)
		{
		var oldDate = (date1).split("/");
		var date=oldDate[2]+"-"+oldDate[0]+"-"+oldDate[1];
		}
		 else
			 {
			 var oldDate = (date1).split("/");
			 
			 var date=oldDate[2]+"-"+oldDate[0]+"-"+oldDate[1];
			 }
		
	if(flag==1)
		{
		
		/* alert('getSelections: ' + JSON.stringify($("#table").bootstrapTable('getSelections')));
		let data =  $('#table').bootstrapTable ('getOptions').data.length;
		let table =  $('#table').bootstrapTable ('getOptions').data;
		alert(data);      
		  table.find('tr:not(:first)').each(function(i, row) {
			    var cols = [];
			    $(this).find('td').each(function(i, col) {
			      cols.push($(this).text());
			    });
			    data.push(cols);
			  }); */
			//  $("#table").bootstrapTable('getHiddenRows', true)
		console.log("else");
			var credittotal=0;
		var debittotal=0;
				var subList = [];
				var subCreditList = [];
				var subDebitList = [];
				
				var bankList = [];
				var bankCreditList = [];
				var bankDebitList = [];
				var rows=$("#total-bank-rows").val();	
				console.log(rows.length);
				for(i=1;i<=rows;i++)
				{
					var subid=$("#bank-data-"+i).val();
					
					//alert (subid);
				//	alert ($("#bankcredit_balance_"+subid).val());
					if($("#bankcredit_balance_"+subid).val()!="")
						var credit1=$("#bankcredit_balance_"+subid).val();
						else
						var credit1=0;
					//alert($("#bankdebit_balance_"+subid).val());
						if($("#bankdebit_balance_"+subid).val()!="")
						var debit1=$("#bankdebit_balance_"+subid).val();
						else
						var debit1=0;
						var credit = credit1.replace(/,/g, '');
						var debit = debit1.replace(/,/g, '');



					credittotal=parseFloat(credittotal)+parseFloat(credit);
					debittotal=parseFloat(debittotal)+parseFloat(debit);		
					//alert(credittotal);
					bankList.push({subid});
					bankCreditList.push({credit});
					bankDebitList.push({debit});					
				}
				
				var supplierList = [];
				var supplierCreditList = [];
				var supplierDebitList = [];
				var rows=$("#total-supplier-rows").val();	
				
				for(i=1;i<=rows;i++)
				{
					var subid=$("#supplier-data-"+i).val();
					if($("#suppliercredit_balance_"+subid).val()!="")
						var credit1=$("#suppliercredit_balance_"+subid).val();
						else
						var credit1=0;
						if($("#supplierdebit_balance_"+subid).val()!="")
						var debit1=$("#supplierdebit_balance_"+subid).val();
						else
						var debit1=0;
						var credit = credit1.replace(/,/g, '');
						var debit = debit1.replace(/,/g, '');
					credittotal=parseFloat(credittotal)+parseFloat(credit);
					debittotal=parseFloat(debittotal)+parseFloat(debit);					
					supplierList.push({subid});
					supplierCreditList.push({credit});
					supplierDebitList.push({debit});					
				}
				
				var customerList = [];
				var customerCreditList = [];
				var customerDebitList = [];
				var rows=$("#total-customer-rows").val();	
				for(i=1;i<=rows;i++)
				{
					var subid=$("#customer-data-"+i).val();
					if($("#customercredit_balance_"+subid).val()!="")
						var credit1=$("#customercredit_balance_"+subid).val();
						else
						var credit1=0;
						if($("#customerdebit_balance_"+subid).val()!="")
						var debit1=$("#customerdebit_balance_"+subid).val();
						else
						var debit1=0;
						
						var credit = credit1.replace(/,/g, '');
						var debit = debit1.replace(/,/g, '');
					credittotal=parseFloat(credittotal)+parseFloat(credit);
					debittotal=parseFloat(debittotal)+parseFloat(debit);					
					customerList.push({subid});
					customerCreditList.push({credit});
					customerDebitList.push({debit});					
				}
				
				var sledgerList = [];
				var sledgerCreditList = [];
				var sledgerDebitList = [];
				var rows=$("#total-sledger-rows").val();	
				for(i=1;i<=rows;i++)
				{
					
					var subid=$("#sledger-data-"+i).val();

					if($("#sledgercredit_balance_"+subid).val()!="")
						var credit1=$("#sledgercredit_balance_"+subid).val();
						else
						var credit1=0;
						if($("#sledgerdebit_balance_"+subid).val()!="")
						var debit1=$("#sledgerdebit_balance_"+subid).val();
						else
						var debit1=0;		
						
						var credit = credit1.replace(/,/g, '');
						var debit = debit1.replace(/,/g, '');
					credittotal=parseFloat(credittotal)+parseFloat(credit);
					debittotal=parseFloat(debittotal)+parseFloat(debit);					
					sledgerList.push({subid});
					sledgerCreditList.push({credit});
					sledgerDebitList.push({debit});					
				}
				
				/* $("#footer-credit-total").html(credittotal);
				$("#footer-debit-total").html(debittotal); */
				$("#footer-credit-total").html(Number(credittotal).toFixed(2));
				$("#footer-debit-total").html(Number(debittotal).toFixed(2));
				credittotal=Math.ceil(credittotal);
				debittotal=Math.ceil(debittotal);
				if((credittotal==0) && (debittotal==0))
					{
					alert("Enter Credit and Debit Balances!");
					}
				else if(credittotal!=debittotal)
				{
					
					alert("Credit and Debit Balance Mismatch!");
					return false;
				}
				else
				{				
					
						/*$.ajax({
					        type: 'POST',
					        url: 'saveopningbalance',
							data: {"type":2,"id": JSON.stringify(subList),"credit":JSON.stringify(subCreditList),"debit":JSON.stringify(subDebitList)},
							
						      	success: function (data){  
						      		//alert(data);	
						      	},
						        error: function (e) {
						            console.log("ERROR: ", e);
						        },
						        done: function (e) {
						            console.log("DONE");
						        }        
					    });	  */
					    
					    
					    $("#update_btn").prop("disabled", true); 
					    
					$.ajax({
					        type: 'POST',
					        url: 'saveopningbalance',
							data: {"type":3,"id": JSON.stringify(bankList),"credit":JSON.stringify(bankCreditList),"debit":JSON.stringify(bankDebitList),"date":date,"year":year,"client_company_id":client_company_id},
							
						      	success: function (data){  
						      		//alert(data);	
						      	},
						        error: function (e) {
						            console.log("ERROR: ", e);
						        },
						        done: function (e) {
						            console.log("DONE");
						        }        
					    });	 
						$.ajax({
					        type: 'POST',
					        url: 'saveopningbalance',
							data: {"type":4,"id": JSON.stringify(supplierList),"credit":JSON.stringify(supplierCreditList),"debit":JSON.stringify(supplierDebitList),"date":date,"year":year,"client_company_id":client_company_id},
							
						      	success: function (data){  
						      		//alert(data);	
						      	},
						        error: function (e) {
						            console.log("ERROR: ", e);
						        },
						        done: function (e) {
						            console.log("DONE");
						        }        
					    });	 
						$.ajax({
					        type: 'POST',
					        url: 'saveopningbalance',
							data: {"type":5,"id": JSON.stringify(customerList),"credit":JSON.stringify(customerCreditList),"debit":JSON.stringify(customerDebitList),"date":date,"year":year,"client_company_id":client_company_id},
							
						      	success: function (data){  
						      		//alert(data);	
						      	},
						        error: function (e) {
						            console.log("ERROR: ", e);
						        },
						        done: function (e) {
						            console.log("DONE");
						        }        
					    });	  
						$.ajax({
					        type: 'POST',
					        url: 'saveopningbalance',
							data: {"type":1,"id": JSON.stringify(sledgerList),"credit":JSON.stringify(sledgerCreditList),"debit":JSON.stringify(sledgerDebitList),"date":date,"year":year,"client_company_id":client_company_id},
							
						      	success: function (data){ 
						      		alert("Opening balances added successfully. Your request is processing, please refresh the page after five minutes.");
						      		$("#update_btn").prop("disabled", false); 
						      	},
						        error: function (e) {
						            console.log("ERROR: ", e);
						        },
						        done: function (e) {
						            console.log("DONE");
						        }        
					    });
					   // alert(cflag);
					//	alert("Opening balances added successfully. Your request is processing, please refresh the page after five minutes.");
						
						if(cflag==0)
							{
							//location.reload();
				            $("input[type=text]").addClass("textBoxCSS");
							}
			          //  
					/* return true;  */
					/* location.reload();  */
				}
	        }
	}
	$(document).ready(function () {
		$("#bulk_upload").hide();
		$("#bulk_upload_opening_balance").hide();
		document.getElementById('progress-div').style.display="none";
		var oflag=<c:out value= "${opening_flag}"/>;
		
		/* if(oflag==0)
		{
            $("input[type=text]").removeClass("textBoxCSS");
		}
		else
		{
            $("input[type=text]").addClass("textBoxCSS");
		} */
		var menuid=localStorage.getItem("menu_aid");
	   $.ajax({
	        type: 'POST',
	        url: 'getActionAccess?menuid='+menuid,
	        async:false,
            contentType: 'application/json',
		      	success: function (data){  
		      	
		      		if(data["access_Insert"]==true)
		      		{
		      		    $("button").removeClass("acs-insert");
		      		}
		      		if(data["access_Update"]==true)
		      		{
		      		    $("td a").removeClass("acs-update");
		      		}
		      		if(data["access_View"]==true)
		      		{
		      		    $("td a").removeClass("acs-view");
		      		}
		      		if(data["access_Delete"]==true)
		      		{
		      		    $("td a").removeClass("acs-delete");
		      		}
		      		
		      	},
		        error: function (e) {
		            console.log("ERROR: ", e);
		        },
		        done: function (e) {
		            console.log("DONE");
		        }        
	    });	     	
	});
	function uploaddiv(){
		$("#bulk_upload").show();
	}
	function hideuploaddiv(){
		$("#bulk_upload").hide();
	}
	
	function uploaddivforopeningbalances(){
		$("#bulk_upload_opening_balance").show();
	}
	function hideuploaddivforopeningbalances(){
		$("#bulk_upload_opening_balance").hide();
	}
	
	function subledgerimportfailure(){
		window.location.assign('<c:url value="subledgerimportfailure"/>');
	}
	function viewCustomer(id){
		window.location.assign('<c:url value="viewCustomer"/>?id='+id+'&flag='+1);
	}
	function viewSupplier(id){
		window.location.assign('<c:url value="viewSupplier"/>?id='+id+'&flag='+1);
	}
	function viewBank(id){
		window.location.assign('<c:url value="viewBank"/>?id='+id+'&flag='+1);
	}
	
	/* both urls are present in subledger controller with functionality */
	function requestForEditingLastYearAccount(){
		window.location.assign('<c:url value="requestForEditingLastYearAccount"/>');
	}
	
	function startNewYearAccounting(){
		window.location.assign('<c:url value="startNewYearAccounting"/>');
	}
	
	
function validate(){
		console.log("validate ");
		var validFileExtensions = [".xls", ".xlsx"];
		var filePath = document.getElementById("excelFile").value;
		if(filePath == null || filePath == ""){
			alert("Please select excel file");
			return false;
		}
		else{
			var blnValid = false;
		    for (var j = 0; j < validFileExtensions.length; j++) {
		        var sCurExtension = validFileExtensions[j];
		        if (filePath.substr(filePath.length - sCurExtension.length, sCurExtension.length).toLowerCase() == sCurExtension.toLowerCase()) {
		            blnValid = true;
		            break;
		        }
		    }
		    
		    if (!blnValid) {
		        alert("File format should be " + validFileExtensions.join(","));
		        $("#excelFile").val('');
		        return false;
		    }
		    
		    var date1=document.getElementById("date").value;
			var year=document.getElementById("year").value;
			var YearRange = $( "#year option:selected" ).text();
			var Year=(YearRange).split("-")
			var oldDate1 = (date1).split("/");
			
			var client_company_id=document.getElementById("client_company_id").value;
			
				 var oldDate = (date1).split("/");
				 
				var date=oldDate[2]+"-"+oldDate[0]+"-"+oldDate[1];
				// var date=oldDate[1]+"/"+oldDate[0]+"/"+oldDate[2]
				
				 document.getElementById("importDate").value = date;
				 document.getElementById("importRange").value = YearRange;
				 
			if(date1=="")
				{
				alert("Select Date");
				return false;
				}	
			
			else if(year=="0")
			{
				alert("Select Year");
				return false;
			}else if(oldDate1[2]!=Year[0])
			{
				var msg="The Year of Opening Balance date should be " + Year[0];
				alert(msg);
				return false;
			}
		}
		document.getElementById('progress-div').style.display="block";
		$("#file-div").hide();
		return true;
	}
	function editclientopeningbalances()
	{
		window.location.assign('<c:url value="editclientopeningbalances"/>');
	}
	function editcompanyopeningbalances()
	{
		window.location.assign('<c:url value="subledgerOpeningList"/>');
	}
	
function checkAvailability(ele)
	{
		//alert(ele);
	//BANK
		var bankdebit = $("#bankdebit_balance_"+ele).val();
		if(bankdebit !=0){
			$('#bankcredit_balance_'+ele).attr('readonly', true);
		}
		else
			{
			
			$('#bankcredit_balance_'+ele).attr('readonly', false);
			}
			
		var bankcredit = $("#bankcredit_balance_"+ele).val();
		if(bankcredit !=0){
			$("#bankdebit_balance_"+ele).attr('readonly', true);
		}
		else
			{
			$("#bankdebit_balance_"+ele).attr('readonly', false);
			}
		
		
		//Sub-Ledger
		
		var sdebit=$("#sledgerdebit_balance_"+ele).val();
		if(sdebit !=0)
		{
			$("#sledgercredit_balance_"+ele).attr('readonly', true);
		}
	else
	{
		$("#sledgercredit_balance_"+ele).attr('readonly', false);
		}
		
		var scredit=$("#sledgercredit_balance_"+ele).val();
		if(scredit !=0){
			$("#sledgerdebit_balance_"+ele).attr('readonly', true);
		}
		else
			{
			$("#sledgerdebit_balance_"+ele).attr('readonly', false);
			}
			
		
		
		//Supplier List
		
		var supplierdebit=$("#supplierdebit_balance_"+ele).val();
		if(supplierdebit!=0)
			{
			$("#suppliercredit_balance_"+ele).attr('readonly', true);
			}
		else
			{
			$("#suppliercredit_balance_"+ele).attr('readonly', false);
			}
		
		var suppliercredit=$("#suppliercredit_balance_"+ele).val();
		if(suppliercredit!=0)
			{
			$("#supplierdebit_balance_"+ele).attr('readonly', true);
			}
		else
			{
			$("#supplierdebit_balance_"+ele).attr('readonly', false);
			}
		
		
		
		//Customer   customercredit_balance_${customer.customer_id}  

		var custDebit=$("#customerdebit_balance_"+ele).val();
		if(custDebit!=0)
			{
			$("#customercredit_balance_"+ele).attr('readonly', true);
			}
		else
			{
			$("#customercredit_balance_"+ele).attr('readonly', false);
			}
			var custCredit=$("#customercredit_balance_"+ele).val();
			
			if(custCredit!=0)
				{
				$("#customerdebit_balance_"+ele).attr('readonly', true);
				}
			else
				{
				$("#customerdebit_balance_"+ele).attr('readonly', false);
				}
		//alert(debit);
	/* var bdebit=document.getElementById("bankdebit_balance_${bank.bank_id}").value;
		var bcredit=document.getElementById("bankcredit_balance_${bank.bank_id}").value;
		if(bdebit!=0)
			{
			alert("credit value is kept zero");
			}
		if(bcrebit!=0)
		{
		alert(Debit value is kept zero);
		} */
	} 
	
</script>
<style>
.textBoxCSS{border:0px;background-color:transparent}
</style>
<%@include file="/WEB-INF/includes/footer.jsp" %>
