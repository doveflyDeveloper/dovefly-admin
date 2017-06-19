package com.deertt.module.sys.help.web;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.deertt.frame.base.web.DvBaseController;
import com.deertt.frame.base.web.page.DvPageVo;
import com.deertt.module.sys.help.service.IHelpService;
import com.deertt.module.sys.help.util.IHelpConstants;
import com.deertt.module.sys.help.vo.HelpVo;
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
@RequestMapping("/helpController")
public class HelpController extends DvBaseController implements IHelpConstants {
	
	@Autowired
	protected IHelpService service;
	
	/**
	 * 新增页面
	 * 
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/create", method = RequestMethod.GET)
	public String create(HttpServletRequest request) throws Exception {
		return JSP_PREFIX + "/insertHelp";
	}
	
	/**
	 * 修改页面
	 * 
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/find/{id}")
	public String find(HttpServletRequest request, @PathVariable(REQUEST_ID) Integer id) throws Exception {
		HelpVo bean = service.find(id);
		request.setAttribute(REQUEST_BEAN, bean);
		request.setAttribute(REQUEST_IS_MODIFY, 1);
		return JSP_PREFIX + "/insertHelp";
	}
	
	/**
	 * 编辑
	 * 
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/edit/{module}")
	public String edit(HttpServletRequest request, @PathVariable("module") String module) throws Exception {
		HelpVo bean = service.findByModule(module);
		request.setAttribute(REQUEST_BEAN, bean);
		request.setAttribute(REQUEST_IS_MODIFY, 1);
		return JSP_PREFIX + "/insertHelp";
	}
	
	/**
	 * 新增保存
	 * 
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/insert")
	public String insert(HttpServletRequest request, HelpVo vo, RedirectAttributes attr) throws Exception {
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
	@RequestMapping("/update")
	public String update(HttpServletRequest request, HelpVo vo, RedirectAttributes attr) throws Exception {
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
	@RequestMapping("/detail/{id}")
	public String detail(HttpServletRequest request, @PathVariable(REQUEST_ID) Integer id) throws Exception {
		HelpVo bean = service.find(id);
		request.setAttribute(REQUEST_BEAN, bean);
		return JSP_PREFIX + "/detailHelp";
	}

	/**
	 * 分页查询
	 * 
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/query")
	public String query(HttpServletRequest request) throws Exception {
		String queryCondition = getQueryCondition(request);
		DvPageVo pageVo = super.transctPageVo(request, service.getRecordCount(queryCondition));
		String orderStr = "";//String orderStr = "create_at desc";
		List<HelpVo> beans = service.queryByCondition(queryCondition, orderStr, pageVo.getStartIndex(), pageVo.getPageSize());
		request.setAttribute(REQUEST_BEANS, beans);
		return JSP_PREFIX + "/listHelp";
	}
	
	/**
	 * 查询全部，清除所有查询条件
	 * 
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/queryAll")
	public String queryAll(HttpServletRequest request) throws Exception {	
		return query(request);
	}
	
	/**
	 * 获得帮助
	 * 
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/getHelp/{module}")
	public String getHelp(HttpServletRequest request, @PathVariable("module") String module) throws Exception {
		HelpVo bean = service.findByModule(module);
		request.setAttribute(REQUEST_BEAN, bean);
		request.setAttribute("module", module);
		return JSP_PREFIX + "/getHelp";
	}
	
	/**
	 * 功能: 从request中获得查询条件
	 *
	 * @param request
	 * @return
	 */
	protected String getQueryCondition(HttpServletRequest request) {
		String queryCondition = null;
		if(request.getAttribute(REQUEST_QUERY_CONDITION) != null) {
			queryCondition = request.getAttribute(REQUEST_QUERY_CONDITION).toString();
		} else {
			List<String> lQuery = new ArrayList<String>();
			lQuery.add(DvSqlHelper.buildQueryStr("module", request.getParameter("module"), DvSqlHelper.TYPE_CHAR_LIKE));
			lQuery.add(DvSqlHelper.buildQueryStr("operation", request.getParameter("operation"), DvSqlHelper.TYPE_CHAR_LIKE));
			lQuery.add(DvSqlHelper.buildQueryStr("description", request.getParameter("description"), DvSqlHelper.TYPE_CHAR_LIKE));
			lQuery.add(DvSqlHelper.buildQueryStr("status", request.getParameter("status"), DvSqlHelper.TYPE_CUSTOM, "='", "'"));
			queryCondition = DvSqlHelper.appendQueryStr(lQuery.toArray(new String[0]));
		}
		return queryCondition;
	}
	
}
