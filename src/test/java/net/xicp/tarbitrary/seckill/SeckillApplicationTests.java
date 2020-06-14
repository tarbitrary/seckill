package net.xicp.tarbitrary.seckill;

import net.xicp.tarbitrary.seckill.controller.LoginAction;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import javax.annotation.Resource;
import java.io.UnsupportedEncodingException;

@SpringBootTest
class SeckillApplicationTests {
    @Resource
    private LoginAction loginAction;

    MockMvc mockMvc;


    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(loginAction).build();
    }

    @Test
    public void testFindAllList() {
        ResultActions resultActions = null;
        try {
            resultActions = this.mockMvc.perform(MockMvcRequestBuilders.post("/doLogin").param("mobile", "15658031688").param("password", "90e002a6d9e738f53c20c65796e52cea"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        MvcResult mvcResult = resultActions.andReturn();
        String result = null;
        try {
            result = mvcResult.getResponse().getContentAsString();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        System.out.println("=====客户端获得反馈数据:" + result);
        // 也可以从response里面取状态码，header,cookies...
//        System.out.println(mvcResult.getResponse().getStatus());

    }
}
