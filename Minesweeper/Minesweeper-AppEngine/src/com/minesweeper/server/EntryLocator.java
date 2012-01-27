package com.minesweeper.server;

import com.google.web.bindery.requestfactory.shared.Locator;

public class EntryLocator extends Locator<Entry, Long>{

	@Override
	public Entry create(Class<? extends Entry> clazz) {
		return new Entry();
	}

	@Override
	public Entry find(Class<? extends Entry> clazz, Long id) {
		return create(clazz);
	}

	@Override
	public Class<Entry> getDomainType() {
		return Entry.class;
	}

	@Override
	public Long getId(Entry domainObject) {
		return domainObject.getId();
	}

	@Override
	public Class<Long> getIdType() {
		return Long.class;
	}

	@Override
	public Object getVersion(Entry domainObject) {
		return domainObject.getVersion();
	}

}
