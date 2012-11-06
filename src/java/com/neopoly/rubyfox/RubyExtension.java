package com.neopoly.rubyfox;

import com.smartfoxserver.v2.core.ISFSEvent;
import com.smartfoxserver.v2.core.SFSEventParam;
import com.smartfoxserver.v2.entities.Room;
import com.smartfoxserver.v2.entities.User;
import com.smartfoxserver.v2.entities.data.ISFSObject;
import com.smartfoxserver.v2.entities.data.SFSObject;

import java.util.*;

public class RubyExtension extends BaseRubyExtension {
    private static final Set<String> DONT_ACCUMULATE = new HashSet<String>();

    @Override
    public void init() {
        super.init();
        ignoreAccumulates();
    }

    @Override
    public void handleClientRequest(String requestId, User sender, ISFSObject params) {
        long start = System.currentTimeMillis();
        super.handleClientRequest(requestId, sender, params);
        long duration = System.currentTimeMillis() - start;
        accumulate(requestId, sender, params, duration);
    }

    @Override
    public void handleServerEvent(ISFSEvent event) throws Exception {
        long start = System.currentTimeMillis();
        super.handleServerEvent(event);
        long duration = System.currentTimeMillis() - start;
        accumulate(event, duration);
    }

    @Override
    public void send(String cmdName, ISFSObject params, List<User> recipients, boolean useUDP) {
        if (recipients.isEmpty()) return;
        accumulate(cmdName, recipients, params, useUDP);
        super.send(cmdName, params, recipients, useUDP);
    }

    @Override
    public void send(String cmdName, ISFSObject params, User recipient, boolean useUDP) {
        accumulate(cmdName, recipient, params, useUDP);
        super.send(cmdName, params, recipient, useUDP);
    }

    // ---------------------------------------------------------

    private void accumulate(ISFSEvent event, long duration) {
        User user = (User) event.getParameter(SFSEventParam.USER);
        Room room = (Room) event.getParameter(SFSEventParam.ROOM);
        String sender = user != null ? user.getName() : room != null ? room.getName() : "?";

        SFSObject params = extractParameters(event);
        String log = String.format("EVNT[%s]: %s(%s) in %dms", sender, event.getType().name(), params.toJson(), duration);
        trace(log);
    }

    private SFSObject extractParameters(ISFSEvent event) {
        SFSObject params = new SFSObject();
        for (SFSEventParam param : SFSEventParam.values()) {
            switch (param) {
                case ZONE:
                case USER:
                    break;
                default:
                    Object value = event.getParameter(param);
                    if (value != null)
                        params.putUtfString(param.name(), String.valueOf(value));
            }
        }
        return params;
    }

    private void accumulate(String requestId, User sender, ISFSObject params, long duration) {
        if (ignoreAccumulate(requestId)) return;
        String log = String.format("RECV[%s]: %s(%s) in %dms", sender.getName(), requestId, params.toJson(), duration);
        trace(log);
    }

    private void accumulate(String cmd, List<User> reciepients, ISFSObject params, boolean udp) {
        if (ignoreAccumulate(cmd)) return;
        String log = String.format("%sSEND[#%s]: %s(%d bytes)", udp ? "U" : "", reciepients.size(), cmd, params.toBinary().length);
        trace(log);
    }

    private void accumulate(String cmd, User reciepient, ISFSObject params, boolean udp) {
        if (ignoreAccumulate(cmd)) return;
        String log = String.format("%sSEND[%s]: %s(%d bytes)", udp ? "U" : "", reciepient.getName(), cmd, params.toBinary().length);
        trace(log);
    }

    private boolean ignoreAccumulate(String cmd) {
        return DONT_ACCUMULATE.contains(cmd);
    }

    private void ignoreAccumulates() {
        String ignoreAccumulates = getConfigProperty("silent_events");
        if (ignoreAccumulates != null) {
            String[] tokens = ignoreAccumulates.split("\\s*,\\s*");
            trace("  Silent events: " + Arrays.toString(tokens));
            Collections.addAll(DONT_ACCUMULATE, tokens);
        }
    }
}
