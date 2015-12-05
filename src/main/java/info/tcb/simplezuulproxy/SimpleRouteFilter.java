package info.tcb.simplezuulproxy;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by Kad on 05/12/2015.
 */
@Slf4j
@Component
public class SimpleRouteFilter extends ZuulFilter {

    @Value("${unauthorized.url.redirect:http://blog.the-coffee-beans.info}")
    private String urlRedirect;

    /**
     * Indicates which type of Zuul filter this is. In our case, the "route" value is returned to indicate to the Zuul
     * server that this is a route filter.
     *
     * @return "route"
     */
    @Override
    public String filterType() {
        return "route";
    }

    @Override
    public int filterOrder() {
        return 0;
    }

    /**
     * Indicates if a request should be filtered or not. In our case, all requests are taken into account
     * (always return true).
     *
     * @return true
     */
    public boolean shouldFilter() {
        return true;
    }

    /**
     * Indicates if the provided request is authorized or not.
     *
     * @param request the provided request
     *
     * @return true if the provided request is authorized, false otherwise
     */
    private boolean isAuthorizedRequest(HttpServletRequest request) {
        // apply your filter here
        return false;
    }

    /**
     * This method allows to set the route host into the Zuul request context provided as parameter.
     * The url is extracted from the orginal request and the host is extracted from it.
     *
     * @param ctx the provided Zuul request context
     *
     * @throws MalformedURLException
     */
    private void setRouteHost(RequestContext ctx) throws MalformedURLException {

        String urlS = ctx.getRequest().getRequestURL().toString();
        URL url = new URL(urlS);
        String protocol = url.getProtocol();
        String rootHost = url.getHost();
        int port = url.getPort();
        String portS = (port > 0 ? ":" + port : "");
        ctx.setRouteHost(new URL(protocol + "://" + rootHost + portS));

    }

    /**
     * The filter execution
     *
     * @return
     */
    public Object run() {

        // logging the interception of the query
        log.debug("query interception");

        // retrieving the Zuul request context
        RequestContext ctx = RequestContext.getCurrentContext();

        try {

            // if the requested url is authorized, the route host is set to the requested one
            if (isAuthorizedRequest(ctx.getRequest())) {
                setRouteHost(ctx);
            } else {
                // if the requested URL is not authorized, the route host is set to the urlRedirect value
                // the client will be redirected to the new host
               ctx.setRouteHost(new URL(urlRedirect));
            }
        } catch (MalformedURLException e) {
            log.error("", e);
        }

        return null;
    }
}
