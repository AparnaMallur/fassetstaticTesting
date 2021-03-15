<%@include file="/WEB-INF/includes/header.jsp"%>
<%@page import="com.fasset.controller.abstracts.MyAbstractController"%>
<spring:url value="/resources/images/edit.png" var="editImg" />
<spring:url value="/resources/images/view.png" var="viewImg" />
<script type="text/javascript" src="${valid}"></script>

<spring:url value="/resources/js/jspdf.min.js" var="jspdfmin" />
<spring:url value="/resources/js/jspdf.plugin.autotable.js"
	var="jspdfauto" />
<spring:url value="/resources/js/report_table/horizontalbalance.js"
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
	<h3>Horizontal Balance Sheet Report</h3>
	<a href="homePage">Home</a> » <a href="horizontalBalanceSheetReport">Horizontal Balance Sheet Report</a>
</div>

	<c:if test="${successMsg != null}">
		<div class="successMsg" id="successMsg">
			<strong>${successMsg}</strong>
		</div>
	</c:if>
	<c:set var="TotalDebitAmount" value="0"/>	
	 <c:set var="TotalCreditAmount" value="0"/>
	 <c:set var="TotalDebitAmount1" value="0"/>	
	 <c:set var="TotalCreditAmount1" value="0"/> 
	 <c:set var="debitCountforAdujustingRow" value="0"/>	
	<c:set var="creditCountforAdujustingRow" value="0"/>
	
	 <c:set var="total_credit_Supplier_beforeStartDate" value="0"/>		
				  <c:set var="total_debit_Supplier_beforeStartDate" value="0"/>	
				  
				  <c:forEach var="obalance" items="${supplierOpeningBalanceBeforeStartDate}">	
									<c:set var="total_debit_Supplier_beforeStartDate" value="${total_debit_Supplier_beforeStartDate + obalance.debit_balance}" />
									<c:set var="total_credit_Supplier_beforeStartDate" value="${total_credit_Supplier_beforeStartDate + obalance.credit_balance}" />
			   		</c:forEach>
				  
				  <c:set var="total_credit_Supplier" value="0"/>		
				  <c:set var="total_debit_Supplier" value="0"/>	
				  
				  <c:forEach var="obalance" items="${supplierOpeningBalanceList}">	
						<c:set var="total_debit_Supplier" value="${total_debit_Supplier + obalance.debit_balance}" />
						<c:set var="total_credit_Supplier" value="${total_credit_Supplier + obalance.credit_balance}" />
			  	</c:forEach>
						
			      <c:set var="total_credit_Customer_beforeStartDate" value="0"/>		
				  <c:set var="total_debit_Customer_beforeStartDate" value="0"/>	
				  
				  
				    <c:forEach var="obalance" items="${customerOpeningBalanceBeforeStartDate}">	
									<c:set var="total_debit_Customer_beforeStartDate" value="${total_debit_Customer_beforeStartDate + obalance.debit_balance}" />
									<c:set var="total_credit_Customer_beforeStartDate" value="${total_credit_Customer_beforeStartDate + obalance.credit_balance}" />
				    </c:forEach>
				
				  <c:set var="total_credit_Customer" value="0"/>		
				  <c:set var="total_debit_Customer" value="0"/>	
				  
				    <c:forEach var="obalance" items="${customerOpeningBalanceList}">	
						<c:set var="total_debit_Customer" value="${total_debit_Customer + obalance.debit_balance}" />
						<c:set var="total_credit_Customer" value="${total_credit_Customer + obalance.credit_balance}" />
			  	</c:forEach>
				  
				
				   <c:set var="total_credit_Bank_beforeStartDate" value="0"/>		
				  <c:set var="total_debit_Bank_beforeStartDate" value="0"/>	
				  
				     <c:forEach var="obalance" items="${bankOpeningBalanceBeforeStartDate}">	
									<c:set var="total_debit_Bank_beforeStartDate" value="${total_debit_Bank_beforeStartDate + obalance.debit_balance}" />
									<c:set var="total_credit_Bank_beforeStartDate" value="${total_credit_Bank_beforeStartDate + obalance.credit_balance}" />
				    </c:forEach>
				  
				  <c:set var="total_credit_Bank" value="0"/>		
				  <c:set var="total_debit_Bank" value="0"/>	
				  
				      <c:forEach var="obalance" items="${bankOpeningBalanceList}">	
						<c:set var="total_debit_Bank" value="${total_debit_Bank + obalance.debit_balance}" />
						<c:set var="total_credit_Bank" value="${total_credit_Bank + obalance.credit_balance}" />
			  	</c:forEach>
			  	
			  	<c:forEach var="obalance" items="${subOpeningList}">
	  <c:if test="${obalance.group.postingSide.posting_id == 3}">
	       <c:choose>
                    <c:when test="${obalance.accountGroupName==group1}">
                    
                    <c:forEach var="obalanceBeStartDate" items="${subOpeningListBeforeStartDate}">	
				                    <c:if test="${obalanceBeStartDate.accountGroupName==obalance.accountGroupName}">
				                   <c:set var="openingBalanceforGroup" value="${(obalanceBeStartDate.totaldebit_balance+total_debit_Customer_beforeStartDate+total_debit_Bank_beforeStartDate)-(obalanceBeStartDate.totalcredit_balance+total_credit_Customer_beforeStartDate+total_credit_Bank_beforeStartDate)}" />
			                        </c:if>
				    </c:forEach>
                    		<c:set var="total_debitGroup" value="${(obalance.totaldebit_balance+total_debit_Customer+total_debit_Bank)}" />
                    			<c:set var="total_creditGroup" value="${(obalance.totalcredit_balance+total_credit_Customer+total_credit_Bank)}"/>
        
					                 <c:set var="TotalDebitAmount" value="${TotalDebitAmount+((openingBalanceforGroup)+(total_debitGroup-total_creditGroup))}" />
                    </c:when>
                    <c:otherwise> 
									
									  <c:forEach var="obalanceBeStartDate" items="${subOpeningListBeforeStartDate}">	
				                    <c:if test="${obalanceBeStartDate.accountGroupName==obalance.accountGroupName}">
				                   <c:set var="TotalDebitAmount" value="${TotalDebitAmount+((obalanceBeStartDate.totaldebit_balance-obalanceBeStartDate.totalcredit_balance)+(obalance.totaldebit_balance-obalance.totalcredit_balance))}" />
			                        </c:if>
				                  </c:forEach>
									
								
				    </c:otherwise>
                    </c:choose>
                    
     	  </c:if>
	   <c:if test="${obalance.group.postingSide.posting_id == 4}">
	    <c:choose>
                    <c:when test="${obalance.accountGroupName==group4}">
                    
                     <c:forEach var="obalanceBeStartDate" items="${subOpeningListBeforeStartDate}">	
				                    <c:if test="${obalanceBeStartDate.accountGroupName==obalance.accountGroupName}">
				                  <c:set var="openingBalanceforGroup" value="${(obalanceBeStartDate.totalcredit_balance+total_credit_Supplier_beforeStartDate)-(obalanceBeStartDate.totaldebit_balance+total_debit_Supplier_beforeStartDate)}" />
			                        </c:if>
				           </c:forEach>
				    
                    		<c:set var="total_debitGroup" value="${(obalance.totaldebit_balance+total_debit_Supplier)}" />
                    			<c:set var="total_creditGroup" value="${(obalance.totalcredit_balance+total_credit_Supplier)}"/>
                    
					                 <c:set var="TotalCreditAmount" value="${TotalCreditAmount+((openingBalanceforGroup)+(total_creditGroup-total_debitGroup))}" />
                    </c:when>
                    <c:otherwise> 
                     <c:forEach var="obalanceBeStartDate" items="${subOpeningListBeforeStartDate}">	
				                    <c:if test="${obalanceBeStartDate.accountGroupName==obalance.accountGroupName}">
				                   <c:set var="TotalCreditAmount" value="${TotalCreditAmount+((obalanceBeStartDate.totalcredit_balance-obalanceBeStartDate.totaldebit_balance)+(obalance.totalcredit_balance-obalance.totaldebit_balance))}" />
			                        </c:if>
				      </c:forEach>
									
				    </c:otherwise>
                    </c:choose>
	
	  </c:if>
	 </c:forEach>
  	    
  	    <c:forEach var="obalance" items="${subOpeningList}">
	  <c:if test="${obalance.group.postingSide.posting_id == 1}">
	  	<c:forEach var="obalanceBeStartDate" items="${subOpeningListBeforeStartDate}">	
				                    <c:if test="${obalanceBeStartDate.accountGroupName==obalance.accountGroupName}">
									 <c:set var="TotalDebitAmount1" value="${TotalDebitAmount1+((obalanceBeStartDate.totaldebit_balance-obalanceBeStartDate.totalcredit_balance)+(obalance.totaldebit_balance-obalance.totalcredit_balance))}" />
			                        </c:if>
	    </c:forEach>
				                   
	  </c:if>
	   <c:if test="${obalance.group.postingSide.posting_id == 2}">
	   
	   <c:forEach var="obalanceBeStartDate" items="${subOpeningListBeforeStartDate}">	
				                    <c:if test="${obalanceBeStartDate.accountGroupName==obalance.accountGroupName}">
			              			 <c:set var="TotalCreditAmount1" value="${TotalCreditAmount1+((obalanceBeStartDate.totalcredit_balance-obalanceBeStartDate.totaldebit_balance)+(obalance.totalcredit_balance-obalance.totaldebit_balance))}" />
			                        </c:if>
	  </c:forEach>
				                   
	
	  </c:if>
	 </c:forEach>
	 
	   <c:if test="${TotalCreditAmount1!=TotalDebitAmount1}">
	    <c:set var="creditCountforAdujustingRow" value="${creditCountforAdujustingRow + 1}" />	
	   </c:if>
	   
	   <c:if test="${subOpeningList.size()>0}">
				<c:forEach var="obalance" items="${subOpeningList}">
				                       <!--  start of group -->		
				               <c:if test="${obalance.group.postingSide.posting_id == 4}">  
				                 <c:set var="closingBalance" value="0"/> 
				                      <c:choose>
                    <c:when test="${obalance.accountGroupName==group4}">
                    		 <c:forEach var="obalanceBeStartDate" items="${subOpeningListBeforeStartDate}">	
				                    <c:if test="${obalanceBeStartDate.accountGroupName==obalance.accountGroupName}">
				                  <c:set var="openingBalanceforGroup" value="${(obalanceBeStartDate.totalcredit_balance+total_credit_Supplier_beforeStartDate)-(obalanceBeStartDate.totaldebit_balance+total_debit_Supplier_beforeStartDate)}" />
			                        </c:if>
				              </c:forEach>
				           
                    		<c:set var="total_debitGroup" value="${(obalance.totaldebit_balance+total_debit_Supplier)}" />
                    			<c:set var="total_creditGroup" value="${(obalance.totalcredit_balance+total_credit_Supplier)}"/>
        
					                  <c:set var="closingBalance" value="${(closingBalance)+(openingBalanceforGroup)+(total_creditGroup-total_debitGroup)}"/> 
                    </c:when>
                    <c:otherwise> 
                                  
                                  <c:forEach var="obalanceBeStartDate" items="${subOpeningListBeforeStartDate}">	
				                    <c:if test="${obalanceBeStartDate.accountGroupName==obalance.accountGroupName}">
				                 <c:set var="closingBalance" value="${(closingBalance)+(obalanceBeStartDate.totalcredit_balance-obalanceBeStartDate.totaldebit_balance)+(obalance.totalcredit_balance-obalance.totaldebit_balance)}" /> 
			                        </c:if>
				              </c:forEach>
									
									
				    </c:otherwise>
                    </c:choose>			
                                    <c:if test="${closingBalance!=0}">	
                                      <c:set var="creditCountforAdujustingRow" value="${creditCountforAdujustingRow + 1}" />		
									<!--  start of subgroup -->
									 <c:if test="${obalance.accountSubGroupNameList.size()>0}">
									<c:forEach var="accSubGroup" items="${obalance.accountSubGroupNameList}">	
								    <c:set var="closingBalanceOfSubgroup" value="0"/>
									 <c:set var="total_credit_Subgroup_beforeStartDate" value="0"/>		
				                  <c:set var="total_debit_Subgroup_beforeStartDate" value="0"/>	
				                   <c:set var="total_credit_Subgroup" value="0"/>		
				                  <c:set var="total_debit_Subgroup" value="0"/>	
				                  	
				                  	 <c:forEach var="obalanceBeStartDate" items="${subOpeningListBeforeStartDate}">	
								   
								   <c:if test="${obalanceBeStartDate.accountSubGroupNameList.size()>0}">
								   
								   <c:forEach var="subGroupName" items="${obalanceBeStartDate.accountSubGroupNameList}">
								   
								   <c:if test="${subGroupName==accSubGroup}">
								    <c:if test="${obalanceBeStartDate.ledgerformlist.size()>0}">
									<c:forEach var="ledgerform1" items="${obalanceBeStartDate.ledgerformlist}">	
				                    <c:if test="${ledgerform1.subgroupName==accSubGroup}">
			                      <c:set var="total_credit_Subgroup_beforeStartDate" value="${total_credit_Subgroup_beforeStartDate + ledgerform1.ledgercredit_balance}" />
			                      <c:set var="total_debit_Subgroup_beforeStartDate" value="${total_debit_Subgroup_beforeStartDate + ledgerform1.ledgerdebit_balance}" />
			                        </c:if>
				                   </c:forEach>
				                    </c:if>
				                     </c:if>
				                 
				                    </c:forEach>
				                   
				                     </c:if>
				                   
				                    </c:forEach>
				                    	
				                    	
				                     <c:if test="${obalance.ledgerformlist.size()>0}">
									<c:forEach var="ledgerform2" items="${obalance.ledgerformlist}">	
				                    <c:if test="${ledgerform2.subgroupName==accSubGroup}">
			                      <c:set var="total_credit_Subgroup" value="${total_credit_Subgroup + ledgerform2.ledgercredit_balance}" />
			                      <c:set var="total_debit_Subgroup" value="${total_debit_Subgroup + ledgerform2.ledgerdebit_balance}" />
			                        </c:if>
				                   </c:forEach>
				                     </c:if>
				                     
				                     
				                      <c:choose>
                    <c:when test="${accSubGroup==subGroup5}">
                    
                    	<c:set var="openingBalanceSubgroup" value="${(total_credit_Subgroup_beforeStartDate+total_credit_Supplier_beforeStartDate)-(total_debit_Subgroup_beforeStartDate+total_debit_Supplier_beforeStartDate)}" />
                    		<c:set var="total_debitSubgroup" value="${(total_debit_Subgroup+total_debit_Supplier)}" />
                    			<c:set var="total_creditSubgroup" value="${(total_credit_Subgroup+total_credit_Supplier)}"/>
                    			
                                  <c:set var="closingBalanceOfSubgroup" value="${(closingBalanceOfSubgroup)+(openingBalanceSubgroup)+(total_creditSubgroup-total_debitSubgroup)}"/>
									
                    </c:when>
                    <c:otherwise> 
                                    <c:set var="closingBalanceOfSubgroup" value="${(closingBalanceOfSubgroup)+(total_credit_Subgroup_beforeStartDate-total_debit_Subgroup_beforeStartDate)+(total_credit_Subgroup-total_debit_Subgroup)}"/>
				                        
				    </c:otherwise>
                    </c:choose>
                                      <c:if test="${closingBalanceOfSubgroup!=0}">
				                      <c:set var="creditCountforAdujustingRow" value="${creditCountforAdujustingRow + 1}" />
				                      
				                      <c:if test="${obalance.ledgerformlist.size()>0}">
											<c:forEach var="ledgerform3"
												items="${obalance.ledgerformlist}">
												<c:if test="${ledgerform3!=null}">


													<c:if test="${ledgerform3.subgroupName==accSubGroup}">
														<c:set var="openiBalanceOfLedger" value="0" />
														<c:set var="total_debitOfLedger" value="0" />
														<c:set var="total_creditOfLedger" value="0" />

														<c:forEach var="obalanceBeStartDate"
															items="${subOpeningListBeforeStartDate}">
															<c:if
																test="${obalanceBeStartDate.accountSubGroupNameList.size()>0}">
																<c:forEach var="subGroupName"
																	items="${obalanceBeStartDate.accountSubGroupNameList}">
																	<c:if test="${subGroupName==accSubGroup}">
																		<c:if
																			test="${obalanceBeStartDate.ledgerformlist.size()>0}">
																			<c:forEach var="ledgerform4"
																				items="${obalanceBeStartDate.ledgerformlist}">
																				<c:if
																					test="${ledgerform4.ledgerName==ledgerform3.ledgerName}">
																					<c:if
																						test="${ledgerform4.subgroupName==accSubGroup}">
																						<c:set var="openiBalanceOfLedger"
																							value="${openiBalanceOfLedger + (ledgerform4.ledgercredit_balance-ledgerform4.ledgerdebit_balance)}" />
																					</c:if>
																				</c:if>
																			</c:forEach>
																		</c:if>
																	</c:if>
																</c:forEach>
															</c:if>
														</c:forEach>
														<c:set var="total_debitOfLedger"
															value="${ledgerform3.ledgerdebit_balance}" />
														<c:set var="total_creditOfLedger"
															value="${ledgerform3.ledgercredit_balance}" />

														<c:if
															test="${openiBalanceOfLedger!=0 || total_debitOfLedger!=0 || total_creditOfLedger!=0}">
															 <c:set var="creditCountforAdujustingRow" value="${creditCountforAdujustingRow + 1}" />
														</c:if>
													</c:if>



													<!--  start of subledger -->
													<c:if test="${ledgerform3.subledgerList.size()>0}">
														<c:if test="${ledgerform3.subgroupName==accSubGroup}">
															<c:forEach var="subledger"
																items="${ledgerform3.subledgerList}">
																<c:if test="${subledger!=null}">

																	<c:set var="openiBalanceOfSubLedger" value="0" />
																	<c:set var="total_debitOfSubLedger" value="0" />
																	<c:set var="total_creditOfSubLedger" value="0" />
																	<c:forEach var="obalanceBeStartDate"
																		items="${subOpeningListBeforeStartDate}">
																		<c:if
																			test="${obalanceBeStartDate.accountSubGroupNameList.size()>0}">
																			<c:forEach var="subGroupName"
																				items="${obalanceBeStartDate.accountSubGroupNameList}">
																				<c:if test="${subGroupName==accSubGroup}">
																					<c:if
																						test="${obalanceBeStartDate.ledgerformlist.size()>0}">
																						<c:forEach var="ledgerform5"
																							items="${obalanceBeStartDate.ledgerformlist}">
																							<c:if
																								test="${ledgerform5.subgroupName==accSubGroup}">
																								<c:forEach var="subledger1"
																									items="${ledgerform5.subledgerList}">
																									<c:if test="${subledger1!=null}">
																										<c:if
																											test="${subledger1.subledgerName==subledger.subledgerName}">
																											<c:set var="openiBalanceOfSubLedger"
																												value="${openiBalanceOfSubLedger + (subledger1.credit_balance-subledger1.debit_balance)}" />
																										</c:if>
																									</c:if>
																								</c:forEach>
																							</c:if>
																						</c:forEach>
																					</c:if>
																				</c:if>
																			</c:forEach>
																		</c:if>
																	</c:forEach>

																	<c:set var="total_debitOfSubLedger"
																		value="${subledger.debit_balance}" />
																	<c:set var="total_creditOfSubLedger"
																		value="${subledger.credit_balance}" />

																	<c:if
																		test="${openiBalanceOfSubLedger!=0 || total_debitOfSubLedger!=0 || total_creditOfSubLedger!=0}">
																		 <c:set var="creditCountforAdujustingRow" value="${creditCountforAdujustingRow + 1}" />
																	</c:if>

																</c:if>
															</c:forEach>

														</c:if>
													</c:if>

												</c:if>

											</c:forEach>
										</c:if>
										
										<c:forEach var="obalanceBeStartDate"
																			items="${subOpeningListBeforeStartDate}">

																			<c:if
																				test="${obalanceBeStartDate.accountSubGroupNameList.size()>0}">
																				<c:forEach var="subGroupName"
																					items="${obalanceBeStartDate.accountSubGroupNameList}">
																					<c:if test="${subGroupName==accSubGroup}">
																						<c:if
																							test="${obalanceBeStartDate.ledgerformlist.size()>0}">
																							<c:forEach var="ledgerform6"
																								items="${obalanceBeStartDate.ledgerformlist}">
																								<c:set var="isNotPresent" value="0" />
																								<c:set var="isPresent" value="0" />
																								<c:if
																									test="${ledgerform6.subgroupName==accSubGroup}">
																									<c:if
																										test="${obalance.ledgerformlist.size()>0}">

																										<c:forEach var="ledgerform7"
																											items="${obalance.ledgerformlist}">
																											<c:if
																												test="${ledgerform7.ledgerName==ledgerform6.ledgerName}">
																												<c:set var="isPresent" value="1" />
																											</c:if>
																										</c:forEach>
																										<c:if test="${isPresent==0}">
																											<c:if
																												test="${obalance.ledgerformlist.size()>0}">
																												<c:forEach var="ledgerform7"
																													items="${obalance.ledgerformlist}">
																													<c:if
																														test="${ledgerform7.ledgerName!=ledgerform6.ledgerName}">
																														<c:if test="${isNotPresent==0}">
																															<c:set var="openiBalanceOfLedger"
																																value="0" />
                                                                                                                           <c:set var="creditCountforAdujustingRow" value="${creditCountforAdujustingRow + 1}" />
																															<c:set var="openiBalanceOfLedger"
																																value="${openiBalanceOfLedger + (ledgerform6.ledgercredit_balance-ledgerform6.ledgerdebit_balance)}" />

																															<c:if
																																test="${ledgerform6.subledgerList.size()>0}">


																																<c:forEach var="subledger1"
																																	items="${ledgerform6.subledgerList}">
																																	<c:if test="${subledger1!=null}">
																																		<c:set var="openiBalanceOfSubLedger"
																																			value="0" />
																																		<c:set var="openiBalanceOfSubLedger"
																																			value="${openiBalanceOfSubLedger + (subledger1.credit_balance-subledger1.debit_balance)}" />
																																		<c:if
																																			test="${openiBalanceOfSubLedger!=0}">
																																			 <c:set var="creditCountforAdujustingRow" value="${creditCountforAdujustingRow + 1}" />
																																		</c:if>
																																	</c:if>

																																</c:forEach>

																															</c:if>
																															<c:set var="isNotPresent" value="1" />
																														</c:if>
																													</c:if>

																												</c:forEach>
																											</c:if>

																										</c:if>
																									</c:if>
																									<c:if test="${obalance.ledgerformlist.size()==0}">
																			 
																							<c:set var="creditCountforAdujustingRow" value="${creditCountforAdujustingRow + 1}" />	
																								<c:if
																									test="${ledgerform6.subledgerList.size()>0}">


																									<c:forEach var="subledger1"
																										items="${ledgerform6.subledgerList}">
																										<c:if test="${subledger1!=null}">
																											<c:set var="creditCountforAdujustingRow" value="${creditCountforAdujustingRow + 1}" />
																										</c:if>

																									</c:forEach>

																								</c:if>
																			              </c:if>
																								</c:if>
																							</c:forEach>
																						</c:if>
																					</c:if>

																				</c:forEach>

																			</c:if>

																		</c:forEach>
																		
																		
											<c:if test="${accSubGroup==subGroup5}">
											<c:forEach var="supOpbalance"
												items="${supplierOpeningBalanceList}">
												<c:set var="supOpeningbalance" value="0" />
												<c:set var="supDebitbalance" value="0" />
												<c:set var="supCreditbalance" value="0" />

												<c:forEach var="supOpbalance1"
													items="${supplierOpeningBalanceBeforeStartDate}">
													<c:if
														test="${supOpbalance1.supplierName == supOpbalance.supplierName}">
														<c:set var="supOpeningbalance"
															value="${supOpeningbalance + (supOpbalance1.credit_balance-supOpbalance1.debit_balance)}" />
													</c:if>
												</c:forEach>

												<c:set var="supDebitbalance"
													value="${supOpbalance.debit_balance}" />
												<c:set var="supCreditbalance"
													value="${supOpbalance.credit_balance}" />

												<c:if
													test="${supOpeningbalance!=0 || supDebitbalance!=0 || supCreditbalance!=0}">

													 <c:set var="creditCountforAdujustingRow" value="${creditCountforAdujustingRow + 1}" />
												</c:if>
											</c:forEach>


											<c:forEach var="supOpbalance1"
												items="${supplierOpeningBalanceBeforeStartDate}">
												<c:set var="issupplierPresent" value="0" />
												<c:set var="openiBalanceOfsupplier" value="0" />

												<c:forEach var="supOpbalance"
													items="${supplierOpeningBalanceList}">

													<c:if
														test="${supOpbalance1.supplierName == supOpbalance.supplierName}">
														<c:set var="issupplierPresent" value="1" />

													</c:if>
												</c:forEach>

												<c:if test="${issupplierPresent==0}">

													<c:set var="openiBalanceOfsupplier"
														value="${(openiBalanceOfsupplier)+(supOpbalance1.credit_balance-supOpbalance1.debit_balance)}" />


													<c:if test="${openiBalanceOfsupplier!=0}">
													
												 <c:set var="creditCountforAdujustingRow" value="${creditCountforAdujustingRow + 1}" />
													</c:if>

												</c:if>

											</c:forEach>

										</c:if>
											
				                      </c:if> 
		                     </c:forEach>
				                      </c:if>
				                      
				                      
				                        </c:if>
				                    </c:if>		
				                    
				                    <c:if test="${obalance.group.postingSide.posting_id == 3}"> 
				                 <c:set var="closingBalance" value="0"/> 
				                <c:choose>
                    <c:when test="${obalance.accountGroupName==group1}">
                    
                            <c:forEach var="obalanceBeStartDate" items="${subOpeningListBeforeStartDate}">	
				                    <c:if test="${obalanceBeStartDate.accountGroupName==obalance.accountGroupName}">
				                <c:set var="openingBalanceforGroup" value="${(obalanceBeStartDate.totaldebit_balance+total_debit_Customer_beforeStartDate+total_debit_Bank_beforeStartDate)-(obalanceBeStartDate.totalcredit_balance+total_credit_Customer_beforeStartDate+total_credit_Bank_beforeStartDate)}" />
			                        </c:if>
				              </c:forEach>
				              
                    	
                    		<c:set var="total_debitGroup" value="${(obalance.totaldebit_balance+total_debit_Customer+total_debit_Bank)}" />
                    			<c:set var="total_creditGroup" value="${(obalance.totalcredit_balance+total_credit_Customer+total_credit_Bank)}"/>
                   
									
					                  <c:set var="closingBalance" value="${(closingBalance)+(openingBalanceforGroup)+(total_debitGroup-total_creditGroup)}"/> 
                    </c:when>
                    <c:otherwise> 
									  <c:forEach var="obalanceBeStartDate" items="${subOpeningListBeforeStartDate}">	
				                    <c:if test="${obalanceBeStartDate.accountGroupName==obalance.accountGroupName}">
				               <c:set var="closingBalance" value="${(closingBalance)+(obalanceBeStartDate.totaldebit_balance-obalanceBeStartDate.totalcredit_balance)+(obalance.totaldebit_balance-obalance.totalcredit_balance)}" /> 
			                        </c:if>
				              </c:forEach>
									  
				    </c:otherwise>
                    </c:choose>
                            		<c:if test="${closingBalance!=0}">		
									 <c:set var="debitCountforAdujustingRow" value="${debitCountforAdujustingRow + 1}" />
								
									<!--  start of subgroup -->
									 <c:if test="${obalance.accountSubGroupNameList.size()>0}">
									<c:forEach var="accSubGroup" items="${obalance.accountSubGroupNameList}">
									<c:set var="closingBalanceOfSubgroup" value="0"/>
									<c:set var="total_credit_Subgroup_beforeStartDate" value="0"/>		
				                  <c:set var="total_debit_Subgroup_beforeStartDate" value="0"/>	
				                   <c:set var="total_credit_Subgroup" value="0"/>		
				                  <c:set var="total_debit_Subgroup" value="0"/>	
				                  
				                   <c:forEach var="obalanceBeStartDate" items="${subOpeningListBeforeStartDate}">	
								   
								   <c:if test="${obalanceBeStartDate.accountSubGroupNameList.size()>0}">
								   
								   <c:forEach var="subGroupName" items="${obalanceBeStartDate.accountSubGroupNameList}">
								   
								   <c:if test="${subGroupName==accSubGroup}">
								    <c:if test="${obalanceBeStartDate.ledgerformlist.size()>0}">
									<c:forEach var="ledgerform1" items="${obalanceBeStartDate.ledgerformlist}">	
				                    <c:if test="${ledgerform.subgroupName==accSubGroup}">
			                      <c:set var="total_credit_Subgroup_beforeStartDate" value="${total_credit_Subgroup_beforeStartDate + ledgerform1.ledgercredit_balance}" />
			                      <c:set var="total_debit_Subgroup_beforeStartDate" value="${total_debit_Subgroup_beforeStartDate + ledgerform1.ledgerdebit_balance}" />
			                        </c:if>
				                   </c:forEach>
				                    </c:if>
				                     </c:if>
				                 
				                    </c:forEach>
				                   
				                     </c:if>
				                   
				                    </c:forEach>
				                    
				                  	 <c:if test="${obalance.ledgerformlist.size()>0}">
									<c:forEach var="ledgerform2" items="${obalance.ledgerformlist}">	
				                    <c:if test="${ledgerform2.subgroupName==accSubGroup}">
			                      <c:set var="total_credit_Subgroup" value="${total_credit_Subgroup + ledgerform2.ledgercredit_balance}" />
			                      <c:set var="total_debit_Subgroup" value="${total_debit_Subgroup + ledgerform2.ledgerdebit_balance}" />
			                        </c:if>
				                   </c:forEach>
				                     </c:if>
				                     
				                       <c:choose>
                    <c:when test="${(accSubGroup==subGroup2)||(accSubGroup==subGroup3)}">
                    
                     <c:if test="${accSubGroup==subGroup2}">
                    	<c:set var="openingBalanceSubgroup" value="${(total_debit_Subgroup_beforeStartDate+total_debit_Customer_beforeStartDate)-(total_credit_Subgroup_beforeStartDate+total_credit_Customer_beforeStartDate)}" />
                    		<c:set var="total_debitSubgroup" value="${(total_debit_Subgroup+total_debit_Customer)}" />
                    			<c:set var="total_creditSubgroup" value="${(total_credit_Subgroup+total_credit_Customer)}"/>
                    			<c:set var="closingBalanceOfSubgroup" value="${(closingBalanceOfSubgroup)+(openingBalanceSubgroup)+(total_debitSubgroup-total_creditSubgroup)}"/>
                    				
					  </c:if>
					  
					   <c:if test="${accSubGroup==subGroup3}">
                    	<c:set var="openingBalanceSubgroup" value="${(total_debit_Subgroup_beforeStartDate+total_debit_Bank_beforeStartDate)-(total_credit_Subgroup_beforeStartDate+total_credit_Bank_beforeStartDate)}" />
                    		<c:set var="total_debitSubgroup" value="${(total_debit_Subgroup+total_debit_Bank)}" />
                    			<c:set var="total_creditSubgroup" value="${(total_credit_Subgroup+total_credit_Bank)}"/>
                    			<c:set var="closingBalanceOfSubgroup" value="${(closingBalanceOfSubgroup)+(openingBalanceSubgroup)+(total_debitSubgroup-total_creditSubgroup)}"/>
					  </c:if>
                    </c:when>
                    <c:otherwise> 
                                 <c:set var="closingBalanceOfSubgroup" value="${(closingBalanceOfSubgroup)+(total_debit_Subgroup_beforeStartDate-total_credit_Subgroup_beforeStartDate)+(total_debit_Subgroup-total_credit_Subgroup)}"/>
				    </c:otherwise>
                    </c:choose>
                    	
                    	 <c:if test="${closingBalanceOfSubgroup!=0}">
				                 <c:set var="debitCountforAdujustingRow" value="${debitCountforAdujustingRow + 1}" />
				                 
				                 <c:if test="${obalance.ledgerformlist.size()>0}">
											<c:forEach var="ledgerform3"
												items="${obalance.ledgerformlist}">
												<c:if test="${ledgerform3!=null}">


													<c:if test="${ledgerform3.subgroupName==accSubGroup}">
														<c:set var="openiBalanceOfLedger" value="0" />
														<c:set var="total_debitOfLedger" value="0" />
														<c:set var="total_creditOfLedger" value="0" />

														<c:forEach var="obalanceBeStartDate"
															items="${subOpeningListBeforeStartDate}">
															<c:if
																test="${obalanceBeStartDate.accountSubGroupNameList.size()>0}">
																<c:forEach var="subGroupName"
																	items="${obalanceBeStartDate.accountSubGroupNameList}">
																	<c:if test="${subGroupName==accSubGroup}">
																		<c:if
																			test="${obalanceBeStartDate.ledgerformlist.size()>0}">
																			<c:forEach var="ledgerform4"
																				items="${obalanceBeStartDate.ledgerformlist}">
																				<c:if
																					test="${ledgerform4.ledgerName==ledgerform3.ledgerName}">
																					<c:if
																						test="${ledgerform4.subgroupName==accSubGroup}">
																						<c:set var="openiBalanceOfLedger"
																							value="${openiBalanceOfLedger + (ledgerform4.ledgerdebit_balance-ledgerform4.ledgercredit_balance)}" />
																					</c:if>
																				</c:if>
																			</c:forEach>
																		</c:if>
																	</c:if>
																</c:forEach>
															</c:if>
														</c:forEach>
														<c:set var="total_debitOfLedger"
															value="${ledgerform3.ledgerdebit_balance}" />
														<c:set var="total_creditOfLedger"
															value="${ledgerform3.ledgercredit_balance}" />

														<c:if
															test="${openiBalanceOfLedger!=0 || total_debitOfLedger!=0 || total_creditOfLedger!=0}">
															 <c:set var="debitCountforAdujustingRow" value="${debitCountforAdujustingRow + 1}" />
														</c:if>
													</c:if>



													<!--  start of subledger -->
													<c:if test="${ledgerform3.subledgerList.size()>0}">
														<c:if test="${ledgerform3.subgroupName==accSubGroup}">
															<c:forEach var="subledger"
																items="${ledgerform3.subledgerList}">
																<c:if test="${subledger!=null}">

																	<c:set var="openiBalanceOfSubLedger" value="0" />
																	<c:set var="total_debitOfSubLedger" value="0" />
																	<c:set var="total_creditOfSubLedger" value="0" />
																	<c:forEach var="obalanceBeStartDate"
																		items="${subOpeningListBeforeStartDate}">
																		<c:if
																			test="${obalanceBeStartDate.accountSubGroupNameList.size()>0}">
																			<c:forEach var="subGroupName"
																				items="${obalanceBeStartDate.accountSubGroupNameList}">
																				<c:if test="${subGroupName==accSubGroup}">
																					<c:if
																						test="${obalanceBeStartDate.ledgerformlist.size()>0}">
																						<c:forEach var="ledgerform5"
																							items="${obalanceBeStartDate.ledgerformlist}">
																							<c:if
																								test="${ledgerform5.subgroupName==accSubGroup}">
																								<c:forEach var="subledger1"
																									items="${ledgerform5.subledgerList}">
																									<c:if test="${subledger1!=null}">
																										<c:if
																											test="${subledger1.subledgerName==subledger.subledgerName}">
																											<c:set var="openiBalanceOfSubLedger"
																												value="${openiBalanceOfSubLedger + (subledger1.debit_balance-subledger1.credit_balance)}" />
																										</c:if>
																									</c:if>
																								</c:forEach>
																							</c:if>
																						</c:forEach>
																					</c:if>
																				</c:if>
																			</c:forEach>
																		</c:if>
																	</c:forEach>

																	<c:set var="total_debitOfSubLedger"
																		value="${subledger.debit_balance}" />
																	<c:set var="total_creditOfSubLedger"
																		value="${subledger.credit_balance}" />

																	<c:if
																		test="${openiBalanceOfSubLedger!=0 || total_debitOfSubLedger!=0 || total_creditOfSubLedger!=0}">
																	 <c:set var="debitCountforAdujustingRow" value="${debitCountforAdujustingRow + 1}" />
																	</c:if>

																</c:if>
															</c:forEach>

														</c:if>
													</c:if>

												</c:if>

											</c:forEach>
										</c:if>

																		<c:forEach var="obalanceBeStartDate"
																			items="${subOpeningListBeforeStartDate}">

																			<c:if
																				test="${obalanceBeStartDate.accountSubGroupNameList.size()>0}">
																				<c:forEach var="subGroupName"
																					items="${obalanceBeStartDate.accountSubGroupNameList}">
																					<c:if test="${subGroupName==accSubGroup}">
																						<c:if
																							test="${obalanceBeStartDate.ledgerformlist.size()>0}">
																							<c:forEach var="ledgerform6"
																								items="${obalanceBeStartDate.ledgerformlist}">
																								<c:set var="isNotPresent" value="0" />
																								<c:set var="isPresent" value="0" />
																								<c:if
																									test="${ledgerform6.subgroupName==accSubGroup}">
																									<c:if
																										test="${obalance.ledgerformlist.size()>0}">

																										<c:forEach var="ledgerform7"
																											items="${obalance.ledgerformlist}">
																											<c:if
																												test="${ledgerform7.ledgerName==ledgerform6.ledgerName}">
																												<c:set var="isPresent" value="1" />
																											</c:if>
																										</c:forEach>
																										<c:if test="${isPresent==0}">
																											<c:if
																												test="${obalance.ledgerformlist.size()>0}">
																												<c:forEach var="ledgerform7"
																													items="${obalance.ledgerformlist}">
																													<c:if
																														test="${ledgerform7.ledgerName!=ledgerform6.ledgerName}">
																														<c:if test="${isNotPresent==0}">
																															<c:set var="openiBalanceOfLedger"
																																value="0" />
                                                                                                                               <c:set var="debitCountforAdujustingRow" value="${debitCountforAdujustingRow + 1}" />
																															<c:set var="openiBalanceOfLedger"
																																value="${openiBalanceOfLedger + (ledgerform6.ledgerdebit_balance-ledgerform6.ledgercredit_balance)}" />

																															

																															<c:if
																																test="${ledgerform6.subledgerList.size()>0}">


																																<c:forEach var="subledger1"
																																	items="${ledgerform6.subledgerList}">
																																	<c:if test="${subledger1!=null}">
																																		<c:set var="openiBalanceOfSubLedger"
																																			value="0" />
																																		<c:set var="openiBalanceOfSubLedger"
																																			value="${openiBalanceOfSubLedger + (subledger1.debit_balance-subledger1.credit_balance)}" />
																																		<c:if
																																			test="${openiBalanceOfSubLedger!=0}">
																																			 <c:set var="debitCountforAdujustingRow" value="${debitCountforAdujustingRow + 1}" />
																																		</c:if>
																																	</c:if>

																																</c:forEach>

																															</c:if>
																															<c:set var="isNotPresent" value="1" />
																														</c:if>
																													</c:if>

																												</c:forEach>
																											</c:if>

																										</c:if>
																									</c:if>
																									<c:if test="${obalance.ledgerformlist.size()==0}">
																			 
																							<c:set var="debitCountforAdujustingRow" value="${debitCountforAdujustingRow + 1}" />	
																								<c:if
																									test="${ledgerform6.subledgerList.size()>0}">


																									<c:forEach var="subledger1"
																										items="${ledgerform6.subledgerList}">
																										<c:if test="${subledger1!=null}">
																											<c:set var="debitCountforAdujustingRow" value="${debitCountforAdujustingRow + 1}" />
																										</c:if>

																									</c:forEach>

																								</c:if>
																			              </c:if>
																								</c:if>
																							</c:forEach>
																						</c:if>
																					</c:if>

																				</c:forEach>

																			</c:if>

																		</c:forEach>
				                
				                
				                <c:if test="${accSubGroup==subGroup3}">

											<c:forEach var="bankOpbalance"
												items="${bankOpeningBalanceList}">
												<c:set var="bankOpeningbalance" value="0" />
												<c:set var="bankDebitbalance" value="0" />
												<c:set var="bankCreditbalance" value="0" />

												<c:forEach var="bankOpbalance1"
													items="${bankOpeningBalanceBeforeStartDate}">
													<c:if
														test="${bankOpbalance1.bankName == bankOpbalance.bankName}">
														<c:set var="bankOpeningbalance"
															value="${(bankOpeningbalance)+(bankOpbalance1.debit_balance-bankOpbalance1.credit_balance)}" />
													</c:if>
												</c:forEach>

												<c:set var="bankDebitbalance"
													value="${bankOpbalance.debit_balance}" />
												<c:set var="bankCreditbalance"
													value="${bankOpbalance.credit_balance}" />

												<c:if
													test="${bankOpeningbalance!=0 || bankDebitbalance!=0 || bankCreditbalance!=0}">
													
													 <c:set var="debitCountforAdujustingRow" value="${debitCountforAdujustingRow + 1}" />
													
												</c:if>
											</c:forEach>

											<c:forEach var="bankOpbalance1"
												items="${bankOpeningBalanceBeforeStartDate}">
												<c:set var="isBankPresent" value="0" />
												<c:set var="openiBalanceOfbank" value="0" />
												<c:forEach var="bankOpbalance"
													items="${bankOpeningBalanceList}">

													<c:if
														test="${bankOpbalance1.bankName == bankOpbalance.bankName}">
														<c:set var="isBankPresent" value="1" />

													</c:if>
												</c:forEach>

												<c:if test="${isBankPresent==0}">

													<c:set var="openiBalanceOfbank"
														value="${(openiBalanceOfbank)+(bankOpbalance1.debit_balance-bankOpbalance1.credit_balance)}" />


													<c:if test="${openiBalanceOfbank!=0}">
														
													 <c:set var="debitCountforAdujustingRow" value="${debitCountforAdujustingRow + 1}" />
													</c:if>

												</c:if>

											</c:forEach>



										</c:if>

										<c:if test="${accSubGroup==subGroup2}">
											<c:forEach var="cusoPbalance"
												items="${customerOpeningBalanceList}">
												<c:set var="cusOpeningbalance" value="0" />
												<c:set var="cusDebitbalance" value="0" />
												<c:set var="cusCreditbalance" value="0" />

												<c:forEach var="cusoPbalance1"
													items="${customerOpeningBalanceBeforeStartDate}">
													<c:if
														test="${cusoPbalance1.customerName == cusoPbalance.customerName}">
														<c:set var="cusOpeningbalance"
															value="${(cusOpeningbalance)+(cusoPbalance1.debit_balance-cusoPbalance1.credit_balance)}" />
													</c:if>
												</c:forEach>
												<c:set var="cusDebitbalance"
													value="${cusoPbalance.debit_balance}" />
												<c:set var="cusCreditbalance"
													value="${cusoPbalance.credit_balance}" />

												<c:if
													test="${cusOpeningbalance!=0 || cusDebitbalance!=0 || cusCreditbalance!=0}">
													
													 <c:set var="debitCountforAdujustingRow" value="${debitCountforAdujustingRow + 1}" />
												</c:if>

											</c:forEach>


											<c:forEach var="cusoPbalance1"
												items="${customerOpeningBalanceBeforeStartDate}">
												<c:set var="iscustomerPresent" value="0" />
												<c:set var="openiBalanceOfcustomer" value="0" />

												<c:forEach var="cusoPbalance"
													items="${customerOpeningBalanceList}">

													<c:if
														test="${cusoPbalance1.customerName == cusoPbalance.customerName}">
														<c:set var="iscustomerPresent" value="1" />

													</c:if>
												</c:forEach>

												<c:if test="${iscustomerPresent==0}">

													<c:set var="openiBalanceOfcustomer"
														value="${(openiBalanceOfcustomer)+(cusoPbalance1.debit_balance-cusoPbalance1.credit_balance)}" />


													<c:if test="${openiBalanceOfcustomer!=0}">
														 <c:set var="debitCountforAdujustingRow" value="${debitCountforAdujustingRow + 1}" />
													</c:if>

												</c:if>

											</c:forEach>
										</c:if>
			            </c:if>
				                     </c:forEach>
				                      </c:if>
				                       </c:if>
				                      
				                    </c:if>		                        
					   </c:forEach>
					   
	 </c:if>
							 
		  <!-- Excel Start -->
	<div style="display:none" id="excel_report">
					<!-- Date -->
					<table>
						<tr><td colspan='5'>Company Name: ${company.company_name}</td></tr>
						<tr><td colspan='5'>Address: ${company.permenant_address}</td></tr>
						<tr><td colspan='5'>
								<fmt:parseDate value="${from_date}" pattern="yyyy-MM-dd" var="parsedDate" type="date" />
	                   			 <fmt:formatDate value="${parsedDate}" var="from_date" type="date" pattern="dd-MM-yyyy" />
	                   			 <fmt:parseDate value="${to_date}" pattern="yyyy-MM-dd" var="parsedDate" type="date" />
	                   			 <fmt:formatDate value="${parsedDate}" var="to_date" type="date" pattern="dd-MM-yyyy" />
						From ${from_date} To ${to_date}</td></tr>
						<tr><td colspan='5'>
						CIN:
						<c:if test="${company.company_statutory_type.company_statutory_id==10 || company.company_statutory_type.company_statutory_id==14}">
						 ${company.registration_no}
					    </c:if>	
						</td></tr>
					</table>
		<table>
	<tr>
	<td>
		<table>
			<thead>
				<tr>
					<th style='text-align: center'>Liabilities</th>
				</tr>
			</thead>
			<tbody>			
				<tr>
					<td>
						<!-- Start of Liabilities  -->
						<table>
							<thead>
								<tr>
									<th data-field="particulars-in" data-filter-control="input"
										data-sortable="true">Particulars</th>
									<th data-field="amount-in" data-filter-control="input"
											data-sortable="true"></th>
									<th data-field="amount" data-filter-control="input"
											data-sortable="true"></th>
								</tr>
							</thead>
							<tbody>
								
			
				
				  <c:if test="${subOpeningList.size()>0}">
				
				<c:forEach var="obalance" items="${subOpeningList}">
				                       <!--  start of group -->		
				                       
				               <c:if test="${obalance.group.postingSide.posting_id == 4}">  
				                 <c:set var="closingBalance" value="0"/> 
				                      <c:choose>
                    <c:when test="${obalance.accountGroupName==group4}">
                    		 <c:forEach var="obalanceBeStartDate" items="${subOpeningListBeforeStartDate}">	
				                    <c:if test="${obalanceBeStartDate.accountGroupName==obalance.accountGroupName}">
				                  <c:set var="openingBalanceforGroup" value="${(obalanceBeStartDate.totalcredit_balance+total_credit_Supplier_beforeStartDate)-(obalanceBeStartDate.totaldebit_balance+total_debit_Supplier_beforeStartDate)}" />
			                        </c:if>
				              </c:forEach>
				           
                    		<c:set var="total_debitGroup" value="${(obalance.totaldebit_balance+total_debit_Supplier)}" />
                    			<c:set var="total_creditGroup" value="${(obalance.totalcredit_balance+total_credit_Supplier)}"/>
        
					                  <c:set var="closingBalance" value="${(closingBalance)+(openingBalanceforGroup)+(total_creditGroup-total_debitGroup)}"/> 
                    </c:when>
                    <c:otherwise> 
                                  
                                  <c:forEach var="obalanceBeStartDate" items="${subOpeningListBeforeStartDate}">	
				                    <c:if test="${obalanceBeStartDate.accountGroupName==obalance.accountGroupName}">
				                 <c:set var="closingBalance" value="${(closingBalance)+(obalanceBeStartDate.totalcredit_balance-obalanceBeStartDate.totaldebit_balance)+(obalance.totalcredit_balance-obalance.totaldebit_balance)}" /> 
			                        </c:if>
				              </c:forEach>
									
									
				    </c:otherwise>
                    </c:choose>			
                                    <c:if test="${closingBalance!=0}">	
                        	
									<tr class='n-style'>
									<td style="text-align: left;"><h5>${obalance.accountGroupName}</h5></td>
									<td></td>
									<td class="tright"><fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${closingBalance}"/>
									</td>
									</tr>
									
									
									
									<!--  start of subgroup -->
									 <c:if test="${obalance.accountSubGroupNameList.size()>0}">
									<c:forEach var="accSubGroup" items="${obalance.accountSubGroupNameList}">	
								    <c:set var="closingBalanceOfSubgroup" value="0"/>
									 <c:set var="total_credit_Subgroup_beforeStartDate" value="0"/>		
				                  <c:set var="total_debit_Subgroup_beforeStartDate" value="0"/>	
				                   <c:set var="total_credit_Subgroup" value="0"/>		
				                  <c:set var="total_debit_Subgroup" value="0"/>	
				                  	
				                  	 <c:forEach var="obalanceBeStartDate" items="${subOpeningListBeforeStartDate}">	
								   
								   <c:if test="${obalanceBeStartDate.accountSubGroupNameList.size()>0}">
								   
								   <c:forEach var="subGroupName" items="${obalanceBeStartDate.accountSubGroupNameList}">
								   
								   <c:if test="${subGroupName==accSubGroup}">
								    <c:if test="${obalanceBeStartDate.ledgerformlist.size()>0}">
									<c:forEach var="ledgerform1" items="${obalanceBeStartDate.ledgerformlist}">	
				                    <c:if test="${ledgerform1.subgroupName==accSubGroup}">
			                      <c:set var="total_credit_Subgroup_beforeStartDate" value="${total_credit_Subgroup_beforeStartDate + ledgerform1.ledgercredit_balance}" />
			                      <c:set var="total_debit_Subgroup_beforeStartDate" value="${total_debit_Subgroup_beforeStartDate + ledgerform1.ledgerdebit_balance}" />
			                        </c:if>
				                   </c:forEach>
				                    </c:if>
				                     </c:if>
				                 
				                    </c:forEach>
				                   
				                     </c:if>
				                   
				                    </c:forEach>
				                    	
				                     <c:if test="${obalance.ledgerformlist.size()>0}">
									<c:forEach var="ledgerform2" items="${obalance.ledgerformlist}">	
				                    <c:if test="${ledgerform2.subgroupName==accSubGroup}">
			                      <c:set var="total_credit_Subgroup" value="${total_credit_Subgroup + ledgerform2.ledgercredit_balance}" />
			                      <c:set var="total_debit_Subgroup" value="${total_debit_Subgroup + ledgerform2.ledgerdebit_balance}" />
			                        </c:if>
				                   </c:forEach>
				                     </c:if>
				                     
				                     
				                      <c:choose>
                    <c:when test="${accSubGroup==subGroup5}">
                    
                    	<c:set var="openingBalanceSubgroup" value="${(total_credit_Subgroup_beforeStartDate+total_credit_Supplier_beforeStartDate)-(total_debit_Subgroup_beforeStartDate+total_debit_Supplier_beforeStartDate)}" />
                    		<c:set var="total_debitSubgroup" value="${(total_debit_Subgroup+total_debit_Supplier)}" />
                    			<c:set var="total_creditSubgroup" value="${(total_credit_Subgroup+total_credit_Supplier)}"/>
                    			
                                  <c:set var="closingBalanceOfSubgroup" value="${(closingBalanceOfSubgroup)+(openingBalanceSubgroup)+(total_creditSubgroup-total_debitSubgroup)}"/>
									
                    </c:when>
                    <c:otherwise> 
                                    <c:set var="closingBalanceOfSubgroup" value="${(closingBalanceOfSubgroup)+(total_credit_Subgroup_beforeStartDate-total_debit_Subgroup_beforeStartDate)+(total_credit_Subgroup-total_debit_Subgroup)}"/>
				                        
				    </c:otherwise>
                    </c:choose>
                                      <c:if test="${closingBalanceOfSubgroup!=0}">
									<tr class='n-style'>
									<td style="text-align: left;">${emptyString}${accSubGroup}</td>
                                   <td class="tright"><fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${closingBalanceOfSubgroup}"/></td>
				                   <td></td>
				                    </tr>
				                    
				                    
				                     <c:if test="${obalance.ledgerformlist.size()>0}">
											<c:forEach var="ledgerform3"
												items="${obalance.ledgerformlist}">
												<c:if test="${ledgerform3!=null}">


													<c:if test="${ledgerform3.subgroupName==accSubGroup}">
														<c:set var="openiBalanceOfLedger" value="0" />
														<c:set var="total_debitOfLedger" value="0" />
														<c:set var="total_creditOfLedger" value="0" />

														<c:forEach var="obalanceBeStartDate"
															items="${subOpeningListBeforeStartDate}">
															<c:if
																test="${obalanceBeStartDate.accountSubGroupNameList.size()>0}">
																<c:forEach var="subGroupName"
																	items="${obalanceBeStartDate.accountSubGroupNameList}">
																	<c:if test="${subGroupName==accSubGroup}">
																		<c:if
																			test="${obalanceBeStartDate.ledgerformlist.size()>0}">
																			<c:forEach var="ledgerform4"
																				items="${obalanceBeStartDate.ledgerformlist}">
																				<c:if
																					test="${ledgerform4.ledgerName==ledgerform3.ledgerName}">
																					<c:if
																						test="${ledgerform4.subgroupName==accSubGroup}">
																						<c:set var="openiBalanceOfLedger"
																							value="${openiBalanceOfLedger + (ledgerform4.ledgercredit_balance-ledgerform4.ledgerdebit_balance)}" />
																					</c:if>
																				</c:if>
																			</c:forEach>
																		</c:if>
																	</c:if>
																</c:forEach>
															</c:if>
														</c:forEach>
														<c:set var="total_debitOfLedger"
															value="${ledgerform3.ledgerdebit_balance}" />
														<c:set var="total_creditOfLedger"
															value="${ledgerform3.ledgercredit_balance}" />

														<c:if
															test="${openiBalanceOfLedger!=0 || total_debitOfLedger!=0 || total_creditOfLedger!=0}">
															<tr class='n-style'>
									<td style="text-align: left;">${emptyString}${emptyString}${ledgerform3.ledgerName}</td>
				                   <td class="tright"><fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${openiBalanceOfLedger+total_creditOfLedger-total_debitOfLedger}"/></td>
				                   <td></td>
				                    </tr>
														</c:if>
													</c:if>



													<!--  start of subledger -->
													<c:if test="${ledgerform3.subledgerList.size()>0}">
														<c:if test="${ledgerform3.subgroupName==accSubGroup}">
															<c:forEach var="subledger"
																items="${ledgerform3.subledgerList}">
																<c:if test="${subledger!=null}">

																	<c:set var="openiBalanceOfSubLedger" value="0" />
																	<c:set var="total_debitOfSubLedger" value="0" />
																	<c:set var="total_creditOfSubLedger" value="0" />
																	<c:forEach var="obalanceBeStartDate"
																		items="${subOpeningListBeforeStartDate}">
																		<c:if
																			test="${obalanceBeStartDate.accountSubGroupNameList.size()>0}">
																			<c:forEach var="subGroupName"
																				items="${obalanceBeStartDate.accountSubGroupNameList}">
																				<c:if test="${subGroupName==accSubGroup}">
																					<c:if
																						test="${obalanceBeStartDate.ledgerformlist.size()>0}">
																						<c:forEach var="ledgerform5"
																							items="${obalanceBeStartDate.ledgerformlist}">
																							<c:if
																								test="${ledgerform5.subgroupName==accSubGroup}">
																								<c:forEach var="subledger1"
																									items="${ledgerform5.subledgerList}">
																									<c:if test="${subledger1!=null}">
																										<c:if
																											test="${subledger1.subledgerName==subledger.subledgerName}">
																											<c:set var="openiBalanceOfSubLedger"
																												value="${openiBalanceOfSubLedger + (subledger1.credit_balance-subledger1.debit_balance)}" />
																										</c:if>
																									</c:if>
																								</c:forEach>
																							</c:if>
																						</c:forEach>
																					</c:if>
																				</c:if>
																			</c:forEach>
																		</c:if>
																	</c:forEach>

																	<c:set var="total_debitOfSubLedger"
																		value="${subledger.debit_balance}" />
																	<c:set var="total_creditOfSubLedger"
																		value="${subledger.credit_balance}" />

																	<c:if
																		test="${openiBalanceOfSubLedger!=0 || total_debitOfSubLedger!=0 || total_creditOfSubLedger!=0}">
																		<tr class='n-style'>
									<td style="text-align: left;">${emptyString}${emptyString}${emptyString}${subledger.subledgerName}</td>
				                   <td class="tright"><fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${openiBalanceOfSubLedger+total_creditOfSubLedger-total_debitOfSubLedger}"/></td>
				                   <td></td>
				                    </tr>
																	</c:if>

																</c:if>
															</c:forEach>

														</c:if>
													</c:if>

												</c:if>

											</c:forEach>
										</c:if>
										
										<c:forEach var="obalanceBeStartDate"
																			items="${subOpeningListBeforeStartDate}">

																			<c:if
																				test="${obalanceBeStartDate.accountSubGroupNameList.size()>0}">
																				<c:forEach var="subGroupName"
																					items="${obalanceBeStartDate.accountSubGroupNameList}">
																					<c:if test="${subGroupName==accSubGroup}">
																						<c:if
																							test="${obalanceBeStartDate.ledgerformlist.size()>0}">
																							<c:forEach var="ledgerform6"
																								items="${obalanceBeStartDate.ledgerformlist}">
																								<c:set var="isNotPresent" value="0" />
																								<c:set var="isPresent" value="0" />
																								<c:if
																									test="${ledgerform6.subgroupName==accSubGroup}">
																									<c:if
																										test="${obalance.ledgerformlist.size()>0}">

																										<c:forEach var="ledgerform7"
																											items="${obalance.ledgerformlist}">
																											<c:if
																												test="${ledgerform7.ledgerName==ledgerform6.ledgerName}">
																												<c:set var="isPresent" value="1" />
																											</c:if>
																										</c:forEach>
																										<c:if test="${isPresent==0}">
																											<c:if
																												test="${obalance.ledgerformlist.size()>0}">
																												<c:forEach var="ledgerform7"
																													items="${obalance.ledgerformlist}">
																													<c:if
																														test="${ledgerform7.ledgerName!=ledgerform6.ledgerName}">
																														<c:if test="${isNotPresent==0}">
																															<c:set var="openiBalanceOfLedger"
																																value="0" />

																															<c:set var="openiBalanceOfLedger"
																																value="${openiBalanceOfLedger + (ledgerform6.ledgercredit_balance-ledgerform6.ledgerdebit_balance)}" />

																															<tr class='n-style'>
																																<td style="text-align: left;">${emptyString}${emptyString}${ledgerform6.ledgerName}</td>
																																<td class="tright"><fmt:formatNumber
																																		type="number" minFractionDigits="2"
																																		maxFractionDigits="2"
																																		value="${openiBalanceOfLedger}" /></td>
																																<td></td>
																															</tr>

																															<c:if
																																test="${ledgerform6.subledgerList.size()>0}">


																																<c:forEach var="subledger1"
																																	items="${ledgerform6.subledgerList}">
																																	<c:if test="${subledger1!=null}">
																																		<c:set var="openiBalanceOfSubLedger"
																																			value="0" />
																																		<c:set var="openiBalanceOfSubLedger"
																																			value="${openiBalanceOfSubLedger + (subledger1.credit_balance-subledger1.debit_balance)}" />
																																		<c:if
																																			test="${openiBalanceOfSubLedger!=0}">
																																			<tr class='n-style'>
																																				<td style="text-align: left;">${emptyString}${emptyString}${emptyString}${subledger1.subledgerName}</td>
																																				<td class="tright"><fmt:formatNumber
																																						type="number"
																																						minFractionDigits="2"
																																						maxFractionDigits="2"
																																						value="${openiBalanceOfSubLedger}" /></td>
																																				<td></td>
																																			</tr>
																																		</c:if>
																																	</c:if>

																																</c:forEach>

																															</c:if>
																															<c:set var="isNotPresent" value="1" />
																														</c:if>
																													</c:if>

																												</c:forEach>
																											</c:if>

																										</c:if>
																									</c:if>
																									<c:if test="${obalance.ledgerformlist.size()==0}">
																			 
																						
																							<c:set var="openiBalanceOfLedger" value="0" />
																							
																							
																							<tr class='n-style'>
																								<td style="text-align: left;">${emptyString}${emptyString}${ledgerform6.ledgerName}</td>
																								<c:set var="openiBalanceOfLedger"
																									value="${openiBalanceOfLedger + (ledgerform6.ledgercredit_balance-ledgerform6.ledgerdebit_balance)}" />
																								<td class="tright"><fmt:formatNumber
																										type="number" minFractionDigits="2"
																										maxFractionDigits="2"
																										value="${openiBalanceOfLedger}" /></td>
																								<td></td>
																							</tr>
																								<c:if
																									test="${ledgerform6.subledgerList.size()>0}">


																									<c:forEach var="subledger1"
																										items="${ledgerform6.subledgerList}">
																										<c:if test="${subledger1!=null}">
																											<c:set var="openiBalanceOfSubLedger"
																												value="0" />
																											<c:set var="openiBalanceOfSubLedger"
																												value="${openiBalanceOfSubLedger + (subledger1.credit_balance-subledger1.debit_balance)}" />

																											<c:if test="${openiBalanceOfSubLedger!=0}">
																												<tr class='n-style'>
																														<td style="text-align: left;">${emptyString}${emptyString}${emptyString}${subledger1.subledgerName}</td>
																														<td class="tright"><fmt:formatNumber
																																type="number"
																																minFractionDigits="2"
																																maxFractionDigits="2"
																																value="${openiBalanceOfSubLedger}" /></td>
																														<td></td>
																													</tr>
																												
																											</c:if>
																										</c:if>

																									</c:forEach>

																								</c:if>
																						
																			</c:if>
																								</c:if>
																							</c:forEach>
																						</c:if>
																					</c:if>

																				</c:forEach>

																			</c:if>

																		</c:forEach>
																		
																		
											<c:if test="${accSubGroup==subGroup5}">
											<c:forEach var="supOpbalance"
												items="${supplierOpeningBalanceList}">
												<c:set var="supOpeningbalance" value="0" />
												<c:set var="supDebitbalance" value="0" />
												<c:set var="supCreditbalance" value="0" />

												<c:forEach var="supOpbalance1"
													items="${supplierOpeningBalanceBeforeStartDate}">
													<c:if
														test="${supOpbalance1.supplierName == supOpbalance.supplierName}">
														<c:set var="supOpeningbalance"
															value="${supOpeningbalance + (supOpbalance1.credit_balance-supOpbalance1.debit_balance)}" />
													</c:if>
												</c:forEach>

												<c:set var="supDebitbalance"
													value="${supOpbalance.debit_balance}" />
												<c:set var="supCreditbalance"
													value="${supOpbalance.credit_balance}" />

												<c:if
													test="${supOpeningbalance!=0 || supDebitbalance!=0 || supCreditbalance!=0}">

														<tr class='n-style'>
															<td style="text-align: left;">${emptyString}${emptyString}${supOpbalance.supplierName}</td>
															<td class="tright"><fmt:formatNumber
																	type="number" minFractionDigits="2"
																	maxFractionDigits="2"
																	value="${(supOpeningbalance)+(supCreditbalance-supDebitbalance)}" /></td>
															<td></td>
														</tr>
												</c:if>
											</c:forEach>


											<c:forEach var="supOpbalance1"
												items="${supplierOpeningBalanceBeforeStartDate}">
												<c:set var="issupplierPresent" value="0" />
												<c:set var="openiBalanceOfsupplier" value="0" />

												<c:forEach var="supOpbalance"
													items="${supplierOpeningBalanceList}">

													<c:if
														test="${supOpbalance1.supplierName == supOpbalance.supplierName}">
														<c:set var="issupplierPresent" value="1" />

													</c:if>
												</c:forEach>

												<c:if test="${issupplierPresent==0}">

													<c:set var="openiBalanceOfsupplier"
														value="${(openiBalanceOfsupplier)+(supOpbalance1.credit_balance-supOpbalance1.debit_balance)}" />


													<c:if test="${openiBalanceOfsupplier!=0}">
													
													<tr class='n-style'>
															<td style="text-align: left;">${emptyString}${emptyString}${supOpbalance1.supplierName}</td>
															<td class="tright"><fmt:formatNumber
																	type="number" minFractionDigits="2"
																	maxFractionDigits="2"
																	value="${openiBalanceOfsupplier}" /></td>
															<td></td>
														</tr>
													</c:if>

												</c:if>

											</c:forEach>

										</c:if>
				                     	
				                      </c:if> 
		                     </c:forEach>
				                      </c:if>
				                      
				                      
				                        </c:if>
				                    </c:if>			                        
					   </c:forEach>
					   
						
				
				 </c:if>
				
				        
				
			</tbody>
			                 <c:set var="totalNetProfit" value="0"/>
							  <c:if test="${TotalCreditAmount1>TotalDebitAmount1}">
							    <tr>
									<td><b>Profit & Loss A/c</b></td>
									<td></td>
									<td class="tright"><b><fmt:formatNumber type="number" minFractionDigits="2"
									maxFractionDigits="2" value="${TotalCreditAmount1-TotalDebitAmount1}" /></b>
									 <c:set var="totalNetProfit" value="${totalNetProfit+(TotalCreditAmount1-TotalDebitAmount1)}" />
									</td>
								</tr>
								  
							 </c:if>
							  <c:if test="${TotalDebitAmount1>TotalCreditAmount1}">
							    <tr>
									<td><b>Profit & Loss A/c</b></td>
									<td></td>
									<td class="tright"><b><fmt:formatNumber type="number" minFractionDigits="2"
									maxFractionDigits="2" value="${TotalCreditAmount1-TotalDebitAmount1}" /></b>
									 <c:set var="totalNetProfit" value="${totalNetProfit+(TotalCreditAmount1-TotalDebitAmount1)}" />
									</td>
								</tr>
								 	
							 </c:if>
			                    <c:if test="${debitCountforAdujustingRow>creditCountforAdujustingRow}">
								 
								 <c:if test="${(debitCountforAdujustingRow-creditCountforAdujustingRow!=0)}">
								 
								 <c:forEach begin="1" end="${debitCountforAdujustingRow-creditCountforAdujustingRow}" var="i">
                                 <tr>
                                 <td></td>
                                 <td></td>
                                 <td></td>
                                 </tr>
                                 </c:forEach>
                                 
								 </c:if>
								 
								 </c:if>
							
								<tr>
									<td><b>Total</b></td>
									<td></td>
									<td class="tright"><b><fmt:formatNumber type="number" minFractionDigits="2"
									maxFractionDigits="2" value="${TotalCreditAmount+totalNetProfit}" /></b></td>
								</tr>
							
						</table> <!-- End of Liabilities  -->
					</td>
				</tr>
			</tbody>
		</table>
	</td>
	
	<td>
		<table>
			<thead>
				<tr>
					<th style='text-align: center'>Assets</th>
				</tr>
			</thead>
			<tbody>			
				<tr>
					<td>
						<!-- Start of Assets  -->
						<table>
							<thead>
								<tr>
									<th data-field="particulars-in" data-filter-control="input"
										data-sortable="true">Particulars</th>
									<th data-field="amount-in" data-filter-control="input"
											data-sortable="true"></th>
									<th data-field="amount" data-filter-control="input"
											data-sortable="true"></th>
								</tr>
							</thead>
							<tbody>
								
		 <c:if test="${subOpeningList.size()>0}">
				
				<c:forEach var="obalance" items="${subOpeningList}">
				                       <!--  start of group -->		
				                       
				               <c:if test="${obalance.group.postingSide.posting_id == 3}"> 
				                 <c:set var="closingBalance" value="0"/> 
				                <c:choose>
                    <c:when test="${obalance.accountGroupName==group1}">
                    
                            <c:forEach var="obalanceBeStartDate" items="${subOpeningListBeforeStartDate}">	
				                    <c:if test="${obalanceBeStartDate.accountGroupName==obalance.accountGroupName}">
				                <c:set var="openingBalanceforGroup" value="${(obalanceBeStartDate.totaldebit_balance+total_debit_Customer_beforeStartDate+total_debit_Bank_beforeStartDate)-(obalanceBeStartDate.totalcredit_balance+total_credit_Customer_beforeStartDate+total_credit_Bank_beforeStartDate)}" />
			                        </c:if>
				              </c:forEach>
				              
                    	
                    		<c:set var="total_debitGroup" value="${(obalance.totaldebit_balance+total_debit_Customer+total_debit_Bank)}" />
                    			<c:set var="total_creditGroup" value="${(obalance.totalcredit_balance+total_credit_Customer+total_credit_Bank)}"/>
                   
									
					                  <c:set var="closingBalance" value="${(closingBalance)+(openingBalanceforGroup)+(total_debitGroup-total_creditGroup)}"/> 
                    </c:when>
                    <c:otherwise> 
									  <c:forEach var="obalanceBeStartDate" items="${subOpeningListBeforeStartDate}">	
				                    <c:if test="${obalanceBeStartDate.accountGroupName==obalance.accountGroupName}">
				               <c:set var="closingBalance" value="${(closingBalance)+(obalanceBeStartDate.totaldebit_balance-obalanceBeStartDate.totalcredit_balance)+(obalance.totaldebit_balance-obalance.totalcredit_balance)}" /> 
			                        </c:if>
				              </c:forEach>
									  
				    </c:otherwise>
                    </c:choose>
                            		<c:if test="${closingBalance!=0}">		
									<tr class='n-style'>
									<td style="text-align: left;"><h5>${obalance.accountGroupName}</h5></td>
									<td></td>
									<td class="tright"><fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${closingBalance}"/>
									</td>
								    </tr>
								   
									
									
									
									<!--  start of subgroup -->
									 <c:if test="${obalance.accountSubGroupNameList.size()>0}">
									<c:forEach var="accSubGroup" items="${obalance.accountSubGroupNameList}">
									<c:set var="closingBalanceOfSubgroup" value="0"/>
									<c:set var="total_credit_Subgroup_beforeStartDate" value="0"/>		
				                  <c:set var="total_debit_Subgroup_beforeStartDate" value="0"/>	
				                   <c:set var="total_credit_Subgroup" value="0"/>		
				                  <c:set var="total_debit_Subgroup" value="0"/>	
				                  
				                   <c:forEach var="obalanceBeStartDate" items="${subOpeningListBeforeStartDate}">	
								   
								   <c:if test="${obalanceBeStartDate.accountSubGroupNameList.size()>0}">
								   
								   <c:forEach var="subGroupName" items="${obalanceBeStartDate.accountSubGroupNameList}">
								   
								   <c:if test="${subGroupName==accSubGroup}">
								    <c:if test="${obalanceBeStartDate.ledgerformlist.size()>0}">
									<c:forEach var="ledgerform1" items="${obalanceBeStartDate.ledgerformlist}">	
				                    <c:if test="${ledgerform.subgroupName==accSubGroup}">
			                      <c:set var="total_credit_Subgroup_beforeStartDate" value="${total_credit_Subgroup_beforeStartDate + ledgerform1.ledgercredit_balance}" />
			                      <c:set var="total_debit_Subgroup_beforeStartDate" value="${total_debit_Subgroup_beforeStartDate + ledgerform1.ledgerdebit_balance}" />
			                        </c:if>
				                   </c:forEach>
				                    </c:if>
				                     </c:if>
				                 
				                    </c:forEach>
				                   
				                     </c:if>
				                   
				                    </c:forEach>
				                    
				                  	 <c:if test="${obalance.ledgerformlist.size()>0}">
									<c:forEach var="ledgerform2" items="${obalance.ledgerformlist}">	
				                    <c:if test="${ledgerform2.subgroupName==accSubGroup}">
			                      <c:set var="total_credit_Subgroup" value="${total_credit_Subgroup + ledgerform2.ledgercredit_balance}" />
			                      <c:set var="total_debit_Subgroup" value="${total_debit_Subgroup + ledgerform2.ledgerdebit_balance}" />
			                        </c:if>
				                   </c:forEach>
				                     </c:if>
				                     
				                       <c:choose>
                    <c:when test="${(accSubGroup==subGroup2)||(accSubGroup==subGroup3)}">
                    
                     <c:if test="${accSubGroup==subGroup2}">
                    	<c:set var="openingBalanceSubgroup" value="${(total_debit_Subgroup_beforeStartDate+total_debit_Customer_beforeStartDate)-(total_credit_Subgroup_beforeStartDate+total_credit_Customer_beforeStartDate)}" />
                    		<c:set var="total_debitSubgroup" value="${(total_debit_Subgroup+total_debit_Customer)}" />
                    			<c:set var="total_creditSubgroup" value="${(total_credit_Subgroup+total_credit_Customer)}"/>
                    			<c:set var="closingBalanceOfSubgroup" value="${(closingBalanceOfSubgroup)+(openingBalanceSubgroup)+(total_debitSubgroup-total_creditSubgroup)}"/>
                    				
					  </c:if>
					  
					   <c:if test="${accSubGroup==subGroup3}">
                    	<c:set var="openingBalanceSubgroup" value="${(total_debit_Subgroup_beforeStartDate+total_debit_Bank_beforeStartDate)-(total_credit_Subgroup_beforeStartDate+total_credit_Bank_beforeStartDate)}" />
                    		<c:set var="total_debitSubgroup" value="${(total_debit_Subgroup+total_debit_Bank)}" />
                    			<c:set var="total_creditSubgroup" value="${(total_credit_Subgroup+total_credit_Bank)}"/>
                    			<c:set var="closingBalanceOfSubgroup" value="${(closingBalanceOfSubgroup)+(openingBalanceSubgroup)+(total_debitSubgroup-total_creditSubgroup)}"/>
					  </c:if>
                    </c:when>
                    <c:otherwise> 
                                 <c:set var="closingBalanceOfSubgroup" value="${(closingBalanceOfSubgroup)+(total_debit_Subgroup_beforeStartDate-total_credit_Subgroup_beforeStartDate)+(total_debit_Subgroup-total_credit_Subgroup)}"/>
				    </c:otherwise>
                    </c:choose>
                    	
                    	 <c:if test="${closingBalanceOfSubgroup!=0}">
									<tr class='n-style'>
									<td style="text-align: left;">${emptyString}${accSubGroup}</td>
				                   <td class="tright"><fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${closingBalanceOfSubgroup}"/></td>     
		                            <td></td>
				                    </tr>
				                    
				                    <c:if test="${obalance.ledgerformlist.size()>0}">
											<c:forEach var="ledgerform3"
												items="${obalance.ledgerformlist}">
												<c:if test="${ledgerform3!=null}">


													<c:if test="${ledgerform3.subgroupName==accSubGroup}">
														<c:set var="openiBalanceOfLedger" value="0" />
														<c:set var="total_debitOfLedger" value="0" />
														<c:set var="total_creditOfLedger" value="0" />

														<c:forEach var="obalanceBeStartDate"
															items="${subOpeningListBeforeStartDate}">
															<c:if
																test="${obalanceBeStartDate.accountSubGroupNameList.size()>0}">
																<c:forEach var="subGroupName"
																	items="${obalanceBeStartDate.accountSubGroupNameList}">
																	<c:if test="${subGroupName==accSubGroup}">
																		<c:if
																			test="${obalanceBeStartDate.ledgerformlist.size()>0}">
																			<c:forEach var="ledgerform4"
																				items="${obalanceBeStartDate.ledgerformlist}">
																				<c:if
																					test="${ledgerform4.ledgerName==ledgerform3.ledgerName}">
																					<c:if
																						test="${ledgerform4.subgroupName==accSubGroup}">
																						<c:set var="openiBalanceOfLedger"
																							value="${openiBalanceOfLedger + (ledgerform4.ledgerdebit_balance-ledgerform4.ledgercredit_balance)}" />
																					</c:if>
																				</c:if>
																			</c:forEach>
																		</c:if>
																	</c:if>
																</c:forEach>
															</c:if>
														</c:forEach>
														<c:set var="total_debitOfLedger"
															value="${ledgerform3.ledgerdebit_balance}" />
														<c:set var="total_creditOfLedger"
															value="${ledgerform3.ledgercredit_balance}" />

														<c:if
															test="${openiBalanceOfLedger!=0 || total_debitOfLedger!=0 || total_creditOfLedger!=0}">
															<tr class='n-style'>
									<td style="text-align: left;">${emptyString}${emptyString}${ledgerform3.ledgerName}</td>
				                   <td class="tright"><fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${openiBalanceOfLedger+total_debitOfLedger-total_creditOfLedger}"/></td>
				                   <td></td>
				                    </tr>
														</c:if>
													</c:if>



													<!--  start of subledger -->
													<c:if test="${ledgerform3.subledgerList.size()>0}">
														<c:if test="${ledgerform3.subgroupName==accSubGroup}">
															<c:forEach var="subledger"
																items="${ledgerform3.subledgerList}">
																<c:if test="${subledger!=null}">

																	<c:set var="openiBalanceOfSubLedger" value="0" />
																	<c:set var="total_debitOfSubLedger" value="0" />
																	<c:set var="total_creditOfSubLedger" value="0" />
																	<c:forEach var="obalanceBeStartDate"
																		items="${subOpeningListBeforeStartDate}">
																		<c:if
																			test="${obalanceBeStartDate.accountSubGroupNameList.size()>0}">
																			<c:forEach var="subGroupName"
																				items="${obalanceBeStartDate.accountSubGroupNameList}">
																				<c:if test="${subGroupName==accSubGroup}">
																					<c:if
																						test="${obalanceBeStartDate.ledgerformlist.size()>0}">
																						<c:forEach var="ledgerform5"
																							items="${obalanceBeStartDate.ledgerformlist}">
																							<c:if
																								test="${ledgerform5.subgroupName==accSubGroup}">
																								<c:forEach var="subledger1"
																									items="${ledgerform5.subledgerList}">
																									<c:if test="${subledger1!=null}">
																										<c:if
																											test="${subledger1.subledgerName==subledger.subledgerName}">
																											<c:set var="openiBalanceOfSubLedger"
																												value="${openiBalanceOfSubLedger + (subledger1.debit_balance-subledger1.credit_balance)}" />
																										</c:if>
																									</c:if>
																								</c:forEach>
																							</c:if>
																						</c:forEach>
																					</c:if>
																				</c:if>
																			</c:forEach>
																		</c:if>
																	</c:forEach>

																	<c:set var="total_debitOfSubLedger"
																		value="${subledger.debit_balance}" />
																	<c:set var="total_creditOfSubLedger"
																		value="${subledger.credit_balance}" />

																	<c:if
																		test="${openiBalanceOfSubLedger!=0 || total_debitOfSubLedger!=0 || total_creditOfSubLedger!=0}">
																		<tr class='n-style'>
									<td style="text-align: left;">${emptyString}${emptyString}${emptyString}${subledger.subledgerName}</td>
				                   <td class="tright"><fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${openiBalanceOfSubLedger+total_debitOfSubLedger-total_creditOfSubLedger}"/></td>
				                   <td></td>
				                    </tr>
																	</c:if>

																</c:if>
															</c:forEach>

														</c:if>
													</c:if>

												</c:if>

											</c:forEach>
										</c:if>

																		<c:forEach var="obalanceBeStartDate"
																			items="${subOpeningListBeforeStartDate}">

																			<c:if
																				test="${obalanceBeStartDate.accountSubGroupNameList.size()>0}">
																				<c:forEach var="subGroupName"
																					items="${obalanceBeStartDate.accountSubGroupNameList}">
																					<c:if test="${subGroupName==accSubGroup}">
																						<c:if
																							test="${obalanceBeStartDate.ledgerformlist.size()>0}">
																							<c:forEach var="ledgerform6"
																								items="${obalanceBeStartDate.ledgerformlist}">
																								<c:set var="isNotPresent" value="0" />
																								<c:set var="isPresent" value="0" />
																								<c:if
																									test="${ledgerform6.subgroupName==accSubGroup}">
																									<c:if
																										test="${obalance.ledgerformlist.size()>0}">

																										<c:forEach var="ledgerform7"
																											items="${obalance.ledgerformlist}">
																											<c:if
																												test="${ledgerform7.ledgerName==ledgerform6.ledgerName}">
																												<c:set var="isPresent" value="1" />
																											</c:if>
																										</c:forEach>
																										<c:if test="${isPresent==0}">
																											<c:if
																												test="${obalance.ledgerformlist.size()>0}">
																												<c:forEach var="ledgerform7"
																													items="${obalance.ledgerformlist}">
																													<c:if
																														test="${ledgerform7.ledgerName!=ledgerform6.ledgerName}">
																														<c:if test="${isNotPresent==0}">
																															<c:set var="openiBalanceOfLedger"
																																value="0" />

																															<c:set var="openiBalanceOfLedger"
																																value="${openiBalanceOfLedger + (ledgerform6.ledgerdebit_balance-ledgerform6.ledgercredit_balance)}" />

																															<tr class='n-style'>
																																<td style="text-align: left;">${emptyString}${emptyString}${ledgerform6.ledgerName}</td>
																																<td class="tright"><fmt:formatNumber
																																		type="number" minFractionDigits="2"
																																		maxFractionDigits="2"
																																		value="${openiBalanceOfLedger}" /></td>
																																<td></td>
																															</tr>

																															<c:if
																																test="${ledgerform6.subledgerList.size()>0}">


																																<c:forEach var="subledger1"
																																	items="${ledgerform6.subledgerList}">
																																	<c:if test="${subledger1!=null}">
																																		<c:set var="openiBalanceOfSubLedger"
																																			value="0" />
																																		<c:set var="openiBalanceOfSubLedger"
																																			value="${openiBalanceOfSubLedger + (subledger1.debit_balance-subledger1.credit_balance)}" />
																																		<c:if
																																			test="${openiBalanceOfSubLedger!=0}">
																																			<tr class='n-style'>
																																				<td style="text-align: left;">${emptyString}${emptyString}${emptyString}${subledger1.subledgerName}</td>
																																				<td class="tright"><fmt:formatNumber
																																						type="number"
																																						minFractionDigits="2"
																																						maxFractionDigits="2"
																																						value="${openiBalanceOfSubLedger}" /></td>
																																				<td></td>
																																			</tr>
																																		</c:if>
																																	</c:if>

																																</c:forEach>

																															</c:if>
																															<c:set var="isNotPresent" value="1" />
																														</c:if>
																													</c:if>

																												</c:forEach>
																											</c:if>

																										</c:if>
																									</c:if>
																									<c:if test="${obalance.ledgerformlist.size()==0}">
																			 
																							<c:set var="openiBalanceOfLedger" value="0" />
																							
																							<tr class='n-style'>
																								<td style="text-align: left;">${emptyString}${emptyString}${ledgerform6.ledgerName}</td>
																								<c:set var="openiBalanceOfLedger"
																									value="${openiBalanceOfLedger + (ledgerform6.ledgerdebit_balance-ledgerform6.ledgercredit_balance)}" />
																								<td class="tright"><fmt:formatNumber
																										type="number" minFractionDigits="2"
																										maxFractionDigits="2"
																										value="${openiBalanceOfLedger}" /></td>
																								<td></td>
																							</tr>
																									
																								<c:if
																									test="${ledgerform6.subledgerList.size()>0}">


																									<c:forEach var="subledger1"
																										items="${ledgerform6.subledgerList}">
																										<c:if test="${subledger1!=null}">
																											<c:set var="openiBalanceOfSubLedger"
																												value="0" />
																											<c:set var="openiBalanceOfSubLedger"
																												value="${openiBalanceOfSubLedger + (subledger1.debit_balance-subledger1.credit_balance)}" />

																											<c:if test="${openiBalanceOfSubLedger!=0}">
																												<tr class='n-style'>
																														<td style="text-align: left;">${emptyString}${emptyString}${emptyString}${subledger1.subledgerName}</td>
																														<td class="tright"><fmt:formatNumber
																																type="number"
																																minFractionDigits="2"
																																maxFractionDigits="2"
																																value="${openiBalanceOfSubLedger}" /></td>
																														<td></td>
																													</tr>

																											</c:if>
																										</c:if>

																									</c:forEach>

																								</c:if>
																								
																						
																			</c:if>
																								</c:if>
																							</c:forEach>
																						</c:if>
																					</c:if>

																				</c:forEach>

																			</c:if>

																		</c:forEach>
				                
				                
				                <c:if test="${accSubGroup==subGroup3}">

											<c:forEach var="bankOpbalance"
												items="${bankOpeningBalanceList}">
												<c:set var="bankOpeningbalance" value="0" />
												<c:set var="bankDebitbalance" value="0" />
												<c:set var="bankCreditbalance" value="0" />

												<c:forEach var="bankOpbalance1"
													items="${bankOpeningBalanceBeforeStartDate}">
													<c:if
														test="${bankOpbalance1.bankName == bankOpbalance.bankName}">
														<c:set var="bankOpeningbalance"
															value="${(bankOpeningbalance)+(bankOpbalance1.debit_balance-bankOpbalance1.credit_balance)}" />
													</c:if>
												</c:forEach>

												<c:set var="bankDebitbalance"
													value="${bankOpbalance.debit_balance}" />
												<c:set var="bankCreditbalance"
													value="${bankOpbalance.credit_balance}" />

												<c:if
													test="${bankOpeningbalance!=0 || bankDebitbalance!=0 || bankCreditbalance!=0}">
													
													<tr class='n-style'>
														<td style="text-align: left;">${emptyString}${emptyString}${bankOpbalance.bankName}</td>
														<td class="tright"><fmt:formatNumber
																type="number" minFractionDigits="2"
																maxFractionDigits="2"
																value="${(bankOpeningbalance)+(bankDebitbalance-bankCreditbalance)}" /></td>
														<td></td>
													</tr>
													
												</c:if>
											</c:forEach>

											<c:forEach var="bankOpbalance1"
												items="${bankOpeningBalanceBeforeStartDate}">
												<c:set var="isBankPresent" value="0" />
												<c:set var="openiBalanceOfbank" value="0" />
												<c:forEach var="bankOpbalance"
													items="${bankOpeningBalanceList}">

													<c:if
														test="${bankOpbalance1.bankName == bankOpbalance.bankName}">
														<c:set var="isBankPresent" value="1" />

													</c:if>
												</c:forEach>

												<c:if test="${isBankPresent==0}">

													<c:set var="openiBalanceOfbank"
														value="${(openiBalanceOfbank)+(bankOpbalance1.debit_balance-bankOpbalance1.credit_balance)}" />


													<c:if test="${openiBalanceOfbank!=0}">
														
														<tr class='n-style'>
														<td style="text-align: left;">${emptyString}${emptyString}${bankOpbalance1.bankName}</td>
														<td class="tright"><fmt:formatNumber
																type="number" minFractionDigits="2"
																maxFractionDigits="2"
																value="${openiBalanceOfbank}" /></td>
														<td></td>
													</tr>
													</c:if>

												</c:if>

											</c:forEach>



										</c:if>

										<c:if test="${accSubGroup==subGroup2}">
											<c:forEach var="cusoPbalance"
												items="${customerOpeningBalanceList}">
												<c:set var="cusOpeningbalance" value="0" />
												<c:set var="cusDebitbalance" value="0" />
												<c:set var="cusCreditbalance" value="0" />

												<c:forEach var="cusoPbalance1"
													items="${customerOpeningBalanceBeforeStartDate}">
													<c:if
														test="${cusoPbalance1.customerName == cusoPbalance.customerName}">
														<c:set var="cusOpeningbalance"
															value="${(cusOpeningbalance)+(cusoPbalance1.debit_balance-cusoPbalance1.credit_balance)}" />
													</c:if>
												</c:forEach>
												<c:set var="cusDebitbalance"
													value="${cusoPbalance.debit_balance}" />
												<c:set var="cusCreditbalance"
													value="${cusoPbalance.credit_balance}" />

												<c:if
													test="${cusOpeningbalance!=0 || cusDebitbalance!=0 || cusCreditbalance!=0}">
													
														<tr class='n-style'>
														<td style="text-align: left;">${emptyString}${emptyString}${cusoPbalance.customerName}</td>
														<td class="tright"><fmt:formatNumber
																type="number" minFractionDigits="2"
																maxFractionDigits="2"
																value="${(cusOpeningbalance)+(cusDebitbalance-cusCreditbalance)}" /></td>
														<td></td>
													</tr>
												</c:if>

											</c:forEach>


											<c:forEach var="cusoPbalance1"
												items="${customerOpeningBalanceBeforeStartDate}">
												<c:set var="iscustomerPresent" value="0" />
												<c:set var="openiBalanceOfcustomer" value="0" />

												<c:forEach var="cusoPbalance"
													items="${customerOpeningBalanceList}">

													<c:if
														test="${cusoPbalance1.customerName == cusoPbalance.customerName}">
														<c:set var="iscustomerPresent" value="1" />

													</c:if>
												</c:forEach>

												<c:if test="${iscustomerPresent==0}">

													<c:set var="openiBalanceOfcustomer"
														value="${(openiBalanceOfcustomer)+(cusoPbalance1.debit_balance-cusoPbalance1.credit_balance)}" />


													<c:if test="${openiBalanceOfcustomer!=0}">
														<tr class='n-style'>
														<td style="text-align: left;">${emptyString}${emptyString}${cusoPbalance1.customerName}</td>
														<td class="tright"><fmt:formatNumber
																type="number" minFractionDigits="2"
																maxFractionDigits="2"
																value="${openiBalanceOfcustomer}" /></td>
														<td></td>
													</tr>
													</c:if>

												</c:if>

											</c:forEach>
										</c:if>
				                
			            </c:if>
				                     </c:forEach>
				                      </c:if>
				                       </c:if>
				                      
				                    </c:if>			                        
					   </c:forEach>
				
				 </c:if>
				              
           			</tbody>
							
							<c:if test="${creditCountforAdujustingRow>debitCountforAdujustingRow}">
								 
								 <c:if test="${(creditCountforAdujustingRow-debitCountforAdujustingRow!=0)}">
								 <c:forEach begin="1" end="${creditCountforAdujustingRow-debitCountforAdujustingRow}" var="i">
                                 <tr>
                                 <td></td>
                                 <td></td>
                                 <td></td>
                                 </tr>
                                 </c:forEach>
								 </c:if>
								 
								 </c:if>	
							     <tr>
									<td><b>Total</b></td>
									<td></td>
									<td class="tright"><b><fmt:formatNumber type="number" minFractionDigits="2"
									maxFractionDigits="2" value="${TotalDebitAmount}" /></b></td>
								</tr>
							
						</table> <!-- End of Assets  -->
					</td>
				</tr>
			</tbody>
		</table>
	</td>
	</tr>
	</table>
</div>
		<!-- Excel End -->
	
	<table style="width: 100%">		  
	   	<tr>
	<td style="vertical-align:top">
	<div class="borderForm">
		<table style="width: 100%">
			<thead>
				<tr>
				
					<th style='text-align: center'>Liabilities</th>
				</tr>
			</thead>
			<tbody>		
				<tr>
					<td style='vertical-align:top'>
						<!-- Start of Liabilities  -->
						<table id="table" data-toggle="table" data-search="false"
							data-escape="false" data-filter-control="true"
							data-show-export="false" data-click-to-select="true"
							data-pagination="true" data-page-size="10"
							data-toolbar="#toolbar" class="table">
							<thead>
								<tr>
									<th data-field="particulars-in" data-filter-control="input"
										data-sortable="true">Particulars</th>
									<th data-field="amount-in" data-filter-control="input"
											data-sortable="true"></th>
									<th data-field="amount" data-filter-control="input"
											data-sortable="true"></th>
								</tr>
							</thead>
							<tbody>
								
			
				
				 <c:if test="${subOpeningList.size()>0}">
				
				<c:forEach var="obalance" items="${subOpeningList}">
				                       <!--  start of group -->		
				                       
				               <c:if test="${obalance.group.postingSide.posting_id == 4}">  
				                 <c:set var="closingBalance" value="0"/> 
				                      <c:choose>
                    <c:when test="${obalance.accountGroupName==group4}">
                    		 <c:forEach var="obalanceBeStartDate" items="${subOpeningListBeforeStartDate}">	
				                    <c:if test="${obalanceBeStartDate.accountGroupName==obalance.accountGroupName}">
				                  <c:set var="openingBalanceforGroup" value="${(obalanceBeStartDate.totalcredit_balance+total_credit_Supplier_beforeStartDate)-(obalanceBeStartDate.totaldebit_balance+total_debit_Supplier_beforeStartDate)}" />
			                        </c:if>
				              </c:forEach>
				           
                    		<c:set var="total_debitGroup" value="${(obalance.totaldebit_balance+total_debit_Supplier)}" />
                    			<c:set var="total_creditGroup" value="${(obalance.totalcredit_balance+total_credit_Supplier)}"/>
        
					                  <c:set var="closingBalance" value="${(closingBalance)+(openingBalanceforGroup)+(total_creditGroup-total_debitGroup)}"/> 
                    </c:when>
                    <c:otherwise> 
                                  
                                  <c:forEach var="obalanceBeStartDate" items="${subOpeningListBeforeStartDate}">	
				                    <c:if test="${obalanceBeStartDate.accountGroupName==obalance.accountGroupName}">
				                 <c:set var="closingBalance" value="${(closingBalance)+(obalanceBeStartDate.totalcredit_balance-obalanceBeStartDate.totaldebit_balance)+(obalance.totalcredit_balance-obalance.totaldebit_balance)}" /> 
			                        </c:if>
				              </c:forEach>
									
									
				    </c:otherwise>
                    </c:choose>			
                                    <c:if test="${closingBalance!=0}">		
									<tr class='n-style'>
									<td style="text-align: left;"><h5>${obalance.accountGroupName}</h5></td>
									<td></td>
									<td class="tright"><fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${closingBalance}"/>
									</td>
									</tr>
									
									
									
									<!--  start of subgroup -->
									 <c:if test="${obalance.accountSubGroupNameList.size()>0}">
									<c:forEach var="accSubGroup" items="${obalance.accountSubGroupNameList}">	
								    <c:set var="closingBalanceOfSubgroup" value="0"/>
									 <c:set var="total_credit_Subgroup_beforeStartDate" value="0"/>		
				                  <c:set var="total_debit_Subgroup_beforeStartDate" value="0"/>	
				                   <c:set var="total_credit_Subgroup" value="0"/>		
				                  <c:set var="total_debit_Subgroup" value="0"/>	
				                  	
				                  	 <c:forEach var="obalanceBeStartDate" items="${subOpeningListBeforeStartDate}">	
								   
								   <c:if test="${obalanceBeStartDate.accountSubGroupNameList.size()>0}">
								   
								   <c:forEach var="subGroupName" items="${obalanceBeStartDate.accountSubGroupNameList}">
								   
								   <c:if test="${subGroupName==accSubGroup}">
								    <c:if test="${obalanceBeStartDate.ledgerformlist.size()>0}">
									<c:forEach var="ledgerform1" items="${obalanceBeStartDate.ledgerformlist}">	
				                    <c:if test="${ledgerform1.subgroupName==accSubGroup}">
			                      <c:set var="total_credit_Subgroup_beforeStartDate" value="${total_credit_Subgroup_beforeStartDate + ledgerform1.ledgercredit_balance}" />
			                      <c:set var="total_debit_Subgroup_beforeStartDate" value="${total_debit_Subgroup_beforeStartDate + ledgerform1.ledgerdebit_balance}" />
			                        </c:if>
				                   </c:forEach>
				                    </c:if>
				                     </c:if>
				                 
				                    </c:forEach>
				                   
				                     </c:if>
				                   
				                    </c:forEach>
				                    	
				                     <c:if test="${obalance.ledgerformlist.size()>0}">
									<c:forEach var="ledgerform2" items="${obalance.ledgerformlist}">	
				                    <c:if test="${ledgerform2.subgroupName==accSubGroup}">
			                      <c:set var="total_credit_Subgroup" value="${total_credit_Subgroup + ledgerform2.ledgercredit_balance}" />
			                      <c:set var="total_debit_Subgroup" value="${total_debit_Subgroup + ledgerform2.ledgerdebit_balance}" />
			                        </c:if>
				                   </c:forEach>
				                     </c:if>
				                     
				                     
				           <c:choose>
                    <c:when test="${accSubGroup==subGroup5}">
                    
                    	<c:set var="openingBalanceSubgroup" value="${(total_credit_Subgroup_beforeStartDate+total_credit_Supplier_beforeStartDate)-(total_debit_Subgroup_beforeStartDate+total_debit_Supplier_beforeStartDate)}" />
                    		<c:set var="total_debitSubgroup" value="${(total_debit_Subgroup+total_debit_Supplier)}" />
                    			<c:set var="total_creditSubgroup" value="${(total_credit_Subgroup+total_credit_Supplier)}" />
                    			
                                  <c:set var="closingBalanceOfSubgroup" value="${(closingBalanceOfSubgroup)+(openingBalanceSubgroup)+(total_creditSubgroup-total_debitSubgroup)}" />
									
                    </c:when>
                    <c:otherwise> 
                                    <c:set var="closingBalanceOfSubgroup" value="${(closingBalanceOfSubgroup)+(total_credit_Subgroup_beforeStartDate-total_debit_Subgroup_beforeStartDate)+(total_credit_Subgroup-total_debit_Subgroup)}" />
				                        
				    </c:otherwise>
                    </c:choose>
                                      <c:if test="${closingBalanceOfSubgroup!=0}">
									<tr class='n-style'>
									<td style="text-align: left;">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;${accSubGroup}</td>
                                   <td class="tright"><fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${closingBalanceOfSubgroup}"/></td>
				                   <td></td>
				                    </tr>
				                    
				                    <c:if test="${obalance.ledgerformlist.size()>0}">
											<c:forEach var="ledgerform3"
												items="${obalance.ledgerformlist}">
												<c:if test="${ledgerform3!=null}">


													<c:if test="${ledgerform3.subgroupName==accSubGroup}">
														<c:set var="openiBalanceOfLedger" value="0" />
														<c:set var="total_debitOfLedger" value="0" />
														<c:set var="total_creditOfLedger" value="0" />

														<c:forEach var="obalanceBeStartDate"
															items="${subOpeningListBeforeStartDate}">
															<c:if
																test="${obalanceBeStartDate.accountSubGroupNameList.size()>0}">
																<c:forEach var="subGroupName"
																	items="${obalanceBeStartDate.accountSubGroupNameList}">
																	<c:if test="${subGroupName==accSubGroup}">
																		<c:if
																			test="${obalanceBeStartDate.ledgerformlist.size()>0}">
																			<c:forEach var="ledgerform4"
																				items="${obalanceBeStartDate.ledgerformlist}">
																				<c:if
																					test="${ledgerform4.ledgerName==ledgerform3.ledgerName}">
																					<c:if
																						test="${ledgerform4.subgroupName==accSubGroup}">
																						<c:set var="openiBalanceOfLedger"
																							value="${openiBalanceOfLedger + (ledgerform4.ledgercredit_balance-ledgerform4.ledgerdebit_balance)}" />
																					</c:if>
																				</c:if>
																			</c:forEach>
																		</c:if>
																	</c:if>
																</c:forEach>
															</c:if>
														</c:forEach>
														<c:set var="total_debitOfLedger"
															value="${ledgerform3.ledgerdebit_balance}" />
														<c:set var="total_creditOfLedger"
															value="${ledgerform3.ledgercredit_balance}" />

														<c:if
															test="${openiBalanceOfLedger!=0 || total_debitOfLedger!=0 || total_creditOfLedger!=0}">
															<tr class='n-style'>
									<td style="text-align: left;">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;${ledgerform3.ledgerName}</td>
				                   <td class="tright"><fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${openiBalanceOfLedger+total_creditOfLedger-total_debitOfLedger}"/></td>
				                   <td></td>
				                    </tr>
														</c:if>
													</c:if>



													<!--  start of subledger -->
													<c:if test="${ledgerform3.subledgerList.size()>0}">
														<c:if test="${ledgerform3.subgroupName==accSubGroup}">
															<c:forEach var="subledger"
																items="${ledgerform3.subledgerList}">
																<c:if test="${subledger!=null}">

																	<c:set var="openiBalanceOfSubLedger" value="0" />
																	<c:set var="total_debitOfSubLedger" value="0" />
																	<c:set var="total_creditOfSubLedger" value="0" />
																	<c:forEach var="obalanceBeStartDate"
																		items="${subOpeningListBeforeStartDate}">
																		<c:if
																			test="${obalanceBeStartDate.accountSubGroupNameList.size()>0}">
																			<c:forEach var="subGroupName"
																				items="${obalanceBeStartDate.accountSubGroupNameList}">
																				<c:if test="${subGroupName==accSubGroup}">
																					<c:if
																						test="${obalanceBeStartDate.ledgerformlist.size()>0}">
																						<c:forEach var="ledgerform5"
																							items="${obalanceBeStartDate.ledgerformlist}">
																							<c:if
																								test="${ledgerform5.subgroupName==accSubGroup}">
																								<c:forEach var="subledger1"
																									items="${ledgerform5.subledgerList}">
																									<c:if test="${subledger1!=null}">
																										<c:if
																											test="${subledger1.subledgerName==subledger.subledgerName}">
																											<c:set var="openiBalanceOfSubLedger"
																												value="${openiBalanceOfSubLedger + (subledger1.credit_balance-subledger1.debit_balance)}" />
																										</c:if>
																									</c:if>
																								</c:forEach>
																							</c:if>
																						</c:forEach>
																					</c:if>
																				</c:if>
																			</c:forEach>
																		</c:if>
																	</c:forEach>

																	<c:set var="total_debitOfSubLedger"
																		value="${subledger.debit_balance}" />
																	<c:set var="total_creditOfSubLedger"
																		value="${subledger.credit_balance}" />

																	<c:if
																		test="${openiBalanceOfSubLedger!=0 || total_debitOfSubLedger!=0 || total_creditOfSubLedger!=0}">
																		<tr class='n-style'>
									<td style="text-align: left;">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;${subledger.subledgerName}</td>
				                   <td class="tright"><fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${openiBalanceOfSubLedger+total_creditOfSubLedger-total_debitOfSubLedger}"/></td>
				                   <td></td>
				                    </tr>
																	</c:if>

																</c:if>
															</c:forEach>

														</c:if>
													</c:if>

												</c:if>

											</c:forEach>
										</c:if>
										
										<c:forEach var="obalanceBeStartDate"
																			items="${subOpeningListBeforeStartDate}">

																			<c:if
																				test="${obalanceBeStartDate.accountSubGroupNameList.size()>0}">
																				<c:forEach var="subGroupName"
																					items="${obalanceBeStartDate.accountSubGroupNameList}">
																					<c:if test="${subGroupName==accSubGroup}">
																						<c:if
																							test="${obalanceBeStartDate.ledgerformlist.size()>0}">
																							<c:forEach var="ledgerform6"
																								items="${obalanceBeStartDate.ledgerformlist}">
																								<c:set var="isNotPresent" value="0" />
																								<c:set var="isPresent" value="0" />
																								<c:if
																									test="${ledgerform6.subgroupName==accSubGroup}">
																									<c:if
																										test="${obalance.ledgerformlist.size()>0}">

																										<c:forEach var="ledgerform7"
																											items="${obalance.ledgerformlist}">
																											<c:if
																												test="${ledgerform7.ledgerName==ledgerform6.ledgerName}">
																												<c:set var="isPresent" value="1" />
																											</c:if>
																										</c:forEach>
																										<c:if test="${isPresent==0}">
																											<c:if
																												test="${obalance.ledgerformlist.size()>0}">
																												<c:forEach var="ledgerform7"
																													items="${obalance.ledgerformlist}">
																													<c:if
																														test="${ledgerform7.ledgerName!=ledgerform6.ledgerName}">
																														<c:if test="${isNotPresent==0}">
																															<c:set var="openiBalanceOfLedger"
																																value="0" />

																															<c:set var="openiBalanceOfLedger"
																																value="${openiBalanceOfLedger + (ledgerform6.ledgercredit_balance-ledgerform6.ledgerdebit_balance)}" />

																															<tr class='n-style'>
																																<td style="text-align: left;">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;${ledgerform6.ledgerName}</td>
																																<td class="tright"><fmt:formatNumber
																																		type="number" minFractionDigits="2"
																																		maxFractionDigits="2"
																																		value="${openiBalanceOfLedger}" /></td>
																																<td></td>
																															</tr>

																															<c:if
																																test="${ledgerform6.subledgerList.size()>0}">


																																<c:forEach var="subledger1"
																																	items="${ledgerform6.subledgerList}">
																																	<c:if test="${subledger1!=null}">
																																		<c:set var="openiBalanceOfSubLedger"
																																			value="0" />
																																		<c:set var="openiBalanceOfSubLedger"
																																			value="${openiBalanceOfSubLedger + (subledger1.credit_balance-subledger1.debit_balance)}" />
																																		<c:if
																																			test="${openiBalanceOfSubLedger!=0}">
																																			<tr class='n-style'>
																																				<td style="text-align: left;">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;${subledger1.subledgerName}</td>
																																				<td class="tright"><fmt:formatNumber
																																						type="number"
																																						minFractionDigits="2"
																																						maxFractionDigits="2"
																																						value="${openiBalanceOfSubLedger}" /></td>
																																				<td></td>
																																			</tr>
																																		</c:if>
																																	</c:if>

																																</c:forEach>

																															</c:if>
																															<c:set var="isNotPresent" value="1" />
																														</c:if>
																													</c:if>

																												</c:forEach>
																											</c:if>

																										</c:if>
																									</c:if>
																									<c:if test="${obalance.ledgerformlist.size()==0}">
																			 
																						
																							<c:set var="openiBalanceOfLedger" value="0" />
																							<tr class='n-style'>
																								<td style="text-align: left;">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;${ledgerform6.ledgerName}</td>
																								<c:set var="openiBalanceOfLedger"
																									value="${openiBalanceOfLedger + (ledgerform6.ledgercredit_balance-ledgerform6.ledgerdebit_balance)}" />
																								<td class="tright"><fmt:formatNumber
																										type="number" minFractionDigits="2"
																										maxFractionDigits="2"
																										value="${openiBalanceOfLedger}" /></td>
																								<td></td>
																							</tr>		
																								<c:if
																									test="${ledgerform6.subledgerList.size()>0}">


																									<c:forEach var="subledger1"
																										items="${ledgerform6.subledgerList}">
																										<c:if test="${subledger1!=null}">
																											<c:set var="openiBalanceOfSubLedger"
																												value="0" />
																											<c:set var="openiBalanceOfSubLedger"
																												value="${openiBalanceOfSubLedger + (subledger1.credit_balance-subledger1.debit_balance)}" />

																											<c:if test="${openiBalanceOfSubLedger!=0}">
																												<tr class='n-style'>
																														<td style="text-align: left;">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;${subledger1.subledgerName}</td>
																														<td class="tright"><fmt:formatNumber
																																type="number"
																																minFractionDigits="2"
																																maxFractionDigits="2"
																																value="${openiBalanceOfSubLedger}" /></td>
																														<td></td>
																													</tr>
																											</c:if>
																										</c:if>

																									</c:forEach>

																								</c:if>
																						
																			</c:if>
																								</c:if>
																							</c:forEach>
																						</c:if>
																					</c:if>

																				</c:forEach>

																			</c:if>

																		</c:forEach>
																		
																		
											<c:if test="${accSubGroup==subGroup5}">
											<c:forEach var="supOpbalance"
												items="${supplierOpeningBalanceList}">
												<c:set var="supOpeningbalance" value="0" />
												<c:set var="supDebitbalance" value="0" />
												<c:set var="supCreditbalance" value="0" />

												<c:forEach var="supOpbalance1"
													items="${supplierOpeningBalanceBeforeStartDate}">
													<c:if
														test="${supOpbalance1.supplierName == supOpbalance.supplierName}">
														<c:set var="supOpeningbalance"
															value="${supOpeningbalance + (supOpbalance1.credit_balance-supOpbalance1.debit_balance)}" />
													</c:if>
												</c:forEach>

												<c:set var="supDebitbalance"
													value="${supOpbalance.debit_balance}" />
												<c:set var="supCreditbalance"
													value="${supOpbalance.credit_balance}" />

												<c:if
													test="${supOpeningbalance!=0 || supDebitbalance!=0 || supCreditbalance!=0}">

														<tr class='n-style'>
															<td style="text-align: left;">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;${supOpbalance.supplierName}</td>
															<td class="tright"><fmt:formatNumber
																	type="number" minFractionDigits="2"
																	maxFractionDigits="2"
																	value="${(supOpeningbalance)+(supCreditbalance-supDebitbalance)}" /></td>
															<td></td>
														</tr>
												</c:if>
											</c:forEach>


											<c:forEach var="supOpbalance1"
												items="${supplierOpeningBalanceBeforeStartDate}">
												<c:set var="issupplierPresent" value="0" />
												<c:set var="openiBalanceOfsupplier" value="0" />

												<c:forEach var="supOpbalance"
													items="${supplierOpeningBalanceList}">

													<c:if
														test="${supOpbalance1.supplierName == supOpbalance.supplierName}">
														<c:set var="issupplierPresent" value="1" />

													</c:if>
												</c:forEach>

												<c:if test="${issupplierPresent==0}">

													<c:set var="openiBalanceOfsupplier"
														value="${(openiBalanceOfsupplier)+(supOpbalance1.credit_balance-supOpbalance1.debit_balance)}" />


													<c:if test="${openiBalanceOfsupplier!=0}">
													
													<tr class='n-style'>
															<td style="text-align: left;">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;${supOpbalance1.supplierName}</td>
															<td class="tright"><fmt:formatNumber
																	type="number" minFractionDigits="2"
																	maxFractionDigits="2"
																	value="${openiBalanceOfsupplier}" /></td>
															<td></td>
														</tr>
													</c:if>

												</c:if>

											</c:forEach>

										</c:if>
				                    
				                      </c:if> 
		                     </c:forEach>
				                      </c:if>
				                      
				                      
				                        </c:if>
				                    </c:if>			                        
					   </c:forEach>
					   
						
				
				 </c:if>
			</tbody>
							
								<tfoot style='font-weight: bold'>
								 <c:set var="totalNetProfit" value="0"/>
							  <c:if test="${TotalCreditAmount1>TotalDebitAmount1}">
							    <tr>
									<td><b>Profit & Loss A/c</b></td>
									<td></td>
									<td class="tright"><b><fmt:formatNumber type="number" minFractionDigits="2"
									maxFractionDigits="2" value="${TotalCreditAmount1-TotalDebitAmount1}" /></b>
									 <c:set var="totalNetProfit" value="${totalNetProfit+(TotalCreditAmount1-TotalDebitAmount1)}" />
									</td>
								</tr>
								 
							 </c:if>
							  <c:if test="${TotalDebitAmount1>TotalCreditAmount1}">
							    <tr>
									<td><b>Profit & Loss A/c</b></td>
									<td></td>
									<td class="tright"><b><fmt:formatNumber type="number" minFractionDigits="2"
									maxFractionDigits="2" value="${TotalCreditAmount1-TotalDebitAmount1}" /></b>
									 <c:set var="totalNetProfit" value="${totalNetProfit+(TotalCreditAmount1-TotalDebitAmount1)}" />
									</td>
								</tr>
								 
							 </c:if>
								<tr>
									<td><b>Total</b></td>
									<td></td>
									<td class="tright"><b><fmt:formatNumber type="number" minFractionDigits="2"
									maxFractionDigits="2" value="${TotalCreditAmount+totalNetProfit}" /></b></td>
								</tr>
							
							</tfoot>
			
						</table> <!-- End of Liabilities  -->
					</td>
				</tr>
			</tbody>
		</table>
	</div>
	</td>
	
	<td style="vertical-align:top">
	<div class="borderForm">
		<table style="width: 100%">
			<thead>
				<tr>
					<th style='text-align: center'>Assets</th>
				</tr>
			</thead>
			<tbody>	
				<tr>
					<td style='vertical-align:top'>
						<!-- Start of Assets  -->
						<table id="table" data-toggle="table" data-search="false"
							data-escape="false" data-filter-control="true"
							data-show-export="false" data-click-to-select="true"
							data-pagination="true" data-page-size="10"
							data-toolbar="#toolbar" class="table">
							<thead>
								<tr>
									<th data-field="particulars-in" data-filter-control="input"
										data-sortable="true">Particulars</th>
									<th data-field="amount-in" data-filter-control="input"
											data-sortable="true"></th>
									<th data-field="amount" data-filter-control="input"
											data-sortable="true"></th>
								</tr>
							</thead>
							<tbody>
								
		
		
				  <c:if test="${subOpeningList.size()>0}">
				
				<c:forEach var="obalance" items="${subOpeningList}">
				                       <!--  start of group -->		
				                       
				               <c:if test="${obalance.group.postingSide.posting_id == 3}"> 
				                 <c:set var="closingBalance" value="0"/> 
				                <c:choose>
                    <c:when test="${obalance.accountGroupName==group1}">
                    
                            <c:forEach var="obalanceBeStartDate" items="${subOpeningListBeforeStartDate}">	
				                    <c:if test="${obalanceBeStartDate.accountGroupName==obalance.accountGroupName}">
				                <c:set var="openingBalanceforGroup" value="${(obalanceBeStartDate.totaldebit_balance+total_debit_Customer_beforeStartDate+total_debit_Bank_beforeStartDate)-(obalanceBeStartDate.totalcredit_balance+total_credit_Customer_beforeStartDate+total_credit_Bank_beforeStartDate)}" />
			                        </c:if>
				              </c:forEach>
				              
                    	
                    		<c:set var="total_debitGroup" value="${(obalance.totaldebit_balance+total_debit_Customer+total_debit_Bank)}" />
                    			<c:set var="total_creditGroup" value="${(obalance.totalcredit_balance+total_credit_Customer+total_credit_Bank)}"/>
                   
									
					                  <c:set var="closingBalance" value="${(closingBalance)+(openingBalanceforGroup)+(total_debitGroup-total_creditGroup)}"/> 
                    </c:when>
                    <c:otherwise> 
									  <c:forEach var="obalanceBeStartDate" items="${subOpeningListBeforeStartDate}">	
				                    <c:if test="${obalanceBeStartDate.accountGroupName==obalance.accountGroupName}">
				               <c:set var="closingBalance" value="${(closingBalance)+(obalanceBeStartDate.totaldebit_balance-obalanceBeStartDate.totalcredit_balance)+(obalance.totaldebit_balance-obalance.totalcredit_balance)}" /> 
			                        </c:if>
				              </c:forEach>
									  
				    </c:otherwise>
                    </c:choose>
                            		<c:if test="${closingBalance!=0}">		
									<tr class='n-style'>
									<td style="text-align: left;"><h5>${obalance.accountGroupName}</h5></td>
									<td></td>
									<td class="tright"><fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${closingBalance}"/>
									</td>
								    </tr>
									
									
									
									<!--  start of subgroup -->
									 <c:if test="${obalance.accountSubGroupNameList.size()>0}">
									<c:forEach var="accSubGroup" items="${obalance.accountSubGroupNameList}">
									<c:set var="closingBalanceOfSubgroup" value="0"/>
									<c:set var="total_credit_Subgroup_beforeStartDate" value="0"/>		
				                  <c:set var="total_debit_Subgroup_beforeStartDate" value="0"/>	
				                   <c:set var="total_credit_Subgroup" value="0"/>		
				                  <c:set var="total_debit_Subgroup" value="0"/>	
				                  
				                   <c:forEach var="obalanceBeStartDate" items="${subOpeningListBeforeStartDate}">	
								   
								   <c:if test="${obalanceBeStartDate.accountSubGroupNameList.size()>0}">
								   
								   <c:forEach var="subGroupName" items="${obalanceBeStartDate.accountSubGroupNameList}">
								   
								   <c:if test="${subGroupName==accSubGroup}">
								    <c:if test="${obalanceBeStartDate.ledgerformlist.size()>0}">
									<c:forEach var="ledgerform1" items="${obalanceBeStartDate.ledgerformlist}">	
				                    <c:if test="${ledgerform.subgroupName==accSubGroup}">
			                      <c:set var="total_credit_Subgroup_beforeStartDate" value="${total_credit_Subgroup_beforeStartDate + ledgerform1.ledgercredit_balance}" />
			                      <c:set var="total_debit_Subgroup_beforeStartDate" value="${total_debit_Subgroup_beforeStartDate + ledgerform1.ledgerdebit_balance}" />
			                        </c:if>
				                   </c:forEach>
				                    </c:if>
				                     </c:if>
				                 
				                    </c:forEach>
				                   
				                     </c:if>
				                   
				                    </c:forEach>
				                    
				                  	 <c:if test="${obalance.ledgerformlist.size()>0}">
									<c:forEach var="ledgerform2" items="${obalance.ledgerformlist}">	
				                    <c:if test="${ledgerform2.subgroupName==accSubGroup}">
			                      <c:set var="total_credit_Subgroup" value="${total_credit_Subgroup + ledgerform2.ledgercredit_balance}" />
			                      <c:set var="total_debit_Subgroup" value="${total_debit_Subgroup + ledgerform2.ledgerdebit_balance}" />
			                        </c:if>
				                   </c:forEach>
				                     </c:if>
				                     
				                       <c:choose>
                    <c:when test="${(accSubGroup==subGroup2)||(accSubGroup==subGroup3)}">
                    
                     <c:if test="${accSubGroup==subGroup2}">
                    	<c:set var="openingBalanceSubgroup" value="${(total_debit_Subgroup_beforeStartDate+total_debit_Customer_beforeStartDate)-(total_credit_Subgroup_beforeStartDate+total_credit_Customer_beforeStartDate)}" />
                    		<c:set var="total_debitSubgroup" value="${(total_debit_Subgroup+total_debit_Customer)}" />
                    			<c:set var="total_creditSubgroup" value="${(total_credit_Subgroup+total_credit_Customer)}"/>
                    			<c:set var="closingBalanceOfSubgroup" value="${(closingBalanceOfSubgroup)+(openingBalanceSubgroup)+(total_debitSubgroup-total_creditSubgroup)}"/>
                    				
					  </c:if>
					  
					   <c:if test="${accSubGroup==subGroup3}">
                    	<c:set var="openingBalanceSubgroup" value="${(total_debit_Subgroup_beforeStartDate+total_debit_Bank_beforeStartDate)-(total_credit_Subgroup_beforeStartDate+total_credit_Bank_beforeStartDate)}" />
                    		<c:set var="total_debitSubgroup" value="${(total_debit_Subgroup+total_debit_Bank)}" />
                    			<c:set var="total_creditSubgroup" value="${(total_credit_Subgroup+total_credit_Bank)}"/>
                    			<c:set var="closingBalanceOfSubgroup" value="${(closingBalanceOfSubgroup)+(openingBalanceSubgroup)+(total_debitSubgroup-total_creditSubgroup)}"/>
					  </c:if>
                    </c:when>
                    <c:otherwise> 
                                 <c:set var="closingBalanceOfSubgroup" value="${(closingBalanceOfSubgroup)+(total_debit_Subgroup_beforeStartDate-total_credit_Subgroup_beforeStartDate)+(total_debit_Subgroup-total_credit_Subgroup)}"/>
				    </c:otherwise>
                    </c:choose>
                    	
                    	 <c:if test="${closingBalanceOfSubgroup!=0}">
									<tr class='n-style'>
									<td style="text-align: left;">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;${accSubGroup}</td>
				                   <td class="tright"><fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${closingBalanceOfSubgroup}"/></td>     
		                            <td></td>
				                    </tr>
				                
				                <c:if test="${obalance.ledgerformlist.size()>0}">
											<c:forEach var="ledgerform3"
												items="${obalance.ledgerformlist}">
												<c:if test="${ledgerform3!=null}">


													<c:if test="${ledgerform3.subgroupName==accSubGroup}">
														<c:set var="openiBalanceOfLedger" value="0" />
														<c:set var="total_debitOfLedger" value="0" />
														<c:set var="total_creditOfLedger" value="0" />

														<c:forEach var="obalanceBeStartDate"
															items="${subOpeningListBeforeStartDate}">
															<c:if
																test="${obalanceBeStartDate.accountSubGroupNameList.size()>0}">
																<c:forEach var="subGroupName"
																	items="${obalanceBeStartDate.accountSubGroupNameList}">
																	<c:if test="${subGroupName==accSubGroup}">
																		<c:if
																			test="${obalanceBeStartDate.ledgerformlist.size()>0}">
																			<c:forEach var="ledgerform4"
																				items="${obalanceBeStartDate.ledgerformlist}">
																				<c:if
																					test="${ledgerform4.ledgerName==ledgerform3.ledgerName}">
																					<c:if
																						test="${ledgerform4.subgroupName==accSubGroup}">
																						<c:set var="openiBalanceOfLedger"
																							value="${openiBalanceOfLedger + (ledgerform4.ledgerdebit_balance-ledgerform4.ledgercredit_balance)}" />
																					</c:if>
																				</c:if>
																			</c:forEach>
																		</c:if>
																	</c:if>
																</c:forEach>
															</c:if>
														</c:forEach>
														<c:set var="total_debitOfLedger"
															value="${ledgerform3.ledgerdebit_balance}" />
														<c:set var="total_creditOfLedger"
															value="${ledgerform3.ledgercredit_balance}" />

														<c:if
															test="${openiBalanceOfLedger!=0 || total_debitOfLedger!=0 || total_creditOfLedger!=0}">
															<tr class='n-style'>
									<td style="text-align: left;">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;${ledgerform3.ledgerName}</td>
				                   <td class="tright"><fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${openiBalanceOfLedger+total_debitOfLedger-total_creditOfLedger}"/></td>
				                   <td></td>
				                    </tr>
														</c:if>
													</c:if>



													<!--  start of subledger -->
													<c:if test="${ledgerform3.subledgerList.size()>0}">
														<c:if test="${ledgerform3.subgroupName==accSubGroup}">
															<c:forEach var="subledger"
																items="${ledgerform3.subledgerList}">
																<c:if test="${subledger!=null}">

																	<c:set var="openiBalanceOfSubLedger" value="0" />
																	<c:set var="total_debitOfSubLedger" value="0" />
																	<c:set var="total_creditOfSubLedger" value="0" />
																	<c:forEach var="obalanceBeStartDate"
																		items="${subOpeningListBeforeStartDate}">
																		<c:if
																			test="${obalanceBeStartDate.accountSubGroupNameList.size()>0}">
																			<c:forEach var="subGroupName"
																				items="${obalanceBeStartDate.accountSubGroupNameList}">
																				<c:if test="${subGroupName==accSubGroup}">
																					<c:if
																						test="${obalanceBeStartDate.ledgerformlist.size()>0}">
																						<c:forEach var="ledgerform5"
																							items="${obalanceBeStartDate.ledgerformlist}">
																							<c:if
																								test="${ledgerform5.subgroupName==accSubGroup}">
																								<c:forEach var="subledger1"
																									items="${ledgerform5.subledgerList}">
																									<c:if test="${subledger1!=null}">
																										<c:if
																											test="${subledger1.subledgerName==subledger.subledgerName}">
																											<c:set var="openiBalanceOfSubLedger"
																												value="${openiBalanceOfSubLedger + (subledger1.debit_balance-subledger1.credit_balance)}" />
																										</c:if>
																									</c:if>
																								</c:forEach>
																							</c:if>
																						</c:forEach>
																					</c:if>
																				</c:if>
																			</c:forEach>
																		</c:if>
																	</c:forEach>

																	<c:set var="total_debitOfSubLedger"
																		value="${subledger.debit_balance}" />
																	<c:set var="total_creditOfSubLedger"
																		value="${subledger.credit_balance}" />

																	<c:if
																		test="${openiBalanceOfSubLedger!=0 || total_debitOfSubLedger!=0 || total_creditOfSubLedger!=0}">
																		<tr class='n-style'>
									<td style="text-align: left;">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;${subledger.subledgerName}</td>
				                   <td class="tright"><fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${openiBalanceOfSubLedger+total_debitOfSubLedger-total_creditOfSubLedger}"/></td>
				                   <td></td>
				                    </tr>
																	</c:if>

																</c:if>
															</c:forEach>

														</c:if>
													</c:if>

												</c:if>

											</c:forEach>
										</c:if>

																		<c:forEach var="obalanceBeStartDate"
																			items="${subOpeningListBeforeStartDate}">

																			<c:if
																				test="${obalanceBeStartDate.accountSubGroupNameList.size()>0}">
																				<c:forEach var="subGroupName"
																					items="${obalanceBeStartDate.accountSubGroupNameList}">
																					<c:if test="${subGroupName==accSubGroup}">
																						<c:if
																							test="${obalanceBeStartDate.ledgerformlist.size()>0}">
																							<c:forEach var="ledgerform6"
																								items="${obalanceBeStartDate.ledgerformlist}">
																								<c:set var="isNotPresent" value="0" />
																								<c:set var="isPresent" value="0" />
																								<c:if
																									test="${ledgerform6.subgroupName==accSubGroup}">
																									<c:if
																										test="${obalance.ledgerformlist.size()>0}">

																										<c:forEach var="ledgerform7"
																											items="${obalance.ledgerformlist}">
																											<c:if
																												test="${ledgerform7.ledgerName==ledgerform6.ledgerName}">
																												<c:set var="isPresent" value="1" />
																											</c:if>
																										</c:forEach>
																										<c:if test="${isPresent==0}">
																											<c:if
																												test="${obalance.ledgerformlist.size()>0}">
																												<c:forEach var="ledgerform7"
																													items="${obalance.ledgerformlist}">
																													<c:if
																														test="${ledgerform7.ledgerName!=ledgerform6.ledgerName}">
																														<c:if test="${isNotPresent==0}">
																															<c:set var="openiBalanceOfLedger"
																																value="0" />

																															<c:set var="openiBalanceOfLedger"
																																value="${openiBalanceOfLedger + (ledgerform6.ledgerdebit_balance-ledgerform6.ledgercredit_balance)}" />

																															<tr class='n-style'>
																																<td style="text-align: left;">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;${ledgerform6.ledgerName}</td>
																																<td class="tright"><fmt:formatNumber
																																		type="number" minFractionDigits="2"
																																		maxFractionDigits="2"
																																		value="${openiBalanceOfLedger}" /></td>
																																<td></td>
																															</tr>

																															<c:if
																																test="${ledgerform6.subledgerList.size()>0}">


																																<c:forEach var="subledger1"
																																	items="${ledgerform6.subledgerList}">
																																	<c:if test="${subledger1!=null}">
																																		<c:set var="openiBalanceOfSubLedger"
																																			value="0" />
																																		<c:set var="openiBalanceOfSubLedger"
																																			value="${openiBalanceOfSubLedger + (subledger1.debit_balance-subledger1.credit_balance)}" />
																																		<c:if
																																			test="${openiBalanceOfSubLedger!=0}">
																																			<tr class='n-style'>
																																				<td style="text-align: left;">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;${subledger1.subledgerName}</td>
																																				<td class="tright"><fmt:formatNumber
																																						type="number"
																																						minFractionDigits="2"
																																						maxFractionDigits="2"
																																						value="${openiBalanceOfSubLedger}" /></td>
																																				<td></td>
																																			</tr>
																																		</c:if>
																																	</c:if>

																																</c:forEach>

																															</c:if>
																															<c:set var="isNotPresent" value="1" />
																														</c:if>
																													</c:if>

																												</c:forEach>
																											</c:if>

																										</c:if>
																									</c:if>
																									<c:if test="${obalance.ledgerformlist.size()==0}">
																			 
																							<c:set var="openiBalanceOfLedger" value="0" />
																							<tr class='n-style'>
																								<td style="text-align: left;">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;${ledgerform6.ledgerName}</td>
																								<c:set var="openiBalanceOfLedger"
																									value="${openiBalanceOfLedger + (ledgerform6.ledgerdebit_balance-ledgerform6.ledgercredit_balance)}" />
																								<td class="tright"><fmt:formatNumber
																										type="number" minFractionDigits="2"
																										maxFractionDigits="2"
																										value="${openiBalanceOfLedger}" /></td>
																								<td></td>
																							</tr>
																								<c:if
																									test="${ledgerform6.subledgerList.size()>0}">


																									<c:forEach var="subledger1"
																										items="${ledgerform6.subledgerList}">
																										<c:if test="${subledger1!=null}">
																											<c:set var="openiBalanceOfSubLedger"
																												value="0" />
																											<c:set var="openiBalanceOfSubLedger"
																												value="${openiBalanceOfSubLedger + (subledger1.debit_balance-subledger1.credit_balance)}" />

																											<c:if test="${openiBalanceOfSubLedger!=0}">
																												<tr class='n-style'>
																														<td style="text-align: left;">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;${subledger1.subledgerName}</td>
																														<td class="tright"><fmt:formatNumber
																																type="number"
																																minFractionDigits="2"
																																maxFractionDigits="2"
																																value="${openiBalanceOfSubLedger}" /></td>
																														<td></td>
																													</tr>

																											</c:if>
																										</c:if>

																									</c:forEach>

																								</c:if>
																								
																						
																			</c:if>
																								</c:if>
																							</c:forEach>
																						</c:if>
																					</c:if>

																				</c:forEach>

																			</c:if>

																		</c:forEach>
				                
				                
				                <c:if test="${accSubGroup==subGroup3}">

											<c:forEach var="bankOpbalance"
												items="${bankOpeningBalanceList}">
												<c:set var="bankOpeningbalance" value="0" />
												<c:set var="bankDebitbalance" value="0" />
												<c:set var="bankCreditbalance" value="0" />

												<c:forEach var="bankOpbalance1"
													items="${bankOpeningBalanceBeforeStartDate}">
													<c:if
														test="${bankOpbalance1.bankName == bankOpbalance.bankName}">
														<c:set var="bankOpeningbalance"
															value="${(bankOpeningbalance)+(bankOpbalance1.debit_balance-bankOpbalance1.credit_balance)}" />
													</c:if>
												</c:forEach>

												<c:set var="bankDebitbalance"
													value="${bankOpbalance.debit_balance}" />
												<c:set var="bankCreditbalance"
													value="${bankOpbalance.credit_balance}" />

												<c:if
													test="${bankOpeningbalance!=0 || bankDebitbalance!=0 || bankCreditbalance!=0}">
													
													<tr class='n-style'>
														<td style="text-align: left;">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;${bankOpbalance.bankName}</td>
														<td class="tright"><fmt:formatNumber
																type="number" minFractionDigits="2"
																maxFractionDigits="2"
																value="${(bankOpeningbalance)+(bankDebitbalance-bankCreditbalance)}" /></td>
														<td></td>
													</tr>
													
												</c:if>
											</c:forEach>

											<c:forEach var="bankOpbalance1"
												items="${bankOpeningBalanceBeforeStartDate}">
												<c:set var="isBankPresent" value="0" />
												<c:set var="openiBalanceOfbank" value="0" />
												<c:forEach var="bankOpbalance"
													items="${bankOpeningBalanceList}">

													<c:if
														test="${bankOpbalance1.bankName == bankOpbalance.bankName}">
														<c:set var="isBankPresent" value="1" />

													</c:if>
												</c:forEach>

												<c:if test="${isBankPresent==0}">

													<c:set var="openiBalanceOfbank"
														value="${(openiBalanceOfbank)+(bankOpbalance1.debit_balance-bankOpbalance1.credit_balance)}" />


													<c:if test="${openiBalanceOfbank!=0}">
														
														<tr class='n-style'>
														<td style="text-align: left;">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;${bankOpbalance1.bankName}</td>
														<td class="tright"><fmt:formatNumber
																type="number" minFractionDigits="2"
																maxFractionDigits="2"
																value="${openiBalanceOfbank}" /></td>
														<td></td>
													</tr>
													</c:if>

												</c:if>

											</c:forEach>



										</c:if>

										<c:if test="${accSubGroup==subGroup2}">
											<c:forEach var="cusoPbalance"
												items="${customerOpeningBalanceList}">
												<c:set var="cusOpeningbalance" value="0" />
												<c:set var="cusDebitbalance" value="0" />
												<c:set var="cusCreditbalance" value="0" />

												<c:forEach var="cusoPbalance1"
													items="${customerOpeningBalanceBeforeStartDate}">
													<c:if
														test="${cusoPbalance1.customerName == cusoPbalance.customerName}">
														<c:set var="cusOpeningbalance"
															value="${(cusOpeningbalance)+(cusoPbalance1.debit_balance-cusoPbalance1.credit_balance)}" />
													</c:if>
												</c:forEach>
												<c:set var="cusDebitbalance"
													value="${cusoPbalance.debit_balance}" />
												<c:set var="cusCreditbalance"
													value="${cusoPbalance.credit_balance}" />

												<c:if
													test="${cusOpeningbalance!=0 || cusDebitbalance!=0 || cusCreditbalance!=0}">
													
														<tr class='n-style'>
														<td style="text-align: left;">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;${cusoPbalance.customerName}</td>
														<td class="tright"><fmt:formatNumber
																type="number" minFractionDigits="2"
																maxFractionDigits="2"
																value="${(cusOpeningbalance)+(cusDebitbalance-cusCreditbalance)}" /></td>
														<td></td>
													</tr>
												</c:if>

											</c:forEach>


											<c:forEach var="cusoPbalance1"
												items="${customerOpeningBalanceBeforeStartDate}">
												<c:set var="iscustomerPresent" value="0" />
												<c:set var="openiBalanceOfcustomer" value="0" />

												<c:forEach var="cusoPbalance"
													items="${customerOpeningBalanceList}">

													<c:if
														test="${cusoPbalance1.customerName == cusoPbalance.customerName}">
														<c:set var="iscustomerPresent" value="1" />

													</c:if>
												</c:forEach>

												<c:if test="${iscustomerPresent==0}">

													<c:set var="openiBalanceOfcustomer"
														value="${(openiBalanceOfcustomer)+(cusoPbalance1.debit_balance-cusoPbalance1.credit_balance)}" />


													<c:if test="${openiBalanceOfcustomer!=0}">
														<tr class='n-style'>
														<td style="text-align: left;">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;${cusoPbalance1.customerName}</td>
														<td class="tright"><fmt:formatNumber
																type="number" minFractionDigits="2"
																maxFractionDigits="2"
																value="${openiBalanceOfcustomer}" /></td>
														<td></td>
													</tr>
													</c:if>

												</c:if>

											</c:forEach>
										</c:if>
				                
			            </c:if>
				                     </c:forEach>
				                      </c:if>
				                       </c:if>
				                      
				                    </c:if>			                        
					   </c:forEach>
				
				 </c:if>
				              
           			</tbody>
							<tfoot style='font-weight: bold'>	
								<tr>
									<td><b>Total</b></td>
									<td></td>
									<td class="tright"><b><fmt:formatNumber type="number" minFractionDigits="2"
									maxFractionDigits="2" value="${TotalDebitAmount}" /></b></td>
								</tr>
							</tfoot>
						</table> <!-- End of Assets  -->
					</td>
				</tr>
			</tbody>
		</table>
	</div>
	</td>
	</tr>
	</table>
	<div class="row text-center-btn">
	<c:if test="${role!=7}">
		<button class="fassetBtn" type="button" onclick = "pdf()"> 
			   Download As PDF
		        </button>
		<button class="fassetBtn" type="button" id='btnExport3' onclick = 'exportexcel("Horizontal Balance Sheet Report")'>
				Download as Excel
			</button>
		</c:if>
		<button class="fassetBtn" type="button" onclick="back();">
			<spring:message code="btn_back" />
		</button>
	</div>

<script type="text/javascript">
	$(function() {
		setTimeout(function() {
			$("#successMsg").hide();
		}, 3000);
	});
	function back() {
		window.location.assign('<c:url value = "horizontalBalanceSheetReport"/>');
	}
    function pdf() {
		window.location.assign('<c:url value = "pdfHorizontalBalanceSheetReport"/>');
	}  
</script>
<%@include file="/WEB-INF/includes/footer.jsp"%>