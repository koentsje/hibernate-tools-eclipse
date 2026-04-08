package org.hibernate.tool.eclipse.search.runtime.common;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.tool.eclipse.common.runtime.Util;
import org.hibernate.tool.eclipse.search.runtime.spi.IDocument;
import org.hibernate.tool.eclipse.search.runtime.spi.IField;

public abstract class AbstractDocument extends AbstractFacade implements IDocument {

	public AbstractDocument(IFacadeFactory facadeFactory, Object target) {
		super(facadeFactory, target);
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public List<IField> getFields() {
		List<Object> targetFields =
				(List<Object>)Util.invokeMethod(getTarget(), "getFields", new Class[] {}, new Object[] {});
		List<IField> fields = new ArrayList<IField>();
		for (Object targetField: targetFields) {
			fields.add(new AbstractField(getFacadeFactory(), targetField) {	});
		}
		return fields;
	}
}
