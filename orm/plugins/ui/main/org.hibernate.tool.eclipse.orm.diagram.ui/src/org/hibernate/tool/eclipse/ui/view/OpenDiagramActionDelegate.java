/*******************************************************************************
 * Copyright (c) 2007 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributor:
 *     Red Hat, Inc. - initial API and implementation
 ******************************************************************************/
package org.hibernate.tool.eclipse.ui.view;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.TreePath;
import org.eclipse.jface.viewers.TreeSelection;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.handlers.HandlerUtil;
import org.eclipse.ui.ide.IDE;
import org.hibernate.tool.eclipse.orm.console.core.ConsoleConfiguration;
import org.hibernate.tool.eclipse.orm.console.core.eclipse.HibernateConsoleCorePlugin;
import org.hibernate.tool.eclipse.orm.runtime.spi.IConfiguration;
import org.hibernate.tool.eclipse.orm.runtime.spi.IPersistentClass;
import org.hibernate.tool.eclipse.orm.diagram.ui.nls.Messages;
import org.hibernate.tool.eclipse.ui.diagram.UiPlugin;

public class OpenDiagramActionDelegate extends AbstractHandler {

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
    	ISelection sel = HandlerUtil.getCurrentSelection(event);
    	if (!(sel instanceof TreeSelection)) {
    		return null;
    	}
    	Map<ConsoleConfiguration, Set<IPersistentClass>> mapCC_PCs = new HashMap<ConsoleConfiguration, Set<IPersistentClass>>();
    	TreePath[] paths = ((TreeSelection)sel).getPaths();
    	for (int i = 0; i < paths.length; i++) {
    		final Object firstSegment = paths[i].getFirstSegment();
    		if (!(firstSegment instanceof ConsoleConfiguration)) {
    			continue;
    		}
    		final ConsoleConfiguration consoleConfig = (ConsoleConfiguration)(firstSegment);
			Set<IPersistentClass> setPC = mapCC_PCs.get(consoleConfig);
			if (null == setPC) {
				setPC = new HashSet<IPersistentClass>();
				mapCC_PCs.put(consoleConfig, setPC);
			}
    		Object last_el = paths[i].getLastSegment();
        	if (last_el instanceof IPersistentClass) {
    			IPersistentClass persClass = (IPersistentClass) last_el;
    			setPC.add(persClass);
    		} else if (last_el instanceof IConfiguration) {
    			IConfiguration config = (IConfiguration)last_el;
    			Iterator<IPersistentClass> it = config.getClassMappings();
    			while (it.hasNext()) {
        			setPC.add(it.next());
    			}
    		} else if (last_el instanceof ConsoleConfiguration) {
    			IConfiguration config = consoleConfig.getConfiguration();
    			if (config == null) {
    				try {
        				consoleConfig.build();
    				} catch (Exception he) {
    					HibernateConsoleCorePlugin.getDefault().logErrorMessage(
    						Messages.OpenDiagramActionDelegate_could_not_load_configuration +
    						' ' + consoleConfig.getName(), he);
    				}
					if (consoleConfig.hasConfiguration()) {
						consoleConfig.buildMappings();
					}
    				config = consoleConfig.getConfiguration();
    			}
    			if (config != null) {
	    			Iterator<IPersistentClass> it = config.getClassMappings();
	    			while (it.hasNext()) {
	        			setPC.add(it.next());
	    			}
    			}
    		}
		}    		
    	for (Iterator<ConsoleConfiguration> it = mapCC_PCs.keySet().iterator(); it.hasNext(); ) {
    		ConsoleConfiguration consoleConfiguration = it.next();
    		Set<IPersistentClass> setPC = mapCC_PCs.get(consoleConfiguration);
	    	try {
	    		openEditor(setPC, consoleConfiguration);
	    	} catch (PartInitException e) {
	    		HibernateConsoleCorePlugin.getDefault().logErrorMessage("Can't open mapping view.", e);		//$NON-NLS-1$
			}
    	}
		return null;
	}

	public IEditorPart openEditor(IPersistentClass persClass,
			ConsoleConfiguration consoleConfig) throws PartInitException {
		DiagramEditorInput input = new DiagramEditorInput(consoleConfig.getName(), persClass.getRootClass());
		IEditorPart result = IDE.openEditor(UiPlugin.getPage(), input, "org.hibernate.tool.eclipse.ui.diagram.editors.DiagramViewer");		//$NON-NLS-1$
		return result;
	}

	public IEditorPart openEditor(Set<IPersistentClass> setPC, ConsoleConfiguration consoleConfig) throws PartInitException {
		
		IPersistentClass[] rcArr = new IPersistentClass[setPC.size()];
		IPersistentClass persClass = null;
		int i = 0;
    	for (Iterator<IPersistentClass> it = setPC.iterator(); it.hasNext(); ) {
    		persClass = it.next();
    		rcArr[i++] = persClass.getRootClass();
    	}
		DiagramEditorInput input = new DiagramEditorInput(consoleConfig.getName(), rcArr);
		return IDE.openEditor(UiPlugin.getPage(), input, "org.hibernate.tool.eclipse.ui.diagram.editors.DiagramViewer");		//$NON-NLS-1$
	}
}