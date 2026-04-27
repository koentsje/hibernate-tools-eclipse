/*
 * JBoss, Home of Professional Open Source
 * Copyright 2005, JBoss Inc., and individual contributors as indicated
 * by the @authors tag. See the copyright.txt in the distribution for a
 * full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */
package org.hibernate.tool.eclipse.orm.base.ui.console.actions;

import java.util.Iterator;

import org.eclipse.jface.viewers.ISelectionProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.actions.SelectionListenerAction;
import org.hibernate.tool.eclipse.orm.console.core.ui.ImageConstants;
import org.hibernate.tool.eclipse.orm.console.core.KnownConfigurations;
import org.hibernate.tool.eclipse.orm.query.IQueryPage;
import org.hibernate.tool.eclipse.orm.base.ui.nls.Messages;
import org.hibernate.tool.eclipse.orm.base.ui.ui.console.utils.EclipseImages;

/**
 * @author max
 *
 */
public class CloseQueryPageAction extends SelectionListenerAction {

	private final ISelectionProvider selectionProvider;

	/**
	 * @param text
	 */
	public CloseQueryPageAction(ISelectionProvider selectionProvider) {
		super(""); //$NON-NLS-1$
		this.selectionProvider = selectionProvider;
		this.selectionProvider.addSelectionChangedListener(this);
		setEnabled(!this.selectionProvider.getSelection().isEmpty() );

		setImageDescriptor(EclipseImages.getImageDescriptor(ImageConstants.CLOSE) );
		setDisabledImageDescriptor(EclipseImages.getImageDescriptor(ImageConstants.CLOSE_DISABLED) );

		setToolTipText(Messages.CloseQueryPageAction_close_query_page);
	}

	public void run() {
		IStructuredSelection selection =
			(IStructuredSelection) this.selectionProvider.getSelection();
		if (!selection.isEmpty() ) {
			for (Iterator<?> i = selection.iterator(); i.hasNext(); ) {
				KnownConfigurations.getInstance().getQueryPageModel().remove( (IQueryPage) i.next() );
			}
		}
	}

	public boolean updateSelection(IStructuredSelection selection) {
        return !selection.isEmpty();
	}

}
