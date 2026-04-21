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
package org.hibernate.tool.eclipse.orm.query;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * @author max
 */
public class QueryPageModel {

	public interface Listener {
		void pageAdded(QueryPageModel model);
		void pagesChanged(QueryPageModel model);
	}

	private final List<IQueryPage> pages = new ArrayList<IQueryPage>();
	private final List<Listener> listeners = new ArrayList<Listener>();

	PropertyChangeListener pcl = new PropertyChangeListener() {
		public void propertyChange(PropertyChangeEvent evt) {
			firePagesChanged();
		}
	};

	public int getSize() {
		return pages.size();
	}

	public IQueryPage get(int index) {
		return pages.get(index);
	}

	public void add(IQueryPage qp) {
		for (int i = pages.size() - 1; i >= 0; i--) {
			IQueryPage element = pages.get(i);
			if (!element.isSticky()) {
				pages.remove(i);
			}
		}
		pages.add(qp);
		firePageAdded();
	}

	public void remove(int i) {
		IQueryPage qp = pages.remove(i);
		if (qp != null) {
			qp.removePropertyChangeListener(pcl);
		}
		firePagesChanged();
	}

	public boolean remove(IQueryPage page) {
		boolean b = pages.remove(page);
		if (b) {
			page.release();
			firePagesChanged();
		}
		return b;
	}

	public Iterator<IQueryPage> getPages() {
		return pages.iterator();
	}

	public List<IQueryPage> getPagesAsList() {
		return new ArrayList<IQueryPage>(pages);
	}

	public void addListener(Listener listener) {
		listeners.add(listener);
	}

	public void removeListener(Listener listener) {
		listeners.remove(listener);
	}

	private void firePageAdded() {
		for (Listener listener : listeners) {
			listener.pageAdded(this);
		}
	}

	private void firePagesChanged() {
		for (Listener listener : listeners) {
			listener.pagesChanged(this);
		}
	}
}
