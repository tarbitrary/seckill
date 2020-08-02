package net.xicp.tarbitrary.seckill.controller;

import net.xicp.tarbitrary.seckill.cache.CacheService;
import net.xicp.tarbitrary.seckill.cache.SeckillKey;
import net.xicp.tarbitrary.seckill.domain.TradeUser;
import net.xicp.tarbitrary.seckill.result.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.imageio.ImageIO;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import javax.servlet.http.HttpServletResponse;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Random;

/**
 * @author tarbitrary
 */
@Controller
public class VerifyCodeController {

    @Autowired
    private CacheService cacheService;

    @RequestMapping(value = "/generateVerifyCode", method = RequestMethod.GET)
    public Result getVerifyCode(TradeUser tradeUser, @RequestParam("goodsId") String goodsId, HttpServletResponse response) {
        final BufferedImage bufferedImage = generateImage(tradeUser, goodsId);
        try {
            ImageIO.write(bufferedImage, "JPEG", response.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public BufferedImage generateImage(TradeUser user, String goodsId) {
        int width = 80;
        int height = 32;
        //create the image
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics g = image.getGraphics();
        // set the background color
        g.setColor(new Color(0xDCDCDC));
        g.fillRect(0, 0, width, height);
        // draw the border
        g.setColor(Color.black);
        g.drawRect(0, 0, width - 1, height - 1);
        // create a random instance to generate the codes
        Random rdm = new Random();
        // make some confusion
        for (int i = 0; i < 50; i++) {
            int x = rdm.nextInt(width);
            int y = rdm.nextInt(height);
            g.drawOval(x, y, 0, 0);
        }
        // generate a random code
        String verifyCode = generateVerifyCode(rdm);
        g.setColor(new Color(0, 100, 0));
        g.setFont(new Font("Candara", Font.BOLD, 24));
        g.drawString(verifyCode, 8, 24);
        g.dispose();
        //把验证码存到redis中
        int rnd = calc(verifyCode);
        cacheService.set(SeckillKey.IMAGE_CACHE, user.getId() + "," + goodsId, rnd);
        //输出图片
        return image;
    }

    private static int calc(String verifyCode) {
        ScriptEngineManager manager = new ScriptEngineManager();
        final ScriptEngine javascript = manager.getEngineByName("JavaScript");
        Object eval = null;
        try {
            eval = javascript.eval(verifyCode);
        } catch (ScriptException e) {
            e.printStackTrace();
        }
        return (int) eval;
    }

    private static final char[] OP_FLAG = {'+', '-', '*'};

    private static String generateVerifyCode(Random random) {
        final int a = random.nextInt(10);
        final int b = random.nextInt(10);
        final int c = random.nextInt(10);


        char opFlagOne = OP_FLAG[random.nextInt(3)];
        char opFlagTwo = OP_FLAG[random.nextInt(3)];


        return a + "" + opFlagOne + b + opFlagTwo + c;
    }

    public static void main(String[] args) {
        Random random = new Random();
        // generate a random code
        String verifyCode = generateVerifyCode(random);
        final int calc = calc(verifyCode);
        System.out.println(verifyCode);
        System.out.println(calc);
    }
}
