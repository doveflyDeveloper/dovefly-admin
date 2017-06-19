package com.deertt.module.sys.role.web;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.deertt.frame.base.web.DvBaseController;
import com.deertt.frame.base.web.page.DvPageVo;
import com.deertt.frame.base.web.vo.HandleResult;
import com.deertt.module.sys.resource.service.IResourceService;
import com.deertt.module.sys.resource.vo.ResourceVo;
import com.deertt.module.sys.role.service.IRoleService;
import com.deertt.module.sys.role.util.IRoleConstants;
import com.deertt.module.sys.role.vo.RoleVo;
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
@RequestMapping("/roleController")
public class RoleController extends DvBaseController implements IRoleConstants {
	
	@Autowired
	protected IRoleService service;
	
	@Autowired
	protected IResourceService resourceService;
	
	/**
	 * 新增页面
	 * 
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequiresPermissions(PERM_WRITE)
	@RequestMapping(value = "/create", method = RequestMethod.GET)
	public String create(HttpServletRequest request) throws Exception {
		return JSP_PREFIX + "/insertRole";
	}
	
	/**
	 * 复制新增页面（根据已有单据信息复制创建新单据）
	 * 
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequiresPermissions(PERM_WRITE)
	@RequestMapping(value = "/copy/{id}", method = RequestMethod.GET)
	public String copy(HttpServletRequest request, @PathVariable Integer id) throws Exception {
		RoleVo bean = service.find(id);
		bean.setId(null);
		request.setAttribute(REQUEST_BEAN, bean);
		return JSP_PREFIX + "/insertRole";
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
		RoleVo bean = service.find(id);
		request.setAttribute(REQUEST_BEAN, bean);
		request.setAttribute(REQUEST_IS_MODIFY, 1);
		return JSP_PREFIX + "/insertRole";
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
	public String insert(HttpServletRequest request, RoleVo vo, RedirectAttributes attrs) throws Exception {
		DvVoHelper.markCreateStamp(request, vo);
		service.insert(vo);
		return redirectWithTip(DEFAULT_REDIRECT, attrs);
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
	public String update(HttpServletRequest request, RoleVo vo, RedirectAttributes attrs) throws Exception {
		DvVoHelper.markModifyStamp(request, vo);
		service.update(vo);
		return redirectWithTip(DEFAULT_REDIRECT, attrs);
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
	public String delete(HttpServletRequest request, @PathVariable(REQUEST_ID) Integer id, RedirectAttributes attrs) throws Exception {
		service.delete(id);
		return redirectWithTip(DEFAULT_REDIRECT, attrs);
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
	public String deleteMulti(HttpServletRequest request, @PathVariable(REQUEST_IDS) String ids, RedirectAttributes attrs) throws Exception {
		service.delete(DvStringHelper.parseStringToIntegerArray(ids, ","));
		return redirectWithTip(DEFAULT_REDIRECT, attrs);
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
		RoleVo bean = service.find(id);
		request.setAttribute(REQUEST_BEAN, bean);
		return JSP_PREFIX + "/detailRole";
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
		List<RoleVo> beans = service.queryByCondition(queryCondition, orderStr, pageVo.getStartIndex(), pageVo.getPageSize());
		request.setAttribute(REQUEST_BEANS, beans);
		return JSP_PREFIX + "/listRole";
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
		request.setAttribute(REQUEST_QUERY_CONDITION, "");
		return query(request);
	}

	/**
	 * 参照信息查询
	 * 
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/reference")
	public String reference( HttpServletRequest request) throws Exception {
		query(request);
		return JSP_PREFIX + "/referenceRole";
	}
	
	/**
	 * 授权页面
	 * 
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequiresPermissions(PERM_WRITE)
	@RequestMapping("/findForAuthRes/{id}")
	public String findForAuthRes(HttpServletRequest request, @PathVariable(REQUEST_ID) Integer id) throws Exception {
		RoleVo bean = service.find(id);
		request.setAttribute(REQUEST_BEAN, bean);
		String queryCondition = null;
		String orderStr = "level asc, sort asc";
		List<ResourceVo> resourceBeans = resourceService.queryByCondition(queryCondition, orderStr, -1, -1);
		if(resourceBeans != null){
			for (ResourceVo resVo : resourceBeans) {
				if(bean.getResources() != null){
					for (ResourceVo selectedVo : bean.getResources()) {
						if(selectedVo.getId().equals(resVo.getId())){
							resVo.setChecked(true);
							break;
						}
					}
				}
			}
		}
		request.setAttribute("resourceBeans", resourceBeans);
		request.setAttribute(REQUEST_IS_MODIFY, 1);
		return JSP_PREFIX + "/findForAuthRes";
	}
	
	/**
	 * 给角色授权
	 * 
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequiresPermissions(PERM_WRITE)
	@RequestMapping("/authRes/{id}")
	public String authRes(HttpServletRequest request, @PathVariable(REQUEST_ID) Integer id, RedirectAttributes attrs) throws Exception {
		String res_ids_str = request.getParameter("res_ids");
		service.executeAuthorize(id, DvStringHelper.parseStringToIntegerArray(res_ids_str, ","));
		return redirectWithTip(DEFAULT_REDIRECT, attrs);
	}
	
	/**
	 * 异步请求，返回JSON
	 * 
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value="/ajaxQuery", produces={"application/json;charset=UTF-8"})
	public Object ajaxQuery(HttpServletRequest request) throws Exception {
		HandleResult result = new HandleResult();
		result.setSuccess(true);
		result.setMessage("Ajax请求数据成功！");
		return result;
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
			lQuery.add(DvSqlHelper.buildQueryStr("id", request.getParameter("id"), DvSqlHelper.TYPE_NUMBER_EQUAL));
			lQuery.add(DvSqlHelper.buildQueryStr("name", request.getParameter("name"), DvSqlHelper.TYPE_CHAR_LIKE));
			lQuery.add(DvSqlHelper.buildQueryStr("remark", request.getParameter("remark"), DvSqlHelper.TYPE_CHAR_LIKE));
			lQuery.add(DvSqlHelper.buildQueryStr("status", request.getParameter("status"), DvSqlHelper.TYPE_CHAR_EQUAL));
			queryCondition = DvSqlHelper.appendQueryStr(lQuery.toArray(new String[0]));
		}
		return queryCondition;
	}
	
}
