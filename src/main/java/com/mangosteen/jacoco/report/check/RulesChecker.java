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
package com.mangosteen.jacoco.report.check;

import com.mangosteen.jacoco.core.analysis.IBundleCoverage;
import com.mangosteen.jacoco.core.data.ExecutionData;
import com.mangosteen.jacoco.core.data.SessionInfo;
import com.mangosteen.jacoco.report.*;


import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Formatter which checks a set of given rules and reports violations to a
 * {@link IViolationsOutput} instance.
 */
public class RulesChecker {

	private List<Rule> rules;
	private ILanguageNames languageNames;

	/**
	 * New formatter instance.
	 */
	public RulesChecker() {
		this.rules = new ArrayList<Rule>();
		this.setLanguageNames(new JavaNames());
	}

	/**
	 * Sets the rules to check by this formatter.
	 *
	 * @param rules
	 *            rules to check
	 */
	public void setRules(final List<Rule> rules) {
		this.rules = rules;
	}

	/**
	 * Sets the implementation for language name display for message formatting.
	 * Java language names are defined by default.
	 *
	 * @param languageNames
	 *            converter for language specific names
	 */
	public void setLanguageNames(final ILanguageNames languageNames) {
		this.languageNames = languageNames;
	}

	/**
	 * Creates a new visitor to process the configured checks.
	 *
	 * @param output
	 *            call-back to report violations to
	 * @return visitor to emit the report data to
	 */
	public IReportVisitor createVisitor(final IViolationsOutput output) {
		final BundleChecker bundleChecker = new BundleChecker(rules,
				languageNames, output);
		return new IReportVisitor() {

			public IReportGroupVisitor visitGroup(final String name)
					throws IOException {
				return this;
			}

			public void visitBundle(final IBundleCoverage bundle,
					final ISourceFileLocator locator) throws IOException {
				bundleChecker.checkBundle(bundle);
			}

			public void visitInfo(final List<SessionInfo> sessionInfos,
					final Collection<ExecutionData> executionData)
					throws IOException {
			}

			public void visitEnd() throws IOException {
			}
		};
	}

}
