<table id="Hiddentable">
			<tr>
				<td></td>
				<td></td>
				<td style="color: blue; margin-left: 50px;">Supplier and
					Customer Advance Unadjusted Report</td>
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
				<td>${from_date}  To${to_date}</td>
			</tr>
			<tr>

				<td colspan='3'>CIN: <c:if
						test="${company.company_statutory_type.company_statutory_id==10 || company.company_statutory_type.company_statutory_id==14}">
					 ${company.registration_no}
				    </c:if>
				</td>
			</tr>

			<tr>

				<th data-field="date" data-filter-control="input"
					data-sortable="true">Date</th>
				<th data-field="customer" data-filter-control="input"
					data-sortable="true">Customer Name/Supplier Name</th>
				<th data-field="voucher" data-filter-control="input"
					data-sortable="true">Voucher No</th>
				<th data-field="type" data-filter-control="select"
					data-sortable="true">Voucher Type</th>
				<th data-field="paymentType" data-filter-control="select"
					data-sortable="true">Payment Type</th>
				<th data-field="receipt" data-filter-control="input"
					data-sortable="true">Total Amount</th>


			</tr>
