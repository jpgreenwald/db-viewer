package com.swsandbox;

import com.swsandbox.db.DbService;
import com.swsandbox.json.JsonUtil;
import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.server.handler.ContextHandler;
import org.eclipse.jetty.server.handler.ContextHandlerCollection;
import org.eclipse.jetty.server.handler.ResourceHandler;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.util.resource.Resource;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * User: jgreenwald
 * Date: 8/10/13
 * Time: 9:04 PM
 */
public class EntryPoint extends HttpServlet
{
    public static void main(String[] args) throws Exception
    {
        Server server = new Server();
        ServerConnector http = new ServerConnector(server);
        http.setPort(args.length > 0 ? Integer.parseInt(args[0]) : 4000);
        http.setIdleTimeout(3000);
        server.addConnector(http);

        ContextHandler resourceContext = new ContextHandler();
        resourceContext.setContextPath("/");
        ResourceHandler resourceHandler = new ResourceHandler();
        resourceHandler.setBaseResource(Resource.newResource("./web"));
        resourceHandler.setDirectoriesListed(true);
        resourceContext.setHandler(resourceHandler);

        ServletContextHandler sh = new ServletContextHandler();
        sh.addServlet(EntryPoint.class, "/api/*");

        ContextHandlerCollection contexts = new ContextHandlerCollection();
        contexts.setHandlers(new Handler[]{resourceContext, sh});
        server.setHandler(contexts);
        server.start();
        server.join();
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException
    {
        InputStream is = req.getInputStream();
        byte[] buf = new byte[4096];
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        while (is.read(buf) != -1)
        {
            outputStream.write(buf);
        }
        String msg = outputStream.toString().trim();
        outputStream.close();
        is.close();

        resp.setContentType("application/json");
        resp.getWriter().write(JsonUtil.prettyPrint(DbService.execute(msg)));
        resp.getWriter().flush();
        resp.getWriter().close();
    }
}
