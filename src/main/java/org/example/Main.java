package org.example;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.HandlerList;
import org.eclipse.jetty.server.handler.ResourceHandler;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.util.resource.Resource;
import org.eclipse.jetty.websocket.server.NativeWebSocketServletContainerInitializer;

import java.awt.*;
import java.io.File;
import java.net.URI;
import java.util.Objects;

public class Main {
    public static void main(String[] args) throws Exception {
        int port = 7000;
        Server server = new Server(port);

        // Load static files
        ResourceHandler resourceHandler = new ResourceHandler();

        Resource rootWebResource;
        if (Objects.requireNonNull(Main.class.getResource("Main.class")).toString().startsWith("jar:")) {
            rootWebResource = Resource.newResource("jar:" + Main.class.getProtectionDomain().getCodeSource().getLocation().toURI() + "!/webapp");
        } else {
            rootWebResource = Resource.newResource((new File(System.getProperty("user.dir"), "/src/main/webapp")).getPath());
        }
        resourceHandler.setBaseResource(rootWebResource);

        // Create a WebSocket context
        ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
        context.setContextPath("/");

        // Initialize WebSocket container
        NativeWebSocketServletContainerInitializer.configure(context, null);

        // Add your custom WebSocket endpoint with a custom path
        context.addServlet(MessageWebSocket.class, "/socket");

        // Configure the order of handlers
        HandlerList handlers = new HandlerList();
        handlers.addHandler(resourceHandler);
        handlers.addHandler(context);

        server.setHandler(handlers);

        server.start();
        System.out.println("WebSocket server started on port " + port);

        // Automatically open browser
        if (Desktop.isDesktopSupported() && Desktop.getDesktop().isSupported(Desktop.Action.BROWSE)) {
            Desktop.getDesktop().browse(new URI("http://localhost:" + port));
            System.out.printf("Opening http://localhost:" + port + "%n");
        }

        server.join();
    }
}