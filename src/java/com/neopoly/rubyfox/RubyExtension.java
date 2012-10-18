package com.neopoly.rubyfox;

import com.smartfoxserver.v2.extensions.SFSExtension;

public class RubyExtension extends SFSExtension {
    public void init() {
        logBanner();

        JRuby jruby = new JRuby(this);
        jruby.load();
    }

    @Override
    public void destroy() {
        trace("Destroying " + this);
        super.destroy();
    }

    private void logBanner() {
        trace("Booting " + this);
        trace("  Name: " + getName());
        trace("  Dir: " + getCurrentFolder());
        trace("  Extension filename: " + getExtensionFileName() + " (" + getLevel() + ")");
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
        trace("  Reload mode: " + getReloadMode());

        trace("  Config file: " + getPropertiesFileName());
        trace("  Config properties: " + getConfigProperties());
    }
}
