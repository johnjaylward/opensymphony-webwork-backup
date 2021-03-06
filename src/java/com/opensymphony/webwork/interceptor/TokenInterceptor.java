/*
 * Copyright (c) 2002-2003 by OpenSymphony
 * All rights reserved.
 */
package com.opensymphony.webwork.interceptor;

import java.util.Map;

import com.opensymphony.webwork.util.TokenHelper;
import com.opensymphony.xwork.ActionContext;
import com.opensymphony.xwork.ActionInvocation;
import com.opensymphony.xwork.ValidationAware;
import com.opensymphony.xwork.interceptor.MethodFilterInterceptor;
import com.opensymphony.xwork.util.LocalizedTextUtil;

/**
 * <!-- START SNIPPET: description -->
 *
 * Ensures that only one request per token is processed. This interceptor can make sure that back buttons and double
 * clicks don't cause un-intended side affects. For example, you can use this to prevent careless users who might double
 * click on a "checkout" button at an online store. This interceptor uses a fairly primitive technique for when an
 * invalid token is found: it returns the result <b>invalid.token</b>, which can be mapped in your action configuration.
 * A more complex implementation, {@link TokenSessionStoreInterceptor}, can provide much better logic for when invalid
 * tokens are found.
 * 
 * <p/>
 *
 * <b>Note:</b> To set a token in your form, you should use the <b>token tag</b>. This tag is required and must be used
 * in the forms that submit to actions protected by this interceptor. Any request that does not provide a token (using
 * the token tag) will be processed as a request with an invalid token.
 * 
 * <p/>
 * 
 * <b>Internationalization Note:</b> The following key could be used to internationalized the action errors generated
 * by this token interceptor
 * 
 * <ul>
 *    <li>webwork.messages.invalid.token</li>
 * </ul>
 * 
 * <p/>
 * 
 * <b>NOTE:</b> As this method extends off MethodFilterInterceptor, it is capable of
 * deciding if it is applicable only to selective methods in the action class. See
 * <code>MethodFilterInterceptor</code> for more info.
 *
 * <!-- END SNIPPET: description -->
 *
 * <p/> <u>Interceptor parameters:</u>
 *
 * <!-- START SNIPPET: parameters -->
 *
 * <ul>
 *
 * <li>None</li>
 *
 * </ul>
 *
 * <!-- END SNIPPET: parameters -->
 *
 * <p/> <u>Extending the interceptor:</u>
 *
 * <p/>
 *
 * <!-- START SNIPPET: extending -->
 *
 * While not very common for users to extend, this interceptor is extended by the {@link TokenSessionStoreInterceptor}.
 * The {@link #handleInvalidToken}  and {@link #handleValidToken} methods are protected and available for more
 * interesting logic, such as done with the token session interceptor.
 *
 * <!-- END SNIPPET: extending -->
 *
 * <p/> <u>Example code:</u>
 *
 * <pre>
 * <!-- START SNIPPET: example -->
 * 
 * &lt;action name="someAction" class="com.examples.SomeAction"&gt;
 *     &lt;interceptor-ref name="token"/&gt;
 *     &lt;interceptor-ref name="basicStack"/&gt;
 *     &lt;result name="success"&gt;good_result.ftl&lt;/result&gt;
 * &lt;/action&gt;
 * 
 * &lt;-- In this case, myMethod of the action class will not 
 *        get checked for invalidity of token --&gt;
 * &lt;action name="someAction" class="com.examples.SomeAction"&gt;
 *     &lt;interceptor-ref name="token"&gt;
 *     	  &lt;param name="excludeMethods"&gt;myMethod&lt;/param&gt;
 *     &lt;/interceptor-ref name="token"/&gt;
 *     &lt;interceptor-ref name="basicStack"/&gt;
 *     &lt;result name="success"&gt;good_result.ftl&lt;/result&gt;
 * &lt;/action&gt;
 * 
 * <!-- END SNIPPET: example -->
 * </pre>
 *
 * @author Jason Carreira
 * @author Rainer Hermanns
 * @author Nils-Helge Garli
 * @author <a href='mailto:the_mindstorm[at]evolva[dot]ro'>Alexandru Popescu</a>
 * @see TokenSessionStoreInterceptor
 * @see TokenHelper
 * 
 * @version $Date$ $Id$
 */
public class TokenInterceptor extends MethodFilterInterceptor {
	
	private static final long serialVersionUID = 9021366162909706309L;
	
	public static final String INVALID_TOKEN_CODE = "invalid.token";

    /**
     * @see com.opensymphony.xwork.interceptor.MethodFilterInterceptor#doIntercept(com.opensymphony.xwork.ActionInvocation)
     */
    protected String doIntercept(ActionInvocation invocation) throws Exception {
        if (log.isDebugEnabled()) {
            log.debug("Intercepting invocation to check for valid transaction token.");
        }

        Map session = ActionContext.getContext().getSession();

        synchronized (session) {
            if (!TokenHelper.validToken()) {
                return handleInvalidToken(invocation);
            }

            return handleValidToken(invocation);
        }
    }

    /**
     * Determines what to do if an invalida token is provided. If the action implements {@link ValidationAware}
     *
     * @param invocation the action invocation where the invalid token failed
     * @return the return code to indicate should be processed
     * @throws Exception when any unexpected error occurs.
     */
    protected String handleInvalidToken(ActionInvocation invocation) throws Exception {
        Object action = invocation.getAction();
        String errorMessage = LocalizedTextUtil.findText(this.getClass(), "webwork.messages.invalid.token",
                invocation.getInvocationContext().getLocale(),
                "The form has already been processed or no token was supplied, please try again.", new Object[0]);

        if (action instanceof ValidationAware) {
            ((ValidationAware) action).addActionError(errorMessage);
        } else {
            log.warn(errorMessage);
        }

        return INVALID_TOKEN_CODE;
    }

    /**
     * Called when a valid token is found. This method invokes the action by can be changed to do something more
     * interesting.
     *
     * @param invocation the action invocation
     * @throws Exception when any unexpected error occurs.
     */
    protected String handleValidToken(ActionInvocation invocation) throws Exception {
        return invocation.invoke();
    }
}
