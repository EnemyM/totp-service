package com.mservice.totp.rest;

import com.mservice.totp.api.Otp;
import com.mservice.totp.api.model.OtpContainer;
import com.mservice.totp.api.model.ResponseContainer;
import com.mservice.totp.api.model.Secret;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.Callable;

/**
 * Created by anton on 15/12/16.
 */
@RestController
@RequestMapping(value = {"/verification/totp"})
public class RestTOTP {

    private static final Logger logger = LoggerFactory.getLogger(RestTOTP.class);

    @Autowired
    Otp otp;

    @PostMapping(value = {"/generate"})
    public Callable<ResponseEntity<?>> otpGenerate(@RequestBody Secret secret) {
        return () -> {
            logger.info("Obtained data to generate opt = " + secret);
            try {
                ResponseContainer responseContainer = (ResponseContainer) otp.generate(secret);
                logger.debug("Response = " + responseContainer);
                return new ResponseEntity<Object>(responseContainer.getContent(), HttpStatus.valueOf(responseContainer.getCode()));
            } catch (Exception e) {
                logger.error("Error generating opt due to ", e);
                return new ResponseEntity<Object>("Ops, Pleas try again", HttpStatus.CONFLICT);
            }
        };
    }

    @PostMapping(value = {"/validate"})
    public Callable<ResponseEntity<?>> otpValidate(@RequestBody OtpContainer otp) {
        return () -> {
            try {
                logger.info("validation opt = " + otp);
                ResponseContainer responseContainer = (ResponseContainer) this.otp.validate(otp.getOtp());
                logger.debug("Response = " + responseContainer);
                return new ResponseEntity<Object>(responseContainer.getContent(), HttpStatus.valueOf(responseContainer.getCode()));
            } catch (Exception e) {
                e.printStackTrace();
                return new ResponseEntity<Object>("Ops, Pleas try again", HttpStatus.CONFLICT);
            }
        };
    }
}
