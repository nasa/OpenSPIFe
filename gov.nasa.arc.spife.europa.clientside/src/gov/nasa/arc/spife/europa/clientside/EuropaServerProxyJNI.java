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
package gov.nasa.arc.spife.europa.clientside;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Vector;
import java.util.concurrent.ConcurrentLinkedQueue;

import org.apache.log4j.Logger;

public class EuropaServerProxyJNI 
	extends EuropaServerProxyBase 
{
	private static final Logger LOGGER = Logger.getLogger(EuropaServerProxyJNI.class);
	
	protected Queue<CommandEntry> commandQueue_;

	protected static Map<EuropaCommand,CommandExecutor> executors_;
	protected static CommandExecutor defaultExecutor_;
	
	public EuropaServerProxyJNI(EuropaServerConfig config)
	{
		super(config,false /*use embedded server*/);
		commandQueue_ = new ConcurrentLinkedQueue<CommandEntry>();
		defaultExecutor_ = new GenericExecutor();
	}
	
	@Override
	protected Object executeCommand(
			EuropaCommand command,
			Vector<Object> args) 
	{
		CommandExecutor ce = getExecutor(command);
		return ce.execute(getEmbeddedServer(), command, args);
	}
	
	protected EuropaServer getEmbeddedServer()
	{
		return serverLauncher_.getServer();
	}

	@Override
	protected void queueCommand(EuropaCommand command, Vector<Object> args) 
	{
		commandQueue_.add(new CommandEntry(command,args));
	}

	@Override
	protected List<?> flushCommandQueue() 
	{
		List<Object> result = new ArrayList<Object>();
		
		while (!commandQueue_.isEmpty()) {
		    CommandEntry e = commandQueue_.remove();
		    try {
		    	Object o = executeCommand(e.command,e.args);
		    	result.add(o);
		    }
		    catch (Exception ex) {
		    	result.add(ex);
		    }
		}
		
		return result;
	}
	
	@Override
	protected boolean serverResponds(EuropaServerConfig config, int timeout) 
	{
		// TODO: do something to verify JNI?
		return true;
	}
	
	protected CommandExecutor getExecutor(EuropaCommand command)
	{
		CommandExecutor ce = executors_.get(command);
		
		return (ce !=null ? ce : defaultExecutor_);
	}
	
	protected static class CommandEntry
	{
		public EuropaCommand command;
		public Vector<Object> args;
		
		public CommandEntry(EuropaCommand c, Vector<Object> a)
		{
			command = c;
			args = a;
		}
	}

	protected static interface CommandExecutor
	{
		public Object execute(EuropaServer server, EuropaCommand command, Vector<Object> args);
	}
	
	protected static void checkArgs(Method m,Vector<Object> args)
	{
		Class ptypes[] = m.getParameterTypes();

		if (ptypes.length != args.size())
			throw new RuntimeException(m.getName()+" expects "+ptypes.length+" parameters, instead got "+args.size());
		
		for (int i=0;i<ptypes.length;i++) {
			Class pType = ptypes[i];
			Class argType = args.get(i).getClass();
			if (!pType.isAssignableFrom(argType)) { 
				// Deal with the conversion issues we're ok with, others will be caught by Java's reflection mechanisms
				if (Long.class.equals(argType) && int.class.equals(pType)) {
					LOGGER.debug("EuropaServer method "+m.getName()+": Converted arg "+i+" from "+argType.getName()+" to "+pType.getName());
					args.set(i, ((Number)args.get(i)).intValue());					
				}
			}
		}
	}
	
	protected static class GenericExecutor
		implements CommandExecutor
	{
		@Override
		public Object execute(EuropaServer server, EuropaCommand command, Vector<Object> args) 
		{
			Method m = findMethod(server,command,args);
			checkArgs(m,args);
			return invokeMethod(m,server,args);
		}
		
		protected Method findMethod(EuropaServer server, EuropaCommand command, Vector<Object> args)
		{
			for (Method m : server.getClass().getMethods()) {
				if (m.getName().equals(command.getXmlrpcString())) {
					return m;
				}	
			}
			
			throw new RuntimeException("Couldn't find method for "+command);
		}

		protected Object invokeMethod(Method m, EuropaServer server, Vector<Object> args)
		{
			try {
				LOGGER.debug("Executing Command:"+m.getName());
				return m.invoke(server,args.toArray());
			}
			catch (Exception e) {
				LOGGER.error("Failed executing command:"+m.getName(),e);
				throw new RuntimeException("Failed executing command:"+m.getName(),e);
			}
		}
	}
	
	protected static class CreateObjectsExecutor
	implements CommandExecutor
	{
		@Override
		public Object execute(EuropaServer server, EuropaCommand command, Vector<Object> args) 
		{
			String session = (String)args.get(0);
			String objectClass = (String)args.get(1);
			Collection c = (Collection)args.get(2);
			VectorString names = new VectorString();
			
			for (Object o : c)
				names.add(o.toString());
			
			server.CreateObjects(session, objectClass, names);
			return null;
		}
	}

	protected static class EndFixingExecutor
		extends FixViolationsExecutor
	{
		@Override
		public Object execute(EuropaServer server, EuropaCommand command, Vector<Object> args) 
		{
			String session = (String)args.get(0);
			VectorLong startTimes = new VectorLong();
			VectorLong endTimes = new VectorLong();
			VectorString scheduled = new VectorString();
			VectorString unscheduled = new VectorString();
			boolean completed[] = {false};
			int plansteps[] = {0};
			VectorString selectedActs = new VectorString();
			if (args.size()==2) {
				Vector<Object> selected = (Vector<Object>)args.get(2);
				for (Object id : selected)
					selectedActs.add(id.toString());
			}
			VectorString opponents = new VectorString();

			server.EndFixing(
					session,
					startTimes,
					endTimes,
					scheduled,
					unscheduled,
					completed,
					plansteps,
					selectedActs,
					opponents);
			
			return packViolationsInfo(scheduled,startTimes,unscheduled,opponents);			
		}
	}
	
	protected static class FixViolationsExecutor
		implements CommandExecutor
	{
		@Override
		public Object execute(EuropaServer server, EuropaCommand command, Vector<Object> args) 
		{
			String session = (String)args.get(0);
			int steps = (Integer)args.get(1);
			VectorLong startTimes = new VectorLong();
			VectorString scheduled = new VectorString();
			VectorString unscheduled = new VectorString();
			boolean completed[] = {false};
			int plansteps[] = {0};
			VectorString selectedActs = new VectorString();
			if (args.size()==3) {
				Vector<Object> selected = (Vector<Object>)args.get(2);
				for (Object id : selected)
					selectedActs.add(id.toString());
			}
			VectorString opponents = new VectorString();
			
			server.FixViolations(
					session, 
					startTimes, 
					scheduled, 
					unscheduled, 
					completed, 
					plansteps, 
					steps, 
					selectedActs, 
					opponents);
			
			return packViolationsInfo(scheduled,startTimes,unscheduled,opponents);			
		}
		
		protected Map<String,Object> packViolationsInfo(
				VectorString scheduled,
				VectorLong startTimes,
				VectorString unscheduled,
				VectorString opponents)
		{
			Map<String,Object> retval = new HashMap<String,Object>();
			
			Map[] scheds = new Map[(int)scheduled.size()];
			for (int i=0;i<scheduled.size();i++) {
				Map<String,Object> m = new HashMap<String,Object>();
				m.put("name",scheduled.get(i));
				m.put("start time", startTimes.get(i));
				scheds[i] = m; 
			}
			retval.put("scheduled",scheds);
			
			Map[] unscheds = new Map[(int)unscheduled.size()];
			for (int i=0;i<unscheduled.size();i++) {
				Map<String,String> m = new HashMap<String,String>();
				m.put("name", unscheduled.get(i));
				unscheds[i] = m;
			}
			retval.put("unscheduled",unscheds);
			
			Object opps[] = new String[(int)opponents.size()];
			for (int i=0;i<opponents.size();i++)
				opps[i] = opponents.get(i);
			retval.put("opponents", opps);
			
			return retval;		
		}		
	}

	protected static class FixViolationsProgressiveExecutor
		extends FixViolationsExecutor
	{
		@Override
		public Object execute(EuropaServer server, EuropaCommand command, Vector<Object> args) 
		{
			String session = (String)args.get(0);
			int steps = (Integer)args.get(1);
			VectorLong startTimes = new VectorLong();
			VectorLong endTimes = new VectorLong();
			VectorString scheduled = new VectorString();
			VectorString unscheduled = new VectorString();
			boolean completed[] = {false};
			int plansteps[] = {0};
			VectorString selectedActs = new VectorString();
			if (args.size()==3) {
				Vector<Object> selected = (Vector<Object>)args.get(2);
				for (Object id : selected)
					selectedActs.add(id.toString());
			}
			VectorString opponents = new VectorString();

			server.FixViolationsProgressive(
					session,
					startTimes,
					endTimes,
					scheduled,
					unscheduled,
					completed,
					plansteps,
					steps,
					selectedActs,
					opponents);

			return packViolationsInfo(scheduled,startTimes,unscheduled,opponents);			
		}
	}

	protected static class GetActivityBoundsExecutor
		implements CommandExecutor
	{
		@Override
		public Object execute(EuropaServer server, EuropaCommand command, Vector<Object> args) 
		{
			String session = (String)args.get(0);
			VectorString activities = new VectorString();
			VectorLong lbounds = new VectorLong();
			VectorLong ubounds = new VectorLong();
			
			server.GetActivityBounds(session, activities, lbounds, ubounds);
			
			Object[] retval = new Object[(int)activities.size()];
			for (int i=0; i < activities.size(); i++) {
				Map<String,Object> entry = new HashMap<String,Object>();
				entry.put("activity", activities.get(i));
				entry.put("lbounds", lbounds.get(i));
				entry.put("ubounds", ubounds.get(i));				
				retval[i]=entry;
			}
			return retval;
		}
	}
	
	protected static class GetActivityConstraintsExecutor
		implements CommandExecutor
	{
		@Override
		public Object execute(EuropaServer server, EuropaCommand command, Vector<Object> args) 
		{
			String session = (String)args.get(0);
			String namestr = (String)args.get(1);
			
			int earliest[]={0},latest[]={0};
			VectorString others = new VectorString();
			VectorLong other_earliest = new VectorLong();
			VectorLong other_latest = new VectorLong();
			VectorLong mins = new VectorLong();
			VectorLong maxs = new VectorLong();
			VectorLong end_earliest = new VectorLong();
			VectorLong end_latest = new VectorLong();
			VectorLong end_mins = new VectorLong();
			VectorLong end_maxs = new VectorLong();
			
			server.GetActivityConstraints(
					session, 
					namestr, 
					earliest, latest, 
					others, 
					other_earliest, other_latest, 
					mins, maxs, 
					end_earliest, end_latest, 
					end_mins, end_maxs);
			
			Map<String,Object> retval = new HashMap<String,Object>();
			retval.put("activity", namestr);
			retval.put("earliest", earliest[0]);
			retval.put("latest", latest[0]);
			
			Object[] constraints = new Object[(int)others.size()];
			for (int i=0;i<others.size();i++) {
				Object[] cv = new Object[9];
				cv[0] = others.get(i);
				cv[1] = other_earliest.get(i);
				cv[2] = other_latest.get(i);
				cv[3] = mins.get(i);
				cv[4] = maxs.get(i);
				cv[5] = end_earliest.get(i);
				cv[6] = end_latest.get(i);
				cv[7] = end_mins.get(i);
				cv[8] = end_maxs.get(i);
				
				constraints[i] = cv;
			}
			retval.put("constraints",constraints);
			
			return retval;
		}
	}

	protected static class GetCpuWindowsExecutor
		implements CommandExecutor
	{
		@Override
		public Object execute(EuropaServer server, EuropaCommand command, Vector<Object> args) 
		{
			String session = (String)args.get(0);
			VectorLong start_times = new VectorLong();
			VectorLong end_times = new VectorLong();
			
			server.GetCpuWindows(session, start_times, end_times);

			List<Map<String,Object>> retval = new ArrayList<Map<String,Object>>();

			for (int i=0;i<start_times.size();i++) {
				Map<String,Object> entry = new HashMap<String,Object>();
				entry.put("start", start_times.get(i));
				entry.put("end", end_times.get(i));
				entry.put("type", "cpu_on");
			}

			return retval.toArray();
		}
	}
	
	protected static class GetInconParametersExecutor
		implements CommandExecutor
	{
		@Override
		public Object execute(EuropaServer server, EuropaCommand command, Vector<Object> args) 
		{
			String session = (String)args.get(0);
			String incon_name = (String)args.get(1);
			String prefix = (String)args.get(2);
			VectorString names = new VectorString();
			server.GetInconParameters(session, incon_name, prefix, names);

			Object retval[] = new Object[(int)names.size()];
			for (int i=0;i<names.size();i++) 
				retval[i] = names.get(i);

			return retval;
		}
	}
	
	protected static class GetFinconInfoExecutor
		implements CommandExecutor
	{
		@Override
		public Object execute(EuropaServer server, EuropaCommand command, Vector<Object> args) 
		{
			String session = (String)args.get(0);
			String incon_name = (String)args.get(1);
			Number time = (Number)args.get(2);
			
			VectorString names = new VectorString();
			VectorString values = new VectorString();
			VectorString types = new VectorString();
			
			server.GetFinconInfo(session, incon_name, time.longValue(), names, values, types);
			
			Object retval[] = new Object[(int)names.size()];
			
			for (int i=0;i<names.size();i++) {
				Map<String,Object> m = new HashMap<String,Object>();
				m.put("name",names.get(i));
				m.put("value",values.get(i));
				m.put("type", types.get(i));
				retval[i] = m;
			}
			
			return retval;
		}
	}

	protected static class GetFlightRuleNamesExecutor
		implements CommandExecutor
	{
		@Override
		public Object execute(EuropaServer server, EuropaCommand command, Vector<Object> args) 
		{
			String session = (String)args.get(0);
			VectorString frnames = new VectorString();
			server.GetFlightRuleNames(session, frnames);
			
			Object retval[] = new Object[(int)frnames.size()];
			for (int i=0;i<frnames.size();i++)
				retval[i] = frnames.get(i);
			return retval;
		}
	}
	
	protected static class GetNogoodExecutor
		implements CommandExecutor
	{
		@Override
		public Object execute(EuropaServer server, EuropaCommand command, Vector<Object> args) 
		{
			String session = (String)args.get(0);
			VectorString activities = new VectorString();
			VectorString points = new VectorString();
			VectorLong lengths = new VectorLong();
			VectorString keys = new VectorString();

			server.GetNogood(session,activities,points,lengths,keys);
			
			List<Map<String,Object>> retval = new ArrayList<Map<String,Object>>();
			for (int i=0;i<activities.size();i++) {
				Map<String,Object> map = new HashMap<String,Object>();
				map.put("activity",activities.get(i));
				map.put("point",points.get(i));
				map.put("length",lengths.get(i));
				map.put("key",keys.get(i));
				retval.add(map);
			}
			
			return retval.toArray();
		}
	}
	
	protected static class GetResourceInTmFormatExecutor
		implements CommandExecutor
	{
		@Override
		public Object execute(EuropaServer server, EuropaCommand command, Vector<Object> args) 
		{
			String resource = (String)args.get(0);
			VectorPairIntDouble resource_levels = new VectorPairIntDouble();		
			
			server.GetResourceMinLevels(resource, resource_levels);
			
			Object[] rows = new Object[(int)resource_levels.size()];
			
			for (int i=0;i<resource_levels.size();i++) {
				PairIntDouble pair = resource_levels.get(i);
				Object[] row = new Object[2];
				row[0] = pair.getFirst();
				row[1] = pair.getSecond();
				
				rows[i] = row;
			}
			
			return rows;
		}
	}

	protected static class GetViolationsExecutor
		implements CommandExecutor
	{
		@Override
		public Object execute(EuropaServer server, EuropaCommand command, Vector<Object> args) 
		{
			String session = (String)args.get(0);
			VectorFlout flouts = new VectorFlout();
			VectorString temps = new VectorString();
			String pretty = server.GetViolations(session,flouts,temps);
			
			Map<String,Object[]> retval = new HashMap<String,Object[]>();
			
			List<Object> frViolations = new ArrayList<Object>();
			for (int i=0;i<flouts.size();i++) {
				Flout f = flouts.get(i);
				Map<String,Object> entry = new HashMap<String,Object>();
				entry.put("time", f.getTime());
				entry.put("type", f.getType());
				entry.put("level", f.getLevel());
				
				Object culprits[] = new Object[(int)f.getCulprits().size()];
				for (int j=0;j<f.getCulprits().size();j++)
					culprits[j]=f.getCulprits().get(j);
				entry.put("culprits", culprits);
				
				frViolations.add(entry);
			}
			retval.put("flight_rule", frViolations.toArray());

			List<Object> temporalViolations = new ArrayList<Object>();
			for (int i=0;i<temps.size();i++) 
				temporalViolations.add(temps.get(i));
			
			retval.put("temporal", temporalViolations.toArray());
			
			retval.put("description", new Object[]{pretty});
			return retval;
			
		}
	}

	// This is necessary only to ignore INCON parameters that were optimized out by the ad-nddl translator
	protected static class SetActivityParamValueExecutor
		implements CommandExecutor
	{
		@Override
		public Object execute(EuropaServer server, EuropaCommand command, Vector<Object> args) 
		{
			String session = (String)args.get(0);
			String activity = (String)args.get(1);
			String parameter = (String)args.get(2);
			String value = (String)args.get(3);
			String value_type = (String)args.get(4);			
			server.SetActivityParamValue(session, activity, parameter, value, value_type);
			
			return null;
		}
	}

	protected static class StartFixingExecutor
		implements CommandExecutor
	{
		@Override
		public Object execute(EuropaServer server, EuropaCommand command, Vector<Object> args) 
		{
			String session = (String)args.get(0);
			int steps = (Integer)args.get(1);
			VectorString selectedActs = new VectorString();
			if (args.size()==3) {
				Vector<Object> selected = (Vector<Object>)args.get(2);
				for (Object id : selected)
					selectedActs.add(id.toString());
			}
			int numGoals[] = {0}; 
			server.StartFixing(session, steps, numGoals, selectedActs);
			return numGoals[0];
		}
	}
	
	protected static class StopServerExecutor
		implements CommandExecutor
	{
		@Override
		public Object execute(EuropaServer server, EuropaCommand command, Vector<Object> args) 
		{
			server.Stop();			
			return null;
		}
	}

	static {
		executors_ = new HashMap<EuropaCommand,CommandExecutor>();
		executors_.put(EuropaCommand.CREATE_OBJECTS, new CreateObjectsExecutor());
		executors_.put(EuropaCommand.END_FIXING, new EndFixingExecutor());
		executors_.put(EuropaCommand.FIX_VIOLATIONS, new FixViolationsExecutor());
		executors_.put(EuropaCommand.FIX_VIOLATIONS_PROGRESSIVELY, new FixViolationsProgressiveExecutor());
		executors_.put(EuropaCommand.GET_ACTIVITY_BOUNDS, new GetActivityBoundsExecutor());
		executors_.put(EuropaCommand.GET_ACTIVITY_CONSTRAINTS, new GetActivityConstraintsExecutor());
		executors_.put(EuropaCommand.GET_CPU_WINDOWS, new GetCpuWindowsExecutor());
		executors_.put(EuropaCommand.GET_INCON_PARAMETERS, new GetInconParametersExecutor());
		executors_.put(EuropaCommand.GET_FINCON_INFO, new GetFinconInfoExecutor());
		executors_.put(EuropaCommand.GET_FLIGHT_RULE_NAMES, new GetFlightRuleNamesExecutor());
		executors_.put(EuropaCommand.GET_NOGOOD, new GetNogoodExecutor());
		executors_.put(EuropaCommand.GET_RESOURCE_IN_TIME_FORMAT, new GetResourceInTmFormatExecutor());		
		executors_.put(EuropaCommand.GET_VIOLATIONS, new GetViolationsExecutor());
		executors_.put(EuropaCommand.SET_ACTIVITY_PARAM_VALUE, new SetActivityParamValueExecutor());		
		executors_.put(EuropaCommand.START_FIXING, new StartFixingExecutor());		
		executors_.put(EuropaCommand.STOP_SERVER, new StopServerExecutor());		
	}
}
