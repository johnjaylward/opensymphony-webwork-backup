package com.opensymphony.webwork.views.freemarker.tags;

import com.opensymphony.xwork.util.OgnlValueStack;
import com.opensymphony.webwork.components.UIBean;
import com.opensymphony.webwork.components.Submit;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * User: plightbo
 * Date: Aug 1, 2005
 * Time: 8:48:36 PM
 */
public class SubmitModel extends TagModel {
    public SubmitModel(OgnlValueStack stack, HttpServletRequest req, HttpServletResponse res) {
        super(stack, req, res);
    }

    protected UIBean getBean() {
        return new Submit(stack, req, res);
    }
}
