<%@include file="/WEB-INF/includes/header.jsp"%>
<%@page import="com.fasset.controller.abstracts.MyAbstractController"%>
<spring:url value="/resources/images/edit.png" var="editImg" />
<spring:url value="/resources/images/view.png" var="viewImg" />
<script type="text/javascript" src="${valid}"></script>


<spring:url value="/resources/js/jspdf.min.js" var="jspdfmin" />
<spring:url value="/resources/js/jspdf.plugin.autotable.js"
	var="jspdfauto" />
<spring:url value="/resources/js/report_table/incomeAndExpenditureReport.js"
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
	<h3>Income and Expenditure Report</h3>
<a href="homePage">Home</a> » <a href="incomeAndExpenditureReport">Income and Expenditure Report</a>
</div>
<div class="col-md-12">
	<c:if test="${successMsg != null}">
<div class="successMsg" id="successMsg">
	<strong>${successMsg}</strong>
</div>
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
<tr>
<td>

	<table>
		<thead>
			<tr>
				<th style='text-align: center'>Expense</th>
	</tr>
</thead>
<tbody>
 			
	<tr>
		<td>
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

<c:set var="closingBalanceOfSubgroup" value="${(closingBalanceOfSubgroup)+(total_debit_Subgroup_beforeStartDate-total_credit_Subgroup_beforeStartDate)+(total_debit_Subgroup-total_credit_Subgroup)}"/>

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
<c:set var="openiBalanceOfSubLedger" value="0" />
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
</c:if>
</c:forEach>
</c:if>

 </c:if>
</c:if>			                        
</c:forEach>

</c:if>
</tbody>
		
			<c:set var="totalNetProfit" value="0"/>
<c:if test="${TotalCreditAmount>TotalDebitAmount}">
  <tr>
<td><b>Excess of Income Over Expenditure</b></td>
<td></td>
<td class="tright"><b><fmt:formatNumber type="number" minFractionDigits="2"
maxFractionDigits="2" value="${TotalCreditAmount-TotalDebitAmount}" /></b>
<c:set var="totalNetProfit" value="${totalNetProfit+(TotalCreditAmount-TotalDebitAmount)}" />
	</td>
</tr>
</c:if>	
<tr>
	<td><b>Total</b></td>
	<td></td>
	<td class="tright"><b><fmt:formatNumber type="number" minFractionDigits="2"
maxFractionDigits="2" value="${TotalDebitAmount+totalNetProfit}" /></b></td>
				</tr>
			
		</table>
	</td>
</tr>
<tr>
	<th style='text-align: center'>Income</th>
</tr>
<tr>
	<td>
		
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
<c:set var="closingBalanceOfSubgroup" value="${(closingBalanceOfSubgroup)+(total_credit_Subgroup_beforeStartDate-total_debit_Subgroup_beforeStartDate)+(total_credit_Subgroup-total_debit_Subgroup)}"/>
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
</c:if>
</c:forEach>
</c:if>
</c:if>

</c:if>			                        
</c:forEach>



</c:if>
</tbody>

<c:set var="totalNetLoss" value="0"/>
<c:if test="${TotalDebitAmount>TotalCreditAmount}">
  <tr>
<td><b>Excess of Expenditure Over Income</b></td>
<td></td>
<td class="tright"><b><fmt:formatNumber type="number" minFractionDigits="2"
maxFractionDigits="2" value="${TotalDebitAmount-TotalCreditAmount}" /></b>
<c:set var="totalNetLoss" value="${totalNetLoss+(TotalDebitAmount-TotalCreditAmount)}" />
	</td>
</tr>
</c:if>	
<tr>
	<td><b>Total</b></td>
	<td></td>
	<td class="tright"><b><fmt:formatNumber type="number" minFractionDigits="2"
maxFractionDigits="2" value="${TotalCreditAmount+totalNetLoss}" /></b></td>
							</tr>
						
					</table> 
				</td>
			</tr>
			
		</tbody>
	</table>
</td>
</tr>
</table>
 </div>
	<!-- Excel End -->


<!-- PDF starts from here -->


<div class="table-scroll" style="display: none;" id="tableDiv">


		
		<c:set var="rowcount" value="0" scope="page" />
		<c:if test="${rowcount == 0}">
	


<table id="Hiddentable">

	<tr>
		<td></td>
		<td></td>
		<td style="color: blue; margin-left: 50px;">Income and Expenditure Report
	</td>
</tr>

<tr>
	<td align="center">Company Name:</td>
	<td></td>
	<td align="center">${company.company_name}</td>
</tr>
<tr>
	<td align="center">Address:</td>
	<td></td>
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
<td></td>
<td>${from_date}To ${to_date}</td>
</tr>
<tr>

	<td colspan='3'>CIN: <c:if test="${company.company_statutory_type.company_statutory_id==10 || company.company_statutory_type.company_statutory_id==14}">
${company.registration_no}
</c:if>
	</td>
</tr>


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


<tr>
	<th style='text-align: center'>Expense</th>
	</tr>

<tr>
	<th data-field="particulars-in" data-filter-control="input"
		data-sortable="true">Particulars</th>
	<th data-field="amount-in" data-filter-control="input"
		data-sortable="true"></th>
	<th data-field="amount" data-filter-control="input"
		data-sortable="true"></th>
</tr>

</c:if>

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


					<c:if test="${rowcount >  45}">
									<c:set var="rowcount" value="0" scope="page" />
								</c:if>
								<c:if test="${rowcount > 44}">
									<%@include file="/WEB-INF/views/report/incomeAndExpenditurReportHeader.jsp"%>
								</c:if>
							
								<c:set var="rowcount" value="${rowcount + 1}" scope="page" />


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

<c:set var="closingBalanceOfSubgroup" value="${(closingBalanceOfSubgroup)+(total_debit_Subgroup_beforeStartDate-total_credit_Subgroup_beforeStartDate)+(total_debit_Subgroup-total_credit_Subgroup)}"/>

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
</c:if>
</c:forEach>
</c:if>

 </c:if>
</c:if>			                        
</c:forEach>

</c:if>
</tbody>


		<c:set var="totalNetProfit" value="0"/>
<c:if test="${TotalCreditAmount>TotalDebitAmount}">
  <tr>
<td><b>Excess of Income Over Expenditure</b></td>
<td></td>
<td class="tright"><b><fmt:formatNumber type="number" minFractionDigits="2"
maxFractionDigits="2" value="${TotalCreditAmount-TotalDebitAmount}" /></b>
<c:set var="totalNetProfit" value="${totalNetProfit+(TotalCreditAmount-TotalDebitAmount)}" />
	</td>
</tr>
</c:if>	
<tr>
	<td><b>Total</b></td>
	<td></td>
	<td class="tright"><b><fmt:formatNumber type="number" minFractionDigits="2"
maxFractionDigits="2" value="${TotalDebitAmount+totalNetProfit}" /></b></td>
</tr>




<!-- credit side ends here  -->


<!-- debit starts from here -->



<tbody>
					

		<tr><td></td></tr>
	<tr><td></td></tr>
	<tr><td></td></tr>
	
	<tr><th style='text-align: center'>Income</th></tr>

<tr>
	<th data-field="particulars-in" data-filter-control="input"
		data-sortable="true">Particulars</th>
	<th data-field="amount-in" data-filter-control="input"
		data-sortable="true"></th>
	<th data-field="amount" data-filter-control="input"
		data-sortable="true"></th>
</tr>
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
<c:set var="closingBalanceOfSubgroup" value="${(closingBalanceOfSubgroup)+(total_credit_Subgroup_beforeStartDate-total_debit_Subgroup_beforeStartDate)+(total_credit_Subgroup-total_debit_Subgroup)}"/>
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
</c:if>
</c:forEach>
</c:if>
</c:if>

</c:if>			                        
</c:forEach>



</c:if>
</tbody>
					
					<c:set var="totalNetLoss" value="0"/>
<c:if test="${TotalDebitAmount>TotalCreditAmount}">
  <tr>
<td><b>Excess of Expenditure Over Income</b></td>
<td></td>
<td class="tright"><b><fmt:formatNumber type="number" minFractionDigits="2"
maxFractionDigits="2" value="${TotalDebitAmount-TotalCreditAmount}" /></b>
<c:set var="totalNetLoss" value="${totalNetLoss+(TotalDebitAmount-TotalCreditAmount)}" />
	</td>
</tr>
</c:if>	
<tr>
	<td><b>Total</b></td>
	<td></td>
	<td class="tright"><b><fmt:formatNumber type="number" minFractionDigits="2"
maxFractionDigits="2" value="${TotalCreditAmount+totalNetLoss}" /></b></td>
						</tr>
				
</table>
</div>




<!-- PDF ends here -->
	
	
	
	
	
	
<table style="width: 100%">
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
<tr>
<td style="vertical-align:top">
<div class="borderForm">
	<table style="width: 100%">
<thead>
	<tr>
		<th style='text-align: center'>Expense</th>
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

<c:set var="closingBalanceOfSubgroup" value="${(closingBalanceOfSubgroup)+(total_debit_Subgroup_beforeStartDate-total_credit_Subgroup_beforeStartDate)+(total_debit_Subgroup-total_credit_Subgroup)}"/>

<c:if test="${closingBalanceOfSubgroup!=0}">
<tr class='n-style'>
<td style="text-align: left;">&nbsp;&nbsp;&nbsp;&nbsp;${accSubGroup}</td>
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
  <tr>
<td><b>Excess of Income Over Expenditure</b></td>
<td></td>
<td class="tright"><b><fmt:formatNumber type="number" minFractionDigits="2"
maxFractionDigits="2" value="${TotalCreditAmount-TotalDebitAmount}" /></b>
<c:set var="totalNetProfit" value="${totalNetProfit+(TotalCreditAmount-TotalDebitAmount)}" />
	</td>
</tr>
</c:if>	
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
		<th style='text-align: center'>Income</th>
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
<c:set var="closingBalanceOfSubgroup" value="${(closingBalanceOfSubgroup)+(total_credit_Subgroup_beforeStartDate-total_debit_Subgroup_beforeStartDate)+(total_credit_Subgroup-total_debit_Subgroup)}"/>
<c:if test="${closingBalanceOfSubgroup!=0}">
<tr class='n-style'>
<td style="text-align: left;">&nbsp;&nbsp;&nbsp;&nbsp;${accSubGroup}</td>
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
  <tr>
<td><b>Excess of Expenditure Over Income</b></td>
<td></td>
<td class="tright"><b><fmt:formatNumber type="number" minFractionDigits="2"
maxFractionDigits="2" value="${TotalDebitAmount-TotalCreditAmount}" /></b>
<c:set var="totalNetLoss" value="${totalNetLoss+(TotalDebitAmount-TotalCreditAmount)}" />
	</td>
</tr>
</c:if>	
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


<div class="row text-center-btn">
<c:if test="${role!=7}">
<button class="fassetBtn" type="button"  onclick ="pdf()">
	Download as PDF</button>
<button class="fassetBtn" type="button" id='btnExport3' onclick = 'exportexcel("Income and Expenditure Report")'>
	Download as Excel
</button>
</c:if>
<button class="fassetBtn" type="button" onclick="back();">
<spring:message code="btn_back" />
		</button>
	</div>
</div>
<script type="text/javascript">
$(function() {
	setTimeout(function() {
		$("#successMsg").hide();
	}, 3000);
});
function back() {
	window.location .assign('<c:url value = "profitAndLossReport"/>');
}
function pdf() {
	window.location.assign('<c:url value = "pdfIncomeAndexpenditureReport"/>');
} 
</script>
<%@include file="/WEB-INF/includes/footer.jsp"%>a