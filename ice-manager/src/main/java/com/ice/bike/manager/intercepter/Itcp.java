package com.ice.bike.manager.intercepter;

import java.io.IOException;
import java.io.PrintWriter;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.ice.bike.manager.core.HttpReq;
import com.ice.bike.manager.core.Rsp.RspErr;
import com.ice.bike.manager.core.UsrStub;
import com.ice.bike.manager.redis.DbRedis;

import misc.Crypto;
import misc.Log;

/**
 * 拦截器，加载配置文件
 * 
 * 创建时间： 2017年7月18日 下午11:46:16
 * 
 * @author ice
 *
 */
public class Itcp extends HandlerInterceptorAdapter {

	@Resource
	public DbRedis dbRedis;

	/** 容器初始化后的首个调用函数, 应用入口被放置在这里. */
	public void init() throws Exception {
		Log.setError();
	}

	/** 请求处理后响应前调用. */
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
		super.postHandle(request, response, handler, modelAndView);
	}

	/** 请求处理前调用. */
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		HttpReq req = new HttpReq(request, response);
		String sign = req.getParStr("sign");
		String salt = req.getParStr("salt");
		String token = req.getParStr("token");
		if (sign == null || salt == null) {
			req.end(RspErr.ERR_FORMAT_ERROR);
			return false;
		}
		if (salt.length() < 8) {
			req.end(RspErr.ERR_SALT);
			return false;
		}
		Log.info("request:%s", request.getRequestURI() + "?" + request.getQueryString());
		Log.info("request:%s", request.getServletPath());
		if (!request.getServletPath().equals("/usr/login")
				&& !request.getServletPath().equals("/usr/regist")) {/* 非用户登录. */
			UsrStub stub = dbRedis.getToken(token);
			if (stub == null) {
				req.end(RspErr.ERR_NOT_AUTH);
				return false;
			}
			if (!Crypto
					.sha1StrLowerCase(
							(salt + stub.pwd + token + request.getServletPath() + "?" + req.getAction()).getBytes())
					.equals(sign)) {
				req.end(RspErr.Err_SIGN);
				return false;
			}
		}
		return true;
	}

	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
			throws Exception {
		super.afterCompletion(request, response, handler, ex);
		response.setCharacterEncoding("UTF-8");
		response.setContentType("text/html; charset=utf-8");
		PrintWriter writer = null;
		try {
			writer = response.getWriter();
		} catch (IOException e) {
		} finally {
			if (writer != null)
				writer.close();
		}
	}

	public void afterConcurrentHandlingStarted(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		super.afterConcurrentHandlingStarted(request, response, handler);
	}

}
