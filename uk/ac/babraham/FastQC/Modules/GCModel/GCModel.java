/**
 * Copyright Copyright 2010-12 Simon Andrews
 *
 *    This file is part of FastQC.
 *
 *    FastQC is free software; you can redistribute it and/or modify
 *    it under the terms of the GNU General Public License as published by
 *    the Free Software Foundation; either version 3 of the License, or
 *    (at your option) any later version.
 *
 *    FastQC is distributed in the hope that it will be useful,
 *    but WITHOUT ANY WARRANTY; without even the implied warranty of
 *    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *    GNU General Public License for more details.
 *
 *    You should have received a copy of the GNU General Public License
 *    along with FastQC; if not, write to the Free Software
 *    Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301  USA
 */
package uk.ac.babraham.FastQC.Modules.GCModel;

import java.util.Arrays;

public class GCModel {
	
	public int readLength;
	public GCModelValue[][] models;
	
	public GCModel (int readLength) {
		
		int [] claimingCounts = new int [101];
		this.readLength = readLength;
		models = new GCModelValue[readLength+1][];
		
		for (int pos=0;pos<=readLength;pos++) {
			double lowCount = pos-0.5;
			double highCount = pos+0.5;
			
			if (lowCount < 0) lowCount = 0;
			if (highCount < 0) highCount = 0;
			if (highCount > readLength) highCount = readLength;
			if (lowCount > readLength) lowCount = readLength;
			
			int lowPercentage = (int)Math.round((lowCount*100) / readLength);
			int highPercentage = (int)Math.round((highCount*100) / readLength);
			
			for (int p=lowPercentage;p<=highPercentage;p++) {
				claimingCounts[p]++;
			}
		}
				
		
		// We now do a second pass to make up the model using the weightings
		// we calculated previously.
		
		for (int pos=0;pos<=readLength;pos++) {
			double lowCount = pos-0.5;
			double highCount = pos+0.5;
			
			if (lowCount < 0) lowCount = 0;
			if (highCount < 0) highCount = 0;
			if (highCount > readLength) highCount = readLength;
			if (lowCount > readLength) lowCount = readLength;
			
			int lowPercentage = (int)Math.round((lowCount*100) / readLength);
			int highPercentage = (int)Math.round((highCount*100) / readLength);
			
			GCModelValue [] modelValues = new GCModelValue [(highPercentage-lowPercentage)+1];
						
			for (int p=lowPercentage;p<=highPercentage;p++) {
				modelValues[p-lowPercentage] = new GCModelValue(p, 1d/claimingCounts[p]);
			}
			models[pos] = modelValues;
		}
		
		
		
	}
	
	/**
	 * Empty constructor used in cloning
	 */
	private GCModel() {
		
	}
	
	public GCModelValue [] getModelValues (int gcCount) {
		return models[gcCount];
	}
	
	/**
	 * Deep copy
	 */
	@Override
	public GCModel clone() {
		GCModel newModel = new GCModel();
		newModel.readLength = this.readLength;
		newModel.models = new GCModelValue[this.models.length][];
		for (int i = 0; i < this.models.length; i++) {
			newModel.models[i] = Arrays.copyOf(this.models[i], this.models.length);
		}
		return newModel;
	}
}
