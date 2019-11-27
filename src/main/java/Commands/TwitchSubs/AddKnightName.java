package Commands.TwitchSubs;

import Util.SQLConnect;
import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import org.slf4j.LoggerFactory;

import java.sql.*;

public class AddKnightName extends Command {

    private static final org.slf4j.Logger log = LoggerFactory.getLogger(AddKnightName.class);
    SQLConnect connection = new SQLConnect();

    public AddKnightName() {

        this.name = "addname";
        this.help = "Make sure you follow the format properly. !!addname subname=knightname";
        this.arguments = " <user>=<knighted name> ";
        this.guildOnly = false;
        this.category = new Category("Mod Commands");
        this.requiredRole = "MODERATOR";

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

                String subname = items[0];
                String knightname = items[1];

                addName(subname, knightname);

            }catch(Exception e){
                log.error(e.getMessage());
            }
        }
    }

    private void addName(String sub, String knight) {
        try {
            sub = sub.replaceAll("[^a-zA-Z0-9 ]", "");
            knight = knight.replaceAll("[^a-zA-Z0-9 ]", "");

            String sql = "INSERT INTO `subs` (`id`,`username`,`subname`,`months_subbed`) " +
                    "VALUES (NULL, '" + sub + "', '" + knight + "', 1)";
            PreparedStatement stmnt = connection.connect().prepareStatement(sql);
            stmnt.execute();

        } catch (SQLException e) {
            connection.disconnect();
        }
    }

}
