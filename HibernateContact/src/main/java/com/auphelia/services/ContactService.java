package com.auphelia.services;

import java.io.IOException;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceUnit;
import javax.persistence.Query;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import com.auphelia.models.Contact;
import com.auphelia.models.Province;
import com.auphelia.rules.DroolsMessage;
import com.auphelia.rules.RuleEngine;


@Path("/contact")
@Stateless
public class ContactService {
	@PersistenceUnit
	EntityManagerFactory emf;
    
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
    	Query q = em.createNativeQuery("DELETE FROM contact.contact WHERE id != 0;");
    	q.executeUpdate();
    	
    	// Emile
    	Contact emile = new Contact();
    	
    	emile.setEmail("nostradamus1935@gmail.com");
    	emile.setPrenom("Émile");
    	emile.setNom("Plourde-Lavoie");
    	emile.setRue("1234 Amherst");
    	emile.setVille("Montréal");
    	emile.setProvince("Québec");
    	emile.setCodePostal("H1S3P8");
    	emile.setId(0);
    	
    	String addResult1 = "";
    	try {
    		em.persist(emile);
    	} catch (Exception e) {
    		addResult1 = e.getMessage();
    	}
        
        // Test
    	Contact test = new Contact();
    	
    	test.setEmail("test@test.com");
    	test.setPrenom("Test");
    	test.setNom("Goodfellow");
    	test.setRue("4321 Nice St.");
    	test.setVille("Montréal");
    	test.setProvince("Québec");
    	test.setCodePostal("H2P2P1");
    	
    	String addResult2 = "";
    	try {
    		em.persist(test);
    	} catch (Exception e) {
    		addResult2 = e.getMessage();
    	}
        
        // Jean
    	Contact jean = new Contact();
    	
    	jean.setEmail("jean@perdu.com");
    	jean.setPrenom("John");
    	jean.setNom("Doe");
    	jean.setRue("4321 Perdu");
    	jean.setVille("Montréal");
    	jean.setProvince("Québec");
    	jean.setCodePostal("H1Z3A5");
    	
    	String addResult3 = "";
    	try {
    		em.persist(jean);
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
     * Retrieves representation of all instances of Contact
     * @return an instance of String
     * @throws IOException 
     * @throws JsonMappingException 
     * @throws JsonGenerationException 
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON + "; charset=utf-8")
    public Response getAllContacts() {
    	EntityManager em = emf.createEntityManager();
    	Query query = null;
    	List<Contact> contacts = null;
    	try {
    		query = em.createQuery("SELECT m FROM Contact as m");
    		contacts = (List<Contact>) query.getResultList();
    	} catch (Exception e) {
    		return Response.status(500).entity("{\"message\": \"" + e.getMessage() + "\"}").build();
    	}
    	
        return Response.ok(contacts).build();
    }
    
    
    /**
     * Retrieves representation of an instance of Contact from email
     * @param email email of contact to return
     * @return a Response with the result of the operation
     * @throws IOException 
     * @throws JsonMappingException 
     * @throws JsonGenerationException 
     */
    @GET
    @Path("/{email}")
    @Produces(MediaType.APPLICATION_JSON + "; charset=utf-8")
    public Response getContactByEmail(@PathParam("email") String email) {
    	EntityManager em = emf.createEntityManager();
    	Query query = null;
    	Contact contact = null;
    	try {
    		query = em.createQuery("SELECT m FROM Contact as m WHERE email = '" + email + "'");
    		contact = (Contact) query.getSingleResult();
    	} catch (Exception e) {
    		return Response.status(500).entity("{\"message\": \"" + e.getMessage() + "\"}").build();
    	}
    	
        return Response.ok(contact).build();
    }

    /**
     * Retrieves representation of an instance of Contact from the first name and the last name
     * @param prenom first name of contact to return
     * @param nom last name of contact to return
     * @return an instance of String
     * @throws IOException 
     * @throws JsonMappingException 
     * @throws JsonGenerationException 
     */
    @GET
    @Path("/{prenom}/{nom}")
    @Produces(MediaType.APPLICATION_JSON + "; charset=utf-8")
    public List<Contact> getContactByFullName(@PathParam("prenom") String prenom, @PathParam("nom") String nom) {
    	EntityManager em = emf.createEntityManager();
    	Query query = em.createQuery("SELECT m FROM Contact as m WHERE prenom = '" + prenom + "' AND nom = '" + nom + "'");
    	
        return query.getResultList();
    }
    
    /**
     * PUT method for updating or creating an instance of Contact
     * @param contact a contact resource
     * @return an HTTP response with id of the updated or created resource.
     */
    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON + "; charset=utf-8")
	public Response putJson(Contact contact) {
    	// Dependents
    	ProvinceService provinceService = new ProvinceService(emf);
    	List<Province> provinces = (List<Province>) provinceService.getAllProvinces().getEntity();
    	Collection nomsProvinces = new ArrayList<String>();
    	
    	for (Province p: provinces) {
    		nomsProvinces.add(p.getFullName());
    	}
    	
    	// Rules
    	List<Object> facts = new ArrayList<Object>();
    	facts.add(nomsProvinces);
    	facts.add(contact);
    	
    	RuleEngine<DroolsMessage> ruleEngine = new RuleEngine<DroolsMessage>("Contact.drl", DroolsMessage.class);
    	List<DroolsMessage> messages = ruleEngine.runWith(facts);
    	
    	JSONObject errorsMap = new JSONObject();
    	if (messages.size() > 0) {
    		boolean errorMessagePresent = false;
    		for (DroolsMessage message: messages) {
    			if (message.isError()) {
    				errorMessagePresent = true;
    			}
    			// DEBUG ALL
    			System.out.println(message.getType().toString() + " == " + message.getRule() + " on " + message.getField() + " : " + message.getMessage());
    			if (errorsMap.get(message.getField()) == null) {
    				errorsMap.put(message.getField(), new JSONArray());
    			}
    			errorsMap.accumulate(message.getField(), message.getMessage());
    		}
            
    		if (errorMessagePresent) {
    			return Response.status(422).entity(errorsMap).build();
    		}
    	}
    	
    	// Persistence
    	EntityManager em = emf.createEntityManager();
    	BigInteger id;
    	try {
    		em.persist(contact);
    		Query q = em.createNativeQuery("SELECT LAST_INSERT_ID();");
    		id = (BigInteger) q.getSingleResult();
    	} catch (Exception e) {
    		return Response.status(409).entity("{\"response\":\"" + e.getMessage() + "\",\"code\":409}").build();
    	}
    	
    	em.close();
    	
        return Response.status(201).entity("{\"response\":\"ok\",\"code\":200,\"id\":" + id + "}").build();
    }
    
    /**
     * POST method to update a contact by email
     * @param email email of contact to modify
     * @param contact representation of the contact resource after modification
     * @return a Response with the result of the operation
     * @throws IOException 
     * @throws JsonMappingException 
     * @throws JsonGenerationException 
     */
    @POST
    @Path("/{email}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON + "; charset=UTF-8")
    public Response changeContact(@PathParam("email") String email, Contact contact){
    	EntityManager em = emf.createEntityManager();
    	Query q = em.createNativeQuery("UPDATE Contact SET prenom = '" + contact.getPrenom() + "', nom = '" + contact.getNom() + "', email = '"+ contact.getEmail() + "', rue = '" + contact.getRue() + "', ville = '" + contact.getVille() + "', province = '" + contact.getProvince() + "', code_postal = '" + contact.getCodePostal() + "' WHERE email = '" + contact.getEmail() + "'");
    	q.executeUpdate();
    	em.close();
    	
    	return Response.status(200).entity("{\"response\":\"ok\",\"code\":200}").build();
    }
    
    /**
     * DELETE method to delete a contact by email
     * @param email email of contact to modify
     * @return a Response with the result of the operation
     * @throws IOException 
     * @throws JsonMappingException 
     * @throws JsonGenerationException 
     */
    @DELETE
    @Path("/{email}")
    @Produces(MediaType.APPLICATION_JSON + "; charset=UTF-8")
    public Response deleteContact(@PathParam("email") String email) {
    	EntityManager em = emf.createEntityManager();
    	Query query = em.createQuery("DELETE FROM Contact as m WHERE email = '" + email + "'");
    	query.executeUpdate();
    	em.flush();
    	
        return Response.status(200).entity("{\"response\":\"ok\",\"code\":200}").build();
    }

}
