package me.swerve.riseuhc.bot;

import lombok.Getter;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.conf.ConfigurationBuilder;

public class TwitterBot {
    @Getter private static TwitterBot instance;

    private final String consumerKey = "D4OrC210WpOg4QrIP4hg3ysbM";
    private final String consumerSecret = "ej8POsXEaK5s346KVvFl5ff8jy9c0mhLEdOUE53NUu5jwaSRDD";
    private final String accessToken = "1466115794849443840-KpnrdKg8nuJCGjK9wdMYZRtPnOFT1x";
    private final String accessTokenSecret = "8MQyryJedf3qVoOC7MDjLgXwwGI2KW5zDG8dwLDr6VhPM";

    private final Twitter twitterInstance;

    public TwitterBot() {
        ConfigurationBuilder configBuilder = new ConfigurationBuilder();

        configBuilder.setDebugEnabled(true)
        .setOAuthConsumerKey(consumerKey)
        .setOAuthConsumerSecret(consumerSecret)
        .setOAuthAccessToken(accessToken)
        .setOAuthAccessTokenSecret(accessTokenSecret);

        TwitterFactory factory = new TwitterFactory(configBuilder.build());
        twitterInstance = factory.getInstance();

        instance = this;
    }

    public void announceGame(String scenarios, String time) throws TwitterException {
        twitterInstance.updateStatus(
                "Rise UHC [1.7-1.8]\n" +
                "\n" +
                "Type: FFA\n" +
                "\n" +
                "Scenarios: " + scenarios + "\n" +
                "\n" +
                "» Whitelist off: " + time + " | (http://time.is/UTC)\n" +
                "\n" +
                "IP → riseuhc.club");
    }
}
