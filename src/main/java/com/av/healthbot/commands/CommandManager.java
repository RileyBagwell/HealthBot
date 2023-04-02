package com.av.healthbot.commands;

import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.guild.GuildReadyEvent;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import java.util.ArrayList;
import java.util.List;

/**
 * This class manages all the commands for the bot
 */

public class CommandManager extends ListenerAdapter {

    /**
     * Fires when a command is entered. Checks for which command was input,
     * then calls the corresponding method for that command
     */
    public void onSlashCommandInteraction(SlashCommandInteractionEvent event) {
        String command = event.getName(); // Get the user's command
        switch (command){
            case "report":
                report(event);
                break;
            case "resources":
                resources(event);
                break;
        }
    }

    /**
     * Fires when the bot comes online. Add each command to the command list
     */
    public void onGuildReady(GuildReadyEvent event) {
        List<CommandData> commandData = new ArrayList<>();
        // /report <user> [issue]
        OptionData option1 = new OptionData(OptionType.USER, "user", "The user you want to report", true);
        OptionData option2 = new OptionData(OptionType.STRING, "issue", "The issue that the user is showing signs of", false)
                .addChoice("Depressed", "depressed")
                .addChoice("Stressed", "stressed");
        commandData.add(Commands.slash("report", "Anonymously provide resources to a user").addOptions(option1, option2));
        // /resources
        commandData.add(Commands.slash("resources", "List resources for users to get help in chat"));

        event.getGuild().updateCommands().addCommands(commandData).queue(); // Add all the commands to the guild
    }

    /**
     * Report a user who has concerning behavior
     */
    public void report(SlashCommandInteractionEvent event){
        OptionMapping option1 = event.getOption("user");
        OptionMapping option2 = event.getOption("issue");
        User user = option1.getAsUser();
        String issue = option2.getAsString();
        String replyMessage = "An anonymous user noticed some concerning messages from you, so here are some resources to get help:\n";
        switch(issue.toLowerCase()){
            case "depressed":
                replyMessage += "American Association of Suicidology (AAS): https://suicidology.org/\n" +
                        "Mental Health America (MHA): https://mhanational.org/";
                break;
            case "stressed":
                replyMessage += "Anxiety and Depression Association of America (ADAA): https://adaa.org/\n"+
                        "Freedom From Fear: https://www.freedomfromfear.org/";
                break;
        }
        event.reply("Report successfully sent!").setEphemeral(true).queue(); // Send success message
        final String finalMessage = replyMessage;
        user.openPrivateChannel()
                .flatMap(channel -> channel.sendMessage(finalMessage))
                .queue();
    }

    /**
     * Send resources into the chat to get help
     */
    public void resources(SlashCommandInteractionEvent event){
        String message = "__**Mental Help Resources**__\n\n" +
                "__*Depression and Suicidal Intentions*__\n" +
                "American Association of Suicidology (AAS): https://suicidology.org/\n" +
                "Mental Health America (MHA): https://mhanational.org/\n\n" +
                "__*Anxiety Disorders*__\n" +
                "Anxiety and Depression Association of America (ADAA): https://adaa.org/\n"+
                "Freedom From Fear: https://www.freedomfromfear.org/\n\n" +
                "__*Substance Use*__\n" +
                "Substance Abuse and Mental Health Services Administration (SAMHSA): 1-800-622-4357 https://www.samhsa.gov\n" +
                "Start Your Recovery: https://www.freedomfromfear.org/";
        event.reply(message).queue();
    }
}
