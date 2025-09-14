package br.com.devpree.shorturl.webservice;

import java.io.Serializable;
import java.net.URL;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;

import br.com.devpree.shorturl.repository.interfaces.IUrlRepository;
import br.com.devpree.shorturl.request.TOCreateUrlRequestRestModel;
import br.com.devpree.shorturl.request.TODeleteUrlRequestRestModel;
import br.com.devpree.shorturl.response.TOCreateUrlResponseRestModel;
import br.com.devpree.shorturl.response.TODeleteUrlResponseRestModel;
import br.com.devpree.shorturl.to.TOUrlDetails;
import br.com.devpree.shorturl.util.StringUtil;
import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/url")
public class WSUrl implements Serializable {
	
	private static final long serialVersionUID = -2339071140393062309L;
	
	@Inject
	private IUrlRepository urlRepository;
	
	private URL serviceAccountStream;
	private FirebaseOptions firebaseOptions;
	
	/**
	 * Init Firebase Config
	 * 
	 * TODO - Find a better to init this config 
	 */
	public WSUrl() {
		try {
			serviceAccountStream = this.getClass().getClassLoader().getResource("firebase-service-account.json");
			firebaseOptions = FirebaseOptions
					.builder()
					.setCredentials(GoogleCredentials.fromStream(serviceAccountStream.openStream()))
					.setProjectId("shorturl-devpree")
					.build();
			
			FirebaseApp.initializeApp(firebaseOptions);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Creates a short URL for a Large URL. Sets a customUrl or a randomURL with specific or default lenght.
	 * 
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@Path("/create")
	@PUT
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response create(TOCreateUrlRequestRestModel request) throws Exception { 
		try {
			TOCreateUrlResponseRestModel response = new TOCreateUrlResponseRestModel();
			
			if (StringUtil.isNull(request.getLongUrl())) {
				return Response.status(Response.Status.BAD_REQUEST)
						.entity(new Exception("Field longUrl is Mandatory"))
						.build();
			}
			
			if (request.getLength() != null && request.getLength() == 0) {
				return Response.status(Response.Status.BAD_REQUEST)
						.entity(new Exception("Field length needs to higher than 0"))
						.build();
			}
			
			if (StringUtil.isNotNull(request.getCustomAlias())) {
				boolean existsUrlWithCustomId = urlRepository.getTOUrlByShortUrl(request.getCustomAlias()) != null;
				
				if (existsUrlWithCustomId) {
					return Response.status(Response.Status.CONFLICT)
							.entity(new Exception("CustomAlias is already in use for another URL. Change the customAlias to continue"))
							.build();
				}
			}
			
			TOUrlDetails urlDetails = urlRepository.createShortUrl(request.getLongUrl(), request.getCustomAlias(), request.getLength());
			response.setShortUrl(urlDetails.getShortUrl());
			
			return Response.ok(response).build();
		} catch (Exception e) {
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
					.entity(e)
					.build();
		}
	}
	
	/**
	 * Returns the original url by the short url. 
	 * 
	 * @param request
	 * @return
	 */
	@Path("/get/original")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response getOriginalUrl(@QueryParam("shortUrl") String shortUrl) {
		try {
			if (StringUtil.isNull(shortUrl)) {
				return Response.status(Response.Status.BAD_REQUEST)
						.entity(new Exception("Field shortUrl is Mandatory"))
						.build();
			}
			
			String originalUrl = urlRepository.getCompleteURLIncreasingViews(shortUrl);
			
			if (StringUtil.isNull(originalUrl)) {
				return Response.status(Response.Status.NOT_FOUND)
						.entity(new Exception("The URL is not available"))
						.build();
			}
			
			return Response.ok(originalUrl).build();
		} catch (Exception e) {
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
					.entity(e)
					.build();
		}
	}
	
	
	/**
	 * Delete URL document from database
	 * 
	 * @param request
	 * @return
	 */
	@Path("/delete")
	@DELETE
	@Produces(MediaType.APPLICATION_JSON)
	public Response deleteUrl(TODeleteUrlRequestRestModel request) {
		try {
			if (StringUtil.isNull(request.getShortUrl())) {
				return Response.status(Response.Status.BAD_REQUEST)
						.entity(new Exception("Field shortUrl is Mandatory"))
						.build();
			}
			
			urlRepository.deleteUrl(request.getShortUrl());
			
			TODeleteUrlResponseRestModel response = new TODeleteUrlResponseRestModel();
			response.setMessage("Url " + request.getShortUrl() +  " deleted from database");
			
			return Response.ok().entity(response).build();
		} catch (Exception e) {
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
					.entity(e)
					.build();
		}
	}
}
