<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<%@include file="/jsp/include/dvGlobal.jsp" %>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
<title>结算清单管理</title>
<style type="text/css">
<!--
-->
</style>
<script type="text/javascript">
	var formHelper = new DvFormHelper("statisticsBillController");
	
	/** 查询 */
	function query_onclick() {
		formHelper.jSubmit(formHelper.buildQueryAction());
	}
	
	/** 新增 */
	function add_onclick() {
		formHelper.jSubmit(formHelper.buildCreateAction());
	}
	
	/** 复制 */	
	function copy_onclick() {
		var sels = findSelections("dv_checkbox");
		if(!sels || !sels.length) {
	  		dvAlert("请选择一条记录！");
	  		return false;
		}
		if(sels.length > 1) {
			dvAlert("只能选择一条记录！");
	  		return false;
		}
		
		//其他一些限制操作的校验：如审批状态等。
		//if(!validateSelections(sels, "issue_state", "0")){
		//	dvAlert("只有发布状态为1的单据才可以删除！");
	  	//	return false;
		//}
		var id = $(sels[0]).val();
		formHelper.jSubmit(formHelper.buildCopyAction(id));
	}
	
	/** 删除 */
	function deleteMulti_onclick() {
		var sels = findSelections("dv_checkbox");
		if(!sels || !sels.length) {
	  		dvAlert("请先选择记录！");
	  		return false;
		}
		//其他一些限制操作的校验：如审批状态等。
		//if(!validateSelections(sels, "issue_state", "1")){
		//	dvAlert("只有发布状态为1的单据才可以删除！");
	  	//	return false;
		//}
	
		var ids = findSelections("dv_checkbox", "value");
		
		dvConfirm("您确定要删除这些数据吗？", 
			function() {
				formHelper.jSubmit(formHelper.buildDeleteMultiAction(ids));
			}, 
			function() {
				//alert("干嘛要取消啊？");
			}
		);
	}
	
	/** 修改 */
	function find_onclick() {
		var sels = findSelections("dv_checkbox");
		if(!sels || !sels.length) {
	  		dvAlert("请选择一条记录！");
	  		return false;
		}
		if(sels.length > 1) {
			dvAlert("只能选择一条记录！");
	  		return false;
		}
		
		//其他一些限制操作的校验：如审批状态等。
		//if(!validateSelections(sels, "issue_state", "0")){
		//	dvAlert("只有发布状态为1的单据才可以删除！");
	  	//	return false;
		//}
		var id = $(sels[0]).val();
		formHelper.jSubmit(formHelper.buildFindAction(id));
	}
	
	/** 查看 */
	function detail_onclick(id){
		if(id) {//点击单据名称超链接查看
			formHelper.jSubmit(formHelper.buildDetailAction(id));
		} else {//勾选复选框查看
			var ids = findSelections("dv_checkbox", "value");
			if(!ids || !ids.length) {
				dvAlert("请选择一条记录！");
		  		return false;
			}
			if(ids.length > 1) {
				dvAlert("只能选择一条记录！");
		  		return false;
			}
			formHelper.jSubmit(formHelper.buildDetailAction(ids));
		}
	}
	
	/** 导入 */
	function importExcel_onclick() {
		var redirectUrl = "statisticsBillController/importExcel";
		var downloadUrl = "statisticsBillController/downloadTemplate";
		var title = "盘点库存导入";
		dvOpenImportDialog(redirectUrl, downloadUrl, title);
	}
	
	/** 导出 */
	function exportDetail_onclick() {
		var sels = findSelections("dv_checkbox");
		if(!sels || !sels.length) {
	  		dvAlert("请选择一条记录！");
	  		return false;
		}
		if(sels.length > 1) {
			dvAlert("只能选择一条记录！");
	  		return false;
		}
		var id = $(sels[0]).val();
		formHelper.submit(formHelper.buildAction("statisticsBillController", "exportDetail", id));
	}
	
	/** 批量导出 */
	function exportList_onclick() {
		var ids = findSelections("dv_checkbox", "value");
		if(!ids || ids.length == 0) ids = "all";	
		formHelper.submit(formHelper.buildAction("statisticsBillController", "exportList", ids));
	}
	
	/** 页面加载后需绑定的事件 */
	$(document).ready(function() {
	
	});
</script>
</head>
<body>
	<form id="form" name="form" method="get">
		<div style="height: 4px"></div>
		<div class="border_div" >
			<div class="header_div">
				<div class="left_div">
					<div class="table_title">查询条件&nbsp;</div>
					<div class="table_title_tip" rel="<%=request.getContextPath()%>/helpController/getHelp/statistics_bill"></div>
				</div>
				<div class="right_div">
					<div class="right_menu">
						<label style="color: #CCC"><input type="checkbox" id="query_state" name="query_state" class="checkbox" value="1" <c:if test='${param.query_state == 1 }'>checked="checked"</c:if>/>更多</label>&nbsp;&nbsp;
						<input type="button" name="query" value="查询" class="button" onclick="query_onclick();"/>
					</div>
				</div>
			</div>
			<div class="padding_2_div">
				<table class="query_table">
					<tr>
						<c:if test="${sessionScope.DV_USER_VO.headquartersRole}">
						<td class="label">城市：</td>
						<td class="field">
							<dv:select name="city_id" dicKeyword="DIC_SYS_CITY" defaultValue="${param.city_id }" ignoreValues="1" hasEmpty="true"/>
						</td>
						</c:if>
						<td class="label">结算日期：</td>
						<td class="field">
							<input type="text" name="bill_date_from" class="half_input Wdate" value="${fn:substring(param.bill_date_from, 0, 10) }"/>&nbsp;&nbsp;~&nbsp;
							<input type="text" name="bill_date_to" class="half_input Wdate" value="${fn:substring(param.bill_date_to, 0, 10) }"/>
						</td>
					</tr>
				</table>
			</div>
		</div>
		<div class="space_h15_div"></div>
		<div class="border_div" >
			<div class="header_div">
				<div class="left_div">
					<div class="table_title">结算清单列表&nbsp;</div>
				</div>
				<div class="right_div">
					<div class="right_menu">
					</div>
				</div>
			</div>
			<div class="padding_2_div">
				<table class="list_table">
					<tr>
						<th width="5%">
							<input type="checkbox" name="dv_checkbox_all" value="" onclick="selectAll(this)"/>	
						</th>
						<th width="5%">序号</th>
						<th width="10%">城市</th>
						<th width="10%">结算单号</th>
						<th width="10%">结算日期</th>
						<th width="10%">期初金额</th>
						<th width="10%">采购金额</th>
						<th width="10%">售出金额</th>
						<th width="10%">库存金额</th>
						<th width="10%">盘点金额</th>
						<th width="10%">结转金额</th>
					</tr>
					<c:forEach var="bean" varStatus="status" items="${beans }">
					<tr>
						<td>
							<input type="checkbox" name="dv_checkbox" value="<c:out value="${bean.id }"/>" onclick="selectCheckBox(this)"/>
						</td>
						<td>${status.count }</td>
						<td><c:out value="${bean.city_name }"/></td>
						<td><a href="javascript:detail_onclick('<c:out value="${bean.id }"/>')"><c:out value="${bean.bill_code }"/></a></td>
						<td>${fn:substring(bean.bill_date, 0, 10) }</td>
						<td><c:out value="${bean.origin_amount }"/></td>
						<td><c:out value="${bean.purchase_amount }"/></td>
						<td><c:out value="${bean.order_amount }"/></td>
						<td><c:out value="${bean.stock_amount }"/></td>
						<td><c:out value="${bean.check_amount }"/></td>
						<td><c:out value="${bean.final_amount }"/></td>
					</tr>
					</c:forEach>
				</table>
			</div>
		</div>
		<div class="page_div">
			<jsp:include page="/jsp/include/page.jsp"/>
		</div>
	</form>
</body>
</html>
