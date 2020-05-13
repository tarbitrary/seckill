package net.xicp.tarbitrary.seckill;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
        //(exclude = {DataSourceAutoConfiguration.class})
@MapperScan(basePackages={"net.xicp.tarbitrary.seckill.dao"})
public class SeckillApplication {

    public static void main(String[] args) {
        final ConfigurableApplicationContext run = SpringApplication.run(SeckillApplication.class, args);
    }

}
