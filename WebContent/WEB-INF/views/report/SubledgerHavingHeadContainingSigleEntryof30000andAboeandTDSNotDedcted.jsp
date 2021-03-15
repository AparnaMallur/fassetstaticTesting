<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>SubledgerHavingHeadContainingSigleEntryof30000andAboeandTDSNotDedcted.jsp</title>
</head>
<body>
	<table>
			<thead>
				<tr>
					<th data-field="ledger" data-filter-control="input"
						data-sortable="true">Supp/Cust_Name</th>
					<th data-field="subLedger" data-filter-control="input"
						data-sortable="true">voucher_type</th>
					<th data-field="debitBalance" data-filter-control="input"
						data-sortable="true">voucher_date</th>
					<th data-field="creditBalance" data-filter-control="input"
						data-sortable="true">voucher_number</th>
					<th data-field="running" data-filter-control="input"
						data-sortable="true">Transaction_value</th>
				</tr>
			</thead>
			<tbody>		
					<tr>
					<td></td>
					<td>Opening Balance</td>
					<td></td>
					<td><Strong><fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${row_debit1-row_credit1}" /></Strong></td>
				    </tr>	
						<c:set var="row_credit" value="0"/>		
						<c:set var="row_debit" value="0"/>	
						<c:set var="row_running" value="0"/>
						<c:forEach var="subledger" items="${subledgerList}">
				<c:if test="${subledger.openingbalances.created_date >= fromDate && subledger.openingbalances.created_date <= toDate}">
					     <tr>
						<td style="text-align: left;">${subledger.ledger.ledger_name}</td>
					    <td style="text-align: left;">${subledger.subledger_name}</td>
					     <td style="text-align: left;">
					     <fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${subledger.openingbalances.debit_balance}" />
					      <c:set var="row_debit" value="${row_debit + subledger.openingbalances.debit_balance}" /></td>
						<td style="text-align: left;">
						 <fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${subledger.openingbalances.credit_balance}" />
					      <c:set var="row_credit" value="${row_credit + subledger.openingbalances.credit_balance}" /></td>
					      <td style="text-align: left;">
						 <fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${(subledger.openingbalances.debit_balance-subledger.openingbalances.credit_balance)+(row_debit1-row_credit1)}" />
					      <c:set var="row_running" value="${row_running + (subledger.openingbalances.debit_balance-subledger.openingbalances.credit_balance)+(row_debit1-row_credit1)}" /></td>
					      </tr>
			   </c:if>
				</c:forEach>			
				<tr>
					<td></td>
					<td></td>
					<td><Strong><fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${row_debit}" /></Strong></td>
			 	<td><Strong><fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${row_credit}" /></Strong></td>
			 	<td><Strong><fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${row_running}" /></Strong></td>
				</tr>
				<tr>
					<td></td>
					<td>Closing Balance</td>
					<td></td>
					<td><Strong><fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${row_debit-row_credit}" /></Strong></td>
					<td></td>
				</tr>
			</tbody>
		</table>
</body>
</html>