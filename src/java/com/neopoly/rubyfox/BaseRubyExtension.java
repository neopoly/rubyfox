package com.neopoly.rubyfox;

import com.smartfoxserver.v2.core.ISFSEvent;
import com.smartfoxserver.v2.core.SFSEventType;
import com.smartfoxserver.v2.entities.User;
import com.smartfoxserver.v2.entities.data.ISFSObject;
import com.smartfoxserver.v2.exceptions.SFSException;
import com.smartfoxserver.v2.extensions.BaseServerEventHandler;
import com.smartfoxserver.v2.extensions.SFSExtension;

import java.io.PrintWriter;
import java.io.StringWriter;

public class BaseRubyExtension extends SFSExtension {
    private JRuby _jruby;

    public void init() {
        logBanner();

        installFakeEventHandlers();

        try {
            _jruby = new JRuby(this);
            _jruby.load();
            _jruby.handleInit();
        } catch (RuntimeException e) {
            trace(getStackTrace(e));
            throw e;
        }
    }

    @Override
    public void handleClientRequest(String requestId, User sender, ISFSObject params) {
        try {
            _jruby.handleClientRequest(requestId, sender, params);
        } catch (RuntimeException e) {
            trace(getStackTrace(e));
            throw e;
        }
    }

    @Override
    public void handleServerEvent(ISFSEvent event) throws Exception {
        try {
            _jruby.handleServerEvent(event);
        } catch (RuntimeException e) {
            trace(getStackTrace(e));
            throw e;
        }
    }

    public String getConfigProperty(String name) {
        return getConfigProperties().getProperty(name);
    }

    @Override
    public void destroy() {
        _jruby.handleDestroy();
        trace("Destroying " + this);
        super.destroy();
    }

    private void logBanner() {
        trace("Booting " + this);
        trace("  Name: " + getName());
        trace("  Dir: " + getCurrentFolder());
        trace("  Extension filename: " + getExtensionFileName() + " (" + getLevel() + ")");
        traceLevel();
        trace("  Reload mode: " + getReloadMode());
        trace("  Config file: " + getPropertiesFileName());
        trace("  Config properties: " + getConfigProperties());
    }

    private void traceLevel() {
        switch (getLevel()) {
            case ZONE:
                trace("  Zone: " + getParentZone());
                break;
            case ROOM:
                trace("  Room: " + getParentRoom());
                break;
            case GLOBAL:
                trace("  Global");
                break;
        }
    }

    /**
     * Install fake handlers for all {@linkplain com.smartfoxserver.v2.core.SFSEvent event types}.
     *
     * None of these events will be handled by the fake handler. {@linkplain #handleServerEvent(com.smartfoxserver.v2.core.ISFSEvent)} handles them.
     * We need to register them all to tell SmartFox to fire all events.
     */
    private void installFakeEventHandlers() {
        for (SFSEventType eventType : SFSEventType.values()) {
            addEventHandler(eventType, FakeHandler.class);
        }
    }

    private static String getStackTrace(Exception ex) {
        StringWriter w = new StringWriter();
        ex.printStackTrace(new PrintWriter(w));
        String stack = w.toString();
        stack = "\n--- STACKTRACE ---\n".concat(stack);
        return stack;
    }


    public static class FakeHandler extends BaseServerEventHandler {
        @Override
        public void handleServerEvent(ISFSEvent event) throws SFSException {
            throw new SFSException("not used");
        }
    }
}
