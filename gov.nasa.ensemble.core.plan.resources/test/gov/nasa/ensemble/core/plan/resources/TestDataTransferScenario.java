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
package gov.nasa.ensemble.core.plan.resources;

import gov.nasa.ensemble.core.activityDictionary.ActivityDictionary;
import gov.nasa.ensemble.core.jscience.JSciencePackage;
import gov.nasa.ensemble.core.jscience.Profile;
import gov.nasa.ensemble.core.jscience.util.DateUtils;
import gov.nasa.ensemble.core.jscience.util.ProfileUtil;
import gov.nasa.ensemble.core.model.plan.EActivity;
import gov.nasa.ensemble.core.model.plan.EPlan;
import gov.nasa.ensemble.core.model.plan.temporal.TemporalMember;
import gov.nasa.ensemble.core.model.plan.translator.WrapperUtils;
import gov.nasa.ensemble.core.model.plan.util.EPlanUtils;
import gov.nasa.ensemble.core.plan.resources.profile.ResourceProfileMember;
import gov.nasa.ensemble.dictionary.EActivityDef;
import gov.nasa.ensemble.dictionary.ENumericResourceDef;
import gov.nasa.ensemble.dictionary.ESummaryResourceDef;
import gov.nasa.ensemble.emf.transaction.ExtensionPointResourceSetListener;
import gov.nasa.ensemble.emf.transaction.TransactionUtils;

import java.util.Date;

import javax.measure.quantity.DataAmount;
import javax.measure.quantity.Duration;
import javax.measure.unit.SI;
import javax.measure.unit.Unit;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.transaction.TransactionalEditingDomain;
import org.jscience.physics.amount.Amount;

public class TestDataTransferScenario extends AbstractResourceTest {

	private static final Unit<DataAmount> MB = JSciencePackage.MEGA_BIT;
	private static final Amount<DataAmount> DATA_SINK_THROUGHPUT = Amount.valueOf(50, MB);
	private static final Amount<DataAmount> ZERO_MB = Amount.valueOf(0, MB);
	
	private static final Date DATA_SOURCE_ACTIVITY_START = DateUtils.add(PLAN_START, ONE_HOUR);
	private static final Date DATA_SOURCE_ACTIVITY_END = DateUtils.add(DATA_SOURCE_ACTIVITY_START, ONE_HOUR);
	
	private static final Date DATA_MULTIPLEXER_ACTIVITY_START = DateUtils.add(DATA_SOURCE_ACTIVITY_END, ONE_HOUR);
	private static final Date DATA_MULTIPLEXER_ACTIVITY_END = DateUtils.add(DATA_MULTIPLEXER_ACTIVITY_START, ONE_HOUR);
	
	private static final Date DATA_SINK_ACTIVITY_START = DateUtils.add(DATA_MULTIPLEXER_ACTIVITY_END, ONE_HOUR);
	private static final Date DATA_SINK_ACTIVITY_END = DateUtils.add(DATA_SINK_ACTIVITY_START, ONE_HOUR);
	
	private ENumericResourceDef DATA_RESOURCE;
	private ENumericResourceDef DATA_BUCKET01_RESOURCE;
	private ENumericResourceDef DATA_BUCKET02_RESOURCE;
	private ESummaryResourceDef TOTAL_DATA_RESOURCE;
	
	private EActivityDef dataSourceDef = null;
	
	private EActivity dataSource = null;
	private TemporalMember dataSourceTemporalMember = null;
	
	private EActivity dataMultiplexer = null;
	private TemporalMember dataMultiplexerTemporalMember = null;
	
	private EActivity dataSink = null;
	private TemporalMember dataSinkTemporalMember = null;
	
	private EPlan plan;

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		load(URI.createPlatformPluginURI("/gov.nasa.ensemble.core.plan.resources/datafiles/test/TestDataTransferScenario.dictionary", true));
		ActivityDictionary dictionary = ActivityDictionary.getInstance();
		DATA_RESOURCE = dictionary.getDefinition(ENumericResourceDef.class, "Data");
		DATA_BUCKET01_RESOURCE = dictionary.getDefinition(ENumericResourceDef.class, "DataBucket01");
		DATA_BUCKET02_RESOURCE = dictionary.getDefinition(ENumericResourceDef.class, "DataBucket02");
		TOTAL_DATA_RESOURCE = dictionary.getDefinition(ESummaryResourceDef.class, "TotalData");

		dataSourceDef = getActivityDef("DataSource");
		
		dataSource = PLAN_FACTORY.createActivity(dataSourceDef);
		dataSource.setName("DataSource01");
		dataSourceTemporalMember = dataSource.getMember(TemporalMember.class);

		dataMultiplexer = PLAN_FACTORY.createActivity(getActivityDef("DataMultiplexer"));
		dataMultiplexerTemporalMember = dataMultiplexer.getMember(TemporalMember.class);

		dataSink = PLAN_FACTORY.createActivity(getActivityDef("DataSink"));
		dataSinkTemporalMember = dataSink.getMember(TemporalMember.class);
		
		plan = PLAN_FACTORY.createPlan("TEST_PLAN");
		EPlanUtils.contributeProductResources(plan);
		TransactionalEditingDomain domain = TransactionUtils.getDomain(plan);
		ExtensionPointResourceSetListener.addListener(domain);
		
		TransactionUtils.writing(plan, new Runnable() {
			@Override
			public void run() {
				plan.getChildren().add(dataSource);
				plan.getChildren().add(dataMultiplexer);
				plan.getChildren().add(dataSink);
				plan.getMember(TemporalMember.class).setStartTime(PLAN_START);
				dataSourceTemporalMember.setStartTime(DATA_SOURCE_ACTIVITY_START);
				dataMultiplexerTemporalMember.setStartTime(DATA_MULTIPLEXER_ACTIVITY_START);
				dataSinkTemporalMember.setStartTime(DATA_SINK_ACTIVITY_START);
			}
		});
		
		recomputePlan(plan);
		
		TransactionUtils.reading(plan, new Runnable() {
			@Override
			public void run() {
				assertNotNull(dataSourceTemporalMember.getDuration());
				assertAmountProximity(ONE_HOUR, dataSourceTemporalMember.getDuration());
				assertEquals(DATA_SOURCE_ACTIVITY_START, dataSourceTemporalMember.getStartTime());
				assertEquals(DATA_SOURCE_ACTIVITY_END, dataSourceTemporalMember.getEndTime());
				
				assertNotNull(dataMultiplexerTemporalMember.getDuration());
				assertAmountProximity(ONE_HOUR, dataMultiplexerTemporalMember.getDuration());
				assertEquals(DATA_MULTIPLEXER_ACTIVITY_START, dataMultiplexerTemporalMember.getStartTime());
				assertEquals(DATA_MULTIPLEXER_ACTIVITY_END, dataMultiplexerTemporalMember.getEndTime());
				
				assertNotNull(dataSinkTemporalMember.getDuration());
				assertAmountProximity(ONE_HOUR, dataSinkTemporalMember.getDuration());
				assertEquals(DATA_SINK_ACTIVITY_START, dataSinkTemporalMember.getStartTime());
				assertEquals(DATA_SINK_ACTIVITY_END, dataSinkTemporalMember.getEndTime());
			}
		});

		assertResourceProfileScenario(Amount.valueOf(100, MB));
	}

	@Override
	protected void tearDown() throws Exception {
		WrapperUtils.dispose(plan);
	}

	public void testNominalDataSourceScenario() {
		setParameterValue(dataSource, "dataParameter", 200.0);
		recomputePlan(plan);
		assertResourceProfileScenario(Amount.valueOf(200, MB));
		TransactionUtils.writing(dataSource, new Runnable() {
			@Override
			public void run() {
				dataSource.getMember(TemporalMember.class).setScheduled(false);
			}
		});
		recomputePlan(plan);
		assertResourceProfileScenario(ZERO_MB);
	}
	
	public void testInsertSourceScenario() {
		final EActivity dataSource2 = PLAN_FACTORY.createActivity(dataSourceDef);
		dataSource2.setName("DataSource02");
		final TemporalMember eDataSource2TemporalMember = dataSource2.getMember(TemporalMember.class);
		TransactionUtils.writing(plan, new Runnable() {
			@Override
			public void run() {
				plan.getChildren().add(dataSource2);
				eDataSource2TemporalMember.setStartTime(DATA_SOURCE_ACTIVITY_START);
			}
		});
		recomputePlan(plan);
		TransactionUtils.reading(plan, new Runnable() {
			@Override
			public void run() {
				Amount<Duration> duration = eDataSource2TemporalMember.getDuration();
				assertAmountProximity(ONE_HOUR.to(SI.SECOND), duration); 
				assertEquals(DATA_SOURCE_ACTIVITY_START, eDataSource2TemporalMember.getStartTime());
				assertEquals(DATA_SOURCE_ACTIVITY_END, eDataSource2TemporalMember.getEndTime());
			}
		});
		assertResourceProfileScenario(Amount.valueOf(200, MB));
	}
	
	public void testMoveMultiplexerScenario() {
		TransactionUtils.writing(plan, new Runnable() {
			@Override
			public void run() {
				dataMultiplexerTemporalMember.setStartTime(PLAN_START);
			}
		});
		recomputePlan(plan);
		TransactionUtils.reading(plan, new Runnable() {
			@Override
			public void run() {
				Date dataMultiplexerEndTime = DateUtils.add(PLAN_START, ONE_HOUR);
				assertEquals(dataMultiplexerEndTime, dataMultiplexerTemporalMember.getEndTime());
				assertProfileValue(plan, DATA_RESOURCE, DATA_SOURCE_ACTIVITY_END, Amount.valueOf(100, MB));
				assertProfileValue(plan, DATA_BUCKET01_RESOURCE, DATA_SOURCE_ACTIVITY_END, ZERO_MB);
				assertProfileValue(plan, DATA_BUCKET02_RESOURCE, DATA_SOURCE_ACTIVITY_END, ZERO_MB);
				assertProfileValue(plan, TOTAL_DATA_RESOURCE, DATA_SOURCE_ACTIVITY_END, ZERO_MB);
				assertProfileValue(plan, DATA_RESOURCE, dataMultiplexerEndTime, ZERO_MB);
				assertProfileValue(plan, DATA_BUCKET01_RESOURCE, dataMultiplexerEndTime, ZERO_MB);
				assertProfileValue(plan, DATA_BUCKET02_RESOURCE, dataMultiplexerEndTime, ZERO_MB);
				assertProfileValue(plan, TOTAL_DATA_RESOURCE, dataMultiplexerEndTime, ZERO_MB);
				assertProfileValue(plan, DATA_RESOURCE, DATA_SINK_ACTIVITY_END, Amount.valueOf(100, MB));
				assertProfileValue(plan, DATA_BUCKET01_RESOURCE, DATA_SINK_ACTIVITY_END, ZERO_MB);
				assertProfileValue(plan, DATA_BUCKET02_RESOURCE, DATA_SINK_ACTIVITY_END, ZERO_MB);
				assertProfileValue(plan, TOTAL_DATA_RESOURCE, DATA_SINK_ACTIVITY_END, ZERO_MB);
				assertADEffect(dataMultiplexer, TOTAL_DATA_RESOURCE, ZERO_MB);
			}
		});
	}
	
	public void testMoveSinkScenario() {
		TransactionUtils.writing(plan, new Runnable() {
			@Override
			public void run() {
				dataSinkTemporalMember.setStartTime(PLAN_START);
			}
		});
		recomputePlan(plan);
		TransactionUtils.reading(plan, new Runnable() {
			@Override
			public void run() {
				Amount<DataAmount> expectedData = Amount.valueOf(100, MB);
				Amount<DataAmount> bucket01Data = expectedData.times(2).divide(5);
				Amount<DataAmount> bucket02Data = expectedData.times(3).divide(5);
				Date dataSinkEndTime = DateUtils.add(PLAN_START, ONE_HOUR);
				assertEquals(dataSinkEndTime, dataSinkTemporalMember.getEndTime());
				assertProfileValue(plan, DATA_RESOURCE, DATA_SOURCE_ACTIVITY_END, expectedData);
				assertProfileValue(plan, DATA_BUCKET01_RESOURCE, DATA_SOURCE_ACTIVITY_END, ZERO_MB);
				assertProfileValue(plan, DATA_BUCKET02_RESOURCE, DATA_SOURCE_ACTIVITY_END, ZERO_MB);
				assertProfileValue(plan, TOTAL_DATA_RESOURCE, DATA_SOURCE_ACTIVITY_END, ZERO_MB);
				assertProfileValue(plan, DATA_RESOURCE, DATA_MULTIPLEXER_ACTIVITY_END, ZERO_MB);
				assertProfileValue(plan, DATA_BUCKET01_RESOURCE, DATA_MULTIPLEXER_ACTIVITY_END, bucket01Data);
				assertProfileValue(plan, DATA_BUCKET02_RESOURCE, DATA_MULTIPLEXER_ACTIVITY_END, bucket02Data);
				assertProfileValue(plan, TOTAL_DATA_RESOURCE, DATA_MULTIPLEXER_ACTIVITY_END, expectedData);
				assertProfileValue(plan, DATA_RESOURCE, dataSinkEndTime, ZERO_MB);
				assertProfileValue(plan, DATA_BUCKET01_RESOURCE, dataSinkEndTime, ZERO_MB);
				assertProfileValue(plan, DATA_BUCKET02_RESOURCE, dataSinkEndTime, ZERO_MB);
				assertProfileValue(plan, TOTAL_DATA_RESOURCE, dataSinkEndTime, ZERO_MB);
			}
		});
	}

	private void assertResourceProfileScenario(final Amount<DataAmount> expectedData) {
		TransactionUtils.reading(plan, new Runnable() {

			@Override
			public void run() {
				Amount<DataAmount> bucket01Data = expectedData.times(2).divide(5);
				Amount<DataAmount> bucket02Data = expectedData.times(3).divide(5);
				
				Amount<DataAmount> deltaData01 = bucket01Data.isLessThan(DATA_SINK_THROUGHPUT) ? bucket01Data : DATA_SINK_THROUGHPUT;
				Amount<DataAmount> bucket01Sink = bucket01Data.minus(deltaData01);
				Amount<DataAmount> bits_left = DATA_SINK_THROUGHPUT.minus(deltaData01);
				Amount<DataAmount> deltaData02 = bucket02Data.isLessThan(bits_left) ? bucket02Data : bits_left;
				Amount<DataAmount> bucket02Sink = bucket02Data.minus(deltaData02);

				assertProfileValue(plan, DATA_RESOURCE, DATA_SOURCE_ACTIVITY_END, expectedData);
				assertProfileValue(plan, DATA_BUCKET01_RESOURCE, DATA_SOURCE_ACTIVITY_END, ZERO_MB);
				assertProfileValue(plan, DATA_BUCKET02_RESOURCE, DATA_SOURCE_ACTIVITY_END, ZERO_MB);
				assertProfileValue(plan, TOTAL_DATA_RESOURCE, DATA_SOURCE_ACTIVITY_END, ZERO_MB);
				
				assertProfileValue(plan, DATA_RESOURCE, DATA_MULTIPLEXER_ACTIVITY_END, ZERO_MB);
				assertProfileValue(plan, DATA_BUCKET01_RESOURCE, DATA_MULTIPLEXER_ACTIVITY_END, bucket01Data);
				assertProfileValue(plan, DATA_BUCKET02_RESOURCE, DATA_MULTIPLEXER_ACTIVITY_END, bucket02Data);
				assertProfileValue(plan, TOTAL_DATA_RESOURCE, DATA_MULTIPLEXER_ACTIVITY_END, expectedData);

				assertProfileValue(plan, DATA_RESOURCE, DATA_SINK_ACTIVITY_END, ZERO_MB);
				assertProfileValue(plan, DATA_BUCKET01_RESOURCE, DATA_SINK_ACTIVITY_END, bucket01Sink);
				assertProfileValue(plan, DATA_BUCKET02_RESOURCE, DATA_SINK_ACTIVITY_END, bucket02Sink);
				assertProfileValue(plan, TOTAL_DATA_RESOURCE, DATA_SINK_ACTIVITY_END, expectedData.minus(deltaData01).minus(deltaData02));
				
				assertADEffect(dataMultiplexer, TOTAL_DATA_RESOURCE, expectedData);
			}
			
		});
	}

	@SuppressWarnings("unused")
	private void debugProfiles() {
		for (Profile<?> p : WrapperUtils.getMember(plan, ResourceProfileMember.class).getResourceProfiles()) {
			ProfileUtil.debugProfile(p);
		}
	}
	
}
