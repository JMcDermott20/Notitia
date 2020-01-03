/**
 *  @author Tacet Nox
 *
 * Intended to launch Discord bot for Tritemare
 */

import Util.SQLConnect;
import com.google.gson.JsonObject;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.MessageBuilder;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.ReadyEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.json.JSONArray;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;


public class NotitiaListener extends ListenerAdapter {

    private final static Logger log = LoggerFactory.getLogger(NotitiaListener.class);
    //variables for isLive function -- Possibly not all needed? Will revise
    /*private static int isRun = 0;
    static int isLive = 0;
    static String justGame;
    static String channel;
    static String title;*/
    private static TextChannel announcements;

    private JDA jda = null;
    private static HashMap<String, TextChannel> channelMap = new HashMap<>();

    //Init DB connection capabilities
    SQLConnect connection = new SQLConnect();



    public void onReady(ReadyEvent event){
        log.info("Now logged in as " + event.getJDA().getAccountType().toString() + " -- " + event.getJDA().getSelfUser().getName());
        jda = event.getJDA();
        jda.getPresence().setActivity(Activity.listening("for Commands | !!help"));
    }

    //listener for messages received. Passes them off to handler after setting the announcement channel
    public void onMessageReceived(MessageReceivedEvent event) {

        //If from self, ignore. Could be spammed with another bot but I can deal with that if the need arises.
        if (event.getAuthor().getId().equals("194179563499159552")) return;


        //Handling PM's
        if (event.isFromType(ChannelType.PRIVATE)) {
            //Commands respond to the channel they are received from, so commands run in a DM respond in DM
            //Will only need to use this in the event of someone sending something other
            //than a command for the bot in a PM. Possibly just a quick response and a flag to reply saying to message
            //me directly if they need assistance with something. Can also log PM's if anyone is trying to screw with the bot
            if(event.getMessage().getContentRaw().startsWith("!!")){

            }else {
                PrivateChannel privateChannel = event.getPrivateChannel();

                Message message;
                MessageEmbed test;
                EmbedBuilder embed = new EmbedBuilder();
                test = (embed
                        .addField("Whoa dude", "I appreciate the closeness, but only commands work here." +
                                "\nIf you need help with the bot that the !!help command doesn't answer, message or tag " +
                                "Tacet Nox", false)
                        .setColor(Color.MAGENTA)
                        .build());
                MessageBuilder last = new MessageBuilder();
                message = last.setEmbed(test).build();

                privateChannel.sendMessage(message).queue();
            }

        } else {

            //Essentially since someone is opening the channel already, it responds directly. If I needed to send info
            //privately to someone using a command in a regular channel, I'd need to open one as done in one of the commands.
            handleMessage(event);
        }
    }

    //basic message handling with calls for commands
    private void handleMessage(MessageReceivedEvent event){

        MessageChannel channel = event.getChannel();
        String message = event.getMessage().getContentRaw();

        User user = event.getAuthor();
        String guild = event.getGuild().getName();

        Date time = new Timestamp(Calendar.getInstance().getTime().getTime());

        log.info(time +" : " + guild+" : "+channel.getName()+" : "+user.getName()+" - "+message);

        /*
        THE FOLLOWING IS A BETTER WAY OF MANAGING API CALLS to Twitch, shows how to set CLIENT-ID and OAUTH Headers
        TODO - Determine better way using OKAYHTTP, should be even easier.
         */

        if(message.equalsIgnoreCase("!getID")){
            HttpURLConnection conn = null;
            try{
                URL url = new URL("https://api.twitch.tv/helix/users?login=tacetnoxpavor");
                conn=(HttpURLConnection)url.openConnection();
                conn.setDoInput(true);
                conn.setDoOutput(true);
                conn.setRequestMethod("GET");
                conn.setRequestProperty("Client-ID", "p6f433iqeua2a3282ghwpxcd3wl4yan");
                conn.setRequestProperty("Authorization", "Bearer 3wgs5fs9vp3nsd8bxkvb15qdwdqb98");

                BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                String inLine;
                while((inLine=in.readLine())!=null){
                    channel.sendMessage(inLine).queue();
                }
                in.close();
                int rc = conn.getResponseCode();
                System.out.print("Response Code = "+rc+"\n");
                String rm=conn.getResponseMessage();
                System.out.print("Response Message = "+rm+"\n");
                System.out.println(conn.getHeaderFields().toString());
                event.getMessage().addReaction("\u2705").queue();

            }catch (Exception e){
                event.getMessage().addReaction("\u274C").queue();
                e.printStackTrace();
            }finally{
                assert conn != null;
                conn.disconnect();
            }
        }


        if(message.equalsIgnoreCase("!active")){
            jda.getPresence().setActivity(Activity.playing("with loads of new people ;)"));
        }

        if(message.equalsIgnoreCase("!ping")){
            event.getMessage().addReaction("\uD83D\uDC4C").queue();
            //noinspection CodeBlock2Expr
            jda.getRestPing().queue(
                    (ping) -> {
                        channel.sendMessageFormat(
                                "Pong\n"
                                        + "HTTP: %d ms\n"
                                        + "WS: %d ms",
                                ping, jda.getGatewayPing()).queue();
                    }
            );
        }


        }
}