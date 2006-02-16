/*
 * Copyright (c) 2002-2006 by OpenSymphony
 * All rights reserved.
 */
package com.opensymphony.webwork.portlet;

import java.util.AbstractMap;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.portlet.PortletRequest;
import javax.portlet.PortletSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * PortletSessionMap. Insert description.
 * 
 * @author Nils-Helge Garli
 * @version $Revision$ $Date$
 */
public class PortletSessionMap extends AbstractMap {
    
    private static final Log LOG = LogFactory.getLog(PortletSessionMap.class);

    private PortletSession session = null;

    private Set            entries = null;

    public PortletSessionMap(PortletRequest request) {
        this.session = request.getPortletSession();
        if(LOG.isDebugEnabled()) {
            LOG.debug("Dumping session info: ");
            Enumeration enumeration = session.getAttributeNames();
            while(enumeration.hasMoreElements()) {
                String key = (String)enumeration.nextElement();
                Object val = session.getAttribute(key);
                LOG.debug(key + " = " + val);
            }
        }
    }

    /**
     * @see java.util.Map#entrySet()
     */
    public Set entrySet() {
        synchronized (session) {
            if (entries == null) {
                entries = new HashSet();

                Enumeration enumeration = session.getAttributeNames();

                while (enumeration.hasMoreElements()) {
                    final String key = enumeration.nextElement().toString();
                    final Object value = session.getAttribute(key);
                    entries.add(new Map.Entry() {
                        public boolean equals(Object obj) {
                            Map.Entry entry = (Map.Entry) obj;

                            return ((key == null) ? (entry.getKey() == null)
                                    : key.equals(entry.getKey()))
                                    && ((value == null) ? (entry.getValue() == null)
                                            : value.equals(entry.getValue()));
                        }

                        public int hashCode() {
                            return ((key == null) ? 0 : key.hashCode())
                                    ^ ((value == null) ? 0 : value.hashCode());
                        }

                        public Object getKey() {
                            return key;
                        }

                        public Object getValue() {
                            return value;
                        }

                        public Object setValue(Object obj) {
                            session.setAttribute(key, obj);

                            return value;
                        }
                    });
                }
            }
        }

        return entries;
    }

    /**
     * Returns the session attribute associated with the given key or
     * <tt>null</tt> if it doesn't exist.
     * 
     * @param key the name of the session attribute.
     * @return the session attribute or <tt>null</tt> if it doesn't exist.
     */
    public Object get(Object key) {
        synchronized (session) {
            return session.getAttribute(key.toString());
        }
    }

    /**
     * Saves an attribute in the session.
     * 
     * @param key the name of the session attribute.
     * @param value the value to set.
     * @return the object that was just set.
     */
    public Object put(Object key, Object value) {
        synchronized (session) {
            entries = null;
            session.setAttribute(key.toString(), value);

            return get(key);
        }
    }

    /**
     * @see java.util.Map#clear()
     */
    public void clear() {
        synchronized (session) {
            entries = null;
            session.invalidate();
        }
    }

    /**
     * Removes the specified session attribute.
     * 
     * @param key the name of the attribute to remove.
     * @return the value that was removed or <tt>null</tt> if the value was
     * not found (and hence, not removed).
     */
    public Object remove(Object key) {
        synchronized (session) {
            entries = null;

            Object value = get(key);
            session.removeAttribute(key.toString());

            return value;
        }
    }
}