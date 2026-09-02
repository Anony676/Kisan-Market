package com.example.data.sample

data class VarietySeedItem(
    val varietyName: String,
    val cropName: String,
    val category: String,
    val seedCompanyOrType: String,
    val typicalUnit: String = "kg",
    val benchmarkPriceRange: String,
    val keyTraits: String
)

object AgriculturalProduceCatalog {

    val CATEGORIES = listOf(
        "Vegetables",
        "Grains and Wheat",
        "Fresh Fruits",
        "Pulses and Dal",
        "Farm Spices"
    )

    val VARIETY_CATALOG: Map<String, List<VarietySeedItem>> = mapOf(
        "Vegetables" to listOf(
            VarietySeedItem(
                varietyName = "Nashik Garwa Red",
                cropName = "Red Onion (प्याज)",
                category = "Vegetables",
                seedCompanyOrType = "Desi Farmer Line / NHRDF Red",
                typicalUnit = "quintal",
                benchmarkPriceRange = "₹2,200 - ₹2,800/quintal",
                keyTraits = "Thick multi-layered dry scales, low moisture, 4-5 months aerated shelf-life."
            ),
            VarietySeedItem(
                varietyName = "Bhima Dark Red / Super",
                cropName = "Kharif Onion (लाल प्याज)",
                category = "Vegetables",
                seedCompanyOrType = "ICAR-DOGR Certified Seed",
                typicalUnit = "quintal",
                benchmarkPriceRange = "₹2,400 - ₹3,100/quintal",
                keyTraits = "Deep crimson red bulbs, fast 100-day maturity, high pungency."
            ),
            VarietySeedItem(
                varietyName = "Abhinav / US-440",
                cropName = "Hybrid Tomato (टमाटर)",
                category = "Vegetables",
                seedCompanyOrType = "Syngenta / Seminis Hybrid F1",
                typicalUnit = "crate",
                benchmarkPriceRange = "₹350 - ₹500/crate (25kg)",
                keyTraits = "Firm square-round fruit, thick pericarp wall, excellent for long-distance transport."
            ),
            VarietySeedItem(
                varietyName = "Arka Rakshak",
                cropName = "Disease Resistant Tomato (टमाटर)",
                category = "Vegetables",
                seedCompanyOrType = "IIHR Certified Hybrid",
                typicalUnit = "crate",
                benchmarkPriceRange = "₹380 - ₹520/crate (25kg)",
                keyTraits = "Triple disease resistance (ToLCV + BW + EB), deep crimson high lycopene yield."
            ),
            VarietySeedItem(
                varietyName = "Kufri Pukhraj / Jyoti",
                cropName = "Potato (आलू)",
                category = "Vegetables",
                seedCompanyOrType = "CPRI Seed Certified Tubers",
                typicalUnit = "quintal",
                benchmarkPriceRange = "₹1,400 - ₹1,800/quintal",
                keyTraits = "Smooth yellow skin, shallow eyes, high dry matter, early bulker (70-90 days)."
            ),
            VarietySeedItem(
                varietyName = "Kufri Chipsona-1 / Lady Rosetta",
                cropName = "Processing Potato (चिप्स आलू)",
                category = "Vegetables",
                seedCompanyOrType = "Contract Farming Certified Grade",
                typicalUnit = "quintal",
                benchmarkPriceRange = "₹1,800 - ₹2,300/quintal",
                keyTraits = "Low reducing sugars (<0.1%), high specific gravity, premium fry color."
            ),
            VarietySeedItem(
                varietyName = "Indra F1 / Asha",
                cropName = "Green Bell Pepper (शिमला मिर्च)",
                category = "Vegetables",
                seedCompanyOrType = "Syngenta Greenhouse F1",
                typicalUnit = "kg",
                benchmarkPriceRange = "₹45 - ₹65/kg",
                keyTraits = "4-lobed blocky shape, glossy dark green, thick flesh with long crispness."
            ),
            VarietySeedItem(
                varietyName = "Radhika F1 / Mahyco 10",
                cropName = "Okra / Bhindi (भिंडी)",
                category = "Vegetables",
                seedCompanyOrType = "Advanta / Mahyco Hybrid",
                typicalUnit = "kg",
                benchmarkPriceRange = "₹30 - ₹45/kg",
                keyTraits = "Deep dark green 5-ridged tender pods, resistant to YVMV and enation leaf curl."
            ),
            VarietySeedItem(
                varietyName = "Pusa Snowball / Girija",
                cropName = "Cauliflower (फूलगोभी)",
                category = "Vegetables",
                seedCompanyOrType = "IARI / Seminis",
                typicalUnit = "kg",
                benchmarkPriceRange = "₹22 - ₹35/kg",
                keyTraits = "Compact snow-white curds, self-blanching leaves, non-yellowing."
            ),
            VarietySeedItem(
                varietyName = "Manjari Gota / Ravaiya",
                cropName = "Round Brinjal (काटेरी वांगी / बैंगन)",
                category = "Vegetables",
                seedCompanyOrType = "MPKV Rahuri / Mahyco",
                typicalUnit = "kg",
                benchmarkPriceRange = "₹24 - ₹38/kg",
                keyTraits = "Purplish-pink with white stripes, spine on calyx, soft sweet pulp without bitterness."
            )
        ),
        "Grains and Wheat" to listOf(
            VarietySeedItem(
                varietyName = "Sharbati (C-306 / MP Gold)",
                cropName = "Premium Sharbati Wheat (शरबती गेहूं)",
                category = "Grains and Wheat",
                seedCompanyOrType = "Sehore / Vidisha Certified Desi",
                typicalUnit = "quintal",
                benchmarkPriceRange = "₹3,400 - ₹4,200/quintal",
                keyTraits = "Golden amber bold grains, high natural sucrose, softest dough with long moisture retention."
            ),
            VarietySeedItem(
                varietyName = "HD-2967 / HD-3086 (Pusa Gautami)",
                cropName = "Bread Wheat (गेहूं)",
                category = "Grains and Wheat",
                seedCompanyOrType = "ICAR-IARI Certified Breeder Seed",
                typicalUnit = "quintal",
                benchmarkPriceRange = "₹2,350 - ₹2,600/quintal",
                keyTraits = "High yielding double dwarf, rust resistant, 12.5% protein with strong gluten matrix."
            ),
            VarietySeedItem(
                varietyName = "DBW-187 (Karan Vandana) / DBW-222",
                cropName = "Bio-fortified Wheat (करन वंदना गेहूं)",
                category = "Grains and Wheat",
                seedCompanyOrType = "IIWBR Karnal Certified",
                typicalUnit = "quintal",
                benchmarkPriceRange = "₹2,500 - ₹2,800/quintal",
                keyTraits = "High iron content (43.1 ppm), heat tolerant, superior chapati score (7.7/10)."
            ),
            VarietySeedItem(
                varietyName = "Pusa Basmati 1121 / 1718",
                cropName = "Aromatic Basmati Paddy (1121 बासमती)",
                category = "Grains and Wheat",
                seedCompanyOrType = "IARI Certified Grain",
                typicalUnit = "quintal",
                benchmarkPriceRange = "₹4,200 - ₹5,100/quintal",
                keyTraits = "Record kernel elongation ratio (up to 22mm cooked), distinct floral 2-AP aroma."
            ),
            VarietySeedItem(
                varietyName = "Pusa Basmati 1509",
                cropName = "Early Basmati Paddy (1509 बासमती)",
                category = "Grains and Wheat",
                seedCompanyOrType = "IARI Certified Grain",
                typicalUnit = "quintal",
                benchmarkPriceRange = "₹3,600 - ₹4,400/quintal",
                keyTraits = "Short duration 115-day cycle, non-lodging, slender extra-long milled grain."
            ),
            VarietySeedItem(
                varietyName = "BPT 5204 (Samba Mahsuri / Sona Masoori)",
                cropName = "Fine Table Rice (सोना मसूरी धान)",
                category = "Grains and Wheat",
                seedCompanyOrType = "ANGRAU Certified Seed",
                typicalUnit = "quintal",
                benchmarkPriceRange = "₹2,600 - ₹3,100/quintal",
                keyTraits = "Medium slender translucent grain, non-sticky texture, low glycemic index table rice."
            ),
            VarietySeedItem(
                varietyName = "Pioneer P3396 / DKC 9108",
                cropName = "Yellow Corn / Maize (मक्का)",
                category = "Grains and Wheat",
                seedCompanyOrType = "Corteva Pioneer / Bayer Dekalb F1",
                typicalUnit = "quintal",
                benchmarkPriceRange = "₹2,100 - ₹2,450/quintal",
                keyTraits = "High test weight orange-yellow flint kernels, low starch breakage, ideal for feed & starch milling."
            ),
            VarietySeedItem(
                varietyName = "Pioneer 86M84 / Pusa-1201",
                cropName = "Pearl Millet / Bajra (बाजरा)",
                category = "Grains and Wheat",
                seedCompanyOrType = "Pioneer / IARI Certified Hybrid",
                typicalUnit = "quintal",
                benchmarkPriceRange = "₹2,250 - ₹2,700/quintal",
                keyTraits = "Bold grey-green grains, high iron and zinc density, excellent drought tolerance."
            )
        ),
        "Fresh Fruits" to listOf(
            VarietySeedItem(
                varietyName = "Ratnagiri / Devgad Alphonso (Hapus)",
                cropName = "GI Alphonso Mango (हापुस आम)",
                category = "Fresh Fruits",
                seedCompanyOrType = "GI Certified Grafted Clone",
                typicalUnit = "crate",
                benchmarkPriceRange = "₹1,200 - ₹2,200/wooden crate (2-3 doz)",
                keyTraits = "Saffron-gold fiberless pulp, rich creamy texture, thin skin with aromatic monoterpenes."
            ),
            VarietySeedItem(
                varietyName = "Gir Kesar",
                cropName = "GI Kesar Mango (गीर केसर आम)",
                category = "Fresh Fruits",
                seedCompanyOrType = "Junagadh Agricultural Univ Clone",
                typicalUnit = "crate",
                benchmarkPriceRange = "₹800 - ₹1,400/box (10kg)",
                keyTraits = "Intense orange sweet pulp, distinct saffron aroma, high TSS (19-21° Brix)."
            ),
            VarietySeedItem(
                varietyName = "Grand Naine (G-9)",
                cropName = "Cavendish Banana (जी-९ केला)",
                category = "Fresh Fruits",
                seedCompanyOrType = "Jain Tissue Culture Certified",
                typicalUnit = "quintal",
                benchmarkPriceRange = "₹1,300 - ₹2,100/quintal",
                keyTraits = "Heavy cylindrical bunches (30-35kg), uniform finger length >7.5 inches, long green life."
            ),
            VarietySeedItem(
                varietyName = "Bhagwa (Sindhuri)",
                cropName = "GI Bhagwa Pomegranate (भगवा अनार)",
                category = "Fresh Fruits",
                seedCompanyOrType = "NRC on Pomegranate (Solapur)",
                typicalUnit = "kg",
                benchmarkPriceRange = "₹90 - ₹160/kg",
                keyTraits = "Glossy deep saffron-red rind, bold soft-seeded sweet arils (15-16° Brix)."
            ),
            VarietySeedItem(
                varietyName = "Thompson Seedless / Sonaka",
                cropName = "Table Grapes (सोनाका / थॉमसन अंगूर)",
                category = "Fresh Fruits",
                seedCompanyOrType = "NRC for Grapes Certified Rootstock",
                typicalUnit = "kg",
                benchmarkPriceRange = "₹65 - ₹110/kg",
                keyTraits = "Elongated golden green berries, crisp bite, TSS >18° Brix, zero seed residue."
            ),
            VarietySeedItem(
                varietyName = "Taiwan Red Lady 786",
                cropName = "Hybrid Papaya (रेड लेडी पपीता)",
                category = "Fresh Fruits",
                seedCompanyOrType = "Known-You Seed Co.",
                typicalUnit = "kg",
                benchmarkPriceRange = "₹22 - ₹38/kg",
                keyTraits = "Early hermaphrodite fruiting, thick sweet red-orange flesh (13° Brix), PRSV resistant."
            ),
            VarietySeedItem(
                varietyName = "Nagpur Santra (Mandarin)",
                cropName = "Nagpur Orange (नागपुर संतरा)",
                category = "Fresh Fruits",
                seedCompanyOrType = "ICAR-CCRI Certified Budded Stock",
                typicalUnit = "quintal",
                benchmarkPriceRange = "₹3,500 - ₹5,500/quintal",
                keyTraits = "Loose-jacket easy peel, aromatic sweet-tart balanced segments, high juice recovery (>42%)."
            ),
            VarietySeedItem(
                varietyName = "Taiwan Pink / Allahabad Safeda",
                cropName = "Guava (ताइवान पिंक अमरूद)",
                category = "Fresh Fruits",
                seedCompanyOrType = "Air-layered clonal stock",
                typicalUnit = "kg",
                benchmarkPriceRange = "₹35 - ₹60/kg",
                keyTraits = "Crisp pink core, low seediness, glossy exterior, uniform 250-400g fruit size."
            )
        ),
        "Pulses and Dal" to listOf(
            VarietySeedItem(
                varietyName = "JAKI-9218 / JG-11",
                cropName = "Desi Brown Gram (देसी चना)",
                category = "Pulses and Dal",
                seedCompanyOrType = "PDKV / JNKVV Certified Breeder",
                typicalUnit = "quintal",
                benchmarkPriceRange = "₹5,400 - ₹6,100/quintal",
                keyTraits = "Bold yellowish-brown seed, high dal milling recovery (>78%), wilt resistant."
            ),
            VarietySeedItem(
                varietyName = "Dollar Chana (KAK-2 / PKV-4)",
                cropName = "Kabuli Chickpea (डॉलर काबुली चना)",
                category = "Pulses and Dal",
                seedCompanyOrType = "ICRISAT / MPKV Certified",
                typicalUnit = "quintal",
                benchmarkPriceRange = "₹10,500 - ₹13,200/quintal",
                keyTraits = "Extra bold 11-12mm calibre seed, chalky white coat, export benchmark count (40-42)."
            ),
            VarietySeedItem(
                varietyName = "BDN-711 / Maruti (ICP-8863)",
                cropName = "White / Red Pigeon Pea (तुअर / अरहर)",
                category = "Pulses and Dal",
                seedCompanyOrType = "VNMKV Parbhani Certified",
                typicalUnit = "quintal",
                benchmarkPriceRange = "₹8,800 - ₹10,200/quintal",
                keyTraits = "Uniform bold grain, high protein (22%), 80% whole toor split dal yield."
            ),
            VarietySeedItem(
                varietyName = "JS-335 / JS-9560 / JS-2034",
                cropName = "Yellow Soybean (सोयाबीन)",
                category = "Pulses and Dal",
                seedCompanyOrType = "ICAR-IISR Indore Certified",
                typicalUnit = "quintal",
                benchmarkPriceRange = "₹4,100 - ₹4,800/quintal",
                keyTraits = "Uniform yellow seed with light brown hilum, 18-20% oil & 40% crude protein, low pod shattering."
            ),
            VarietySeedItem(
                varietyName = "Samrat (PDM-139) / Virat (IPM 205-7)",
                cropName = "Green Gram / Moong (मूंग दाल)",
                category = "Pulses and Dal",
                seedCompanyOrType = "IIPR Kanpur Certified",
                typicalUnit = "quintal",
                benchmarkPriceRange = "₹7,600 - ₹8,900/quintal",
                keyTraits = "Extra-early 55-60 days maturity, bright shiny green seed coat, synchronous pod ripening."
            ),
            VarietySeedItem(
                varietyName = "TAU-1 / Shekhar-2 (IPU-2-43)",
                cropName = "Black Gram / Urad (उड़द दाल)",
                category = "Pulses and Dal",
                seedCompanyOrType = "PDKV Akola / IIPR",
                typicalUnit = "quintal",
                benchmarkPriceRange = "₹7,200 - ₹8,400/quintal",
                keyTraits = "Bold dull black grain, high glutinous papad/batter ferment strength, MYMV resistant."
            ),
            VarietySeedItem(
                varietyName = "Pusa Shivalik / DPL-62",
                cropName = "Small Red Lentil / Masoor (मसूर दाल)",
                category = "Pulses and Dal",
                seedCompanyOrType = "IARI / IIPR Certified",
                typicalUnit = "quintal",
                benchmarkPriceRange = "₹6,000 - ₹6,700/quintal",
                keyTraits = "Uniform reddish-orange cotyledons, quick cooking time, high zinc & iron."
            )
        ),
        "Farm Spices" to listOf(
            VarietySeedItem(
                varietyName = "Guntur S4 (Sanam) / Teja (S17)",
                cropName = "Dry Red Chilli (गुंटूर तेजा लाल मिर्च)",
                category = "Farm Spices",
                seedCompanyOrType = "AP Seed Corp / Spice Board Clonal",
                typicalUnit = "quintal",
                benchmarkPriceRange = "₹18,500 - ₹23,000/quintal",
                keyTraits = "High capsaicin heat (>65,000 SHU), deep crimson ASTA color 70-85, thick stemless pod."
            ),
            VarietySeedItem(
                varietyName = "Byadagi Dabbi / Kaddi",
                cropName = "GI Byadagi Chilli (ब्याडगी मिर्च)",
                category = "Farm Spices",
                seedCompanyOrType = "Kadur Clonal Line Certified",
                typicalUnit = "quintal",
                benchmarkPriceRange = "₹32,000 - ₹44,000/quintal",
                keyTraits = "Deep wrinkled skin, dark ruby natural oleoresin color (ASTA 140-160+), mild savory heat (<15k SHU)."
            ),
            VarietySeedItem(
                varietyName = "Salem / Waigaon / Pragati",
                cropName = "GI Turmeric Finger (सालेम / वैगांव हल्दी)",
                category = "Farm Spices",
                seedCompanyOrType = "IISR Calicut Certified Mother Rhizome",
                typicalUnit = "quintal",
                benchmarkPriceRange = "₹12,500 - ₹16,500/quintal",
                keyTraits = "High active curcumin (>5.5% to 6.2%), golden-orange core, hard polished fingers."
            ),
            VarietySeedItem(
                varietyName = "Gujarat Cumin 4 (GC-4) / RZ-209",
                cropName = "Cumin Seed / Jeera (जीरा)",
                category = "Farm Spices",
                seedCompanyOrType = "SDAU Jagudan / ICAR-NRCSS Certified",
                typicalUnit = "quintal",
                benchmarkPriceRange = "₹24,000 - ₹31,000/quintal",
                keyTraits = "Bold ridged greenish-grey seeds, essential volatile cumin oil >3.8%, resistant to wilt & blight."
            ),
            VarietySeedItem(
                varietyName = "Gujarat Coriander 2 (GC-2) / Kumbhraj",
                cropName = "Coriander Seed / Dhaniya (धनिया)",
                category = "Farm Spices",
                seedCompanyOrType = "SDAU / NRCSS Ajmer",
                typicalUnit = "quintal",
                benchmarkPriceRange = "₹7,200 - ₹9,500/quintal",
                keyTraits = "Globe shaped greenish-yellow seed, pleasant linalool fragrance (oil >0.45%), non-splitting husk."
            ),
            VarietySeedItem(
                varietyName = "Yamuna Safed (G-1) / Bhima Purple",
                cropName = "Garlic Bulb (लहसुन)",
                category = "Farm Spices",
                seedCompanyOrType = "NHRDF / DOGR Certified Cloves",
                typicalUnit = "quintal",
                benchmarkPriceRange = "₹9,500 - ₹14,000/quintal",
                keyTraits = "Compact 15-20 clove bulb, purplish-white outer skin, concentrated allicin with sharp aroma."
            ),
            VarietySeedItem(
                varietyName = "Gujarat Fennel 11 (GF-11) / Abu Green",
                cropName = "Fennel Seed / Saunf (सौंफ)",
                category = "Farm Spices",
                seedCompanyOrType = "SDAU Jagudan",
                typicalUnit = "quintal",
                benchmarkPriceRange = "₹11,000 - ₹15,500/quintal",
                keyTraits = "Bright parrots-green bold grains, naturally high anethole sweetness, chewable soft seed."
            ),
            VarietySeedItem(
                varietyName = "Giriraj (DRMRIJ-31) / Kranti",
                cropName = "Mustard Seed / Rai (सरसों)",
                category = "Farm Spices",
                seedCompanyOrType = "DRMR Bharatpur Certified",
                typicalUnit = "quintal",
                benchmarkPriceRange = "₹5,300 - ₹6,000/quintal",
                keyTraits = "Bold dark reddish-brown seed, high oil yield (>41.5%), pungency with allylisothiocyanate."
            )
        )
    )
}
