/**
 * (C) Copyright IBM Corp. 2006, 2009
 *
 * THIS FILE IS PROVIDED UNDER THE TERMS OF THE ECLIPSE PUBLIC LICENSE 
 * ("AGREEMENT"). ANY USE, REPRODUCTION OR DISTRIBUTION OF THIS FILE 
 * CONSTITUTES RECIPIENTS ACCEPTANCE OF THE AGREEMENT.
 *
 * You can obtain a current copy of the Eclipse Public License from
 * http://www.opensource.org/licenses/eclipse-1.0.php
 *
 * @author : Endre Bak, ebak@de.ibm.com  
 * 
 * Flag       Date        Prog         Description
 * -------------------------------------------------------------------------------
 * 1565892    2006-12-04  ebak         Make SBLIM client JSR48 compliant
 * 1720707    2007-05-17  ebak         Conventional Node factory for CIM-XML SAX parser
 * 1735614    2007-06-12  ebak         Wrong ARRAYSIZE attribute handling in SAX/PULL
 * 1820763    2007-10-29  ebak         Supporting the EmbeddedInstance qualifier
 * 2003590    2008-06-30  blaschke-oss Change licensing from CPL to EPL
 * 2433593    2008-12-18  rgummada     isArray returns true for method parameters of type reference
 * 2524131    2009-01-21  raman_arora  Upgrade client to JDK 1.5 (Phase 1)
 */

package org.sblim.cimclient.internal.cimxml.sax.node;

import javax.cim.CIMDataType;

import org.xml.sax.Attributes;

/**
 * <pre>
 * ELEMENT PARAMETER.REFARRAY (QUALIFIER*)
 * ATTLIST PARAMETER.REFARRAY
 *   %CIMName;
 *   %ReferenceClass;
 *   %ArraySize;
 * </pre>
 */
public class ParameterRefArrayNode extends AbstractParameterNode {

	private CIMDataType iType;

	/**
	 * Ctor.
	 */
	public ParameterRefArrayNode() {
		super(PARAMETER_REFARRAY);
	}

	@Override
	protected void specificInit(Attributes pAttribs) {
		this.iType = new CIMDataType(getReferenceClass(pAttribs), getArraySize(pAttribs));

	}

	@Override
	public void testCompletness() { /* */}

	public CIMDataType getType() {
		return this.iType;
	}

}
