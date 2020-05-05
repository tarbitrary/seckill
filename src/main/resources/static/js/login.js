function login() {
    $("#loginForm").validate({
        submitHandler: function () {
            doLogin();
        }
    });

}

function doLogin() {
    g_showLoading();
    var originPass = jQuery("#password").val();
    let length = g_passsword_salt.length;
    let middle = length >>> 1;
    let assemblePassword = g_passsword_salt.substring(0, middle) + originPass + g_passsword_salt.substring(middle, length);
    let md5Password = md5(assemblePassword);
    console.log(md5Password)
    jQuery.ajax({
        type: "POST",
        data: {
            "mobile": jQuery("#mobile").val(),
            "password": md5Password
        },
        url: "/doLogin",
        success: function (data) {
            if (data.code == 0) {
                layer.msg("成功");
                window.location.href = "/goods_list"
            } else {
                layer.msg(data.msg);
            }
            // layer.closeAll();
            // alert("login success");
            // window.location.href="/goods/to_list";
        },
        error: function (data) {
            layer.closeAll();

        }

    });
    return false;

}