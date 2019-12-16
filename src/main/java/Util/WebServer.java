package Util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.ServerSocket;

public class WebServer {
    private static Logger out = LoggerFactory.getLogger(WebServer.class);
    private int port;
    public void WebServer(int portNumber){
        out.info("Assigning provided port: " +portNumber);
        this.port=portNumber;
        launchServer();
    }

    public void WebServer(){
        out.info("Assigning default port as none was provided.");
        this.port=4568;
        launchServer();
    }

    private void launchServer(){
        try(ServerSocket serverSocket = new ServerSocket(port)) {
            while(true){
                out.info("Awaiting connection");
                new ServerThreads(serverSocket.accept()).start();
            }
        } catch (IOException e){
            out.debug("Oops: " + e.getMessage());
        }
    }

}
