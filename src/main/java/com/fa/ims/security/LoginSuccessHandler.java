/*-============================================================================
 * PizSoft. PROPRIETARY
 * Copyright© 2021 PizSoft.
 * UNPUBLISHED WORK
 * ALL RIGHTS RESERVED
 *
 * This software is the confidential and proprietary information of
 * PizSoft. ("Proprietary Information"). Any use, reproduction,
 * distribution or disclosure of the software or Proprietary Information,
 * in whole or in part, must comply with the terms of the license
 * agreement, nondisclosure agreement or contract entered into with
 * PizSoft. providing access to this software.
 *
 *=============================================================================
 */
package com.fa.ims.security;

import com.fa.ims.constant.CommonConstants;
import com.fa.ims.enums.UserRole;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.security.web.server.authentication.RedirectServerAuthenticationSuccessHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class LoginSuccessHandler extends SimpleUrlAuthenticationSuccessHandler implements AuthenticationSuccessHandler {


    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {
//        String referer = request.getHeader("referer");
        String userName = SecurityUtil.getCurrentUserLogin()
                .orElseThrow(() -> new UsernameNotFoundException("Can not get user login"));
        request.getSession().setAttribute(CommonConstants.USERNAME_SESSION, userName);
        request.getSession().setAttribute(CommonConstants.USER_ROLE_SESSION, SecurityUtil.getListUserLoginRole());

        super.onAuthenticationSuccess(request, response, authentication);
    }

    /**
     * Determine target url based on role
     */
    @Override
    protected String determineTargetUrl(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication) {
        return "/home-page";
    }
}