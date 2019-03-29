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

/**
 * <p>
 * Coverage calculation and analysis. The coverage information is calculated
 * with an {@link com.mangosteen.jacoco.core.analysis.Analyzer} instance from class files
 * (target) and
 * {@linkplain com.mangosteen.jacoco.core.data.IExecutionDataVisitor execution data}
 * (actual).
 * </p>
 *
 * <p>
 * The {@link com.mangosteen.jacoco.core.analysis.CoverageBuilder} creates a hierarchy of
 * {@link com.mangosteen.jacoco.core.analysis.ICoverageNode} instances with the following
 * {@link com.mangosteen.jacoco.core.analysis.ICoverageNode.ElementType types}:
 * </p>
 *
 * <pre>
 * +-- {@linkplain com.mangosteen.jacoco.core.analysis.ICoverageNode.ElementType#GROUP Group} (optional)
 *     +-- {@linkplain com.mangosteen.jacoco.core.analysis.ICoverageNode.ElementType#BUNDLE Bundle}
 *         +-- {@linkplain com.mangosteen.jacoco.core.analysis.ICoverageNode.ElementType#PACKAGE Package}
 *             +-- {@linkplain com.mangosteen.jacoco.core.analysis.ICoverageNode.ElementType#SOURCEFILE Source File}
 *                 +-- {@linkplain com.mangosteen.jacoco.core.analysis.ICoverageNode.ElementType#CLASS Class}
 *                     +-- {@linkplain com.mangosteen.jacoco.core.analysis.ICoverageNode.ElementType#METHOD Method}
 * </pre>
 */
package com.mangosteen.jacoco.core.analysis;
