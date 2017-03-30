/**
 * 
 */
package com.jumore.devmaster.listener;

import com.google.common.eventbus.AsyncEventBus;
import com.google.common.eventbus.Subscribe;
import com.jumore.devmaster.common.model.vo.MessageParam;
import com.jumore.devmaster.service.MessageService;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;


/** 
*
* @author: long
* @since: 2017-03-28
*/
public class MessageEventListener implements InitializingBean {

    @Autowired
    private AsyncEventBus  asyncEventBus;
    @Autowired
    private MessageService messageService;

    @Subscribe
    public boolean one(MessageParam param) {
        messageService.sendMessage(param);
        return true;
    }

    @Subscribe
    public boolean list(List<MessageParam> paramList) {
        messageService.batchSendMessage(paramList);
        return true;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        asyncEventBus.register(this);
    }
}
