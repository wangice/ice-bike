package com.ice.bike.api.intercepter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.ice.bike.api.core.MsgLoader;

import misc.Log;
import misc.Misc;

/**
 * 拦截器，加载配置文件
 * 
 * 创建时间： 2017年7月18日 下午11:46:16
 * 
 * @author ice
 *
 */
public class Itcp extends HandlerInterceptorAdapter {

	/** 容器初始化后的首个调用函数, 应用入口被放置在这里. */
	public void init() throws Exception {
		if (!MsgLoader.init()) { /* 加载配置文件. */
			Log.fault("system startup failed.");
			Misc.lazySystemExit();
		}
	}

	/** 请求处理后响应前调用. */
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
		super.postHandle(request, response, handler, modelAndView);
	}

	/** 请求处理前调用. */
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		return true;
	}

	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
			throws Exception {
		super.afterCompletion(request, response, handler, ex);
	}

	public void afterConcurrentHandlingStarted(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		super.afterConcurrentHandlingStarted(request, response, handler);
	}

}
