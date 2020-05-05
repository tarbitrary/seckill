package net.xicp.tarbitrary.seckill.domain;

import lombok.Data;

import java.util.Date;

/**
 * @author tarbitrary
 */
@Data
public class TradeUser {
    private Long id;

    private String nickname;

    private String password;

    private String salt;

    private String head;

    private Date registerDate;

    private Date lastLoginDate;

    private Integer loginCount;
}
