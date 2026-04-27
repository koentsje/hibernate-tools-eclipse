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
import java.util.List;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.StructuredViewer;
import org.eclipse.ui.actions.SelectionListenerAction;
import org.hibernate.tool.eclipse.orm.console.core.ConsoleConfiguration;
import org.hibernate.tool.eclipse.orm.console.core.KnownConfigurations;
import org.hibernate.tool.eclipse.orm.base.ui.nls.Messages;

/**
 * @author max
 *
 */
public class DeleteConfigurationAction extends SelectionListenerAction {

	public static final String DELETECONFIG_ACTIONID = "actionid.deleteconfig"; //$NON-NLS-1$

	private StructuredViewer part;

	public DeleteConfigurationAction(StructuredViewer selectionProvider) {
		super(Messages.DeleteConfigurationAction_delete_config);
		setEnabled(false);
		this.part = selectionProvider;
		setId(DELETECONFIG_ACTIONID);
	}

	public void run() {
		List<?> selectedNonResources = getSelectedNonResources();
		boolean ccSelected = false;
		Iterator<?> iter = selectedNonResources.iterator();
		while (iter.hasNext() ) {
			if (iter.next() instanceof ConsoleConfiguration) {
				ccSelected = true;
				break;
			}
		}
		
		if (!ccSelected) return;
		
		String question =  Messages.DeleteConfigurationAction_do_you_wish_del_selected_config;
		String title = Messages.DeleteConfigurationAction_delete_console_config;
		if (selectedNonResources.size() > 1){
			question += Messages.DeleteConfigurationAction_str_1;
			title += Messages.DeleteConfigurationAction_str_2;
		}
		question += Messages.DeleteConfigurationAction_str_3;

		if( MessageDialog.openConfirm( null, title, question)) {
			iter = selectedNonResources.iterator();
			while (iter.hasNext() ) {
				Object selElement = iter.next();
				if (selElement instanceof ConsoleConfiguration) {
					ConsoleConfiguration element = (ConsoleConfiguration) selElement;
					KnownConfigurations.getInstance().removeConfiguration(element, false);
				}
			}

			part.refresh();
		}
	}

	protected boolean updateSelection(IStructuredSelection selection) {
		if(!selection.isEmpty() ) {
			Iterator<?> iter = getSelectedNonResources().iterator();
			while (iter.hasNext() ) {
				Object element = iter.next();
				if(element instanceof ConsoleConfiguration) {
					return true;
				}
			}
		}
		return false;
	}
}
