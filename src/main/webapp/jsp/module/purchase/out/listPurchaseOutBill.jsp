<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<%@include file="/jsp/include/dvGlobal.jsp" %>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
<title>出库单管理</title>
<style type="text/css">
<!--
-->
</style>
<script type="text/javascript">
	var formHelper = new DvFormHelper("purchaseOutBillController");
	
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
		if(!validateSelections(sels, "status", "0")){
			dvAlert("只能删除未确认出库的订单！");
	  		return false;
		}
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
		if(!validateSelections(sels, "status", "0")){
			dvAlert("只能删除未确认出库的订单！");
	  		return false;
		}
	
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
		if(!validateSelections(sels, "status", "0")){
			dvAlert("只能修改未确认的订单！");
	  		return false;
		}
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
	
	/** 导出列表 */
	function exportList_onclick() {
		var ids = findSelections("dv_checkbox", "value");
		if(!ids || ids.length == 0) ids = "all";
		formHelper.submit(formHelper.buildAction("purchaseOutBillController", "exportList", ids));
	}
	
	/** 导出明细列表 */
	function exportListDetail_onclick() {
		var ids = findSelections("dv_checkbox", "value");
		if(!ids || ids.length == 0) ids = "all";
		formHelper.submit(formHelper.buildAction("purchaseOutBillController", "exportListDetail", ids));
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
					<div class="table_title_tip" rel="<%=request.getContextPath()%>/helpController/getHelp/purchase_out_bill"></div>
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
						<td class="label">货仓：</td>
						<td class="field">
							<dv:select name="warehouse_id" dicKeyword="DIC_SYS_WAREHOUSE" defaultValue="${param.warehouse_id }" ignoreValues="1" hasEmpty="true"/>
						</td>
						</c:if>
						<td class="label">订单编号：</td>
						<td class="field">
							<input type="text" name="bill_code" class="input" maxlength="50" value="<c:out value="${param.bill_code }"/>"/>
						</td>
						<td class="label">订单日期：</td>
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
					<div class="table_title">出库单列表&nbsp;</div>
				</div>
				<div class="right_div">
					<div class="right_menu">
						<c:if test="${sessionScope.DV_USER_VO.cityManagerRole}">
						<input type="button" name="insert" value="新增" class="button" onclick="add_onclick();"/>
						<input type="button" name="update" value="修改" class="button" onclick="find_onclick();"/>
						<input type="button" name="delete" value="删除" class="button" onclick="deleteMulti_onclick();"/>
						</c:if>
						<input type="button" name="exportList" value="导出订单" class="button" onclick="exportList_onclick();"/>
						<input type="button" name="exportListDetail" value="导出明细" class="button" onclick="exportListDetail_onclick();"/>
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
						<th width="10%">货仓</th>
						<th width="12%">出库单号</th>
						<th width="10%">订单时间</th>
						<th width="10%">订单金额</th>
						<th width="10%">出库状态</th>
					</tr>
					<c:forEach var="bean" varStatus="status" items="${beans }">
					<tr>
						<td>
							<input type="checkbox" name="dv_checkbox" value="<c:out value="${bean.id }"/>" status="${bean.status }" onclick="selectCheckBox(this)"/>
						</td>
						<td>${status.count }</td>
						<td><c:out value="${bean.city_name }"/></td>
						<td><c:out value="${bean.warehouse_name }"/></td>
						<td><a href="javascript:detail_onclick('<c:out value="${bean.id }"/>')"><c:out value="${bean.bill_code }"/></a></td>
						<td>${fn:substring(bean.bill_time, 0, 19) }</td>
						<td><c:out value="${bean.amount }"/></td>
						<td><dv:display dicKeyword="DIC_PURCHASE_OUT_BILL_STATUS" value="${bean.status }"/></td>
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
