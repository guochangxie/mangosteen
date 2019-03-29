package com.mangosteen.configContext;
import org.springframework.context.annotation.Configuration;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * @author guochang.xie
 * @Description: TODO
 * @date 2019/3/2711:26 AM
 */
@Configuration
public class LoginFilter implements Filter {


    String[] includeUrls = new String[]{"/isLogin","/login","/static/","/mangosteen/log"};

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        HttpSession session = request.getSession(false);
        String uri = request.getRequestURI();

        //是否需要过滤
        boolean needFilter = isNeedFilter(uri);

        if (needFilter) { //不需要过滤直接传给下一个过滤器
            filterChain.doFilter(servletRequest, servletResponse);

        } else {

            // session中包含user对象,则是登录状态
            if(session!=null&&session.getAttribute("userName") != null){
                filterChain.doFilter(request, response);
            }else{
                    //重定向到登录页(需要在static文件夹下建立此html文件)
                    response.sendRedirect("/mangosteen/project/login");
            }
        }
    }

    /**
     * @Author: xxxxx
     * @Description: 是否需要过滤
     * @Date: 2018-03-12 13:20:54
     * @param uri
     */
    public boolean isNeedFilter(String uri) {

        for (String includeUrl : includeUrls) {
            if(uri.contains(includeUrl)) {
                return true;
            }
        }

        return false;
    }

    @Override
    public void destroy() {

    }
}
