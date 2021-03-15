
<table id="Hiddentable">
	<tr>
		<td></td>
		<td></td>
	</tr>
	<tr>
		<td></td>
		<td></td>
	</tr>
	<tr>
		<td></td>

	</tr>

	<tr>
		<td></td>
		<td></td>
		<td style="color: blue; margin-left: 50px; text-align: center;">Trial
			Balance</td>
	</tr>

	<tr>
		<td align="center">Company Name:</td>

		<td align="center;fontSize = 1;">${company.company_name}</td>
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

		<td style="color: red; size: 200px;">${from_date}To ${to_date}</td>
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