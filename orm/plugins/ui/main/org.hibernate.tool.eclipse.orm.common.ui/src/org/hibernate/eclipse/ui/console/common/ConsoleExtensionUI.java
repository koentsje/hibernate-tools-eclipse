package org.hibernate.eclipse.ui.console.common;

import org.eclipse.ui.views.properties.IPropertySource;
import org.hibernate.console.QueryPage;
import org.hibernate.eclipse.console.common.ConsoleExtension;
import org.hibernate.eclipse.console.common.HibernateExtension;
import org.hibernate.tool.eclipse.orm.runtime.spi.HibernateException;
import org.hibernate.tool.eclipse.orm.runtime.spi.IHQLCodeAssist;
import org.hibernate.tool.eclipse.orm.runtime.spi.ISession;

public class ConsoleExtensionUI {

    private final ConsoleExtension consoleExtension;

    public ConsoleExtensionUI(ConsoleExtension consoleExtension) {
        this.consoleExtension = consoleExtension;
    }

    public CompletionProposalsResult hqlCodeComplete(String query,
            int startPosition, int currentOffset) {
        HibernateExtension hibernateExtension = consoleExtension.getHibernateExtension();
        HQLCompletionHandler handler = new HQLCompletionHandler(startPosition);
        if (!hibernateExtension.hasConfiguration()) {
            try {
                hibernateExtension.build();
                hibernateExtension.buildMappings();
            } catch (HibernateException e) {
                // ignore
            }
        }
        IHQLCodeAssist hqlEval = hibernateExtension.getHibernateService()
                .newHQLCodeAssist(hibernateExtension.getConfiguration());
        query = query.replace('\t', ' ');
        hqlEval.codeComplete(query, currentOffset, handler);
        return new CompletionProposalsResult(handler.getCompletionProposals(),
                handler.getLastErrorMessage());
    }

    public IPropertySource getPropertySource(Object object,
            QueryPage selectedQueryPage) {
        HibernateExtension hibernateExtension = consoleExtension.getHibernateExtension();
        ISession currentSession = ((ISession) selectedQueryPage.getSession());
        if ((currentSession.isOpen() && currentSession.contains(object))
                || hasMetaData(object, currentSession, hibernateExtension)) {
            return new EntityPropertySource(object, currentSession, hibernateExtension);
        }
        return null;
    }

    private boolean hasMetaData(Object object, ISession currentSession, HibernateExtension hibernateExtension) {
        return currentSession.getSessionFactory().getClassMetadata(
                hibernateExtension.getHibernateService().getClassWithoutInitializingProxy(object)) != null;
    }
}
