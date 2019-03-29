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
package com.mangosteen.jacoco.report.internal.html.page;

import com.mangosteen.jacoco.core.analysis.ICoverageNode;
import com.mangosteen.jacoco.core.analysis.ISourceFileCoverage;
import com.mangosteen.jacoco.report.internal.html.table.ITableItem;
import com.mangosteen.jacoco.report.internal.ReportOutputFolder;
import com.mangosteen.jacoco.report.internal.html.resources.Styles;

/**
 * Table items representing a source file which cannot be linked.
 *
 */
final class SourceFileItem implements ITableItem {

	private final ICoverageNode node;

	SourceFileItem(final ISourceFileCoverage node) {
		this.node = node;
	}

	public String getLinkLabel() {
		return node.getName();
	}

	public String getLinkStyle() {
		return Styles.EL_SOURCE;
	}

	public String getLink(final ReportOutputFolder base) {
		return null;
	}

	public ICoverageNode getNode() {
		return node;
	}

}
