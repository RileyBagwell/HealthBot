package com.av.healthbot.listeners;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import java.util.Arrays;
import java.util.List;

public class EventListener extends ListenerAdapter {
    // List of keywords to watch for
    private static final List<String> KEYWORDS = Arrays.asList(
            "depress", "bummed", "upset", "sad", "angry", "stressed", "feel", "ugly", "gross", "anxious", "anxiety");

    /**
     * Fires when a message is sent to the server.
     * Used to detect important messages to DM user
     */
    public void onMessageReceived(MessageReceivedEvent event) {
        String message = event.getMessage().getContentRaw();

        // Check if the message contains at least two keywords
        long keywordCount = KEYWORDS.stream().filter(keyword -> message.toLowerCase().contains(keyword)).count();
        if (keywordCount >= 2) {
            event.getAuthor().openPrivateChannel().queue(channel -> {
                channel.sendMessage("Hey, I have noticed you used some keywords indicating that you might " +
                        "be feeling down. Here are some resources you can use to get help. And remember, you can " +
                        "always reach out to someone you trust to talk.\n\n" +
                        "__**Mental Help Resources**__\n\n" +
                        "__*Depression and Suicidal Intentions*__\n" +
                        "American Association of Suicidology (AAS): https://suicidology.org/\n" +
                        "Mental Health America (MHA): https://mhanational.org/\n\n" +
                        "__*Anxiety Disorders*__\n" +
                        "Anxiety and Depression Association of America (ADAA): https://adaa.org/\n" +
                        "Freedom From Fear: https://www.freedomfromfear.org/\n\n" +
                        "__*Substance Use*__\n" +
                        "Substance Abuse and Mental Health Services Administration (SAMHSA): 1-800-622-4357 https://www.samhsa.gov\n" +
                        "Start Your Recovery: https://www.freedomfromfear.org/").queue();
            });
        }
    }
}
