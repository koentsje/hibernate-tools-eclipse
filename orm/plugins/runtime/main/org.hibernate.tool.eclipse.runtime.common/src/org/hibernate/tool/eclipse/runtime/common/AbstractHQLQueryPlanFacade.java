package org.hibernate.tool.eclipse.runtime.common;

import org.hibernate.tool.eclipse.common.runtime.Util;

import java.util.ArrayList;

import org.hibernate.tool.eclipse.runtime.spi.IHQLQueryPlan;
import org.hibernate.tool.eclipse.runtime.spi.IQueryTranslator;

public abstract class AbstractHQLQueryPlanFacade 
extends AbstractFacade 
implements IHQLQueryPlan {

	protected IQueryTranslator[] translators = null;
	
	public AbstractHQLQueryPlanFacade(
			IFacadeFactory facadeFactory, 
			Object target) {
		super(facadeFactory, target);
	}

	@Override
	public IQueryTranslator[] getTranslators() {
		if (translators == null) {
			initializeTranslators();
		}
		return translators;
	}
	
	protected void initializeTranslators() {
		Object[] targetTranslators = (Object[])Util.invokeMethod(
				getTarget(), 
				"getTranslators", 
				new Class[] {}, 
				new Object[] {});
		ArrayList<IQueryTranslator> destination = 
				new ArrayList<IQueryTranslator>(targetTranslators.length);
		for (Object translator : targetTranslators) {
			destination.add(getFacadeFactory().createQueryTranslator(translator));
		}
		translators = destination.toArray(
				new IQueryTranslator[targetTranslators.length]);
	}

}
