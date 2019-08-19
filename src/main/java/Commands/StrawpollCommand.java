package Commands;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import com.samuelmaddock.strawpollwrapper.*;
import com.samuelmaddock.strawpollwrapper.StrawPoll;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class StrawpollCommand extends Command {

    private static final org.slf4j.Logger log = LoggerFactory.getLogger(StrawpollCommand.class);

    public StrawpollCommand() {

        this.name = "strawpoll";
        this.help = "generates a strawpoll with the given options";
        this.arguments = " <option 1> <option 2> etc...";
        this.guildOnly = false;
    }

    @Override
    protected void execute(CommandEvent event) {

        if (event.getArgs().isEmpty()) {
            event.getMessage().addReaction("\u274C").queue();
            event.replyWarning("You need to provide me a user to look for!");
        } else {
            event.getMessage().addReaction("\u2705").queue();
            // split the choices on all whitespace
            List<String> options = new ArrayList<>();
            String[] tempOptions = event.getArgs().split("\\s+");

            options.addAll(Arrays.asList(tempOptions));

            for(String x : options) {
                log.info(x);
            }

            StrawPoll newPoll = new StrawPoll("Choose an option!", options);
            newPoll.create();
            event.reply("The poll is now available at: " + newPoll.getPollURL());
        }
    }
}