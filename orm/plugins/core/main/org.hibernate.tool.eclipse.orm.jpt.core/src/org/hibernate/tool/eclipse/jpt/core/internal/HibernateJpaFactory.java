/*******************************************************************************
 * Copyright (c) 2008-2010 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributor:
 *     Red Hat, Inc. - initial API and implementation
 ******************************************************************************/
package org.hibernate.tool.eclipse.jpt.core.internal;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jpt.common.core.resource.java.JavaResourcePackage;
import org.eclipse.jpt.jpa.core.JpaProject;
import org.eclipse.jpt.jpa.core.context.PersistentType;
import org.eclipse.jpt.jpa.core.context.java.JavaEmbeddable;
import org.eclipse.jpt.jpa.core.context.java.JavaPersistentType;
import org.eclipse.jpt.jpa.core.resource.java.EmbeddableAnnotation;
import org.hibernate.tool.eclipse.orm.jpt.java.context.java.HibernateAbstractJpaFactory;
import org.hibernate.tool.eclipse.orm.jpt.java.context.java.HibernateJavaEmbeddable;
import org.hibernate.tool.eclipse.orm.jpt.java.context.java.HibernatePackageInfo;
import org.hibernate.tool.eclipse.orm.jpt.java.context.java.HibernatePackageInfoImpl;
import org.hibernate.tool.eclipse.orm.runtime.spi.IService;
import org.hibernate.tool.eclipse.orm.runtime.spi.RuntimeServiceManager;


/**
 * @author Dmitry Geraskov
 *
 */
public class HibernateJpaFactory extends HibernateAbstractJpaFactory {

	// ********** Core Model **********

	@Override
	public JpaProject buildJpaProject(JpaProject.Config config, IProgressMonitor monitor) {
		return new HibernateJpaProject(config, monitor);
	}

	@Override
	public HibernatePackageInfo buildJavaPackageInfo(
			PersistentType.Parent parent, JavaResourcePackage jrpt) {
		return new HibernatePackageInfoImpl(parent, jrpt);
	}

	// ********** Hibernate Specific **********

	@Override
	public JavaEmbeddable buildJavaEmbeddable(JavaPersistentType parent, EmbeddableAnnotation embeddableAnnotation) {
		return new HibernateJavaEmbeddable(parent, embeddableAnnotation);
	}

	@Override
	public IService getHibernateService() {
		return RuntimeServiceManager.getInstance().findService("3.6");
	}

}
