package org.hibernate.tool.eclipse.common.base.core.logging;

import java.io.PrintStream;

/**
 * Abstraction for providing logging output streams.
 * The UI layer registers an implementation that writes to Eclipse console views.
 */
public interface LoggingStreamProvider {

	PrintStream findLoggingStream(String name);

	void removeLoggingStream(String name);
}
