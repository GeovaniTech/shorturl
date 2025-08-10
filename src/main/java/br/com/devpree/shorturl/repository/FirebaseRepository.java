package br.com.devpree.shorturl.repository;

import com.google.cloud.firestore.Firestore;
import com.google.firebase.cloud.FirestoreClient;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;

@ApplicationScoped
@Transactional
public abstract class FirebaseRepository {
	protected Firestore db = FirestoreClient.getFirestore();
}
