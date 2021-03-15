package com.fasset.mobileApp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.fasset.mobileApp.entity.LoginDetails;
import com.fasset.service.interfaces.IUserService;
import com.google.gson.JsonObject;

import org.json.JSONObject;

@RestController
public class LoginMobileAppController {
	
	@Autowired
	private IUserService userService;
	
	@RequestMapping(value="loginDetails", method=RequestMethod.POST,produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	private LoginDetails loginDetails(@RequestBody LoginDetails  details)
	{
		
		return userService.authenticateLoginDetail(details.getUsername(),details.getPassword());
	}
	
	
}