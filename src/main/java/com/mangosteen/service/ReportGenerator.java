/*******************************************************************************
 * Copyright (c) 2009, 2018 Mountainminds GmbH & Co. KG and Contributors
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Brock Janiczak - initial API and implementation
 *
 *******************************************************************************/
package com.mangosteen.service;

import com.mangosteen.jacoco.core.analysis.Analyzer;
import com.mangosteen.jacoco.core.analysis.CoverageBuilder;
import com.mangosteen.jacoco.core.analysis.IBundleCoverage;
import com.mangosteen.jacoco.core.tools.ExecFileLoader;
import com.mangosteen.jacoco.report.DirectorySourceFileLocator;
import com.mangosteen.jacoco.report.FileMultiReportOutput;
import com.mangosteen.jacoco.report.IReportVisitor;
import com.mangosteen.jacoco.report.html.HTMLFormatter;


import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * This example creates a HTML report for eclipse like projects based on a
 * single execution data store called jacoco.exec. The report contains no
 * grouping information.
 *
 * The class files under test must be compiled with debug information, otherwise
 * source highlighting will not work.
 */
public class ReportGenerator {

	private final String title;

	private  File executionDataFile;
	private  File classesDirectory;
	private  File sourceDirectory;
	private  File reportDirectory;

	private List<String> changefiles;
	private ExecFileLoader execFileLoader;

	/**
	 * Create a new generator based for the given project.
	 *
	 * @param projectDirectory
	 */
	public ReportGenerator(final File projectDirectory) {
		this.title = projectDirectory.getName();

	}

	/**
	 * Create the report.
	 *
	 * @throws IOException
	 */
	public void create() throws IOException {

		// Read the jacoco.exec file. Multiple data files could be merged
		// at this point
		loadExecutionData();

		// Run the structure analyzer on a single class folder to build up
		// the coverage model. The process would be similar if your classes
		// were in a jar file. Typically you would create a bundle for each
		// class folder and each jar you want in your report. If you have
		// more than one bundle you will need to add a grouping node to your
		// report
		final IBundleCoverage bundleCoverage = analyzeStructure();

		createReport(bundleCoverage);

	}

	private void createReport(final IBundleCoverage bundleCoverage)
			throws IOException {

		// Create a concrete report visitor based on some supplied
		// configuration. In this case we use the defaults
		final HTMLFormatter htmlFormatter = new HTMLFormatter();
		final IReportVisitor visitor = htmlFormatter
				.createVisitor(new FileMultiReportOutput(reportDirectory));

		// Initialize the report with all of the execution and session
		// information. At this point the report doesn't know about the
		// structure of the report being created
		visitor.visitInfo(execFileLoader.getSessionInfoStore().getInfos(),
				execFileLoader.getExecutionDataStore().getContents());

		// Populate the report structure with the bundle coverage information.
		// Call visitGroup if you need groups in your report.
		visitor.visitBundle(bundleCoverage, new DirectorySourceFileLocator(
				sourceDirectory, "utf-8", 4));

		// Signal end of structure information to allow report to write all
		// information out
		visitor.visitEnd();

	}

	private void loadExecutionData() throws IOException {
		execFileLoader = new ExecFileLoader();
		execFileLoader.load(executionDataFile);


	}

	private IBundleCoverage analyzeStructure() throws IOException {
		final CoverageBuilder coverageBuilder = new CoverageBuilder();
		final Analyzer analyzer = new Analyzer(
				execFileLoader.getExecutionDataStore(), coverageBuilder);
		analyzer.setChangeClassFiles(changefiles);
		analyzer.analyzeAll(classesDirectory);

		return coverageBuilder.getBundle(title);
	}

	public File getExecutionDataFile() {
		return executionDataFile;
	}

	public void setExecutionDataFile(File executionDataFile) {
		this.executionDataFile = executionDataFile;
	}

	public File getClassesDirectory() {
		return classesDirectory;
	}

	public void setClassesDirectory(File classesDirectory) {
		this.classesDirectory = classesDirectory;
	}

	public File getSourceDirectory() {
		return sourceDirectory;
	}

	public void setSourceDirectory(File sourceDirectory) {
		this.sourceDirectory = sourceDirectory;
	}

	public File getReportDirectory() {
		return reportDirectory;
	}

	public void setReportDirectory(File reportDirectory) {
		this.reportDirectory = reportDirectory;
	}

	public void setChangefiles(List<String> changefiles) {
		this.changefiles = changefiles;
	}

	/**
	 * Starts the report generation process
	 *
	 * @param args
	 *            Arguments to the application. This will be the location of the
	 *            eclipse projects that will be used to generate reports for
	 * @throws IOException
	 */




	public static void main(final String[] args) throws IOException {
	/*	for (int i = 0; i < args.length; i++) {
			final ReportGenerator generator = new ReportGenerator(new File(
					args[i]));
			generator.create();
		}*/
	final  ReportGenerator generator=new ReportGenerator(new File("/Users/guochang.xie/Documents/tmp/ci-jacoco"));
	       generator.create();
	}

}
