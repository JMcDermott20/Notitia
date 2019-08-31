/**
 *  @author Tacet Nox
 *
 * Intended to launch Discord bot for Tritemare
 */

import Util.SQLConnect;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.ReadyEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;


public class NotitiaListener extends ListenerAdapter {

    final static Logger log = LoggerFactory.getLogger(NotitiaListener.class);
    //variables for isLive function -- Possibly not all needed? Will revise

    /*private static int isRun = 0;
    static int isLive = 0;
    static String justGame;
    static String channel;
    static String title;*/
    private static TextChannel announcements;

    //private List<Member> users;
    private static Date time;
    private JDA jda = null;
    private static HashMap<String, TextChannel> channelMap = new HashMap<>();

    //Init DB connection capabilities
    SQLConnect connection = new SQLConnect();



    public void onReady(ReadyEvent event){
        log.info("Now logged in as " + event.getJDA().getAccountType().toString() + " -- " + event.getJDA().getSelfUser().getName());
        jda = event.getJDA();
        jda.getPresence().setActivity(Activity.watching("Clouds float by..."));
    }

    //listener for messages received. Passes them off to handler after setting the announcement channel
    public void onMessageReceived(MessageReceivedEvent event) {

        //If from self, ignore. Could be spammed with another bot but I can deal with that if the need arises.
        if (event.getAuthor().getId() == "194179563499159552") return;

        /*//Building list of channels
         if (channelMap.isEmpty()) {
           Iterator<TextChannel> x = jda.getGuilds().get(0).getManager().getGuild().getTextChannels().iterator();
          while (x.hasNext()) {
               TextChannel chan = x.next();
               String name = chan.getName();
                //System.out.println(chan.toString());
                //System.out.println(name);
                       channelMap.put(name, chan);
            }
            announcements = channelMap.get("announcements");
            //LOG.log(Level.INFO, "Channel Set!");
        }*/

        //TODO: Possibly make a separate call for responding privately to command calls, see below commented section
        // for how to open user specific pm's

        //Handling PM's
        if (event.isFromType(ChannelType.PRIVATE)) {

            //Commands respond to the channel they are received from, so commands created and added that way
            //are handled automatically. Will only need to use this in the event of someone sending something other
            //than a command for the bot in a PM. Possibly just a quick response and a flag to reply saying to message
            //me directly if they need assistance with something. Can also log PM's if anyone is trying to screw with the bot

            /*
            PrivateChannel privateChannel = event.getPrivateChannel();

            Message message;
            MessageEmbed test;
            EmbedBuilder embed = new EmbedBuilder();
            test = (embed
                    .addField("Whoa dude", "This is a short test of PM's.", true)
                    .setColor(Color.MAGENTA)
                    .build());
            MessageBuilder last = new MessageBuilder();
            message = last.setEmbed(test).build();

            privateChannel.sendMessage(message).queue();*/

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
        //String mention = event.getAuthor().getAsMention();
        User user = event.getAuthor();
        String guild = event.getGuild().getName();

        //users = guild.getMembers();

        //LOG.info(users.size() + "");

		/*
		user.openPrivateChannel().queue(success -> {
			user.getPrivateChannel().sendMessage("Hey").queue();
		});
		*/

        time = new Timestamp(Calendar.getInstance().getTime().getTime());

        log.info(time+" : " + guild+" : "+channel.getName()+" : "+user.getName()+" - "+message);

        /*
        THE FOLLOWING IS A BETTER WAY OF MANAGING API CALLS to Twitch, shows how to set CLIENT-ID and OAUTH Headers
         */
        if(message.equalsIgnoreCase("!test")){
            HttpURLConnection conn = null;
            try{
                URL url = new URL("https://api.twitch.tv/helix/videos?user_id=64207063");
                conn=(HttpURLConnection)url.openConnection();
                conn.setDoInput(true);
                conn.setDoOutput(true);
                conn.setRequestMethod("GET");
                conn.setRequestProperty("Client-ID", "p6f433iqeua2a3282ghwpxcd3wl4yan");
                conn.setRequestProperty("Authorization", "OAuth 3wgs5fs9vp3nsd8bxkvb15qdwdqb98");

                BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                String inLine;
                while((inLine=in.readLine())!=null){
                    System.out.println(inLine+"\n");
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
                conn.disconnect();
            }
        } else if(message.equalsIgnoreCase("!ping")){
            jda.getRestPing().queue(
                    (ping) -> {
                        channel.sendMessageFormat(
                                "Ping\n"
                                        + "HTTP: %d ms\n"
                                        + "WS: %d ms",
                                ping, jda.getGatewayPing()).queue();
                    }
            );
        }
        //TODO Example of adding unicode reaction to message

        /*
        https://www.unicode.org/emoji/charts/full-emoji-list.html

        URL of unicode list of emojis


         */

        //event.getMessage().addReaction("\uD83D\uDCA5").queue();

        /*if(message.startsWith("!")) {
            String commandVar = "";
            if(message.contains(" ")){
                if((message.split(" ", 2)[1]) == null){
                    message = message.split(" ")[0];
                }else {
                    commandVar = message.split(" ", 2)[1];
                    message = message.split(" ")[0];
                }
            }

            }*/
        }
}