/*
 * Copyright (c) 2002-2003 by OpenSymphony
 * All rights reserved.
 */
/*
 * WebWork, Web Application Framework
 *
 * Distributable under Apache license.
 * See terms of license at opensource.org
 */
package com.opensymphony.webwork.views.jsp;

import com.opensymphony.webwork.views.util.UrlHelper;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpUtils;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;
import java.io.IOException;
import java.util.HashMap;


/**
 * This tag is used to create a URL.
 * You can use the "param" tag inside the body to provide
 * additional request parameters.
 *
 * @author Rickard �berg (rickard@dreambean.com)
 * @version $Revision$
 * @see com.opensymphony.webwork.views.jsp.ParamTag
 */
public class URLTag extends ParametereizedBodyTagSupport {
    //~ Static fields/initializers /////////////////////////////////////////////

    private static final Log LOG = LogFactory.getLog(URLTag.class);

    // Public --------------------------------------------------------

    /**
     * The includeParams attribute may have the value 'none', 'get' or 'all'.
     * It is used when the url tag is used without a value or page attribute.
     * Its value is looked up on the ValueStack
     * If no includeParams is specified then 'get' is used.
     * none - include no parameters in the URL
     * get  - include only GET parameters in the URL (default)
     * all  - include both GET and POST parameters in the URL
     */
    public static final String NONE = "none";
    public static final String GET = "get";
    public static final String ALL = "all";

    //~ Instance fields ////////////////////////////////////////////////////////

    protected String includeParamsAttr;
    protected String schemeAttr;
    protected String value;

    // Attributes ----------------------------------------------------
    protected String valueAttr;
    protected boolean encode = true;
    protected boolean includeContext = true;

    //~ Methods ////////////////////////////////////////////////////////////////

    public void setEncode(boolean encode) {
        this.encode = encode;
    }

    public void setIncludeContext(boolean includeContext) {
        this.includeContext = includeContext;
    }

    public void setIncludeParams(String aName) {
        includeParamsAttr = aName;
    }

    public void setScheme(String aScheme) {
        schemeAttr = aScheme;
    }

    public void setValue(String aName) {
        valueAttr = aName;
    }

    public int doEndTag() throws JspException {
        HttpServletRequest request = (HttpServletRequest) pageContext.getRequest();
        HttpServletResponse response = (HttpServletResponse) pageContext.getResponse();
        String scheme = request.getScheme();

        if (schemeAttr != null) {
            scheme = schemeAttr;
        }

        String result = UrlHelper.buildUrl(value, request, response, params, scheme, includeContext, encode);

        String id = getId();

        if (id != null) {
            getStack().getContext().put(id, result);

            // add to the request and page scopes as well
            pageContext.setAttribute(id, result);
            pageContext.setAttribute(id, result, PageContext.REQUEST_SCOPE);
        } else {
            try {
                pageContext.getOut().write(result);
            } catch (IOException _ioe) {
                throw new JspException("IOError: " + _ioe.getMessage());
            }
        }

        value = null;
        includeParamsAttr = null;

        return EVAL_PAGE;
    }

    // BodyTag implementation ----------------------------------------
    public int doStartTag() throws JspException {
        if (valueAttr != null) {
            value = findString(valueAttr);
        }

        // Clear the params map if it has been instantiated before
        if (params != null) {
            params.clear();
        }
        else {
            params = new HashMap();
        }

        // no explicit url set so attach params from current url, do
        // this at start so body params can override any of these they wish.
        try {
            String includeParams = GET;

            if (includeParamsAttr != null) {
                includeParams = findString(includeParamsAttr);
            }

            if ((includeParams == null && value == null) || GET.equalsIgnoreCase(includeParams)) {
                // Parse the query string to make sure that the parameters come from the query, and not some posted data
                HttpServletRequest req = (HttpServletRequest) pageContext.getRequest();
                String query = req.getQueryString();

                if (query != null) {
                    // Remove possible #foobar suffix
                    int idx = query.lastIndexOf('#');

                    if (idx != -1) {
                        query = query.substring(0, idx - 1);
                    }

                    params.putAll(HttpUtils.parseQueryString(query));
                }
            } else if (ALL.equalsIgnoreCase(includeParams)) {
                params.putAll(pageContext.getRequest().getParameterMap());
            } else if (!NONE.equalsIgnoreCase(includeParams)) {
                LOG.warn("Unknown value for includeParams parameter to URL tag: " + includeParams);
            }
        } catch (Exception e) {
            LOG.warn("Unable to put request parameters (" + ((HttpServletRequest) pageContext.getRequest()).getQueryString() + ") into parameter map.", e);
        }

        return EVAL_BODY_BUFFERED;
    }
}