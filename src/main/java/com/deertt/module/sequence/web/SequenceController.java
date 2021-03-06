package com.deertt.module.sequence.web;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.deertt.frame.base.web.DvBaseController;
import com.deertt.frame.base.web.page.DvPageVo;
import com.deertt.module.sequence.service.ICommonSeqService;
import com.deertt.module.sequence.service.ISequenceService;
import com.deertt.module.sequence.util.ISequenceConstants;
import com.deertt.module.sequence.vo.SequenceVo;
import com.deertt.utils.helper.DvSqlHelper;
import com.deertt.utils.helper.DvVoHelper;
import com.deertt.utils.helper.string.DvStringHelper;

/**
 * 功能、用途、现存BUG:
 * 
 * @author fengcm
 * @version 1.0.0
 * @see 需要参见的其它类
 * @since 1.0.0
 */
@Controller
@RequestMapping("/sequenceController")
public class SequenceController extends DvBaseController implements ISequenceConstants {
	
	@Autowired
	protected ISequenceService service;
	
	@Autowired
	protected ICommonSeqService commonService;
	
	/**
	 * 新增页面
	 * 
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/test", method = RequestMethod.GET)
	public void test(HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		ExecutorService cachedThreadPool = Executors.newCachedThreadPool();
		for (int i = 0; i < 100; i++) {
			cachedThreadPool.execute(new Runnable() {
				public void run() {
					for (int i = 0; i < 1000; i++) {
						String seq = commonService.queryNextSequenceId("seq0");
						//System.out.println(seq);
					}
				}
			});
		}
		response.getWriter().print("开始测试");
		response.flushBuffer();
	}
	
	/**
	 * 修改页面
	 * 
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequiresPermissions(PERM_WRITE)
	@RequestMapping("/find/{id}")
	public String find(HttpServletRequest request, @PathVariable(REQUEST_ID) Integer id) throws Exception {
		SequenceVo bean = service.find(id);
		request.setAttribute(REQUEST_BEAN, bean);
		request.setAttribute(REQUEST_IS_MODIFY, 1);
		return JSP_PREFIX + "/insertPurchaseSequence";
	}
	
	/**
	 * 新增保存
	 * 
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequiresPermissions(PERM_WRITE)
	@RequestMapping("/insert")
	public String insert(HttpServletRequest request, SequenceVo vo, RedirectAttributes attr) throws Exception {
		DvVoHelper.markCreateStamp(request, vo);
		service.insert(vo);
		return redirectWithTip(DEFAULT_REDIRECT, attr);
	}

	/**
	 * 修改保存
	 * 
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequiresPermissions(PERM_WRITE)
	@RequestMapping("/update")
	public String update(HttpServletRequest request, SequenceVo vo, RedirectAttributes attr) throws Exception {
		DvVoHelper.markModifyStamp(request, vo);
		service.update(vo);
		return redirectWithTip(DEFAULT_REDIRECT, attr);
	}
	
	/**
	 * 删除单条记录
	 * 
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequiresPermissions(PERM_WRITE)
	@RequestMapping("/delete/{id}")
	public String delete(HttpServletRequest request, @PathVariable(REQUEST_ID) Integer id, RedirectAttributes attr) throws Exception {
		service.delete(id);
		return redirectWithTip(DEFAULT_REDIRECT, attr);
	}

	/**
	 * 删除多条记录
	 * 
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequiresPermissions(PERM_WRITE)
	@RequestMapping("/deleteMulti/{ids}")
	public String deleteMulti(HttpServletRequest request, @PathVariable(REQUEST_IDS) String ids, RedirectAttributes attr) throws Exception {
		service.delete(DvStringHelper.parseStringToIntegerArray(ids, ","));
		return redirectWithTip(DEFAULT_REDIRECT, attr);
	}
	
	/**
	 * 查看
	 * 
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequiresPermissions(PERM_READ)
	@RequestMapping("/detail/{id}")
	public String detail(HttpServletRequest request, @PathVariable(REQUEST_ID) Integer id) throws Exception {
		SequenceVo bean = service.find(id);
		request.setAttribute(REQUEST_BEAN, bean);
		return JSP_PREFIX + "/detailPurchaseSequence";
	}

	/**
	 * 分页查询
	 * 
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequiresPermissions(PERM_READ)
	@RequestMapping("/query")
	public String query(HttpServletRequest request) throws Exception {
		String queryCondition = getQueryCondition(request);
		DvPageVo pageVo = super.transctPageVo(request, service.getRecordCount(queryCondition));
		String orderStr = "";//String orderStr = "create_at desc";
		List<SequenceVo> beans = service.queryByCondition(queryCondition, orderStr, pageVo.getStartIndex(), pageVo.getPageSize());
		request.setAttribute(REQUEST_BEANS, beans);
		return JSP_PREFIX + "/listPurchaseSequence";
	}
	
	/**
	 * 查询全部，清除所有查询条件
	 * 
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequiresPermissions(PERM_READ)
	@RequestMapping("/queryAll")
	public String queryAll(HttpServletRequest request) throws Exception {	
		return query(request);
	}
	
	/**
	 * 功能: 从request中获得查询条件
	 *
	 * @param request
	 * @return
	 */
	protected String getQueryCondition(HttpServletRequest request) {
		String queryCondition = null;
		if (request.getAttribute(REQUEST_QUERY_CONDITION) != null) {
			queryCondition = request.getAttribute(REQUEST_QUERY_CONDITION).toString();
		} else {
			List<String> lQuery = new ArrayList<String>();
			lQuery.add(DvSqlHelper.buildQueryStr("exp_company", request.getParameter("exp_company"), DvSqlHelper.TYPE_CHAR_LIKE));
			lQuery.add(DvSqlHelper.buildQueryStr("exp_tracking_no", request.getParameter("exp_tracking_no"), DvSqlHelper.TYPE_CHAR_LIKE));
			lQuery.add(DvSqlHelper.buildQueryStr("exp_date", request.getParameter("exp_date_from"), DvSqlHelper.TYPE_DATE_GREATER_EQUAL));
			lQuery.add(DvSqlHelper.buildQueryStr("exp_date", request.getParameter("exp_date_to"), DvSqlHelper.TYPE_DATE_LESS_EQUAL));
			lQuery.add(DvSqlHelper.buildQueryStr("sender_name", request.getParameter("sender_name"), DvSqlHelper.TYPE_CHAR_LIKE));
			lQuery.add(DvSqlHelper.buildQueryStr("sender_mobile", request.getParameter("sender_mobile"), DvSqlHelper.TYPE_CHAR_LIKE));
			lQuery.add(DvSqlHelper.buildQueryStr("sender_address", request.getParameter("sender_address"), DvSqlHelper.TYPE_CHAR_LIKE));
			lQuery.add(DvSqlHelper.buildQueryStr("receiver_name", request.getParameter("receiver_name"), DvSqlHelper.TYPE_CHAR_LIKE));
			lQuery.add(DvSqlHelper.buildQueryStr("receiver_mobile", request.getParameter("receiver_mobile"), DvSqlHelper.TYPE_CHAR_LIKE));
			lQuery.add(DvSqlHelper.buildQueryStr("receiver_address", request.getParameter("receiver_address"), DvSqlHelper.TYPE_CHAR_LIKE));
			lQuery.add(DvSqlHelper.buildQueryStr("status", request.getParameter("status"), DvSqlHelper.TYPE_CUSTOM, "='", "'"));
			queryCondition = DvSqlHelper.appendQueryStr(lQuery.toArray(new String[0]));
		}
		return queryCondition;
	}
	
}
