			<table  class="noBorder">
						<tr>
							<td></td>
							<td></td>
							<td></td>
							<td>Receipt Report</td>
						</tr>
						<tr>
							<td >Company Name: </td>
							<td></td>
							<td >${company.company_name}</td>
						</tr>
						<tr>
							<td>Address: </td>
							<td></td>
							<td>${company.permenant_address}</td>
						</tr>
						<tr>
							<td>From </td>
							<td></td>
							<td> ${from_date} To ${to_date}</td>
						</tr>
						<tr>
						<td >CIN:</td>
						<td></td>
							<td >
							<c:if test="${company.company_statutory_type.company_statutory_id==10 || company.company_statutory_type.company_statutory_id==14}">
							 ${company.registration_no}
						    </c:if>	
							</td>
						</tr>
					
			<%-- <table id="Hiddentable" >
				<!-- for PDf heading -->
					
			
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
			
				 --%> 
					<tr>
						<th data-field="date" data-filter-control="input"
							data-sortable="true">Date</th>
						<th data-field="voucher number" data-filter-control="input"
							data-sortable="true">Voucher Number</th>
						<th data-field="particulars" data-filter-control="input"
							data-sortable="true">Particulars</th>
						<th data-field="Ref NO" data-filter-control="input"
							data-sortable="true">Ref NO.</th>
						<th data-field="bank" data-filter-control="input"
							data-sortable="true">Cash/bank</th>
						<th data-field="chno" data-filter-control="input"
							data-sortable="true">Ch. No./ IB no.</th>
						<th data-field="receiptamount" data-filter-control="input"
							data-sortable="true">Receipt Amount</th>
					    <th data-field="tds" data-filter-control="input"
							data-sortable="true" style="text-align: right;">TDS</th>
						<th data-field="voucher type" data-filter-control="input"
							data-sortable="true">Total Amount</th>							
					</tr>
				