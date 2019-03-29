/*******************************************************************************
 * Copyright (c) 2009, 2019 Mountainminds GmbH & Co. KG and Contributors
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Marc R. Hoffmann - initial API and implementation
 *
 *******************************************************************************/
package com.mangosteen.jacoco.core;

/**
 * Static Meta information about JaCoCo.
 */
public final class JaCoCo {

	/** Qualified build version of the JaCoCo core library. */
	public static final String VERSION;

	/** Absolute URL of the current JaCoCo home page */
	public static final String HOMEURL;

	/** Name of the runtime package of this build */
	public static final String RUNTIMEPACKAGE;

	static {
		/*final ResourceBundle bundle = ResourceBundle
				.getBundle("jacoco");*/
		VERSION = "$qualified.bundle.version$";
		HOMEURL = "$jacoco.home.url$";
		RUNTIMEPACKAGE = "$jacoco.home.url$";
	}

	private JaCoCo() {
	}

}
