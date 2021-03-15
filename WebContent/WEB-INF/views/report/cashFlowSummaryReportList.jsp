<%@include file="/WEB-INF/includes/header.jsp"%>
<script type="text/javascript" src="${valid}"></script>
<spring:url value="/resources/js/jspdf.min.js" var="jspdfmin" />
<spring:url value="/resources/js/jspdf.plugin.autotable.js" var="jspdfauto" />
<spring:url value="/resources/js/report_table/cashFlowSummary.js" var="tableexport" />
 <script type="text/javascript" src="http://code.jquery.com/jquery-latest.min.js"></script>
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
       tableName: 'Countries',
       worksheetName: 'Countries by population'
     };
     $.extend(true, options, params);
     $(selector).tableExport(options);
     $("#tableDiv").css("display", "none"); 	
   }
 
</script>

<div class="breadcrumb">
	<h3>Cash Flow Summary</h3>
	<a href="homePage">Home</a> » <a href="cashFlowSummaryReport">Cash Flow Summary</a>
</div>
<div class="col-md-12">

<c:set var="totalopeningBalance" value="0" />
				<c:set var="totalopeningBalanceOfBank" value="0" />
				
				<c:forEach var="bankOpbalance" items="${bankOpeningBalanceBeforeStartDate}">
							<c:set var="totalopeningBalance" value="${totalopeningBalance+(bankOpbalance.debit_balance-bankOpbalance.credit_balance)}" />	
				             <c:set var="totalopeningBalanceOfBank" value="${totalopeningBalanceOfBank+(bankOpbalance.debit_balance-bankOpbalance.credit_balance)}" />					
               </c:forEach>
               <c:forEach var="form" items="${subledgerListbeforestartDate}">
                            <c:set var="totalopeningBalance" value="${totalopeningBalance+(form.debit_balance-form.credit_balance)}" />	       
             </c:forEach>
				
					<c:set var="total_credit_Customer" value="0" />
					<c:set var="total_debit_Supplier" value="0" />
				<c:forEach var="receipt" items="${receiptList}">
				<c:if test="${receipt.customer!=null}">
					<c:set var="total_credit_Customer" value="${total_credit_Customer + (receipt.amount-receipt.tds_amount)}" />
				</c:if>
				</c:forEach>
				<c:forEach var="payment" items="${paymentList}">
				<c:if test="${payment.supplier!=null}">
					<c:set var="total_debit_Supplier" value="${total_debit_Supplier + (payment.amount)}" /><!-- REmoved -payment.tds_amount bcoz amount is equal total tran amount -tds amount -->
						</c:if>
				</c:forEach>

<!-- -------------------------EXCEL START------------------------------------------------------------------ -->		
<div style="display:none" id="excel_report">
				<!-- Date -->
					<table>
						<tr style="text-align:center;"><td></td><td></td><td><b>Cash Flow Summary</b></td></tr>					
						<tr><td colspan='4'>Company Name: ${company.company_name}</td></tr>
						<tr><td colspan='4'>Address: ${company.permenant_address}</td></tr>
						<tr><td colspan='4'>
								<fmt:parseDate value="${from_date}" pattern="yyyy-MM-dd" var="parsedDate" type="date" />
	                   			 <fmt:formatDate value="${parsedDate}" var="from_date" type="date" pattern="dd-MM-yyyy" />
	                   			 <fmt:parseDate value="${to_date}" pattern="yyyy-MM-dd" var="parsedDate" type="date" />
	                   			 <fmt:formatDate value="${parsedDate}" var="to_date" type="date" pattern="dd-MM-yyyy" />
						From ${from_date} To ${to_date}</td></tr>
						<tr><td colspan='4'>
						CIN:
						<c:if test="${company.company_statutory_type.company_statutory_id==10 || company.company_statutory_type.company_statutory_id==14}">
						 ${company.registration_no}
					    </c:if>	
						</td></tr>
					</table>
			<!-- Date -->
						<table>
							<thead>
							<tr>
					<th data-field="particulars" data-filter-control="input"
						data-sortable="true"></th>
					<th data-field="openingbalance" data-filter-control="input"
						data-sortable="true"></th>
					<th data-field="debit" data-filter-control="input"
						data-sortable="true"></th>
					<th data-field="credit" data-filter-control="input"
						data-sortable="true"></th>
				            </tr>
							</thead>
							<tbody>
							<tr class='n-style'>
<td style="text-align: left;"><h5>Opening Balance</h5></td>
<td></td>
<td></td>
<td class="tright"> <fmt:formatNumber type="number"
											minFractionDigits="2" maxFractionDigits="2"
											value="${totalopeningBalance}" /></td>
</tr>

<tr class='n-style'>
<td style="text-align: left;">Bank Accounts</td>
<td></td>
<td class="tright"> <fmt:formatNumber type="number"
											minFractionDigits="2" maxFractionDigits="2"
											value="${totalopeningBalanceOfBank}" /></td>
<td></td>

</tr>

<c:forEach var="bankOpbalance" items="${bankOpeningBalanceBeforeStartDate}">
                                    <tr class='n-style'>
														<td style="text-align: left;">${bankOpbalance.bankName}</td>
														<td class="tright"><fmt:formatNumber type="number"
																minFractionDigits="2" maxFractionDigits="2"
																value="${bankOpbalance.debit_balance-bankOpbalance.credit_balance}" />
														<td class="tright"></td>
														<td class="tright"></td>
									 </tr>
													
</c:forEach>

<c:forEach var="form" items="${subledgerListbeforestartDate}">
                                    <tr class='n-style'>
														<td style="text-align: left;">${form.subledgerName}</td>
														<td class="tright"></td>
														<td class="tright"><fmt:formatNumber type="number"
																minFractionDigits="2" maxFractionDigits="2"
																value="${form.debit_balance-form.credit_balance}" />
														<td class="tright"></td>
									 </tr>
													
</c:forEach>

<tr class='n-style'>
<td style="text-align: left;"><h5>Receipts:</h5></td>
<td></td>
<td></td>
<td></td>
</tr>

<c:set var="totalcreditOfallGroup" value="0" />
<c:if test="${subOpeningList.size()>0}">
					<c:forEach var="obalance" items="${subOpeningList}">
					<c:if test="${obalance.isReceipt==1 || obalance.accountGroupName==group1}">
					<c:set var="total_creditGroup" value="0" />
					<c:choose>
										<c:when test="${obalance.accountGroupName==group1}">
											<c:set var="total_creditGroup" value="${(obalance.totalcredit_balance+total_credit_Customer)}" />

										</c:when>
										<c:otherwise>
											<c:set var="total_creditGroup" value="${obalance.totalcredit_balance}" />

										</c:otherwise>
									</c:choose>
								<c:if test="${total_creditGroup!=0}">	
								<c:set var="totalcreditOfallGroup" value="${(totalcreditOfallGroup+total_creditGroup)}" />
								<tr class='n-style'>
									<td style="text-align: left;"><h5>${obalance.accountGroupName}</h5></td>
									<td class="tright"></td>
									<td class="tright"><fmt:formatNumber type="number"
											minFractionDigits="2" maxFractionDigits="2"
											value="${total_creditGroup}" /></td>
									<td class="tright"></td>
								</tr>
								</c:if>
								<c:if test="${obalance.accountSubGroupNameList.size()>0}">
								
								<c:forEach var="accSubGroup" items="${obalance.accountSubGroupNameList}">
									<c:set var="total_credit_Subgroup" value="0" />
									
									<c:if test="${obalance.ledgerformlist.size()>0}">
										<c:forEach var="ledgerform2"
											items="${obalance.ledgerformlist}">
											<c:if test="${ledgerform2.subgroupName==accSubGroup}">
												<c:set var="total_credit_Subgroup" value="${total_credit_Subgroup + ledgerform2.ledgercredit_balance}" />
											</c:if>
										</c:forEach>
									</c:if>
									<c:set var="total_creditSubgroup" value="0" />
									
									<c:choose>
										<c:when test="${(accSubGroup==subGroup2)}">
												<c:set var="total_creditSubgroup" value="${(total_credit_Subgroup+total_credit_Customer)}" />
										</c:when>
										<c:otherwise>
											<c:set var="total_creditSubgroup" value="${total_credit_Subgroup}" />
										</c:otherwise>
									</c:choose>
									<c:if test="${total_creditSubgroup!=0}">
									<tr class='n-style'>
											<td style="text-align: left;">${emptyString}${accSubGroup}</td>
											<td class="tright"><fmt:formatNumber type="number"
													minFractionDigits="2" maxFractionDigits="2"
													value="${total_creditSubgroup}" /></td>
											<td class="tright"></td>
											<td class="tright"></td>
										</tr>
										<c:if test="${obalance.ledgerformlist.size()>0}">
										
										<c:forEach var="ledgerform3" items="${obalance.ledgerformlist}">
												<c:if test="${ledgerform3!=null}">
												
												<c:if test="${ledgerform3.subgroupName==accSubGroup}">
												<c:set var="total_creditOfLedger" value="0" />
												
												<c:set var="total_creditOfLedger" value="${ledgerform3.ledgercredit_balance}" />
												<c:if test="${total_creditOfLedger!=0}">
															
															<tr class='n-style'>
																<td style="text-align: left;">${emptyString}${emptyString}${ledgerform3.ledgerName}</td>
																<td class="tright"><fmt:formatNumber type="number"
																		minFractionDigits="2" maxFractionDigits="2"
																		value="${total_creditOfLedger}" /></td>
																<td class="tright"></td>
											                    <td class="tright"></td>
															</tr>
												</c:if>
												</c:if>
												<c:if test="${ledgerform3.subledgerList.size()>0}">
														<c:if test="${ledgerform3.subgroupName==accSubGroup}">
															<c:forEach var="subledger"
																items="${ledgerform3.subledgerList}">
																<c:if test="${subledger!=null}">
																<c:set var="total_creditOfSubLedger" value="0" />
																<c:set var="total_creditOfSubLedger" value="${subledger.credit_balance}" />
																		
																		<c:if test="${total_creditOfSubLedger!=0}">
																		<tr class='n-style'>
																			<td style="text-align: left;">${emptyString}${emptyString}${emptyString}${subledger.subledgerName}</td>
																			<td class="tright"><fmt:formatNumber
																					type="number" minFractionDigits="2"
																					maxFractionDigits="2"
																					value="${total_creditOfSubLedger}" /> 
																			</td>
																			<td class="tright"></td>
											                                <td class="tright"></td>
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
									
									<c:if test="${(accSubGroup==subGroup2)}">
									
									<c:forEach var="customerBalance" items="${customerBalanceList}">
				                     <c:if test="${customerBalance.customer!=null}">
					                                          <tr class='n-style'>
																			<td style="text-align: left;">${emptyString}${emptyString}${emptyString}${customerBalance.customer.firm_name}</td>
																			<td class="tright"><fmt:formatNumber
																					type="number" minFractionDigits="2"
																					maxFractionDigits="2"
																					value="${customerBalance.creditBalance}" /> 
																			</td>
																			<td class="tright"></td>
											                                <td class="tright"></td>
																		</tr>
					                 </c:if>
				                     </c:forEach>
									</c:if>
								</c:forEach>
								
								</c:if>
					</c:if>
					</c:forEach>
</c:if>
<tr class='n-style'>
<td style="text-align: left;"><h5>Nett Receipts:</h5></td>
<td></td>
<td></td>
<td class="tright"><fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${totalcreditOfallGroup}" /></td>
</tr>
<tr class='n-style'>
<td style="text-align: left;"></td>
<td></td>
<td></td>
<td class="tright"><fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${totalcreditOfallGroup+totalopeningBalance}" /></td>
</tr>

<tr class='n-style'>
<td style="text-align: left;"><h5>Payments:</h5></td>
<td></td>
<td></td>
<td></td>
</tr>


<c:set var="totaldebitOfallGroup" value="0" />
<c:if test="${subOpeningList.size()>0}">
					<c:forEach var="obalance" items="${subOpeningList}">
					<c:if test="${obalance.isPayment==1 || obalance.accountGroupName==group4}">
					<c:set var="total_debitGroup" value="0" />
					<c:choose>
										<c:when test="${obalance.accountGroupName==group4}">
											<c:set var="total_debitGroup" value="${(obalance.totaldebit_balance+total_debit_Supplier)}" />

										</c:when>
										<c:otherwise>
											<c:set var="total_debitGroup" value="${obalance.totaldebit_balance}" />

										</c:otherwise>
									</c:choose>
								<c:if test="${total_debitGroup!=0}">	
								<c:set var="totaldebitOfallGroup" value="${(totaldebitOfallGroup+total_debitGroup)}" />
								<tr class='n-style'>
	 								<td style="text-align: left;"><h5>${obalance.accountGroupName}</h5></td>
									<td class="tright"></td>
									<td class="tright"><fmt:formatNumber type="number"
											minFractionDigits="2" maxFractionDigits="2"
											value="${total_debitGroup}" /></td>
									<td class="tright"></td>
								</tr>
								</c:if>
								<c:if test="${obalance.accountSubGroupNameList.size()>0}">
								
								<c:forEach var="accSubGroup" items="${obalance.accountSubGroupNameList}">
									<c:set var="total_debit_Subgroup" value="0" />
									
									<c:if test="${obalance.ledgerformlist.size()>0}">
										<c:forEach var="ledgerform4"
											items="${obalance.ledgerformlist}">
											<c:if test="${ledgerform4.subgroupName==accSubGroup}">
												<c:set var="total_debit_Subgroup"
													value="${total_debit_Subgroup + ledgerform4.ledgerdebit_balance}" />
											</c:if>
										</c:forEach>
									</c:if>
									<c:set var="total_debitSubgroup" value="0" />
									
									<c:choose>
										<c:when
											test="${(accSubGroup==subGroup5)}">

											<c:if test="${accSubGroup==subGroup5}">
												<c:set var="total_debitSubgroup" value="${(total_debit_Subgroup+total_debit_Supplier)}" />
											</c:if>
										</c:when>
										<c:otherwise>
											<c:set var="total_debitSubgroup" value="${total_debit_Subgroup}" />
										</c:otherwise>
									</c:choose>
									<c:if test="${total_debitSubgroup!=0}">
									<tr class='n-style'>
											<td style="text-align: left;">${emptyString}${accSubGroup}</td>
											<td class="tright"><fmt:formatNumber type="number"
													minFractionDigits="2" maxFractionDigits="2"
													value="${total_debitSubgroup}" /></td>
											<td class="tright"></td>
											<td class="tright"></td>
										</tr>
										<c:if test="${obalance.ledgerformlist.size()>0}">
										
										<c:forEach var="ledgerform5" items="${obalance.ledgerformlist}">
												<c:if test="${ledgerform5!=null}">
												
												<c:if test="${ledgerform5.subgroupName==accSubGroup}">
												<c:set var="total_debitOfLedger" value="0" />
												
												<c:set var="total_debitOfLedger" value="${ledgerform5.ledgerdebit_balance}" />
												<c:if test="${total_debitOfLedger!=0}">
															
															<tr class='n-style'>
																<td style="text-align: left;">${emptyString}${emptyString}${ledgerform5.ledgerName}</td>
																<td class="tright"><fmt:formatNumber type="number"
																		minFractionDigits="2" maxFractionDigits="2"
																		value="${total_debitOfLedger}" /></td>
																<td class="tright"></td>
											                    <td class="tright"></td>
															</tr>
												</c:if>
												</c:if>
												<c:if test="${ledgerform5.subledgerList.size()>0}">
														<c:if test="${ledgerform5.subgroupName==accSubGroup}">
															<c:forEach var="subledger"
																items="${ledgerform5.subledgerList}">
																<c:if test="${subledger!=null}">
																<c:set var="total_debitOfSubLedger" value="0" />
																<c:set var="total_debitOfSubLedger" value="${subledger.debit_balance}" />
																		
																		<c:if test="${total_debitOfSubLedger!=0}">
																		<tr class='n-style'>
																			<td style="text-align: left;">${emptyString}${emptyString}${emptyString}${subledger.subledgerName}</td>
																			<td class="tright"><fmt:formatNumber
																					type="number" minFractionDigits="2"
																					maxFractionDigits="2"
																					value="${total_debitOfSubLedger}" /> 
																			</td>
																			<td class="tright"></td>
											                                <td class="tright"></td>
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
									
									<c:if test="${(accSubGroup==subGroup5)}">
									
									<c:forEach var="supplierBalance" items="${supplierBalanceList}">
				                     <c:if test="${supplierBalance.supplier!=null}">
					                                          <tr class='n-style'>
																			<td style="text-align: left;">${emptyString}${emptyString}${emptyString}${supplierBalance.supplier.company_name}</td>
																			<td class="tright"><fmt:formatNumber
																					type="number" minFractionDigits="2"
																					maxFractionDigits="2"
																					value="${supplierBalance.debitBalance}" /> 
																			</td>
																			<td class="tright"></td>
											                                <td class="tright"></td>
																		</tr>
					                 </c:if>
				                     </c:forEach>
									</c:if>
									
								</c:forEach>
								
								</c:if>
					</c:if>
					</c:forEach>
</c:if>

<tr class='n-style'>
<td style="text-align: left;"><h5>Nett Payments:</h5></td>
<td></td>
<td></td>
<td class="tright"><fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${totaldebitOfallGroup}" /></td>
</tr>
<c:set var="totalclosingBalanceOfBank" value="0" />
<c:set var="openingBalanceOfCashinHand" value="0" />
<c:set var="closngBalanceOfCashinHand" value="0" />


<c:forEach var="bankOpbalance" items="${bankOpeningBalanceList}">
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

											   <c:set var="totalclosingBalanceOfBank"
													value="${totalclosingBalanceOfBank+((bankOpeningbalance)+(bankDebitbalance-bankCreditbalance))}" />
												
											
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
														 <c:set var="totalclosingBalanceOfBank"
													value="${totalclosingBalanceOfBank+openiBalanceOfbank}" />
													</c:if>

												</c:if>

											</c:forEach>

  <c:forEach var="form" items="${subledgerListbeforestartDate}">
                            <c:set var="openingBalanceOfCashinHand"
													value="${openingBalanceOfCashinHand+(form.debit_balance-form.credit_balance)}" />	       
  </c:forEach>
  
   <c:forEach var="form" items="${subledgerListBetweenstartDateandEndDate}">
                            <c:set var="closngBalanceOfCashinHand" value="${openingBalanceOfCashinHand+(form.debit_balance-form.credit_balance)}" />	       
  </c:forEach>


<tr class='n-style'>
<td style="text-align: left;"><h5>Closing Balance</h5></td>
<td></td>
<td></td>
<td class="tright"> <fmt:formatNumber type="number"
											minFractionDigits="2" maxFractionDigits="2"
											value="${totalclosingBalanceOfBank+closngBalanceOfCashinHand}" /></td>
</tr>

<tr class='n-style'>
<td style="text-align: left;">Bank Accounts</td>
<td></td>
<td class="tright"> <fmt:formatNumber type="number"
											minFractionDigits="2" maxFractionDigits="2"
											value="${totalclosingBalanceOfBank}" /></td>
<td></td>

</tr>

<c:forEach var="bankOpbalance" items="${bankOpeningBalanceList}">
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

												<c:if test="${bankOpeningbalance!=0 || bankDebitbalance!=0 || bankCreditbalance!=0}">
													<tr class='n-style'>
														<td style="text-align: left;">${bankOpbalance.bankName}</td>
														<td class="tright"><fmt:formatNumber type="number"
																minFractionDigits="2" maxFractionDigits="2"
																value="${(bankOpeningbalance)+(bankDebitbalance-bankCreditbalance)}" />
														</td>
														<td></td>
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
														<td style="text-align: left;">${bankOpbalance1.bankName}</td>
														<td class="tright"><fmt:formatNumber type="number"
																minFractionDigits="2" maxFractionDigits="2"
																value="${(openiBalanceOfbank)}" />
														</td>
														<td></td>
					                                     <td></td>
													</tr>
													</c:if>

												</c:if>

											</c:forEach>

<%-- <c:forEach var="bankOpbalance" items="${bankOpeningBalanceList}">
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

												<c:if test="${bankOpeningbalance!=0 || bankDebitbalance!=0 || bankCreditbalance!=0}">
													<tr class='n-style'>
														<td style="text-align: left;">${bankOpbalance.bankName}</td>
														<td class="tright"><fmt:formatNumber type="number"
																minFractionDigits="2" maxFractionDigits="2"
																value="${(bankOpeningbalance)+(bankDebitbalance-bankCreditbalance)}" />
														</td>
														<td></td>
					                                     <td></td>
													</tr>
												</c:if>
</c:forEach> --%>

<tr class='n-style'>
<td style="text-align: left;">Cash in Hand</td>
<td></td>
<td class="tright"> <fmt:formatNumber type="number"
											minFractionDigits="2" maxFractionDigits="2"
											value="${closngBalanceOfCashinHand}" /></td>
<td></td>

</tr>
							
					    </tbody>
					    <tr>
					<td></td>
					<td></td>
					<td></td>
					<td></td>
				        </tr>
						</table>	
							
				</div>
				
<!-- -------------------------EXCEL END------------------------------------------------------------------ -->					
				
<!-- -------------------------PDF START------------------------------------------------------------------ -->	
				<div class="table-scroll"  style="display:none;" id="tableDiv">
	
		<c:set var="rowcount" value="0" scope="page" />
		
		<c:if test="${rowcount == 0}">
		
			<table id="Hiddentable">
			
					<tr>
						<td></td>
						<td></td>
						<td style="color:blue; margin-left: 50px;">Cash Flow Summary</td>
					</tr>
			
					<tr>
						<td align="center">Company Name: </td>
						<td></td>
						<td align="center">${company.company_name}</td>
					</tr>
					<tr>
							<td align="center">Address: </td>
							<td></td>
							<td align="center">${company.permenant_address}</td>
					</tr>
					<tr>
						<td>
							
						From: 
						</td>
						<td></td>
						<td>${from_date} To ${to_date}</td>
					</tr>
					<tr>
					
					<td colspan='3'>
					CIN:
					<c:if test="${company.company_statutory_type.company_statutory_id==10 || company.company_statutory_type.company_statutory_id==14}">
					 ${company.registration_no}
				    </c:if>	
					</td>
					</tr>

			<tr>
				<th data-field="particulars" data-filter-control="input"
						data-sortable="true"></th>
					<th data-field="openingbalance" data-filter-control="input"
						data-sortable="true"></th>
					<th data-field="debit" data-filter-control="input"
						data-sortable="true"></th>
					<th data-field="credit" data-filter-control="input"
						data-sortable="true"></th>
			</tr>
</c:if>			
			<tbody>
			<tr class='n-style'>
<td style="text-align: left;"><h5>Opening Balance</h5></td>
<td></td>
<td></td>
<td class="tright"> <fmt:formatNumber type="number"
											minFractionDigits="2" maxFractionDigits="2"
											value="${totalopeningBalance}" /></td>
</tr>

<tr class='n-style'>
<td style="text-align: left;">Bank Accounts</td>
<td></td>
<td class="tright"> <fmt:formatNumber type="number"
											minFractionDigits="2" maxFractionDigits="2"
											value="${totalopeningBalanceOfBank}" /></td>
<td></td>

</tr>

<c:forEach var="bankOpbalance" items="${bankOpeningBalanceBeforeStartDate}">
                                    <tr class='n-style'>
														<td style="text-align: left;">${bankOpbalance.bankName}</td>
														<td class="tright"><fmt:formatNumber type="number"
																minFractionDigits="2" maxFractionDigits="2"
																value="${bankOpbalance.debit_balance-bankOpbalance.credit_balance}" />
														<td class="tright"></td>
														<td class="tright"></td>
									 </tr>
													
</c:forEach>

<c:forEach var="form" items="${subledgerListbeforestartDate}">
                                    <tr class='n-style'>
														<td style="text-align: left;">${form.subledgerName}</td>
														<td class="tright"></td>
														<td class="tright"><fmt:formatNumber type="number"
																minFractionDigits="2" maxFractionDigits="2"
																value="${form.debit_balance-form.credit_balance}" />
														<td class="tright"></td>
									 </tr>
													
</c:forEach>

<tr class='n-style'>
<td style="text-align: left;"><h5>Receipts:</h5></td>
<td></td>
<td></td>
<td></td>
</tr>

<c:set var="totalcreditOfallGroup" value="0" />
<c:if test="${subOpeningList.size()>0}">
					<c:forEach var="obalance" items="${subOpeningList}">
					<c:if test="${obalance.isReceipt==1 || obalance.accountGroupName==group1}">
					<c:set var="total_creditGroup" value="0" />
					<c:choose>
										<c:when test="${obalance.accountGroupName==group1}">
											<c:set var="total_creditGroup" value="${(obalance.totalcredit_balance+total_credit_Customer)}" />

										</c:when>
										<c:otherwise>
											<c:set var="total_creditGroup" value="${obalance.totalcredit_balance}" />

										</c:otherwise>
									</c:choose>
								<c:if test="${total_creditGroup!=0}">	
								<c:set var="totalcreditOfallGroup" value="${(totalcreditOfallGroup+total_creditGroup)}" />
								<tr class='n-style'>
									<td style="text-align: left;"><h5>${obalance.accountGroupName}</h5></td>
									<td class="tright"></td>
									<td class="tright"><fmt:formatNumber type="number"
											minFractionDigits="2" maxFractionDigits="2"
											value="${total_creditGroup}" /></td>
									<td class="tright"></td>
								</tr>
								</c:if>
								<c:if test="${obalance.accountSubGroupNameList.size()>0}">
								
								<c:forEach var="accSubGroup" items="${obalance.accountSubGroupNameList}">
									<c:set var="total_credit_Subgroup" value="0" />
									
									<c:if test="${obalance.ledgerformlist.size()>0}">
										<c:forEach var="ledgerform2"
											items="${obalance.ledgerformlist}">
											<c:if test="${ledgerform2.subgroupName==accSubGroup}">
												<c:set var="total_credit_Subgroup" value="${total_credit_Subgroup + ledgerform2.ledgercredit_balance}" />
											</c:if>
										</c:forEach>
									</c:if>
									<c:set var="total_creditSubgroup" value="0" />
									
									<c:choose>
										<c:when test="${(accSubGroup==subGroup2)}">
												<c:set var="total_creditSubgroup" value="${(total_credit_Subgroup+total_credit_Customer)}" />
										</c:when>
										<c:otherwise>
											<c:set var="total_creditSubgroup" value="${total_credit_Subgroup}" />
										</c:otherwise>
									</c:choose>
									<c:if test="${total_creditSubgroup!=0}">
									<tr class='n-style'>
											<td style="text-align: left;">${emptyString}${accSubGroup}</td>
											<td class="tright"><fmt:formatNumber type="number"
													minFractionDigits="2" maxFractionDigits="2"
													value="${total_creditSubgroup}" /></td>
											<td class="tright"></td>
											<td class="tright"></td>
										</tr>
										<c:if test="${obalance.ledgerformlist.size()>0}">
										
										<c:forEach var="ledgerform3" items="${obalance.ledgerformlist}">
												<c:if test="${ledgerform3!=null}">
												
												<c:if test="${ledgerform3.subgroupName==accSubGroup}">
												<c:set var="total_creditOfLedger" value="0" />
												
												<c:set var="total_creditOfLedger" value="${ledgerform3.ledgercredit_balance}" />
												<c:if test="${total_creditOfLedger!=0}">
															
															<tr class='n-style'>
																<td style="text-align: left;">${emptyString}${emptyString}${ledgerform3.ledgerName}</td>
																<td class="tright"><fmt:formatNumber type="number"
																		minFractionDigits="2" maxFractionDigits="2"
																		value="${total_creditOfLedger}" /></td>
																<td class="tright"></td>
											                    <td class="tright"></td>
															</tr>
												</c:if>
												</c:if>
												<c:if test="${ledgerform3.subledgerList.size()>0}">
														<c:if test="${ledgerform3.subgroupName==accSubGroup}">
															<c:forEach var="subledger"
																items="${ledgerform3.subledgerList}">
																<c:if test="${subledger!=null}">
																<c:set var="total_creditOfSubLedger" value="0" />
																<c:set var="total_creditOfSubLedger" value="${subledger.credit_balance}" />
																		
																		<c:if test="${total_creditOfSubLedger!=0}">
																		<tr class='n-style'>
																			<td style="text-align: left;">${emptyString}${emptyString}${emptyString}${subledger.subledgerName}</td>
																			<td class="tright"><fmt:formatNumber
																					type="number" minFractionDigits="2"
																					maxFractionDigits="2"
																					value="${total_creditOfSubLedger}" /> 
																			</td>
																			<td class="tright"></td>
											                                <td class="tright"></td>
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
									
									<c:if test="${(accSubGroup==subGroup2)}">
									
									<c:forEach var="customerBalance" items="${customerBalanceList}">
				                     <c:if test="${customerBalance.customer!=null}">
					                                          <tr class='n-style'>
																			<td style="text-align: left;">${emptyString}${emptyString}${emptyString}${customerBalance.customer.firm_name}</td>
																			<td class="tright"><fmt:formatNumber
																					type="number" minFractionDigits="2"
																					maxFractionDigits="2"
																					value="${customerBalance.creditBalance}" /> 
																			</td>
																			<td class="tright"></td>
											                                <td class="tright"></td>
																		</tr>
					                 </c:if>
				                     </c:forEach>
									</c:if>
								</c:forEach>
								
								</c:if>
					</c:if>
					</c:forEach>
</c:if>
<tr class='n-style'>
<td style="text-align: left;"><h5>Nett Receipts:</h5></td>
<td></td>
<td></td>
<td class="tright"><fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${totalcreditOfallGroup}" /></td>
</tr>
<tr class='n-style'>
<td style="text-align: left;"></td>
<td></td>
<td></td>
<td class="tright"><fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${totalcreditOfallGroup+totalopeningBalance}" /></td>
</tr>

<tr class='n-style'>
<td style="text-align: left;"><h5>Payments:</h5></td>
<td></td>
<td></td>
<td></td>
</tr>


<c:set var="totaldebitOfallGroup" value="0" />
<c:if test="${subOpeningList.size()>0}">
					<c:forEach var="obalance" items="${subOpeningList}">
					<c:if test="${obalance.isPayment==1 || obalance.accountGroupName==group4}">
					<c:set var="total_debitGroup" value="0" />
					<c:choose>
										<c:when test="${obalance.accountGroupName==group4}">
											<c:set var="total_debitGroup" value="${(obalance.totaldebit_balance+total_debit_Supplier)}" />

										</c:when>
										<c:otherwise>
											<c:set var="total_debitGroup" value="${obalance.totaldebit_balance}" />

										</c:otherwise>
									</c:choose>
								<c:if test="${total_debitGroup!=0}">	
								<c:set var="totaldebitOfallGroup" value="${(totaldebitOfallGroup+total_debitGroup)}" />
								<tr class='n-style'>
									<td style="text-align: left;"><h5>${obalance.accountGroupName}</h5></td>
									<td class="tright"></td>
									<td class="tright"><fmt:formatNumber type="number"
											minFractionDigits="2" maxFractionDigits="2"
											value="${total_debitGroup}" /></td>
									<td class="tright"></td>
								</tr>
								</c:if>
								<c:if test="${obalance.accountSubGroupNameList.size()>0}">
								
								<c:forEach var="accSubGroup" items="${obalance.accountSubGroupNameList}">
									<c:set var="total_debit_Subgroup" value="0" />
									
									<c:if test="${obalance.ledgerformlist.size()>0}">
										<c:forEach var="ledgerform4"
											items="${obalance.ledgerformlist}">
											<c:if test="${ledgerform4.subgroupName==accSubGroup}">
												<c:set var="total_debit_Subgroup"
													value="${total_debit_Subgroup + ledgerform4.ledgerdebit_balance}" />
											</c:if>
										</c:forEach>
									</c:if>
									<c:set var="total_debitSubgroup" value="0" />
									
									<c:choose>
										<c:when
											test="${(accSubGroup==subGroup5)}">

											<c:if test="${accSubGroup==subGroup5}">
												<c:set var="total_debitSubgroup" value="${(total_debit_Subgroup+total_debit_Supplier)}" />
											</c:if>
										</c:when>
										<c:otherwise>
											<c:set var="total_debitSubgroup" value="${total_debit_Subgroup}" />
										</c:otherwise>
									</c:choose>
									<c:if test="${total_debitSubgroup!=0}">
									<tr class='n-style'>
											<td style="text-align: left;">${emptyString}${accSubGroup}</td>
											<td class="tright"><fmt:formatNumber type="number"
													minFractionDigits="2" maxFractionDigits="2"
													value="${total_debitSubgroup}" /></td>
											<td class="tright"></td>
											<td class="tright"></td>
										</tr>
										<c:if test="${obalance.ledgerformlist.size()>0}">
										
										<c:forEach var="ledgerform5" items="${obalance.ledgerformlist}">
												<c:if test="${ledgerform5!=null}">
												
												<c:if test="${ledgerform5.subgroupName==accSubGroup}">
												<c:set var="total_debitOfLedger" value="0" />
												
												<c:set var="total_debitOfLedger" value="${ledgerform5.ledgerdebit_balance}" />
												<c:if test="${total_debitOfLedger!=0}">
															
															<tr class='n-style'>
																<td style="text-align: left;">${emptyString}${emptyString}${ledgerform5.ledgerName}</td>
																<td class="tright"><fmt:formatNumber type="number"
																		minFractionDigits="2" maxFractionDigits="2"
																		value="${total_debitOfLedger}" /></td>
																<td class="tright"></td>
											                    <td class="tright"></td>
															</tr>
												</c:if>
												</c:if>
												<c:if test="${ledgerform5.subledgerList.size()>0}">
														<c:if test="${ledgerform5.subgroupName==accSubGroup}">
															<c:forEach var="subledger"
																items="${ledgerform5.subledgerList}">
																<c:if test="${subledger!=null}">
																<c:set var="total_debitOfSubLedger" value="0" />
																<c:set var="total_debitOfSubLedger" value="${subledger.debit_balance}" />
																		
																		<c:if test="${total_debitOfSubLedger!=0}">
																		<tr class='n-style'>
																			<td style="text-align: left;">${emptyString}${emptyString}${emptyString}${subledger.subledgerName}</td>
																			<td class="tright"><fmt:formatNumber
																					type="number" minFractionDigits="2"
																					maxFractionDigits="2"
																					value="${total_debitOfSubLedger}" /> 
																			</td>
																			<td class="tright"></td>
											                                <td class="tright"></td>
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
									
									<c:if test="${(accSubGroup==subGroup5)}">
									
									<c:forEach var="supplierBalance" items="${supplierBalanceList}">
				                     <c:if test="${supplierBalance.supplier!=null}">
					                                          <tr class='n-style'>
																			<td style="text-align: left;">${emptyString}${emptyString}${emptyString}${supplierBalance.supplier.company_name}</td>
																			<td class="tright"><fmt:formatNumber
																					type="number" minFractionDigits="2"
																					maxFractionDigits="2"
																					value="${supplierBalance.debitBalance}" /> 
																			</td>
																			<td class="tright"></td>
											                                <td class="tright"></td>
																		</tr>
					                 </c:if>
				                     </c:forEach>
									</c:if>
									
								</c:forEach>
								
								</c:if>
					</c:if>
					</c:forEach>
</c:if>

<tr class='n-style'>
<td style="text-align: left;"><h5>Nett Payments:</h5></td>
<td></td>
<td></td>
<td class="tright"><fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${totaldebitOfallGroup}" /></td>
</tr>
<c:set var="totalclosingBalanceOfBank" value="0" />
<c:set var="openingBalanceOfCashinHand" value="0" />
<c:set var="closngBalanceOfCashinHand" value="0" />

<c:forEach var="bankOpbalance" items="${bankOpeningBalanceList}">
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

											   <c:set var="totalclosingBalanceOfBank"
													value="${totalclosingBalanceOfBank+((bankOpeningbalance)+(bankDebitbalance-bankCreditbalance))}" />
												
											
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
														 <c:set var="totalclosingBalanceOfBank"
													value="${totalclosingBalanceOfBank+openiBalanceOfbank}" />
													</c:if>

												</c:if>

											</c:forEach>

  <c:forEach var="form" items="${subledgerListbeforestartDate}">
                            <c:set var="openingBalanceOfCashinHand"
													value="${openingBalanceOfCashinHand+(form.debit_balance-form.credit_balance)}" />	       
  </c:forEach>
  
   <c:forEach var="form" items="${subledgerListBetweenstartDateandEndDate}">
                            <c:set var="closngBalanceOfCashinHand" value="${openingBalanceOfCashinHand+(form.debit_balance-form.credit_balance)}" />	       
  </c:forEach>


<tr class='n-style'>
<td style="text-align: left;"><h5>Closing Balance</h5></td>
<td></td>
<td></td>
<td class="tright"> <fmt:formatNumber type="number"
											minFractionDigits="2" maxFractionDigits="2"
											value="${totalclosingBalanceOfBank+closngBalanceOfCashinHand}" /></td>
</tr>

<tr class='n-style'>
<td style="text-align: left;">Bank Accounts</td>
<td></td>
<td class="tright"> <fmt:formatNumber type="number"
											minFractionDigits="2" maxFractionDigits="2"
											value="${totalclosingBalanceOfBank}" /></td>
<td></td>

</tr>

<c:forEach var="bankOpbalance" items="${bankOpeningBalanceList}">
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

												<c:if test="${bankOpeningbalance!=0 || bankDebitbalance!=0 || bankCreditbalance!=0}">
													<tr class='n-style'>
														<td style="text-align: left;">${bankOpbalance.bankName}</td>
														<td class="tright"><fmt:formatNumber type="number"
																minFractionDigits="2" maxFractionDigits="2"
																value="${(bankOpeningbalance)+(bankDebitbalance-bankCreditbalance)}" />
														</td>
														<td></td>
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
														<td style="text-align: left;">${bankOpbalance1.bankName}</td>
														<td class="tright"><fmt:formatNumber type="number"
																minFractionDigits="2" maxFractionDigits="2"
																value="${(openiBalanceOfbank)}" />
														</td>
														<td></td>
					                                     <td></td>
													</tr>
													</c:if>

												</c:if>

											</c:forEach>

<%-- <c:forEach var="bankOpbalance" items="${bankOpeningBalanceList}">
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

												<c:if test="${bankOpeningbalance!=0 || bankDebitbalance!=0 || bankCreditbalance!=0}">
													<tr class='n-style'>
														<td style="text-align: left;">${bankOpbalance.bankName}</td>
														<td class="tright"><fmt:formatNumber type="number"
																minFractionDigits="2" maxFractionDigits="2"
																value="${(bankOpeningbalance)+(bankDebitbalance-bankCreditbalance)}" />
														</td>
														<td></td>
					                                     <td></td>
													</tr>
												</c:if>
</c:forEach> --%>

<tr class='n-style'>
<td style="text-align: left;">Cash in Hand</td>
<td></td>
<td class="tright"> <fmt:formatNumber type="number"
											minFractionDigits="2" maxFractionDigits="2"
											value="${closngBalanceOfCashinHand}" /></td>
<td></td>

</tr>
			</tbody>
			
			<tfoot style='background-color: #eee'>
					<tr>
						<td></td>
						<td></td>
						<td></td>
						<td></td>
					</tr>
				</tfoot>
		</table>	
	</div>	
		
<!-- -------------------------PDF END------------------------------------------------------------------ -->	
		
<!-- -------------------------UI Start------------------------------------------------------------------ -->
<div class = "borderForm" >		
	<table id="table" data-toggle="table" data-search="false"
			data-escape="false" data-filter-control="true"
			data-show-export="false" data-click-to-select="true"
			data-pagination="true" data-page-size="10" data-toolbar="#toolbar"
			class="table">
			<thead>
				<tr>
					<th data-field="particulars" data-filter-control="input"
						data-sortable="true"></th>
					<th data-field="openingbalance" data-filter-control="input"
						data-sortable="true"></th>
					<th data-field="debit" data-filter-control="input"
						data-sortable="true"></th>
					<th data-field="credit" data-filter-control="input"
						data-sortable="true"></th>
				</tr>
			</thead>
			<tbody>
				
<tr class='n-style'>
<td style="text-align: left;"><h5>Opening Balance</h5></td>
<td></td>
<td></td>
<td class="tright"> <fmt:formatNumber type="number"
											minFractionDigits="2" maxFractionDigits="2"
											value="${totalopeningBalance}" /></td>
</tr>

<tr class='n-style'>
<td style="text-align: left;">Bank Accounts</td>
<td></td>
<td class="tright"> <fmt:formatNumber type="number"
											minFractionDigits="2" maxFractionDigits="2"
											value="${totalopeningBalanceOfBank}" /></td>
<td></td>

</tr>

<c:forEach var="bankOpbalance" items="${bankOpeningBalanceBeforeStartDate}">
                                    <tr class='n-style'>
														<td style="text-align: left;">${bankOpbalance.bankName}</td>
														<td class="tright"><fmt:formatNumber type="number"
																minFractionDigits="2" maxFractionDigits="2"
																value="${bankOpbalance.debit_balance-bankOpbalance.credit_balance}" />
														<td class="tright"></td>
														<td class="tright"></td>
									 </tr>
													
</c:forEach>

<c:forEach var="form" items="${subledgerListbeforestartDate}">
                                    <tr class='n-style'>
														<td style="text-align: left;">${form.subledgerName}</td>
														<td class="tright"></td>
														<td class="tright"><fmt:formatNumber type="number"
																minFractionDigits="2" maxFractionDigits="2"
																value="${form.debit_balance-form.credit_balance}" />
														<td class="tright"></td>
									 </tr>
													
</c:forEach>

<tr class='n-style'>
<td style="text-align: left;"><h5>Receipts:</h5></td>
<td></td>
<td></td>
<td></td>
</tr>

<c:set var="totalcreditOfallGroup" value="0" />
<c:if test="${subOpeningList.size()>0}">
					<c:forEach var="obalance" items="${subOpeningList}">
					<c:if test="${obalance.isReceipt==1 || obalance.accountGroupName==group1}">
					<c:set var="total_creditGroup" value="0" />
					<c:choose>
										<c:when test="${obalance.accountGroupName==group1}">
											<c:set var="total_creditGroup" value="${(obalance.totalcredit_balance+total_credit_Customer)}" />

										</c:when>
										<c:otherwise>
											<c:set var="total_creditGroup" value="${obalance.totalcredit_balance}" />

										</c:otherwise>
									</c:choose>
								<c:if test="${total_creditGroup!=0}">	
								<c:set var="totalcreditOfallGroup" value="${(totalcreditOfallGroup+total_creditGroup)}" />
								<tr class='n-style'>
									<td style="text-align: left;"><h5>${obalance.accountGroupName}</h5></td>
									<td class="tright"></td>
									<td class="tright"><fmt:formatNumber type="number"
											minFractionDigits="2" maxFractionDigits="2"
											value="${total_creditGroup}" /></td>
									<td class="tright"></td>
								</tr>
								</c:if>
								<c:if test="${obalance.accountSubGroupNameList.size()>0}">
								
								<c:forEach var="accSubGroup" items="${obalance.accountSubGroupNameList}">
									<c:set var="total_credit_Subgroup" value="0" />
									
									<c:if test="${obalance.ledgerformlist.size()>0}">
										<c:forEach var="ledgerform2"
											items="${obalance.ledgerformlist}">
											<c:if test="${ledgerform2.subgroupName==accSubGroup}">
												<c:set var="total_credit_Subgroup" value="${total_credit_Subgroup + ledgerform2.ledgercredit_balance}" />
											</c:if>
										</c:forEach>
									</c:if>
									<c:set var="total_creditSubgroup" value="0" />
									
									<c:choose>
										<c:when test="${(accSubGroup==subGroup2)}">
												<c:set var="total_creditSubgroup" value="${(total_credit_Subgroup+total_credit_Customer)}" />
										</c:when>
										<c:otherwise>
											<c:set var="total_creditSubgroup" value="${total_credit_Subgroup}" />
										</c:otherwise>
									</c:choose>
									<c:if test="${total_creditSubgroup!=0}">
									<tr class='n-style'>
											<td style="text-align: left;">${emptyString}${accSubGroup}</td>
											<td class="tright"><fmt:formatNumber type="number"
													minFractionDigits="2" maxFractionDigits="2"
													value="${total_creditSubgroup}" /></td>
											<td class="tright"></td>
											<td class="tright"></td>
										</tr>
										<c:if test="${obalance.ledgerformlist.size()>0}">
										
										<c:forEach var="ledgerform3" items="${obalance.ledgerformlist}">
												<c:if test="${ledgerform3!=null}">
												
												<c:if test="${ledgerform3.subgroupName==accSubGroup}">
												<c:set var="total_creditOfLedger" value="0" />
												
												<c:set var="total_creditOfLedger" value="${ledgerform3.ledgercredit_balance}" />
												<c:if test="${total_creditOfLedger!=0}">
															
															<tr class='n-style'>
																<td style="text-align: left;">${emptyString}${emptyString}${ledgerform3.ledgerName}</td>
																<td class="tright"><fmt:formatNumber type="number"
																		minFractionDigits="2" maxFractionDigits="2"
																		value="${total_creditOfLedger}" /></td>
																<td class="tright"></td>
											                    <td class="tright"></td>
															</tr>
												</c:if>
												</c:if>
												<c:if test="${ledgerform3.subledgerList.size()>0}">
														<c:if test="${ledgerform3.subgroupName==accSubGroup}">
															<c:forEach var="subledger"
																items="${ledgerform3.subledgerList}">
																<c:if test="${subledger!=null}">
																<c:set var="total_creditOfSubLedger" value="0" />
																<c:set var="total_creditOfSubLedger" value="${subledger.credit_balance}" />
																		
																		<c:if test="${total_creditOfSubLedger!=0}">
																		<tr class='n-style'>
																			<td style="text-align: left;">${emptyString}${emptyString}${emptyString}${subledger.subledgerName}</td>
																			<td class="tright"><fmt:formatNumber
																					type="number" minFractionDigits="2"
																					maxFractionDigits="2"
																					value="${total_creditOfSubLedger}" /> 
																			</td>
																			<td class="tright"></td>
											                                <td class="tright"></td>
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
									
									<c:if test="${(accSubGroup==subGroup2)}">
									
									<c:forEach var="customerBalance" items="${customerBalanceList}">
				                     <c:if test="${customerBalance.customer!=null}">
					                                          <tr class='n-style'>
																			<td style="text-align: left;">${emptyString}${emptyString}${emptyString}${customerBalance.customer.firm_name}</td>
																			<td class="tright"><fmt:formatNumber
																					type="number" minFractionDigits="2"
																					maxFractionDigits="2"
																					value="${customerBalance.creditBalance}" /> 
																			</td>
																			<td class="tright"></td>
											                                <td class="tright"></td>
																		</tr>
					                 </c:if>
				                     </c:forEach>
									</c:if>
								</c:forEach>
								
								</c:if>
					</c:if>
					</c:forEach>
</c:if>
<tr class='n-style'>
<td style="text-align: left;"><h5>Nett Receipts:</h5></td>
<td></td>
<td></td>
<td class="tright"><fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${totalcreditOfallGroup}" /></td>
</tr>
<tr class='n-style'>
<td style="text-align: left;"></td>
<td></td>
<td></td>
<td class="tright"><fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${totalcreditOfallGroup+totalopeningBalance}" /></td>
</tr>

<tr class='n-style'>
<td style="text-align: left;"><h5>Payments:</h5></td>
<td></td>
<td></td>
<td></td>
</tr>


<c:set var="totaldebitOfallGroup" value="0" />
<c:if test="${subOpeningList.size()>0}">
					<c:forEach var="obalance" items="${subOpeningList}">
					<c:if test="${obalance.isPayment==1 || obalance.accountGroupName==group4}">
					<c:set var="total_debitGroup" value="0" />
					<c:choose>
										<c:when test="${obalance.accountGroupName==group4}">
											<c:set var="total_debitGroup" value="${(obalance.totaldebit_balance+total_debit_Supplier)}" />

										</c:when>
										<c:otherwise>
											<c:set var="total_debitGroup" value="${obalance.totaldebit_balance}" />

										</c:otherwise>
									</c:choose>
								<c:if test="${total_debitGroup!=0}">	
								<c:set var="totaldebitOfallGroup" value="${(totaldebitOfallGroup+total_debitGroup)}" />
								<tr class='n-style'>
									<td style="text-align: left;"><h5>${obalance.accountGroupName}</h5></td>
									<td class="tright"></td>
									<td class="tright"><fmt:formatNumber type="number"
											minFractionDigits="2" maxFractionDigits="2"
											value="${total_debitGroup}" /></td>
									<td class="tright"></td>
								</tr>
								</c:if>
								<c:if test="${obalance.accountSubGroupNameList.size()>0}">
								
								<c:forEach var="accSubGroup" items="${obalance.accountSubGroupNameList}">
									<c:set var="total_debit_Subgroup" value="0" />
									
									<c:if test="${obalance.ledgerformlist.size()>0}">
										<c:forEach var="ledgerform4"
											items="${obalance.ledgerformlist}">
											<c:if test="${ledgerform4.subgroupName==accSubGroup}">
												<c:set var="total_debit_Subgroup"
													value="${total_debit_Subgroup + ledgerform4.ledgerdebit_balance}" />
											</c:if>
										</c:forEach>
									</c:if>
									<c:set var="total_debitSubgroup" value="0" />
									
									<c:choose>
										<c:when
											test="${(accSubGroup==subGroup5)}">

											<c:if test="${accSubGroup==subGroup5}">
												<c:set var="total_debitSubgroup" value="${(total_debit_Subgroup+total_debit_Supplier)}" />
											</c:if>
										</c:when>
										<c:otherwise>
											<c:set var="total_debitSubgroup" value="${total_debit_Subgroup}" />
										</c:otherwise>
									</c:choose>
									<c:if test="${total_debitSubgroup!=0}">
									<tr class='n-style'>
											<td style="text-align: left;">${emptyString}${accSubGroup}</td>
											<td class="tright"><fmt:formatNumber type="number"
													minFractionDigits="2" maxFractionDigits="2"
													value="${total_debitSubgroup}" /></td>
											<td class="tright"></td>
											<td class="tright"></td>
										</tr>
										<c:if test="${obalance.ledgerformlist.size()>0}">
										
										<c:forEach var="ledgerform5" items="${obalance.ledgerformlist}">
												<c:if test="${ledgerform5!=null}">
												
												<c:if test="${ledgerform5.subgroupName==accSubGroup}">
												<c:set var="total_debitOfLedger" value="0" />
												
												<c:set var="total_debitOfLedger" value="${ledgerform5.ledgerdebit_balance}" />
												<c:if test="${total_debitOfLedger!=0}">
															
															<tr class='n-style'>
																<td style="text-align: left;">${emptyString}${emptyString}${ledgerform5.ledgerName}</td>
																<td class="tright"><fmt:formatNumber type="number"
																		minFractionDigits="2" maxFractionDigits="2"
																		value="${total_debitOfLedger}" /></td>
																<td class="tright"></td>
											                    <td class="tright"></td>
															</tr>
												</c:if>
												</c:if>
												<c:if test="${ledgerform5.subledgerList.size()>0}">
														<c:if test="${ledgerform5.subgroupName==accSubGroup}">
															<c:forEach var="subledger"
																items="${ledgerform5.subledgerList}">
																<c:if test="${subledger!=null}">
																<c:set var="total_debitOfSubLedger" value="0" />
																<c:set var="total_debitOfSubLedger" value="${subledger.debit_balance}" />
																		
																		<c:if test="${total_debitOfSubLedger!=0}">
																		<tr class='n-style'>
																			<td style="text-align: left;">${emptyString}${emptyString}${emptyString}${subledger.subledgerName}</td>
																			<td class="tright"><fmt:formatNumber
																					type="number" minFractionDigits="2"
																					maxFractionDigits="2"
																					value="${total_debitOfSubLedger}" /> 
																			</td>
																			<td class="tright"></td>
											                                <td class="tright"></td>
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
									
									<c:if test="${(accSubGroup==subGroup5)}">
									
									<c:forEach var="supplierBalance" items="${supplierBalanceList}">
				                     <c:if test="${supplierBalance.supplier!=null}">
					                                          <tr class='n-style'>
																			<td style="text-align: left;">${emptyString}${emptyString}${emptyString}${supplierBalance.supplier.company_name}</td>
																			<td class="tright"><fmt:formatNumber
																					type="number" minFractionDigits="2"
																					maxFractionDigits="2"
																					value="${supplierBalance.debitBalance}" /> 
																			</td>
																			<td class="tright"></td>
											                                <td class="tright"></td>
																		</tr>
					                 </c:if>
				                     </c:forEach>
									</c:if>
									
								</c:forEach>
								
								</c:if>
					</c:if>
					</c:forEach>
</c:if>

<tr class='n-style'>
<td style="text-align: left;"><h5>Nett Payments:</h5></td>
<td></td>
<td></td>
<td class="tright"><fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${totaldebitOfallGroup}" /></td>
</tr>
<c:set var="totalclosingBalanceOfBank" value="0" />
<c:set var="openingBalanceOfCashinHand" value="0" />
<c:set var="closngBalanceOfCashinHand" value="0" />

<c:forEach var="bankOpbalance" items="${bankOpeningBalanceList}">
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

											   <c:set var="totalclosingBalanceOfBank"
													value="${totalclosingBalanceOfBank+((bankOpeningbalance)+(bankDebitbalance-bankCreditbalance))}" />
												
											
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
														 <c:set var="totalclosingBalanceOfBank"
													value="${totalclosingBalanceOfBank+openiBalanceOfbank}" />
													</c:if>

												</c:if>

											</c:forEach>

  <c:forEach var="form" items="${subledgerListbeforestartDate}">
                            <c:set var="openingBalanceOfCashinHand"
													value="${openingBalanceOfCashinHand+(form.debit_balance-form.credit_balance)}" />	       
  </c:forEach>
  
   <c:forEach var="form" items="${subledgerListBetweenstartDateandEndDate}">
                            <c:set var="closngBalanceOfCashinHand" value="${openingBalanceOfCashinHand+(form.debit_balance-form.credit_balance)}" />	       
  </c:forEach>


<tr class='n-style'>
<td style="text-align: left;"><h5>Closing Balance</h5></td>
<td></td>
<td></td>
<td class="tright"> <fmt:formatNumber type="number"
											minFractionDigits="2" maxFractionDigits="2"
											value="${totalclosingBalanceOfBank+closngBalanceOfCashinHand}" /></td>
</tr>

<tr class='n-style'>
<td style="text-align: left;">Bank Accounts</td>
<td></td>
<td class="tright"> <fmt:formatNumber type="number"
											minFractionDigits="2" maxFractionDigits="2"
											value="${totalclosingBalanceOfBank}" /></td>
<td></td>

</tr>

<c:forEach var="bankOpbalance" items="${bankOpeningBalanceList}">
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

												<c:if test="${bankOpeningbalance!=0 || bankDebitbalance!=0 || bankCreditbalance!=0}">
													<tr class='n-style'>
														<td style="text-align: left;">${bankOpbalance.bankName}</td>
														<td class="tright"><fmt:formatNumber type="number"
																minFractionDigits="2" maxFractionDigits="2"
																value="${(bankOpeningbalance)+(bankDebitbalance-bankCreditbalance)}" />
														</td>
														<td></td>
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
														<td style="text-align: left;">${bankOpbalance1.bankName}</td>
														<td class="tright"><fmt:formatNumber type="number"
																minFractionDigits="2" maxFractionDigits="2"
																value="${(openiBalanceOfbank)}" />
														</td>
														<td></td>
					                                     <td></td>
													</tr>
													</c:if>

												</c:if>

											</c:forEach>

<tr class='n-style'>
<td style="text-align: left;">Cash in Hand</td>
<td></td>
<td class="tright"> <fmt:formatNumber type="number"
											minFractionDigits="2" maxFractionDigits="2"
											value="${closngBalanceOfCashinHand}" /></td>
<td></td>

</tr>
			</tbody>
			<tfoot style='background-color: #eee'>
				<tr>
					<td></td>
					<td></td>
					<td></td>
					<td></td>
				</tr>
			</tfoot>
		</table>
		
		<div class="row text-center-btn">
		     <c:if test="${role!=7}">
			<button class="fassetBtn" type="button"  onclick ="pdf('#Hiddentable', {type: 'pdf',
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
			<button class="fassetBtn" type="button" id='btnExport3' onclick = 'exportexcel("CashFlow-Report")'>
				Download as Excel
			</button>
			</c:if>
		 	<button class="fassetBtn" type="button" onclick = "back();">
				<spring:message code="btn_back" />
			</button>
		</div>
		
		</div>
</div>
<script type="text/javascript">
	$(function() {
		setTimeout(function() {
			$("#successMsg").hide();
		}, 3000);
	});
	function back() {
		window.location.assign('<c:url value = "cashFlowSummaryReport"/>');
	}
	/* function pdf() {
		window.location.assign('<c:url value = "pdfcashFlowSummaryReport"/>');
	} */
</script>
<%@include file="/WEB-INF/includes/footer.jsp"%>