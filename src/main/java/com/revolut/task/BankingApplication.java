package com.revolut.task;

import com.revolut.task.config.ApplicationConfig;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.servlet.ServletContainer;

public class BankingApplication {

    public static void main(final String[] args) throws Exception {

        final ResourceConfig resourceConfig = new ApplicationConfig();
        resourceConfig.packages("com.revolut.task");

        final Server server = new Server(8080);

        final ServletContextHandler jerseyHandler = new ServletContextHandler(ServletContextHandler.NO_SESSIONS);
        jerseyHandler.setContextPath("/");

        final ServletHolder servletHolder = new ServletHolder(new ServletContainer(resourceConfig));
        servletHolder.setInitOrder(0);

        jerseyHandler.addServlet(servletHolder, "/*");

        server.setHandler(jerseyHandler);
        server.start();
        server.join();
    }

}
