import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nonnull;

public class TestListener extends ListenerAdapter {

    final static Logger log = LoggerFactory.getLogger(TestListener.class);


    @Override
    public void onGuildMessageReceived(@Nonnull GuildMessageReceivedEvent event) {

        log.debug(event.getAuthor() + ": " + event.getMessage());

    }
}
