/*******************************************************************************
 * Copyright (c) 2009-2011 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributor:
 *     Red Hat, Inc. - initial API and implementation
 ******************************************************************************/

package org.hibernate.tool.eclipse.orm.jpt.mapping.context.orm;

import org.eclipse.jpt.jpa.core.context.NamedColumn;
import org.eclipse.jpt.jpa.core.context.orm.OrmPersistentAttribute;
import org.eclipse.jpt.jpa.core.context.orm.OrmSpecifiedPersistentAttribute;
import org.eclipse.jpt.jpa.core.internal.context.orm.AbstractOrmBasicMapping;
import org.eclipse.jpt.jpa.core.resource.orm.XmlBasic;
import org.eclipse.wst.validation.internal.provisional.core.IMessage;
import org.hibernate.tool.eclipse.orm.jpt.api.context.IHibernateJpaProject;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.Status;
import org.hibernate.tool.eclipse.orm.jpt.api.context.Messages;
import org.hibernate.tool.eclipse.orm.jpt.api.validation.HibernateJpaValidationMessage;
import org.hibernate.tool.eclipse.orm.runtime.spi.INamingStrategy;

/**
 * @author Dmitry Geraskov
 *
 */
public class HibernateOrmBasicMapping extends AbstractOrmBasicMapping<XmlBasic> {

	public HibernateOrmBasicMapping(OrmSpecifiedPersistentAttribute parent,
			XmlBasic resourceMapping) {
		super(parent, resourceMapping);
	}

	@Override
	public IHibernateJpaProject getJpaProject() {
		return (IHibernateJpaProject) super.getJpaProject();
	}

	@Override
	public String getDefaultColumnName(NamedColumn column) {
		if (getName() != null){
			INamingStrategy ns = getJpaProject().getNamingStrategy();
			if (getJpaProject().isNamingStrategyEnabled() && ns != null) {
				try {
					return ns.propertyToColumnName(getName());
				} catch (Exception e) {
					IMessage m = HibernateJpaValidationMessage.buildMessage(
							IMessage.HIGH_SEVERITY,
							Messages.NAMING_STRATEGY_EXCEPTION, this);
					Platform.getLog(getClass()).log(new Status(IStatus.ERROR, getClass(), m.getText(), e));
				}
			}
		}
		return super.getDefaultColumnName(column);
	}

}
