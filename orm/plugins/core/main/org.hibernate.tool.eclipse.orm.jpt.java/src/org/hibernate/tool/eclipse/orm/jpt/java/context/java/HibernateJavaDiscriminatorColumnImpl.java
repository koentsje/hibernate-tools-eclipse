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

package org.hibernate.tool.eclipse.orm.jpt.java.context.java;

import java.util.List;

import org.eclipse.jpt.jpa.core.context.java.JavaSpecifiedDiscriminatorColumn;
import org.eclipse.jpt.jpa.core.internal.jpa1.context.java.GenericJavaDiscriminatorColumn;
import org.eclipse.jpt.jpa.core.validation.JptJpaCoreValidationMessages;
import org.eclipse.jpt.jpa.db.Column;
import org.eclipse.jpt.jpa.db.Table;
import org.eclipse.wst.validation.internal.provisional.core.IMessage;
import org.eclipse.wst.validation.internal.provisional.core.IReporter;
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
public class HibernateJavaDiscriminatorColumnImpl extends
		GenericJavaDiscriminatorColumn implements
		HibernateJavaDiscriminatorColumn {

	public HibernateJavaDiscriminatorColumnImpl(JavaSpecifiedDiscriminatorColumn.ParentAdapter parent) {
		super(parent);
	}

	@Override
	public IHibernateJpaProject getJpaProject() {
		return (IHibernateJpaProject) super.getJpaProject();
	}

	@Override
	public Column getDbColumn() {
		Table table = this.getDbTable();
		return (table == null) ? null : table.getColumnForIdentifier(this.getDBColumnName());
	}

	public String getDBColumnName(){
		return getSpecifiedDBColumnName() != null ? getSpecifiedDBColumnName()
				: getDefaultDBColumnName();
	}

	public String getSpecifiedDBColumnName(){
		if (getSpecifiedName() == null) return null;
		INamingStrategy ns = getJpaProject().getNamingStrategy();
		if (getJpaProject().isNamingStrategyEnabled() && ns != null){
			try {
				return ns.columnName(getSpecifiedName());
			} catch (Exception e) {
				IMessage m = HibernateJpaValidationMessage.buildMessage(IMessage.HIGH_SEVERITY,
						Messages.NAMING_STRATEGY_EXCEPTION, this);
				Platform.getLog(getClass()).log(new Status(IStatus.ERROR, getClass(), m.getText(), e));
			}
		}
		return this.getSpecifiedName();
	}
	
	public String getDefaultDBColumnName() {
		return getDefaultName();
	}
	
	@Override
	public void validate(List<IMessage> messages, IReporter reporter) {
		super.validate(messages, reporter);
		if (this.connectionProfileIsActive()) {
			if ( ! this.isResolved()) {
				messages.add(
					HibernateJpaValidationMessage.buildMessage(
						IMessage.HIGH_SEVERITY,
						JptJpaCoreValidationMessages.DISCRIMINATOR_COLUMN_UNRESOLVED_NAME.getID(),
						new String[] {this.getDBColumnName()}, 
						this,
						this.getNameValidationTextRange()
					)
				);
			}
		}
	}
}
