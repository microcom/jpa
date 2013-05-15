package com.auphelia.services;

import java.io.IOException;
import java.math.BigInteger;
import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceUnit;
import javax.persistence.Query;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.auphelia.models.Province;

@Path("/province")
@Stateless
public class ProvinceService {
	@PersistenceUnit
	EntityManagerFactory emf;
	
	public ProvinceService() {
	}
	
	public ProvinceService(EntityManagerFactory emf) {
		this.emf = emf;
	}

	/**
     * GET method for filling with test content
     * @return an HTTP response with operation result
     * @throws Exception 
     */
    @GET
    @Path("/fill")
    @Produces(MediaType.APPLICATION_JSON + "; charset=utf-8")
	public Response fill() throws Exception {
    	EntityManager em = emf.createEntityManager();
    	Query q = em.createNativeQuery("DELETE FROM contact.province WHERE id != 0;");
    	q.executeUpdate();
    	
    	// Québec
    	Province quebec = new Province();
    	quebec.setSimpleName("quebec");
    	quebec.setFullName("Québec");
    	
    	String addResult1 = "";
    	try {
    		em.persist(quebec);
    	} catch (Exception e) {
    		addResult1 = e.getMessage();
    	}
        
        // Ontario
    	Province ontario = new Province();
    	ontario.setSimpleName("ontario");
    	ontario.setFullName("Ontario");
    	
    	String addResult2 = "";
    	try {
    		em.persist(ontario);
    	} catch (Exception e) {
    		addResult2 = e.getMessage();
    	}
        
        // Colombie-Britannique
    	Province bc = new Province();
    	bc.setSimpleName("colombie-britannique");
    	bc.setFullName("Colombie-Britannique");
    	
    	String addResult3 = "";
    	try {
    		em.persist(bc);
    	} catch (Exception e) {
    		addResult3 = e.getMessage();
    	}

    	em.close();
    	
    	if (!addResult1.equals("") || !addResult2.equals("") || !addResult3.equals("")) {
    		return Response.status(412).entity("[\"" + addResult1 + "\",\"" + addResult2 + "\",\"" + addResult3 + "\"]").build();
    	} else {
    		return Response.status(201).entity("[\"" + addResult1 + "\",\"" + addResult2 + "\",\"" + addResult3 + "\"]").build();
    	}
    }
    
    /**
     * Retrieves representation of all instances of Province
     * @return an instance of String
     * @throws IOException 
     * @throws JsonMappingException 
     * @throws JsonGenerationException 
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON + "; charset=utf-8")
    public Response getAllProvinces() {
    	EntityManager em = emf.createEntityManager();
    	Query query = null;
    	List<Province> provinces = null;
    	try {
    		query = em.createQuery("SELECT m FROM Province as m");
    		provinces = (List<Province>) query.getResultList();
    	} catch (Exception e) {
    		return Response.status(500).entity("{\"message\": \"" + e.getMessage() + "\"}").build();
    	}
    	
        return Response.ok(provinces).build();
    }
    
    /**
     * PUT method for updating or creating an instance of Contact
     * @param contact a contact resource
     * @return an HTTP response with id of the updated or created resource.
     */
    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON + "; charset=utf-8")
	public Response putJson(Province province) {
    	// Rules
//    	List<Province> facts = new ArrayList<Province>();
//    	facts.add(province);
//    	
//    	RuleEngine<DroolsMessage> ruleEngine = new RuleEngine<DroolsMessage>("Province.drl", DroolsMessage.class);
//    	List<DroolsMessage> messages = ruleEngine.runWith(facts);
//    	
//    	for (DroolsMessage dm: messages) {
//    		if (dm.isError()) {
//    			return Response.status(409).entity("{\"response\":\"Erreur de validation\",\"code\":409}").build();
//    		}
//    	}
    	
    	// Persistence
    	EntityManager em = emf.createEntityManager();
    	BigInteger id;
    	try {
    		em.persist(province);
    		Query q = em.createNativeQuery("SELECT LAST_INSERT_ID();");
    		id = (BigInteger) q.getSingleResult();
    	} catch (Exception e) {
    		return Response.status(409).entity("{\"response\":\"" + e.getMessage() + "\",\"code\":409}").build();
    	}
    	
    	em.close();
    	
        return Response.status(201).entity("{\"response\":\"ok\",\"code\":200,\"id\":" + id + "}").build();
    }

}
