<%@include file="/WEB-INF/includes/header.jsp"%>
<%@page import="com.fasset.controller.abstracts.MyAbstractController"%>
<script type="text/javascript" src="${valid}"></script>
<div class="breadcrumb">
	<h3>Edit Employee </h3>	
		 <a href="homePage">Home</a> »<a href="payrollAutoJVList">Payroll Auto JV</a> » <a href="#">Edit Employee </a>
	</div>	
	
	<div class="col-md-12 wideform">
	<div class="fassetForm">
	
		<form:form id="editProductForm" action="saveEmployeeForPayroll" method="post" commandName = "entry" onsubmit = "return validate();">
	
	
			<div class="row">				
				<form:input path="employee_id" hidden = "hidden"/>
				<form:input path="employeeDetails_id" hidden = "hidden"/>
				<form:input path="wdays" value="${payrollEntry.days}"  hidden = "hidden"/>
				
					
				
				
						<div class="row">	
				<div class="col-md-2 control-label"><label>Employee Id</label></div>		
				<div class="col-md-4">
					<input value="${entry.code}"  style="width:100%;" readonly="true"/>
				</div>	
				<div class="col-md-2 control-label"><label>Employee Name<span>*</span></label></div>		
				<div class="col-md-4">	
				<input value="${entry.name}"  style="width:100%;" readonly="true"/>
					<%-- <form:input path="name" class="logInput" id = "name" placeholder="name" />
					<span class="logError"><form:errors path="name" /></span> --%>
				</div>
			</div>
			
			<div class="row">	
				<div class="col-md-2 control-label"><label>PAN No</label></div>		
				<div class="col-md-4">
					<input value="${entry.pan}"  style="width:100%;" readonly="true"/>
				</div>	
				<div class="col-md-2 control-label"><label>Total Days<span>*</span></label></div>		
				<div class="col-md-4">	
					<form:input path="totaldays" class="logInput" id = "totaldays" placeholder="totaldays"   onchange="calculaterate(this.value,1)" onkeypress="return isNumber(event)"  />
					<span class="logError"><form:errors path="totaldays" /></span>
				</div>
			</div>
			
			<div class="row">	
				<div class="col-md-2 control-label"><label>Basic Salary</label></div>		
				<div class="col-md-4">
				<form:input path="basicSalary" class="logInput" id = "basicSalary" placeholder="basicSalary" readonly="true"   onchange="calculaterate(this.value,2)" onkeypress="return isNumber(event)"  />
					<span class="logError"><form:errors path="basicSalary" /></span>
			<%-- 		<input value="${entry.basicSalary}"  style="width:100%;"    onchange="calculaterate(this.value,2)" onkeypress="return isNumber(event)" />
			 --%>	</div>	
				<div class="col-md-2 control-label"><label>DA<span>*</span></label></div>		
				<div class="col-md-4">	
				<form:input path="DA" class="logInput" id = "DA" placeholder="DA" readonly="true"   onchange="calculaterate(this.value,3)" onkeypress="return isNumber(event)"  />
					<span class="logError"><form:errors path="DA" /></span>
					<%-- <input value="${entry.DA}"  style="width:100%;" readonly="true"  onchange="calculaterate(this.value,3)" onkeypress="return isNumber(event)" />
				 --%>	<!-- onchange="calculaterate(this.value,1)" onkeypress="return isNumber(event)" -->
				
				
				</div>
			</div>
			
			<div class="row">	
				<div class="col-md-2 control-label"><label>Conveyance Allowance</label></div>		
				<div class="col-md-4">
				<form:input path="conveyanceAllowance" class="logInput" id = "conveyanceAllowance"  readonly="true"  placeholder="conveyanceAllowance"  onchange="calculaterate1(this.value,4)" onkeypress="return isNumber(event)" maxlength="10"/>
					<span class="logError"><form:errors path="conveyanceAllowance" /></span>
					<%-- <input value="${entry.conveyanceAllowance}"  style="width:100%;" readonly="true"  onchange="calculaterate(this.value,4)" onkeypress="return isNumber(event)" />
		 --%>		</div>	
				<div class="col-md-2 control-label"><label>Other allowances<span>*</span></label></div>		
				<div class="col-md-4">	
					<form:input path="otherAllowances" class="logInput" id = "otherAllowances" placeholder="otherAllowances" readonly="true"  onchange="calculaterate1(this.value,5)" onkeypress="return isNumber(event)" maxlength="10"/>
					<span class="logError"><form:errors path="otherAllowances" /></span>
						
				</div>
			</div>
			
			<div class="row">	
				<div class="col-md-2 control-label"><label>Gross Salary</label></div>		
				<div class="col-md-4">
					<input value="${entry.grossSalary}" id="grossSalary" style="width:100%;" readonly="true"   onkeypress="return isNumber(event)" />
				</div>	
				<div class="col-md-2 control-label"><label>PF Employee Contribution<span>*</span></label></div>		
				<div class="col-md-4">	
					<form:input path="pfEmployeeContribution" class="logInput" id = "pfEmployeeContribution" placeholder="pfEmployeeContribution"  onchange="calculaterate1(this.value,1)" onkeypress="return isNumber(event)" maxlength="10"/>
					<span class="logError"><form:errors path="pfEmployeeContribution" /></span>
				</div>
			</div>
			
			
			<div class="row">	
				<div class="col-md-2 control-label"><label>ESIC Employee Contribution</label></div>		
				<div class="col-md-4">
				<form:input path="eSICEmployeeContribution" class="logInput" id = "eSICEmployeeContribution" placeholder="eSICEmployeeContribution" onchange="calculaterate1(this.value,2)" onkeypress="return isNumber(event)" maxlength="10" />
					<span class="logError"><form:errors path="eSICEmployeeContribution" /></span>
				<%-- 	<input value="${entry.eSICEmployeeContribution}"  style="width:100%;" onchange="calculaterate1(this.value,2)" onkeypress="return isNumber(event)" maxlength="10"/>
			 --%>	</div>	
				<div class="col-md-2 control-label"><label>Profession Tax<span>*</span></label></div>		
				<div class="col-md-4">	
					<form:input path="professionTax" class="logInput" id = "professionTax" placeholder="professionTax" onchange="calculaterate1(this.value,3)" onkeypress="return isNumber(event)" maxlength="10" />
					<span class="logError"><form:errors path="professionTax" /></span>
				</div>
			</div>
			
			<div class="row">	
				<div class="col-md-2 control-label"><label>LWF</label></div>		
				<div class="col-md-4">
				<form:input path="lWF" class="logInput" id = "lWF" placeholder="lWF" onchange="calculaterate1(this.value,4)" onkeypress="return isNumber(event)" maxlength="10"/>
					<span class="logError"><form:errors path="tDS" /></span>
					<%-- <input value="${entry.lWF}"  style="width:100%;" onchange="calculaterate1(this.value,4)" onkeypress="return isNumber(event)" maxlength="10"/>
			 --%>	</div>	
				<div class="col-md-2 control-label"><label>TDS<span>*</span></label></div>		
				<div class="col-md-4">	
					<form:input path="tDS" class="logInput" id = "tDS" placeholder="tDS" onchange="calculaterate1(this.value,5)" onkeypress="return isNumber(event)" maxlength="10"/>
					<span class="logError"><form:errors path="tDS" /></span>
				</div>
			</div>
			
			<div class="row">	
				<div class="col-md-2 control-label"><label>Other Deductions</label></div>		
				<div class="col-md-4">
				<form:input path="otherDeductions" class="logInput" id = "otherDeductions" placeholder="otherDeductions" onchange="calculaterate1(this.value,6)" onkeypress="return isNumber(event)" maxlength="10" />
					<span class="logError"><form:errors path="otherDeductions" /></span>
				<%-- 	<input value="${entry.otherDeductions}"  style="width:100%;" onchange="calculaterate1(this.value,6)" onkeypress="return isNumber(event)" maxlength="10"/>
		 --%>		</div>	
				<div class="col-md-2 control-label"><label>Advance Adjustment<span>*</span></label></div>		
				<div class="col-md-4">	
					<form:input path="advanceAdjustment" class="logInput" id = "advanceAdjustment" placeholder="advanceAdjustment" onchange="calculaterate1(this.value,7)" onkeypress="return isNumber(event)" maxlength="10" />
					<span class="logError"><form:errors path="advanceAdjustment" /></span>
				</div>
			</div>
			
			<div class="row">	
				<div class="col-md-2 control-label"><label>Total Deductions</label></div>		
				<div class="col-md-4">
					<form:input path="totalDeductions" class="logInput" id = "totalDeductions" placeholder="totalDeductions"  onkeypress="return isNumber(event)" maxlength="10" readonly="true"/>
					<span class="logError"><form:errors path="totalDeductions" /></span>
				<%-- 	<input value="${entry.totalDeductions}"  style="width:100%;"/> --%>
				</div>	
				<div class="col-md-2 control-label"><label>Net Salary<span>*</span></label></div>		
				<div class="col-md-4">	
					<form:input path="netSalary" class="logInput" id = "netSalary" placeholder="netSalary" onkeypress="return isNumber(event)" onchange="calculaterate1(this.value,7)"  readonly="true" />
					<span class="logError"><form:errors path="netSalary" /></span>
				</div>
			</div>
			<div class="row">	
				<div class="col-md-2 control-label"><label>PF Employer Contribution</label></div>		
				<div class="col-md-4">
					<form:input path="pfEmployerContribution" class="logInput" id = "pfEmployerContribution" placeholder="pfEmployerContribution"  onkeypress="return isNumber(event)" onchange="calculaterate1(this.value,8)"  />
					<span class="logError"><form:errors path="pfEmployerContribution" /></span>
					<%-- <input value="${entry.pfEmployerContribution}"  style="width:100%;"/> --%>
				</div>	
				<div class="col-md-2 control-label"><label>ESIC Employer Contribution<span>*</span></label></div>		
				<div class="col-md-4">	
					<form:input path="eSICEmployerContribution" class="logInput" id = "eSICEmployerContribution" placeholder="eSICEmployerContribution"  onkeypress="return isNumber(event)" onchange="calculaterate1(this.value,9)"  />
					<span class="logError"><form:errors path="eSICEmployerContribution" /></span>
				</div>
			</div>
			
			<div class="row">	
				<div class="col-md-2 control-label"><label>TotPF Admin Charges</label></div>		
				<div class="col-md-4">
				<form:input path="pFAdminCharges" class="logInput" id = "pFAdminCharges" placeholder="pFAdminCharges"  onkeypress="return isNumber(event)" onchange="calculaterate1(this.value,10)"  />
					<span class="logError"><form:errors path="pFAdminCharges" /></span>
					<%-- <input value="${entry.pFAdminCharges}"  style="width:100%;"/> --%>
				</div>	
				
			</div>
			
				
	</div>
	
	<div class="row text-center-btn">
				<button class="fassetBtn" type="submit" >
					<spring:message code="btn_save" />
				</button>
				<button class="fassetBtn" type="button" onclick = "cancel()">
					<spring:message code="btn_cancel" />
				</button>
			</div>
	</form:form>
	</div>
	</div>
	<script>
	
	function calculaterate1(e,flag){
	    var empId = $("#employee_id").val();
		var employeeName = $("#employeeName").val();
		var PAN = $("#PAN").val();
		var totaldays = $("#totaldays").val();
		var basicSalary =$("#basicSalary").val();
		var DA =  $("#DA").val(); 
		var conveyanceAllowance = $("#conveyanceAllowance").val();  
		var otherAllowances = $("#otherAllowances").val();
		var grossSalary = $("#grossSalary").val();
		var pfEmployeeContribution = $("#pfEmployeeContribution").val(); // aDI
		var eSICEmployeeContribution = $("#eSICEmployeeContribution").val();
		var professionTax = $("#professionTax").val();
		var lWF = $("#lWF").val();
		var tDS = $("#tDS").val();
		var otherDeductions = $("#otherDeductions").val();
		var advanceAdjustment = $("#advanceAdjustment").val();
		var totalDeductions = $("#totalDeductions").val();
		var netSalary = $("#netSalary").val();
		var pfEmployerContribution = $("#pfEmployerContribution").val();
		var eSICEmployerContribution = $("#eSICEmployerContribution").val();
		
		
		
		var pFAdminCharges = $("#pFAdminCharges").val();
		
		
		totalDeductions=parseFloat(pfEmployeeContribution)+parseFloat(professionTax)+parseFloat(eSICEmployeeContribution)+parseFloat(tDS)+parseFloat(lWF)+parseFloat(advanceAdjustment)+parseFloat(otherDeductions);
		
		totalDeductions=Math.round(totalDeductions);
		
		//calculate Net Salary
		netSalary=parseFloat(grossSalary)-parseFloat(totalDeductions);
		
		netSalary=Math.round(netSalary);
		
		if(parseFloat(grossSalary) < 21000)
		{
		eSICEmployerContribution=grossSalary*0.0475;
		}
	else
		{
		eSICEmployerContribution=0;
		}
		//if(parseFloyerContribution=Math.round(eSICEmployerContribution);
		eSICEmployerContribution=eSICEmployerContribution.toFixed(2); 


	//calculate PF Admin Charges
	//pFAdminCharges= 0;


		
		
		$("#totalDeductions").val((totalDeductions).toFixed(2)); // cHANGE
		$("#netSalary").val((netSalary).toFixed(2)); // cHANGE
		$("#pfEmployerContribution").val(pfEmployerContribution);// cHANGE
		$("#eSICEmployerContribution").val((eSICEmployerContribution)); // cHANGE
		$("#pFAdminCharges").val(pFAdminCharges);	   // cHANGE
		
		
		if(!isNaN(totalDeductions)){
			$("#totalDeductions").val(totalDeductions);	   // cHANGE	
		}else{
			var charges = 0;
			$("#totalDeductions").val(charges);;
		}
		
		if(!isNaN(netSalary)){
			$("#netSalary").val(netSalary);	   // cHANGE	
		}else{
			var charges = 0;
			$("#netSalary").val(charges);;
		}
		
		
	}

	function calculaterate(e,flag){

		$("#pfEmployeeContribution").val(0);
		$("#eSICEmployeeContribution").val(0);
		$("#professionTax").val(0);
		$("#lWF").val(0);
		$("#tDS").val(0);
		$("#advanceAdjustment").val(0);
	    var empId = $("#employee_id").val();
		var employeeName = $("#employeeName").val();
		var PAN = $("#PAN").val();
		var totaldays = $("#totaldays").val();
		var basicSalary =$("#basicSalary").val();
		var DA =  $("#DA").val(); 
		var conveyanceAllowance = $("#conveyanceAllowance").val();  
		var otherAllowances = $("#otherAllowances").val();
		var grossSalary = $("#grossSalary").val();
		var pfEmployeeContribution = $("#pfEmployeeContribution").val(); // aDI
		var eSICEmployeeContribution = $("#eSICEmployeeContribution").val();
		var professionTax = $("#professionTax").val();
		var lWF = $("#lWF").val();
		var tDS = $("#tDS").val();
		var otherDeductions = $("#otherDeductions").val();
		var advanceAdjustment = $("#advanceAdjustment").val();
		var totalDeductions = $("#totalDeductions").val();
		var netSalary = $("#netSalary").val();
		var pfEmployerContribution = $("#pfEmployerContribution").val();
		var eSICEmployerContribution = $("#eSICEmployerContribution").val();
		var pFAdminCharges = $("#pFAdminCharges").val();
		var workingdays = $("#wdays").val();
	
		
		
		
	
		
		  if(workingdays < totaldays)
		{
		$("#totaldays").val("");
		alert("Total Days Should be less than or equal to   Working days");
		return false;
		}

//		var grossamountcal=parseFloat(basicSalary) +parseFloat(DA)+parseFloat(conveyanceAllowance)+parseFloat(otherAllowances);
//calculate GrossSalary
		grossSalary=((parseFloat(basicSalary)+parseFloat(DA)+parseFloat(conveyanceAllowance)+parseFloat(otherAllowances))/workingdays)*totaldays;
		grossSalary=Math.round(grossSalary);

		
		//	document.getElementById("grossSalary").innerHTML = Math.round(grossSalary);
		//alert("new grossSalary  after round  off is :"+grossSalary);
		//calculate PF Employee Contribution
		if(parseFloat(grossSalary)>=15000 )
			{
				pfEmployeeContribution=1800
			}
		else
			{
				pfEmployeeContribution=(parseFloat(grossSalary)*0.12);
			}
		pfEmployeeContribution=Math.round(pfEmployeeContribution);
		//alert("new pfEmployeeContribution  after round  off is :"+pfEmployeeContribution);
//		pfEmployeeContribution=pfEmployeeContribution.toFixed(2); 
		//calculate ESIC Employee Contribution
		if(parseFloat(grossSalary) < 21000)
			{
				eSICEmployeeContribution=grossSalary*0.0475;
			}
		else
			{
				eSICEmployeeContribution=0;
			}

		eSICEmployeeContribution=Math.round(eSICEmployeeContribution);
		//alert("new eSICEmployeeContribution  after round  off is :"+eSICEmployeeContribution);
		//eSICEmployeeContribution=eSICEmployeeContribution.toFixed(2); 
		//calculate Profession Tax
		 if(parseFloat(grossSalary) > 7500 && parseFloat(grossSalary) <= 10000)
			{
				professionTax=175;
			}
		 else if(parseFloat(grossSalary) > 10000)
			{
				professionTax=200;
			}
		else
			{
				professionTax=0;
			}
		// professionTax=professionTax.toFixed(2); 
		//calculate lWF
		lWF=0;
		
		
		
		//calculate tDS
		tDS=0;
		
		
		//calculate Advance Adjustment
		advanceAdjustment=0;
		
		
		//calculate Other Deductions
		otherDeductions=0;
		
		
		//calculate Total Deductions
		totalDeductions=(parseFloat(pfEmployeeContribution)+parseFloat(professionTax)+parseFloat(eSICEmployeeContribution)+parseFloat(tDS)+parseFloat(lWF)+parseFloat(advanceAdjustment)+parseFloat(otherDeductions));

		
		
		//calculate Net Salary
		netSalary=parseFloat(grossSalary)-parseFloat(totalDeductions);
		
		
		//calculate PF Employer Contribution
		
		//calculate PF Employee Contribution
		if(parseFloat(grossSalary)>=15000 )
			{
			pfEmployerContribution=1800
			}
		else
			{
			pfEmployerContribution=(parseFloat(grossSalary)*0.12);
			}
		pfEmployerContribution=Math.round(pfEmployerContribution);

		//calculate ESIC Employer Contribution
		if(parseFloat(grossSalary) < 21000)
			{
			eSICEmployerContribution=grossSalary*0.0475;
			}
		else
			{
			eSICEmployerContribution=0;
			}
		

		eSICEmployerContribution=Math.round(eSICEmployerContribution);
		//calculate PF Admin Charges
		
		//pFAdminCharges= 0;
		pFAdminCharges= parseFloat(pFAdminCharges);

		
		

	$("#grossSalary").val(grossSalary);
	$("#pfEmployeeContribution").val(pfEmployeeContribution);
	$("#eSICEmployeeContribution").val(eSICEmployeeContribution);// cHANGE
	$("#professionTax").val(professionTax);

	$("#lWF").val(lWF);
	$("#tDS").val(tDS);
	$("#advanceAdjustment").val(advanceAdjustment);


	$("#otherDeductions").val(otherDeductions);

	//totalDeductions=parseFloat((pfEmployeeContribution)+parseFloat(professionTax)+parseFloat(eSICEmployeeContribution)+parseFloat(tDS)+parseFloat(lWF)+parseFloat(advanceAdjustment)+parseFloat(otherDeductions));

	$("#totalDeductions").val(totalDeductions);
	$("#netSalary").val(netSalary);
	$("#pfEmployerContribution").val(pfEmployerContribution);
	$("#eSICEmployerContribution").val(eSICEmployerContribution);
	if(!isNaN(pFAdminCharges)){
		$("#pFAdminCharges").val(pFAdminCharges);	   // cHANGE	
	}else{
		var charges = 0;
		$("#pFAdminCharges").val(charges);;
	}

		

	}

	
	function validate()
	{
		
		var empId = $("#employeecode").val();
		var id = $("#employee_id").val();

		var employeeName = $("#employeeName").val();
		var PAN = $("#PAN").val();
		var totaldays = $("#totaldays").val();
		var basicSalary =$("#basicSalary").val();
		var DA =  $("#DA").val(); 
		var conveyanceAllowance = $("#conveyanceAllowance").val();  
		var otherAllowances = $("#otherAllowances").val();
		var grossSalary = $("#grossSalary").val();
		var pfEmployeeContribution = $("#pfEmployeeContribution").val();
		var eSICEmployeeContribution = $("#eSICEmployeeContribution").val();
		var professionTax = $("#professionTax").val();
		var lWF = $("#lWF").val();
		var tDS = $("#tDS").val();
		var otherDeductions = $("#otherDeductions").val();
		var advanceAdjustment = $("#advanceAdjustment").val();
		var totalDeductions = $("#totalDeductions").val();
		var netSalary = $("#netSalary").val();
		var pfEmployerContribution = $("#pfEmployerContribution").val();
		var eSICEmployerContribution = $("#eSICEmployerContribution").val();
		var pFAdminCharges = $("#pFAdminCharges").val();
		var workingdays = $("#wdays").val();
	   
		
		if(totaldays.trim()=="" || totaldays.trim()=="0"){
			alert("Please Enter Total Days");
			return false;
		}

		else if(pfEmployeeContribution.trim()=="" ){
			alert("Please Enter at least zero in pfEmployeeContribution");
			return false;
		}
		else if(eSICEmployeeContribution.trim()==""){
			alert("Please Enter   at least zero in  eSICEmployeeContribution");
			return false;
		}
		else if(professionTax.trim()=="" ){
			alert("Please Enter  at least zero in  professionTax");
			return false;
		}
		else if(lWF.trim()==""){
			alert("Please Enter  at least zero in  lWF");
			return false;
		}
		else if(tDS.trim()=="" ){
			alert("Please Enter  at least zero in  tDS");
			return false;
		}
		else if(otherDeductions.trim()=="" ){
			alert("Please Enter at least zero in  otherDeductions");
			return false;
		}
		else if(advanceAdjustment.trim()=="" ){
			alert("Please Enter  at least zero in  advanceAdjustment");
			return false;
		}
		
		
		
		
	}
	
	
	</script>
	<%@include file="/WEB-INF/includes/footer.jsp" %>