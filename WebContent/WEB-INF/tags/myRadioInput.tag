<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>

<%@ attribute name="fase" required="true"%>
<%@ attribute name="id" required="true"%>
<%@ attribute name="ids" required="true"%>
<%@ attribute name="lbl" required="true"%>
<%@ attribute name="lbltp" required="false"%>
<%@ attribute name="idslbl" required="true"%>
<%@ attribute name="lblstp" required="false"%>
<%@ attribute name="vari" required="true"%>
<%@ attribute name="divChild" required="false"%>
<%@ attribute name="tblIDChild" required="false"%>
<%@ attribute name="contrID" required="false"%>
<%@ attribute name="prefix" required="true"%>
<%@ attribute name="subgrp" required="false"%>
<%@ attribute name="vertAlign" required="false"%>
<%@ attribute name="img" required="false"%>
<%@ attribute name="open" required="false"%>
<%@ attribute name="noClassLongRadio" required="false"%>
<%@ attribute name="noClassQuestion" required="false"%>
<%@ attribute name="subq" required="false"%>
<%@ attribute name="basic" required="false"%>
<%@ attribute name="alert" required="false"%>
<%@ attribute name="optAlert" required="false"%>
<%@ attribute name="grandfather" required="false"%>
<%@ attribute name="grandson" required="false"%>
<%@ attribute name="divChildren" required="false"%>
<%@ attribute name="argument" required="false"%>
<%@ attribute name="argID" required="false"%>
<%@ attribute name="rela" required="false"%>

<c:set var="classQuestion" value="revQuestion" />
<c:if test="${fase ne 'REVIEW' || subgrp == 'true'}">
	<c:set var="classQuestion" value="question" />
	<c:if test="${noClassQuestion == 'true'}">
		<c:set var="classQuestion" value="" />
	</c:if>
</c:if>
<c:set var="classRadio" value="radioLong radio" />
<c:if test="${noClassLongRadio == 'true'}">
	<c:set var="classRadio" value="radio" />
</c:if>

<c:set var="classTableRadio" value="" />
<c:set var="classStyle" value="float: left" />
<c:if test="${vertAlign == 'true'}">
	<c:set var="classTableRadio" value="tableDisplay" />
	<c:set var="classStyle" value="" />
</c:if>

<c:if test="${alert == 'true'}">
	<c:set var="alert_radio"><spring:message code="alert_radio" /></c:set>
</c:if>

<c:set var="arrayIDs" value="${fn:split(ids, ',')}" />
<c:set var="arrayIDslbl" value="${fn:split(idslbl, ',')}" />
<c:set var="arrayLBLstp" value="${fn:split(lblstp, ',')}" />

<c:choose>
	<c:when test="${subq == true}">
		<label class="testLabelThin "><spring:message code="${lbl}" /></label>
		<div id="ERROR_${id}" class="adjust"></div>
		<div class="radio" style="float: left">
			<spring:bind path="${vari}">
				<div class="${classRadio}">
					<c:forEach var="rid" begin="0" end="${fn:length(arrayIDs)-1}">
						<div class="${classTableRadio}" style="${classStyle}">
							<form:radiobutton path="${status.expression}" class="radioLabel"
								id="optr${id}${arrayIDs[rid]}" value="${arrayIDs[rid]}" />
							
							<label for="optr${id}${arrayIDs[rid]}"><span
								class="radio-replacement-helper"></span>
								<c:choose>
									<c:when test="${rid == argID}">
										<spring:message
											code="${arrayIDslbl[rid]}" arguments="${argument}" />
									</c:when>
									<c:otherwise>
										<spring:message
											code="${arrayIDslbl[rid]}" />
									</c:otherwise>
								</c:choose>
							</label>
							<c:if
								test="${arrayLBLstp[rid] != null && arrayLBLstp[rid] != 'EMPTY' && arrayLBLstp[rid] != ''}">
								<span class="appQuestion"> <span
									class="appQuestionTooltip"> <span
										class="tootltipMessage"> <spring:message
												code="${arrayLBLstp[rid]}" />
									</span> <span class="tootltipClose"></span>
								</span>
								</span>
							</c:if>
						</div>
					</c:forEach>
				</div>
			</spring:bind>
		</div>

	</c:when>
	<c:otherwise>
		<div class="${classQuestion}">
			<c:if test="${fase ne 'REVIEW'}">
				<label class="fillLabel"><spring:message code="${lbl}" /></label>
				<c:if test="${lbltp != null}">
					<span class="appQuestion"> <span class="appQuestionTooltip">
							<span class="tootltipMessage"><spring:message
									code="${lbltp}" /></span> <c:if test="${img != null && img != ''}">
								<span class="tooltipImage"> <img src="${img}" />
								</span>
							</c:if><span class="tootltipClose"></span>
					</span></span>
				</c:if>
				<spring:bind path="${vari}">
					<div class="${classRadio}">
						<c:forEach var="rid" begin="0" end="${fn:length(arrayIDs)-1}">
							<div class="${classTableRadio}" style="${classStyle}">
								<form:radiobutton path="${status.expression}" class="radioLabel"
									id="optr${id}${arrayIDs[rid]}" value="${arrayIDs[rid]}" />
								<label for="optr${id}${arrayIDs[rid]}"><span
									class="radio-replacement-helper"></span>
									<c:choose>
										<c:when test="${rid == argID}">
											<spring:message
												code="${arrayIDslbl[rid]}" arguments="${argument}" />
										</c:when>
										<c:otherwise>
											<spring:message
												code="${arrayIDslbl[rid]}" />
										</c:otherwise>
									</c:choose>
								</label>
								<c:if
									test="${arrayLBLstp[rid] != null && arrayLBLstp[rid] != 'EMPTY' && arrayLBLstp[rid] != ''}">
									<span class="appQuestion"> <span
										class="appQuestionTooltip"> <span
											class="tootltipMessage"> <spring:message
													code="${arrayLBLstp[rid]}" />
										</span> <span class="tootltipClose"></span>
									</span>
									</span>
								</c:if>
							</div>
						</c:forEach>
					</div>
					<c:if test="${status.error}">
						<div class="appError" id="${id}alert">
							<span class="appErrorIcon"> <span class="appErrorTooltip">
									<span class="tootltipMessage"><form:errors
											path="${status.expression}" /></span>
							</span>
							</span>
						</div>
					</c:if>
					<c:if test="${alert == true}">
					<div class="appAdvert" id="${id}alert">
	                    <span class="appAdvertIcon">
	                        <span class="appAdvertTooltip">
	                            <span class="tootltipMessage"><spring:message code="${alert_radio}" /></span>
	                            <span id="${id}close" class="tootltipClose"></span>
	                        </span>
	                    </span>
	                </div>
	                </c:if>
				</spring:bind>
			</c:if>
			<c:if test="${fase eq 'REVIEW'}">
				<div class="revInfo">
					<div class="revItemLeft">
						<div class="revLabelFix">
							<label class="fillLabel"><spring:message code="${lbl}" /></label>
						</div>
						<div id="LABEL${id}" class="revAnwser">
							<span><spring:bind path="${vari}">
									<c:if test="${status.value ne null}">
										<spring:message code="${prefix}${status.value}"  arguments="${argument}" />
									</c:if>
								</spring:bind></span>
						</div>
					</div>
					<c:if test="${subgrp == null || subgrp == ''}">
						<div id="ID_BTED${id}" class="revItemButton">
							<a class="midBt" id="BTED${id}"><spring:message
									code="btn_edit" /></a>
						</div>
					</c:if>
				</div>

				<spring:bind path="${vari}">
					<div id="FIELD${id}" class="field-hidden">
						<div class="${classRadio}">
							<c:forEach var="rid" begin="0" end="${fn:length(arrayIDs)-1}">
								<div class="${classTableRadio}" style="${classStyle}">
									<form:radiobutton path="${status.expression}"
										class="radioLabel" id="optr${id}${arrayIDs[rid]}"
										value="${arrayIDs[rid]}" />
									<label for="optr${id}${arrayIDs[rid]}"><span
										class="radio-replacement-helper"></span> 
										
										<c:choose>
											<c:when test="${rid == argID}">
												<spring:message
													code="${arrayIDslbl[rid]}" arguments="${argument}" />
											</c:when>
											<c:otherwise>
												<spring:message
													code="${arrayIDslbl[rid]}" />
											</c:otherwise>
										</c:choose>
									</label>
									<c:if
										test="${arrayLBLstp[rid] != null && arrayLBLstp[rid] != 'EMPTY' && arrayLBLstp[rid] != ''}">
										<span class="appQuestion"> <span
											class="appQuestionTooltip"> <span
												class="tootltipMessage"> <spring:message
														code="${arrayLBLstp[rid]}" />
											</span> <span class="tootltipClose"></span>
										</span>
										</span>
									</c:if>
								</div>
							</c:forEach>
						</div>
					</div>
					<c:if test="${status.error}">
						<div class="appError">
							<span class="appErrorIcon"> <span class="appErrorTooltip">
									<span class="tootltipMessage"><form:errors
											path="${status.expression}" /></span>
							</span>
							</span>
						</div>
					</c:if>
					<c:if test="${alert == true}">
					<div class="appAdvert" id="${id}alert">
	                    <span class="appAdvertIcon">
	                        <span class="appAdvertTooltip">
	                            <span class="tootltipMessage"><spring:message code="${alert_radio}" /></span>
	                            <span id="${id}close" class="tootltipClose"></span>
	                        </span>
	                    </span>
	                </div>
	                </c:if>
				</spring:bind>
				<c:if test="${(divChild == '' || divChild == null) && (divChildren == '' || divChildren == null) }">
					<div id="ID_BTSV${id}" class="revSave">
						<div class="editBtSave">
							<a class="midBt" id="BTSV${id}"><spring:message
									code="btn_save" /></a>
						</div>
						<div class="editBtCancel">
							<a class="textBt" id="BTCN${id}"><spring:message
									code="btn_cancel" /></a>
						</div>
					</div>
				</c:if>
			</c:if>
		</div>
	</c:otherwise>
</c:choose>
<script>
	$(function() {
		var id = "<c:out value='${id}'/>";

		/*id edit*/

		if (("${fase}" == 'REVIEW')) {

			var add = $('<div id="ID_BTSV${id}" class="revSave">'
					+ '<div class="editBtSave">'
					+ '<a class="midBt" id="BTSV${id}"><spring:message code="btn_save" /></a>'
					+ '</div>'
					+ '<div class="editBtCancel">'
					+ '<a class="textBt" id="BTCN${id}"><spring:message code="btn_cancel" /></a>'
					+ '</div>' + '</div>');

			if(("${divChildren}" != '') && $('#${divChildren} .saveCancelBtn').length){
				$('#${divChildren} .saveCancelBtn').append(add);
				disableAllChildV2("${id}"+$("#${id}").val());
			}else{
				if("${divChild}" != ''){
					disableAllChildV2("${divChild}");
					if ($('#${divChild} .qSubsection').length) {
						$("#${divChild} .qSubsection").append(add);
					} else {
						$("#${divChild}").append(add);
					}
				}
			}
		}

		$("#FIELD${id}").hide();
		$("#ID_BTSV${id}").hide();

		$('#BTED${id}').click(function() {
			prev["${id}"] = $("input:radio[name='${vari}']:checked").val();
			$("#ID_BTSV${id}").show();
			$("#ID_BTED${id}").hide();
			$("#LABEL${id}").hide();
			$("#FIELD${id}").show();
			
			if("${divChildren}" != ''){
				savePreviousValues("${id}"+$("input:radio[name='${vari}']:checked").val());
				
				$("input:radio[name='${vari}']").each(function(){
					enableAllChildV2("${id}"+$(this).val());
				});
				//enableAllChildV2("${id}"+$("#${id}").val());
			}else{
				if ("${divChild}" != '') {
					$("#SUBTBL${id}").show();
	
					var url = "${contrID}";
					if (url.indexOf('?') >= 0) {
						url += "&";
					} else {
						url += "?";
					}
	
					$("#${tblIDChild}").load(url + "edit=1");
					savePreviousValues("${divChild}");
					enableAllChildV2("${divChild}");
				}
			}
		});

		$('#BTCN${id}').click(function() {
			$("#optr${id}" + prev["${id}"]).trigger("click");
			$("#ID_BTED${id}").removeAttr("style");
			$("#ID_BTSV${id}").hide();
			$("#LABEL${id}").show();
			$("#FIELD${id}").hide();
			$("#SUBTBL${id}").hide();
			if("${divChildren}" != ''){
				restorePreviousValues("${id}"+prev["${id}"]);
				disableAllChildV2("${id}"+prev["${id}"]);
			}else{
				if ("${divChild}" != '') {
					restorePreviousValues("${divChild}");
					disableAllChildV2("${divChild}");
	
					var url = "${contrID}";
					if (url.indexOf('?') >= 0) {
						url += "&";
					} else {
						url += "?";
					}
	
					$("#${tblIDChild}").load(url + "edit=0");
				}
			}
			delete prev["${id}"];
		});
		
		$('#BTSV${id}').click(
				function() {
					$("#ID_BTED${id}").removeAttr("style");
					$("#ID_BTSV${id}").hide();
					$("#FIELD${id}").hide();
					var current = $("input:radio[name='${vari}']:checked").val();
				//	alert(current);
				//	alert("hhhhhhhhhhh");
					if(current != undefined) {
						var env = "${prefix}" + current;
						
						$.ajax({
							url : "bundle",
							data : {
								"key" : env
							},
							type : "POST",
							success : function(data) {
								$("#LABEL${id}").empty().append(
										"<span>" + data + "</span>").show();
							//	alert(data);
							//	alert("hhaaahhhhhhhhh");
								if("${rela}" != ""){
									$("#LABEL${rela}").empty().append(
											"<span></span>").show();
								}
							}
						});
					} else {
						$("#LABEL${id}").empty().append(
								"<span></span>").show();
					}
					
					if("${divChildren}" != ''){
						saveNewValues("${id}"+$("input:radio[name='${vari}']:checked").val(), "${prefix}");
						disableAllChildV2("${id}"+$("input:radio[name='${vari}']:checked").val());
					}else{
						if ("${divChild}" != '') {
							saveNewValues("${divChild}", "${prefix}");
							$("#SUBTBL${id}").hide();
							disableAllChildV2("${divChild}");
							var url = "${contrID}";
							if (url.indexOf('?') >= 0) {
								url += "&";
							} else {
								url += "?";
							}
							$("#${tblIDChild}").load(url + "edit=0");
						}
					}
					delete prev["${id}"];
				});

		var crml_cmmt = $("input:radio[name='${vari}']:checked").val();
		if (crml_cmmt == "${open}") {
			$("#${divChild}").show();
			$("#${tblIDChild}").load("${contrID}");
		} else {
			$("#${divChild}").hide();
		}

		if ("${divChild}" != '' && "${basic}" != '' && "${basic}" == "TRUE") {
			$("input:radio[name='${vari}']").click(function() {
				if ($(this).attr("value") == "${open}") {
					$("#${divChild}").show();
					$("#${tblIDChild}").load("${contrID}");
				} else {
					cleanPreviousValues("${divChild}");
					$("#${divChild}").find(".appError").hide();
					$("#${divChild} input[type='text']").removeClass("inputError");
					$("#${divChild}").hide();
				}
			});
		}
		
		if("${alert}" == "true"){
			$("#${id}alert").hide();
			
			$("input:radio[name='${vari}']").click(function() {

				if ($(this).attr("value") == "${optAlert}") {
					$("#${id}alert").show();
				} else {
					$("#${id}alert").hide();
				}
			});
			
			$("#${id}close").click(function(){
				$("#${id}alert").hide();
			});
		}
		
		function cleanPreviousValues(name) {
			$('#' + name + ' input').each(function() {
				var type = $(this).attr("type");
				if (type == "radio") {
					if (this.checked) {
						$(this).attr('checked', false);
					}
				} else if (type == "checkbox") {
					if (this.checked) {
						$(this).attr('checked', false);
					}
				} else if (type != "button" || type != "submit") {
					if ($(this).attr('id') != undefined) {
						$(this).val('');
					}
				}
			});
		}
	});
</script>