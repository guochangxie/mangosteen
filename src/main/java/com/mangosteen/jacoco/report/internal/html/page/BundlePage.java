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

import com.mangosteen.jacoco.core.analysis.IBundleCoverage;
import com.mangosteen.jacoco.core.analysis.ICoverageNode;
import com.mangosteen.jacoco.core.analysis.IPackageCoverage;
import com.mangosteen.jacoco.report.ISourceFileLocator;
import com.mangosteen.jacoco.report.internal.ReportOutputFolder;
import com.mangosteen.jacoco.report.internal.html.HTMLElement;
import com.mangosteen.jacoco.report.internal.html.IHTMLReportContext;

import java.io.IOException;

/**
 * Page showing coverage information for a bundle. The page contains a table
 * with all packages of the bundle.
 */
public class BundlePage extends TablePage<ICoverageNode> {

	private final ISourceFileLocator locator;

	private IBundleCoverage bundle;

	/**
	 * Creates a new visitor in the given context.
	 *
	 * @param bundle
	 *            coverage date for the bundle
	 * @param parent
	 *            optional hierarchical parent
	 * @param locator
	 *            source locator
	 * @param folder
	 *            base folder for this bundle
	 * @param context
	 *            settings context
	 */
	public BundlePage(final IBundleCoverage bundle, final ReportPage parent,
                      final ISourceFileLocator locator, final ReportOutputFolder folder,
                      final IHTMLReportContext context) {
		super(bundle.getPlainCopy(), parent, folder, context);
		this.bundle = bundle;
		this.locator = locator;
	}

	@Override
	public void render() throws IOException {
		renderPackages();
		super.render();
		// Don't keep the bundle structure in memory
		bundle = null;
	}

	private void renderPackages() throws IOException {
		for (final IPackageCoverage p : bundle.getPackages()) {
			if (!p.containsCode()) {
				continue;
			}
			final String packagename = p.getName();
			final String foldername = packagename.length() == 0 ? "default"
					: packagename.replace('/', '.');
			final PackagePage page = new PackagePage(p, this, locator,
					folder.subFolder(foldername), context);
			page.render();
			addItem(page);
		}
	}

	@Override
	protected String getOnload() {
		return "initialSort(['breadcrumb', 'coveragetable'])";
	}

	@Override
	protected String getFileName() {
		return "index.html";
	}

	@Override
	protected void content(HTMLElement body) throws IOException {
		if (bundle.getPackages().isEmpty()) {
			body.p().text("No class files specified.");
		} else if (!bundle.containsCode()) {
			body.p().text(
					"None of the analyzed classes contain code relevant for code coverage.");
		} else {
			super.content(body);
		}
	}

}
