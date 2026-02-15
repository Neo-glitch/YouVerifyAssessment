package org.neo.yvstore.features.product.data.datasource.remote

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.Source
import org.neo.yvstore.core.common.dispatcher.DispatcherProvider
import org.neo.yvstore.core.network.utils.awaitWithTimeout
import org.neo.yvstore.features.product.data.datasource.remote.model.ProductDto

/**
 * Implementation of ProductRemoteDatasource using Firestore.
 *
 * @property firestore FirebaseFirestore instance for product data retrieval
 */
class ProductRemoteDatasourceImpl(
    private val firestore: FirebaseFirestore,
    private val dispatcherProvider: DispatcherProvider
) : ProductRemoteDatasource {

    override suspend fun getProducts(): List<ProductDto> {
        val querySnapshot = firestore.collection("products")
            .orderBy("created_at", Query.Direction.DESCENDING)
            .get(Source.SERVER)
            .awaitWithTimeout(dispatcher = dispatcherProvider.io)

        return querySnapshot.toObjects(ProductDto::class.java)
    }

    override suspend fun searchProducts(query: String): List<ProductDto> {
        val trimmed = query.trim().lowercase()

        if (trimmed.isEmpty()) {
            return getProducts()
        }

        val querySnapshot = firestore.collection("products")
            .orderBy("search_name")
            .whereGreaterThanOrEqualTo("search_name", trimmed)
            .whereLessThan("search_name", trimmed + '\uf8ff')
            .get(Source.SERVER)
            .awaitWithTimeout(dispatcher = dispatcherProvider.io)

        return querySnapshot.toObjects(ProductDto::class.java)
    }

}
