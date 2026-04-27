package org.hibernate.tool.eclipse.orm.base.ui.ui.console.common;

import org.eclipse.ui.views.properties.IPropertySource;
import org.hibernate.tool.eclipse.orm.query.IQueryPage;
import org.hibernate.tool.eclipse.common.runtime.HibernateRuntimeException;
import org.hibernate.tool.eclipse.orm.runtime.spi.IHQLCodeAssist;
import org.hibernate.tool.eclipse.orm.runtime.spi.IRuntimeManager;
import org.hibernate.tool.eclipse.orm.runtime.spi.ISession;

public class ConsoleExtensionUI {

    private final IRuntimeManager runtimeManager;

    public ConsoleExtensionUI(IRuntimeManager runtimeManager) {
        this.runtimeManager = runtimeManager;
    }

    public CompletionProposalsResult hqlCodeComplete(String query,
            int startPosition, int currentOffset) {
        HQLCompletionHandler handler = new HQLCompletionHandler(startPosition);
        if (!runtimeManager.hasConfiguration()) {
            try {
                runtimeManager.build();
                runtimeManager.buildMappings();
            } catch (HibernateRuntimeException e) {
                // ignore
            }
        }
        IHQLCodeAssist hqlEval = runtimeManager.getHibernateService()
                .newHQLCodeAssist(runtimeManager.getConfiguration());
        query = query.replace('\t', ' ');
        hqlEval.codeComplete(query, currentOffset, handler);
        return new CompletionProposalsResult(handler.getCompletionProposals(),
                handler.getLastErrorMessage());
    }

    public IPropertySource getPropertySource(Object object,
            IQueryPage selectedQueryPage) {
        ISession currentSession = ((ISession) selectedQueryPage.getSession());
        if ((currentSession.isOpen() && currentSession.contains(object))
                || hasMetaData(object, currentSession)) {
            return new EntityPropertySource(object, currentSession, runtimeManager);
        }
        return null;
    }

    private boolean hasMetaData(Object object, ISession currentSession) {
        return currentSession.getSessionFactory().getClassMetadata(
                runtimeManager.getHibernateService().getClassWithoutInitializingProxy(object)) != null;
    }
}
