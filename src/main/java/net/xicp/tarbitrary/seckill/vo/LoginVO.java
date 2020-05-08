package net.xicp.tarbitrary.seckill.vo;

import lombok.Data;
import net.xicp.tarbitrary.seckill.validator.IsMobile;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
public class LoginVO {
    @NotNull
    @IsMobile
    private long mobile;

    @NotEmpty
    private String password;

}
