/*
 *  @author Tacet Nox
 *
 * Intended to launch both the Twitch and Discord bots for Tritemare
 */

import Commands.*;
import Util.GetLogin0Auth;
import Util.GetLoginOwner;
import Util.SQLConnect;
import com.jagrosh.jdautilities.command.CommandClientBuilder;
import com.jagrosh.jdautilities.commons.waiter.EventWaiter;
import com.jagrosh.jdautilities.examples.command.AboutCommand;
import com.jagrosh.jdautilities.examples.command.PingCommand;
import com.jagrosh.jdautilities.examples.command.ShutdownCommand;
import net.dv8tion.jda.api.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.*;
import java.sql.Connection;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class Notitia {
        /*
         * JDA Implementation via me. Log/thread pool first, then launching bot in the generated thread.
         */

        //https://discordapp.com/oauth2/authorize?&client_id=185442980805476352&scope=bot&permissions=0
        //Use that link to invite the bot to any channel i have server manage on
        private static Logger log = LoggerFactory.getLogger(Notitia.class);
        /*
        Probably not necessary, but set rotating threads for reboot. Will basically switch back and forth between thread 1
        and thread 2 as the reboot command is used.
         */
        private static final ExecutorService pool = Executors.newFixedThreadPool(2);

        public static void main( String[] args ) {

                ////Currently just using the console, will look into something akin to JFrame to make a little clickable button
                ////or something in order to execute the commands.
                log.info("BEGIN BOT LOADING, INITIALIZING THREAD POOL");
                ThreadMe bot = new ThreadMe();
                newBot(bot);
        }

        //Start up the new thread
        private static void newBot(ThreadMe instance){
                pool.execute(instance);
        }


}

//Class to actually thread out bot building, launches bot connections after being added to thread pool in main
class ThreadMe implements Runnable{

        private static Logger log = LoggerFactory.getLogger(ThreadMe.class);
        private JDA jda = null;

        public void run(){
                try{

                        Thread.sleep(TimeUnit.MILLISECONDS.toSeconds(2));
                        log.info("~~ Acquiring properties ~~");
                        Connection conn = new SQLConnect().connect();
                        Thread.sleep(TimeUnit.MILLISECONDS.toSeconds(2));

                        log.info("~~ Assigning token ~~");
                        String token = new GetLogin0Auth().getAuth(conn);
                        Thread.sleep(TimeUnit.MILLISECONDS.toSeconds(2));

                        log.info("~~ Assigning owner ~~");
                        String owner = new GetLoginOwner().getOwner(conn);
                        Thread.sleep(TimeUnit.MILLISECONDS.toSeconds(2));
            /*
                Testing JDA utils
            */

                        log.info("~~ Setting up JDA client ~~");
                        Thread.sleep(TimeUnit.MILLISECONDS.toSeconds(2));
                        EventWaiter waiter = new EventWaiter();
                        CommandClientBuilder client = new CommandClientBuilder();

                        client.setOwnerId(owner); //Owner currently listed as me

                        //Setting emojis for bot reaction responses
                        client.setEmojis("\uD83D\uDE03", "\uD83D\uDE2E", "\uD83D\uDE26");
                        //Assigning prefix
                        client.setPrefix("!!");
                        Thread.sleep(TimeUnit.MILLISECONDS.toSeconds(2));
                        log.info("~~ Adding commands ~~");
                        client.addCommands(
                                new AboutCommand(Color.BLUE, "an experiment in being useful in providing information and being a testing ground for new ideas!",
                                        new String[]{"See the Length of Time You've Been Subscribed", "Trite's Schedule Information", "Generate a Strawpoll"},
                                        new Permission[]{Permission.ADMINISTRATOR}),
                                new ChooseCommand(),
                                new HelloCommand(waiter),
                                new PingCommand(),
                                new ShutdownCommand(),
                                new ScheduleCommand(),
                                new MinecraftInfoCommand(),
                                new StrawpollCommand(),
                                new StatsCmd(),
                                new SubNameCommand(),
                                new SubLengthCommand());


                        log.info("~~ Building bot async");
                        jda = new JDABuilder(AccountType.BOT)
                                .setToken(token)

                                //Status while loading
                                .setStatus(OnlineStatus.DO_NOT_DISTURB)


                                .addEventListeners(new NotitiaListener(), waiter, client.build())
                                .build();

                        conn.close();
                        log.info(jda.getAccountType() + " --- BUILDING JDA INSTANCE COMPLETE");

                }catch(Exception e){
                        log.warn(e.getMessage());
                }
        }
}
