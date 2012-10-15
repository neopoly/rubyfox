package com.neopoly.rubyfox;

import com.smartfoxserver.v2.core.ISFSEvent;
import com.smartfoxserver.v2.core.SFSEventParam;
import com.smartfoxserver.v2.core.SFSEventType;
import com.smartfoxserver.v2.entities.Room;
import com.smartfoxserver.v2.entities.User;
import com.smartfoxserver.v2.entities.data.ISFSObject;
import com.smartfoxserver.v2.entities.data.SFSObject;
import com.smartfoxserver.v2.exceptions.SFSException;
import com.smartfoxserver.v2.extensions.BaseServerEventHandler;
import com.smartfoxserver.v2.extensions.SFSExtension;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Main extends SFSExtension {
    // TODO proper extension reloading?
    private JRuby _jruby;

    public void init() {
        trace("Booting " + this.getClass().getName());
        trace("Name: " + getName());
        trace("Current folder: " + getCurrentFolder());
        trace("Extension filename: " + getExtensionFileName());
        trace("getPropertiesFileName: " + getPropertiesFileName());
        trace("getConfigProperties: " + getConfigProperties());

        _jruby = new JRuby(this);
        _jruby.load();
    }

    @Override
    public void destroy() {
        super.destroy();
        trace("Destroying " + this.getClass().getName());
    }
}
