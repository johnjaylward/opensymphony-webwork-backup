/*
 * Copyright (c) 2002-2003 by OpenSymphony
 * All rights reserved.
 */
package com.opensymphony.webwork.dispatcher;

import java.io.Serializable;

import java.util.AbstractMap;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;


/**
 *
 * @author Rickard �berg (rickard@middleware-company.com)
 * @version $Revision$
 */
public class SessionMap extends AbstractMap implements Serializable {
    //~ Instance fields ////////////////////////////////////////////////////////

    HttpServletRequest request;
    Set entries;

    //~ Constructors ///////////////////////////////////////////////////////////

    public SessionMap(HttpServletRequest request) {
        this.request = request;
    }

    //~ Methods ////////////////////////////////////////////////////////////////

    public void clear() {
        HttpSession session = request.getSession();

        synchronized (session) {
            entries = null;
            session.invalidate();
        }
    }

    public Set entrySet() {
        HttpSession session = request.getSession();

        synchronized (session) {
            if (entries == null) {
                entries = new HashSet();

                Enumeration enum = session.getAttributeNames();

                while (enum.hasMoreElements()) {
                    final String key = enum.nextElement().toString();
                    final Object value = session.getAttribute(key);
                    entries.add(new Map.Entry() {
                            public boolean equals(Object obj) {
                                Map.Entry entry = (Map.Entry) obj;

                                return ((key == null) ? (entry.getKey() == null) : key.equals(entry.getKey())) && ((value == null) ? (entry.getValue() == null) : value.equals(entry.getValue()));
                            }

                            public int hashCode() {
                                return ((key == null) ? 0 : key.hashCode()) ^ ((value == null) ? 0 : value.hashCode());
                            }

                            public Object getKey() {
                                return key;
                            }

                            public Object getValue() {
                                return value;
                            }

                            public Object setValue(Object obj) {
                                request.getSession().setAttribute(key.toString(), obj);

                                return value;
                            }
                        });
                }
            }
        }

        return entries;
    }

    public Object get(Object key) {
        HttpSession session = request.getSession();

        synchronized (session) {
            return session.getAttribute(key.toString());
        }
    }

    public Object put(Object key, Object value) {
        HttpSession session = request.getSession();

        synchronized (session) {
            entries = null;
            session.setAttribute(key.toString(), value);

            return get(key);
        }
    }

    public Object remove(Object key) {
        HttpSession session = request.getSession();

        synchronized (session) {
            entries = null;

            Object value = get(key);
            session.removeAttribute(key.toString());

            return value;
        }
    }
}