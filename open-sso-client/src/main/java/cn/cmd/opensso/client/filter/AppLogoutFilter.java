package cn.cmd.opensso.client.filter;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * APP单点登出Filter
 */
public class AppLogoutFilter extends LogoutFilter {

    @Override
    public boolean isAccessAllowed(HttpServletRequest request, HttpServletResponse response) throws IOException {
        if (getLogoutParam(request) != null) {
        	request.getSession().invalidate();
            return false;
        }
        return true;
    }
}