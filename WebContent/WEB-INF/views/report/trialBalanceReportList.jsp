<%@include file="/WEB-INF/includes/header.jsp"%>
<%@page import="com.fasset.controller.abstracts.MyAbstractController"%>
<spring:url value="/resources/images/edit.png" var="editImg" />
<spring:url value="/resources/images/view.png" var="viewImg" />
<script type="text/javascript" src="${valid}"></script>

<spring:url value="/resources/js/jspdf.min.js" var="jspdfmin" />
<spring:url value="/resources/js/jspdf.plugin.autotable.js" var="jspdfauto" />
<spring:url value="/resources/js/report_table/trialBalance.js" var="tableexport" />
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
			tableName : 'Countries',
			worksheetName : 'Countries by population'
		};
		$.extend(true, options, params);
		$(selector).tableExport(options);
		$("#tableDiv").css("display", "none");
	}
</script>


<div class="breadcrumb">
	<h3>Trial Balance</h3>
	<a href="homePage">Home</a> » <a href="trialBalanceReport">Trial Balance</a>
</div>
<div class="col-md-12">
	<c:if test="${successMsg != null}">
		<div class="successMsg" id="successMsg">
			<strong>${successMsg}</strong>
		</div>
	</c:if>
	<!-- Excel Start -->
	<div style="display: none" id="excel_report">
		<!-- Date -->
		<table>
			<tr style="text-align: center;">
				<td></td>
				<td></td>
				<td><b>Trial Balance</b></td>
			</tr>
			<tr></tr>
			<tr>
				<td colspan='5'>Company Name: ${company.company_name}</td>
			</tr>
			<tr>
				<td colspan='5'>Address: ${company.permenant_address}</td>
			</tr>
			<tr>
				<td colspan='5'><fmt:parseDate value="${from_date}"
						pattern="yyyy-MM-dd" var="parsedDate" type="date" /> <fmt:formatDate
						value="${parsedDate}" var="from_date" type="date"
						pattern="dd-MM-yyyy" /> <fmt:parseDate value="${to_date}"
						pattern="yyyy-MM-dd" var="parsedDate" type="date" /> <fmt:formatDate
						value="${parsedDate}" var="to_date" type="date"
						pattern="dd-MM-yyyy" /> From ${from_date} To ${to_date}</td>
			</tr>
			<tr>
				<td colspan='5'>CIN: <c:if
						test="${company.company_statutory_type.company_statutory_id==10 || company.company_statutory_type.company_statutory_id==14}">
						 ${company.registration_no}
					    </c:if>
				</td>
			</tr>
		</table>

		<table>
			<thead>
				<tr>
					<th data-field="particulars" data-filter-control="input"
						data-sortable="true">Particulars</th>
					<th data-field="openingbalance" data-filter-control="input"
						data-sortable="true">Opening Balance</th>
					<th data-field="debit" data-filter-control="input"
						data-sortable="true">Debit</th>
					<th data-field="credit" data-filter-control="input"
						data-sortable="true">Credit</th>
					<th data-field="closingbalance" data-filter-control="input"
						data-sortable="true">Closing Balance</th>
				</tr>
			</thead>
			<tbody>
				<c:set var="row_count_credit" value="0" />
				<c:set var="row_count_debit" value="0" />

				<c:set var="total_credit_Supplier_beforeStartDate" value="0" />
				<c:set var="total_debit_Supplier_beforeStartDate" value="0" />
				<c:set var="Asset_total" value="0" />
				<c:set var="Liability_total" value="0" />

				<c:set var="Income_total" value="0" />
				<c:set var="Expenses_total" value="0" />
				<c:set var="Asset_CB_total" value="0" />
				<c:set var="Liability_CB_total" value="0" />

				<c:set var="Income_CB_total" value="0" />
				<c:set var="Expenses_CB_total" value="0" />

<c:set var="grandtotal_credit_group" value="0" />
				<c:set var="grandtotal_debit_group" value="0" />
				<c:forEach var="obalance" items="${supplierOpeningBalanceBeforeStartDate}">
					<c:set var="total_debit_Supplier_beforeStartDate" value="${total_debit_Supplier_beforeStartDate + obalance.debit_balance}" />
					<c:set var="total_credit_Supplier_beforeStartDate" value="${total_credit_Supplier_beforeStartDate + obalance.credit_balance}" />
				</c:forEach>

				<c:set var="total_credit_Supplier" value="0" />
				<c:set var="total_debit_Supplier" value="0" />

				<c:forEach var="obalance" items="${supplierOpeningBalanceList}">
					<c:set var="total_debit_Supplier"
						value="${total_debit_Supplier + obalance.debit_balance}" />
					<c:set var="total_credit_Supplier"
						value="${total_credit_Supplier + obalance.credit_balance}" />
				</c:forEach>

				<c:set var="total_credit_Customer_beforeStartDate" value="0" />
				<c:set var="total_debit_Customer_beforeStartDate" value="0" />


				<c:forEach var="obalance"
					items="${customerOpeningBalanceBeforeStartDate}">
					<c:set var="total_debit_Customer_beforeStartDate"
						value="${total_debit_Customer_beforeStartDate + obalance.debit_balance}" />
					<c:set var="total_credit_Customer_beforeStartDate"
						value="${total_credit_Customer_beforeStartDate + obalance.credit_balance}" />
				</c:forEach>

				<c:set var="total_credit_Customer" value="0" />
				<c:set var="total_debit_Customer" value="0" />

				<c:forEach var="obalance" items="${customerOpeningBalanceList}">
					<c:set var="total_debit_Customer"
						value="${total_debit_Customer + obalance.debit_balance}" />
					<c:set var="total_credit_Customer"
						value="${total_credit_Customer + obalance.credit_balance}" />
				</c:forEach>


				<c:set var="total_credit_Bank_beforeStartDate" value="0" />
				<c:set var="total_debit_Bank_beforeStartDate" value="0" />

				<c:forEach var="obalance"
					items="${bankOpeningBalanceBeforeStartDate}">
					<c:set var="total_debit_Bank_beforeStartDate"
						value="${total_debit_Bank_beforeStartDate + obalance.debit_balance}" />
					<c:set var="total_credit_Bank_beforeStartDate"
						value="${total_credit_Bank_beforeStartDate + obalance.credit_balance}" />
				</c:forEach>

				<c:set var="total_credit_Bank" value="0" />
				<c:set var="total_debit_Bank" value="0" />

				<c:forEach var="obalance" items="${bankOpeningBalanceList}">
					<c:set var="total_debit_Bank"
						value="${total_debit_Bank + obalance.debit_balance}" />
					<c:set var="total_credit_Bank"
						value="${total_credit_Bank + obalance.credit_balance}" />
				</c:forEach>

				<c:if test="${subOpeningList.size()>0}">

					<c:forEach var="obalance" items="${subOpeningList}">
						<!--  start of group -->
						<c:if test="${obalance.group.postingSide.posting_id == 3}">

							<c:set var="openingBalanceforGroup" value="0" />
							<c:set var="total_debitGroup" value="0" />
							<c:set var="total_creditGroup" value="0" />

							<c:forEach var="obalanceBeStartDate"
								items="${subOpeningListBeforeStartDate}">
								<c:if
									test="${obalanceBeStartDate.accountGroupName==obalance.accountGroupName}">
									<c:choose>
										<c:when test="${obalance.accountGroupName==group1}">

											<c:set var="openingBalanceforGroup"
												value="${(obalanceBeStartDate.totaldebit_balance+total_debit_Customer_beforeStartDate+total_debit_Bank_beforeStartDate)-(obalanceBeStartDate.totalcredit_balance+total_credit_Customer_beforeStartDate+total_credit_Bank_beforeStartDate)}" />
											<c:set var="total_debitGroup"
												value="${(obalance.totaldebit_balance+total_debit_Customer+total_debit_Bank)}" />
											<c:set var="total_creditGroup"
												value="${(obalance.totalcredit_balance+total_credit_Customer+total_credit_Bank)}" />

										</c:when>
										<c:otherwise>
											<c:set var="openingBalanceforGroup"
												value="${obalanceBeStartDate.totaldebit_balance-obalanceBeStartDate.totalcredit_balance}" />
											<c:set var="total_debitGroup"
												value="${obalance.totaldebit_balance}" />
											<c:set var="total_creditGroup"
												value="${obalance.totalcredit_balance}" />

										</c:otherwise>
									</c:choose>
								</c:if>
							</c:forEach>
                <c:set var="grandtotal_credit_group" value="${grandtotal_credit_group+total_creditGroup}" />
				<c:set var="grandtotal_debit_group" value="${grandtotal_debit_group+total_debitGroup}" />
					<c:set var="Asset_total" value="${Asset_total+openingBalanceforGroup}" />
					 <c:set var="CB_total" value="${(openingBalanceforGroup)+(total_debitGroup-total_creditGroup)}" />
					<c:set var="Asset_CB_total" value="${Asset_CB_total+CB_total}"/> 
							<c:if
								test="${openingBalanceforGroup!=0 || total_debitGroup!=0 || total_creditGroup!=0}">
								<tr class='n-style'>
									<td style="text-align: left;"><h5>${obalance.accountGroupName}</h5></td>
									<td class="tright"><fmt:formatNumber type="number"
											minFractionDigits="2" maxFractionDigits="2"
											value="${openingBalanceforGroup}" /></td>
									<td class="tright"><fmt:formatNumber type="number"
											minFractionDigits="2" maxFractionDigits="2"
											value="${total_debitGroup}" /></td>
									<td class="tright"><fmt:formatNumber type="number"
											minFractionDigits="2" maxFractionDigits="2"
											value="${total_creditGroup}" /></td>
									<td class="tright"><fmt:formatNumber type="number"
											minFractionDigits="2" maxFractionDigits="2"
											value="${(openingBalanceforGroup)+(total_debitGroup-total_creditGroup)}" />
									</td>
								</tr>
							</c:if>


							<!--  start of subgroup -->
							<c:if test="${obalance.accountSubGroupNameList.size()>0}">
								<c:forEach var="accSubGroup"
									items="${obalance.accountSubGroupNameList}">

									<c:set var="total_credit_Subgroup_beforeStartDate" value="0" />
									<c:set var="total_debit_Subgroup_beforeStartDate" value="0" />
									<c:set var="total_credit_Subgroup" value="0" />
									<c:set var="total_debit_Subgroup" value="0" />

									<c:forEach var="obalanceBeStartDate"
										items="${subOpeningListBeforeStartDate}">

										<c:if
											test="${obalanceBeStartDate.accountSubGroupNameList.size()>0}">

											<c:forEach var="subGroupName"
												items="${obalanceBeStartDate.accountSubGroupNameList}">

												<c:if test="${subGroupName==accSubGroup}">
													<c:if test="${obalanceBeStartDate.ledgerformlist.size()>0}">
														<c:forEach var="ledgerform1"
															items="${obalanceBeStartDate.ledgerformlist}">
															<c:if test="${ledgerform1.subgroupName==accSubGroup}">
																<c:set var="total_credit_Subgroup_beforeStartDate"
																	value="${total_credit_Subgroup_beforeStartDate + ledgerform1.ledgercredit_balance}" />
																<c:set var="total_debit_Subgroup_beforeStartDate"
																	value="${total_debit_Subgroup_beforeStartDate + ledgerform1.ledgerdebit_balance}" />
															</c:if>
														</c:forEach>
													</c:if>
												</c:if>

											</c:forEach>

										</c:if>

									</c:forEach>

									<c:if test="${obalance.ledgerformlist.size()>0}">
										<c:forEach var="ledgerform2"
											items="${obalance.ledgerformlist}">
											<c:if test="${ledgerform2.subgroupName==accSubGroup}">
												<c:set var="total_credit_Subgroup"
													value="${total_credit_Subgroup + ledgerform2.ledgercredit_balance}" />
												<c:set var="total_debit_Subgroup"
													value="${total_debit_Subgroup + ledgerform2.ledgerdebit_balance}" />
											</c:if>
										</c:forEach>
									</c:if>


									<c:set var="openingBalanceSubgroup" value="0" />
									<c:set var="total_debitSubgroup" value="0" />
									<c:set var="total_creditSubgroup" value="0" />

									<c:choose>
										<c:when
											test="${(accSubGroup==subGroup2)||(accSubGroup==subGroup3)}">

											<c:if test="${accSubGroup==subGroup2}">
												<c:set var="openingBalanceSubgroup"
													value="${(total_debit_Subgroup_beforeStartDate+total_debit_Customer_beforeStartDate)-(total_credit_Subgroup_beforeStartDate+total_credit_Customer_beforeStartDate)}" />
												<c:set var="total_debitSubgroup"
													value="${(total_debit_Subgroup+total_debit_Customer)}" />
												<c:set var="total_creditSubgroup"
													value="${(total_credit_Subgroup+total_credit_Customer)}" />
											</c:if>

											<c:if test="${accSubGroup==subGroup3}">
												<c:set var="openingBalanceSubgroup"
													value="${(total_debit_Subgroup_beforeStartDate+total_debit_Bank_beforeStartDate)-(total_credit_Subgroup_beforeStartDate+total_credit_Bank_beforeStartDate)}" />
												<c:set var="total_debitSubgroup"
													value="${(total_debit_Subgroup+total_debit_Bank)}" />
												<c:set var="total_creditSubgroup"
													value="${(total_credit_Subgroup+total_credit_Bank)}" />

											</c:if>

										</c:when>
										<c:otherwise>
											<c:set var="openingBalanceSubgroup"
												value="${total_debit_Subgroup_beforeStartDate-total_credit_Subgroup_beforeStartDate}" />
											<c:set var="total_debitSubgroup"
												value="${total_debit_Subgroup}" />
											<c:set var="total_creditSubgroup"
												value="${total_credit_Subgroup}" />
										</c:otherwise>
									</c:choose>

									<c:if
										test="${openingBalanceSubgroup!=0 || total_debitSubgroup!=0 || total_creditSubgroup!=0}">
										<tr class='n-style'>
											<td style="text-align: left;">${emptyString}${accSubGroup}</td>
											<td class="tright"><fmt:formatNumber type="number"
													minFractionDigits="2" maxFractionDigits="2"
													value="${openingBalanceSubgroup}" /></td>
											<td class="tright"><fmt:formatNumber type="number"
													minFractionDigits="2" maxFractionDigits="2"
													value="${total_debitSubgroup}" /></td>
											<td class="tright"><fmt:formatNumber type="number"
													minFractionDigits="2" maxFractionDigits="2"
													value="${total_creditSubgroup}" /></td>
											<td class="tright"><fmt:formatNumber type="number"
													minFractionDigits="2" maxFractionDigits="2"
													value="${(openingBalanceSubgroup)+(total_debitSubgroup-total_creditSubgroup)}" />
											</td>
										</tr>


										<!--  start of ledger -->
										<c:if test="${AllledgerFormList.size()>0}">
										
										
										<c:forEach var="allLedger"
												items="${AllledgerFormList}">
													<c:set var="Ispresent" value="0" />
													
										<c:if test="${obalance.ledgerformlist.size()>0}">
											<c:forEach var="ledgerform3"
												items="${obalance.ledgerformlist}">
												<c:if test="${ledgerform3!=null}">
												
<c:if
																					test="${allLedger.ledgerName==ledgerform3.ledgerName}">
																					
																					<c:if test="${Ispresent==0}">

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
																<td class="tright"><fmt:formatNumber type="number"
																		minFractionDigits="2" maxFractionDigits="2"
																		value="${openiBalanceOfLedger}" /></td>
																<td class="tright"><fmt:formatNumber type="number"
																		minFractionDigits="2" maxFractionDigits="2"
																		value="${total_debitOfLedger}" /></td>
																<td class="tright"><fmt:formatNumber type="number"
																		minFractionDigits="2" maxFractionDigits="2"
																		value="${total_creditOfLedger}" /></td>
																<td class="tright"><fmt:formatNumber type="number"
																		minFractionDigits="2" maxFractionDigits="2"
																		value="${(openiBalanceOfLedger)+(total_debitOfLedger-total_creditOfLedger)}" />
																</td>
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
																			<td class="tright"><fmt:formatNumber
																					type="number" minFractionDigits="2"
																					maxFractionDigits="2"
																					value="${openiBalanceOfSubLedger}" /></td>
																			<td class="tright"><fmt:formatNumber
																					type="number" minFractionDigits="2"
																					maxFractionDigits="2"
																					value="${total_debitOfSubLedger}" /> <c:set
																					var="row_count_debit"
																					value="${row_count_debit + total_debitOfSubLedger}" />
																			</td>
																			<td class="tright"><fmt:formatNumber
																					type="number" minFractionDigits="2"
																					maxFractionDigits="2"
																					value="${total_creditOfSubLedger}" /> <c:set
																					var="row_count_credit"
																					value="${row_count_credit + total_creditOfSubLedger}" />
																			</td>
																			<td class="tright"><fmt:formatNumber
																					type="number" minFractionDigits="2"
																					maxFractionDigits="2"
																					value="${(openiBalanceOfSubLedger)+(total_debitOfSubLedger-total_creditOfSubLedger)}" />
																			</td>
																		</tr>
																	</c:if>

																</c:if>
															</c:forEach>

														</c:if>
													</c:if>
<c:set var="Ispresent" value="1" />
</c:if>
</c:if>
												</c:if>

											</c:forEach>
										</c:if>

<c:if test="${Ispresent==0}">

										<c:forEach var="obalanceBeStartDate"
											items="${subOpeningListBeforeStartDate}">
											<c:set var="isPresent1" value="0" />
											<c:if
												test="${obalanceBeStartDate.accountSubGroupNameList.size()>0}">
												<c:forEach var="subGroupName"
													items="${obalanceBeStartDate.accountSubGroupNameList}">
													<c:if test="${subGroupName==accSubGroup}">
														<c:if
															test="${obalanceBeStartDate.ledgerformlist.size()>0}">
															
															<c:forEach var="ledgerform6"
																items="${obalanceBeStartDate.ledgerformlist}">
																<%-- <c:set var="isNotPresent" value="0" />
																<c:set var="isPresent" value="0" /> --%>
																<c:if test="${ledgerform6.subgroupName==accSubGroup}">
																	<%-- <c:if test="${obalance.ledgerformlist.size()>0}"> --%>

																		<%-- <c:forEach var="ledgerform7"
																			items="${obalance.ledgerformlist}">
																			<c:if
																				test="${ledgerform7.ledgerName==ledgerform6.ledgerName}">
																				<c:set var="isPresent" value="1" />
																			</c:if>
																		</c:forEach> --%>
																		<%-- <c:if test="${isPresent==0}"> --%>
																			<%-- <c:if test="${obalance.ledgerformlist.size()>0}"> --%>
																				<%-- <c:forEach var="ledgerform7"
																					items="${obalance.ledgerformlist}"> --%>
																					<c:if
																						test="${allLedger.ledgerName==ledgerform6.ledgerName}">
																						<c:if test="${isPresent1==0}">
																							<c:set var="openiBalanceOfLedger" value="0" />
																							<tr class='n-style'>
																								<td style="text-align: left;">${emptyString}${emptyString}${ledgerform6.ledgerName}</td>
																								<c:set var="openiBalanceOfLedger"
																									value="${openiBalanceOfLedger + (ledgerform6.ledgerdebit_balance-ledgerform6.ledgercredit_balance)}" />
																								<td class="tright"><fmt:formatNumber
																										type="number" minFractionDigits="2"
																										maxFractionDigits="2"
																										value="${openiBalanceOfLedger}" /></td>
																								<td class="tright"><fmt:formatNumber
																										type="number" minFractionDigits="2"
																										maxFractionDigits="2" value="0" /></td>
																								<td class="tright"><fmt:formatNumber
																										type="number" minFractionDigits="2"
																										maxFractionDigits="2" value="0" /></td>
																								<td class="tright"><fmt:formatNumber
																										type="number" minFractionDigits="2"
																										maxFractionDigits="2"
																										value="${openiBalanceOfLedger}" /></td>
																							</tr>
																							<c:if
																								test="${ledgerform6.subledgerList.size()>0}">


																								<c:forEach var="subledger1"
																									items="${ledgerform6.subledgerList}">
																									<c:if test="${subledger1!=null}">
																										<c:set var="openiBalanceOfSubLedger" value="0" />
																										<c:set var="openiBalanceOfSubLedger"
																											value="${openiBalanceOfSubLedger + (subledger1.debit_balance-subledger1.credit_balance)}" />
																										<c:if test="${openiBalanceOfSubLedger!=0}">
																											<tr class='n-style'>
																												<td style="text-align: left;">${emptyString}${emptyString}${emptyString}${subledger1.subledgerName}</td>
																												<td class="tright"><fmt:formatNumber
																														type="number" minFractionDigits="2"
																														maxFractionDigits="2"
																														value="${openiBalanceOfSubLedger}" /></td>
																												<td class="tright"><fmt:formatNumber
																														type="number" minFractionDigits="2"
																														maxFractionDigits="2" value="0" /></td>
																												<td class="tright"><fmt:formatNumber
																														type="number" minFractionDigits="2"
																														maxFractionDigits="2" value="0" /></td>
																												<td class="tright"><fmt:formatNumber
																														type="number" minFractionDigits="2"
																														maxFractionDigits="2"
																														value="${openiBalanceOfSubLedger}" /></td>
																											</tr>
																										</c:if>
																									</c:if>

																								</c:forEach>

																							</c:if>
																							 <c:set var="isPresent1" value="1" /> 
																						</c:if>
																					</c:if>

																				<%-- </c:forEach> --%>
																			<%-- </c:if> --%>
																	<%-- 	</c:if> --%>
																	<%-- </c:if> --%>
													<%-- 				<c:if test="${obalance.ledgerformlist.size()==0}">

																		<c:set var="openiBalanceOfLedger" value="0" />
																		<tr class='n-style'>
																			<td style="text-align: left;">${emptyString}${emptyString}${ledgerform6.ledgerName}</td>
																			<c:set var="openiBalanceOfLedger"
																				value="${openiBalanceOfLedger + (ledgerform6.ledgerdebit_balance-ledgerform6.ledgercredit_balance)}" />
																			<td class="tright"><fmt:formatNumber
																					type="number" minFractionDigits="2"
																					maxFractionDigits="2"
																					value="${openiBalanceOfLedger}" /></td>
																			<td class="tright"><fmt:formatNumber
																					type="number" minFractionDigits="2"
																					maxFractionDigits="2" value="0" /></td>
																			<td class="tright"><fmt:formatNumber
																					type="number" minFractionDigits="2"
																					maxFractionDigits="2" value="0" /></td>
																			<td class="tright"><fmt:formatNumber
																					type="number" minFractionDigits="2"
																					maxFractionDigits="2"
																					value="${openiBalanceOfLedger}" /></td>
																		</tr>
																		<c:if test="${ledgerform6.subledgerList.size()>0}">


																			<c:forEach var="subledger1"
																				items="${ledgerform6.subledgerList}">
																				<c:if test="${subledger1!=null}">
																					<c:set var="openiBalanceOfSubLedger" value="0" />
																					<c:set var="openiBalanceOfSubLedger"
																						value="${openiBalanceOfSubLedger + (subledger1.debit_balance-subledger1.credit_balance)}" />

																					<c:if test="${openiBalanceOfSubLedger!=0}">
																						<tr class='n-style'>
																							<td style="text-align: left;">${emptyString}${emptyString}${emptyString}${subledger1.subledgerName}</td>
																							<td class="tright"><fmt:formatNumber
																									type="number" minFractionDigits="2"
																									maxFractionDigits="2"
																									value="${openiBalanceOfSubLedger}" /></td>
																							<td class="tright"><fmt:formatNumber
																									type="number" minFractionDigits="2"
																									maxFractionDigits="2" value="0" /></td>
																							<td class="tright"><fmt:formatNumber
																									type="number" minFractionDigits="2"
																									maxFractionDigits="2" value="0" /></td>
																							<td class="tright"><fmt:formatNumber
																									type="number" minFractionDigits="2"
																									maxFractionDigits="2"
																									value="${openiBalanceOfSubLedger}" /></td>
																						</tr>

																					</c:if>
																				</c:if>

																			</c:forEach>

																		</c:if>


																	</c:if> --%>
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

										<c:if test="${accSubGroup==subGroup3}">
                              <c:forEach var="allbank"
												items="${Allbank}">
												<c:set var="isPresent1" value="0" />
											<c:forEach var="bankOpbalance"
												items="${bankOpeningBalanceList}">
												<c:set var="bankOpeningbalance" value="0" />
												<c:set var="bankDebitbalance" value="0" />
												<c:set var="bankCreditbalance" value="0" />
												
                                             <c:if
														test="${allbank.bankName == bankOpbalance.bankName}">
														<c:if test="${isPresent1==0}">
														
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
														<td class="tright"><fmt:formatNumber type="number"
																minFractionDigits="2" maxFractionDigits="2"
																value="${bankOpeningbalance}" />
														<td class="tright"><fmt:formatNumber type="number"
																minFractionDigits="2" maxFractionDigits="2"
																value="${bankDebitbalance}" /> <c:set
																var="row_count_debit"
																value="${row_count_debit + bankDebitbalance}" /></td>
														<td class="tright"><fmt:formatNumber type="number"
																minFractionDigits="2" maxFractionDigits="2"
																value="${bankCreditbalance}" /> <c:set
																var="row_count_credit"
																value="${row_count_credit + bankCreditbalance}" /></td>
														<td class="tright"><fmt:formatNumber type="number"
																minFractionDigits="2" maxFractionDigits="2"
																value="${(bankOpeningbalance)+(bankDebitbalance-bankCreditbalance)}" />
														</td>
													</tr>
												</c:if>
												<c:set var="isPresent1" value="1" />
												</c:if>
												</c:if>
											</c:forEach>

<c:if test="${isPresent1==0}">
											<c:forEach var="bankOpbalance1"
												items="${bankOpeningBalanceBeforeStartDate}">
												<c:set var="isBankPresent" value="0" />
												<c:set var="openiBalanceOfbank" value="0" />
												<c:set var="Ispresent" value="0" />
												<%-- <c:forEach var="bankOpbalance"
													items="${bankOpeningBalanceList}">

													<c:if
														test="${bankOpbalance1.bankName == bankOpbalance.bankName}">
														<c:set var="isBankPresent" value="1" />

													</c:if>
												</c:forEach> --%>
									<c:if
														test="${bankOpbalance1.bankName == allbank.bankName}">
												<c:if test="${Ispresent==0}">

													<c:set var="openiBalanceOfbank"
														value="${(openiBalanceOfbank)+(bankOpbalance1.debit_balance-bankOpbalance1.credit_balance)}" />
											

													<c:if test="${openiBalanceOfbank!=0}">
														<tr class='n-style'>
															<td style="text-align: left;">${emptyString}${emptyString}${bankOpbalance1.bankName}</td>
															<td class="tright"><fmt:formatNumber type="number"
																	minFractionDigits="2" maxFractionDigits="2"
																	value="${openiBalanceOfbank}" />
															<td class="tright"><fmt:formatNumber type="number"
																	minFractionDigits="2" maxFractionDigits="2" value="0" /></td>
															<td class="tright"><fmt:formatNumber type="number"
																	minFractionDigits="2" maxFractionDigits="2" value="0" /></td>
															<td class="tright"><fmt:formatNumber type="number"
																	minFractionDigits="2" maxFractionDigits="2"
																	value="${(openiBalanceOfbank)}" /></td>
														</tr>
													</c:if>
												<c:set var="Ispresent" value="1" />
												</c:if>
</c:if>
											</c:forEach>
											</c:if>
</c:forEach>

										</c:if>


										<c:if test="${accSubGroup==subGroup2}">
										
										<c:forEach var="allcust"
												items="${Allcustomer}">
												<c:set var="isPresent1" value="0" />
											<c:forEach var="cusoPbalance"
												items="${customerOpeningBalanceList}">
												<c:set var="cusOpeningbalance" value="0" />
												<c:set var="cusDebitbalance" value="0" />
												<c:set var="cusCreditbalance" value="0" />
												
												<c:if
														test="${allcust.customerName == cusoPbalance.customerName}">
														<c:if test="${isPresent1==0}">
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
														<td class="tright"><fmt:formatNumber type="number"
																minFractionDigits="2" maxFractionDigits="2"
																value="${cusOpeningbalance}" />
														<td class="tright"><fmt:formatNumber type="number"
																minFractionDigits="2" maxFractionDigits="2"
																value="${cusDebitbalance}" /> <c:set
																var="row_count_debit"
																value="${row_count_debit + cusDebitbalance}" /></td>
														<td class="tright"><fmt:formatNumber type="number"
																minFractionDigits="2" maxFractionDigits="2"
																value="${cusCreditbalance}" /> <c:set
																var="row_count_credit"
																value="${row_count_credit + cusCreditbalance}" /></td>
														<td class="tright"><fmt:formatNumber type="number"
																minFractionDigits="2" maxFractionDigits="2"
																value="${(cusOpeningbalance)+(cusDebitbalance-cusCreditbalance)}" />
														</td>
													</tr>
												</c:if>
												<c:set var="isPresent1" value="1" />
												</c:if>
</c:if>
											</c:forEach>

			<c:if test="${isPresent1==0}">
											<c:forEach var="cusoPbalance1"
												items="${customerOpeningBalanceBeforeStartDate}">
												<c:set var="iscustomerPresent" value="0" />
												<c:set var="openiBalanceOfcustomer" value="0" />
												<c:set var="Ispresent" value="0" />
												<c:if
														test="${cusoPbalance1.customerName == allcust.customerName}">
												<%-- <c:forEach var="cusoPbalance"
													items="${customerOpeningBalanceList}">

													<c:if
														test="${cusoPbalance1.customerName == cusoPbalance.customerName}">
														<c:set var="iscustomerPresent" value="1" />

													</c:if>
												</c:forEach> --%>

												<c:if test="${Ispresent==0}">

													<c:set var="openiBalanceOfcustomer"
														value="${(openiBalanceOfcustomer)+(cusoPbalance1.debit_balance-cusoPbalance1.credit_balance)}" />


													<c:if test="${openiBalanceOfcustomer!=0}">
														<tr class='n-style'>
															<td style="text-align: left;">${emptyString}${emptyString}${cusoPbalance1.customerName}</td>
															<td class="tright"><fmt:formatNumber type="number"
																	minFractionDigits="2" maxFractionDigits="2"
																	value="${openiBalanceOfcustomer}" />
															<td class="tright"><fmt:formatNumber type="number"
																	minFractionDigits="2" maxFractionDigits="2" value="0" /></td>
															<td class="tright"><fmt:formatNumber type="number"
																	minFractionDigits="2" maxFractionDigits="2" value="0" /></td>
															<td class="tright"><fmt:formatNumber type="number"
																	minFractionDigits="2" maxFractionDigits="2"
																	value="${(openiBalanceOfcustomer)}" /></td>
														</tr>
													</c:if>
											<c:set var="Ispresent" value="1" />
												</c:if>
</c:if>
											</c:forEach>
											</c:if>
											</c:forEach>
										</c:if>

									</c:if>
								</c:forEach>

							</c:if>

						</c:if>
					</c:forEach>
<tr><td><h5>Asset Total</h5></td> 
<td class="tright"><fmt:formatNumber type="number"
																	minFractionDigits="2" maxFractionDigits="2"
																	value="${(Asset_total)}" /></td>
																	
																	<td></td><td></td><td class="tright"><fmt:formatNumber type="number"
																	minFractionDigits="2" maxFractionDigits="2"
																	value="${(Asset_CB_total)}" /></td>
																	
</tr>
					<c:forEach var="obalance" items="${subOpeningList}">
						<!--  start of group -->

						<c:if test="${obalance.group.postingSide.posting_id == 4}">

							<c:set var="openingBalanceforGroup" value="0" />
							<c:set var="total_debitGroup" value="0" />
							<c:set var="total_creditGroup" value="0" />

							<c:forEach var="obalanceBeStartDate"
								items="${subOpeningListBeforeStartDate}">
								<c:if
									test="${obalanceBeStartDate.accountGroupName==obalance.accountGroupName}">
									<c:choose>
										<c:when test="${obalance.accountGroupName==group4}">

											<c:set var="openingBalanceforGroup"
												value="${(obalanceBeStartDate.totalcredit_balance+total_credit_Supplier_beforeStartDate)-(obalanceBeStartDate.totaldebit_balance+total_debit_Supplier_beforeStartDate)}" />
											<c:set var="total_debitGroup"
												value="${(obalance.totaldebit_balance+total_debit_Supplier)}" />
											<c:set var="total_creditGroup"
												value="${(obalance.totalcredit_balance+total_credit_Supplier)}" />

										</c:when>
										<c:otherwise>
											<c:set var="openingBalanceforGroup"
												value="${obalanceBeStartDate.totalcredit_balance-obalanceBeStartDate.totaldebit_balance}" />
											<c:set var="total_debitGroup"
												value="${obalance.totaldebit_balance}" />
											<c:set var="total_creditGroup"
												value="${obalance.totalcredit_balance}" />
										</c:otherwise>
									</c:choose>
								</c:if>
							</c:forEach>
                <c:set var="grandtotal_credit_group" value="${grandtotal_credit_group+total_creditGroup}" />
				<c:set var="grandtotal_debit_group" value="${grandtotal_debit_group+total_debitGroup}" />
							<c:if
								test="${openingBalanceforGroup!=0 || total_debitGroup!=0 || total_creditGroup!=0}">
								<c:set var="Liability_total" value="${Liability_total+openingBalanceforGroup}" />
								<c:set var="Liability_CB" value="${(openingBalanceforGroup)+(total_creditGroup-total_debitGroup)}" />
								<c:set var="Liability_CB_total" value="${Liability_CB_total+Liability_CB}" />
								<tr class='n-style'>
									<td style="text-align: left;"><h5>${obalance.accountGroupName}</h5></td>
									<td class="tright"><fmt:formatNumber type="number"
											minFractionDigits="2" maxFractionDigits="2"
											value="${openingBalanceforGroup}" /></td>
									<td class="tright"><fmt:formatNumber type="number"
											minFractionDigits="2" maxFractionDigits="2"
											value="${total_debitGroup}" /></td>
									<td class="tright"><fmt:formatNumber type="number"
											minFractionDigits="2" maxFractionDigits="2"
											value="${total_creditGroup}" /></td>
									<td class="tright"><fmt:formatNumber type="number"
											minFractionDigits="2" maxFractionDigits="2"
											value="${(openingBalanceforGroup)+(total_creditGroup-total_debitGroup)}" />
									</td>
								</tr>
							</c:if>


							<!--  start of subgroup -->
							<c:if test="${obalance.accountSubGroupNameList.size()>0}">
								<c:forEach var="accSubGroup"
									items="${obalance.accountSubGroupNameList}">
									
									<c:set var="total_credit_Subgroup_beforeStartDate" value="0" />
									<c:set var="total_debit_Subgroup_beforeStartDate" value="0" />
									<c:set var="total_credit_Subgroup" value="0" />
									<c:set var="total_debit_Subgroup" value="0" />
									
									
															
																
																
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
																						test="${ledgerform4.subgroupName==accSubGroup}">
															
																<c:set var="total_credit_Subgroup_beforeStartDate"
																	value="${total_credit_Subgroup_beforeStartDate + ledgerform4.ledgercredit_balance}" />
																<c:set var="total_debit_Subgroup_beforeStartDate"
																	value="${total_debit_Subgroup_beforeStartDate + ledgerform4.ledgerdebit_balance}" />
															</c:if>
														</c:forEach>
													</c:if>
												</c:if>

											</c:forEach>

										</c:if>

									</c:forEach>


									<c:if test="${obalance.ledgerformlist.size()>0}">
										<c:forEach var="ledgerform2"
											items="${obalance.ledgerformlist}">
											<c:if test="${ledgerform2.subgroupName==accSubGroup}">
												<c:set var="total_credit_Subgroup"
													value="${total_credit_Subgroup + ledgerform2.ledgercredit_balance}" />
												<c:set var="total_debit_Subgroup"
													value="${total_debit_Subgroup + ledgerform2.ledgerdebit_balance}" />
											</c:if>
										</c:forEach>
									</c:if>


									<c:set var="openingBalanceSubgroup" value="0" />
									<c:set var="total_debitSubgroup" value="0" />
									<c:set var="total_creditSubgroup" value="0" />

									<c:choose>
										<c:when test="${accSubGroup==subGroup5}">

											<c:set var="openingBalanceSubgroup"
												value="${(total_credit_Subgroup_beforeStartDate+total_credit_Supplier_beforeStartDate)-(total_debit_Subgroup_beforeStartDate+total_debit_Supplier_beforeStartDate)}" />
											<c:set var="total_debitSubgroup"
												value="${(total_debit_Subgroup+total_debit_Supplier)}" />
											<c:set var="total_creditSubgroup"
												value="${(total_credit_Subgroup+total_credit_Supplier)}" />
										</c:when>
										<c:otherwise>
											<c:set var="openingBalanceSubgroup"
												value="${total_credit_Subgroup_beforeStartDate-total_debit_Subgroup_beforeStartDate}" />
											<c:set var="total_debitSubgroup"
												value="${total_debit_Subgroup}" />
											<c:set var="total_creditSubgroup"
												value="${total_credit_Subgroup}" />
										</c:otherwise>
									</c:choose>

									<c:if
										test="${openingBalanceSubgroup!=0 || total_debitSubgroup!=0 || total_creditSubgroup!=0}">
										<tr class='n-style'>
											<td style="text-align: left;">${emptyString}${accSubGroup}</td>
											<td class="tright"><fmt:formatNumber type="number"
													minFractionDigits="2" maxFractionDigits="2"
													value="${openingBalanceSubgroup}" /></td>
											<td class="tright"><fmt:formatNumber type="number"
													minFractionDigits="2" maxFractionDigits="2"
													value="${total_debitSubgroup}" /></td>
											<td class="tright"><fmt:formatNumber type="number"
													minFractionDigits="2" maxFractionDigits="2"
													value="${total_creditSubgroup}" /></td>
											<td class="tright"><fmt:formatNumber type="number"
													minFractionDigits="2" maxFractionDigits="2"
													value="${(openingBalanceSubgroup)+(total_creditSubgroup-total_debitSubgroup)}" />
											</td>
										</tr>


										<!--  start of ledger -->
										<c:if test="${AllledgerFormList.size()>0}">
										<c:forEach var="allLedger"
												items="${AllledgerFormList}">
												<c:set var="Ispresent" value="0" />
												  
										<c:if test="${obalance.ledgerformlist.size()>0}">
											<c:forEach var="ledgerform3"
												items="${obalance.ledgerformlist}">
												<c:if test="${ledgerform3!=null}">
												
												<c:if
																					test="${allLedger.ledgerName==ledgerform3.ledgerName}">
 
                                                <c:if test="${isPresent==0}">
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
																<td class="tright"><fmt:formatNumber type="number"
																		minFractionDigits="2" maxFractionDigits="2"
																		value="${openiBalanceOfLedger}" /></td>
																<td class="tright"><fmt:formatNumber type="number"
																		minFractionDigits="2" maxFractionDigits="2"
																		value="${total_debitOfLedger}" /></td>
																<td class="tright"><fmt:formatNumber type="number"
																		minFractionDigits="2" maxFractionDigits="2"
																		value="${total_creditOfLedger}" /></td>
																<td class="tright"><fmt:formatNumber type="number"
																		minFractionDigits="2" maxFractionDigits="2"
																		value="${(openiBalanceOfLedger)+(total_creditOfLedger-total_debitOfLedger)}" />
																</td>
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
																			<td class="tright"><fmt:formatNumber
																					type="number" minFractionDigits="2"
																					maxFractionDigits="2"
																					value="${openiBalanceOfSubLedger}" /></td>
																			<td class="tright"><fmt:formatNumber
																					type="number" minFractionDigits="2"
																					maxFractionDigits="2"
																					value="${total_debitOfSubLedger}" /> <c:set
																					var="row_count_debit"
																					value="${row_count_debit + total_debitOfSubLedger}" />
																			</td>
																			<td class="tright"><fmt:formatNumber
																					type="number" minFractionDigits="2"
																					maxFractionDigits="2"
																					value="${total_creditOfSubLedger}" /> <c:set
																					var="row_count_credit"
																					value="${row_count_credit + total_creditOfSubLedger}" />
																			</td>
																			<td class="tright"><fmt:formatNumber
																					type="number" minFractionDigits="2"
																					maxFractionDigits="2"
																					value="${(openiBalanceOfSubLedger)+(total_creditOfSubLedger-total_debitOfSubLedger)}" />
																			</td>
																		</tr>
																	</c:if>

																</c:if>
															</c:forEach>

														 </c:if> 
													</c:if>
<c:set var="Ispresent" value="1" />

</c:if>
</c:if><!-- checks name -->
												</c:if>

											</c:forEach>
										</c:if>





<c:if test="${Ispresent==0}">
										<c:forEach var="obalanceBeStartDate"
											items="${subOpeningListBeforeStartDate}">
											
										<c:set var="isPresent1" value="0" />
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
																<c:if test="${ledgerform6.subgroupName==accSubGroup}">
															
																	<%-- <c:if test="${obalance.ledgerformlist.size()>0}"> --%>

																<%-- 		<c:forEach var="ledgerform7"
																			items="${obalance.ledgerformlist}">
																			<c:if
																				test="${ledgerform7.ledgerName==ledgerform6.ledgerName}">
																				<c:set var="isPresent" value="1" />
																			</c:if>
																		</c:forEach> --%>
																		<%-- <c:if test="${isPresent==0}"> --%>
																			<%-- <c:if test="${obalance.ledgerformlist.size()>0}"> --%>
																				<%-- <c:forEach var="ledgerform7"
																					items="${obalance.ledgerformlist}"> --%>
																					<c:if
																						test="${allLedger.ledgerName==ledgerform6.ledgerName}">
																						
																						<c:if test="${isPresent1==0}">
																							<c:set var="openiBalanceOfLedger" value="0" />
																							
																							<tr class='n-style'>
																								<td style="text-align: left;">${emptyString}${emptyString}${ledgerform6.ledgerName}</td>
																								<c:set var="openiBalanceOfLedger"
																									value="${openiBalanceOfLedger + (ledgerform6.ledgercredit_balance-ledgerform6.ledgerdebit_balance)}" />
																								<td class="tright"><fmt:formatNumber
																										type="number" minFractionDigits="2"
																										maxFractionDigits="2"
																										value="${openiBalanceOfLedger}" /></td>
																								<td class="tright"><fmt:formatNumber
																										type="number" minFractionDigits="2"
																										maxFractionDigits="2" value="0" /></td>
																								<td class="tright"><fmt:formatNumber
																										type="number" minFractionDigits="2"
																										maxFractionDigits="2" value="0" /></td>
																								<td class="tright"><fmt:formatNumber
																										type="number" minFractionDigits="2"
																										maxFractionDigits="2"
																										value="${openiBalanceOfLedger}" /></td>
																							</tr>
																							<c:if
																								test="${ledgerform6.subledgerList.size()>0}">


																								<c:forEach var="subledger1"
																									items="${ledgerform6.subledgerList}">
																									<c:if test="${subledger1!=null}">
																										<c:set var="openiBalanceOfSubLedger" value="0" />
																										<c:set var="openiBalanceOfSubLedger"
																											value="${openiBalanceOfSubLedger + (subledger1.credit_balance-subledger1.debit_balance)}" />

																										<c:if test="${openiBalanceOfSubLedger!=0}">
																										
																											<tr class='n-style'>
																												<td style="text-align: left;">${emptyString}${emptyString}${emptyString}${subledger1.subledgerName}</td>
																												<td class="tright"><fmt:formatNumber
																														type="number" minFractionDigits="2"
																														maxFractionDigits="2"
																														value="${openiBalanceOfSubLedger}" /></td>
																												<td class="tright"><fmt:formatNumber
																														type="number" minFractionDigits="2"
																														maxFractionDigits="2" value="0" /></td>
																												<td class="tright"><fmt:formatNumber
																														type="number" minFractionDigits="2"
																														maxFractionDigits="2" value="0" /></td>
																												<td class="tright"><fmt:formatNumber
																														type="number" minFractionDigits="2"
																														maxFractionDigits="2"
																														value="${openiBalanceOfSubLedger}" /></td>
																											</tr>

																										</c:if>
																									</c:if>

																								</c:forEach>

																							</c:if>
																							<%-- <c:set var="isNotPresent" value="1" /> --%>
																							
																							<c:set var="isPresent1" value="1" />
																						</c:if>
																					</c:if>

																				<%-- </c:forEach> --%>
																			<%-- </c:if> --%>
																		<%-- </c:if> --%>
																	<%-- </c:if> --%>
<%-- 
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
																			<td class="tright"><fmt:formatNumber
																					type="number" minFractionDigits="2"
																					maxFractionDigits="2" value="0" /></td>
																			<td class="tright"><fmt:formatNumber
																					type="number" minFractionDigits="2"
																					maxFractionDigits="2" value="0" /></td>
																			<td class="tright"><fmt:formatNumber
																					type="number" minFractionDigits="2"
																					maxFractionDigits="2"
																					value="${openiBalanceOfLedger}" /></td>
																		</tr>
																		<c:if test="${ledgerform6.subledgerList.size()>0}">


																			<c:forEach var="subledger1"
																				items="${ledgerform6.subledgerList}">
																				<c:if test="${subledger1!=null}">
																					<c:set var="openiBalanceOfSubLedger" value="0" />
																					<c:set var="openiBalanceOfSubLedger"
																						value="${openiBalanceOfSubLedger + (subledger1.credit_balance-subledger1.debit_balance)}" />

																					<c:if test="${openiBalanceOfSubLedger!=0}">
																						<tr class='n-style'>
																							<td style="text-align: left;">${emptyString}${emptyString}${emptyString}${subledger1.subledgerName}</td>
																							<td class="tright"><fmt:formatNumber
																									type="number" minFractionDigits="2"
																									maxFractionDigits="2"
																									value="${openiBalanceOfSubLedger}" /></td>
																							<td class="tright"><fmt:formatNumber
																									type="number" minFractionDigits="2"
																									maxFractionDigits="2" value="0" /></td>
																							<td class="tright"><fmt:formatNumber
																									type="number" minFractionDigits="2"
																									maxFractionDigits="2" value="0" /></td>
																							<td class="tright"><fmt:formatNumber
																									type="number" minFractionDigits="2"
																									maxFractionDigits="2"
																									value="${openiBalanceOfSubLedger}" /></td>
																						</tr>
																					</c:if>
																				</c:if>

																			</c:forEach>

																		</c:if>

																	</c:if> --%>
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

										<c:if test="${accSubGroup==subGroup5}">
										<c:forEach var="allsup"
												items="${Allsupplier}">
												<c:set var="isPresent1" value="0" />
											<c:forEach var="supOpbalance"
												items="${supplierOpeningBalanceList}">
												<c:set var="supOpeningbalance" value="0" />
												<c:set var="supDebitbalance" value="0" />
												<c:set var="supCreditbalance" value="0" />
												
												<c:if
														test="${allsup.supplierName == supOpbalance.supplierName}">
														<c:if test="${isPresent1==0}">
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
														<td class="tright"><fmt:formatNumber type="number"
																minFractionDigits="2" maxFractionDigits="2"
																value="${supOpeningbalance}" /></td>
														<td class="tright"><fmt:formatNumber type="number"
																minFractionDigits="2" maxFractionDigits="2"
																value="${supDebitbalance}" /> <c:set
																var="row_count_debit"
																value="${row_count_debit + supDebitbalance}" /></td>
														<td class="tright"><fmt:formatNumber type="number"
																minFractionDigits="2" maxFractionDigits="2"
																value="${supCreditbalance}" /> <c:set
																var="row_count_credit"
																value="${row_count_credit + supCreditbalance}" /></td>
														<td class="tright"><fmt:formatNumber type="number"
																minFractionDigits="2" maxFractionDigits="2"
																value="${(supOpeningbalance)+(supCreditbalance-supDebitbalance)}" />
													</tr>
												</c:if>
												<c:set var="isPresent1" value="1" />
												</c:if>
											</c:if>
											</c:forEach>
                                           <c:if test="${isPresent1==0}">
											<c:forEach var="supOpbalance1"
												items="${supplierOpeningBalanceBeforeStartDate}">
												<c:set var="issupplierPresent" value="0" />
												<c:set var="openiBalanceOfsupplier" value="0" />
												<c:set var="Ispresent" value="0" />
												<%-- <c:forEach var="supOpbalance"
													items="${supplierOpeningBalanceList}">

													<c:if
														test="${supOpbalance1.supplierName == supOpbalance.supplierName}">
														<c:set var="issupplierPresent" value="1" />

													</c:if>
												</c:forEach> --%>
												<c:if
														test="${supOpbalance1.supplierName == allsup.supplierName}">
												<c:if test="${Ispresent==0}">

													<c:set var="openiBalanceOfsupplier"
														value="${(openiBalanceOfsupplier)+(supOpbalance1.credit_balance-supOpbalance1.debit_balance)}" />


													<c:if test="${openiBalanceOfsupplier!=0}">
														<tr class='n-style'>
															<td style="text-align: left;">${emptyString}${emptyString}${supOpbalance1.supplierName}</td>
															<td class="tright"><fmt:formatNumber type="number"
																	minFractionDigits="2" maxFractionDigits="2"
																	value="${openiBalanceOfsupplier}" />
															<td class="tright"><fmt:formatNumber type="number"
																	minFractionDigits="2" maxFractionDigits="2" value="0" /></td>
															<td class="tright"><fmt:formatNumber type="number"
																	minFractionDigits="2" maxFractionDigits="2" value="0" /></td>
															<td class="tright"><fmt:formatNumber type="number"
																	minFractionDigits="2" maxFractionDigits="2"
																	value="${(openiBalanceOfsupplier)}" /></td>
														</tr>
													</c:if>

												</c:if>
</c:if>
											</c:forEach>
											</c:if>
											</c:forEach>
										</c:if>

									</c:if>
								</c:forEach>
							</c:if>
						</c:if>
					</c:forEach>
<tr><td><h5>Liability Total</h5></td> 
<td class="tright"><fmt:formatNumber type="number"
																	minFractionDigits="2" maxFractionDigits="2"
																	value="${(Liability_total)}" /></td>
																	<td></td><td></td>
																	
																	<td class="tright"><fmt:formatNumber type="number"
																	minFractionDigits="2" maxFractionDigits="2"
																	value="${(Liability_CB_total)}" /></td>
																	</tr>
					<c:forEach var="obalance" items="${subOpeningList}">
						<!--  start of group -->
						<c:if test="${obalance.group.postingSide.posting_id == 2}">

							<c:set var="openingBalanceforGroup" value="0" />
							<c:set var="total_debitGroup" value="0" />
							<c:set var="total_creditGroup" value="0" />

							<c:forEach var="obalanceBeStartDate"
								items="${subOpeningListBeforeStartDate}">
								<c:if
									test="${obalanceBeStartDate.accountGroupName==obalance.accountGroupName}">
									<c:set var="openingBalanceforGroup"
										value="${obalanceBeStartDate.totalcredit_balance-obalanceBeStartDate.totaldebit_balance}" />
									<c:set var="total_debitGroup"
										value="${obalance.totaldebit_balance}" />
									<c:set var="total_creditGroup"
										value="${obalance.totalcredit_balance}" />
								</c:if>
							</c:forEach>
                <c:set var="grandtotal_credit_group" value="${grandtotal_credit_group+total_creditGroup}" />
				<c:set var="grandtotal_debit_group" value="${grandtotal_debit_group+total_debitGroup}" />
					<c:if
								test="${openingBalanceforGroup!=0 || total_debitGroup!=0 || total_creditGroup!=0}">
								<c:set var="Income_total" value="${Income_total+openingBalanceforGroup}" />
								<c:set var="Liability_total" value="${Liability_total+Income_total}" />
								<c:set var="Income_CB" value="${(openingBalanceforGroup)+(total_creditGroup-total_debitGroup)}" />
								<c:set var="Income_CB_total" value="${Income_CB_total+Income_CB}" />
								<c:set var="Liability_CB_total" value="${Liability_CB_total+Income_CB_total}" />
								<tr class='n-style'>
									<td style="text-align: left;"><h5>${obalance.accountGroupName}</h5></td>
									<td class="tright"><fmt:formatNumber type="number"
											minFractionDigits="2" maxFractionDigits="2"
											value="${openingBalanceforGroup}" /></td>
									<td class="tright"><fmt:formatNumber type="number"
											minFractionDigits="2" maxFractionDigits="2"
											value="${total_debitGroup}" /></td>
									<td class="tright"><fmt:formatNumber type="number"
											minFractionDigits="2" maxFractionDigits="2"
											value="${total_creditGroup}" /></td>
									<td class="tright"><fmt:formatNumber type="number"
											minFractionDigits="2" maxFractionDigits="2"
											value="${(openingBalanceforGroup)+(total_creditGroup-total_debitGroup)}" />
									</td>
								</tr>
							</c:if>


							<!--  start of subgroup -->
							<c:if test="${obalance.accountSubGroupNameList.size()>0}">
								<c:forEach var="accSubGroup"
									items="${obalance.accountSubGroupNameList}">

									<c:set var="total_credit_Subgroup_beforeStartDate" value="0" />
									<c:set var="total_debit_Subgroup_beforeStartDate" value="0" />
									<c:set var="total_credit_Subgroup" value="0" />
									<c:set var="total_debit_Subgroup" value="0" />

									<c:forEach var="obalanceBeStartDate"
										items="${subOpeningListBeforeStartDate}">

										<c:if
											test="${obalanceBeStartDate.accountSubGroupNameList.size()>0}">

											<c:forEach var="subGroupName"
												items="${obalanceBeStartDate.accountSubGroupNameList}">

												<c:if test="${subGroupName==accSubGroup}">
													<c:if test="${obalanceBeStartDate.ledgerformlist.size()>0}">
														<c:forEach var="ledgerform1"
															items="${obalanceBeStartDate.ledgerformlist}">
															<c:if test="${ledgerform1.subgroupName==accSubGroup}">
																<c:set var="total_credit_Subgroup_beforeStartDate"
																	value="${total_credit_Subgroup_beforeStartDate + ledgerform1.ledgercredit_balance}" />
																<c:set var="total_debit_Subgroup_beforeStartDate"
																	value="${total_debit_Subgroup_beforeStartDate + ledgerform1.ledgerdebit_balance}" />
															</c:if>
														</c:forEach>
													</c:if>
												</c:if>

											</c:forEach>

										</c:if>

									</c:forEach>

									<c:if test="${obalance.ledgerformlist.size()>0}">
										<c:forEach var="ledgerform2"
											items="${obalance.ledgerformlist}">
											<c:if test="${ledgerform2.subgroupName==accSubGroup}">
												<c:set var="total_credit_Subgroup"
													value="${total_credit_Subgroup + ledgerform2.ledgercredit_balance}" />
												<c:set var="total_debit_Subgroup"
													value="${total_debit_Subgroup + ledgerform2.ledgerdebit_balance}" />
											</c:if>
										</c:forEach>
									</c:if>

									<c:set var="openingBalanceSubgroup" value="0" />
									<c:set var="total_debitSubgroup" value="0" />
									<c:set var="total_creditSubgroup" value="0" />

									<c:set var="openingBalanceSubgroup"
										value="${total_credit_Subgroup_beforeStartDate-total_debit_Subgroup_beforeStartDate}" />
									<c:set var="total_debitSubgroup"
										value="${total_debit_Subgroup}" />
									<c:set var="total_creditSubgroup"
										value="${total_credit_Subgroup}" />

									<c:if
										test="${openingBalanceSubgroup!=0 || total_debitSubgroup!=0 || total_creditSubgroup!=0}">
									
										<tr class='n-style'>
											<td style="text-align: left;">${emptyString}${accSubGroup}</td>
											<td class="tright"><fmt:formatNumber type="number"
													minFractionDigits="2" maxFractionDigits="2"
													value="${openingBalanceSubgroup}" /></td>
											<td class="tright"><fmt:formatNumber type="number"
													minFractionDigits="2" maxFractionDigits="2"
													value="${total_debitSubgroup}" /></td>
											<td class="tright"><fmt:formatNumber type="number"
													minFractionDigits="2" maxFractionDigits="2"
													value="${total_creditSubgroup}" /></td>
											<td class="tright"><fmt:formatNumber type="number"
													minFractionDigits="2" maxFractionDigits="2"
													value="${(openingBalanceSubgroup)+(total_creditSubgroup-total_debitSubgroup)}" /></td>
										</tr>


										<!--  start of ledger -->
										
										<c:if test="${AllledgerFormList.size()>0}">
										<c:forEach var="allLedger"
												items="${AllledgerFormList}">
												<c:set var="Ispresent" value="0" />
										<c:if test="${obalance.ledgerformlist.size()>0}">
											<c:forEach var="ledgerform3"
												items="${obalance.ledgerformlist}">
												<c:if test="${ledgerform3!=null}">

												<c:if
																					test="${allLedger.ledgerName==ledgerform3.ledgerName}">
 
                                                <c:if test="${isPresent==0}">

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
																<td class="tright"><fmt:formatNumber type="number"
																		minFractionDigits="2" maxFractionDigits="2"
																		value="${openiBalanceOfLedger}" /></td>
																<td class="tright"><fmt:formatNumber type="number"
																		minFractionDigits="2" maxFractionDigits="2"
																		value="${total_debitOfLedger}" /></td>
																<td class="tright"><fmt:formatNumber type="number"
																		minFractionDigits="2" maxFractionDigits="2"
																		value="${total_creditOfLedger}" /></td>
																<td class="tright"><fmt:formatNumber type="number"
																		minFractionDigits="2" maxFractionDigits="2"
																		value="${(openiBalanceOfLedger)+(total_creditOfLedger-total_debitOfLedger)}" />
																</td>
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
																			<td class="tright"><fmt:formatNumber
																					type="number" minFractionDigits="2"
																					maxFractionDigits="2"
																					value="${openiBalanceOfSubLedger}" /></td>
																			<td class="tright"><fmt:formatNumber
																					type="number" minFractionDigits="2"
																					maxFractionDigits="2"
																					value="${total_debitOfSubLedger}" /> <c:set
																					var="row_count_debit"
																					value="${row_count_debit + total_debitOfSubLedger}" />
																			</td>
																			<td class="tright"><fmt:formatNumber
																					type="number" minFractionDigits="2"
																					maxFractionDigits="2"
																					value="${total_creditOfSubLedger}" /> <c:set
																					var="row_count_credit"
																					value="${row_count_credit + total_creditOfSubLedger}" />
																			</td>
																			<td class="tright"><fmt:formatNumber
																					type="number" minFractionDigits="2"
																					maxFractionDigits="2"
																					value="${(openiBalanceOfSubLedger)+(total_creditOfSubLedger-total_debitOfSubLedger)}" />
																			</td>
																		</tr>
																	</c:if>

																</c:if>
															</c:forEach>

														</c:if>
													</c:if>
<c:set var="Ispresent" value="1" />
</c:if>
</c:if>
												</c:if>

											</c:forEach>
										</c:if>
<c:if test="${Ispresent==0}">
										<c:forEach var="obalanceBeStartDate"
											items="${subOpeningListBeforeStartDate}">
										<c:set var="isPresent1" value="0" />
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
																<c:if test="${ledgerform6.subgroupName==accSubGroup}">
																	<%-- <c:if test="${obalance.ledgerformlist.size()>0}"> --%>
<%-- 
																		<c:forEach var="ledgerform7"
																			items="${obalance.ledgerformlist}">
																			<c:if
																				test="${ledgerform7.ledgerName==ledgerform6.ledgerName}">
																				<c:set var="isPresent" value="1" />
																			</c:if>
																		</c:forEach> --%>
																		<%-- <c:if test="${isPresent==0}"> --%>
																			<%-- <c:if test="${obalance.ledgerformlist.size()>0}"> --%>
																				<%-- <c:forEach var="ledgerform7"
																					items="${obalance.ledgerformlist}"> --%>
																					<c:if
																						test="${allLedger.ledgerName==ledgerform6.ledgerName}">
																						<c:if test="${isPresent1==0}">
																							<c:set var="openiBalanceOfLedger" value="0" />
																							<tr class='n-style'>
																								<td style="text-align: left;">${emptyString}${emptyString}${ledgerform6.ledgerName}</td>
																								<c:set var="openiBalanceOfLedger"
																									value="${openiBalanceOfLedger + (ledgerform6.ledgercredit_balance-ledgerform6.ledgerdebit_balance)}" />
																									
																								<td class="tright"><fmt:formatNumber
																										type="number" minFractionDigits="2"
																										maxFractionDigits="2"
																										value="${openiBalanceOfLedger}" /></td>
																								<td class="tright"><fmt:formatNumber
																										type="number" minFractionDigits="2"
																										maxFractionDigits="2" value="0" /></td>
																								<td class="tright"><fmt:formatNumber
																										type="number" minFractionDigits="2"
																										maxFractionDigits="2" value="0" /></td>
																								<td class="tright"><fmt:formatNumber
																										type="number" minFractionDigits="2"
																										maxFractionDigits="2"
																										value="${openiBalanceOfLedger}" /></td>
																							</tr>
																							<c:if
																								test="${ledgerform6.subledgerList.size()>0}">


																								<c:forEach var="subledger1"
																									items="${ledgerform6.subledgerList}">
																									<c:if test="${subledger1!=null}">
																										<c:set var="openiBalanceOfSubLedger" value="0" />
																										<c:set var="openiBalanceOfSubLedger"
																											value="${openiBalanceOfSubLedger + (subledger1.credit_balance-subledger1.debit_balance)}" />

																										<c:if test="${openiBalanceOfSubLedger!=0}">
																										
																											<tr class='n-style'>
																												<td style="text-align: left;">${emptyString}${emptyString}${emptyString}${subledger1.subledgerName}</td>
																												<td class="tright"><fmt:formatNumber
																														type="number" minFractionDigits="2"
																														maxFractionDigits="2"
																														value="${openiBalanceOfSubLedger}" /></td>
																												<td class="tright"><fmt:formatNumber
																														type="number" minFractionDigits="2"
																														maxFractionDigits="2" value="0" /></td>
																												<td class="tright"><fmt:formatNumber
																														type="number" minFractionDigits="2"
																														maxFractionDigits="2" value="0" /></td>
																												<td class="tright"><fmt:formatNumber
																														type="number" minFractionDigits="2"
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

																				<%-- </c:forEach> --%>
																			<%-- </c:if> --%>
																		<%-- </c:if> --%>
																	<%-- </c:if> --%>
															<%-- 	//comm1	<c:if test="${obalance.ledgerformlist.size()==0}">


																		<c:set var="openiBalanceOfLedger" value="0" />
																		<tr class='n-style'>
																			<td style="text-align: left;">${emptyString}${emptyString}${ledgerform6.ledgerName}</td>
																			<c:set var="openiBalanceOfLedger"
																				value="${openiBalanceOfLedger + (ledgerform6.ledgercredit_balance-ledgerform6.ledgerdebit_balance)}" />
																				
																			<td class="tright"><fmt:formatNumber
																					type="number" minFractionDigits="2"
																					maxFractionDigits="2"
																					value="${openiBalanceOfLedger}" /></td>
																			<td class="tright"><fmt:formatNumber
																					type="number" minFractionDigits="2"
																					maxFractionDigits="2" value="0" /></td>
																			<td class="tright"><fmt:formatNumber
																					type="number" minFractionDigits="2"
																					maxFractionDigits="2" value="0" /></td>
																			<td class="tright"><fmt:formatNumber
																					type="number" minFractionDigits="2"
																					maxFractionDigits="2"
																					value="${openiBalanceOfLedger}" /></td>
																		</tr>
																		<c:if test="${ledgerform6.subledgerList.size()>0}">


																			<c:forEach var="subledger1"
																				items="${ledgerform6.subledgerList}">
																				<c:if test="${subledger1!=null}">
																					<c:set var="openiBalanceOfSubLedger" value="0" />
																					<c:set var="openiBalanceOfSubLedger"
																						value="${openiBalanceOfSubLedger + (subledger1.credit_balance-subledger1.debit_balance)}" />

																					<c:if test="${openiBalanceOfSubLedger!=0}">
																					
																						<tr class='n-style'>
																							<td style="text-align: left;">${emptyString}${emptyString}${emptyString}${subledger1.subledgerName}</td>
																							<td class="tright"><fmt:formatNumber
																									type="number" minFractionDigits="2"
																									maxFractionDigits="2"
																									value="${openiBalanceOfSubLedger}" /></td>
																							<td class="tright"><fmt:formatNumber
																									type="number" minFractionDigits="2"
																									maxFractionDigits="2" value="0" /></td>
																							<td class="tright"><fmt:formatNumber
																									type="number" minFractionDigits="2"
																									maxFractionDigits="2" value="0" /></td>
																							<td class="tright"><fmt:formatNumber
																									type="number" minFractionDigits="2"
																									maxFractionDigits="2"
																									value="${openiBalanceOfSubLedger}" /></td>
																						</tr>
																					</c:if>
																				</c:if>

																			</c:forEach>

																		</c:if>

																	</c:if> --%>
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
								</c:forEach>
							</c:if>
						</c:if>
					</c:forEach>

<tr><td><h5>Income Total</h5></td> 
<td class="tright"><fmt:formatNumber type="number"
																	minFractionDigits="2" maxFractionDigits="2"
																	value="${(Income_total)}" /></td>
																	<td ></td>
																	
																	<td></td>
																	
																	<td ></td>
																	<td class="tright"><fmt:formatNumber type="number"
																	minFractionDigits="2" maxFractionDigits="2"
																	value="${(Income_CB_total)}" /></td>
</tr>
					<c:forEach var="obalance" items="${subOpeningList}">
						<!--  start of group -->
						<c:if test="${obalance.group.postingSide.posting_id ==1}">

							<c:set var="openingBalanceforGroup" value="0" />
							<c:set var="total_debitGroup" value="0" />
							<c:set var="total_creditGroup" value="0" />

							<c:forEach var="obalanceBeStartDate"
								items="${subOpeningListBeforeStartDate}">
								<c:if
									test="${obalanceBeStartDate.accountGroupName==obalance.accountGroupName}">
									<c:set var="openingBalanceforGroup"
										value="${obalanceBeStartDate.totaldebit_balance-obalanceBeStartDate.totalcredit_balance}" />
									<c:set var="total_debitGroup"
										value="${obalance.totaldebit_balance}" />
									<c:set var="total_creditGroup"
										value="${obalance.totalcredit_balance}" />
								</c:if>
							</c:forEach>
               <c:set var="grandtotal_credit_group" value="${grandtotal_credit_group+total_creditGroup}" />
				<c:set var="grandtotal_debit_group" value="${grandtotal_debit_group+total_debitGroup}" />
						<c:if
								test="${openingBalanceforGroup!=0 || total_debitGroup!=0 || total_creditGroup!=0}">
								<c:set var="Expenses_total" value="${Expenses_total+openingBalanceforGroup}" />
									
									
									<c:set var="Expenses_CB" value="${(openingBalanceforGroup)+(total_debitGroup-total_creditGroup)}" />
									<c:set var="Expenses_CB_total" value="${Expenses_CB_total+Expenses_CB}" />
								
									
								<tr class='n-style'>
									<td style="text-align: left;"><h5>${obalance.accountGroupName}</h5></td>
									<td class="tright"><fmt:formatNumber type="number"
											minFractionDigits="2" maxFractionDigits="2"
											value="${openingBalanceforGroup}" /></td>
									<td class="tright"><fmt:formatNumber type="number"
											minFractionDigits="2" maxFractionDigits="2"
											value="${total_debitGroup}" /></td>
									<td class="tright"><fmt:formatNumber type="number"
											minFractionDigits="2" maxFractionDigits="2"
											value="${total_creditGroup}" /></td>
									<td class="tright"><fmt:formatNumber type="number"
											minFractionDigits="2" maxFractionDigits="2"
											value="${(openingBalanceforGroup)+(total_debitGroup-total_creditGroup)}" />
									</td>
								</tr>
							</c:if>


							<!--  start of subgroup -->
							<c:if test="${obalance.accountSubGroupNameList.size()>0}">
								<c:forEach var="accSubGroup"
									items="${obalance.accountSubGroupNameList}">

									<c:set var="total_credit_Subgroup_beforeStartDate" value="0" />
									<c:set var="total_debit_Subgroup_beforeStartDate" value="0" />
									<c:set var="total_credit_Subgroup" value="0" />
									<c:set var="total_debit_Subgroup" value="0" />

									<c:forEach var="obalanceBeStartDate"
										items="${subOpeningListBeforeStartDate}">

										<c:if
											test="${obalanceBeStartDate.accountSubGroupNameList.size()>0}">

											<c:forEach var="subGroupName"
												items="${obalanceBeStartDate.accountSubGroupNameList}">

												<c:if test="${subGroupName==accSubGroup}">
													<c:if test="${obalanceBeStartDate.ledgerformlist.size()>0}">
														<c:forEach var="ledgerform1"
															items="${obalanceBeStartDate.ledgerformlist}">
															<c:if test="${ledgerform1.subgroupName==accSubGroup}">
																<c:set var="total_credit_Subgroup_beforeStartDate"
																	value="${total_credit_Subgroup_beforeStartDate + ledgerform1.ledgercredit_balance}" />
																<c:set var="total_debit_Subgroup_beforeStartDate"
																	value="${total_debit_Subgroup_beforeStartDate + ledgerform1.ledgerdebit_balance}" />
															</c:if>
														</c:forEach>
													</c:if>
												</c:if>

											</c:forEach>

										</c:if>

									</c:forEach>

									<c:if test="${obalance.ledgerformlist.size()>0}">
										<c:forEach var="ledgerform2"
											items="${obalance.ledgerformlist}">
											<c:if test="${ledgerform2.subgroupName==accSubGroup}">
												<c:set var="total_credit_Subgroup"
													value="${total_credit_Subgroup + ledgerform2.ledgercredit_balance}" />
												<c:set var="total_debit_Subgroup"
													value="${total_debit_Subgroup + ledgerform2.ledgerdebit_balance}" />
											</c:if>
										</c:forEach>
									</c:if>

									<c:set var="openingBalanceSubgroup" value="0" />
									<c:set var="total_debitSubgroup" value="0" />
									<c:set var="total_creditSubgroup" value="0" />

									<c:set var="openingBalanceSubgroup"
										value="${total_debit_Subgroup_beforeStartDate-total_credit_Subgroup_beforeStartDate}" />
									<c:set var="total_debitSubgroup"
										value="${total_debit_Subgroup}" />
									<c:set var="total_creditSubgroup"
										value="${total_credit_Subgroup}" />

									<c:if
										test="${openingBalanceSubgroup!=0 || total_debitSubgroup!=0 || total_creditSubgroup!=0}">
									
										<tr class='n-style'>
											<td style="text-align: left;">${emptyString}${accSubGroup}</td>
											<td class="tright"><fmt:formatNumber type="number"
													minFractionDigits="2" maxFractionDigits="2"
													value="${openingBalanceSubgroup}" /></td>
											<td class="tright"><fmt:formatNumber type="number"
													minFractionDigits="2" maxFractionDigits="2"
													value="${total_debitSubgroup}" /></td>
											<td class="tright"><fmt:formatNumber type="number"
													minFractionDigits="2" maxFractionDigits="2"
													value="${total_creditSubgroup}" /></td>
											<td class="tright"><fmt:formatNumber type="number"
													minFractionDigits="2" maxFractionDigits="2"
													value="${(openingBalanceSubgroup)+(total_debitSubgroup-total_creditSubgroup)}" /></td>
										</tr>


										<!--  start of ledger -->
										<c:if test="${AllledgerFormList.size()>0}">
										<c:forEach var="allLedger"
												items="${AllledgerFormList}">
												<c:set var="Ispresent" value="0" />
										<c:if test="${obalance.ledgerformlist.size()>0}">
											<c:forEach var="ledgerform3"
												items="${obalance.ledgerformlist}">
												<c:if test="${ledgerform3!=null}">
<c:if
																					test="${allLedger.ledgerName==ledgerform3.ledgerName}">
 
                                                <c:if test="${isPresent==0}">

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
																<td class="tright"><fmt:formatNumber type="number"
																		minFractionDigits="2" maxFractionDigits="2"
																		value="${openiBalanceOfLedger}" /></td>
																<td class="tright"><fmt:formatNumber type="number"
																		minFractionDigits="2" maxFractionDigits="2"
																		value="${total_debitOfLedger}" /></td>
																<td class="tright"><fmt:formatNumber type="number"
																		minFractionDigits="2" maxFractionDigits="2"
																		value="${total_creditOfLedger}" /></td>
																<td class="tright"><fmt:formatNumber type="number"
																		minFractionDigits="2" maxFractionDigits="2"
																		value="${(openiBalanceOfLedger)+(total_debitOfLedger-total_creditOfLedger)}" />
																</td>
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
																			<td class="tright"><fmt:formatNumber
																					type="number" minFractionDigits="2"
																					maxFractionDigits="2"
																					value="${openiBalanceOfSubLedger}" /></td>
																			<td class="tright"><fmt:formatNumber
																					type="number" minFractionDigits="2"
																					maxFractionDigits="2"
																					value="${total_debitOfSubLedger}" /> <c:set
																					var="row_count_debit"
																					value="${row_count_debit + total_debitOfSubLedger}" />
																			</td>
																			<td class="tright"><fmt:formatNumber
																					type="number" minFractionDigits="2"
																					maxFractionDigits="2"
																					value="${total_creditOfSubLedger}" /> <c:set
																					var="row_count_credit"
																					value="${row_count_credit + total_creditOfSubLedger}" />
																			</td>
																			<td class="tright"><fmt:formatNumber
																					type="number" minFractionDigits="2"
																					maxFractionDigits="2"
																					value="${(openiBalanceOfSubLedger)+(total_debitOfSubLedger-total_creditOfSubLedger)}" />
																			</td>
																		</tr>
																	</c:if>

																</c:if>
															</c:forEach>

														</c:if>
													</c:if>
<c:set var="Ispresent" value="1" />
												</c:if>
												</c:if>
												</c:if>

											</c:forEach>
										</c:if>
<c:if test="${Ispresent==0}">
										<c:forEach var="obalanceBeStartDate"
											items="${subOpeningListBeforeStartDate}">
<c:set var="isPresent1" value="0" />
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
																<c:if test="${ledgerform6.subgroupName==accSubGroup}">
																	<%-- <c:if test="${obalance.ledgerformlist.size()>0}"> --%>

																	<%-- 	<c:forEach var="ledgerform7"
																			items="${obalance.ledgerformlist}">
																			<c:if
																				test="${ledgerform7.ledgerName==ledgerform6.ledgerName}">
																				<c:set var="isPresent" value="1" />
																			</c:if>
																		</c:forEach> --%>
																		<%-- <c:if test="${isPresent==0}"> --%>
																			<%-- <c:if test="${obalance.ledgerformlist.size()>0}"> --%>
																				<%-- <c:forEach var="ledgerform7"
																					items="${obalance.ledgerformlist}"> --%>
																					<c:if
																						test="${allLedger.ledgerName==ledgerform6.ledgerName}">
																						<c:if test="${isPresent1==0}">
																							<c:set var="openiBalanceOfLedger" value="0" />
																							<tr class='n-style'>
																								<td style="text-align: left;">${emptyString}${emptyString}${ledgerform6.ledgerName}</td>
																								<c:set var="openiBalanceOfLedger"
																									value="${openiBalanceOfLedger + (ledgerform6.ledgerdebit_balance-ledgerform6.ledgercredit_balance)}" />
																									
																								<td class="tright"><fmt:formatNumber
																										type="number" minFractionDigits="2"
																										maxFractionDigits="2"
																										value="${openiBalanceOfLedger}" /></td>
																								<td class="tright"><fmt:formatNumber
																										type="number" minFractionDigits="2"
																										maxFractionDigits="2" value="0" /></td>
																								<td class="tright"><fmt:formatNumber
																										type="number" minFractionDigits="2"
																										maxFractionDigits="2" value="0" /></td>
																								<td class="tright"><fmt:formatNumber
																										type="number" minFractionDigits="2"
																										maxFractionDigits="2"
																										value="${openiBalanceOfLedger}" /></td>
																							</tr>
																							<c:if
																								test="${ledgerform6.subledgerList.size()>0}">


																								<c:forEach var="subledger1"
																									items="${ledgerform6.subledgerList}">
																									<c:if test="${subledger1!=null}">
																										<c:set var="openiBalanceOfSubLedger" value="0" />
																										<c:set var="openiBalanceOfSubLedger"
																											value="${openiBalanceOfSubLedger + (subledger1.debit_balance-subledger1.credit_balance)}" />
																										<c:if test="${openiBalanceOfSubLedger!=0}">
																										
																											<tr class='n-style'>
																												<td style="text-align: left;">${emptyString}${emptyString}${emptyString}${subledger1.subledgerName}</td>
																												<td class="tright"><fmt:formatNumber
																														type="number" minFractionDigits="2"
																														maxFractionDigits="2"
																														value="${openiBalanceOfSubLedger}" /></td>
																												<td class="tright"><fmt:formatNumber
																														type="number" minFractionDigits="2"
																														maxFractionDigits="2" value="0" /></td>
																												<td class="tright"><fmt:formatNumber
																														type="number" minFractionDigits="2"
																														maxFractionDigits="2" value="0" /></td>
																												<td class="tright"><fmt:formatNumber
																														type="number" minFractionDigits="2"
																														maxFractionDigits="2"
																														value="${openiBalanceOfSubLedger}" /></td>
																											</tr>
																										</c:if>
																									</c:if>

																								</c:forEach>

																							</c:if>
																							<%-- <c:set var="isNotPresent" value="1" /> --%>
																							<c:set var="isPresent1" value="1" />
																						</c:if>
																					</c:if>

																				<%-- </c:forEach> --%>
																			<%-- </c:if> --%>
																		<%-- </c:if> --%>
																	<%-- </c:if> --%>
																	<%-- <c:if test="${obalance.ledgerformlist.size()==0}">

																		<c:set var="openiBalanceOfLedger" value="0" />
																		<tr class='n-style'>
																			<td style="text-align: left;">${emptyString}${emptyString}${ledgerform6.ledgerName}</td>
																			<c:set var="openiBalanceOfLedger"
																				value="${openiBalanceOfLedger + (ledgerform6.ledgerdebit_balance-ledgerform6.ledgercredit_balance)}" />
																					
																			<td class="tright"><fmt:formatNumber
																					type="number" minFractionDigits="2"
																					maxFractionDigits="2"
																					value="${openiBalanceOfLedger}" /></td>
																			<td class="tright"><fmt:formatNumber
																					type="number" minFractionDigits="2"
																					maxFractionDigits="2" value="0" /></td>
																			<td class="tright"><fmt:formatNumber
																					type="number" minFractionDigits="2"
																					maxFractionDigits="2" value="0" /></td>
																			<td class="tright"><fmt:formatNumber
																					type="number" minFractionDigits="2"
																					maxFractionDigits="2"
																					value="${openiBalanceOfLedger}" /></td>
																		</tr>
																		<c:if test="${ledgerform6.subledgerList.size()>0}">


																			<c:forEach var="subledger1"
																				items="${ledgerform6.subledgerList}">
																				<c:if test="${subledger1!=null}">
																					<c:set var="openiBalanceOfSubLedger" value="0" />
																					<c:set var="openiBalanceOfSubLedger"
																						value="${openiBalanceOfSubLedger + (subledger1.debit_balance-subledger1.credit_balance)}" />

																					<c:if test="${openiBalanceOfSubLedger!=0}">
																					
																						<tr class='n-style'>
																							<td style="text-align: left;">${emptyString}${emptyString}${emptyString}${subledger1.subledgerName}</td>
																							<td class="tright"><fmt:formatNumber
																									type="number" minFractionDigits="2"
																									maxFractionDigits="2"
																									value="${openiBalanceOfSubLedger}" /></td>
																							<td class="tright"><fmt:formatNumber
																									type="number" minFractionDigits="2"
																									maxFractionDigits="2" value="0" /></td>
																							<td class="tright"><fmt:formatNumber
																									type="number" minFractionDigits="2"
																									maxFractionDigits="2" value="0" /></td>
																							<td class="tright"><fmt:formatNumber
																									type="number" minFractionDigits="2"
																									maxFractionDigits="2"
																									value="${openiBalanceOfSubLedger}" /></td>
																						</tr>

																					</c:if>
																				</c:if>

																			</c:forEach>

																		</c:if>


																	</c:if> --%>
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
								</c:forEach>
							</c:if>
						</c:if>
					</c:forEach>
	<c:set var="Liability_CB_total" value="${Liability_CB_total - Expenses_CB_total}" />
	<c:set var="Liability_total" value="${Liability_total-Expenses_total}" />
	<tr><td><h5>Expense Total</h5></td> 
<td class="tright"><fmt:formatNumber type="number"
																	minFractionDigits="2" maxFractionDigits="2"
															value="${(Expenses_total)}" /></td>
															
															<td></td><td ></td>
															
															<td class="tright"><fmt:formatNumber type="number"
																	minFractionDigits="2" maxFractionDigits="2"
															value="${(Expenses_CB_total)}" /></td>
															
</tr>
				</c:if>

			</tbody>

			<tr>
			<tr><td> <h5> Total Liability</h5></td> <td class="tright"><fmt:formatNumber type="number"
																	minFractionDigits="2" maxFractionDigits="2"
																	value="${(Liability_total) }" /></td>
																	<td></td><td></td>
																	<td class="tright"><fmt:formatNumber type="number"
																	minFractionDigits="2" maxFractionDigits="2"
															value="${(Liability_CB_total)}" /></td>
																	</tr>
			<tr>
				<td>Total</td>
				<td></td>
				<%-- <c:forEach var="obalanceBeStartDate"
					items="${subOpeningListBeforeStartDate}">
					<c:set var="row_count_credit"
						value="${row_count_credit + obalanceBeStartDate.totalcredit_balance}" />
					<c:set var="row_count_debit"
						value="${row_count_debit + obalanceBeStartDate.totaldebit_balance}" />
				</c:forEach> --%>
				<%--<c:set var="grandtotal_credit_group" value="${grandtotal_credit_group+total_creditGroup}" />
				<c:set var="grandtotal_debit_group" value="${grandtotal_debit_group+total_debitGroup}" />
				 <td class="tright"><fmt:formatNumber type="number"
						minFractionDigits="2" maxFractionDigits="2"
						value="${row_count_debit+total_debit_Supplier_beforeStartDate+total_debit_Customer_beforeStartDate+total_debit_Bank_beforeStartDate}" /></td>
				<td class="tright"><fmt:formatNumber type="number"
						minFractionDigits="2" maxFractionDigits="2"
						value="${row_count_credit+total_credit_Supplier_beforeStartDate+total_credit_Customer_beforeStartDate+total_credit_Bank_beforeStartDate}" /></td>
				<td></td> --%>
				<td class="tright"><fmt:formatNumber type="number"
						minFractionDigits="2" maxFractionDigits="2"
						value="${grandtotal_debit_group}" /></td>
				<td class="tright"><fmt:formatNumber type="number"
						minFractionDigits="2" maxFractionDigits="2"
						value="${grandtotal_credit_group}" /></td>
				<td></td>
			</tr>

		</table>
	</div>
	<!-- Excel End -->
	<!-- ------------------------------------------------------------Excel End--------------------------------------------------------------- -->

	<!-- pdf started -->

	<div class="table-scroll" style="display: none;" id="tableDiv">

		<c:set var="rowcount" value="0" scope="page" />
		
		<c:if test="${rowcount == 0}">
		
			<table id="Hiddentable">

			<tr>
				<td></td>
				<td></td>
				<td style="color: blue; margin-left: 50px; text-align: left	; size: 2000px ;">Trial Balance</td>
			</tr>
		
			<tr>
				<td align="center">Company Name:</td>
		
				<td align="center">${company.company_name}</td>
			</tr>
			<tr>
				<td align="center">Address:</td>
		
				<td align="center">${company.permenant_address}</td>
			</tr>
			<tr>
				<td>
					<%-- <fmt:parseDate value="${from_date}" pattern="yyyy-MM-dd" var="parsedDate" type="date" />
		                   			 <fmt:formatDate value="${parsedDate}" var="from_date" type="date" pattern="dd-MM-yyyy" />
		                   			 <fmt:parseDate value="${to_date}" pattern="yyyy-MM-dd" var="parsedDate" type="date" />
		                   			 <fmt:formatDate value="${parsedDate}" var="to_date" type="date" pattern="dd-MM-yyyy" /> --%>
					From:
				</td>
		
				<td>${from_date} To  ${to_date}</td>
			</tr>
			<tr>
		
				<td colspan='3'>CIN: <c:if
						test="${company.company_statutory_type.company_statutory_id==10 || company.company_statutory_type.company_statutory_id==14}">
							 ${company.registration_no}
						    </c:if>
				</td>
			</tr>
		
			<tr>
				<th data-field="particulars" data-filter-control="input"
					data-sortable="true">Particulars</th>
				<th data-field="openingbalance" data-filter-control="input"
					data-sortable="true" style="text-align: center;">Opening Balance</th>
				<th data-field="debit" data-filter-control="input"
					data-sortable="true" style="text-align: center;">Debit</th>
				<th data-field="credit" data-filter-control="input"
					data-sortable="true" style="text-align: center;">Credit</th>
				<th data-field="closingbalance" data-filter-control="input"
					data-sortable="true" style="text-align: center;">Closing Balance</th>
			</tr>
		
		</c:if>
		<c:set var="row_count_credit" value="0" />
		<c:set var="row_count_debit" value="0" />

		<c:set var="total_credit_Supplier_beforeStartDate" value="0" />
		<c:set var="total_debit_Supplier_beforeStartDate" value="0" />
        <c:set var="grandtotal_credit_group" value="0" />
		<c:set var="grandtotal_debit_group" value="0" />
		
		<c:set var="Asset_total" value="0" />
		<c:set var="Liability_total" value="0" />
		<c:set var="Income_total" value="0" />
    	<c:set var="Expenses_total" value="0" />
				
				<c:set var="Asset_CB_total" value="0" />
				<c:set var="Liability_CB_total" value="0" />

				<c:set var="Income_CB_total" value="0" />
				<c:set var="Expenses_CB_total" value="0" />
				
		<c:forEach var="obalance"
			items="${supplierOpeningBalanceBeforeStartDate}">
			<c:set var="total_debit_Supplier_beforeStartDate"
				value="${total_debit_Supplier_beforeStartDate + obalance.debit_balance}" />
			<c:set var="total_credit_Supplier_beforeStartDate"
				value="${total_credit_Supplier_beforeStartDate + obalance.credit_balance}" />
		</c:forEach>

		<c:set var="total_credit_Supplier" value="0" />
		<c:set var="total_debit_Supplier" value="0" />

		<c:forEach var="obalance" items="${supplierOpeningBalanceList}">
			<c:set var="total_debit_Supplier"
				value="${total_debit_Supplier + obalance.debit_balance}" />
			<c:set var="total_credit_Supplier"
				value="${total_credit_Supplier + obalance.credit_balance}" />
		</c:forEach>

		<c:set var="total_credit_Customer_beforeStartDate" value="0" />
		<c:set var="total_debit_Customer_beforeStartDate" value="0" />


		<c:forEach var="obalance"
			items="${customerOpeningBalanceBeforeStartDate}">
			<c:set var="total_debit_Customer_beforeStartDate"
				value="${total_debit_Customer_beforeStartDate + obalance.debit_balance}" />
			<c:set var="total_credit_Customer_beforeStartDate"
				value="${total_credit_Customer_beforeStartDate + obalance.credit_balance}" />
		</c:forEach>

		<c:set var="total_credit_Customer" value="0" />
		<c:set var="total_debit_Customer" value="0" />

		<c:forEach var="obalance" items="${customerOpeningBalanceList}">
			<c:set var="total_debit_Customer"
				value="${total_debit_Customer + obalance.debit_balance}" />
			<c:set var="total_credit_Customer"
				value="${total_credit_Customer + obalance.credit_balance}" />
		</c:forEach>


		<c:set var="total_credit_Bank_beforeStartDate" value="0" />
		<c:set var="total_debit_Bank_beforeStartDate" value="0" />

		<c:forEach var="obalance" items="${bankOpeningBalanceBeforeStartDate}">
			<c:set var="total_debit_Bank_beforeStartDate"
				value="${total_debit_Bank_beforeStartDate + obalance.debit_balance}" />
			<c:set var="total_credit_Bank_beforeStartDate"
				value="${total_credit_Bank_beforeStartDate + obalance.credit_balance}" />
		</c:forEach>

		<c:set var="total_credit_Bank" value="0" />
		<c:set var="total_debit_Bank" value="0" />

		<c:forEach var="obalance" items="${bankOpeningBalanceList}">
			<c:set var="total_debit_Bank"
				value="${total_debit_Bank + obalance.debit_balance}" />
			<c:set var="total_credit_Bank"
				value="${total_credit_Bank + obalance.credit_balance}" />
		</c:forEach>

		<c:if test="${subOpeningList.size()>0}">

			<c:forEach var="obalance" items="${subOpeningList}">

				<c:if test="${obalance.group.postingSide.posting_id == 3}">

					<c:set var="openingBalanceforGroup" value="0" />
					<c:set var="total_debitGroup" value="0" />
					<c:set var="total_creditGroup" value="0" />

					<c:forEach var="obalanceBeStartDate"
						items="${subOpeningListBeforeStartDate}">
						
						<c:if
							test="${obalanceBeStartDate.accountGroupName==obalance.accountGroupName}">
							<c:choose>
						
								<c:when test="${obalance.accountGroupName==group1}">

									<c:set var="openingBalanceforGroup"
										value="${(obalanceBeStartDate.totaldebit_balance+total_debit_Customer_beforeStartDate+total_debit_Bank_beforeStartDate)-(obalanceBeStartDate.totalcredit_balance+total_credit_Customer_beforeStartDate+total_credit_Bank_beforeStartDate)}" />
									<c:set var="total_debitGroup"
										value="${(obalance.totaldebit_balance+total_debit_Customer+total_debit_Bank)}" />
									<c:set var="total_creditGroup"
										value="${(obalance.totalcredit_balance+total_credit_Customer+total_credit_Bank)}" />

								</c:when>
								<c:otherwise>
									<c:set var="openingBalanceforGroup"
										value="${obalanceBeStartDate.totaldebit_balance-obalanceBeStartDate.totalcredit_balance}" />
									<c:set var="total_debitGroup"
										value="${obalance.totaldebit_balance}" />
									<c:set var="total_creditGroup"
										value="${obalance.totalcredit_balance}" />

								</c:otherwise>
							</c:choose>
						</c:if>
					</c:forEach>
					<tbody>
						<%-- <% int Loopcount=0; %>   --%>
						<%-- <c:set var="Loopcount" value="${rowcount}" /> --%>
						<c:set var="grandtotal_credit_group" value="${grandtotal_credit_group +total_creditGroup}" />
				<c:set var="grandtotal_debit_group" value="${grandtotal_debit_group +total_debitGroup}" />
					<c:if
							test="${openingBalanceforGroup!=0 || total_debitGroup!=0 || total_creditGroup!=0}">
                        <c:set var="Asset_total" value="${Asset_total+openingBalanceforGroup}" />
                         <c:set var="Asset_CB" value="${(openingBalanceforGroup)+(total_debitGroup-total_creditGroup)}"  />
                          <c:set var="Asset_CB_total" value="${Asset_CB_total+Asset_CB}" />
							<!--  start of group -->
							<tr class='n-style'>
								<%-- <td style="text-align: left;"><h5>${rowcount}</h5></td>
							 --%>		<td style="text-align: left;"><h5>${obalance.accountGroupName}</h5></td>
								<td class="tright"><fmt:formatNumber type="number"
										minFractionDigits="2" maxFractionDigits="2"
										value="${openingBalanceforGroup}" /></td>
								<td class="tright"><fmt:formatNumber type="number"
										minFractionDigits="2" maxFractionDigits="2"
										value="${total_debitGroup}" /></td>
								<td class="tright"><fmt:formatNumber type="number"
										minFractionDigits="2" maxFractionDigits="2"
										value="${total_creditGroup}" /></td>
								<td class="tright"><fmt:formatNumber type="number"
										minFractionDigits="2" maxFractionDigits="2"
										value="${(openingBalanceforGroup)+(total_debitGroup-total_creditGroup)}" />
								</td>
							</tr>
						</c:if>


						<!--  start of subgroup -->
						<c:if test="${obalance.accountSubGroupNameList.size()>0}">
							<c:forEach var="accSubGroup"
								items="${obalance.accountSubGroupNameList}">

								<c:set var="total_credit_Subgroup_beforeStartDate" value="0" />
								<c:set var="total_debit_Subgroup_beforeStartDate" value="0" />
								<c:set var="total_credit_Subgroup" value="0" />
								<c:set var="total_debit_Subgroup" value="0" />

								<c:forEach var="obalanceBeStartDate"
									items="${subOpeningListBeforeStartDate}">

									<c:if
										test="${obalanceBeStartDate.accountSubGroupNameList.size()>0}">

										<c:forEach var="subGroupName"
											items="${obalanceBeStartDate.accountSubGroupNameList}">

											<c:if test="${subGroupName==accSubGroup}">
												<c:if test="${obalanceBeStartDate.ledgerformlist.size()>0}">
													<c:forEach var="ledgerform1"
														items="${obalanceBeStartDate.ledgerformlist}">
														<c:if test="${ledgerform1.subgroupName==accSubGroup}">
															<c:set var="total_credit_Subgroup_beforeStartDate"
																value="${total_credit_Subgroup_beforeStartDate + ledgerform1.ledgercredit_balance}" />
															<c:set var="total_debit_Subgroup_beforeStartDate"
																value="${total_debit_Subgroup_beforeStartDate + ledgerform1.ledgerdebit_balance}" />
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
											<c:set var="total_credit_Subgroup"
												value="${total_credit_Subgroup + ledgerform2.ledgercredit_balance}" />
											<c:set var="total_debit_Subgroup"
												value="${total_debit_Subgroup + ledgerform2.ledgerdebit_balance}" />
										</c:if>
									</c:forEach>
								</c:if>


								<c:set var="openingBalanceSubgroup" value="0" />
								<c:set var="total_debitSubgroup" value="0" />
								<c:set var="total_creditSubgroup" value="0" />

								<c:choose>
									<c:when
										test="${(accSubGroup==subGroup2)||(accSubGroup==subGroup3)}">

										<c:if test="${accSubGroup==subGroup2}">
											<c:set var="openingBalanceSubgroup"
												value="${(total_debit_Subgroup_beforeStartDate+total_debit_Customer_beforeStartDate)-(total_credit_Subgroup_beforeStartDate+total_credit_Customer_beforeStartDate)}" />
											<c:set var="total_debitSubgroup"
												value="${(total_debit_Subgroup+total_debit_Customer)}" />
											<c:set var="total_creditSubgroup"
												value="${(total_credit_Subgroup+total_credit_Customer)}" />
										</c:if>

										<c:if test="${accSubGroup==subGroup3}">
											<c:set var="openingBalanceSubgroup"
												value="${(total_debit_Subgroup_beforeStartDate+total_debit_Bank_beforeStartDate)-(total_credit_Subgroup_beforeStartDate+total_credit_Bank_beforeStartDate)}" />
											<c:set var="total_debitSubgroup"
												value="${(total_debit_Subgroup+total_debit_Bank)}" />
											<c:set var="total_creditSubgroup"
												value="${(total_credit_Subgroup+total_credit_Bank)}" />

										</c:if>

									</c:when>
									<c:otherwise>
										<c:set var="openingBalanceSubgroup"
											value="${total_debit_Subgroup_beforeStartDate-total_credit_Subgroup_beforeStartDate}" />
										<c:set var="total_debitSubgroup"
											value="${total_debit_Subgroup}" />
										<c:set var="total_creditSubgroup"
											value="${total_credit_Subgroup}" />
									</c:otherwise>
								</c:choose>

								<c:if
									test="${openingBalanceSubgroup!=0 || total_debitSubgroup!=0 || total_creditSubgroup!=0}">
									<tr class='n-style'>
									<%-- 	<td style="text-align: left;"><h5>${rowcount}</h5></td>
									 --%>	<td style="text-align: left;">${emptyString}${accSubGroup}</td>
										<td class="tright"><fmt:formatNumber type="number"
												minFractionDigits="2" maxFractionDigits="2"
												value="${openingBalanceSubgroup}" /></td>
										<td class="tright"><fmt:formatNumber type="number"
												minFractionDigits="2" maxFractionDigits="2"
												value="${total_debitSubgroup}" /></td>
										<td class="tright"><fmt:formatNumber type="number"
												minFractionDigits="2" maxFractionDigits="2"
												value="${total_creditSubgroup}" /></td>
										<td class="tright"><fmt:formatNumber type="number"
												minFractionDigits="2" maxFractionDigits="2"
												value="${(openingBalanceSubgroup)+(total_debitSubgroup-total_creditSubgroup)}" />
										</td>
									</tr>


									<!--  start of ledger -->
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
														<%-- 	<td style="text-align: left;"><h5>${rowcount}</h5></td>
														 --%>	<td style="text-align: left;">${emptyString}${emptyString}${ledgerform3.ledgerName}</td>
															<td class="tright"><fmt:formatNumber type="number"
																	minFractionDigits="2" maxFractionDigits="2"
																	value="${openiBalanceOfLedger}" /></td>
															<td class="tright"><fmt:formatNumber type="number"
																	minFractionDigits="2" maxFractionDigits="2"
																	value="${total_debitOfLedger}" /></td>
															<td class="tright"><fmt:formatNumber type="number"
																	minFractionDigits="2" maxFractionDigits="2"
																	value="${total_creditOfLedger}" /></td>
															<td class="tright"><fmt:formatNumber type="number"
																	minFractionDigits="2" maxFractionDigits="2"
																	value="${(openiBalanceOfLedger)+(total_debitOfLedger-total_creditOfLedger)}" />
															</td>
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
																	<%-- 	<td style="text-align: left;"><h5>${rowcount}</h5></td>
																	 --%>	<td style="text-align: left;">${emptyString}${emptyString}${emptyString}${subledger.subledgerName}</td>
																		<td class="tright"><fmt:formatNumber
																				type="number" minFractionDigits="2"
																				maxFractionDigits="2"
																				value="${openiBalanceOfSubLedger}" /></td>
																		<td class="tright"><fmt:formatNumber
																				type="number" minFractionDigits="2"
																				maxFractionDigits="2"
																				value="${total_debitOfSubLedger}" /> <c:set
																				var="row_count_debit"
																				value="${row_count_debit + total_debitOfSubLedger}" />
																		</td>
																		<td class="tright"><fmt:formatNumber
																				type="number" minFractionDigits="2"
																				maxFractionDigits="2"
																				value="${total_creditOfSubLedger}" /> <c:set
																				var="row_count_credit"
																				value="${row_count_credit + total_creditOfSubLedger}" />
																		</td>
																		<td class="tright"><fmt:formatNumber
																				type="number" minFractionDigits="2"
																				maxFractionDigits="2"
																				value="${(openiBalanceOfSubLedger)+(total_debitOfSubLedger-total_creditOfSubLedger)}" />
																		</td>
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
													<c:if test="${obalanceBeStartDate.ledgerformlist.size()>0}">
														<c:forEach var="ledgerform6"
															items="${obalanceBeStartDate.ledgerformlist}">
															<c:set var="isNotPresent" value="0" />
															<c:set var="isPresent" value="0" />
															<c:if test="${ledgerform6.subgroupName==accSubGroup}">
																<c:if test="${obalance.ledgerformlist.size()>0}">

																	<c:forEach var="ledgerform7"
																		items="${obalance.ledgerformlist}">
																		<c:if
																			test="${ledgerform7.ledgerName==ledgerform6.ledgerName}">
																			<c:set var="isPresent" value="1" />
																		</c:if>
																	</c:forEach>
																	<c:if test="${isPresent==0}">
																		<c:if test="${obalance.ledgerformlist.size()>0}">
																			<c:forEach var="ledgerform7"
																				items="${obalance.ledgerformlist}">
																				<c:if
																					test="${ledgerform7.ledgerName!=ledgerform6.ledgerName}">
																					<c:if test="${isNotPresent==0}">
																						<c:set var="openiBalanceOfLedger" value="0" />
																						<tr class='n-style'>
																						<%-- 	<td style="text-align: left;"><h5>${rowcount}</h5></td>
																						 --%>	<td style="text-align: left;">${emptyString}${emptyString}${ledgerform6.ledgerName}</td>
																							<c:set var="openiBalanceOfLedger"
																								value="${openiBalanceOfLedger + (ledgerform6.ledgerdebit_balance-ledgerform6.ledgercredit_balance)}" />
																							<td class="tright"><fmt:formatNumber
																									type="number" minFractionDigits="2"
																									maxFractionDigits="2"
																									value="${openiBalanceOfLedger}" /></td>
																							<td class="tright"><fmt:formatNumber
																									type="number" minFractionDigits="2"
																									maxFractionDigits="2" value="0" /></td>
																							<td class="tright"><fmt:formatNumber
																									type="number" minFractionDigits="2"
																									maxFractionDigits="2" value="0" /></td>
																							<td class="tright"><fmt:formatNumber
																									type="number" minFractionDigits="2"
																									maxFractionDigits="2"
																									value="${openiBalanceOfLedger}" /></td>
																						</tr>
																						<c:if test="${ledgerform6.subledgerList.size()>0}">


																							<c:forEach var="subledger1"
																								items="${ledgerform6.subledgerList}">
																								<c:if test="${subledger1!=null}">
																									<c:set var="openiBalanceOfSubLedger" value="0" />
																									<c:set var="openiBalanceOfSubLedger"
																										value="${openiBalanceOfSubLedger + (subledger1.debit_balance-subledger1.credit_balance)}" />
																									<c:if test="${openiBalanceOfSubLedger!=0}">
																										<tr class='n-style'>
																										 	<td style="text-align: left;">${emptyString}${emptyString}${emptyString}${subledger1.subledgerName}</td>
																											<td class="tright"><fmt:formatNumber
																													type="number" minFractionDigits="2"
																													maxFractionDigits="2"
																													value="${openiBalanceOfSubLedger}" /></td>
																											<td class="tright"><fmt:formatNumber
																													type="number" minFractionDigits="2"
																													maxFractionDigits="2" value="0" /></td>
																											<td class="tright"><fmt:formatNumber
																													type="number" minFractionDigits="2"
																													maxFractionDigits="2" value="0" /></td>
																											<td class="tright"><fmt:formatNumber
																													type="number" minFractionDigits="2"
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
																	<tr class='n-style'>
																	<%-- 	<td style="text-align: left;"><h5>${rowcount}</h5></td>
																	 --%>	<td style="text-align: left;">${emptyString}${emptyString}${ledgerform6.ledgerName}</td>
																		<c:set var="openiBalanceOfLedger"
																			value="${openiBalanceOfLedger + (ledgerform6.ledgerdebit_balance-ledgerform6.ledgercredit_balance)}" />
																		<td class="tright"><fmt:formatNumber
																				type="number" minFractionDigits="2"
																				maxFractionDigits="2"
																				value="${openiBalanceOfLedger}" /></td>
																		<td class="tright"><fmt:formatNumber
																				type="number" minFractionDigits="2"
																				maxFractionDigits="2" value="0" /></td>
																		<td class="tright"><fmt:formatNumber
																				type="number" minFractionDigits="2"
																				maxFractionDigits="2" value="0" /></td>
																		<td class="tright"><fmt:formatNumber
																				type="number" minFractionDigits="2"
																				maxFractionDigits="2"
																				value="${openiBalanceOfLedger}" /></td>
																	</tr>
																	<c:if test="${ledgerform6.subledgerList.size()>0}">


																		<c:forEach var="subledger1"
																			items="${ledgerform6.subledgerList}">
																			<c:if test="${subledger1!=null}">
																				<c:set var="openiBalanceOfSubLedger" value="0" />
																				<c:set var="openiBalanceOfSubLedger"
																					value="${openiBalanceOfSubLedger + (subledger1.debit_balance-subledger1.credit_balance)}" />

																				<c:if test="${openiBalanceOfSubLedger!=0}">
																					<tr class='n-style'>
																				<%-- 		<td style="text-align: left;"><h5>${rowcount}</h5></td>
																				 --%>		<td style="text-align: left;">${emptyString}${emptyString}${emptyString}${subledger1.subledgerName}</td>
																						<td class="tright"><fmt:formatNumber
																								type="number" minFractionDigits="2"
																								maxFractionDigits="2"
																								value="${openiBalanceOfSubLedger}" /></td>
																						<td class="tright"><fmt:formatNumber
																								type="number" minFractionDigits="2"
																								maxFractionDigits="2" value="0" /></td>
																						<td class="tright"><fmt:formatNumber
																								type="number" minFractionDigits="2"
																								maxFractionDigits="2" value="0" /></td>
																						<td class="tright"><fmt:formatNumber
																								type="number" minFractionDigits="2"
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
													<%-- <td style="text-align: left;"><h5>${rowcount}</h5></td>
 --%>
														<td style="text-align: left;">${emptyString}${emptyString}${bankOpbalance.bankName}</td>
													
													<td class="tright"><fmt:formatNumber type="number"
															minFractionDigits="2" maxFractionDigits="2"
															value="${bankOpeningbalance}" />
													<td class="tright"><fmt:formatNumber type="number"
															minFractionDigits="2" maxFractionDigits="2"
															value="${bankDebitbalance}" /> <c:set
															var="row_count_debit"
															value="${row_count_debit + bankDebitbalance}" /></td>
													<td class="tright"><fmt:formatNumber type="number"
															minFractionDigits="2" maxFractionDigits="2"
															value="${bankCreditbalance}" /> <c:set
															var="row_count_credit"
															value="${row_count_credit + bankCreditbalance}" /></td>
													<td class="tright"><fmt:formatNumber type="number"
															minFractionDigits="2" maxFractionDigits="2"
															value="${(bankOpeningbalance)+(bankDebitbalance-bankCreditbalance)}" />
													</td>
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
														<%-- <td style="text-align: left;"><h5>${rowcount}</h5></td>

 --%>
														<td style="text-align: left;">${emptyString}${emptyString}${bankOpbalance1.bankName}</td>
														<td class="tright"><fmt:formatNumber type="number"
																minFractionDigits="2" maxFractionDigits="2"
																value="${openiBalanceOfbank}" />
														<td class="tright"><fmt:formatNumber type="number"
																minFractionDigits="2" maxFractionDigits="2" value="0" /></td>
														<td class="tright"><fmt:formatNumber type="number"
																minFractionDigits="2" maxFractionDigits="2" value="0" /></td>
														<td class="tright"><fmt:formatNumber type="number"
																minFractionDigits="2" maxFractionDigits="2"
																value="${(openiBalanceOfbank)}" /></td>
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
											<%-- 		<td style="text-align: left;"><h5>${rowcount}</h5></td>
													 --%><td style="text-align: left;">${emptyString}${emptyString}${cusoPbalance.customerName}</td>
													<td class="tright"><fmt:formatNumber type="number"
															minFractionDigits="2" maxFractionDigits="2"
															value="${cusOpeningbalance}" />
													<td class="tright"><fmt:formatNumber type="number"
															minFractionDigits="2" maxFractionDigits="2"
															value="${cusDebitbalance}" /> <c:set
															var="row_count_debit"
															value="${row_count_debit + cusDebitbalance}" /></td>
													<td class="tright"><fmt:formatNumber type="number"
															minFractionDigits="2" maxFractionDigits="2"
															value="${cusCreditbalance}" /> <c:set
															var="row_count_credit"
															value="${row_count_credit + cusCreditbalance}" /></td>
													<td class="tright"><fmt:formatNumber type="number"
															minFractionDigits="2" maxFractionDigits="2"
															value="${(cusOpeningbalance)+(cusDebitbalance-cusCreditbalance)}" />
													</td>
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
													<%-- 	<td style="text-align: left;"><h5>${rowcount}</h5></td>
													 --%>	<td style="text-align: left;">${emptyString}${emptyString}${cusoPbalance1.customerName}</td>
														<td class="tright"><fmt:formatNumber type="number"
																minFractionDigits="2" maxFractionDigits="2"
																value="${openiBalanceOfcustomer}" />
														<td class="tright"><fmt:formatNumber type="number"
																minFractionDigits="2" maxFractionDigits="2" value="0" /></td>
														<td class="tright"><fmt:formatNumber type="number"
																minFractionDigits="2" maxFractionDigits="2" value="0" /></td>
														<td class="tright"><fmt:formatNumber type="number"
																minFractionDigits="2" maxFractionDigits="2"
																value="${(openiBalanceOfcustomer)}" /></td>
													</tr>
												</c:if>

											</c:if>

										</c:forEach>
									</c:if>

								</c:if>
							</c:forEach>

						</c:if>
				</c:if>
			</c:forEach>
<tr><td>Asset Total</td> 
<td class="tright"><fmt:formatNumber type="number"
																	minFractionDigits="2" maxFractionDigits="2"
																	value="${(Asset_total)}" /></td>
																	<td></td><td></td>
																	<td class="tright"><fmt:formatNumber type="number"
																	minFractionDigits="2" maxFractionDigits="2"
																	value="${(Asset_CB_total)}" /></td>
</tr>
			<c:forEach var="obalance" items="${subOpeningList}">
				<!--  start of group -->

				<c:if test="${obalance.group.postingSide.posting_id == 4}">

					<c:set var="openingBalanceforGroup" value="0" />
					<c:set var="total_debitGroup" value="0" />
					<c:set var="total_creditGroup" value="0" />

					<c:forEach var="obalanceBeStartDate"
						items="${subOpeningListBeforeStartDate}">
						<c:if
							test="${obalanceBeStartDate.accountGroupName==obalance.accountGroupName}">
							<c:choose>
								<c:when test="${obalance.accountGroupName==group4}">

									<c:set var="openingBalanceforGroup"
										value="${(obalanceBeStartDate.totalcredit_balance+total_credit_Supplier_beforeStartDate)-(obalanceBeStartDate.totaldebit_balance+total_debit_Supplier_beforeStartDate)}" />
									<c:set var="total_debitGroup"
										value="${(obalance.totaldebit_balance+total_debit_Supplier)}" />
									<c:set var="total_creditGroup"
										value="${(obalance.totalcredit_balance+total_credit_Supplier)}" />

								</c:when>
								<c:otherwise>
									<c:set var="openingBalanceforGroup"
										value="${obalanceBeStartDate.totalcredit_balance-obalanceBeStartDate.totaldebit_balance}" />
									<c:set var="total_debitGroup"
										value="${obalance.totaldebit_balance}" />
									<c:set var="total_creditGroup"
										value="${obalance.totalcredit_balance}" />
								</c:otherwise>
							</c:choose>
						</c:if>
					</c:forEach>
<c:set var="grandtotal_credit_group" value="${grandtotal_credit_group +total_creditGroup}" />
				<c:set var="grandtotal_debit_group" value="${grandtotal_debit_group +total_debitGroup}" />
			<c:if
						test="${openingBalanceforGroup!=0 || total_debitGroup!=0 || total_creditGroup!=0}">
						<c:set var="Liability_total" value="${Liability_total+openingBalanceforGroup}" />
						<c:set var="Liability_CB" value="${(openingBalanceforGroup)+(total_creditGroup-total_debitGroup)}" />
						<c:set var="Liability_CB_total" value="${Liability_CB_total+Liability_CB}" />
						<tr class='n-style'>
						<%-- 	<td style="text-align: left;"><h5>${rowcount}</h5></td>
						 --%>	<td style="text-align: left;"><h5>${obalance.accountGroupName}</h5></td>
							<td class="tright"><fmt:formatNumber type="number"
									minFractionDigits="2" maxFractionDigits="2"
									value="${openingBalanceforGroup}" /></td>
							<td class="tright"><fmt:formatNumber type="number"
									minFractionDigits="2" maxFractionDigits="2"
									value="${total_debitGroup}" /></td>
							<td class="tright"><fmt:formatNumber type="number"
									minFractionDigits="2" maxFractionDigits="2"
									value="${total_creditGroup}" /></td>
							<td class="tright"><fmt:formatNumber type="number"
									minFractionDigits="2" maxFractionDigits="2"
									value="${(openingBalanceforGroup)+(total_creditGroup-total_debitGroup)}" />
							</td>
						</tr>
					</c:if>


					<!--  start of subgroup -->
					<c:if test="${obalance.accountSubGroupNameList.size()>0}">
						<c:forEach var="accSubGroup"
							items="${obalance.accountSubGroupNameList}">

							<c:set var="total_credit_Subgroup_beforeStartDate" value="0" />
							<c:set var="total_debit_Subgroup_beforeStartDate" value="0" />
							<c:set var="total_credit_Subgroup" value="0" />
							<c:set var="total_debit_Subgroup" value="0" />

							<c:forEach var="obalanceBeStartDate"
								items="${subOpeningListBeforeStartDate}">

								<c:if
									test="${obalanceBeStartDate.accountSubGroupNameList.size()>0}">

									<c:forEach var="subGroupName"
										items="${obalanceBeStartDate.accountSubGroupNameList}">

										<c:if test="${subGroupName==accSubGroup}">
											<c:if test="${obalanceBeStartDate.ledgerformlist.size()>0}">
												<c:forEach var="ledgerform1"
													items="${obalanceBeStartDate.ledgerformlist}">
													<c:if test="${ledgerform1.subgroupName==accSubGroup}">
														<c:set var="total_credit_Subgroup_beforeStartDate"
															value="${total_credit_Subgroup_beforeStartDate + ledgerform1.ledgercredit_balance}" />
														<c:set var="total_debit_Subgroup_beforeStartDate"
															value="${total_debit_Subgroup_beforeStartDate + ledgerform1.ledgerdebit_balance}" />
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
										<c:set var="total_credit_Subgroup"
											value="${total_credit_Subgroup + ledgerform2.ledgercredit_balance}" />
										<c:set var="total_debit_Subgroup"
											value="${total_debit_Subgroup + ledgerform2.ledgerdebit_balance}" />
									</c:if>
								</c:forEach>
							</c:if>

							<c:set var="openingBalanceSubgroup" value="0" />
							<c:set var="total_debitSubgroup" value="0" />
							<c:set var="total_creditSubgroup" value="0" />

							<c:choose>
								<c:when test="${accSubGroup==subGroup5}">

									<c:set var="openingBalanceSubgroup"
										value="${(total_credit_Subgroup_beforeStartDate+total_credit_Supplier_beforeStartDate)-(total_debit_Subgroup_beforeStartDate+total_debit_Supplier_beforeStartDate)}" />
									<c:set var="total_debitSubgroup"
										value="${(total_debit_Subgroup+total_debit_Supplier)}" />
									<c:set var="total_creditSubgroup"
										value="${(total_credit_Subgroup+total_credit_Supplier)}" />
								</c:when>
								<c:otherwise>
									<c:set var="openingBalanceSubgroup"
										value="${total_credit_Subgroup_beforeStartDate-total_debit_Subgroup_beforeStartDate}" />
									<c:set var="total_debitSubgroup"
										value="${total_debit_Subgroup}" />
									<c:set var="total_creditSubgroup"
										value="${total_credit_Subgroup}" />
								</c:otherwise>
							</c:choose>

							<c:if
								test="${openingBalanceSubgroup!=0 || total_debitSubgroup!=0 || total_creditSubgroup!=0}">
								<tr class='n-style'>
									<%-- <%-- <td style="text-align: left;"><h5>${rowcount}</h5></td>
									 --%> --%>	<td style="text-align: left;">${emptyString}${accSubGroup}</td>
									<td class="tright"><fmt:formatNumber type="number"
											minFractionDigits="2" maxFractionDigits="2"
											value="${openingBalanceSubgroup}" /></td>
									<td class="tright"><fmt:formatNumber type="number"
											minFractionDigits="2" maxFractionDigits="2"
											value="${total_debitSubgroup}" /></td>
									<td class="tright"><fmt:formatNumber type="number"
											minFractionDigits="2" maxFractionDigits="2"
											value="${total_creditSubgroup}" /></td>
									<td class="tright"><fmt:formatNumber type="number"
											minFractionDigits="2" maxFractionDigits="2"
											value="${(openingBalanceSubgroup)+(total_creditSubgroup-total_debitSubgroup)}" />
									</td>
								</tr>


								<!--  start of ledger -->
								<c:if test="${obalance.ledgerformlist.size()>0}">
									<c:forEach var="ledgerform3" items="${obalance.ledgerformlist}">
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
																			<c:if test="${ledgerform4.subgroupName==accSubGroup}">
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
													<%-- 	<td style="text-align: left;"><h5>${rowcount}</h5></td>
													 --%>	<td style="text-align: left;">${emptyString}${emptyString}${ledgerform3.ledgerName}</td>
														<td class="tright"><fmt:formatNumber type="number"
																minFractionDigits="2" maxFractionDigits="2"
																value="${openiBalanceOfLedger}" /></td>
														<td class="tright"><fmt:formatNumber type="number"
																minFractionDigits="2" maxFractionDigits="2"
																value="${total_debitOfLedger}" /></td>
														<td class="tright"><fmt:formatNumber type="number"
																minFractionDigits="2" maxFractionDigits="2"
																value="${total_creditOfLedger}" /></td>
														<td class="tright"><fmt:formatNumber type="number"
																minFractionDigits="2" maxFractionDigits="2"
																value="${(openiBalanceOfLedger)+(total_creditOfLedger-total_debitOfLedger)}" />
														</td>
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
																<%-- 	<td style="text-align: left;"><h5>${rowcount}</h5></td>
																 --%>	<td style="text-align: left;">${emptyString}${emptyString}${emptyString}${subledger.subledgerName}</td>
																			
																	<td class="tright"><fmt:formatNumber type="number"
																			minFractionDigits="2" maxFractionDigits="2"
																			value="${openiBalanceOfSubLedger}" /></td>
																	<td class="tright"><fmt:formatNumber type="number"
																			minFractionDigits="2" maxFractionDigits="2"
																			value="${total_debitOfSubLedger}" /> <c:set
																			var="row_count_debit"
																			value="${row_count_debit + total_debitOfSubLedger}" />
																	</td>
																	<td class="tright"><fmt:formatNumber type="number"
																			minFractionDigits="2" maxFractionDigits="2"
																			value="${total_creditOfSubLedger}" /> <c:set
																			var="row_count_credit"
																			value="${row_count_credit + total_creditOfSubLedger}" />
																	</td>
																	<td class="tright"><fmt:formatNumber type="number"
																			minFractionDigits="2" maxFractionDigits="2"
																			value="${(openiBalanceOfSubLedger)+(total_creditOfSubLedger-total_debitOfSubLedger)}" />
																	</td>
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
												<c:if test="${obalanceBeStartDate.ledgerformlist.size()>0}">
													<c:forEach var="ledgerform6"
														items="${obalanceBeStartDate.ledgerformlist}">
														<c:set var="isNotPresent" value="0" />
														<c:set var="isPresent" value="0" />
														<c:if test="${ledgerform6.subgroupName==accSubGroup}">
															<c:if test="${obalance.ledgerformlist.size()>0}">

																<c:forEach var="ledgerform7"
																	items="${obalance.ledgerformlist}">
																	<c:if
																		test="${ledgerform7.ledgerName==ledgerform6.ledgerName}">
																		<c:set var="isPresent" value="1" />
																	</c:if>
																</c:forEach>
																<c:if test="${isPresent==0}">
																	<c:if test="${obalance.ledgerformlist.size()>0}">
																		<c:forEach var="ledgerform7"
																			items="${obalance.ledgerformlist}">
																			<c:if
																				test="${ledgerform7.ledgerName!=ledgerform6.ledgerName}">
																				<c:if test="${isNotPresent==0}">
																					<c:set var="openiBalanceOfLedger" value="0" />
																					<tr class='n-style'>
																					<%-- 	<td style="text-align: left;"><h5>${rowcount}</h5></td>
																					 --%>	<td style="text-align: left;">${emptyString}${emptyString}${ledgerform6.ledgerName}</td>
																							
																						<c:set var="openiBalanceOfLedger"
																							value="${openiBalanceOfLedger + (ledgerform6.ledgercredit_balance-ledgerform6.ledgerdebit_balance)}" />
																						<td class="tright"><fmt:formatNumber
																								type="number" minFractionDigits="2"
																								maxFractionDigits="2"
																								value="${openiBalanceOfLedger}" /></td>
																						<td class="tright"><fmt:formatNumber
																								type="number" minFractionDigits="2"
																								maxFractionDigits="2" value="0" /></td>
																						<td class="tright"><fmt:formatNumber
																								type="number" minFractionDigits="2"
																								maxFractionDigits="2" value="0" /></td>
																						<td class="tright"><fmt:formatNumber
																								type="number" minFractionDigits="2"
																								maxFractionDigits="2"
																								value="${openiBalanceOfLedger}" /></td>
																					</tr>
																					<c:if test="${ledgerform6.subledgerList.size()>0}">


																						<c:forEach var="subledger1"
																							items="${ledgerform6.subledgerList}">
																							<c:if test="${subledger1!=null}">
																								<c:set var="openiBalanceOfSubLedger" value="0" />
																								<c:set var="openiBalanceOfSubLedger"
																									value="${openiBalanceOfSubLedger + (subledger1.credit_balance-subledger1.debit_balance)}" />
																								<c:if test="${openiBalanceOfSubLedger!=0}">
																									<tr class='n-style'>
																									<%-- 	<td style="text-align: left;"><h5>${rowcount}</h5></td>
																									 --%>		<td style="text-align: left;">${emptyString}${emptyString}${emptyString}${subledger1.subledgerName}</td>
																												
																										<td class="tright"><fmt:formatNumber
																												type="number" minFractionDigits="2"
																												maxFractionDigits="2"
																												value="${openiBalanceOfSubLedger}" /></td>
																										<td class="tright"><fmt:formatNumber
																												type="number" minFractionDigits="2"
																												maxFractionDigits="2" value="0" /></td>
																										<td class="tright"><fmt:formatNumber
																												type="number" minFractionDigits="2"
																												maxFractionDigits="2" value="0" /></td>
																										<td class="tright"><fmt:formatNumber
																												type="number" minFractionDigits="2"
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
																<tr class='n-style'>
															<%-- 		<td style="text-align: left;"><h5>${rowcount}</h5></td>
															 --%>			<td style="text-align: left;">${emptyString}${emptyString}${ledgerform6.ledgerName}</td>
																							
																	<c:set var="openiBalanceOfLedger"
																		value="${openiBalanceOfLedger + (ledgerform6.ledgercredit_balance-ledgerform6.ledgerdebit_balance)}" />
																	<td class="tright"><fmt:formatNumber type="number"
																			minFractionDigits="2" maxFractionDigits="2"
																			value="${openiBalanceOfLedger}" /></td>
																	<td class="tright"><fmt:formatNumber type="number"
																			minFractionDigits="2" maxFractionDigits="2" value="0" /></td>
																	<td class="tright"><fmt:formatNumber type="number"
																			minFractionDigits="2" maxFractionDigits="2" value="0" /></td>
																	<td class="tright"><fmt:formatNumber type="number"
																			minFractionDigits="2" maxFractionDigits="2"
																			value="${openiBalanceOfLedger}" /></td>
																</tr>
																<c:if test="${ledgerform6.subledgerList.size()>0}">


																	<c:forEach var="subledger1"
																		items="${ledgerform6.subledgerList}">
																		<c:if test="${subledger1!=null}">
																			<c:set var="openiBalanceOfSubLedger" value="0" />
																			<c:set var="openiBalanceOfSubLedger"
																				value="${openiBalanceOfSubLedger + (subledger1.credit_balance-subledger1.debit_balance)}" />

																			<c:if test="${openiBalanceOfSubLedger!=0}">
																				<tr class='n-style'>
																				<%-- 	<td style="text-align: left;"><h5>${rowcount}</h5></td>
																				 --%>	<td style="text-align: left;">${emptyString}${emptyString}${emptyString}${subledger1.subledgerName}</td>
																													
																					<td class="tright"><fmt:formatNumber
																							type="number" minFractionDigits="2"
																							maxFractionDigits="2"
																							value="${openiBalanceOfSubLedger}" /></td>
																					<td class="tright"><fmt:formatNumber
																							type="number" minFractionDigits="2"
																							maxFractionDigits="2" value="0" /></td>
																					<td class="tright"><fmt:formatNumber
																							type="number" minFractionDigits="2"
																							maxFractionDigits="2" value="0" /></td>
																					<td class="tright"><fmt:formatNumber
																							type="number" minFractionDigits="2"
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
												<%-- <td style="text-align: left;"><h5>${rowcount}</h5></td> --%>
												<td style="text-align: left;">${emptyString}${emptyString}${supOpbalance.supplierName}</td>
													
												<td class="tright"><fmt:formatNumber type="number"
														minFractionDigits="2" maxFractionDigits="2"
														value="${supOpeningbalance}" /></td>
												<td class="tright"><fmt:formatNumber type="number"
														minFractionDigits="2" maxFractionDigits="2"
														value="${supDebitbalance}" /> <c:set
														var="row_count_debit"
														value="${row_count_debit + supDebitbalance}" /></td>
												<td class="tright"><fmt:formatNumber type="number"
														minFractionDigits="2" maxFractionDigits="2"
														value="${supCreditbalance}" /> <c:set
														var="row_count_credit"
														value="${row_count_credit + supCreditbalance}" /></td>
												<td class="tright"><fmt:formatNumber type="number"
														minFractionDigits="2" maxFractionDigits="2"
														value="${(supOpeningbalance)+(supCreditbalance-supDebitbalance)}" />
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
												<%-- 	<td style="text-align: left;"><h5>${rowcount}</h5></td>
												 --%>			<td style="text-align: left;">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;${supOpbalance1.supplierName}</td>
													
													<td class="tright"><fmt:formatNumber type="number"
															minFractionDigits="2" maxFractionDigits="2"
															value="${openiBalanceOfsupplier}" />
													<td class="tright"><fmt:formatNumber type="number"
															minFractionDigits="2" maxFractionDigits="2" value="0" /></td>
													<td class="tright"><fmt:formatNumber type="number"
															minFractionDigits="2" maxFractionDigits="2" value="0" /></td>
													<td class="tright"><fmt:formatNumber type="number"
															minFractionDigits="2" maxFractionDigits="2"
															value="${(openiBalanceOfsupplier)}" /></td>
												</tr>
											</c:if>

										</c:if>

									</c:forEach>
								</c:if>

							</c:if>
						</c:forEach>
					</c:if>
				</c:if>
			</c:forEach>

<tr><td>Libaility Total</td> 
<td class="tright"><fmt:formatNumber type="number"
																	minFractionDigits="2" maxFractionDigits="2"
																	value="${(Liability_total)}" /></td>
																	<td></td><td></td>
																	<td class="tright"><fmt:formatNumber type="number"
																	minFractionDigits="2" maxFractionDigits="2"
																	value="${(Liability_CB_total)}" /></td>
																	<td>
</tr>
			<c:forEach var="obalance" items="${subOpeningList}">
				<!--  start of group -->
				<c:if test="${obalance.group.postingSide.posting_id == 2}">

					<c:set var="openingBalanceforGroup" value="0" />
					<c:set var="total_debitGroup" value="0" />
					<c:set var="total_creditGroup" value="0" />

					<c:forEach var="obalanceBeStartDate"
						items="${subOpeningListBeforeStartDate}">
						<c:if
							test="${obalanceBeStartDate.accountGroupName==obalance.accountGroupName}">
							<c:set var="openingBalanceforGroup"
								value="${obalanceBeStartDate.totalcredit_balance-obalanceBeStartDate.totaldebit_balance}" />
							<c:set var="total_debitGroup"
								value="${obalance.totaldebit_balance}" />
							<c:set var="total_creditGroup"
								value="${obalance.totalcredit_balance}" />
						</c:if>
					</c:forEach>
                <c:set var="grandtotal_credit_group" value="${grandtotal_credit_group +total_creditGroup}" />
				<c:set var="grandtotal_debit_group" value="${grandtotal_debit_group +total_debitGroup}" />
				<c:if
						test="${openingBalanceforGroup!=0 || total_debitGroup!=0 || total_creditGroup!=0}">
						<c:set var="Income_total" value="${Income_total+openingBalanceforGroup}" />
						<c:set var="Liability_total" value="${Liability_total+Income_total}" />
							<c:set var="Income_CB" value="${(openingBalanceforGroup)+(total_creditGroup-total_debitGroup)}" />
								<c:set var="Income_CB_total" value="${Income_CB_total+Income_CB}" />
						<tr class='n-style'>
							<%-- <td style="text-align: left;"><h5>${rowcount}</h5></td> --%>
								<td style="text-align: left;"><h5>${obalance.accountGroupName}</h5></td>

							<td class="tright"><fmt:formatNumber type="number"
									minFractionDigits="2" maxFractionDigits="2"
									value="${openingBalanceforGroup}" /></td>
							<td class="tright"><fmt:formatNumber type="number"
									minFractionDigits="2" maxFractionDigits="2"
									value="${total_debitGroup}" /></td>
							<td class="tright"><fmt:formatNumber type="number"
									minFractionDigits="2" maxFractionDigits="2"
									value="${total_creditGroup}" /></td>
							<td class="tright"><fmt:formatNumber type="number"
									minFractionDigits="2" maxFractionDigits="2"
									value="${(openingBalanceforGroup)+(total_creditGroup-total_debitGroup)}" />
							</td>
						</tr>
					</c:if>


					<!--  start of subgroup -->
					<c:if test="${obalance.accountSubGroupNameList.size()>0}">
						<c:forEach var="accSubGroup"
							items="${obalance.accountSubGroupNameList}">

							<c:set var="total_credit_Subgroup_beforeStartDate" value="0" />
							<c:set var="total_debit_Subgroup_beforeStartDate" value="0" />
							<c:set var="total_credit_Subgroup" value="0" />
							<c:set var="total_debit_Subgroup" value="0" />

							<c:forEach var="obalanceBeStartDate"
								items="${subOpeningListBeforeStartDate}">

								<c:if
									test="${obalanceBeStartDate.accountSubGroupNameList.size()>0}">

									<c:forEach var="subGroupName"
										items="${obalanceBeStartDate.accountSubGroupNameList}">

										<c:if test="${subGroupName==accSubGroup}">
											<c:if test="${obalanceBeStartDate.ledgerformlist.size()>0}">
												<c:forEach var="ledgerform1"
													items="${obalanceBeStartDate.ledgerformlist}">
													<c:if test="${ledgerform1.subgroupName==accSubGroup}">
														<c:set var="total_credit_Subgroup_beforeStartDate"
															value="${total_credit_Subgroup_beforeStartDate + ledgerform1.ledgercredit_balance}" />
														<c:set var="total_debit_Subgroup_beforeStartDate"
															value="${total_debit_Subgroup_beforeStartDate + ledgerform1.ledgerdebit_balance}" />
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
										<c:set var="total_credit_Subgroup"
											value="${total_credit_Subgroup + ledgerform2.ledgercredit_balance}" />
										<c:set var="total_debit_Subgroup"
											value="${total_debit_Subgroup + ledgerform2.ledgerdebit_balance}" />
									</c:if>
								</c:forEach>
							</c:if>

							<c:set var="openingBalanceSubgroup" value="0" />
							<c:set var="total_debitSubgroup" value="0" />
							<c:set var="total_creditSubgroup" value="0" />

							<c:set var="openingBalanceSubgroup"
								value="${total_credit_Subgroup_beforeStartDate-total_debit_Subgroup_beforeStartDate}" />
							<c:set var="total_debitSubgroup" value="${total_debit_Subgroup}" />
							<c:set var="total_creditSubgroup"
								value="${total_credit_Subgroup}" />

							<c:if
								test="${openingBalanceSubgroup!=0 || total_debitSubgroup!=0 || total_creditSubgroup!=0}">
								<tr class='n-style'>
									<%-- <td style="text-align: left;"><h5>${rowcount}</h5></td> --%>
											<td style="text-align: left;">${emptyString}${accSubGroup}</td>
									
									<td class="tright"><fmt:formatNumber type="number"
											minFractionDigits="2" maxFractionDigits="2"
											value="${openingBalanceSubgroup}" /></td>
									<td class="tright"><fmt:formatNumber type="number"
											minFractionDigits="2" maxFractionDigits="2"
											value="${total_debitSubgroup}" /></td>
									<td class="tright"><fmt:formatNumber type="number"
											minFractionDigits="2" maxFractionDigits="2"
											value="${total_creditSubgroup}" /></td>
									<td class="tright"><fmt:formatNumber type="number"
											minFractionDigits="2" maxFractionDigits="2"
											value="${(openingBalanceSubgroup)+(total_creditSubgroup-total_debitSubgroup)}" /></td>
								</tr>


								<!--  start of ledger -->
								<c:if test="${obalance.ledgerformlist.size()>0}">
									<c:forEach var="ledgerform3" items="${obalance.ledgerformlist}">
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
																			<c:if test="${ledgerform4.subgroupName==accSubGroup}">
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
													<%-- 	<td style="text-align: left;"><h5>${rowcount}</h5></td> --%>
																<td style="text-align: left;">${emptyString}${emptyString}${ledgerform3.ledgerName}</td>
												
														<td class="tright"><fmt:formatNumber type="number"
																minFractionDigits="2" maxFractionDigits="2"
																value="${openiBalanceOfLedger}" /></td>
														<td class="tright"><fmt:formatNumber type="number"
																minFractionDigits="2" maxFractionDigits="2"
																value="${total_debitOfLedger}" /></td>
														<td class="tright"><fmt:formatNumber type="number"
																minFractionDigits="2" maxFractionDigits="2"
																value="${total_creditOfLedger}" /></td>
														<td class="tright"><fmt:formatNumber type="number"
																minFractionDigits="2" maxFractionDigits="2"
																value="${(openiBalanceOfLedger)+(total_creditOfLedger-total_debitOfLedger)}" />
														</td>
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
																<%-- 	<td style="text-align: left;"><h5>${rowcount}</h5></td> --%>
																			<td style="text-align: left;">${emptyString}${emptyString}${emptyString}${subledger.subledgerName}</td>
															
																	<td class="tright"><fmt:formatNumber type="number"
																			minFractionDigits="2" maxFractionDigits="2"
																			value="${openiBalanceOfSubLedger}" /></td>
																	<td class="tright"><fmt:formatNumber type="number"
																			minFractionDigits="2" maxFractionDigits="2"
																			value="${total_debitOfSubLedger}" /> <c:set
																			var="row_count_debit"
																			value="${row_count_debit + total_debitOfSubLedger}" />
																	</td>
																	<td class="tright"><fmt:formatNumber type="number"
																			minFractionDigits="2" maxFractionDigits="2"
																			value="${total_creditOfSubLedger}" /> <c:set
																			var="row_count_credit"
																			value="${row_count_credit + total_creditOfSubLedger}" />
																	</td>
																	<td class="tright"><fmt:formatNumber type="number"
																			minFractionDigits="2" maxFractionDigits="2"
																			value="${(openiBalanceOfSubLedger)+(total_creditOfSubLedger-total_debitOfSubLedger)}" />
																	</td>
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
												<c:if test="${obalanceBeStartDate.ledgerformlist.size()>0}">
													<c:forEach var="ledgerform6"
														items="${obalanceBeStartDate.ledgerformlist}">
														<c:set var="isNotPresent" value="0" />
														<c:set var="isPresent" value="0" />
														<c:if test="${ledgerform6.subgroupName==accSubGroup}">
															<c:if test="${obalance.ledgerformlist.size()>0}">

																<c:forEach var="ledgerform7"
																	items="${obalance.ledgerformlist}">
																	<c:if
																		test="${ledgerform7.ledgerName==ledgerform6.ledgerName}">
																		<c:set var="isPresent" value="1" />
																	</c:if>
																</c:forEach>
																<c:if test="${isPresent==0}">
																	<c:if test="${obalance.ledgerformlist.size()>0}">
																		<c:forEach var="ledgerform7"
																			items="${obalance.ledgerformlist}">
																			<c:if
																				test="${ledgerform7.ledgerName!=ledgerform6.ledgerName}">
																				<c:if test="${isNotPresent==0}">
																					<c:set var="openiBalanceOfLedger" value="0" />
																					<tr class='n-style'>
																					<%-- 	<td style="text-align: left;"><h5>${rowcount}</h5></td>
																					 --%>	<td style="text-align: left;">${emptyString}${emptyString}${ledgerform6.ledgerName}</td>
																						
																						<c:set var="openiBalanceOfLedger"
																							value="${openiBalanceOfLedger + (ledgerform6.ledgercredit_balance-ledgerform6.ledgerdebit_balance)}" />
																						<td class="tright"><fmt:formatNumber
																								type="number" minFractionDigits="2"
																								maxFractionDigits="2"
																								value="${openiBalanceOfLedger}" /></td>
																						<td class="tright"><fmt:formatNumber
																								type="number" minFractionDigits="2"
																								maxFractionDigits="2" value="0" /></td>
																						<td class="tright"><fmt:formatNumber
																								type="number" minFractionDigits="2"
																								maxFractionDigits="2" value="0" /></td>
																						<td class="tright"><fmt:formatNumber
																								type="number" minFractionDigits="2"
																								maxFractionDigits="2"
																								value="${openiBalanceOfLedger}" /></td>
																					</tr>
																					<c:if test="${ledgerform6.subledgerList.size()>0}">


																						<c:forEach var="subledger1"
																							items="${ledgerform6.subledgerList}">
																							<c:if test="${subledger1!=null}">
																								<c:set var="openiBalanceOfSubLedger" value="0" />
																								<c:set var="openiBalanceOfSubLedger"
																									value="${openiBalanceOfSubLedger + (subledger1.credit_balance-subledger1.debit_balance)}" />
																								<c:if test="${openiBalanceOfSubLedger!=0}">
																									<tr class='n-style'>
																									<%-- 	<td style="text-align: left;"><h5>${rowcount}</h5></td>
																									 --%>		<td style="text-align: left;">${emptyString}${emptyString}${emptyString}${subledger1.subledgerName}</td>
																									
																										<td class="tright"><fmt:formatNumber
																												type="number" minFractionDigits="2"
																												maxFractionDigits="2"
																												value="${openiBalanceOfSubLedger}" /></td>
																										<td class="tright"><fmt:formatNumber
																												type="number" minFractionDigits="2"
																												maxFractionDigits="2" value="0" /></td>
																										<td class="tright"><fmt:formatNumber
																												type="number" minFractionDigits="2"
																												maxFractionDigits="2" value="0" /></td>
																										<td class="tright"><fmt:formatNumber
																												type="number" minFractionDigits="2"
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
																<tr class='n-style'>
																	<%-- <td style="text-align: left;"><h5>${rowcount}</h5></td>
																 --%>	<td style="text-align: left;">${emptyString}${emptyString}${ledgerform6.ledgerName}</td>
																
																	<c:set var="openiBalanceOfLedger"
																		value="${openiBalanceOfLedger + (ledgerform6.ledgercredit_balance-ledgerform6.ledgerdebit_balance)}" />
																	<td class="tright"><fmt:formatNumber type="number"
																			minFractionDigits="2" maxFractionDigits="2"
																			value="${openiBalanceOfLedger}" /></td>
																	<td class="tright"><fmt:formatNumber type="number"
																			minFractionDigits="2" maxFractionDigits="2" value="0" /></td>
																	<td class="tright"><fmt:formatNumber type="number"
																			minFractionDigits="2" maxFractionDigits="2" value="0" /></td>
																	<td class="tright"><fmt:formatNumber type="number"
																			minFractionDigits="2" maxFractionDigits="2"
																			value="${openiBalanceOfLedger}" /></td>
																</tr>
																<c:if test="${ledgerform6.subledgerList.size()>0}">


																	<c:forEach var="subledger1"
																		items="${ledgerform6.subledgerList}">
																		<c:if test="${subledger1!=null}">
																			<c:set var="openiBalanceOfSubLedger" value="0" />
																			<c:set var="openiBalanceOfSubLedger"
																				value="${openiBalanceOfSubLedger + (subledger1.credit_balance-subledger1.debit_balance)}" />

																			<c:if test="${openiBalanceOfSubLedger!=0}">
																				<tr class='n-style'>
																			<%-- <td style="text-align: left;"><h5>${rowcount}</h5></td>
																					 --%>	<td style="text-align: left;">${emptyString}${emptyString}${emptyString}${subledger1.subledgerName}</td>
																				
																					<td class="tright"><fmt:formatNumber
																							type="number" minFractionDigits="2"
																							maxFractionDigits="2"
																							value="${openiBalanceOfSubLedger}" /></td>
																					<td class="tright"><fmt:formatNumber
																							type="number" minFractionDigits="2"
																							maxFractionDigits="2" value="0" /></td>
																					<td class="tright"><fmt:formatNumber
																							type="number" minFractionDigits="2"
																							maxFractionDigits="2" value="0" /></td>
																					<td class="tright"><fmt:formatNumber
																							type="number" minFractionDigits="2"
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
			</c:forEach>

		<tr><td>Income Total</td> 
<td class="tright"><fmt:formatNumber type="number"
																	minFractionDigits="2" maxFractionDigits="2"
																	value="${(Income_total)}" /></td>
																	<td></td><td></td>
																	<td class="tright"><fmt:formatNumber type="number"
																	minFractionDigits="2" maxFractionDigits="2"
																	value="${(Income_CB_total)}" /></td>
</tr>	
			<c:forEach var="obalance" items="${subOpeningList}">
				<!--  start of group -->
				<c:if test="${obalance.group.postingSide.posting_id ==1}">

					<c:set var="openingBalanceforGroup" value="0" />
					<c:set var="total_debitGroup" value="0" />
					<c:set var="total_creditGroup" value="0" />

					<c:forEach var="obalanceBeStartDate"
						items="${subOpeningListBeforeStartDate}">
						<c:if
							test="${obalanceBeStartDate.accountGroupName==obalance.accountGroupName}">
							<c:set var="openingBalanceforGroup"
								value="${obalanceBeStartDate.totaldebit_balance-obalanceBeStartDate.totalcredit_balance}" />
							<c:set var="total_debitGroup"
								value="${obalance.totaldebit_balance}" />
							<c:set var="total_creditGroup"
								value="${obalance.totalcredit_balance}" />
						</c:if>
					</c:forEach>
<c:set var="grandtotal_credit_group" value="${grandtotal_credit_group +total_creditGroup}" />
				<c:set var="grandtotal_debit_group" value="${grandtotal_debit_group +total_debitGroup}" />
					<c:if
						test="${openingBalanceforGroup!=0 || total_debitGroup!=0 || total_creditGroup!=0}">
						<tr class='n-style'>
<c:set var="Expenses_total" value="${Expenses_total+openingBalanceforGroup}" />
<c:set var="Expenses_CB" value="${(openingBalanceforGroup)+(total_debitGroup-total_creditGroup)}" />
<c:set var="Expenses_CB_total" value="${Expenses_CB_total+Expenses_CB}" />

							<td style="text-align: left;"><h5>${obalance.accountGroupName}</h5></td>
							<td class="tright"><fmt:formatNumber type="number"
									minFractionDigits="2" maxFractionDigits="2"
									value="${openingBalanceforGroup}" /></td>
							<td class="tright"><fmt:formatNumber type="number"
									minFractionDigits="2" maxFractionDigits="2"
									value="${total_debitGroup}" /></td>
							<td class="tright"><fmt:formatNumber type="number"
									minFractionDigits="2" maxFractionDigits="2"
									value="${total_creditGroup}" /></td>
							<td class="tright"><fmt:formatNumber type="number"
									minFractionDigits="2" maxFractionDigits="2"
									value="${(openingBalanceforGroup)+(total_debitGroup-total_creditGroup)}" />
							</td>
						</tr>
					</c:if>


					<!--  start of subgroup -->
					<c:if test="${obalance.accountSubGroupNameList.size()>0}">
						<c:forEach var="accSubGroup"
							items="${obalance.accountSubGroupNameList}">

							<c:set var="total_credit_Subgroup_beforeStartDate" value="0" />
							<c:set var="total_debit_Subgroup_beforeStartDate" value="0" />
							<c:set var="total_credit_Subgroup" value="0" />
							<c:set var="total_debit_Subgroup" value="0" />

							<c:forEach var="obalanceBeStartDate"
								items="${subOpeningListBeforeStartDate}">

								<c:if
									test="${obalanceBeStartDate.accountSubGroupNameList.size()>0}">

									<c:forEach var="subGroupName"
										items="${obalanceBeStartDate.accountSubGroupNameList}">

										<c:if test="${subGroupName==accSubGroup}">
											<c:if test="${obalanceBeStartDate.ledgerformlist.size()>0}">
												<c:forEach var="ledgerform1"
													items="${obalanceBeStartDate.ledgerformlist}">
													<c:if test="${ledgerform1.subgroupName==accSubGroup}">
														<c:set var="total_credit_Subgroup_beforeStartDate"
															value="${total_credit_Subgroup_beforeStartDate + ledgerform1.ledgercredit_balance}" />
														<c:set var="total_debit_Subgroup_beforeStartDate"
															value="${total_debit_Subgroup_beforeStartDate + ledgerform1.ledgerdebit_balance}" />
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
										<c:set var="total_credit_Subgroup"
											value="${total_credit_Subgroup + ledgerform2.ledgercredit_balance}" />
										<c:set var="total_debit_Subgroup"
											value="${total_debit_Subgroup + ledgerform2.ledgerdebit_balance}" />
									</c:if>
								</c:forEach>
							</c:if>

							<c:set var="openingBalanceSubgroup" value="0" />
							<c:set var="total_debitSubgroup" value="0" />
							<c:set var="total_creditSubgroup" value="0" />

							<c:set var="openingBalanceSubgroup"
								value="${total_debit_Subgroup_beforeStartDate-total_credit_Subgroup_beforeStartDate}" />
							<c:set var="total_debitSubgroup" value="${total_debit_Subgroup}" />
							<c:set var="total_creditSubgroup"
								value="${total_credit_Subgroup}" />

							<c:if
								test="${openingBalanceSubgroup!=0 || total_debitSubgroup!=0 || total_creditSubgroup!=0}">
								<tr class='n-style'>
									<%-- //<td style="text-align: left;"><h5>${rowcount}</h5></td> --%>
									<td style="text-align: left;">${emptyString}${accSubGroup}</td>
									<td class="tright"><fmt:formatNumber type="number"
											minFractionDigits="2" maxFractionDigits="2"
											value="${openingBalanceSubgroup}" /></td>
									<td class="tright"><fmt:formatNumber type="number"
											minFractionDigits="2" maxFractionDigits="2"
											value="${total_debitSubgroup}" /></td>
									<td class="tright"><fmt:formatNumber type="number"
											minFractionDigits="2" maxFractionDigits="2"
											value="${total_creditSubgroup}" /></td>
									<td class="tright"><fmt:formatNumber type="number"
											minFractionDigits="2" maxFractionDigits="2"
											value="${(openingBalanceSubgroup)+(total_debitSubgroup-total_creditSubgroup)}" /></td>
								</tr>


								<!--  start of ledger -->
								<c:if test="${obalance.ledgerformlist.size()>0}">
									<c:forEach var="ledgerform3" items="${obalance.ledgerformlist}">
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
																			<c:if test="${ledgerform4.subgroupName==accSubGroup}">
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
												<%-- 		<td style="text-align: left;"><h5>${rowcount}</h5></td> --%>
														<td style="text-align: left;">${emptyString}${emptyString}${ledgerform3.ledgerName}</td>
													
														<td class="tright"><fmt:formatNumber type="number"
																minFractionDigits="2" maxFractionDigits="2"
																value="${openiBalanceOfLedger}" /></td>
														<td class="tright"><fmt:formatNumber type="number"
																minFractionDigits="2" maxFractionDigits="2"
																value="${total_debitOfLedger}" /></td>
														<td class="tright"><fmt:formatNumber type="number"
																minFractionDigits="2" maxFractionDigits="2"
																value="${total_creditOfLedger}" /></td>
														<td class="tright"><fmt:formatNumber type="number"
																minFractionDigits="2" maxFractionDigits="2"
																value="${(openiBalanceOfLedger)+(total_debitOfLedger-total_creditOfLedger)}" />
														</td>
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
																	<%-- <td style="text-align: left;"><h5>${rowcount}</h5></td>
																	 --%><td style="text-align: left;">${emptyString}${emptyString}${emptyString}${subledger.subledgerName}</td>
																	
																	<td class="tright"><fmt:formatNumber type="number"
																			minFractionDigits="2" maxFractionDigits="2"
																			value="${openiBalanceOfSubLedger}" /></td>
																	<td class="tright"><fmt:formatNumber type="number"
																			minFractionDigits="2" maxFractionDigits="2"
																			value="${total_debitOfSubLedger}" /> <c:set
																			var="row_count_debit"
																			value="${row_count_debit + total_debitOfSubLedger}" />
																	</td>
																	<td class="tright"><fmt:formatNumber type="number"
																			minFractionDigits="2" maxFractionDigits="2"
																			value="${total_creditOfSubLedger}" /> <c:set
																			var="row_count_credit"
																			value="${row_count_credit + total_creditOfSubLedger}" />
																	</td>
																	<td class="tright"><fmt:formatNumber type="number"
																			minFractionDigits="2" maxFractionDigits="2"
																			value="${(openiBalanceOfSubLedger)+(total_debitOfSubLedger-total_creditOfSubLedger)}" />
																	</td>
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
												<c:if test="${obalanceBeStartDate.ledgerformlist.size()>0}">
													<c:forEach var="ledgerform6"
														items="${obalanceBeStartDate.ledgerformlist}">
														<c:set var="isNotPresent" value="0" />
														<c:set var="isPresent" value="0" />
														<c:if test="${ledgerform6.subgroupName==accSubGroup}">
															<c:if test="${obalance.ledgerformlist.size()>0}">

																<c:forEach var="ledgerform7"
																	items="${obalance.ledgerformlist}">
																	<c:if
																		test="${ledgerform7.ledgerName==ledgerform6.ledgerName}">
																		<c:set var="isPresent" value="1" />
																	</c:if>
																</c:forEach>
																<c:if test="${isPresent==0}">
																	<c:if test="${obalance.ledgerformlist.size()>0}">
																		<c:forEach var="ledgerform7"
																			items="${obalance.ledgerformlist}">
																			<c:if
																				test="${ledgerform7.ledgerName!=ledgerform6.ledgerName}">
																				<c:if test="${isNotPresent==0}">
																					<c:set var="openiBalanceOfLedger" value="0" />
																					<tr class='n-style'>
																					<%-- 	<td style="text-align: left;"><h5>${rowcount}</h5></td>
																					 --%>	<td style="text-align: left;">${emptyString}${emptyString}${ledgerform6.ledgerName}</td>
																						
																						<c:set var="openiBalanceOfLedger"
																							value="${openiBalanceOfLedger + (ledgerform6.ledgerdebit_balance-ledgerform6.ledgercredit_balance)}" />
																						<td class="tright"><fmt:formatNumber
																								type="number" minFractionDigits="2"
																								maxFractionDigits="2"
																								value="${openiBalanceOfLedger}" /></td>
																						<td class="tright"><fmt:formatNumber
																								type="number" minFractionDigits="2"
																								maxFractionDigits="2" value="0" /></td>
																						<td class="tright"><fmt:formatNumber
																								type="number" minFractionDigits="2"
																								maxFractionDigits="2" value="0" /></td>
																						<td class="tright"><fmt:formatNumber
																								type="number" minFractionDigits="2"
																								maxFractionDigits="2"
																								value="${openiBalanceOfLedger}" /></td>
																					</tr>
																					<c:if test="${ledgerform6.subledgerList.size()>0}">


																						<c:forEach var="subledger1"
																							items="${ledgerform6.subledgerList}">
																							<c:if test="${subledger1!=null}">
																								<c:set var="openiBalanceOfSubLedger" value="0" />
																								<c:set var="openiBalanceOfSubLedger"
																									value="${openiBalanceOfSubLedger + (subledger1.debit_balance-subledger1.credit_balance)}" />
																								<c:if test="${openiBalanceOfSubLedger!=0}">
																									<tr class='n-style'>
																									<%-- 	<td style="text-align: left;"><h5>${rowcount}</h5></td>
																									 --%>			<td style="text-align: left;">${emptyString}${emptyString}${emptyString}${subledger1.subledgerName}</td>
																										
																										<td class="tright"><fmt:formatNumber
																												type="number" minFractionDigits="2"
																												maxFractionDigits="2"
																												value="${openiBalanceOfSubLedger}" /></td>
																										<td class="tright"><fmt:formatNumber
																												type="number" minFractionDigits="2"
																												maxFractionDigits="2" value="0" /></td>
																										<td class="tright"><fmt:formatNumber
																												type="number" minFractionDigits="2"
																												maxFractionDigits="2" value="0" /></td>
																										<td class="tright"><fmt:formatNumber
																												type="number" minFractionDigits="2"
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
																<tr class='n-style'>
																<%-- 	<td style="text-align: left;"><h5>${rowcount}</h5></td>
																	 --%>	<td style="text-align: left;">${emptyString}${emptyString}${ledgerform6.ledgerName}</td>
																
																	<c:set var="openiBalanceOfLedger"
																		value="${openiBalanceOfLedger + (ledgerform6.ledgerdebit_balance-ledgerform6.ledgercredit_balance)}" />
																	<td class="tright"><fmt:formatNumber type="number"
																			minFractionDigits="2" maxFractionDigits="2"
																			value="${openiBalanceOfLedger}" /></td>
																	<td class="tright"><fmt:formatNumber type="number"
																			minFractionDigits="2" maxFractionDigits="2" value="0" /></td>
																	<td class="tright"><fmt:formatNumber type="number"
																			minFractionDigits="2" maxFractionDigits="2" value="0" /></td>
																	<td class="tright"><fmt:formatNumber type="number"
																			minFractionDigits="2" maxFractionDigits="2"
																			value="${openiBalanceOfLedger}" /></td>
																</tr>
																<c:if test="${ledgerform6.subledgerList.size()>0}">


																	<c:forEach var="subledger1"
																		items="${ledgerform6.subledgerList}">
																		<c:if test="${subledger1!=null}">
																			<c:set var="openiBalanceOfSubLedger" value="0" />
																			<c:set var="openiBalanceOfSubLedger"
																				value="${openiBalanceOfSubLedger + (subledger1.debit_balance-subledger1.credit_balance)}" />

																			<c:if test="${openiBalanceOfSubLedger!=0}">
																				<tr class='n-style'>
																					<td style="text-align: left;"><h5>${rowcount}</h5></td>
																						<td style="text-align: left;">${emptyString}${emptyString}${emptyString}${subledger1.subledgerName}</td>
																				
																					<td class="tright"><fmt:formatNumber
																							type="number" minFractionDigits="2"
																							maxFractionDigits="2"
																							value="${openiBalanceOfSubLedger}" /></td>
																					<td class="tright"><fmt:formatNumber
																							type="number" minFractionDigits="2"
																							maxFractionDigits="2" value="0" /></td>
																					<td class="tright"><fmt:formatNumber
																							type="number" minFractionDigits="2"
																							maxFractionDigits="2" value="0" /></td>
																					<td class="tright"><fmt:formatNumber
																							type="number" minFractionDigits="2"
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
			</c:forEach>
<c:set var="Liability_total" value="${Liability_total - Expenses_total}" />
<c:set var="Liability_CB_total" value="${Liability_CB_total - Expenses_CB_total}" />
		<tr><td>Expense Total</td> 
<td class="tright"><fmt:formatNumber type="number"
																	minFractionDigits="2" maxFractionDigits="2"
																	value="${(Expenses_total)}" /></td>
																	<td></td><td></td>
																	<td class="tright"><fmt:formatNumber type="number"
																	minFractionDigits="2" maxFractionDigits="2"
																	value="${(Expenses_CB_total)}" /></td>
</tr>
		</c:if>

		</tbody>

		<tfoot style='background-color: #eee'>
			<tr>
			<tr><td> Liability Toatal</td> <td class="tright"><fmt:formatNumber type="number"
																	minFractionDigits="2" maxFractionDigits="2"
																	value="${(Liability_total) }" /></td>
																	<td></td><td></td>
																	<td class="tright"><fmt:formatNumber type="number"
																	minFractionDigits="2" maxFractionDigits="2"
																	value="${(Liability_CB_total)}" /></td>
																	</tr>
			
			<tr>
				<td>Total</td>
				<td></td>
			<%-- 	<c:forEach var="obalanceBeStartDate"
					items="${subOpeningListBeforeStartDate}">
					<c:set var="row_count_credit"
						value="${row_count_credit + obalanceBeStartDate.totalcredit_balance}" />
					<c:set var="row_count_debit"
						value="${row_count_debit + obalanceBeStartDate.totaldebit_balance}" />
				</c:forEach> --%>
				<%--<c:set var="grandtotal_credit_group" value="${grandtotal_credit_group +total_creditGroup}" />
				<c:set var="grandtotal_debit_group" value="${grandtotal_debit_group +total_debitGroup}" />
				 <td class="tright"><fmt:formatNumber type="number"
						minFractionDigits="2" maxFractionDigits="2"
						value="${row_count_debit+total_debit_Supplier_beforeStartDate+total_debit_Customer_beforeStartDate+total_debit_Bank_beforeStartDate}" /></td>
				<td class="tright"><fmt:formatNumber type="number"
						minFractionDigits="2" maxFractionDigits="2"
						value="${row_count_credit+total_credit_Supplier_beforeStartDate+total_credit_Customer_beforeStartDate+total_credit_Bank_beforeStartDate}" /></td>
				<td></td> --%>
				<td class="tright"><fmt:formatNumber type="number"
						minFractionDigits="2" maxFractionDigits="2"
						value="${grandtotal_debit_group}" /></td>
				<td class="tright"><fmt:formatNumber type="number"
						minFractionDigits="2" maxFractionDigits="2"
						value="${grandtotal_credit_group}" /></td>
				<td></td>
			</tr>

		</tfoot>
		</table>

	</div>





	<!-- pdf ended -->

	<div class="borderForm">
		<table id="table" data-toggle="table" data-search="false"
			data-escape="false" data-filter-control="true"
			data-show-export="false" data-click-to-select="true"
			data-pagination="true" data-page-size="10" data-toolbar="#toolbar"
			class="table">
			<thead>
				<tr>
					<th data-field="particulars" data-filter-control="input"
						data-sortable="true">Particulars</th>
					<th data-field="openingbalance" data-filter-control="input"
						data-sortable="true">Opening Balance</th>
					<th data-field="debit" data-filter-control="input"
						data-sortable="true">Debit</th>
					<th data-field="credit" data-filter-control="input"
						data-sortable="true">Credit</th>
					<th data-field="closingbalance" data-filter-control="input"
						data-sortable="true">Closing Balance</th>
				</tr>
			</thead>
			<tbody>
				<c:set var="row_count_credit" value="0" />
				<c:set var="row_count_debit" value="0" />

				<c:set var="total_credit_Supplier_beforeStartDate" value="0" />
				<c:set var="total_debit_Supplier_beforeStartDate" value="0" />
				<c:set var="grandtotal_credit_group" value="0" />
				<c:set var="grandtotal_debit_group" value="0" />
				
																					<c:set var="Asset_total" value="0" />
				<c:set var="Liability_total" value="0" />

				<c:set var="Income_total" value="0" />
				<c:set var="Expenses_total" value="0" />
				
				<c:set var="Asset_CB_total" value="0" />
				<c:set var="Liability_CB_total" value="0" />

				<c:set var="Income_CB_total" value="0" />
				<c:set var="Expenses_CB_total" value="0" />
				
				<c:forEach var="obalance"
					items="${supplierOpeningBalanceBeforeStartDate}">
					<c:set var="total_debit_Supplier_beforeStartDate"
						value="${total_debit_Supplier_beforeStartDate + obalance.debit_balance}" />
					<c:set var="total_credit_Supplier_beforeStartDate"
						value="${total_credit_Supplier_beforeStartDate + obalance.credit_balance}" />
				</c:forEach>

				<c:set var="total_credit_Supplier" value="0" />
				<c:set var="total_debit_Supplier" value="0" />

				<c:forEach var="obalance" items="${supplierOpeningBalanceList}">
					<c:set var="total_debit_Supplier"
						value="${total_debit_Supplier + obalance.debit_balance}" />
					<c:set var="total_credit_Supplier"
						value="${total_credit_Supplier + obalance.credit_balance}" />
				</c:forEach>

				<c:set var="total_credit_Customer_beforeStartDate" value="0" />
				<c:set var="total_debit_Customer_beforeStartDate" value="0" />


				<c:forEach var="obalance"
					items="${customerOpeningBalanceBeforeStartDate}">
					<c:set var="total_debit_Customer_beforeStartDate"
						value="${total_debit_Customer_beforeStartDate + obalance.debit_balance}" />
					<c:set var="total_credit_Customer_beforeStartDate"
						value="${total_credit_Customer_beforeStartDate + obalance.credit_balance}" />
				</c:forEach>

				<c:set var="total_credit_Customer" value="0" />
				<c:set var="total_debit_Customer" value="0" />

				<c:forEach var="obalance" items="${customerOpeningBalanceList}">
					<c:set var="total_debit_Customer"
						value="${total_debit_Customer + obalance.debit_balance}" />
					<c:set var="total_credit_Customer"
						value="${total_credit_Customer + obalance.credit_balance}" />
				</c:forEach>


				<c:set var="total_credit_Bank_beforeStartDate" value="0" />
				<c:set var="total_debit_Bank_beforeStartDate" value="0" />

				<c:forEach var="obalance"
					items="${bankOpeningBalanceBeforeStartDate}">
					<c:set var="total_debit_Bank_beforeStartDate"
						value="${total_debit_Bank_beforeStartDate + obalance.debit_balance}" />
					<c:set var="total_credit_Bank_beforeStartDate"
						value="${total_credit_Bank_beforeStartDate + obalance.credit_balance}" />
				</c:forEach>

				<c:set var="total_credit_Bank" value="0" />
				<c:set var="total_debit_Bank" value="0" />

				<c:forEach var="obalance" items="${bankOpeningBalanceList}">
					<c:set var="total_debit_Bank"
						value="${total_debit_Bank + obalance.debit_balance}" />
					<c:set var="total_credit_Bank"
						value="${total_credit_Bank + obalance.credit_balance}" />
				</c:forEach>


				<c:if test="${subOpeningList.size()>0}">

					<c:forEach var="obalance" items="${subOpeningList}">
					<!-- <% out.println("accountGroupName"); %> -->
						<!--  start of group -->
						<c:if test="${obalance.group.postingSide.posting_id == 3}">

							<c:set var="openingBalanceforGroup" value="0" />
							<c:set var="total_debitGroup" value="0" />
							<c:set var="total_creditGroup" value="0" />

							<c:forEach var="obalanceBeStartDate"
								items="${subOpeningListBeforeStartDate}">
								<c:if
									test="${obalanceBeStartDate.accountGroupName==obalance.accountGroupName}">
									<c:choose>
										<c:when test="${obalance.accountGroupName==group1}">


											<c:set var="openingBalanceforGroup"
												value="${(obalanceBeStartDate.totaldebit_balance+total_debit_Customer_beforeStartDate+total_debit_Bank_beforeStartDate)-(obalanceBeStartDate.totalcredit_balance+total_credit_Customer_beforeStartDate+total_credit_Bank_beforeStartDate)}" />
											<c:set var="total_debitGroup"
												value="${(obalance.totaldebit_balance+total_debit_Customer+total_debit_Bank)}" />
											<c:set var="total_creditGroup"
												value="${(obalance.totalcredit_balance+total_credit_Customer+total_credit_Bank)}" />

										</c:when>
										<c:otherwise>
											<c:set var="openingBalanceforGroup"
												value="${obalanceBeStartDate.totaldebit_balance-obalanceBeStartDate.totalcredit_balance}" />
											<c:set var="total_debitGroup"
												value="${obalance.totaldebit_balance}" />
											<c:set var="total_creditGroup"
												value="${obalance.totalcredit_balance}" />

										</c:otherwise>
									</c:choose>
								</c:if>
							</c:forEach>
<c:set var="grandtotal_credit_group" value="${grandtotal_credit_group +total_creditGroup}" />
				<c:set var="grandtotal_debit_group" value="${grandtotal_debit_group +total_debitGroup}" />
						<c:if
								test="${openingBalanceforGroup!=0 || total_debitGroup!=0 || total_creditGroup!=0}">
							<c:set var="Asset_total" value="${Asset_total+openingBalanceforGroup}" />
							<c:set var="Asset_CB" value="${(openingBalanceforGroup)+(total_debitGroup-total_creditGroup)}"/>
							<c:set var="Asset_CB_total" value="${Asset_CB_total+Asset_CB}" />
								<tr class='n-style'>
									<td style="text-align: left;"><h5>${obalance.accountGroupName}</h5></td>
									<td class="tright"><fmt:formatNumber type="number"
											minFractionDigits="2" maxFractionDigits="2"
											value="${openingBalanceforGroup}" /></td>
									<td class="tright"><fmt:formatNumber type="number"
											minFractionDigits="2" maxFractionDigits="2"
											value="${total_debitGroup}" /></td>
									<td class="tright"><fmt:formatNumber type="number"
											minFractionDigits="2" maxFractionDigits="2"
											value="${total_creditGroup}" /></td>
									<td class="tright"><fmt:formatNumber type="number"
											minFractionDigits="2" maxFractionDigits="2"
											value="${(openingBalanceforGroup)+(total_debitGroup-total_creditGroup)}" />
									</td>
								</tr>
							</c:if>


							<!--  start of subgroup -->
							<c:if test="${obalance.accountSubGroupNameList.size()>0}">
								<c:forEach var="accSubGroup"
									items="${obalance.accountSubGroupNameList}">

									<c:set var="total_credit_Subgroup_beforeStartDate" value="0" />
									<c:set var="total_debit_Subgroup_beforeStartDate" value="0" />
									<c:set var="total_credit_Subgroup" value="0" />
									<c:set var="total_debit_Subgroup" value="0" />

									<c:forEach var="obalanceBeStartDate"
										items="${subOpeningListBeforeStartDate}">

										<c:if
											test="${obalanceBeStartDate.accountSubGroupNameList.size()>0}">

											<c:forEach var="subGroupName"
												items="${obalanceBeStartDate.accountSubGroupNameList}">

												<c:if test="${subGroupName==accSubGroup}">
													<c:if test="${obalanceBeStartDate.ledgerformlist.size()>0}">
														<c:forEach var="ledgerform1"
															items="${obalanceBeStartDate.ledgerformlist}">
															<c:if test="${ledgerform1.subgroupName==accSubGroup}">
																<c:set var="total_credit_Subgroup_beforeStartDate"
																	value="${total_credit_Subgroup_beforeStartDate + ledgerform1.ledgercredit_balance}" />
																<c:set var="total_debit_Subgroup_beforeStartDate"
																	value="${total_debit_Subgroup_beforeStartDate + ledgerform1.ledgerdebit_balance}" />
															</c:if>
														</c:forEach>
													</c:if>
												</c:if>

											</c:forEach>

										</c:if>

									</c:forEach>

									<c:if test="${obalance.ledgerformlist.size()>0}">
										<c:forEach var="ledgerform2"
											items="${obalance.ledgerformlist}">
											<c:if test="${ledgerform2.subgroupName==accSubGroup}">
												<c:set var="total_credit_Subgroup"
													value="${total_credit_Subgroup + ledgerform2.ledgercredit_balance}" />
												<c:set var="total_debit_Subgroup"
													value="${total_debit_Subgroup + ledgerform2.ledgerdebit_balance}" />
											</c:if>
										</c:forEach>
									</c:if>


									<c:set var="openingBalanceSubgroup" value="0" />
									<c:set var="total_debitSubgroup" value="0" />
									<c:set var="total_creditSubgroup" value="0" />

									<c:choose>
										<c:when
											test="${(accSubGroup==subGroup2)||(accSubGroup==subGroup3)}">

											<c:if test="${accSubGroup==subGroup2}">
												<c:set var="openingBalanceSubgroup"
													value="${(total_debit_Subgroup_beforeStartDate+total_debit_Customer_beforeStartDate)-(total_credit_Subgroup_beforeStartDate+total_credit_Customer_beforeStartDate)}" />
												<c:set var="total_debitSubgroup"
													value="${(total_debit_Subgroup+total_debit_Customer)}" />
												<c:set var="total_creditSubgroup"
													value="${(total_credit_Subgroup+total_credit_Customer)}" />
											</c:if>

											<c:if test="${accSubGroup==subGroup3}">
												<c:set var="openingBalanceSubgroup"
													value="${(total_debit_Subgroup_beforeStartDate+total_debit_Bank_beforeStartDate)-(total_credit_Subgroup_beforeStartDate+total_credit_Bank_beforeStartDate)}" />
												<c:set var="total_debitSubgroup"
													value="${(total_debit_Subgroup+total_debit_Bank)}" />
												<c:set var="total_creditSubgroup"
													value="${(total_credit_Subgroup+total_credit_Bank)}" />

											</c:if>

										</c:when>
										<c:otherwise>
											<c:set var="openingBalanceSubgroup"
												value="${total_debit_Subgroup_beforeStartDate-total_credit_Subgroup_beforeStartDate}" />
											<c:set var="total_debitSubgroup"
												value="${total_debit_Subgroup}" />
											<c:set var="total_creditSubgroup"
												value="${total_credit_Subgroup}" />
										</c:otherwise>
									</c:choose>

									<c:if
										test="${openingBalanceSubgroup!=0 || total_debitSubgroup!=0 || total_creditSubgroup!=0}">
										<tr class='n-style'>
											<td style="text-align: left;">&nbsp;&nbsp;&nbsp;&nbsp;${accSubGroup}</td>
											<td class="tright"><fmt:formatNumber type="number"
													minFractionDigits="2" maxFractionDigits="2"
													value="${openingBalanceSubgroup}" /></td>
											<td class="tright"><fmt:formatNumber type="number"
													minFractionDigits="2" maxFractionDigits="2"
													value="${total_debitSubgroup}" /></td>
											<td class="tright"><fmt:formatNumber type="number"
													minFractionDigits="2" maxFractionDigits="2"
													value="${total_creditSubgroup}" /></td>
											<td class="tright"><fmt:formatNumber type="number"
													minFractionDigits="2" maxFractionDigits="2"
													value="${(openingBalanceSubgroup)+(total_debitSubgroup-total_creditSubgroup)}" />
											</td>
										</tr>


										<!--  start of ledger -->
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
																<td class="tright"><fmt:formatNumber type="number"
																		minFractionDigits="2" maxFractionDigits="2"
																		value="${openiBalanceOfLedger}" /></td>
																<td class="tright"><fmt:formatNumber type="number"
																		minFractionDigits="2" maxFractionDigits="2"
																		value="${total_debitOfLedger}" /></td>
																<td class="tright"><fmt:formatNumber type="number"
																		minFractionDigits="2" maxFractionDigits="2"
																		value="${total_creditOfLedger}" /></td>
																<td class="tright"><fmt:formatNumber type="number"
																		minFractionDigits="2" maxFractionDigits="2"
																		value="${(openiBalanceOfLedger)+(total_debitOfLedger-total_creditOfLedger)}" />
																</td>
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
																			<td class="tright"><fmt:formatNumber
																					type="number" minFractionDigits="2"
																					maxFractionDigits="2"
																					value="${openiBalanceOfSubLedger}" /></td>
																			<td class="tright"><fmt:formatNumber
																					type="number" minFractionDigits="2"
																					maxFractionDigits="2"
																					value="${total_debitOfSubLedger}" /> <c:set
																					var="row_count_debit"
																					value="${row_count_debit + total_debitOfSubLedger}" />
																			</td>
																			<td class="tright"><fmt:formatNumber
																					type="number" minFractionDigits="2"
																					maxFractionDigits="2"
																					value="${total_creditOfSubLedger}" /> <c:set
																					var="row_count_credit"
																					value="${row_count_credit + total_creditOfSubLedger}" />
																			</td>
																			<td class="tright"><fmt:formatNumber
																					type="number" minFractionDigits="2"
																					maxFractionDigits="2"
																					value="${(openiBalanceOfSubLedger)+(total_debitOfSubLedger-total_creditOfSubLedger)}" />
																			</td>
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
																<c:if test="${ledgerform6.subgroupName==accSubGroup}">
																	<c:if test="${obalance.ledgerformlist.size()>0}">

																		<c:forEach var="ledgerform7"
																			items="${obalance.ledgerformlist}">
																			<c:if
																				test="${ledgerform7.ledgerName==ledgerform6.ledgerName}">
																				<c:set var="isPresent" value="1" />
																			</c:if>
																		</c:forEach>
																		<c:if test="${isPresent==0}">
																			<c:if test="${obalance.ledgerformlist.size()>0}">
																				<c:forEach var="ledgerform7"
																					items="${obalance.ledgerformlist}">
																					<c:if
																						test="${ledgerform7.ledgerName!=ledgerform6.ledgerName}">
																						<c:if test="${isNotPresent==0}">
																							<c:set var="openiBalanceOfLedger" value="0" />
																							<tr class='n-style'>
																								<td style="text-align: left;">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;${ledgerform6.ledgerName}</td>
																								<c:set var="openiBalanceOfLedger"
																									value="${openiBalanceOfLedger + (ledgerform6.ledgerdebit_balance-ledgerform6.ledgercredit_balance)}" />
																								<td class="tright"><fmt:formatNumber
																										type="number" minFractionDigits="2"
																										maxFractionDigits="2"
																										value="${openiBalanceOfLedger}" /></td>
																								<td class="tright"><fmt:formatNumber
																										type="number" minFractionDigits="2"
																										maxFractionDigits="2" value="0" /></td>
																								<td class="tright"><fmt:formatNumber
																										type="number" minFractionDigits="2"
																										maxFractionDigits="2" value="0" /></td>
																								<td class="tright"><fmt:formatNumber
																										type="number" minFractionDigits="2"
																										maxFractionDigits="2"
																										value="${openiBalanceOfLedger}" /></td>
																							</tr>
																							<c:if
																								test="${ledgerform6.subledgerList.size()>0}">


																								<c:forEach var="subledger1"
																									items="${ledgerform6.subledgerList}">
																									<c:if test="${subledger1!=null}">
																										<c:set var="openiBalanceOfSubLedger" value="0" />
																										<c:set var="openiBalanceOfSubLedger"
																											value="${openiBalanceOfSubLedger + (subledger1.debit_balance-subledger1.credit_balance)}" />

																										<c:if test="${openiBalanceOfSubLedger!=0}">
																											<tr class='n-style'>
																												<td style="text-align: left;">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;${subledger1.subledgerName}</td>
																												<td class="tright"><fmt:formatNumber
																														type="number" minFractionDigits="2"
																														maxFractionDigits="2"
																														value="${openiBalanceOfSubLedger}" /></td>
																												<td class="tright"><fmt:formatNumber
																														type="number" minFractionDigits="2"
																														maxFractionDigits="2" value="0" /></td>
																												<td class="tright"><fmt:formatNumber
																														type="number" minFractionDigits="2"
																														maxFractionDigits="2" value="0" /></td>
																												<td class="tright"><fmt:formatNumber
																														type="number" minFractionDigits="2"
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
																		<tr class='n-style'>
																			<td style="text-align: left;">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;${ledgerform6.ledgerName}</td>
																			<c:set var="openiBalanceOfLedger"
																				value="${openiBalanceOfLedger + (ledgerform6.ledgerdebit_balance-ledgerform6.ledgercredit_balance)}" />
																			<td class="tright"><fmt:formatNumber
																					type="number" minFractionDigits="2"
																					maxFractionDigits="2"
																					value="${openiBalanceOfLedger}" /></td>
																			<td class="tright"><fmt:formatNumber
																					type="number" minFractionDigits="2"
																					maxFractionDigits="2" value="0" /></td>
																			<td class="tright"><fmt:formatNumber
																					type="number" minFractionDigits="2"
																					maxFractionDigits="2" value="0" /></td>
																			<td class="tright"><fmt:formatNumber
																					type="number" minFractionDigits="2"
																					maxFractionDigits="2"
																					value="${openiBalanceOfLedger}" /></td>
																		</tr>
																		<c:if test="${ledgerform6.subledgerList.size()>0}">


																			<c:forEach var="subledger1"
																				items="${ledgerform6.subledgerList}">
																				<c:if test="${subledger1!=null}">
																					<c:set var="openiBalanceOfSubLedger" value="0" />
																					<c:set var="openiBalanceOfSubLedger"
																						value="${openiBalanceOfSubLedger + (subledger1.debit_balance-subledger1.credit_balance)}" />

																					<c:if test="${openiBalanceOfSubLedger!=0}">
																						<tr class='n-style'>
																							<td style="text-align: left;">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;${subledger1.subledgerName}</td>
																							<td class="tright"><fmt:formatNumber
																									type="number" minFractionDigits="2"
																									maxFractionDigits="2"
																									value="${openiBalanceOfSubLedger}" /></td>
																							<td class="tright"><fmt:formatNumber
																									type="number" minFractionDigits="2"
																									maxFractionDigits="2" value="0" /></td>
																							<td class="tright"><fmt:formatNumber
																									type="number" minFractionDigits="2"
																									maxFractionDigits="2" value="0" /></td>
																							<td class="tright"><fmt:formatNumber
																									type="number" minFractionDigits="2"
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
														<td class="tright"><fmt:formatNumber type="number"
																minFractionDigits="2" maxFractionDigits="2"
																value="${bankOpeningbalance}" />
														<td class="tright"><fmt:formatNumber type="number"
																minFractionDigits="2" maxFractionDigits="2"
																value="${bankDebitbalance}" /> <c:set
																var="row_count_debit"
																value="${row_count_debit + bankDebitbalance}" /></td>
														<td class="tright"><fmt:formatNumber type="number"
																minFractionDigits="2" maxFractionDigits="2"
																value="${bankCreditbalance}" /> <c:set
																var="row_count_credit"
																value="${row_count_credit + bankCreditbalance}" /></td>
														<td class="tright"><fmt:formatNumber type="number"
																minFractionDigits="2" maxFractionDigits="2"
																value="${(bankOpeningbalance)+(bankDebitbalance-bankCreditbalance)}" />
														</td>
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
															<td class="tright"><fmt:formatNumber type="number"
																	minFractionDigits="2" maxFractionDigits="2"
																	value="${openiBalanceOfbank}" />
															<td class="tright"><fmt:formatNumber type="number"
																	minFractionDigits="2" maxFractionDigits="2" value="0" /></td>
															<td class="tright"><fmt:formatNumber type="number"
																	minFractionDigits="2" maxFractionDigits="2" value="0" /></td>
															<td class="tright"><fmt:formatNumber type="number"
																	minFractionDigits="2" maxFractionDigits="2"
																	value="${(openiBalanceOfbank)}" /></td>
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
														<td class="tright"><fmt:formatNumber type="number"
																minFractionDigits="2" maxFractionDigits="2"
																value="${cusOpeningbalance}" />
														<td class="tright"><fmt:formatNumber type="number"
																minFractionDigits="2" maxFractionDigits="2"
																value="${cusDebitbalance}" /> <c:set
																var="row_count_debit"
																value="${row_count_debit + cusDebitbalance}" /></td>
														<td class="tright"><fmt:formatNumber type="number"
																minFractionDigits="2" maxFractionDigits="2"
																value="${cusCreditbalance}" /> <c:set
																var="row_count_credit"
																value="${row_count_credit + cusCreditbalance}" /></td>
														<td class="tright"><fmt:formatNumber type="number"
																minFractionDigits="2" maxFractionDigits="2"
																value="${(cusOpeningbalance)+(cusDebitbalance-cusCreditbalance)}" />
														</td>
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
															<td class="tright"><fmt:formatNumber type="number"
																	minFractionDigits="2" maxFractionDigits="2"
																	value="${openiBalanceOfcustomer}" />
															<td class="tright"><fmt:formatNumber type="number"
																	minFractionDigits="2" maxFractionDigits="2" value="0" /></td>
															<td class="tright"><fmt:formatNumber type="number"
																	minFractionDigits="2" maxFractionDigits="2" value="0" /></td>
															<td class="tright"><fmt:formatNumber type="number"
																	minFractionDigits="2" maxFractionDigits="2"
																	value="${(openiBalanceOfcustomer)}" /></td>
														</tr>
													</c:if>

												</c:if>

											</c:forEach>
										</c:if>

									</c:if>
								</c:forEach>

							</c:if>

						</c:if>
					</c:forEach>

								<tr><td> <h5>Asset Total</h5></td> 
<td class="tright"><fmt:formatNumber type="number"
																	minFractionDigits="2" maxFractionDigits="2"
																	value="${(Asset_total)}" /></td>
																	<td></td><td></td>
																	<td class="tright"><fmt:formatNumber type="number"
																	minFractionDigits="2" maxFractionDigits="2"
																	value="${(Asset_CB_total)}" /></td>
</tr>
					<c:forEach var="obalance" items="${subOpeningList}">
						<!--  start of group -->

						<c:if test="${obalance.group.postingSide.posting_id == 4}">

							<c:set var="openingBalanceforGroup" value="0" />
							<c:set var="total_debitGroup" value="0" />
							<c:set var="total_creditGroup" value="0" />

							<c:forEach var="obalanceBeStartDate"
								items="${subOpeningListBeforeStartDate}">
								<c:if
									test="${obalanceBeStartDate.accountGroupName==obalance.accountGroupName}">
									<c:choose>
										<c:when test="${obalance.accountGroupName==group4}">

											<c:set var="openingBalanceforGroup"
												value="${(obalanceBeStartDate.totalcredit_balance+total_credit_Supplier_beforeStartDate)-(obalanceBeStartDate.totaldebit_balance+total_debit_Supplier_beforeStartDate)}" />
											<c:set var="total_debitGroup"
												value="${(obalance.totaldebit_balance+total_debit_Supplier)}" />
											<c:set var="total_creditGroup"
												value="${(obalance.totalcredit_balance+total_credit_Supplier)}" />

										</c:when>
										<c:otherwise>
											<c:set var="openingBalanceforGroup"
												value="${obalanceBeStartDate.totalcredit_balance-obalanceBeStartDate.totaldebit_balance}" />
											<c:set var="total_debitGroup"
												value="${obalance.totaldebit_balance}" />
											<c:set var="total_creditGroup"
												value="${obalance.totalcredit_balance}" />
										</c:otherwise>
									</c:choose>
								</c:if>
							</c:forEach>
<c:set var="grandtotal_credit_group" value="${grandtotal_credit_group +total_creditGroup}" />
				<c:set var="grandtotal_debit_group" value="${grandtotal_debit_group +total_debitGroup}" />
						<c:if
								test="${openingBalanceforGroup!=0 || total_debitGroup!=0 || total_creditGroup!=0}">
								<c:set var="Liability_total" value="${Liability_total+openingBalanceforGroup}" />
								<c:set var="Liability_CB" value="${(openingBalanceforGroup)+(total_creditGroup-total_debitGroup)}" />
								<c:set var="Liability_CB_total" value="${Liability_CB_total+Liability_CB}" />
								<tr class='n-style'>
									<td style="text-align: left;"><h5>${obalance.accountGroupName}</h5></td>
									<td class="tright"><fmt:formatNumber type="number"
											minFractionDigits="2" maxFractionDigits="2"
											value="${openingBalanceforGroup}" /></td>
									<td class="tright"><fmt:formatNumber type="number"
											minFractionDigits="2" maxFractionDigits="2"
											value="${total_debitGroup}" /></td>
									<td class="tright"><fmt:formatNumber type="number"
											minFractionDigits="2" maxFractionDigits="2"
											value="${total_creditGroup}" /></td>
									<td class="tright"><fmt:formatNumber type="number"
											minFractionDigits="2" maxFractionDigits="2"
											value="${(openingBalanceforGroup)+(total_creditGroup-total_debitGroup)}" />
									</td>
								</tr>
							</c:if>

							<!--  start of subgroup -->
							<c:if test="${obalance.accountSubGroupNameList.size()>0}">
								<c:forEach var="accSubGroup"
									items="${obalance.accountSubGroupNameList}">

									<c:set var="total_credit_Subgroup_beforeStartDate" value="0" />
									<c:set var="total_debit_Subgroup_beforeStartDate" value="0" />
									<c:set var="total_credit_Subgroup" value="0" />
									<c:set var="total_debit_Subgroup" value="0" />

									<c:forEach var="obalanceBeStartDate"
										items="${subOpeningListBeforeStartDate}">

										<c:if
											test="${obalanceBeStartDate.accountSubGroupNameList.size()>0}">

											<c:forEach var="subGroupName"
												items="${obalanceBeStartDate.accountSubGroupNameList}">

												<c:if test="${subGroupName==accSubGroup}">
													<c:if test="${obalanceBeStartDate.ledgerformlist.size()>0}">
														<c:forEach var="ledgerform1"
															items="${obalanceBeStartDate.ledgerformlist}">
															<c:if test="${ledgerform1.subgroupName==accSubGroup}">
																<c:set var="total_credit_Subgroup_beforeStartDate"
																	value="${total_credit_Subgroup_beforeStartDate + ledgerform1.ledgercredit_balance}" />
																<c:set var="total_debit_Subgroup_beforeStartDate"
																	value="${total_debit_Subgroup_beforeStartDate + ledgerform1.ledgerdebit_balance}" />
															</c:if>
														</c:forEach>
													</c:if>
												</c:if>

											</c:forEach>

										</c:if>

									</c:forEach>

									<c:if test="${obalance.ledgerformlist.size()>0}">
										<c:forEach var="ledgerform2"
											items="${obalance.ledgerformlist}">
											<c:if test="${ledgerform2.subgroupName==accSubGroup}">
												<c:set var="total_credit_Subgroup"
													value="${total_credit_Subgroup + ledgerform2.ledgercredit_balance}" />
												<c:set var="total_debit_Subgroup"
													value="${total_debit_Subgroup + ledgerform2.ledgerdebit_balance}" />
											</c:if>
										</c:forEach>
									</c:if>

									<c:set var="openingBalanceSubgroup" value="0" />
									<c:set var="total_debitSubgroup" value="0" />
									<c:set var="total_creditSubgroup" value="0" />

									<c:choose>
										<c:when test="${accSubGroup==subGroup5}">

											<c:set var="openingBalanceSubgroup"
												value="${(total_credit_Subgroup_beforeStartDate+total_credit_Supplier_beforeStartDate)-(total_debit_Subgroup_beforeStartDate+total_debit_Supplier_beforeStartDate)}" />
											<c:set var="total_debitSubgroup"
												value="${(total_debit_Subgroup+total_debit_Supplier)}" />
											<c:set var="total_creditSubgroup"
												value="${(total_credit_Subgroup+total_credit_Supplier)}" />
										</c:when>
										<c:otherwise>
											<c:set var="openingBalanceSubgroup"
												value="${total_credit_Subgroup_beforeStartDate-total_debit_Subgroup_beforeStartDate}" />
											<c:set var="total_debitSubgroup"
												value="${total_debit_Subgroup}" />
											<c:set var="total_creditSubgroup"
												value="${total_credit_Subgroup}" />
										</c:otherwise>
									</c:choose>

									<c:if
										test="${openingBalanceSubgroup!=0 || total_debitSubgroup!=0 || total_creditSubgroup!=0}">
										<tr class='n-style'>
											<td style="text-align: left;">&nbsp;&nbsp;&nbsp;&nbsp;${accSubGroup}</td>
											<td class="tright"><fmt:formatNumber type="number"
													minFractionDigits="2" maxFractionDigits="2"
													value="${openingBalanceSubgroup}" /></td>
											<td class="tright"><fmt:formatNumber type="number"
													minFractionDigits="2" maxFractionDigits="2"
													value="${total_debitSubgroup}" /></td>
											<td class="tright"><fmt:formatNumber type="number"
													minFractionDigits="2" maxFractionDigits="2"
													value="${total_creditSubgroup}" /></td>
											<td class="tright"><fmt:formatNumber type="number"
													minFractionDigits="2" maxFractionDigits="2"
													value="${(openingBalanceSubgroup)+(total_creditSubgroup-total_debitSubgroup)}" />
											</td>
										</tr>


										<!--  start of ledger -->
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
																<td class="tright"><fmt:formatNumber type="number"
																		minFractionDigits="2" maxFractionDigits="2"
																		value="${openiBalanceOfLedger}" /></td>
																<td class="tright"><fmt:formatNumber type="number"
																		minFractionDigits="2" maxFractionDigits="2"
																		value="${total_debitOfLedger}" /></td>
																<td class="tright"><fmt:formatNumber type="number"
																		minFractionDigits="2" maxFractionDigits="2"
																		value="${total_creditOfLedger}" /></td>
																<td class="tright"><fmt:formatNumber type="number"
																		minFractionDigits="2" maxFractionDigits="2"
																		value="${(openiBalanceOfLedger)+(total_creditOfLedger-total_debitOfLedger)}" />
																</td>
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
																			<td class="tright"><fmt:formatNumber
																					type="number" minFractionDigits="2"
																					maxFractionDigits="2"
																					value="${openiBalanceOfSubLedger}" /></td>
																			<td class="tright"><fmt:formatNumber
																					type="number" minFractionDigits="2"
																					maxFractionDigits="2"
																					value="${total_debitOfSubLedger}" /> <c:set
																					var="row_count_debit"
																					value="${row_count_debit + total_debitOfSubLedger}" />
																			</td>
																			<td class="tright"><fmt:formatNumber
																					type="number" minFractionDigits="2"
																					maxFractionDigits="2"
																					value="${total_creditOfSubLedger}" /> <c:set
																					var="row_count_credit"
																					value="${row_count_credit + total_creditOfSubLedger}" />
																			</td>
																			<td class="tright"><fmt:formatNumber
																					type="number" minFractionDigits="2"
																					maxFractionDigits="2"
																					value="${(openiBalanceOfSubLedger)+(total_creditOfSubLedger-total_debitOfSubLedger)}" />
																			</td>
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
																<c:if test="${ledgerform6.subgroupName==accSubGroup}">
																	<c:if test="${obalance.ledgerformlist.size()>0}">

																		<c:forEach var="ledgerform7"
																			items="${obalance.ledgerformlist}">
																			<c:if
																				test="${ledgerform7.ledgerName==ledgerform6.ledgerName}">
																				<c:set var="isPresent" value="1" />
																			</c:if>
																		</c:forEach>
																		<c:if test="${isPresent==0}">
																			<c:if test="${obalance.ledgerformlist.size()>0}">
																				<c:forEach var="ledgerform7"
																					items="${obalance.ledgerformlist}">
																					<c:if
																						test="${ledgerform7.ledgerName!=ledgerform6.ledgerName}">
																						<c:if test="${isNotPresent==0}">
																							<c:set var="openiBalanceOfLedger" value="0" />
																							<tr class='n-style'>
																								<td style="text-align: left;">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;${ledgerform6.ledgerName}</td>
																								<c:set var="openiBalanceOfLedger"
																									value="${openiBalanceOfLedger + (ledgerform6.ledgercredit_balance-ledgerform6.ledgerdebit_balance)}" />
																								<td class="tright"><fmt:formatNumber
																										type="number" minFractionDigits="2"
																										maxFractionDigits="2"
																										value="${openiBalanceOfLedger}" /></td>
																								<td class="tright"><fmt:formatNumber
																										type="number" minFractionDigits="2"
																										maxFractionDigits="2" value="0" /></td>
																								<td class="tright"><fmt:formatNumber
																										type="number" minFractionDigits="2"
																										maxFractionDigits="2" value="0" /></td>
																								<td class="tright"><fmt:formatNumber
																										type="number" minFractionDigits="2"
																										maxFractionDigits="2"
																										value="${openiBalanceOfLedger}" /></td>
																							</tr>
																							<c:if
																								test="${ledgerform6.subledgerList.size()>0}">


																								<c:forEach var="subledger1"
																									items="${ledgerform6.subledgerList}">
																									<c:if test="${subledger1!=null}">
																										<c:set var="openiBalanceOfSubLedger" value="0" />
																										<c:set var="openiBalanceOfSubLedger"
																											value="${openiBalanceOfSubLedger + (subledger1.credit_balance-subledger1.debit_balance)}" />

																										<c:if test="${openiBalanceOfSubLedger!=0}">
																											<tr class='n-style'>
																												<td style="text-align: left;">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;${subledger1.subledgerName}</td>
																												<td class="tright"><fmt:formatNumber
																														type="number" minFractionDigits="2"
																														maxFractionDigits="2"
																														value="${openiBalanceOfSubLedger}" /></td>
																												<td class="tright"><fmt:formatNumber
																														type="number" minFractionDigits="2"
																														maxFractionDigits="2" value="0" /></td>
																												<td class="tright"><fmt:formatNumber
																														type="number" minFractionDigits="2"
																														maxFractionDigits="2" value="0" /></td>
																												<td class="tright"><fmt:formatNumber
																														type="number" minFractionDigits="2"
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
																		<tr class='n-style'>
																			<td style="text-align: left;">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;${ledgerform6.ledgerName}</td>
																			<c:set var="openiBalanceOfLedger"
																				value="${openiBalanceOfLedger + (ledgerform6.ledgercredit_balance-ledgerform6.ledgerdebit_balance)}" />
																			<td class="tright"><fmt:formatNumber
																					type="number" minFractionDigits="2"
																					maxFractionDigits="2"
																					value="${openiBalanceOfLedger}" /></td>
																			<td class="tright"><fmt:formatNumber
																					type="number" minFractionDigits="2"
																					maxFractionDigits="2" value="0" /></td>
																			<td class="tright"><fmt:formatNumber
																					type="number" minFractionDigits="2"
																					maxFractionDigits="2" value="0" /></td>
																			<td class="tright"><fmt:formatNumber
																					type="number" minFractionDigits="2"
																					maxFractionDigits="2"
																					value="${openiBalanceOfLedger}" /></td>
																		</tr>
																		<c:if test="${ledgerform6.subledgerList.size()>0}">


																			<c:forEach var="subledger1"
																				items="${ledgerform6.subledgerList}">
																				<c:if test="${subledger1!=null}">
																					<c:set var="openiBalanceOfSubLedger" value="0" />
																					<c:set var="openiBalanceOfSubLedger"
																						value="${openiBalanceOfSubLedger + (subledger1.credit_balance-subledger1.debit_balance)}" />

																					<c:if test="${openiBalanceOfSubLedger!=0}">
																						<tr class='n-style'>
																							<td style="text-align: left;">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;${subledger1.subledgerName}</td>
																							<td class="tright"><fmt:formatNumber
																									type="number" minFractionDigits="2"
																									maxFractionDigits="2"
																									value="${openiBalanceOfSubLedger}" /></td>
																							<td class="tright"><fmt:formatNumber
																									type="number" minFractionDigits="2"
																									maxFractionDigits="2" value="0" /></td>
																							<td class="tright"><fmt:formatNumber
																									type="number" minFractionDigits="2"
																									maxFractionDigits="2" value="0" /></td>
																							<td class="tright"><fmt:formatNumber
																									type="number" minFractionDigits="2"
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
														<td class="tright"><fmt:formatNumber type="number"
																minFractionDigits="2" maxFractionDigits="2"
																value="${supOpeningbalance}" /></td>
														<td class="tright"><fmt:formatNumber type="number"
																minFractionDigits="2" maxFractionDigits="2"
																value="${supDebitbalance}" /> <c:set
																var="row_count_debit"
																value="${row_count_debit + supDebitbalance}" /></td>
														<td class="tright"><fmt:formatNumber type="number"
																minFractionDigits="2" maxFractionDigits="2"
																value="${supCreditbalance}" /> <c:set
																var="row_count_credit"
																value="${row_count_credit + supCreditbalance}" /></td>
														<td class="tright"><fmt:formatNumber type="number"
																minFractionDigits="2" maxFractionDigits="2"
																value="${(supOpeningbalance)+(supCreditbalance-supDebitbalance)}" />
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
															<td class="tright"><fmt:formatNumber type="number"
																	minFractionDigits="2" maxFractionDigits="2"
																	value="${openiBalanceOfsupplier}" />
															<td class="tright"><fmt:formatNumber type="number"
																	minFractionDigits="2" maxFractionDigits="2" value="0" /></td>
															<td class="tright"><fmt:formatNumber type="number"
																	minFractionDigits="2" maxFractionDigits="2" value="0" /></td>
															<td class="tright"><fmt:formatNumber type="number"
																	minFractionDigits="2" maxFractionDigits="2"
																	value="${(openiBalanceOfsupplier)}" /></td>
														</tr>
													</c:if>

												</c:if>

											</c:forEach>

										</c:if>

									</c:if>
								</c:forEach>
							</c:if>
						</c:if>
					</c:forEach>

					
				<tr><td><h5>Liability Total</h5></td> 
<td class="tright"><fmt:formatNumber type="number"
																	minFractionDigits="2" maxFractionDigits="2"
																	value="${(Liability_total)}" /></td>
																	<td></td><td></td>
																	<td class="tright"><fmt:formatNumber type="number"
																	minFractionDigits="2" maxFractionDigits="2"
																	value="${(Liability_CB_total)}" /></td>
</tr>
					<c:forEach var="obalance" items="${subOpeningList}">
						<!--  start of group -->
						<c:if test="${obalance.group.postingSide.posting_id == 2}">

							<c:set var="openingBalanceforGroup" value="0" />
							<c:set var="total_debitGroup" value="0" />
							<c:set var="total_creditGroup" value="0" />

							<c:forEach var="obalanceBeStartDate"
								items="${subOpeningListBeforeStartDate}">
								<c:if
									test="${obalanceBeStartDate.accountGroupName==obalance.accountGroupName}">
									<c:set var="openingBalanceforGroup"
										value="${obalanceBeStartDate.totalcredit_balance-obalanceBeStartDate.totaldebit_balance}" />
									<c:set var="total_debitGroup"
										value="${obalance.totaldebit_balance}" />
									<c:set var="total_creditGroup"
										value="${obalance.totalcredit_balance}" />
								</c:if>
							</c:forEach>
<c:set var="grandtotal_credit_group" value="${grandtotal_credit_group +total_creditGroup}" />
				<c:set var="grandtotal_debit_group" value="${grandtotal_debit_group +total_debitGroup}" />
					<c:if
								test="${openingBalanceforGroup!=0 || total_debitGroup!=0 || total_creditGroup!=0}">
								<c:set var="Income_total" value="${Income_total+openingBalanceforGroup}" />
								<c:set var="Liability_total" value="${Liability_total+Income_total}" />
								<c:set var="Income_CB" value="${(openingBalanceforGroup)+(total_creditGroup-total_debitGroup)}" />
								<c:set var="Income_CB_total" value="${Income_CB_total+Income_CB}" />
								<tr class='n-style'>
									<td style="text-align: left;"><h5>${obalance.accountGroupName}</h5></td>
									<td class="tright"><fmt:formatNumber type="number"
											minFractionDigits="2" maxFractionDigits="2"
											value="${openingBalanceforGroup}" /></td>
									<td class="tright"><fmt:formatNumber type="number"
											minFractionDigits="2" maxFractionDigits="2"
											value="${total_debitGroup}" /></td>
									<td class="tright"><fmt:formatNumber type="number"
											minFractionDigits="2" maxFractionDigits="2"
											value="${total_creditGroup}" /></td>
									<td class="tright"><fmt:formatNumber type="number"
											minFractionDigits="2" maxFractionDigits="2"
											value="${(openingBalanceforGroup)+(total_creditGroup-total_debitGroup)}" />
									</td>
								</tr>
							</c:if>


							<!--  start of subgroup -->
							<c:if test="${obalance.accountSubGroupNameList.size()>0}">
								<c:forEach var="accSubGroup"
									items="${obalance.accountSubGroupNameList}">

									<c:set var="total_credit_Subgroup_beforeStartDate" value="0" />
									<c:set var="total_debit_Subgroup_beforeStartDate" value="0" />
									<c:set var="total_credit_Subgroup" value="0" />
									<c:set var="total_debit_Subgroup" value="0" />

									<c:forEach var="obalanceBeStartDate"
										items="${subOpeningListBeforeStartDate}">

										<c:if
											test="${obalanceBeStartDate.accountSubGroupNameList.size()>0}">

											<c:forEach var="subGroupName"
												items="${obalanceBeStartDate.accountSubGroupNameList}">

												<c:if test="${subGroupName==accSubGroup}">
													<c:if test="${obalanceBeStartDate.ledgerformlist.size()>0}">
														<c:forEach var="ledgerform1"
															items="${obalanceBeStartDate.ledgerformlist}">
															<c:if test="${ledgerform1.subgroupName==accSubGroup}">
																<c:set var="total_credit_Subgroup_beforeStartDate"
																	value="${total_credit_Subgroup_beforeStartDate + ledgerform1.ledgercredit_balance}" />
																<c:set var="total_debit_Subgroup_beforeStartDate"
																	value="${total_debit_Subgroup_beforeStartDate + ledgerform1.ledgerdebit_balance}" />
															</c:if>
														</c:forEach>
													</c:if>
												</c:if>

											</c:forEach>

										</c:if>

									</c:forEach>

									<c:if test="${obalance.ledgerformlist.size()>0}">
										<c:forEach var="ledgerform2"
											items="${obalance.ledgerformlist}">
											<c:if test="${ledgerform2.subgroupName==accSubGroup}">
												<c:set var="total_credit_Subgroup"
													value="${total_credit_Subgroup + ledgerform2.ledgercredit_balance}" />
												<c:set var="total_debit_Subgroup"
													value="${total_debit_Subgroup + ledgerform2.ledgerdebit_balance}" />
											</c:if>
										</c:forEach>
									</c:if>

									<c:set var="openingBalanceSubgroup" value="0" />
									<c:set var="total_debitSubgroup" value="0" />
									<c:set var="total_creditSubgroup" value="0" />

									<c:set var="openingBalanceSubgroup"
										value="${total_credit_Subgroup_beforeStartDate-total_debit_Subgroup_beforeStartDate}" />
									<c:set var="total_debitSubgroup"
										value="${total_debit_Subgroup}" />
									<c:set var="total_creditSubgroup"
										value="${total_credit_Subgroup}" />

									<c:if
										test="${openingBalanceSubgroup!=0 || total_debitSubgroup!=0 || total_creditSubgroup!=0}">
										<tr class='n-style'>
											<td style="text-align: left;">&nbsp;&nbsp;&nbsp;&nbsp;${accSubGroup}</td>
											<td class="tright"><fmt:formatNumber type="number"
													minFractionDigits="2" maxFractionDigits="2"
													value="${openingBalanceSubgroup}" /></td>
											<td class="tright"><fmt:formatNumber type="number"
													minFractionDigits="2" maxFractionDigits="2"
													value="${total_debitSubgroup}" /></td>
											<td class="tright"><fmt:formatNumber type="number"
													minFractionDigits="2" maxFractionDigits="2"
													value="${total_creditSubgroup}" /></td>
											<td class="tright"><fmt:formatNumber type="number"
													minFractionDigits="2" maxFractionDigits="2"
													value="${(openingBalanceSubgroup)+(total_creditSubgroup-total_debitSubgroup)}" /></td>
										</tr>


										<!--  start of ledger -->
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
																<td class="tright"><fmt:formatNumber type="number"
																		minFractionDigits="2" maxFractionDigits="2"
																		value="${openiBalanceOfLedger}" /></td>
																<td class="tright"><fmt:formatNumber type="number"
																		minFractionDigits="2" maxFractionDigits="2"
																		value="${total_debitOfLedger}" /></td>
																<td class="tright"><fmt:formatNumber type="number"
																		minFractionDigits="2" maxFractionDigits="2"
																		value="${total_creditOfLedger}" /></td>
																<td class="tright"><fmt:formatNumber type="number"
																		minFractionDigits="2" maxFractionDigits="2"
																		value="${(openiBalanceOfLedger)+(total_creditOfLedger-total_debitOfLedger)}" />
																</td>
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
																			<td class="tright"><fmt:formatNumber
																					type="number" minFractionDigits="2"
																					maxFractionDigits="2"
																					value="${openiBalanceOfSubLedger}" /></td>
																			<td class="tright"><fmt:formatNumber
																					type="number" minFractionDigits="2"
																					maxFractionDigits="2"
																					value="${total_debitOfSubLedger}" /> <c:set
																					var="row_count_debit"
																					value="${row_count_debit + total_debitOfSubLedger}" />
																			</td>
																			<td class="tright"><fmt:formatNumber
																					type="number" minFractionDigits="2"
																					maxFractionDigits="2"
																					value="${total_creditOfSubLedger}" /> <c:set
																					var="row_count_credit"
																					value="${row_count_credit + total_creditOfSubLedger}" />
																			</td>
																			<td class="tright"><fmt:formatNumber
																					type="number" minFractionDigits="2"
																					maxFractionDigits="2"
																					value="${(openiBalanceOfSubLedger)+(total_creditOfSubLedger-total_debitOfSubLedger)}" />
																			</td>
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
																<c:if test="${ledgerform6.subgroupName==accSubGroup}">
																	<c:if test="${obalance.ledgerformlist.size()>0}">

																		<c:forEach var="ledgerform7"
																			items="${obalance.ledgerformlist}">
																			<c:if
																				test="${ledgerform7.ledgerName==ledgerform6.ledgerName}">
																				<c:set var="isPresent" value="1" />
																			</c:if>
																		</c:forEach>
																		<c:if test="${isPresent==0}">
																			<c:if test="${obalance.ledgerformlist.size()>0}">
																				<c:forEach var="ledgerform7"
																					items="${obalance.ledgerformlist}">
																					<c:if
																						test="${ledgerform7.ledgerName!=ledgerform6.ledgerName}">
																						<c:if test="${isNotPresent==0}">
																							<c:set var="openiBalanceOfLedger" value="0" />
																							<tr class='n-style'>
																								<td style="text-align: left;">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;${ledgerform6.ledgerName}</td>
																								<c:set var="openiBalanceOfLedger"
																									value="${openiBalanceOfLedger + (ledgerform6.ledgercredit_balance-ledgerform6.ledgerdebit_balance)}" />
																								<td class="tright"><fmt:formatNumber
																										type="number" minFractionDigits="2"
																										maxFractionDigits="2"
																										value="${openiBalanceOfLedger}" /></td>
																								<td class="tright"><fmt:formatNumber
																										type="number" minFractionDigits="2"
																										maxFractionDigits="2" value="0" /></td>
																								<td class="tright"><fmt:formatNumber
																										type="number" minFractionDigits="2"
																										maxFractionDigits="2" value="0" /></td>
																								<td class="tright"><fmt:formatNumber
																										type="number" minFractionDigits="2"
																										maxFractionDigits="2"
																										value="${openiBalanceOfLedger}" /></td>
																							</tr>
																							<c:if
																								test="${ledgerform6.subledgerList.size()>0}">


																								<c:forEach var="subledger1"
																									items="${ledgerform6.subledgerList}">
																									<c:if test="${subledger1!=null}">
																										<c:set var="openiBalanceOfSubLedger" value="0" />
																										<c:set var="openiBalanceOfSubLedger"
																											value="${openiBalanceOfSubLedger + (subledger1.credit_balance-subledger1.debit_balance)}" />
																										<c:if test="${openiBalanceOfSubLedger!=0}">
																											<tr class='n-style'>
																												<td style="text-align: left;">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;${subledger1.subledgerName}</td>
																												<td class="tright"><fmt:formatNumber
																														type="number" minFractionDigits="2"
																														maxFractionDigits="2"
																														value="${openiBalanceOfSubLedger}" /></td>
																												<td class="tright"><fmt:formatNumber
																														type="number" minFractionDigits="2"
																														maxFractionDigits="2" value="0" /></td>
																												<td class="tright"><fmt:formatNumber
																														type="number" minFractionDigits="2"
																														maxFractionDigits="2" value="0" /></td>
																												<td class="tright"><fmt:formatNumber
																														type="number" minFractionDigits="2"
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
																		<tr class='n-style'>
																			<td style="text-align: left;">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;${ledgerform6.ledgerName}</td>
																			<c:set var="openiBalanceOfLedger"
																				value="${openiBalanceOfLedger + (ledgerform6.ledgercredit_balance-ledgerform6.ledgerdebit_balance)}" />
																			<td class="tright"><fmt:formatNumber
																					type="number" minFractionDigits="2"
																					maxFractionDigits="2"
																					value="${openiBalanceOfLedger}" /></td>
																			<td class="tright"><fmt:formatNumber
																					type="number" minFractionDigits="2"
																					maxFractionDigits="2" value="0" /></td>
																			<td class="tright"><fmt:formatNumber
																					type="number" minFractionDigits="2"
																					maxFractionDigits="2" value="0" /></td>
																			<td class="tright"><fmt:formatNumber
																					type="number" minFractionDigits="2"
																					maxFractionDigits="2"
																					value="${openiBalanceOfLedger}" /></td>
																		</tr>
																		<c:if test="${ledgerform6.subledgerList.size()>0}">


																			<c:forEach var="subledger1"
																				items="${ledgerform6.subledgerList}">
																				<c:if test="${subledger1!=null}">
																					<c:set var="openiBalanceOfSubLedger" value="0" />
																					<c:set var="openiBalanceOfSubLedger"
																						value="${openiBalanceOfSubLedger + (subledger1.credit_balance-subledger1.debit_balance)}" />

																					<c:if test="${openiBalanceOfSubLedger!=0}">
																						<tr class='n-style'>
																							<td style="text-align: left;">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;${subledger1.subledgerName}</td>
																							<td class="tright"><fmt:formatNumber
																									type="number" minFractionDigits="2"
																									maxFractionDigits="2"
																									value="${openiBalanceOfSubLedger}" /></td>
																							<td class="tright"><fmt:formatNumber
																									type="number" minFractionDigits="2"
																									maxFractionDigits="2" value="0" /></td>
																							<td class="tright"><fmt:formatNumber
																									type="number" minFractionDigits="2"
																									maxFractionDigits="2" value="0" /></td>
																							<td class="tright"><fmt:formatNumber
																									type="number" minFractionDigits="2"
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
					</c:forEach>

					<tr><td><h5>Income Total</h5></td> 
<td class="tright"><fmt:formatNumber type="number"
																	minFractionDigits="2" maxFractionDigits="2"
																	value="${(Income_total)}" /></td>
																	<td></td><td></td>
																	
																	<td class="tright"><fmt:formatNumber type="number"
																	minFractionDigits="2" maxFractionDigits="2"
																	value="${(Income_CB_total)}" /></td>
																	
</tr>
					<c:forEach var="obalance" items="${subOpeningList}">
						<!--  start of group -->
						<c:if test="${obalance.group.postingSide.posting_id ==1}">

							<c:set var="openingBalanceforGroup" value="0" />
							<c:set var="total_debitGroup" value="0" />
							<c:set var="total_creditGroup" value="0" />

							<c:forEach var="obalanceBeStartDate"
								items="${subOpeningListBeforeStartDate}">
								<c:if
									test="${obalanceBeStartDate.accountGroupName==obalance.accountGroupName}">
									<c:set var="openingBalanceforGroup"
										value="${obalanceBeStartDate.totaldebit_balance-obalanceBeStartDate.totalcredit_balance}" />
									<c:set var="total_debitGroup"
										value="${obalance.totaldebit_balance}" />
									<c:set var="total_creditGroup"
										value="${obalance.totalcredit_balance}" />
								</c:if>
							</c:forEach>
<c:set var="grandtotal_credit_group" value="${grandtotal_credit_group +total_creditGroup}" />
				<c:set var="grandtotal_debit_group" value="${grandtotal_debit_group +total_debitGroup}" />
						<c:if
								test="${openingBalanceforGroup!=0 || total_debitGroup!=0 || total_creditGroup!=0}">
								<c:set var="Expenses_total" value="${Expenses_total+openingBalanceforGroup}" />
								<c:set var="Expenses_CB" value="${Expenses_total+openingBalanceforGroup}" />
								<c:set var="Expenses_CB_total" value="${Expenses_CB_total+Expenses_CB}" />
								<tr class='n-style'>
									<td style="text-align: left;"><h5>${obalance.accountGroupName}</h5></td>
									<td class="tright"><fmt:formatNumber type="number"
											minFractionDigits="2" maxFractionDigits="2"
											value="${openingBalanceforGroup}" /></td>
									<td class="tright"><fmt:formatNumber type="number"
											minFractionDigits="2" maxFractionDigits="2"
											value="${total_debitGroup}" /></td>
									<td class="tright"><fmt:formatNumber type="number"
											minFractionDigits="2" maxFractionDigits="2"
											value="${total_creditGroup}" /></td>
									<td class="tright"><fmt:formatNumber type="number"
											minFractionDigits="2" maxFractionDigits="2"
											value="${(openingBalanceforGroup)+(total_debitGroup-total_creditGroup)}" />
									</td>
								</tr>
							</c:if>


							<!--  start of subgroup -->
							<c:if test="${obalance.accountSubGroupNameList.size()>0}">
								<c:forEach var="accSubGroup"
									items="${obalance.accountSubGroupNameList}">

									<c:set var="total_credit_Subgroup_beforeStartDate" value="0" />
									<c:set var="total_debit_Subgroup_beforeStartDate" value="0" />
									<c:set var="total_credit_Subgroup" value="0" />
									<c:set var="total_debit_Subgroup" value="0" />

									<c:forEach var="obalanceBeStartDate"
										items="${subOpeningListBeforeStartDate}">

										<c:if
											test="${obalanceBeStartDate.accountSubGroupNameList.size()>0}">

											<c:forEach var="subGroupName"
												items="${obalanceBeStartDate.accountSubGroupNameList}">

												<c:if test="${subGroupName==accSubGroup}">
													<c:if test="${obalanceBeStartDate.ledgerformlist.size()>0}">
														<c:forEach var="ledgerform1"
															items="${obalanceBeStartDate.ledgerformlist}">
															<c:if test="${ledgerform1.subgroupName==accSubGroup}">
																<c:set var="total_credit_Subgroup_beforeStartDate"
																	value="${total_credit_Subgroup_beforeStartDate + ledgerform1.ledgercredit_balance}" />
																<c:set var="total_debit_Subgroup_beforeStartDate"
																	value="${total_debit_Subgroup_beforeStartDate + ledgerform1.ledgerdebit_balance}" />
															</c:if>
														</c:forEach>
													</c:if>
												</c:if>

											</c:forEach>

										</c:if>

									</c:forEach>

									<c:if test="${obalance.ledgerformlist.size()>0}">
										<c:forEach var="ledgerform2"
											items="${obalance.ledgerformlist}">
											<c:if test="${ledgerform2.subgroupName==accSubGroup}">
												<c:set var="total_credit_Subgroup"
													value="${total_credit_Subgroup + ledgerform2.ledgercredit_balance}" />
												<c:set var="total_debit_Subgroup"
													value="${total_debit_Subgroup + ledgerform2.ledgerdebit_balance}" />
											</c:if>
										</c:forEach>
									</c:if>

									<c:set var="openingBalanceSubgroup" value="0" />
									<c:set var="total_debitSubgroup" value="0" />
									<c:set var="total_creditSubgroup" value="0" />

									<c:set var="openingBalanceSubgroup"
										value="${total_debit_Subgroup_beforeStartDate-total_credit_Subgroup_beforeStartDate}" />
									<c:set var="total_debitSubgroup"
										value="${total_debit_Subgroup}" />
									<c:set var="total_creditSubgroup"
										value="${total_credit_Subgroup}" />

									<c:if
										test="${openingBalanceSubgroup!=0 || total_debitSubgroup!=0 || total_creditSubgroup!=0}">
										<tr class='n-style'>
											<td style="text-align: left;">&nbsp;&nbsp;&nbsp;&nbsp;${accSubGroup}</td>
											<td class="tright"><fmt:formatNumber type="number"
													minFractionDigits="2" maxFractionDigits="2"
													value="${openingBalanceSubgroup}" /></td>
											<td class="tright"><fmt:formatNumber type="number"
													minFractionDigits="2" maxFractionDigits="2"
													value="${total_debitSubgroup}" /></td>
											<td class="tright"><fmt:formatNumber type="number"
													minFractionDigits="2" maxFractionDigits="2"
													value="${total_creditSubgroup}" /></td>
											<td class="tright"><fmt:formatNumber type="number"
													minFractionDigits="2" maxFractionDigits="2"
													value="${(openingBalanceSubgroup)+(total_debitSubgroup-total_creditSubgroup)}" /></td>
										</tr>


										<!--  start of ledger -->
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
																<td class="tright"><fmt:formatNumber type="number"
																		minFractionDigits="2" maxFractionDigits="2"
																		value="${openiBalanceOfLedger}" /></td>
																<td class="tright"><fmt:formatNumber type="number"
																		minFractionDigits="2" maxFractionDigits="2"
																		value="${total_debitOfLedger}" /></td>
																<td class="tright"><fmt:formatNumber type="number"
																		minFractionDigits="2" maxFractionDigits="2"
																		value="${total_creditOfLedger}" /></td>
																<td class="tright"><fmt:formatNumber type="number"
																		minFractionDigits="2" maxFractionDigits="2"
																		value="${(openiBalanceOfLedger)+(total_debitOfLedger-total_creditOfLedger)}" />
																</td>
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
																			<td class="tright"><fmt:formatNumber
																					type="number" minFractionDigits="2"
																					maxFractionDigits="2"
																					value="${openiBalanceOfSubLedger}" /></td>
																			<td class="tright"><fmt:formatNumber
																					type="number" minFractionDigits="2"
																					maxFractionDigits="2"
																					value="${total_debitOfSubLedger}" /> <c:set
																					var="row_count_debit"
																					value="${row_count_debit + total_debitOfSubLedger}" />
																			</td>
																			<td class="tright"><fmt:formatNumber
																					type="number" minFractionDigits="2"
																					maxFractionDigits="2"
																					value="${total_creditOfSubLedger}" /> <c:set
																					var="row_count_credit"
																					value="${row_count_credit + total_creditOfSubLedger}" />
																			</td>
																			<td class="tright"><fmt:formatNumber
																					type="number" minFractionDigits="2"
																					maxFractionDigits="2"
																					value="${(openiBalanceOfSubLedger)+(total_debitOfSubLedger-total_creditOfSubLedger)}" />
																			</td>
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
																<c:if test="${ledgerform6.subgroupName==accSubGroup}">
																	<c:if test="${obalance.ledgerformlist.size()>0}">

																		<c:forEach var="ledgerform7"
																			items="${obalance.ledgerformlist}">
																			<c:if
																				test="${ledgerform7.ledgerName==ledgerform6.ledgerName}">
																				<c:set var="isPresent" value="1" />
																			</c:if>
																		</c:forEach>
																		<c:if test="${isPresent==0}">
																			<c:if test="${obalance.ledgerformlist.size()>0}">
																				<c:forEach var="ledgerform7"
																					items="${obalance.ledgerformlist}">
																					<c:if
																						test="${ledgerform7.ledgerName!=ledgerform6.ledgerName}">
																						<c:if test="${isNotPresent==0}">
																							<c:set var="openiBalanceOfLedger" value="0" />
																							<tr class='n-style'>
																								<td style="text-align: left;">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;${ledgerform6.ledgerName}</td>
																								<c:set var="openiBalanceOfLedger"
																									value="${openiBalanceOfLedger + (ledgerform6.ledgerdebit_balance-ledgerform6.ledgercredit_balance)}" />
																								<td class="tright"><fmt:formatNumber
																										type="number" minFractionDigits="2"
																										maxFractionDigits="2"
																										value="${openiBalanceOfLedger}" /></td>
																								<td class="tright"><fmt:formatNumber
																										type="number" minFractionDigits="2"
																										maxFractionDigits="2" value="0" /></td>
																								<td class="tright"><fmt:formatNumber
																										type="number" minFractionDigits="2"
																										maxFractionDigits="2" value="0" /></td>
																								<td class="tright"><fmt:formatNumber
																										type="number" minFractionDigits="2"
																										maxFractionDigits="2"
																										value="${openiBalanceOfLedger}" /></td>
																							</tr>
																							<c:if
																								test="${ledgerform6.subledgerList.size()>0}">


																								<c:forEach var="subledger1"
																									items="${ledgerform6.subledgerList}">
																									<c:if test="${subledger1!=null}">
																										<c:set var="openiBalanceOfSubLedger" value="0" />
																										<c:set var="openiBalanceOfSubLedger"
																											value="${openiBalanceOfSubLedger + (subledger1.debit_balance-subledger1.credit_balance)}" />
																										<c:if test="${openiBalanceOfSubLedger!=0}">
																											<tr class='n-style'>
																												<td style="text-align: left;">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;${subledger1.subledgerName}</td>
																												<td class="tright"><fmt:formatNumber
																														type="number" minFractionDigits="2"
																														maxFractionDigits="2"
																														value="${openiBalanceOfSubLedger}" /></td>
																												<td class="tright"><fmt:formatNumber
																														type="number" minFractionDigits="2"
																														maxFractionDigits="2" value="0" /></td>
																												<td class="tright"><fmt:formatNumber
																														type="number" minFractionDigits="2"
																														maxFractionDigits="2" value="0" /></td>
																												<td class="tright"><fmt:formatNumber
																														type="number" minFractionDigits="2"
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
																		<tr class='n-style'>
																			<td style="text-align: left;">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;${ledgerform6.ledgerName}</td>
																			<c:set var="openiBalanceOfLedger"
																				value="${openiBalanceOfLedger + (ledgerform6.ledgerdebit_balance-ledgerform6.ledgercredit_balance)}" />
																			<td class="tright"><fmt:formatNumber
																					type="number" minFractionDigits="2"
																					maxFractionDigits="2"
																					value="${openiBalanceOfLedger}" /></td>
																			<td class="tright"><fmt:formatNumber
																					type="number" minFractionDigits="2"
																					maxFractionDigits="2" value="0" /></td>
																			<td class="tright"><fmt:formatNumber
																					type="number" minFractionDigits="2"
																					maxFractionDigits="2" value="0" /></td>
																			<td class="tright"><fmt:formatNumber
																					type="number" minFractionDigits="2"
																					maxFractionDigits="2"
																					value="${openiBalanceOfLedger}" /></td>
																		</tr>
																		<c:if test="${ledgerform6.subledgerList.size()>0}">


																			<c:forEach var="subledger1"
																				items="${ledgerform6.subledgerList}">
																				<c:if test="${subledger1!=null}">
																					<c:set var="openiBalanceOfSubLedger" value="0" />
																					<c:set var="openiBalanceOfSubLedger"
																						value="${openiBalanceOfSubLedger + (subledger1.debit_balance-subledger1.credit_balance)}" />

																					<c:if test="${openiBalanceOfSubLedger!=0}">
																						<tr class='n-style'>
																							<td style="text-align: left;">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;${subledger1.subledgerName}</td>
																							<td class="tright"><fmt:formatNumber
																									type="number" minFractionDigits="2"
																									maxFractionDigits="2"
																									value="${openiBalanceOfSubLedger}" /></td>
																							<td class="tright"><fmt:formatNumber
																									type="number" minFractionDigits="2"
																									maxFractionDigits="2" value="0" /></td>
																							<td class="tright"><fmt:formatNumber
																									type="number" minFractionDigits="2"
																									maxFractionDigits="2" value="0" /></td>
																							<td class="tright"><fmt:formatNumber
																									type="number" minFractionDigits="2"
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
					</c:forEach>
<c:set var="Liability_total" value="${Liability_total -Expenses_total}" />
<c:set var="Liability_CB_total" value="${Liability_CB_total -Expenses_CB_total}" />
							<tr><td><h5>Expense Total</h5></td> 
<td class="tright"><fmt:formatNumber type="number"
																	minFractionDigits="2" maxFractionDigits="2"
																	value="${(Expenses_total)}" /></td>
																	<td></td><td></td>
																	<td class="tright"><fmt:formatNumber type="number"
																	minFractionDigits="2" maxFractionDigits="2"
																	value="${(Expenses_CB_total)}" /></td>
</tr>
				</c:if>

			</tbody>
			<tfoot style='background-color: #eee'>
				<tr>
				<tr><td> Liability Toatal</td> <td class="tright"><fmt:formatNumber type="number"
																	minFractionDigits="2" maxFractionDigits="2"
																	value="${(Liability_total) }" /></td>
																	<td></td><td></td>
																	<td class="tright"><fmt:formatNumber type="number"
																	minFractionDigits="2" maxFractionDigits="2"
																	value="${(Liability_CB_total)}" /></td>
																	</tr>
					<td>Total</td>
					<td></td>
					<%-- <c:forEach var="obalanceBeStartDate"
						items="${subOpeningListBeforeStartDate}">
						<c:set var="row_count_credit"
							value="${row_count_credit + obalanceBeStartDate.totalcredit_balance}" />
						<c:set var="row_count_debit"
							value="${row_count_debit + obalanceBeStartDate.totaldebit_balance}" />
					</c:forEach> --%>
				<%--	<c:set var="grandtotal_credit_group" value="${grandtotal_credit_group +total_creditGroup}" />
				<c:set var="grandtotal_debit_group" value="${grandtotal_debit_group +total_debitGroup}" />
					 <td class="tright"><fmt:formatNumber type="number"
							minFractionDigits="2" maxFractionDigits="2"
							value="${row_count_debit+total_debit_Supplier_beforeStartDate+total_debit_Customer_beforeStartDate+total_debit_Bank_beforeStartDate}" /></td>
					<td class="tright"><fmt:formatNumber type="number"
							minFractionDigits="2" maxFractionDigits="2"
							value="${row_count_credit+total_credit_Supplier_beforeStartDate+total_credit_Customer_beforeStartDate+total_credit_Bank_beforeStartDate}" /></td>
					<td></td> --%>
					<td class="tright"><fmt:formatNumber type="number"
							minFractionDigits="2" maxFractionDigits="2"
							value="${grandtotal_debit_group}" /></td>
					<td class="tright"><fmt:formatNumber type="number"
							minFractionDigits="2" maxFractionDigits="2"
							value="${grandtotal_credit_group}" /></td>
					<td></td>
				</tr>
			</tfoot>
		</table>

		<div class="row text-center-btn">
			<c:if test="${role!=7}">
				<button class="fassetBtn" type="button" onclick="pdf('#Hiddentable', {type: 'pdf',
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


				<button class="fassetBtn" type="button" id='btnExport3'
					onclick='exportexcel("Trial Balance Report")'>Download as Excel</button>
			</c:if>
			<button class="fassetBtn" type="button" onclick="back();">
				<spring:message code="btn_back" />
			</button>
		</div>
	</div>
</div>

<script type="text/javascript">

$("btnExport3").click(function(e) {
    e.preventDefault();
    $.ajax({
        type: "POST",
        url: "http://localhost/test.php",
        data: { 
            id: 555, // < note use of 'this' here
            name: 'avinash' 
        },
        success: function(result) {
            alert('ok');
        },
        error: function(result) {
            alert('error');
        }
    });
});
	$(function() {
		setTimeout(function() {
			$("#successMsg").hide();
		}, 3000);
	});

	function back() {
		window.location.assign('<c:url value = "trialBalanceReport"/>');
	}

	/* code commented for call pdf function java */	
/* 	function pdf() {
		window.location.assign('<c:url value = "pdfTrialBalanceReport"/>');
	}
 */	
</script>
<%@include file="/WEB-INF/includes/footer.jsp"%>