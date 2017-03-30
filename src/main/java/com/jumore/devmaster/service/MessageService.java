package com.jumore.devmaster.service;

import com.jumore.devmaster.common.model.vo.MessageParam;
import com.jumore.dove.service.BaseService;

import java.util.List;

/**
 * 消息接口
 * Created by Long on 2017/3/28.
 */
public interface MessageService extends BaseService {
    /**
     * 发送短信
     * @param messageParam
     */
    void sendMessage(MessageParam messageParam);
    void batchSendMessage(List<MessageParam> paramList);

}
