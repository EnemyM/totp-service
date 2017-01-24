import com.fasterxml.jackson.databind.ObjectMapper;
import com.mservice.totp.api.Otp;
import com.mservice.totp.api.config.ApplicationContext;
import com.mservice.totp.api.model.OtpContainer;
import com.mservice.totp.api.model.ResponseContainer;
import com.mservice.totp.api.model.Secret;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.io.IOException;
import java.net.URI;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

/**
 * Created by anton on 24.01.17.
 */

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {ApplicationContext.class})
@WebAppConfiguration
public class TestOtp {

    private MockMvc mockMvc;

    private String url = "/verification/totp";

    @Autowired
    private WebApplicationContext context;

    @Before
    public void setup() {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(context)
                .build();
        assertNotNull(otp);
    }

    @Autowired
    Otp otp;

    /**
     * successful generation opt
     */
    @Test
    public void testGenerateOtpService() throws IOException {
        assertNotNull((new ObjectMapper().readValue(((ResponseContainer) otp.generate(new Secret("someUniqueData"))).getContent(), OtpContainer.class)).getOtp());
    }

    /**
     * successful validation
     */
    @Test
    public void testValidateOpt() throws IOException {
        ResponseContainer responseContainer = (ResponseContainer) otp.generate(new Secret("someUniqueData"));
        OtpContainer otpContainer = new ObjectMapper().readValue(responseContainer.getContent(), OtpContainer.class);
        assertEquals(((ResponseContainer) otp.validate(otpContainer.getOtp())).getCode(), new Integer(200));
    }

    @Test
    public void testFailedToptValidation() {
        assertEquals(((ResponseContainer) otp.validate(("hello"))).getCode(), new Integer(500));
    }

    @Test
    public void testMockGenerate() throws Exception {
        MvcResult result = mockMvc.perform(post(new URI(url + "/generate"))
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(new ObjectMapper().writeValueAsString(new Secret("someData"))))
                .andReturn();
        assertEquals(new Integer(((ResponseEntity) result.getAsyncResult()).getStatusCodeValue()), new Integer(200));
        MvcResult result1 = mockMvc.perform(post(new URI(url + "/validate"))
                .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
                .content((String) ((ResponseEntity) result.getAsyncResult()).getBody()))
                .andReturn();
        assertEquals(new Integer(((ResponseEntity) result1.getAsyncResult()).getStatusCodeValue()), new Integer(200));
    }
}
