/*******************************************************************************
 * Copyright 2014 United States Government as represented by the
 * Administrator of the National Aeronautics and Space Administration.
 * All Rights Reserved.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
package gov.nasa.ensemble.dictionary.nddl;

import gov.nasa.ensemble.core.activityDictionary.Constant;
import gov.nasa.ensemble.dictionary.EActivityDef;
import gov.nasa.ensemble.dictionary.EActivityDictionary;
import gov.nasa.ensemble.dictionary.EAttributeParameter;
import gov.nasa.ensemble.dictionary.EExtendedNumericResourceDef;
import gov.nasa.ensemble.dictionary.ENumericRequirement;
import gov.nasa.ensemble.dictionary.ENumericResourceDef;
import gov.nasa.ensemble.dictionary.ENumericResourceEffect;
import gov.nasa.ensemble.dictionary.ENumericResourceEffectMode;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import org.apache.commons.lang.StringUtils;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.jscience.physics.amount.Amount;
import org.stringtemplate.v4.ST;

/*
 * This class generates nddl for numeric resources in the AD.
 * It needs to generate:
 *     - A class definition for each numeric resource type (see writeObjects)
 *     - An object instance and corresponding initialization for each 
 *       numeric resource type (see writeInitialState)
 *     - A compatibility for each activity requirement of effect where a 
 *       numeric resource is involved
 *       
 *     TODO:
 *     		- Deal with requirements (not just with effects)
 *     		- effect sign (consumer/producer?) need to be determined at runtime
 *     		- deal with time offsets for requirements/effects
 *     		- make sure the resource capacity is being initialized with the right numbers
 */
public class NumericResourceTranslator implements ADTranslator {
	private static final Logger LOGGER = Logger.getLogger(NumericResourceTranslator.class.getName());

	protected ParseInterpreter context_;
	protected List<ENumericResourceDef> resourceDefs_;
	protected List<ActResourceEffect> actResourceEffects_;

	// PHM 10/22/2012 support for Constant defs.Constants.
	protected List<Constant> allConstants;

	public NumericResourceTranslator(ParseInterpreter pi) {
		context_ = pi;
	}

	@Override
	public void interpretAD(EActivityDictionary ad) {
		resourceDefs_ = new ArrayList<ENumericResourceDef>();
		List<ENumericResourceDef> rds = ad.getDefinitions(ENumericResourceDef.class);
		for (ENumericResourceDef rd : rds) {
			// Shareables and Claimables are handled separately
			if (!(rd instanceof EExtendedNumericResourceDef))
				resourceDefs_.add(rd);
		}

		// PHM 10/22/2012 Constants.
		allConstants = ad.getDefinitions(Constant.class);

		actResourceEffects_ = new ArrayList<ActResourceEffect>();
		List<EActivityDef> activityDefs = ad.getDefinitions(EActivityDef.class);
		for (EActivityDef activityDef : activityDefs) {
			for (ENumericRequirement req : activityDef.getNumericRequirements()) {
				// TODO: process requirements
			}

			ActResourceEffect are = new ActResourceEffect(activityDef);
			for (ENumericResourceEffect effect : activityDef.getNumericEffects()) {
				// TODO: deal with offsets?
				if (effect.getMode() == ENumericResourceEffectMode.DELTA) {
					are.effects.add(new ResourceEffect(effect.getDefinition(), effect.getStartEffect(), effect.getEndEffect()));
				} else {
					LOGGER.severe("AD-NDDL translator can only deal with DELTA effects.\n" + "Ignored definition for:\n" + NDDLUtil.escape(effect.getName()) + " " + effect.getDescription());
				}
			}
			if (are.effects.size() > 0)
				actResourceEffects_.add(are);
		}

	}

	// PHM 04/12/2013 New function to declare numeric incon parameters
	protected void writeExtraInconParameters(PrintStream out) {
		for (ENumericResourceDef res : resourceDefs_) {
			out.printf("    float \t _%s;\n", NDDLUtil.escape(res.getName()));
		}
	}

	// PHM 10/22/2012 New function to to optimize param/constant
	// declarations. Checks whether param or constant is used in an
	// activityDef resource formula. If activityDef is null, searches
	// formulas for all activities.
	protected Boolean formulaUsesIdentifier(EActivityDef activityDef, String identifier) {
		for (ActResourceEffect are : actResourceEffects_) {
			if (activityDef == null || are.activity == activityDef) {
				for (ResourceEffect effect : are.effects) {
					if ((effect.startEffect != null) && (effect.startEffect.indexOf(identifier) != -1))
						return true;
					if ((effect.endEffect != null) && (effect.endEffect.indexOf(identifier) != -1))
						return true;
				}
			}
		}
		return false;
	}

	// PHM 10/22/2012 New function to translate parameters if needed.
	// Remark: For mslice, all the Amount values seem to be durations
	// measured in SECONDS. TODO: MAKE SURE SPIFE-EUROPA INTERFACE
	// PASSES MATCHING UNITS, NOT NECESSARILY MILLISECONDS, for
	// formula parameters that represent duration amounts.
	protected void writeExtraParameters(PrintStream out, EActivityDef activityDef) {
		EList<EStructuralFeature> params = activityDef.getEStructuralFeatures();
		for (EStructuralFeature param : params) {
			if ((param instanceof EAttributeParameter) && formulaUsesIdentifier(activityDef, param.getName())) {
				EAttributeParameter parm = (EAttributeParameter) param;
				String pname = NDDLUtil.escape(parm.getName());
				String ptype = NDDLUtil.escape(parm.getEType().getName());
				out.printf("    ");
				if (ptype == "EDuration" || ptype == "EDouble" || ptype == "EFloat")
					out.printf("float");
				else if (ptype == "EInt" || ptype == "ELong")
					out.printf("int");
				else if (ptype == "EString")
					out.printf("string");
				else if (ptype == "EBoolean")
					out.printf("bool");
				else
					out.printf("// **Unknown type** %s", ptype);
				out.printf(" \t %s;", pname);
				String units = null;
				if (parm.getUnitsDisplayName() != null)
					units = parm.getUnitsDisplayName().toString();
				if (StringUtils.isBlank(units) && parm.getUnits() != null)
					units = parm.getUnits().toString();
				if (StringUtils.isNotBlank(units))
					out.printf(" // %s", units);
				out.printf("\n");
			}
		}
	}

	// PHM 10/22/2012 New function to translate Constants.
	protected void writeConstant(PrintStream out, Constant constant) {
		// Do nothing if no activity uses the constant.
		if (!formulaUsesIdentifier(null, NDDLUtil.escape(constant.getName())))
			return;
		// Could include constant comments, but clutters.
		// if (constant.getNote() != null)
		// out.printf("/* %s */\n", constant.getNote());
		Object value = constant.getValue();
		// What to do about units? Have to assume they are ok for the
		// compat resource formulas, but show them as comments.
		if (value instanceof Amount<?>) {
			try {
				// Remark: For mslice, all the Amount constants seem
				// to be durations measured in seconds.
				Amount<?> val = (Amount<?>) value;
				if (val.isExact()) {
					out.printf("int  \t %s = \t %d; // %s\n", NDDLUtil.escape(constant.getName()), val.getExactValue(), val.getUnit().toString());
				} else {
					out.printf("float  \t %s = \t %f; // %s\n", NDDLUtil.escape(constant.getName()), val.getEstimatedValue(), val.getUnit().toString());
				}
				return;
			} catch (Exception e) {
				out.printf("// **Unhandled amount**");
			}
		} else if (value instanceof Boolean) {
			out.printf("bool");
		} else if (value instanceof Float || value instanceof Double) {
			out.printf("float");
		} else if (value instanceof Integer || value instanceof Long) {
			out.printf("int");
		} else if (value instanceof String) {
			out.printf("string");
		} else {
			out.printf("// **Unknown type**");
		}
		out.printf(" \t %s = \t ", NDDLUtil.escape(constant.getName()));
		if (value instanceof String) {
			out.printf("\"%s\"", value); // String in quotes
		} else if (value instanceof Float || value instanceof Double) {
			out.printf("%f", value); // Nddl disallows exponents
		} else {
			out.print(value);
		}
		out.printf(";");
		if (constant.getUnits() != null)
			out.printf(" // %s", constant.getUnits());
		out.printf("\n");
	}

	@Override
	public void writeObjects(PrintStream oStrm) {
		for (ENumericResourceDef res : resourceDefs_) {
			ST classDecl = new ST(classTemplate);
			classDecl.add("className", NDDLUtil.escape(res.getName()));
			oStrm.append(classDecl.render());
		}
	}

	@Override
	public void writeCompats(PrintStream oStrm) {

		// PHM 10/22/2012 Define global constants needed for
		// numeric compats More useful to see here than buried in
		// writeObjects. TODO: prune if not in resource effect.
		for (Constant constant : allConstants) {
			writeConstant(oStrm, constant);
		}
		oStrm.printf("\n");

		// PHM 03/12/2013 Need an incon compat to set the
		// numerical resources. Omit the passive/active guards
		// for now, so only one.
		oStrm.print("InitialConds::incon {\n" + "  if (scheduled == true) {\n" + "        eq(inconStart, start);\n\n");

		for (ENumericResourceDef res : resourceDefs_) {
			String startVar = NDDLUtil.escape("init_" + res.getName());
			String resName = NDDLUtil.escape(res.getName());
			oStrm.printf("\n" + "\t starts(%s.produce %s);\n" + "\t eq(%s.quantity, _%s);\n", resName, startVar, startVar, resName);
		}
		oStrm.print("    }\n  }\n\n");

		// PHM 10/22/2012 The compats are currently not legal
		// nddl, so put comment around them. They could be
		// manually rewritten.

		// PHM 04/12/2013 The compats are now legal but formulas
		// are limited to params and negations of params.
		// Intermediate variables should be used to evaluate
		// javascript quantities in SPIFe.

		// oStrm.printf("/* COMPATS FOR NUMERIC RESOURCES\n\n");

		for (ActResourceEffect are : actResourceEffects_) {
			for (ResourceEffect effect : are.effects) {
				if ((effect.startEffect != null) && (effect.startEffect.length() > 0)) {
					ST compat = createCompat(are.activity, effect, effect.startEffect, "start");
					oStrm.append(compat.render());
				}
				if ((effect.endEffect != null) && (effect.endEffect.length() > 0)) {
					ST compat = createCompat(are.activity, effect, effect.endEffect, "end");
					oStrm.append(compat.render());
				}
			}
		}

		// oStrm.printf("\n*/");
	}

	protected String formulaToNDDL(EActivityDef activityDef, String formula) {
		// PHM 04/12/2013 Currently limited to params or negated params
		if (formula == null || formula == "")
			return "0";
		EList<EStructuralFeature> params = activityDef.getEStructuralFeatures();
		for (EStructuralFeature param : params) {
			if (param instanceof EAttributeParameter) {
				EAttributeParameter parm = (EAttributeParameter) param;
				if (parm.getName() != null) {
					if (formula.equals(parm.getName()))
						return NDDLUtil.escape(parm.getName());
					if (formula.equals("- " + parm.getName()))
						return NDDLUtil.escape(parm.getName());
					if (formula.equals("-" + parm.getName()))
						return NDDLUtil.escape(parm.getName());
				}
			}
		}
		return "0; // **Not translated: " + formula;
	}

	protected ST createCompat(EActivityDef activity, ResourceEffect effect, String quantity, String timepoint) {
		ST compat = (timepoint == "end") ? new ST(endCompatTemplate) : new ST(startCompatTemplate);
		compat.add("className", context_.getActivitySubsystem(activity));
		compat.add("actName", NDDLUtil.escape(activity.getName()));
		compat.add("resource", NDDLUtil.escape(effect.resource.getName()));
		compat.add("quantity", formulaToNDDL(activity, quantity));
		String effectForm = (timepoint == "end") ? effect.endEffect : effect.startEffect;
		// TODO: Hack! we decide between produce and consume depending on the static sign of the
		// expression for effect. Instead it should be determined at runtime by europa.
		String effectType = (effectForm.startsWith("-") ? "consume" : "produce");
		compat.add("effectType", effectType);

		return compat;
	}

	@Override
	public void writeInitialState(PrintStream oStrm) {
		for (ENumericResourceDef res : resourceDefs_) {
			ST objDecl = new ST(objInstanceTemplate);
			objDecl.add("className", NDDLUtil.escape(res.getName()));
			objDecl.add("initCap", "0.0"); // TODO: do we have initCap at this point?
			// Hack! what should we use as maxCap if absent?
			double maxCap = (res.getMaximum() != null ? res.getMaximum() : 1e6);
			objDecl.add("maxCap", maxCap);
			// TODO: what about getMinimum, should we pass it to the model?
			// objDecl.add("minCap", res.getMinimum());
			oStrm.append(objDecl.render());
		}
	}

	// TODO: move these templates to separate text files
	static final String classTemplate = "\n" + "class <className> extends Reservoir {\n" + "    string profileType;\n" + "    string detectorType;\n" + "    <className>(float initCap, float maxCap) {\n" + "        super(initCap, 0.0, maxCap);\n" + "        profileType = \"AddGroundedProfile\";\n" + "        detectorType = \"GroundedFlawDetector\";\n" + "    }\n" + "}\n";

	static final String objInstanceTemplate = "<className> NR_<className> = new <className>(<initCap>,<maxCap>);\n";

	static final String startCompatTemplate = "<className>::<actName> {\n" + "  if (scheduled == true\n" + "       && Enable_Passive_Checking == true) {\n" + "    condleq(afterIncon, inconStart, start);\n" + "    starts(<resource>.<effectType> resourceEffect);\n" + "    resourceEffect.quantity == <quantity>;\n" + "  }\n" + "  if (scheduled == true\n" + "       && Enable_Active_Enforcement == true\n" + "       && subSolved == true\n" + "       && enforced == true) {\n"
			+ "    condleq(afterIncon, inconStart, start);\n" + "    starts(<resource>.<effectType> resourceEffect);\n" + "    resourceEffect.quantity == <quantity>;\n" + "  }\n" + "}\n";

	static final String endCompatTemplate = "<className>::<actName> {\n" + "  if (scheduled == true\n" + "       && Enable_Passive_Checking == true) {\n" + "    condleq(afterIncon, inconStart, start);\n" + "    ends(<resource>.<effectType> resourceEffect);\n" + "    resourceEffect.quantity == <quantity>;\n" + "  }\n" + "  if (scheduled == true\n" + "       && Enable_Active_Enforcement == true\n" + "       && subSolved == true\n" + "       && enforced == true) {\n"
			+ "    condleq(afterIncon, inconStart, start);\n" + "    ends(<resource>.<effectType> resourceEffect);\n" + "    resourceEffect.quantity == <quantity>;\n" + "  }\n" + "}\n";

	/*
	 * class to keep track of he relationships between activities and resources
	 */
	protected static class ActResourceEffect {
		public EActivityDef activity;
		public List<ResourceEffect> effects;

		public ActResourceEffect(EActivityDef act) {
			activity = act;
			effects = new ArrayList<ResourceEffect>();
		}
	}

	protected static class ResourceEffect {
		public ENumericResourceDef resource;
		public String startEffect;
		public String endEffect;

		public ResourceEffect(ENumericResourceDef res, String se, String ee) {
			resource = res;
			startEffect = se;
			endEffect = ee;
		}
	}
}