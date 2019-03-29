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
import com.mangosteen.jacoco.report.internal.html.table.ITableItem;
import com.mangosteen.jacoco.report.internal.ReportOutputFolder;
import com.mangosteen.jacoco.report.internal.html.HTMLElement;
import com.mangosteen.jacoco.report.internal.html.IHTMLReportContext;
import com.mangosteen.jacoco.report.internal.html.resources.Resources;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Report page that contains a table of items linked to other pages.
 *
 * @param <NodeType>
 *            type of the node represented by this page
 */
public abstract class TablePage<NodeType extends ICoverageNode>
		extends NodePage<NodeType> {

	private final List<ITableItem> items = new ArrayList<ITableItem>();

	/**
	 * Creates a new node page.
	 *
	 * @param node
	 *            corresponding node
	 * @param parent
	 *            optional hierarchical parent
	 * @param folder
	 *            base folder to create this report in
	 * @param context
	 *            settings context
	 */
	protected TablePage(final NodeType node, final ReportPage parent,
			final ReportOutputFolder folder, final IHTMLReportContext context) {
		super(node, parent, folder, context);
	}

	/**
	 * Adds the given item to the table. Method must be called before the page
	 * is rendered.
	 *
	 * @param item
	 *            table item to add
	 */
	public void addItem(final ITableItem item) {
		items.add(item);
	}

	@Override
	protected void head(final HTMLElement head) throws IOException {
		super.head(head);
		head.script(
				context.getResources().getLink(folder, Resources.SORT_SCRIPT));
	}

	@Override
	protected void content(final HTMLElement body) throws IOException {
		context.getTable().render(body, items, getNode(),
				context.getResources(), folder);
		// free memory, otherwise we will keep the complete page tree:
		items.clear();
	}

}
