package info.tcb.simplezuulproxy;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;

/**
 * Created by Kad on 05/12/2015.
 */
@SpringBootApplication
@EnableZuulProxy
public class SimpleZuulProxyApplication {

    /**
     * Spring boot application launch
     *
     * @param args
     */
    public static void main(String[] args) {
        new SpringApplicationBuilder(SimpleZuulProxyApplication.class).run(args);
    }
}
