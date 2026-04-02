package org.hibernate.tool.eclipse.runtime.common;

public class UsageTracker {

	private static UsageTracker INSTANCE;

	public static UsageTracker getInstance() {
		if (INSTANCE == null) {
			INSTANCE = new UsageTracker();
		}
		return INSTANCE;
	}

	private UsageTracker() {
	}

	public void trackNewConfigurationEvent(String hibernateVersion) {
		// Usage tracking removed - was dependent on org.jboss.tools.usage
	}

}