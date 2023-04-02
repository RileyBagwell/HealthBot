package com.av.healthbot;

import com.av.healthbot.commands.CommandManager;
import com.av.healthbot.listeners.EventListener;
import io.github.cdimascio.dotenv.Dotenv;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.sharding.DefaultShardManagerBuilder;
import net.dv8tion.jda.api.sharding.ShardManager;
import net.dv8tion.jda.api.utils.ChunkingFilter;
import net.dv8tion.jda.api.utils.MemberCachePolicy;
import net.dv8tion.jda.api.utils.cache.CacheFlag;
import javax.security.auth.login.LoginException;

/**
 * Authors: Riley Bagwell, Daniel Nemirovskiy
 * Created: 4/1/2023
 * Last Edit: 4/2/2023
 *
 * 2023 Axxess Hackathon Project
 * Discord bot focused on helping users facing mental health issues.
 * Features include auto-DMs from the bot to users with concerning behavior/messages
 * Various commands to provide resources for help, and to send them anonymously to another user
 */

public class HealthBot {
    private final Dotenv config;
    private final ShardManager shardManager;

    public HealthBot() throws LoginException {
        // Load environment variables
        config = Dotenv.configure().load();
        String token = config.get("TOKEN");

        // Build shard manager
        DefaultShardManagerBuilder builder = DefaultShardManagerBuilder.createDefault(token);
        builder.enableIntents(GatewayIntent.GUILD_MEMBERS, GatewayIntent.MESSAGE_CONTENT, GatewayIntent.GUILD_PRESENCES);
        builder.setMemberCachePolicy(MemberCachePolicy.ALL);
        builder.setChunkingFilter(ChunkingFilter.ALL);
        builder.enableCache(CacheFlag.ACTIVITY, CacheFlag.ONLINE_STATUS);
        // Set information about bot
        builder.setStatus(OnlineStatus.ONLINE);
        shardManager = builder.build();

        // Register listeners
        shardManager.addEventListener(new EventListener(), new CommandManager());
    }

    /** Return config */
    public Dotenv getconfig(){ return config; }

    /** Return shardManager */
    public ShardManager getShardManager(){ return shardManager; }

    /**
     * Main driver method, creates the bot
     */
    public static void main(String[] args){
        try {
            HealthBot bot = new HealthBot();
        } catch (LoginException e){
            System.out.println("ERROR: Provided bot token is invalid.");
        }
    }
}