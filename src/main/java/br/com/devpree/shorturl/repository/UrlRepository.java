package br.com.devpree.shorturl.repository;

import java.util.Date;

import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.DocumentSnapshot;

import br.com.devpree.shorturl.repository.interfaces.IUrlRepository;
import br.com.devpree.shorturl.to.TOUrlDetails;
import br.com.devpree.shorturl.util.StringUtil;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;

@ApplicationScoped
@Transactional
public class UrlRepository extends FirebaseRepository implements IUrlRepository {
	private static String URLS_COLLECTION = "urls";
	
	@Override
	public TOUrlDetails getTOUrlByShortUrl(String shortUrl) throws Exception {
		DocumentReference document = db.collection(URLS_COLLECTION).document(shortUrl);
		DocumentSnapshot documentSnapshot = document.get().get();
		TOUrlDetails urlDetails = null;
		
		if (documentSnapshot != null && documentSnapshot.exists()) {
			urlDetails = new TOUrlDetails();
			urlDetails.setShortUrl(documentSnapshot.getId());
			urlDetails.setCompleteUrl(documentSnapshot.getString("completeUrl"));
			urlDetails.setCreationDate(documentSnapshot.getDate("creationDate"));
			urlDetails.setViews(documentSnapshot.getLong("views"));
		}
		
		return urlDetails;
	}

	@Override
	public TOUrlDetails createShortUrl(String completeUrl, Integer length) throws Exception {
		String shortUrl = this.getShortUrl(length);
		DocumentReference document = db.collection(URLS_COLLECTION).document(shortUrl);
		
		TOUrlDetails urlData = new TOUrlDetails();
		urlData.setCompleteUrl(completeUrl);
		urlData.setShortUrl(shortUrl);
		urlData.setCreationDate(new Date());
		urlData.setViews(0L);
		
		document.set(urlData);
		
		return urlData;
	}
	
	/**
	 * Returns a shortUrlId that does not exists on the database
	 * 
	 * @param length
	 * @return
	 * @throws Exception
	 */
	private String getShortUrl(Integer length) throws Exception {
		String shortUrl = null;
		
		do {
			shortUrl = StringUtil.generateRandomString(length);
		} while (db.collection(URLS_COLLECTION).document(shortUrl).get().get().exists());
		
		return shortUrl;
	}
}
