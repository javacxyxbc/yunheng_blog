package com.moxi.hyblog.sms.config;

import com.moxi.hyblog.sms.global.SysConf;
import org.springframework.amqp.core.*;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * RabbitMQ配置文件
 */
/**
 * @author hzh
 * @since 2020-08-07
 */
@Configuration
public class RabbitMqConfig {

    public static final  String hy_BLOG = "hyBlog";
    public static final  String hy_EMAIL = "hyEmail";
    public static final  String Hy_SMS = "hySms";
    public static final  String HY_COLLECT="hyCollect";
    public static final  String EXCHANGE_DIRECT = "hyExchange";
    public static final  String ROUTING_KEY_BLOG = "hyBlog";
    public static final  String ROUTING_KEY_EMAIL = "hyEmail";
    public static final  String ROUTING_KEY_SMS = "hySms";
    public static final String  ROUTING_KEY_COLLECT="hyCollect";


    /**
     * 申明交换机
     */
    @Bean(EXCHANGE_DIRECT)
    public Exchange EXCHANGE_DIRECT() {
        // 申明路由交换机，durable:在rabbitmq重启后，交换机还在
        return ExchangeBuilder.directExchange(EXCHANGE_DIRECT).durable(true).build();
    }

    /**
     * 申明Blog队列
     * @return
     */
    @Bean(hy_BLOG)
    public Queue HY_BLOG() {
        return new Queue(hy_BLOG);
    }

    /**
     * 申明Email队列
     * @return
     */
    @Bean(hy_EMAIL)
    public Queue HY_EMAIL() {
        return new Queue(hy_EMAIL);
    }

    /**
     * 申明SMS队列
     * @return
     */
    @Bean(Hy_SMS)
    public Queue HY_SMS() {
        return new Queue(Hy_SMS);
    }
    /**
     * 声明collect统计队列
     */
    @Bean(HY_COLLECT)
    public Queue HY_COLLECT(){return new Queue(HY_COLLECT);};
    /**
     * blog 队列绑定交换机，指定routingKey
     * @param queue
     * @param exchange
     * @return
     */
    @Bean
    public Binding BINDING_QUEUE_INFORM_BLOG(@Qualifier(hy_BLOG) Queue queue, @Qualifier(EXCHANGE_DIRECT) Exchange exchange) {
        return BindingBuilder.bind(queue).to(exchange).with(ROUTING_KEY_BLOG).noargs();
    }

    /**
     * mail 队列绑定交换机，指定routingKey
     * @param queue
     * @param exchange
     * @return
     */
    @Bean
    public Binding BINDING_QUEUE_INFORM_EMAIL(@Qualifier(hy_EMAIL) Queue queue, @Qualifier(EXCHANGE_DIRECT) Exchange exchange) {
        return BindingBuilder.bind(queue).to(exchange).with(ROUTING_KEY_EMAIL).noargs();
    }

    /**
     * sms 队列绑定交换机，指定routingKey
     * @param queue
     * @param exchange
     * @return
     */
    @Bean
    public Binding BINDING_QUEUE_INFORM_SMS(@Qualifier(Hy_SMS) Queue queue, @Qualifier(EXCHANGE_DIRECT) Exchange exchange) {
        return BindingBuilder.bind(queue).to(exchange).with(ROUTING_KEY_SMS).noargs();
    }


    /**
     * collect 队列绑定交换机，指定routingKey
     * @param queue
     * @param exchange
     * @return
     */
    @Bean
    public Binding BINDING_QUEUE_INFORM_COLLECT(@Qualifier(HY_COLLECT) Queue queue, @Qualifier(EXCHANGE_DIRECT) Exchange exchange) {
        return BindingBuilder.bind(queue).to(exchange).with(ROUTING_KEY_COLLECT).noargs();
    }


    @Bean
    public MessageConverter messageConverter() {
        return new Jackson2JsonMessageConverter();
    }

}
