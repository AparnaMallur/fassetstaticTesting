package com.fasset.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.ModelAndView;

import com.fasset.controller.abstracts.MyAbstractController;
import com.fasset.entities.MenuAccessMaster;
import com.fasset.entities.MenuMaster;
import com.fasset.entities.User;
import com.fasset.exceptions.MyWebException;
import com.fasset.form.UserInfo;
import com.fasset.service.interfaces.IMenuAccessMasterService;
import com.fasset.service.interfaces.IMenuMasterService;
import com.fasset.service.interfaces.IUserService;


@Controller
@SessionAttributes("user")
public class MenuAccessController extends MyAbstractController {
	
	
		@Autowired
		private IMenuMasterService MenuMasterService;
		
		@Autowired
		private IMenuAccessMasterService menuAccessService;
		
		@Autowired
		private IUserService UserService;
		
		
		private Long Id;
	
	@RequestMapping(value="MenuAccess", method=RequestMethod.GET)
	public ModelAndView MasaterMenuList(HttpServletRequest request)
	{
		HttpSession session = request.getSession();
	    Map<Long,String> roleMaster =  new HashMap<Long, String>();
		User user = (User) session.getAttribute("user");
		long user_Id= (long) session.getAttribute("user_id");
		long roleId= (long) session.getAttribute("role");

		roleMaster =menuAccessService.getRoleList(roleId);
	
		ModelAndView model = new ModelAndView();
		
		MenuAccessMaster MenuAccess=new MenuAccessMaster();
		List<MenuMaster> menuMasterList=MenuMasterService.findAllbyrole(roleId,user_Id);
		
		
		model.addObject("menuMasterList",menuMasterList);
		
		model.addObject("MenuAccess",MenuAccess);
		model.addObject("roleMaster",roleMaster  );

		model.setViewName("/master/menuAccess");
		return model;
		
	}
	
	@RequestMapping(value="getList" , method=RequestMethod.POST)
	public  @ResponseBody List<UserInfo> getListNames(@RequestParam("id") long id,HttpServletRequest request)
	{
		ModelAndView model=new ModelAndView();
		HttpSession session = request.getSession();
		long role=(long)session.getAttribute("role");
		long company_id=(long)session.getAttribute("company_id");
		List<UserInfo> users = new ArrayList<UserInfo>();
		List<User> userNames=menuAccessService.getnameList(id,role,company_id);
		
		for(User user : userNames)
		{
			UserInfo info = new UserInfo();
			info.setUsername(user.getFirst_name());
			info.setUser_id(user.getUser_id());
			users.add(info);
		}
		model.addObject("users",users);
		return users;
		
	}
	
	@RequestMapping(value="getMenuAccessList" , method=RequestMethod.POST)
	public  @ResponseBody List<MenuAccessMaster> getMenuNames(@RequestParam("id") long id,@RequestParam("role_Id") long role_Id)
	{
		System.out.println("getMenuAccessList userId roleId==========:>"+id+"	"+role_Id);
		Id=id;
		List<MenuAccessMaster> accessList =	menuAccessService.getAccess(id , role_Id);
		System.out.println("accessList===:>"+accessList.size());
		return accessList;
	}
	@RequestMapping(value="menuAccessStore", method=RequestMethod.POST)
	public @ResponseBody String MenuAccessStore(@RequestBody String access,
			HttpServletRequest request,@RequestParam("role_Id") long role_Id,@RequestParam("allWithMenu") long allWithMenu)
	{
		System.out.println("Role Id allWithMenu======:>"+role_Id+"	"+allWithMenu);
		
		String msg="";
		JSONObject json;
		MenuAccessMaster isexists;
		ModelAndView model = new ModelAndView();
		JSONArray jsonArray = new JSONArray(access);
		for(int i=0; i < jsonArray.length(); i++) {
			Boolean flag = false;
			MenuAccessMaster menuaccess = new MenuAccessMaster();
			json = jsonArray.getJSONObject(i);
			JSONArray accessArray = json.getJSONArray("access");

			for(int j=0; j<4; j++) {
				if(accessArray.getBoolean(j) == true) {
					flag = true;
					break;
				}
			}
			System.out.println("Id ======:>"+Id);
			System.out.println("accessArray"+accessArray.getBoolean(0));
			isexists=menuAccessService.findmenuexist(Id, json.getInt("menuId"), role_Id);
			if(isexists==null)
			{	
				System.out.println("entering null");
				if(flag) {
					if(allWithMenu==0) {
						 menuaccess.setMenu_Id(json.getInt("menuId"));
						menuaccess.setAccess_List(accessArray.getBoolean(0));
						 menuaccess.setAccess_Insert(accessArray.getBoolean(1));
						 menuaccess.setAccess_Update(accessArray.getBoolean(2));
						 menuaccess.setAccess_View(accessArray.getBoolean(3));
						 menuaccess.setAccess_Delete(accessArray.getBoolean(4));
						menuaccess.setUser_Id((long) 0);
						 menuaccess.setRole_Id(role_Id);
						 menuaccess.setFlag(true);
						 
						 String remoteAddr = "";
						 remoteAddr = request.getHeader("X-FORWARDED-FOR");
				            if (remoteAddr == null || "".equals(remoteAddr)) {
				                remoteAddr = request.getRemoteAddr();
				            }
				            menuaccess.setIpAddress(remoteAddr);
						 msg= menuAccessService.SaveMenuAccessMaster(menuaccess);
					}
					else {
						//long roleId=0;
						 menuaccess.setMenu_Id(json.getInt("menuId"));
						menuaccess.setAccess_List(accessArray.getBoolean(0));
						 menuaccess.setAccess_Insert(accessArray.getBoolean(1));
						 menuaccess.setAccess_Update(accessArray.getBoolean(2));
						 menuaccess.setAccess_View(accessArray.getBoolean(3));
						 menuaccess.setAccess_Delete(accessArray.getBoolean(4));
						 menuaccess.setUser_Id(Id);
						 menuaccess.setRole_Id(role_Id);
						 menuaccess.setFlag(false);
						 
						 String remoteAddr = "";
						 remoteAddr = request.getHeader("X-FORWARDED-FOR");
				            if (remoteAddr == null || "".equals(remoteAddr)) {
				                remoteAddr = request.getRemoteAddr();
				            }
				            menuaccess.setIpAddress(remoteAddr);
						 msg= menuAccessService.SaveMenuAccessMaster(menuaccess);
					}
				}
			}		
			else
			{
				isexists.setAccess_List(accessArray.getBoolean(0));
				isexists.setAccess_Insert(accessArray.getBoolean(1));
				isexists.setAccess_Update(accessArray.getBoolean(2));
				isexists.setAccess_View(accessArray.getBoolean(3));
				isexists.setAccess_Delete(accessArray.getBoolean(4));
				msg="Record Updated Successfully";
				try {
					menuAccessService.update(isexists);
				} catch (MyWebException e) {
					e.printStackTrace();
				}
			}
		}		
		model.setViewName("/master/menuAccess");
		return msg;
		
	}
	
	@RequestMapping(value="getDynamicList" , method=RequestMethod.POST)
	public  @ResponseBody List getDynamicParentList(HttpServletRequest request, HttpServletResponse response
)
	{
		System.out.println("=========getDynamicParentList===========");
		HttpSession session = request.getSession(true);
		User user = (User) session.getAttribute("user");
		//long  user_Id=user.getUser_id();
		long user_Id= (long) session.getAttribute("user_id");
		long role_Id= (long) session.getAttribute("role");

		System.out.println("User  Id======"+user_Id);
		List dynamicList=menuAccessService.getDynamicMenuAccess(user_Id,role_Id);
		//System.out.println("getDynamicList Controller======"+dynamicList.size());
		session.setAttribute("data0", dynamicList.get(0));
		session.setAttribute("data1", dynamicList.get(1));
		session.setAttribute("data2", dynamicList.get(2));
		return dynamicList;
		
	}
	@RequestMapping(value="subMenuList" , method=RequestMethod.POST)
	public  @ResponseBody List<MenuMaster> getDynamicSubMenuList(@RequestParam("menu_Id") Long menu_Id)
	{
		//System.out.println("=========getDynamicSubMenuList==========="+menu_Id);
		List<MenuMaster> dynamicSubMenuList=MenuMasterService.getSubMenuList(menu_Id);
		//System.out.println("dynamicSubMenuList Size======="+dynamicSubMenuList.size());
		return dynamicSubMenuList;
		
	}

}
