<%@include file="/WEB-INF/includes/header.jsp"%>
<%@page import="com.fasset.controller.abstracts.MyAbstractController"%>
<script type="text/javascript" src="${valid}"></script>
<c:set var="SIZE_EIGHTEEN"><%=MyAbstractController.SIZE_EIGHTEEN%></c:set>
<c:set var="SIZE_THIRTY"><%=MyAbstractController.SIZE_THIRTY%></c:set>
<c:set var="SIZE_TEN"><%=MyAbstractController.SIZE_TEN%></c:set>
<c:set var="SIZE_SIX"><%=MyAbstractController.SIZE_SIX%></c:set>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>

<div class="breadcrumb">
	<h3>Menu  Access Master</h3>					
	 <a href="homePage">Home</a> » <a href="#">Menu Access</a> 
	 	 
</div>	

<div class="col-md-12 wideform">
	<div class="successMsg" id = "successMsg"> <strong id="success" style='display:none'></strong>
	</div>
	
	<div class="fassetForm">
	<form:form id="accessMenuForm"  name="myform" commandName = "MenuAccess">
			
	 <div class="row">	
				<div class="col-md-2 control-label"><label>User Role<span>*</span></label></div>		
				<div class="col-md-10">	
					<form:select path="access_List" id="role_id" class="logInput" placeholder="" onchange="getAllList(this.value)">	
					<form:option value = "N/A">-- Select Role --</form:option>	
					
					<c:forEach var="role" items="${roleMaster}"> 										
						<form:option value = "${role.key}">${role.value}</form:option>					
					</c:forEach>
					</form:select>
				</div>	
	 </div>	
	
	
	 <div class="row">	
				<div class="col-md-2 control-label"><label>User Name<span>*</span></label></div>		
				<div class="col-md-10">					
					<form:select path="access_List" class="logInput" id="userName" placeholder="userName" onchange="getMenuList(this.value)">
						<form:option value = "N/A">-- Select Name --</form:option>	 -
					 </form:select>
				
				</div>	
	 </div>	
	
	
		<div class="row" style="">
		<p id="success">
		</p>
		<div class="col-md-2"> Menu Name</div>
		    <div class="col-md-2"> All </div>
		    <div class="col-md-2"> Create</div>
		    <div class="col-md-2"> Update</div>
		    <div class="col-md-2"> View</div>
		    <div class="col-md-2"> Delete</div>
		</div>
		<div id="menuaccess" style='height: 300px;overflow-y: scroll;'>
				<c:forEach var="MenuMaster" items="${menuMasterList}">
					<c:if test="${MenuMaster.parent_id==Null}">
						<div class="row" style="background-color:#4d5054;color:#fff;padding:2px 0px 8px 0px">
							<div class="col-md-2">
								<span style="color:#fff">${MenuMaster.menu_name}</span>
							</div>
							<div class="col-md-2 " style=''>
 		<c:set var="menuname" value="${fn:replace(MenuMaster.menu_name,' ', '')}" />
								<form:input path="access_List" id="access_List"
									value=" ${MenuMaster.menu_id}" hidden="hidden" />
					<input style="position: absolute; left: 7px;"
					id="access_List${MenuMaster.menu_id}" type="checkbox" onclick='checkall(this,document.myform.${menuname}, ${MenuMaster.menu_id},1)' >
							</div>

							<div class="col-md-2">
								<form:input path="access_Insert" id="access_Insert"
									value=" ${MenuMaster.menu_id}" hidden="hidden" />
								<input style="position: absolute; left: 7px;"
									id="access_Insert${MenuMaster.menu_id}"
									name="${menuname}"  type="checkbox"
									onchange="create(this,${MenuMaster.menu_id})">
							</div>

							<div class="col-md-2">
								<form:input path="access_Update" id="access_Update"
									value=" ${MenuMaster.menu_id}" hidden="hidden" />
								<input style="position: absolute; left: 7px;"
									id="access_Update${MenuMaster.menu_id}"
									name="${menuname}"  type="checkbox"
									onchange="update(this,${MenuMaster.menu_id})">
							</div>

							<div class="col-md-2">
								<form:input path="access_View" id="access_View"
									value=" ${MenuMaster.menu_id}" hidden="hidden" />
								<input style="position: absolute; left: 7px;"
									id="access_View${MenuMaster.menu_id}"
									name="${menuname}"  type="checkbox"
									onchange="view(this,${MenuMaster.menu_id})" onclick='checkall(this,${menuname}, ${MenuMaster.menu_id},2)'>
							</div>
							<div class="col-md-2">
								<form:input path="access_Delete" id="access_Delete"
									value=" ${MenuMaster.menu_id}" hidden="hidden" />
								<input style="position: absolute; left: 7px;"
									id="access_Delete${MenuMaster.menu_id}"
									name="${menuname}" type="checkbox"
									onchange="deleteAccess(this,${MenuMaster.menu_id})">
							</div>

						</div>
					</c:if>
					<c:forEach var="MenuMasterdetails" items="${menuMasterList}">
						<c:if
							test="${MenuMasterdetails.parent_id.menu_id==MenuMaster.menu_id}">
							<div class="row" id="child-menu-${MenuMaster.menu_id}">
								<div class="col-md-2">
									<span>${MenuMasterdetails.menu_name}</span>
								</div>
 		<c:set var="menuchildname" value="${fn:replace(MenuMasterdetails.menu_name,' ', '')}" />
 		<c:set var="menuchildname" value="${fn:replace(menuchildname,'&', '')}" />
								<div class="col-md-2 ">
									<form:input path="access_List" id="access_List"
										value=" ${MenuMasterdetails.menu_id}" hidden="hidden" />
									<input style="position: absolute; left: 7px;"
										id="access_List${MenuMasterdetails.menu_id}" type="checkbox"
										onclick="checkall(this,document.myform.${menuchildname}, ${MenuMasterdetails.menu_id},0)" >
								</div>

								<div class="col-md-2">
									<form:input path="access_Insert" id="access_Insert"
										value=" ${MenuMasterdetails.menu_id}" hidden="hidden" />
									<input style="position: absolute; left: 7px;"
										id="access_Insert${MenuMasterdetails.menu_id}"
										name="${menuchildname}"  type="checkbox"
										onchange="create(this,${MenuMasterdetails.menu_id})">
								</div>

								<div class="col-md-2">
									<form:input path="access_Update" id="access_Update"
										value=" ${MenuMasterdetails.menu_id}" hidden="hidden" />
									<input style="position: absolute; left: 7px;"
										id="access_Update${MenuMasterdetails.menu_id}"
										name="${menuchildname}"  type="checkbox"
										onchange="update(this,${MenuMasterdetails.menu_id})">
								</div>

								<div class="col-md-2">
									<form:input path="access_View" id="access_View"
										value=" ${MenuMasterdetails.menu_id}" hidden="hidden" />
									<input style="position: absolute; left: 7px;"
										id="access_View${MenuMasterdetails.menu_id}"
										name="${menuchildname}"  type="checkbox"
										onchange="view(this,${MenuMasterdetails.menu_id})">
								</div>
								<div class="col-md-2">
									<form:input path="access_Delete" id="access_Delete"
										value=" ${MenuMasterdetails.menu_id}" hidden="hidden" />
									<input style="position: absolute; left: 7px;"
										id="access_Delete${MenuMasterdetails.menu_id}"
										name="${menuchildname}" type="checkbox"
										onchange="deleteAccess(this,${MenuMasterdetails.menu_id})">
								</div>

							</div>
						</c:if>
					</c:forEach>
				</c:forEach>
			</div>
		<div class="row text-center-btn">
				<button class="fassetBtn" type="submit" onclick = "return checkdata()" >
					<spring:message code="btn_save" />
				</button>
				<button class="fassetBtn" type="button" onclick = "cancel()">
					<spring:message code="btn_cancel" />
				</button>
			</div>
	</form:form>	
	</div>
</div>
<script type="text/javascript">
 function cancel()
 {
	 <c:forEach  var = "MenuMaster" items = "${menuMasterList}">
		document.getElementById("access_List${MenuMaster.menu_id}").checked = false;
		document.getElementById("access_Insert${MenuMaster.menu_id}").checked = false;
		document.getElementById("access_Update${MenuMaster.menu_id}").checked = false;
		document.getElementById("access_View${MenuMaster.menu_id}").checked = false;
		document.getElementById("access_Delete${MenuMaster.menu_id}").checked = false;
	</c:forEach>
 }
</script>
<script type="text/javascript">
	var access = [];
	var accessMaster=[];
	$(function() {
		
		<c:forEach var = "MenuMaster" items = "${menuMasterList}">
			access.push({"menuId":${MenuMaster.menu_id}, "access":[false,false,false,false,false]});
			<c:if test="${MenuMaster.parent_id==Null}">
			console.log("menu master Id");
			console.log(${MenuMaster.menu_id});
			accessMaster.push({"menuId":${MenuMaster.menu_id}});
			</c:if>
		</c:forEach>
		
		console.log(JSON.stringify(access));
	});
	
	function checkall(e,chk,menuId,flag) {
		var length = access.length;
		
		if(e.checked){
			
		//	Listcreate(e,menuId);
			
			
	 		
	 		if(flag==2){
				var myRegExp3 = /access_View/;
				var opt = 'access_View' +menuId;
				var b= document.getElementById(opt);
				var cid=0;
				 console.log("view click");
				  b.checked=true;
				  for(i = 0; i < length; i++){
						if(access[i].menuId == menuId){
							for (j = 0; j <= 4; j++) {
								access[i].access[3] = true;
							  }
							
							console.log(JSON.stringify(access));
						}
					}
				  $('#child-menu-'+menuId+' input[type="checkbox"]').each(function(){
					 
					 
					  if(myRegExp3.test($(this).attr('id'))){
						 
						var a= document.getElementById($(this).attr('id'));
						 
						  a.checked=true;
						  cid=$(this).attr('id').replace(myRegExp3, '');
					  }
					  for(i = 0; i < length; i++){
							if(access[i].menuId == cid){
								for (j = 0; j <= 4; j++) {
									access[i].access[3] = true;
								  }
								
								console.log(JSON.stringify(access));
							}
						}
					});
				 
				  
				  console.log("view click done");
				  
			}
	 		else{ 
	 			for (i = 0; i < chk.length; i++) {
	 		
			    chk[i].checked = true; 
			  }
	 			for(i = 0; i < length; i++){
					if(access[i].menuId == menuId){
						for (j = 0; j <= 4; j++) {
							access[i].access[j] = true;
						  }
						console.log(JSON.stringify(access));
					}
				} 
			if(flag==1)
			{
				
				 $('#child-menu-'+menuId+' input[type="checkbox"]').each(function(){
			            $(this).prop('checked', true);
			            var cid=0;
			            var myRegExp = /access_List/;
			            var myRegExp1 = /access_Insert/;
			            var myRegExp2 = /access_Update/;
			            var myRegExp3 = /access_View/;
			            var myRegExp4 = /access_Delete/;
			            if(myRegExp.test($(this).attr('id')))
			            	{
			            	cid=$(this).attr('id').replace(myRegExp, '');
			            	}
			            else if(myRegExp1.test($(this).attr('id')))
			            	{
			            	cid=$(this).attr('id').replace(myRegExp1, '');
			            	}
			            else if(myRegExp2.test($(this).attr('id')))
		            	{
			            	cid=$(this).attr('id').replace(myRegExp2, '');
		            	}
			            else if(myRegExp3.test($(this).attr('id')))
		            	{
			            	cid=$(this).attr('id').replace(myRegExp3, '');
		            	}
			            else if(myRegExp4.test($(this).attr('id')))
		            	{
			            	cid=$(this).attr('id').replace(myRegExp4, '');
		            	}
			            for(i = 0; i < length; i++){
							if(access[i].menuId == cid){
								for (j = 0; j <= 4; j++) {
									access[i].access[j] = true;
								  }
							}
			            }
			         
			        });
			}
	 		}
		}
		else{
			if(flag==2){
				
				var myRegExp3 = /access_View/;
				var opt = 'access_View' +menuId;
				var b= document.getElementById(opt);
				var cid=0;
				 console.log("view click");
				  b.checked=false;
				  for(i = 0; i < length; i++){
						if(access[i].menuId == menuId){
							for (j = 0; j <= 4; j++) {
								access[i].access[3] = false;
							  }
							
							console.log(JSON.stringify(access));
						}
					}
				  $('#child-menu-'+menuId+' input[type="checkbox"]').each(function(){
					 
					 
					  if(myRegExp3.test($(this).attr('id'))){
						 
						var a= document.getElementById($(this).attr('id'));
						 
						  a.checked=false;
						  cid=$(this).attr('id').replace(myRegExp3, '');
					  }
					  for(i = 0; i < length; i++){
							if(access[i].menuId == cid){
								for (j = 0; j <= 4; j++) {
									access[i].access[3] = false;
								  }
								
								console.log(JSON.stringify(access));
							}
						}
					});
				 
				  
				  console.log("view click done");
				  
			}
	 		else{ 
			
			for (i = 0; i < chk.length; i++) {
			    chk[i].checked = false; 
			  }
			
			for(i = 0; i < length; i++){
				if(access[i].menuId == menuId){
					for (j = 0; j <= 4; j++) {
						access[i].access[j] = false;
					  }
					console.log(JSON.stringify(access));
				}
			}
			if(flag==1)
			{
				//alert("flag true n");
				 $('#child-menu-'+menuId+' input[type="checkbox"]').each(function(){
			            $(this).prop('checked', false);
			            var cid=0;
			            var myRegExp = /access_List/;
			            var myRegExp1 = /access_Insert/;
			            var myRegExp2 = /access_Update/;
			            var myRegExp3 = /access_View/;
			            var myRegExp4 = /access_Delete/;
			            if(myRegExp.test($(this).attr('id')))
			            	{
			            	cid=$(this).attr('id').replace(myRegExp, '');
			            	}
			            else if(myRegExp1.test($(this).attr('id')))
			            	{
			            	cid=$(this).attr('id').replace(myRegExp1, '');
			            	}
			            else if(myRegExp2.test($(this).attr('id')))
		            	{
			            	cid=$(this).attr('id').replace(myRegExp2, '');
		            	}
			            else if(myRegExp3.test($(this).attr('id')))
		            	{
			            	cid=$(this).attr('id').replace(myRegExp3, '');
		            	}
			            else if(myRegExp4.test($(this).attr('id')))
		            	{
			            	cid=$(this).attr('id').replace(myRegExp4, '');
		            	}
			            for(i = 0; i < length; i++){
							if(access[i].menuId == cid){
								for (j = 0; j <= 4; j++) {
									access[i].access[j] = false;
								  }
							}
			            }
			        });
			}
	 		}
		}  
		  
	}
	
	function Listcreate(e, menuId){
		
		var length = access.length;
		if(e.checked){
			for(i = 0; i < length; i++){
				if(access[i].menuId == menuId){
					access[i].access[0] = true;
					console.log(JSON.stringify(access));
				}
			}
		}
		else{
			
			$("#access_List").checked = false;
			for(i = 0; i < length; i++){
				if(access[i].menuId == menuId){
					access[i].access[0] = false;
					console.log(JSON.stringify(access));
				}
			}
		}
	}
	function create(e, menuId){
		
		var length = access.length;
		if(e.checked){
			for(i = 0; i < length; i++){
				if(access[i].menuId == menuId){
					access[i].access[1] = true;
					console.log(JSON.stringify(access));
				}
			}
		}
		else{
			
			$("#access_List").checked = false;
			for(i = 0; i < length; i++){
				if(access[i].menuId == menuId){
					access[i].access[1] = false;
					console.log(JSON.stringify(access));
				}
			}
		}
	}
	
	function update(e, menuId){
		var length = access.length;
		if(e.checked){
			for(i = 0; i < length; i++){
				if(access[i].menuId == menuId){
					access[i].access[2] = true;
					console.log(JSON.stringify(access));
				}
			}
		}
		else{
			$("#access_List").checked = false;
			for(i = 0; i < length; i++){
				if(access[i].menuId == menuId){
					access[i].access[2] = false;
					console.log(JSON.stringify(access));
				}
			}
		}
	}
	
	function view(e, menuId){
		var length = access.length;
		if(e.checked){
			for(i = 0; i < length; i++){
				if(access[i].menuId == menuId){
					access[i].access[3] = true;
					console.log(JSON.stringify(access));
				}
			}
		}
		else{
			$("#access_List").checked = false;
			for(i = 0; i < length; i++){
				if(access[i].menuId == menuId){
					access[i].access[3] = false;
					console.log(JSON.stringify(access));
				}
			}
		}
	}
	
	function deleteAccess(e, menuId){
		var length = access.length;
		if(e.checked){
			for(i = 0; i < length; i++){
				if(access[i].menuId == menuId){
					access[i].access[4] = true;
					console.log(JSON.stringify(access));
				}
			}
		}
		else{
			$("#access_List").checked = false;
			for(i = 0; i < length; i++){
				if(access[i].menuId == menuId){
					access[i].access[4] = false;
					console.log(JSON.stringify(access));
				}
			}
		}
	}
</script>
<script type="text/javascript">
var role_Id;
var allWithMenu;
function getAllList(value)
{
	var id=value;
	var admin=0;
	var loginRole = ${role};
	//alert(loginRole);
	role_Id=id;
	<c:forEach  var = "MenuMaster" items = "${menuMasterList}">
		document.getElementById("access_List${MenuMaster.menu_id}").checked = false;
		document.getElementById("access_Insert${MenuMaster.menu_id}").checked = false;
		document.getElementById("access_Update${MenuMaster.menu_id}").checked = false;
		document.getElementById("access_View${MenuMaster.menu_id}").checked = false;
		document.getElementById("access_Delete${MenuMaster.menu_id}").checked = false;
		if(role_Id== 1 && loginRole!=2){
			
			document.getElementById("access_List${MenuMaster.menu_id}").disabled  = true;
			document.getElementById("access_Insert${MenuMaster.menu_id}").disabled  = true;
			document.getElementById("access_Update${MenuMaster.menu_id}").disabled  = true;
		
			document.getElementById("access_Delete${MenuMaster.menu_id}").disabled  = true;
		}
		else{
			document.getElementById("access_List${MenuMaster.menu_id}").disabled  = false;
			document.getElementById("access_Insert${MenuMaster.menu_id}").disabled  = false;
			document.getElementById("access_Update${MenuMaster.menu_id}").disabled  = false;
		
			document.getElementById("access_Delete${MenuMaster.menu_id}").disabled  = false;
			
		}
	</c:forEach>

	
	$.ajax({
        type: "POST",
        url: "getList?id=" + id,
        contentType: 'application/json',
        dataType: 'json',
        
        success: function (data) {
        	console.log("User Name==="+data);
        	 $('#userName').empty();
        	 $('#userName').append('<option value="">Select Name</option>');
        	 $('#userName').append('<option value="0">All</option>');
        	 $.each(data, function (index, UserInfo) {
        		 
        		 
                 $('#userName').append("<option value = " + UserInfo.user_id + ">" + UserInfo.username + "</option>");
                         
             });
        },
        error: function (e) {
            console.log("ERROR: ", e);
            
        },
        done: function (e) {
            
            console.log("DONE");
        }
    });
}
</script>

<script type="text/javascript">
function getMenuList(value)
{
	allWithMenu=value;
	<c:forEach  var = "MenuMaster" items = "${menuMasterList}">
	document.getElementById("access_List${MenuMaster.menu_id}").checked = false;
		document.getElementById("access_Insert${MenuMaster.menu_id}").checked = false;
		document.getElementById("access_Update${MenuMaster.menu_id}").checked = false;
		document.getElementById("access_View${MenuMaster.menu_id}").checked = false;
		document.getElementById("access_Delete${MenuMaster.menu_id}").checked = false;
	</c:forEach>
	
	var length = access.length;
	
	for(i = 0; i < length; i++){		
		access[i].access[0] = false;
		access[i].access[1] = false;
		access[i].access[2] = false;
		access[i].access[3] = false;	
		access[i].access[4] = false;

	}
	var id=value;
	
	$.ajax({
        type: "POST",
        url: "getMenuAccessList?id="+id+"&role_Id="+role_Id,
        contentType: 'application/json',
        dataType: 'json',
        
        success: function (data) {
       		
        	 var myObjectId=0;
			 var access_Insert=false;
			 var access_Delete=false;
			 var access_Update=false;
			 var access_View=false;
			 var access_List=false;
//alert("ls");

			 var result=(data + '').length ;
			 var menulength = accessMaster.length;
			 
			 var myRegExp3 = /access_View/;
			 var menuId;
			 if(role_Id==1 && result==0){
				 
				 for(i=0;i<menulength;i++){
					
					  menuId=accessMaster[i].menuId;
					  opt='access_View' + menuId;
					  var b= document.getElementById( opt);
						//  access[cid][3]=true;
						  b.checked=true;
					 //alert(menuId);
					 $('#child-menu-'+menuId+' input[type="checkbox"]').each(function(){
						 if(myRegExp3.test($(this).attr('id'))){
							
							var a= document.getElementById( $(this).attr('id'));
							
							  a.checked=true;
						 }
						});
				 }
				 for(i = 0; i < length; i++){
						//if(access[i].menuId == cid){
						//	for (j = 0; j <= 4; j++) {
								access[i].access[3] = true;
							//  }
						//}
		            }
				
			 }
			 else{
				// alert("records");
        	$.each(data, function(key, value){
       	  		myObjectId=value.menu_Id;
			  	access_List=value.access_List;
			  	access_Insert=value.access_Insert;
			  	access_Delete=value.access_Delete;
			  	access_Update=value.access_Update;
			  	access_View=value.access_View;
			  
				for(i = 0; i < length; i++){
			 		
					if(access[i].menuId == myObjectId){
						access[i].access[0] = access_List;
						access[i].access[1] = access_Insert;
						access[i].access[2] = access_Update;
						access[i].access[3] = access_View;
						access[i].access[4] = access_Delete;
						
					}
					
				}
				console.log("Onchange"+JSON.stringify(access));
			 	console.log("Onchange"+key, value);
			 	$("#access_List"+myObjectId).prop( "checked", access_List );
			 	$("#access_Insert"+myObjectId).prop( "checked", access_Insert );
			 	$("#access_Delete"+myObjectId).prop( "checked", access_Delete );
			 	$("#access_Update"+myObjectId).prop( "checked", access_Update );
			 	$("#access_View"+myObjectId).prop( "checked", access_View );
			 				
			});
        }	
        },
        error: function (e) {
            console.log("ERROR: ", e);
            
        },
        done: function (e) {
            
            console.log("DONE");
        }
    });
}
</script>
<script type="text/javascript">
 $('#accessMenuForm').submit(function (e) {
	
        $.ajax({
            type: "post",
            url: "menuAccessStore?role_Id="+role_Id+"&allWithMenu="+allWithMenu,
           	data: JSON.stringify(access),
            contentType: 'application/json',
             success: function (data)
            { 
            	 <c:forEach  var = "MenuMaster" items = "${menuMasterList}">
            		document.getElementById("access_List${MenuMaster.menu_id}").checked = false;
            			document.getElementById("access_Insert${MenuMaster.menu_id}").checked = false;
            			document.getElementById("access_Update${MenuMaster.menu_id}").checked = false;
            			document.getElementById("access_View${MenuMaster.menu_id}").checked = false;
            			document.getElementById("access_Delete${MenuMaster.menu_id}").checked = false;
            		</c:forEach>
            	document.getElementById('success').innerHTML = data; 
            	$("#success").css('display', 'inline');
                $("html, body").animate({scrollTop: 0}, 600);
             },
             error: function (e) {
                console.log("ERROR: ", e);
                
            }
        });
     return false;
    });
 function checkdata()
 {
	// alert("ok");
	 //role_id
	 //userName
	 var role_id=document.getElementById("role_id").value;
	 var user=document.getElementById("userName").value;
	//	var length = access.length;
    	 if(role_id=="N/A")
		 {
		 alert("Please Select Role");
		 	return false;
		 }
	  if((user=="N/A") || (user=="") || (user==" "))
		 {
			 alert("Please Select User");
			 return false;
		 }
	 else
		 {		
			return true;
		}		
 }
</script>

<%@include file="/WEB-INF/includes/footer.jsp" %>