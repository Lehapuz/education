package main.service;

import com.github.cage.Cage;
import com.github.cage.GCage;
import main.api.response.CaptchaResponse;
import main.model.CaptchaCode;
import main.repositories.CaptchaCodeRepository;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.Random;
import java.util.UUID;

@Service
public class CaptchaService {

    private static final Cage CAGE = new GCage();
    private final CaptchaCodeRepository captchaCodeRepository;


    public CaptchaService(CaptchaCodeRepository captchaCodeRepository) {
        this.captchaCodeRepository = captchaCodeRepository;
    }


    public CaptchaResponse getCaptchaResponse() {
        captchaCodeRepository.deleteLessThanTime(LocalDateTime.now().minusHours(1));
        CaptchaCode captchaCode = new CaptchaCode();
        captchaCode.setTime(LocalDateTime.now());
        captchaCode.setCode(generateCode(4));
        captchaCode.setSecretCode(UUID.randomUUID().toString());
        captchaCodeRepository.save(captchaCode);

        CaptchaResponse captchaResponse = new CaptchaResponse();
        captchaResponse.setImage("data:image/png;base64, ".concat(generateBase64Image(captchaCode.getCode())));
        captchaResponse.setSecret(captchaCode.getSecretCode());

        return captchaResponse;
    }


    private String generateCode(int length) {
        Random r = new Random();
        String str = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < length; i++) {
            int number = r.nextInt(62);
            sb.append(str.charAt(number));
        }
        return sb.toString();
    }


    private static String generateBase64Image(String text) {
        String result = "";
        try {
            BufferedImage captchaImage = ImageIO.read(new ByteArrayInputStream(CAGE.draw(text)));   //(CAGE.draw(text)));
            captchaImage = resizeImage(captchaImage);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write(captchaImage, "png", baos);
            baos.flush();
            result = new String(Base64.getEncoder().encode(baos.toByteArray()), StandardCharsets.UTF_8);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }


    private static BufferedImage resizeImage(BufferedImage captchaImage) {
        final int width = 100;
        final int height = 35;
        BufferedImage newImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        int widthStep = captchaImage.getWidth() / width;
        int heightStep = captchaImage.getHeight() / height;
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                int rgb = captchaImage.getRGB(x * widthStep, y * heightStep);
                newImage.setRGB(x, y, rgb);
            }
        }
        return newImage;
    }
}
