/*
 * WebWork, Web Application Framework
 *
 * Distributable under Apache license.
 * See terms of license at opensource.org
 */
package webwork.action.standard;

import com.opensymphony.webwork.ServletActionContext;
import webwork.action.Action;
import webwork.action.ActionSupport;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;

/**
 * JSP execution wrapper. This action allows JSP pages to be executed as actions.
 * <p/>
 * The JSP page should set the request attribute "result" to the result status.
 * Well defined result status constants are defined in the Action interface.
 *
 * @author Rickard �berg (rickard@middleware-company.com)
 * @version $Revision$
 * @see webwork.action.Action
 */
public class JSP
        extends ActionSupport {
    // Static --------------------------------------------------------

    // Attributes ----------------------------------------------------
    String pageName;
    RequestDispatcher dispatcher;

    /**
     * Get name of the page that is wrapped
     */
    public String getPage() {
        return pageName;
    }

    /**
     * Set the name of the page that is to be executed.
     *
     * @see webwork.action.factory.JspActionFactoryProxy
     */
    public void setPage(String aPageName) {
        pageName = aPageName;
        dispatcher = ServletActionContext.getRequest().getRequestDispatcher(aPageName);
    }

    // Action implementation -----------------------------------------
    /**
     * Execute the wrapper. This will forward to the given JSP-page,
     * which then can execute arbitrary code (possibly using tag libraries).
     * The JSP page should then set the "result" request attribute to the
     * result state of the execution.
     * <p/>
     * Any content generated by the JSP page will be thrown away.
     */
    public String execute()
            throws Exception {
        // Create wrapper which ignores all content-modifying calls
        HttpServletResponse res = new WrapperHttpServletResponse(ServletActionContext.getResponse());

        String oldResult = (String) ServletActionContext.getRequest().getAttribute("result");

        // Execute JSP
        dispatcher.forward(ServletActionContext.getRequest(), res);

        // Extract the result
        String result = (String) ServletActionContext.getRequest().getAttribute("result");

        // Default to success
        if (result == null)
            result = Action.SUCCESS;

        // Restore request attribute
        if (oldResult == null)
            ServletActionContext.getRequest().removeAttribute("result");
        else
            ServletActionContext.getRequest().setAttribute("result", oldResult);
        return result;
    }

    // Public --------------------------------------------------------

    // Protected -----------------------------------------------------

    // Inner classes -------------------------------------------------
    /**
     * Response wrapper. Delegate to real response, but
     * ignore all content modifying calls and return
     * dummies instead of real streams.
     */
    public class WrapperHttpServletResponse
            extends HttpServletResponseWrapper {
        WrapperHttpServletResponse(HttpServletResponse aResponse) {
            super(aResponse);
        }

        public void flushBuffer() throws IOException {
            // Ignore
        }

        public ServletOutputStream getOutputStream() throws IOException {
            return new ServletOutputStream() {
                public void write(int b) throws IOException {
                    // Throw away
                }
            };
        }

        public PrintWriter getWriter() throws IOException {
            return new PrintWriter(new StringWriter());
        }

        public void resetBuffer() {
            //response.resetBuffer();
        }

        public void setContentType(String s) {
            // Ignore
        }
    }
}
