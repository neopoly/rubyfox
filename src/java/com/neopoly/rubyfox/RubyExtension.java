package com.neopoly.rubyfox;

import com.smartfoxserver.v2.core.ISFSEvent;
import com.smartfoxserver.v2.entities.User;
import com.smartfoxserver.v2.entities.data.ISFSObject;
import com.smartfoxserver.v2.extensions.SFSExtension;

public class RubyExtension extends SFSExtension {
    private JRuby _jruby;

    public void init() {
        logBanner();

        _jruby = new JRuby(this);
        _jruby.load();
        _jruby.handleInit();
    }

    @Override
    public void handleClientRequest(String requestId, User sender, ISFSObject params) {
        _jruby.handleClientRequest(requestId, sender, params);
    }

    @Override
    public void handleServerEvent(ISFSEvent event) throws Exception {
        _jruby.handleServerEvent(event);
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
}
