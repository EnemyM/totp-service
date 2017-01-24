package com.mservice.totp.api.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mservice.totp.api.Otp;
import com.mservice.totp.api.model.OtpContainer;
import com.mservice.totp.api.model.ResponseContainer;
import com.mservice.totp.api.model.Secret;
import org.jboss.aerogear.security.otp.Totp;
import org.jboss.aerogear.security.otp.api.Base32;
import org.jboss.aerogear.security.otp.api.Clock;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * Created by anton on 15/12/16. verification
 */
@Service
public class JBossVerificationOtpService implements Otp<Object, Secret> {

    private static final Logger logger = LoggerFactory.getLogger(JBossVerificationOtpService.class);

    @Value("${totp.expiration}")
    private String expiration;

    private Totp totp;

    @Override
    public Object generate(Secret secret) {
        if (secret == null || secret.getSecret() == null) {
            logger.info("Bad data was received = " + secret);
            return new ResponseContainer(500, "Secret data cannot be null");
        }
        try {
            totp = new Totp(Base32.encode((secret.getSecret()).getBytes()), new Clock(Integer.parseInt(expiration)));
            return new ResponseContainer(200, new ObjectMapper().writeValueAsString(new OtpContainer(totp.now())));
        } catch (Exception e) {
            logger.error("Error during generating opt due to ", e);
            return new ResponseContainer(500, "Bad data");
        }
    }

    @Override
    public Object validate(Object opt) {
        if (opt == null) {
            logger.error("Bad data was received = " + opt);
            return new ResponseContainer(500, "Secret data cannot be null");
        }
        try {
            return new ResponseContainer(200, String.valueOf(totp.verify(String.valueOf(opt))));
        } catch (Exception e) {
            logger.error("Error during validating opt due to ", e);
            return new ResponseContainer(500, "Bad opt secret");
        }
    }
}
