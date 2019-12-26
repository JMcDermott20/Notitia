package Commands.General;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import net.dv8tion.jda.api.entities.Activity;


public class ActivityUpdate extends Command
{

    public ActivityUpdate()
    {
        this.name = "active";
        this.arguments = "<New Activity String>";
        this.help = "Updates bot activity message";
        this.category = new Category("Mod Commands");
        this.requiredRole = "MODERATOR";
    }


    @Override
    protected void execute(CommandEvent event)
    {
        String input = event.getArgs();
        event.getChannel().sendMessage("Activity Updating shortly...").queue();
        try {
            Thread.sleep(4000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        event.getJDA().getPresence().setActivity(Activity.playing(input));
    }

}