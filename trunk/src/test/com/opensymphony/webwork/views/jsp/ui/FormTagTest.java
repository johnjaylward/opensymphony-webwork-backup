/*
 * Copyright (c) 2002-2003 by OpenSymphony
 * All rights reserved.
 */
package com.opensymphony.webwork.views.jsp.ui;

import com.opensymphony.webwork.TestAction;
import com.opensymphony.webwork.TestConfigurationProvider;
import com.opensymphony.webwork.views.jsp.AbstractUITagTest;

import com.opensymphony.xwork.config.ConfigurationManager;


/**
 * FormTagTest
 *
 * @author Jason Carreira
 *         Created Apr 3, 2003 10:28:58 AM
 */
public class FormTagTest extends AbstractUITagTest {
    //~ Methods ////////////////////////////////////////////////////////////////

    public void testForm() throws Exception {
        TestAction testAction = (TestAction) action;
        testAction.setFoo("bar");

        FormTag tag = new FormTag();
        tag.setPageContext(pageContext);
        tag.setName("'myForm'");
        tag.setMethod("'POST'");
        tag.setAction("'myAction'");
        tag.setEnctype("'myEncType'");

        tag.doStartTag();
        tag.doEndTag();

        verify(FormTag.class.getResource("Formtag-1.txt"));
    }

    protected void setUp() throws Exception {
        super.setUp();
        ConfigurationManager.clearConfigurationProviders();
        ConfigurationManager.addConfigurationProvider(new TestConfigurationProvider());
    }
}
