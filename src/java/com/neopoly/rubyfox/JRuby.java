package com.neopoly.rubyfox;

import com.smartfoxserver.v2.extensions.SFSExtension;
import org.jruby.CompatVersion;
import org.jruby.Ruby;
import org.jruby.RubyInstanceConfig;
import org.jruby.javasupport.JavaUtil;
import org.jruby.runtime.builtin.IRubyObject;

public class JRuby {
    private Ruby _ruby;
    private SFSExtension _extension;

    public JRuby(SFSExtension extension) {
        _extension = extension;
        boot();
    }

    private void boot() {
        log("Booting JRuby");

        RubyInstanceConfig config = new RubyInstanceConfig();
        config.setCompatVersion(CompatVersion.RUBY1_9);
        _ruby = Ruby.newInstance(config);
        log("  " + eval("%{Version: jruby-#{JRUBY_VERSION} (ruby-#{RUBY_VERSION})}").toString());

        appendLoadPath(_extension.getConfigProperties().getProperty("load_path"));
        exposeGlobalVariable();
    }

    public void load() {
        require("rubyfox");

        log("Botting JRuby completed");
    }

    private void appendLoadPath(String loadPath) {
        if (loadPath != null) {
            String[] pathes = loadPath.split(":");
            for (String path : pathes) {
                evalLogged("$LOAD_PATH << \"" + path + "\"");
            }
        } else {
            log("  No load_path found!");
        }
    }

    private void exposeGlobalVariable() {
        log("  Setting " + _extension + " as $extension");
        _ruby.getGlobalVariables().set("$extension", JavaUtil.convertJavaToRuby(_ruby, _extension));
    }

    private void log(String msg) {
        _extension.trace(msg);
    }

    private IRubyObject eval(String code) {
        return _ruby.evalScriptlet(code);
    }

    private IRubyObject evalLogged(String code) {
        log("  " + code);
        IRubyObject result = eval(code);
        log("  # => " + result);
        return result;
    }

    private void require(String lib) {
        evalLogged("require '" + lib + "'");
    }
}
