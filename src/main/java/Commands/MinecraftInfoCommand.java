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
public class MinecraftInfoCommand extends Command
{

    private static final org.slf4j.Logger log = LoggerFactory.getLogger(MinecraftInfoCommand.class);

    public MinecraftInfoCommand()
    {
        this.name = "connect";
        this.aliases = new String[]{"mine", "minecraft", "server"};
        this.help = "Displays connection info for our Minecraft Server";
        this.category = new Category("User Commands");

    }

    /**
     * TODO: Maybe try playing with the formatting
     * @param event
     */
    @Override
    protected void execute(CommandEvent event)
    {
        log.info("~ Minecraft Command Used ~");
        Message message;
        MessageEmbed test;
        EmbedBuilder embed = new EmbedBuilder();
        test = (embed
                .addField("Thanks for your interest in the server!", "Please read the following information for connecting.", true)
                .addField("*Connection Info:*", "\n" +
                        "The server is currently down, apologies! If there is enough interest to get " +
                        "\na server running again, this command will be updated with the info!", false).setColor(Color.GREEN)
                .build());
        MessageBuilder last = new MessageBuilder();
        message = last.setEmbed(test).build();

        //Send it off
        event.reply(message);

    }

}
