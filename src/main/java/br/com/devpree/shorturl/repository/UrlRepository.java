package br.com.devpree.shorturl.repository;

import java.util.Date;
import java.util.List;

import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.QueryDocumentSnapshot;

import br.com.devpree.shorturl.exception.UrlNotFoundException;
import br.com.devpree.shorturl.repository.interfaces.IUrlRepository;
import br.com.devpree.shorturl.to.TOUrlDetails;
import br.com.devpree.shorturl.util.StringUtil;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;

@ApplicationScoped
@Transactional
public class UrlRepository extends FirebaseRepository implements IUrlRepository {
	private static String URLS_COLLECTION = "urls";
	private static String BASE_URL = "https://ly.devpree.com.br/";
	
	@Override
	public TOUrlDetails getTOUrlByShortUrl(String shortUrl) throws Exception {
		DocumentReference document = db.collection(URLS_COLLECTION).document(shortUrl);
		DocumentSnapshot documentSnapshot = document.get().get();
		TOUrlDetails urlDetails = null;
		
		if (documentSnapshot != null && documentSnapshot.exists()) {
			urlDetails = new TOUrlDetails();
			urlDetails.setShortUrl(documentSnapshot.getId());
			urlDetails.setLongUrl(documentSnapshot.getString("longUrl"));
			urlDetails.setCreationDate(documentSnapshot.getDate("creationDate"));
			urlDetails.setViews(documentSnapshot.getLong("views"));
		}
		
		return urlDetails;
	}

	@Override
	public TOUrlDetails createShortUrl(String completeUrl, String customId, Integer length) throws Exception {
		String shortUrl = StringUtil.isNull(customId) ? this.getShortUrl(length) : customId;
		
		DocumentReference document = db.collection(URLS_COLLECTION).document(shortUrl);
		
		TOUrlDetails urlData = new TOUrlDetails();
		urlData.setLongUrl(completeUrl);
		urlData.setShortUrl(BASE_URL + shortUrl);
		urlData.setCreationDate(new Date());
		urlData.setViews(0L);
		
		document.set(urlData);
		
		return urlData;
	}
	
	@Override
	public String getCompleteURLIncreasingViews(String shortUrl) throws Exception {
		List<QueryDocumentSnapshot> documents = db.collection(URLS_COLLECTION).whereEqualTo("shortUrl", shortUrl).get().get().getDocuments();
		
		if (documents.isEmpty()) {
			return null;
		}
		
		DocumentReference document = documents.get(0).getReference();
		DocumentSnapshot snapshot = document.get().get();
		
		if (snapshot != null && snapshot.exists()) {
			Long views = snapshot.getLong("views") + 1L;
			document.update("views", views);
			
			return snapshot.getString("longUrl");
		}
		
		return null;
	}
	
	@Override
	public void deleteUrl(String shortUrl) throws Exception {
		List<QueryDocumentSnapshot> documents = db.collection(URLS_COLLECTION).whereEqualTo("shortUrl", shortUrl).get().get().getDocuments();
		
		if (documents.isEmpty()) {
			throw new UrlNotFoundException("Url does not exist");
		}
		
		DocumentReference document = documents.get(0).getReference();
		document.delete();
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
