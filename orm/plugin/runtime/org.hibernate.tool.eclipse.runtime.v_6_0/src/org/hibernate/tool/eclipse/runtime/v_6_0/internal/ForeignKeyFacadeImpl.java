package org.hibernate.tool.eclipse.runtime.v_6_0.internal;

import java.util.ArrayList;
import java.util.Iterator;

import org.hibernate.mapping.Column;
import org.hibernate.mapping.ForeignKey;
import org.hibernate.tool.eclipse.runtime.common.AbstractForeignKeyFacade;
import org.hibernate.tool.eclipse.runtime.common.IFacadeFactory;
import org.hibernate.tool.eclipse.runtime.spi.IColumn;

public class ForeignKeyFacadeImpl extends AbstractForeignKeyFacade {

	public ForeignKeyFacadeImpl(IFacadeFactory facadeFactory, Object target) {
		super(facadeFactory, target);
	}
	
	@Override
	public Iterator<IColumn> columnIterator() {
		ArrayList<IColumn> facades = new ArrayList<IColumn>();
		Iterator<Column> iterator = ((ForeignKey)getTarget()).getColumnIterator();
		while (iterator.hasNext()) {
			facades.add(getFacadeFactory().createColumn(iterator.next()));
		}
		return facades.iterator();
	}

}
