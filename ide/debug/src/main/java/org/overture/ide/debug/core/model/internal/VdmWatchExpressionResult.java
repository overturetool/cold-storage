/*******************************************************************************
 * Copyright (c) 2009, 2011 Overture Team and others.
 *
 * Overture is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Overture is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Overture.  If not, see <http://www.gnu.org/licenses/>.
 * 	
 * The Overture Tool web-site: http://overturetool.org/
 *******************************************************************************/
package org.overture.ide.debug.core.model.internal;

import org.eclipse.debug.core.DebugException;
import org.eclipse.debug.core.model.IValue;
import org.eclipse.debug.core.model.IWatchExpressionResult;
import org.overture.ide.debug.core.model.eval.IVdmEvaluationResult;

public class VdmWatchExpressionResult implements IWatchExpressionResult {

	private final IVdmEvaluationResult result;

	public VdmWatchExpressionResult(IVdmEvaluationResult result) {
		this.result = result;
	}

	public String[] getErrorMessages() {
		return result.getErrorMessages();
	}

	public DebugException getException() {
		return result.getException();
	}

	public String getExpressionText() {
		return result.getSnippet();
	}

	public IValue getValue() {
		return result.getValue();
	}

	public boolean hasErrors() {
		return result.hasErrors();
	}
}
