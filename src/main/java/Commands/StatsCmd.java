package Commands;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import org.slf4j.LoggerFactory;

import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;

/**
 *
 * @author John Grosh (jagrosh)
 */
public class StatsCmd extends Command {

    private static final org.slf4j.Logger log = LoggerFactory.getLogger(ChooseCommand.class);
    private final OffsetDateTime start = OffsetDateTime.now();
    public StatsCmd()
    {
        this.name = "stats";
        this.help = "shows some statistics on the bot";
        this.ownerCommand = true;
        this.guildOnly = false;
        this.category = new Category("User Commands");

    }

    @Override
    protected void execute(CommandEvent event) {
        System.out.println("~ Stats Command Used ~");
        long totalMb = Runtime.getRuntime().totalMemory()/(1024*1024);
        long usedMb = (Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory())/(1024*1024);
        event.reply("**"+event.getSelfUser().getName()+"** statistics:"
                + "\nLast Startup: "+start.format(DateTimeFormatter.RFC_1123_DATE_TIME)
                + "\nGuilds: "+event.getJDA().getGuilds().size()
                + "\nMemory: "+usedMb+"Mb / "+totalMb+"Mb"
                + "\nResponse Total: "+event.getJDA().getResponseTotal());
    }

}