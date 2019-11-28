package Commands.General;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import com.jagrosh.jdautilities.commons.waiter.EventWaiter;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.util.concurrent.TimeUnit;

/**
 *
 * @author John Grosh (john.a.grosh@gmail.com)
 */
public class BotCount extends Command
{
    private final EventWaiter waiter;
    public BotCount(EventWaiter waiter)
    {
        this.waiter = waiter;
        this.name = "bots";
        this.help = "Counts amount of bots this bot can see";
        this.category = new Category("Sample Commands");

    }


    @Override
    protected void execute(CommandEvent event)
    {
        int bots = 0;
        int notBots = 0;

        JDA jda = event.getJDA();
        for(User user : jda.getUsers()){
            if(user.isBot()){
                //System.out.println(user.getName() + " is a BOT");
                bots++;
            }else if (!user.isBot()){
               //System.out.println(user.getName() + " is NOT a BOT");
                notBots++;
            }
        }
        event.reply("There are currently " + bots + " BOTS and currently " + notBots + " USERS visible to me.");

    }

}