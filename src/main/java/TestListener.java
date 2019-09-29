import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;

public class TestListener extends ListenerAdapter {

    private final static Logger log = LoggerFactory.getLogger(TestListener.class);


    private int isChatting = 0;
    private MessageReceivedEvent[] phoneLine = new MessageReceivedEvent[2];
    private ArrayList<String> phoneChannels = new ArrayList<>();

    //Would rather use general Message Received Event as it captures DM's as well as regular messages.
    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        //Ignore bot messages, could cause an infinite loop if the bot somehow says something that triggers itself
        if(event.getAuthor().isBot()) return;


        //Grabbing user and message for easier interaction
        String user = event.getAuthor().getName();
        String message = event.getMessage().getContentRaw();


        //log messages to console for testing purposes, two different options shown below
        log.info("\ngetmessage.contentDisplay\n" + event.getAuthor().getName() + ": " + event.getMessage().getContentDisplay() + "\nn\n");
        log.info("\ngetmessage.contentRaw\n" + user + ": " + message + "\n\n");


        /*
        The following block is my attempt at a small recreation of Yydrasill's "Userphone" feature,
        allowing users to chat between multiple servers. I have it working with a single instance, however I would need
        more thought and effort to have it be something that could have more than one line working at a time. Would need
        to be threaded and possibly a lambda function to work properly at scale (SEE HELLO COMMAND FOR EXAMPLE)
         */
        if(message.startsWith("!!phone")) {
            if(isChatting == 0){
                log.info("Phone Line 1");
                isChatting =1;
                phoneLine[0]=event;
                phoneChannels.add(event.getMessage().getChannel().getName());
                log.info(phoneLine[0].getChannel().getName());
            }else if(isChatting == 1){
                log.info("Phone Line 2");
                isChatting = 2;
                phoneLine[1] = event;
                phoneChannels.add(event.getChannel().getName());
                log.info(phoneLine[1].getChannel().getName());
            }else if(isChatting == 2){
                log.info("Resetting");
                if(event.getChannel().getName().equalsIgnoreCase(phoneChannels.get(0)) ||
                        event.getChannel().getName().equalsIgnoreCase(phoneChannels.get(1))
                ){
                    phoneLine[0].getChannel().sendMessage("The phone has been hung up by "
                            + event.getAuthor()).queue();
                    phoneLine[1].getChannel().sendMessage("The phone has been hung up by "
                            + event.getAuthor()).queue();

                    //Wipe the array
                    phoneLine = new MessageReceivedEvent[2];
                    //Reset the check for activity
                    isChatting = 0;



                }else{
                    event.getChannel().sendMessage("The phone is currently in use, please wait for the other" +
                            " parties to finish their conversation!").queue();
                }
            }
        }
        if(isChatting==2){

            if(phoneLine[0].getChannel().getName().equalsIgnoreCase(event.getChannel().getName())) {
                log.info("sending to second line");
                phoneLine[1].getChannel().sendMessage(event.getAuthor().getName() + " " +
                        ":: " + event.getMessage().getContentRaw()).queue();
            }else if(phoneLine[1].getChannel().getName().equalsIgnoreCase(event.getChannel().getName())){
                log.info("sending to first line");
                phoneLine[0].getChannel().sendMessage(event.getAuthor().getName() + " " +
                        ":: " + event.getMessage().getContentRaw()).queue();
            }
        }
    }
}
