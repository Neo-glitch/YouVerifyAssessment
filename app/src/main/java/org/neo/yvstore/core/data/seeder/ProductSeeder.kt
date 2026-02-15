package org.neo.yvstore.core.data.seeder

import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore

object ProductSeeder {

    private const val TAG = "ProductSeeder"
    private const val COLLECTION = "products"

    fun seed(firestore: FirebaseFirestore) {
        val products = listOf(
            mapOf(
                "id" to "prod1",
                "name" to "Fjallraven - Foldsack No. 1 Backpack, Fits 15 Laptops",
                "description" to "Your perfect pack for everyday use and walks in the forest. Stash your laptop (up to 15 inches) in the padded sleeve, your everyday",
                "price" to 109.95,
                "image_url" to "https://fakestoreapi.com/img/81fPKd-2AYL._AC_SL1500_t.png",
                "rating" to 3.9,
                "review_count" to 120,
                "created_at" to "2025-01-10T08:00:00Z",
                "search_name" to "fjallraven - foldsack no. 1 backpack, fits 15 laptops"
            ),
            mapOf(
                "id" to "prod2",
                "name" to "Mens Casual Premium Slim Fit T-Shirts",
                "description" to "Slim-fitting style, contrast raglan long sleeve, three-button henley placket, light weight & soft fabric for breathable and comfortable wearing. And Solid stitched shirts with round neck made for durability and a great fit for casual fashion wear and diehard baseball fans. The Henley style round neckline includes a three-button placket.",
                "price" to 22.3,
                "image_url" to "https://fakestoreapi.com/img/71-3HjGNDUL._AC_SY879._SX._UX._SY._UY_t.png",
                "rating" to 4.1,
                "review_count" to 259,
                "created_at" to "2025-01-12T09:30:00Z",
                "search_name" to "mens casual premium slim fit t-shirts"
            ),
            mapOf(
                "id" to "prod3",
                "name" to "Mens Cotton Jacket",
                "description" to "great outerwear jackets for Spring/Autumn/Winter, suitable for many occasions, such as working, hiking, camping, mountain/rock climbing, cycling, traveling or other outdoors. Good gift choice for you or your family member. A warm hearted love to Father, husband or son in this thanksgiving or Christmas Day.",
                "price" to 55.99,
                "image_url" to "https://fakestoreapi.com/img/71li-ujtlUL._AC_UX679_t.png",
                "rating" to 4.7,
                "review_count" to 500,
                "created_at" to "2025-01-15T10:00:00Z",
                "search_name" to "mens cotton jacket"
            ),
            mapOf(
                "id" to "prod4",
                "name" to "Mens Casual Slim Fit",
                "description" to "The color could be slightly different between on the screen and in practice. / Please note that body builds vary by person, therefore, detailed size information should be reviewed below on the product description.",
                "price" to 15.99,
                "image_url" to "https://fakestoreapi.com/img/71YXzeOuslL._AC_UY879_t.png",
                "rating" to 2.1,
                "review_count" to 430,
                "created_at" to "2025-01-18T11:15:00Z",
                "search_name" to "mens casual slim fit"
            ),
            mapOf(
                "id" to "prod5",
                "name" to "John Hardy Women's Legends Naga Gold & Silver Dragon Station Chain Bracelet",
                "description" to "From our Legends Collection, the Naga was inspired by the mythical water dragon that protects the ocean's pearl. Wear facing inward to be bestowed with love and abundance, or outward for protection.",
                "price" to 695.0,
                "image_url" to "https://fakestoreapi.com/img/71pWzhdJNwL._AC_UL640_QL65_ML3_t.png",
                "rating" to 4.6,
                "review_count" to 400,
                "created_at" to "2025-01-20T12:00:00Z",
                "search_name" to "john hardy women's legends naga gold & silver dragon station chain bracelet"
            ),
            mapOf(
                "id" to "prod6",
                "name" to "Solid Gold Petite Micropave",
                "description" to "Satisfaction Guaranteed. Return or exchange any order within 30 days.Designed and sold by Hafeez Center in the United States. Satisfaction Guaranteed. Return or exchange any order within 30 days.",
                "price" to 168.0,
                "image_url" to "https://fakestoreapi.com/img/61sbMiUnoGL._AC_UL640_QL65_ML3_t.png",
                "rating" to 3.9,
                "review_count" to 70,
                "created_at" to "2025-01-22T13:30:00Z",
                "search_name" to "solid gold petite micropave"
            ),
            mapOf(
                "id" to "prod7",
                "name" to "White Gold Plated Princess",
                "description" to "Classic Created Wedding Engagement Solitaire Diamond Promise Ring for Her. Gifts to spoil your love more for Engagement, Wedding, Anniversary, Valentine's Day...",
                "price" to 9.99,
                "image_url" to "https://fakestoreapi.com/img/71YAIFU48IL._AC_UL640_QL65_ML3_t.png",
                "rating" to 3.0,
                "review_count" to 400,
                "created_at" to "2025-01-25T14:00:00Z",
                "search_name" to "white gold plated princess"
            ),
            mapOf(
                "id" to "prod8",
                "name" to "Pierced Owl Rose Gold Plated Stainless Steel Double",
                "description" to "Rose Gold Plated Double Flared Tunnel Plug Earrings. Made of 316L Stainless Steel",
                "price" to 10.99,
                "image_url" to "https://fakestoreapi.com/img/51UDEzMJVpL._AC_UL640_QL65_ML3_t.png",
                "rating" to 1.9,
                "review_count" to 100,
                "created_at" to "2025-01-28T08:45:00Z",
                "search_name" to "pierced owl rose gold plated stainless steel double"
            ),
            mapOf(
                "id" to "prod9",
                "name" to "WD 2TB Elements Portable External Hard Drive - USB 3.0",
                "description" to "USB 3.0 and USB 2.0 Compatibility Fast data transfers Improve PC Performance High Capacity; Compatibility Formatted NTFS for Windows 10, Windows 8.1, Windows 7; Reformatting may be required for other operating systems; Compatibility may vary depending on user's hardware configuration and operating system",
                "price" to 64.0,
                "image_url" to "https://fakestoreapi.com/img/61IBBVJvSDL._AC_SY879_t.png",
                "rating" to 3.3,
                "review_count" to 203,
                "created_at" to "2025-02-01T09:00:00Z",
                "search_name" to "wd 2tb elements portable external hard drive - usb 3.0"
            ),
            mapOf(
                "id" to "prod10",
                "name" to "SanDisk SSD PLUS 1TB Internal SSD - SATA III 6 Gb/s",
                "description" to "Easy upgrade for faster boot up, shutdown, application load and response (As compared to 5400 RPM SATA 2.5\" hard drive; Based on published specifications and internal benchmarking tests using PCMark vantage scores) Boosts burst write performance, making it ideal for typical PC workloads The perfect balance of performance and reliability Read/write speeds of up to 535MB/s/450MB/s (Based on internal testing; Performance may vary depending upon drive capacity, host device, OS and application.)",
                "price" to 109.0,
                "image_url" to "https://fakestoreapi.com/img/61U7T1koQqL._AC_SX679_t.png",
                "rating" to 2.9,
                "review_count" to 470,
                "created_at" to "2025-02-05T10:30:00Z",
                "search_name" to "sandisk ssd plus 1tb internal ssd - sata iii 6 gb/s"
            ),
            mapOf(
                "id" to "prod11",
                "name" to "Silicon Power 256GB SSD 3D NAND A55 SLC Cache Performance Boost SATA III 2.5",
                "description" to "3D NAND flash are applied to deliver high transfer speeds Remarkable transfer speeds that enable faster bootup and improved overall system performance. The advanced SLC Cache Technology allows performance boost and longer lifespan 7mm slim design suitable for Ultrabooks and Ultra-slim notebooks. Supports TRIM command, Garbage Collection technology, RAID, and ECC (Error Checking & Correction) to provide the optimized performance and enhanced reliability.",
                "price" to 109.0,
                "image_url" to "https://fakestoreapi.com/img/71kWymZ+c+L._AC_SX679_t.png",
                "rating" to 4.8,
                "review_count" to 319,
                "created_at" to "2025-02-08T11:00:00Z",
                "search_name" to "silicon power 256gb ssd 3d nand a55 slc cache performance boost sata iii 2.5"
            ),
            mapOf(
                "id" to "prod12",
                "name" to "WD 4TB Gaming Drive Works with Playstation 4 Portable External Hard Drive",
                "description" to "Expand your PS4 gaming experience, Play anywhere Fast and easy, setup Sleek design with high capacity, 3-year manufacturer's limited warranty",
                "price" to 114.0,
                "image_url" to "https://fakestoreapi.com/img/61mtL65D4cL._AC_SX679_t.png",
                "rating" to 4.8,
                "review_count" to 400,
                "created_at" to "2025-02-12T12:15:00Z",
                "search_name" to "wd 4tb gaming drive works with playstation 4 portable external hard drive"
            ),
            mapOf(
                "id" to "prod13",
                "name" to "Acer SB220Q bi 21.5 inches Full HD (1920 x 1080) IPS Ultra-Thin",
                "description" to "21. 5 inches Full HD (1920 x 1080) widescreen IPS display And Radeon free Sync technology. No compatibility for VESA Mount Refresh Rate: 75Hz - Using HDMI port Zero-frame design | ultra-thin | 4ms response time | IPS panel Aspect ratio - 16: 9. Color Supported - 16. 7 million colors. Brightness - 250 nit Tilt angle -5 degree to 15 degree. Horizontal viewing angle-178 degree. Vertical viewing angle-178 degree 75 hertz",
                "price" to 599.0,
                "image_url" to "https://fakestoreapi.com/img/81QpkIctqPL._AC_SX679_t.png",
                "rating" to 2.9,
                "review_count" to 250,
                "created_at" to "2025-02-15T13:00:00Z",
                "search_name" to "acer sb220q bi 21.5 inches full hd (1920 x 1080) ips ultra-thin"
            ),
            mapOf(
                "id" to "prod14",
                "name" to "Samsung 49-Inch CHG90 144Hz Curved Gaming Monitor (LC49HG90DMNXZA) – Super Ultrawide Screen QLED",
                "description" to "49 INCH SUPER ULTRAWIDE 32:9 CURVED GAMING MONITOR with dual 27 inch screen side by side QUANTUM DOT (QLED) TECHNOLOGY, HDR support and factory calibration provides stunningly realistic and accurate color and contrast 144HZ HIGH REFRESH RATE and 1ms ultra fast response time work to eliminate motion blur, ghosting, and reduce input lag",
                "price" to 999.99,
                "image_url" to "https://fakestoreapi.com/img/81Zt42ioCgL._AC_SX679_t.png",
                "rating" to 2.2,
                "review_count" to 140,
                "created_at" to "2025-02-18T14:30:00Z",
                "search_name" to "samsung 49-inch chg90 144hz curved gaming monitor (lc49hg90dmnxza) – super ultrawide screen qled"
            ),
            mapOf(
                "id" to "prod15",
                "name" to "BIYLACLESEN Women's 3-in-1 Snowboard Jacket Winter Coats",
                "description" to "Note:The Jackets is US standard size, Please choose size as your usual wear Material: 100% Polyester; Detachable Liner Fabric: Warm Fleece. Detachable Functional Liner: Skin Friendly, Lightweigt and Warm.Stand Collar Liner jacket, keep you warm in cold weather. Zippered Pockets: 2 Zippered Hand Pockets, 2 Zippered Pockets on Chest (enough to keep cards or keys)and 1 Hidden Pocket Inside.Zippered Hand Pockets and Hidden Pocket keep your things secure. Humanized Design: Adjustable and Detachable Hood and Adjustable cuff to prevent the wind and water,for a comfortable fit. 3 in 1 Detachable Design provide more convenience, you can separate the coat and inner as needed, or wear it together. It is suitable for different season and help you adapt to different climates",
                "price" to 56.99,
                "image_url" to "https://fakestoreapi.com/img/51Y5NI-I5jL._AC_UX679_t.png",
                "rating" to 2.6,
                "review_count" to 235,
                "created_at" to "2025-02-20T08:00:00Z",
                "search_name" to "biylaclesen women's 3-in-1 snowboard jacket winter coats"
            ),
            mapOf(
                "id" to "prod16",
                "name" to "Lock and Love Women's Removable Hooded Faux Leather Moto Biker Jacket",
                "description" to "100% POLYURETHANE(shell) 100% POLYESTER(lining) 75% POLYESTER 25% COTTON (SWEATER), Faux leather material for style and comfort / 2 pockets of front, 2-For-One Hooded denim style faux leather jacket, Button detail on waist / Detail stitching at sides, HAND WASH ONLY / DO NOT BLEACH / LINE DRY / DO NOT IRON",
                "price" to 29.95,
                "image_url" to "https://fakestoreapi.com/img/81XH0e8fefL._AC_UY879_t.png",
                "rating" to 2.9,
                "review_count" to 340,
                "created_at" to "2025-02-22T09:30:00Z",
                "search_name" to "lock and love women's removable hooded faux leather moto biker jacket"
            ),
            mapOf(
                "id" to "prod17",
                "name" to "Rain Jacket Women Windbreaker Striped Climbing Raincoats",
                "description" to "Lightweight perfet for trip or casual wear---Long sleeve with hooded, adjustable drawstring waist design. Button and zipper front closure raincoat, fully stripes Lined and The Raincoat has 2 side pockets are a good size to hold all kinds of things, it covers the hips, and the hood is generous but doesn't overdo it.Attached Cotton Lined Hood with Adjustable Drawstrings give it a real styled look.",
                "price" to 39.99,
                "image_url" to "https://fakestoreapi.com/img/71HblAHs5xL._AC_UY879_-2t.png",
                "rating" to 3.8,
                "review_count" to 679,
                "created_at" to "2025-02-25T10:45:00Z",
                "search_name" to "rain jacket women windbreaker striped climbing raincoats"
            ),
            mapOf(
                "id" to "prod18",
                "name" to "MBJ Women's Solid Short Sleeve Boat Neck V",
                "description" to "95% RAYON 5% SPANDEX, Made in USA or Imported, Do Not Bleach, Lightweight fabric with great stretch for comfort, Ribbed on sleeves and neckline / Double stitching on bottom hem",
                "price" to 9.85,
                "image_url" to "https://fakestoreapi.com/img/71z3kpMAYsL._AC_UY879_t.png",
                "rating" to 4.7,
                "review_count" to 130,
                "created_at" to "2025-02-28T11:00:00Z",
                "search_name" to "mbj women's solid short sleeve boat neck v"
            ),
            mapOf(
                "id" to "prod19",
                "name" to "Opna Women's Short Sleeve Moisture",
                "description" to "100% Polyester, Machine wash, 100% cationic polyester interlock, Machine Wash & Pre Shrunk for a Great Fit, Lightweight, roomy and highly breathable with moisture wicking fabric which helps to keep moisture away, Soft Lightweight Fabric with comfortable V-neck collar and a slimmer fit, delivers a sleek, more feminine silhouette and Added Comfort",
                "price" to 7.95,
                "image_url" to "https://fakestoreapi.com/img/51eg55uWmdL._AC_UX679_t.png",
                "rating" to 4.5,
                "review_count" to 146,
                "created_at" to "2025-03-02T12:30:00Z",
                "search_name" to "opna women's short sleeve moisture"
            ),
            mapOf(
                "id" to "prod20",
                "name" to "DANVOUY Womens T Shirt Casual Cotton Short",
                "description" to "95%Cotton,5%Spandex, Features: Casual, Short Sleeve, Letter Print,V-Neck,Fashion Tees, The fabric is soft and has some stretch., Occasion: Casual/Office/Beach/School/Home/Street. Season: Spring,Summer,Autumn,Winter.",
                "price" to 12.99,
                "image_url" to "https://fakestoreapi.com/img/61pHAEJ4NML._AC_UX679_t.png",
                "rating" to 3.6,
                "review_count" to 145,
                "created_at" to "2025-03-05T13:00:00Z",
                "search_name" to "danvouy womens t shirt casual cotton short"
            ),
        )

        val batch = firestore.batch()

        products.forEach { product ->
            val docId = product["id"] as String
            val docRef = firestore.collection(COLLECTION).document(docId)
            batch.set(docRef, product)
        }

        batch.commit()
            .addOnSuccessListener { Log.d(TAG, "All ${products.size} products seeded successfully") }
            .addOnFailureListener { e -> Log.e(TAG, "Failed to seed products", e) }
    }
}
