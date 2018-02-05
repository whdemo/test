package com.gxkj.app.controller;

import java.util.HashMap;
import java.util.Map;

import com.gxkj.basis.model.Menu;
import com.gxkj.basis.model.User;
import com.gxkj.core.utils.BaseController;
import com.gxkj.core.utils.Const;
import com.gxkj.core.utils.Utils;

/**
 * 微信小程序接口
 * @author xiaoxinrui
 * @date 2017-12-27
 */
public class AppController extends BaseController {
    
//    /** 小程序ID */
//    private final static String APPID = "wxc54b96cad9240f6b";
//    /** 小程序密钥 */
//    private final static String APPSECRET = "c521587937e8b7a3e4b504544df2c9eb";
    
    /**
     * App微信小程序登录接口
     */
	public void login(){
	    
        String username = getPara("name");
        String pwd = getPara("password");
        
        Map<String, Object> loginMsg = new HashMap<String, Object>();
        try {
            if (Utils.isNull(username) || Utils.isNull(pwd)) {
                loginMsg.put("msg", "帐号密码不能为空");
                loginMsg.put("status", false);
                renderJson(loginMsg);
                return;
            }
            
            User user = User.me.findUsername(username);
            if (user == null) {
                loginMsg.put("msg", "用户不存在");
                loginMsg.put("status", false);
                renderJson(loginMsg);
                return;
            }
            
            if (!user.get("pwd").equals(Utils.MD5(pwd))) {
                loginMsg.put("msg", "密码错误");
                loginMsg.put("status", false);
                renderJson(loginMsg);
                return;
            } else if (Utils.isNotNull(user.get("userstatus")) && user.getBoolean("userstatus")) {
                loginMsg.put("msg", "帐号已锁定");
                loginMsg.put("status", false);
                renderJson(loginMsg);
                return;
            }else if(!Menu.me.checkMenu(Const.LOG_URI, user)){
                loginMsg.put("msg", "帐号无权限");
                loginMsg.put("status", false);
                renderJson(loginMsg);
                return;
            }
            
            setSessionAttr(Const.LOG_USER, user);
            setSessionAttr(Const.LOG_USERID, user.getStr("id"));
            setSessionAttr(Const.LOG_USERNAME, user.getStr("username"));
            
            loginMsg.put("msg", "授权登录");
            loginMsg.put("status", true);
            loginMsg.put("userid", user.getStr("id"));
            loginMsg.put("username", user.getStr("username"));
            
            renderJson(loginMsg);
            
        } catch (Exception e) {
            loginMsg.put("msg", "操作异常，请重试");
            loginMsg.put("status", false);
            renderJson(loginMsg);
        }
        
	}
	
	/**
     * App微信小程序登出接口
     */
    public void logout(){
        
        Map<String, Object> loginMsg = new HashMap<String, Object>();
        String userId = getPara("token");
        if (Utils.isNotNull(User.me.findById(userId))) {
            loginMsg.put("msg", "退出成功");
            loginMsg.put("status", true);
            renderJson(loginMsg);
        } else {
            loginMsg.put("msg", "操作异常，请重试");
            loginMsg.put("status", false);
            renderJson(loginMsg);
        }
    }
      
}
