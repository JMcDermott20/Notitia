package Commands.TwitchSubs;

import Util.SQLConnect;
import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class AddKnightName extends Command {

    private static final org.slf4j.Logger log = LoggerFactory.getLogger(AddKnightName.class);
    SQLConnect connection = new SQLConnect();

    public AddKnightName() {

        this.name = "addname";
        this.help = "Make sure you follow the format properly. !!addname subname=knightname";
        this.aliases = new String[]{"name", "subname"};
        this.arguments = " <user>= <knighted name> ";
        this.guildOnly = false;
        this.category = new Category("User Commands");

    }

    @Override
    protected void execute(CommandEvent event) {
        System.out.println("~ Sub name command used ~");
        // check that the user provided choices
        if (event.getArgs().isEmpty()) {
            event.getMessage().addReaction("\u274C").queue();
            event.replyWarning("You need to provide a username=knightname combo!");
        } else {
            event.getMessage().addReaction("\u2705").queue();
            // split the choices on all whitespace
            String[] items = event.getArgs().split("=");

            try {
                Connection conn = new SQLConnect().connect();
                String subname = items[0];
                String knightname = items[1];

                addName(subname, knightname, conn);

                conn.close();
            }catch(SQLException e){
                log.error(e.getMessage());
            }
        }
    }

    //Get the amount of months a user has been subbed
    public String addName(String sub, String knight, Connection conn) {
        try {
            //System.out.println(username);
            Statement stmt = connection.connect().createStatement();
            ResultSet rs = stmt.executeQuery("Select subname FROM subs WHERE username");
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
