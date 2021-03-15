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

