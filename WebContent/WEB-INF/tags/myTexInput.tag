<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>

<%@ attribute name="id" required="true"%>
<%@ attribute name="fase" required="true"%>
<%@ attribute name="lbl" required="true"%>
<%@ attribute name="lbltp" required="false"%>
<%@ attribute name="vari" required="true"%>
<%@ attribute name="leng" required="true"%>
<%@ attribute name="subq" required="true"%>
<%@ attribute name="img" required="false"%>
<%@ attribute name="subgrp" required="false"%>
<%@ attribute name="placehold" required="false"%>
<%@ attribute name="errorClass" required="false"%>

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
				<form:input id="${id}" path="${status.expression}"
					class="testInput fSmall ${inputError}" maxlength="${leng}" type="text"
					placeholder="${placehold}" />
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
							<span class="tootltipMessage"> <spring:message
									code="${lbltp}" />
						</span> <c:if test="${img != null && img != ''}">
								<span class="tooltipImage"> <img src="${img}" />
								</span>
							</c:if> <span class="tootltipClose"></span>
					</span>
					</span>
				</c:if>
				<spring:bind path="${vari}">
					<c:if test="${status.error}">
						<c:set var="inputError" value="inputError" />
					</c:if>
					<form:input id="${id}" path="${status.expression}"
						class="fillInput ${inputError}" maxlength="${leng}" type="text"
						placeholder="${placehold}" />
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
							<span><spring:bind path="${vari}">
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
						<form:input id="${id}" path="${status.expression}"
							class="fillInput" maxlength="${leng}" type="text" />
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
				<div id="ERROR_${id}" class="${error}"></div>
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
