package Util;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URL;
import java.util.Date;

public class ServerThreads extends Thread {
    private Socket socket;
    private static Logger out = LoggerFactory.getLogger(ServerThreads.class);

    public ServerThreads(Socket socket){
        this.socket=socket;
    }

    @Override
    public void run() {
        out.info("Connection received");
        try{
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            String line;
            line = in.readLine();
            StringBuilder raw = new StringBuilder();
            raw.append("" + line);
            boolean isPost = line.startsWith("POST");
            int contentLength = 0;
            while (!(line = in.readLine()).equals("")) {
                raw.append('\n' + line);
                if (isPost) {
                    final String contentHeader = "Content-Length: ";
                    if (line.startsWith(contentHeader)) {
                        contentLength = Integer.parseInt(line.substring(contentHeader.length()));
                    }
                }
            }
            StringBuilder body = new StringBuilder();
            if (isPost) {
                int c = 0;
                for (int i = 0; i < contentLength; i++) {
                    c = in.read();
                    body.append((char) c);
                }
            }
            out.info(body.toString());


                out.info("Here");
                JsonParser parser = new JsonParser();
                out.info("parser creation");
                JsonArray data = parser.parse(body.toString())
                        .getAsJsonObject().getAsJsonArray("data");

                String fromUser="", toUser = "";

                out.info("Looking for json values");
                out.info(data.toString());
                for(JsonElement key : data){
                    out.info(key.toString());
                    out.info(key.getAsJsonObject().get("from_name").toString());
                    out.info(key.getAsJsonObject().get("to_name").toString());
                }

                BufferedWriter respond = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            respond.write("HTTP/1.1 200 OK\r\n");
            respond.write("Content-Type: text/html\r\n");
            respond.write("\r\n");
            respond.write(new Date().toString());
            respond.close();

            socket.close();
            out.info("CLOSING SOCKET, TRANSACTION COMPLETE");

        } catch (IOException e){
            e.getMessage();
        }
    }
}
