package org.hibernate.tool.eclipse.orm.runtime.v_6_0.internal;

import java.util.ArrayList;
import java.util.Iterator;

import org.hibernate.mapping.Column;
import org.hibernate.mapping.ForeignKey;
import org.hibernate.tool.eclipse.orm.runtime.legacy.AbstractForeignKeyFacade;
import org.hibernate.tool.eclipse.orm.runtime.legacy.IFacadeFactory;
import org.hibernate.tool.eclipse.orm.runtime.spi.IColumn;

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
