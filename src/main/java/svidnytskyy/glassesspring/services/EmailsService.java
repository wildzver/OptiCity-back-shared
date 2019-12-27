package svidnytskyy.glassesspring.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring5.SpringTemplateEngine;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;
import org.thymeleaf.templateresolver.ITemplateResolver;
import svidnytskyy.glassesspring.models.Order;
import svidnytskyy.glassesspring.models.OrderItem;

import javax.mail.MessagingException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.util.stream.Collectors;

@PropertySource("classpath:application.properties")
@Service
public class EmailsService {
    @Autowired
    Environment env;

    @Autowired
    JavaMailSender javaMailSender;

    @Bean(name = "htmlTemplateEngine")
    public TemplateEngine htmlTemplateEngine() {
        SpringTemplateEngine templateEngine = new SpringTemplateEngine();
        templateEngine.addTemplateResolver(htmlTemplateResolver());
        return templateEngine;
    }

    @Bean(name = "cssTemplateEngine")
    public TemplateEngine cssTemplateEngine() {
        SpringTemplateEngine templateEngine = new SpringTemplateEngine();
        templateEngine.addTemplateResolver(cssTemplateResolver());
        return templateEngine;
    }

    private ITemplateResolver htmlTemplateResolver() {
        ClassLoaderTemplateResolver templateResolver = new ClassLoaderTemplateResolver();
        templateResolver.setPrefix("/templates/");
        templateResolver.setSuffix(".html");
        templateResolver.setTemplateMode(TemplateMode.HTML);
        templateResolver.setCharacterEncoding("UTF8");
        templateResolver.setCheckExistence(true);
        templateResolver.setCacheable(false);
        return templateResolver;
    }

    private static String getCurrentBaseUrl() {
        ServletRequestAttributes sra = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest req = sra.getRequest();
        return req.getScheme() + "://" + req.getServerName() + ":" + req.getServerPort() + req.getContextPath();
    }

    private ITemplateResolver cssTemplateResolver() {
        ClassLoaderTemplateResolver templateResolver = new ClassLoaderTemplateResolver();
        templateResolver.setPrefix("/static/css/");
        templateResolver.setSuffix(".css");
        templateResolver.setTemplateMode(TemplateMode.CSS);
        return templateResolver;
    }

    public void sendEmail(Order order) throws MessagingException, IOException {

        Context ctx = new Context();
        ctx.setVariable("orederNo", order.getOrderNo());
        ctx.setVariable("clientName", order.getUser().getLastName().concat(" ").concat(order.getUser().getFirstName()));
        ctx.setVariable("clientPhone", order.getUser().getPhone());
        ctx.setVariable("deliveryMethod", order.getAdress().getDeliveryMethod());
        ctx.setVariable("settlement", order.getAdress().getSettlement());
        ctx.setVariable("branch", order.getAdress().getBranch());
        ctx.setVariable("orderlist", order.getOrderList());
        ctx.setVariable("total", order.getTotal());
        ctx.setVariable("quantityTotal", order.getQuantityTotal());
        ctx.setVariable("adress", order.getAdress());
        ctx.setVariable("baseUrl", getCurrentBaseUrl());

        sendEmailAdmin(order, ctx);
        sendEmailClient(order, ctx);
    }

    private void sendEmailAdmin(Order order, Context ctx) throws MessagingException {
        MimeMessage mimeMessageAdmin = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessageAdmin, true);
        try {
            System.out.println("USERNAME" + env.getProperty("spring.mail.username"));
            System.out.println("PASSWORD" + env.getProperty("spring.mail.password"));
            mimeMessageAdmin.setFrom(new InternetAddress(env.getProperty("spring.mail.username")));
            helper.setTo("tarassvidnytskyy@gmail.com");
            helper.setSubject("Замовлення № " + order.getOrderNo());

            String htmlContent = this.htmlTemplateEngine().process("orderMail.html", ctx);
            helper.setText(htmlContent, true);

        } catch (MessagingException e) {
            e.printStackTrace();
        }
        javaMailSender.send(mimeMessageAdmin);

    }

    private void sendEmailClient(Order order, Context ctx) throws MessagingException {
        MimeMessage mimeMessageClient = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessageClient, true);
        try {
            mimeMessageClient.setFrom(new InternetAddress(env.getProperty("spring.mail.username")));
            helper.setTo(order.getUser().getEmail());
            helper.setSubject("OptiCity. Замовлення №" + order.getOrderNo());

            String htmlContent = this.htmlTemplateEngine().process("orderMailClient.html", ctx);
            helper.setText(htmlContent, true);

            order.getOrderList().stream().map(OrderItem::getProduct).peek(product -> {
                String imageName = product
                        .getImages()
                        .stream()
                        .filter(image -> image.isMainImage() == true)
                        .findFirst()
                        .orElseGet(() -> product.getImages().get(0))
                        .getImageName();
                try {
                    FileSystemResource logoImage2 = new FileSystemResource(new File("public/upload-dir/logo-white.png"));
                    helper.addInline("logo", logoImage2);
                    FileSystemResource productImage = new FileSystemResource(new File("public/products-imgs/" + imageName));
                    helper.addInline(product.getProductNumber(), productImage);
                } catch (MessagingException e) {
                    e.printStackTrace();
                }
            }).collect(Collectors.toList());

        } catch (MessagingException e) {
            e.printStackTrace();
        }
        javaMailSender.send(mimeMessageClient);
    }
}


