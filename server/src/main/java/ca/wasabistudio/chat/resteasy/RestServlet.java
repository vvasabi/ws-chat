package ca.wasabistudio.chat.resteasy;

import java.io.IOException;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.jboss.resteasy.plugins.server.servlet.HttpServletDispatcher;
import org.jboss.resteasy.spi.Registry;
import org.jboss.resteasy.spi.ResourceFactory;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

@SuppressWarnings("serial")
public class RestServlet extends HttpServlet {

    private static final String AccessControlAllowOrigin = "Access-Control-Allow-Origin";
    private static final String AccessControlAllowMethods = "Access-Control-Allow-Methods";
    private static final String AccessControlAllowHeaders = "Access-Control-Allow-Headers";
    private static final String CONFIG_BEANS = "beans";

    private HttpServletDispatcher resteasyServlet;
    private String allowOrigin;
    private String allowMethods;
    private String allowHeaders;

    public RestServlet() {
        resteasyServlet = new HttpServletDispatcher();
        allowOrigin = null;
        allowMethods = null;
        allowHeaders = null;
    }

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        initAccessControlHeaders(config);
        ServletContext context = getServletContext();
        WebApplicationContext appContext = WebApplicationContextUtils
            .getWebApplicationContext(context);
        resteasyServlet.init(config);
        createResourceFactories(appContext);
    }

    private void initAccessControlHeaders(ServletConfig config) {
        allowOrigin = config.getInitParameter(AccessControlAllowOrigin);
        allowMethods = config.getInitParameter(AccessControlAllowMethods);
        allowHeaders = config.getInitParameter(AccessControlAllowHeaders);
    }

    @Override
    public void service(HttpServletRequest request,
            HttpServletResponse response) throws ServletException, IOException {
        resteasyServlet.service(request, response);
        setAccessControlHeaders(response);
    }

    private void setAccessControlHeaders(HttpServletResponse response)
            throws IOException {
        if (allowOrigin != null) {
            response.addHeader(AccessControlAllowOrigin, allowOrigin);
        }
        if (allowMethods != null) {
            response.addHeader(AccessControlAllowMethods, allowMethods);
        }
        if (allowHeaders != null) {
            response.addHeader(AccessControlAllowHeaders, allowHeaders);
        }
    }

    @Override
    public void destroy() {
        super.destroy();
        resteasyServlet.destroy();
    }

    private void createResourceFactories(WebApplicationContext context) {
        String[] beans = getBeanNames();
        Registry registry = resteasyServlet.getDispatcher().getRegistry();
        for (String bean : beans) {
            ResourceFactory factory = new SpringResourceFactory(context, bean);
            registry.addResourceFactory(factory);
        }
    }

    private String[] getBeanNames() {
        ServletConfig config = getServletConfig();
        String configBeans = config.getInitParameter(CONFIG_BEANS);
        if (configBeans == null) {
            return new String[0];
        }
        return configBeans.split(",\\s+");
    }

}
