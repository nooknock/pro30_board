package com.myspring.pro30.common.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

public class ViewNameInterceptor extends HandlerInterceptorAdapter {

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		try {
			String viewName=getViewName(request);
			request.setAttribute("viewName", viewName);
			System.out.println("¿Œ≈Õº¡≈Õø°º≠ ¬Ô¿∫ ∫‰≥◊¿”"+viewName);
		} catch (Exception e) {
			// TODO: handle exception
		}
		return true;
	}
	
	
	
	private String getViewName(HttpServletRequest request) throws Exception {

		String contextPath = request.getContextPath();

		String uri = (String) request.getAttribute("javax.servlet.include.request_uri");

//		System.out.println(uri + "§ª");

		if (uri == null || uri.trim().equals("")) {
			uri = request.getRequestURI();
		}

		System.out.println(uri + "§ª§ª");
		int begin = 0;

		if (!((contextPath == null) || ("".equals(contextPath)))) {
//			System.out.println(contextPath);
			begin = contextPath.length();

		}

		int end;
		if (uri.indexOf(";") != -1) {
			end = uri.indexOf(";");
		} else if (uri.indexOf("?") != -1) {
			end = uri.indexOf("?");
		} else {
			end = uri.length();
		}

		String fileName = uri.substring(begin, end);// /test/memberInfo.do
//		System.out.println(fileName + "   »Æ¿Œ");
		if (fileName.indexOf(".") != -1) {
			fileName = fileName.substring(0, fileName.lastIndexOf(".")); // /test/memberInfo
		}
//		System.out.println(fileName + "   »Æ¿Œ2");
//		System.out.println(fileName.lastIndexOf("/", 0));
//		System.out.println(fileName.lastIndexOf("/", 1));
		if (fileName.indexOf("/") != -1) {
			fileName = fileName.substring(fileName.lastIndexOf("/", 1), fileName.length());
		}
		System.out.println(fileName);
		return fileName;
	}
	
	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
		
	}
	
	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
			throws Exception {
		
	}
}
