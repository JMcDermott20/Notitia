package Commands;

import Util.SQLConnect;
import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import org.slf4j.LoggerFactory;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class SubNameCommand extends Command {

    private static final org.slf4j.Logger log = LoggerFactory.getLogger(SubLengthCommand.class);
    SQLConnect connection = new SQLConnect();

    public SubNameCommand() {

        this.name = "myname";
        this.help = "retrieves amount of months a user has been subscribed";
        this.aliases = new String[]{"name", "subname"};
        this.arguments = " <user> ";
        this.guildOnly = false;
    }

    @Override
    protected void execute(CommandEvent event) {
        System.out.println("~ Sub name command used ~");
        // check that the user provided choices
        if (event.getArgs().isEmpty()) {
            event.getMessage().addReaction("\u274C").queue();
            event.replyWarning("You need to provide me a user to look for!");
        } else {
            event.getMessage().addReaction("\u2705").queue();
            // split the choices on all whitespace
            String[] items = event.getArgs().split("\\s+");

            String subName = getMonths(items[0]);

            event.reply(items[0] + "'s Knightly name is " + subName);

        }
    }

    //Get the amount of months a user has been subbed
    public String getMonths(String username) {
        try {
            //System.out.println(username);
            Statement stmt = connection.connect().createStatement();
            ResultSet rs = stmt.executeQuery("Select subname FROM subs WHERE username = '" + username + "'");
            while (rs.next()) {
                String knightName = rs.getString("subname");
                log.info(knightName);

                if (knightName!=null) {
                    connection.disconnect();
                    rs.close();
                    return knightName;
                } else {
                    connection.disconnect();
                    rs.close();
                    return "Something went wrong, let Tacet Nox know what you did to get this message!";
                }
            }
        } catch (SQLException e) {
            connection.disconnect();
            return "Something went wrong, let Tacet Nox know what you did to get this message!";
        } finally {
            connection.disconnect();

        }
        return "No name was found for that user!";
    }

}
