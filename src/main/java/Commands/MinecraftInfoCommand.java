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
                .addField("*Connection Info:*", "" +
                        "The IP address is: bandy.theyak.center:25565" +
                        "\nThe current modpack is Direwolf20 Version 1.10, located on the Feed the Beast launcher." +
                        "\nNOTE: We have altered it slightly, you will need to download the new version of the Numina mod: "
                        + "http://minecraft.curseforge.com/projects/numina/files/2256264/download" +
                        "\nOnce you download the new version from the above link, go to your FTB folder. Under direwolf20_17 / minecraft / mods, "
                        + "locate the OLD version of Numina and remove it(may want to save for later" +
                        "\nPlace the newly downloaded version in its place and you should be able to connect just fine!" +
                        "\n", false).setColor(Color.GREEN)
                .build());
        MessageBuilder last = new MessageBuilder();
        message = last.setEmbed(test).build();

        //Send it off
        event.reply(message);

    }

}