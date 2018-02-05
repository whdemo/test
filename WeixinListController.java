package com.gxkj.app.controller;

import java.util.List;

import com.gxkj.basis.model.BizDict;
import com.gxkj.basis.model.Gfile;
import com.gxkj.basis.model.User;
import com.gxkj.core.utils.BaseController;
import com.gxkj.core.utils.Utils;
import com.gxkj.project.model.Budget;
import com.gxkj.project.model.Chedule;
import com.gxkj.project.model.SocialProject;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;

public class WeixinListController  extends BaseController {

	/**
     * App微信社会投资项目列表接口
     */
	public void socialList(){
		String sname = getPara("sname");//项目名称
		String name = getPara("name");
		SocialProject temp = getModel(SocialProject.class);
		int pageSize = 9;
		int pageNum = 1;
		Page<Record> pg = temp.weixinList(pageNum, pageSize, sname);
		setAttr("pg", pg);
		setAttr("name",name);
		setAttr("sname",sname);
		render("/WEB-INF/view/wechat/search/social_list.jsp");
	}
	public void chedulelList(){
		String sname = getPara("sname");//项目名称
		String name = getPara("name");
		Chedule temp = getModel(Chedule.class);
		int pageSize = 9;
		int pageNum = 1;
		Page<Record> pg = temp.weixinList(pageNum, pageSize, sname);
		setAttr("pg", pg);
		setAttr("name",name);
		setAttr("sname",sname);
		render("/WEB-INF/view/wechat/search/governmental_list.jsp");
	}
	
	/**
	 * @explain 获取瀑布流json数据 
	 * @author wanghao
	 * @data 2018年1月12日 上午10:14:20
	 */
	public void getListJson(){
		String name = getPara("name");
		String sname = getPara("sname");
		if(name.equals("social")){
			SocialProject temp = getModel(SocialProject.class, "temp");
			//获取第几页
			Integer currPage = Integer.parseInt(getPara("pageCurrent"));
			int pageSize = 9;
			
			Page<Record> pg = temp.weixinList(currPage, pageSize, sname);
			
			renderJson(pg);
		}else{
			Chedule temp = getModel(Chedule.class);
			//获取第几页
			Integer currPage = Integer.parseInt(getPara("pageCurrent"));
			int pageSize = 9;
			
			Page<Record> pg = temp.weixinList(currPage, pageSize,sname);
			
			renderJson(pg);
		}
		
	}
	
	/**
	 * @explain 获取社会项目详细 
	 * @author chengyujun
	 * @data 2018年1月15日 上午10:14:20
	 */
	public void socialshow(){
		String id = getPara("id");
		String name = getPara("name");
		SocialProject temp = SocialProject.me.findById(id);
        if (Utils.isNotNull(temp)) {
            String createuserId = temp.getStr("createuser");
            String typeId = temp.getStr("type");
            temp.set("createuser", User.me.findById(createuserId).getStr("name"));
            temp.set("type", BizDict.me.findById(typeId).getStr("name"));
        }
        setAttr("id", id);
        setAttr("name", name);
        setAttr("temp", temp);
        
        Gfile gf = new Gfile();
        gf.set("pid", id);
        //gf.set("xmstatus", temp.getStr("period"));
        gf.set("belongsClass", "SocialProject");
        List<Gfile> itemFiles = Gfile.me.getItemFiles(gf);
        setAttr("itemFiles", itemFiles);
        
		render("/WEB-INF/view/wechat/search/social_show.jsp");
	}
	/**
	 * @explain 获取政府项目详细 
	 * @author chengyujun
	 * @data 2018年1月15日 上午10:14:20
	 */
	public void cheduleshow(){
		String name = getPara("name");
		String id = getPara("id");
		Chedule temp = Chedule.me.show(id);
		List<Budget> temps = Budget.me.budget(id);
		setAttr("temp", temp);
		setAttr("name", name);
		setAttr("id", id);
		setAttr("temps", temps);
		
		Gfile gf = new Gfile();
        gf.set("pid", id);
        //gf.set("xmstatus", temp.getStr("period"));
        gf.set("belongsClass", "Chedule");
        List<Gfile> itemFiles = Gfile.me.getItemFiles(gf);
        setAttr("itemFiles", itemFiles);
        
		render("/WEB-INF/view/wechat/search/chedule_show.jsp");
	}
	
	/**
	 * 跳转项目查询
	 */
	public void select(){
		String name = getPara("name");
		if(name.equals("social")){
			String sname = getPara("sname");//项目名称
			SocialProject temp = getModel(SocialProject.class);
			int pageSize = 9;
			int pageNum = 1;
			Page<Record> pg = temp.weixinList(pageNum, pageSize, sname);
			setAttr("pg", pg);
			setAttr("name", "social");
			render("/WEB-INF/view/wechat/search/social_list.jsp");
		}else if(name.equals("chedule")){
			String sname = getPara("sname");//项目名称
			Chedule temp = getModel(Chedule.class);
			int pageSize = 9;
			int pageNum = 1;
			Page<Record> pg = temp.weixinList(pageNum, pageSize, sname);
			setAttr("pg", pg);
			setAttr("name", "chedule");
			render("/WEB-INF/view/wechat/search/governmental_list.jsp");
		}
		
	}
}
