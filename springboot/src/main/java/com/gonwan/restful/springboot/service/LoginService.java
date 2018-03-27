package com.gonwan.restful.springboot.service;

import java.security.GeneralSecurityException;
import java.util.Objects;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gonwan.restful.springboot.RestfulException;
import com.gonwan.restful.springboot.RestfulException.Predefined;
import com.gonwan.restful.springboot.RestfulRepository;
import com.gonwan.restful.springboot.model.TAuthority;
import com.gonwan.restful.springboot.model.TUser;
import com.gonwan.restful.springboot.request.LoginRequest;
import com.gonwan.restful.springboot.util.AesCryptography;

@Service
public class LoginService {

    private static final Logger logger = LoggerFactory.getLogger(LoginService.class);

    @Autowired
    private RestfulRepository repo;
    @Autowired
    private AesCryptography crypto;

    public void login(LoginRequest request) throws RestfulException {
        /* authentication */
        checkAuthentication(request.getUsername(), request.getPassword());
    }

    public void checkAuthentication(String username, String password) throws RestfulException {
        /* user */
        TUser userEntry = repo.getUsers().stream()
                .filter(x -> StringUtils.equals(x.getUsername(), username))
                .findFirst().orElse(null);
        if (userEntry == null) {
            throw Predefined.USER_NOT_EXIST;
        }
        /* authority */
        TAuthority authEntity = repo.getAuthorities().stream()
                .filter(x -> Objects.equals(x.getUserId(), userEntry.getId()))
                .findFirst().orElse(null);
        if (authEntity == null) {
            throw Predefined.USER_NOT_AUTHORIZED;
        }
        /* password */
        String encryptedPassword;
        try {
            encryptedPassword = crypto.Encrypt(password);
        } catch (GeneralSecurityException e) {
            logger.info("", e);
            throw Predefined.USER_WRONG_PASSWORD;
        }
        if (!StringUtils.equals(userEntry.getPassword(), encryptedPassword)) {
            throw Predefined.USER_WRONG_PASSWORD;
        }
    }

}
