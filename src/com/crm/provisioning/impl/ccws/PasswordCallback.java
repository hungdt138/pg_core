package com.crm.provisioning.impl.ccws;

import java.io.*;
import javax.security.auth.callback.*;
import javax.security.auth.callback.Callback;

import org.apache.ws.security.*;

/**
 * <p>Title: </p>
 *
 * <p>Description: </p>
 *
 * <p>Copyright: Copyright (c) 2004</p>
 *
 * <p>Company: </p>
 *
 * @version 1.0
 */
public class PasswordCallback implements CallbackHandler {

        /**
         *
         */
		private String mstrUsername = "";
        private String mstrPassWord = "";

        public PasswordCallback() {
//            mstrPassWord = "epos123456";
        }

        public PasswordCallback(String mstrUsername, String pstrPassWord) {
        	this.mstrUsername = mstrUsername;
            this.mstrPassWord = pstrPassWord;
        }
        /**
         * PasswordCallback
         *
         * @param cCWSDispatcherSplitThread CCWSDispatcherSplitThread
         */

        /**
         *
         * @param callbacks Callback[]
         * @throws IOException
         * @throws UnsupportedCallbackException
         */
        public void handle(Callback[] callbacks) throws IOException,
                        UnsupportedCallbackException {
                for (int i = 0; i < callbacks.length; i++) {
                	if (callbacks[i] instanceof WSPasswordCallback)
                	{
                        WSPasswordCallback pc = (WSPasswordCallback) callbacks[i];
                        
                        if (this.mstrUsername.equals(pc.getIdentifer()))
                        {
                        	pc.setPassword(this.mstrPassWord);
                        }
                	}
                	else
                	{
                        throw new UnsupportedCallbackException(callbacks[i], "Unrecognized Callback");
                	}
                }
        }
}
