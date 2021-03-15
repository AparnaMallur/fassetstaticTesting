 </div>
 <script>
 
 	function exportexcel(report){ 
 		console.log("exportexcel method");
 				$("summary").css({"display":"none"}); 
		 	$('#excel_report').attr('id','Export_table');
		 	
			var table = document.getElementById('Export_table');
			
			$('#excel_report').attr('border', '1px solid black');
			$('#Export_table ').css('padding', '4');			
			$("#excel_report th").css({"border":"1 px solid black","color":"black"}); 
			$("#excel_report td").css({"border":"1 px solid black","color":"black"}); 
			
			var filename=report+".xls";
			var heading=report;
			download(table.outerHTML, filename, "application/vnd.ms-excel");
			}
	
</script>
<footer class="footer"> <p><spring:message code="allRights" />  </p> <spring:message code="buildVersion"/></footer>
<script type="text/javascript" src="${bootstrapmin}"></script>
<script type="text/javascript" src="${modernizrmin}"></script>
<script type="text/javascript" src="${detect}"></script>
<script type="text/javascript" src="${fastclick}"></script>
<script type="text/javascript" src="${slimscroll}"></script>
<script type="text/javascript" src="${blockUI}"></script>
<script type="text/javascript" src="${waves}"></script>
<script type="text/javascript" src="${wowmin}"></script>
<script type="text/javascript" src="${nicescroll}"></script>
<script type="text/javascript" src="${scrollTo}"></script>
<script type="text/javascript" src="${app}"></script>
<script type="text/javascript" src="${calenderjs}"></script>
<script type="text/javascript" src="${bootstraptable}"></script>
<script type="text/javascript" src="${bootstrapfilter}"></script>
</div>
</body>
</html>