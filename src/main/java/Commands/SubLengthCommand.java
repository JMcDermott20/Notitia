package Commands;

import Util.SQLConnect;
import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import org.slf4j.LoggerFactory;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;


public class SubLengthCommand extends Command {

    private static final org.slf4j.Logger log = LoggerFactory.getLogger(SubLengthCommand.class);
    SQLConnect connection = new SQLConnect();

    public SubLengthCommand() {

        this.name = "subbed";
        this.help = "retrieves amount of months a user has been subscribed";
        this.arguments = " <user> ";
        this.guildOnly = false;
        this.category = new Category("User Commands");

    }

    @Override
    protected void execute(CommandEvent event) {
        System.out.println("~ Sub length command used ~");
        // check that the user provided choices
        if (event.getArgs().isEmpty()) {
            event.getMessage().addReaction("\u274C").queue();
            event.replyWarning("You need to provide me a user to look for!");
        } else {
            event.getMessage().addReaction("\u2705").queue();
            // split the choices on all whitespace
            String[] items = event.getArgs().split("\\s+");

            int months = getMonths(items[0]);

            if(months >= 0) {
                event.replySuccess(items[0] + " has been subbed for " + months + " months");
            }else if(months<0){
                event.replyWarning(" That user wasn't found, did you spell their name correctly?");

            }
        }
    }

    //Get the amount of months a user has been subbed
    public Integer getMonths(String username) {
        try {
            //System.out.println(username);
            Statement stmt = connection.connect().createStatement();
            ResultSet rs = stmt.executeQuery("Select months_subbed FROM subs WHERE username = '" + username + "'");
            while (rs.next()) {
                Integer months = rs.getInt("months_subbed");
                //System.out.println(months);
                if (months > -1) {
                    connection.disconnect();
                    rs.close();
                    return months;
                } else {
                    connection.disconnect();
                    rs.close();
                    return 999;
                }
            }
        } catch (SQLException e) {

            connection.disconnect();
            return 999;
        } finally {
            connection.disconnect();
        }
        //If it gets to here, I'm really not sure what the hell happened
        return -1;
    }
}