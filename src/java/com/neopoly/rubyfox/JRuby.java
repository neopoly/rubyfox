package com.neopoly.rubyfox;

import com.smartfoxserver.v2.core.ISFSEvent;
import com.smartfoxserver.v2.entities.User;
import com.smartfoxserver.v2.entities.data.ISFSObject;
import com.smartfoxserver.v2.extensions.SFSExtension;
import org.jruby.*;
import org.jruby.javasupport.JavaUtil;
import org.jruby.runtime.builtin.IRubyObject;

public class JRuby {
    private Ruby _ruby;
    private SFSExtension extension;

    public JRuby(SFSExtension extension) {
        this.extension = extension;

        extension.trace("Booting JRuby");
        RubyInstanceConfig config = new RubyInstanceConfig();
        config.setCompatVersion(CompatVersion.RUBY1_9);
        _ruby = Ruby.newInstance(config);
        extension.trace("  Version: jruby-" + _ruby.evalScriptlet("JRUBY_VERSION") + " (ruby-" + _ruby.evalScriptlet("RUBY_VERSION") + ")");

        String loadPath = extension.getConfigProperties().getProperty("load_path");
        if (loadPath != null) {
          extension.trace("  Adding " + loadPath + " to $LOAD_PATH");
          _ruby.evalScriptlet("$LOAD_PATH << \"" + loadPath + "\"");
        }
        extension.trace("  $LOAD_PATH = " + _ruby.evalScriptlet("$LOAD_PATH.inspect"));
        extension.trace("  Setting " + extension + " as $extension");
        _ruby.getGlobalVariables().set("$extension", JavaUtil.convertJavaToRuby(_ruby, extension));
        extension.trace("  Require java");
        _ruby.evalScriptlet("require 'java'");
    }

    public void load() {
        extension.trace("  Require " + extension.getName());
        _ruby.evalScriptlet("require '" + extension.getName() + "'");
        extension.trace("Booting JRuby completed");
    }
}
