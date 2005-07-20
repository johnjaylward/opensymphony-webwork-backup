package com.opensymphony.webwork.components;

import com.opensymphony.webwork.config.Configuration;
import com.opensymphony.webwork.views.jsp.TagUtils;
import com.opensymphony.webwork.views.util.UrlHelper;
import com.opensymphony.xwork.config.ConfigurationManager;
import com.opensymphony.xwork.config.entities.ActionConfig;
import com.opensymphony.xwork.util.OgnlValueStack;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * User: plightbo
 * Date: Jul 1, 2005
 * Time: 11:23:47 PM
 */
public class Form extends ClosingUIBean {
    final public static String OPEN_TEMPLATE = "form";
    final public static String TEMPLATE = "form-close";

    String action;
    String target;
    String enctype;
    String method;
    String namespace;
    String validate;

    public Form(OgnlValueStack stack, HttpServletRequest request, HttpServletResponse response) {
        super(stack, request, response);
    }

    protected boolean evaluateNameValue() {
        return false;
    }

    public String getDefaultOpenTemplate() {
        return OPEN_TEMPLATE;
    }

    protected String getDefaultTemplate() {
        return TEMPLATE;
    }

    protected void evaluateExtraParams() {
        super.evaluateExtraParams();

        if (action != null) {
            final String action = findString(this.action);
            String namespace;

            if (this.namespace == null) {
                namespace = TagUtils.buildNamespace(getStack(), request);
            } else {
                namespace = findString(this.namespace);
            }

            if (namespace == null) {
                namespace = "";
            }

            final ActionConfig actionConfig = ConfigurationManager.getConfiguration().getRuntimeConfiguration().getActionConfig(namespace, action);

            if (actionConfig != null) {
                String result = UrlHelper.buildUrl(namespace + "/" + action + "." + Configuration.get("webwork.action.extension"), request, response, null);
                addParameter("action", result);
                addParameter("namespace", namespace);

                // if the name isn't specified, use the action name
                if (name == null) {
                    addParameter("name", action);
                }

                // if the id isn't specified, use the action name
                if (id == null) {
                    addParameter("id", action);
                }
            } else if (action != null) {
                String result = UrlHelper.buildUrl(action, request, response, null);
                addParameter("action", result);

                // namespace: cut out anything between the start and the last /
                int slash = result.lastIndexOf('/');
                if (slash != -1) {
                    addParameter("namespace", result.substring(0, slash));
                } else {
                    addParameter("namespace", "");
                }

                // name/id: cut out anything between / and . should be the id and name
                if (id == null) {
                    slash = result.lastIndexOf('/');
                    int dot = result.indexOf('.', slash);
                    if (dot != -1) {
                        id = result.substring(slash + 1, dot);
                    } else {
                        id = result.substring(slash + 1);
                    }
                    addParameter("id", id);
                }
            }
        }

        if (target != null) {
            addParameter("target", findString(target));
        }

        if (enctype != null) {
            addParameter("enctype", findString(enctype));
        }

        if (method != null) {
            addParameter("method", findString(method));
        }

        if (validate != null) {
            addParameter("validate", findValue(validate, Boolean.class));
        }
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getTarget() {
        return target;
    }

    public void setTarget(String target) {
        this.target = target;
    }

    public String getEnctype() {
        return enctype;
    }

    public void setEnctype(String enctype) {
        this.enctype = enctype;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getNamespace() {
        return namespace;
    }

    public void setNamespace(String namespace) {
        this.namespace = namespace;
    }

    public String getValidate() {
        return validate;
    }

    public void setValidate(String validate) {
        this.validate = validate;
    }
}
