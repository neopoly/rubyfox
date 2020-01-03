package com.neopoly.rubyfox;

import com.smartfoxserver.v2.extensions.SFSExtension;
import org.apache.commons.lang.SystemUtils;
import org.jruby.Ruby;
import org.jruby.javasupport.JavaUtil;
import org.jruby.runtime.builtin.IRubyObject;

public class JRuby {
    private Ruby _ruby;
    private SFSExtension _sfsExtension;
    private IRubyObject _extensionHandler;

    public JRuby(SFSExtension sfsExtension) {
        _sfsExtension = sfsExtension;
        boot();
    }

    private void boot() {
        info("Booting JRuby");

        _ruby = Ruby.newInstance();
        info("  " + eval("%{Version: jruby-#{JRUBY_VERSION} (ruby-#{RUBY_VERSION})}").toString());

        appendLoadPath(getConfigProperty("load_path", null));
    }

    public void load() {
        require(getConfigProperty("gem_name", "rubyfox"));
        initRubyExtension(getConfigProperty("module_name", "Rubyfox"));
        debug("Booting JRuby completed");
    }

    public void handleInit() {
        delegateToHandler("on_init");
    }

    public void handleClientRequest(Object... p) {
        delegateToHandler("on_request", p);
    }

    public void handleServerEvent(Object... p) {
        delegateToHandler("on_event", p);
    }

    public void handleDestroy() {
        delegateToHandler("on_destroy");
    }

    private void delegateToHandler(String method, Object... p) {
        debug("Booting JRuby completed");
        _extensionHandler.callMethod(_ruby.getCurrentContext(), method, JavaUtil.convertJavaArrayToRuby(_ruby, p));
    }

    private void initRubyExtension(String moduleName) {
        IRubyObject module = evalLogged(moduleName);
        _extensionHandler = module.callMethod(
                _ruby.getCurrentContext(),
                "init",
                JavaUtil.convertJavaArrayToRuby(
                        _ruby,
                        new Object[]{_sfsExtension}
                        )
        );
    }

    private String getConfigProperty(String name, String def) {
        String value = _sfsExtension.getConfigProperties().getProperty(name);
        if (value == null) value = def;
        return value;
    }

    private void appendLoadPath(String loadPath) {
        String[] paths;
        if(SystemUtils.IS_OS_WINDOWS) {
            paths = loadPath.split(";");
        } else {
           paths = loadPath.split(":");
        }
        for (String path : paths) {
            evalLogged("$LOAD_PATH << \"" + path + "\"");
        }
    }

    private void debug(String msg) {
        _sfsExtension.getLogger().debug(msg);
    }

    private void info(String msg) {
        _sfsExtension.getLogger().info(msg);
    }

    private void warn(String msg) {
        _sfsExtension.getLogger().warn(msg);
    }

    private void error(String msg) {
        _sfsExtension.getLogger().error(msg);
    }

    private IRubyObject eval(String code) {
        return _ruby.evalScriptlet(code);
    }

    private IRubyObject evalLogged(String code) {
        debug("  " + code);
        IRubyObject result = eval(code);
        debug("  # => " + result);
        return result;
    }

    private void require(String lib) {
        evalLogged("require '" + lib + "'");
    }
}
