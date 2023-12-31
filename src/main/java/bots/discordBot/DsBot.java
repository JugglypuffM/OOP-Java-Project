package bots.discordBot;

import bots.Bot;
import bots.BotDriver;
import bots.platforms.Platform;
import io.github.cdimascio.dotenv.Dotenv;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.channel.concrete.PrivateChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.FileUpload;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.net.URL;
import java.util.List;

public class DsBot extends ListenerAdapter implements Bot {
    private final Dotenv dotenv = Dotenv.load();
    private final BotDriver driver;
    private final JDA jda = JDABuilder.createDefault(dotenv.get("DS_BOT_TOKEN"))
            .enableIntents(GatewayIntent.MESSAGE_CONTENT)
            .build();
    public DsBot(BotDriver m_driver){
        this.driver = m_driver;
    }
    public boolean start() {
        try {
            jda.addEventListener(new DsBot(this.driver));
            jda.awaitReady();
        } catch (InterruptedException e) {
            driver.getLogger().error("Bot registration failed", e);
            return false;
        }
        return true;
    }

    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        if(event.getAuthor().isBot()){
            return;
        }
        String message = event.getMessage().getContentDisplay();
        String platformId = event.getChannel().getId();
        String username = event.getAuthor().getName();
        List<Message.Attachment> attachments = event.getMessage().getAttachments();
        boolean hasPhoto = (!attachments.isEmpty() && attachments.get(0).isImage());
        String[] reply = new String[24];
        if (hasPhoto){
            try {
                String photo = attachments.get(0).getUrl();
                URL url = new URL(photo);
                BufferedImage img = ImageIO.read(url);
                String[] filename = photo.split("\\?ex")[0].split("/")[6].split("\\.");
                String extension = filename[filename.length-1];
                String picture =  driver.getDatabase().getAccountWithPlatformId(platformId, Platform.DISCORD).getId() + "." + extension;
                File file = new File("./pictures/" + picture);
                ImageIO.write(img, extension, file);
                reply = driver.handleUpdate(platformId, username, picture, Platform.DISCORD, true);
            }catch (Exception e){
                driver.getLogger().error("Failed to download the image.", e);
                reply[0] = "Не удалось загрузить твою фотографию, отправь другую или попробуй еще раз.";
            }
        }else reply = driver.handleUpdate(platformId, username, message, Platform.DISCORD, false);
        driver.send(this, platformId, username, message, reply);
    }


    @Override
    public synchronized boolean executePhoto(String platformId, String message, String photo) {
        try {
            PrivateChannel channel = jda.getPrivateChannelById(platformId);
            if (channel == null) return false;
            File file = new File("./pictures/" + photo);
            channel.sendMessage(message).addFiles(FileUpload.fromData(file)).queue();
            return true;
        }catch (Exception e){
            driver.getLogger().error("Failed to send the image.", e);
            return false;
        }
    }

    @Override
    public boolean executeText(String platformId, String message) {
        PrivateChannel channel = jda.getPrivateChannelById(platformId);
        if (channel != null){
            channel.sendMessage(message).queue();
            return true;
        }
        return false;
    }

    @Override
    public Platform getPlatform() {
        return Platform.DISCORD;
    }
}
