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

import com.mangosteen.jacoco.core.analysis.ICounter;
import com.mangosteen.jacoco.core.analysis.ICoverageNode;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Descriptor for a limit which is given by a {@link Rule}.
 */
public class Limit {

	private static final Map<ICounter.CounterValue, String> VALUE_NAMES;
	private static final Map<ICoverageNode.CounterEntity, String> ENTITY_NAMES;

	static {
		final Map<ICounter.CounterValue, String> values = new HashMap<ICounter.CounterValue, String>();
		values.put(ICounter.CounterValue.TOTALCOUNT, "total count");
		values.put(ICounter.CounterValue.MISSEDCOUNT, "missed count");
		values.put(ICounter.CounterValue.COVEREDCOUNT, "covered count");
		values.put(ICounter.CounterValue.MISSEDRATIO, "missed ratio");
		values.put(ICounter.CounterValue.COVEREDRATIO, "covered ratio");
		VALUE_NAMES = Collections.unmodifiableMap(values);

		final Map<ICoverageNode.CounterEntity, String> entities = new HashMap<ICoverageNode.CounterEntity, String>();
		entities.put(ICoverageNode.CounterEntity.INSTRUCTION, "instructions");
		entities.put(ICoverageNode.CounterEntity.BRANCH, "branches");
		entities.put(ICoverageNode.CounterEntity.COMPLEXITY, "complexity");
		entities.put(ICoverageNode.CounterEntity.LINE, "lines");
		entities.put(ICoverageNode.CounterEntity.METHOD, "methods");
		entities.put(ICoverageNode.CounterEntity.CLASS, "classes");
		ENTITY_NAMES = Collections.unmodifiableMap(entities);
	}

	private ICoverageNode.CounterEntity entity;

	private ICounter.CounterValue value;

	private BigDecimal minimum;

	private BigDecimal maximum;

	/**
	 * Creates a new instance with the following defaults:
	 * <ul>
	 * <li>counter entity: {@link ICoverageNode.CounterEntity#INSTRUCTION}
	 * <li>counter value: {@link ICounter.CounterValue#COVEREDRATIO}
	 * <li>minimum: no limit
	 * <li>maximum: no limit
	 * </ul>
	 */
	public Limit() {
		this.entity = ICoverageNode.CounterEntity.INSTRUCTION;
		this.value = ICounter.CounterValue.COVEREDRATIO;
	}

	/**
	 * @return the configured counter entity to check
	 */
	public ICoverageNode.CounterEntity getEntity() {
		return entity;
	}

	/**
	 * Sets the counter entity to check.
	 *
	 * @param entity
	 *            counter entity to check
	 * TODO: use CounterEntity directly once Maven 3 is required.
	 */
	public void setCounter(final String entity) {
		this.entity = ICoverageNode.CounterEntity.valueOf(entity);
	}

	/**
	 * @return the configured value to check
	 */
	public ICounter.CounterValue getValue() {
		return value;
	}

	/**
	 * Sets the value to check.
	 *
	 * @param value
	 *            value to check
	 * TODO: use CounterValue directly once Maven 3 is required.
	 */
	public void setValue(final String value) {
		this.value = ICounter.CounterValue.valueOf(value);
	}

	/**
	 * @return configured minimum value, or <code>null</code> if no minimum is
	 *         given
	 */
	public String getMinimum() {
		return minimum == null ? null : minimum.toPlainString();
	}

	/**
	 * Sets allowed maximum value as decimal string or percent representation.
	 * The given precision is also considered in error messages. Coverage ratios
	 * are given in the range from 0.0 to 1.0.
	 *
	 * @param minimum
	 *            allowed minimum or <code>null</code>, if no minimum should be
	 *            checked
	 */
	public void setMinimum(final String minimum) {
		this.minimum = parseValue(minimum);
	}

	/**
	 * @return configured maximum value, or <code>null</code> if no maximum is
	 *         given
	 */
	public String getMaximum() {
		return maximum == null ? null : maximum.toPlainString();
	}

	/**
	 * Sets allowed maximum value as decimal string or percent representation.
	 * The given precision is also considered in error messages. Coverage ratios
	 * are given in the range from 0.0 to 1.0.
	 *
	 * @param maximum
	 *            allowed maximum or <code>null</code>, if no maximum should be
	 *            checked
	 */
	public void setMaximum(final String maximum) {
		this.maximum = parseValue(maximum);
	}

	private static BigDecimal parseValue(final String value) {
		if (value == null) {
			return null;
		}

		final String trimmedValue = value.trim();
		if (trimmedValue.endsWith("%")) {
			final String percent = trimmedValue.substring(0, trimmedValue.length() - 1);
			return new BigDecimal(percent).movePointLeft(2);
		}

		return new BigDecimal(trimmedValue);
	}

	String check(final ICoverageNode node) {
		final double d = node.getCounter(entity).getValue(value);
		if (Double.isNaN(d)) {
			return null;
		}
		final BigDecimal bd = BigDecimal.valueOf(d);
		if (minimum != null && minimum.compareTo(bd) > 0) {
			return message("minimum", bd, minimum, RoundingMode.FLOOR);
		}
		if (maximum != null && maximum.compareTo(bd) < 0) {
			return message("maximum", bd, maximum, RoundingMode.CEILING);
		}
		return null;
	}

	private String message(final String minmax, final BigDecimal v,
			final BigDecimal ref, final RoundingMode mode) {
		final BigDecimal rounded = v.setScale(ref.scale(), mode);
		return String.format("%s %s is %s, but expected %s is %s",
				ENTITY_NAMES.get(entity), VALUE_NAMES.get(value),
				rounded.toPlainString(), minmax, ref.toPlainString());
	}

}
