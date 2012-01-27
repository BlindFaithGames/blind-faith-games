package com.minesweeper.server;

import com.google.web.bindery.requestfactory.shared.Locator;

/**
 * Clase necesaria para el almacenamiento de POJOS (objetos Java antiguos y simples)
 *
 */
public class LogLocator extends Locator<Log, Void> {

	@Override
	public Log create(Class<? extends Log> clazz) {
		return new Log();
	}

	@Override
	public Log find(Class<? extends Log> clazz, Void id) {
		return create(clazz);
	}

	@Override
	public Class<Log> getDomainType() {
		return Log.class;
	}

	@Override
	public Void getId(Log domainObject) {
		return null;
	}

	@Override
	public Class<Void> getIdType() {
		return Void.class;
	}

	@Override
	public Object getVersion(Log domainObject) {
		return null;
	}

}
