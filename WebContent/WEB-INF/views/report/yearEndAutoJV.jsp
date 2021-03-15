<%@include file="/WEB-INF/includes/header.jsp"%>
<%@page import="com.fasset.controller.abstracts.MyAbstractController"%>
<spring:url value="/resources/images/edit.png" var="editImg" />
<spring:url value="/resources/images/view.png" var="viewImg" />
<script type="text/javascript" src="${valid}"></script>


<spring:url value="/resources/js/jspdf.min.js" var="jspdfmin" />
<spring:url value="/resources/js/jspdf.plugin.autotable.js"
	var="jspdfauto" />
<spring:url value="/resources/js/report_table/horizontalbalanceprofitandloss.js"
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
	<h3>Year End Journal Voucher</h3>
	<a href="homePage">Home</a> » <a href="yearEndAutoJV">Year End Journal Voucher</a>
</div>
<div class="col-md-12">
<c:set var="debitCountforAdujustingRow" value="0"/>	
<c:set var="creditCountforAdujustingRow" value="0"/>

<c:set var="TotalDebitAmount" value="0"/>	
<c:set var="TotalCreditAmount" value="0"/>
<c:forEach var="obalance" items="${subOpeningList}">
	<c:if test="${obalance.group.postingSide.posting_id == 1}">
<c:forEach var="obalanceBeStartDate" items="${subOpeningListBeforeStartDate}">	      
	<c:if test="${obalanceBeStartDate.accountGroupName==obalance.accountGroupName}">
		<c:set var="TotalDebitAmount" value="${TotalDebitAmount+((obalanceBeStartDate.totaldebit_balance-obalanceBeStartDate.totalcredit_balance)+(obalance.totaldebit_balance-obalance.totalcredit_balance))}" />
    </c:if>
</c:forEach>
              
</c:if>
<c:if test="${obalance.group.postingSide.posting_id == 2}">

<c:forEach var="obalanceBeStartDate" items="${subOpeningListBeforeStartDate}">	
               <c:if test="${obalanceBeStartDate.accountGroupName==obalance.accountGroupName}">
        			 <c:set var="TotalCreditAmount" value="${TotalCreditAmount+((obalanceBeStartDate.totalcredit_balance-obalanceBeStartDate.totaldebit_balance)+(obalance.totalcredit_balance-obalance.totaldebit_balance))}" />
                  </c:if>
</c:forEach>
              

</c:if>
</c:forEach>
<c:if test="${TotalCreditAmount>TotalDebitAmount}">
     <c:set var="debitCountforAdujustingRow" value="${debitCountforAdujustingRow + 1}" />
</c:if>
<c:if test="${TotalDebitAmount>TotalCreditAmount}">
 <c:set var="creditCountforAdujustingRow" value="${creditCountforAdujustingRow + 1}" />	
</c:if>

<c:if test="${subOpeningList.size()>0}">

<c:forEach var="obalance" items="${subOpeningList}">
                  <!--  start of group -->		
                  
          <c:if test="${obalance.group.postingSide.posting_id == 1}"> 
                <c:set var="closingBalance" value="0"/> 
                	<c:forEach var="obalanceBeStartDate" items="${subOpeningListBeforeStartDate}">	
               <c:if test="${obalanceBeStartDate.accountGroupName==obalance.accountGroupName}">
               <c:set var="closingBalance" value="${(closingBalance)+(obalanceBeStartDate.totaldebit_balance-obalanceBeStartDate.totalcredit_balance)+(obalance.totaldebit_balance-obalance.totalcredit_balance)}"/> 
                  </c:if>
              </c:forEach>
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
                
                <c:set var="closingBalanceOfSubgroup" value="${(closingBalanceOfSubgroup)+(total_debit_Subgroup_beforeStartDate-total_credit_Subgroup_beforeStartDate)+(total_debit_Subgroup-total_credit_Subgroup)}"/>
                
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

																						<c:set var="openiBalanceOfLedger"
																							value="${openiBalanceOfLedger + (ledgerform6.ledgerdebit_balance-ledgerform6.ledgercredit_balance)}" />

																					<c:set var="debitCountforAdujustingRow" value="${debitCountforAdujustingRow + 1}" />

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
                </c:if>
                </c:forEach>
                 </c:if>
                  
                   </c:if>
               </c:if>	
         	
               
                <c:if test="${obalance.group.postingSide.posting_id == 2}">  
          
                   <c:set var="closingBalance" value="0"/> 
                	<c:forEach var="obalanceBeStartDate" items="${subOpeningListBeforeStartDate}">	
               <c:if test="${obalanceBeStartDate.accountGroupName==obalance.accountGroupName}">
               <c:set var="closingBalance" value="${(closingBalance)+(obalanceBeStartDate.totalcredit_balance-obalanceBeStartDate.totaldebit_balance)+(obalance.totalcredit_balance-obalance.totaldebit_balance)}"/> 
                  </c:if>
              </c:forEach>
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
               <c:set var="closingBalanceOfSubgroup" value="${(closingBalanceOfSubgroup)+(total_credit_Subgroup_beforeStartDate-total_debit_Subgroup_beforeStartDate)+(total_credit_Subgroup-total_debit_Subgroup)}"/>
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
									
               </c:if>
          </c:forEach>
                 </c:if>
                 </c:if>
                  
               </c:if>			                                                
</c:forEach>

</c:if>

<table style="width: 100%">
<tr>
<td style="vertical-align:top">
<div class="borderForm">
	<table style="width: 100%">
<thead>
	<tr>
		<th style='text-align: center'> debit side</th>
	</tr>y
</thead>
<tbody>
			
	<tr>
		<td style='vertical-align:top'>
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
                  
          <c:if test="${obalance.group.postingSide.posting_id == 1}"> 
                <c:set var="closingBalance" value="0"/> 
                	<c:forEach var="obalanceBeStartDate" items="${subOpeningListBeforeStartDate}">	
               <c:if test="${obalanceBeStartDate.accountGroupName==obalance.accountGroupName}">
               <c:set var="closingBalance" value="${(closingBalance)+(obalanceBeStartDate.totaldebit_balance-obalanceBeStartDate.totalcredit_balance)+(obalance.totaldebit_balance-obalance.totalcredit_balance)}"/> 
                  </c:if>
              </c:forEach>
               <c:if test="${closingBalance!=0}">
                			
<tr class='n-style'>

<td style="text-align: left;"><h5>${obalance.accountGroupName}</h5></td>
<td></td>
<td></td>

<%-- <td class="tright"><fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${closingBalance}"/>
</td> --%>
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
                
                <c:set var="closingBalanceOfSubgroup" value="${(closingBalanceOfSubgroup)+(total_debit_Subgroup_beforeStartDate-total_credit_Subgroup_beforeStartDate)+(total_debit_Subgroup-total_credit_Subgroup)}"/>
                
                <c:if test="${closingBalanceOfSubgroup!=0}">
<%-- <tr class='n-style'>
<td style="text-align: left;">&nbsp;&nbsp;&nbsp;&nbsp;${accSubGroup}</td>
              <td class="tright"><fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${closingBalanceOfSubgroup}"/></td>
              <td></td>
               </tr> --%>
               
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
						<%-- <tr class='n-style'>
<td style="text-align: left;">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;${ledgerform3.ledgerName}</td>
              <td class="tright"><fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${openiBalanceOfLedger+total_debitOfLedger-total_creditOfLedger}"/></td>
              <td></td>
               </tr> --%>
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
               <td class="tright"><fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${openiBalanceOfSubLedger+total_debitOfSubLedger-total_creditOfSubLedger}"/></td>
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

																						<%-- <tr class='n-style'>
																							<td style="text-align: left;">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;${ledgerform6.ledgerName}</td>
																							<td class="tright"><fmt:formatNumber
																									type="number" minFractionDigits="2"
																									maxFractionDigits="2"
																									value="${openiBalanceOfLedger}" /></td>
																							<td></td>
																						</tr> --%>

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
																											<td class="tright"><fmt:formatNumber
																													type="number"
																													minFractionDigits="2"
																													maxFractionDigits="2"
																													value="${openiBalanceOfSubLedger}" /></td>
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
														<%-- <tr class='n-style'>
															<td style="text-align: left;">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;${ledgerform6.ledgerName}</td>
															<c:set var="openiBalanceOfLedger"
																value="${openiBalanceOfLedger + (ledgerform6.ledgerdebit_balance-ledgerform6.ledgercredit_balance)}" />
															<td class="tright"><fmt:formatNumber
																	type="number" minFractionDigits="2"
																	maxFractionDigits="2"
																	value="${openiBalanceOfLedger}" /></td>
															<td></td>
														</tr> --%>
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
																					<td class="tright"><fmt:formatNumber
																							type="number"
																							minFractionDigits="2"
																							maxFractionDigits="2"
																							value="${openiBalanceOfSubLedger}" /></td>
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
   <c:if test="${TotalCreditAmount>TotalDebitAmount}">
     <c:set var="debitCountforAdujustingRow" value="${debitCountforAdujustingRow + 1}" />
  <tr>
<td><b>Net Profit</b></td>
<td></td>
<td class="tright"><b><fmt:formatNumber type="number" minFractionDigits="2"
maxFractionDigits="2" value="${TotalCreditAmount-TotalDebitAmount}" /></b>
 <c:set var="totalNetProfit" value="${totalNetProfit+(TotalCreditAmount-TotalDebitAmount)}" />
	</td>
</tr>
</c:if>	
<%--  <c:if test="${creditCountforAdujustingRow>debitCountforAdujustingRow}">

<c:if test="${(creditCountforAdujustingRow-debitCountforAdujustingRow!=0)}">
<c:forEach begin="1" end="${creditCountforAdujustingRow-debitCountforAdujustingRow}" var="i">
                        <tr>
                        <td></td>
                        <td></td>
                        <td></td>
                        </tr>
                        </c:forEach>
</c:if>

</c:if>	 --%>
<tr>
	<td><b>Total</b></td>
	<td></td>
	<td class="tright"><b><fmt:formatNumber type="number" minFractionDigits="2"
maxFractionDigits="2" value="${TotalDebitAmount+totalNetProfit}" /></b></td>
							</tr>
						</tfoot>
					</table>
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
		<th style='text-align: center'>credit side</th>
	</tr>
</thead>
<tbody>
				
	<tr>
		<td style='vertical-align:top'>

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
                  
          <c:if test="${obalance.group.postingSide.posting_id == 2}">  
          
                   <c:set var="closingBalance" value="0"/> 
                	<c:forEach var="obalanceBeStartDate" items="${subOpeningListBeforeStartDate}">	
               <c:if test="${obalanceBeStartDate.accountGroupName==obalance.accountGroupName}">
               <c:set var="closingBalance" value="${(closingBalance)+(obalanceBeStartDate.totalcredit_balance-obalanceBeStartDate.totaldebit_balance)+(obalance.totalcredit_balance-obalance.totaldebit_balance)}"/> 
                  </c:if>
              </c:forEach>
              	 <c:if test="${closingBalance!=0}">			
<tr class='n-style'>
<td style="text-align: left;"><h5>${obalance.accountGroupName}</h5></td>
<td></td>
<td></td>
<%-- <td class="tright"><fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${closingBalance}"/>
</td> --%>
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
               <c:set var="closingBalanceOfSubgroup" value="${(closingBalanceOfSubgroup)+(total_credit_Subgroup_beforeStartDate-total_debit_Subgroup_beforeStartDate)+(total_credit_Subgroup-total_debit_Subgroup)}"/>
               <c:if test="${closingBalanceOfSubgroup!=0}">
<%-- <tr class='n-style'>
<td style="text-align: left;">&nbsp;&nbsp;&nbsp;&nbsp;${accSubGroup}</td>
              <td class="tright"><fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${closingBalanceOfSubgroup}"/></td>
              <td></td>
               </tr> --%>
               
               
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
						<%-- <tr class='n-style'>
<td style="text-align: left;">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;${ledgerform3.ledgerName}</td>
              <td class="tright"><fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${openiBalanceOfLedger+total_creditOfLedger-total_debitOfLedger}"/></td>
              <td></td>
               </tr> --%>
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
              <td class="tright"><fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${openiBalanceOfSubLedger+total_creditOfSubLedger-total_debitOfSubLedger}"/></td>
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

																						<%-- <tr class='n-style'>
																							<td style="text-align: left;">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;${ledgerform6.ledgerName}</td>
																							<td class="tright"><fmt:formatNumber
																									type="number" minFractionDigits="2"
																									maxFractionDigits="2"
																									value="${openiBalanceOfLedger}" /></td>
																							<td></td>
																						</tr> --%>

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
																											<td class="tright"><fmt:formatNumber
																													type="number"
																													minFractionDigits="2"
																													maxFractionDigits="2"
																													value="${openiBalanceOfSubLedger}" /></td>
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
														<%-- <tr class='n-style'>
															<td style="text-align: left;">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;${ledgerform6.ledgerName}</td>
															<c:set var="openiBalanceOfLedger"
																value="${openiBalanceOfLedger + (ledgerform6.ledgercredit_balance-ledgerform6.ledgerdebit_balance)}" />
															<td class="tright"><fmt:formatNumber
																	type="number" minFractionDigits="2"
																	maxFractionDigits="2"
																	value="${openiBalanceOfLedger}" /></td>
															<td></td>
														</tr> --%>
														
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
																					<td class="tright"><fmt:formatNumber
																							type="number"
																							minFractionDigits="2"
																							maxFractionDigits="2"
																							value="${openiBalanceOfSubLedger}" /></td>
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
               
               </c:if>
          </c:forEach>
                 </c:if>
                 </c:if>
                  
               </c:if>			                        
</c:forEach>



</c:if>
</tbody>

<tfoot style='font-weight: bold'>
<c:set var="totalNetLoss" value="0"/>
<c:if test="${TotalDebitAmount>TotalCreditAmount}">
 <c:set var="creditCountforAdujustingRow" value="${creditCountforAdujustingRow + 1}" />	
  <tr>
<td><b>Net Loss</b></td>
<td></td>
<td class="tright"><b><fmt:formatNumber type="number" minFractionDigits="2"
maxFractionDigits="2" value="${TotalDebitAmount-TotalCreditAmount}" /></b>
 <c:set var="totalNetLoss" value="${totalNetLoss+(TotalDebitAmount-TotalCreditAmount)}" />
	</td>
</tr>
</c:if>	

<%--  <c:if test="${debitCountforAdujustingRow>creditCountforAdujustingRow}">

<c:if test="${(debitCountforAdujustingRow-creditCountforAdujustingRow!=0)}">

<c:forEach begin="1" end="${debitCountforAdujustingRow-creditCountforAdujustingRow}" var="i">
                        <tr>
                        <td></td>
                        <td></td>
                        <td></td>
                        </tr>
                        </c:forEach>
                        
</c:if>

</c:if> --%>

<tr>
	<td><b>Total</b></td>
	<td></td>
	<td class="tright"><b><fmt:formatNumber type="number" minFractionDigits="2"
maxFractionDigits="2" value="${TotalCreditAmount+totalNetLoss}" /></b></td>
							</tr>
						
						</tfoot>
					</table> 
				</td>
			</tr>
		</tbody>
	</table>
</div>
</td>
</tr>
</table>

<div class="row" style="text-align: center; margin: 15px;">
				<button class="fassetBtn" type="button" onclick="ok()">
					<spring:message code="btn_save" />
				</button>
				<button class="fassetBtn" type="button" onclick="cancel()">
					<spring:message code="btn_cancel" />
				</button>
</div>
</div>

<script type="text/javascript">
$(function() {
	setTimeout(function() {
		$("#successMsg").hide();
	}, 3000);
});
function cancel(){
	window.location.assign('<c:url value="yearEndAutoJVList"/>');
}
function ok(){
	window.location.assign('<c:url value="saveYearEndAutoJV"/>');
}
</script>
<%@include file="/WEB-INF/includes/footer.jsp"%>