import net.dv8tion.jda.api.entities.Emote;
import net.dv8tion.jda.api.events.Event;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nonnull;
import java.util.ArrayList;

public class TestListener extends ListenerAdapter {

    final static Logger log = LoggerFactory.getLogger(TestListener.class);


    int isChatting = 0;
    MessageReceivedEvent[] phoneLine = new MessageReceivedEvent[2];

    //Would rather use general Message Received Event as it captures DM's as well as regular messages.
    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        //log messages to console for testing purposes
        log.info("\ngetmessage.contentDisplay\n" + event.getAuthor().getName() + ": " + event.getMessage().getContentDisplay() + "\nn\n");

        if(event.getAuthor().isBot())
        {
            return;
        }



        String user = event.getAuthor().getName();
        String message = event.getMessage().getContentRaw();

        log.info("\ngetmessage.contentRaw\n" + user + ": " + message + "\n\n");

        if(message.startsWith("!!chat")) {
            if(isChatting == 0){
                log.info("Line 1");
                isChatting =1;
                phoneLine[0]=event;
                log.info(phoneLine[0].getChannel().getName());
            }else if(isChatting == 1){
                log.info("Line 2");
                isChatting = 2;
                phoneLine[1] = event;
                log.info(phoneLine[1].getChannel().getName());
            }else if(isChatting == 2){
                log.info("Resetting");
                phoneLine = new MessageReceivedEvent[2];
                isChatting = 0;
            }
        }
        if(isChatting==2){

            if(phoneLine[0].getChannel().getName().equalsIgnoreCase(event.getChannel().getName())) {
                log.info("sending to second line");
                phoneLine[1].getChannel().sendMessage(event.getAuthor().getName() + " " +
                        "::" + event.getMessage().getContentRaw()).queue();
            }else if(phoneLine[1].getChannel().getName().equalsIgnoreCase(event.getChannel().getName())){
                log.info("sending to first line");
                phoneLine[0].getChannel().sendMessage(event.getAuthor().getName() + " " +
                        "::" + event.getMessage().getContentRaw()).queue();
            }
        }




    }
}
