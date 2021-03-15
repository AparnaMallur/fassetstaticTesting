 <%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="joda" uri="http://www.joda.org/joda/time/tags"%>
<%@taglib prefix="sec" uri="http://www.springframework.org/security/tags"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<meta http-equiv="X-UA-Compatible" content="IE=edge" />
<meta name="viewport" content="width=device-width, initial-scale=1" />
<!-- The above 3 meta tags *must* come first in the head; any other head content must come *after* these tags -->
<title><spring:message code="APP_TITLE" /></title>
<spring:url value="/resources/images/logo.jpg" var="logoTopIMG" />
<spring:url value="/resources/images/logo-top1.png" var="logoTopIMG1" />
<spring:url value="/resources/css/bootstrap.min.css" var="styleCSSBoostrap" />
<spring:url value="/resources/css/style.css" var="styleCSS" />
<spring:url value="/resources/css/icon.css" var="vimeoCSS" />
<spring:url value="/resources/css/jquery-ui.css" var="calendercss" />

<spring:url value="/resources/css/font-awesome.min.css" var="chosenCSSfont" />
<spring:url value="/resources/images/favicon.ico" var="favicon" />
<spring:url value="/resources/images/user.png" var="user" />
<spring:url value="/resources/images/logo/" var="userlogo" />
<spring:url value="/resources/css/images/user-profile.png" var="nologo" />


<spring:url value="/resources/js/jquery.min.js" var="jquerymin" />
<spring:url value="/resources/js/validations.js" var="valid" />
<spring:url value="/resources/js/bootstrap.min.js" var="bootstrapmin" />
<spring:url value="/resources/js/modernizr.min.js" var="modernizrmin" />
<spring:url value="/resources/js/detect.js" var="detect" />
<spring:url value="/resources/js/fastclick.js" var="fastclick" />
<spring:url value="/resources/js/jquery.slimscroll.js" var="slimscroll" />
<spring:url value="/resources/js/jquery.blockUI.js" var="blockUI" />
<spring:url value="/resources/js/waves.js" var="waves" />
<spring:url value="/resources/js/wow.min.js" var="wowmin" />
<spring:url value="/resources/js/jquery.nicescroll.js" var="nicescroll" />
<!--<spring:url value="/resources/js/jquery.scrollTo.min.js" var="scrollTo" />-->
<spring:url value="/resources/js/app.js" var="app" />
<spring:url value="/resources/js/jquery-ui.js" var="calenderjs" />
<spring:url value="/resources/js/bootstrap-table.js" var="bootstraptable" />
<spring:url value="/resources/js/bootstrap-table-filter-control.js" var="bootstrapfilter" />
<spring:url value="/resources/js/download.js" var="exceldownload" />
<script type="text/javascript" src="${exceldownload}"></script>  
<script type="text/javascript" src="${jquerymin}"></script>
<c:url value="/j_spring_security_logout" var="logoutUrl" />
<link rel="shortcut icon" href="${favicon}" type="image/x-icon">
<link rel="icon" href="${favicon}" type="image/x-icon">
<link href="${styleCSSBoostrap}" rel="stylesheet" />
<link href="${styleCSS}" rel="stylesheet" />
<link href="${chosenCSSfont}" rel="stylesheet" />
<link href="${calendercss}" rel="stylesheet" />
<spring:url value="/resources/images/loading.gif" var="processing" />

</head>
<body class="fixed-left">
     			<c:set var = "userdata" value = '<%=request.getSession().getAttribute("user")%>' />
    
 <div id="wrapper">
   <div class="topbar">
    <div class="topbar-left col-md-2" style="padding-left:0px">
        <div class="text-center side-logo"> 
        <c:choose>
        	<c:when test="${is_updated==true}">
        	 <a class='logo' href="homePage"><img src="${logoTopIMG}" /></a>
        <a class='logo1' href="homePage"><img src="${logoTopIMG1}" /></a>
        	</c:when>
        	<c:otherwise>
        	 <a class='logo' ><img src="${logoTopIMG}" /></a>
        <a class='logo1'><img src="${logoTopIMG1}" /></a>
        	</c:otherwise>
        </c:choose>
       </div>
    </div>
    <div class="navbar navbar-default col-md-10" role="navigation">
        <div class="container">
            <div class="row">
            <div class="col-md-2 col-sm-2 col-xs-2">
                <div class="pull-left">
                    <button type="button" class="button-menu-mobile open-left waves-effect waves-light"> <i class="fa fa-bars"></i></button> <span class="clearfix"></span>
                   </div>
             </div>
            <div class="col-md-8 col-sm-4 col-xs-5" style='text-align:center'>             
                <div class="logo"><span>            ${company_name}</span></div>
             </div>
             <div class="col-md-2 col-sm-2 col-xs-3">                
                <ul class="nav navbar-nav navbar-right pull-right">
                    <li class="dropdown hidden-xs">
                        <a href="#" data-target="#" class="dropdown-toggle waves-effect waves-light notification-icon-box" data-toggle="dropdown" aria-expanded="true"> <i class="fa fa-bell"></i> <span class="badge badge-xs badge-danger"></span> </a>
                        <ul class="dropdown-menu dropdown-menu-lg">
                        	<c:set var = "notifications" value = '<%=request.getSession().getAttribute("notifications")%>' />
                            <li class="text-center notifi-title">Notification <span class="badge badge-xs badge-success"><c:out value = "${notifications.size()}"/> </span></li>                           
                            <c:if test="${reminder != null}">
	                             <li class="list-group">
	                                <a href="#" class="list-group-item">
	                                    <div class="media">
	                                        <div class="media-heading">Password renewal notification</div>
	                                        <p class="m-0"> <small>${reminder}</small> </p>
	                                    </div>
	                                </a>
	                            </li>
                            </c:if>
                            
                            <c:forEach var = "notification" items = "${notifications}">                            	
                             	<li class="list-group">
	                                <a href = "${notification.url}" class="list-group-item">
	                                    <div class="media">
	                                        <div class="media-heading">${notification.header}</div>
	                                        <p class="m-0"> <small>${notification.discription}</small> </p>
	                                    </div>
	                                </a>
	                            </li>
                            </c:forEach>
                       </ul>
                    </li>
                    <li class="dropdown">
                        <a href="" class="dropdown-toggle profile waves-effect waves-light" data-toggle="dropdown" aria-expanded="true">
                             <c:choose>
			                	<c:when test="${company_logo != null}">
			                	
			                	   <img src="${userlogo}${company_logo}"  onerror="this.src='${nologo}'" alt="" class="img-circle">                 	
			                	</c:when>
			                	<c:otherwise>
			                	   <img src="${nologo}" alt="" class="img-circle">    
			                	</c:otherwise>
			                </c:choose>
                            <span class="profile-username"> <c:out value = "${first_name}" /> </span> </a>
                        <ul class="dropdown-menu">
                         	<c:if test="${is_updated==true}">                         
		                            <li><a href="viewUser"> My Profile</a></li>
		                            <li><a href="changePassword"> Change Password</a></li>
                            </c:if>
                            <li class="divider"></li>
                            <li><a href="logout"> Logout</a></li>
                        </ul>
                    </li>
                </ul>
             </div>
            
            </div>
        </div>
    </div>
</div>
<div class="left side-menu" id="a">
            <div class="sidebar-inner slimscrollleft">
               <div class="user-details">
                <div class="text-center"> 
                <c:choose>
                	<c:when test="${company_logo != null}">
                	   <img src="${userlogo}${company_logo}" onerror="this.src='${nologo}'" alt="" class="img-circle">                 	
                	</c:when>
                	<c:otherwise>
                	   <img src="${nologo}" alt="" class="img-circle">    
                	</c:otherwise>
                </c:choose>
                
                </div>
                
                <div class="user-info">
                    <a href="#" class="dropdown-toggle" data-toggle="dropdown" aria-expanded="false"><c:out value = "${username}" /> </a>
                    <p class="text-muted m-0"><i class="fa fa-dot-circle-o text-success"></i> ${rolename}</p>
                </div>
            </div>
            <div id="sidebar-menu">
 		  	<input type="hidden" value="${is_updated}" id="user-status"/>	
 		<c:if test="${is_updated==true}"> 
 					 <ul id='mail-ul'>
	            	 </ul>
 		 </c:if>	            	
            </div>
            <div class="clearfix"></div>
       </div>
</div>
<div class="content-page">
<script type="text/javascript">

$(document).ready(function () {

	<c:if test="${data0 == null && data1== null && data2 == null}">
	var userstatus=document.getElementById("user-status").value;
	var role=<%=request.getSession().getAttribute("role")%>;
    $.ajax({
        type: 'POST',
        url: 'getDynamicList',
        async:true,
        dataType : 'json',
        contentType: 'application/json',
      	success: function (data){                
        	$.each(data[0], function( index, Parrent ) {
        		var show=0;
        		if(role==2)
        		{ show=1; }
        		else
        		{
	            	$.each(data[2], function( index, Showmenu ) {	            		
						  if(Showmenu.menu_Id==Parrent.menu_id)
							{
							  show=1;
							}            		
	            	});
        		}
	            	
	            	if(show==1)
	            		{
			        		var li = document.createElement("li");
				    		  var a = document.createElement("a");
				    		  var span = document.createElement("span");
				    		  var menu_icon=Parrent.menu_icon;
				    		  span.setAttribute('Class','pull-right');		
							var ichild = document.createElement("i");
				    		  ichild.setAttribute('Class','fa fa-plus');
				    		  var span1 = document.createElement("p");
				    		  span1.innerHTML=Parrent.menu_name;
				    		  span1.setAttribute('Class','web-menu');	
				    		  a.appendChild(span1);
				    		  li.appendChild(a);
				    		  var imain=document.createElement("i");
				    		  if(menu_icon=="N/A")
				    		  imain.setAttribute('Class','fa fa-check-circle-o');		
				    		  else
					    		imain.setAttribute('Class','fa '+menu_icon);	
				    		  imain.setAttribute('style','float:left');		
				    		  a.appendChild(imain);		    		  
							   var ul = document.createElement("ul");
					    	   li.setAttribute('id','main_'+Parrent.menu_id);		

								var submenu=0;
								var mlichild = document.createElement("li");
								  var machild = document.createElement("a");
					    		  machild.innerHTML =Parrent.menu_name;
					    		  mlichild.appendChild(machild);
					    		  mlichild.setAttribute("onclick","setmenuid("+Parrent.menu_id+")");  
					    		  machild.setAttribute('href',Parrent.menu_url);
					    		  ul.appendChild(mlichild);
					    		  mlichild.setAttribute('Class','mobilehead-menu');	
								$.each(data[1], function( index, Child ) {
									 
								  if(Parrent.menu_id==Child.parent_id.menu_id)
									{
									 
									  if(userstatus!="false")  
							    		{							    		
							    	  document.getElementById("mail-ul").appendChild(li);
							    		}
									  
						    		  
									  if(role==2)
						        	  { 
										  var lichild = document.createElement("li");
							    		  var achild = document.createElement("a");
							    		  achild.innerHTML =Child.menu_name;
							    		  lichild.appendChild(achild);
							    		  lichild.setAttribute("onclick","setmenuid("+Child.menu_id+")");  
							    		  achild.setAttribute('href',Child.menu_url);
							    		  ul.appendChild(lichild);
							    		  li.setAttribute('Class','has_sub');		
							    		  ul.setAttribute('Class','list-unstyled');		
							    		  a.setAttribute('Class','waves-effect');	
							    		  span.appendChild(ichild);	
							    		  a.appendChild(span);	
							    		  li.setAttribute('id','main_'+Parrent.menu_id);		
							    		  ul.setAttribute('id','submain_'+Parrent.menu_id);							    		
							    		  submenu=1;
										  
						        	  }
									  else
									  {
										$.each(data[2], function( index, Access ) {
											  if(Access.menu_Id==Child.menu_id)
											  {
												  var lichild = document.createElement("li");
									    		  var achild = document.createElement("a");
									    		  achild.innerHTML =Child.menu_name;
									    		  lichild.appendChild(achild);
									    		  lichild.setAttribute("onclick","setmenuid("+Child.menu_id+")");  
									    		  achild.setAttribute('href',Child.menu_url);
									    		  ul.appendChild(lichild);
									    		  li.setAttribute('Class','has_sub');		
									    		  ul.setAttribute('Class','list-unstyled');		
									    		  a.setAttribute('Class','waves-effect');	
									    		  span.appendChild(ichild);	
									    		  a.appendChild(span);	
									    		  li.setAttribute('id','main_'+Parrent.menu_id);		
									    		  ul.setAttribute('id','submain_'+Parrent.menu_id);							    		
									    		  submenu=1;
											  }
										});
										
									  }
				
									}
					    		  
				
								});		
								if(submenu==0)
									{
						    		  a.setAttribute('href',Parrent.menu_url);
									  li.setAttribute("onclick","setmenuid("+Parrent.menu_id+")");  

									}
								else
									{
								    li.setAttribute("onclick","dropDown("+Parrent.menu_id+")");  
					    		//a.setAttribute('href','void(0);');	
								li.appendChild(ul);
									}	
						    	if(userstatus!="false")  
						    		{						    		
						    		document.getElementById("mail-ul").appendChild(li);	
						    		}
						    
	            		}
			});
        	var menuid=localStorage.getItem("menu_aid");
        	   $("#main_"+menuid).addClass('active');    	
        },

        error: function (e) {
            console.log("ERROR: ", e);
        },
        done: function (e) {
            console.log("DONE");
        }
    });
    </c:if>
   
    <c:if test="${data0 != null && data1 != null && data2 != null}">
    menu();
    </c:if>

});
<c:if test="${menuList == null}">
$(document).ready(function () {
	var menuid=localStorage.getItem("menu_aid");
   $.ajax({
        type: 'POST',
        url: 'getActionAccess?menuid='+menuid,
        async:false,
        contentType: 'application/json',
	      	success: function (data){  
	      			
	      	},
	        error: function (e) {
	            console.log("ERROR: ", e);
	        },
	        done: function (e) {
	            console.log("DONE");
	        }        
    });	     	
	
});
</c:if>

function menu()
{
	
	var userstatus=document.getElementById("user-status").value;
	var role=<%=request.getSession().getAttribute("role")%>;
     <c:forEach var = "Parrent" items = "${data0}">
     var parrentMenuId='${Parrent.menu_id}';
     var parrentMenuUrl='${Parrent.menu_url}';
     var parrentMenuName='${Parrent.menu_name}';
     var show=0;
		if(role==2)
		{ 
			show=1; 
		}
		else
 		{
			 <c:forEach var = "Showmenu" items = "${data2}">
			 var ShowmenuId='${Showmenu.menu_Id}';           		
					  if(ShowmenuId==parrentMenuId)
						{
						  show=1;
						}            		
         
         	</c:forEach>
 		}
		
		if(show==1)
 		{
        		var li = document.createElement("li");
	    		  var a = document.createElement("a");
	    		  var span = document.createElement("span");
	    		  var menu_icon='${Parrent.menu_icon}';
	    		  span.setAttribute('Class','pull-right');		
				var ichild = document.createElement("i");
	    		  ichild.setAttribute('Class','fa fa-plus');
	    		  var span1 = document.createElement("p");
	    		  span1.innerHTML=parrentMenuName;
	    		  span1.setAttribute('Class','web-menu');	
	    		  a.appendChild(span1);
	    		  li.appendChild(a);
	    		  var imain=document.createElement("i");
	    		  if(menu_icon=="N/A")
	    		  imain.setAttribute('Class','fa fa-check-circle-o');		
	    		  else
		    		imain.setAttribute('Class','fa '+menu_icon);	
	    		  imain.setAttribute('style','float:left');		
	    		  a.appendChild(imain);		    		  
				   var ul = document.createElement("ul");
		    	   li.setAttribute('id','main_'+parrentMenuId);		

					var submenu=0;
					var mlichild = document.createElement("li");
					  var machild = document.createElement("a");
		    		  machild.innerHTML =parrentMenuName;
		    		  mlichild.appendChild(machild);
		    		  mlichild.setAttribute("onclick","setmenuid("+parrentMenuId+")");  
		    		  machild.setAttribute('href',parrentMenuUrl);
		    		  ul.appendChild(mlichild);
		    		  mlichild.setAttribute('Class','mobilehead-menu');	
		    		  
		    		  
		    		  <c:forEach var = "Child" items = "${data1}">
		    		  var childParentMenuID='${Child.parent_id.menu_id}';
		    		  var childMenuId='${Child.menu_id}';
		    		  var childMenuUrl='${Child.menu_url}';
		    		  var childMenuName='${Child.menu_name}';
		    		  if(parrentMenuId==childParentMenuID)
						{
						 
						  if(userstatus!="false")  
				    		{							    		
				    	  document.getElementById("mail-ul").appendChild(li);
				    		}
						  
			    		  
						  if(role==2)
			        	  { 
							  var lichild = document.createElement("li");
				    		  var achild = document.createElement("a");
				    		  achild.innerHTML =childMenuName;
				    		  lichild.appendChild(achild);
				    		  lichild.setAttribute("onclick","setmenuid("+childMenuId+")");  
				    		  achild.setAttribute('href',childMenuUrl);
				    		  ul.appendChild(lichild);
				    		  li.setAttribute('Class','has_sub');		
				    		  ul.setAttribute('Class','list-unstyled');		
				    		  a.setAttribute('Class','waves-effect');	
				    		  span.appendChild(ichild);	
				    		  a.appendChild(span);	
				    		  li.setAttribute('id','main_'+parrentMenuId);		
				    		  ul.setAttribute('id','submain_'+parrentMenuId);							    		
				    		  submenu=1;
							  
			        	  }
						  else
						  {
							  
							  <c:forEach var = "Access" items = "${data2}">
							  var accessMenuId='${Access.menu_Id}';
							  if(accessMenuId==childMenuId)
							  {
								  var lichild = document.createElement("li");
					    		  var achild = document.createElement("a");
					    		  achild.innerHTML =childMenuName;
					    		  lichild.appendChild(achild);
					    		  lichild.setAttribute("onclick","setmenuid("+childMenuId+")");  
					    		  achild.setAttribute('href',childMenuUrl);
					    		  ul.appendChild(lichild);
					    		  li.setAttribute('Class','has_sub');		
					    		  ul.setAttribute('Class','list-unstyled');		
					    		  a.setAttribute('Class','waves-effect');	
					    		  span.appendChild(ichild);	
					    		  a.appendChild(span);	
					    		  li.setAttribute('id','main_'+parrentMenuId);		
					    		  ul.setAttribute('id','submain_'+parrentMenuId);							    		
					    		  submenu=1;
							  }      		
		         
		         	            </c:forEach>
						  }
	
						}       		
         
         	         </c:forEach>
         	
					if(submenu==0)
						{
			    		  a.setAttribute('href',parrentMenuUrl);
						  li.setAttribute("onclick","setmenuid("+parrentMenuId+")");  

						}
					else
						{
					    li.setAttribute("onclick","dropDown("+parrentMenuId+")");  
		    			
					li.appendChild(ul);
						}	
			    	if(userstatus!="false")  
			    		{						    		
			    		document.getElementById("mail-ul").appendChild(li);	
			    		}
			    
 		}
	</c:forEach>
	var menuid=localStorage.getItem("menu_aid");
	   $("#main_"+menuid).addClass('active');  
	  
}
function dropDown(id) {
		var mainmenu='main_'+id;
		var submain='submain_'+id;
		$('#mail-ul li').removeClass('selected');
        var submenu=document.getElementById(submain);
        if(submenu.style.display=="block")
        	{
        	document.getElementById(submain).style.display="none";
            $('#'+mainmenu).removeClass("selected"); 
        	}
        else
        	{
    	    $('#mail-ul li ul').css('display','none');
        	document.getElementById(submain).style.display="block";
            $('#'+mainmenu).addClass("selected"); 
        	}
	}
	function setmenuid(menuid)
	{ 
		localStorage.setItem("menu_aid", menuid);
		//main_2
		 $("#main_"+menuid).addClass('active');        
		//alert(localStorage.getItem("menu_aid"));
	}
</script>