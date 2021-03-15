<table id="Hiddentable">
			<!-- for PDf heading -->
			<tr>
				<td></td>

				<td style="text-align: center;">Day Book</td>
			</tr>
			<tr>
				<td>Company Name: ${company.company_name}</td>

			</tr>
			<tr>
				<td>Address: ${company.permenant_address}</td>

			</tr>
			<tr>
				<td>Date: ${date}</td>
				<%-- <fmt:parseDate value="${date}" pattern="yyyy-MM-dd" var="parsedDate" type="date" />
	                   			 <fmt:formatDate value="${parsedDate}" var="date" type="date" pattern="dd-MM-yyyy" /> --%>
				<%-- <td>${date}</td> --%>
			</tr>
			<tr>
				<td>CIN: ${company.registration_no}</td>
				<td><c:if
						test="${company.company_statutory_type.company_statutory_id==10 || company.company_statutory_type.company_statutory_id==14}">

					</c:if></td>
			</tr>

			<tr>
				<th data-field="date" data-filter-control="input"
					data-sortable="true">Date</th>
				<th data-field="vtype" data-filter-control="input"
					data-sortable="true">Voucher Number</th>
				<th data-field="type" data-filter-control="input"
					data-sortable="true">Voucher Type</th>
				<th data-field="particulars" data-filter-control="input"
					data-sortable="true">Particulars</th>
				<th data-field="debit" data-filter-control="input"
					data-sortable="true">Debit</th>
				<th data-field="credit" data-filter-control="input"
					data-sortable="true">Credit</th>
			</tr>
