package Commands;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.MessageBuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageEmbed;
import org.slf4j.LoggerFactory;

import java.awt.*;

/**
 *
 * @author John Grosh (john.a.grosh@gmail.com)
 */
public class ScheduleCommand extends Command
{

    private static final org.slf4j.Logger log = LoggerFactory.getLogger(ScheduleCommand.class);

    public ScheduleCommand()
    {
        this.name = "schedule";
        this.aliases = new String[]{"sched"};
        this.help = "Displays Tritemare's current streaming schedule";
    }

    @Override
    protected void execute(CommandEvent event)
    {
        System.out.println("~ Schedule Command Used ~");
        Message message;
        MessageEmbed test;
        EmbedBuilder embed = new EmbedBuilder();
        test = (embed
                .addField("*Tritemare's typical schedule is as follows:*", "" +
                        "Monday and Tuesday --> OFF " +
                        "\nWednesday - Friday --> 7 PM - 11 PM  EST" +
                        "\nSaturday - Sunday --> 2 PM - 8 PM EST" +
                        "\n", false).setColor(Color.GREEN)
                .addField("Please note that all times are subject to change.", "(Real life does occur unfortunately for all of us)", false)
                .build());
        message = new MessageBuilder().setEmbed(test).build();
        event.reply(message);

    }

}