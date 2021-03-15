<%@include file="/WEB-INF/includes/header.jsp"%>
<%@page import="com.fasset.controller.abstracts.MyAbstractController"%>
<spring:url value="/resources/images/edit.png" var="editImg" />
<spring:url value="/resources/images/view.png" var="viewImg" />
<spring:url value="/resources/images/delete.png" var="deleteImg" />

<script type="text/javascript" src="${valid}"></script>
<div class="breadcrumb">
	<h3>Inventory Quantity Adjustment</h3>
	<a href="homePage">Home</a> » <a href="#">Inventory Quantity Adjustment</a>
</div>
<div class="col-md-12">
	<c:if test="${successMsg != null}">
		<div class="successMsg" id="successMsg">
			<strong>${successMsg}</strong>
		</div>
	</c:if>
	<div class="col-md-12 text-center">
		<button id="add"  type="button" onclick = "add()">
			Add New Inventory Quantity Adjustment
		</button>
	<!-- 	//<button class="fassetBtn acs-insert" type="button" onclick="add()">Add New Inventory Quantity Adjustment</button> -->
	</div>
	<div class="borderForm">
		<table id="table" data-toggle="table" data-search="false"
			data-escape="false" data-filter-control="true"
			data-show-export="false" data-click-to-select="true"
			data-pagination="true" data-page-size="10" data-toolbar="#toolbar"
			class="table">
			<thead>
				<tr>
					<th class='test'>Action</th>
					<th data-field="date" data-filter-control="input"
						data-sortable="true">Date</th>
					<th data-field="voucherNumber" data-filter-control="input"
						data-sortable="true">Product</th>
					<th data-field="billNumber" data-filter-control="input"
						data-sortable="true">Quantity</th>
					<th data-field="stock" data-filter-control="input"
						data-sortable="true">Total Quantity in Stock</th>
				</tr>
			</thead>
			<tbody>
				<c:forEach var="invQuantity" items="${inventoryQuantityList}">
					<tr>
						<td style="text-align: left;">
								<i  id='view-ico' onclick = "viewInvQuantity('${invQuantity.inventory_adj_id}')" class="acs-view fa fa-search" ></i>
								<%-- <i  id='update-ico' onclick = "editInvQuantity('${invQuantity.inventory_adj_id}')" class="acs-update fa fa-pencil" ></i> --%>
								<i  id='delete-ico' onclick = "deleteInvQuantity('${invQuantity.inventory_adj_id}')" class="acs-delete fa fa-times" ></i>	
						</td>
						<td style="text-align: left;">
						<fmt:parseDate value="${invQuantity.date}" pattern="yyyy-MM-dd" var="parsedDate" type="date" />
                   		 <fmt:formatDate value="${parsedDate}" var="date" type="date" pattern="dd-MM-yyyy" />
						 ${date}</td>
						<td style="text-align: left;">${invQuantity.product.product_name}</td>
						<td style="text-align: left;">${invQuantity.quantity}</td>
						<td style="text-align: left;">${invQuantity.stock.quantity}</td>						
					</tr>
				</c:forEach>
			</tbody>
		</table>
	</div>
</div>
<script type="text/javascript">
	$(function() {		
	    setTimeout(function() {
	        $("#successMsg").hide();
	    }, 3000);   
		
	});
	function viewInvQuantity(id){
		window.location.assign('<c:url value="viewInvQuantity"/>?id='+id);
	}
	
	function add(){
		window.location.assign('<c:url value="inventoryQuantity"/>');
	}
	
	function editInvQuantity(id){
		window.location.assign('<c:url value="editInvQuantity"/>?id='+id);
	}
	
	
	function deleteInvQuantity(id){
		 if (confirm("Are you sure you want to delete record?") == true) {
		window.location.assign('<c:url value="deleteInvQuantity"/>?id='+id);
		 } 
	}
	
	$(document).ready(function () {
		var menuid=localStorage.getItem("menu_aid");
	  /*  $.ajax({
	        type: 'POST',
	        url: 'getActionAccess?menuid='+menuid,
	        async:false,
            contentType: 'application/json',
		      	success: function (data){  
		      	
		      		if(data["access_Insert"]==true)
		      		{
		      		    $("button").removeClass("acs-insert");
		      		}
		      		if(data["access_Update"]==true)
		      		{
		      		    $("i.update-ico").removeClass("acs-update");
		      		}
		      		if(data["access_View"]==true)
		      		{
		      		    $("i.view-ico").removeClass("acs-view");
		      		}
		      		if(data["access_Delete"]==true)
		      		{
		      		    $("i.delete-ico").removeClass("acs-delete");
		      		}
		      		
		      	},
		        error: function (e) {
		            console.log("ERROR: ", e);
		        },
		        done: function (e) {
		            console.log("DONE");
		        }        
	    });	     	 */
	    var access_Insert;
	     var access_Update;
	     var access_View;
	     var access_Delete;
	     var menuid=localStorage.getItem("menu_aid");
	     
	     
	     <c:forEach var = "menu" items = "${menuList}">
			var id='${menu.menu_Id}';
			if(id==menuid){
				
				var access_Insert='${menu.access_Insert}';
				var access_Update='${menu.access_Update}';
				var access_View='${menu.access_View}';
				var access_Delete='${menu.access_Delete}';
			   
				
				 <c:forEach var = "entry" items = "${receiptList}">
				 
				 if(access_Insert=='false')
		      		{
						
						 
						var link = document.getElementById("add");
						link.style.display = 'none';
						
		      		}
				if(access_View=='false')
	      		{
					
					 var ID='view_${entry.receipt_id}';
					var link = document.getElementById(ID);
					link.style.display = 'none';
					
	      		}
				if(access_Update=='false')
	      		{
					
					 var ID='update_${entry.receipt_id}';
					var link = document.getElementById(ID);
					link.style.display = 'none';
					
	      		}
				if(access_Delete=='false')
	      		{

					var ID='delete_${entry.receipt_id}';
					var link = document.getElementById(ID);
					link.style.display = 'none';
	      		}
				 </c:forEach>
				
			}
	    </c:forEach>
	    document.getElementById('progress-div').style.display="none";
		$("#file-div").show();	
		 var file='<c:out value="${successMsg}"/>';
		 if(file=="NA")
			{
				   $('#file-model').modal({
				    	show: true,
				   	});	
			}
	
	});
</script>
<%@include file="/WEB-INF/includes/footer.jsp"%>
