/*******************************************************************************
 * Copyright (c) 2009, 2019 Mountainminds GmbH & Co. KG and Contributors
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Brock Janiczak - initial API and implementation
 *
 *******************************************************************************/
package com.mangosteen.jacoco.report.csv;

import com.mangosteen.jacoco.core.analysis.IClassCoverage;
import com.mangosteen.jacoco.core.analysis.ICounter;
import com.mangosteen.jacoco.core.analysis.ICoverageNode;
import com.mangosteen.jacoco.report.ILanguageNames;

import java.io.IOException;

/**
 * Writer for rows in the CVS report representing the summary data of a single
 * class.
 */
class ClassRowWriter {

	private static final ICoverageNode.CounterEntity[] COUNTERS = { ICoverageNode.CounterEntity.INSTRUCTION,
			ICoverageNode.CounterEntity.BRANCH, ICoverageNode.CounterEntity.LINE,
			ICoverageNode.CounterEntity.COMPLEXITY, ICoverageNode.CounterEntity.METHOD };

	private final DelimitedWriter writer;

	private final ILanguageNames languageNames;

	/**
	 * Creates a new row writer that writes class information to the given CSV
	 * writer.
	 *
	 * @param writer
	 *            writer for csv output
	 * @param languageNames
	 *            converter for Java identifiers
	 * @throws IOException
	 *             in case of problems with the writer
	 */
	public ClassRowWriter(final DelimitedWriter writer,
			final ILanguageNames languageNames) throws IOException {
		this.writer = writer;
		this.languageNames = languageNames;
		writeHeader();
	}

	private void writeHeader() throws IOException {
		writer.write("GROUP", "PACKAGE", "CLASS");
		for (final ICoverageNode.CounterEntity entity : COUNTERS) {
			writer.write(entity.name() + "_MISSED");
			writer.write(entity.name() + "_COVERED");
		}
		writer.nextLine();
	}

	/**
	 * Writes the class summary information as a row.
	 *
	 * @param groupName
	 *            name of the group
	 * @param packageName
	 *            vm name of the package
	 * @param node
	 *            class coverage data
	 * @throws IOException
	 *             in case of problems with the writer
	 */
	public void writeRow(final String groupName, final String packageName,
			final IClassCoverage node) throws IOException {
		writer.write(groupName);
		writer.write(languageNames.getPackageName(packageName));
		final String className = languageNames.getClassName(node.getName(),
				node.getSignature(), node.getSuperName(),
				node.getInterfaceNames());
		writer.write(className);

		for (final ICoverageNode.CounterEntity entity : COUNTERS) {
			final ICounter counter = node.getCounter(entity);
			writer.write(counter.getMissedCount());
			writer.write(counter.getCoveredCount());
		}

		writer.nextLine();
	}

}
