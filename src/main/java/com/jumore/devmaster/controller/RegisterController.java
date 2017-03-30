package com.jumore.devmaster.controller;

import com.google.common.eventbus.AsyncEventBus;
import com.jumore.devmaster.common.DevMasterConst;
import com.jumore.devmaster.common.model.vo.DevMasterUserVO;
import com.jumore.devmaster.common.model.vo.MessageParam;
import com.jumore.devmaster.common.util.RSAKeyUtil;
import com.jumore.devmaster.common.util.StringUtil;
import com.jumore.devmaster.entity.DevMasterUser;
import com.jumore.devmaster.enums.MessageTemplateEnum;
import com.jumore.dove.aop.annotation.PublicMethod;
import com.jumore.dove.cache.CacheService;
import com.jumore.dove.dao.CommonDao;
import com.jumore.dove.util.MD5;
import com.jumore.dove.util.ParamMap;
import com.jumore.dove.util.RSAUtils;
import com.jumore.dove.web.model.Const;
import com.jumore.dove.web.model.ResponseVo;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.security.interfaces.RSAPrivateKey;
import java.util.Date;
import java.util.List;

/**
 * 注册
 * Created by LongQiong on 2017/3/27.
 */
@Controller
@RequestMapping(value = "")
@PublicMethod
public class RegisterController {

    /**
     * 日志对象
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(RegisterController.class);
    @Resource
    private AsyncEventBus asyncEventBus;

    @Resource
    private CacheService cacheService;

    @Resource
    private CommonDao commonDao;


    @RequestMapping(value = "/register")
    public ModelAndView register(HttpServletRequest request) throws Exception {
        ModelAndView mv = new ModelAndView();

        RSAKeyUtil.getRSAKey(request, mv);
        return mv;

    }

    /**
     * 用户注册
     * @param request
     * @param userParam
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/doRegister")
    @ResponseBody
    public ResponseVo<String> doRegister(HttpServletRequest request, DevMasterUserVO userParam) throws Exception {
        String msg = validateUserParam(userParam);
        if(StringUtil.isNotEmpty(msg)){
            return ResponseVo.<String> BUILDER().setDesc(msg).setCode(Const.BUSINESS_CODE.FAILED);
        }

        //解密后的密码,password是提交过来的密码
        RSAPrivateKey privateKey = (RSAPrivateKey)request.getSession().getAttribute(RSAUtils.RAS_Key_Private);
        String password = RSAUtils.decryptByPrivateKey(userParam.getPassword(), privateKey);
        userParam.setPassword(MD5.md5(password));

//        String rePassword = RSAUtils.decryptByPrivateKey(userParam.getRePassword(), privateKey);
//        userParam.setRePassword(MD5.md5(rePassword));

        DevMasterUser devMasterUser = new DevMasterUser();
        userParam.setCreateTime(new Date());
        BeanUtils.copyProperties(devMasterUser, userParam);

        devMasterUser.setDeleteFlag(DevMasterConst.Flag.NotDelete);
        try {
            commonDao.save(devMasterUser);
            return ResponseVo.<String> BUILDER().setDesc("注册成功！").setCode(Const.BUSINESS_CODE.SUCCESS);
        } catch (Exception e){
            return ResponseVo.<String> BUILDER().setDesc(String.format("注册失败，%s", e.getMessage())).setCode(Const.BUSINESS_CODE.FAILED);
        }

    }

    /**
     * 注册参数验证
     * @param userParam
     * @return
     */
    private String validateUserParam(DevMasterUserVO userParam){
        String msg = "";
        if(StringUtil.isEmpty(userParam.getAccount())){
            msg += "账号不能为空！<br>";
        }

        if(StringUtil.isEmpty(userParam.getMobile())){
            msg += "手机号不能为空！<br>";
        }

        if(StringUtil.isEmpty(userParam.getPassword())){
            msg += "密码不能为空！<br>";
        }

        if(StringUtil.isEmpty(userParam.getRePassword())){
            msg += "重复密码不能为空！<br>";
        }

        if(StringUtil.isEmpty(userParam.getAccount())){
            msg += "验证码不能为空！<br>";
        }

        if(StringUtil.isNotEmpty(userParam.getPassword()) && StringUtil.isNotEmpty(userParam.getRePassword()) && !userParam.getPassword().equals(userParam.getRePassword())){
            msg += "两次密码不一致！<br>";
        }

        //校验短信校验
        String objType = DevMasterConst.CacheKey.SYS_KEY_MAIN + ":" + DevMasterConst.CacheKey.USER_REGISTER;
        String smsCode = cacheService.getString(objType, userParam.getMobile());
        if(StringUtils.isBlank(smsCode)){
            msg += "短信验证码已失效！<br>";
        }else if (!smsCode.equals(userParam.getSmsCode())) {
            msg += "短信验证码输入错误！<br>";
        }

        //验证账号或者手机号码是否有重复
        if(StringUtil.isNotEmpty(userParam.getAccount())&&StringUtil.isNotEmpty(userParam.getMobile())){
            List<DevMasterUser> users = commonDao.listByParams(DevMasterUser.class, "DevMasterUser.getUser", new ParamMap(userParam));
            if(!CollectionUtils.isEmpty(users)){
                if(users.get(0).getAccount().equals(userParam.getAccount())){
                    msg+="账号已存在！<br>";
                }
                if(users.get(0).getMobile().equals(userParam.getMobile())){
                    msg+="手机号码已存在！<br>";
                }
            }
        }

        return msg;
    }



    /**
     * 发送短信验证码
     * @param param

     */
    @RequestMapping(value = "/sendVerifyCodeMsg")
    @ResponseBody
    public ResponseVo<String> sendVerifyCodeMsg(HttpServletRequest request, DevMasterUser param) {
        if(StringUtils.isBlank(param.getMobile())) {
            return ResponseVo.<String> BUILDER().setDesc("手机号码不能为空").setCode(Const.BUSINESS_CODE.FAILED);
        }

        //生成验证码放入缓存
        String objType = DevMasterConst.CacheKey.SYS_KEY_MAIN + ":" + DevMasterConst.CacheKey.USER_REGISTER;
        String veriCode = StringUtil.getRandomNum2();
        LOGGER.info("短信验证码："+veriCode);
        cacheService.set(objType, param.getMobile(), veriCode, DevMasterConst.CacheKey.MOBILE_CHK_CODE_EXPIRE_TIME);

        //发送短信
        String ip = request.getHeader("x-forwarded-for");
        if (ip == null || ip.length() ==0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() ==0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() ==0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        MessageParam messageParam ;
        //对短信验证码进行判断，判断是忘记密码还是注册
        messageParam = new MessageParam(param.getMobile(), MessageTemplateEnum.SMS_REGISTER, veriCode);

        //异步提交
        asyncEventBus.post(messageParam);

        return ResponseVo.<String> BUILDER().setCode(Const.BUSINESS_CODE.SUCCESS);
    }

    /**
     * 获取图片验证码
     * @return
     */
    /*@RequestMapping(value="/getVerifyCode")
    public ModelAndView getVerifyCode(HttpServletRequest request, HttpServletResponse response) {
        response.setHeader("Pragma", "No-cache");
        response.setHeader("Cache-Control", "no-cache");
        response.setDateHeader("Expires", 0);
        response.setContentType("image/jpeg");
        try {
            //生成随机字串
            String verifyCode = VerifyCodeUtils.generateVerifyCode(4);

            //生成验证码放入缓存
            String objType = JdtWebConstants.SYS_KEY_MAIN + ":" + JdtWebConstants.OBJTYPE_USER_GETVERIFYCODE;
            cacheService.set(objType, request.getRequestedSessionId(), verifyCode, JdtWebConstants.IMAGE_CHK_CODE_EXPIRE_TIME);
            VerifyCodeUtils.outputImage(VERIFY_WIDTH, VERIFY_HEIGHT, response.getOutputStream(), verifyCode);
            //操作日志
            CommonUtil.optionLogAdd(null, "生成验证码放入缓存",verifyCode );
        } catch (IOException e) {
            e.printStackTrace();
            try {
                //生成随机字串
                String objType = JdtWebConstants.SYS_KEY_MAIN + ":" + JdtWebConstants.OBJTYPE_USER_GETVERIFYCODE;
                String verifyCode = VerifyCodeUtils.generateVerifyCode(4).toUpperCase();
                //生成验证码放入缓存
                cacheService.set(objType, request.getRequestedSessionId(), verifyCode, JdtWebConstants.IMAGE_CHK_CODE_EXPIRE_TIME);
                VerifyCodeUtils.outputImage(VERIFY_WIDTH, VERIFY_HEIGHT, response.getOutputStream(), verifyCode);
                //操作日志
                CommonUtil.optionLogAdd(null, "生成验证码放入缓存",verifyCode );
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }
        return null;
    }*/
}
