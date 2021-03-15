<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>

<%@ attribute name="id" required="true"%>
<%@ attribute name="idto" required="true"%>
<%@ attribute name="fase" required="true"%>
<%@ attribute name="lbl" required="true"%>
<%@ attribute name="lbltp" required="false"%>
<%@ attribute name="vari" required="true"%>
<%@ attribute name="qstVari" required="true"%>
<%@ attribute name="myClass" required="true"%>
<%@ attribute name="subgrp" required="false"%>
<%@ attribute name="subq" required="false"%>
<%@ attribute name="img" required="false"%>
<%@ attribute name="placehold" required="false"%>
<%@ attribute name="errorClass" required="false"%>
<%@ attribute name="allowToday" required="false"%>
<%@ attribute name="allowFuture" required="false"%>

<spring:message code="jquery.date" var="dt_pattern" />
<c:set var="error" value="not_adjust" />
<c:if test="${errorClass == 'true'}">
	<c:set var="error" value="adjust" />
</c:if>

<c:choose>
	<c:when test="${subq == true && fase ne 'REVIEW'}">
		<div class="floatInputTravel travelAdjust">
			<label class="testLabelThin "><spring:message code="${lbl}" /></label>
			<spring:bind path="${vari}">
				<c:if test="${status.error}">
					<c:set var="inputError" value="inputError" />
				</c:if>
				<form:input
					class="datepicker${id} testInput ${myClass} ${inputError}"
					id="${id}" path="${status.expression}" type='text'
					placeholder="${placehold}" />
				<a class="dateBt" id="dateBt${id}"></a>
				<c:if test="${status.error}">
					<div class="appError">
						<span class="appErrorIcon"> <span class="appErrorTooltip">
								<span class="tootltipMessage"><form:errors
										path="${status.expression}" /></span>
						</span>
						</span>
					</div>
				</c:if>
			</spring:bind>
		</div>
		<div id="ERROR_${id}" class="${error}"></div>
	</c:when>
	<c:otherwise>
		<c:set var="classQuestion" value="revQuestion" />
		<c:if test="${fase ne 'REVIEW' || subgrp == true}">
			<c:set var="classQuestion" value="question" />
		</c:if>

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
					<c:if test="${status.error}">
						<c:set var="inputError" value="inputError" />
					</c:if>
					<form:input
						class="datepicker${id} fillInput ${inputError} ${myClass}"
						id="${id}" path="${status.expression}" type='text'
						placeholder="${placehold}" />
						<a class="dateBt" id="dateBt${id}"></a>
					<c:if test="${status.error}">
						<div class="appError">
							<span class="appErrorIcon"> <span class="appErrorTooltip">
									<span class="tootltipMessage"><form:errors
											path="${status.expression}" /></span>
							</span>
							</span>
						</div>
					</c:if>
				</spring:bind>
				<div id="ERROR_${id}" class="${error}"></div>
			</c:if>
			<c:if test="${fase eq 'REVIEW'}">
				<div class="revInfo">
					<div class="revItemLeft">
						<div class="revLabelFix">
							<label class="fillLabel"><spring:message code="${lbl}" /></label>
						</div>
						<div id="LABEL${id}" class="revAnwser">
							<span><spring:bind path="${qstVari}">
						${status.value}
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
					<c:if test="${status.error}">
						<c:set var="inputError" value="inputError" />
					</c:if>
					<div id="FIELD${id}" class="field-hidden">
						<form:input
							class="datepicker${id} ${inputError} fillInput ${myClass}"
							id="${id}" path="${status.expression}" type='text' />
						<a class="dateBt" id="dateBt${id}"></a>
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
				</spring:bind>
				<div id="ERROR_${id}"></div>
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
		</div>
	</c:otherwise>
</c:choose>

<script>
	var dt_pattern = "<c:out value='${dt_pattern}'/>";
	
	$('#${id}').inputmask("99/99/9999");
	/*$('#${id}').inputmask('Regex', { 
	    regex: "((^[0]\\d)|(^[1][0-2]))/(([0-2]\\d)|([3][0-1]))/\\d\\d\\d\\d$"
	});*/
	var options;
	if("${allowFuture}" == "true"){
		options = {
				showAnim : "slide",
				showOn : "#dateBt${id}",
				changeMonth : true,
				changeYear : true,
				yearRange : '-120:c+30',
				dateFormat : dt_pattern,
				onSelect : function() {
					if ("${idto}" != '') {
						fixDate('#${id}', '#${idto}');
					}
					executeOtherFunc('${id}');
				},
		};
	} else {
		if ("${allowToday}" == "false") {
			var maxDate = '-1D';
		}else{
			var maxDate = 0;
		}
		options = {
			showAnim : "slide",
			showOn : "#dateBt${id}",
			changeMonth : true,
			changeYear : true,
			yearRange : '-120:+0',
			maxDate : maxDate,
			dateFormat : dt_pattern,
			onSelect : function() {
				if ("${idto}" != '') {
					fixDate('#${id}', '#${idto}');
				}
				executeOtherFunc('${id}');
			},	
		};
	}
	

	$(".datepicker${id}").datepicker(options);

	$("#dateBt${id}").click(function() {
		$(".datepicker${id}").datepicker("show");
	});

	$('.datepicker${id}').on("change", function() {
		fixDate('#${id}', '#${idto}');
	});

	/*id edit*/
	$("#ID_BTSV${id}").hide();
	$("#FIELD${id}").hide();

	$("#BTED${id}").click(function() {
		prev["${id}"] = $("#${id}").val();
		$("#ID_BTSV${id}").show();
		$("#ID_BTED${id}").hide();
		$("#FIELD${id}").show();
		$("#LABEL${id}").hide();
	});

	$('#BTCN${id}').click(function() {
		$("#${id}").val(prev["${id}"]);
		$("#ID_BTED${id}").removeAttr("style");
		$("#ID_BTSV${id}").hide();
		$("#FIELD${id}").hide();
		$("#LABEL${id}").show();
		delete prev["${id}"];
	});

	$('#BTSV${id}').click(function() {
		$("#ID_BTED${id}").removeAttr("style");
		$("#ID_BTSV${id}").hide();
		$("#FIELD${id}").hide();
		$("#LABEL${id}").empty().html($("#${id}").val()).show();
		delete prev["${id}"];
	});
</script>