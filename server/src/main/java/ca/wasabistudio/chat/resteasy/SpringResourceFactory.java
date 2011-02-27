package ca.wasabistudio.chat.resteasy;

import org.jboss.resteasy.spi.HttpRequest;
import org.jboss.resteasy.spi.HttpResponse;
import org.jboss.resteasy.spi.InjectorFactory;
import org.jboss.resteasy.spi.ResourceFactory;
import org.springframework.web.context.WebApplicationContext;

public final class SpringResourceFactory implements ResourceFactory {

    private WebApplicationContext context;
    private String name;

    public SpringResourceFactory(WebApplicationContext context, String name) {
        this.context = context;
        this.name = name;
    }

    @Override
    public Object createResource(HttpRequest request, HttpResponse response,
            InjectorFactory factory) {
        return context.getBean(name);
    }

    @Override
    public Class<?> getScannableClass() {
        return context.getType(name);
    }

    @Override
    public void registered(InjectorFactory factory) {
        // not implemented
    }

    @Override
    public void requestFinished(HttpRequest request, HttpResponse response,
            Object resource) {
        // not implemented
    }

    @Override
    public void unregistered() {
        // not implemented
    }

}
