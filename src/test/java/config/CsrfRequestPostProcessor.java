package config;

import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.test.web.servlet.request.RequestPostProcessor;


/**
 * Created by tewe on 1/10/2017.
 */

/**
 * A request post processor to add <em>csrf</em> information.
 */
public class CsrfRequestPostProcessor implements RequestPostProcessor {

    private boolean useInvalidToken = false;

    private boolean asHeader = false;


    @Override
    public MockHttpServletRequest postProcessRequest(MockHttpServletRequest request) {

        return request;
    }

    public RequestPostProcessor invalidToken() {
        this.useInvalidToken = true;
        return this;
    }

    public RequestPostProcessor asHeader() {
        this.asHeader = true;
        return this;
    }

    public static CsrfRequestPostProcessor csrf() {
        return new CsrfRequestPostProcessor();
    }
}