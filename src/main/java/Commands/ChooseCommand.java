package Commands;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import org.slf4j.LoggerFactory;

import java.util.Random;

public class ChooseCommand extends Command
{

    private static final org.slf4j.Logger log = LoggerFactory.getLogger(ChooseCommand.class);
    Random r = new Random();

    public ChooseCommand()
    {
        this.name = "choose";
        this.help = "make a decision";
        this.arguments = "<item> <item> ...";
        this.guildOnly = false;
    }

    @Override
    protected void execute(CommandEvent event)
    {
        System.out.println("~ Choose command used ~");

        // check that the user provided choices
        if(event.getArgs().isEmpty())
        {
            event.replyWarning("You didn't give me any choices!");
        }
        else
        {

            // split the choices on all whitespace
            String[] items = event.getArgs().split("\\s+");

            // if there is only one option, have a special reply
            if(items.length==1)
                event.replyWarning("You only gave me one option, `"+items[0]+"`");

                // otherwise, pick a random response
            else
            {
                event.replySuccess("I choose `"+items[r.nextInt(items.length)]+"`");
            }
        }
    }

}