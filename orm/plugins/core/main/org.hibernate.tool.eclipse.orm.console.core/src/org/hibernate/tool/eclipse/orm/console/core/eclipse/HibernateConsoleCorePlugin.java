package org.hibernate.tool.eclipse.orm.console.core.eclipse;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.MultiStatus;
import org.eclipse.core.runtime.Status;
import org.hibernate.tool.eclipse.common.base.core.console.HibernateConsoleMessages;

public class HibernateConsoleCorePlugin {

	public static final String ID = "org.hibernate.eclipse.console"; //$NON-NLS-1$

	private static final HibernateConsoleCorePlugin INSTANCE = new HibernateConsoleCorePlugin();

	public static HibernateConsoleCorePlugin getDefault() {
		return INSTANCE;
	}

	public void log(IStatus status) {
		HibernatePlugin.getDefault().getLog().log(status);
	}

	public void log(String message) {
		log(new Status(IStatus.INFO, ID, 0, message, null));
	}

	public void logErrorMessage(String message, Throwable t) {
		logMessage(IStatus.ERROR, message, t);
	}

	public void logMessage(int lvl, String message, Throwable t) {
		if (t == null) {
			log(message);
		} else {
			log(new MultiStatus(ID, lvl, new IStatus[] { throwableToStatus(t) }, message, null));
		}
	}

	public static IStatus throwableToStatus(Throwable t, int code) {
		List<IStatus> causes = new ArrayList<IStatus>();
		Throwable temp = t;
		while (temp != null && temp.getCause() != temp) {
			causes.add(new Status(IStatus.ERROR, ID, code,
					temp.getMessage() == null ? temp.toString() + HibernateConsoleMessages.HibernateBasePlugin_no_message_1 : temp.toString(), temp));
			temp = temp.getCause();
		}
		String msg = HibernateConsoleMessages.HibernateBasePlugin_no_message_2;
		if (t != null && t.getMessage() != null) {
			msg = t.toString();
		}

		if (causes.isEmpty()) {
			return new Status(IStatus.ERROR, ID, code, msg, t);
		} else {
			return new MultiStatus(ID, code, causes.toArray(new IStatus[causes.size()]), msg, t);
		}
	}

	public static IStatus throwableToStatus(Throwable t) {
		return throwableToStatus(t, 150);
	}

	public void logErrorMessage(String message, Throwable t[]) {
		IStatus[] children = new IStatus[t.length];
		for (int i = 0; i < t.length; i++) {
			Throwable throwable = t[i];
			children[i] = throwableToStatus(throwable);
		}
		IStatus s = new MultiStatus(ID, 150, children, message, null);
		log(s);
	}

	public void log(Throwable e) {
		log(new Status(IStatus.ERROR, ID, 150, "Hibernate Console Internal Error", e)); //$NON-NLS-1$
	}

	public void logWarning(Exception he) {
		logMessage(IStatus.WARNING, he == null ? null : he.getMessage(), he);
	}
}
