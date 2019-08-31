import net.dv8tion.jda.api.entities.Emote;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nonnull;

public class TestListener extends ListenerAdapter {

    final static Logger log = LoggerFactory.getLogger(TestListener.class);

    //Would rather use general Message Received Event as it captures DM's as well as regular messages.
    @Override
    public void onMessageReceived(MessageReceivedEvent event){
        //log messages to console for testing purposes
        log.info("\ngetmessage.contentDisplay\n"+event.getAuthor().getName() + ": " + event.getMessage().getContentDisplay()+"\nn\n");

        String user = event.getAuthor().getName();
        String message = event.getMessage().getContentRaw();

        log.info("\ngetmessage.contentRaw\n"+user + ": " + message + "\n\n");

        if(user.equalsIgnoreCase("Tacet Nox")){
            event.getMessage().addReaction("U+1F643").queue();
        }
    }
}
