			<table id="Hiddentable">

				<tr>
					<td></td>
					<td></td>
					<td style="color: blue; margin-left: 50px;">Credit Note Report</td>
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
							
						From: 
						</td>
						<td></td>
						<td>${from_date} To ${to_date}</td>
					</tr>
					<tr>
					
					<td colspan='3'>
					CIN:
					<c:if
							test="${company.company_statutory_type.company_statutory_id==10 || company.company_statutory_type.company_statutory_id==14}">
					 ${company.registration_no}
				    </c:if>	
					</td>
					</tr>
					
				<tr>
					<th data-field="Cr note Date" data-filter-control="input"
						data-sortable="true">Cr note Date</th>
				    <th data-field="Cr note Number" data-filter-control="input"
						data-sortable="true"> Cr note Number</th>
					<th data-field="particulars" data-filter-control="input"
						data-sortable="true">Particulars</th>
					<th data-field="voucherType" data-filter-control="input"
						data-sortable="true">Voucher Type</th>
					<th data-field="credit" data-filter-control="input"
						data-sortable="true">Credit  Note Amount</th>
					<th data-field="Original Invoice amount"
						data-filter-control="input" data-sortable="true"> Original Invoice amount</th>
							<th data-field="Original Inv no." data-filter-control="input"
						data-sortable="true" style="text-align: center;">Original Inv no.</th>
					<th data-field="Original Invoice date" data-filter-control="input"
						data-sortable="true" style="text-align: center;">Original Invoice date.</th>
					<th data-field="Reason for Cr note" data-filter-control="input"
						data-sortable="true" style="text-align: center;">Reason for Cr note</th>
				</tr>