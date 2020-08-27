package com.moxi.hyblog.sms.listener;

import com.moxi.hyblog.sms.util.SendMailUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.mail.MessagingException;
import java.util.Map;
/**
 * @author hzh
 * @since 2020-08-07
 */
@Slf4j
@Component

public class MailListener {

    @Autowired
    private SendMailUtils sendMailUtils;


    @RabbitListener(queues = "hyEmail")
    public void sendMail(Map<String, String> map) {
        if(map != null) {
            try {
                sendMailUtils.sendEmail(
                        map.get("receiver"),
                        map.get("text")
                );
            } catch (MessagingException e) {
                log.error("发送邮件失败！");
            }
        }

    }
}
