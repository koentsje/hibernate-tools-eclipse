/*******************************************************************************
 * Copyright (c) 2007-2009 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributor:
 *     Red Hat, Inc. - initial API and implementation
 ******************************************************************************/
package org.hibernate.tool.eclipse.ui.diagram.editors.command;

import org.eclipse.gef.commands.Command;
import org.hibernate.tool.eclipse.ui.diagram.DiagramViewerMessages;
import org.hibernate.tool.eclipse.ui.diagram.rulers.DiagramGuide;
import org.hibernate.tool.eclipse.ui.diagram.rulers.DiagramRuler;

/**
 *
 */
public class CreateGuideCommand extends Command {

	private DiagramGuide guide;
	private DiagramRuler parent;
	private int position;

	public CreateGuideCommand(DiagramRuler parent, int position) {
		super(DiagramViewerMessages.CreateGuideCommand_Label);
		this.parent = parent;
		this.position = position;
	}

	public boolean canUndo() {
		return true;
	}

	public void execute() {
		if (guide == null) {
			guide = new DiagramGuide(!parent.isHorizontal());
		}
		guide.setPosition(position);
		parent.addGuide(guide);
	}

	public void undo() {
		parent.removeGuide(guide);
	}

}
