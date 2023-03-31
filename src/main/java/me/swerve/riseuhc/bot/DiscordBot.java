package me.swerve.riseuhc.bot;

import discord4j.common.util.Snowflake;
import discord4j.core.DiscordClient;
import discord4j.core.GatewayDiscordClient;
import discord4j.core.object.entity.channel.GuildMessageChannel;
import discord4j.core.spec.EmbedCreateFields;
import discord4j.core.spec.EmbedCreateSpec;
import discord4j.rest.util.Color;
import lombok.Getter;
import me.swerve.riseuhc.attribute.MatchAttribute;

import javax.security.auth.login.LoginException;

public class DiscordBot {
    @Getter private static DiscordBot instance;

    private final GatewayDiscordClient gateway;
    public DiscordBot() throws LoginException {
        DiscordClient client = DiscordClient.create("OTE3MjM1Mzc5NzY0MDgwNjQw.GYx9lR.xhNG9J8FQN-mwW1aGJ4oP3afyldxoP9wVXoMo8");
        gateway = client.login().block();

        instance = this;
    }

    public void announceGame(String scenario, String time) {
        EmbedCreateSpec build = EmbedCreateSpec.builder()
                .title("RiseUHC")
                .addField(EmbedCreateFields.Field.of("Team Size ⇒", MatchAttribute.getAttributeFromName("Team Size").getCurrentSelection().getName(), true))
                .addField(EmbedCreateFields.Field.of("IP ⇒", "riseuhc.club", true))
                .addField(EmbedCreateFields.Field.of("Open Time ⇒", time + " UTC", true))
                .addField(EmbedCreateFields.Field.of("Scenarios ⇒", scenario, false))
                .addField(EmbedCreateFields.Field.of("Version ⇒", "[1.7-1.8]", false))
                .description("<@&1058584908144066580>")
                .color(Color.ORANGE).build();
        gateway.getChannelById(Snowflake.of("1058584793278844999")).ofType(GuildMessageChannel.class).flatMap(channel -> channel.createMessage(build)).subscribe();
    }
}
