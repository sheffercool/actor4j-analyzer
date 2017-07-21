/*
 * Copyright (c) 2015-2017, David A. Bauer. All rights reserved.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package actor4j.analyzer;

import java.util.UUID;
import java.util.function.BiConsumer;

import actor4j.core.ActorSystemImpl;
import actor4j.core.DefaultActorMessageDispatcher;
import actor4j.core.messages.ActorMessage;

public class AnalyzerActorMessageDispatcher extends DefaultActorMessageDispatcher {
	public AnalyzerActorMessageDispatcher(ActorSystemImpl system) {
		super(system);
	}

	@Override
	public void post(ActorMessage<?> message, UUID source, String alias) {
		if (alias!=null) {
			UUID dest = system.getAliases().get(alias);
			if (dest!=null)
				message.dest = dest;
		}
		analyze(message);
		super.post(message, source, alias);
	}
	
	@Override
	protected void postQueue(ActorMessage<?> message, BiConsumer<Long, ActorMessage<?>> biconsumer) {
		analyze(message);
		super.postQueue(message, biconsumer);
	}
	
	@Override
	public void postOuter(ActorMessage<?> message) {
		analyze(message);
		super.postOuter(message);
	}
	
	protected void analyze(ActorMessage<?> message) {
		if (message!=null && ((AnalyzerActorSystemImpl)system).getAnalyzeMode().get())
				((AnalyzerActorSystemImpl)system).getAnalyzerThread().getOuterQueue().offer(message.copy());
	}
}
