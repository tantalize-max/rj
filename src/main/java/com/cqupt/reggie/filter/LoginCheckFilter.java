package com.cqupt.reggie.filter;

import com.alibaba.fastjson.JSON;
import com.cqupt.reggie.common.BaseContext;
import com.cqupt.reggie.common.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.AntPathMatcher;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
@WebFilter(filterName = "loginCheckFilter", urlPatterns = "/*")
public class LoginCheckFilter implements Filter {

    public static final AntPathMatcher PATH_MATCHER = new AntPathMatcher();

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        String requestURI = request.getRequestURI();

        log.info("拦截到请求：{}", requestURI);

        //定义不需要处理的请求路径
        String[] urls = new String[]{
                "/employee/login",
                "/employee/logout",
                "/backend/**",
                "/front/**",
                //对用户登陆操作放行
                "/user/login",
                "/user/sendMsg"
        };

        log.info("拦截到请求：{}", request.getRequestURI());
        //2、判断本次请求是否需要处理
        boolean check = checkUrl(urls, requestURI);
        if (check) {
            log.info("用户已登录，用户id为：{}", request.getSession().getAttribute("employee"));
            Long empId= (Long) request.getSession().getAttribute("employee");
            BaseContext.setCurrentId(empId);
            log.info("当前empId为：{}",empId);
            filterChain.doFilter(request, response);
            return;
        }
        //4、判断登录状态，如果已登录，则直接放行
        if (request.getSession().getAttribute("employee") != null) {
            log.info("用户已登录，用户id为：{}", request.getSession().getAttribute("employee"));
            filterChain.doFilter(request, response);
            return;
        }
        //        4-2、判断登录状态，如果已登录，则直接放行
        if (request.getSession().getAttribute("user") != null) {
            log.info("用户已登录，用户id为：{}", request.getSession().getAttribute("user"));

            Long userId= (Long) request.getSession().getAttribute("user");

            BaseContext.setCurrentId(userId);

            filterChain.doFilter(request, response);
            return;
        }
        log.info("用户未登录");
        //5.如果未登录则会返回未登录结果，通过流的方式返回客户端
        response.getWriter().write(JSON.toJSONString(R.error("NOTLOGIN")));
        return;
    }


    /**
     * 路径匹配，检查本次请求是否放行
     *
     * @param urls
     * @param requestUrl
     * @return boolean
     */
    public boolean checkUrl(String[] urls, String requestUrl) {
        for (String url : urls) {
            boolean match = PATH_MATCHER.match(url, requestUrl);
            if (match) {
                return true;
            }
        }
        return false;
    }
}
