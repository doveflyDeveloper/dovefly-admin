package com.deertt.module.sys.resource.dao;

import java.util.List;

import com.deertt.frame.base.dao.IDvBaseDao;
import com.deertt.module.sys.resource.vo.ResourceVo;

/**
 * 功能、用途、现存BUG:
 * 
 * @author fengcm
 * @version 1.0.0
 * @see 需要参见的其它类
 * @since 1.0.0
 */
public interface IResourceDao extends IDvBaseDao<ResourceVo, Integer> {
	
	/**
	 * 更新父级资源名称
	 * @param id
	 * @param parent_name 新父级资源名称
	 * @return
	 */
	public int updateParentName(Integer id, String parent_name);
	
	/**
	 * 更新是否为叶子节点状态
	 * @param ids
	 * @param is_leaf ‘0’不是叶子节点， ‘1’是叶子节点
	 * @return
	 */
	public int updateIs_leaf(Integer[] ids, String is_leaf);
	
	/**
	 * 查询当前节点下的所有子节点的最大编号
	 * @param id
	 * @return
	 */
	public String queryChildNodeMaxCode(Integer id);
	
	/**
	 * 查找角色拥有的资源
	 * @param role_id 角色ID
	 * @return
	 */
	public List<ResourceVo> queryResourcesByRoleId(Integer roleId);

	/**
	 * 查找用户菜单资源
	 * @param user_id
	 * @return
	 */
	public List<ResourceVo> queryMenusByUserId(Integer user_id);
	
	/**
	 * 对所选数据进行重新排序（排序规则依据sort_ids的顺序）
	 * @return
	 */
	public int sort(String sort_ids);
}
