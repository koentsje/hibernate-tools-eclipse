package org.hibernate.eclipse.ui.logging;

import java.io.IOException;
import java.io.PrintStream;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.ui.console.ConsolePlugin;
import org.eclipse.ui.console.IConsole;
import org.eclipse.ui.console.IConsoleManager;
import org.eclipse.ui.console.MessageConsole;
import org.eclipse.ui.console.MessageConsoleStream;
import org.hibernate.tool.eclipse.orm.console.core.nls.Messages;
import org.hibernate.tool.eclipse.common.base.core.logging.LoggingStreamProvider;

public class EclipseLoggingStreamProvider implements LoggingStreamProvider {

    private final Map<String, Object[]> loggingStreams = new HashMap<>();

    @Override
    public PrintStream findLoggingStream(String name) {
        Object[] console = loggingStreams.get(name);
        if (console == null) {
            console = new Object[2];
            String secondaryId = Messages.KnownConfigurations_hibernate_log
                + (name == null ? Messages.KnownConfigurations_unknown : name);
            console[0] = new MessageConsole(secondaryId, null);
            IConsoleManager consoleManager = ConsolePlugin.getDefault().getConsoleManager();
            consoleManager.addConsoles(new IConsole[] { (IConsole) console[0] });
            MessageConsoleStream stream = ((MessageConsole) console[0]).newMessageStream();
            console[1] = new PrintStream(stream);
            loggingStreams.put(name, console);
        }
        return (PrintStream) console[1];
    }

    @Override
    public void removeLoggingStream(String name) {
        Object[] object = loggingStreams.remove(name);
        if (object != null) {
            MessageConsole mc = (MessageConsole) object[0];
            PrintStream ps = (PrintStream) object[1];
            ps.close();
            MessageConsoleStream stream = ((MessageConsole) mc).newMessageStream();
            try { stream.close(); } catch (IOException e) { /* ignore */ }
            IConsoleManager consoleManager = ConsolePlugin.getDefault().getConsoleManager();
            consoleManager.removeConsoles(new IConsole[] { mc });
        }
    }
}
