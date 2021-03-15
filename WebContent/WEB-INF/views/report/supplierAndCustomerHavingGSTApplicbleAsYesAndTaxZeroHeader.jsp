<table id="Hiddentable">
			<tr>
				<td></td>
				<td></td>
				<td style="color: blue;">SHORT GST EXCEPTION REPORT</td>
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
				<td>${from_date}To${to_date}</td>
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
					data-sortable="true"  align="left">Date</th>
				<th data-field="particulars" data-filter-control="input"
					data-sortable="true"  align="left">Supplier/Customer Name</th>
				<th data-field="voucher number" data-filter-control="input"
					data-sortable="true"  align="left" colspan="1">Voucher Number</th>
				<th class="tright" data-field="voucher type" data-filter-control="input"
					data-sortable="true"  style="left:80px;">Voucher Type</th>
				<th class="tright"  data-field="Invoice No" data-filter-control="input"
					data-sortable="true" align="right">Invoice No</th>
				<th  class="tright" data-field="Total Value(Invoice value)"
					data-filter-control="input" data-sortable="true" align="right" >Total
					Value(Invoice value)</th>
			</tr>
