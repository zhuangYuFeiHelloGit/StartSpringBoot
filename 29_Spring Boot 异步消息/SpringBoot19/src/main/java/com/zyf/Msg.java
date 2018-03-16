package com.zyf;

import org.springframework.jms.core.MessageCreator;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;

/**
 * Created by zyf on 2018/3/15.
 */
public class Msg implements MessageCreator {
	@Override
	public Message createMessage(Session session) throws JMSException {
		return session.createTextMessage("zyf：hello");
	}
}
