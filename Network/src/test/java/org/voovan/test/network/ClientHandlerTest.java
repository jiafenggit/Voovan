package org.voovan.test.network;

import org.voovan.network.IoHandler;
import org.voovan.network.IoSession;
import org.voovan.tools.TEnv;
import org.voovan.tools.log.Logger;

public class ClientHandlerTest implements IoHandler {

	@Override
	public Object onConnect(IoSession session) {
		Logger.simple("onConnect");
		session.setAttribute("key", "attribute value");
		String msg = new String("test message\r\n");
		return msg;
	}

	@Override
	public void onDisconnect(IoSession session) {
		Logger.simple("onDisconnect");
	}

	@Override
	public Object onReceive(IoSession session, Object obj) {
		Logger.simple(session.remoteAddress()+":"+session.remotePort());
		//+"["+session.remoteAddress()+":"+session.remotePort()+"]"
		Logger.simple("Client onRecive: "+obj.toString());
		Logger.simple("Attribute onRecive: "+session.getAttribute("key"));
		TEnv.sleep(10000);
		session.close();
		return null;
	}

	@Override
	public void onException(IoSession session, Exception e) {
		Logger.simple("Client Exception: "+ e.getClass() + " => " +e.getMessage());
		Logger.error(e);
		session.close();
	}

	@Override
	public void onIdle(IoSession session) {
		Logger.info("idle");
	}

	@Override
	public void onSent(IoSession session, Object obj) {
		String sad = (String)obj;
		Logger.simple("Client onSent: "+ sad);
	}

}
