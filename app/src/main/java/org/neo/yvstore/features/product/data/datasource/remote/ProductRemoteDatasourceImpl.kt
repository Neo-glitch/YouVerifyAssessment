package org.neo.yvstore.features.product.data.datasource.remote

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.coroutines.tasks.await
import org.neo.yvstore.features.product.data.datasource.remote.model.ProductDto

/**
 * Implementation of ProductRemoteDatasource using Firestore.
 *
 * @property firestore FirebaseFirestore instance for product data retrieval
 */
class ProductRemoteDatasourceImpl(
    private val firestore: FirebaseFirestore
) : ProductRemoteDatasource {

    override suspend fun getProducts(): List<ProductDto> {
        val querySnapshot = firestore.collection("products")
            .orderBy("created_at", Query.Direction.DESCENDING)
            .get()
            .await()

        return querySnapshot.toObjects(ProductDto::class.java)
    }

    override suspend fun searchProducts(query: String): List<ProductDto> {
        val querySnapshot = firestore.collection("products")
            .orderBy("created_at", Query.Direction.DESCENDING)
            .get()
            .await()

        val allProducts = querySnapshot.toObjects(ProductDto::class.java)

        return if (query.isEmpty()) {
            allProducts
        } else {
            allProducts.filter { it.name.contains(query, ignoreCase = true) }
        }
    }
}
