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
package org.hibernate.tool.eclipse.orm.jdt.ui.internal;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.ui.text.java.ContentAssistInvocationContext;
import org.eclipse.jdt.ui.text.java.IJavaCompletionProposalComputer;
import org.eclipse.jdt.ui.text.java.JavaContentAssistInvocationContext;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.contentassist.ICompletionProposal;
import org.eclipse.jface.text.contentassist.IContextInformation;
import org.hibernate.tool.eclipse.orm.console.core.ConsoleConfiguration;
import org.hibernate.eclipse.console.HibernateBasePlugin;
import org.hibernate.tool.eclipse.orm.jdt.core.nls.Messages;
import org.hibernate.eclipse.ui.console.common.CompletionProposalsResult;
import org.hibernate.eclipse.ui.console.common.ConsoleExtensionUI;
import org.hibernate.eclipse.hqleditor.HQLCompletionProcessor;
import org.hibernate.tool.eclipse.orm.console.core.eclipse.HibernateProjectConsoleManager;
import org.hibernate.tool.eclipse.orm.runtime.spi.IRuntimeManager;

public class HQLJavaCompletionProposalComputer implements IJavaCompletionProposalComputer {

	HQLCompletionProcessor hqlProcessor;
	private String errorMessage;

	public HQLJavaCompletionProposalComputer() {
		super();
		hqlProcessor = new HQLCompletionProcessor(null);
	}

	ConsoleConfiguration getConfiguration(IJavaProject javaProject) {
		if(javaProject != null) {
			return HibernateProjectConsoleManager.getDefaultConsoleConfiguration(javaProject);
		} else {
			return null;
		}
	}
	public List<ICompletionProposal> computeCompletionProposals(ContentAssistInvocationContext context, IProgressMonitor monitor) {
		List<ICompletionProposal> proposals = new ArrayList<ICompletionProposal>();
		errorMessage = null;

		if (!(context instanceof JavaContentAssistInvocationContext)) {
			return proposals;
		}

		JavaContentAssistInvocationContext ctx = (JavaContentAssistInvocationContext)context;

		try {
				 ConsoleConfiguration consoleConfiguration = getConfiguration( ctx.getProject() );
				 if(consoleConfiguration!=null) {
					 String query = ""; //$NON-NLS-1$
					 int stringStart = getStringStart( ctx.getDocument(), ctx.getInvocationOffset() );
					 int stringEnd = getStringEnd( ctx.getDocument(), ctx.getInvocationOffset() );
					 query = ctx.getDocument().get(stringStart, stringEnd-stringStart );
					 IRuntimeManager runtimeManager = consoleConfiguration.getRuntimeManager();
					 ConsoleExtensionUI consoleExtensionUI = new ConsoleExtensionUI(runtimeManager);
					 CompletionProposalsResult codeCompletions = consoleExtensionUI.hqlCodeComplete(query, stringStart, ctx.getInvocationOffset() - stringStart);

					 errorMessage = codeCompletions.getErrorMessage();
					 proposals = codeCompletions.getCompletionProposals();
				 }
		} catch(RuntimeException re) {
			HibernateBasePlugin.getDefault().logErrorMessage( Messages.HQLJavaCompletionProposalComputer_errormessage, re );
		}
		catch (BadLocationException e) {
			HibernateBasePlugin.getDefault().logErrorMessage( Messages.HQLJavaCompletionProposalComputer_errormessage, e );
		}

		return proposals;
	}

	public int getStringStart(IDocument document, int location) throws BadLocationException {

		if (document == null) {
			return -1;
		}

		int end = location;
		int start = end;
		while (--start >= 0) {
			if ('"'==document.getChar(start)) {
				break;
			}
		}
		start++;

		return start;
	}

	public int getStringEnd(IDocument document, int location) throws BadLocationException {

		if (document == null) {
			return -1;
		}

		int end = document.getLength();
		int start = location;
		while (start < end) {
			char c = document.getChar(start);
			if ('"'==c) {
				break;
			}
			start++;
		}
		return start;
	}

	public List<IContextInformation> computeContextInformation(ContentAssistInvocationContext context, IProgressMonitor monitor) {
		return Collections.emptyList();
	}

	public String getErrorMessage() {
		return errorMessage;
	}

	public void sessionEnded() {

	}

	public void sessionStarted() {

	}

}
