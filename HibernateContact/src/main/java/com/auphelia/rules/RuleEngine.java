package com.auphelia.rules;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.drools.KnowledgeBase;
import org.drools.KnowledgeBaseFactory;
import org.drools.builder.KnowledgeBuilder;
import org.drools.builder.KnowledgeBuilderError;
import org.drools.builder.KnowledgeBuilderErrors;
import org.drools.builder.KnowledgeBuilderFactory;
import org.drools.builder.ResourceType;
import org.drools.io.ResourceFactory;
import org.drools.runtime.ObjectFilter;
import org.drools.runtime.StatefulKnowledgeSession;

public class RuleEngine<T> {
	
	private String rulesFileName;
	
	private Class<T> returnType;

	public RuleEngine(String rulesFileName, Class<T> returnType) {
		this.rulesFileName = rulesFileName;
		this.returnType = returnType;
	}
	
	public List<T> runWith(List<?> facts) {
		return new ArrayList<T>(runWithInternal(facts));
	}
	
	@SuppressWarnings("unchecked")
	private Collection<T> runWithInternal(List<?> facts) {
		KnowledgeBase knowlegeBase = readKnowledgeBase(this.rulesFileName);
		StatefulKnowledgeSession knowledgeSession = knowlegeBase.newStatefulKnowledgeSession();

		for (Object fact : facts) {
			knowledgeSession.insert(fact);
		}

		knowledgeSession.fireAllRules();
		return (Collection<T>) knowledgeSession.getObjects(new ObjectFilter() {
			public boolean accept(Object object) {
				return returnType.isInstance(object);
			}
		});

	}
	
	private KnowledgeBase readKnowledgeBase(String fileName) {
		KnowledgeBuilder knowledgeBuilder = KnowledgeBuilderFactory.newKnowledgeBuilder();
		knowledgeBuilder.add(ResourceFactory.newClassPathResource(fileName), ResourceType.DRL);
		KnowledgeBuilderErrors errors = knowledgeBuilder.getErrors();
		
		if (errors.size() > 0) {
			for (KnowledgeBuilderError error : errors) {
				System.err.println(error);
			}
			throw new IllegalArgumentException("Could not parse knowledge.");
		}
		
		KnowledgeBase knowledgeBase = KnowledgeBaseFactory.newKnowledgeBase();
		knowledgeBase.addKnowledgePackages(knowledgeBuilder.getKnowledgePackages());
		
		return knowledgeBase;
	}

}
